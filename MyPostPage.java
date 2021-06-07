package com.example.opensourcesoftwareproject_team;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.pagingbutton.LakuePagingButton;
import com.lakue.pagingbutton.OnPageSelectListener;

import java.util.ArrayList;

public class MyPostPage extends AppCompatActivity {
    ArrayList<Post> list = new ArrayList<Post>();
    RecyclerView postList; // 게시글 목록 리사이클러뷰
    LakuePagingButton page_Button;
    ImageButton previousPage_ImageButton;
    DatabaseManagement dm;
    User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypostpage_layout);
        set();
    }

    protected void onResume() {
        super.onResume();

        setList(user.getId(), 1);

        page_Button.addBottomPageButton(((dm.getPostsCount("") - 1) / 10) + 1, 1); //총 페이지 버튼 수와 현재 페이지 설정
        page_Button.setOnPageSelectListener(new OnPageSelectListener() {
            @Override
            public void onPageBefore(int now_page) {
                page_Button.addBottomPageButton((list.size() / 10) + 1,now_page);
                Toast.makeText(MyPostPage.this, ""+now_page, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageCenter(int now_page) {
                setList(user.getId() ,now_page);
            }

            //NextButton Click
            @Override
            public void onPageNext(int now_page) {
                page_Button.addBottomPageButton((list.size() / 10) + 1,now_page);
                Toast.makeText(MyPostPage.this, ""+now_page, Toast.LENGTH_SHORT).show();
            }
        });

        postList.setLayoutManager(new LinearLayoutManager(this)); // 리사이클러뷰에 LinearLayoutManager 객체 지정

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

        page_Button.setOnPageSelectListener(null);

        postList.setAdapter(null);
        postList.setLayoutManager(null);
        previousPage_ImageButton.setOnClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        page_Button.setOnPageSelectListener(null);

        postList.setAdapter(null);
        postList.setLayoutManager(null);
        previousPage_ImageButton.setOnClickListener(null);

        dm = null;
        user = null;
        previousPage_ImageButton = null;
        postList = null;
        list = null;
    }

    void set() {
        dm = (DatabaseManagement) getIntent().getSerializableExtra("DBM");
        user = (User) getIntent().getSerializableExtra("USER");

        page_Button = findViewById(R.id.lpb_buttonlist_Mpp);
        previousPage_ImageButton = findViewById(R.id.previousPage_ImageButton_Mpp);
        postList = findViewById(R.id.postList_Recyclerview_Mpp);
    }

    void setList(String id, int page) {
        ArrayList<String> postInfomation = dm.getPostInfomation_Id(id);
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

        System.out.println("페이지 수" + dm.getPostsCount("") / 10);

        postList.setAdapter(new Recycler_Adapter(list, dm, this)); // 리사이클러뷰에 Recycler_Adapter 객체 지정
    }
}
