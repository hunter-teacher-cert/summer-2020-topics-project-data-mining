
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public class JsonReader {

  private JSONArray jarray;
  private JSONObject jobj;
  private String jsonText;
  
  public JsonReader()	{   // consstructor
	  this.jarray = null;	// If there is an array of JSON objects, we put it here
	  this.jobj = null;		// If there is just one objecct, we put it here
	  this.jsonText = null;	// The raw JSON text before it is parsed 
  }	  
  
  // method to retrieve the array
  public  JSONArray getJSONArray() {
	  return this.jarray;
  }

	// if there is just an object, method to retrieve that object
  public  JSONObject getJSONobj()	{
	  return this.jobj;
  }		
	  
		// method to build a String from the raw data
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
        sb.append((char) cp);
    }
    return sb.toString();
  }

	// method to take the URL, read from the web, and return a String
  private static String readtextFromUrl(String url) throws IOException, JSONException {
	
    InputStream is = new URL(url).openStream();	// this opens the URL
    try {
		// now we buffer the input, and convert it to a readable String
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	  String jsonText = readAll(rd);
	  return jsonText;
    } finally {
      is.close();               
    }
  }
 	
	// main method which takes a URL, reads it, and then either creates an array
	// of JSON objects, or just a single JSON object (depending on the layout of the data)
	public  void readJSONURL(String url) throws IOException, JSONException  {
		
		this.jsonText = readtextFromUrl(url);	// get the 
		
		if (this.jsonText.charAt(0) == '[')	{  // we have a JSON array here
			this.jarray = new JSONArray(jsonText);	// store the JSON Array 
		  }  
		 // we just have a plain JSON Object
		 else this.jobj = new JSONObject(jsonText);	// store the JSON Object
	
	}
		
		// basic method to take a key input and return the value
		// in a standard JSON pair
	public static Object getValue(JSONObject json, String key)	{
		return(json.get(key));
	}
	
		 	// basic method to take a key input and return the STRING value
		// in a standard JSON pair 
	public static String getKeyValueString(JSONObject json, String key)	{
		Object obj;
		obj = getValue(json, key);
		return obj.toString();
	}

	// puts all the keys in a JSON object into an Array List
	//  so we can use those keys later to retrieve the corresponding values
	public static ArrayList<String> getKeyList(JSONObject json)	{
		ArrayList<String> al = new ArrayList<String>();	// initialize the Array List
		json.keys().forEachRemaining(key -> {
			al.add(key);		// go through each one and add to the list
		});
		return al;   // return the resultant Array List
	}	
	  

	/*
	 // Specific functions to read either the object or the array, but this is no longer necessary
	public static JSONObject getJsonObj(String url) throws IOException, JSONException  {
		String jsonText = readtextFromUrl(url);
		JSONObject json;
		
		if (jsonText.charAt(0) == '[')	{  // we have a JSON array here
			System.out.println("Error - You tried to get an object and istead found an array");
			return null;
		  }  
		 // we just have a plain JSON Object
		 json = new JSONObject(jsonText);
		 return json;
	}

	public static JSONArray getJsonArray(String url) throws IOException, JSONException  {
		JSONArray ja;
		String jsonText = readtextFromUrl(url);
		
		if (jsonText.charAt(0) != '[')	{  // we have a JSON array here
			System.out.println("Error - You tried to get an array but there is no array here");
			return null;
		  }  
		 // we just have a plain JSON Object
		 ja = new JSONArray(jsonText);
		 return ja;
	}

	*/
}