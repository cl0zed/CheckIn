package com.example.teamlabproject.environment;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import com.example.teamlabproject.R;
import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.screens.MainActivity;

public class TeamLabNotificationManager {

	@TargetApi(20)
	public Notification createConnectedNotification(){
		Context context = TeamlabApplication.getInstance().getApplicationContext();
		String comeToWork = context.getString(R.string.toWork);
		String goFromDinner = context.getString(R.string.fromDinner);
		String title = context.getString(R.string.connectedWIFI);

		return createNotificationWithActions(context, title, comeToWork, goFromDinner);
	}

	public Notification createDisconnectedNotification(){
		Context context = TeamlabApplication.getInstance().getApplicationContext();
		String toDinner = context.getString(R.string.toDinner);
		String toHome = context.getString(R.string.toHome);
		String title = context.getString(R.string.disconnectedWIFI);

		return createNotificationWithActions(context, title,  toHome, toDinner);
	}

	@TargetApi(20)
	private Notification createNotificationWithActions(Context context, String title, String firstActionName, String secondActionName){

		PendingIntent firstActionIntent = createActionSendPendingIntent(0, context, firstActionName);
		Notification.Action.Builder sendFirstActionName =
				new Notification.Action.Builder(R.mipmap.icon_notification_action_send, firstActionName, firstActionIntent);

		PendingIntent secondActonIntent = createActionSendPendingIntent(1, context, secondActionName);
		Notification.Action.Builder sendSecondActionName =
				new Notification.Action.Builder(R.mipmap.icon_notification_action_send, secondActionName, secondActonIntent);

		PendingIntent activityIntent = createActivityPendingIntent(context);

		Notification.Builder builder = new Notification.Builder(context)
				.setSmallIcon(R.mipmap.icon_notification_main)
				.setContentTitle(title)
				.setContentText(context.getString(R.string.message_for_coming))
				.setContentIntent(activityIntent)
				.setLights(Color.WHITE, 1000, 1000)
				.setAutoCancel(true)
				.setSound(Uri.parse("android.resource://com.example.teamlabproject/" + R.raw.notification_sound))
				.setVibrate(new long[] {500, 500, 500})
				.addAction(sendFirstActionName.build())
				.addAction(sendSecondActionName.build());

		return builder.build();
	}


	private PendingIntent createActionSendPendingIntent(int requestCode, Context context, String extraMessage){
		Intent intent = new Intent(context, NotificationService.class);
		intent.putExtra(NotificationService.MESSAGE_EXTRA, extraMessage);
		return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private PendingIntent createActivityPendingIntent(Context context){
		Intent intent = new Intent(context, MainActivity.class);
		return PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_ONE_SHOT);
	}

}
