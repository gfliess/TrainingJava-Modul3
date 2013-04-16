
/** 
 * Copyright 2013 SSI Schaefer PEEM GmbH. All Rights reserved. 
 * <br /> <br />
 * 
 * $Id$
 * <br /> <br />
 *
 */

package at.edu.ati.webserver.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;



/**
 * This is the class header. The first sentence (ending with "."+SPACE) is important,
 * because it is used summary in the package overview pages.<br />
 * <br />
 *
 *
 * @author  jog
 * @version $Revision$
 */

public class SimpleRequestProcessor implements IRequestProcessor {
	
	private Queue<QueueElement> queue = new LinkedBlockingQueue<QueueElement>();
	private String directory;
 
  
  public class QueueElement{
  	public final SocketChannel channel;
  	public final String data;
  	public QueueElement(Channel channel, String data){
  		this.channel = (SocketChannel)channel;
  		this.data = data;
  	}
  	
  }
  
  public void setDirectory(String directory){
	  this.directory = directory;
  }
  
  public void addData(String data, Channel channel) {
    // enqueue data
  	System.out.println("HTTP request string: "+data);
    this.queue.add(new QueueElement(channel, data));
  }
  
  public void run() {
  
    while(!queue.isEmpty()){  	
	    System.out.println("Handle request");	
	    
	    //dequeue data process it and send response
	    QueueElement queueElement = queue.remove();
	  	String data = queueElement.data;
	    SocketChannel channel = queueElement.channel;
	    
	    StringTokenizer tokenizer = new StringTokenizer(data);
	 	  if(tokenizer.countTokens() < 2){
	 	  	return;
	 	  }
	 	  
	    String httpMethod = tokenizer.nextToken();
	 	 	String httpQueryString = tokenizer.nextToken();
	 	 
	  	
	  	StringBuffer responseBuffer = new StringBuffer();
		  responseBuffer.append("<b> This is the HTTP Server Home Page.... </b><BR>");
	    responseBuffer.append("The HTTP Client request is ....<BR>");
	    responseBuffer.append(data + "<BR>");
	    
		  if (httpMethod.equals("GET")) {
			  if (httpQueryString.equals("/")) {
			   // The default home page
			  	sendResponse(channel,200, responseBuffer.toString(), false);
			  } else {
			  //This is interpreted as a file name
				  String fileName = httpQueryString.replaceFirst("/", "");
				  System.out.println("file name: "+fileName);
				  fileName = URLDecoder.decode(directory+"/"+fileName);
				  
				  if (new File(fileName).isFile()){
				  	sendResponse(channel,200, fileName, true);
				  }
				  else {
				  	sendResponse(channel,404, "<b>The Requested resource not found ...." +
				  			"Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>", false);
				  }
			  }
		  }
		  else sendResponse(channel,404, "<b>The Requested resource not found ...." +
		  "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>", false);
    }
   
  }
  
  public void sendResponse (SocketChannel channel, int statusCode, String responseString, boolean isFile) {

  	StringBuffer buf = new StringBuffer();
  	FileInputStream fin = null;
  	String fileName = null;
  	String statusLine = null;
  	String serverdetails = "Server: Java HTTPServer";
  	String contentLengthLine = null;
  	String contentTypeLine = "Content-Type: text/html" + "\r\n";
  	

  	if (statusCode == 200)
  		statusLine = "HTTP/1.1 200 OK" + "\r\n";
  	else
  		statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

  	if (isFile) {
  		fileName = responseString;
	  	try {
	      fin = new FileInputStream(fileName);
        contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";
      	if (!fileName.endsWith(".htm") && !fileName.endsWith(".html")){
  	  		contentTypeLine = "Content-Type: \r\n";
  	  	}
      } catch (FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
      } 
	  	catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
      }
		}
  	else {
	  	contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
  	}
  	
  	buf.append(statusLine);
		buf.append(serverdetails);
		buf.append(contentTypeLine);
		buf.append(contentLengthLine);
		buf.append("Connection: close\r\n");
		buf.append("\r\n");
  	
  	if (isFile){
  		try {
	      sendFile(fileName, fin, channel, buf);
      } catch (Exception e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
      }
  	}
  	else{
  		try {
    		
    		buf.append(responseString);
    		//write back to client
    		channel.write(ByteBuffer.wrap(buf.toString().getBytes()));

  		} catch (IOException ioe) {
         System.err.println("Could not send response");
         ioe.printStackTrace();
      }
  	}
  	
  	
  	

  }
  
  public void sendFile (String fileName, FileInputStream fin, SocketChannel channel, StringBuffer buf) throws Exception {
  	System.out.println("send file");
  	File file = new File(fileName);
  	byte fileContent[] = new byte[(int)file.length()];
     
     /*
      * To read content of the file in byte array, use
      * int read(byte[] byteArray) method of java FileInputStream class.
      *
      */
     fin.read(fileContent);
   
     //create string from byte array
     String strFileContent = new String(fileContent);
     buf.append(strFileContent);
   //write back to client
 		channel.write(ByteBuffer.wrap(buf.toString().getBytes()));
  	
  	fin.close();
 
  }

  	
  
}


//---------------------------- Revision History ----------------------------
//$Log$
//
