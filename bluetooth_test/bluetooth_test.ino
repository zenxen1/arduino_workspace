#include <SoftwareSerial.h>
#include <string.h>

//받는핀과 보내는핀 설정
int blueRx=2; // 보내는 핀
int blueTx=3; // 받는핀 

//시리얼 통신을 위한 객체 선언
SoftwareSerial blueSerial(blueTx, blueRx);
int LED=10;
int CHECK=8;
String receiveData="off"; //받는 문자열
boolean isOn=true;

void setup() {
  blueSerial.begin(); //블루투스 시리얼 개방!!
  Serial.begin(9600);

  pinMode(LED,OUTPUT);
  pinMode(CHECK, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:

}
