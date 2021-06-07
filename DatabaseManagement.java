package com.example.opensourcesoftwareproject_team;
// https://whitememo.tistory.com/241
// https://citynetc.tistory.com/150 - 사진 저장
// https://yoo-hyeok.tistory.com/16?category=708422

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseManagement implements Serializable {
    static SQLiteDatabase db;
    static DBHelper helper;

    public DatabaseManagement(Context context) {
        helper = new DBHelper(context, "OSSWP.db", null, 1);
        db = helper.getWritableDatabase();
    }

    // 선택한 게시글 정보
    ArrayList<String> getPostInformation(String id, String title) {
        String query = "select * from posts_Information where id=\"" + id + "\" and title=\"" + title + "\"";
        ArrayList<String> columns = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            if (c.moveToNext()) {
                columns.add(c.getString(2)); //
                columns.add(c.getString(3)); //
                columns.add(c.getString(6)); //
            }

            for (int i = 7; i < 15; i++) {
                String uri = c.getString(i);
                columns.add(uri);
            }
            columns.add(String.valueOf(c.getPosition())); // 행 번호

            c.close();
        }

        return columns;
    }

    // 로그인 정보 확인 메소드
    boolean login(String id, String password) {
        String query = "select id from member_Information where id=\"" + id + "\";";
        Cursor c = db.rawQuery(query, null);
        boolean rt = false;

        if (c.moveToNext()) { // 해당 아이디가 존재할 경우
            query = "select password from member_Information where id=\"" + id + "\";";
            c = db.rawQuery(query, null);

            if (c.moveToNext()) { // 해당 아이디를 사용하는 유저 비밀번호가 존재할 경우

                if (c.getString(c.getColumnIndex("password")).equals(password)) { // 해당 비밀번호와 전달받은 비밀번호가 동일한 경우
                    rt = true; // 비밀번호 일치
                } else { // 일치하지 않을 경우
                    rt = false; // 비밀번호 불일치
                }
            } else {
                rt = false; // 해당 아이디가 존재하지만 비밀번호가 존재하지 않음 - 일반적인 경우로는 불가능
            }
        } else {
            rt = false; // 아이디가 존재하지 않음
        }
        c.close();

        return rt;
    }

    void removePost(String id, String title) {
        //DELETE FROM posts_information where id='11' and title ='test';
        String query = "delete from posts_Information where id=\"" + id + "\" and title=\"" + title + "\"";

        try {
            db.execSQL(query);
        } catch (Exception e) {
            System.out.println("게시글 삭제 오류");
        }
    }

    // 회원가입 정보 등록 메소드
    void signUp(String id, String password, String name, String gender, String email, String phone_number) {

        if (double_Check(id)) {
            try {
                String query = "insert into member_Information values (\"" + id + "\", \"" + password + "\", \"" + name + "\", \"" + gender + "\", \"" + email + "\", \"" + phone_number + "\")";
                db.execSQL(query);
            } catch (Exception e) {
                System.out.println("회원정보 저장 오류");
            }
        } else {
            System.out.println("아이디 중복");
        }
    }

    // 아이디 중복 확인 메소드
    boolean double_Check(String id) {
        String query = "select id from member_Information where id=\"" + id + "\"";
        Cursor c = db.rawQuery(query, null);

        if (c.moveToNext()) { // 해당 아이디가 이미 존재할 경우
            c.close();

            return false;
        } else { // 존재하지 않을 경우
            c.close();

            return true;
        }
    }

    // 태그에 맞는 게시글 불러오기
    ArrayList<String> getPostInfomation(String tag) {
        String query;
        ArrayList<String> columns = new ArrayList<String>();
        Cursor c = null;

        if (tag.equals("")) {
            query = "select * from posts_Information";
        } else {
            query = "select * from posts_Information where tag=\"" + tag + "\"";
        }

        try {
            c = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        if (c != null) {
            while (c.moveToNext()) {
                columns.add(c.getString(0)); // id or name 가져오기
                columns.add(c.getString(1)); // title 가져오기
                columns.add(c.getString(4)); // time 가져오기
                columns.add(c.getString(5)); // tag 가져오기
                columns.add(c.getString(6)); // 첫번째 사진 경로
                columns.add(String.valueOf(c.getPosition())); // 행 번호
            }

            c.close();
        }

        return columns; // 게시글 정보가 들어있는 문자열 배열 반환
    }

    // 내 게시글 불러오기
    ArrayList<String> getPostInfomation_Id(String id) {
        ArrayList<String> columns = new ArrayList<String>();
        Cursor c = null;

        String query = "select * from posts_Information where id=\"" + id + "\"";


        try {
            c = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        if (c != null) {
            while (c.moveToNext()) {
                columns.add(c.getString(0)); // id or name 가져오기
                columns.add(c.getString(1)); // title 가져오기
                columns.add(c.getString(4)); // time 가져오기
                columns.add(c.getString(5)); // tag 가져오기
                columns.add(c.getString(6)); // 첫번째 사진 경로
                columns.add(String.valueOf(c.getPosition())); // 행 번호
            }

            c.close();
        }

        return columns; // 게시글 정보가 들어있는 문자열 배열 반환
    }

    // 게시글 검색 메소드
    ArrayList<String> getPostInfomation_Title(String title) {
        ArrayList<String> columns = new ArrayList<String>();
        Cursor c = null;

        String query = "select * from posts_Information where title like \"%" + title + "%\"";

        try {
            c = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        if (c != null) {
            while (c.moveToNext()) {
                columns.add(c.getString(0)); // id or name 가져오기
                columns.add(c.getString(1)); // title 가져오기
                columns.add(c.getString(4)); // time 가져오기
                columns.add(c.getString(5)); // tag 가져오기
                columns.add(c.getString(6)); // 첫번째 사진 경로
                columns.add(String.valueOf(c.getPosition())); // 행 번호
            }

            c.close();
        }

        return columns; // 게시글 정보가 들어있는 문자열 배열 반환
    }

    void postRegistration(String id, String title, String tag, String price, String content, String time) {
        String query = "insert into posts_Information values (\"" + id + "\", \"" + title + "\", \"" + price + "\", \"" + content + "\", \"" + time + "\", \"" + tag;

        int count = WritePostPage.count;

        System.out.println("카운트 : " + count);

        for (int i = 0; i < count; i++) {
            query += "\", \"";
            query += WritePostPage.uris.get(i);
        }
        for (int i = 0; i < 9 - count; i++) {
            System.out.println("반복 횟수 : " + i);
            query += "\", \"";
            query += "null";
        }
        query += "\")";

        db.execSQL(query);
    }

    void updateUserInformation(String id, String password, String name, String email, String phoneNumber) {
        String query = "update member_Information set password = \"" + password + "\", name = \"" + name + "\"," +
                " email = \"" + email + "\", " + "phone_number = \"" + phoneNumber + "\" where id = \"" + id + "\"";

        db.execSQL(query);
    }

    String getUserInformation(String id) {
        String query = "select name, email from member_Information where id=\"" + id + "\"";
        String value = "";
        Cursor c = null;

        try {
            c = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("유저 정보가 없습니다.");
        }

        if (c != null) {
            if (c.moveToNext()) {
                value += c.getString(0);
                value += ",";
                value += c.getString(1);
            }
        }

        return value;
    }

    String getUser_Gender(String id) {
        String query = "select gender from member_Information where id = \"" + id + "\"";
        String value = "";
        Cursor c = null;

        try {
            c = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("유저 성별 정보가 없습니다.");
        }

        if (c != null) {
            if (c.moveToNext()) {
                value += c.getString(0);
            }
        }

        return value;
    }

    int getPostsCount(String tag) {
        String query;
        Cursor c = null;
        int count = 0;

        if (tag.equals("")) {
            query = "select * from posts_Information";
        } else {
            query = "select * from posts_Information where tag=\"" + tag + "\"";
        }

        try {
            c = db.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        if (c != null) {
            count = c.getCount();

            c.close();
        }

        return count;
    }
}