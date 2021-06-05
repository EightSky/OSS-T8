package com.example.opensourcesoftwareproject_team;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_Member_Table()); // 유저 정보 테이블 생성
        db.execSQL(create_Posts_Table()); // 게시글 테이블 생성
        db.execSQL(create_Comments_Table()); // 댓글 테이블 생성
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists mytable";

        db.execSQL(sql);
        onCreate(db);
    }

    String create_Member_Table() {
        String create_Member_Table = "create table if not exists member_Information (id text primary key , " +
                "password text not null , name text not null , gender text not null , email text not null , phone_number text not null);";
        return create_Member_Table;
    }

    String create_Posts_Table() {
        String create_Posts_Table = "create table if not exists posts_Information (id text nou null , title text not null , " +
                "price text not null , contents text not null , write_time text not null , tag text not null, image1 blob not null, " +
                "image2 blob, image3 blob, image4 blob, image5 blob, image6 blob, image7 blob, image8 blob, image9 blob);";

        return create_Posts_Table;
    }

    String create_Comments_Table() {
        String create_Comments_Table = "create table if not exists comments_Information (id text primary key , title text not null , " +
                "comment_user_id text not null , comment_content text not null);";

        return create_Comments_Table;
    }
}
