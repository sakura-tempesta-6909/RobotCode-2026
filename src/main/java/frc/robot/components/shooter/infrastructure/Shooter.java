package frc.robot.components.shooter.infrastructure;

import frc.robot.domain.repository.ShooterRepository;

public class Shooter implements ShooterRepository {

    public Shooter() {
    }

    @Override
    public void moveShooterSpecifedPower(double targetPower){
    }

    @Override
    public void moveShooterSpecifiedSpeed(double targetSpeed){
    }

    @Override
    public void resetPID(){
    }

    @Override
    public void feed(){
    }

    @Override
    public void reverseShooter(){
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
    }
    
}
