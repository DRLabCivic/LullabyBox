package com.drl.lutz.lullabyapp.utils;


import android.content.Context;
import android.util.Log;

import com.drl.lutz.lullabyapp.database.Location;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by lutz on 13/04/15.
 */
public class FileUploader {

    private File file;
    private Location location;

    private Context context;

    public FileUploader(Context context, File file, Location location) {
        this.file = file;
        this.location = location;
        this.context = context;
    }

    public void upload(final String urlString, AsyncHttpResponseHandler handler) throws FileNotFoundException {

        AsyncHttpClient httpClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        try {
            params.put("file", this.file);
            params.put("location", this.location.toJsonString());
            //params.put("location","test");
        } catch(FileNotFoundException e) {
            throw e;
        }

        Log.d("HTTP", "params:" + this.location.toJsonString());

        httpClient.post(this.context,urlString,params,handler);
    }

    public void deleteFile() {
        this.file.delete();
    }

}