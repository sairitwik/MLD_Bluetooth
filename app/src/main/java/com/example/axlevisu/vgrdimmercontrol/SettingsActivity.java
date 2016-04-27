package com.example.axlevisu.vgrdimmercontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
//import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
//import java.sql.Time;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


/**
 * Created by axlevisu on 5/3/16.
 */
public class SettingsActivity extends AppCompatActivity {
    Button loadBAdd1;
    Button loadBAdd2;
    Button loadBAdd3;
    Button loadBButton1;
    Button loadBButton2;
    Button loadBButton3;
    ListView loadLList1;
    ListView loadLList2;
    ListView loadLList3;
    ListView presetlist;
    private int max = 12;
    private final static int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    //    private static final int MESSAGE_READ =3;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int SUCCESS_CONNECT =4;
    private static final int SEND_STRING =5;
    private BluetoothAdapter mBluetoothAdapter = null;
    private  ConnectThread mConnectThread = null;
    private BluetoothDevice mDevice = null;
    private ConfigListAdapter adapter1;
    private ConfigListAdapter adapter2;
    private ConfigListAdapter adapter3;
    private ConfigListAdapter adapter;
    ArrayList<Configuration> config_list1 = new ArrayList<Configuration>();
    ArrayList<Configuration> config_list2 = new ArrayList<Configuration>();
    ArrayList<Configuration> config_list3 = new ArrayList<Configuration>();


    public android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_CONNECT:
                    ConnectedThread connectedThread = new ConnectedThread(
                            (BluetoothSocket) msg.obj);
                    if(connectedThread!=null){Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_SHORT).show();}
                    else{Toast.makeText(getApplicationContext(), "CONNECTION FAILED", Toast.LENGTH_SHORT).show();}
                    break;
                case SEND_STRING:
                    ConnectedThread connectionThread = new ConnectedThread(
                            (BluetoothSocket) msg.obj);
                    int load = (int)msg.arg1;
                    switch (load){
                        case 1:
//                            presetlist = (ListView)findViewById(R.id.loadList1);
//                            adapter = presetlist.getAdapter();
                            adapter = adapter1;
                            break;
                        case 2:
                            adapter = adapter2;
                            break;
                        case 3:
                            adapter = adapter3;
                            break;
                    }
                    int startbit = 256 - load;
                    byte[] start_byte = ByteBuffer.allocate(4).putInt(startbit).array();
                    byte[] start_byt = {start_byte[3]};
                    connectionThread.write(start_byt);
                    int num = adapter.getCount();
                    Calendar now = Calendar.getInstance();

                    int hours = now.get(Calendar.HOUR_OF_DAY);
                    int mins = now.get(Calendar.MINUTE);
//                    int hours = new Time(System.currentTimeMillis()).getHours();
//                    int mins = new Time(System.currentTimeMillis()).getMinutes();
                    for(int i =0; i<num; i++){
                        Configuration config = adapter.getItem(i);
                        int h = (config.getHours()-hours);
                        int m = (config.getMinutes()-mins)%60;
                        if(m<0) {m +=60; h-=1;}
                        h = h%24;
                        if(h<0) h +=24;
                        Toast.makeText(getApplicationContext(), String.valueOf(h) + String.valueOf(m) + String.valueOf(config.getSpeed()), Toast.LENGTH_SHORT).show();
                        int message = config.getSpeed() + 256*(m) + 65536*(h);
                        byte[] in_msg = ByteBuffer.allocate(4).putInt(message).array();
                        byte[] in_message = {in_msg[1], in_msg[2], in_msg[3]};
                        connectionThread.write(in_message);
                    }
                    connectionThread.write(start_byt);
                    break;
            }
        }
    };

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        //        private ConnectedThread mConnectedThread = null;
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
//                Toast.makeText(getApplicationContext(), "CONNECTION FAILED!", Toast.LENGTH_SHORT).show();
                // Unable to connect; close the socket and get out
                try {
//                    Toast.makeText(getApplicationContext(), "CONNECTION FAILED!", Toast.LENGTH_SHORT).show();
                    mmSocket.connect();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);

        }


        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void manageConnectedSocket(BluetoothSocket mSocket){
        final BluetoothSocket mmSocket = mSocket;
        mHandler.obtainMessage(SUCCESS_CONNECT, mSocket).sendToTarget();
        loadBButton1 = (Button) findViewById(R.id.loadButton1);
        loadBButton2 = (Button) findViewById(R.id.loadButton2);
        loadBButton3 = (Button) findViewById(R.id.loadButton3);
        loadBButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "DEVICE DOES NOT SUPPORT BLUETOOTH ", Toast.LENGTH_SHORT).show();

                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "PLEASE ENABLE BLUETOOTH", Toast.LENGTH_SHORT).show();
                } else {
                    mHandler.obtainMessage(SEND_STRING, 1, 1, mmSocket).sendToTarget();
                }
            }
        });
        loadBButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "DEVICE DOES NOT SUPPORT BLUETOOTH ", Toast.LENGTH_SHORT).show();

                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "PLEASE ENABLE BLUETOOTH", Toast.LENGTH_SHORT).show();
                } else {
                    mHandler.obtainMessage(SEND_STRING, 2, 2, mmSocket).sendToTarget();
                }
            }
        });
        loadBButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "DEVICE DOES NOT SUPPORT BLUETOOTH ", Toast.LENGTH_SHORT).show();

                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "PLEASE ENABLE BLUETOOTH", Toast.LENGTH_SHORT).show();
                } else {
                    mHandler.obtainMessage(SEND_STRING, 3, 3, mmSocket).sendToTarget();
                }
            }
        });
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {//TODO: Complete it later

        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
//                Toast.makeText(getApplicationContext(),bytes,Toast.LENGTH_SHORT)
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
    @Override
    protected void onDestroy(){
        if(mConnectThread != null){mConnectThread.cancel();}

        if(mDevice !=null){
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, mDevice.getAddress());

        // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        super.onDestroy();

    }

    @Override
    public void onBackPressed(){
        if(mConnectThread != null){mConnectThread.cancel();}

        if(mDevice !=null){
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, mDevice.getAddress());

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_preset);
        adapter1 = new ConfigListAdapter(SettingsActivity.this,R.layout.time_details, config_list1);
        adapter2 = new ConfigListAdapter(SettingsActivity.this,R.layout.time_details, config_list2);
        adapter3 = new ConfigListAdapter(SettingsActivity.this,R.layout.time_details, config_list3);
        loadLList1 = (ListView)findViewById(R.id.loadList1);
        loadLList2 = (ListView)findViewById(R.id.loadList2);
        loadLList3 = (ListView)findViewById(R.id.loadList3);
        loadLList1.setAdapter(adapter1);
        loadLList2.setAdapter(adapter2);
        loadLList3.setAdapter(adapter3);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String address = getIntent().getStringExtra(MainActivity.DEVICE_ADDRESS);
        if(address != null){mDevice = mBluetoothAdapter.getRemoteDevice(address);}
        if(mDevice!=null){
            mConnectThread = new ConnectThread(mDevice);
            mConnectThread.start();
//            Toast.makeText(getApplicationContext(), "CONNECTED Maybe?", Toast.LENGTH_SHORT).show();
        }
        initViews();
    }
    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    private void initViews(){
        Toast.makeText(getApplicationContext(), "Previous settings will be overwritten", Toast.LENGTH_SHORT).show();
        loadBAdd1 = (Button)findViewById(R.id.loadAdd1);
        loadBAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (config_list1.size() < max) {
                    config_list1.add(new Configuration(0, 0, 0));
                    adapter1.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(loadLList1);
//                    Toast.makeText(getApplicationContext(), String.valueOf(adapter1.getCount()), Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadBAdd2 = (Button)findViewById(R.id.loadAdd2);
        loadBAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (config_list2.size() < max) {
                    config_list2.add(new Configuration(0, 0, 0));
                    adapter2.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(loadLList2);
//                    Toast.makeText(getApplicationContext(), String.valueOf(adapter2.getCount()), Toast.LENGTH_SHORT).show();
                }

            }
        });
        loadBAdd3 = (Button)findViewById(R.id.loadAdd3);
        loadBAdd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (config_list3.size() < max) {
                    config_list3.add(new Configuration(0, 0, 0));
                    adapter3.notifyDataSetChanged();
                    justifyListViewHeightBasedOnChildren(loadLList3);
//                    Toast.makeText(getApplicationContext(), String.valueOf(adapter3.getCount()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    mDevice = mBluetoothAdapter.getRemoteDevice(address);
                    if(mConnectThread != null){mConnectThread.cancel();}
                    if(mDevice!=null){
                        mConnectThread = new ConnectThread(mDevice);
                        mConnectThread.start();
                    }

                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {

                    Intent devices_intent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(devices_intent, REQUEST_CONNECT_DEVICE);

                } else {
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.item_settings:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                break;
            case R.id.device_connect:
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(),"Device does not support Bluetooth",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                else{
//
                    Intent devices_intent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(devices_intent, REQUEST_CONNECT_DEVICE);

                }



        }
        return super.onOptionsItemSelected(item);
    }


}
