package com.example.mario.raizin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList extends AppCompatActivity {

    Button btnPaired;
    ListView devicelist;
    SharedPreferences myPrefs;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    int deviceScoreTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        //myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);

        btnPaired = (Button) findViewById(R.id.button);
        devicelist = (ListView) findViewById(R.id.listView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            Toast.makeText(getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
            finish();
        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();  //was pairedDevicesList();
                btnPaired.setVisibility(View.INVISIBLE);
                //Intent i = new Intent(getApplicationContext(), UVDisplay.class);
                //Intent intent4=getIntent();
                //String passedNameToDeviceListString=intent4.getStringExtra("passedNameToDeviceList");
                //i.putExtra("passedNameToDeviceList", passedNameToDeviceListString);
                //myPrefs.edit().putString("device_add", address).apply();
                //startActivity(i);
            }
        });
    }

    private void pairedDevicesList () {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if ( pairedDevices.size() > 0 ) {
            for ( BluetoothDevice bt : pairedDevices ) {
                list.add(bt.getName().toString() + "\n" + bt.getAddress().toString());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length()-17);// gets bluetooth address

            Intent i = new Intent(getApplicationContext(), UVDisplay.class);
            i.putExtra(EXTRA_ADDRESS, address);
            deviceScoreTrack = i.getIntExtra("SCORE_TRACK", 0);
            i.putExtra("SCORE_TRACK", deviceScoreTrack);
            Intent intent4=getIntent();
            String passedNameToDeviceListString=intent4.getStringExtra("passedNameToDeviceList");
            i.putExtra("passedNameToDeviceList", passedNameToDeviceListString);
            //myPrefs.edit().putString("device_add", address).apply();
            startActivity(i);
        }
    };
}