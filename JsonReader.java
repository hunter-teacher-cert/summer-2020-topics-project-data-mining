
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonReader {

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
    public static void main(String[] args) throws IOException, JSONException {
    JSONObject json = readJsonFromUrl("https://covidtracking.com/api/v1/states/ny/current.json");
    System.out.println(json.toString());
	handleJSONObject(json);
	System.out.printf("positiveCasesViral:%s\n",json.get("positiveCasesViral"));
	}
	
	public static void handleJSONObject(JSONObject jsonObject) {
		jsonObject.keys().forEachRemaining(key -> {
			Object value = jsonObject.get(key);
			System.out.printf("Key: %s\tValue: %s\n", key, value);
		});	
    }

}