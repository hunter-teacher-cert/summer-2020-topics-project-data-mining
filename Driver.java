import java.io.*;
import java.util.*;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;



public class Driver {
    public static void main(String[] args ) throws IOException, JSONException  {
		JSONObject jobj;
		JSONArray jarray;
		String  state;
		String maxState = "", minState = "";
		int positives;
		int deaths;
		float deathpct, maxDeathPct, minDeathPct, changePct;
		JsonReader jr = new JsonReader();  // initiate the class
		String urlnycurrent = "https://covidtracking.com/api/v1/states/ny/current.json";
		String urlstatesCurrent = "https://covidtracking.com/api/v1/states/current.json";
		String urlnydaily = "https://covidtracking.com/api/v1/states/ny/daily.json";
	
		//jr.readJSONURL(urlnycurrent); 
			
		//handleJSONObject(jr, jr.getJSONobj());
		
		// task one
		// Calculating the overall Death rate in New York
		jr.readJSONURL(urlnycurrent); // read into one JSON data record
		jobj = jr.getJSONobj();		// get the JSON object for this one record query
		positives = (int) jr.getValue(jobj, "positive");	// positive cases reported
		deaths = (int) jr.getValue(jobj, "death");		// deaths reported
		deathpct = (100 * (float) deaths)/(float) positives;
		
		System.out.printf("Total Positive Cases: %d, total deaths: %d.  Death rate = %5.2f %%\n",
						positives, deaths, deathpct);
		
		// task two
		// go through every state and display the info on positives and deaths and pct
		System.out.printf("State\tPositives\tDeaths\tPct\n");
		// get the JSON Array of all records
		jr.readJSONURL(urlstatesCurrent);	// read in the data
		jarray = jr.getJSONArray();
		maxDeathPct = 0;  // initialize max
		minDeathPct = 100; // initialize min
		for (int i=0; i<jarray.length(); i++)	{  // go through each record
			jobj = jarray.getJSONObject(i);		// get the record
			state = (String) jr.getValue(jobj, "state");
			positives = (int) jr.getValue(jobj, "positive");	// positive cases reported
			deaths = (int) jr.getValue(jobj, "death");		// deaths reported
			if (positives == 0)
				deathpct = 0;  // don't want to divide by zero
			else deathpct = (100 * (float) deaths)/(float) positives;
			if (deaths > 0 && deathpct < minDeathPct)	{
				minDeathPct = deathpct;   // we have a new minimum percentage
				minState = state;			// assign the state
			}
			if (deathpct > maxDeathPct)		{
				maxDeathPct = deathpct;	// we have a new maximum percentage
				maxState = state;		// assign the state
			}	
			System.out.printf("%s\t%d\t%d\t %5.2f %%\n",
						state, positives, deaths, deathpct);
						
		}
		System.out.printf("Max Death Rate is in %s - %5.2f %%\n", maxState, maxDeathPct);
		System.out.printf("Min Death Rate is in %s - %5.2f %%\n", minState, minDeathPct);
		
	// Task 3
	// List for each week starting with May 3 in New York the total number of deaths for the week
	// Print out the first day of the week and the total number of deaths for that week.
		System.out.printf("Week End\tPositives\tDeaths\tPct Change in Positives from Prior Week\n");
		// get the JSON Array of all records
		jr.readJSONURL(urlnydaily);	// read in the data
		jarray = jr.getJSONArray();
		deaths = 0;
		positives = 0;
		int priorPositives = 0;		// to compare with prior week
		for (int i=jarray.length()-4; i>=0; i--)	{	// we work backwards starting with the earliest date
			jobj = jarray.getJSONObject(i);		// get the record
				// we start with the 5th to the end which is a Sunday
		// we want to recaculate each Sunday
		// otherwise we add up totals each week.
			if (i % 7 == 3)	{	// we are at a Sunday
				print_change_datea(jr.getKeyValueString(jobj, "date"), priorPositives, positives, deaths);
				priorPositives = positives;	// store the prior week's value
				positives = 0;	// reset our data for the new week
				deaths = 0;		// reset for the new week
			} else		{	// not on a Sunday we just add things up
				positives = (int) jr.getValue(jobj, "positiveIncrease");	// positive cases reported
				deaths = (int) jr.getValue(jobj, "deathIncrease");		// deaths reported

			}
		}
		print_change_datea("Current Week", priorPositives, positives, deaths);

				
				
		
		
		
		
		
	}
			// print out weekly results
	public static void print_change_datea(String dateString, int priorPositives, int positives, int deaths) {
		float changePct = 0;
		if (priorPositives > 0)
			changePct = (100 * (float) (positives - priorPositives))/(float) priorPositives;
		
		System.out.printf("%s\t\t%d\t%d\t\t%5.2f %%\n", dateString, positives, deaths, changePct); 
		
	}	

   
	public static void handleJSONObject(JsonReader jr, JSONObject jsonObject) {
		/* jsonObject.keys().forEachRemaining(key -> {
			Object value = jsonObject.get(key);
		*/
			ArrayList<String> al = jr.getKeyList(jsonObject);
			for (int i=0; i<al.size(); i++)
				System.out.printf("Key: %s\tValue: %s\n", al.get(i), jr.getKeyValueString(jsonObject, al.get(i)));
			
			
			
			
			
    }
	
}	