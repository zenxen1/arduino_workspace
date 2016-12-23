//아날로그 핀은 별도의 pinMode 설정하지 않음!!
//LED 별도로 회로 구성한 후, 이 LED의 밝기를 조도센서 반비례로 처리하기..
//아날로그 입력은 0 ~ 1023 LED를 대상으로 analog 0~255
//측정값과 출력할 값이 서로 비율이 틀리므로, 개발자가 계산해야 하는 번거로움을 해결한 함수 = map
// map(측정값, 측정값의 범위)
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  int data = analogRead(0);
  int result = map(data,148,466,0,255);
  Serial.println(data);
  analogWrite(9,255-result);
  
}
