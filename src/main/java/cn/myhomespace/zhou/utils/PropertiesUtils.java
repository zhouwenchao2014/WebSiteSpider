package cn.myhomespace.zhou.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created by zhouw on 2018/5/3.
 */
public class PropertiesUtils {

    private static final String CONF_PATH="./conf.properties";

    public static void buildConfig(){
        File file = new File(CONF_PATH);
        InputStreamReader reader=null;
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            Properties properties = new Properties();
            properties.load(reader);
            System.out.println(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        buildConfig();
    }
}
