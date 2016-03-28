package com.example.axlevisu.vgrdimmercontrol;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
//    private static final int MESSAGE_READ =3;
    private static final int SUCCESS_CONNECT =4;
    private static final int SEND_STRING =5;
    private BluetoothAdapter mBluetoothAdapter = null;
    private  ConnectThread mConnectThread = null;
    private BluetoothDevice mDevice = null;

    SeekBar seekBar;
    TextView tvRating;
    SeekBar seekBar1;
    TextView tvRating1;
    SeekBar seekBar2;
    TextView tvRating2;
    SeekBar seekBar3;
    TextView tvRating3;
//    Button btnSend;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_CONNECT:
                    // DO something
                    ConnectedThread connectedThread = new ConnectedThread(
                            (BluetoothSocket) msg.obj);
                    if(connectedThread!=null){Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_SHORT).show();}
                    else{Toast.makeText(getApplicationContext(), "CONNECTION FAILED", Toast.LENGTH_SHORT).show();}
//                    tvRating1 = (TextView) findViewById(R.id.loadText1);
//                    String s = tvRating1.getText().toString();
//                    connectedThread.write(s.getBytes());
                    break;
                case SEND_STRING:
                    ConnectedThread connectionThread = new ConnectedThread(
                            (BluetoothSocket) msg.obj);
                    int load = (int)msg.arg1;
                    switch (load){
                        case 1:
                            seekBar = (SeekBar) findViewById(R.id.loadSeekBar1);
                            tvRating = (TextView) findViewById(R.id.loadText1);
                            break;
                        case 2:
                            seekBar = (SeekBar) findViewById(R.id.loadSeekBar2);
                            tvRating = (TextView) findViewById(R.id.loadText2);
                            break;
                        case 3:
                            seekBar = (SeekBar) findViewById(R.id.loadSeekBar3);
                            tvRating = (TextView) findViewById(R.id.loadText3);
                            break;
                        default:
                            seekBar = (SeekBar) findViewById(R.id.loadSeekBar1);
                            tvRating = (TextView) findViewById(R.id.loadText1);
                            break;
                    }

                    String s = String.valueOf(seekBar.getProgress());
//                    int intvalue = Integer.parseInt(s);
                    int intvalue = seekBar.getProgress();
                    intvalue = intvalue + 16*load;
                    int minutes = 0;
                    int hours =0;
                    int message = hours*65536 + minutes*256 + intvalue;
                    String s_g =Character.toString ((char) message);
                    connectionThread.write(s_g.getBytes());
                    tvRating.setText(s);
                    break;
//                case MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    String string = new String(readBuf);
//                    Toast.makeText(getApplicationContext(), string, 0).show();
//                    break;
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
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
//            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
            manageConnectedSocket(mmSocket);
//            mConnectedThread = new ConnectedThread(mmSocket);
//            mConnectedThread.start();
        }
//        mConnectedThread = new ConnectedThread(mmSocket);
//        mConnectedThread.start();

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
//    public void convert_to_hex(){
//
//    }
    private void manageConnectedSocket(BluetoothSocket mSocket)
    {   final BluetoothSocket mmSocket = mSocket;
        mHandler.obtainMessage(SUCCESS_CONNECT, mSocket).sendToTarget();

        seekBar1 = (SeekBar) findViewById(R.id.loadSeekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.loadSeekBar2);
        seekBar3 = (SeekBar) findViewById(R.id.loadSeekBar3);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "DEVICE DOES NOT SUPPORT BLUETOOTH ", Toast.LENGTH_SHORT).show();

                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "PLEASE ENABLE BLUETOOTH", Toast.LENGTH_SHORT).show();
                }
                else {
                    mHandler.obtainMessage(SEND_STRING, 1, 1, mmSocket).sendToTarget();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {

            }

        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar2, int progress, boolean fromUser) {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "DEVICE DOES NOT SUPPORT BLUETOOTH ", Toast.LENGTH_SHORT).show();

                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "PLEASE ENABLE BLUETOOTH", Toast.LENGTH_SHORT).show();
                }
                else {
                    mHandler.obtainMessage(SEND_STRING, 2, 2, mmSocket).sendToTarget();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar2) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {

            }

        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar3, int progress, boolean fromUser) {
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "DEVICE DOES NOT SUPPORT BLUETOOTH ", Toast.LENGTH_SHORT).show();

                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "PLEASE ENABLE BLUETOOTH", Toast.LENGTH_SHORT).show();
                }
                else {
                    mHandler.obtainMessage(SEND_STRING, 3, 3, mmSocket).sendToTarget();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar3) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar3) {

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
//            byte[] buffer = new byte[1024];  // buffer store for the stream
//            int bytes; // bytes returned from read()
//
//            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//                    // Send the obtained bytes to the UI activity
//                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                } catch (IOException e) {
//                    break;
//                }
//            }
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



    // Handler in DataTransferActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    private void initViews() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        tvRating1 = (TextView) findViewById(R.id.loadText1);
        seekBar1 = (SeekBar) findViewById(R.id.loadSeekBar1);
        tvRating2 = (TextView) findViewById(R.id.loadText2);
        seekBar2 = (SeekBar) findViewById(R.id.loadSeekBar2);
        tvRating3 = (TextView) findViewById(R.id.loadText3);
        seekBar3 = (SeekBar) findViewById(R.id.loadSeekBar3);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                tvRating1.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {

            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar2, int progress, boolean fromUser) {
                tvRating2.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar2) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {

            }
        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar3, int progress, boolean fromUser) {
                tvRating3.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar3) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar3) {

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
            case R.id.item_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
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
