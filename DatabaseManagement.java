import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.ArrayList;

// DB 관리를 위한 클래스
public class DatabaseManagement {
    Connection con = null; // Connection 객체
    Statement stmt = null; // Statement 객체
    String url = "jdbc:mysql://localhost/stock?serverTimezone=Asia/Seoul";
    String user = "root"; // id
    String passwd = "!bh08152562"; // password

    DatabaseManagement() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, passwd);
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 테이블 생성 메소드
    // 총 5개의 테이블의 존재여부를 확인한 후 존재하지 않는 경우에만 테이블 생성
    void craeteTable() {
        String create_Member_Table = "create table if not exists member_Information (id varchar(50) primary key , " +
                "password varchar(50) not null , name varchar(50) not null , gender varchar(30) not null , email varchar(90) not null , phone_number varchar(50) not null);";
        String create_Posts_Table = "create table if not exists posts_Information (id varchar(50) nou null , title varchar(100) not null , " +
                "price varchar(30) not null , contents varchar(200) not null , write_time varchar(60) not null , tag varchar(50) not null, image1 blob not null, " +
                "image2 blob, image3 blob, image4 blob, image5 blob, image6 blob, image7 blob, image8 blob, image9 blob);";

        try {
            stmt.executeUpdate(create_Member_Table);
            stmt.executeUpdate(create_Posts_Table);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // 로그인 정보 확인 메소드
    boolean login(String id, String password) {
        boolean rt = false;
        try {
            String query = "select id from member_Information where id=\"" + id + "\";";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) { // 해당 아이디가 존재할 경우
                query = "select password from member_Information where id=\"" + id + "\";";

                rs = stmt.executeQuery(query);


                if (rs.next()) { // 해당 아이디를 사용하는 유저 비밀번호가 존재할 경우

                    if (rs.getString(rs.getString("password")).equals(password)) { // 해당 비밀번호와 전달받은 비밀번호가 동일한 경우
                        rt = true; // 비밀번호 일치
                    }
                }
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("로그인 정보 확인 오류");
        }

        return rt;
    }

    void removePost(String id, String title) {
        //DELETE FROM posts_information where id='11' and title ='test';
        String query = "delete from posts_Information where id=\"" + id + "\" and title=\"" + title + "\"";

        try {
            stmt.executeQuery(query);
        } catch (Exception e) {
            System.out.println("게시글 삭제 오류");
        }
    }

    // 회원가입 정보 등록 메소드
    void signUp(String id, String password, String name, String gender, String email, String phone_number) {

        if (double_Check(id)) {
            try {
                String query = "insert into member_Information values (\"" + id + "\", \"" + password + "\", \"" + name + "\", \"" + gender + "\", \"" + email + "\", \"" + phone_number + "\")";
                stmt.executeQuery(query);
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
        boolean value = false;
        ResultSet rs;
        try {
            rs = stmt.executeQuery(query);

            if (rs.next()) { // 해당 아이디가 이미 존재할 경우
                rs.close();
                value = false;
            } else { // 존재하지 않을 경우
                rs.close();
                value = true;
            }
        } catch (SQLException e) {
            System.out.println("아이디 중복 확인 오류");
        }
        return value;
    }

    // 태그에 맞는 게시글 불러오기
    ArrayList<String> getPostInfomation(String tag) {
        String query;
        ArrayList<String> columns = new ArrayList<String>();
        ResultSet rs = null;

        if (tag.equals("")) {
            query = "select * from posts_Information";
        } else {
            query = "select * from posts_Information where tag=\"" + tag + "\"";
        }

        ResultSetMetaData rsmd = null;

        try {
            rs = stmt.executeQuery(query);

            if (rs != null) {
                rsmd = rs.getMetaData();

                while (rs.next()) {
                    columns.add(rs.getString(1)); // id or name 가져오기
                    columns.add(rs.getString(2)); // title 가져오기
                    columns.add(rs.getString(5)); // time 가져오기
                    columns.add(rs.getString(6)); // tag 가져오기
                    columns.add(String.valueOf(rsmd.getColumnCount())); // 행 번호
                }

                rs.close();
            }
        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        return columns; // 게시글 정보가 들어있는 문자열 배열 반환
    }

    // 내 게시글 불러오기
    ArrayList<String> getPostInfomation_Id(String id) {
        ArrayList<String> columns = new ArrayList<String>();
        ResultSet rs = null;

        String query = "select * from posts_Information where id=\"" + id + "\"";

        ResultSetMetaData rsmd = null;

        try {
            rs = stmt.executeQuery(query);

            if (rs != null) {
                rsmd = rs.getMetaData();

                while (rs.next()) {
                    columns.add(rs.getString(1)); // id or name 가져오기
                    columns.add(rs.getString(2)); // title 가져오기
                    columns.add(rs.getString(5)); // time 가져오기
                    columns.add(rs.getString(6)); // tag 가져오기
                    columns.add(String.valueOf(rsmd.getColumnCount())); // 행 번호
                }

                rs.close();
            }
        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        return columns; // 게시글 정보가 들어있는 문자열 배열 반환
    }

    // 게시글 검색 메소드
    ArrayList<String> getPostInfomation_Title(String title) {
        ArrayList<String> columns = new ArrayList<String>();
        ResultSet rs = null;

        String query = "select * from posts_Information where title like \"%" + title + "%\"";

        ResultSetMetaData rsmd = null;

        try {
            rs = stmt.executeQuery(query);

            if (rs != null) {
                rsmd = rs.getMetaData();

                while (rs.next()) {
                    columns.add(rs.getString(1)); // id or name 가져오기
                    columns.add(rs.getString(2)); // title 가져오기
                    columns.add(rs.getString(5)); // time 가져오기
                    columns.add(rs.getString(6)); // tag 가져오기
                    columns.add(String.valueOf(rsmd.getColumnCount())); // 행 번호
                }

                rs.close();
            }
        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        return columns; // 게시글 정보가 들어있는 문자열 배열 반환
    }

    void postRegistration(String id, String title, String tag, String price, String content, String time) {
        String query = "insert into posts_Information values (\"" + id + "\", \"" + title + "\", \"" + price + "\", \"" + content + "\", \"" + time + "\", \"" + tag + "\")";

        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("게시글 추가 오류");
        }
    }

    void updateUserInformation(String id, String password, String name, String email, String phoneNumber) {
        String query = "update member_Information set password = \"" + password + "\", name = \"" + name + "\"," +
                " email = \"" + email + "\", " + "phone_number = \"" + phoneNumber + "\" where id = \"" + id + "\"";

        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("유저 정보 업데이트 오류");
        }
    }

    String getUserInformation(String id) {
        String query = "select name, email from member_Information where id=\"" + id + "\"";
        String value = "";
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery(query);

            if (rs != null) {
                if (rs.next()) {
                    value += rs.getString(1);
                    value += ",";
                    value += rs.getString(2);
                }
            }
        } catch (Exception e) {
            System.out.println("유저 정보가 없습니다.");
        }

        return value;
    }

    String getUser_Gender(String id) {
        String query = "select gender from member_Information where id = \"" + id + "\"";
        String value = "";
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery(query);

            if (rs != null) {
                if (rs.next()) {
                    value += rs.getString(1);
                }
            }
        } catch (Exception e) {
            System.out.println("유저 성별 정보가 없습니다.");
        }

        return value;
    }

    int getPostsCount(String tag) {
        String query;
        ResultSet rs = null;
        int count = 0;

        if (tag.equals("")) {
            query = "select count(*) from posts_Information";
        } else {
            query = "select count(*) from posts_Information where tag=\"" + tag + "\"";
        }

        try {
            rs = stmt.executeQuery(query);

            if (rs != null) {
                if (rs.next()) {
                    count = rs.getInt(1);

                }
                rs.close();
            }

        } catch (Exception e) {
            System.out.println("게시글이 없습니다.");
        }

        return count;
    }
}
