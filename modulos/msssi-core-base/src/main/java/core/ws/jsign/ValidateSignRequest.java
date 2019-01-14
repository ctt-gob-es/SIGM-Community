package core.ws.jsign;


public class ValidateSignRequest {
    
    private String applicationAlias;
    private String signatureType;
    private byte[] signData;    
    private byte[] detachedOriginalData;
    private boolean returnSignedContent;
    
    public ValidateSignRequest(){
        applicationAlias = "";
        signatureType = "";
        signData = null;
        detachedOriginalData = null;
        returnSignedContent = false;
    }

    public String getApplicationAlias() {
        return applicationAlias;
    }

    public void setApplicationAlias(String applicationAlias) {
        this.applicationAlias = applicationAlias;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public byte[] getSignData() {
        return signData;
    }

    public void setSignData(byte[] signData) {
        this.signData = signData;
    }

    public byte[] getDetachedOriginalData() {
        return detachedOriginalData;
    }

    public void setDetachedOriginalData(byte[] detachedOriginalData) {
        this.detachedOriginalData = detachedOriginalData;
    }

    public boolean isReturnSignedContent() {
        return returnSignedContent;
    }

    public void setReturnSignedContent(boolean returnSignedContent) {
        this.returnSignedContent = returnSignedContent;
    }

}
