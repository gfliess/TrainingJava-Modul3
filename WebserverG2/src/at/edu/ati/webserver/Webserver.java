package at.edu.ati.webserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import at.edu.ati.webserver.processor.IRequestProcessor;
import at.edu.ati.webserver.processor.SimpleRequestProcessor;

/**
 * <tt>Main<tt/> class.
 *
 * @author <a href="mailto:a.leitner@ssi-schaefer-peem.com">Andreas Leitner</a>
 * @author <a href="mailto:j.gruber@ssi-schaefer-peem.com">Johannes Gruber</a>
 * @author <a href="mailto:p.clement@ssi-schaefer-peem.com">Patrice Clement</a>
 */
public class Webserver {

  private static int port;
  private static String htdocs;
  
  private static final String PROP_PORT = "port";
  private static final String PROP_HTDOCS = "htdocs";
  
  
  /**
   * Main method.
   */
  public static void main(String[] args) {
    try {
      readConfig();
      IRequestProcessor processor = new SimpleRequestProcessor();
      processor.setDirectory(htdocs);
      Server server = new Server(port, htdocs, processor);
      while (true) {
        server.run();  // Also using the Executor-framework?
      }
    } catch (IOException ioe) {
      System.err.println("Could not read the properties file");
      ioe.printStackTrace();
    }
  }

  private static void readConfig() throws IOException {
    Properties properties = new Properties();
    BufferedInputStream stream = new BufferedInputStream(new FileInputStream("webserver.cfg"));
    properties.load(stream);
    stream.close();
    port = Integer.parseInt(properties.getProperty(PROP_PORT, String.valueOf(8000)));
    htdocs = properties.getProperty(PROP_HTDOCS, ".");
  }
  
}