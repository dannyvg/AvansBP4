#include <SoftwareSerial.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

SoftwareSerial BTserial(0, 1);  // RX | TX
LiquidCrystal_I2C lcd(0x27, 16, 2);

int valueToSend = 50;

char data = '\0';

void setup() {
  Serial.begin(9600);
  BTserial.begin(9600);  // Set Bluetooth serial baud rate

  lcd.init();
  lcd.backlight();
  lcd.setCursor(0, 0);
  lcd.print("Welkom!");
  delay(5000);
  lcd.clear();
}

void loop() {
  if (BTserial.available()) {
    
    data = BTserial.read();

    if (data == 'x') {
      byte highByte = (valueToSend >> 8) & 0xFF;
      byte lowByte = valueToSend & 0xFF;

      Serial.write(highByte);
      Serial.write(lowByte);
      lcd.clear();
    } else {
      lcd.print(data);
      // lcd.setCursor(0, 1);
      // lcd.print("Stappen:" + valueToSend);
    }
  } else {
    lcd.setCursor(0, 1);
    lcd.print("Stappen:");
    lcd.setCursor(0, 0);
  }
}
