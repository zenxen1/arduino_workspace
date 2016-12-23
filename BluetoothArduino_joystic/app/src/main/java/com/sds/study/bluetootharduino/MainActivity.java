package com.sds.study.bluetootharduino;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    static MainActivity mainActivity;
    static final int REQUSET_ENABLE_BLUETOOTH =1;
    static final int REQUEST_ACESS_PERMMISION=2;

    BluetoothAdapter bluetoothAdapter;
    //시스템이 앱들에게 인텐트를 방송할때, 그 방송을 받을 수 있는 컴포넌트
    BroadcastReceiver receiver;
    ListView listView;
    EditText edit_send,edit_receive;


    ListAdapter listAdapter;
    String UUID="00001101-0000-1000-8000-00805F9B34FB";
    BluetoothSocket socket;
    Thread connectThread;
    Handler handler;
    DataThread dataThread;
    GameActivity gameActivity;

    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TAG = this.getClass().getName();
        mainActivity =this;

        listView = (ListView)findViewById(R.id.listView);
        edit_send = (EditText)findViewById(R.id.edit_send);
        edit_receive = (EditText)findViewById(R.id.edit_receive);

        listAdapter = new ListAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

        checkSupportDevice();
        checkEnableBluetooth();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //Toast.makeText(MainActivity.this, bluetoothDevice+"발견했어!!", Toast.LENGTH_SHORT).show();
                    listAdapter.list.add(bluetoothDevice);
                    listAdapter.notifyDataSetChanged();
                }
            }

        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter); //리시버 등록

        handler = new Handler(){
            @Override
            public void handleMessage(Message message) {
               String msg = message.getData().getString("msg");
                Log.d(TAG,"아두이노에서 넘어오는 x,y,sw"+msg);
                //gameActivity.gameView.rect.set();
                if(gameActivity!=null) {
                    if (msg.substring(0) == ">") {
                        String submsg = msg.substring(1, 4);
                        Log.d(TAG, "들어오는값" + submsg.toString());
                        gameActivity.gameView.rect.set(Integer.parseInt(submsg)-500,0,Integer.parseInt(submsg)+100-500,100);
                        gameActivity.gameView.invalidate();
                    } else {
                       String submsg = msg.substring(1, 4);
                        Log.d(TAG, "들어오는값" + submsg.toString());
                        gameActivity.gameView.rect.set(0,Integer.parseInt(submsg.toString())-500,0, Integer.parseInt(submsg.toString()) + 100-500);
                        gameActivity.gameView.invalidate();
                    }
                }
            }
        };
    }


    /*------------------------------------------------------------------
    디바이스가 블루투스 지원하는지 여부 체크
    -------------------------------------------------------------------- */
    public void checkSupportDevice(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            showAlert("안내","이 디바이스는 블루투스를 지원하지 않습니다");
            finish();
            return;
        }
    }
    /*-----------------------------------------------------------------
    * 활성화가 안되어 있따면, 활성화 하도록 요청
    * ------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUSET_ENABLE_BLUETOOTH:
                if(resultCode == RESULT_CANCELED){
                    showAlert("경고","블루투스를 켜셔야 합니다");
                }break;
        }
    }

    public void checkEnableBluetooth(){
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUSET_ENABLE_BLUETOOTH);
        }
    }

    /*-----------------------------------------------------------------
    * 현재 우리 앱이 권한이 취득되어 있는지 체크
    * ACESS_CORSE_LOCATION
    * ------------------------------------------------------------------*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_ACESS_PERMMISION:
                if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_DENIED){
                    showAlert("경고","권한을 주셔야 합니다.");
                }
                break;
        }
    }

    public void checkAccessPermision(){
        int accessPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(accessPermission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_ACESS_PERMMISION);
        }else {
            //검색시작
            scanDevice();
        }
    }

    /*-----------------------------------------------------------------
    기기검색
    브로드 케스트 리시버 등록과 동시에 검색 시작
    ACTION_FOUND 인텐트를 받겠다고,필터로 등록
    * ------------------------------------------------------------------*/
    public void scanDevice(){
        bluetoothAdapter.startDiscovery();//검색시작

    }

    /*-----------------------------------------------------------------
    * 검색된 기기 목록을 통해 접속을 시도...
    * 주의!! - 현재 진행중인 검색은 종료해야 한다...cancelDiscover()...
    * BuletoothSocket 필요....UUID
    * ------------------------------------------------------------------*/
    public void connectDevice(BluetoothDevice device){
        bluetoothAdapter.cancelDiscovery();//검색종료
        Toast.makeText(this,device.getName()+"을 접속할까요?",Toast.LENGTH_SHORT).show();
        try {
            socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //쓰레드를 이용하여 접속 시도
        connectThread = new Thread(){
            @Override
            public void run() {
                try {
                    socket.connect(); // 접속시도
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","접속성공");
                    message.setData(bundle);
                    handler.sendMessage(message);

                    //소켓으로부터 스트림을 뽑아 데이터를 주고받자
                    dataThread = new DataThread(MainActivity.this, socket);
                    dataThread.start();


                } catch (IOException e) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","접속실패");
                    message.setData(bundle);
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        };
        connectThread.start();
    }

    /*-----------------------------------------------------------------
    * 접속된 이후, 스트림 뽑아서 대화 나누면...
    * C-java 걱정하지말라..소켓에 의한 스트림만 제어하면 됨...
    * ------------------------------------------------------------------*/


    /*-----------------------------------------------------------------
    * 메세지 창 띄위기
    * ------------------------------------------------------------------*/
    public void showAlert(String title, String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title).setMessage(msg).show();
    }

    public void sendMsg(){
        String msg = edit_send.getText().toString();
        dataThread.send(msg);
        Log.d(TAG,"보낼 메서지는?"+msg);
    }

    /*별도의 액티비티를 띄워서 그래픽 처리하자*/
    public void showGame(){
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
        Toast.makeText(mainActivity, "눌리냐?", Toast.LENGTH_SHORT).show();
    }

    public void btnClick(View view){
        switch (view.getId()) {
            case R.id.bt_scan : checkAccessPermision(); break;
            case R.id.bt_send :sendMsg(); break;
            case R.id.bt_game : showGame(); break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        //선택한 아이템의 어드레스를 축출하여 몇번쨰 아이템인지 조사한후 같은 인덱스에 있는 Device를 추출하자
        TextView txt_address = (TextView)view.findViewById(R.id.txt_address);
        String adress = txt_address.getText().toString();
        for(int i=0;i<listAdapter.list.size();i++){
            BluetoothDevice device=listAdapter.list.get(i);
            if(device.getAddress().equals(adress)){
                connectDevice(device); //발견과 동시에 넘기기!!
                break;
            }
        }

    }
}
