package org.bambrikii.examples.com.emc.documentum.docapp;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
	private DocInstallerMojoParams params;

	public void execute() throws MojoExecutionException, MojoFailureException {
		logger.debug("Beginning docapps install");

		System.out.println("DocbaseName:" + params.getDocbaseName());
		System.out.println("UserDomain: " + params.getUserDomain());
		System.out.println("UserName: " + params.getUserName());
		System.out.println("UserPassword: " + params.getUserPassword());
		System.out.println("LogFileLocation: " + params.getLogFileLocation());
		System.out.println("PropertiesFile: " + params.getPropertiesFile());

		for (DocApp docApp : params.getDocApps()) {
			System.out.println("LogFileName: " + docApp.getLogFileName());
			System.out.println("ApplicationFileName: " + docApp.getApplicationFileName());
			System.out.println("Beginning docapp install");

			try {
				DfAppInstaller dfappinstaller = new DfAppInstaller();
				if (StringUtils.isEmpty(docApp.getApplicationFileName())) {
					dfappinstaller.displayUsage();
				} else {

					if (!(StringUtils.isEmpty(params.getDocbaseName()) || StringUtils.isEmpty(params.getUserName())
							|| StringUtils.isEmpty(params.getUserPassword())
							|| StringUtils.isEmpty(params.getLogFileLocation())
							|| StringUtils.isEmpty(docApp.getApplicationFileName())
							|| StringUtils.isEmpty(docApp.getLogFileName()) || StringUtils.isEmpty(params
							.getPropertiesFile()))) {
						dfappinstaller.connectionInfo(params.getDocbaseName(), params.getUserDomain() == null ? ""
								: params.getUserDomain(), params.getUserName(), params.getUserPassword());
						dfappinstaller.logFile(docApp.getLogFileName(), params.getLogFileLocation());
						dfappinstaller.appFile(docApp.getApplicationFileName());
						dfappinstaller.setPropertiesFile(params.getPropertiesFile());
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
