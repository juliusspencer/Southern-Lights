#include <Wire.h>

#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

#define  CHANNEL_0      2
#define  CHANNEL_1      3
#define  CHANNEL_2      4

#define  CHANNEL_3      5
#define  CHANNEL_4      6
#define  CHANNEL_5      7

#define  CHANNEL_6      8
#define  CHANNEL_7      9
#define  CHANNEL_8     10

AndroidAccessory acc("Auckland GTUG",
		     "Southern Lights",
		     "Merry Christmas!",
		     "0.1",
		     "http://gplus.to/AucklandGTUG",
		     "0000000020111225");

void setup();
void loop();

void init_channels()
{
	digitalWrite(CHANNEL_0, 0);
	digitalWrite(CHANNEL_1, 0);
	digitalWrite(CHANNEL_2, 0);

	pinMode(CHANNEL_0, OUTPUT);
	pinMode(CHANNEL_1, OUTPUT);
	pinMode(CHANNEL_2, OUTPUT);

	digitalWrite(CHANNEL_3, 0);
	digitalWrite(CHANNEL_4, 0);
	digitalWrite(CHANNEL_5, 0);

	pinMode(CHANNEL_3, OUTPUT);
	pinMode(CHANNEL_4, OUTPUT);
	pinMode(CHANNEL_5, OUTPUT);

	digitalWrite(CHANNEL_6, 0);
	digitalWrite(CHANNEL_7, 0);
	digitalWrite(CHANNEL_8, 0);

	pinMode(CHANNEL_6, OUTPUT);
	pinMode(CHANNEL_7, OUTPUT);
	pinMode(CHANNEL_8, OUTPUT);
}

byte b1, b2, b3, b4, c;
void setup()
{
	Serial.begin(115200);
	Serial.print("\r\nStart");

	init_channels();

	c = 0;

	acc.powerOn();
}

void loop()
{
	byte err;
	byte idle;
	static byte count = 0;
	byte msg[3];

	if (acc.isConnected()) {
		int len = acc.read(msg, sizeof(msg), 1);
		int i;
		byte b;
		uint16_t val;
		int x, y;
		char c0;

		if (len > 0) {
			// assumes only one command per packet
			if (msg[0] == 0x2) {
				if (msg[1] == 0x0)
					analogWrite(CHANNEL_0, msg[2]);
				else if (msg[1] == 0x1)
					analogWrite(CHANNEL_1, msg[2]);
				else if (msg[1] == 0x2)
					analogWrite(CHANNEL_2, msg[2]);
				else if (msg[1] == 0x3)
					analogWrite(CHANNEL_3, msg[2]);
				else if (msg[1] == 0x4)
					analogWrite(CHANNEL_4, msg[2]);
				else if (msg[1] == 0x5)
					analogWrite(CHANNEL_5, msg[2]);
				else if (msg[1] == 0x6)
					analogWrite(CHANNEL_6, msg[2]);
				else if (msg[1] == 0x7)
					analogWrite(CHANNEL_7, msg[2]);
				else if (msg[1] == 0x8)
					analogWrite(CHANNEL_8, msg[2]);
			}
		}
	} else {
		// reset outputs to default values on disconnect
		analogWrite(CHANNEL_0, 0);
		analogWrite(CHANNEL_1, 0);
		analogWrite(CHANNEL_2, 0);
		analogWrite(CHANNEL_3, 0);
		analogWrite(CHANNEL_4, 0);
		analogWrite(CHANNEL_5, 0);
		analogWrite(CHANNEL_6, 0);
		analogWrite(CHANNEL_7, 0);
		analogWrite(CHANNEL_8, 0);
	}

	//delay(10);
}
