package beans;

import java.util.Date;

public class SignResponse {

    private String documentId;
    private byte[] signData;
    private Date fechaFirma;
    
    public SignResponse(){
        this.documentId = "";
        this.signData = null;
    }
    
    public SignResponse(String documentId, byte[] signData){
        this.documentId = documentId;
        this.signData = signData;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public byte[] getSignData() {
        return signData;
    }

    public void setSignData(byte[] signData) {
        this.signData = signData;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }
}
