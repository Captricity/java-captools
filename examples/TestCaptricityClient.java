import com.captricity.api.CaptricityClient;
import org.json.*;

public class TestCaptricityClient {
	
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("TEST_API_TOKEN");
			String testFileLocation =  "/Users/davids/Documents/CLEARDTOP/API_demo/EZ-return1.pdf";
      String testFileLocation2 = "/Users/davids/Documents/CLEARDTOP/API_demo/EZ-return2.pdf";
			String testBatchName = "Java Batch with Metadata July29-A";
			
			CaptricityClient capClient = new CaptricityClient(apiToken);
			
			JSONObject newBatch = capClient.createBatch(testBatchName);
			System.out.println("Create New Batch:  " + newBatch.getInt("id") + ", " + newBatch.getString("name"));
			// System.out.println(newBatch);
			System.out.println();

			JSONObject batchFile = capClient.addFileToBatch(newBatch.getInt("id"), testFileLocation);
			System.out.println("Added Batch File:  " + batchFile.getString("file_name") + ", " + batchFile.getString("uuid"));
			System.out.println(batchFile);
			System.out.println();
      
      JSONObject meta = new JSONObject().put("DCN", "c9269c7b-52fd-43f4-9cba-2215c8678a97");
      meta.put("sequence-id", 1);
      
			JSONObject batchFile2 = capClient.addFileToBatch(newBatch.getInt("id"), testFileLocation2, meta);
			System.out.println("Added Batch File:  " + batchFile2.getString("file_name") + ", " + batchFile2.getString("uuid"));
			System.out.println(batchFile2);
			System.out.println();
			
      // JSONObject submitBatch = capClient.submitBatch(newBatch.getInt("id"));
      // System.out.println("Submitted Batch:  " + submitBatch.getInt("id") + ", " + submitBatch.getString("name") +
      //                       " - " + submitBatch.getString("status"));
      // // System.out.println(submitBatch);
      // System.out.println();
			
      JSONObject testBatch = capClient.readBatch(newBatch.getInt("id"));
			System.out.println("Batch:  " + testBatch.getInt("id") + ", " + 
												  testBatch.getString("name") + " (files = " + testBatch.getInt("file_count") + ") - " +
													testBatch.getString("status"));

      //       JSONArray batches = capClient.showBatches();
      // int numOfBatches = batches.length();
      // System.out.println("Number of Batches = " + numOfBatches);
      // if ( numOfBatches > 0 ) {
      //         System.out.println();
      //   for (int i = 0; i < numOfBatches; i = i + 1) {
      //     JSONObject batch = batches.getJSONObject(i);
      //     System.out.println("Batch:  " + batch.getInt("id") + ", " +
      //                         batch.getString("name") + " (files = " + batch.getInt("file_count") + ") - " +
      //                         batch.getString("status"));
      //     // System.out.println(batch.toString(2));
      //     // System.out.println();
      //   }
      // }
			System.out.println();
			
      // JSONArray documents = capClient.showDocuments();
      // int numOfDocuments = documents.length();
      // System.out.println("Number of Documents = " + numOfDocuments);
      // if ( numOfDocuments > 0 ) {
      //   for (int i = 0; i < numOfDocuments; i = i + 1) {
      //     JSONObject doc = documents.getJSONObject(i);
      //     System.out.println("Template:  " + doc.getInt("id") + ", " + doc.getString("name"));
      //     // System.out.println(doc.toString(2));
      //     // System.out.println();
      //   }
      // }
      // System.out.println();
			
      return;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
}
