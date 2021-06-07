package com.example.opensourcesoftwareproject_team;
// https://lktprogrammer.tistory.com/188

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyPage extends AppCompatActivity {
    TextView name_Label; // 이름 라벨
    TextView eMail_Label; // 이메일 라벨
    Button myPostList_Button; // 내 게시판 버튼
    Button setProfile_Button; // 프로필 설정 버튼
    ImageButton previousPage_ImageButton;

    DatabaseManagement dm;
    User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_layout);

        set();
    }

    protected void onResume() {
        super.onResume();

        String[] user_Information = dm.getUserInformation(user.getId()).split(",");

        name_Label.setText(user_Information[0]);
        eMail_Label.setText(user_Information[1]);

        myPostList_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 해당 사용자가 작성한 게시글 목록 출력 화면으로 전환
                Intent myPost_Page = new Intent(getApplicationContext(), MyPostPage.class);
                myPost_Page.putExtra("DBM", dm);
                myPost_Page.putExtra("USER", user);
                startActivity(myPost_Page); // 내 게시글 목록으로 화면 전환
            }
        });

        setProfile_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 입력된 게시글 정보를 체크한 후 DB에 저장
                Intent profileSetting_Page = new Intent(getApplicationContext(), ProfileSettingPage.class);
                profileSetting_Page.putExtra("USER", user);
                profileSetting_Page.putExtra("DBM", dm);
                startActivity(profileSetting_Page); // 프로필 새팅으로 화면 전환
            }
        });

        previousPage_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 뒤로가기 버튼 클릭 시 해당 페이지 종료
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        myPostList_Button.setOnClickListener(null);
        setProfile_Button.setOnClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dm = null;

        myPostList_Button.setOnClickListener(null);
        setProfile_Button.setOnClickListener(null);

        previousPage_ImageButton = null;
        name_Label = null;
        eMail_Label = null;
        myPostList_Button = null;
        setProfile_Button = null;
    }

    void set() {
        dm = (DatabaseManagement) getIntent().getSerializableExtra("DBM");
        user = (User) getIntent().getSerializableExtra("USER");

        previousPage_ImageButton = findViewById(R.id.previousPage_ImageButton_Mp);
        name_Label = findViewById(R.id.name_Label_M); // 이름 라벨 정의
        eMail_Label = findViewById(R.id.email_Label_M); // 이메일 라벨 정의
        myPostList_Button = findViewById(R.id.myPost_Button_M); // 내 게시판 버튼 정의
        setProfile_Button = findViewById(R.id.profileSetting_Button_M); // 프로필 설정 버튼 정의
    }
}