package kr.hkit.iotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import kr.hkit.iotmobile.preference.AddressPreference;
import kr.hkit.iotmobile.socket.SocketClient;
import kr.hkit.iotmobile.socket.SocketClientListener;

public class DoorLockControlActivity extends AppCompatActivity {
    private AddressPreference ap;

    Button DoorLockButton;
    ImageView   DoorLockCheck;

    SocketClient socketClient;

    String ip;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_doorlock);

        DoorLockButton = findViewById(R.id.DoorLockButton);
        DoorLockButton.setOnClickListener(onDoorLookClickListener);

        Button ComSettingButton = findViewById(R.id.ComSettingButton);
        ComSettingButton.setOnClickListener(onWiFiClickListener);

        DoorLockCheck = findViewById(R.id.DoorLockCheck);
    }

    private View.OnClickListener onDoorLookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ap = new AddressPreference(getBaseContext());

            ip = ap.getIp();
            port = ap.getPort();

            socketClient = new SocketClient(ip, port, "D.O", socketClientListener); // 클라이언트(앱)에서 요청
            socketClient.execute();
        }
    };

    private View.OnClickListener onWiFiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(),SettingActivity.class); // 통신 설정 화면으로
        startActivity(intent);

        }
    };

    private SocketClientListener socketClientListener = new SocketClientListener(){
        @Override
        public void onReceiveMessage(String receiveMessage){// 받는 데이터
            String door_msg = receiveMessage.substring(0,6);
            Log.e("TCPIP","받은 그대로의 메세지 : "+receiveMessage);

            if(door_msg.equals("DOOR.O")) {
                DoorLockCheck.setImageResource(R.drawable.opened);
            }
        }
    };
}
