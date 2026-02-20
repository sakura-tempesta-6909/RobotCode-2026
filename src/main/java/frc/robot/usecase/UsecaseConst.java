package frc.robot.usecase;

import com.pathplanner.lib.path.PathConstraints;

/**
 * Usecaseで使うような定数
 */
public class UsecaseConst {
    public static final class PathPlannerConst {
        public static final PathConstraints Unlimited = PathConstraints.unlimitedConstraints(12);
    }

    /**
     * ロボットの構造に関する定数
     */
    public static final class RobotStructure {
        public static final double DistanceToArm = -10;
    }

    public static final class SpeedAndPower {
        public static final double MaxPowerToIntakePosition = 1;
        public static final double MaxPowerToInitialPosition = -1;
    }

    
}
