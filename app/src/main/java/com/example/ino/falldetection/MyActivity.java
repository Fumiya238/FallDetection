package com.example.ino.falldetection;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;


public class MyActivity extends Activity implements SensorEventListener {
    private SensorManager managerACC, managerPRE, managerORI;
    private TextView text1, text2,text3;
    private boolean doesRun;
    double cmp, v, q;
    String result,str, b, vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        doesRun = false;
        text1 = (TextView)this.findViewById(R.id.txt1);
        text2 = (TextView)this.findViewById(R.id.txt2);
        text3 = (TextView)this.findViewById(R.id.txt3);
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
                managerPRE.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            managerORI = (SensorManager)this.getSystemService(SENSOR_SERVICE);
            List<Sensor> sensorsori = managerORI.getSensorList(Sensor.TYPE_ORIENTATION);
            if(sensorsori.size() > 0){
                Sensor sensor = sensorsori.get(0);
                managerORI.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }else{
            doesRun = false;
            managerACC.unregisterListener(this);
            managerPRE.unregisterListener(this);
            managerORI.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//        result  = "Gx:" +String.valueOf(event.values[0]) + "\n";
//        result += "Gy:" +String.valueOf(event.values[1]) + "\n";
//        result += "Gz:" +String.valueOf(event.values[2]);
        cmp = Math.sqrt((Math.pow((event.values[0]), 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2)));
        BigDecimal ba = new BigDecimal(cmp);
        v = ba.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        str = Double.toString(v);
        text1.setText("m/s＾2 : " + str);
    }else if (event.sensor.getType() == Sensor.TYPE_PRESSURE){
        double ph = event.values[0];
        BigDecimal bi = new BigDecimal(ph);
        q = bi.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        vv = Double.toString(q);
        text2.setText("hPa : " + vv);
    }else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
        result = "方位角:" + String.valueOf(event.values[0]) + "\n";
        result += "傾斜角:" + String.valueOf(event.values[1]) + "\n";
        result += "回転角:" + String.valueOf(event.values[2]) + "\n";
        text3.setText(result);
    }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
