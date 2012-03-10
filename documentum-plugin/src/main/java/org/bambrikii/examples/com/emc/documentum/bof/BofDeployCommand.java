package org.bambrikii.examples.com.emc.documentum.bof;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.documentum.bof.DfBOFPackageException;
import com.documentum.bof.DfModuleFolder;
import com.documentum.bof.PrimaryClass;
import com.documentum.bof.Utils;
import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfService;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;

public class BofDeployCommand {

	private static Logger logger = LoggerFactory.getLogger(BofDeployCommand.class);

	private IDfSession session;

	public void setSession(IDfSession session) {
		this.session = session;
	}

	private void initEnvCheck(IDfSession session) throws DfException {
		IDfId idfid = Utils.getObjIdFromDbPath(session, "dm_folder", "Modules", "/System", false);
		if (idfid == null || idfid.isNull()) {
			Utils.createFolder(session, "dm_folder", "/System", "Modules");
			Utils.createFolder(session, "dm_folder", "/System/Modules", "TBO");
			Utils.createFolder(session, "dm_folder", "Modules", "SBO");
		}
	}

	private void updateVMVersion(String s, IDfId idfid) throws DfException {
		String s1;
		IDfCollection idfcollection;
		s1 = (new StringBuilder()).append("select r_object_id from dmc_jar where min_vm_version <> '").append(s)
				.append("' and FOLDER (ID('").append(idfid.getId()).append("'))").toString();
		idfcollection = null;
		DfQuery dfquery = new DfQuery();
		dfquery.setDQL(s1);
		IDfPersistentObject idfpersistentobject;
		for (idfcollection = dfquery.execute(session, 0); idfcollection.next(); idfpersistentobject.save()) {
			IDfId idfid1 = idfcollection.getId("r_object_id");
			idfpersistentobject = session.getObject(idfid1);
			idfpersistentobject.setString("min_vm_version", s);
		}

		if (idfcollection != null)
			idfcollection.close();
		if (idfcollection != null)
			idfcollection.close();
	}

	// private boolean addToDocApp(String moduleName, String docAppName,
	// IDfPersistentObject idfpersistentobject, IDfApplication idfapplication) throws DfException {
	// boolean flag;
	// if (idfapplication == null) {
	// idfapplication = DfAppUtils.newApplication(session, docAppName);
	// if (idfapplication == null) {
	// String as[] = { docAppName };
	// Utils.handleError("BOF_PACK_NEW_DOCAPP_FAILED", as);
	// }
	// }
	// if (idfapplication.isObjectInApplication(idfpersistentobject.getObjectId()) && distModuleOption == null
	// && distJavaLibOption == null)
	// return false;
	// flag = false;
	// if (idfapplication.isCheckedOut()) {
	// if (!idfapplication.getLockOwner().equals(session.getLoginUserName())) {
	// String as1[] = { docAppName };
	// Utils.handleError("BOF_PACK_DOCAPP_CO", as1);
	// }
	// } else {
	// idfapplication.checkout();
	// flag = true;
	// }
	// IDfAppRef idfappref = addModuleToDocApp(idfpersistentobject, idfapplication, flag);
	// if (idfappref == null) {
	// if (flag) {
	// idfapplication.cancelCheckout();
	// flag = false;
	// }
	// String as2[] = { moduleName, docAppName };
	// Utils.handleError("BOF_PACK_INSERT_MODULE_FAILED", as2);
	// } else {
	// int i = toCompleteUpgradeOption(distModuleOption, distJavaLibOption);
	// idfapplication.setUpgradeOptionById(idfpersistentobject.getObjectId(), i);
	// idfapplication.checkin(false, null);
	// flag = false;
	// }
	// if (flag)
	// idfapplication.cancelCheckout();
	// if (flag)
	// idfapplication.cancelCheckout();
	// if (flag)
	// idfapplication.cancelCheckout();
	// return true;
	// }

	public void installBof(Bof bof /*
									 * ,String moduleName, String primaryClass, String moduleType, String minDFCVersion,
									 * String contactInfo, String description, String minVMVersion, String implTech,
									 * Boolean generateWebService, String docAppName
									 */) throws DfBOFPackageException, DfException {
		String primaryClass = bof.getPrimaryClass();
		String moduleName = bof.getModuleName();
		String moduleType = bof.getModuleType();
		String minVMVersion = bof.getMinVMVersion();
		String description = bof.getDescription();
		String contactInfo = bof.getContactInfo();
		String minDFCVersion = bof.getMinDFCVersion();
		String implTech = bof.getImplTech();

		PrimaryClass primaryClassValue = null; // TODO: StringUtils.isEmpty(primaryClass) ? null : new
		// NamedDataType();
		//
		initEnvCheck(session);
		if (moduleName == null)
			throw new DfBOFPackageException("BOF_PACK_NAME_NOT_SPECIFIED");
		IDfSysObject idfsysobject = (IDfSysObject) Utils.getObjFromDbPath(session, "dmc_module", moduleName,
				"/System/Modules", true);
		if (primaryClass == null)
			if (idfsysobject != null)
				primaryClassValue = new PrimaryClass(implTech, idfsysobject.getString("primary_class"));
			else
				throw new DfBOFPackageException("BOF_PACK_PRIMARY_CLASS_NOT_SPECIFIED");
		DfModuleFolder dfmodulefolder = getModuleFolder(idfsysobject != null ? idfsysobject.getObjectId() : null,
				primaryClass);
		if (idfsysobject == null)
			dfmodulefolder.setName(moduleName);
		if (moduleType != null) {
			List list = dfmodulefolder.getInterfaceNames();
			if (moduleType.equals("SBO") && !list.contains(IDfService.class.getName())) {
				String s = dfmodulefolder.getPrimaryClass() != null ? dfmodulefolder.getPrimaryClass().asString() : "";
				throw new DfBOFPackageException("BOF_PACK_MODULE_INTERFACE_MISSING", new String[] { s });
			}
			if (moduleType.equals("TBO")) {
				IDfType idftype = session.getType(moduleName);
				if (idftype == null)
					throw new DfBOFPackageException("BOF_PACK_INVALID_TBO_NAME", new String[] { moduleName });
			}
			dfmodulefolder.setType(moduleType);
		}
		if (minDFCVersion != null)
			dfmodulefolder.setMinDFCVersion(minDFCVersion);
		if (contactInfo != null)
			dfmodulefolder.setContactInfo(contactInfo);
		if (description != null)
			dfmodulefolder.setDesc(description);
		boolean flag = false;
		String s1 = null;
		if (idfsysobject == null) {
			flag = true;
			String s2 = dfmodulefolder.getType();
			if ("Aspect".equals(s2))
				idfsysobject = (IDfSysObject) session.newObject("dmc_aspect_type");
			else
				idfsysobject = (IDfSysObject) session.newObject("dmc_module");
			s1 = Utils.getContainerDir(s2);
			IDfPersistentObject idfpersistentobject = session.getObjectByPath(s1);
			if (idfpersistentobject == null)
				Utils.createFolder(session, "dm_folder", "/System/Modules", dfmodulefolder.getType());
			idfsysobject.setACLName("BOF_acl");
			idfsysobject.setACLDomain("dm_dbo");
		}
		dfmodulefolder.save(idfsysobject);
		DfClientX dfclientx = new DfClientX();
		// if (removeFromPackage != null)
		// removeFromPackage.exec(session, idfsysobject.getObjectId());
		if (minVMVersion != null)
			updateVMVersion(minVMVersion, idfsysobject.getObjectId());
		// if (addToPackage != null)
		// addToPackage.exec(dfclientx, session, idfsysobject.getObjectId());
		if (s1 != null) {
			idfsysobject.link(s1);
			idfsysobject.save();
		}
		// if (generateWebService && needGenerateWebService()) {
		// webServiceCodeGen(flag, idfsysobject);
		// if (isNewWebService() || isWebServiceConfigModified())
		// updateWSConfigXML(idfsysobject);
		// }
		// if (targetWARs != null)
		// WebServicePackager.exec(session, moduleName, targetWARs);
		// if (!generateWebService && !isNewWebService()) {
		// IDfId idfid = Utils.getObjIdFromDbPath(session, "dm_folder", "Web Service", idfsysobject.getObjectId()
		// .getId(), false);
		// IDfPersistentObject idfpersistentobject1 = session.getObject(idfid);
		// RemoveUtils.removeObjectFromId(session, idfid, (IDfSysObject) idfpersistentobject1);
		// }
		// if (docAppName != null && docAppName.length() > 0) {
		// IDfApplication idfapplication = DfAppUtils.findFromApplicationName(session, docAppName);
		// addToDocApp(idfsysobject, idfapplication);
		// }
	}

	private DfModuleFolder getModuleFolder(IDfId idfid, String primaryClass) throws DfException {
		DfModuleFolder dfmodulefolder = new DfModuleFolder();
		if (primaryClass == null)
			return dfmodulefolder;
		ArrayList arraylist = new ArrayList();
		IDfPersistentObject idfpersistentobject = null;
		if (idfid != null && !idfid.isNull()) {
			arraylist.addAll(loadCoreJarsFromDocbase(idfid.getId()));
			idfpersistentobject = session.getObject(idfid);
		}
		// if (addToPackage != null) {
		// List list = addToPackage.getCoreJarList(2);
		// list.addAll(addToPackage.getCoreJarList(1));
		// list.addAll(addToPackage.getJavaLibsJarList());
		// list.addAll(addToPackage.getDepModuleJarList());
		// int i = list.size();
		// for (int k = 0; k < i; k++) {
		// FileEntry fileentry = (FileEntry) list.get(k);
		// String s = fileentry.getFileName();
		// int i1 = indexInFilePathList(arraylist, s);
		// if (i1 >= 0)
		// arraylist.remove(i1);
		// arraylist.add(fileentry.filePath);
		// }
		//
		// }
		ArrayList arraylist1 = new ArrayList(5);
		if (idfpersistentobject != null) {
			int j = idfpersistentobject.getValueCount("a_interfaces");
			for (int l = 0; l < j; l++)
				arraylist1.add(idfpersistentobject.getRepeatingString("a_interfaces", l));

		}
		// dfmodulefolder.loadData(primaryClassValue, arraylist, arraylist1);
		return dfmodulefolder;
	}

	private List loadCoreJarsFromDocbase(String s) throws DfException {
		List list;
		List arraylist;
		IDfQuery idfquery;
		IDfCollection idfcollection;
		list = null;
		// if (removeFromPackage != null)
		// list = removeFromPackage.getObjs();
		arraylist = new ArrayList();
		StringBuilder stringbuffer = new StringBuilder();
		stringbuffer.append("select r_object_id, object_name from ");
		stringbuffer.append("dmc_jar");
		stringbuffer.append(" where FOLDER(ID('");
		stringbuffer.append(s);
		stringbuffer.append("'), DESCEND)");
		idfquery = null;// cx.getQuery();
		idfquery.setDQL(stringbuffer.toString());
		idfcollection = null;
		idfcollection = idfquery.execute(session, 0);
		Object obj = null;
		Object obj1 = null;
		Object obj2 = null;
		do {
			if (idfcollection == null || !idfcollection.next())
				break;
			String s1 = idfcollection.getString("r_object_id");
			String s2 = idfcollection.getString("object_name");
			if (list == null || !list.contains(s1) && !list.contains(s2)) {
				// IDfSysObject idfsysobject = (IDfSysObject) session.getObject(cx.getId(s1));
				// arraylist.add(idfsysobject.getFile(null));
			}
		} while (true);
		if (idfcollection != null)
			try {
				idfcollection.close();
			} catch (DfException dfexception) {
				logger.error("BOF_PACK_COLLECTION_CLOSE_FAILED", dfexception);
			}
		// logger.error("BOF_PACK_LOAD_DOCBASE_IMPL_JAR_FAILED", dfexception1);
		if (idfcollection != null)
			try {
				idfcollection.close();
			} catch (DfException dfexception2) {
				logger.error("BOF_PACK_COLLECTION_CLOSE_FAILED", dfexception2);
			}
		if (idfcollection != null)
			try {
				idfcollection.close();
			} catch (DfException dfexception3) {
				logger.error("BOF_PACK_COLLECTION_CLOSE_FAILED", dfexception3);
			}
		return arraylist;
	}
}
