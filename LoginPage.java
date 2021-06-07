package com.example.opensourcesoftwareproject_team;
// 참조 : https://seongjaemoon.github.io/android/2017/12/04/androidActivity.html - 안드로이드 생명주기
// 참조 : https://programmingfbf7290.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0Activity-%EC%83%9D%EB%AA%85%EC%A3%BC%EA%B8%B0-%EC%B4%9D%EC%A0%95%EB%A6%AC
// 생명주기
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {
    EditText id_TextField; // 아이디 입력란 선언
    private EditText password_TextField; // 비밀번호 입력한 선언
    Button login_Button; // 로그인 버튼 선언
    Button signUp_Button; // 회원가입 버튼 선언
    DatabaseManagement dm;
    User user = null;
    ChatClient cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        set();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 로그인 버튼 클릭 리스너 선언 및 정의
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = id_TextField.getText().toString(); // 입력된 아이디 문자열로 가져오기
                String password = password_TextField.getText().toString(); // 입력된 비밀번호 문자열로 가져오기

                // 가져온 아이디와 비밀번호가 "null", ""이 아닐때 - "" = 공백
                if (id != null && password != null && id != "" && password != "") {

                    // 아이디, 비밀번호가 맞을 경우
                    if (dm.login(id, password)) {
                        user = User.getInstence(id);

                        Intent main_Page = new Intent(getApplicationContext(), MainPage.class);
                        main_Page.putExtra("DBM", dm);
                        main_Page.putExtra("USER", user);
                        startActivity(main_Page); // 메인 페이지로 화면 전환

                        finish(); // 로그인 페이지 종료
                    } else { // 아이디, 비밀번호가 틀릴 경우

                    }
                } else { // 아이디 또는 비밀번호가 공백이거나 null일 경우
                    // 표시할 방식 정해야 함

                }
            }
        });

        // 회원가입 버튼 클릭 리스너 선언 및 정의
        signUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 버튼 클릭 시 회원가입 화면으로 전환
                // 로그인 화면 위로 회원가입 화면을 올리는 방식 - 뒤로가기를 눌렀을때 로그인 화면으로 이동 됨
                Intent signUp_Page = new Intent(getApplicationContext(), SignUpPage.class);
                signUp_Page.putExtra("DBM", dm);
                startActivity(signUp_Page);
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();

        login_Button.setOnClickListener(null);
        signUp_Button.setOnClickListener(null);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        dm = null;
        user = null;

        login_Button.setOnClickListener(null);
        signUp_Button.setOnClickListener(null);

        id_TextField = null;
        password_TextField = null;
        login_Button = null;
        signUp_Button = null;
    }

    void set() {
        dm = new DatabaseManagement(this);
        //cc = new ChatClient();

        id_TextField = findViewById(R.id.id_TextField_L); // 아이디 입력란 정의
        password_TextField = findViewById(R.id.password_TextField_L); // 비밀번호 입력란 정의
        login_Button = findViewById(R.id.login_Button_L); // 로그인 버튼 정의
        signUp_Button = findViewById(R.id.signUp_Button_L); // 회원가입 버튼 정의
        signUp_Button = findViewById(R.id.signUp_Button_L); // 회원가입 버튼 정의
    }
}