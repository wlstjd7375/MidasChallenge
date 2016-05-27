package com.example.woqja.young;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
* Json 응답온거 파싱 ~
* */

public final class JsonResponse {
    private JSONObject mObject;

    public JsonResponse(JSONObject object) {
        this.mObject = object;
    }

    public static JsonResponse newResponse(JSONObject object) {
        return new JsonResponse(object);
    }

    public boolean isSucceed() {
        int errorCode = -1;
        try {
            errorCode = mObject.getInt("errorCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return errorCode == 0;
    }


    public String getErrorMessage() {
        String message = "";
        try {
            message = mObject.getString("errorMessage");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    public String getString(String key) {
        try {
            return mObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public int getInt(String key) {
        try {
            return mObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean getBoolean(String key) {
        try {
            return mObject.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public JsonResponse[] getJsonArray(String key) {
        JsonResponse[] jsonResponses = new JsonResponse[0];

        try {
            JSONArray jsonArray = mObject.getJSONArray(key);
            int length = jsonArray.length();
            jsonResponses = new JsonResponse[length];

            for (int i = 0; i < length; ++i) {
                jsonResponses[i] = new JsonResponse(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResponses;
    }

    public List<String> getStringArray(String key) {
        List<String> values = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = mObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); ++i) {
                values.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    public JsonResponse getJsonObject(String key) {
        try {
            return new JsonResponse(mObject.getJSONObject(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
