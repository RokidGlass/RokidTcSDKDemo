package com.rokid.tcsdkdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rokid.glass.ir.thermometer.entity.TemperatureInfo;
import com.rokid.thermometercontrollersdk.TcSDK;
import com.rokid.thermometercontrollersdk.utils.Constants;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TcSDK tcSDK;
    Button btn_startAPP_region,btn_startAPP_multiFace,btn_finishAPP,btn_moveTaskToBack,btn_getTempInfo;
    TextView tv_tempInfoResult;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startAPP_region = findViewById(R.id.btn_startAPP_region);
        btn_startAPP_multiFace = findViewById(R.id.btn_startAPP_multiFace);
        tv_tempInfoResult = findViewById(R.id.tv_tempInfoResult);
        btn_finishAPP = findViewById(R.id.btn_finishAPP);
        btn_moveTaskToBack = findViewById(R.id.btn_moveTaskToBack);
        btn_getTempInfo = findViewById(R.id.btn_getTempInfo);
        init();
        setListener();
    }

    private void init() {
        tcSDK = new TcSDK();
        tcSDK.initSDK(this);
    }

    private void setListener(){
        btn_startAPP_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcSDK.startAPP(Constants.START_MODE_REGION);
            }
        });

        btn_startAPP_multiFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcSDK.startAPP(Constants.START_MODE_MULTI_FACE);
            }
        });

        btn_getTempInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        while (true){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            getTempInfo();
                        }
                    }
                }.start();
            }
        });

        btn_finishAPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tcSDK.finishAPP();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_moveTaskToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tcSDK.moveTaskToBack();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTempInfo(){
        try {
            List<TemperatureInfo> temperatureInfos = tcSDK.getTemperatureInfo();
            final StringBuilder sb = new StringBuilder();
            if(temperatureInfos!= null && temperatureInfos.size()>0){
                for(int i=0; i< temperatureInfos.size(); i++){
                    TemperatureInfo temperatureInfo = temperatureInfos.get(i);
                    sb.append("temperatureInfo ").append(i)
                            .append(" temp:").append(temperatureInfo.getTemperature())
                            .append(" unit:").append(temperatureInfo.getUnit())
                            .append(" precision:").append(temperatureInfo.getPrecision())
                            .append(" mode:").append(temperatureInfo.getMode())
                            .append(" timeStamp:").append(temperatureInfo.getTimeStamp())
                            .append('\n');
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,sb.toString(),Toast.LENGTH_SHORT).show();
                        tv_tempInfoResult.setText(sb.toString());
                    }
                });

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tcSDK!=null){
            tcSDK.release();
        }
    }
}
