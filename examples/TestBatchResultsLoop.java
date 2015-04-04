import com.captricity.api.CaptricityClient;
import org.json.*;
import java.util.Timer;
import java.util.TimerTask;

public class TestBatchResultsLoop {
	
  Timer timer;
  
  public TestBatchResultsLoop() {
    timer = new Timer();
    timer.schedule(new StatusChecker(), 0, 1*1000);
  }
  
  class StatusChecker extends TimerTask {
    int loop = 10;
    
    public void run() {
      if ( loop > 0 ) {
        System.out.println("Checking status...");
        loop--;
      } else {
        System.out.println("All done.");
        timer.cancel();
      }
    }
  }
  
  public static void main(String[] args) {
    try {
			String apiToken = System.getenv("TEST_API_TOKEN");
			
			CaptricityClient capClient = new CaptricityClient(apiToken);
			
      //       JSONArray batches = capClient.showBatches();
      //
      // if ( batches.length() > 0 ) {
      //   for (int i = 0; i < batches.length(); i = i + 1) {
      //     JSONObject batch = batches.getJSONObject(i);
      //     String results = capClient.getBatchResults(batch.getInt("id"));
      //     System.out.println(results);
      //   }
      // }
      // System.out.println();
      
      new TestBatchResultsLoop();
      System.out.println("Start checking status...");
			
      return;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
}


