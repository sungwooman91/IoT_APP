package kr.hkit.iotmobile.preference;

import android.content.Context;
import android.content.SharedPreferences;
// 간단한 값들을 저장해서 때 쓸 수 있게 구현한 Preference
public class AddressPreference {//이 클래스는 getSharedPreferences의 인터페이스 역할을 하는 것
    private static final String KEY_ADDR= "address";
    private static final String KEY_IP  = "ip";
    private static final String KEY_PORT= "port"; // 여기다 이렇게 만들어야 getSharedPreferences의 파일입출력을 저장하고 불러올 수가 있지~

    private SharedPreferences sp;

    public AddressPreference(Context context){
        sp = context.getSharedPreferences(KEY_ADDR,context.MODE_PRIVATE);
        //내가 저장할 데이터(파일?)의 이름, 다른사람이 못 읽게 설정
    }
    public void putIp(String ip){//getter & setter 중 getter의 기능과 같음
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_IP,ip);
        editor.apply();

        /*sp.edit().putString(KEY_IP,ip);
        sp.edit().apply();*/ // 권장하지는 않으나 이렇게도 사용 가능.

        //꺼낼 때 이름으로 찾게끔 설정, 초기값은 ip로(넣지도 않았는데 갖고오면 null값이 나온다. 이 소스는 ip값이 나옴)

        sp.edit().putString(KEY_IP,ip);
        sp.edit().apply();
    }
    public String getIp(){
        String ip = sp.getString(KEY_IP,null);
        return ip; // 권장하는 정석의 방법

        /*return sp.getString(KEY_IP,null);*/ // 정석의 방법과 (같은 결과를 내는) 다른 방법
    }

    public void putPort(int port){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_PORT,port);
        editor.apply();   // 권장하는 정석의 방법

        /*sp.edit().putInt(KEY_PORT,port);
        sp.edit().apply();*/ // 정석의 방법과 (같은 결과를 내는) 다른 방법
    }
    public int getPort() {
        int port = sp.getInt(KEY_PORT, 0);
        return port;    // 권장하는 정석의 방법

        //return sp.getInt(KEY_PORT,0); 정석의 방법과 (같은 결과를 내는) 다른 방법
    }
}
//http://arabiannight.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CAndroid-SharedPreferences-%EC%82%AC%EC%9A%A9-%EC%98%88%EC%A0%9C