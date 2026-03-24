package frc.robot.components.drive.infrastructure.SimulationModule;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;

public class SimulationConst {
    public static final class RobotSize{
        /** ロボットの横の大きさ | [m] */
        public static final double weitht = 0.8;
        /** ロボットの縦の大きさ | [m] */
        public static final double length = 0.8;
        /** バンパーの厚さ | [m] */
        public static final double bumperHeight = 0.2;
    }
    public static final class IntakeSize{
        /** intakeできる範囲の縦の長さ | [m] */
        public static final double length = 0.20;
        /** intakeできる範囲の横の長さ | [m] */
        public static final double weitht = 0.60;
        public static final double xMin = RobotSize.length;
        public static final double xMax = xMin + length;
        public static final double yMin = -weitht/2;
        public static final double yMax = weitht/2;
    }
    public static final class Shooter{
        /** shooterの高さ | [m] */
        //public static final Distance height = Units.Meters.of(0.504);
        public static final double height = 0.504;
        /** Fuelを打ち出す上下方向の角度 */
        public static final Angle hoodAngle = Units.Degrees.of(48);
        /** Fuelを打ち出す左右方向の角度 */
        public static final Angle turretYaw = Units.Degrees.of(0);
    }
}
