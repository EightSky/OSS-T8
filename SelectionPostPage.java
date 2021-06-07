package com.example.opensourcesoftwareproject_team;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectionPostPage extends AppCompatActivity {
    TextView id;
    TextView title;
    TextView time;
    TextView price;
    TextView content;
    TextView tag;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;
    ImageView image6;
    ImageView image7;
    ImageView image8;
    ImageView image9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectionpost_layout);
        set();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void set() {
        id = findViewById(R.id.id_TextView_Sp);
        title = findViewById(R.id.title_TextView_Sp);
        price = findViewById(R.id.price_TextView_Sp);
        time = findViewById(R.id.time_TextView_Sp);
        content = findViewById(R.id.content_TextView_Sp);
        tag = findViewById(R.id.tag_TextView_Sp);
        image1 = findViewById(R.id.imageView1_Sp);
        image2 = findViewById(R.id.imageView2_Sp);
        image3 = findViewById(R.id.imageView3_Sp);
        image4 = findViewById(R.id.imageView4_Sp);
        image5 = findViewById(R.id.imageView5_Sp);
        image6 = findViewById(R.id.imageView6_Sp);
        image7 = findViewById(R.id.imageView7_Sp);
        image8 = findViewById(R.id.imageView8_Sp);
        image9 = findViewById(R.id.imageView9_Sp);

        Intent intent = getIntent();

        id.setText("작성자 : " + intent.getExtras().getString("id"));
        title.setText(intent.getExtras().getString("title"));
        price.setText(intent.getExtras().getString("price") + "₩");
        time.setText(intent.getExtras().getString("time"));
        content.setText(intent.getExtras().getString("content"));
        tag.setText(intent.getExtras().getString("tag"));
        image1.setImageURI(Uri.parse(intent.getExtras().getString("image1")));
        image2.setImageURI(Uri.parse(intent.getExtras().getString("image2")));
        image3.setImageURI(Uri.parse(intent.getExtras().getString("image3")));
        image4.setImageURI(Uri.parse(intent.getExtras().getString("image4")));
        image5.setImageURI(Uri.parse(intent.getExtras().getString("image5")));
        image6.setImageURI(Uri.parse(intent.getExtras().getString("image6")));
        image7.setImageURI(Uri.parse(intent.getExtras().getString("image7")));
        image8.setImageURI(Uri.parse(intent.getExtras().getString("image8")));
        image9.setImageURI(Uri.parse(intent.getExtras().getString("image9")));
    }
}
