package com.example.myapplication.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.screens.event.EventActivity;
import com.example.myapplication.screens.organizer.OrganizerActivity;
import com.example.myapplication.screens.request.RequestActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private TextView tvName, tvSecName, tvPoint, tvPointClass;

    private ImageView imView, imStatus;
    private ConstraintLayout layoutStudent, layoutCounselor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tvName);
        tvSecName = findViewById(R.id.tvSecName);
        tvPoint = findViewById(R.id.tvPoint);
        tvPointClass = findViewById(R.id.tvPointClass);
        layoutStudent = findViewById(R.id.layoutStudent);
        layoutCounselor = findViewById(R.id.layoutCounselor);

        imView = findViewById(R.id.imView);
        imStatus = findViewById(R.id.imStatus);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String secName = dataSnapshot.child("secName").getValue(String.class);
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                Integer point = dataSnapshot.child("point").getValue(Integer.class);

                if(point > 1000){
                    point = 1000;
                }

                tvName.setText(name);
                tvSecName.setText(secName);
                tvPoint.setText("Баллы: " + String.valueOf(point));

                if(point <= 201){
                    tvPointClass.setText("Бронзовый");
                    imStatus.setImageResource(R.drawable.star_bronze);
                } else if (point <= 401) {
                    tvPointClass.setText("Серебряный");
                    imStatus.setImageResource(R.drawable.star_silver);
                } else if (point <= 601) {
                    tvPointClass.setText("Золотой");
                    imStatus.setImageResource(R.drawable.star_gold);
                } else if (point <= 801) {
                    tvPointClass.setText("Рубиновый");
                    imStatus.setImageResource(R.drawable.stone_ruby);
                } else if (point <= 1000) {
                    tvPointClass.setText("Бриллиантовый");
                    imStatus.setImageResource(R.drawable.stone_diamond);
                }

                if(imageUrl.equals("")){
                    imView.setImageResource(R.drawable.profile_close);
                } else {
                    String photoUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    Glide.with(MainActivity.this).load(photoUrl).into(imView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post = dataSnapshot.child("post").getValue(String.class);


                Toast.makeText(getApplicationContext(), post, Toast.LENGTH_SHORT).show();


                if(post.equals("Вожатый")){
                    layoutStudent.setVisibility(View.GONE);

                    layoutCounselor.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void onClickExit(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

        FirebaseAuth.getInstance().signOut();
    }

    public void onClickMain (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickEvent (View view){
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickOrganizer (View view){
        Intent intent = new Intent(this, OrganizerActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickRequest (View view){
        Intent intent = new Intent(this, RequestActivity.class);
        startActivity(intent);
        finish();
    }
}