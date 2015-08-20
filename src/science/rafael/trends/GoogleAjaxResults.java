package science.rafael.trends;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import science.rafael.trends.ResponseHandler.Result;

import com.google.gson.Gson;

/**
 * Google Search Client - Ajax version
 * 
 * @author Rafael Silvério Amaral
 * @email  contato@rafael.science
 */
public class GoogleAjaxResults {
	private static JSONArray trends;
	private static JSONArray singleTrends;
	
	public GoogleAjaxResults() {

	}

	/**
	 * Returns google results from a List of trend keywords
	 * 
	 * @param keyword
	 *            - String keyword to be searched
	 * @return trends - JSONArray of JSONObjects that represents the results
	 */
	@SuppressWarnings({ "unchecked"})
	public JSONArray getTrendResults(List<String> keywords) throws Exception {
		trends = new JSONArray();
		
		for (String keyword : keywords) {
			String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
			String word = keyword.contains(" ") ? 
					keyword.toLowerCase().replace(" ", "+") : 
					keyword;
			String charset = "UTF-8";

			ResponseHandler res = null;
			
			try{
			URL url = new URL(google + URLEncoder.encode(word, charset));
		    Reader reader = new InputStreamReader(url.openStream(), charset);
		    res = new Gson().fromJson(reader, ResponseHandler.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				JSONObject rs = new JSONObject();
				JSONObject gObj = new JSONObject();
				JSONArray rsArr = new JSONArray();
				
				for(Result x :res.getResponseData().getResults()){
					gObj.put("title",x.getTitleNoFormatting());
					gObj.put("url", x.getUrl());
					gObj.put("description", x.getContent());
					rsArr.add(gObj);
				}
				
				rs.put(keyword, rsArr);
				
				trends.add(rs);

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
	public JSONArray getSingleTrendResults(String keyword) {

		String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String word = keyword.contains(" ") ? 
				keyword.toLowerCase().replace(" ", "+") : 
				keyword;
		String charset = "UTF-8";

		ResponseHandler res = null;
		
		try{
			URL url = new URL(google + URLEncoder.encode(word, charset));
		    Reader reader = new InputStreamReader(url.openStream(), charset);
		    res = new Gson().fromJson(reader, ResponseHandler.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JSONObject rs = new JSONObject();
			JSONObject gObj = new JSONObject();
			JSONArray rsArr = new JSONArray();
			
			for(Result x :res.getResponseData().getResults()){
				gObj.put("title",x.getTitleNoFormatting());
				gObj.put("url", x.getUrl());
				gObj.put("description", x.getContent());
				rsArr.add(gObj);
			}
			
			rs.put(keyword, rsArr);
			
			singleTrends.add(rs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return singleTrends;
	}
}