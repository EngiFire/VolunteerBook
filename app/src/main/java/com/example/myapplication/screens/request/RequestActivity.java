package com.example.myapplication.screens.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.models.Event;
import com.example.myapplication.models.User;
import com.example.myapplication.screens.MainActivity;
import com.example.myapplication.screens.event.EventActivity;
import com.example.myapplication.screens.organizer.OrganizerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private RadioGroup toggle;
    private RadioButton rbUser, rbEvent;
    private ListView lvUser, lvEvent;
    private ArrayAdapter<String> eventAdapter, userAdapter;
    private List<String> listUserData;
    private List<String> listEventData;
    private List<User> listUserTemp;
    private List<Event> listEventTemp;
    private DatabaseReference rDataBase, eDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        init();

        getEventDataFromDB();
        setOnClickItem();

        getUserDataFromDB();
        setOnClickUserItem();

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(rbEvent.isChecked()){
                    lvUser.setVisibility(View.GONE);
                    lvEvent.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "Event", Toast.LENGTH_SHORT).show();
                } else if (rbUser.isChecked()) {
                    lvEvent.setVisibility(View.GONE);
                    lvUser.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "User", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init(){
        toggle = findViewById(R.id.toggle);
        rbUser = findViewById(R.id.rbUser);
        rbEvent = findViewById(R.id.rbEvent);

        lvEvent = findViewById(R.id.lvEvent);
        lvUser = findViewById(R.id.lvUser);

        listUserData = new ArrayList<>();
        listEventData = new ArrayList<>();
        listUserTemp = new ArrayList<>();
        listEventTemp = new ArrayList<>();

        eventAdapter = new ArrayAdapter<>(RequestActivity.this, android.R.layout.simple_list_item_1, listEventData);
        lvEvent.setAdapter(eventAdapter);
        userAdapter = new ArrayAdapter<>(RequestActivity.this, android.R.layout.simple_list_item_1, listUserData);
        lvUser.setAdapter(userAdapter);

        eDataBase = FirebaseDatabase.getInstance().getReference("Event");
        rDataBase = FirebaseDatabase.getInstance().getReference("Request");
    }

    private void getEventDataFromDB(){
        ValueEventListener vListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listEventData.size() > 0)listEventData.clear();
                if(listEventTemp.size() > 0)listEventTemp.clear();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Event event = ds.getValue(Event.class);
                    assert event != null;
                    if(event.responsible.equals(uid)){
                        listEventData.add(event.name);
                        //Log.d("listTemp", event.toString());
                        listEventTemp.add(event);
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        eDataBase.addValueEventListener(vListener);
    }

    private void setOnClickItem(){
        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                Event event = listEventTemp.get(position);
                Intent i = new Intent(RequestActivity.this, EventRequestActivity.class);
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

    private void getUserDataFromDB(){
        ValueEventListener vListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listUserData.size() > 0)listUserData.clear();
                if(listUserTemp.size() > 0)listUserTemp.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    User user = ds.getValue(User.class);
                    assert user != null;
                    listUserData.add(user.secName + " " + user.name);
                    //Log.d("listTemp", event.toString());
                    listUserTemp.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        rDataBase.addValueEventListener(vListener);
    }

    private void setOnClickUserItem(){
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                User user = listUserTemp.get(position);
                Intent i = new Intent(RequestActivity.this, UserRequestActivity.class);
                i.putExtra(Constant.USER_ID, user.id);
                i.putExtra(Constant.USER_NAME, user.name);
                i.putExtra(Constant.USER_SEC_NAME, user.secName);
                i.putExtra(Constant.USER_PASSWORD, user.password);
                i.putExtra(Constant.USER_EMAIL, user.email);
                i.putExtra(Constant.USER_DATA, user.data);
                startActivity(i);
            }
        });
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