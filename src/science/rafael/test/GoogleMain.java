package science.rafael.test;

import java.util.ArrayList;
import java.util.List;

import science.rafael.trends.GoogleResults;

/**
 * Google Search Client
 * 
 * @author Rafael Silvério Amaral
 * @email  contato@rafael.science
 */
public class GoogleMain {
	public static void main(String[] args) {
		GoogleResults googleResults = new GoogleResults();
		
		List<String> keywords = new ArrayList<String>();
		
		keywords.add("Atlético Mineiro");
		keywords.add("UTFPR");
		keywords.add("shellshock");
		
		try {
			System.out.println(googleResults.getTrendResults(keywords));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}