package com.example.teamlabproject.network;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class TeamRequestQueue {

    private RequestQueue mRequestQueue;

    public TeamRequestQueue(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public <T> void addToRequsetQueue(Request<T> request, String tag){
        request.setTag(tag);
        mRequestQueue.add(request);
    }

    public void cancelPendingRequeset(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
