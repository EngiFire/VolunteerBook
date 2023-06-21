package com.example.myapplication.screens.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.models.Event;
import com.example.myapplication.screens.MainActivity;
import com.example.myapplication.screens.request.RequestActivity;
import com.example.myapplication.screens.event.EventActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrganizerActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<Event> listTemp;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);
        init();
        getDataFromDB();
        setOnClickItem();
    }

    private void init(){
        listView = findViewById(R.id.listView);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child("Event").child("abc").orderByChild("responsible").equalTo(uid);
        mDataBase = FirebaseDatabase.getInstance().getReference("Event");
    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listData.size() > 0)listData.clear();
                if(listTemp.size() > 0)listTemp.clear();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Event event = ds.getValue(Event.class);
                    assert event != null;
                    if(event.responsible.equals(uid)){
                        listData.add(event.name);
                        //Log.d("listTemp", event.toString());
                        listTemp.add(event);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                Event event = listTemp.get(position);
                Intent i = new Intent(OrganizerActivity.this, ShowOrganizerActivity.class);
                i.putExtra(Constant.EVENT_NAME, event.name);
                i.putExtra(Constant.EVENT_ID, event.id);
                i.putExtra(Constant.EVENT_DIRECTION, event.direction);
                i.putExtra(Constant.EVENT_QUANTITY, event.quantity);
                i.putExtra(Constant.EVENT_DATA, event.data);
                i.putExtra(Constant.EVENT_PLACE, event.place);
                i.putExtra(Constant.EVENT_RESPONSIBLE, event.responsible);
                i.putExtra(Constant.EVENT_DESCRIPTION, event.description);
                startActivity(i);
            }
        });
    }

    public void onClickCreate (View view){
        Intent intent = new Intent(this, OrganizerCreateActivity.class);
        startActivity(intent);
        finish();
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