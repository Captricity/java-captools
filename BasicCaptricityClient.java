// import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
	
	
	private static JSONObject makePostCall(String apiToken, String target, JSONObject payload) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost postRequest = new HttpPost(target);
			postRequest.addHeader("Captricity-API-Token", apiToken);
			// add payload to the mix
			StringEntity input = new StringEntity(payload.toString());
			input.setContentType("application/json");
			postRequest.setEntity(input);
			
			CloseableHttpResponse response = client.execute(postRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String json_string = EntityUtils.toString(entity);
				return new JSONObject(json_string);
	    }
			
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
			client.close();
	  }
		return new JSONObject();
	}
	
	
	private static JSONArray showBatches(String apiToken) throws Exception {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
    JSONArray response = makeGetCall(apiToken, batchesUri);
		return response;
	}
	
	
	private static JSONObject createBatch(String apiToken, String name, Boolean sorting_enabled, Boolean is_sorting_only) throws Exception {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
		// assemble payload
		JSONObject payload = new JSONObject();
		payload.put("name", name);
		payload.put("sorting_enabled", sorting_enabled);
		payload.put("is_sorting_only", is_sorting_only);
		JSONObject response = makePostCall(apiToken, batchesUri, payload);
		return response;
	}
	
	
	// private static JSONArray addBatchFile() throws Exception {
	//
	// }
	
	
	private static JSONArray showDocuments(String apiToken) throws Exception {
		String documentsUri = "https://shreddr.captricity.com/api/v1/document/";
		JSONArray response = makeGetCall(apiToken, documentsUri);
		return response;
	}
	
	
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("METLIFE_API_TOKEN");
			
			JSONObject newBatch = createBatch(apiToken, "Test Java Batch-1", true, false);
			System.out.println("New Batch:  " + newBatch.getInt("id") + ", " + newBatch.getString("name"));
			System.out.println();
			
      JSONArray batches = showBatches(apiToken);
			int numOfBatches = batches.length();
			System.out.println("Number of Batches = " + numOfBatches);
			if ( numOfBatches > 0 ) {
				for (int i = 0; i < numOfBatches; i = i + 1) {
					JSONObject batch = batches.getJSONObject(i);
					System.out.println("Batch:  " + batch.getInt("id") + ", " + 
														  batch.getString("name") + " (files = " + batch.getInt("file_count") + ")");
					// System.out.println(batch.toString(2));
					// System.out.println();
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
					// System.out.println();
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