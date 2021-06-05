package com.example.opensourcesoftwareproject_team;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectionPostPage extends AppCompatActivity {
    ArrayList<Post> list = new ArrayList<>();
    RecyclerView chat;
    DatabaseManagement dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionpost_layout);
        set();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정
        chat.setAdapter(new Recycler_Adapter(list));

        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        chat.setLayoutManager(new LinearLayoutManager(this));
    }

    void set() {
        dm = (DatabaseManagement) getIntent().getSerializableExtra("DBM");

        chat = findViewById(R.id.chat);
    }
}
