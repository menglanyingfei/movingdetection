package com.example.demo5_1min;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.lixingyang.loginview2.NameLogin;
import com.example.demo5_1min.NetWork;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
//import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
//import android.os.PowerManager;
//import android.os.PowerManager.WakeLock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {  
      
        private static final String TAG = MainActivity.class.getSimpleName();
        public static final String PREFERENCES = "LoginSession";
        private SensorManager mSensorManager;  
        private Sensor mSensor; 
         
        private TextView textviewX;  
        private TextView textviewY;  
        private TextView textviewZ;    
        private TextView textviewT;
        private Button button1;
//        private Button button2;
        private EditText editText;
        private CheckBox saveName;
        private SharedPreferences sp;
        private Toast toast;
        
        
        private String currentTime;
        private long lasttimestamp = 0;        
        private int mX, mY;
        private int mZ = 9;        
 
        private Calendar mCalendar;
        private int times; // �ƶ��Ĵ���
        private String name;
//		public boolean stopThread = false;
		
		// ���ø������̵߳�run()����ִ�������̡߳�
//		Handler updateBarHandler = new Handler();
		
//		private Handler mHandler = new Handler();		
//		private Timer timerTask;
        /** 
         *  
         *1.����һ��SharedPreferences����sp 
         *2.��ʼ��SharedPreferences  ����1:sp���ļ����� ;����2:sp�ı���ģʽ 
         *3.��sp���汣������,����,��ȡһ���ı��༭�� Editor 
         *4.�洢������ݼǵ�ִ��commit()�������� 
         *5.��ȡ���� sp.getString()
         */  
          
        @Override  
        protected void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);  
            setContentView(R.layout.activity_main);
            // ͨ��Handler�����߳�
    		
            
            // ͨ��DateFormat��ȡϵͳ��ʱ��
            currentTime = DateFormat.format("yyyy/MM/dd  hh:mm:ss", new Date()).toString();
            currentTime = "��ǰ���ں�ʱ��Ϊ:\n" + currentTime; 
                               	
            textviewT = (TextView) findViewById(R.id.textView5);     
            textviewX = (TextView) findViewById(R.id.textView1);  
            textviewY = (TextView) findViewById(R.id.textView3);  
            textviewZ = (TextView) findViewById(R.id.textView4);
            
            button1 = (Button) findViewById(R.id.button1);
//            button2 = (Button) findViewById(R.id.button2);
            
            editText = (EditText) findViewById(R.id.edit_text);
            
            button1.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
 					case R.id.button1:
 						String inputText = editText.getText().toString();
 						Toast.makeText(getApplicationContext(), inputText, Toast.LENGTH_SHORT).show();
 						name = inputText;
 						break;
 					default:
 						break;
 					}
					
					String name = editText.getText().toString();
					
					if (TextUtils.isEmpty(name)) {  
			            Toast.makeText(getApplicationContext(), "�û�������Ϊ��", Toast.LENGTH_SHORT).show();  
			        } else {  
			            
			            if (saveName.isChecked()) {  
			                  
			                boolean result = NameLogin.saveUserInfo(getApplicationContext(), name);
			                if (result) {  
			                    Log.i("TAG", "����ɹ�");  
			                }  
			                  
			                Toast.makeText(getApplicationContext(), "�����û����ɹ�", Toast.LENGTH_SHORT).show();  
			            }  
			  
			            
			        }
					
 					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);           
					startActivity(intent);					
				}				
			});
//            button2.setOnClickListener(new OnClickListener() {				
//				@Override
//				public void onClick(View v) {					
// 					switch (v.getId()) {
// 					case R.id.button2:
// 						String inputText = editText.getText().toString();
// 						Toast.makeText(getApplicationContext(), inputText, Toast.LENGTH_SHORT).show();
// 						name = inputText;
// 						break;
// 					default:
// 						break;
// 					}
//				}				
//			});
                                               
            // ����ΪInputType.TYPE_TEXT_VARIATION_PASSWORD,Ҳ����0x81
            // ��ʾΪInputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,Ҳ����0x91
            
            
            // PowerManager:�ֻ��ĵ�Դ����������.
            // WakeLock:���ڳ�������Ƿ�һֱ�����ֻ�����״̬���.
            // WakeLock�ı������й������ĸ����ͣ����ʹ�ñȽϵ͵ļ����Խ����ֻ���ص�ʹ������
//          PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        	WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MainActivity.class.getName());
//        	wakeLock.acquire();
            
         // 1.���sp�ĳ�ʼ��  
            sp = getSharedPreferences("nameFile", MODE_PRIVATE);                              
            saveName = (CheckBox) findViewById(R.id.save_name);                                                   
            editText.setText(name);                      
            saveName.setChecked(true); // Ĭ��Ϊ��ס             
            SharedPreferences preference = getSharedPreferences("nameFile",Context.MODE_PRIVATE);
            
            editText.setText(preference.getString("name","")); // preference.getString(��ʾ��,Ĭ��ֵ<����Ϊ��>��
			editText.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            sp.edit().putString(name, "").commit();
            
            
            //�ж��Ƿ��б����¼ ,�����Ϊ�յĻ�,ȡ����ʾ��������
            Map<String, String> map = new HashMap<String, String>();  
            map = NameLogin.getSavedUserInfo(this);  
            if (map != null) {        
            	editText.setText(map.get("username"));                   
            }  
                           
                                                        
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // ���ٶȴ�����  
            if (null == mSensorManager) {  
                Log.d(TAG, "This deveice not support SensorManager");  
            }  
              
            mSensorManager.registerListener(this, mSensor,  
                    SensorManager.SENSOR_DELAY_NORMAL);	// ������:���ľ�׼��
           
            /* name = intent.getExtras().getString("name"); */            
            // ��ȡ��ǰ�û����û���
            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            name = sharedPreferences.getString("name","");
            sendToServerTimesEvery10Minutes();
            
//            wakeLock.release();
//        	  wakeLock = null;
        }
       
        @Override
        public void onDestroy() {
        	System.exit(times);
        	// ���߳����ٵ�
//    		mHandler.removeCallbacks(mRunnable);
//    		timerTask.cancel();
//        	stopThread = true;
        	super.onDestroy();
        	if (mSensorManager != null) {
        		mSensorManager.unregisterListener(this);
        	}
        }
              
        @Override  
        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
      
        }  
        
//      ������ԭ�����ͨ��ÿ�εõ���x,y,z�����ֵ,����һ�ε�ֵ���Ƚ�,
//             ����ÿ����ֵ�о���ֵ����,�������ĳһ����ֵ(�Լ�����)����������״̬������x��(�Լ�����)��
//             ���Ǿ���Ϊ�ֻ����ڣ��������ƶ�״̬����Ȼ�����жϿ϶��ǲ���ѧ�ģ�
//             ��ʱ��Ҳ��������У��Ƚ�����ĳ������ǣ�Я���ֻ����ڹ����ϻ��ǿ�����(���Դ������Ҫע�⣡)
        
        @Override  
        public void onSensorChanged(SensorEvent event) {  
            if (event.sensor == null) {  
                return;  
            }  
      
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {  
                int x = (int) event.values[0];  
                int y = (int) event.values[1];  
                int z = (int) event.values[2];                
                mCalendar = Calendar.getInstance();  
                long stamp = mCalendar.getTimeInMillis() / 1000; 
//              long second = mCalendar.get(Calendar.SECOND);
                
                textviewT.setText(currentTime);                               
                textviewX.setText("");  
                textviewY.setText("");  
                textviewZ.setText("");
                                              
                int pX = Math.abs(mX - x);  
                int pY = Math.abs(mY - y);  
                int pZ = Math.abs(mZ - z);
                
//              Log.d(TAG, "pX:" + pX + "  pY:" + pY + "  pZ:" + pZ + "    stamp:"  
//                        + stamp + "  second:" + second);
                
                int maxValue = getMaxValue(pX, pY, pZ);                               
                if (maxValue > 2 && stamp - lasttimestamp > 2) {
                	times++;
                    lasttimestamp = stamp;                    
                    Log.d(TAG, "This mobile is moving...");
                    
                    Toast.makeText(getApplicationContext(), "��⵽�ֻ������ƶ�...", Toast.LENGTH_SHORT).show();                   
                    toast = Toast.makeText(getApplicationContext(),
                    	     "���ǵ�" + times + "���ƶ�!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();                   
                }
                
                mX = x;  
                mY = y;  
                mZ = z;  
            }   
        }  
      
        /**
         * ��ȡ���е�һ�����ֵ            
         */  
        public int getMaxValue(int px, int py, int pz) {  
            int max = 0;  
            if (px > py && px > pz) {  
                max = px;  
            } else if (py > px && py > pz) {  
                max = py;  
            } else if (pz > px && pz > py) {  
                max = pz;  
            }  
      
            return max;  
        }
               
        // ÿ���ʱ���ϴ�һ��ÿ���ʱ���ƶ��Ĵ���
        protected void sendToServerTimesEvery10Minutes() {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                	
//                	if (!stopThread) {
                    // ÿ��(����:10minutes)��Ҫִ�еĴ���
                	                 		
                    NetWork.postFrequency(name, times + " ");
                    times = 0;
                    Log.d("xxx", name);
                    // ��ȻMessage�Ĺ��캯����public��,
                    // ���������ʹ��Message.obtain()��Handler.obtainMessage( )����
                    // ����ȡMessage����,��ΪMessage��ʵ���а����˻��������õĻ��ƣ������ṩЧ��.
                    
                	}
//                }
            };
            
            // 10minutes = 10 * 60 * 1000 ms,����ʱ��Ϊÿ60��
            // �ϴ���times��0,���¼���.         
            timer.schedule(timerTask, 1000, 30 * 1000);                
        }
        
        
 
}  
