package com.example.teamlabproject.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamlabproject.R;
import com.example.teamlabproject.TeamlabApplication;
import com.example.teamlabproject.events.ResponseMessageEvent;
import com.example.teamlabproject.utils.AppLog;
import com.example.teamlabproject.utils.Events;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationRequest extends BaseRequest {

    public static final String TAG = "Auth-request";

    final String AUTHENTICATION_URL = "authentication.json";

    @Override
    public void parseResponse(JSONObject response) {
        try {
            JSONObject responseData = response.getJSONObject("response");
            String token = responseData.getString("token");
            AppLog.d(token);
            TeamlabApplication.getInstance().getUserSettings().setToken(token);
            AppLog.d("Authentication request done, started selfProfileRequest");
            new SelfProfileRequest();
        } catch (JSONException e){
            AppLog.e("Failed to parse authentication response" + e.getMessage());
            sendError();
        }
    }

    @Override
    public void sendError() {
        AppLog.d("Authentication request error");
        Events.postOnUI(new ResponseMessageEvent(TeamlabApplication.getInstance().getString(R.string.authentication_error)));
    }

    public AuthenticationRequest(final String userName, final String password){
        try {
            Map<String, String> params = getParams(userName, password);
            JsonObjectRequest objectRequest = createRequest(Request.Method.POST, AUTHENTICATION_URL, params);
            addRequest(objectRequest, TAG);
        } catch (AuthFailureError e){
            AppLog.e("Fail to create request " + e.getMessage());
        }

    }

    protected Map<String, String> getParams(final String userName, final String password) throws AuthFailureError {
        Map<String, String> params = new HashMap<>();

        params.put("userName", userName);
        params.put("password", password);

        return params;
    }
}
