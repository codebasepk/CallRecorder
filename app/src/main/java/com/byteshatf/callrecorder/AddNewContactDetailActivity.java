package com.byteshatf.callrecorder;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.byteshatf.callrecorder.contactpicker.ContactsPicker;

public class AddNewContactDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mSelectContactsButton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.add_details);
        mSelectContactsButton = (ImageButton) findViewById(R.id.selectContacts);
        mSelectContactsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectContacts:
                Intent intent = new Intent(getApplicationContext(), ContactsPicker.class);
                startActivityForResult(intent,AppGlobals.REQUEST_CODE);
                break;
        }
    }
}
