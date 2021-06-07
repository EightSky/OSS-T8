package com.example.opensourcesoftwareproject_team;
// 참조 : https://lakue.tistory.com/34 - 페이징 버튼
// https://gun0912.tistory.com/71 - 간편화 라이브러리 - 미적용

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.pagingbutton.LakuePagingButton;
import com.lakue.pagingbutton.OnPageSelectListener;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {
    ArrayList<Post> list = new ArrayList<Post>();
    RecyclerView postList; // 게시글 목록 리사이클러뷰
    LakuePagingButton page_Button; // 페이징 버튼
    Button myPage_Button; // 마이페이지 버튼
    Button all_Button; // 전체글 버튼
    Button buy_Button; // 구매글 버튼
    Button sale_Button; // 판매글 버튼
    Button writePost_Button; // 글쓰기 버튼

    DatabaseManagement dm; // 데이터베이스 객체

    private String setTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_layout);
        set();
    }

    protected void onResume() {
        super.onResume();
        setList(1);

        postList.setLayoutManager(new LinearLayoutManager(this)); // 리사이클러뷰에 LinearLayoutManager 객체 지정

        page_Button.addBottomPageButton(((dm.getPostsCount(setTag) - 1) / 10) + 1, 1); //총 페이지 버튼 수와 현재 페이지 설정
        page_Button.setOnPageSelectListener(new OnPageSelectListener() {
            @Override
            public void onPageBefore(int now_page) {
                page_Button.addBottomPageButton((list.size() / 10) + 1,now_page);
                Toast.makeText(MainPage.this, ""+now_page, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageCenter(int now_page) {
                setList(now_page);
            }

            //NextButton Click
            @Override
            public void onPageNext(int now_page) {
                page_Button.addBottomPageButton((list.size() / 10) + 1,now_page);
                Toast.makeText(MainPage.this, ""+now_page, Toast.LENGTH_SHORT).show();
            }
        });

        // MyPage 버튼 클릭 리스너 선언 및 정의
        myPage_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 버튼 클릭 시 회원가입 화면으로 전환
                // 로그인 화면 위로 회원가입 화면을 올리는 방식 - 뒤로가기를 눌렀을때 로그인 화면으로 이동 됨
                Intent myPage = new Intent(getApplicationContext(), MyPage.class);
                myPage.putExtra("DBM", dm);
                myPage.putExtra("USER", (User) getIntent().getSerializableExtra("USER"));
                startActivity(myPage);
            }
        });

        // 구매글 버튼 클릭 리스너 선언 및 정의
        all_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시글 목록 구매 태그로 변경
                setTag = "";
                setList(1);
                page_Button.addBottomPageButton(((dm.getPostsCount(setTag) - 1) / 10) + 1, 1); //총 페이지 버튼 수와 현재 페이지 설정
            }
        });

        // 구매글 버튼 클릭 리스너 선언 및 정의
        buy_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시글 목록 구매 태그로 변경
                setTag = "buy";
                setList(1);
                page_Button.addBottomPageButton(((dm.getPostsCount(setTag) - 1) / 10) + 1, 1); //총 페이지 버튼 수와 현재 페이지 설정
            }
        });

        // 판매글 버튼 클릭 리스너 선언 및 정의
        sale_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시글 목록 판매 태그로 변경
                setTag = "sale";
                setList(1);
                page_Button.addBottomPageButton(((dm.getPostsCount(setTag) - 1) / 10) + 1, 1); //총 페이지 버튼 수와 현재 페이지 설정
            }
        });

        // 글쓰기 버튼 클릭 리스너 선언 및 정의
        writePost_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 글쓰기 버튼 클릭 시 글쓰기 화면으로 전환
                // 메인 화면 위로 글쓰기 화면을 올리는 방식 - 뒤로가기를 눌렀을때 메인 화면으로 이동 됨
                Intent writePost_Page = new Intent(getApplicationContext(), WritePostPage.class);
                writePost_Page.putExtra("DBM", dm);
                writePost_Page.putExtra("USER", (User) getIntent().getSerializableExtra("USER"));
                startActivity(writePost_Page);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        postList.setAdapter(null);
        postList.setLayoutManager(null);
        page_Button.setOnPageSelectListener(null);
        myPage_Button.setOnClickListener(null);
        buy_Button.setOnClickListener(null);
        sale_Button.setOnClickListener(null);
        writePost_Button.setOnClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dm = null;

        postList.setAdapter(null);
        postList.setLayoutManager(null);
        page_Button.setOnPageSelectListener(null);
        myPage_Button.setOnClickListener(null);
        buy_Button.setOnClickListener(null);
        sale_Button.setOnClickListener(null);
        writePost_Button.setOnClickListener(null);

        postList = null;
        page_Button = null;
        myPage_Button = null;
        buy_Button = null;
        sale_Button = null;
        writePost_Button = null;
    }

    void set() {
        dm = (DatabaseManagement) getIntent().getSerializableExtra("DBM");

        all_Button = findViewById(R.id.all_Button_Mp);
        postList = findViewById(R.id.postList_Recyclerview_Mpp);
        page_Button = findViewById(R.id.lpb_buttonlist_Mpp);
        myPage_Button = findViewById(R.id.myPage_Button_Mp);
        buy_Button = findViewById(R.id.buy_Button_Mp);
        sale_Button = findViewById(R.id.sale_Button_Mp);
        writePost_Button = findViewById(R.id.writePost_Button_Mp);
    }

    // 게시판 목록 설정 메소드
    // 전부 출력, 구매, 판매
    void setList(int page) {
        ArrayList<String> postInfomation = dm.getPostInfomation(setTag);
        int count;

        list = null;
        list = new ArrayList<Post>();

        for (int i = postInfomation.size() - 1; i >= 0; i -= 6) {
            count = Integer.parseInt(postInfomation.get(i));

            if ((count - (i / 6)) / 10 == (page - 1)) {
                list.add(new Post(postInfomation.get(i - 5), postInfomation.get(i - 4), postInfomation.get(i - 3),
                        "", postInfomation.get(i - 2).replace("sale", "판매").replace("buy", "구매"),
                        postInfomation.get(i - 1), null, null, null, null, null, null, null, null));
            }
        }

        postList.setAdapter(new Recycler_Adapter(list, dm, this)); // 리사이클러뷰에 Recycler_Adapter 객체 지정
    }
}