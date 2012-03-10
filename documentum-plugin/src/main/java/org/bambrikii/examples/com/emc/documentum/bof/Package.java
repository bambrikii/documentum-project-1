package org.bambrikii.examples.com.emc.documentum.bof;

import java.util.List;

public class Package {

	private String versionPolicy;
	private List<JarFile> jarFiles;

	public String getVersionPolicy() {
		return versionPolicy;
	}

	public void setVersionPolicy(String versionPolicy) {
		this.versionPolicy = versionPolicy;
	}

	public List<JarFile> getJarFiles() {
		return jarFiles;
	}

	public void setJarFiles(List<JarFile> jarFiles) {
		this.jarFiles = jarFiles;
	}
}
