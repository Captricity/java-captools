import com.captricity.api.CaptricityClient;
import org.json.*;
import java.util.Timer;
import java.util.TimerTask;

public class TestBatchResultsLoop {
  
	String apiToken = System.getenv("TEST_API_TOKEN");
	CaptricityClient capClient = new CaptricityClient(apiToken);
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
    String testFileLocation = "/Users/davids/Desktop/EZ-return1.pdf";
    String testBatchName = "Java Batch Results Test 1-1";

    JSONObject newBatch = capClient.createBatch(testBatchName);
    System.out.println("Created New Batch:  " + newBatch.getInt("id") + ", " + newBatch.getString("name"));
    System.out.println();

    JSONObject batchFile = capClient.addFileToBatch(newBatch.getInt("id"), testFileLocation);
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


