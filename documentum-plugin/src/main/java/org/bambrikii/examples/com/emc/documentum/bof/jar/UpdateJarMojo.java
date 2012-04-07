package org.bambrikii.examples.com.emc.documentum.bof.jar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.bambrikii.examples.com.emc.documentum.DfAbstractMojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;

/**
 * @goal update-jar
 */
public class UpdateJarMojo extends DfAbstractMojo {

	private static Logger logger = LoggerFactory.getLogger(UpdateJarMojo.class);

	/**
	 * @parameter
	 * @required
	 */
	private List<Jar> jars;

	public void execute() throws MojoExecutionException, MojoFailureException {
		logger.debug("Starting...");
		final IDfSession session = createSession();

		for (Jar jar : jars) {
			logger.debug("	Updating  : " + jar + " ... ");
			final IDfClientX clientX = new DfClientX();
			final IDfQuery dfQuery = clientX.getQuery();
			IDfCollection coll = null;
			final List<IDfId> ids = new ArrayList<IDfId>();

			String query = "select r_object_id from dmc_jar where object_name='";
			query = query + jar.getName() + "'";
			final StringBuffer dql = new StringBuffer(52);
			dql.append(query);
			dfQuery.setDQL(dql.toString());
			try {
				coll = dfQuery.execute(session, IDfQuery.DF_READ_QUERY);
				while (coll.next()) {
					ids.add(coll.getId("r_object_id"));
				}
				coll.close();
				coll = null;
				for (final Iterator<IDfId> iterator = ids.iterator(); iterator.hasNext();) {
					final IDfId jarId = iterator.next();
					final IDfSysObject dfJar = (IDfSysObject) session.getObject(jarId);
					MDC.put(R_OBJECT_ID, jarId.getId());
					logger.debug("		dmc_jar found : " + jarId.getId());
					dfJar.checkout();
					dfJar.setFile(jar.getPath());
					dfJar.checkin(false, "");
					MDC.remove(R_OBJECT_ID);
				}
				logger.debug("	Update complete.");
			} catch (DfException ex) {
				throw new MojoExecutionException("Failed.", ex);
			} finally {
				if (coll != null) {
					try {
						coll.close();
					} catch (final DfException e) {
						coll = null;
					}
				}
			}
		}
		logger.debug("Complete.");
	}
}
