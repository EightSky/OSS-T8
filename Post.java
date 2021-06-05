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
    ImageView image;

    Post(String un, String tt, String wd, String ct, String t){
        userName = un;
        title = tt;
        write_Date = wd;
        content = ct;
        tag = t;
    }
}
