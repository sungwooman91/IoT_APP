package kr.hkit.iotmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button controlButton    = findViewById(R.id.controlButton);
        controlButton.setOnClickListener(onControlStartClickListener);

        Button CheckStatus     = findViewById(R.id.CheckStatus);
        CheckStatus.setOnClickListener(onCheckClickListener);

        Button ComSettingButton   = findViewById(R.id.ComSettingButton);
        ComSettingButton.setOnClickListener(onSettingClickListener);

        Button QuitButton       = findViewById(R.id.QuitButton);
        QuitButton.setOnClickListener(OnQuitClickListener);
    }

    private View.OnClickListener onControlStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 장비 제어창으로 넘어가는 화면
            Intent intent = new Intent(getBaseContext(),ControlTowerActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener onCheckClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            // IoT 상태 체크 화면
            Intent intent = new Intent(getBaseContext(),DeviceStatusActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener onSettingClickListener = new View.OnClickListener(){     // 통신 설정 띄우는 화면
        @Override
        public void onClick(View v){
            // 통신 설정 띄우는 화면
            Intent intent = new Intent(getBaseContext(),SettingActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener OnQuitClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
            // http://g-y-e-o-m.tistory.com/96 - 앱 종료 참고했음
        }
    };
}