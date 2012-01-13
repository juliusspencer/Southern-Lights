package org.gtugs.auckland.usb.led;

import org.gtugs.auckland.usb.UsbAccessoryController;



public class LedControllerImpl implements LedControllerInterface {

	private final int ledId;
	private final UsbAccessoryController controller;

	public LedControllerImpl(final UsbAccessoryController controller, int ledId) {
		this.controller = controller;
		this.ledId = ledId;
		
	}
	
	public void setIntensityAndColour(byte value, int colour) {
		
		controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId), value);

	}
	
	public static void setLed(LedControllerGroup leds, boolean isChecked, int ledId, int intensity) {
//		leds.setIntensityAndColour((byte) intensity, ledId);
	}
}
