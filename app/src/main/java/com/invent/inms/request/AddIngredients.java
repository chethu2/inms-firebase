package com.invent.inms.request;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.invent.inms.MainActivity;
import com.invent.inms.helper.Constants;

import java.util.ArrayList;
import java.util.Arrays;

import inms.DetectionGrpc;
import inms.Inms;
import inms.invent.com.i_invent_inms.R;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class AddIngredients extends Activity {
    Button addMoreIngredients;
    Button proceedAddedIngredientsButton;
    private ArrayList<String> ingredients;
    EditText addIngredient ;
    private String text = Constants.EMPTY_STRING;
    String host = "";
    int port = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredients);
        Bundle bundle = getIntent().getExtras();
        ingredients = new ArrayList<>(Arrays.asList(bundle.getStringArray("ingredientsList")));
        host = bundle.getString("host");
        port = bundle.getInt("port");

        addIngredient = findViewById(R.id.add_ingredient);
        addMoreIngredients = findViewById(R.id.add_more);
        proceedAddedIngredientsButton = findViewById(R.id.proceed_add);
        addMoreIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredientsToTheExistingArray();
            }
        });
        proceedAddedIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = addIngredient.getText().toString();
                if(text.length() != 0 && !isIngredientAlreadyExists(text)) {
                    ingredients.add(text);
                }
                new UpdateIngredients().execute();
            }
        });
    }

    private boolean isIngredientAlreadyExists(String text) {
        for(String ingredient : ingredients) {
            if(ingredient.contains(text) || ingredient.startsWith(text)) {
                return true;
            }
        }
        return false;
    }

    private class UpdateIngredients extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            inms.DetectionGrpc.DetectionBlockingStub detectionBlockingStub = DetectionGrpc.newBlockingStub(channel);
            try{
                Inms.DetectedIngredientsResponse detectedIngredientsResponse = detectionBlockingStub.updateDetectedIngredients(Inms.DetectedIngredientsRequest.newBuilder().setContainerId(Constants.CONTAINER_ID).addAllIngredients(ingredients).build());
                Log.i("AddIngredients()",detectedIngredientsResponse.toString());
                directToMainActivity();
            }catch (Exception e){
                Log.e("AddIngredients() :",e.getMessage());
            }
            return null;
        }
    }
    private void directToMainActivity() {
        Intent intent = new Intent(AddIngredients.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void addIngredientsToTheExistingArray() {
        text = addIngredient.getText().toString();
        if(text.length() != 0 && !isIngredientAlreadyExists(text)){
            ingredients.add(text);
            addIngredient.setText(Constants.EMPTY_STRING);
            text = Constants.EMPTY_STRING;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        directToMainActivity();
    }
}