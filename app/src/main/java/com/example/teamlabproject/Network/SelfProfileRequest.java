package com.example.teamlabproject.network;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamlabproject.R;
import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.events.ResponseMessageEvent;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;
import com.example.teamlabproject.utils.Gender;

import org.json.JSONException;
import org.json.JSONObject;


public class SelfProfileRequest extends RequestAfterAuthentication {

    public static final String TAG = "self-profile-request";

    private static final String PROFILE_URL = "PEOPLE/@SELF";

    @Override
    public void parseResponse(JSONObject response) {
        try{
            JSONObject object = response.getJSONObject("response");
            String displayName = object.getString("displayName");
            String sex = object.getString("sex");
            TeamlabApplication.getInstance().getUserSettings().setGender(Gender.fromString(sex));
            TeamlabApplication.getInstance().getUserSettings().setTitle(displayName);
            AppLog.d("Self profile request done, started topicIDdRequest");
            new TopicIdRequest();
        } catch (JSONException e){
            AppLog.e("Error in parse json in self profile response " + e.getMessage());
            sendError();
        }
    }

    @Override
    public void sendError() {
        AppLog.d("Self profile request error");
        Events.postOnUI(new ResponseMessageEvent(TeamlabApplication.getInstance().getString(R.string.profile_error)));
    }

    public SelfProfileRequest() {

        JsonObjectRequest request = createRequest(Request.Method.GET, PROFILE_URL, null);

        AppLog.d("Self Profile request url is: " + request.getUrl());

        addRequest(request, TAG);
    }
}
