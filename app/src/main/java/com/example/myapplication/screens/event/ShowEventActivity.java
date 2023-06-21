package com.example.myapplication.screens.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.models.ApplicationEvent;
import com.example.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowEventActivity extends AppCompatActivity {

    private TextView tvName, tvData, tvResponsible, tvQuantity, tvPlace, tvDirection, tvDescription, tvEventPoint;
    private CheckBox checkBox;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        init();
        getIntentMain();
    }

    private void init(){
        tvName = findViewById(R.id.tvEventName);
        tvData = findViewById(R.id.tvEventData);
        tvResponsible = findViewById(R.id.tvEventResponsible);
        tvDirection = findViewById(R.id.tvEventDirection);
        tvQuantity = findViewById(R.id.tvEventQuantity);
        tvPlace = findViewById(R.id.tvEventPlace);
        tvDescription = findViewById(R.id.tvEventDescription);
        tvEventPoint = findViewById(R.id.tvEventPoint);
        checkBox = findViewById(R.id.checkBox);
        mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    private void getIntentMain(){
        Intent i = getIntent();
        if(i != null){
            tvName.setText(i.getStringExtra(Constant.EVENT_NAME));
            tvData.setText("Дата проведения мероприятия:\n" + i.getStringExtra(Constant.EVENT_DATA));
            tvQuantity.setText("Количество участников:\n" + i.getIntExtra(Constant.EVENT_PARTICIPANT, 0) + "/" +i.getIntExtra(Constant.EVENT_QUANTITY, 0));
            tvPlace.setText("Место проведения мероприятия:\n" + i.getStringExtra(Constant.EVENT_PLACE));
            tvDirection.setText("Направление мероприятия:\n" + i.getStringExtra(Constant.EVENT_DIRECTION));
            tvDescription.setText("Описание мероприятия:\n" + i.getStringExtra(Constant.EVENT_DESCRIPTION));
            tvEventPoint.setText("Баллы за мероприятие:\n" + i.getIntExtra(Constant.EVENT_POINT, 0));

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(i.getStringExtra(Constant.EVENT_RESPONSIBLE));
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String respName = snapshot.child("name").getValue(String.class);
                    String respSecName = snapshot.child("secName").getValue(String.class);
                    tvResponsible.setText("Руководитель мероприятия:\n" + respSecName + " " + respName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        String eventId = i.getStringExtra(Constant.EVENT_ID);
        Integer eventPart = i.getIntExtra(Constant.EVENT_PARTICIPANT, 0);
        Integer eventQuan = i.getIntExtra(Constant.EVENT_QUANTITY, 0);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("ApplicationEvent").child(eventId).orderByChild("userId").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    //username found
                    checkBox.setClickable(false);
                    checkBox.setChecked(true);
                } else if (eventPart.equals(eventQuan)) {
                    checkBox.setClickable(false);
                    checkBox.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onClickCheck(View view){
        if(checkBox.isChecked()){
            checkBox.setClickable(false);

            Intent i = getIntent();
            String eventName = i.getStringExtra(Constant.EVENT_NAME);
            String eventId = i.getStringExtra(Constant.EVENT_ID);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mDataBase.child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String uName = dataSnapshot.child("name").getValue(String.class);
                    String uSecName = dataSnapshot.child("secName").getValue(String.class);

                    ApplicationEvent newAppEvent = new ApplicationEvent(eventId, eventName, uid, uName, uSecName, "На рассмотрении");
                    mDataBase.child("ApplicationEvent").child(eventId).child(uid).setValue(newAppEvent);mDataBase.child("ApplicationEvent").child(eventId).child(uid).setValue(newAppEvent);

                    Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void onClickReturn (View view){
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
        finish();
    }
}