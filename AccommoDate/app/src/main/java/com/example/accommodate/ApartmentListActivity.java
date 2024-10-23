package com.example.accommodate;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.accommodate.global.Room;
import java.util.ArrayList;

public class ApartmentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);

        // Get the list of rooms from the intent
        ArrayList<Room> rooms = (ArrayList<Room>) getIntent().getSerializableExtra("rooms");

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomAdapter = new RoomAdapter(this, rooms);
        recyclerView.setAdapter(roomAdapter);
    }
}
