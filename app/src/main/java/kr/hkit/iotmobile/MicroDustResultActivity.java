package kr.hkit.iotmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import kr.hkit.iotmobile.Dataform.Dust;
import kr.hkit.iotmobile.Dataform.GetDustDataResult;

public class MicroDustResultActivity extends Activity {
    String http_ip;
    String http_port=":8000";
    TextView metaData;
    TextView pm10Value;
    TextView pm25Value;

    String sido;                                        // 선택한 시,도를 저장하는 변수
    String gudong;                                      // 선택한 구,동을 저장하는 변수

    Button exit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);              // 상단 타이틀 제거
        setContentView(R.layout.activity_micro_dust);
        metaData = findViewById(R.id.metaData);
        pm10Value = findViewById(R.id.pm10Value);
        pm25Value = findViewById(R.id.pm25Value);

        exit_button = findViewById(R.id.exit_button);
        exit_button.setOnClickListener(OnExitClickListener);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();                                // 액티비티 간 데이터 주고받기
        sido = intent.getStringExtra("sido");
        gudong = intent.getStringExtra("gudong");
        http_ip = intent.getStringExtra("ip");

        Log.e("dust",sido);
        Log.e("dust",gudong);
        RequestMicroDust("dust");
    }

    void RequestMicroDust(String kind) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = http_ip+http_port+"/get_data?id=android&kind="+kind+"&sido=" + sido+"&gudong="+gudong;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseGetDustListener, null);    // 미세먼지 데이터 요청
        queue.add(stringRequest);
    }

    private Response.Listener<String> responseGetDustListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {           // 미세먼지 결과
            Log.e("dust","이게 안나오면..통신이 안됨~");
            GetDustDataResult result = new Gson().fromJson(response, GetDustDataResult.class);
            List<Dust> dusts = result.getDusts();

            String pm10 = dusts.get(0).getPm10Value();
            String pm25 = dusts.get(0).getPm25Value();

            //String Date = dusts.get(0).getDate();     // 시간이 정상적으로 받아지지 않는듯해서 무시
            //Log.e("dust",Date);
            pm10Value.setText("pm10Value : "+pm10);
            pm25Value.setText("pm25Value : "+pm25);
            metaData.setText("지역 : "+sido+"/"+gudong);
        }
    };

    View.OnClickListener OnExitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();               // 창문 닫기
        }
    };
}
