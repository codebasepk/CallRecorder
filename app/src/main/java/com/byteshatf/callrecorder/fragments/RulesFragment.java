package com.byteshatf.callrecorder.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.R;
import com.byteshatf.callrecorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class RulesFragment extends android.support.v4.app.Fragment implements Spinner.OnItemSelectedListener,
        AdapterView.OnItemClickListener {

    private View baseView;
    private Spinner mSpinner;
    private Helpers mHelpers;
    private ArrayList<String> arrayList;
    private ListView mListView;
    private DatabaseHelpers mDatabaseHelpers;
    private boolean mListViewDisplayed = false;
    private ArrayAdapter<String> mArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.rules_fragment, container, false);
        mHelpers = new Helpers(getActivity());
        mDatabaseHelpers = new DatabaseHelpers(getActivity().getApplicationContext());
        mSpinner = (Spinner) baseView.findViewById(R.id.spinner_main);
        mListView = (ListView) baseView.findViewById(R.id.rulesList);
        arrayList = mDatabaseHelpers.getAllPresentNotes();
        if (arrayList.isEmpty() && arrayList.size() == 0) {
            mHelpers.showDialogForFirstTime();
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
        mSpinner.setSelection(mHelpers.getValuesFromSharedPreferences("key", 0));
        return baseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListViewDisplayed) {
            mListView.setAdapter(mArrayAdapter);
            mListView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mHelpers.saveValues("key", parent.getSelectedItemPosition());
        System.out.println(parent.getSelectedItemPosition());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(parent.getItemAtPosition(position));

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
        String uriBase = "android.resource://com.fgm.callrecorder/";
        int specificIcon = mHelpers.getValuesFromSharedPreferences(title, Call.TURN_OFF);
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
                return uriBase + R.drawable.off;
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
        public static final int TURN_OFF = 3;
    }
}
