package com.example.myapplication.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.models.ApplicationEvent;
import com.example.myapplication.models.Event;
import com.example.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventRequestActivity extends AppCompatActivity {
    private TextView tvEvName, tvEvDirection, tvEvData, tvEvQuantity, tvEvDescription;
    private ListView lvParticipant;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<ApplicationEvent> listTemp;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_request);

        init();
        getIntentMain();

        getDataFromDB();
    }

    private void init(){
        tvEvName = findViewById(R.id.tvEvName);
        tvEvDirection = findViewById(R.id.tvEvDirection);
        tvEvData = findViewById(R.id.tvEvData);
        tvEvQuantity = findViewById(R.id.tvEvQuantity);
        tvEvDescription = findViewById(R.id.tvEvDescription);

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
                    listData.add(apEvent.userName);
                    //Log.d("listTemp", event.toString());
                    listTemp.add(apEvent);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(vListener);
    }

//    private void setOnClickItem(){
//        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick (AdapterView < ? > parent, View view, int position, long id){
//                Event event = listEventTemp.get(position);
//                Intent i = new Intent(RequestActivity.this, EventRequestActivity.class);
//                i.putExtra(Constant.EVENT_NAME, event.name);
//                i.putExtra(Constant.EVENT_ID, event.id);
//                i.putExtra(Constant.EVENT_DIRECTION, event.direction);
//                i.putExtra(Constant.EVENT_QUANTITY, event.quantity);
//                i.putExtra(Constant.EVENT_DATA, event.data);
//                i.putExtra(Constant.EVENT_PLACE, event.place);
//                i.putExtra(Constant.EVENT_RESPONSIBLE, event.responsible);
//                i.putExtra(Constant.EVENT_DESCRIPTION, event.description);
//                startActivity(i);
//            }
//        });
//    }
}