package es.gob.afirma.signfolder.server.proxy;

public class FireLoadDataResult {

	private boolean statusOk;

	private String transactionId;

	private String urlRedirect;

	public FireLoadDataResult() {
		this.statusOk = false;
		this.transactionId = null;
		this.urlRedirect = null;
	}

	public void setStatusOk(final boolean statusOk) {
		this.statusOk = statusOk;
	}

	public void setTransactionId(final String transactionId) {
		this.transactionId = transactionId;
	}

	public void setUrlRedirect(final String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}

	public boolean isStatusOk() {
		return this.statusOk;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public String getUrlRedirect() {
		return this.urlRedirect;
	}
}
