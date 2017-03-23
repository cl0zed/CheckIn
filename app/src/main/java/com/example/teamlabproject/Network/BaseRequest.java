package com.example.teamlabproject.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamlabproject.R;
import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.events.ResponseMessageEvent;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BaseRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    protected static final String BASE_URL = "http://team.apollophone.ru/API/2.0/";

    @Override
    public void onResponse(JSONObject response) {
        try {
            int responseCode = response.getInt("statusCode");
            if (responseCode == 200 || responseCode == 201){
                parseResponse(response);
            } else {
                sendError();
            }
        } catch (JSONException e){
            AppLog.e("Failed to parse authentication response" + e.getMessage());
            sendError();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AppLog.e("Error in base request " + error.getMessage());
        sendError();
    }

    public void sendError() {
        Events.postOnUI(new ResponseMessageEvent(TeamlabApplication.getInstance().getString(R.string.base_error_text)));
    }

    public void parseResponse(JSONObject response){

    }

    public JsonObjectRequest createRequest(int method, String apiPath, @Nullable Map<String, String> params){
        JSONObject jsonParams = new JSONObject(params);
        String resultURL = BASE_URL + apiPath;
        return new JsonObjectRequest(method, resultURL, jsonParams, this, this);
    }

    protected void addRequest(JsonObjectRequest request, String TAG){
        TeamlabApplication.getInstance().getRequestQueue().addToRequsetQueue(request, TAG);
    }
}
