package es.dipucr.sigem.api.rule.procedures.secretaria;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.DateUtil;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class DipucrTrasladarAcuerdosRule implements IRule {
	
	
	private static final Logger logger = Logger.getLogger(DipucrTrasladarAcuerdosRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		try
		{
			/**
			 * Variables que se utilizarán para insertar en la bbdd los datos 
			 * sobre el envío correcto o incorrecto del email.
			 * */
			String emailNotif = "";
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
			Object[] imagen = {rutaImg, new Boolean(true), "logoCabecera.gif", "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);

			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
						
        	//Comprobar que el acuerdo esté firmado
			// --> Está ya hecho en ValidateFirmaRule, ejecutada antes que esta regla
			
	        //Obtener las propuestas y urgencias incluidas en la sesión
	        List listPropuestas = SecretariaUtil.getPropuestas(rulectx, entitiesAPI);
	        Iterator it = listPropuestas.iterator();
	        
	        List listUrgencias = SecretariaUtil.getUrgencias(rulectx, entitiesAPI);
	        IItem iProp = null;
	        
	        boolean urgencia = false;

	        //Para cada propuesta se envía un email a sus participantes trasladados
	        int orden = 0;
	        while (it.hasNext())
	        {
	        	orden++;
	        	iProp = (IItem)it.next();
	        	String numexp_origen = iProp.getString("NUMEXP_ORIGEN");
	        	
				//Obtener Participantes de la propuesta actual, con relación "Trasladado"
				IItemCollection participantes = entitiesAPI.getParticipants(numexp_origen, "ROL= 'TRAS'", "ID");
				//logger.warn("1.- sqlQueryPart "+sqlQueryPart);
				
				//Obtener el número de la propuesta de urgencia
				if(urgencia){
					IItemCollection itcolPropuesta = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), "(DESCRIPCION LIKE '%"+numexp_origen+"%')", "");
					Iterator<IItem> itPropuesta = itcolPropuesta.iterator();
					while (itPropuesta.hasNext()) {
			        	IItem itemPropuesta = ((IItem)itPropuesta.next());
			        	String desc = itemPropuesta.getString("DESCRIPCION");
						desc = desc.replaceFirst("Propuesta Urgencia - ", "");
	
			        	logger.warn("desc "+desc);
			        	char sNumPropuesta = desc.charAt(0);
			        	logger.warn("sNumPropuesta "+sNumPropuesta);
			        	char sNumPropuestaDecima = desc.charAt(1);
			        	logger.warn("sNumPropuestaDecima "+sNumPropuestaDecima);
			        	if(sNumPropuestaDecima != ' '){
			        		String numD = desc.substring(0, 2);
			        		orden = Integer.parseInt(numD);
			        	}
			        	else{
			        		orden = Integer.parseInt(sNumPropuesta+"");
			        	}
			        	logger.warn("orden "+orden);
					}
				}
				
				
				//Obtener el documento Certificado de acuerdos para anexarlo al email 
				int taskId = rulectx.getTaskId();
				String sqlQueryDoc = "";
				if(!urgencia){
					sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA IN ('02','03') AND DESCRIPCION = '"+orden+".-Certificado de acuerdos'";
				}
				else{
					sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA IN ('02','03') AND DESCRIPCION = '"+orden+".- Urgencia Certificado de acuerdos'";
				}
				
				//logger.warn("---------------------------- sqlQueryDoc "+sqlQueryDoc);
				
				IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
				
				if (documentos.toList().size() == 1) 
				{
					//Enviar email con el decreto adjunto
					IItem sesion = SecretariaUtil.getSesion(rulectx, rulectx.getNumExp());
					Date fechaConv = sesion.getDate("FECHA");
					String horaConv = sesion.getString("HORA");
					String numexpConv = rulectx.getNumExp();
					String asuntoProp = iProp.getString("EXTRACTO");
					String ordenProp = iProp.getString("ORDEN");
					
					String organo = SecretariaUtil.getNombreOrganoSesion(rulectx, rulectx.getNumExp());
					String cContenido = "<br/>Número de expediente de la convocatoria: " +numexpConv+
							", de fecha y hora: " +DateUtil.format(fechaConv, "dd/MM/yyyy")+ " "+ horaConv+
							", número de expediente de la Propuesta: " +numexp_origen+" -> "+ordenProp+": "+asuntoProp+"\n"+
							"Adjunto se envía el Certificado ";
					String cAsunto= "[SIGEM] Traslado de "+organo+", convocatoria Nº"+sesion.getString("NUMCONV");
					
					
					// Fichero a adjuntar
					IItem doc = (IItem)documentos.iterator().next();
					String infoPag = doc.getString("INFOPAG_RDE");
					
					Date fecha = doc.getDate("FAPROBACION");
					
					String numconv = sesion.getString("NUMCONV");
					String [] vNum = numconv.split("/");
					String nombreFichero=organo+"-"+fecha.getYear()+"-"+vNum[0]+"-"+vNum[1];
					
					File file = DocumentosUtil.getFile(cct,infoPag, nombreFichero, null);
					
					// Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
					if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
						for (int i=0; i<participantes.toList().size(); i++)
						{
							IItem participante = (IItem) participantes.toList().get(i);
							emailNotif = participante.getString("DIRECCIONTELEMATICA");
							
							//logger.warn("-"+nombreNotif);
							
							if (emailNotif != null)
							{
								StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
								while (tokens.hasMoreTokens()) 
								{
									String cCorreoDestino = tokens.nextToken();	
									if (participante!=null)
									{
							        	if (!cCorreoDestino.equals("")) 
							        	{
							        		cContenido = MailUtil.formateContenidoEmail(rulectx, cAsunto, cContenido);
							        		MailUtil.enviarCorreoVarios(rulectx, cCorreoDestino, cAsunto, cContenido, false, file, imagenes);
							        	}
								    }
								}
							}
						}
					}
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
			logger.error(e.getMessage(), e);
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
