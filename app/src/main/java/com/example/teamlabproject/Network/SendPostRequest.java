package com.example.teamlabproject.network;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamlabproject.R;
import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.events.ResponseMessageEvent;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SendPostRequest extends RequestAfterAuthentication{

    private static final String TAG = "send-post-tag";

    private static final String URL = "COMMUNITY/FORUM/TOPIC/";

    @Override
    public void parseResponse(JSONObject response) {
        Events.postOnUI(new ResponseMessageEvent(TeamlabApplication.getInstance().getString(R.string.message_send)));
    }

    @Override
    public void sendError(){
        Events.postOnUI(new ResponseMessageEvent(TeamlabApplication.getInstance().getString(R.string.send_post_error)));
    }

    public SendPostRequest(String message){
        String topicId = String.valueOf(TeamlabApplication.getInstance().getUserSettings().getId());
        //String topicId = String.valueOf(844);
        String resultURL = "";
        try {
            resultURL = URL + URLEncoder.encode(topicId, "UTF-8");
            AppLog.d("request url is: " + resultURL);
        }catch (UnsupportedEncodingException e) {
            AppLog.e("Can't create response");
        }
        JsonObjectRequest jsonObjectRequest = createRequest(Request.Method.POST, resultURL, createParams(message));
        addRequest(jsonObjectRequest, TAG);
    }

    private Map<String, String> createParams(String message){
        Map<String, String> params = new HashMap<>();

        params.put("subject", TeamlabApplication.getInstance().getUserSettings().getTitle());
        params.put("content", message);

        return params;
    }
}
