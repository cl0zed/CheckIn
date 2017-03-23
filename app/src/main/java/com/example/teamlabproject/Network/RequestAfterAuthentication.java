package com.example.teamlabproject.network;


import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamlabproject.TeamlabApplication;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestAfterAuthentication extends BaseRequest {

    @Override
    public JsonObjectRequest createRequest(int method, String apiPath, @Nullable Map<String, String> params) {
        JSONObject jsonParams;
        if (params == null){
            jsonParams = new JSONObject();
        } else {
            jsonParams = new JSONObject(params);
        }
        String resultURL = BASE_URL + apiPath;
        JsonObjectRequest request = new JsonObjectRequest(method, resultURL, jsonParams, this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createHeader();
            }
        };
        return request;
    }

    private Map<String, String> createHeader(){
        Map<String, String> header = new HashMap<>();
        String token = TeamlabApplication.getInstance().getUserSettings().getToken();
        header.put("Authorization", token);

        return header;
    }
}
