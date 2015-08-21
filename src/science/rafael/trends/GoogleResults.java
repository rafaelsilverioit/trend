package science.rafael.trends;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import science.rafael.utils.PropertyManager;

/**
 * Google Search Client
 * 
 * Google Custom Search method
 * 
 * Limit: 100 requests/day (FREE VERSION!!)
 * 
 * @author Rafael Silvério Amaral
 * @email contato@rafael.science
 */
public class GoogleResults {
	// stores trend words
	private static JSONArray trends;
	private static JSONArray resultTrends;
	private static JSONArray singleTrends;

	// configuration properties
	private static Properties confs;

	// prepare urls
	private static URI url;

	// handles response
	private static HttpResponse result;

	// used to parse json response
	private static String json;

	public GoogleResults() {

	}

	/**
	 * Returns google results from a List of trend keywords
	 * 
	 * @param keyword
	 *            - String keyword to be searched
	 * @return trends - JSONArray of JSONObjects that represents the results
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getTrendResults(List<String> keywords) throws Exception {
		// disable SSL verification, because (don't know why) googleapis
		// certificate is not listed by org.apache 4, but will be in next
		// release
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

		trends = new JSONArray();

		try {
			// loads configurations
			confs = PropertyManager.readProperties("search.properties");
		} catch (FileNotFoundException nf) {
			nf.printStackTrace();
		}

		// set parameters
		String key = confs.getProperty("key");
		String cx = confs.getProperty("cx");
		String charset = confs.getProperty("charset");

		for (String keyword : keywords) {
			// cleans keyword
			String word = keyword.contains(" ") ? keyword.toLowerCase()
					.replace(" ", "+") : keyword;
			try {
				// prepares url
				url = new URI("https://www.googleapis.com/customsearch/v1?key="
						+ key + "&cx=" + cx + "&q=" + word + "&alt=json");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// creates get request
			HttpGet get = new HttpGet(url);

			try {
				// do the request and receives the response
				result = httpClient.execute(get);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				// format
				json = EntityUtils.toString(result.getEntity(), charset);
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}

			resultTrends = new JSONArray();

			try {
				JSONParser parser = new JSONParser();
				// parse json
				Object resultObject = parser.parse(json);

				// creates JSONObject from Object
				JSONObject obj = (JSONObject) resultObject;

				// select and creates JSONArray from "items"
				JSONArray jA = (JSONArray) obj.get("items");

				for (Object ob : jA) {
					JSONObject jObj = (JSONObject) ob;

					// creates JSONObject with a single result,
					// then adds to resultTrends
					JSONObject results = new JSONObject();

					results.put("title", jObj.get("title"));
					results.put("description", jObj.get("snippet"));
					results.put("url", jObj.get("formattedUrl"));

					resultTrends.add(results);
				}

				// associates above results with the specific keyword
				JSONObject resTrend = new JSONObject();
				resTrend.put(keyword, resultTrends);

				// then adds to a JSONArray
				trends.add(resTrend);
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
	public JSONArray getSingleTrendResults(String keyword) {
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

		try {
			confs = PropertyManager.readProperties("search.properties");
		} catch (FileNotFoundException nf) {
			nf.printStackTrace();
		}

		String key = confs.getProperty("key");
		String cx = confs.getProperty("cx");
		String charset = confs.getProperty("charset");

		try {
			url = new URI("https://www.googleapis.com/customsearch/v1?key="
					+ key + "&cx=" + cx + "&q=" + keyword.replace(" ", "+")
					+ "&alt=json");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		HttpGet get = new HttpGet(url);
		HttpResponse result = null;

		try {
			result = httpClient.execute(get);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String json = null;

		try {
			json = EntityUtils.toString(result.getEntity(), charset);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

		singleTrends = new JSONArray();

		try {
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(json);

			JSONObject obj = (JSONObject) resultObject;
			JSONArray jA = (JSONArray) obj.get("items");

			for (Object ob : jA) {
				JSONObject jObj = (JSONObject) ob;

				JSONObject results = new JSONObject();

				results.put("title", jObj.get("title"));
				results.put("description", jObj.get("snippet"));
				results.put("url", jObj.get("formattedUrl"));

				singleTrends.add(results);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return singleTrends;
	}
}