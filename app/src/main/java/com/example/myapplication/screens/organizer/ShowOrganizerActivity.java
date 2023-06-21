package com.example.myapplication.screens.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Constant;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.Event;
import com.example.myapplication.screens.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowOrganizerActivity extends AppCompatActivity {

    private TextView tvName, tvData, tvResponsible, tvQuantity, tvPlace, tvDirection, tvDescription;
    private CheckBox cbComplete;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_organizer);

        init();
        getIntentMain();
    }

    private void init(){
        tvName = findViewById(R.id.tvEventNameOrg);
        tvData = findViewById(R.id.tvEventDataOrg);
        tvResponsible = findViewById(R.id.tvEventResponsibleOrg);
        tvDirection = findViewById(R.id.tvEventDirectionOrg);
        tvQuantity = findViewById(R.id.tvEventQuantityOrg);
        tvPlace = findViewById(R.id.tvEventPlaceOrg);
        tvDescription = findViewById(R.id.tvEventDescriptionOrg);
        cbComplete = findViewById(R.id.cbComplete);
        mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    private void getIntentMain(){
        Intent i = getIntent();
        if(i != null){
            tvName.setText(i.getStringExtra(Constant.EVENT_NAME));
            tvData.setText("Дата проведения мероприятия:\n" + i.getStringExtra(Constant.EVENT_DATA));
            tvQuantity.setText("Количество участников:\n" + i.getIntExtra(Constant.EVENT_PARTICIPANT, 0) + "/" + i.getIntExtra(Constant.EVENT_QUANTITY, 0));
            tvPlace.setText("Место проведения мероприятия:\n" + i.getStringExtra(Constant.EVENT_PLACE));
            tvDirection.setText("Направление мероприятия:\n" + i.getStringExtra(Constant.EVENT_DIRECTION));
            tvDescription.setText("Описание мероприятия:\n" + i.getStringExtra(Constant.EVENT_DESCRIPTION));

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

    public void checkBoxCompleteEvent(View view){
        if(cbComplete.isChecked()){

            Intent i = getIntent();
            String eventId = i.getStringExtra(Constant.EVENT_NAME);
            String eventPoint = i.getStringExtra(Constant.EVENT_POINT);



//            mDataBase.child("ApplicationEvent").child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot ds : dataSnapshot.getChildren()){
//
//                        Event event = ds.getValue(Event.class);
//                        assert event != null;
//                        if(event.responsible.equals(uid)){
//                            listEventData.add(event.name);
//                            //Log.d("listTemp", event.toString());
//                            listEventTemp.add(event);
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("ApplicationEvent").child(eventId);

//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot ds : dataSnapshot.getChildren()){
//                        if(){
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });



//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//            Query query = reference.child("ApplicationEvent").child(eventId).orderByChild("status").equalTo("Одобрена");
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.getChildrenCount()>0) {
//                        //username found
//                        for(DataSnapshot ds : dataSnapshot.getChildren()){
//
//
//                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                            Query query = reference.child("ApplicationEvent").child(eventId).orderByChild("status").equalTo("Одобрена");
//                            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if(dataSnapshot.getChildrenCount()>0) {
//                                        //username found
//                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
//
//                                            Event event = ds.getValue(Event.class);
//                                            assert event != null;
//                                            if(event.responsible.equals(uid)){
//                                                listEventData.add(event.name);
//                                                //Log.d("listTemp", event.toString());
//                                                listEventTemp.add(event);
//                                            }
//                                        }
//                                    } else{
//                                        // username not found
//                                        Toast.makeText(getApplicationContext(), "Нет участников", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                }
//                            });
//
//
//                        }
//                    } else{
//                        // username not found
//                        Toast.makeText(getApplicationContext(), "Нет участников", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });




            /////////////////////////////////




//            mDataBase.child("Event").child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
//                        userSnapshot.getRef().removeValue();
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//            Toast.makeText(getApplicationContext(), "Завершено", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(this, OrganizerActivity.class);
//            startActivity(intent);
//            finish();
        }
    }

    public void onClickShowExit (View view){
        Intent intent = new Intent(this, OrganizerActivity.class);
        startActivity(intent);
        finish();
    }
}