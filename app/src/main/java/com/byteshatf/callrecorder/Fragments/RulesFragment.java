package com.byteshatf.callrecorder.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.R;
import com.byteshatf.callrecorder.contactpicker.ContactsPicker;

public class RulesFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    private View baseView;
    private ImageButton imageButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.rules_fragment, container, false);
        imageButton = (ImageButton) baseView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
        return baseView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                Intent intent = new Intent(getActivity(), ContactsPicker.class);
                startActivityForResult(intent, AppGlobals.REQUEST_CODE);
                break;
        }

    }
}
