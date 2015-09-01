package com.byteshatf.callrecorder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.byteshatf.callrecorder.contactpicker.ContactsPicker;


public class AddRuleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_details);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                Intent intent = new Intent(getApplicationContext(), ContactsPicker.class);
                startActivityForResult(intent, AppGlobals.REQUEST_CODE);
                break;
        }

    }
}
