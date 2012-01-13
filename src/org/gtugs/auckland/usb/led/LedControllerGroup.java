package org.gtugs.auckland.usb.led;

import org.gtugs.auckland.usb.UsbAccessoryController;

public class LedControllerGroup {
	
	private static final int LED_COUNT = 9;
	LedControllerInterface[] ledControls = new LedControllerInterface[LED_COUNT];
	

	public LedControllerGroup(final UsbAccessoryController controller) {
		for(int x = 0; x < LED_COUNT; x++) {
			//led index is zero based
			ledControls[x] = new LedControllerImpl(controller, x);
		}
	}
	
	
	public void setIntensityAndColour(byte intensityValue, int ledId) {
		this.ledControls[ledId].setIntensityAndColour(intensityValue, ledId);
	}

}
