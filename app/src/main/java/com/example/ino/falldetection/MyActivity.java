package com.example.ino.falldetection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MyActivity extends FragmentActivity implements SensorEventListener,LocationListener {
    private SensorManager managerACC, managerPRE, managerORI,managerSTE;
    private TextView text1, text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12;
    private Button btn;
    private boolean Run1,Run2,jud1,jud2,jud3;
    private LocationManager location;
    private GoogleMap mMap;
    private LatLng fall,lab;
    private PolylineOptions line;
    private Vibrator vibrator;
    private AlertDialog.Builder builder1;
    double cmp, v, q;
    double ido = 0.0;
    double kdo = 0.0;


    String result,str, vv,postido,postkdo;
    ArrayList<Double> cmpbox = new ArrayList<Double>();
    ArrayList<Double> phabox = new ArrayList<Double>();
    ArrayList<Double> phabox2 = new ArrayList<Double>();
    ArrayList<Double> idobox = new ArrayList<Double>();
    ArrayList<Double> kdobox = new ArrayList<Double>();
    MediaPlayer mp;
    Timer timer = null;
    Handler handle = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Run1 = false;
        Run2 = false;
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
        text11 = (TextView)this.findViewById(R.id.txt11);
        text12 = (TextView)this.findViewById(R.id.txt12);
        mp = MediaPlayer.create(getBaseContext(),R.raw.mdai);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//        final BluetoothManager bluetoothManager =(BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
//        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
//        mBluetoothAdapter.startLeScan(mLeScanCallback);
        location =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        lab = new LatLng(35.561888, 139.575263);
        fall = new LatLng(35.561888, 139.575263);
        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lab, 16));
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        }

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

//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback(){
//        @Override
//        public void onLeScan(BluetoothDevice device,int rssi,byte[] scanRecord){
//
//        }
//    };

    public void doAction(View view){
        if(!Run1){
            Run1 = true;
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
            Run1 = false;
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
            double ave1, ppp1=0;
            Iterator<Double> iter = phabox.iterator();
                for (i=0;i<100;i++){
                    ppp1 = ppp1 + phabox.get(i);
                }
            ave1 = ppp1/100;

            BigDecimal eA = new BigDecimal(ave1);
            double A = eA.setScale(4,BigDecimal.ROUND_DOWN).doubleValue();
            text8.setText(""+A);
         //   Log.v("A",""+A);
            phabox2.add(A);
            double p0 = phabox2.get(0);
            Log.v("A",""+A);
            if (A - p0 >0.05){
                text5.setText("落下");
                Log.v("D","○");
                jud2 = true;
                phabox2.clear();
            }if (p0 - A > 0.05){
                text5.setText("上昇");
                jud2 = false;
                phabox2.clear();
            }else if (phabox2.size() == 15) {
                text5.setText("判定");
                jud2 = false;
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

        if (jud1 == true ){
            //  Log.v("Jud","1");
              if (jud2 == true){
              //    Log.v("Jud","2");
                  if (jud3 == true){
               //       Log.v("Jud","3");
                      FallAction();
//                      doSend();
                      doPost();
                  }else{
                      jud2 = false;
                  }
              }else{
                  jud1 = false;
              }
        }
        else{
            text7.setText("No転倒");
            text12.setText("" + ido + " , " + kdo );
        }
    }

    public void FallAction(){
    //    Log.v("Jud","○");
        text7.setText("転倒しました");
        mp.start();
        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("転倒を検知しました！");
        builder1.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                timer.cancel();
                timer = null;
                vibrator.cancel();

            }
        });
        builder1.show();

        mMap.addMarker(new MarkerOptions().position(fall).title("転倒位置"));
        jud1 = false;
        jud2 = false;
        jud3 = false;
        vibrator.vibrate(15000);
        Time();
//        doSend();
    }

    public void Time(){
        if(timer == null){
            timer = new Timer();
            timer.schedule(new MyTimer(),10000);
        }
    }

    class MyTimer extends TimerTask {
        public void run() {
            handle.post(new Runnable() {
                @Override
                public void run() {

                    Dialog();
                }
            });
        }
    }

    public void Dialog(){

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setMessage("○○さんに連絡しました。");
        builder2.show();
        doSend();
//        timer.cancel();
//        timer = null;
    }

    public void doFall(View view){
            jud1 = true;
            jud2 = true;
            jud3 = true;
      }

    public void doView(View view){
        if(!Run2) {
            Run2 = true;
            onDraw();
        }else {
            mMap.clear();
            Run2 = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(16)
                .bearing(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
        ido = location.getLatitude();
        kdo = location.getLongitude();
        fall = new LatLng(ido,kdo);
        idobox.add(ido);
        kdobox.add(kdo);
        postido = String.valueOf(ido);
        postkdo = String.valueOf(kdo);
        doPost();
    }


    public void onDraw(){
        int i;
        for (i=0;i<idobox.size() -1 ; i++){
            line = new PolylineOptions();
            line.add(new LatLng(idobox.get(i),kdobox.get(i)));
            line.add(new LatLng(idobox.get(i+1),kdobox.get(i+1)));
            line.color(Color.argb(50, 0, 255, 255));
            line.width(10);
            mMap.addPolyline(line);
        }
    }


    public void doSend(){
        final String mUser = "FallDetection123@gmail.com";
        final String sUser = "g1383103@tcu.ac.jp";
        final String mPassword = "ar232323";

        try {
            Properties property = new Properties();
            property.put("mail.smtp.host","smtp.gmail.com");
            property.put("mail.smtp.auth", "true");
            property.put("mail.smtp.starttls.enable", "true");
            property.put("mail.smtp.host", "smtp.gmail.com");
            property.put("mail.smtp.port", "587");
            property.put("mail.smtp.debug", "true");
            Session session = Session.getInstance(property, new javax.mail.Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication("FallDetection123@gmail.com", "ar232323");
                }
            });
            Session sess = Session.getDefaultInstance(property);
            MimeMessage msg = new MimeMessage(sess);
            msg.setFrom(new InternetAddress(mUser));
            msg.setRecipient(Message.RecipientType.TO,new InternetAddress(sUser));
            msg.setContent("body","text/plain;utf-8");
            msg.setHeader("Content-Transfer-Encording","7bit");
            msg.setSubject("転倒発生！");
            msg.setText("転倒した恐れがあります！\n https://www.google.co.jp/maps?q=loc:"+ido+","+kdo+"\n上記の場所で転倒が検知されました！","utf-8");

            Transport transport = sess.getTransport("smtp");
            transport.connect(mUser,mPassword);
            transport.sendMessage(msg,msg.getAllRecipients());
            transport.close();
    }catch (Exception e){
        e.printStackTrace();}
    }

    public void doPost(){

        //-----[クライアント設定]
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://133.78.124.26/ino/koushin2.php");

        //-----[POST送信するデータを格納]
        List<NameValuePair> Value = new ArrayList<NameValuePair>();
        Value.add(new BasicNameValuePair("ido",postido));
        Value.add(new BasicNameValuePair("kdo",postkdo));
        try {
            //-----[POST送信]
            httppost.setEntity(new UrlEncodedFormEntity(Value));
            HttpResponse response = httpclient.execute(httppost);

            //-----[サーバーからの応答を取得]
            if (response.getStatusLine().getStatusCode() ==
                    HttpStatus.SC_OK) {
                Log.v("","ok");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        switch (i){
            case LocationProvider.AVAILABLE:
                Log.v("Status", "AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.v("Status","OUT_OF_SERVICE");
                break;
            case  LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v("Status","TEMPORARILY_UNAVAILABLE");
                break;
        }

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }



}
