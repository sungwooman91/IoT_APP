package kr.hkit.iotmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/* 실질적인 메인화면 */
public class ControlTowerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_tower);

        ImageButton LightCtrlButton     = findViewById(R.id.LightCtrlButton);
        LightCtrlButton.setOnClickListener(onLightControlClickListener);

        ImageButton WindowCtrlButton    = findViewById(R.id.WindowCtrlButton);
        WindowCtrlButton.setOnClickListener(onWindowControlClickListener);

        ImageButton CurtainCtrlButton   = findViewById(R.id.CurtainCtrlButton);
        CurtainCtrlButton.setOnClickListener(onCurtainControlClickListener);

        Button speech_button = findViewById(R.id.speech_button);
        speech_button.setOnClickListener(on_Speech_ClickListener);

        Button smart_button = findViewById(R.id.smart_button);
        smart_button.setOnClickListener(on_Smart_ClickListener);

        Button ai_button = findViewById(R.id.ai_button);
        ai_button.setOnClickListener(on_AI_ClickListener);

    }

    private View.OnClickListener onLightControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(),LightControlActivity.class); // 조명 제어 화면으로
            startActivity(intent);
        }
    };

    private View.OnClickListener onWindowControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(),WindowControlActivity.class); // 창문 제어 화면으로로
            startActivity(intent);
        }
    };

    private View.OnClickListener onCurtainControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(),CurtainControlActivity.class); // 창문 제어 화면으로로
            startActivity(intent);
        }
    };

    private View.OnClickListener on_Speech_ClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(getBaseContext(),SpeechActivity.class); // 음성 제어 화면으로로
            startActivity(intent);
        }
    };

    private View.OnClickListener on_Smart_ClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(getBaseContext(),SmartControlActivity.class); // 음성 제어 화면으로로
            startActivity(intent);
        }
    };

    private View.OnClickListener on_AI_ClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(getBaseContext(),AIActivity.class); // 음성 제어 화면으로로
            startActivity(intent);
        }
    };
}
