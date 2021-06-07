package com.example.opensourcesoftwareproject_team;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingPage extends AppCompatActivity {
    TextView id_TextView;
    EditText password_TextField;
    EditText passwordCheck_TextField;
    EditText name_TextField;
    EditText email_TextFiled;
    EditText phoneNumber_TextFiled;
    TextView gender_TextView;
    Button complete_Button;

    DatabaseManagement dm;
    User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilesettingpage_layout);
        set();
    }

    protected void onResume() {
        super.onResume();

        TextWatchers tx1 = new TextWatchers(2);
        TextWatchers tx2 = new TextWatchers(3);
        TextWatchers tx3 = new TextWatchers(4);

        password_TextField.addTextChangedListener(tx1);
        passwordCheck_TextField.addTextChangedListener(tx2);
        name_TextField.addTextChangedListener(tx3);

        complete_Button.setOnClickListener(v -> {
            //
            String password = password_TextField.getText().toString();
            String name = name_TextField.getText().toString();
            String email = email_TextFiled.getText().toString();
            String phoneNumber = phoneNumber_TextFiled.getText().toString();

            dm.updateUserInformation(user.getId(), password, name, email, phoneNumber);

            finish();
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        complete_Button.setOnClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dm = null;
        user = null;

        complete_Button.setOnClickListener(null);

        id_TextView = null;
        password_TextField = null;
        passwordCheck_TextField = null;
        name_TextField = null;
        gender_TextView = null;
        email_TextFiled = null;
        phoneNumber_TextFiled = null;
    }

    void set() {
        dm = (DatabaseManagement) getIntent().getSerializableExtra("DBM");
        user = (User) getIntent().getSerializableExtra("USER");

        id_TextView = findViewById(R.id.id_textView_Ps);
        password_TextField = findViewById(R.id.password_TextField_Ps);
        passwordCheck_TextField = findViewById(R.id.check_Password_TextField_Ps);
        name_TextField = findViewById(R.id.name_TextField_Ps);
        gender_TextView = findViewById(R.id.gender_textView_Ps);
        email_TextFiled = findViewById(R.id.email_TextField_Ps);
        phoneNumber_TextFiled = findViewById(R.id.phoneNumber_TextField_Ps);

        complete_Button = findViewById(R.id.complete_Button_Ps);

        id_TextView.setText(user.getId());
        gender_TextView.setText(dm.getUser_Gender(user.getId()));
    }

    // 문자 입력 이벤트 클래스
    class TextWatchers implements TextWatcher {
        private int check;

        // 2 = 비밀번호
        // 3 = 비밀번호 확인
        // 4 = 이름
        TextWatchers(int num) {
            check = num;
        }

        // 입력 전
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        // 입력 중
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        // 입력 후
        @Override
        public void afterTextChanged(Editable s) {
            String msg =  s.toString();

            if (msg.equals("")) {
                // 입력된 무언가가 공백

            } else {
               if (check == 2) { // 비밀번호 – 특수문자와 영문, 숫자의 조합
                    for (int i = 0; i < msg.length(); i++) {
                        int x = msg.charAt(i);

                        if (33 <= x && x <= 126) { // 출력 가능한 아스키코드 중 스페이스를 제외한 모든 문자 허용 (특수문자, 문자)

                        } else {
                            unacceptable_Char();
                        }
                    }
                } else if (check == 3) { // 비밀번호 확인 – 비밀번호와 동일
                    if (password_TextField.getText().toString().equals(msg)) { // 비밀번호 확인 문자열과 비밀번호 문자열이 동일할 떄
                        // 비밀번호 동일
                    } else { // 비밀번호 확인 문자열과 비밀번호 문자열이 동일하지 않을 때
                        passwordCheck_TextField.setHint("비밀번호가 일치하지 않습니다.");
                    }
                } else if (check == 4) { // 이름 – 문자
                    for (int i = 0; i < msg.length(); i++) {
                        int x = msg.charAt(i);

                        if ((65 <= x && x <= 90) || (97 <= x && x <= 122)) { // 소문자 대문자
                        } else if ( 44032  <= x && x <= 55203){ // 한글
                        } else {
                            unacceptable_Char();
                        }
                    }
                } else {

                }
            }
        }

        // 사용자에게 잘못된 문자라는 것을 알려줌
        void unacceptable_Char() {

        }
    }
}
