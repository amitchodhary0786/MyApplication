package com.example.myapplication.utils;

import org.json.JSONException;

public interface WebServiceListener {
    public void onResponse(String response) throws JSONException;
    public void onFailer(String failure) throws JSONException;
}
