package com.byteshatf.callrecorder.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.byteshatf.callrecorder.AddRuleActivity;
import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.R;
import com.byteshatf.callrecorder.database.DatabaseHelpers;

import java.util.ArrayList;


public class RulesFragment extends android.support.v4.app.Fragment implements Spinner.OnItemSelectedListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, CompoundButton.OnCheckedChangeListener {

    private View baseView;
    private Spinner mSpinner;
    private Helpers mHelpers;
    private ArrayList<String> arrayList;
    private ListView mListView;
    private DatabaseHelpers mDatabaseHelpers;
    private boolean mListViewDisplayed = false;
    private ArrayAdapter<String> mArrayAdapter;
    private CheckBox mCheckBox;
    private SharedPreferences mSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.rules_fragment, container, false);
        mHelpers = new Helpers(getActivity());
        mSharedPreferences = mHelpers.getPreferenceManager();
        mDatabaseHelpers = new DatabaseHelpers(getActivity().getApplicationContext());
        mSpinner = (Spinner) baseView.findViewById(R.id.spinner_main);
        mListView = (ListView) baseView.findViewById(R.id.rulesList);
        mCheckBox = (CheckBox) baseView.findViewById(R.id.checkboxUnknownCalls);
        mCheckBox.setChecked(mSharedPreferences.getBoolean(AppGlobals.sCheckBoxState, false));
        mCheckBox.setOnCheckedChangeListener(this);
        arrayList = mDatabaseHelpers.getAllPresentNotes();
        if (mSharedPreferences.getBoolean("fresh_install", true)) {
            mHelpers.showDialogForFirstTime();
            mSharedPreferences.edit().putBoolean("fresh_install", false).apply();
        } else {
            mArrayAdapter = new CategoriesAdapter(getActivity()
                    .getApplicationContext(), R.layout.main_listview, arrayList);
            mListViewDisplayed = true;
        }
        mSpinner.setOnItemSelectedListener(this);
        String[] mainSpinner = {"All Incoming Calls", "All Outgoing Calls", "All Incoming/Outgoing",
                "Unknown Calls Only","Selected Only"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, mainSpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setSelection(mHelpers.getValuesFromSharedPreferences(AppGlobals.MAIN_SPINNER_KEY, 0));
        return baseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListViewDisplayed) {
            arrayList = mDatabaseHelpers.getAllPresentNotes();
            mArrayAdapter = new CategoriesAdapter(getActivity()
                    .getApplicationContext(), R.layout.main_listview, arrayList);
            mListViewDisplayed = true;
            mListView.setAdapter(mArrayAdapter);
            mListView.setOnItemClickListener(this);
            mListView.setOnItemLongClickListener(this);
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mHelpers.saveValues(AppGlobals.MAIN_SPINNER_KEY, parent.getSelectedItemPosition());
        if(position == 4) {
            mCheckBox.setVisibility(View.VISIBLE);
        } else {
            mCheckBox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AddRuleActivity.class);
        intent.putExtra("title",  parent.getItemAtPosition(position).toString());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this Rule?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabaseHelpers.deleteCategory(parent.getItemAtPosition(position).toString());
                SharedPreferences sharedPreferences = mHelpers.getPreferenceManager();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(parent.getItemAtPosition(position).toString());
                editor.remove((AppGlobals.sSwitchState +
                        parent.getItemAtPosition(position).toString()).trim());
                String item = mArrayAdapter.getItem(position);
                mArrayAdapter.remove(item);
                mArrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mSharedPreferences.edit().putBoolean(AppGlobals.sCheckBoxState, isChecked).apply();
    }

    class CategoriesAdapter extends ArrayAdapter<String> {

        public CategoriesAdapter(Context context, int resource, ArrayList<String> videos) {
            super(context, resource, videos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.main_listview, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.FilePath);
                holder.direction = (ImageView) convertView.findViewById(R.id.call_direction);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String title = arrayList.get(position);
            holder.title.setText(title);
            holder.direction.setImageURI(Uri.parse(getDirectionThumbnail(title)));
            return convertView;
        }
    }

    private String getDirectionThumbnail(String title) {
        String uriBase = "android.resource://com.fgm.recorder/";
        int specificIcon = mHelpers.getValuesFromSharedPreferences(title, Call.SHOW_INCOMING_CALL);
        return getDirectionIcon(uriBase, specificIcon);
    }

    @NonNull
    private String getDirectionIcon(String uriBase, int noteShowPreference) {
        switch (noteShowPreference) {
            case Call.SHOW_INCOMING_CALL:
                return uriBase + R.drawable.incoming_call;
            case Call.SHOW_OUTGOING_CALL:
                return uriBase + R.drawable.outgoing_call;
            case Call.SHOW_INCOMING_OUTGOING:
                return uriBase + R.drawable.incoming_outgoing_call;
            default:
                return uriBase + R.drawable.incoming_call;
        }
    }

    static class ViewHolder {
        public TextView title;
        public ImageView direction;
    }

    public static class Call {
        static final int SHOW_INCOMING_CALL = 0;
        static final int SHOW_OUTGOING_CALL = 1;
        static final int SHOW_INCOMING_OUTGOING = 2;
    }
}
