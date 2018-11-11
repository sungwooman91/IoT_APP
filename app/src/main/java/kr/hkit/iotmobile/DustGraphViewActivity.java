package kr.hkit.iotmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.toolbox.NetworkImageView;

import kr.hkit.iotmobile.Volley.VolleySingleton;

public class DustGraphViewActivity extends AppCompatActivity {
    //String http_url="http://192.168.0.33:";
    String http_url="http://192.168.0.186:";    // 집 주소(와이파이만 있으면 세팅 바꿀 일 없이 잘 된다.) 에뮬레이터는 잘 되는데 앱은 안뜸..(고정 아이피 써도 안됨.. 공유기만 쓰고 와이파이 안써서 그런듯)
    String http_port="7000";
    String path="/graph/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphview);            // 똑같은 역할을 하는 view를 또 만들 필요가 있을까 싶어서 있던걸로 써봄

        NetworkImageView graphNetworkImageView = findViewById(R.id.graphNetworkImageView);

        graphNetworkImageView.setImageUrl(http_url+http_port+path+"graph220180902.png", VolleySingleton.getInstance(this).getImageLoader());
        //에뮬상에선 화면을 옆으로 돌리면 이미지가 자동으로 맞춰졌음
    }
}