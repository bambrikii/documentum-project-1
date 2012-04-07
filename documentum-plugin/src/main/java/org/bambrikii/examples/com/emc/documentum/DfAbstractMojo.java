package org.bambrikii.examples.com.emc.documentum;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

public abstract class DfAbstractMojo extends AbstractMojo {

	protected static final String R_OBJECT_ID = "rObjectId";

	private static Logger logger = Logger.getLogger(DfAbstractMojo.class);

	/**
	 * @parameter
	 * @required
	 */
	protected Credentials credentials;

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public IDfSession createSession() throws MojoExecutionException {
		try {
			logger.debug("Acquiring dm session... " + credentials);
			IDfClient client = new DfClientX().getLocalClient();
			IDfLoginInfo loginInfo = new DfClientX().getLoginInfo();
			loginInfo.setUser(credentials.getUserName());
			loginInfo.setPassword(credentials.getUserPassword());
			return client.newSession(credentials.getDocbaseName(), loginInfo);
		} catch (DfException e1) {
			throw new MojoExecutionException("Failed to create Df session", e1);
		}
	}

}
