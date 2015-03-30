import com.captricity.api.CaptricityClient;
import org.json.*;

public class TestCaptricityClient {
	
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("TEST_API_TOKEN");
			
			CaptricityClient capClient = new CaptricityClient(apiToken);
			
			// JSONObject newBatch = createBatch(apiToken, "Test Java Batch-1", true, false);
			// System.out.println("New Batch:  " + newBatch.getInt("id") + ", " + newBatch.getString("name"));
			// System.out.println();
			
			// JSONObject batchFile = addFileToBatch(apiToken, newBatch.getInt("id"), "/Users/davids/Desktop/EZ-return1.pdf");
			// System.out.println("Batch File:  " + batchFile.getString("file_name") + ", " + batchFile.getString("uuid"));
			// // System.out.println(batchFile);
			// System.out.println();
			
      JSONArray batches = capClient.showBatches();
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
			
			// JSONArray documents = showDocuments(apiToken);
			// int numOfDocuments = documents.length();
			// System.out.println("Number of Documents = " + numOfDocuments);
			// if ( numOfDocuments > 0 ) {
			// 	for (int i = 0; i < numOfDocuments; i = i + 1) {
			// 		JSONObject doc = documents.getJSONObject(i);
			// 		System.out.println("Template:  " + doc.getInt("id") + ", " + doc.getString("name"));
			// 		// System.out.println(doc.toString(2));
			// 		// System.out.println();
			// 	}
			// }
			// System.out.println();
			
      return;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
}


