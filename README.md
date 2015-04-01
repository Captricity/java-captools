# java-captools
####Captricity API Client Library written in Java

Very basic functionality at this point.  Currently, the TestCaptricityClient.java program demonstrates how to create a batch, add a batch file to that batch and list existing unprocessed batches and templates using the CaptricityClient class.

### Instructions for use

This code was built against Java 1.8.0_40 and uses ant 1.9.4 for the build tool.

From the base of the directory, type

    ant jar

A jar file named captricity-1.0.jar will be generated in the base directory.

Copy/move the captricity-1.0.jar file into your classpath.

and then in your java program, such as TestCaptricityClient.java,

    import com.captricity.api.CaptricityClient;

In order to use this code, you will also need a Captricity API Token.

To instantiate a Captricity API client object, use something like this:

    CaptricityClient capClient = new CaptricityClient('6cf43...88b75');

where the parameter is your Captricity API token.

You can then use this client object to make method calls against the Captricity API, such as shown in the example below:

    JSONObject newBatch = capClient.createBatch("name of new batch", true, false);
		
### Summary of available methods

`public JSONArray showBatches() throws Exception {...}`

* Returns a JSONArray of Batches that exist in your account.
* Parameters
  * None

<br/>
`public JSONObject createBatch(String name, Boolean sorting_enabled, Boolean is_sorting_only) throws Exception {...}`

* Returns a JSONObject representing the Batch that was created by the method call.
* Parameters
  * String name - Provide a name for the Batch you are creating
	* Boolean sorting\_enabled \- Set to true to enable sorting for this Batch
	* Boolean is\_sorting\_only \- Set to true if you only want to sort this Batch (as opposed to submit for data extraction)

<br/>
`public JSONObject addFileToBatch(int batchID, String fileName) throws Exception {...}`
	
* Returns a JSONObject representing the Batch File that was just added to the specified Batch by the method call.
* Parameters
  * int batchID \- Batch ID of the Batch to which you want to add file (this can be obtained from the resulting JSONObject after you create a Batch)
	* String fileName \- Full pathname of the file on your local file system

<br/>
`public JSONArray showDocuments() throws Exception {...}`

* Returns a JSONArray of Documents (master templates) that exist in your account.
* Parameters
  * None

### Dependencies

[Apache HttpClient 4.4](http://psg.mtu.edu/pub/apache//httpcomponents/httpclient/binary/httpcomponents-client-4.4-bin.zip)

[org.json](http://central.maven.org/maven2/org/json/json/20140107/json-20140107.jar)

### In the works...
- Additional methods for Captricity API functionality
