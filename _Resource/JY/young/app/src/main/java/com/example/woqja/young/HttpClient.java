package com.example.woqja.young;


import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public final class HttpClient {

    /*
    * 여기에 원하는 API 추가 뭐 요청할건지
    * */
    public static SessionRequest newJoinRequest(String userId,
                                                String userName,
                                                String userType,
                                                String sessionKey,
                                                Response.Listener<JSONObject> listener,
                                                Response.ErrorListener errorListener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody = requestBody
                    .put("userId", userId)
                    .put("userName", userName)
                    .put("userType", userType)
                    .put("sessionKey", sessionKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return SessionRequest.newPostRequest(C.Url.JOIN, requestBody, listener, errorListener);
    }

    public static SessionRequest newRestaurantsRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        return SessionRequest.newGetRequest(C.Url.RESTAURANTS, listener, errorListener);
    }

}
