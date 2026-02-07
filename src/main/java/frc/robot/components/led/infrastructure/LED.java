package frc.robot.components.led.infrastructure;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.components.led.LEDConst;
import frc.robot.domain.repository.LEDRepository;

public class LED implements LEDRepository {
    private final AddressableLED led;
    private final AddressableLEDBuffer ledBuffer;
    public LED() {
        led =  new AddressableLED(LEDConst.Ports.LED);
        ledBuffer = new AddressableLEDBuffer(LEDConst.LEDLength);
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
    }

    @Override
    public void changeLight(int red, int green, int blue) {
    }

    @Override
    public void flashLight(int red, int green, int blue) {
    }

    
}
