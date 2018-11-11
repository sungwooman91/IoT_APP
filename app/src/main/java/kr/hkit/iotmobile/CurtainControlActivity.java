package kr.hkit.iotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import kr.hkit.iotmobile.preference.AddressPreference;
import kr.hkit.iotmobile.socket.SocketClient;
import kr.hkit.iotmobile.socket.SocketClientListener;

public class CurtainControlActivity extends AppCompatActivity implements View.OnTouchListener{
    private AddressPreference ap;

    ImageView CurtainImage;
    float old_yValue;

    SocketClient socketClient;

    String ip;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_curtain);

        //이 곳에는 커튼 제어만
        CurtainImage = findViewById(R.id.CurtainImage);
        CurtainImage.setOnTouchListener(this);

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
            old_yValue = event.getY();
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            Log.i("CuratinXY","eventY : " + event.getY() );
        }else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (event.getY() < old_yValue) {
                CurtainImage.setImageResource(R.drawable.curtain_up);           // 커튼 올리기
                msg="CURTAIN.O";
            } else {
                CurtainImage.setImageResource(R.drawable.curtain_closed);       // 커튼 내리기
                msg="CURTAIN.C";
            }

            if(ip !=null && port != 0) {
                Toast.makeText(getApplicationContext(),R.string.SIGN,Toast.LENGTH_SHORT).show();
                socketClient = new SocketClient(ip, port, msg, OnCurtainClickListener);
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
            Intent intent = new Intent(getBaseContext(),SettingActivity.class); // 통신 세팅 화면으로
            startActivity(intent);
        }
    };

    private SocketClientListener OnCurtainClickListener = new SocketClientListener(){
        @Override
        public void onReceiveMessage(String receiveMessage) {
            // 피드백을 받을 수 없으니 받은 데이터 무시
        }
    };
}
