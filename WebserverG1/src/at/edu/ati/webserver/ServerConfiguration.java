package at.edu.ati.webserver;

import java.io.File;

public interface ServerConfiguration {

	int getPort();
	
	File documentRoot();
}
