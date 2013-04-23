package at.edu.ati.webserver.configuration;

import java.io.File;

import at.edu.ati.webserver.IServerConfiguration;

public class PropertyConfiguration implements IServerConfiguration {

	private PropertyConfiguration() {

	}

	public static IServerConfiguration instance(File file)
			throws IllegalConfigurationException {

		throw new IllegalConfigurationException("not implemented yet");

	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public File getDocumentRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getThreadPoolSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
