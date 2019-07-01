package com.invent.inms.request;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.invent.inms.helper.Constants;

import camera.Camera;
import camera.ImageDetectionGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class NotifyDetectionModule extends AsyncTask{
    ManagedChannel channel;
    String host = "";

    public NotifyDetectionModule(String host){
        this.host = host;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            channel = ManagedChannelBuilder.forAddress(host, Constants.CAMERA_MODULE_PORT).usePlaintext().build();
            ImageDetectionGrpc.ImageDetectionBlockingStub imageDetectionBlockingStub = ImageDetectionGrpc.newBlockingStub(channel);
            Camera.UrlResponse urlResponse = imageDetectionBlockingStub.sendUrl(Camera.UrlRequest.newBuilder().setImageUrl("upload").build());
            Log.i("NotifyDetectionModule()", urlResponse.toString());
        }
        catch (Exception e) {
            Log.e("NotifyDetectionModule()", e.getMessage() );
        }
        return null;
    }
}
