package kr.hkit.iotmobile;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import kr.hkit.iotmobile.Dataform.GetRankingDataResult;

public class NaverRankingResultActivity extends Activity{
    String http_ip;
    String http_port = ":8000";        // 학원
    TextView naver_result;

    String kind = "ranking";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_result);
        naver_result = findViewById(R.id.naver_result);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        http_ip = intent.getStringExtra("ip");
        RequestRanking(kind);
    }

    void RequestRanking(String kind) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = http_ip+http_port+"/get_data?id=android&kind="+kind;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseGetRankingListener,null );
        queue.add(stringRequest);
        Log.e("ranking","큐에 추가");
    }

    private Response.Listener<String> responseGetRankingListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {//집에선 동글이 필요하다
            Log.e("rank","리스폰스");
            GetRankingDataResult result = new Gson().fromJson(response, GetRankingDataResult.class);
            Log.e("rank","Gson.fromJson");
            String word ="";

            for(int i=0;i<20;i++) {
                String rank = result.getRanks()[i][0];           // 순위 번호
                rank = rank.concat("위\t\t");
                String content = result.getRanks()[i][1];        // 해당 순위 검색어
                content = content.concat("\n");

                word = word.concat(rank.concat(content));
            }
            naver_result.setText(word);
        }
    };
}