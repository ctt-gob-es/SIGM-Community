package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [dipucr-Felipe 3#675]
 * Regla que genera el oficio de remisión
 * @author Felipe
 * @since 19.02.2018
 */
public class GenerarOficioRemisionTablonRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(GenerarOficioRemisionTablonRule.class);
	
	protected static final String _DOC_OFICIO = "eTablon Oficio de remisión";
	
	protected IItemCollection colInteresados;
	protected IItem itemDocDiligencia;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		String numexp = "";
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
			//*********************************************
			
			numexp = rulectx.getNumExp();
			
			//Controlamos que exista el interesado
			colInteresados = ParticipantesUtil.getParticipantesByRol
					(cct, numexp, ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil.ID);
			
			if (colInteresados.toList().size() == 0){
				rulectx.setInfoMessage("Debe insertar al menos participante de tipo INTERESADO al "
						+ "que se le notificará el oficio de remisión. No existe ningún INTERESADO");
				return false;
			}
			
			//Controlamos que exista la diligencia de publicación y esta esté firmada
			itemDocDiligencia = DocumentosUtil.getPrimerDocumentByNombre
					(rulectx.getNumExp(), rulectx, GenerarCertificadoTablonRule._DOC_CERTIFICADO);
			if (null == itemDocDiligencia || StringUtils.isEmpty(itemDocDiligencia.getString("INFOPAG_RDE"))){
				rulectx.setInfoMessage("El documento de diligencia no existe o aún no está firmado.");
				return false;
			}
			
		}
		catch (Exception e) {
			String error = "Error al validar los datos para generar el oficio de remisión en el expediente: " + numexp;
        	logger.error(error + ". " + e.getMessage(), e);
			throw new ISPACRuleException(error, e);
		}
		
		
		return true;
	}
	
	/**
	 * Generación de la fase, el trámite y envío al Jefe de departamento para firma
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = "";
		
		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			//*********************************************
			
			numexp = rulectx.getNumExp();
			
			//*********************************************
			//Generamos el oficio
			//*********************************************
			//Obtenemos el fichero de diligencia
			String strInfoPagPub = itemDocDiligencia.getString("INFOPAG_RDE");
			File fileDiligencia = DocumentosUtil.getFile(cct, strInfoPagPub, null, null);
			
			@SuppressWarnings("unchecked")
			List<IItem> listInteresados = colInteresados.toList();
			
			for (IItem itemInteresado : listInteresados){
			
				setSessionVariables(numexp, cct, itemInteresado);
						
				//Generamos la plantilla de certificado
				IItem itemDocOficio = DocumentosUtil.generarDocumento(rulectx, _DOC_OFICIO, _DOC_OFICIO);
				int idDocumentoCert = itemDocOficio.getKeyInt();
				
				//Convertimos a pdf la plantilla
				String strInfoPagCert = itemDocOficio.getString("INFOPAG");
				String sRutaCertificado = DocumentConverter.convert2PDF(invesFlowAPI, strInfoPagCert, Constants._EXTENSION_ODT);
		        //Obtenemos el file
				File fileOficioPDF = new File(sRutaCertificado);
				
				//Adjuntamos el archivo
	        	PdfUtil.anexarDocumento(fileOficioPDF, fileDiligencia, "Diligencia.pdf");
	
	        	itemDocOficio = DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(),
	        			idDocumentoCert, fileOficioPDF, Constants._EXTENSION_PDF, _DOC_OFICIO);
	        	
	        	String templateDescripcion = itemDocOficio.getString("DESCRIPCION");
				templateDescripcion = templateDescripcion + " - " + cct.getSsVariable("NOMBRE");
				itemDocOficio.set("DESCRIPCION", templateDescripcion);
				itemDocOficio.set("DESTINO", cct.getSsVariable("NOMBRE"));
	        	itemDocOficio.set("AUTOR", itemDocDiligencia.getString("AUTOR"));
	        	itemDocOficio.set("AUTOR_INFO", itemDocDiligencia.getString("AUTOR_INFO"));
	        	itemDocOficio.store(cct);
	        	
	        	if(fileOficioPDF != null && fileOficioPDF.exists()) fileOficioPDF.delete();
	        	fileOficioPDF = null;
	        	
	        	deleteSessionVariables(cct);
			}
				
		}
		catch (Exception e) {
			String error = "Error al generar el oficio de remisión en el expediente: " + numexp;
        	logger.error(error + ". " + e.getMessage(), e);
			throw new ISPACRuleException(error, e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	/**
	 * Setea las variables de sesión del participante
	 * @param rulectx
	 * @param itemInteresado 
	 * @throws ISPACRuleException
	 */
	public void setSessionVariables(String numexp, IClientContext cct, IItem itemInteresado) throws ISPACRuleException{

		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			if (itemInteresado!=null){
		        
				String nombre = StringUtils.nullToEmpty(itemInteresado.getString("NOMBRE"));
				String dirnot = StringUtils.nullToEmpty(itemInteresado.getString("DIRNOT"));
	        	String cPostal = StringUtils.nullToEmpty(itemInteresado.getString("C_POSTAL"));
	        	String localidad = StringUtils.nullToEmpty(itemInteresado.getString("LOCALIDAD"));
	        	String caut = StringUtils.nullToEmpty(itemInteresado.getString("CAUT"));
	        	String recurso = StringUtils.nullToEmpty(itemInteresado.getString("RECURSO"));
	        	String observaciones = StringUtils.nullToEmpty(itemInteresado.getString("OBSERVACIONES"));
	        	String tipoDireccion = StringUtils.nullToEmpty(itemInteresado.getString("TIPO_DIRECCION"));
	        	
	        	String sqlQuery = "WHERE VALOR = '"+recurso+"'";
	        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQuery);
	        	if (colRecurso.iterator().hasNext()){
	        		IItem iRecurso = (IItem)colRecurso.iterator().next();
	        		recurso = iRecurso.getString("SUSTITUTO");
	        	}
	        	if (StringUtils.isEmpty(recurso)){
	        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
	        	}
	        	else{
	        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
	        	}
	        	
	        	cct.setSsVariable("NOMBRE", nombre);
	        	cct.setSsVariable("DIRNOT", dirnot);
	        	cct.setSsVariable("C_POSTAL", cPostal);
	        	cct.setSsVariable("LOCALIDAD", localidad);
	        	cct.setSsVariable("CAUT", caut);
	        	cct.setSsVariable("RECURSO", recurso);
	        	cct.setSsVariable("OBSERVACIONES", observaciones);
	        	cct.setSsVariable("TIPO_DIRECCION", tipoDireccion);
			}
		}
		catch (Exception e) {
			String error = "Error al crear las variables de sesión del interesado al generar el oficio de remisión: " + numexp;
        	logger.error(error + ". " + e.getMessage(), e);
			throw new ISPACRuleException(error, e);
		}
	}
	
	public void deleteSessionVariables(IClientContext cct) throws ISPACException{

		cct.deleteSsVariable("NOMBRE");
    	cct.deleteSsVariable("DIRNOT");
    	cct.deleteSsVariable("C_POSTAL");
    	cct.deleteSsVariable("LOCALIDAD");
    	cct.deleteSsVariable("CAUT");
    	cct.deleteSsVariable("RECURSO");
    	cct.deleteSsVariable("OBSERVACIONES");
    	cct.deleteSsVariable("TIPO_DIRECCION");
	}

}
