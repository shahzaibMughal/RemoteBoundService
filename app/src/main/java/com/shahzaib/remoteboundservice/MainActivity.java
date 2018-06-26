package com.shahzaib.remoteboundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Messenger messenger = null;
    boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), MyRemoteService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection myConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            messenger = new Messenger(service);
            isBind = true;
        }
        public void onServiceDisconnected(ComponentName arg0) {
            isBind = false;
            messenger = null;
        }
    };

    public void sendDataToRemoteService(View view) {
        if (!isBind) return;
        // packing the data, for parcelling to the server (service)
        Bundle bundle = new Bundle();
        bundle.putString("MyString", "Hallo World!");
        Message message = new Message();
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(this, "Data Sending Failed, Exception occur...", Toast.LENGTH_SHORT).show();
        }

    }
}
