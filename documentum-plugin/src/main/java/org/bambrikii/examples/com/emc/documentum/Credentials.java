package org.bambrikii.examples.com.emc.documentum;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Credentials {
	private String docbaseName;
	private String userName;
	private String userPassword;
	private String userDomain;

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

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}

	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this);
		sb.append("docbaseName", getDocbaseName());
		sb.append("userName", getUserName());
		sb.append("userPassword", getUserPassword());
		sb.append("userDomain", getUserDomain());
		return sb.toString();
	}
}
