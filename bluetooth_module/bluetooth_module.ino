#include <SoftwareSerial.h> //시리얼 통신 라이브러리 호출
#include <string.h>

int blueRx=2; //받는쪽 핀
int blueTx=3; //보내는쪽 핀
//블루투스 시리얼 통신을 위한 객체 선언
SoftwareSerial blueSerial(blueTx,blueRx);
String str="";

void setup() {
  // put your setup code here, to run once:
  blueSerial.begin(9600);
  Serial.begin(9600);
  pinMode(8,OUTPUT);
  
}

void loop() {
  
  while(blueSerial.available()){ //접속자가 보낸 데이터가 존재한다면....
    int data = blueSerial.read(); //한자씩 읽는다
    str +=(char)data;
  }
  if(!str.equals("")){
    Serial.println(str);
    if(str=="on"){
      digitalWrite(8,HIGH);
    }else{
      digitalWrite(8,LOW);
    }
    blueSerial.print(str+"\n");
  }
  delay(100);
  str="";
  
}
