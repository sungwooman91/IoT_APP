package kr.hkit.iotmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import kr.hkit.iotmobile.preference.AddressPreference;
import kr.hkit.iotmobile.socket.SocketClient;
import kr.hkit.iotmobile.socket.SocketClientListener;

public class WindowControlActivity  extends AppCompatActivity implements View.OnTouchListener {
    private AddressPreference ap;
    ImageView WindowImage;

    float old_xValue;

    SocketClient socketClient;

    String ip;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_window);

        WindowImage = findViewById(R.id.WindowImage);
        WindowImage.setOnTouchListener(this);

        Button ComSettingButton = findViewById(R.id.ComSettingButton);
        ComSettingButton.setOnClickListener(onTCPIPClickListener);
    }

    @Override
    protected void onStart(){
        super.onStart();

        ap = new AddressPreference(getBaseContext());

        ip = ap.getIp();
        port = ap.getPort();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String msg;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            old_xValue = event.getX();

        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            Log.i("windowXY","eventX : " + event.getX() );
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() < old_xValue) {
                WindowImage.setImageResource(R.drawable.opened_window);
                msg="WINDOW.O";
            } else {
                WindowImage.setImageResource(R.drawable.closed_window);
                msg="WINDOW.C";
            }
            if(ip !=null || port != 0) {
                Toast.makeText(getApplicationContext(),R.string.SIGN,Toast.LENGTH_SHORT).show();
                socketClient = new SocketClient(ip, port, msg, OnWindowListener);
                socketClient.execute();
            }else{
                Toast.makeText(getApplicationContext(),"IP나 PORT 설정을 확인하세요",Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private View.OnClickListener onTCPIPClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(),SettingActivity.class); // TCPIP 세팅 화면으로
            startActivity(intent);
        }
    };

    private SocketClientListener OnWindowListener = new SocketClientListener(){
        @Override
        public void onReceiveMessage(String receiveMessage) {
            // 피드백을 받을 수 없으니 받은 데이터 무시
        }
    };
}
// 코드 참고 - http://blog.naver.com/PostView.nhn?blogId=tkddlf4209&logNo=220734131855
// getRawX() <-> getX() - https://www.androidpub.com/1947442