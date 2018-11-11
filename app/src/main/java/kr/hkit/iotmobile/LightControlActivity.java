package kr.hkit.iotmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import kr.hkit.iotmobile.preference.AddressPreference;
import kr.hkit.iotmobile.socket.SocketClient;
import kr.hkit.iotmobile.socket.SocketClientListener;

public class LightControlActivity extends AppCompatActivity {
    private AddressPreference ap;
    private int pwm_data=0;                                         // 밝기 값 저장용 변수

    private ImageView Light_Image;                                  // 조명 이미지(켜짐과 꺼짐 구분용)
    private LinearLayout LightLayoutView;                           // 레이어 밝기 조절을 위해 객체 변수 선언
    private TextView txt;

    final static String LIGHT = "L";

    SocketClient socketClient;

    String ip;
    int port;

    SeekBar SeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_light);

        Light_Image     = findViewById(R.id.LightImage);
        LightLayoutView = findViewById(R.id.LightLayoutView);

        Button SendPWM  = findViewById(R.id.SendPWM);
        SendPWM.setOnClickListener(onLightClickListener);

        SeekBar = findViewById(R.id.SeekBar);
        SeekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);

        Button TcpIpSetting = findViewById(R.id.TcpIpSetting);
        TcpIpSetting.setOnClickListener(onComSettingClickListener);

        txt = findViewById(R.id.LightControlInfo);
    }

    @Override
    protected void onStart(){
        super.onStart();

        ap = new AddressPreference(getBaseContext());

        ip = ap.getIp();
        port = ap.getPort();

        if(ip != null && port != 0) {

            Toast.makeText(getApplicationContext(), "조명상태 확인", Toast.LENGTH_SHORT).show();

            socketClient = new SocketClient(ip, port, "REQL", LightClientListener); // 클라이언트(앱)에서 요청
            socketClient.execute();

            socketClient = null;
        }else{
            Toast.makeText(getApplicationContext(), "IP나 PORT 설정을 확인하세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBackGround(){
        LightLayoutView.setBackgroundColor(0x1C1C1C1C +0x1010100* (pwm_data * 2));
        //LinearLayout 에 접근하여 ARGB 값을 제어하는 코드다. A,R,G,B 모두 같은 값을 통해 제어
    }
    private void updateMessage(){
        txt.setText("밝기 : "+pwm_data);
    }

    private SeekBar.OnSeekBarChangeListener OnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}                                // 누르기 시작

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {    // 움직일 때 마다
            pwm_data=seekBar.getProgress();
            updateBackGround();
            updateMessage();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {                                  // 멈출 때 호출
            if(pwm_data == 0){      // 밝기 값이 조금도 없다면 이 그림을
                Light_Image.setImageResource(R.drawable.light_off);
            }else{                  // 밝기 값이 조금이라도 있다면 이 그림을
                Light_Image.setImageResource(R.drawable.light_on);
            }
        }
    };

    private View.OnClickListener onLightClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String message = String.valueOf(pwm_data);

            if(ip !=null || port != 0) {
                Toast.makeText(getApplicationContext(),R.string.SIGN,Toast.LENGTH_SHORT).show();
                socketClient = new SocketClient(ip, port, LIGHT + message, LightClientListener);
                socketClient.execute();
            }else{
                Toast.makeText(getApplicationContext(),"IP나 PORT 설정을 확인하세요",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private SocketClientListener LightClientListener = new SocketClientListener(){
        // 이 리스너는 onLightClickListener의 socketCilent에서 이어짐
        @Override
        public void onReceiveMessage(String receiveMessage){// 받는 데이터
            boolean rightMessage = true;
            String lightCtrl;
            String lightCtrl2;
            String etcCtrl;

            //receiveMessage = receiveMessage.substring(receiveMessage.length() - 1);// 널문자 자르기 위한 작업..///////////////////////////////참고로 원래 잘됐던 부분임

            Log.e("TCPIP","받은 그대로의 메세지 : "+receiveMessage);
            lightCtrl = receiveMessage.substring(0,1);
            lightCtrl2 = receiveMessage.substring(0,2);
            etcCtrl = receiveMessage.substring(0,6);//////////// 널 포인터 오류 때문에 자름(아니면 그냥 일치확인만 하면 됐는데..)

            Toast.makeText(getApplicationContext(),lightCtrl2,Toast.LENGTH_SHORT).show();

            try{
                pwm_data = Integer.parseInt(lightCtrl2);
            }catch (Exception e){

                try{
                    pwm_data = Integer.parseInt(lightCtrl);
                }catch (Exception err){
                    pwm_data = 0;
                }
                pwm_data = 0;
            }


            // 데이터 크기를 정해주거나 C에서 UTF-8 설정(라이브러리 있다고 함)을 해줘야 정상적으로 받을 수 있다고함
            if(receiveMessage.equals("Connection refused")){   // 연결 거부시 처리X
                rightMessage=false;
                Toast.makeText(getApplicationContext(),"연결이 거부됐습니다.",Toast.LENGTH_SHORT).show();
            }
            else if(etcCtrl.equals("DOOR.O")){                      // 받은 데이터는 있지만 여기서 처리할 데이터는 아니므로 무시한다.
                rightMessage=false;
            }
            else if(receiveMessage.equals('\0')){                   // 아무것도 받은게 없을때
                rightMessage=false;
                Log.e("TCPIP","통신은 성공했는데 받은 데이터가 없을 때");// 이런 경우 없었음 ㅎ
            }

            lightCtrl = Integer.toString(pwm_data);

            if(rightMessage) {
                txt.setText("밝기 : " + lightCtrl + " 전송");
                txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                SeekBar.setProgress(pwm_data);
                updateBackGround();                                                 // 서버에서 받은 데이터로 배경색 변경

                Log.e("TCPIP", "Progress바 이동 성공");

                if (pwm_data != 0) {                            // PWM 값이 0이 아닌 경우
                    Light_Image.setImageResource(R.drawable.light_on);

                    Log.e("TCPIP", "0이 아닌 정수값을 받았음");
                } else {                                          // PWM 값이 0인 경우
                    Light_Image.setImageResource(R.drawable.light_off);
                }
            }
        }
    };

    private View.OnClickListener onComSettingClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(getBaseContext(),SettingActivity.class);
            startActivity(intent);
        }
    };
}