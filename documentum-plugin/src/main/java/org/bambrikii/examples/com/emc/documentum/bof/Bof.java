package org.bambrikii.examples.com.emc.documentum.bof;

import java.util.List;

public class Bof {

	private String primaryClass;
	private String contactInfo;
	private String description;
	private String minVMVersion;
	private String moduleName;
	private String moduleType;
	private List<Package> packages;
	private String minDFCVersion;
	private String implTech;

	public String getPrimaryClass() {
		return primaryClass;
	}

	public void setPrimaryClass(String primaryClass) {
		this.primaryClass = primaryClass;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMinVMVersion() {
		return minVMVersion;
	}

	public void setMinVMVersion(String minVMVersion) {
		this.minVMVersion = minVMVersion;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public List<Package> getPackages() {
		return packages;
	}

	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}

	public String getMinDFCVersion() {
		return minDFCVersion;
	}

	public void setMinDFCVersion(String minDFCVersion) {
		this.minDFCVersion = minDFCVersion;
	}

	public String getImplTech() {
		return implTech;
	}

	public void setImplTech(String implTech) {
		this.implTech = implTech;
	}

}
