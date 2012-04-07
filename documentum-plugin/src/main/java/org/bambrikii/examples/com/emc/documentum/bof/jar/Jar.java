package org.bambrikii.examples.com.emc.documentum.bof.jar;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Jar {
	private String name;
	private String path;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this);
		sb.append("name", getName());
		sb.append("path", getPath());
		return sb.toString();
	}
}
