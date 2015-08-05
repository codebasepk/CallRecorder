package com.byteshatf.callrecorder;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import com.byteshatf.callrecorder.contactpicker.ContactsPicker;

public class AddNewContactDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mSelectContactsButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_details);
        mSelectContactsButton = (ImageButton) findViewById(R.id.imageButton);
        mSelectContactsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                Intent intent = new Intent(getApplicationContext(), ContactsPicker.class);
                startActivityForResult(intent,AppGlobals.REQUEST_CODE);
                break;
        }
    }
}
