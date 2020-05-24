package com.antonageev.popularlibs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    String bodyText;

    public CustomDialogFragment(String bodyText) {
        this.bodyText = bodyText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        ((TextView)view.findViewById(R.id.body)).setText(bodyText);
        view.findViewById(R.id.closeBtn).setOnClickListener(v -> dismiss());

        return view;
    }
}
