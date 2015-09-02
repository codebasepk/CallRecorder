package com.byteshatf.callrecorder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshatf.callrecorder.contactpicker.ContactsPicker;
import com.byteshatf.callrecorder.database.DatabaseHelpers;

import java.util.ArrayList;


public class AddRuleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButton;
    private EditText editText;
    private String editTextData;
    private boolean mShowTemporaryCheckedContacts = false;
    private String mCheckedContacts;
    private boolean isStartedFresh = false;
    private Spinner mSpinner;
    private int mSpinnerValue;
    private DatabaseHelpers mDatabaseHelpers;
    private Switch mSwitch;
    private ListView mContactsListView;
    private Helpers mHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_details);
        isStartedFresh = true;
        mDatabaseHelpers = new DatabaseHelpers(getApplicationContext());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006666")));
        mSpinner = (Spinner) findViewById(R.id.spinner_add_fragment);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        mSwitch = (Switch) findViewById(R.id.switch1);
        editText = (EditText) findViewById(R.id.et_title);
        mContactsListView = (ListView) findViewById(R.id.lv_edit_rule);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                mCheckedContacts = null;
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
            mSpinnerValue = mSpinner.getSelectedItemPosition();
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
                mDatabaseHelpers.createNewEntry(editTextData, String.valueOf(mSwitch.isChecked()),
                        mCheckedContacts);
                mHelpers.saveValues(editTextData, mSpinnerValue);
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
                        if (mCheckedContacts != null) {
                            String[] items = mCheckedContacts.split(",");
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (String item : items) {
                                arrayList.add(item);
                            }
                            ArrayAdapter<String> arrayAdapter = new FinalizedContacts(getApplicationContext(),
                                    R.layout.row, arrayList);
                            mContactsListView.setAdapter(arrayAdapter);
                        }
                    }
                }
        }
    }

    class FinalizedContacts extends ArrayAdapter<String> {

        private ArrayList<String> mArrayList;


        public FinalizedContacts(Context context, int resource,
                                 ArrayList<String> arrayList) {
            super(context, resource, arrayList);
            mArrayList = arrayList;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String title = mArrayList.get(position);
            holder.title.setText(title);
            return convertView;
        }

        class ViewHolder {
            public TextView title;
        }
    }
}
