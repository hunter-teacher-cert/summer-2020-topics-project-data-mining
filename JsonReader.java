
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
  
  public JsonReader()	{
	  this.jarray = null;
	  this.jobj = null;
	  this.jsonText = null;
  }	  
  
  public  JSONArray getJSONArray() {
	  return this.jarray;
  }

  public  JSONObject getJSONobj()	{
	  return this.jobj;
  }		
	  
	  
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
        sb.append((char) cp);
    }
    return sb.toString();
  }

  public static String readtextFromUrl(String url) throws IOException, JSONException {
	JSONObject json;
    InputStream is = new URL(url).openStream();
    try {
	  JSONArray ja;
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	  String jsonText = readAll(rd);
	  return jsonText;
    } finally {
      is.close();               
    }
  }
  
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
	
	public  void readJSONURL(String url) throws IOException, JSONException  {
		
		this.jsonText = readtextFromUrl(url);
		
		if (this.jsonText.charAt(0) == '[')	{  // we have a JSON array here
			this.jarray = new JSONArray(jsonText);
		  }  
		 // we just have a plain JSON Object
		 else this.jobj = new JSONObject(jsonText);
	
	}
		
	public static Object getValue(JSONObject json, String key)	{
		return(json.get(key));
	}

	public static ArrayList<String> getKeyList(JSONObject json)	{
		ArrayList<String> al = new ArrayList<String>();
		json.keys().forEachRemaining(key -> {
			al.add(key);
		});
		return al;
	}	
	  
	public static String getKeyValueString(JSONObject json, String key)	{
		Object obj;
		obj = getValue(json, key);
		return obj.toString();
	}
}