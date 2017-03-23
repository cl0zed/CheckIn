package com.example.teamlabproject.environment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.teamlabproject.TeamlabApplication;

public class WifiReceiver extends WakefulBroadcastReceiver {

	private static final String[] wifiPointsName = {"\"STAND2\"", "\"STAND1\"", "\"APOLLO2\""};

    @Override
    public void onReceive(Context context, Intent intent) {
	    Bundle bundle = intent.getExtras();
	    Notification notification = null;
	    NetworkInfo info = (NetworkInfo) bundle.get("networkInfo");
	    final int topicID = TeamlabApplication.getInstance().getUserSettings().getId();
	    if (info != null && topicID != 0) {
		    switch (info.getDetailedState()) {
			    case CONNECTED:
					String wifiName = info.getExtraInfo();
				    if (isNeededWIFI(wifiName)){
					    notification = TeamlabApplication.getInstance().getNotificationManager().createConnectedNotification();
				    }
				    TeamlabApplication.getInstance().getUserSettings().setLastWifiName(wifiName);
				    break;
			    case DISCONNECTED:
				    String lastWifiName = TeamlabApplication.getInstance().getUserSettings().getLastWifiName();
				    if (isNeededWIFI(lastWifiName)){
					    notification = TeamlabApplication.getInstance().getNotificationManager().createDisconnectedNotification();
				    }
				    TeamlabApplication.getInstance().getUserSettings().setLastWifiName("");
				    break;
		    }
		    int notificationID = 11;
		    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		    if (notification != null) {
			    manager.notify(notificationID, notification);
		    }
	    }
    }

	private boolean isNeededWIFI(String wifiName){
		for (String wifiPointName : wifiPointsName){
			if (wifiPointName.equals(wifiName)){
				return true;
			}
		}
		return false;
	}
}
