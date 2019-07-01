package com.invent.inms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.invent.inms.helper.Constants;
import com.invent.inms.helper.CustomAdapter;
import com.invent.inms.helper.Ingredients;
import com.invent.inms.helper.IngredientsData;
import com.invent.inms.helper.SendImage;
import com.invent.inms.request.AddIngredients;
import com.invent.inms.request.HealthOption;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import inms.invent.com.i_invent_inms.R;

public class MainActivity extends Activity{

    String[] checkedText;
    TypedArray imageId;
    List<IngredientsData> ingredientsData;
    ListView ingredientsList;
    CheckedTextView simpleCheckedTextView ;
    Button process;
    Bundle bundle;

    Set<String> ingredientsSet;
    private ProgressDialog progressDialog;
    ImageView scanImage;
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};
    String mCurrentPhotoPath;
    private File photoFile;
    private List<String> selectedIngredients = new ArrayList<>();
    String host = "";
    int port = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_VALUES, MODE_PRIVATE );
        host = sp.getString(Constants.HOST, null);
        port = sp.getInt(Constants.PORT,0);
        Log.i("host:port", host+port);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProgressDialogWithTitle("Fetching ingredients...");
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button addIngredients = findViewById(R.id.add);
        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddIngredients.class);
                Bundle addIngredientBundle = new Bundle();
                addIngredientBundle.putStringArray("ingredientsList", ingredientsSet.toArray(new String[ingredientsSet.size()]) );
                addIngredientBundle.putString("host", host);
                addIngredientBundle.putInt("port", port);
                intent.putExtras(addIngredientBundle);
                startActivity(intent);
            }
        });
        scanImage = findViewById(R.id.scan);
        scanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraAndCapture();
            }
        });
       new loadViewParallelly().execute();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void launchCameraAndCapture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            photoFile = createImageFile();
            Log.i("Camera Activity", "host: "+host);
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Bundle sendPictureBundle = new Bundle();
                sendPictureBundle.putString("host", host);
                sendPictureBundle.putInt("port", port);
                Log.i("CLasss", "launchCameraAndCapture:host "+host+port);
                startActivityForResult(takePictureIntent, Constants.CAMERA_REQUEST,sendPictureBundle);
            }
        }
    }

    private void setUpView() {
        if(ingredientsSet.size()!=0) {
            checkedText = ingredientsSet.toArray(new String[ingredientsSet.size()]);
            int[] a = {R.drawable.onion, R.drawable.bellpepper, R.drawable.carrot, R.drawable.egg, R.drawable.tomato};
            imageId = obtainStyledAttributes(a);
            for (int i = 0; i < checkedText.length; i++) {
                IngredientsData item = new IngredientsData(getResourseId(this,checkedText[i],"drawable",getPackageName()),
                        checkedText[i]);
                ingredientsData.add(item);
            }
            bundle = new Bundle();
            ingredientsList = findViewById(R.id.ingredients_list);
            CustomAdapter adapter = new CustomAdapter(this, ingredientsData);
            ingredientsList.setAdapter(adapter);
            ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    simpleCheckedTextView = view.findViewById(R.id.simpleCheckedTextView);
                    simpleCheckedTextView.setText(checkedText[position]);
                    if (simpleCheckedTextView.isChecked()) {
                        simpleCheckedTextView.setCheckMarkDrawable(0);
                        simpleCheckedTextView.setChecked(false);
                        MainActivity.this.selectedIngredients.remove((String) simpleCheckedTextView.getText());
                    } else {
                        simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check_mark);
                        simpleCheckedTextView.setChecked(true);
                        MainActivity.this.selectedIngredients.add((String) simpleCheckedTextView.getText());
                    }
                }
            });

            process = findViewById(R.id.proceed);
            process.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(MainActivity.this.selectedIngredients.size()!=0) {
                        processHealthOptions();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please select at least One ingredient!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "No Ingredients Found!!", Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressDialogWithTitle(String substring) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(substring);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_VALUES, MODE_PRIVATE );
        String host = sp.getString(Constants.HOST, null);
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == RESULT_OK) {
           new SendImage(photoFile, host).execute();
        }
    }

    private File createImageFile(){
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, "capture.jpg");
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    private void processHealthOptions() {
        Intent intent = new Intent(this,HealthOption.class);
        bundle.putStringArray("ingredientsList", selectedIngredients.toArray(new String[selectedIngredients.size()]) );
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        int id = 0;
        try {
            id =  context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            Log.e("getRecourseId()", e.getMessage());
            return R.drawable.default_image;
        }
        if (id == 0) {
            return R.drawable.default_image;
        }
        return id;
    }

    private class loadViewParallelly extends AsyncTask{

        SharedPreferences sp = getSharedPreferences(Constants.SHARED_VALUES, MODE_PRIVATE  );
        String host = sp.getString(Constants.HOST, null);
        int port = sp.getInt(Constants.PORT, 0);

        @Override
        protected Object doInBackground(Object[] objects) {
            ingredientsData = new ArrayList<>();
            ingredientsSet = new HashSet<>(Ingredients.getIngredientsResponse(host,port));
            hideProgressDialogWithTitle();
            return ingredientsSet;
        }

        @Override
        protected void onPostExecute(Object o) {
            setUpView();
        }
    }
}