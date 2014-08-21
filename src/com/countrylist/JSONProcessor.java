package com.countrylist;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONProcessor {
	
	private String json_url = null;
	private ArrayList<String> countryList;
	
	public volatile boolean parsingComplete = false;
		
	public JSONProcessor(String url){
		this.json_url = url;
	}
	   
	public int getCountryCount(){
		return countryList.size();
	}
	   
	public String getCountry(int i){
		return countryList.get(i);
	}
	   
	public void readAndParseJSON(String in) {		  
		JSONObject reader = null;
		JSONObject query1 = null;
		JSONObject query2 = null;
		JSONArray place   = null;
		try {
			reader = new JSONObject(in);
			query1 = reader.getJSONObject("query");
			query2 = query1.getJSONObject("results");
			place = query2.getJSONArray("place");
			countryList = new ArrayList<String>();
			for (int i=0; i<place.length(); i++) {
				String temp = place.getJSONObject(i).getString("name");
				countryList.add(temp);
				Log.e("TAG", "Name found! Name: " + countryList.get(i));				
			}
			parsingComplete = true;	
		} catch (JSONException e) {
			Log.e("TAG", "No reader object!");
		}
				       	    
	}
	
	   public void fetchJSON(){
	      Thread jsonThread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	            URL url = new URL(json_url);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setReadTimeout(10000);
	            connection.setConnectTimeout(15000);
	            connection.setRequestMethod("GET");
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream ipstream = connection.getInputStream();
	            java.util.Scanner scanner = new java.util.Scanner(ipstream).useDelimiter("\\A");
	            String data = scanner.hasNext() ? scanner.next() : "";	         
	            readAndParseJSON(data);
	            ipstream.close();
	         } catch (Exception e) {	        	 
	        	 e.printStackTrace();
	         	}
	        }
	      });
	       jsonThread.start(); 		
	   }
	   
	   static String convertStreamToString(java.io.InputStream is) {
	      java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
	      return scanner.hasNext() ? scanner.next() : "";
	   }
	}