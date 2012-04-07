package org.bambrikii.examples.com.emc.documentum.bof.jar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
 * @goal remove-duplicate-jars
 */
public class RemoveDuplicateJarsMojo extends DfAbstractMojo {

	private static Logger logger = LoggerFactory.getLogger(RemoveDuplicateJarsMojo.class);

	/**
	 * @parameter
	 * @required
	 */
	private List<Jar> jars;

	public void execute() throws MojoExecutionException, MojoFailureException {
		logger.debug("Starting...");
		IDfSession session = createSession();
		for (Jar jar : jars) {
			logger.debug("	Looking for duplicates for : " + jar + " ... ");
			final IDfClientX clientX = new DfClientX();
			final IDfQuery dfQuery = clientX.getQuery();
			IDfCollection coll = null;
			final List<IDfId> ids = new ArrayList<IDfId>();
			final Set<String> folderIDs = new HashSet<String>();

			String query = "select r_object_id from dmc_jar where object_name='";
			query = query + jar.getName() + "' order by r_modify_date desc";
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
				boolean first = true;
				IDfSysObject firstJar = null;
				for (final Iterator<IDfId> iterator = ids.iterator(); iterator.hasNext();) {
					final IDfId jarId = iterator.next();
					final IDfSysObject dfJar = (IDfSysObject) session.getObject(jarId);
					MDC.put(R_OBJECT_ID, jarId.getId());
					logger.debug("		dmc_jar found : " + jarId.getId());
					if (first) {
						first = false;
						firstJar = dfJar;
						for (int i = 0; i < dfJar.getFolderIdCount(); i++) {
							folderIDs.add(dfJar.getFolderId(i).getId());
						}
					} else {
						logger.debug("		Removing duplicates...");
						for (int i = 0; i < dfJar.getFolderIdCount(); i++) {
							final String folderId = dfJar.getFolderId(i).getId();
							if (!folderIDs.contains(folderId)) {
								logger.debug("		Removing from " + folderId);
								firstJar.link(folderId);
								firstJar.save();
							}
							folderIDs.add(folderId);
						}
						dfJar.destroyAllVersions();
					}
					MDC.remove(R_OBJECT_ID);
				}
				logger.debug("	Complete.");
			} catch (DfException e) {
				throw new MojoExecutionException("Failure", e);
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
