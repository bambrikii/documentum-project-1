package org.bambrikii.examples.com.emc.documentum.bof;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.documentum.bof.DfBOFPackageException;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

/**
 * @goal install-bof
 * @phase process-sources
 */
public class BofInstallerMojo extends AbstractMojo {

	private static Logger logger = LoggerFactory.getLogger(BofInstallerMojo.class);

	/**
	 * @parameter
	 * @required
	 */
	private List<Bof> bofs;

	public void execute() throws MojoExecutionException, MojoFailureException {
		logger.debug("Beginning bof install");

		logger.debug("Debug 1");
		// com.documentum.bof.ant.BOFPackaging
		IDfSession session = null;// TODO:
		for (Bof bof : bofs) {
			try {
				BofDeployCommand deployer = new BofDeployCommand();
				deployer.setSession(session);
				deployer.installBof(bof);
			} catch (DfBOFPackageException ex) {
				logger.error("Bof installation exception", ex);
			} catch (DfException ex) {
				logger.error("Bof installation exception", ex);
			}
		}
		// installBof(moduleName);

		logger.debug("Bof install complete");
	}

}
