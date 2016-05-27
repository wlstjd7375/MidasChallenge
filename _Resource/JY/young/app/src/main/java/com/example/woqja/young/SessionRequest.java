package com.example.woqja.young;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionRequest extends JsonObjectRequest {
    /*
    * Get 또는 Post 방식 기본 요청 형태
    * */

    public SessionRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public static SessionRequest newGetRequest(String url,
                                               Response.Listener<JSONObject> listener,
                                               Response.ErrorListener errorListener) {
        return new SessionRequest(Method.GET, url, null, listener, errorListener);
    }

    public static SessionRequest newPostRequest(String url,
                                                Response.Listener<JSONObject> listener,
                                                Response.ErrorListener errorListener) {
        return new SessionRequest(Method.POST, url, null, listener, errorListener);
    }


    public static SessionRequest newPostRequest(String url,
                                                JSONObject bodyJsonObject,
                                                Response.Listener<JSONObject> listener,
                                                Response.ErrorListener errorListener) {
        return new SessionRequest(Method.POST, url, bodyJsonObject, listener, errorListener);
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        AppController.getInstance().checkSessionCookie(response.headers);

        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.EMPTY_MAP)) {
            headers = new HashMap<>();
        }

        AppController.getInstance().addSessionCookie(headers);
        return headers;
    }
}
