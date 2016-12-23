
int data;
void setup() {
  // put your setup code here, to run once:

  Serial.begin(9600);

}

//아날로그 출력은 0에서 5V를 0 ~ 255까지 표현한다.
void loop() {
  // put your main code here, to run repeatedly:
  data=analogRead(0);
  Serial.println(data);
  delay(100);

}
