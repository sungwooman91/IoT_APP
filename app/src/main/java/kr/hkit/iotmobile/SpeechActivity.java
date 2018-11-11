package kr.hkit.iotmobile;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import kr.hkit.iotmobile.preference.AddressPreference;
import kr.hkit.iotmobile.socket.SocketClient;
import kr.hkit.iotmobile.socket.SocketClientListener;

public class SpeechActivity extends AppCompatActivity {

    private final int REQUEST_CODE_RECOGNIZE_SPEECH = 100;

    private TextView speechTextView;
    private TextView receiveMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        speechTextView = findViewById(R.id.speechTextView);
        receiveMessageTextView = findViewById(R.id.receiveMessageTextView);

        ImageButton speakImageButton = findViewById(R.id.speakImageButton);
        speakImageButton.setOnClickListener(onSpeakClickListener);
    }

    View.OnClickListener onSpeakClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startRecognizeSpeech();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_RECOGNIZE_SPEECH) {
            if(resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String speech = result.get(0);

                speechTextView.setText(speech);

                AddressPreference ap = new AddressPreference(this);
                String ip = ap.getIp();
                int port = ap.getPort();

                SocketClient socketClient = new SocketClient(ip, port, speech, socketClientListener);
                socketClient.execute();
            }
        }
    }

    private SocketClientListener socketClientListener = new SocketClientListener() {
        @Override
        public void onReceiveMessage(String receiveMessage) {
            receiveMessageTextView.setText(receiveMessage);
        }
    };

    private void startRecognizeSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,R.string.speech_msg);
        startActivityForResult(intent, REQUEST_CODE_RECOGNIZE_SPEECH);
    }
}