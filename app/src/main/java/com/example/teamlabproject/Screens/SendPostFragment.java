package com.example.teamlabproject.screens;

import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.events.NfcReadEvent;
import com.example.teamlabproject.network.SendPostRequest;
import com.example.teamlabproject.R;
import com.example.teamlabproject.events.StartRequestEvent;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;
import com.example.teamlabproject.utils.Gender;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendPostFragment extends Fragment {

    public static final String TAG = "send_post";

    @Bind(R.id.send_go_home_message)
    protected Button goHome;

    @Bind(R.id.send_on_work_message)
    protected Button onWork;

    @Bind(R.id.send_dinner_message)
    protected Button onDinner;

    @Bind(R.id.logged_as_text_view)
    protected TextView loggedAs;


    private Gender userGender;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.send_post_layout, container, false);
        ButterKnife.bind(this, root);

        Resources resources = TeamlabApplication.getInstance().getResources();

        userGender = TeamlabApplication.getInstance().getUserSettings().getGender();

        loggedAs.setText(resources.getString(R.string.logged_as, TeamlabApplication.getInstance().getUserSettings().getTitle()));


        setTextForButtons();

        return root;
    }

    private void setTextForButtons(){
        switch (userGender){
            case MALE:
                onWork.setText("Пришёл");
                onDinner.setText("Ушёл на обед");
                goHome.setText("Ушёл домой");
                break;
            case FEMALE:
                onWork.setText("Пришла");
                onDinner.setText("Ушла на обед");
                goHome.setText("Ушла домой");
                break;
        }
    }

    @OnClick(R.id.send_on_work_message)
    public void sendOnWorkMessage() {
        createBackgroundRequest(onWork.getText().toString());
    }

    @OnClick(R.id.send_go_home_message)
    public void sendGoHomeMessage() {
        createBackgroundRequest(goHome.getText().toString());
    }

    @OnClick(R.id.send_dinner_message)
    public void sendDinnerMessage(){
        createBackgroundRequest(onDinner.getText().toString());
    }

    @Subscribe
    public void onNfc(NfcReadEvent event) {
        AppLog.d("Nfc Read Event in fragment");
        if (event.getDataString().equalsIgnoreCase("0499aea2c83182")) {
            new SendPostRequest(onWork.getText().toString());
        }
        if (event.getDataString().equalsIgnoreCase("34552759e48966")) {
            new SendPostRequest(onDinner.getText().toString());
        }
        if (event.getDataString().equalsIgnoreCase("34d7dcb9bcda76")) {
            new SendPostRequest(goHome.getText().toString());
        }
    }

    private void createBackgroundRequest(final String text) {
        new SendPostRequest(text);
        Events.postOnUI(new StartRequestEvent());
    }



    @Override
    public void onStart() {
        super.onStart();
        Events.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Events.unregisterListener(this);
    }
}
