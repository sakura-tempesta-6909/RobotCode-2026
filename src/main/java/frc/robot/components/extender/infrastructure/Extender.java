package frc.robot.components.extender.infrastructure;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.components.extender.ExtenderConst;
import frc.robot.components.extender.ExtenderParameter;
import frc.robot.components.extender.ExtenderTools;
import frc.robot.domain.repository.ExtenderRepository;
import frc.robot.domain.state.ExtenderState;

public class Extender implements ExtenderRepository {
    private final SparkMax extenderMotor;
    private final RelativeEncoder extenderEncoder;

    private final SparkClosedLoopController extenderPID;

    public Extender() {
        extenderMotor = new SparkMax(ExtenderConst.Ports.extenderMotor, SparkMax.MotorType.kBrushless);

        SparkMaxConfig extenderMotorConfig = new SparkMaxConfig();
        /** エンコーダーとpidControllerを読み込む */
        extenderEncoder = extenderMotor.getEncoder();
        extenderPID = extenderMotor.getClosedLoopController();
        /** 回転方向を指定 */
        extenderMotorConfig.inverted(true);
        /** Brakeモードに設定 */
        extenderMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        extenderMotorConfig.limitSwitch.reverseLimitSwitchType(Type.kNormallyOpen).forwardLimitSwitchType(Type.kNormallyOpen);
 

        /** PIDの設定 */
        /** FFを適用したPIDの設定 */
        extenderMotorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ExtenderParameter.PID.PositionP,ExtenderParameter.PID.PositionI,ExtenderParameter.PID.PositionD, ExtenderConst.Slot.ExtenderRaisingSlot)
                .iZone(ExtenderParameter.PID.PositionIZone, ExtenderConst.Slot.ExtenderRaisingSlot)
                .maxOutput(ExtenderParameter.PID.MaxOutput, ExtenderConst.Slot.ExtenderRaisingSlot)
                .minOutput(ExtenderParameter.PID.MinOutput,ExtenderConst.Slot.ExtenderRaisingSlot)
                .feedForward.kCos(ExtenderParameter.FFPower)
                            .kCosRatio(ExtenderParameter.kCosRatio);
        extenderMotorConfig.softLimit
                .forwardSoftLimit(ExtenderTools.getRotationsOfMotorShaft(ExtenderParameter.InitialAngle))
                .forwardSoftLimitEnabled(true)
                .reverseSoftLimit(ExtenderTools.getRotationsOfMotorShaft(ExtenderParameter.IntakeAngle))
                .reverseSoftLimitEnabled(true);
        extenderEncoder.setPosition(ExtenderTools.getRotationsOfMotorShaft(ExtenderParameter.InitialAngle));
        
        /** 設定の適用 */
        extenderMotor.configure(extenderMotorConfig, SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);

        /** VelosityのPID */
        extenderMotorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ExtenderParameter.PID.EndexerVelocityP,ExtenderParameter.PID.EndexerVelocityI,ExtenderParameter.PID.EndexerVelocityD,ExtenderConst.Slot.ExtenderVelocitySlot).feedForward.kCos(ExtenderParameter.FFPower).kCosRatio(ExtenderConst.GearRatio);  
        
        

    }


    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        /** Extenderのモーターが動作しているか|動いている->true,停止->false*/
        ExtenderState.isMotorActive = Math.abs(extenderEncoder.getVelocity()) > ExtenderConst.ExtenderMotorMinRotation;
        /** 底面が地面と平行な場合を0度としたExtenderの角度[degree]|0<=currentAngle<=90|ロボット側に回転するのが正方向*/
        ExtenderState.currentAngle = ExtenderTools.getAngleOfExtender(extenderEncoder.getPosition());
        /** intakeできる位置にExtenderがあるかないか|可能->true,不可->false */
        ExtenderState.lowerLimit = extenderMotor.getForwardLimitSwitch().isPressed();
        ExtenderState.isIntakePosition = (ExtenderParameter.IntakeAngle - ExtenderParameter.arrowedAngleToJudgeIsInitialAngle < ExtenderState.currentAngle)&&(ExtenderState.currentAngle < ExtenderParameter.InitialAngle + ExtenderParameter.arrowedAngleToJudgeIsInitialAngle);
        /** extenderが初期位置(地面に対して鉛直方向)にあるかどうか|ある->true,ない->false */
        ExtenderState.upperLimit = extenderMotor.getReverseLimitSwitch().isPressed();
        ExtenderState.isInitialPosition = (ExtenderParameter.InitialAngle - ExtenderParameter.arrowedAngleToJudgeIsInitialAngle < ExtenderState.currentAngle)&&(ExtenderState.currentAngle < ExtenderParameter.InitialAngle + ExtenderParameter.arrowedAngleToJudgeIsInitialAngle);
        SmartDashboard.putNumber("current angle", ExtenderState.currentAngle);
        SmartDashboard.putNumber("current position", extenderEncoder.getPosition());
        ExtenderState.appliedOutput = extenderMotor.getAppliedOutput();
        ExtenderState.outputCurrent = extenderMotor.getOutputCurrent();
        ExtenderState.busVoltage = extenderMotor.getBusVoltage();
    }

    /** Extenderを任意の角度に動かす(Position) |targetAngle:Extenderが地面に対して並行な時を0とした目標の角度[degree]|地面に対して上に動かす方向を正 */
    @Override
    public void moveExtenderSpecifiedAngle(double targetAngle) {
        /** 指定の距離移動するために必要な回転数を求める | rotation *
         * 360は1回転の角度*/
        double targetPosition = ExtenderTools.getRotationsOfMotorShaft(targetAngle);
        extenderPID.setSetpoint(targetPosition, SparkBase.ControlType.kPosition, ExtenderConst.Slot.ExtenderRaisingSlot);
    }

    /** Extenderを任意の力で動かす(PercentOutput) */
    @Override
    public void moveExtenderSpecifiedPower(double targetPower) {
        extenderMotor.set(targetPower);

    }

    /** PIDをリセットする */
    @Override
    public void resetPID() {
       extenderPID.setIAccum(0);
    }

    /** encoderをリセットする 
     * @param resetPosition Encorderをリセットする際の角度[degree]
     * 初期位置を90°とする
    */
    @Override
    public void resetEncoder(double resetPosition) {
       extenderEncoder.setPosition(ExtenderTools.getRotationsOfMotorShaft(resetPosition));
    }

    /** 現在の角度を維持する */
    @Override
    public void keepCurrentAngle(){
        extenderPID.setSetpoint(0, SparkBase.ControlType.kVelocity,ExtenderConst.Slot.ExtenderVelocitySlot);
            

    }
    
}
