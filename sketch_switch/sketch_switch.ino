int a=8;
boolean flag=true;
int data;

void setup() {
  // 12번핀에 전기가 나가도록 출력할 것임을 알려준다!!
  for(int i=8;i<=12;i++){
    pinMode(8,OUTPUT);
  }
  pinMode(4,INPUT);
  Serial.begin(9600);
  
}

void loop() {
  
   //전기가 들어오면 1====HIGH, 안들어오면 0==LOW
  
  data=digitalRead(4);
  Serial.println(data);
  
  if(data==1){
    if(flag){
      for(int i=a;i<=12;i++){
        digitalWrite(i,HIGH);
        delay(100);
        digitalWrite(i,LOW);
        delay(100);
       
        data=digitalRead(4);
        Serial.println(data);
        if(data==0){
          break;
        }
        a++;
        
      }
      if(data==1){
        flag=false;
      }
    }else{
    
    for(int j=a;j>=8;j--){
      digitalWrite(j,HIGH);
      delay(100);
      digitalWrite(j,LOW);
      delay(100);
      
      data=digitalRead(4);
      if(data==0){
        break;
        }
      a--;
      
    }
    if(data==1){
      flag=true;
    }
    }
  }

}
