# java-captools
####Captricity API Client Library written in Java

Very basic functionality at this point.  Currently, the TestCaptricityClient.java program demonstrates how to create a batch, add a batch file to that batch and list existing unprocessed batches and templates using the CaptricityClient class.

Include com/captricity/api/CaptricityClient.class in your classpath,

and then in your java program (such as TestCaptricityClient.java),

import com.captricity.api.CaptricityClient;

This code was built against Java 1.8.0_40.

In order to use this code, you will also need a Captricity API Token.

### Dependencies

[Apache HttpClient 4.4](http://psg.mtu.edu/pub/apache//httpcomponents/httpclient/binary/httpcomponents-client-4.4-bin.zip)

[org.json](http://central.maven.org/maven2/org/json/json/20140107/json-20140107.jar)

### In the works...
- Ant build file
- jar file for com.captricity.api
- Additional methods for Captricity API functionality
