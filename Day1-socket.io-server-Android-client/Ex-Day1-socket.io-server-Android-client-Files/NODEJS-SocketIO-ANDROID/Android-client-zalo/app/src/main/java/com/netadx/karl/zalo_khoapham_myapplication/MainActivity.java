package com.netadx.karl.zalo_khoapham_myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    Socket socket;
    EditText edtUsername;
    Button btnDangky;
    TextView txtvThongbao;
    final String TAG="SocketService";
    ListView lvUsername;
    ArrayAdapter adapter;

    ArrayList<String> mangUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            socket = IO.socket("http://192.168.1.54:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.connect();

        socket.on("kqdangky", onCallback_dk);
        socket.on("serverguinguoimoi", onCallback_nguoimoi);


        edtUsername = (EditText) findViewById(R.id.editTextUsername);
        btnDangky = (Button) findViewById(R.id.buttonDangky);
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.emit("client-gui-username", edtUsername.getText().toString());
                // ack from client to server


            }
        });

        lvUsername = (ListView) findViewById(R.id.listViewUsername);
        mangUsername = new ArrayList<String>();
        mangUsername.add("Teo");
        mangUsername.add("Ti");
        adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, mangUsername);
        lvUsername.setAdapter(adapter);





    }
    // onCallback_dk
    private Emitter.Listener onCallback_dk = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String noidung;
                    try {
                        noidung = data.getString("noidung");
                        Log.d(TAG, noidung);
                        if (noidung == "true") {
                            Toast.makeText(getApplicationContext(), "Complete " + noidung, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Fail " + noidung, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        return;
                    }
                }
            });

        }
    };
    //END  onCallback_dk

    //onCallback_nguoimoi
    private Emitter.Listener onCallback_nguoimoi = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String noidung;
                    try {
                        noidung = data.getString("nguoimoi");
                        Log.d(TAG, noidung);
                        Toast.makeText(getApplicationContext(), "nguoimoi " + noidung, Toast.LENGTH_SHORT).show();
                        addItems(noidung);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });

        }
    };
    //END  onCallback_nguoimoi
    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(String nguoimoi) {
        adapter.insert(nguoimoi,0);
    }
    //END METHOD WHICH WILL HANDLE DYNAMIC INSERTION
}
