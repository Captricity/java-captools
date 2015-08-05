import com.captricity.api.CaptricityStagingClient;
import org.json.*;
import java.util.Timer;
import java.util.TimerTask;

public class TestBatchResultsLoop {
  
	String apiToken = System.getenv("TEST_API_TOKEN");
	CaptricityStagingClient capClient = new CaptricityStagingClient(apiToken);
  Timer timer;
  
  public TestBatchResultsLoop() {}
  
  public TestBatchResultsLoop(int batchID) {
    timer = new Timer();
    timer.schedule(new StatusChecker(batchID), 0, 10*1000);
  }
  
  class StatusChecker extends TimerTask {
    int loop = 30;
    int batchID;
    
    public StatusChecker(int batchID) {
      this.batchID = batchID;
    }
    
    @Override
    public void run() {
      if ( loop > 0 ) {
        try {
          System.out.println(capClient.getBatchResults(batchID, true));
        } catch (Throwable t) {
          t.printStackTrace();
        }
        loop--;
      } else {
        System.out.println("All done.");
        timer.cancel();
      }
    }
  }
  
  public void run() throws Exception {
    String testFileLocation = "/Users/davids/Documents/CLEARDTOP/API_demo/EZ-return1.pdf";
    String testBatchName = "Java Batch Results Test 8-4";

    JSONObject newBatch = capClient.createBatch(testBatchName);
    System.out.println("Created New Batch:  " + newBatch.getInt("id") + ", " + newBatch.getString("name"));
    System.out.println();
    
    JSONObject meta = new JSONObject().put("DCN", "c3469c7b-52fd-43f4-9cba-2215c8678a97");
    meta.put("sequence-id", 1);
    
		JSONObject batchFile = capClient.addFileToBatch(newBatch.getInt("id"), testFileLocation, meta);
    System.out.println("Added Batch File:  " + batchFile.getString("file_name") + ", " + batchFile.getString("uuid"));
    System.out.println();

    JSONObject submitBatch = capClient.submitBatch(newBatch.getInt("id"));
    System.out.println("Submitted Batch:  " + submitBatch.getInt("id") + ", " + submitBatch.getString("name") +
                          " - " + submitBatch.getString("status"));
    System.out.println();
    
    new TestBatchResultsLoop(newBatch.getInt("id"));
    System.out.println("Start checking Batch results...\n");
  }
  
  public static void main(String[] args) {
    try {
			
      TestBatchResultsLoop looper = new TestBatchResultsLoop();
      looper.run();
			
      return;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }
}


