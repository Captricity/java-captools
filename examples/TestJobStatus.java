import com.captricity.api.CaptricityClient;
import org.json.*;

public class TestJobStatus {
	
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("TEST_API_TOKEN");
			
			CaptricityClient capClient = new CaptricityClient(apiToken);
			
      JSONArray batches = capClient.showBatches();
			int numOfBatches = batches.length();
			System.out.println("Number of Batches = " + numOfBatches);
			if ( numOfBatches > 0 ) {
				for (int i = 0; i < numOfBatches; i = i + 1) {
					JSONObject batch = batches.getJSONObject(i);
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


