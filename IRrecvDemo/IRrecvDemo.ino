/*
 * IRremote: IRrecvDemo - demonstrates receiving IR codes with IRrecv
 * An IR detector/demodulator must be connected to the input RECV_PIN.
 * Version 0.1 July, 2009
 * Copyright 2009 Ken Shirriff
 * http://arcfn.com
 */

#include <IRremote.h>

int RECV_PIN = 8;
long currentKey = 0;

IRrecv irrecv(RECV_PIN);

decode_results results;

void setup()
{
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
  pinMode(10,OUTPUT);
  pinMode(11,OUTPUT);
  pinMode(12,OUTPUT);
}

void loop() {
  if (irrecv.decode(&results)) {
    Serial.println(results.value, HEX);
    //Serial.println(currentKey);
    //FF30CF -- 1번
    //FF18E7 -- 2번
    //FF7A85 -- 3번
    switch(results.value){
      case 0xFF30CF : 
        digitalWrite(10,HIGH);
        delay(1000);
        digitalWrite(10,LOW);break;

      case 0xFF18E7 : 
        digitalWrite(11,HIGH);
        delay(1000);
        digitalWrite(11,LOW);break;
  
      }
    
    irrecv.resume(); // Receive the next value
  }
  delay(100);
}
