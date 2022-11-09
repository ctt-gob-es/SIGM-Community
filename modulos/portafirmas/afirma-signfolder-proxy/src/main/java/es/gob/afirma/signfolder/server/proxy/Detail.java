package es.gob.afirma.signfolder.server.proxy;

/**
 * Datos identificados de un fichero para su descarga o previsualizaci&oacute;n.
 */
public class Detail {

	/** Flujo de firma en cascada. */
	public static final String SIGN_LINES_FLOW_CASCADE = "CASCADA"; //$NON-NLS-1$
	/** Flujo de firma en paralelo. */
	public static final String SIGN_LINES_FLOW_PARALLEL = "PARALELO"; //$NON-NLS-1$

	private final String id;

	private String priority = "1"; //$NON-NLS-1$

	private boolean workflow = false;

	private boolean forward = false;

	private String subject = null;

	private String text = null;

	private String[] senders = null;

	private String date = null;

	private String expdate = null;

	private String app = null;

	private String rejectReason = null;

	private String ref = null;

	private String type = null;

	private String signLinesFlow = SIGN_LINES_FLOW_CASCADE;

	private SignLine[] signLines;

	private SignRequestDocument[] docs;

	private SignRequestDocument[] attached;

	/**
	 * Construye el detalle de una petici&oacute;n de firma.
	 * @param id Identificador de la petici&oacute;n.
	 */
	public Detail(final String id) {
		this.id = id;
	}

	/**
	 * Recupera el identificador de la petici&oacute;n de firma.
	 * @return Identificador de la petici&oacute;n.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Recupera la prioridad de la petici&oacute;n. Cuando m&aacute;s alto el valor, m&aacute;s prioritario.
	 * @return Prioridad.
	 */
	public String getPriority() {
		return this.priority;
	}

	/**
	 * Establece la prioridad de la petici&oacute;n. Cuando m&aacute;s alto el valor, m&aacute;s prioritario.
	 * @param priority Prioridad
	 */
	public void setPriority(final String priority) {
		this.priority = priority;
	}

	/**
	 * Indica si el proceso de firma forma parte de un flujo de trabajo.
	 * @return {@code true} cuando procede de un flujo de trabajo, {@code false} en caso contrario.
	 */
	public boolean isWorkflow() {
		return this.workflow;
	}

	/**
	 * Establece si el proceso de firma forma parte de un flujo de trabajo.
	 * @param workflow {@code true} cuando procede de un flujo de trabajo, {@code false} en caso contrario.
	 */
	public void setWorkflow(final boolean workflow) {
		this.workflow = workflow;
	}

	/**
	 * Indica si a petici&oacute;n se reenvi&oacute;n.
	 * @return {@code true} si se reenvi&oacute;, {@code false} en caso contrario.
	 */
	public boolean isForward() {
		return this.forward;
	}

	/**
	 * Establece si a petici&oacute;n se reenvi&oacute;n.
	 * @param forward {@code true} si se reenvi&oacute;, {@code false} en caso contrario.
	 */
	public void setForward(final boolean forward) {
		this.forward = forward;
	}

	/**
	 * Recupera el asunto de la petici&oacute;n.
	 * @return Asunto.
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * Establece el asunto de la petici&oacute;n.
	 * @param subject Asunto.
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * Recupera el mensaje de texto asociado a la petici&oacute;n.
	 * @return Texto de la petici&oacute;n.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Establece el mensaje de texto asociado a la petici&oacute;n.
	 * @param text Texto de la petici&oacute;n.
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * Recupera el listado de usuarios que enviaron la petici&oacute;n.
	 * @return Listado de usuarios.
	 */
	public String[] getSenders() {
		return this.senders;
	}

	/**
	 * Establece el listado de usuarios que enviaron la petici&oacute;n.
	 * @param senders Listado de usuarios.
	 */
	public void setSenders(final String[] senders) {
		this.senders = senders;
	}

	/**
	 * Recupera la fecha de la petici&oacute;n.
	 * @return Fecha.
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * Establece la fecha de la petici&oacute;n.
	 * @param date Fecha.
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * Recupera la fecha de caducidad de la petici&oacute;n.
	 * @return Fecha.
	 */
	public String getExpDate() {
		return this.expdate;
	}

	/**
	 * Establece la fecha de caducidad de la petici&oacute;n.
	 * @param expdate Fecha.
	 */
	public void setExpDate(final String expdate) {
		this.expdate = expdate;
	}

	/**
	 * Recupera el nombre de la aplicaci&oacute;n que solicit&oacute; la firma.
	 * @return Nombre de la aplicaci&oacute;n.
	 */
	public String getApp() {
		return this.app;
	}

	/**
	 * Establece el nombre de la aplicaci&oacute;n que solicit&oacute; la firma.
	 * @param app Nombre de la aplicaci&oacute;n.
	 */
	public void setApp(final String app) {
		this.app = app;
	}

	/**
	 * Recupera la raz&oacute;n del rechazo de la petici&oacute;n.
	 * @return El motivo del rechazo de la peticion, si hubiese sido rechazada
	 * y se indicase el camino. En caso contrario, se devuelve {@code null}.
	 */
	public String getRejectReason() {
		return this.rejectReason;
	}

	/**
	 * Establece el motivo del rechazo de la petici&oacute;n.
	 * @param rejectReason Motivo del rechazo.
	 */
	public void setRejectReason(final String rejectReason) {
		this.rejectReason = rejectReason;
	}

	/**
	 * Recupera la referencia de la petici&oacute;n.
	 * @return Referencia de la petici&oacute;n.
	 */
	public String getRef() {
		return this.ref;
	}

	/**
	 * Establece la referencia de la petici&oacute;n.
	 * @param ref Referencia de la petici&oacute;n.
	 */
	public void setRef(final String ref) {
		this.ref = ref;
	}

	/**
	 * Recupera el tipo de la petici&oacute;n.
	 * @return Tipo de la petici&oacute;n.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Establece el tipo de la petici&oacute;n.
	 * @param type Tipo de la petici&oacute;n.
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Recupera el flujo de las l&iacute;neas de firma: CASCADA o PARALELO.
	 * @return Flujo de las l&iacute;neas de firma.
	 */
	public String getSignLinesFlow() {
		return this.signLinesFlow;
	}

	/**
	 * Establece el flujo de las l&iacute;neas de firma.
	 * @param signLinesFlow Flujo de l&iacute;neas de firma.
	 */
	public void setSignLinesFlow(final String signLinesFlow) {
		this.signLinesFlow = signLinesFlow;
	}

	/**
	 * Recupera el listado de l&iacute;neas de firma de la petici&oacute;n. Las l&iacute;neas de firma
	 * se componen de un listado de nombre de usuarios por los que debe pasar o ha pasado la firma.
	 * @return Listado de l&iacute;neas de firma.
	 */
	public SignLine[] getSignLines() {
		return this.signLines;
	}

	/**
	 * Establece el listado de l&iacute;neas de firma de la petici&oacute;n. Las l&iacute;neas de firma
	 * se componen de un listado de nombre de usuarios por los que debe pasar o ha pasado la firma.
	 * @param signLines Listado de l&iacute;neas de firma.
	 */
	public void setSignLines(final SignLine[] signLines) {
		this.signLines = signLines;
	}

	/**
	 * Recupera el listado de documentos que componen la petici&oacute;n de firma. La informaci&oacute;n
	 * de cada documento se almacena en objetos de tipo {@link es.gob.afirma.signfolder.server.proxy.SignRequestDocument}.
	 * @return Listado de documentos.
	 */
	public SignRequestDocument[] getDocs() {
		
		//[DipuCR-Agustin #1277] Los documentos con mimetype pdf que no llevan extension hay veces que no se abren, si no la llevan se la pongo 
		String nombreDocumentoAux = null;
		for (int i = 0; i < this.docs.length; i++) {
			
			nombreDocumentoAux = "";
			nombreDocumentoAux = this.docs[i].getName();
			
			if(this.docs[i].getMimeType().equals("application/pdf"))
			{
				if(!nombreDocumentoAux.endsWith(".pdf")) {
					
					nombreDocumentoAux = nombreDocumentoAux.concat(".pdf");
					this.docs[i].setName(nombreDocumentoAux);
					
				}
			}
			else
			{
				if(!nombreDocumentoAux.endsWith(".xml")) {
					
					nombreDocumentoAux = nombreDocumentoAux.concat(".doc");
					this.docs[i].setName(nombreDocumentoAux);
					
				}
					
			}
			
		}
		
		//[DipuCR-Agustin #1277] FIN		
		
		
		return this.docs;
	}

	/**
	 * Establece el listado de documentos que componen la petici&oacute;n de firma. La informaci&oacute;n
	 * de cada documento se almacena en objetos de tipo {@link es.gob.afirma.signfolder.server.proxy.SignRequestDocument}.
	 * @param docs Listado de documentos.
	 */
	public void setDocs(final SignRequestDocument[] docs) {
		this.docs = docs;
	}

	/**
	 * Recupera el listado de anexos que componen la petici&oacute;n de firma. La informaci&oacute;n
	 * de cada documento se almacena en objetos de tipo {@link es.gob.afirma.signfolder.server.proxy.SignRequestDocument}.
	 * @return Listado de anexos.
	 */
	public SignRequestDocument[] getAttached() {
		return this.attached;
	}

	/**
	 * Establece el listado de anexos que componen la petici&oacute;n de firma. La informaci&oacute;n
	 * de cada documento se almacena en objetos de tipo {@link es.gob.afirma.signfolder.server.proxy.SignRequestDocument}.
	 * @param attached Listado de anexos.
	 */
	public void setAttached(final SignRequestDocument[] attached) {
		this.attached = attached;
	}
}
