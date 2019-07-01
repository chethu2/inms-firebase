package com.invent.inms.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.invent.inms.helper.Constants;
import com.invent.inms.helper.RecipeParser;
import java.util.Arrays;
import java.util.List;
import inms.BackendRequestsGrpc;
import inms.Inms;
import inms.invent.com.i_invent_inms.R;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RecipeList extends Activity{

    private Bundle bundle;

    private String[] ingredients;
    private static String[] healthLables;
    private List<String> recipesNames;
    Inms.GetRecipesResponse getRecipesResponse;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_menu);
        showProgressDialogWithTitle("Fetching Recipes...");
        bundle = getIntent().getExtras();
        healthLables = bundle.getStringArray("heathOptionSelected");
        ingredients  = bundle.getStringArray("ingredientsList");
        new loadViewParallelly().execute();
    }

    private void setUpRecipeListView() {
        if(recipesNames.size()!=0) {
            hideProgressDialogWithTitle();

            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.recipe_list, recipesNames);
            ListView listView = (ListView) findViewById(R.id.recipe_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    processUponGetRecipe(getRecipesResponse,recipesNames.get(position));
                }
            });
        }
        else {
            hideProgressDialogWithTitle();
            Toast.makeText(this, "Recipes Not Found !!", Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressDialogWithTitle(String substring) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(substring); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    // Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    private void processUponGetRecipe(Inms.GetRecipesResponse getRecipesResponse, String recipeName) {
        Intent intent = new Intent(RecipeList.this,RecipeDescription.class);
        Inms.Recipes recipeObject = RecipeParser.getRecipeObject(getRecipesResponse,recipeName);
        bundle.putString("recipeImageUrl",recipeObject.getImageUrl());
        bundle.putString("recipeName",recipeObject.getRecipeName());
        bundle.putString("preparationTime",recipeObject.getPreparationTime());
        bundle.putString("preparationUrl",recipeObject.getPreparationUrl());
        bundle.putDouble("calorieCount",recipeObject.getCalorieCount());
        bundle.putStringArray("ingredients", recipeObject.getIngredientsList().toArray(new String[recipeObject.getIngredientsList().size()]));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(recipesNames.size()!=0){
            recipesNames.clear();
        }
        finish();
    }

    private Inms.GetRecipesResponse GetRecipesResponse(String[] ingredients, String[] healthLables) {

        ManagedChannel channel;
        Inms.GetRecipesResponse getRecipesResponse = null;
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_VALUES, MODE_PRIVATE  );
        String host = sp.getString(Constants.HOST, null);
        String port = sp.getString(Constants.PORT, null);

        try{
        channel = ManagedChannelBuilder.forAddress(host, Integer.parseInt(port)).usePlaintext().build();
        BackendRequestsGrpc.BackendRequestsBlockingStub backendRequestsBlockingStub = BackendRequestsGrpc.newBlockingStub(channel);
        getRecipesResponse = backendRequestsBlockingStub.getRecipes(getRecipeRequestBuilder(ingredients,healthLables));
        return getRecipesResponse;}
        catch (Exception e){
            Log.e("RecipeList()", e.getMessage());
            return getRecipesResponse;
        }
    }

    private Inms.GetRecipesRequest getRecipeRequestBuilder(String[] ingredients, String[] healthLables) {
        return Inms.GetRecipesRequest.newBuilder().addAllHealthLabels(Arrays.asList(healthLables))
                .addAllIngredients(Arrays.asList(ingredients)).setContainerId("inms").build();
    }

    private class loadViewParallelly extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            getRecipesResponse = GetRecipesResponse(ingredients, healthLables);
            recipesNames = RecipeParser.getRecipeName(getRecipesResponse);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            setUpRecipeListView();
        }
    }

}
