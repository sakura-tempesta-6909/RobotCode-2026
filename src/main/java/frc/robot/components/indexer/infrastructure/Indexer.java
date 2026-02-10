package frc.robot.components.indexer.infrastructure;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.components.indexer.IndexerConst;
import frc.robot.domain.repository.IndexerRepository;
import frc.robot.domain.state.IndexerState;

public class Indexer implements IndexerRepository {
    private final SparkMax IndexerMotor;
    private final SparkMaxConfig IndexerMotorConfig;
    private final RelativeEncoder IndexerEncoder;

    public Indexer() {
        IndexerMotor = new SparkMax(IndexerConst.Ports.IndexerMotor, SparkLowLevel.MotorType.kBrushless);
        IndexerMotorConfig = new SparkMaxConfig();
        IndexerEncoder = IndexerMotor.getEncoder();

        IndexerMotorConfig.inverted(false);
        IndexerMotorConfig.idleMode(SparkBaseConfig.IdleMode.kCoast);

        IndexerMotor.configure(IndexerMotorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    /**
     * Indexerを任意の速度で動かす(PercentOutput)
     * @param targetSpeed Indexerを動かす速さ | shooterに送る方向が正 | [-1~1](最大速度を基準とした割合です)
     */
    @Override
    public void moveIndexerSpecifiedSpeed(double targetSpeed){
        IndexerMotor.set(targetSpeed);
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        IndexerState.motorSpeed = IndexerEncoder.getVelocity() / IndexerConst.maxRPM;
        IndexerState.isMotorActive = Math.abs(IndexerEncoder.getVelocity()) > 0.1;
    }
    
}
