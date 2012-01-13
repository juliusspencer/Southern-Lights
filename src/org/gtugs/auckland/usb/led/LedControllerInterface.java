package org.gtugs.auckland.usb.led;


public interface LedControllerInterface {
	
	public static final byte LED_SERVO_COMMAND = 2;
	
	public void setIntensityAndColour(byte value, int colour);
   
}