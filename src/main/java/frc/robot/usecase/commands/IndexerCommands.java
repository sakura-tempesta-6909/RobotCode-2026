package frc.robot.usecase.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.components.indexer.IndexerParameter;
import frc.robot.domain.repository.IndexerRepository;

public class IndexerCommands {
    private static IndexerRepository IndexerRepository;

    public static void init(IndexerRepository in) {
        IndexerRepository = in;
    }

    public static Command feedToShooter(){
        return IndexerRepository.runEnd(() -> {
            IndexerRepository.moveIndexerSpecifiedPower(IndexerParameter.LongRollerIndexer.FeedSpeed, IndexerParameter.StarWheelIndexer.FeedSpeed);
        },() ->
            IndexerRepository.moveIndexerSpecifiedPower(IndexerParameter.LongRollerIndexer.Neutral, IndexerParameter.StarWheelIndexer.Neutral));
    }

    public static Command reverseIndexer(){
        return IndexerRepository.runEnd(() -> {
            IndexerRepository.moveIndexerSpecifiedPower(IndexerParameter.LongRollerIndexer.ReverseSpeed, IndexerParameter.StarWheelIndexer.ReverseSpeed);
        },() ->
                IndexerRepository.moveIndexerSpecifiedPower(IndexerParameter.LongRollerIndexer.Neutral, IndexerParameter.StarWheelIndexer.Neutral));
    }

    public static Command stopIndexer(){
        return IndexerRepository.run(() -> {
            IndexerRepository.moveIndexerSpecifiedPower(IndexerParameter.LongRollerIndexer.Neutral, IndexerParameter.StarWheelIndexer.Neutral);
        });
    }
}
