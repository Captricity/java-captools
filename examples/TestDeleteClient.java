import com.captricity.api.CaptricityClient;
import org.json.*;

public class TestDeleteClient {
	
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("TEST_API_TOKEN");
			String testBatchName = "Short Lived Batch";
			
			CaptricityClient capClient = new CaptricityClient(apiToken);
			
			JSONObject newBatch = capClient.createBatch(testBatchName, true, false);
			System.out.println("Create New Batch:  " + newBatch.getInt("id") + ", " + newBatch.getString("name"));
			// System.out.println(newBatch);
			System.out.println();
						
      JSONArray batchesBefore = capClient.showBatches();
			int numOfBatches = batchesBefore.length();
			System.out.println("Number of Batches = " + numOfBatches);
			if ( numOfBatches > 0 ) {
				for (int i = 0; i < numOfBatches; i = i + 1) {
					JSONObject batch = batchesBefore.getJSONObject(i);
					System.out.println("Batch:  " + batch.getInt("id") + ", " + 
														  batch.getString("name") + " (files = " + batch.getInt("file_count") + ") - " +
															batch.getString("status"));
					// System.out.println(batch.toString(2));
					// System.out.println();
				}
			}
			System.out.println();
			
			// call to delete new batch
			// JSONObject status = capClient.deleteBatch(newBatch.getInt("id"));
			// System.out.println(status.toString(2));
			// System.out.println();
			
      JSONArray batchesAfter = capClient.showBatches();
			int numOfBatches = batchesAfter.length();
			System.out.println("Number of Batches = " + numOfBatches);
			if ( numOfBatches > 0 ) {
				for (int i = 0; i < numOfBatches; i = i + 1) {
					JSONObject batch = batchesAfter.getJSONObject(i);
					System.out.println("Batch:  " + batch.getInt("id") + ", " + 
														  batch.getString("name") + " (files = " + batch.getInt("file_count") + ") - " +
															batch.getString("status"));
					// System.out.println(batch.toString(2));
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


