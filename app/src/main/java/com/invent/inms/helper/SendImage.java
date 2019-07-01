package com.invent.inms.helper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.invent.inms.request.NotifyDetectionModule;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SendImage extends AsyncTask {

    File currentImagePath ;
    HttpResponse response = null;
    String host = "";

    public SendImage(File currentImagePath, String host){
        this.currentImagePath = currentImagePath;
        this.host = host;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(
                "http://"+host+":50053/receiveImages");
        ContentBody cbFile = new FileBody(currentImagePath);
        MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("upload",  cbFile);
        try {
                reqEntity.addPart("Content-type", new StringBody("multipart/form-data"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(reqEntity);
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            Log.e("SendImage()", "Error :", e);
            e.printStackTrace();
        }
        httpClient.getConnectionManager().shutdown();
     return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(response.getStatusLine().getStatusCode()== 200){
            new NotifyDetectionModule(host).execute();
        }
        else { new NotifyDetectionModule(host).execute();}
    }
}