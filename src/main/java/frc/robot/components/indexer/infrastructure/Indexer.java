package frc.robot.components.indexer.infrastructure;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.components.indexer.IndexerConst;
import frc.robot.components.indexer.IndexerParameter;
import frc.robot.domain.repository.IndexerRepository;
import frc.robot.domain.state.IndexerState;

public class Indexer implements IndexerRepository {
    private final SparkMax LongRollerIndexer;
    private final SparkMaxConfig LongRollerIndexerConfig;
    private final RelativeEncoder LongRollerIndexerEncoder;

    private final SparkMax StarWheelIndexer;
    private final SparkMaxConfig StarWheelIndexerConfig;
    private final RelativeEncoder StarWheelIndexerEncoder;

    public Indexer() {
        LongRollerIndexer = new SparkMax(IndexerConst.Ports.LongRollerIndexer, SparkLowLevel.MotorType.kBrushless);
        LongRollerIndexerConfig = new SparkMaxConfig();
        LongRollerIndexerEncoder = LongRollerIndexer.getEncoder();

        LongRollerIndexerConfig.inverted(true);
        LongRollerIndexerConfig.idleMode(SparkBaseConfig.IdleMode.kCoast);
        LongRollerIndexerConfig.smartCurrentLimit(IndexerParameter.LongRollerIndexer.CurrentLimit);


        StarWheelIndexer = new SparkMax(IndexerConst.Ports.StarWheelIndexer, SparkLowLevel.MotorType.kBrushless);
        StarWheelIndexerConfig = new SparkMaxConfig();
        StarWheelIndexerEncoder = StarWheelIndexer.getEncoder();

        StarWheelIndexerConfig.inverted(false);
        StarWheelIndexerConfig.idleMode(SparkBaseConfig.IdleMode.kCoast);
        StarWheelIndexerConfig.smartCurrentLimit(IndexerParameter.StarWheelIndexer.CurrentLimit);

        StarWheelIndexer.configure(StarWheelIndexerConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        LongRollerIndexer.configure(LongRollerIndexerConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    /**
     * Indexerを任意のパワーで動かす(PercentOutput)
     * @param LongRollerPower LongRollerIndexerを動かすパワー | shooterに送る方向が正 | [-1~1]((出力のパワーの割合)
     * @param StarWheelPower StarWheelIndexerを動かすパワー | shooterに送る方向が正 | [-1~1]((出力のパワーの割合)
     */
    @Override
    public void moveIndexerSpecifiedPower(double LongRollerPower, double StarWheelPower){
        LongRollerIndexer.set(LongRollerPower);
        StarWheelIndexer.set(StarWheelPower);
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
        IndexerState.LongRollerIndexer.motorSpeed = LongRollerIndexerEncoder.getVelocity() / IndexerConst.LongRollerIndexerMaxRPM;
        IndexerState.LongRollerIndexer.isMotorActive = Math.abs(LongRollerIndexerEncoder.getVelocity()) > 0.1;
        IndexerState.LongRollerIndexer.outputCurrent = LongRollerIndexer.getOutputCurrent();
        IndexerState.LongRollerIndexer.appliedOutput = LongRollerIndexer.getAppliedOutput();
        IndexerState.LongRollerIndexer.busVoltage = LongRollerIndexer.getBusVoltage();

        IndexerState.StarWheelIndexer.motorSpeed = StarWheelIndexerEncoder.getVelocity() / IndexerConst.StarWheelIndexerMaxRPM;
        IndexerState.StarWheelIndexer.isMotorActive = Math.abs(StarWheelIndexerEncoder.getVelocity()) > 0.1;
        IndexerState.StarWheelIndexer.outputCurrent = StarWheelIndexer.getOutputCurrent();
        IndexerState.StarWheelIndexer.appliedOutput = StarWheelIndexer.getAppliedOutput();
        IndexerState.StarWheelIndexer.busVoltage = StarWheelIndexer.getBusVoltage();
    }

}
