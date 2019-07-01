package com.invent.inms.helper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import inms.BackendRequestsGrpc;
import inms.Inms;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Ingredients {
    public static List<String> getIngredientsResponse(String host, int port){
            ManagedChannel channel;
            List<String> ingredients = new ArrayList<>();
           try {
                Inms.GetIngredientsRequest getIngredientsRequest;
                channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                BackendRequestsGrpc.BackendRequestsBlockingStub backendRequestsBlockingStub = BackendRequestsGrpc.newBlockingStub(channel);
                getIngredientsRequest = Inms.GetIngredientsRequest.newBuilder().setContainerId("inms").build();
                List<String> response = backendRequestsBlockingStub.getIngredients(getIngredientsRequest).getIngredientsList();
                return response;
           }
           catch (Exception e){
                Log.i("Exception", "getIngredientsResponse: "+ e.toString()+e.getMessage());
                return ingredients;
           }
    }
}
