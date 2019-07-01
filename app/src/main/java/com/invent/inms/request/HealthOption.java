package com.invent.inms.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.invent.inms.helper.HealthOptionsAdapter;
import com.invent.inms.MainActivity;

import java.util.ArrayList;
import java.util.List;

import inms.invent.com.i_invent_inms.R;

public class HealthOption extends Activity {

    String[] healthOptions;
    private Button getRecipesButton;
    private static Bundle bundle;
    private static String[] ingredients;
    static List <String> healthOptionsSelected  = new ArrayList<>();
    private CheckedTextView healthOptionCheckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heath_options_activity);

        healthOptions =  new String[]{"balanced","high-protein","low-fat","low-carb","alcohol-free","vegan",
                "vegetarian","sugar-conscious","tree-nut-free","peanut-free"} ;

        ListView listView = findViewById(R.id.healthOptionList);
        HealthOptionsAdapter customAdapter = new HealthOptionsAdapter(getApplicationContext(), healthOptions);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                healthOptionCheckList =  view.findViewById(R.id.HealthOptionCheckedText);
                healthOptionCheckList.setText(healthOptions[position]);
                if (!healthOptionCheckList.isChecked()) {
                    healthOptionCheckList.setCheckMarkDrawable(R.drawable.check_mark);
                    healthOptionCheckList.setChecked(true);
                    if(!healthOptions[position].contains("balance")) {
                        healthOptionsSelected.add(healthOptions[position]);
                    }
                }
                else {
                    healthOptionCheckList.setCheckMarkDrawable(0);
                    healthOptionCheckList.setChecked(false);
                    healthOptionsSelected.remove(healthOptions[position]);
                }
            }
        });

        getRecipesButton = findViewById(R.id.healthOptionButton);
        getRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processUponHealthOptions();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(healthOptionsSelected.size()!=0){
            healthOptionsSelected.clear();
        }
        Intent intent=new Intent(HealthOption.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void processUponHealthOptions() {
        Intent intent = new Intent(this,RecipeList.class);
        bundle = getIntent().getExtras();
        ingredients = bundle.getStringArray("ingredientsList");
        bundle.putStringArray("ingredientsList", ingredients );
        bundle.putStringArray("heathOptionSelected",healthOptionsSelected.toArray(new String[healthOptionsSelected.size()]) );
        intent.putExtras(bundle);
        startActivity(intent);
    }
}