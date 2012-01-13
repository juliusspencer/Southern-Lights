package org.gtugs.auckland.usb.led;

import org.gtugs.auckland.usb.UsbAccessoryController;



public class LedControllerImpl implements LedControllerInterface {

	public static final int LED_1 = 0; 
	public static final int LED_2 = 1; 
	public static final int LED_3 = 2; 
	public static final int LED_4 = 3; 
	public static final int LED_5 = 4; 
	public static final int LED_6 = 5; 
	public static final int LED_7 = 6; 
	public static final int LED_8 = 7; 
	public static final int LED_9 = 8; 
	
	private final int ledId;
	private final UsbAccessoryController controller;

	public LedControllerImpl(final UsbAccessoryController controller, int ledId) {
		this.controller = controller;
		this.ledId = ledId;
		
	}
	
	public void setIntensityAndColour(byte value, int colour) {
		
		controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId), value);

//		switch (colour) {
//		case LED_1:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_2:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_3:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_4:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_5:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_6:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_7:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_8:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		case LED_9:
//			controller.sendCommand(LED_SERVO_COMMAND, (byte) (ledId+1), value);
//			
//			break;
//
//		default:
//			break;
//		}
	}
	
	public static void setLed(LedControllerGroup leds, boolean isChecked, int ledId, int intensity) {
//		leds.setIntensityAndColour((byte) intensity, ledId);
	}
}
