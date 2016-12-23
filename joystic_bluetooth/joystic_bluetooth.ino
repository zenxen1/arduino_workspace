#include <SoftwareSerial.h>
#include <string.h>

int blueRx=2;
int blueTx=3;
SoftwareSerial blueSerial(blueTx,blueRx);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  blueSerial.begin(9600);
  pinMode(12,INPUT);
  digitalWrite(12,HIGH);
}

void loop() {
  // put your main code here, to run repeatedly:
  int x=analogRead(0);
  int y=analogRead(5);
  int sw = digitalRead(12);

  blueSerial.print("<");
  blueSerial.print(x);
  blueSerial.print("\n"); // 안드로이드에게 보내기 위함...버퍼로 기다리고있으므로..
  blueSerial.print(">");
  blueSerial.print(y);
  blueSerial.print("\n");
  
  Serial.print("x=");
  Serial.print(x);
  Serial.print("y=");
  Serial.print(y);
  Serial.print("sw=");
  Serial.println(sw);

  delay(100);
  

}
