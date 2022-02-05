package utility;

import java.io.*;
import java.util.*;

public class PropertyReader
{
    String path =  getPath();
    Properties prop;

    /*
        this method will be used to read the properties from config file
        @Param: key and name of file
     */
    public String readProperty(String key){
        String value = "";
        try{
            prop = new Properties();
            File f = new File(path + "/src/main/resources/config/" +"application.properties" );
            if(f.exists()){
                prop.load(new FileInputStream(f));
                value = prop.getProperty(key);
            }
        }
        catch(Exception e){
            System.out.println("Failed to read from application.properties file.");
        }
        return value;
    }

    /*
        this method will be used to write some property in config file
        @param: key and value that needs to put inside a config file
        @param: file, if file exists then it will use that one else will create a new file
     */
    public void writeProperty(String key, String value) throws IOException {
        prop = new Properties();
        File f1 = new File(path + "/src/main/resources/config/" + "application.properties");
        prop.putIfAbsent(key, value);
        FileWriter fr = new FileWriter(f1, true);
        BufferedWriter br = new BufferedWriter(fr);
        prop.store(br, "");


    }

    /**
     *
     * @param key
     * @param value
     * @throws IOException
     * @description this file is being used to store the exeuction results of test caess in terms of test case id
     */
    public void writeResults(String key, String value) throws IOException {
        prop = new Properties();
        File f = new File(path+"/result.properties");
        if(f.exists()){

            prop.putIfAbsent(key, value);
            FileWriter fr = new FileWriter(f, true);
            BufferedWriter br = new BufferedWriter(fr);
            prop.store(br, "");
        }
        else{
            File f1 = new File(path+"/result.properties");
            prop.putIfAbsent(key, value);
            FileWriter fr = new FileWriter(f1, true);
            BufferedWriter br = new BufferedWriter(fr);
            prop.store(br, "");
        }

    }


    public String getPath()
    {
        String path ="";
        File file = new File("");
        String absolutePathOfFirstFile = file.getAbsolutePath();
        path = absolutePathOfFirstFile.replaceAll("\\\\+", "/");
        return path;
    }

}