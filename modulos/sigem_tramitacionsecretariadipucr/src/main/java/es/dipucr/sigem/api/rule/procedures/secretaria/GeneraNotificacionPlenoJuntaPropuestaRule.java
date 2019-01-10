package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GeneraNotificacionPlenoJuntaPropuestaRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(GeneraNotificacionPlenoJuntaPropuestaRule.class);

	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	String numexp = "";
    	try{
    		//----------------------------------------------------------------------------------------------
			IClientContext cct = rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            numexp = rulectx.getNumExp();
            
            String strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifAc");
            String strNombreDocCab = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifAcCab");
            String strNombreDocPie = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifAcPie");
            
            boolean urgencia = false;
            Vector <IItem> vPropuesta = null;
            int i = 1;            

    		//GENERACION DE LAS PROPUESTAS
    		//logger.warn("GENERACION DE LAS PROPUESTAS");
    		IItemCollection collection = DocumentosUtil.getDocumentsByNombre(numexp, rulectx, "Propuesta", "DESCRIPCION");

    		if(!collection.iterator().hasNext()){
    			//logger.warn("CARGA DE URGENCIAS");
        		//Ya no hay mas propuestas ahora se mira las urgencias
   	    		collection = DocumentosUtil.getDocumentsByNombre(numexp, rulectx, "Propuesta Urgencia");

  	  		  	if(collection.toList().size()!=0){
	  	  		  	urgencia = true;
	  	  		  	vPropuesta = SecretariaUtil.orderUrgencias(collection);
	  	  		  	i = 0;
  	  		  	}
    		} else{
    			vPropuesta = SecretariaUtil.orderPropuestas(rulectx);
    		}
    		    		
    		SimpleDateFormat dateformat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
    		String fecha = dateformat.format(new Date());
    		if(vPropuesta != null){
	 	        while( i < vPropuesta.size()) {
	 	        	//logger.warn("COMIENZO WHILEEEEE. "+vPropuesta.get(i));
	 	        	if(vPropuesta.get(i)!=null){
	 	        		
	 	        		IItem item = ((IItem)vPropuesta.get(i));
		 	        	String descripcion = item.getString("DESCRIPCION");
		 	        			 	        	
		 	    		//Sacar el expediente de esa propuesta
		 	    		String numexp_origen=sacarNumExp(descripcion);
		 	    		
		 	    		//Se genera una notificación por cada participante de la propuesta
			        	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexp_origen, "ROL != 'TRAS' OR ROL IS NULL", "ID");
			        	
			        	Iterator itParticipante = participantes.iterator();
			        	
						String ndoc = "";
						String nombre = "";
				    	String dirnot = "";
				    	String c_postal = "";
				    	String localidad = "";
				    	String caut = "";
				    	String recurso = "";
				    	/**
				    	 * INICIO[Teresa] Ticket #106 añadir el id_ext
				    	 * **/
				    	String id_ext = "";
				    	/**
				    	 * FIN[Teresa] Ticket #106 añadir el id_ext
				    	 * **/
			        	while (itParticipante.hasNext()) {
			        		ooHelper = OpenOfficeHelper.getInstance();
			        		
			        		cct.setSsVariable("FECHA", fecha);
			        		IItem participante = (IItem)itParticipante.next();
			        		
			        		recurso = "";

							// Añadir a la sesion los datos para poder utilizar <ispatag sessionvar='var'> en la plantilla
				        	if ((String)participante.getString("NDOC")!=null) ndoc = (String)participante.getString("NDOC");
				        	if ((String)participante.getString("NOMBRE")!=null) nombre = (String)participante.getString("NOMBRE");
				        	if ((String)participante.getString("DIRNOT")!=null) dirnot = (String)participante.getString("DIRNOT");
				        	if ((String)participante.getString("C_POSTAL")!=null) c_postal = (String)participante.getString("C_POSTAL");
				        	if ((String)participante.getString("LOCALIDAD")!=null) localidad = (String)participante.getString("LOCALIDAD");
				        	if ((String)participante.getString("CAUT")!=null) caut = (String)participante.getString("CAUT");
				        	if ((String)participante.getString("RECURSO")!=null) recurso = (String)participante.getString("RECURSO");
				        	/**
					    	 * INICIO[Teresa] Ticket #106 añadir el id_ext
					    	 * **/
				        	if ((String)participante.getString("ID_EXT")!=null) id_ext = (String)participante.getString("ID_EXT");
					    	/**
					    	 * FIN[Teresa] Ticket #106 añadir el id_ext
					    	 * **/
				        	
				        	cct.setSsVariable("NDOC", ndoc);
				        	cct.setSsVariable("NOMBRE", nombre);
				        	cct.setSsVariable("DIRNOT", dirnot);
				        	cct.setSsVariable("C_POSTAL", c_postal);
				        	cct.setSsVariable("LOCALIDAD", localidad);
				        	cct.setSsVariable("CAUT", caut);
				        	
				        	// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
				        	String sqlQueryPart = "WHERE VALOR = '"+recurso+"'";
				        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
				        	if (colRecurso.iterator().hasNext()){
				        		IItem iRecurso = (IItem)colRecurso.iterator().next();
				        		recurso = iRecurso.getString("SUSTITUTO");
				        	}
				        	/**
				        	 * INICIO
				        	 * ##Ticket #172 SIGEM decretos y secretaria, modificar el recurso
				        	 * **/
				        	if (recurso.equals("")){
				        		recurso += Constants.SECRETARIAPROC.sinRECUSO;
				        	}
				        	else{
				        		recurso += Constants.SECRETARIAPROC.conRECUSO;
				        	}
				        	/**
				        	 * FIN
				        	 * ##Ticket #172 SIGEM decretos y secretaria, modificar el recurso
				        	 * **/
				        	cct.setSsVariable("RECURSO", recurso);
				        				        		
			        		//logger.warn("NOTIFICACION CABECERA INICIO "+strNombreDocCab);
			            	DocumentosUtil.generarDocumento(rulectx, strNombreDocCab, null);
			            	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(numexp, rulectx, strNombreDocCab);
			            	//logger.warn(strInfoPag);
			            	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			            	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
			        		file.delete();
			        		//logger.warn("FIN CABECERA NOTIFICACION");
			        		
			        		//Cuerpo
			 	        	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(numexp, rulectx, descripcion);
			 	        	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			 	        	DipucrCommonFunctions.concatena(xComponent, "file://" + file.getPath());
			 	    		file.delete();
			        		
			        		//Pie
			 	    		//logger.warn("NOTIFICACION PIE INICIO "+strNombreDocPie);
			    	    	DocumentosUtil.generarDocumento(rulectx, strNombreDocPie, null);
			    	    	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(numexp, rulectx, strNombreDocPie);
			    	    	//logger.warn(strInfoPag);
			    	    	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			    	    	DipucrCommonFunctions.concatena(xComponent, "file://" + file.getPath());
			    			file.delete();
			    			//logger.warn("FIN PIE NOTIFICACION");
			    	
			    			//Guarda el resultado en repositorio temporal
			    			String fileName = FileTemporaryManager.getInstance().newFileName(".odt");
			    			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			    			file = new File(fileName);
			    			
			    			
			    			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			    			
			    			//Guarda el resultado en gestor documental
			    			int tpdoc = DocumentosUtil.getTipoDoc(cct, strNombreDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);

			    			IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, strNombreDoc, file, Constants._EXTENSION_ODT);

			    			String orden = "";
			    			if(i<10){
			    				orden = "0"+i;
			    			}
			    			else{
			    				orden = i+"";
			    			}
			    			if(!urgencia){
			    				entityDoc.set("DESCRIPCION", orden +".-"+strNombreDoc+" - "+nombre);
			    			}
			    			else{
			    				entityDoc.set("DESCRIPCION", orden +".- Urgencia "+strNombreDoc+" - "+nombre);
			    			}
			    			entityDoc.set("DESTINO", nombre);
			    			
			    			/**
					    	 * INICIO[Teresa] Ticket #106 añadir el id_ext
					    	 * **/
			    			entityDoc.set("DESTINO_ID", id_ext);
					    	/**
					    	 * FIN[Teresa] Ticket #106 añadir el id_ext
					    	 * **/
			    			
			    			entityDoc.store(cct);
			    			file.delete();
			    			
			    			//Borra los documentos intermedios del gestor documental			    			
			    	        collection = entitiesAPI.getDocuments(numexp, "DESCRIPCION LIKE '" + strNombreDoc + " -%' OR DESCRIPCION LIKE 'Notificación de acuerdos -%'", "");
			    	        Iterator it = collection.iterator();
			    	        while (it.hasNext())
			    	        {
			    	        	IItem doc = (IItem)it.next();
			    	        	entitiesAPI.deleteDocument(doc);
			    	        }
			        		strInfoPag = DocumentosUtil.getInfoPagByDescripcion(numexp, rulectx, descripcion);
			 	        	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			 	        	DipucrCommonFunctions.concatena(xComponent, "file://" + file.getPath());
			 	    		file.delete();
			 	    		file = null;

				        	//Generación del acuse de recibo
				        	
							cct.deleteSsVariable("NDOC");
							cct.deleteSsVariable("NOMBRE");
							cct.deleteSsVariable("DIRNOT");
							cct.deleteSsVariable("C_POSTAL");
							cct.deleteSsVariable("LOCALIDAD");
							cct.deleteSsVariable("CAUT");
							cct.deleteSsVariable("RECURSO");
							cct.deleteSsVariable("FECHA");
							
							if(null != ooHelper){
					        	ooHelper.dispose();
							}
			 	    		if (xComponent != null){
			 	    			xComponent.dispose();
			 	    		}
			        	}
	 	        	}
	 	        	
	 	        	i=i+1;
	 	        	if(i == vPropuesta.size() && !urgencia){
		        		//logger.warn("CARGA DE URGENCIAS");
		        		//Ya no hay mas propuestas ahora se mira las urgencias
		        		
		   	    		collection = DocumentosUtil.getDocumentsByNombre(numexp, rulectx, "Propuesta Urgencia");

		  	  		  	if(collection.toList().size()!=0){
			  	  		  	urgencia = true;
			  	  		  	vPropuesta = SecretariaUtil.orderUrgencias(collection);
			  	  		  	i = 0;
		  	  		  	}
		        	}
	 	        }
    		}
    		
        return new Boolean(true);
    		
        } catch(Exception e) {
        	logger.error("No se ha podido generar las notificaciones del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar las notificaciones del expediente: " + numexp + ". " + e.getMessage(), e);
        } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
    }

	private String sacarNumExp(String descripcion) throws ISPACRuleException {
		String exp = "";
		try{
        	int pos = descripcion.indexOf(", numexp=");
        	
        	String res = descripcion.substring(pos, descripcion.length());
        	
        	descripcion = res.replaceFirst(", numexp=", "");
        	
        	exp = descripcion.substring(0, descripcion.length());
     
		}catch(Exception e)
		{
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
		return exp;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

}

