package kr.hkit.iotmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kr.hkit.iotmobile.preference.AddressPreference;

public class SettingActivity extends AppCompatActivity {

    private EditText ipEditText;
    private EditText portEditText;
    private AddressPreference ap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpip_setting);

        ipEditText = findViewById(R.id.IdEditText);
        portEditText = findViewById(R.id.PortEditText);

        ap = new AddressPreference(getBaseContext());

        String ip = ap.getIp();
        ipEditText.setText(ip);

        int port = ap.getPort();

        portEditText.setText(String.valueOf(port));

        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(onSaveClickListener);
    }

    private View.OnClickListener onSaveClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            ipEditText = findViewById(R.id.IdEditText);
            String ip = ipEditText.getText().toString();

            portEditText = findViewById(R.id.PortEditText);
            String portText = portEditText.getText().toString();

            int port = Integer.valueOf(portText);//문자->정수로 왜 바꾸냐면

            ap = new AddressPreference(getBaseContext());// 데이터를 파일형식 비슷하게 저장

            ap.putIp(ip);
            ap.putPort(port);//정수로 입력받게 만들었으니 정수가 들어간다

            Toast.makeText(getApplicationContext(),ip+"이(가) 저장되었습니다.", Toast.LENGTH_SHORT).show();

            finish(); // 해당 액티비티 종료
        }
    };
}