package com.example.teamlabproject.network;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamlabproject.R;
import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.events.ResponseMessageEvent;
import com.example.teamlabproject.events.TokenChangedEvent;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TopicIdRequest extends RequestAfterAuthentication{

    public static final String TAG = "topic-id-request";

    private static final String URL = "COMMUNITY/FORUM/@SEARCH/";

    @Override
    public void parseResponse(JSONObject response) {
        try{
            JSONArray array = response.getJSONArray("response");
            for (int i = 0; i < array.length(); ++i) {
                JSONObject result = array.getJSONObject(i);
                String displayName = TeamlabApplication.getInstance().getUserSettings().getTitle();
                if (result.getString("title").equals(displayName)) {
                    int topicId = result.getInt("id");
                    TeamlabApplication.getInstance().getUserSettings().setId(topicId);
                    Events.postOnUI(new TokenChangedEvent());
                    return;
                }
            }
            sendError();
        } catch (JSONException e){
            AppLog.e("Error in parse topic id response " + e.getMessage());
            sendError();
        }
    }

    @Override
    public void sendError(){
        Events.postOnUI(new ResponseMessageEvent(TeamlabApplication.getInstance().getString(R.string.topic_error)));
    }
    public TopicIdRequest(){
        String resultURL = "";
        try{
            resultURL = URL + URLEncoder.encode(TeamlabApplication.getInstance().getUserSettings().getTitle(), "UTF-8").replace("+", "%20");

        } catch (UnsupportedEncodingException e){
            AppLog.e("Exception in encode");
        }

        JsonObjectRequest jsonObjectRequest = createRequest(Request.Method.GET, resultURL, null);
        jsonObjectRequest.setRetryPolicy(createDefaultPolicy());

        AppLog.d("Topic id request url is: " + jsonObjectRequest.getUrl());
        addRequest(jsonObjectRequest, TAG);
    }

    private DefaultRetryPolicy createDefaultPolicy(){
        int socketTimeout = 30000;
        return new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
