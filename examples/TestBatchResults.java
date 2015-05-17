import com.captricity.api.CaptricityClient;
import org.json.*;

public class TestBatchResults {
	
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("TEST_API_TOKEN");
			
			CaptricityClient capClient = new CaptricityClient(apiToken);
			
      JSONArray batches = capClient.showBatches();
      int numOfBatches = batches.length();
      System.out.println("Number of Batches = " + numOfBatches);
      System.out.println();
      
      // JSONObject testBatch = capClient.readBatch(<enter batch number here>);
      // String results = capClient.getBatchResults(testBatch.getInt("id"), true);
      // System.out.println(results);
      
      if ( batches.length() > 0 ) {
        for (int i = 0; i < batches.length(); i = i + 1) {
          JSONObject batch = batches.getJSONObject(i);
          String results = capClient.getBatchResults(batch.getInt("id"), true);
          System.out.println(results);
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


