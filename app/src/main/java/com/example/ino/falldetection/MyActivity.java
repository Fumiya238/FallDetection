package com.example.ino.falldetection;

import android.app.Activity;
import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MyActivity extends Activity implements SensorEventListener {
    private SensorManager managerACC, managerPRE, managerORI,managerSTE;
    private TextView text1, text2,text3,text4,text5,text6,text7,text8,text9,text10;
    private Button btn;
    private boolean doesRun,jud1,jud2,jud3;
    double cmp, v, q;
    String result,str, vv;
    ArrayList<Double> cmpbox = new ArrayList<Double>();
    ArrayList<Double> phabox = new ArrayList<Double>();
    ArrayList<Double> phabox2 = new ArrayList<Double>();
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        doesRun = false;
        jud1 = false;
        jud2 = false;
        jud3 = false;
        btn = (Button)this.findViewById(R.id.button);
        text1 = (TextView)this.findViewById(R.id.txt1);
        text2 = (TextView)this.findViewById(R.id.txt2);
        text3 = (TextView)this.findViewById(R.id.txt3);
        text4 = (TextView)this.findViewById(R.id.txt4);
        text5 = (TextView)this.findViewById(R.id.txt5);
        text6 = (TextView)this.findViewById(R.id.txt6);
        text7 = (TextView)this.findViewById(R.id.txt7);
        text8 = (TextView)this.findViewById(R.id.txt8);
        text9 = (TextView)this.findViewById(R.id.txt9);
        text10 = (TextView)this.findViewById(R.id.txt10);
        mp = MediaPlayer.create(getBaseContext(),R.raw.mdai);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doAction(View view){
        if(!doesRun){
            doesRun = true;
            btn.setText("OFF");
            managerACC = (SensorManager)this.getSystemService(SENSOR_SERVICE);
            List<Sensor> sensorsacc = managerACC.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if(sensorsacc.size() > 0){
                Sensor sensor = sensorsacc.get(0);
                managerACC.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            managerPRE = (SensorManager)this.getSystemService(SENSOR_SERVICE);
            List<Sensor> sensorspre = managerPRE.getSensorList(Sensor.TYPE_PRESSURE);
            if(sensorspre.size() > 0){
                Sensor sensor = sensorspre.get(0);
                managerPRE.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
            managerORI = (SensorManager)this.getSystemService(SENSOR_SERVICE);
            List<Sensor> sensorsori = managerORI.getSensorList(Sensor.TYPE_ORIENTATION);
            if(sensorsori.size() > 0){
                Sensor sensor = sensorsori.get(0);
                managerORI.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            managerSTE = (SensorManager)this.getSystemService(SENSOR_SERVICE);
            List<Sensor> sensorsste = managerSTE.getSensorList(Sensor.TYPE_STEP_COUNTER);
            if(sensorsste.size() > 0){
                Sensor sensor = sensorsste.get(0);
                managerSTE.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }else{
            doesRun = false;
            btn.setText("ON");
            managerACC.unregisterListener(this);
            managerPRE.unregisterListener(this);
            managerORI.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
        cmp = Math.sqrt((Math.pow((event.values[0]), 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2)));
        BigDecimal ba = new BigDecimal(cmp);
        v = ba.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        str = Double.toString(v);
        text1.setText("加速度 : " + str);
        cmpbox.add(cmp);
            if (cmpbox.size() == 15){
                double c0 = cmpbox.get(0);double c1 = cmpbox.get(1);double c2 = cmpbox.get(2);
                double c3 = cmpbox.get(3);double c4 = cmpbox.get(4);double c5 = cmpbox.get(5);
                double c6 = cmpbox.get(6);double c7 = cmpbox.get(7);double c8 = cmpbox.get(8);
                double c9 = cmpbox.get(9);double c10 = cmpbox.get(10);double c11 = cmpbox.get(11);
                double c12 = cmpbox.get(12);double c13 = cmpbox.get(13);double c14 = cmpbox.get(14);
                if(c0<11 && c1<11 && c2<11 && c3<11 && c4<11 && c5<11 && c6<11 && c7<11 && c8<11 && c9<11 && c10<11 && c11<11 && c12<11 && c13<11 && c14<11 && 9<c0 && 9<c1 && 9<c2 && 9<c3 && 9<c4 && 9<c5 && 9<c6 && 9<c7 && 9<c8 && 9<c9 && 9<c10 && 9<c11 && 9<c12 && 9<c13 && 9<c14){
                    text4.setText("停止");
                    jud1 = true;
                }else{
                    text4.setText("歩行");
                    jud2 = false;
                }cmpbox.clear();
            }
    }else if (event.sensor.getType() == Sensor.TYPE_PRESSURE){
        double ph = event.values[0];
        BigDecimal bi = new BigDecimal(ph);
        q = bi.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        vv = Double.toString(q);
        text2.setText("気圧値 : " + vv);
        phabox.add(q);
        if (phabox.size() == 100){
            int i;
            double ave1,ave2, ppp1=0, ppp2=0;
            Iterator<Double> iter = phabox.iterator();
                for (i=0;i<100;i++){
                    ppp1 = ppp1 + phabox.get(i);
                }
            ave1 = ppp1/100;

            BigDecimal eA = new BigDecimal(ave1);
            double A = eA.setScale(3,BigDecimal.ROUND_DOWN).doubleValue();
            text8.setText(""+A);
//            Log.v("A", "" + A);
            phabox2.add(A);
            double p0 = phabox2.get(0);
            if (A - p0 >0.05){
                text5.setText("落下");
                jud2 = true;
                phabox2.clear();
            }if (p0 - A > 0.05){
                text5.setText("上昇");
                jud2 = false;
                phabox2.clear();
            }else if (phabox2.size() == 15) {
                text5.setText("落ちてない");
                jud3 = false;
            }
            for (i=0;i<1; i++){
                iter.next();
                iter.remove();
            }
        }
    }else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
        result = "Pitch :" + String.valueOf(event.values[1]) + "\n";
        result += "Roll  :" + String.valueOf(event.values[2]) + "\n";
        text3.setText(result);
             if ((-120 < event.values[1] && event.values[1] < -50) || (50< event.values[1] && event.values[1] < 120)){
                text6.setText("直立");
                 jud3 = false;
              }else{
                 text6.setText("寝");
                 jud3 = true;
             }
    }else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
        double k,kkk ;
        result = "歩数" + String.valueOf(event.values[0]);
        text9.setText(result);
        k = event.values[0] * 0.033;
        BigDecimal ek = new BigDecimal(k);
        kkk = ek.setScale(4,BigDecimal.ROUND_DOWN).doubleValue();
        text10.setText(String.valueOf(kkk)+" kcal");
    }
        if (jud1 == true && jud2 == true && jud3 ==true){
            text7.setText("転倒しました");
            mp.start();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("転倒しました");
            builder1.show();
            jud1 = false;
            jud2 = false;
            jud3 = false;
        }else{
            text7.setText("No転倒");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
