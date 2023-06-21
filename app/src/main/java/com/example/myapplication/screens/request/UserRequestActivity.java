package com.example.myapplication.screens.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.screens.request.RequestActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRequestActivity extends AppCompatActivity {

    private TextView tvUsName, tvUsSecName, tvUsData, tvUsEmail;
    private CheckBox checkBox2;
    private DatabaseReference mDataBase;
    private Spinner spUsPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);

        init();

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.post,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spUsPost.setAdapter(adapter);

        getIntentMain();
    }

    private void init(){
        tvUsName = findViewById(R.id.tvUsName);
        tvUsSecName = findViewById(R.id.tvUsSecName);
        tvUsData = findViewById(R.id.tvUsData);
        spUsPost = findViewById(R.id.spUsPost);
        tvUsEmail = findViewById(R.id.tvUsEmail);
        checkBox2 = findViewById(R.id.checkBox2);
        mDataBase = FirebaseDatabase.getInstance().getReference();
    }


    private void getIntentMain(){
        Intent i = getIntent();
        if(i != null){
            tvUsName.setText(i.getStringExtra(Constant.USER_NAME));
            tvUsSecName.setText(i.getStringExtra(Constant.USER_SEC_NAME));
            tvUsData.setText(i.getStringExtra(Constant.USER_DATA));
            tvUsEmail.setText(i.getStringExtra(Constant.USER_EMAIL));
        }
    }

    public void onClickCheckSave(View view){
        if(checkBox2.isChecked()){
            checkBox2.setClickable(false);

            Intent i = getIntent();
            String usId = i.getStringExtra(Constant.USER_ID);
            String usName = i.getStringExtra(Constant.USER_NAME);
            String usSecName = i.getStringExtra(Constant.USER_SEC_NAME);
            String usPassword = i.getStringExtra(Constant.USER_PASSWORD);
            String usEmail = i.getStringExtra(Constant.USER_EMAIL);
            String usData = i.getStringExtra(Constant.USER_DATA);
            String imageUrl = i.getStringExtra(Constant.USER_IMAGE_URL);
            String usPost = spUsPost.getSelectedItem().toString();

            User newUser = new User(usId, usName, usSecName, usData, usEmail, usPassword, usPost, 0, true, imageUrl);

            mDataBase.child("User").child(usId).setValue(newUser);

            Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();

            mDataBase.child("Request").child(usId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        userSnapshot.getRef().removeValue();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Intent intent = new Intent(this, RequestActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onClickUserRequestExit(View view){
        Intent intent = new Intent(this, RequestActivity.class);
        startActivity(intent);
        finish();
    }
}