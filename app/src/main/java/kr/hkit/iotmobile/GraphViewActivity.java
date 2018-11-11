package kr.hkit.iotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.toolbox.NetworkImageView;
import kr.hkit.iotmobile.Volley.VolleySingleton;

import java.util.Calendar;
import java.text.SimpleDateFormat;
//http://hspmuse.tistory.com/entry/java-오늘일자-string-format-YYYYMMDD [개발자인생]

public class GraphViewActivity extends AppCompatActivity {
    String http_ip;
    String http_port=":7000";
    String path="/graph/";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    Calendar c1 = Calendar.getInstance();
    String strToday = sdf.format(c1.getTime());


    String file_name="graph"+strToday+".png";
    NetworkImageView graphNetworkImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphview);
        graphNetworkImageView = findViewById(R.id.graphNetworkImageView);
        Log.e("graph00","oncreate");
    }

    @Override
    protected  void onStart(){
        super.onStart();
        Intent intent = getIntent();
        http_ip = intent.getStringExtra("ip");
        try {
            if("dust".equals(intent.getStringExtra("dust"))){
                file_name="graph2"+strToday+".png";
            }
        }catch (Exception e){
            file_name="graph"+strToday+".png";
        }
        Log.e("graph00",http_ip);
        Log.e("graph00",http_port);
        Log.e("graph00",path);
        Log.e("graph00",file_name);
        graphNetworkImageView.setImageUrl(http_ip+http_port+path+file_name, VolleySingleton.getInstance(this).getImageLoader());
        //화면을 옆으로 돌리면 이미지가 자동으로 맞춰진다.
    }
}