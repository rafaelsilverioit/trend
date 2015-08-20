package science.rafael.trends;

import java.util.List;

/**
 * Format responses from Ajax requests
 * 
 * @author Rafael Silvério Amaral
 */
public class ResponseHandler {
	private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }

    public static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }

    public static class Result {
        private String url;
        private String titleNoFormatting;
        private String content;
        public String getUrl() { return url; }
        public String getTitleNoFormatting() { return titleNoFormatting; }
        public String getContent() { return content; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String titleNoFormatting) { this.titleNoFormatting = titleNoFormatting; }
        public void setContent(String content) { this.content = content; }

		public String toString() {
			return "{url:"
					+ url
					+ ",title:"
					+ titleNoFormatting
					+ ",content:"
					+ content.replace("\n", " ").replace("<b>", "")
							.replace("</b>", "") + "}";
		}
	}
}
