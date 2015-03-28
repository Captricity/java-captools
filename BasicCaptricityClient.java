// import java.io.IOException;
// import org.apache.http.*;
// import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
// import org.apache.http.HttpHost;
// import org.apache.http.HttpRequest;
// import org.apache.http.HttpResponse;
// import org.apache.http.ParseException;
// import org.apache.http.client.CookieStore;
// import org.apache.http.client.CredentialsProvider;
// import org.apache.http.client.config.AuthSchemes;
// import org.apache.http.client.config.CookieSpecs;
// import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
// import org.apache.http.client.protocol.HttpClientContext;
// import org.apache.http.config.ConnectionConfig;
// import org.apache.http.config.MessageConstraints;
// import org.apache.http.config.Registry;
// import org.apache.http.config.RegistryBuilder;
// import org.apache.http.config.SocketConfig;
// import org.apache.http.conn.DnsResolver;
// import org.apache.http.conn.HttpConnectionFactory;
// import org.apache.http.conn.ManagedHttpClientConnection;
// import org.apache.http.conn.routing.HttpRoute;
// import org.apache.http.conn.socket.ConnectionSocketFactory;
// import org.apache.http.conn.socket.PlainConnectionSocketFactory;
// import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
// import org.apache.http.impl.DefaultHttpResponseFactory;
// import org.apache.http.impl.client.BasicCookieStore;
// import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.conn.DefaultHttpResponseParser;
// import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
// import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
// import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
// import org.apache.http.impl.conn.SystemDefaultDnsResolver;
// import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
// import org.apache.http.io.HttpMessageParser;
// import org.apache.http.io.HttpMessageParserFactory;
// import org.apache.http.io.HttpMessageWriterFactory;
// import org.apache.http.io.SessionInputBuffer;
// import org.apache.http.message.BasicHeader;
// import org.apache.http.message.BasicLineParser;
// import org.apache.http.message.LineParser;
// import org.apache.http.ssl.SSLContexts;
// import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.*;

public class BasicCaptricityClient {
	
	private static JSONArray makeGetCall(String apiToken, String target) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			
			HttpGet getRequest = new HttpGet(target);
			getRequest.addHeader("Captricity-API-Token", apiToken);
			
			CloseableHttpResponse response = client.execute(getRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String json_string = EntityUtils.toString(entity);
				return new JSONArray(json_string);
	    } 
			
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
			client.close();
	  }
		return new JSONArray();
	}
	
	
	private static JSONArray makePostCall(String apiToken, String target, JSONObject payload) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			
			HttpPost postRequest = new HttpGet(target);
			getRequest.addHeader("Captricity-API-Token", apiToken);
			// add payload to the mix
			
			CloseableHttpResponse response = client.execute(postRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String json_string = EntityUtils.toString(entity);
				return new JSONArray(json_string);
	    } 
			
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
			client.close();
	  }
		return new JSONArray();
	}
	
	
	private static JSONArray showBatches(String apiToken) throws Exception {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
    JSONArray response = makeGetCall(apiToken, batchesUri);
		return response;
	}
	
	
	private static JSONArray createBatch(String apiToken, String name, Boolean sorting_enabled, Boolean is_sorting_only) {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
		// assemble payload
		JSONObject payload = new JSONObject();
		payload.put("name", name);
		payload.put("sorting_enabled", sorting_enabled);
		payload.put("is_sorting_only", is_sorting_only);
		JSONArray response = makePostCall(apiToken, batchesUri, payload);
		return response;
	}
	
	
	private static JSONArray add
	
	private static JSONArray showDocuments(String apiToken) throws Exception {
		String documentsUri = "https://shreddr.captricity.com/api/v1/document/";
		JSONArray response = makeGetCall(apiToken, documentsUri);
		return response;
	}
	
	
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("METLIFE_API_TOKEN");
			
			JSONArray newBatch = createBatch(apiToken, "Test Java Batch-1", true, false);
			System.out.println(newBatch);
			
      JSONArray batches = showBatches(apiToken);
			int numOfBatches = batches.length();
			System.out.println("Number of Batches = " + numOfBatches);
			if ( numOfBatches > 0 ) {
				for (int i = 0; i < numOfBatches; i = i + 1) {
					JSONObject batch = batches.getJSONObject(i);
					System.out.println("Batch:  " + batch.getInt("id") + ", " + batch.getString("name"));
					// System.out.println(batch.toString(2));
					System.out.println();
				}
			}
			System.out.println();
			
			JSONArray documents = showDocuments(apiToken);
			int numOfDocuments = documents.length();
			System.out.println("Number of Documents = " + numOfDocuments);
			if ( numOfDocuments > 0 ) {
				for (int i = 0; i < numOfDocuments; i = i + 1) {
					JSONObject doc = documents.getJSONObject(i);
					System.out.println("Template:  " + doc.getInt("id") + ", " + doc.getString("name"));
					// System.out.println(doc.toString(2));
					System.out.println();
				}
			}
			System.out.println();
			
      return;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
}