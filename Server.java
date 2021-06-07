// https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=hyoun1202&logNo=220015124971 파일 수신

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server {
    private ServerSocket ss; // 서버 소켓 객체 선언
    ArrayList<Client> clients = new ArrayList<Client>(); // 클라이언트 관리를 위한 어레이 리스트 정의
    ArrayList<UsersId> conChatUserList = new ArrayList<UsersId>();
    DatabaseManagement dm;

    // 실행 메소드
    public static void main(String[] args) {
        Server s = new Server();
        s.start(s);
    }

    // 소켓 생성 및 연결을 위한 메소드
    private void start(Server server) {
        try {
            dm = new DatabaseManagement();
            dm.craeteTable();
            ss = new ServerSocket(55555); // 서버 소켓 객체 생성

            System.out.println("서버 생성 완료");
            while (true) {
                Socket s = ss.accept();
                Client c = new Client(s, server); // 연결될 클라이언트 객체 생성
                clients.add(c); // 클라이언트 명단 추가
                c.start();

                Receiver receiver = new Receiver(s);
                receiver.start();
            }
        } catch (IOException e) {
            System.out.println("서버소켓 및 클라이언트 소켓 생성 오류");
        }
    }
}

class Client extends Thread {
    private final Server server; // 서버의 ArrayList에 접근하기 위한 서버 객체
    Socket socket; // 클라이언트와 연결될 소켓

    StringTokenizer st; // 토큰 사용을 위한 StringTokenizer 객체 정의

    InputStream in = null; // InputStream 객체 생성
    DataInputStream din = null; // DataInputStream 객체 생성
    OutputStream out = null; // OutputStream 객체 생성
    DataOutputStream dout = null; // DataOutputStream 객체 생성

    boolean connected = false;
    private String userId;

    Client(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            in = socket.getInputStream();
            din = new DataInputStream(in);
            out = socket.getOutputStream();
            dout = new DataOutputStream(out);
        } catch (IOException e) {
            System.out.println("클라이언트 I/O 객체 생성 오류");
        }
    }

    Client selectClient(String id) {
        if (userId.equals(id)) {
            return this;
        }

        return null;
    }

    // 연결 요청 메시지 형식 : REQUEST/(해당유저)id/(연결하고자하는 유저)id - 최초 1회 필수
    // request 이후
    // 수신 메시지 형식 : SENDMESSAGE/(연결하고자하는 유저)id/(메시지 내용)
    // 송신 메시지 형식 : ECHOMESSAGE/(연결하고자하는 유저)id/(메시지 내용)
    public void run() {
        String type;
        String msg;

        while (true) {
            try {
                msg = din.readUTF(); // 메시지 수신

                st = new StringTokenizer(msg, "/");

                if (st.hasMoreTokens()) {
                    type = st.nextToken();

                    if (!connected && type.equals("REQUEST")) { // 아직 연결이 되지 않았고 연결 요청이 들어온다면
                        userId = st.nextToken(); // 해당 유저 아이디
                        String connectedUserId = st.nextToken(); // 연결하고자 하는 유저 아이디

                        server.conChatUserList.add(new UsersId(userId, connectedUserId)); // 명단에 추가

                        connected = true;
                    } else if (connected && type.equals("SENDMESSAGE")) {
                        String connectedUserId = st.nextToken();
                        String content = st.nextToken();

                        for (int i = 0; i < server.conChatUserList.size(); i++) {
                            if (server.conChatUserList.get(i).usersId[0].equals(userId)) {
                                if (server.conChatUserList.get(i).usersId[1].equals(connectedUserId)) { // 연결된 유저 목록에 있을 경우
                                    echoMessage(content, connectedUserId);
                                }
                            } else if (server.conChatUserList.get(i).usersId[0].equals(connectedUserId)) {
                                if (server.conChatUserList.get(i).usersId[1].equals(userId)) { // 연결된 유저 목록에 있을 경우
                                    echoMessage(content, connectedUserId);
                                }
                            } else {

                            }
                        }
                    }
                }


            } catch (IOException e) {
                System.out.println("메시지 송/수신 오류");
                break;
            }
        }
    }

    void echoMessage(String content, String connectedUserId) {
        try {
            dout.writeUTF("ECHOMESSAGE" + userId + "/" + content);

            for (int i = 0; i < server.clients.size(); i++) {
                if (server.clients.get(i).selectClient(connectedUserId) != null) {
                    server.clients.get(i).selectClient(connectedUserId).dout.writeUTF("ECHOMESSAGE" + connectedUserId + "/" + content);
                }
            }
        } catch (Exception e) {
            System.out.println("에코 메시지 전송 오류");
        }
    }

    // 소켓 연결 종료
    void closedSocket() {
        try {
            if (checkConnection()) { // 소켓이 연결 되있다면
                socket.close(); // 연결 종료

                connected = false;
            } else {
                System.out.println("이미 연결이 종료된 소켓입니다.");
            }
        } catch (IOException e) {
            System.out.println("소켓 연결 종료 오류");
        }
    }

    // 소켓이 연결되어 있는지 확인하는 메소드
    boolean checkConnection() {
        if (socket.isConnected() && !socket.isClosed()) {
            return true;
        }

        return false;
    }
}

class UsersId {
    String[] usersId = new String[2];

    UsersId(String userId, String connectedUserId) {
        usersId[0] = userId;
        usersId[1] = connectedUserId;
    }
}

class Receiver extends Thread {

    Socket socket;
    DataInputStream dis = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public Receiver(Socket socket) {
        this.socket = socket;
    }

    //@Override
    public void run() {

        try {
            dis = new DataInputStream(socket.getInputStream());
            String type = dis.readUTF();



            /*type값('file'또는 'msg')을 기준으로 파일이 전송됐는지 문자열이 전송됐는지 구분한다.*/

            if(type.equals("file")){
                //전송된 파일 쓰기!
                String result = fileWrite(dis);
                System.out.println("result : " + result);

            }else if(type.equals("msg")){
                //수신된 메세지 쓰기
                String result = getMsg(dis);
                System.out.println("result : " + result);
            }

            //클라이언트에 결과 전송 - 먹통이된다.
            //DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            //dos.writeUTF(result);

        }catch (IOException e) {
            System.out.println("run() Fail!");
            e.printStackTrace();
        }
    }

    private String fileWrite(DataInputStream dis){

        String result;
        String filePath = "C:/develop/testdata/receiver";

        try {
            System.out.println("파일 수신 작업을 시작합니다.");

            // 파일명을 전송 받고 파일명 수정
            String fileNm = dis.readUTF();
            System.out.println("파일명 " + fileNm + "을 전송받았습니다.");

            // 파일을 생성하고 파일에 대한 출력 스트림 생성
            File file = new File(filePath + "/" + fileNm);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            System.out.println(fileNm + "파일을 생성하였습니다.");

            // 바이트 데이터를 전송받으면서 기록
            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = dis.read(data)) != -1) {
                bos.write(data, 0, len);
            }

            //bos.flush();
            result = "SUCCESS";

            System.out.println("파일 수신 작업을 완료하였습니다.");
            System.out.println("받은 파일의 사이즈 : " + file.length());
        } catch (IOException e) {
            e.printStackTrace();
            result = "ERROR";
        }finally{
            try { bos.close(); } catch (IOException e) { e.printStackTrace(); }
            try { fos.close(); } catch (IOException e) { e.printStackTrace(); }
            try { dis.close(); } catch (IOException e) { e.printStackTrace(); }
        }

        return result;
    }

    private String getMsg(DataInputStream dis){

        String result;

        try {
            System.out.println("파일 수신 작업을 시작합니다.");

            // 파일명을 전송 받고 파일명 수정
            String msg = dis.readUTF();
            System.out.println("msg : " + msg);

            result = "SUCCESS";
            System.out.println("메세지 수신 작업을 완료하였습니다.");
        }catch (IOException e) {
            e.printStackTrace();
            result = "ERROR";
        }finally{
            try { dis.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        return result;
    }
}