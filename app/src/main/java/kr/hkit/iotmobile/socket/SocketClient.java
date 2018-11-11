package kr.hkit.iotmobile.socket;

import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient extends AsyncTask<Void, Void, Void> {

    private static final int BUFF_SIZE = 1024;

    private String ip;
    private int port;
    private String sendMessage;
    private String receiveMessage;
    private SocketClientListener socketClientListener;

    public SocketClient(String ip, int port, String sendMessage, SocketClientListener socketClientListener) {
        this.ip = ip;
        this.port = port;
        this.sendMessage = sendMessage;
        this.socketClientListener = socketClientListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Socket socket = new Socket(ip, port);

            //출력 스트림
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            //서버에 데이터 송신
            writer.write(sendMessage);
            writer.flush();
            //서버에서 데이터 수신
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            char[] cbuf = new char[BUFF_SIZE];
            StringBuilder sb = new StringBuilder();
            while(reader.read(cbuf) != -1) {
                sb.append(cbuf);
            }
            receiveMessage = sb.toString();
            writer.close();
            reader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            receiveMessage = e.getMessage();
            Log.e("TCPIP", "Method called on the UI thread", new Exception("STACK TRACE"));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(socketClientListener !=null) {                           //널포인터 참조 오류
            socketClientListener.onReceiveMessage(receiveMessage);
            Log.e("TCPIP","Null Pointer Error");
        }
    }
}