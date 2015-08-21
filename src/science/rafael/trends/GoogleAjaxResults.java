package science.rafael.trends;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import science.rafael.trends.ResponseHandler.Result;
import science.rafael.utils.PropertyManager;

import com.google.gson.Gson;

/**
 * Google Search Client
 * 
 * Google Ajax method
 * 
 * Limit: 100 requests/hour (NOT EXACTLY!! Number based on manual tests)
 * 
 * @author Rafael Silvério Amaral
 * @email contato@rafael.science
 */
public class GoogleAjaxResults {
	// stores trend words
	private static JSONArray trends;
	private static JSONArray singleTrends;

	// configuration properties
	private static Properties confs;

	// handles responses
	private static ResponseHandler res;

	public GoogleAjaxResults() {

	}

	/**
	 * Returns google results from a List of trend keywords
	 * 
	 * @param keyword
	 *            - String keyword to be searched
	 * @return trends - JSONArray of JSONObjects that represents the results
	 */
	@SuppressWarnings({ "unchecked" })
	public JSONArray getTrendResults(List<String> keywords) throws Exception {
		trends = new JSONArray();

		try {
			// loads configuration
			confs = PropertyManager.readProperties("search.properties");
		} catch (FileNotFoundException nf) {
			nf.printStackTrace();
		}

		// set parameters
		String google = confs.getProperty("ajax");
		String charset = confs.getProperty("charset");

		for (String keyword : keywords) {
			// cleans keywords
			String word = keyword.contains(" ") ? keyword.toLowerCase()
					.replace(" ", "+") : keyword;

			try {
				// requests and receives the response
				URL url = new URL(google + URLEncoder.encode(word, charset));
				Reader reader = new InputStreamReader(url.openStream(), charset);
				res = new Gson().fromJson(reader, ResponseHandler.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				JSONObject rs = new JSONObject();
				JSONObject resultObject = new JSONObject();
				JSONArray resArr = new JSONArray();

				// creates a JSONObject with a single result, then
				// adds on a JSONArray
				for (Result rss : res.getResponseData().getResults()) {
					resultObject.put("title", rss.getTitleNoFormatting());
					resultObject.put("url", rss.getUrl());
					resultObject.put("description", rss.getContent());
					resArr.add(resultObject);
				}

				// creates a JSONObject for a single keyword with
				// its respectively results
				rs.put(keyword, resArr);

				// then adds to final array
				trends.add(rs);

				// avoid being blocked by google (10 secs or more!)
				Thread.sleep(10000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return trends;
	}

	/**
	 * Returns google results from a single trend keyword
	 * 
	 * @param keyword
	 *            - String keyword to be searched
	 * @return trends - JSONArray of JSONObjects that represents the results
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getSingleTrendResults(String keyword) throws Exception {

		try {
			confs = PropertyManager.readProperties("search.properties");
		} catch (FileNotFoundException nf) {
			nf.printStackTrace();
		}

		String google = confs.getProperty("ajax");
		String charset = confs.getProperty("charset");

		String word = keyword.contains(" ") ? keyword.toLowerCase().replace(
				" ", "+") : keyword;

		try {
			URL url = new URL(google + URLEncoder.encode(word, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);
			res = new Gson().fromJson(reader, ResponseHandler.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JSONObject rs = new JSONObject();
			JSONObject resultObject = new JSONObject();
			JSONArray resArr = new JSONArray();

			for (Result rss : res.getResponseData().getResults()) {
				resultObject.put("title", rss.getTitleNoFormatting());
				resultObject.put("url", rss.getUrl());
				resultObject.put("description", rss.getContent());
				resArr.add(resultObject);
			}

			rs.put(keyword, resArr);

			singleTrends.add(rs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return singleTrends;
	}
}