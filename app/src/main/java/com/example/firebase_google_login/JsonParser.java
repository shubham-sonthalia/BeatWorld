package com.example.firebase_google_login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject object){
        HashMap<String, String> dataList = new HashMap<>();
        try {
            String name = object.getString("name");
            String latitude = object.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = object.getJSONObject("geometry").getJSONObject("location").getString("lng");
            dataList.put("name", name);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    private List<HashMap<String, String>> parseJSONArray(JSONArray jsonArray){
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for(int i = 0;i < jsonArray.length();i++){
            try {
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }
    public List<HashMap<String, String>> parseResult(JSONObject jsonObject){
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseJSONArray(jsonArray);
    }
}
