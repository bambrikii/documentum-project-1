package org.bambrikii.examples.com.emc.documentum.docapp;

import java.util.List;

public class DocInstallerMojoParams {
	private String logFileLocation;
	private String propertiesFile;

	private List<DocApp> docApps;

	public String getLogFileLocation() {
		return logFileLocation;
	}

	public void setLogFileLocation(String logFileLocation) {
		this.logFileLocation = logFileLocation;
	}

	public String getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	public List<DocApp> getDocApps() {
		return docApps;
	}

	public void setDocApps(List<DocApp> docApps) {
		this.docApps = docApps;
	}
}
