package com.example.teamlabproject.environment;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.network.SendPostRequest;
import com.example.teamlabproject.utils.AppLog;

public class NotificationService extends Service {

	public final static String MESSAGE_EXTRA = "message_to_send";

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		AppLog.d("Extra is: " + intent.getStringExtra(MESSAGE_EXTRA));
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String postMessage = intent.getStringExtra(MESSAGE_EXTRA);
			new SendPostRequest(postMessage);
			NotificationManager manager = (NotificationManager) TeamlabApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(11);
		}
		return super.onStartCommand(intent, flags, startId);

	}
}
