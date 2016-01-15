# java-captools 1.2
#### Captricity API client library written in Java

Currently, the Captricity API client library provides methods to accomplish the following:
* create a Batch
* read (get) a Batch
* delete a Batch
* add Batch Files to the Batch
* submit the Batch for processing
* get results for a specific Batch
* show a list of Batches in the account
* get Job status (percent completed)
* get Job results (for Jobs that are 100% complete)
* show a list of master Templates in the account
* get a list of all the Fields defined on a Template

I have also provided a number of example programs that demonstrate how the API client library works in a controlled environment.  You can find these programs in the examples folder.

### Instructions for use

This code was built against **Java 1.8.0\_40** and uses **ant 1.9.4** for the build tool.

From the base of the directory, type

```
ant jar
```

A jar file named **captricity-1.2.jar** will be generated in the base directory.

Copy/move the captricity-1.2.jar file into your classpath.

and then in your java program, like is done in the example code-- TestCaptricityClient.java,

```java
import com.captricity.api.CaptricityClient;
```

In order to use this code, you will also need a Captricity API Token.

To instantiate a Captricity API client object, use something like this:

```java
CaptricityClient capClient = new CaptricityClient('6cf43...88b75');
```

where the parameter is your Captricity API token.

You can then use this client object to make method calls against the Captricity API, as is shown in the example below:

```java
JSONObject newBatch = capClient.createBatch("name of new batch", true, false);
```

### Constructor methods

```java
public CaptricityClient(String token)
public CaptricityClient(String token, String url) 
```
* Create a Captricity API client object.
* You will need a Captricity API token.
* Unless you have been directed otherwise, use the simpler constructor, and the endpoint URL will be set for you.

### Summary of available methods

```java
public JSONArray showBatches() throws Exception {...}
```
* Returns a `JSONArray` of the Batches that exist in your account.
* Parameters:
  - *None*

```java
public JSONObject createBatch(String name) throws Exception {...}
public JSONObject createBatch(String name, ArrayList<Integer> docIds) throws Exception {...}
public JSONObject createBatch(String name, Boolean sortingEnabled, Boolean isSortingOnly) throws Exception {...}
public JSONObject createBatch(String name, Boolean sortingEnabled, Boolean isSortingOnly, ArrayList<Integer> docIds) throws Exception {...}
```
* Creates a Batch in accordance with the given name and properties.
* Returns a `JSONObject` representing the Batch that was created by the method call.
* Parameters:
  - `String name` - Provide a name for the Batch you are creating
  - `Boolean sortingEnabled` \- Set to true to enable sorting for this Batch.  The default value is `true` if not specified.
  - `Boolean isSortingOnly` \- Set to true if you only want to sort this Batch (as opposed to submit for data extraction).  The default value is `false` if not specified.
  - `ArrayList<Integer> docIds` \- Add an `ArrayList` of integers representing the Template ID's of the templates that you want to use for sorting.  Your account must have sorting enabled for this to work. If sorting is enabled on your account and you do not specify a list of Template ID's, your Batch will be sorted against all active templates in your account.

```java
public JSONObject readBatch(int batchID) throws Exception {...}
```
* Gets the Batch specified by the batchID parameter.
* Returns a `JSONObject` representing the Batch.
* Parameters:
  - `int batchID` \- Batch ID of the Batch you want to get

```java
public JSONObject deleteBatch(int batchID) throws Exception {...}
```
* Deletes the Batch specified by the batchID parameter.
* Returns a `JSONObject` representing the status of the delete request.
* Parameters:
  - `int batchID` \- Batch ID of the Batch you want to delete

```java
public JSONObject addFileToBatch(int batchID, String fileName) throws Exception {...}
public JSONObject addFileToBatch(int batchID, String fileName, JSONObject metadata) throws Exception {...}
```
* Adds the file found at the given pathname to the Batch with the specified Batch ID.
* Returns a `JSONObject` representing the Batch File that was just added to the specified Batch by the method call.
* Parameters:
  - `int batchID` \- Batch ID of the Batch to which you want to add file (obtained from the resulting `JSONObject` after you create a Batch)
  - `String fileName` \- Full pathname of the file on your local file system
  - `JSONObject metadata` \- A `JSONObject` containing key:value pairs of your desired metadata for this Batch File. Defaults to empty `JSONObject` if not specified.

```java
public JSONObject submitBatch(int batchID) throws Exception {...}
```
* Submits the Batch specified by the batchID parameter for processing.
* Returns a `JSONObject` representing the Batch that was submitted for processing.
* Parameters:
  - `int batchID` \- Batch ID of the Batch you want to submit for processing

```java
public String getBatchResults(int batchID) throws Exception {...}
public String getBatchResults(int batchID, Boolean verboseResults) throws Exception {...}
```
* Gets the results for the Batch specified by the batchID parameter.
* Returns a `String` representing the CSV output of the Batch results, or indicates current status otherwise.
* Parameters:
  - `int batchID` \- Batch ID of the Batch for which you want to obtain results.
  - `Boolean verboseResults` \- Set to true to enable verbose results output.  The default value is `false` if not specified.

```java
public int getJobStatus(int jobID) throws Exception {...}
```
* Gets the percent completed value for the Job specified by the jobID parameter.
* Returns an `int` representing the percent completed.
* Parameters:
  - `int jobID` \- Job ID of the Job in question

```java
public String getJobResults(int jobID) throws Exception {...}
```
* Gets the results of the Job specified by the jobID parameter.
* Returns a `String` representing the CSV output of the Job results, or indicates that the Job is not 100% complete.
* Parameters:
  - `int jobID` \- Job ID of the Job in question

```java
public JSONArray showTemplates() throws Exception {...}
public JSONArray showDocuments() throws Exception {...}
```
* Returns a `JSONArray` of the master templates that exist in your account.  `showDocuments()` method just points to `showTemplates()` and is kept to maintain backwards compatibility.
* Parameters:
  - *None*

```java
public JSONObject readTemplate(int templateId) throws Exception {...}
```
* Gets the Template specified by the templateId parameter.
* Returns a `JSONObject` representing the Template.
* Parameters:
  - `int templateId` \- Template ID of the Template you want to get.

```java
public String listTemplateFields(int templateId) throws Exception {...}
public String listTemplateFields(int templateId, Boolean includeChoices) throws Exception {...} 
public String listTemplateFields(int templateId, Boolean includeChoices, Boolean showAllVersions) throws Exception {...}
```
* Gets the list of Fields associated with the Template specified by the templateId parameter.
* Returns a `String` representing the list of fields associated with the Template.  Note that the fields will be ordered alphabetically by name.
* Parameters:
  - `int templateId` \- Template ID of the Template for which you want to get the Field list.
  - `Boolean includeChoices` \- Set to true to show enumeration of choices of multiple choice fields.  The default value is `false` if not specified.
  - `Boolean showAllVersions` \- Set to true to show all version pages of a template.  The default value is `false` if not specified.


### Dependencies

[Apache HttpClient 4.4](http://psg.mtu.edu/pub/apache//httpcomponents/httpclient/binary/httpcomponents-client-4.4-bin.zip)

[org.json](http://central.maven.org/maven2/org/json/json/20140107/json-20140107.jar)
