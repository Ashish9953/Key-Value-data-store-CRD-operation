package database1.database1;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
public class databaseTTL {
	private final Properties properties;
	/** This executor does the commit job. */
	private static final ExecutorService updateExecutorService = Executors.newSingleThreadExecutor();
	/*
	  Using this variable when i want to prevent update of properties happen
	 */
	private boolean updatePropertiesLocked;
	
	/*
	 The absolute path of the properties file
	 */
	private String fileAbsolutePath;
	// creating time to live variable
	  private long ttl;
	  private final HashMap<String,Long> timestamps=new HashMap();
	//creating constructor 
public databaseTTL(String fileAbsolutePath, boolean updatePropertiesLocked,long ttlvalue ) {
		
		//Fields
		this.fileAbsolutePath = fileAbsolutePath;
		this.updatePropertiesLocked = updatePropertiesLocked;
		this.properties = new Properties();
		this.ttl=ttlvalue;
	}
   public String readDatabase(String Key)
   {
	   JSONObject jsonObject=new JSONObject();
	   String value=this.properties.getProperty(Key);
	   if(value==null)throw new UserNotFoundException("key Not present with Id"+Key);
	   
	   if ( expired(Key,value)) {
           System.out.println("Time Limit Exceeded :- Cannot Retrieve data");
           properties.remove(Key);
           timestamps.remove(Key);
           return null;
       } else {
           jsonObject.put(Key,value);
           return jsonObject.toString();
       }
   }
   // expired function if time to live exceed for a key
   private boolean expired(String key, String value) {
       return (System.currentTimeMillis() - timestamps.get(key)) > this.ttl;
   }
   // here i used *synchronized* to avoid 
   public synchronized void createDatabase(String key, String value) throws Exception {
	   if (updatePropertiesLocked)
			return;
	   // try Catch is to check to control multiple access thread.
	     
       try {
           Thread.sleep(400);
       }
       catch (Exception e){
           e.printStackTrace();
       }
       if(key.length()>32)
           throw new Exception("Id character cannot exceed 32 size");
       if(properties.containsKey(key)){
           throw new Exception("Value Stored with given Key");
       }
       
     //Check if exists [ Create if Not ] 
     		File file = new File(fileAbsolutePath);
     		if (!file.exists()) {
     			try {
     				file.createNewFile();
     			} catch (IOException e) {
     				e.printStackTrace();
     			}
     		}
//       System.out.println("object inserted in storage with "+ key);
       timestamps.put(key, System.currentTimeMillis());
       properties.put(key, value);
   }
   public void delete(String key) {
       timestamps.remove(key);
       properties.remove(key);
   }

   public void getAll(){
       if(properties.size()==0)System.out.println("No element in data storage ");
       JSONArray jsonArray=new JSONArray();
       for(HashMap.Entry it:properties.entrySet()){
//       System.out.println(it.getKey()+" -> "+it.getValue());
           JSONObject jsonObject = new JSONObject(properties);
           System.out.printf(jsonObject.toString());
       }
   }
   public Properties getProperties() {
		return properties;
	}
   public boolean isUpdatePropertiesLocked() {
		return updatePropertiesLocked;
	}
   public void setUpdatePropertiesLocked(boolean updatePropertiesLocked) {
		this.updatePropertiesLocked = updatePropertiesLocked;
	}
   /**
	 * @param fileAbsolutePath
	 *            The new absolute path of the properties file
	 */
	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}
	/**
	 * @return the propertiesAbsolutePath
	 */
	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}
   }
	


