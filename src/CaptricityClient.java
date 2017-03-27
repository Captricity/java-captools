package com.captricity.shreddr.restapipackage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class CaptricityClient {
	
	private String apiToken;
	private String endpoint;
	
	/**
	 * Create a new instace of the CaptricityClient class.
	 * 
	 * @param token is the Captricity API token
	 * @param url is the base url to the Captricity API
	 * @return CaptricityClient
	 */
	public CaptricityClient(String token, String url) {
		apiToken = token;
		endpoint = url;
	}

	/**
	 * Create a new instace of the CaptricityClient class.
	 * 
	 * @param token is the Captricity API token
	 * @return CaptricityClient
	 */
	public CaptricityClient(String token) {
		this(token, "https://shreddr.captricity.com");
	}

	/**
	 * Method to GET a JSONArray from a REST API endpoint.
	 * 
	 * @param target is the REST API endpoint 
	 * @return JSONArray of the result 
	 * @throws Exception 
	 */
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
		
	/**
	 * Make a REST GET API Call.
	 * 
	 * @param target This reperesent the API substring to be used to create the full API.
	 * @return JSONObject of the results of the API call.
	 * @throws Exception 
	 */
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
	
	/**
	 * Make a REST POST API Call.
	 * 
	 * @param target This reperesent the API substring to be used to create the full API.
	 * @param payload A JSONObject of the data that needs to be sent to make the POST Call.
	 * @return JSONObject of the results of the API call.
	 * @throws Exception 
	 */	
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
	
	/**
	 * Make a REST Delete API Call.
	 * 
	 * @param target This reperesent the API substring to be used to create the full API.
	 * @return JSONObject of the results of the API call.
	 * @throws Exception 
	 */
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

	
	/**
	 * Sort a JSON Array based on a field in each JSONObject in the Array.
	 *
	 * @param array JSONArray to be sorted.
	 * @param sortingField Field used to sort JSONArray.
	 * @return Sorted JSONArray.
	 * @throws Exception 
	 */	
	private JSONArray sortJsonArray(JSONArray array, String sortField) {
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsons.add(array.getJSONObject(i));
		}
		Collections.sort(jsons, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject lhs, JSONObject rhs) {
				String lid = lhs.getString(sortField);
				String rid = rhs.getString(sortField);
				return lid.compareTo(rid);
			}
		});
		return new JSONArray(jsons);
	}


	/**
	 * Show All Batches for the current user a/c.
	 * 
	 * @return JSONArray of all Batches.
	 * @throws Exception 
	 */
	public JSONArray showBatches() throws Exception {
		String batchesUri = endpoint + "/api/v1/batch/";
		JSONArray response = makeGetArrayCall(batchesUri);
		return response;
	}
	
	/**
	 * Create a new Batch with sorting parameters and Document IDs.
	 * 
	 * @param name Name of the Batch being created.
	 * @param sortingEnabled Boolean flag to indicate whether the Batch can be sorted.
	 * @param isSortingOnly Boolean flag to indicate whether the Batch should onle be sorted and not digitized. 
	 * @param docIds List of Document/Template Ids to be used to sort and digitize the batch to.
	 * @return JSONObject representing the Batch details of the Batch.
	 * @throws Exception 
	 */	
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
	
	/**
	 * Create a new Batch and associate it to documents in Captricity.
	 * 
	 * @param name Name of the Batch being created.
	 * @param docIds List of Document/Template Ids to be used to sort and digitize the batch to.
	 * @return JSONObject representing the Batch details of the Batch.
	 * @throws Exception 
	 */
	public JSONObject createBatch(String name, ArrayList<Integer> docIds) throws Exception {
		return createBatch(name, true, false, docIds);
	}

	/**
	 * Create a new Batch with sorting parameters.
	 * 
	 * @param name Name of the Batch being created.
	 * @param sortingEnabled Boolean flag to indicate whether the Batch can be sorted.
	 * @param isSortingOnly Boolean flag to indicate whether the Batch should onle be sorted and not digitized. 
	 * @return JSONObject representing the Batch details of the Batch.
	 * @throws Exception 
	 */
	public JSONObject createBatch(String name, Boolean sortingEnabled, Boolean isSortingOnly) throws Exception {
		ArrayList<Integer> docIds = new ArrayList<Integer>();
		return createBatch(name, sortingEnabled, isSortingOnly, docIds);
	}
	
	/**
	 * Create a new Batch.
	 * 
	 * @param name Name of the Batch being created.
	 * @return JSONObject representing the Batch details of the Batch that was created.
	 * @throws Exception 
	 */
	public JSONObject createBatch(String name) throws Exception {
		ArrayList<Integer> docIds = new ArrayList<Integer>();
		return createBatch(name, true, false, docIds);
	}

	/**
	 * Get Batch Details
	 * 
	 * @param batchID of Batch
	 * @return JSONObject representing the Batch details of the Batch.
	 * @throws Exception 
	 */
	public JSONObject readBatch(int batchID) throws Exception {
		String readBatchUri = endpoint + "/api/v1/batch/" + batchID;
		JSONObject response = makeGetObjectCall(readBatchUri);
		return response;
	}
	
	/**
	 * Delete a Batch.
	 * 
	 * @param batchID of Batch
	 * @return JSONObject representing the Batch details of the Batch that was deleted.
	 * @throws Exception 
	 */
	public JSONObject deleteBatch(int batchID) throws Exception {
		String deleteBatchUri = endpoint + "/api/v1/batch/" + batchID;
		// JSONObject payload = new JSONObject();
		JSONObject response = makeDeleteCall(deleteBatchUri);
		return response;
	}
	
	/**
	 * Add A File to A Batch.
	 * 
	 * @param batchID of Batch to add the file to.
	 * @param fileName UNC path to file.
	 * @param metadata a JSONObject of data that needs to be output per batch file in the Digitzed Results.
	 * @return JSONObject representing a BatchFile that was created.
	 * @throws Exception 
	 */
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

	/**
	 * Add a new File to a Batch.
	 * 
	 * @param batchID of Batch to add the file to.
	 * @param fileName UNC path to file.
	 * @return JSONObject representing a BatchFile that was created.
	 * @throws Exception
	 */
	public JSONObject addFileToBatch(int batchID, String fileName) throws Exception {
		JSONObject meta = new JSONObject();
		return addFileToBatch(batchID, fileName, meta);
	}
	
	/**
	 * Method to Submit a Batch
	 * 
	 * @param batchID of Batch.
	 * @return JSONObject representing the Batch Details of the Batch that was submitted.
	 */
	public JSONObject submitBatch(int batchID) throws Exception {
		String submitBatchUri = endpoint + "/api/v1/batch/" + batchID + "/submit";
		JSONObject payload = new JSONObject();
		JSONObject response = makePostCall(submitBatchUri, payload);
		return response;
	}
	
	/**
	 * Returns batch info for all IDs passed in the idList.
	 * 
	 * @param idList List of Batch IDs 
	 * @return JSONArray of batch information for all Batch IDs
	 * @throws Exception
	 */
	public JSONArray getMultipleBatchInfoById(List<String> idList) throws Exception {

		// Check that idList is not null or empty
		if (idList == null || idList.isEmpty()){
			throw new Exception("list of ids must contain at least one id");
		}
		
		CloseableHttpClient client = null;
		String result = null;
		
		// create a comma delimited string of Ids from List
		String ids = String.join(",", idList);
		
		try {
			client = HttpClients.createDefault();

			HttpGet getRequest = new HttpGet(endpoint + "/api/v1/batch/?ids="+ ids);
			getRequest.addHeader("Captricity-API-Token", apiToken);

			CloseableHttpResponse response = client.execute(getRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}		
		} catch (Exception e) {
			throw new Exception("Error getting batch info based on Ids", e);
		} finally {
			client.close();
		}
		
		return new JSONArray(result);
	}
	
	/**
	 * Get String Representation of Batch Results.
	 *
	 * @param batchID of Batch.
	 * @param verboseResults Boolean Flag indicating whether the results should contain extra verbose information.
	 * @return String data of the Batch Results.
	 * @throws Exception 
	 */
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
	/**
	 * Get the Batch Results
	 * 
	 * @param batchID 
	 * @return String of Batch Results 
	 * @throws Exception 
	 */
	public String getBatchResults(int batchID) throws Exception {
		return getBatchResults(batchID, false);
	}

	/**
	 * Get The Job Status
	 * 
	 * @param jobID for the Job.
	 * @return Job Status Integer
	 * @throws Exception 
	 */
	public int getJobStatus(int jobID) throws Exception {
		String getJobUri = endpoint + "/api/v1/job/" + jobID;
		JSONObject response = makeGetObjectCall(getJobUri);
		int percent_complete = response.getInt("percent_completed");
		return percent_complete;
	}

	/**
	 * Get Job Results.
	 * 
	 * @param jobID for the Job.
	 * @return job results string.
	 * @throws Exception 
	 */
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
	
	/**
	 * Get All Templates.
	 * 
	 * @return JSONArray of all Templates
	 * @throws Exception 
	 */
	public JSONArray showTemplates() throws Exception {
		String documentsUri = endpoint + "/api/v1/document/";
		JSONArray response = makeGetArrayCall(documentsUri);
		return response;
	}

	/**
	 * Name Compatibility Method to get all Documents/Templates.
	 * 
	 * @return JSONArray of all Documents/Templates.
	 * @throws Exception 
	 */
	public JSONArray showDocuments() throws Exception {
    // included method to maintain backwards-compatibility
		return showTemplates();
	}

	/**
	 * Get Template Details.
	 * 
	 * @param templateId of Template.
	 * @return JSONObject representing Template.
	 * @throws Exception 
	 */
	public JSONObject readTemplate(int templateId) throws Exception {
		String readTemplateUri = endpoint + "/api/v1/document/" + templateId;
		JSONObject response = makeGetObjectCall(readTemplateUri);
		return response;
	}

	/**
	 * Get All Fields on a Template Sheet.
	 * 
	 * @param sheetId of Sheet.
	 * @return JSONArray of all Fields on the Sheet.
	 * @throws Exception 
	 */
	public JSONArray getSheetFields(int sheetId) throws Exception {
		String readSheetUri = endpoint + "/api/v1/sheet/" + sheetId + "/field/";
		JSONArray response = makeGetArrayCall(readSheetUri);
		return response;
	}

	/**
	 * Get all Fields for a Template.
	 * 
	 * @param templateId of Template
	 * @param includeChoices Boolean Flag for Including OMR/Choice field Data.
	 * @return String Results of all Fields on a Template.
	 * @throws Exception 
	 */
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
						int pageNum = sheet.getInt("page_number") + 1;
						fieldList.append("Page number:  " + pageNum + "\n");
						JSONArray unsortedFields = getSheetFields(sheet.getInt("id"));
						if ( unsortedFields.length() > 0 ) {
							JSONArray fields = sortJsonArray(unsortedFields, "name");
							for (int j=0; j < fields.length(); j++) {
								JSONObject field = fields.getJSONObject(j);
                // fieldList.append(field.getInt("id") + ", " + field.getString("name") + ", " + field.getString("friendly_name"));
								fieldList.append(field.getString("name") + ", " + field.getString("friendly_name"));
								if (includeChoices) {
									if ( field.getString("friendly_name").equals("Select many") | field.getString("friendly_name").equals("Select one") ) {
										fieldList.append(", " + field.getJSONArray("categorical_constraint").toString());
									}
								}
								fieldList.append("\n");
							}
						}
						fieldList.append("\n");
					}
				}
			}

		} else {
			fieldList.append("Sorry, either no template with that ID exists or it is not currently active.");
		}

		return fieldList.toString();
	}

	/**
	 * List Template Fields
	 * 
	 * @param templateId of Template
	 * @return String Results of all Fields on a Template.
	 * @throws Exception
	 */
	public String listTemplateFields(int templateId) throws Exception {
		return listTemplateFields(templateId, false); 
	}

}
