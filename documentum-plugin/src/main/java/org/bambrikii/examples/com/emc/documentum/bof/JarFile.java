package org.bambrikii.examples.com.emc.documentum.bof;

public class JarFile {
	private boolean downloadable = true;
	private String file;
	private String jarType;

	public boolean isDownloadable() {
		return downloadable;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getJarType() {
		return jarType;
	}

	public void setJarType(String jarType) {
		this.jarType = jarType;
	}

}
