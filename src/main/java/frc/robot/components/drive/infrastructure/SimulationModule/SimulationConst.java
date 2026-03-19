package frc.robot.components.drive.infrastructure.SimulationModule;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;

public class SimulationConst {
    public static final class RobotSize{
        public static final double weitht = 0.8;
        public static final double length = 0.8;
        public static final double bumperHeight = 0.2;
    }
    public static final class IntakeSize{
        public static final double length = 0.20;
        public static final double weitht = 0.60;
        public static final double xMin = RobotSize.length;
        public static final double xMax = xMin + length;
        public static final double yMin = -weitht/2;
        public static final double yMax = weitht/2;
    }
    public static final class Shooter{
        public static final Distance height = edu.wpi.first.units.Units.Meters.of(0.45);
        /**上下方向 */
        public static final Angle hoodAngle = edu.wpi.first.units.Units.Degrees.of(50);
        /**左右方向 */
        public static final Angle turretYaw = edu.wpi.first.units.Units.Degrees.of(0);
    }
}
