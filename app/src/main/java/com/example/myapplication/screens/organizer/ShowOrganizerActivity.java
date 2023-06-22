package com.example.myapplication.screens.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Constant;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.ApplicationEvent;
import com.example.myapplication.models.Event;
import com.example.myapplication.models.User;
import com.example.myapplication.screens.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowOrganizerActivity extends AppCompatActivity {

    private TextView tvName, tvData, tvResponsible, tvQuantity, tvPlace, tvDirection, tvDescription, tvExpOrgComp, expUsName, expUsEmail, expUsData, tvPartName, tvPartEmail, tvPartData;
    private CheckBox cbComplete, chbAddPoint;
    private DatabaseReference mDataBase, pDataBase, aeDataBase, cDataBase;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<ApplicationEvent> listTemp;
    private ListView lvParticipant;
    private Spinner spAddPoint;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_organizer);

        init();
        getIntentMain();

        getDataFromDB();
        setOnClickItem();

        ArrayAdapter<?> adPoint =
                ArrayAdapter.createFromResource(this, R.array.point,
                        android.R.layout.simple_spinner_item);
        adPoint.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spAddPoint.setAdapter(adPoint);
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

        tvExpOrgComp = findViewById(R.id.tvExpOrgComp);
        expUsName = findViewById(R.id.expUsName);
        expUsEmail = findViewById(R.id.expUsEmail);
        expUsData = findViewById(R.id.expUsData);
        spAddPoint = findViewById(R.id.spAddPoint);
        tvPartName = findViewById(R.id.tvPartName);
        tvPartEmail = findViewById(R.id.tvPartEmail);
        tvPartData = findViewById(R.id.tvPartData);
        chbAddPoint = findViewById(R.id.chbAddPoint);

        Intent i = getIntent();
        String eventName = i.getStringExtra(Constant.EVENT_ID);
        mDataBase = FirebaseDatabase.getInstance().getReference("ApplicationEvent").child(eventName);

        lvParticipant = findViewById(R.id.lvParticipant);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        lvParticipant.setAdapter(adapter);
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

    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listData.size() > 0) listData.clear();
                if (listTemp.size() > 0) listTemp.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ApplicationEvent apEvent = ds.getValue(ApplicationEvent.class);
                    assert apEvent != null;
                    if (apEvent.status.equals("Одобрена")) {
                        listData.add(apEvent.userSecName + " " + apEvent.userName);
                        //Log.d("listTemp", event.toString());
                        listTemp.add(apEvent);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(vListener);
    }

    private void setOnClickItem() {
        lvParticipant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationEvent apEvent = listTemp.get(position);

                userId = apEvent.userId;

                showSigned();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(apEvent.userId);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String secName = dataSnapshot.child("secName").getValue(String.class);
                        String data = dataSnapshot.child("data").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        tvPartName.setText(secName + " " + name);
                        tvPartEmail.setText(email);
                        tvPartData.setText(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    public void chBoxAddPoint(View view) {
        if (chbAddPoint.isChecked()) {

            int spinner_pos = spAddPoint.getSelectedItemPosition();
            String[] size_values = getResources().getStringArray(R.array.point);
            int point = Integer.valueOf(size_values[spinner_pos]);

            Intent i = getIntent();
            String eventId = i.getStringExtra(Constant.EVENT_ID);

            aeDataBase = FirebaseDatabase.getInstance().getReference().child("ApplicationEvent").child(eventId).child(userId).child("status");
            aeDataBase.setValue("Завершена");

            pDataBase = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("point");

            pDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer lastPoint = dataSnapshot.getValue(Integer.class);

                    Integer nowPoint = lastPoint + Integer.valueOf(point);

                    pDataBase.setValue(nowPoint);

                    Toast.makeText(getApplicationContext(), "Баллы начислены", Toast.LENGTH_SHORT).show();

                    notSigned();

                    //chbAddPoint.setChecked(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        }
    }
    public void checkBoxCompleteEvent(View view){
        if (cbComplete.isChecked()) {

            Intent i = getIntent();
            String eventName = i.getStringExtra(Constant.EVENT_NAME);

            cDataBase = FirebaseDatabase.getInstance().getReference();

            cDataBase.child("Event").orderByChild("name").equalTo(eventName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        eventSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Toast.makeText(getApplicationContext(), "Завершено", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, OrganizerActivity.class);
            startActivity(intent);
            finish();
        }

    }


    public void onClickShowExit (View view){
        Intent intent = new Intent(this, OrganizerActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSigned() {
        lvParticipant.setVisibility(View.GONE);
        cbComplete.setVisibility(View.GONE);

        expUsName.setVisibility(View.VISIBLE);
        expUsEmail.setVisibility(View.VISIBLE);
        expUsData.setVisibility(View.VISIBLE);
        spAddPoint.setVisibility(View.VISIBLE);
        tvPartName.setVisibility(View.VISIBLE);
        tvPartEmail.setVisibility(View.VISIBLE);
        tvPartData.setVisibility(View.VISIBLE);
        chbAddPoint.setVisibility(View.VISIBLE);
    }

    private void notSigned() {
        lvParticipant.setVisibility(View.VISIBLE);
        cbComplete.setVisibility(View.VISIBLE);

        expUsName.setVisibility(View.GONE);
        expUsEmail.setVisibility(View.GONE);
        expUsData.setVisibility(View.GONE);
        spAddPoint.setVisibility(View.GONE);
        tvPartName.setVisibility(View.GONE);
        tvPartEmail.setVisibility(View.GONE);
        tvPartData.setVisibility(View.GONE);
        chbAddPoint.setVisibility(View.GONE);
    }
}