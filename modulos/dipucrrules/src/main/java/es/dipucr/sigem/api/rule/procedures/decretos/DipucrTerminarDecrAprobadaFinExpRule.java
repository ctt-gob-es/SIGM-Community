package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrTerminarDecrAprobadaFinExpRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrTerminarDecrAprobadaFinExpRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		boolean cerrarTramite = true;
		logger.warn("INICIO DipucrTerminarDecrAprobadaFinExpRule");
		try
		{
    		//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------
            
            //Obtengo los expedientes relacionados
            String consultaSQL = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
            
            //Si no tenemos prodecimiento padre no hacemos nada
            if(itemCollection != null && itemCollection.next()){
				Iterator<?> it = itemCollection.iterator();
		        IItem item = null;
		        
		        String numexp_padre = "";
	
	            String relacion="", tramitePadre = "", fasePadre= "";
		        while (it.hasNext()) {
	                item = ((IItem)it.next());
	                numexp_padre = item.getString("NUMEXP_PADRE");
	                relacion = item.getString("RELACION");

			        if(!relacion.equals(""))tramitePadre = StringUtils.split(relacion,"|")[1];
			        
			        IItemCollection itemCollectionFase = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, "WHERE NUMEXP ='"+numexp_padre+"' AND UPPER(NOMBRE) LIKE '%RESOLUCI%'");
			        if(itemCollectionFase != null && itemCollectionFase.next()) fasePadre = ((IItem)itemCollectionFase.iterator().next()).getString("ID_FASE_PCD");
			        //Ya se ha obtenido el numero de expediente relacionado
			        //Ahora con ese expediente miro si tiene expedientes relacionado y seria una propuesta
			        //Si no tuviera sería un decreto
			        IItem iExped = ExpedientesUtil.getExpediente(cct, numexp_padre);
			        String nombreProced = "";
			        if(iExped != null){
			        	nombreProced = iExped.getString("NOMBREPROCEDIMIENTO");
			        }
			        else{
			        	rulectx.setInfoMessage(Constants.MENSAJE.MENSAJERELACIONEXPEDIENTE);
			        }
			        
			        //Ahora se comprueba si en este procedimiento tiene el tramite dictamenes cerrados
			        //y así compruebo q ha sido dictaminada o certificada para ello se miran los tramites
			        //para comprobar esto se mira la tabla spac_dt_tramites y el estado=3.
			        //Notificaciones y traslado de dictamenes
			        //Certificado de acuerdos
			        consultaSQL = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND " +
			        		"(NOMBRE='"+Constants.DECRETOSTRAMITES.FIRMASTRASLADO+"') AND ESTADO=3";
			        
			        itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, consultaSQL);
		
			        it = itemCollection.iterator();
			        if(it.hasNext()){
				        while(it.hasNext()){
				        	item = ((IItem)it.next());
				        	int estado = item.getInt("ESTADO");
				        	if(estado == 3){
				        		cerrarTramite = true;
				        		insertarDocumentosNotifDecreto(cct, gendocAPI, entitiesAPI, rulectx, rulectx.getNumExp(), numexp_padre, tramitePadre, fasePadre);
				        	}
				        	else{
				        		if(nombreProced.contains(Constants.PROPUESTA.NOMBRE)){
				        			rulectx.setInfoMessage(Constants.MENSAJESECRETARIA.MENSAJEDICTAMINADO);
				        		}
				        		else{
				        			rulectx.setInfoMessage(Constants.MENSAJESDECRETOS.MENSAJEDECRETADO);
				        		}
				        	}
				        }
			        }
			        else{
			        	if(nombreProced.contains(Constants.PROPUESTA.NOMBRE)){
		        			rulectx.setInfoMessage(Constants.MENSAJESECRETARIA.MENSAJEDICTAMINADO);
		        		}
		        		else{
		        			rulectx.setInfoMessage(Constants.MENSAJESDECRETOS.MENSAJEDECRETADO);
		        		}
			        }
		        }
            }
            else{
            	cerrarTramite = true;
            }
		}catch(Exception e) {
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		logger.warn("FIN DipucrTerminarDecrAprobadaFinExpRule");
		return cerrarTramite;
	}

	private void insertarDocumentosNotifDecreto(ClientContext cct, IGenDocAPI gendocAPI, IEntitiesAPI entitiesAPI, IRuleContext rulectx, String numexp_origen, String numexpPadre, String tramitePadre, String fasePadre) throws ISPACRuleException {
		//Metodo que servira para insetar en el tramite lo documentos de notificacion del decreto y de la propuesta
		//y el certificado de dictamen, certificado de acuerdo o el decreto
		try{
	        Iterator<?> it = null;	        

			//Insertar todos los documentos
			 //Obtenemos los documentos a partir de su nombre
			List<IItem> filesConcatenate = new ArrayList<IItem>();
			
			IItemCollection documentsCollection = DocumentosUtil.getDocumentos(cct, numexp_origen, "FDOC DESC");
			
			if(documentsCollection!= null && documentsCollection.next()){
				it  = documentsCollection.iterator();
		        IItem itemDoc = null;
		        while (it.hasNext()){
		        	itemDoc = (IItem)it.next();
		        	filesConcatenate.add(itemDoc);
				}
			}	
			
			//obtenemos el tipo de documento que le vamos a poner a zip que se va a crear
			//Obtenemos el id del tipo de documento de "Contenido de la propuesta" para
	        //ponerle el nombre del documento al zip
			int idTypeDocument = DocumentosUtil.getTipoDoc(cct, Constants.TIPODOC.DOCUMENTACION_APROBACION, DocumentosUtil.BUSQUEDA_EXACTA, false);
			if (idTypeDocument != 0){
				String sExtension = "zip";
					
				// Crear el zip con los documentos
				//    http://www.manual-java.com/codigos-java/compresion-decompresion-archivos-zip-java.html
				File zipFile = DocumentosUtil.createDocumentsZipFile(gendocAPI, filesConcatenate);

				//Insertamos el número de decreto en SUBV_CONVOCATORIA
				String strQuery = "WHERE NUMEXP='"+numexpPadre+"'";
				IItemCollection colConvoc = entitiesAPI.queryEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, strQuery);
		        Iterator<?> itConvoc = colConvoc.iterator();
		        IItem convocatoria = (IItem)itConvoc.next();
		        
		        strQuery = "WHERE NUMEXP='"+numexp_origen+"'";
				IItemCollection iDecreto = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
		        Iterator<?> itDecreto = iDecreto.iterator();
		        IItem decreto = null;
		        Date fechaDecreto = null;
		        if(itDecreto != null){
		        	decreto = (IItem) itDecreto.next(); 
		            String numDecreto = decreto.getString("ANIO")+"/"+decreto.getString("NUMERO_DECRETO");
		            fechaDecreto = decreto.getDate("FECHA_DECRETO");
		            convocatoria.set("NUM_DECRETO_RESOLUCION", numDecreto);
		            convocatoria.set("FECHA_RESOLUCION", fechaDecreto);
		            convocatoria.set("NUM_DECRETO_APROBACION", numDecreto);
		            convocatoria.set("FECHA", fechaDecreto);
		            convocatoria.set("ANIO", decreto.getString("ANIO"));
		            convocatoria.set("ANIO_RESOLUCION", decreto.getString("ANIO"));
		           
		            convocatoria.store(cct);
		        }
		        int tramite=0;
				if(!tramitePadre.equals(""))tramite = Integer.parseInt(tramitePadre);
				//Creación del documento zip en base de datos.
				
		        int fasePadreInt =0;
		        if(!fasePadre.equals(""))fasePadreInt = Integer.parseInt(fasePadre);
		      //Generamos el documento zip
		        String sFase = FasesUtil.getFase(rulectx, entitiesAPI, fasePadreInt);

		        String sName = sFase+"& Numexp = "+numexp_origen;//Campo descripción del documento
				
				IItem docZip = DocumentosUtil.generaYAnexaDocumento(rulectx, tramite, idTypeDocument, sName, zipFile, sExtension);
				docZip.set("FAPROBACION", fechaDecreto);
				docZip.store(cct);
					
				if(zipFile != null && zipFile.exists()) zipFile.delete();
			}
			else{
				throw new ISPACInfo("Error al obtener el tipo de documento");
			}
	    	
		}catch(Exception e){
			throw new ISPACRuleException(e);
		}
	}
}

