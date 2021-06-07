package com.example.opensourcesoftwareproject_team;
// https://youngest-programming.tistory.com/54 - 이미지 다중 선택
// https://webnautes.tistory.com/1240

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class WritePostPage extends AppCompatActivity {
    final int PICTURE_REQUEST_CODE = 100;

    String[] tag = {"판매", "구매"}; // 스피너 재료
    Button complete_Button; // 완료 버튼
    EditText title_TextField; // 제목 텍스트 필드
    Spinner tag_Spinner; // 태그 스피너
    EditText price_TextField; // 가격 텍스트필드
    EditText contents_TextField; // 내용 텍스트 필드
    Button imageChange;

    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;
    ImageView image6;
    ImageView image7;
    ImageView image8;
    ImageView image9;

    DatabaseManagement dm; // 데이터베이스 객체
    User user;
    FileUtils fu = new FileUtils(this);

    static int count = 0;
    static ArrayList<String> uris = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writepost_layout);

        set();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TableRow.LayoutParams ll = new TableRow.LayoutParams(200, 200);

        if (requestCode == PICTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                image2.setPadding(0, 0, 0, 20);

                //ClipData 또는 Uri를 가져온다
                Uri uri = data.getData();

                ClipData clipData = data.getClipData();

                //이미지 URI 를 이용하여 이미지뷰에 순서대로 세팅한다.
                if (clipData != null) {
                    count = clipData.getItemCount();
                    uris = new ArrayList<>();
                    Uri sUri;

                    for (int i = 0; i < count; i++) {
                        sUri = clipData.getItemAt(i).getUri();

                        if ((i + 1) == 1) {
                            image1.setImageResource(0);
                            image1.setLayoutParams(ll);
                            image1.setPadding(0, 0, 0, 20);
                            image1.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 2) {
                            image2.setImageResource(0);
                            image2.setLayoutParams(ll);
                            image2.setPadding(0, 0, 0, 20);
                            image2.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 3) {
                            image3.setImageResource(0);
                            image3.setLayoutParams(ll);
                            image3.setPadding(0, 0, 0, 20);
                            image3.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 4) {
                            image4.setImageResource(0);
                            image4.setLayoutParams(ll);
                            image4.setPadding(0, 0, 0, 20);
                            image4.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 5) {
                            image5.setImageResource(0);
                            image5.setLayoutParams(ll);
                            image5.setPadding(0, 0, 0, 20);
                            image5.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 6) {
                            image6.setImageResource(0);
                            image6.setLayoutParams(ll);
                            image6.setPadding(0, 0, 0, 20);
                            image6.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 7) {
                            image7.setImageResource(0);
                            image7.setLayoutParams(ll);
                            image7.setPadding(0, 0, 0, 20);
                            image7.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 8) {
                            image8.setImageResource(0);
                            image8.setLayoutParams(ll);
                            image8.setPadding(0, 0, 0, 20);
                            image8.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        } else if ((i + 1) == 9) {
                            image9.setImageResource(0);
                            image9.setLayoutParams(ll);
                            image9.setPadding(0, 0, 0, 20);
                            image9.setImageURI(sUri);
                            uris.add(fu.getPath(sUri));
                        }
                    }
                } else if (uri != null) {
                    image1.setImageURI(uri);
                }
            }
        }
    }

    protected void onResume() {
        super.onResume();

        imageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_REQUEST_CODE);
            }
        });

        complete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 입력된 게시글 정보를 체크한 후 DB에 저장
                Calendar c = Calendar.getInstance(); // 현재 시간 정보
                String timeNow = c.get(Calendar.YEAR) + "년 " + (c.get(Calendar.MONTH) + 1) + "월 " + c.get(Calendar.DATE) + "일, " + c.get(Calendar.HOUR_OF_DAY) + "시 " + c.get(Calendar.MINUTE) + "분 " + c.get(Calendar.SECOND) + "초";

                dm.postRegistration(user.getId(), title_TextField.getText().toString(), tag_Spinner.getSelectedItem().toString().replace("판매", "sale").replace("구매", "buy"), price_TextField.getText().toString(), contents_TextField.getText().toString(), timeNow);

                finish();
            }
        });

        tag_Spinner.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tag)); // 스피너 구성 요소 추가
    }

    @Override
    public void onPause() {
        super.onPause();

        complete_Button.setOnClickListener(null);
        tag_Spinner.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        complete_Button.setOnClickListener(null);
        tag_Spinner.setAdapter(null);

        tag = null;
        complete_Button = null;
        title_TextField = null;
        tag_Spinner = null;
        price_TextField = null;
        contents_TextField = null;
        dm = null;
    }

    void set() {
        dm = (DatabaseManagement) getIntent().getSerializableExtra("DBM");
        user = (User) getIntent().getSerializableExtra("USER");

        image1 = findViewById(R.id.imageView1_W);
        image2 = findViewById(R.id.imageView2_W);
        image3 = findViewById(R.id.imageView3_W);
        image4 = findViewById(R.id.imageView4_W);
        image5 = findViewById(R.id.imageView5_W);
        image6 = findViewById(R.id.imageView6_W);
        image7 = findViewById(R.id.imageView7_W);
        image8 = findViewById(R.id.imageView8_W);
        image9 = findViewById(R.id.imageView9_W);

        imageChange = findViewById(R.id.changeImage_Button_W);
        complete_Button = findViewById(R.id.complete_Button_W);
        title_TextField = findViewById(R.id.writeTitle_TextField_W);
        tag_Spinner = findViewById(R.id.tag_Spinner_W);
        price_TextField = findViewById(R.id.price_TextFiled_W);
        contents_TextField = findViewById(R.id.contents_TextField_W);
    }
}
