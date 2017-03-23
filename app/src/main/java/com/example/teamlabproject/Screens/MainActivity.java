package com.example.teamlabproject.screens;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.teamlabproject.R;
import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.events.NfcReadEvent;
import com.example.teamlabproject.events.ResponseMessageEvent;
import com.example.teamlabproject.events.TokenChangedEvent;
import com.example.teamlabproject.events.StartRequestEvent;
import com.example.teamlabproject.network.SendPostRequest;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private PendingIntent mPendingIntent;

    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.progress_bar_layout)
    public ViewGroup ProgressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        } else {

            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

            try {
                ndef.addDataType("*/*");
            } catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("fail", e);
            }

            mFilters = new IntentFilter[] {
                    ndef,
            };
            mTechLists = new String[][] { new String[] { NfcA.class.getName() } };

            handleIntent(getIntent());
        }


        showNeededFragment();
    }

    public void handleIntent(Intent intent) {
        AppLog.d("handleIntent()");
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            AppLog.d("handleIntent(): tag is " + tagFromIntent.toString());

            byte[] data;

            try {
                data = tagFromIntent.getId();
                String dataString = byteArrayToHex(data);
                AppLog.d(dataString);
                Events.postOnUI(new NfcReadEvent(dataString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showNeededFragment(){

        final int topicID = TeamlabApplication.getInstance().getUserSettings().getId();
        if (topicID != 0){
            showSendPostFragment();
        } else {
            showRegistrationFragment();
        }
    }

    private void showSendPostFragment(){
        FragmentTransaction transaction = getFragmentTransaction(RegistrationFragment.TAG);
        transaction.add(R.id.container, new SendPostFragment(), SendPostFragment.TAG).commit();
    }

    private void showRegistrationFragment(){
        FragmentTransaction transaction = getFragmentTransaction(SendPostFragment.TAG);
        transaction.add(R.id.container, new RegistrationFragment(), RegistrationFragment.TAG).commit();
    }

    private FragmentTransaction getFragmentTransaction(String TAG){
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null){
            transaction = transaction.remove(fragment);
        }
        return transaction;
    }

    @Subscribe
    public void on(ResponseMessageEvent e){
        hideProgressBar();
        Toast.makeText(this, e.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void on(TokenChangedEvent e){
        hideProgressBar();
        showNeededFragment();
    }


    @Subscribe
    public void on(StartRequestEvent e){
        showProgressBar();
    }

    private void hideProgressBar() {
        ProgressLayout.setVisibility(View.GONE);
    }

    private void showProgressBar(){
        ProgressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Events.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Events.registerListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        handleIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
}
