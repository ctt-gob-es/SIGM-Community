package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class DipucrTrasladarDictamenRule implements IRule {
	
	protected static final Logger logger = Logger.getLogger(DipucrTrasladarDictamenRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		try
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
			Object[] imagen = {rutaImg, new Boolean(true), "logoCabecera.gif", "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
	        
        	//Comprobar que el acuerdo esté firmado
			// --> Está ya hecho en ValidateFirmaRule, ejecutada antes que esta regla
			
	        //Obtener las propuestas y urgencias incluidas en la sesión
	        List listPropuestas = SecretariaUtil.getPropuestas(rulectx, entitiesAPI);
	        Iterator it = listPropuestas.iterator();
	        
	        List listUrgencias = SecretariaUtil.getUrgencias(rulectx, entitiesAPI);
	        IItem iProp = null;
	        
	        boolean urgencia = false;

	        //Para cada propuesta se envía un email a sus participantes trasladados
	        int orden = 1;
	        if(it.hasNext()){
	        	it.next();
	        }
	        while (it.hasNext())
	        {
	        	orden++;
	        	iProp = (IItem)it.next();
	        	String numexp_origen = iProp.getString("NUMEXP_ORIGEN");
	        	
	        	IItem expediente = ExpedientesUtil.getExpediente(cct, numexp_origen);
		        String seccionIniciadora = "";
		        
		        if(expediente != null){
		        	seccionIniciadora = expediente.getString("SECCIONINICIADORA");
		        }

		        //Obtener el documento Certificado de acuerdos para anexarlo al email 
				String sqlQueryDoc = "";
				if(!urgencia){
					sqlQueryDoc = "ESTADOFIRMA = '02' AND DESCRIPCION = '"+orden+".-Certificado de dictamen'";
				}
				else{
					sqlQueryDoc = "ESTADOFIRMA = '02' AND DESCRIPCION = '"+orden+".- Urgencia Certificado de dictamen'";
				}
				File file = null;
				//logger.warn("sqlQueryDoc "+sqlQueryDoc);
				IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
				//logger.warn("documentos "+documentos.toList().size());
				if (documentos.toList().size() == 1) 
				{
					// Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
					String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
					AccesoBBDDRegistro accsRegistro = new AccesoBBDDRegistro(entidad);
					String direccionesCorreo = accsRegistro.getEmailDepartamento(seccionIniciadora);

					
					if (direccionesCorreo != null){
						StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
						while (tokens.hasMoreTokens()) 
						{
							String cCorreoDestino = tokens.nextToken();	
							String cContenido = "<br/>Adjunto se envía el Certificado del Dictamen "+orden+" ";
							String cAsunto= "[SIGEM] Traslado Dictamen";
							
							// Fichero a adjuntar
							IItem doc = (IItem)documentos.iterator().next();
							String infoPag = doc.getString("INFOPAG_RDE");
							
							file = DocumentosUtil.getFile(cct, infoPag, null, null);
							
							cContenido = MailUtil.formateContenidoEmail(rulectx, cAsunto, cContenido);
			        		MailUtil.enviarCorreoVarios(rulectx, cCorreoDestino, cAsunto, cContenido, false, file, imagenes);
							
							//MailUtil.enviarCorreoConAcuses(rulectx, cCorreoDestino, file, cContenido, cAsunto, nombreDoc, cAsunto, false);
						}
					}
				}
				if(file!=null){
					file.delete();
				}				
				if(!it.hasNext() && !urgencia){
	        		it = listUrgencias.iterator();
	        		urgencia = true;
	        		orden = 0;
	        	}
	        }
			return null;
		}
		catch(Exception e) 
		{
			logger.error("Error al generar el dictamen. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
