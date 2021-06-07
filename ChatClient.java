package com.example.opensourcesoftwareproject_team;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ChatClient {
    Socket socket; // 소켓 통신을 위한 소켓
    MessageListener ml; // 메시지 수신을 위한 MessageListener 클래스 객체 생성

    Scanner scn = null;

    OutputStream out = null; // OutputStream 객체 생성
    DataOutputStream dout = null; // DataOutputStream 객체 생성

    private String id;
    private String connectedUserId;

    // 생성자
    // 클라이언트의 CID 를 입력받고 서버와의 소켓을 연결
    // 서버에게 메시지를 전송하기 위한 각각의 객체 정의
    // 메시지 수신을 위한 MessageListener 클래스 객체 정의
    // MessageListener 객체의 쓰레드 실행
    ChatClient() {
        try {
            socket = new Socket("LocalHost", 55555); // 소켓 연결

            out = socket.getOutputStream();
            dout = new DataOutputStream(out);

            scn = new Scanner(System.in);
        } catch (IOException e) {
            System.out.println("소켓 설정 오류");
        }
    }

    // 연결 요청 형식 - REQUEST/(유저 아이디)id/(연결하고자 하는 유저 아이디)connectedUserId
    void connected(String id, String connectedUserId) {
        this.id = id;
        this.connectedUserId = connectedUserId;

        try {
            dout.writeUTF("REQUEST/" + id + "/" + connectedUserId);

            ml = new MessageListener(socket); // 메시지 리스너 정의
            ml.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 사용자의 입력을 받아 올바른 명령어일 경우 해당 작업을 위한 요청을 서버에게 전송하는 메소드
    void start() {
        while (true) {
            // 사용자 입력 받기
            // 전송 버튼을 클릭했을 때

            sendMessage("");
        }
    }

    void sendMessage(String content) {
        try {
            dout.writeUTF("SENDMESSAGE/" + connectedUserId + "/" + content);
        } catch (IOException e) {
            System.out.println("메시지 전송 오류");
        }
    }

    void requestQuery(String sql) {
        try {
            dout.writeUTF(sql);
        } catch (IOException e) {
            System.out.println("query 전송 오류");
        }
    }

    // 메시지 수신을 위한 클래스
    class MessageListener extends Thread {
        Socket socket; // 소켓 통신을 위한 소켓
        InputStream in = null; // InputStream 객체 생성
        DataInputStream din = null; // DataInputStream 객체 생성
        StringTokenizer st; // 토큰 사용을 위한 StringTokenizer 객체 정의

        // 생성자
        MessageListener(Socket s) {
            socket = s;

            try {
                in = socket.getInputStream();
                din = new DataInputStream(in);
            } catch (IOException e) {
                System.out.println("클라이언트 I/O 객체 생성 오류");
            }
        }

        // Thread 실행 메소드
        public void run() {
            String type;

            while (true) {
                try {
                    String responseMsg = din.readUTF();

                    st = new StringTokenizer(responseMsg, "/");

                    if (st.hasMoreTokens()) {
                        type = st.nextToken();

                        if (type.equals("ECHOMESSAGE")) {
                            if (st.nextToken().equals(id)) {
                                String content = st.nextToken();

                                // 수신한 content ui에 출력
                            }
                        }
                    }

                } catch (IOException e) {
                    System.out.println("메시지 수신 오류");
                }
            }
        }
    }
}