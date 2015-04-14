package com.drl.lutz.lullabyapp.logic;


import android.content.Context;

import com.drl.lutz.lullabyapp.database.Location;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

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

    public void upload(final String urlString) throws Exception {

        AsyncHttpClient httpClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        try {
            params.put("soundfile", this.file);
        } catch(FileNotFoundException e) {
            throw e;
        }

        httpClient.post(this.context,urlString,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                //TODO: throw Exception
            }
        });
    }

}