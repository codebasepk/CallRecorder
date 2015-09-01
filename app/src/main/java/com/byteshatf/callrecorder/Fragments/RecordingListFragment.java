package com.byteshatf.callrecorder.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.R;

import java.util.ArrayList;

public class RecordingListFragment extends android.support.v4.app.Fragment {

    private ArrayAdapter<String> arrayAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Helpers helpers = new Helpers(getActivity());
        view = inflater.inflate(R.layout.list_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.recording_list);
        ArrayList<String> arrayList = helpers.getAllFilesFromFolder();
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        return view;
    }
}
