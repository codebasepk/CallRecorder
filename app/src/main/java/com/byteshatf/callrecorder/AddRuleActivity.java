package com.byteshatf.callrecorder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.byteshatf.callrecorder.contactpicker.ContactsPicker;


public class AddRuleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButton;
    private EditText editText;
    private String editTextData;
    private boolean mShowTemporaryCheckedContacts = false;
    private String mCheckedContacts;
    private boolean isStartedFresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_details);
        isStartedFresh = true;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        editText = (EditText) findViewById(R.id.editText);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                Intent intent = new Intent(getApplicationContext(), ContactsPicker.class);
                intent.putExtra("pre_checked", mCheckedContacts);
                intent.putExtra("temporary_select", mShowTemporaryCheckedContacts);
                startActivityForResult(intent, AppGlobals.REQUEST_CODE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_rules_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            editTextData = editText.getText().toString();
            if (editTextData.isEmpty()) {
                Toast.makeText(getApplicationContext(), "please enter a title", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (mCheckedContacts == null) {
                Toast.makeText(getApplicationContext(), "please select at least one contact",
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!editTextData.isEmpty() && editTextData != null && mCheckedContacts != null) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppGlobals.REQUEST_CODE:
                if (resultCode == AppGlobals.RESULT_OK) {
                    mShowTemporaryCheckedContacts = true;
                    isStartedFresh = false;
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        mCheckedContacts = null;
                    } else {
                        mCheckedContacts = extras.getString("selected_contacts");
                    }
                }
        }
    }
}
