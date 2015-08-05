package com.byteshatf.callrecorder;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.byteshatf.callrecorder.contactpicker.ContactsPicker;

public class MainActivity extends AppCompatActivity {
    private Helpers mHelpers;
    private IncomingCallListener mIncomingCallListener;
    private TelephonyManager mTelephonyManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelpers = new Helpers(getApplicationContext());
        mIncomingCallListener = new IncomingCallListener();
        mTelephonyManager = mHelpers.getTelephonyManager();
        mTelephonyManager.listen(mIncomingCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mIncomingCallListener.mOutgoingCall, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
