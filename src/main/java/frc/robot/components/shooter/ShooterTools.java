package frc.robot.components.shooter;

public class ShooterTools {

    /** m/sからRPMへ変換　*/
    public static double MpsToRPM(double supplierMps) {
        return ShooterConst.maxMps / supplierMps * ShooterConst.maxRPM;
    }
}
