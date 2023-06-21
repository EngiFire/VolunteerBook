package com.example.myapplication.screens.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.models.ApplicationEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventRequestActivity extends AppCompatActivity {
    private TextView tvEvName, tvEvDirection, tvEvData, tvEvQuantity, tvEvDescription, textView11, textView13, textView14, textView15, textView17, textView19, textVUser, textVData, textVPost, textVEmail;
    private CheckBox checkBox3, checkBox4;
    private ListView lvParticipant;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<ApplicationEvent> listTemp;
    private DatabaseReference mDataBase, database;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_request);

        init();
        getIntentMain();

        getDataFromDB();
        setOnClickItem();
    }

    private void init(){
        tvEvName = findViewById(R.id.tvEvName);
        tvEvDirection = findViewById(R.id.tvEvDirection);
        tvEvData = findViewById(R.id.tvEvData);
        tvEvQuantity = findViewById(R.id.tvEvQuantity);
        tvEvDescription = findViewById(R.id.tvEvDescription);

        textView11 = findViewById(R.id.textView11);
        textView13 = findViewById(R.id.textView13);
        textView14 = findViewById(R.id.textView14);
        textView15 = findViewById(R.id.textView15);
        textView17 = findViewById(R.id.textView17);
        textView19 = findViewById(R.id.textView19);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        textVUser = findViewById(R.id.textVUser);
        textVData = findViewById(R.id.textVData);
        textVPost = findViewById(R.id.textVPost);
        textVEmail = findViewById(R.id.textVEmail);

        lvParticipant = findViewById(R.id.lvParticipant);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        lvParticipant.setAdapter(adapter);

        Intent i = getIntent();
        String eventName = i.getStringExtra(Constant.EVENT_ID);
        mDataBase = FirebaseDatabase.getInstance().getReference("ApplicationEvent").child(eventName);
    }

    private void getIntentMain(){
        Intent i = getIntent();
        if(i != null){
            tvEvName.setText(i.getStringExtra(Constant.EVENT_NAME));
            tvEvDirection.setText(i.getStringExtra(Constant.EVENT_DIRECTION));
            tvEvData.setText(i.getStringExtra(Constant.EVENT_DATA));
            tvEvQuantity.setText("" + i.getIntExtra(Constant.EVENT_QUANTITY, 0));
            tvEvDescription.setText(i.getStringExtra(Constant.EVENT_DESCRIPTION));
        }
    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size() > 0)listData.clear();
                if(listTemp.size() > 0)listTemp.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ApplicationEvent apEvent = ds.getValue(ApplicationEvent.class);
                    assert apEvent != null;
                    if (apEvent.status.equals("На рассмотрении")){
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

    private void setOnClickItem(){
        lvParticipant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view, int position, long id){
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
                        String post = dataSnapshot.child("post").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        textVUser.setText(secName + " " + name);
                        textVData.setText(data);
                        textVPost.setText(post);
                        textVEmail.setText(email);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        });
    }

    public void onClickCheckAdd(View view){
        if(checkBox3.isChecked()){
            checkBox3.setClickable(false);

            Intent i = getIntent();
            String eventId = i.getStringExtra(Constant.EVENT_ID);

            database = FirebaseDatabase.getInstance().getReference().child("ApplicationEvent").child(eventId).child(userId).child("status");
            database.setValue("Одобрена");

            Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();

            notSigned();
        }
    }

    public void onClickCheckCanceled(View view){
        if(checkBox3.isChecked()){
            checkBox3.setClickable(false);

            Intent i = getIntent();
            String eventId = i.getStringExtra(Constant.EVENT_ID);

            database = FirebaseDatabase.getInstance().getReference().child("ApplicationEvent").child(eventId).child(userId).child("status");
            database.setValue("Отменена");

            Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();

            notSigned();
        }
    }

    public void onClickEventRequestExit(View view){
        Intent intent = new Intent(this, RequestActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSigned(){
        lvParticipant.setVisibility(View.GONE);
        textView11.setVisibility(View.GONE);

        textView13.setVisibility(View.VISIBLE);
        textView14.setVisibility(View.VISIBLE);
        textView15.setVisibility(View.VISIBLE);
        textView17.setVisibility(View.VISIBLE);
        textView19.setVisibility(View.VISIBLE);
        checkBox3.setVisibility(View.VISIBLE);
        checkBox4.setVisibility(View.VISIBLE);
        textVUser.setVisibility(View.VISIBLE);
        textVData.setVisibility(View.VISIBLE);
        textVPost.setVisibility(View.VISIBLE);
        textVEmail.setVisibility(View.VISIBLE);
    }

    private void notSigned(){
        lvParticipant.setVisibility(View.VISIBLE);
        textView11.setVisibility(View.VISIBLE);

        textView13.setVisibility(View.GONE);
        textView14.setVisibility(View.GONE);
        textView15.setVisibility(View.GONE);
        textView17.setVisibility(View.GONE);
        textView19.setVisibility(View.GONE);
        checkBox3.setVisibility(View.GONE);
        checkBox4.setVisibility(View.GONE);
        textVUser.setVisibility(View.GONE);
        textVData.setVisibility(View.GONE);
        textVPost.setVisibility(View.GONE);
        textVEmail.setVisibility(View.GONE);
    }
}