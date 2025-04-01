package chris.dicom.bean;

public class InstanceBean {
	
	
	String sopClassUid;
	String sopInstanceUid;		
	String modality;
	String patientName;		
	String patientId;	
	String seriesName;		
	String createdOn;	
	String instanceName;	
	String fileName;
	String studyUid;
	String seriesUid;
	int instanceNumber;
	
	
	public String getSopClassUid() {
		return sopClassUid;
	}
	public void setSopClassUid(String sopClassUid) {
		this.sopClassUid = sopClassUid;
	}
	public String getSopInstanceUid() {
		return sopInstanceUid;
	}
	public void setSopInstanceUid(String sopInstanceUid) {
		this.sopInstanceUid = sopInstanceUid;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStudyUid() {
		return studyUid;
	}
	public void setStudyUid(String studyUid) {
		this.studyUid = studyUid;
	}
	public String getSeriesUid() {
		return seriesUid;
	}
	public void setSeriesUid(String seriesUid) {
		this.seriesUid = seriesUid;
	}
	public int getInstanceNumber() {
		return instanceNumber;
	}
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}
	
	
	
	
	

}
