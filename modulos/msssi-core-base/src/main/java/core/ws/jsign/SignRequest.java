package core.ws.jsign;

public class SignRequest {

    private String applicationAlias;
    private String attachmentType;
    private String certificateAlias;
    private byte[] fileData;
    private String fileName;
    private String signatureType;
    private String multisignatureType;
    private String policyId;
    private String entidadId;
    
    public SignRequest(){
        applicationAlias = "";
        attachmentType = "";
        certificateAlias = "";
        fileData = null;
        fileName = "";
        signatureType = "";
        multisignatureType = "";
        policyId = "";
        entidadId = "";
    }

    public String getApplicationAlias() {
        return applicationAlias;
    }

    public void setApplicationAlias(String applicationAlias) {
        this.applicationAlias = applicationAlias;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getCertificateAlias() {
        return certificateAlias;
    }

    public void setCertificateAlias(String certificateAlias) {
        this.certificateAlias = certificateAlias;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public String getMultisignatureType() {
        return multisignatureType;
    }

    public void setMultisignatureType(String multisignatureType) {
        this.multisignatureType = multisignatureType;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(String entidadId) {
        this.entidadId = entidadId;
    }
}
