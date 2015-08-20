package science.rafael.test;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import science.rafael.trends.GoogleAjaxResults;
import science.rafael.trends.GoogleResults;

/**
 * Google Search - EXAMPLE
 * 
 * This class intend to be an usage example.
 * Below it shows how to make google search
 * with google custom search and with google
 * ajax (deprecated, but still working, the
 * only problem is the limited number of
 * requests which is the same of a normal
 * users navigation).
 * 
 * @author Rafael Silvério Amaral
 * @email  contato@rafael.science
 */
public class GoogleMain {
	public static void main(String[] args) {
		// choose one of them or both to make the search
		GoogleResults googleResults = new GoogleResults();
		GoogleAjaxResults googleAjaxResults = new GoogleAjaxResults();
		
		// array with trends to be searched for
		List<String> keywords = new ArrayList<String>();
		
		keywords.add("rafael.science");
		keywords.add("South Park");
		keywords.add("shellshock");
		
		try {
			// google custom search
			for (Object rs : googleResults.getTrendResults(keywords)) {
				JSONObject results = (JSONObject) rs;
				
				System.out.println(results.get("title"));
			}
			
			// ajax request
			for (Object res : googleAjaxResults.getTrendResults(keywords)){
				JSONObject results = (JSONObject) res;
				
				System.out.println(results.get("title"));
			}
			
			// or you can search by a single trend
			System.out.println(googleResults.getSingleTrendResults("rafael.science"));
			System.out.println(googleAjaxResults.getSingleTrendResults("rafael.science"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}