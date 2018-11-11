package kr.hkit.iotmobile;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import kr.hkit.iotmobile.preference.AddressPreference;
import kr.hkit.iotmobile.socket.SocketClient;
import kr.hkit.iotmobile.socket.SocketClientListener;

public class DeviceStatusActivity extends AppCompatActivity {
    private AddressPreference ap;

    SocketClient socketClient;

    String ip;
    int port;

    ImageView light_State;
    ImageView curtain_State;
    ImageView doorLock_State;
    ImageView window_State;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_status);

        light_State = findViewById(R.id.lightState);
        curtain_State = findViewById(R.id.curtainState);
        doorLock_State = findViewById(R.id.doorLockState);
        window_State = findViewById(R.id.windowState);

        ImageButton RefreshButton    = findViewById(R.id.RefreshButton);
        RefreshButton.setOnClickListener(onRefreshClickListener);

        Button TcpIpSetting = findViewById(R.id.TcpIpSetting);
        TcpIpSetting.setOnClickListener(onTcpIpClickListener);
    }
    @Override
    protected void onStart(){
        super.onStart();
        // 여기서 처음으로 데이터 읽고 버튼을 누를 때마다 상태 확인
    }

    private View.OnClickListener onTcpIpClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View V){
            Intent intent = new Intent(getBaseContext(),SettingActivity.class); // TCP/IP 세팅 화면으로
            startActivity(intent);
        }
    };

    private View.OnClickListener onRefreshClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ap = new AddressPreference(getBaseContext());

            ip = ap.getIp();
            port = ap.getPort();

            if(ip != null && port != 0) {

                Toast.makeText(getApplicationContext(), R.string.all_device_check_msg, Toast.LENGTH_SHORT).show();

                socketClient = new SocketClient(ip, port, "REQA", socketClientListener); // 클라이언트(앱)에서 요청
                socketClient.execute();
                socketClient = null;
            }else{
                Toast.makeText(getApplicationContext(),R.string.com_check_msg,Toast.LENGTH_SHORT).show();
                Log.e("TCPIP","IP PORT null");
            }
        }
    };

    private SocketClientListener socketClientListener = new SocketClientListener(){
        @Override
        public void onReceiveMessage(String receiveMessage){// 받는 데이터
            doorLock_State.setImageResource(R.drawable.locked);/// 구현 하기 귀찮으니 락 상태로 고정
            Log.e("TCPIP","받은 그대로의 메세지 : "+receiveMessage);

            String light_value = receiveMessage.substring(0,1);
            String light_value2 = receiveMessage.substring(0,2);
            String division1 = receiveMessage.substring(1,2);
            String division2 = receiveMessage.substring(2,3);
            int recv_pwm;

            if(receiveMessage.equals("Connection refused")){
                Toast.makeText(getApplicationContext(),"연결이 거부됐습니다.",Toast.LENGTH_SHORT).show();
                return;
            }

            try{
                if(division1.equals("_")){
                    recv_pwm = Integer.parseInt(light_value);
                }else if(division2.equals("_")){
                    recv_pwm = Integer.parseInt(light_value2);
                }
                light_State.setImageResource(R.drawable.light_on);
            }catch (Exception e){
                light_State.setImageResource(R.drawable.light_off);
            }
        }
    };
}
