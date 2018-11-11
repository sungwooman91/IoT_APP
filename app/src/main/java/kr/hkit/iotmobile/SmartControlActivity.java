package kr.hkit.iotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import kr.hkit.iotmobile.preference.AddressPreference;
import kr.hkit.iotmobile.socket.SocketClient;
import kr.hkit.iotmobile.socket.SocketClientListener;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

// 스피너 레이아웃 - http://bitsoul.tistory.com/44?category=623707
public class SmartControlActivity extends AppCompatActivity{
    private AddressPreference ap;
    public boolean SMART_MODE = false;

    Spinner sidoSpinner;
    Spinner gudongSpinner;

    ArrayAdapter sidoAdapter;                               //시,도

    ArrayAdapter gudongAdapter;                             // 서울 선택일때
    ArrayAdapter gudongAdapter2;                            // 경기 선택일때
    ArrayAdapter gudongAdapter3;                            // 대구 선택일때
    String sido;                                            // 선택한 시,도를 저장하는 변수
    String gudong;                                          // 선택한 구,동을 저장하는 변수

    TextView http_addr;

    SocketClient socketClient;

    String ip;
    int port;

    String http_addr_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_control);

        Button th_graph_button = findViewById(R.id.th_graph_button);
        th_graph_button.setOnClickListener(OnthGraphClickListener);

        Button naver_searh_btn = findViewById(R.id.naver_searh_btn);
        naver_searh_btn.setOnClickListener(OnSearchClickListener);

        Button microdust_button = findViewById(R.id.microdust_button);      // 미세먼지 버튼
        microdust_button.setOnClickListener(OnMicroDustClickListener);

        Button dust_graph_button = findViewById(R.id.dust_graph_button);
        dust_graph_button.setOnClickListener(OnDustGraphClickListener);

        Button smart_on_btn = findViewById(R.id.smart_on_btn);              // 스마트모드 활성화 버튼
        smart_on_btn.setOnClickListener(OnSmartModeActivateClickListener);

        Button smart_off_btn = findViewById(R.id.smart_off_btn);            // 스마트모드 비활성화 버튼
        smart_off_btn.setOnClickListener(OnSmartModedeActivateClickListener);

        Button http_addr_button = findViewById(R.id.http_addr_button);
        http_addr_button.setOnClickListener(OnhttpAddressClickListener);

        http_addr = findViewById(R.id.http_addr);
        http_addr_msg = http_addr.getText().toString();                     // http 주소 저장

        sidoSpinner = findViewById(R.id.spinner_dosi);
        sidoAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_dosi, android.R.layout.simple_spinner_item);
        sidoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sidoSpinner.setAdapter(sidoAdapter);/// 기본 시,도는 서울 (생략시 안됨)
        sidoSpinner.setOnItemSelectedListener(OnSidoSelectedListener);


        gudongSpinner = findViewById(R.id.spinner_gudong);
        gudongAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_gudong, android.R.layout.simple_spinner_item);
        gudongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gudongSpinner.setAdapter(gudongAdapter);//기본 구,동은 중구
        gudongSpinner.setOnItemSelectedListener(OnGudongSelectedListener);


        gudongAdapter2 = ArrayAdapter.createFromResource(this,//경기
                R.array.spinner_gudong2, android.R.layout.simple_spinner_item);
        gudongAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gudongAdapter3 = ArrayAdapter.createFromResource(this,//대구
                R.array.spinner_gudong3, android.R.layout.simple_spinner_item);
        gudongAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private AdapterView.OnItemSelectedListener OnSidoSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?>parent, View view, int position, long id){
            sido = (String)parent.getItemAtPosition(position);

            if(sido.equals(getString(R.string.Seoul))){                 // 서울
                gudongSpinner.setAdapter(gudongAdapter);

            }else if(sido.equals(getString(R.string.Gyeonggi))){            // 경기
                gudongSpinner.setAdapter(gudongAdapter2);

            }else if(sido.equals(getString(R.string.Daegu))){           // 대구
                gudongSpinner.setAdapter(gudongAdapter3);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent){}
    };

    private AdapterView.OnItemSelectedListener OnGudongSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            gudong = (String)parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private View.OnClickListener OnMicroDustClickListener = new View.OnClickListener(){     // 미세먼지
        @Override
        public void onClick(View v){
            Log.e("response","미세먼지 버튼 누름");
            Intent intent = new Intent(getBaseContext(),MicroDustResultActivity.class);  // 음성 제어 화면으로로
            intent.putExtra("sido",sido);
            intent.putExtra("gudong",gudong);
            intent.putExtra("ip",http_addr_msg);
            startActivity(intent);
            //액티비티 간 데이터 보내기 - https://m.blog.naver.com/PostView.nhn?blogId=eominsuk55&logNo=220228053631&proxyReferer=https%3A%2F%2Fwww.google.co.kr%2F
        }
    };

    private View.OnClickListener OnSearchClickListener = new View.OnClickListener(){        
        @Override
        public void onClick(View v){
            Log.e("naver","네이버 버튼 누름");
            Intent intent = new Intent(getBaseContext(),NaverRankingResultActivity.class);  // 음성 제어 화면으로로
            intent.putExtra("ip",http_addr_msg);
            startActivity(intent);
        }
    };

    private View.OnClickListener OnthGraphClickListener = new View.OnClickListener(){     // 그래프 확인 버튼 클릭시
        @Override
        public void onClick(View v){
            // 실내 온습도 그래프 확인
            Log.e("response","그래프 버튼 누름");
            Intent intent = new Intent(getBaseContext(),GraphViewActivity.class);         // 그래프 화면뷰로
            intent.putExtra("ip",http_addr_msg);
            startActivity(intent);
        }
    };

    private View.OnClickListener OnDustGraphClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Log.e("response","미세먼지 그래프");
            Intent intent = new Intent(getBaseContext(),GraphViewActivity.class);
            intent.putExtra("ip",http_addr_msg);
            intent.putExtra("dust","dust");
            startActivity(intent);
        }
    };

    private View.OnClickListener OnSmartModeActivateClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            ap = new AddressPreference(getBaseContext());

            ip = ap.getIp();
            port = ap.getPort();

            if(ip !=null && port != 0) {
                socketClient = new SocketClient(ip, port, "smart.o", SmartModeClientListener);
                Log.e("TCPIP", "스마트모드 활성화");
                socketClient.execute();
            }else{
                Toast.makeText(getApplicationContext(),"IP나 PORT 설정을 먼저 확인하세요",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener OnSmartModedeActivateClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            ap = new AddressPreference(getBaseContext());

            ip = ap.getIp();
            port = ap.getPort();

            if(ip !=null && port != 0) {
                socketClient = new SocketClient(ip, port, "smart.f", SmartModeClientListener);
                Log.e("TCPIP", "스마트모드 비활성화");
                socketClient.execute();
            }else{
                Toast.makeText(getApplicationContext(),"IP나 PORT 설정을 먼저 확인하세요",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener OnhttpAddressClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            http_addr_msg = http_addr.getText().toString();
            Toast.makeText(getBaseContext(),http_addr_msg,Toast.LENGTH_SHORT).show();
        }

    };

    private SocketClientListener SmartModeClientListener = new SocketClientListener(){
        @Override
        public void onReceiveMessage(String receiveMessage){// 받는 데이터
            String smartMode;

            Log.e("TCPIP","받은 그대로의 메세지 : "+receiveMessage);
            smartMode = receiveMessage.substring(0,7);

            // 데이터 크기를 정해주거나 C에서 UTF-8 설정(라이브러리 있다고 함)을 해줘야 정상적으로 받을 수 있다고함
            if(smartMode.equals("smart.o")) {
                SMART_MODE=TRUE;
                Toast.makeText(getApplicationContext(),R.string.smartActivate,Toast.LENGTH_SHORT).show();
            }else if(smartMode.equals("smart.f")){
                SMART_MODE=FALSE;
                Toast.makeText(getApplicationContext(),R.string.smartdeActivate,Toast.LENGTH_SHORT).show();
            }
        }
    };
}