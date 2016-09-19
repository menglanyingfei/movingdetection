package com.lixingyang.loginview2;  
  
import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;   
import java.util.HashMap;  
import java.util.Map;  
  
import android.content.Context;  
  
public class NameLogin {  
    // 没有使用任何类的成员变量,建议使用静态方法  
    public static boolean saveUserInfo(Context context, String username) {  
  
        try {  
            // File file = new File("/data/data/com.lixingyang.loginview/info.txt");    
  
            File file = new File(context.getFilesDir(), "info.txt"); // 获取路径 如 "/data/data/com.lixingyang.loginview/files / 创建文件  info.txt   
            // context.getFilesDir();
            // 返回一个目录/data/data/com.lixingyang.loginview/files 
              
            @SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(file);                
            fos.write((username).getBytes());  
            return true;  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return false;  
        } catch (IOException e) {    
            e.printStackTrace();  
            return false;
            } 
        }  
  
          
    public static Map<String, String> getSavedUserInfo(Context context) {  
        File file = new File(context.getFilesDir(), "info.txt");  
        try {  
            FileInputStream fis = new FileInputStream(file);
            
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));  
            String str = br.readLine();  
            String[] infos = str.split("##");  
            Map<String, String> map = new HashMap<String, String>();  
            map.put("username", infos[0]);                
            return map;  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return null;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        }
    }
    
}
    