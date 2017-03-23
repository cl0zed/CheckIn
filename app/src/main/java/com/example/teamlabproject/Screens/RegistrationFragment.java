package com.example.teamlabproject.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.teamlabproject.network.AuthenticationRequest;
import com.example.teamlabproject.R;
import com.example.teamlabproject.events.StartRequestEvent;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment extends Fragment {

    public static final String TAG = "registration";

    @Bind(R.id.enter_username_field)
    EditText mUsernameField;

    @Bind(R.id.enter_password_field)
    EditText mPasswordField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.registration_layout, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.send_auth_request)
    public void sendRequest(){
        new AuthenticationRequest(mUsernameField.getText().toString(), mPasswordField.getText().toString());
        Events.postOnUI(new StartRequestEvent());
    }

}
