package com.byteshatf.callrecorder.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordingListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ArrayAdapter<String> arrayAdapter;
    private View view;
    private ListView mRecordList;
    private Helpers mHelpers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHelpers = new Helpers(getActivity());
        view = inflater.inflate(R.layout.list_fragment, container, false);
        mRecordList = (ListView) view.findViewById(R.id.recording_list);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<String> arrayList = mHelpers.getAllFilesFromFolder();
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        mRecordList.setOnItemLongClickListener(this);
        mRecordList.setOnItemClickListener(this);
        mRecordList.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this recording?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = AppGlobals.getDataDirectory("CallRec")+ "/" + parent.getItemAtPosition(position) ;
                removeFiles(path);
                String item = arrayAdapter.getItem(position);
                arrayAdapter.remove(item);
                arrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    private void removeFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = AppGlobals.getDataDirectory("CallRec")+ "/" + parent.getItemAtPosition(position) ;
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            File file = new File(path);
            intent.setPackage("com.google.android.music");
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
            System.out.println(path);
            startActivity(intent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
