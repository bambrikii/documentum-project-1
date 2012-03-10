package org.bambrikii.examples.com.emc.documentum.bof;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		logger.debug("Bof install complete");
	}
}
