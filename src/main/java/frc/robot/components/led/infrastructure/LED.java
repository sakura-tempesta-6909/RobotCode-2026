package frc.robot.components.led.infrastructure;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.components.led.LEDConst;
import frc.robot.components.led.LEDParameter;
import frc.robot.domain.repository.LEDRepository;

public class LED implements LEDRepository {
    private final AddressableLED led;
    private final AddressableLED led2;
    private final AddressableLEDBuffer ledBuffer;
    private final Timer timer;
    public LED() {
        led =  new AddressableLED(LEDConst.Ports.LED);
        led2 = new AddressableLED(LEDConst.Ports.LED2);
        ledBuffer = new AddressableLEDBuffer(LEDConst.LEDLength);
        led.setLength(ledBuffer.getLength());
        led.setData(ledBuffer);
        led.start();
        led2.setLength(ledBuffer.getLength());
        led2.setData(ledBuffer);
        led2.start();
        timer = new Timer();
        timer.start();
    }

    /**
     * Stateへの書き込みを行う
     */
    @Override
    public void periodic() {
    }

    @Override
    public void changeLight(int red, int green, int blue) {
        for (var i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setRGB(i, red, green, blue);
        }
        led.setData(ledBuffer);
        led2.setData(ledBuffer);
    }

    @Override
    public void flashLight(int red, int green, int blue) {
         for (var i = 0; i < ledBuffer.getLength(); i++) {
            if (timer.get() < 0.1) {
                ledBuffer.setRGB(i, red, green, blue);
            } else if (timer.get() < 0.2) {
                ledBuffer.setRGB(i, 0, 0, 0);
            } else {
                led.start();
                timer.restart();
            }
        }
        led.setData(ledBuffer);
        led2.setData(ledBuffer);
    }

    
}
