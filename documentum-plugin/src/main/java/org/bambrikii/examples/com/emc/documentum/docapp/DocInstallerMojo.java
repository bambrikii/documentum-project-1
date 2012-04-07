package org.bambrikii.examples.com.emc.documentum.docapp;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.bambrikii.examples.com.emc.documentum.Credentials;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.documentum.ApplicationInstall.DfAppInstaller;
import com.documentum.fc.common.DfException;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal install-docapp
 * @phase process-sources
 */
public class DocInstallerMojo extends AbstractMojo {

	private static Logger logger = LoggerFactory.getLogger(DocInstallerMojo.class);

	/**
	 * @parameter
	 * @required
	 */
	protected Credentials credentials;

	/**
	 * @parameter
	 * @required
	 */
	private DocInstallerMojoParams docAppsConfig;

	public void execute() throws MojoExecutionException, MojoFailureException {
		logger.debug("Beginning docapps install");

		System.out.println("DocbaseName:" + credentials.getDocbaseName());
		System.out.println("UserDomain: " + credentials.getUserDomain());
		System.out.println("UserName: " + credentials.getUserName());
		System.out.println("UserPassword: " + credentials.getUserPassword());
		System.out.println("LogFileLocation: " + docAppsConfig.getLogFileLocation());
		System.out.println("PropertiesFile: " + docAppsConfig.getPropertiesFile());

		for (DocApp docApp : docAppsConfig.getDocApps()) {
			System.out.println("LogFileName: " + docApp.getLogFileName());
			System.out.println("ApplicationFileName: " + docApp.getApplicationFileName());
			System.out.println("Beginning docapp install");

			try {
				DfAppInstaller dfappinstaller = new DfAppInstaller();
				if (StringUtils.isEmpty(docApp.getApplicationFileName())) {
					dfappinstaller.displayUsage();
				} else {

					if (!(StringUtils.isEmpty(credentials.getDocbaseName())
							|| StringUtils.isEmpty(credentials.getUserName())
							|| StringUtils.isEmpty(credentials.getUserPassword())
							|| StringUtils.isEmpty(docAppsConfig.getLogFileLocation())
							|| StringUtils.isEmpty(docApp.getApplicationFileName())
							|| StringUtils.isEmpty(docApp.getLogFileName()) || StringUtils.isEmpty(docAppsConfig
							.getPropertiesFile()))) {
						dfappinstaller.connectionInfo(credentials.getDocbaseName(),
								credentials.getUserDomain() == null ? "" : credentials.getUserDomain(),
								credentials.getUserName(), credentials.getUserPassword());
						dfappinstaller.logFile(docApp.getLogFileName(), docAppsConfig.getLogFileLocation());
						dfappinstaller.appFile(docApp.getApplicationFileName());
						dfappinstaller.setPropertiesFile(docAppsConfig.getPropertiesFile());
						dfappinstaller.startInstall(false);
					} else {
						dfappinstaller.displayUsage();
					}
				}
			} catch (DfException ex) {
				throw new MojoExecutionException("Exception in docapp install", ex);
			}

			System.out.println("Docapp installation complete");
		}

		logger.debug("Docapps installation complete");
	}
}
