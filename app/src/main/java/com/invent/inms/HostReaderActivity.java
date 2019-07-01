package com.invent.inms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.invent.inms.helper.Constants;

import inms.invent.com.i_invent_inms.R;

public class HostReaderActivity extends AppCompatActivity {

    String host = "";
    int port = 0;
    private Button proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_resourse);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("hosts").document("host");
        final CollectionReference Hosts = db.collection("hosts");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                host = documentSnapshot.getString("host_ip");
                port = Integer.parseInt(documentSnapshot.getString("port"));
                Log.i("firebase:read hosts", host+":"+port);
                proceedToMainActivity();
            }
        });
    }

    private void proceedToMainActivity() {

            SharedPreferences sp = getSharedPreferences(Constants.SHARED_VALUES, MODE_PRIVATE );
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constants.HOST, host);
            editor.putInt(Constants.PORT, port);
            editor.apply();
            Intent intent=new Intent(HostReaderActivity.this,MainActivity.class);
            startActivity(intent);
    }
}