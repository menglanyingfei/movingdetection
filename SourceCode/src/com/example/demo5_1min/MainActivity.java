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
        private int times; // 移动的次数
        private String name;
//		public boolean stopThread = false;
		
		// 调用该任务线程的run()方法执行任务线程。
//		Handler updateBarHandler = new Handler();
		
//		private Handler mHandler = new Handler();		
//		private Timer timerTask;
        /** 
         *  
         *1.创建一个SharedPreferences对象sp 
         *2.初始化SharedPreferences  参数1:sp的文件名称 ;参数2:sp的保存模式 
         *3.向sp里面保存数据,首先,获取一个文本编辑器 Editor 
         *4.存储完毕数据记得执行commit()保存数据 
         *5.读取数据 sp.getString()
         */  
          
        @Override  
        protected void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);  
            setContentView(R.layout.activity_main);
            // 通过Handler启动线程
    		
            
            // 通过DateFormat获取系统的时间
            currentTime = DateFormat.format("yyyy/MM/dd  hh:mm:ss", new Date()).toString();
            currentTime = "当前日期和时间为:\n" + currentTime; 
                               	
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
			            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();  
			        } else {  
			            
			            if (saveName.isChecked()) {  
			                  
			                boolean result = NameLogin.saveUserInfo(getApplicationContext(), name);
			                if (result) {  
			                    Log.i("TAG", "保存成功");  
			                }  
			                  
			                Toast.makeText(getApplicationContext(), "保存用户名成功", Toast.LENGTH_SHORT).show();  
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
                                               
            // 隐藏为InputType.TYPE_TEXT_VARIATION_PASSWORD,也就是0x81
            // 显示为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,也就是0x91
            
            
            // PowerManager:手机的电源管理相关组件.
            // WakeLock:用于程序控制是否一直保持手机运行状态组件.
            // WakeLock的保持运行共包括四个类型，最好使用比较低的级别，以降低手机电池的使用量。
//          PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        	WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MainActivity.class.getName());
//        	wakeLock.acquire();
            
         // 1.完成sp的初始化  
            sp = getSharedPreferences("nameFile", MODE_PRIVATE);                              
            saveName = (CheckBox) findViewById(R.id.save_name);                                                   
            editText.setText(name);                      
            saveName.setChecked(true); // 默认为记住             
            SharedPreferences preference = getSharedPreferences("nameFile",Context.MODE_PRIVATE);
            
            editText.setText(preference.getString("name","")); // preference.getString(标示符,默认值<这里为空>）
			editText.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            sp.edit().putString(name, "").commit();
            
            
            //判断是否有保存记录 ,如果不为空的话,取出显示到界面上
            Map<String, String> map = new HashMap<String, String>();  
            map = NameLogin.getSavedUserInfo(this);  
            if (map != null) {        
            	editText.setText(map.get("username"));                   
            }  
                           
                                                        
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // 加速度传感器  
            if (null == mSensorManager) {  
                Log.d(TAG, "This deveice not support SensorManager");  
            }  
              
            mSensorManager.registerListener(this, mSensor,  
                    SensorManager.SENSOR_DELAY_NORMAL);	// 参数三:检测的精准度
           
            /* name = intent.getExtras().getString("name"); */            
            // 获取当前用户的用户名
            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            name = sharedPreferences.getString("name","");
            sendToServerTimesEvery10Minutes();
            
//            wakeLock.release();
//        	  wakeLock = null;
        }
       
        @Override
        public void onDestroy() {
        	System.exit(times);
        	// 将线程销毁掉
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
        
//      传感器原理就是通过每次得到的x,y,z三轴的值,和下一次的值作比较,
//             它们每个差值中绝对值最大的,如果超过某一个阈值(自己定义)，并且这种状态持续了x秒(自己定义)，
//             我们就视为手机处于（颠簸）移动状态，当然这种判断肯定是不科学的，
//             有时候也会产生误判，比较理想的场景就是：携带手机坐在公交上或是开车。(所以此种情况要注意！)
        
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
                    
                    Toast.makeText(getApplicationContext(), "检测到手机正在移动...", Toast.LENGTH_SHORT).show();                   
                    toast = Toast.makeText(getApplicationContext(),
                    	     "这是第" + times + "次移动!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();                   
                }
                
                mX = x;  
                mY = y;  
                mZ = z;  
            }   
        }  
      
        /**
         * 获取其中的一个最大值            
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
               
        // 每间隔时间上传一次每间隔时间移动的次数
        protected void sendToServerTimesEvery10Minutes() {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                	
//                	if (!stopThread) {
                    // 每次(例如:10minutes)需要执行的代码
                	                 		
                    NetWork.postFrequency(name, times + " ");
                    times = 0;
                    Log.d("xxx", name);
                    // 虽然Message的构造函数是public的,
                    // 但是最好是使用Message.obtain()或Handler.obtainMessage( )函数
                    // 来获取Message对象,因为Message的实现中包含了回收再利用的机制，可以提供效率.
                    
                	}
//                }
            };
            
            // 10minutes = 10 * 60 * 1000 ms,测试时候为每60秒
            // 上传后times归0,重新计数.         
            timer.schedule(timerTask, 1000, 30 * 1000);                
        }
        
        
 
}  
