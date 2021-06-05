package com.example.opensourcesoftwareproject_team;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpPage extends AppCompatActivity {
    String[] gender = {"남성", "여성"}; // 스피너 재료
    boolean doubleCheck = false;

    EditText id_TextField; // 아이디 텍스트 필드
    EditText password_TextField; // 비밀번호 텍스트 필드
    EditText check_Password_TextField; // 비밀번호 확인 텍스트 필드
    EditText name_TextField; // 이름 텍스트 필드
    EditText email_TextField; // 이메일 텍스트 필드
    EditText phone_Number_TextField; // 전화번호 텍스트 필드
    Spinner gender_Spinner; // 성별 스피너

    Button complete_Button; // 완료 버튼
    Button doubleCheck_Button; // id 중복 확인 버튼

    DatabaseManagement dm; // DatabaseManagement 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage_layout);

        set();
    }

    @Override
    protected void onResume() {
        super.onResume();

        id_TextField.addTextChangedListener(new TextWatchers(1));
        password_TextField.addTextChangedListener(new TextWatchers(2));
        check_Password_TextField.addTextChangedListener(new TextWatchers(3));
        name_TextField.addTextChangedListener(new TextWatchers(4));

        gender_Spinner.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gender)); // 스피너 구성 요소 추가

        // 완료 버튼 리스너
        complete_Button.setOnClickListener(v -> {
            // 작성된 내용을 검증 후
            // DB에 저장하기 위한 메소드 호출
            String id = id_TextField.getText().toString();
            String password = password_TextField.getText().toString();
            String name = name_TextField.getText().toString();
            String gender = gender_Spinner.getSelectedItem().toString();
            String email = email_TextField.getText().toString();
            String phone_number = phone_Number_TextField.getText().toString();

            if (doubleCheck) {
                dm.signUp(id, password, name, gender, email, phone_number);
            } else {
                // id 중복 미확인 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            }

            finish();
        });

        // 중복 확인 버튼 리스너
        doubleCheck_Button.setOnClickListener(v -> {
            // 사용자가 입력한 id를 db의 id 들과 비교하여 중복일 경우 사용자가 입력한 아이디를 공백으로 변경하고
            String id = id_TextField.getText().toString();

            // 아이디가 중복이 아닐 경우
            if (dm.double_Check(id)) {
                // 사용 가능한 아이디
                System.out.println("가능");
                doubleCheck = true;
            } else { // 아이디가 중복일 경우
                // 중복 아이디
                System.out.println("불가능");
                doubleCheck = false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        gender_Spinner.setAdapter(null);
        complete_Button.setOnClickListener(null);
        doubleCheck_Button.setOnClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dm = null;

        complete_Button.setOnClickListener(null);
        doubleCheck_Button.setOnClickListener(null);
        gender_Spinner.setAdapter(null);

        id_TextField = null;
        password_TextField = null;
        check_Password_TextField = null;
        name_TextField = null;
        email_TextField = null;
        phone_Number_TextField = null;
        gender_Spinner = null;
    }

    void set() {
        dm = (DatabaseManagement) getIntent().getSerializableExtra("DBM");

        // 텍스트 필드들 정의 시작
        id_TextField = findViewById(R.id.id_TextField_S);
        password_TextField = findViewById(R.id.password_TextField_Ps);
        check_Password_TextField = findViewById(R.id.check_Password_TextField_Ps);
        name_TextField = findViewById(R.id.name_TextField_Ps);
        email_TextField = findViewById(R.id.email_TextField_Ps);
        phone_Number_TextField = findViewById(R.id.phoneNumber_TextField_Ps);
        // 텍스트 필드들 정의 끝

        gender_Spinner = findViewById(R.id.gender_Spinner_S); // 스피너 정의

        complete_Button = findViewById(R.id.complete_Button_Ps); // 완료 버튼 정의
        doubleCheck_Button = findViewById(R.id.doubleCheck_Button_S); // id 중복 확인 버튼 정의
    }

    // 문자 입력 이벤트 클래스
    class TextWatchers implements TextWatcher {
        private int check;

        // 1 = 아이디
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
            String msg = s.toString();

            if (msg.equals("") || msg == null) {
                // 입력된 무언가가 공백

            } else {
                if (check == 1) { // 아이디 - 제한적 특수문자(! ^ * - _ / ? ~ ` + = " ")와 영문, 숫자의 조합 33 94 42 45 95 47 63 126 96 43 61 32
                    for (int i = 0; i < msg.length(); i++) {
                        int x = msg.charAt(i);

                        switch (x / 10) {
                            case 3:
                                if (x == 32 || x == 33) { // " " !
                                } else {
                                    // 허용하지 않은 문자 사용
                                    unacceptable_Char();
                                }
                                break;
                            case 4:
                                if (x == 42 || x == 43 || x == 45 || x >= 47) { // * + - / and 48 ~ 49 숫자
                                } else {
                                    // 허용하지 않은 문자 사용
                                    unacceptable_Char();
                                }
                                break;
                            case 5:
                                if (x <= 57) { // 50 ~ 57 숫자 허용한 문자
                                } else {
                                    // 허용하지 않은 문자 사용
                                    unacceptable_Char();
                                }
                                break;
                            case 6:
                                if (x == 61 || x == 63 || x >= 65) { // = ? and 65 ~ 69 대문자 영문
                                } else {
                                    // 허용하지 않은 문자 사용
                                    unacceptable_Char();
                                }
                                break;
                            case 9:
                                if (x >= 94 || x == 90) { // ^ _ ` and 97 ~ 99 소문자 영문 and 90 대문자 영문
                                } else {
                                    // 허용하지 않은 문자 사용
                                    unacceptable_Char();
                                }
                                break;
                            case 12:
                                if (x <= 122 || x == 126) { // ~ and 120 ~ 122 소문자 영문
                                } else {
                                    // 허용하지 않은 문자 사용
                                    unacceptable_Char();
                                }
                                break;
                            case 7: // 7 ~ 11, 모두 허용된 문자
                            case 8:
                            case 10:
                            case 11:
                                break;
                            default: // 제한적 특수문자 + 영문 + 숫자를 제외한 문자가 들어간 경우
                                unacceptable_Char();
                                break;
                        }
                    }
                } else if (check == 2) { // 비밀번호 – 특수문자와 영문, 숫자의 조합
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
                        check_Password_TextField.setHint("비밀번호가 일치하지 않습니다.");
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