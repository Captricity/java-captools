package com.captricity.api;

import java.io.File;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
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
	
	private JSONArray makeGetArrayCall(String target) throws Exception {
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
	
	private JSONObject makeGetObjectCall(String target) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet getRequest = new HttpGet(target);
			getRequest.addHeader("Captricity-API-Token", apiToken);
			
			CloseableHttpResponse response = client.execute(getRequest);
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
	
	private JSONObject makePostCall(String target, JSONObject payload) throws Exception {
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
	
	private JSONObject makeDeleteCall(String target) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpDelete deleteRequest = new HttpDelete(target);
			deleteRequest.addHeader("Captricity-API-Token", apiToken);
			// add payload to the mix
			// StringEntity input = new StringEntity(payload.toString());
			// input.setContentType("application/json");
			// deleteRequest.setEntity(input);
			
			CloseableHttpResponse response = client.execute(deleteRequest);
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
	
	public JSONArray showBatches() throws Exception {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
    JSONArray response = makeGetArrayCall(batchesUri);
		return response;
	}
	
	public JSONObject createBatch(String name, Boolean sorting_enabled, Boolean is_sorting_only) throws Exception {
		String batchesUri = "https://shreddr.captricity.com/api/v1/batch/";
		// assemble payload
		JSONObject payload = new JSONObject();
		payload.put("name", name);
		payload.put("sorting_enabled", sorting_enabled);
		payload.put("is_sorting_only", is_sorting_only);
		JSONObject response = makePostCall(batchesUri, payload);
		return response;
	}
	
  public JSONObject createBatch(String name) throws Exception {
    return createBatch(name, true, false);
  }
  
	public JSONObject readBatch(int batchID) throws Exception {
		String readBatchUri = "https://shreddr.captricity.com/api/v1/batch/" + batchID;
    JSONObject response = makeGetObjectCall(readBatchUri);
		return response;
	}
	
	public JSONObject deleteBatch(int batchID) throws Exception {
		String deleteBatchUri = "https://shreddr.captricity.com/api/v1/batch/" + batchID;
		// JSONObject payload = new JSONObject();
		JSONObject response = makeDeleteCall(deleteBatchUri);
		return response;
	}
	
	public JSONObject addFileToBatch(int batchID, String fileName, String metadata) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			File new_batch_file = new File(fileName);
			
			HttpPost postRequest = new HttpPost("https://shreddr.captricity.com/api/v1/batch/" + batchID + "/batch-file/");
			postRequest.addHeader("Captricity-API-Token", apiToken);
			
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("uploaded_with", "api");
			builder.addTextBody("file_name", new_batch_file.getName());
      if (metadata != null) {
        builder.addTextBody("metadata", metadata);
      }
      builder.addBinaryBody("uploaded_file", new_batch_file);
      
			HttpEntity input = builder.build();
      
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
  
  public JSONObject addFileToBatch(int batchID, String fileName) throws Exception {
    return addFileToBatch(batchID, fileName, null);
  }
	
	public JSONObject submitBatch(int batchID) throws Exception {
		String submitBatchUri = "https://shreddr.captricity.com/api/v1/batch/" + batchID + "/submit";
		JSONObject payload = new JSONObject();
		JSONObject response = makePostCall(submitBatchUri, payload);
		return response;
	}
	
  public String getBatchResults(int batchID, Boolean verboseResults) throws Exception {
    StringBuilder results = new StringBuilder();
    JSONObject batch = readBatch(batchID);
		
    if ( batch.isNull("parent_id") ) {
      // indicates original batch submitted by customer...
      
      if ( verboseResults ) {
        results.append("Batch:  " + batch.getInt("id") + "\n");
        results.append("Name: " + batch.getString("name") + "\n");
        results.append("Status: " + batch.getString("status") + "\n");
        results.append("File Count: " + batch.getInt("file_count") + "\n");
        results.append("Is digitized: " + batch.getBoolean("is_digitized") + "\n");
      }
      
      if ( batch.getBoolean("is_digitized") ) {
        
        if ( ! batch.isNull("related_job_id") ) {
          
          if ( verboseResults ) {
            results.append("Related Job ID: " + batch.getInt("related_job_id") + " results:\n");
          }
          String jobResults = getJobResults(batch.getInt("related_job_id"));
          results.append(jobResults);
        
        } else {
          
          if ( verboseResults ) {
            results.append("Looking for child Batches resulting from sorting...\n");
          }
          JSONArray children = batch.getJSONArray("children_ids");
          if ( children.length() > 0 ) {
            for (int j=0; j < children.length(); j++) {
              int childBatchId = children.getInt(j);
              JSONObject childBatch = readBatch(childBatchId);
              if ( verboseResults ) {
                results.append(childBatch.getInt("id") + ":  " + childBatch.getString("name") + "\n");
              }
              if ( ! childBatch.isNull("related_job_id") ) {
                if ( verboseResults ) {
                  results.append("Related Job ID: " + childBatch.getInt("related_job_id") + " results:\n");
                }
                String jobResults = getJobResults(childBatch.getInt("related_job_id"));
                results.append(jobResults);
              }
            }
          
          } else {
            // No child Batches present...
            results.append("No results found.\n");
          }
        }
        
      } else {
        // is_digitized is false
        
        int rejectCount = 0;
        if ( verboseResults ) {
          results.append("Looking for child Batches resulting from sorting...\n");
        }
        JSONArray children = batch.getJSONArray("children_ids");
        if ( children.length() > 0 ) {
          for (int k=0; k < children.length(); k++) {
            int childBatchId = children.getInt(k);
            JSONObject childBatch = readBatch(childBatchId);
            if ( verboseResults ) {
              results.append(childBatch.getInt("id") + ":  " + childBatch.getString("name") + "\n");
            }
            if ( childBatch.getString("status").equals("rejected") ) {
              if ( verboseResults ) {
                results.append(childBatch.getString("status") + "\n");
              }
              rejectCount++;
            } else if ( ! childBatch.isNull("related_job_id") ) {
              int jobStatus = getJobStatus(childBatch.getInt("related_job_id"));
              if ( verboseResults ) {
                results.append("Related Job ID: " + childBatch.getInt("related_job_id") + " (" + jobStatus + "% complete)\n");
              }
            } else {
              if ( verboseResults ) {
                results.append(childBatch.getString("status") + "\n");
              }
            }
          }
        } else {
          // No child Batches present...
          results.append("No results found.\n");
        }
        if ( rejectCount == children.length() ) {
          results.append("This Batch contained no matching documents.\n");
        } else {
          results.append("Batch has not finished digitization yet.\n");
        }
      }
    } else {
      // case of an internal child batch -- don't want to show anything for this type of batch
      results.append("");
    }
    return results.toString();
  }
  
  public String getBatchResults(int batchID) throws Exception {
    return getBatchResults(batchID, false);
  }
  
	public int getJobStatus(int jobID) throws Exception {
		String getJobUri = "https://shreddr.captricity.com/api/v1/job/" + jobID;
    JSONObject response = makeGetObjectCall(getJobUri);
		int percent_complete = response.getInt("percent_completed");
		return percent_complete;
	}
	
	public String getJobResults(int jobID) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			int jobStatus = getJobStatus(jobID);
			if ( jobStatus == 100 ) {
				HttpGet getRequest = new HttpGet("https://shreddr.captricity.com/api/v1/job/" + jobID + "/csv/");
				getRequest.addHeader("Captricity-API-Token", apiToken);
			
				CloseableHttpResponse response = client.execute(getRequest);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String results_string = EntityUtils.toString(entity);
					return results_string;
		    }
			} else {
				return new String("Job is only " + jobStatus + "% complete at this time.");
			}
			
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
			client.close();
	  }
		return new String("There were no job results found for the specified Job ID.");
	}
	
	public JSONArray showDocuments() throws Exception {
		String documentsUri = "https://shreddr.captricity.com/api/v1/document/";
		JSONArray response = makeGetArrayCall(documentsUri);
		return response;
	}
}
