package frc.robot.components.extender.infrastructure;

import frc.robot.domain.repository.ExtenderRepository;
import frc.robot.domain.state.ExtenderState;
import frc.robot.domain.repository.ExtenderRepository;
import frc.robot.components.extender.ExtenderConst;
import frc.robot.components.extender.ExtenderTools;
import frc.robot.components.extender.ExtenderParameter;

import static edu.wpi.first.units.Units.Percent;

import java.lang.annotation.Target;

import com.ctre.phoenix6.signals.ControlModeValue;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.*;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Extender implements ExtenderRepository {
    private final SparkMax extenderMotor;
    private final DigitalInput upperExtenderLimitSwitch;
    private final DigitalInput lowerExtenderLimitSwitch;
    private final RelativeEncoder extenderEncoder;

    private final SparkClosedLoopController extenderPID;

    public Extender() {
        extenderMotor = new SparkMax(ExtenderConst.Ports.extenderMotor, SparkMax.MotorType.kBrushless);
        upperExtenderLimitSwitch = new DigitalInput(ExtenderConst.Ports.upperExtenderLimitSwitch);
        lowerExtenderLimitSwitch = new DigitalInput(ExtenderConst.Ports.lowerExtenderLimitSwitch);

        SparkMaxConfig extenderMotorConfig = new SparkMaxConfig();
        /** エンコーダーとpidControllerを読み込む */
        extenderEncoder = extenderMotor.getEncoder();
        extenderPID = extenderMotor.getClosedLoopController();
        /** 回転方向を指定 */
        extenderMotorConfig.inverted(false);
        /** Brakeモードに設定 */
        extenderMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);

        /** PIDの設定 */
        /** FFを適用したPIDの設定(上げる際) */
        extenderMotorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ExtenderParameter.PID.RaisingP,ExtenderParameter.PID.RaisingI,ExtenderParameter.PID.RaisingD, ExtenderConst.Slot.ExtenderRaisingSlot);
        extenderMotorConfig.closedLoop.iZone(ExtenderParameter.PID.RaisingIZone, ExtenderConst.Slot.ExtenderRaisingSlot);

        /** FFを適用したPIDの設定(下げる際) */
        extenderMotorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ExtenderParameter.PID.LoweringP,ExtenderParameter.PID.LoweringI,ExtenderParameter.PID.LoweringD, ExtenderConst.Slot.ExtenderLoweringSlot);
        extenderMotorConfig.closedLoop.iZone(ExtenderParameter.PID.LoweringIZone, ExtenderConst.Slot.ExtenderRaisingSlot);
        /** 設定の適用 */
        extenderMotor.configure(extenderMotorConfig, SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kPersistParameters);
    }


    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        /** Extenderのモーターが動作しているか|動いている->true,停止->false*/
        ExtenderState.isMotorActive = Math.abs(extenderEncoder.getVelocity()) > 0.1;
        /** 底面が地面と平行な場合を0度としたExtenderの角度[degree]|0<=currentAngle<=90|ロボット側に回転するのが正方向*/
        ExtenderState.currentAngle = extenderEncoder.getPosition() + ExtenderParameter.InitialAngle;
        /** intakeできる位置にExtenderがあるかないか|可能->true,不可->false */
        ExtenderState.isIntakeAngle = lowerExtenderLimitSwitch.get();
        /** extenderが初期位置(地面に対して鉛直方向)にあるかどうか|ある->true,ない->false */
        ExtenderState.isInitialAngle = upperExtenderLimitSwitch.get();
    }

    @Override
    public void moveExtenderSpecifiedAngle(double targetAngle) {
        /** 指定の距離移動するために必要な回転数を求める | rotation */
        double targetPosition = ExtenderTools.getRotationsForDistance(ExtenderTools.getDistanceToTarget(targetAngle));
        if (targetAngle > ExtenderState.currentAngle) {
            /** 上昇する場合 */
            extenderPID.setReference(targetPosition, SparkBase.ControlType.kPosition, ExtenderConst.Slot.ExtenderRaisingSlot, ExtenderParameter.FFPower);
            
                
        } else {
            /** 下降する場合 */  
            
            extenderPID.setReference(targetPosition, SparkBase.ControlType.kPosition, ExtenderConst.Slot.ExtenderLoweringSlot, ExtenderParameter.FFPower);
        }
    }

    @Override
    
    public void moveIndexerSpecifiedPower(double targetPower) {
        extenderMotor.set(targetPower);

    }

    @Override
    public void resetPID() {
       extenderPID.setIAccum(0);
    }

    @Override
    public void resetEncorder() {
       extenderEncoder.setPosition(0);
    }
    
}
