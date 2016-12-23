package com.sds.study.bluetootharduino;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lee on 2016-12-08.
 */

public class ListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
    TextView txt_name;
    TextView txt_adress;

    public ListAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);




    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        View view = null;
        BluetoothDevice bluetoothDevice = list.get(i);
        if(convertview!=null){
            view =convertview;

        }else{
            view = inflater.inflate(R.layout.item_device,null);
         }
        txt_name = (TextView)view.findViewById(R.id.txt_name);
        txt_adress = (TextView)view.findViewById(R.id.txt_address);
        txt_name.setText(bluetoothDevice.getName());
        txt_adress.setText(bluetoothDevice.getAddress());
        return view;
    }
}
