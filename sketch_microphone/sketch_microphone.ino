void setup() {
  // 8번핀으로 사운드 데이터 읽기
  pinMode(8,INPUT);
  Serial.begin(9600);
}

void loop() {
  // 
  //int soundDetectValue = digitalRead(8);
  int soundDetectValue = analogRead(0);
  if(soundDetectValue >100){
      Serial.println(soundDetectValue);
      Serial.println("you are so loud!!");
    }else{
      Serial.println(soundDetectValue);
      Serial.println("quiet....zzzz");
    }
  delay(100);

}
