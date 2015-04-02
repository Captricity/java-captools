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
			System.out.println();
			
			if ( numOfBatches > 0 ) {
				for (int i = 0; i < numOfBatches; i = i + 1) {
					JSONObject obj = batches.getJSONObject(i);
					JSONObject batch = capClient.readBatch(obj.getInt("id"));
					
					System.out.println("Batch:  " + batch.getInt("id"));
					System.out.println("Name: " + batch.getString("name"));
					System.out.println("Status: " + batch.getString("status"));
					System.out.println("Is digitized: " + batch.getBoolean("is_digitized"));
					System.out.println("File Count: " + batch.getInt("file_count"));
					if ( ! batch.isNull("related_job_id") ) {
						System.out.println("Related Job ID: " + batch.getInt("related_job_id"));
					}
					// System.out.println(": " + batch.getString(""));
					// System.out.println(": " + batch.getString(""));
					// System.out.println(": " + batch.getString(""));
					// System.out.println(": " + batch.getString(""));
					System.out.println();
					// System.out.println(batch.toString(2));
					System.out.println();
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


