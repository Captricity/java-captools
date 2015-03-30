package com.captricity.api;

import java.io.File;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.*;

public class CaptricityClient {
	
	private String apiToken;
	
	public CaptricityClient(String token) {
		apiToken = token;
	}
	
	private static JSONArray makeGetCall(String target) throws Exception {
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
	
	private static JSONObject makePostCall(String target, JSONObject payload) throws Exception {
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
	
	public static JSONArray showBatches() throws Exception {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
    JSONArray response = makeGetCall(apiToken, batchesUri);
		return response;
	}
	
	public static JSONObject createBatch(String name, Boolean sorting_enabled, Boolean is_sorting_only) throws Exception {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
		// assemble payload
		JSONObject payload = new JSONObject();
		payload.put("name", name);
		payload.put("sorting_enabled", sorting_enabled);
		payload.put("is_sorting_only", is_sorting_only);
		JSONObject response = makePostCall(apiToken, batchesUri, payload);
		return response;
	}
	
	public static JSONObject addFileToBatch(int batchID, String fileName) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			File new_batch_file = new File(fileName);
			
			HttpPost postRequest = new HttpPost("https://shreddr.captricity.com/api/v1/batch/" + batchID + "/batch-file/");
			postRequest.addHeader("Captricity-API-Token", apiToken);
			
			HttpEntity input = MultipartEntityBuilder
			 .create()
			 .addTextBody("uploaded_with", "api")
			 .addTextBody("file_name", new_batch_file.getName())
			 .addBinaryBody("uploaded_file", new_batch_file)
			 .build();
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
	
	public static JSONArray showDocuments() throws Exception {
		String documentsUri = "https://shreddr.captricity.com/api/v1/document/";
		JSONArray response = makeGetCall(apiToken, documentsUri);
		return response;
	}
}