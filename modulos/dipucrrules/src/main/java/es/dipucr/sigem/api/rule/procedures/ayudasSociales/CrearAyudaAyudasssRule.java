package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FileBean;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.common.utils.ZipUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.webempleado.domain.beans.DocumentoAyudas;
import es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWSProxy;


/**
 * [dipucr-Felipe #469]
 * Regla que crea una nueva ayuda social en la bbdd de ayudasss (portal empleado)
 * @since 29.03.17
 */
public class CrearAyudaAyudasssRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CrearAyudaAyudasssRule.class);
	
	protected static final String COD_TRAMITE_AYUDASSS = "TRAM-AYUDASSS";
	protected static final String COD_DOC_ANEXO = "AYUDASSS-ANEXO";
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			ArrayList<DocumentoAyudas> listDocumentos = new ArrayList<DocumentoAyudas>();
			
			IItemCollection anexosCollection = entitiesAPI.getDocuments(numexp, " TP_REG = 'ENTRADA' AND UPPER(DESCRIPCION) LIKE '%ZIP'", "");
			
			if (anexosCollection.toList().size() > 0){ //Tiene documentos anexos
				
				//Creación del trámite - Necesitamos sacar la fase y la fasePcd, que no vienen en el contexto
				int idTramite = crearTramite(rulectx, COD_TRAMITE_AYUDASSS);

				int idTipoDocumento = DocumentosUtil.getTipoDocByCodigo(cct, COD_DOC_ANEXO).getKeyInt();
				
				IItem itemZipAnexo =  (IItem) anexosCollection.iterator().next();
				String sInfopagZipAnexo = itemZipAnexo.getString("INFOPAG");
				
				File fileZipAnexos = DocumentosUtil.getFile(cct, sInfopagZipAnexo, null, null);
				
				List<FileBean> listFicheros = ZipUtils.extraerTodosFicheros(fileZipAnexos);
				for (FileBean fb : listFicheros){
					
					String nombreFichero = FilenameUtils.getBaseName(fb.getName());
					String extension = FilenameUtils.getExtension(fb.getName());
				
                    IItem itemDocAnexo = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramite, idTipoDocumento, 
                    		nombreFichero, fb.getFile(), extension);
                    
                    itemDocAnexo.set("ESTADO", "PÚBLICO");
                    itemDocAnexo.store(cct);
                    
                    DocumentoAyudas oDocumento = new DocumentoAyudas();
                    oDocumento.setIdDocumento(itemDocAnexo.getKeyInt());
                    oDocumento.setNombre(nombreFichero);
                    oDocumento.setExtension(extension);
                    
                    listDocumentos.add(oDocumento);
				}
			}
			
			//Petición al webservice
			IItemCollection colAyudasss = entitiesAPI.getEntities("AYUDASSS", numexp);
			IItem itemAyudasss = (IItem) colAyudasss.iterator().next();
			
			IItem itemExpedient = entitiesAPI.getExpedient(numexp);
			
			AyudasSocialesWSProxy ws = new AyudasSocialesWSProxy();
			int idAyuda = ws.crearAyuda
			(
					itemAyudasss.getString("ANO_AYUDA"),
					Integer.valueOf(itemAyudasss.getString("ID_CONCEPTO")),
					Integer.valueOf(itemAyudasss.getString("ID_GRUPO")),
					itemAyudasss.getString("NIF_EMPLEADO"),
					itemAyudasss.getString("PARENTESCO"),
					itemAyudasss.getString("NUM_FACTURA"),
					itemAyudasss.getString("FECHA_FACTURA"),
					(!StringUtils.isEmpty(itemAyudasss.getString("IMPORTE")) ? Double.valueOf(itemAyudasss.getString("IMPORTE")) : 0.0),
					(!StringUtils.isEmpty(itemAyudasss.getString("IMPORTE_CONCEDIDO")) ? Double.valueOf(itemAyudasss.getString("IMPORTE_CONCEDIDO")) : 0.0),
					itemExpedient.getString("FREG"),
					itemAyudasss.getString("BENEFICIARIO"),
					itemAyudasss.getString("FECHA_NAC_BENEFICIARIO"),
					itemExpedient.getString("DIRECCIONTELEMATICA"),
					itemExpedient.getString("TFNOMOVIL"),
					itemAyudasss.getString("OBSERVACIONES"),
					itemExpedient.getString("NREG"),
					numexp,
					listDocumentos.toArray(new DocumentoAyudas[listDocumentos.size()]),
					itemAyudasss.getString("TIPO_CONTRATO"),
					itemAyudasss.getString("ID_CONVOCATORIA") //[dipucr-Felipe WE#156]
			);
			itemAyudasss.set("ID_AYUDA", idAyuda);
			itemAyudasss.store(cct);
		
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la creación de la " + e.getMessage(), e);
		}
		return null;
	}

	
	private int crearTramite(IRuleContext rulectx, String codTramite) throws ISPACException {
		
		IClientContext cct = rulectx.getClientContext();
  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		ITXTransaction tx = invesFlowAPI.getTransactionAPI();
		
		int pid = rulectx.getProcessId();
		
		IItemCollection stages = invesFlowAPI.getStagesProcess(pid);
		IItem itemFase = (IItem) stages.toList().get(0);
		int idFase = Integer.valueOf(itemFase.getString("ID_FASE_BPM"));
		int idFasePcd = itemFase.getInt("ID_FASE");
		
		IItem itemCtTramite = TramitesUtil.getTramiteByCode(cct, rulectx.getNumExp(), codTramite);
		String strQuery = "WHERE ID_FASE = " + idFasePcd + " AND NOMBRE = '" + itemCtTramite.getString("NOMBRE") + "'";
		IItemCollection collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQuery);
		IItem itemPTramite = (IItem)collection.iterator().next();
		int idTramitePcd = itemPTramite.getInt("ID"); 
		
		//Creamos el trámite
		int idTramite = tx.createTask(idFase, idTramitePcd);
		return idTramite;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
