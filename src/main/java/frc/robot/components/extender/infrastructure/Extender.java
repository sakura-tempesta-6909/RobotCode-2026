package frc.robot.components.extender.infrastructure;

import frc.robot.domain.repository.ExtenderRepository;

public class Extender implements ExtenderRepository {

    public Extender() {
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
    }

    @Override
    public void moveExtenderSpecifiedAngle(double targetAngle) {

    }

    @Override
    public void moveIndexerSpecifiedSpeed(double targetSpeed) {

    }

    @Override
    public void resetPID() {

    }

    @Override
    public void resetEncorder() {
        
    }
    
}
