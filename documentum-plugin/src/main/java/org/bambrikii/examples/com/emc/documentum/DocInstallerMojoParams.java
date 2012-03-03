package org.bambrikii.examples.com.emc.documentum;

import java.util.List;

public class DocInstallerMojoParams {
	private String docbaseName;
	private String userName;
	private String userDomain;
	private String userPassword;
	private String logFileLocation;
	private String propertiesFile;

	private List<DocApp> docApps;

	public String getDocbaseName() {
		return docbaseName;
	}

	public void setDocbaseName(String docbaseName) {
		this.docbaseName = docbaseName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

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
