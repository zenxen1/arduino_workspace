#include <Servo.h>
Servo myservo;
int speakerPin = 3;

int length = 15; // the number of notes
//char notes[] = "ccggaagffeeddc "; // a space represents a rest
char notes[] = { 'c', 'd', 'e', 'f', 'g', 'a', 'b', 'C' };
int beats[] = { 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 4 };
int tempo = 300;

void playTone(int tone, int duration) {
  for (long i = 0; i < duration * 1000L; i += tone * 2) {
    digitalWrite(speakerPin, HIGH);
    delayMicroseconds(tone);
    digitalWrite(speakerPin, LOW);
    delayMicroseconds(tone);
  }
}

void playNote(char note, int duration) {
  char names[] = { 'c', 'd', 'e', 'f', 'g', 'a', 'b', 'C' };
  int tones[] = { 1915, 1700, 1519, 1432, 1275, 1136, 1014, 956 };

  // play the tone corresponding to the note name
  for (int i = 0; i < 8; i++) {
    if (names[i] == note) {
      playTone(tones[i], duration);
    }
  }
}

void setup() {
  // put your setup code here, to run once:
  pinMode(speakerPin, OUTPUT);
  Serial.begin(9600);
  myservo.attach(11);
}

//아날로그 출력은 0에서 5V를 0 ~ 255까지 표현한다.
void loop() {
  // put your main code here, to run repeatedly:
  int data=0;
  data=analogRead(0);
  Serial.println(data);
  
 
  analogWrite(5,map(data,0,1023,0,255));
  myservo.write(map(data,0,1023,0,180));
  delay(100);
/*
  for (int i = 0; i < length; i++) {
    if (notes[i] == ' ') {
      delay(beats[i] * tempo); // rest
    } else {
      playNote(notes[i], beats[i] * tempo);
    }

    // pause between notes
    delay(tempo / 2); 
  }
  */
  if(data>10){
   playNote(notes[map(data,0,1023,0,7)], 1 * tempo);
  }
}
