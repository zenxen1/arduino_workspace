//애노드 방식을 사용하므로, VCC만 연결하면 되고
//켤려면 0으로 세팅함
//각 LED에 겨질 숫자를 배열로 표현하자

int num[]={0,0,0,0,0,0,1}; //0
int num1[]={1,0,0,1,1,1,1}; //1

int numbers[10][7]={
  {0,0,0,0,0,0,1},
  {1,0,0,1,1,1,1},
  {0,0,1,0,0,1,0},
  {0,0,0,0,1,1,0},
  {1,0,0,1,1,0,0},
  {0,1,0,0,1,0,0},
  {1,1,0,0,0,0,0},
  {0,0,0,1,1,1,1},
  {0,0,0,0,0,0,0},
  {0,0,0,0,1,0,0}
  };

void setup() {
  Serial.begin(9600);
  for(int i=2;i<=9;i++){
    pinMode(i, OUTPUT);
  }
}

void loop() {
  for(int a=0;a<=9;a++){
    for(int i=0;i<=6;i++){
      if(numbers[a][i]==1){
        digitalWrite(i+2,HIGH);
      }else{
        digitalWrite(i+2,LOW);
      }
    }
    delay(1000);
  }  
}
