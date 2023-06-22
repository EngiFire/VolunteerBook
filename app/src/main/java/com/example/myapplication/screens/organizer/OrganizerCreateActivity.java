package com.example.myapplication.screens.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrganizerCreateActivity extends AppCompatActivity {

    private EditText edName, edData, edPlace, edDescription, edQuantity;
    private DatabaseReference mDataBase;
    private Spinner spDirection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_create);
        init();

        ArrayAdapter<?> adDirection =
                ArrayAdapter.createFromResource(this, R.array.direction,
                        android.R.layout.simple_spinner_item);
        adDirection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDirection.setAdapter(adDirection);
    }

    private void init(){
        edName = findViewById(R.id.edCrEventName);
        edPlace = findViewById(R.id.edCrEventPlace);
        edData = findViewById(R.id.edCrEventData);
        edDescription = findViewById(R.id.edCrEventDescription);
        edQuantity = findViewById(R.id.edCrEventQuantity);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        spDirection = findViewById(R.id.spDirection);
    }

    public void onClickCreateSave(View view){

        String direction = spDirection.getSelectedItem().toString();

        String name = edName.getText().toString();
        String place = edPlace.getText().toString();
        String data = edData.getText().toString();
        String description = edDescription.getText().toString();
        String strQuantity = edQuantity.getText().toString();
        int quantity = Integer.parseInt(strQuantity);

        String id = mDataBase.child("Event").push().getKey();
        String responsible = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Event newEvent = new Event(id, name, direction, data, responsible, place, description, quantity, 0);

        mDataBase.child("Event").child(id).setValue(newEvent);

        Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, OrganizerActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickCreateExit(View view){
        Intent intent = new Intent(this, OrganizerActivity.class);
        startActivity(intent);
        finish();
    }
}