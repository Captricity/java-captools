package com.captricity.api;

import java.io.File;
import java.util.ArrayList;
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
  private String endpoint;
	
  public CaptricityClient(String token, String url) {
    apiToken = token;
    endpoint = url;
  }
  
	public CaptricityClient(String token) {
		this(token, "https://shreddr.captricity.com");
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
		String batchesUri = endpoint + "/api/v1/batch/";
    JSONArray response = makeGetArrayCall(batchesUri);
		return response;
	}
	
	public JSONObject createBatch(String name, Boolean sortingEnabled, Boolean isSortingOnly, ArrayList<Integer> docIds) throws Exception {
		String batchesUri = endpoint + "/api/v1/batch/";
    JSONArray docs = new JSONArray(docIds);
		// assemble payload
		JSONObject payload = new JSONObject();
		payload.put("name", name);
		payload.put("sorting_enabled", sortingEnabled);
		payload.put("is_sorting_only", isSortingOnly);
    if ( docs.length() > 0 ) {
      payload.put("documents", docs);
    }
		JSONObject response = makePostCall(batchesUri, payload);
		return response;
	}
	
  public JSONObject createBatch(String name, ArrayList<Integer> docIds) throws Exception {
    return createBatch(name, true, false, docIds);
  }
  
  public JSONObject createBatch(String name, Boolean sortingEnabled, Boolean isSortingOnly) throws Exception {
    ArrayList<Integer> docIds = new ArrayList<Integer>();
    return createBatch(name, sortingEnabled, isSortingOnly, docIds);
  }
  
  public JSONObject createBatch(String name) throws Exception {
    ArrayList<Integer> docIds = new ArrayList<Integer>();
    return createBatch(name, true, false, docIds);
  }
  
	public JSONObject readBatch(int batchID) throws Exception {
		String readBatchUri = endpoint + "/api/v1/batch/" + batchID;
    JSONObject response = makeGetObjectCall(readBatchUri);
		return response;
	}
	
	public JSONObject deleteBatch(int batchID) throws Exception {
		String deleteBatchUri = endpoint + "/api/v1/batch/" + batchID;
		// JSONObject payload = new JSONObject();
		JSONObject response = makeDeleteCall(deleteBatchUri);
		return response;
	}
	
	public JSONObject addFileToBatch(int batchID, String fileName, JSONObject metadata) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			File new_batch_file = new File(fileName);
			
			HttpPost postRequest = new HttpPost(endpoint + "/api/v1/batch/" + batchID + "/batch-file/");
			postRequest.addHeader("Captricity-API-Token", apiToken);
			
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("uploaded_with", "api");
			builder.addTextBody("file_name", new_batch_file.getName());
      if (metadata.length() > 0) {
        builder.addTextBody("metadata", metadata.toString());
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
    JSONObject meta = new JSONObject();
    return addFileToBatch(batchID, fileName, meta);
  }
	
	public JSONObject submitBatch(int batchID) throws Exception {
		String submitBatchUri = endpoint + "/api/v1/batch/" + batchID + "/submit";
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
        
      } else if ( batch.getString("status").equals("setup") ) {
        // batch has not been submitted yet...
        results.append("Batch has not been submitted yet.\n");
      
      } else if ( batch.getString("status").equals("processed") ) {
        // is_digitized is false but it is marked as processed
        
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
      } else {
        // in an in-between state
        results.append("Batch is processing.  Please check back later.");
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
		String getJobUri = endpoint + "/api/v1/job/" + jobID;
    JSONObject response = makeGetObjectCall(getJobUri);
		int percent_complete = response.getInt("percent_completed");
		return percent_complete;
	}
	
	public String getJobResults(int jobID) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			int jobStatus = getJobStatus(jobID);
			if ( jobStatus == 100 ) {
				HttpGet getRequest = new HttpGet(endpoint + "/api/v1/job/" + jobID + "/csv/?include-json-metadata-column=true");
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
	
	public JSONArray showTemplates() throws Exception {
		String documentsUri = endpoint + "/api/v1/document/";
		JSONArray response = makeGetArrayCall(documentsUri);
		return response;
	}
  
	public JSONArray showDocuments() throws Exception {
    // included method to maintain backwards-compatibility
		return showTemplates();
	}
  
	public JSONObject readTemplate(int templateId) throws Exception {
		String readTemplateUri = endpoint + "/api/v1/document/" + templateId;
    JSONObject response = makeGetObjectCall(readTemplateUri);
		return response;
	}
  
  public JSONArray getSheetFields(int sheetId) throws Exception {
    String readSheetUri = endpoint + "/api/v1/sheet/" + sheetId + "/field/";
		JSONArray response = makeGetArrayCall(readSheetUri);
		return response;
  }
  
	public String listTemplateFields(int templateId, Boolean includeChoices) throws Exception {
    StringBuilder fieldList = new StringBuilder();
    JSONObject template = readTemplate(templateId);
    
    if ( template.getBoolean("active") & template.getBoolean("user_visible") ) {
      fieldList.append("Template ID:  " + template.getInt("id") + "\n");
      fieldList.append("Template name:  " + template.getString("name") + "\n");
      fieldList.append("Template page count:  " + template.getInt("page_count") + "\n");
      fieldList.append("Versioned template?  " + template.getBoolean("is_versioned") + "\n");
      
      if ( template.getBoolean("has_fields") ) {
        JSONArray sheets = template.getJSONArray("sheets");
        if ( sheets.length() > 0 ) {
          for (int i=0; i < sheets.length(); i++) {
            JSONObject sheet = sheets.getJSONObject(i);
            fieldList.append("Page number:  " + sheet.getInt("page_number") + "\n");
            JSONArray fields = getSheetFields(sheet.getInt("id"));
            if ( fields.length() > 0 ) {
              for (int j=0; j < fields.length(); j++) {
                JSONObject field = fields.getJSONObject(j);
                fieldList.append(field.getString("name") + ", " + field.getString("friendly_name"));
                if (includeChoices) {
                  if ( field.getString("friendly_name").equals("Select many") | field.getString("friendly_name").equals("Select one") ) {
                    fieldList.append(", " + field.getJSONArray("categorical_constraint").toString() + "\n");
                  } else {
                    fieldList.append("\n");
                  }
                } else {
                  fieldList.append("\n");
                }
              }
            }
            fieldList.append("\n");
          }
        }
      }

    } else {
      fieldList.append("Sorry, either that no template with that ID exists or it is not currently active.");
    }
    
    return fieldList.toString();
	}
  
  public String listTemplateFields(int templateId) throws Exception {
    return listTemplateFields(templateId, false); 
  }
  
}
