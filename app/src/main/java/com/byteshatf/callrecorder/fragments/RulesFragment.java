package com.byteshatf.callrecorder.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.byteshatf.callrecorder.R;

public class RulesFragment extends android.support.v4.app.Fragment {

    private View baseView;
    private Button ruleActivityButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.rules_fragment, container, false);
        return baseView;
    }
}
