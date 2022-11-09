package es.dipucr.sigem.rechazoFirma.api;

import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;

import java.util.Date;
import java.util.List;

@Deprecated
public interface ISignRechazoAPI extends ISignAPI {

	public List rechazarFirma(String[] stepIds, String signCertificate, String entityId, String motivoRechazo) throws ISPACException;
	public boolean rechazarFirma(SignDocument signDocument, int instancedStepId) throws ISPACException;
	public void rechazar(SignDocument signDocument, boolean changeState)throws ISPACException;
	
	//MQE Añadimos la pantalla de histórico de documentos rechazados
	IItemCollection getHistorics(String respId, Date init, Date end, int state) throws ISPACException;
}
