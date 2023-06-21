package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.models.User;
import com.example.myapplication.screens.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edName, edSecName, edData, edEmail, edPassword;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init(){
        edName = findViewById(R.id.edName);
        edSecName = findViewById(R.id.edSecName);
        edEmail = findViewById(R.id.edEmail);
        edData = findViewById(R.id.edData);
        edPassword = findViewById(R.id.edPassword);
        mDataBase = FirebaseDatabase.getInstance().getReference("Request");
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickSave(View view){

        String name = edName.getText().toString();
        String sec_name = edSecName.getText().toString();
        String email = edEmail.getText().toString();
        String data = edData.getText().toString();
        String password = edPassword.getText().toString();


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(sec_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(data) && !TextUtils.isEmpty(password)) {
            mAuth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        User newUser = new User(id, name, sec_name, data, email, password, "Студент", 0, false, null);

                        mDataBase.child(id).setValue(newUser);

                        Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Пустое поле", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRegExit(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}