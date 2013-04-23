package at.edu.ati.webserver;

import java.io.File;

public interface IServerConfiguration {

	int getPort();
	
	File getDocumentRoot();
	
	int getThreadPoolSize();
	
}
