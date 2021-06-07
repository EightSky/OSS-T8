package com.example.opensourcesoftwareproject_team;

import android.media.Image;
import android.widget.ImageView;

import java.util.Date;

public class Post {
    String userName; // 글쓴이 이름
    String title; // 글 제목
    String write_Date; // 쓴 날짜
    String content; // 글 내용
    String tag; // 글 태그 (판매 상태 관련)
    String image1;
    String image2;
    String image3;
    String image4;
    String image5;
    String image6;
    String image7;
    String image8;
    String image9;

    Post(String un, String tt, String wd, String ct, String t, String i1, String i2,String i3,String i4,String i5,String i6,String i7,String i8,String i9) {
        userName = un;
        title = tt;
        write_Date = wd;
        content = ct;
        tag = t;
        image1 = i1;
        image2 = i2;
        image3 = i3;
        image4 = i4;
        image5 = i5;
        image6 = i6;
        image7 = i7;
        image8 = i8;
        image9 = i9;
    }
}
