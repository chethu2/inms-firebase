package com.invent.inms.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.invent.inms.helper.WebViewClass;
import com.squareup.picasso.Picasso;

import inms.invent.com.i_invent_inms.R;

public class RecipeDescription extends Activity{

    public static String imageUrl;
    public static double calorieCount;
    public static String recipeName;
    public static String preparationTime;
    public static String [] ingredientsArray;
    Bundle bundle;
    TextView calorieCountTextView;
    TextView ingredientsTextView;
    ListView ingredientsListView;
    TextView recipeNameTextView;
    TextView preparationTimeTextView;
    ImageView recipeImageView;
    Button detailedInfo;
    private String preparationUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseValues();
        setContentView(R.layout.recipe_description);
        calorieCountTextView = findViewById(R.id.calorie_count);
        ingredientsTextView = findViewById(R.id.recipe_ingredients);
        ingredientsListView = findViewById(R.id.recipe_ingredients_list);
        recipeNameTextView = findViewById(R.id.recipe_description_name);
        preparationTimeTextView = findViewById(R.id.preparation_time);
        recipeImageView = findViewById(R.id.recipe_image);
        recipeNameTextView.setText(recipeName);
        Picasso.with(this.getApplicationContext()).load(imageUrl).into(recipeImageView);
        preparationTimeTextView.setText("Preparation Time :"+preparationTime+" Mins");
        calorieCountTextView.setText("Calorie count :"+calorieCount+" Cal");
        ingredientsTextView.setText("Ingredients Required :");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.recipe_list, ingredientsArray);
        ingredientsListView.setAdapter(adapter);

        detailedInfo = findViewById(R.id.get_detailed_info);
        detailedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecipeProcess();
            }
        });
    }

    private void loadRecipeProcess() {
        Intent intent = new Intent(RecipeDescription.this,WebViewClass.class);
        bundle.putString("preparationUrl",preparationUrl);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void initialiseValues() {
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        imageUrl = bundle.getString("recipeImageUrl");
        calorieCount = bundle.getDouble("calorieCount");
        recipeName = bundle.getString("recipeName");
        preparationTime = bundle.getString("preparationTime");
        ingredientsArray = bundle.getStringArray("ingredients");
        preparationUrl= bundle.getString("preparationUrl");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
