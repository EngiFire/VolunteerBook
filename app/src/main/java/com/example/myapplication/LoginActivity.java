package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.screens.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    private Button bReg, bStart, bExit, bSignIn;
    private TextView tvUserEmail, tvGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null){
            showSigned();

            String uid = mAuth.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getChildrenCount()>0) {
                        //username found
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String secName = dataSnapshot.child("secName").getValue(String.class);

                        tvUserEmail.setText(secName + " " + name);
                    } else{
                        // username not found
                        String userName = cUser.getEmail();
                        tvUserEmail.setText(userName);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            notSigned();

            Toast.makeText(this, "Авторизуйтесь или зарегестрируйтесь", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        edLogin = findViewById(R.id.edLogEmail);
        edPassword = findViewById(R.id.edLogPassword);
        bSignIn  = findViewById(R.id.bSignIn);
        bReg  = findViewById(R.id.bReg);

        mAuth = FirebaseAuth.getInstance();

        tvGreeting = findViewById(R.id.tvGreeting);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        bStart = findViewById(R.id.bStart);
        bExit = findViewById(R.id.bExit);
    }


    public void onClickSignIn (View view){

        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())){

            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("User").orderByChild("id").equalTo(uid);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount()>0) {
                                    //username found
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else{
                                    // username not found
                                    Toast.makeText(getApplicationContext(), "Ваша заявка находится на рассмотрении", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else{
                        Toast.makeText(getApplicationContext(), "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onClickSignUp(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickStart(View view){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("User").orderByChild("id").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    //username found
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    // username not found
                    Toast.makeText(getApplicationContext(), "Ваша заявка ещё находится на рассмотрении", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onClickExitLog(View view){
        FirebaseAuth.getInstance().signOut();

        notSigned();
    }

    private void showSigned(){
        edLogin.setVisibility(View.GONE);
        edPassword.setVisibility(View.GONE);
        bSignIn.setVisibility(View.GONE);
        bReg.setVisibility(View.GONE);

        bStart.setVisibility(View.VISIBLE);
        bExit.setVisibility(View.VISIBLE);
        tvUserEmail.setVisibility(View.VISIBLE);
        tvGreeting.setVisibility(View.VISIBLE);
    }

    private void notSigned(){
        bStart.setVisibility(View.GONE);
        bExit.setVisibility(View.GONE);
        tvUserEmail.setVisibility(View.GONE);
        tvGreeting.setVisibility(View.GONE);

        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
        bReg.setVisibility(View.VISIBLE);
    }
}