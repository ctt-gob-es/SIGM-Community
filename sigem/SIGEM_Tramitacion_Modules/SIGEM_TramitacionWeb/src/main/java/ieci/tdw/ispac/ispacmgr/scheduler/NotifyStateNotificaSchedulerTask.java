package ieci.tdw.ispac.ispacmgr.scheduler;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbResultSet;
import ieci.tdw.ispac.ispaclib.resp.RespFactory;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.scheduler.SchedulerTask;
import ieci.tecdoc.sgm.core.admin.web.AdministracionHelper;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

import es.dipucr.sigem.api.rule.common.comparece.CompareceConfiguration;
import es.dipucr.sigem.api.rule.common.extra.ExtraConfiguration;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.NotificaUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


public class NotifyStateNotificaSchedulerTask
  extends SchedulerTask
{
  Map<String, String> estadosValorNotificacion;
  private static final Logger logger = Logger.getLogger(NotifyStateNotificaSchedulerTask.class);
  
  public NotifyStateNotificaSchedulerTask() {}
  

  public void run()
  {
    ClientContext context = new ClientContext();
    
    IInvesflowAPI invesFlowAPI = new InvesflowAPI(context);
    context.setAPI(invesFlowAPI);
    
    if (logger.isDebugEnabled()) {
      logger.debug("Ejecutando NotifyStateNotificaScheduler (SIGEM)");
    }
    

    List<?> entidades = AdministracionHelper.obtenerListaEntidades();
    if (logger.isInfoEnabled()) {
      logger.warn("Se han encontrado " + (entidades != null ? entidades.size() : 0) + " entidades");
    }
    
    if (!CollectionUtils.isEmpty(entidades)) {
      for (int i = 0; i < entidades.size(); i++)
      {

        Entidad entidad = (Entidad)entidades.get(i);
        if (entidad != null)
        {
          if (logger.isInfoEnabled()) {
            logger.warn("Inicio de proceso de entidad #" + (i + 1) + ": " + 
              entidad.getIdentificador() + " - " + entidad.getNombre());
          }
          

          EntidadHelper.setEntidad(entidad);
          

          execute(entidad);
          
          if (logger.isInfoEnabled()) {
            logger.warn("Fin de proceso de entidad #" + (i + 1) + ": " + 
              entidad.getIdentificador() + " - " + entidad.getNombre());
          }
        }
      }
    }
  }
  

  public void execute(Entidad entidad)
  {
    ClientContext context = new ClientContext();
    
    IInvesflowAPI invesFlowAPI = new InvesflowAPI(context);
    context.setAPI(invesFlowAPI);
    
    
    try{
		
		if(!(ConfigurationMgr.getVarGlobal(context, Constants.NOTIFICA.API_KEY_NOTIFICA).equals(""))){
    
		    try
		    {
		    
		      logger.warn("INICIO DE TAREA DE ACTUALIZACION DE ESTADOS DE NOTIFICACIONES ELECTRONICAS ID: "+ entidad.getIdentificador() + " - " + entidad.getNombre() );
		    	
		      IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		      
		
		      cargarEstadosValorNotificacion();
		      
		
		      String query = "WHERE CORREO_CADUCIDAD_ENVIADA IS NULL";
		      IItemCollection collectionDpcrAcusesNotifica = entitiesAPI.queryEntities("DPCR_ACUSES_NOTIFICA", query);
		      
		
		      actualizarEstadosValorNotificacion(context, collectionDpcrAcusesNotifica, entitiesAPI, entidad);
		      
		
		      HashMap<String, String> tramite_expediente = estanTodasLasNotificacionesDelTramitePorExpediente(context, entitiesAPI);
		      
		
		      descargarAcusesCertificaciones(context, entitiesAPI, tramite_expediente, entidad);
		    }
		    catch (Exception e)
		    {
		      try
		      {
		          String cadenaError = "Error general del proceso scheduller de Notifica, ";				 
		          cadenaError =cadenaError.concat("\nentidad: ");
		          cadenaError =cadenaError.concat(entidad.getIdentificador());
		          cadenaError =cadenaError.concat(" - ");
		          cadenaError =cadenaError.concat( entidad.getNombre());
		          cadenaError =cadenaError.concat("\nexcepcion: ");
		          cadenaError =cadenaError.concat(e.getMessage());
		  		  
		  		  String temaCorreo = "Error Notifica NotifyStateNotificaSchedulerTask --> execute ";
		  		  temaCorreo =temaCorreo.concat(entidad.getIdentificador());
		  		  temaCorreo = temaCorreo.concat(" - ");
		  		  temaCorreo =temaCorreo.concat(entidad.getNombre());	    				  		  
		          logger.error(cadenaError, e);
		          enviarCorreoError(context, new Exception(cadenaError), temaCorreo ); 
		        
		      } catch (Exception e1) {
		        logger.error("ERROR AL ENVIAR CORREO DE error del proceso scheduller de notifica");
		      }
		    }
    
    
		}
		else{
			
			logger.warn("La entidad no tiene configurado la variable de sistema API_KEY_NOTIFICA, el proceso de actualización de estado salta esta entidad");
			
		}
	
	}
    catch(Exception e){
    	
    	logger.error("La entidad no tiene configurado la variable de sistema API_KEY_NOTIFICA");
    	
    }
  }

  private ClientContext asignarResponsableContexto(ClientContext context, String responsable)
    throws ISPACException
  {
    Responsible responsible = RespFactory.createResponsible(responsable);
    context.setUser(responsible);
    return context;
  }
  

  public void actualizarEstadosValorNotificacion(ClientContext context, IItemCollection collection, IEntitiesAPI entitiesAPI, Entidad entidad)
    throws Exception
  {
	  
	logger.warn("INICIO - " + getClass().getName());
	  
    String estadoNotificacion = "";
    String estadoVarloNotificacion = "";
    IItem docAux = null;
    IItem dpcrAcusesNotifica = null;
    String idNotificacion = "";
    String idDocumento = "";
    
    try
    {
      while (collection.next())
      {
        context.beginTX();
        dpcrAcusesNotifica = collection.value();
        idNotificacion = dpcrAcusesNotifica.getString("ID_NOTIFICACION");
        idDocumento = dpcrAcusesNotifica.getString("IDENT_DOC");
        if (StringUtils.isNotEmpty(idNotificacion))
        {

          NotificaUtil nu = new NotificaUtil(ConfigurationMgr.getVarGlobal(context, "API_KEY_NOTIFICA"));
          
          try
          {
        	  	//LLAMADA AL SERVICIO WEB
        	  	estadoNotificacion = nu.notifica_consultaEstado_dameEstado(idNotificacion);
          }
          catch (Exception e)
          {
	              String cadenaError = "Error en el proceso scheduller de notificaciones(notifica_consultaEstado_dameEstado)";
	              cadenaError =cadenaError.concat("\nidNotificacion: ");
	              cadenaError =cadenaError.concat(idNotificacion);
	              cadenaError =cadenaError.concat("\nentidad actual es: ");
	              cadenaError =cadenaError.concat(entidad.getIdentificador());
	              cadenaError =cadenaError.concat(" - ");
	              cadenaError =cadenaError.concat( entidad.getNombre());
	              cadenaError =cadenaError.concat("\nexcepcion: ");
	              cadenaError =cadenaError.concat(e.getMessage());
		  		  
		          String temaCorreo = "Error Notifica consultaCertificacion ";
		          temaCorreo =temaCorreo.concat(entidad.getIdentificador());
		          temaCorreo =temaCorreo.concat(" - ");
		          temaCorreo =temaCorreo.concat(idNotificacion);	    				  		  
		          logger.error(cadenaError, e);
		          enviarCorreoError(context, new Exception(cadenaError), temaCorreo);
          }
          

          estadoVarloNotificacion = validarEstadosValorNotificacion(estadoNotificacion);
          if (estadoVarloNotificacion == null) {
            throw new Exception("Error del proceso scheduller de notificaciones, método actualizarEstadosValorNotificacion, estado incorrecto para la notificación: " + idNotificacion);
          }
          if (estadoNotificacion != null) 
          {
            docAux = DocumentosUtil.getDocumento(entitiesAPI, Integer.parseInt(idDocumento));
            docAux.set("ESTADONOTIFICACION", estadoVarloNotificacion);
            dpcrAcusesNotifica.set("ESTADO", estadoVarloNotificacion);
            
            if ((estadoVarloNotificacion.equals(Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_NOTIFICADA)))
            {
              docAux.set("FNOTIFICACION", Calendar.getInstance().getTime());
            }
            else if(estadoVarloNotificacion.equals(Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_PENDIENTE_DE_COMPARECENCIA) || (estadoVarloNotificacion.equals(Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_REHUSADA)) ){
            	
            	if(haCaducado(docAux)){
            		String textoAviso = "Ha caducado notificacion sIdent_doc= ";
            		docAux.set("ESTADONOTIFICACION", Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_CADUCADA);
                    dpcrAcusesNotifica.set("ESTADO", Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_CADUCADA);
                    textoAviso.concat(idDocumento);
                    logger.warn(textoAviso);
                    dpcrAcusesNotifica.set("CORREO_CADUCIDAD_ENVIADA", 1);
                    dpcrAcusesNotifica.store(context);
  	              	logger.warn("después de actualizar el estado de la notificación");
  	              	
  	                String cadenaError = "Error en el proceso scheduller de notificaciones(se ha llegado al limite de intentos de descarga de certificado)";
  	                cadenaError = cadenaError.concat("\nidNotificacion: ");
  	                cadenaError = cadenaError.concat(idNotificacion);
  	                cadenaError = cadenaError.concat("\nentidad actual es: ");
  	                cadenaError = cadenaError.concat(entidad.getIdentificador());
  	                cadenaError = cadenaError.concat(" - ");
  	                cadenaError = cadenaError.concat( entidad.getNombre());
		  		  
		            String temaCorreo = "Error Notifica, se llegó al limite de intentos de descarga ";
		            temaCorreo = temaCorreo.concat(entidad.getIdentificador());
		            temaCorreo = temaCorreo.concat(" - ");
		            temaCorreo = temaCorreo.concat(idNotificacion);	    				  		  
		            logger.warn(cadenaError);
		            enviarCorreoError(context, new Exception(cadenaError), temaCorreo);
            	}
            }
          }
          else
          {
            logger.error("Error del proceso scheduller de notificaciones, método actualizarEstadosValorNotificacion, no existe en Notifica la notificación con id: " + idNotificacion);
          }
        }
        dpcrAcusesNotifica.store(context);
        docAux.store(context);
        context.endTX(true);
      }
    }
    catch (Exception e)
    {
    	String cadenaError = "Error en el proceso scheduller de notificaciones(actualizando estados, posible error en entidades de sigem)";
    	  cadenaError = cadenaError.concat("\nidNotificacion: ");
    	  cadenaError = cadenaError.concat(idNotificacion);
    	  cadenaError = cadenaError.concat("\nentidad actual es: ");
    	  cadenaError = cadenaError.concat(entidad.getIdentificador());
    	  cadenaError = cadenaError.concat(" - ");
    	  cadenaError = cadenaError.concat( entidad.getNombre());
    	  cadenaError = cadenaError.concat("\nexcepcion: ");
    	  cadenaError = cadenaError.concat(e.getMessage());
		  
        String temaCorreo = "Error Notifica actualizando estados bbdd sigem ";
          temaCorreo =temaCorreo.concat(entidad.getIdentificador());
          temaCorreo =temaCorreo.concat(" - ");
          temaCorreo =temaCorreo.concat(idNotificacion);	    				  		  
        logger.error(cadenaError, e);      
        enviarCorreoError(context, new Exception(cadenaError), temaCorreo);
    }
  }
  
  public boolean haCaducado(IItem documento) throws ISPACException, ParseException{
	  
	  boolean result = false;
	  Calendar cal_actual = Calendar.getInstance();	 
	  
	  String fecha_registro = documento.getString("FREG");
	  DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	  Date date_reg = format.parse(fecha_registro);
	  
	  Calendar cal_registro = Calendar.getInstance();	
	  cal_registro.setTime(date_reg);
	  cal_registro.add(Calendar.DAY_OF_YEAR, 14);
	  	  
	  if(cal_actual.after(cal_registro))
		  result = true;
	  
	  return result;
	  
  }
  
  
  public void cargarEstadosValorNotificacion()
  {
	  
    estadosValorNotificacion = new HashMap<String, String>();
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_PENDIENTE, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_PENDIENTE);
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_EN_PROCESO, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_EN_PROCESO);//("En Proceso", "PR");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_OK, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_OK);//("Finalizada", "OK");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_CADUCADA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_CADUCADA);//("Caducada", "CA");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_RECHAZADA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_RECHAZADA);//("Rechazada", "RE");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_ERROR, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_ERROR);//("Error", "ER");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_COMPARECE, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_COMPARECE);//("Enviada a Comparece", "CO");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA);//("Enviada a Notifica", "NT");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_AUSENTE, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_AUSENTE);//("Ausente", "NTAUSENT");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_DESCONOCIDO, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_DESCONOCIDO);//("Desconocido", "NTDESCON");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_DIRECCION_INCORRECTA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_DIRECCION_INCORRECTA);//("Dirección incorrecta", "NTDIERRO");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_ENVIO_AL_CENTRO_DE_IMPRESION, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_ENVIO_AL_CENTRO_DE_IMPRESION);//("Enviado al centro de impresión", "NTENVIMP");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_ENVIO_A_LA_DEH, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_ENVIO_A_LA_DEH);//("Enviado a la DEH", "NTENVDEH");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_LEIDA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_LEIDA);//("Leída", "NTILEIDA");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_ERROR, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_ERROR);//("Error en el envío", "NTIERROR");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_EXTRAVIADA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_EXTRAVIADA);//("Extraviada", "NTEXTRAV");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_FALLECIDO, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_FALLECIDO);//("Fallecido", "NTFALLEC");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_NOTIFICADA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_NOTIFICADA);//("Notificada", "NTIFCAOK");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_PENDIENTE_DE_ENVIO, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_PENDIENTE_DE_ENVIO);//("Pendiente de envío", "NTPENENV");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_PENDIENTE_DE_COMPARECENCIA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_PENDIENTE_DE_COMPARECENCIA);//("Pendiente de comparecencia", "NTPENCOM");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_REHUSADA, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_REHUSADA);//("Rehusada", "NTREHUSA");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_FECHA_ENVIO_PROGRAMADO, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_FECHA_ENVIO_PROGRAMADO);//("Fecha envío programado", "NTWNVPRO");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_SIN_INFORMACION, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_SIN_INFORMACION);//("Sin información", "NTSININF");
    estadosValorNotificacion.put(Constants.NOTIFICACIONES_ESTADOS.ESTADO_NOTIFICA_ENVIO_POSTAL, Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_ENVIO_POSTAL);//("Envío postal", "NTPOSTAL");
  
  }
  


  public String validarEstadosValorNotificacion(String estado)
    throws Exception
  {
    try
    {
      return (String)estadosValorNotificacion.get(estado);
    }
    catch (Exception e) {
      String cadenaError = "Error del proceso scheduller de notificaciones, no exite valor para el estado: " + estado + "\n" + e.getMessage();
      throw new Exception(cadenaError);
    }
  }
  
  public String validarEstadosNotificacion(String estado)
	  throws Exception
 {
	   try
	   {
		 if(estado.equals(Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_REHUSADA))
			 return "Notificación rehusada, comprobar si el certificado de acuse de recibo es de rechazo automático o la ha rehusado el destinatario.";
		 else  return "Notificación no abierta en Notifica, valorar el envío postal.";
	   }
	   catch (Exception e) {
	      String cadenaError = "Error del proceso scheduller de notificaciones, no exite valor para el estado: " + estado + "\n" + e.getMessage();
	   throw new Exception(cadenaError);
	   }
  }


  public HashMap<String, String> estanTodasLasNotificacionesDelTramitePorExpediente(ClientContext context, IEntitiesAPI entitiesAPI) throws Exception {
	logger.warn("INICIO - " + getClass().getName());  
	  
    String idTramite = "";
    String numexp = "";
    String id_responsable = "";
    String id_proc = "";
    HashMap<String, String> tramite_expediente = new HashMap<String, String>();
    Vector<ItemBean> valores = new Vector<ItemBean>();
    
    String query = "SELECT A.NUMEXP, A.TRAMITE, A.CORREO_CADUCIDAD_ENVIADA, A.ID_RESPONSABLE, A.ID_PROC FROM DPCR_ACUSES_NOTIFICA AS A WHERE A.CORREO_CADUCIDAD_ENVIADA IS NULL GROUP BY NUMEXP, TRAMITE, CORREO_CADUCIDAD_ENVIADA, ID_RESPONSABLE, ID_PROC HAVING (SELECT COUNT(*) FROM DPCR_ACUSES_NOTIFICA WHERE TRAMITE = A.TRAMITE)-(SELECT COUNT(*) FROM DPCR_ACUSES_NOTIFICA WHERE TRAMITE = A.TRAMITE AND (ESTADO LIKE 'NTIFCAOK' OR ESTADO LIKE 'NTREHUSA' OR ESTADO LIKE 'NTPENENV' OR ESTADO LIKE 'NTCADUCA') ) = 0 ORDER BY NUMEXP, TRAMITE";
    
    DbCnt conn = null;
	DbResultSet rs = null;
	ResultSet datos = null;
	
	conn = context.getConnection();
	rs = conn.executeQuery(query);
	
	try{
		datos = rs.getResultSet();
	    if (datos != null) {
	      while (datos.next()){
	        ItemBean itemB = new ItemBean();
	        if ((datos.getString("TRAMITE") != null) && (datos.getString("NUMEXP") != null) && (datos.getString("ID_RESPONSABLE") != null) && (datos.getString("ID_PROC") != null)) {
	          itemB.setProperty("TRAMITE", datos.getString("TRAMITE"));
	          itemB.setProperty("NUMEXP", datos.getString("NUMEXP"));
	          itemB.setProperty("ID_RESPONSABLE", datos.getString("ID_RESPONSABLE"));
	          itemB.setProperty("ID_PROC", datos.getString("ID_PROC"));
	          valores.add(itemB);
	        } else {
	          throw new Exception("Error en el proceso scheduller de notificaciones, error al consultar los campos tramite, numexp, idresponsableo id_proc de DPCR_ACUSES_NOTIFICA");
	        }
	      }
	    } else {
	      throw new Exception("Error en el proceso scheduller de notificaciones, error al consultar el estado de las notificaciones en la entidad DPCR_ACUSES_NOTIFICA");
	    }
	} finally {
		if(null!= datos){
			datos.close();
		}
		if (null != rs) {
			rs.close();
		}
		
		if(null != conn){
			context.releaseConnection(conn);
		}
	}
	
	for (int i = 0; i < valores.size(); i++){
	      context.beginTX();
	      idTramite = ((ItemBean)valores.get(i)).getString("TRAMITE");
	      numexp = ((ItemBean)valores.get(i)).getString("NUMEXP");
	      //id_responsable = ((ItemBean)valores.get(i)).getString("ID_RESPONSABLE");
	      //id_proc = ((ItemBean)valores.get(i)).getString("ID_PROC");
	      tramite_expediente.put(idTramite, numexp);
	   // Enviar aviso para que cierren el tramite, lo quito de aqui, lo agrego cuando se hayan descargado correctamente los acuses de recibo    
//	      if (!enviadoAvisoDeNotificaATramite(context, entitiesAPI, Integer.parseInt(idTramite), numexp, id_responsable, id_proc)) {
//	        enviarAvisoDeNotificaATramite(context, entitiesAPI, Integer.parseInt(idTramite), numexp, id_responsable, id_proc);
//	      }
	      context.endTX(true);
	}    
    return tramite_expediente;
    
  }
  











  public boolean enviadoAvisoDeNotificaATramite(ClientContext context, IEntitiesAPI entitiesAPI, int idTramite, String numexp, String id_responsable, String id_proc) throws Exception {
	  String query = "SELECT * FROM spac_avisos_electronicos WHERE id_expediente ='" + numexp + "' AND mensaje like 'Expediente: " + numexp + " Tramite: " + idTramite + "%Aviso de NOTIFICA%'";
	  
	  DbCnt conn = null;
	  DbResultSet rs = null;
	  ResultSet datos = null;
	  
	  try{
		  conn = context.getConnection();
		  rs = conn.executeQuery(query);
		  
		  datos = rs.getResultSet();
		  
		  if (datos != null) {
			  if (datos.next()) {
				  return true;
			  }
			  return false;
		  }		  
	  } finally {
		  if (null != rs) {
			  rs.close();
		  }
		  if(null != conn){
			  context.releaseConnection(conn);
		  }
		  if(null != datos){
			  datos.close();
		  }
	   }
	  throw new Exception("Error en el proceso scheduller de notificaciones, error al consultar el estado de las notificaciones en la entidad DPCR_ACUSES_NOTIFICA");
  	}
  

  public boolean estanTodasLasNotificacionesConAcuseDeReciboDescargado(ClientContext context, String idTramite) throws Exception{
	  DbCnt conn = null;
	  DbResultSet rs = null;
	  ResultSet datos = null;
	  
	  try{
		  String query = "select * from dpcr_acuses_notifica where tramite = " + idTramite + " and (correo_caducidad_enviada is null or correo_caducidad_enviada <> 1)";
		  
		  conn = context.getConnection();
		  rs = conn.executeQuery(query);
		  
		  datos = rs.getResultSet();
		  
		  if (datos != null) {
		      if (datos.next()) {
		         return false;
		      }		      
		      return true;
		  }
		  throw new Exception("Error en el proceso scheduller de notificaciones, error al consultar el estado de los acuses de recibo en la entidad DPCR_ACUSES_NOTIFICA");
	  } finally {
		  if (null != rs) {
			  rs.close();
		  }		  
		  if(null != conn){
			  context.releaseConnection(conn);
		  }
		  if (null != datos) {
			  datos.close();
		  }
	  }
  }



  public void enviarAvisoDeNotificaATramite(ClientContext context, IEntitiesAPI entitiesAPI, int idTramite, String numexp, String id_responsable, String id_proc)
    throws Exception
  {
    String message = "Expediente: " + numexp + " Tramite: " + idTramite + ", Aviso de NOTIFICA todos los interesados notificados o pendientes de envio postal. Ya puede terminar trámite de notificaciones.";
    AvisosUtil.generarAvisoAtramite(entitiesAPI, Integer.parseInt(id_proc), idTramite, numexp, message, id_responsable, context);
  }
  


  private void enviarCorreoError(IClientContext cct, Exception ex, String asunto){
    try
    {
      ExtraConfiguration extraConf = ExtraConfiguration.getInstance(cct);
      
      String strTo = extraConf.get("notifica.mail_error.to");
      //String strAsunto = extraConf.get("notifica.mail_error.subject");
      String strAsunto = asunto;
      
      String strContenido = extraConf.get("notifica.mail_error.content");
      HashMap<String, String> variables = new HashMap<String, String>();
      variables.put("EXCEPCION", ex.getMessage());
      strContenido = StringUtils.replaceVariables(strContenido, variables);
      
      MailUtil.enviarCorreoInfoTask(cct, strTo, strAsunto, strContenido);
    }
    catch (Exception ex1) {
      logger.error("Tarea de actualización de estados Notifica: Se produjo una excepción al enviar el correo de error", ex1);
    }
  }
  

  public void descargarAcusesCertificaciones(ClientContext cct, IEntitiesAPI entitiesAPI, HashMap<String, String> tramite_expediente, Entidad entidad_)throws Exception {
    logger.warn("INICIO - " + getClass().getName());
    
    ArrayList<FileInputStream> inputStreamNotificaciones = null;
    ArrayList<String> filePathNotificaciones = new ArrayList<String>();
    Document documentNotifica = null;
    File fileNotificaNombre = null;
    boolean error = false;
    
    Font fuenteTitulo = new Font(Font.TIMES_ROMAN);
	fuenteTitulo.setStyle(Font.BOLD);
	fuenteTitulo.setSize(15);

	Font fuenteDocumento = new Font(Font.TIMES_ROMAN);
	fuenteDocumento.setStyle(Font.BOLD);


    String entidad = "";
    String imageLogoPath = "";
    String imagePiePath = "";
    String imageFondoPath ="";
    int idTipDoc = 0;
    int idTipDocNotifica = 0;
    int idPlantilla = 0;
    boolean tieneParticipantesNotifica = false;
    String sIdent_doc = "";
    IItem iAcuse = null;
    Vector<String> vIdDoc = new Vector<String>();

    try
    {
      if ((tramite_expediente != null) && (tramite_expediente.size() > 0))
      {

        IInvesflowAPI invesFlowAPI = cct.getAPI();
        entitiesAPI = invesFlowAPI.getEntitiesAPI();
        IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
        

        entidad = EntidadesAdmUtil.obtenerEntidad(cct);
        
        idTipDoc = DocumentosUtil.getTipoDoc(cct, "Documentos Sellados", "=", false);
        


        String consulta = "SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='Documentos Sellados' AND ID_TPDOC=" + idTipDoc + ";";
        
        DbCnt conn = cct.getConnection();
        DbResultSet rs = conn.executeQuery(consulta);
        ResultSet planDocIterator = rs.getResultSet();
        
        if (planDocIterator.next()) { 
        	idPlantilla = planDocIterator.getInt("ID");
        }
        if(null != planDocIterator) {
        	planDocIterator.close();
        }
        if(null != rs){
        	rs.close();
        }
        if(null != conn){
        	cct.releaseConnection(conn);
        }


        tieneParticipantesNotifica = false;
        
        idTipDocNotifica = DocumentosUtil.getTipoDoc(cct, "Acuse Notifica", "=", false);
        
        Iterator<?> iterator_tram_exp = tramite_expediente.entrySet().iterator();        

        while (iterator_tram_exp.hasNext()) {
        	documentNotifica = new Document();
        	inputStreamNotificaciones = new ArrayList<FileInputStream>();
        	
         fileNotificaNombre = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + File.separator + FileTemporaryManager.getInstance().newFileName(".pdf"));
            
         PdfCopy.getInstance(documentNotifica, new FileOutputStream(fileNotificaNombre));
         documentNotifica.open();
            
         imageLogoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath(new StringBuilder("skinEntidad_").append(entidad).append(File.separator).toString(), "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty("imagen_cabecera");
         imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_FONDO_PATH_DIPUCR);
    	 imagePiePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_PIE_PATH_DIPUCR);

    	 File logoURL = new File(imageLogoPath);
    	 if (logoURL != null) {
    			Image logo = Image.getInstance(imageLogoPath);
    			logo.scalePercent(50);
    			documentNotifica.add(logo);
    	  }
    		
    	  File fondoURL = new File(imageFondoPath);
    	  if(fondoURL != null){
    			Image fondo = Image.getInstance(imageFondoPath);
    			fondo.setAbsolutePosition(250, 50);
    			fondo.scalePercent(70);
    			documentNotifica.add(fondo);
    			
    	  }
            
            
          File pieURL = new File(imagePiePath);
          if (pieURL != null) {
              Image pie = Image.getInstance(imagePiePath);
              pie.setAbsolutePosition(documentNotifica.getPageSize().getWidth() - 550.0F, 15.0F);
              pie.scalePercent(80.0F);
              documentNotifica.add(pie);
          }
            
          documentNotifica.add(new Phrase("\n\n"));
    	  Paragraph titulo = new Paragraph(new Phrase("PARTICIPANTES QUE NO HAN LEIDO O HAN REHUSADO LA NOTIFICACIÓN EN NOTIFICA", fuenteTitulo));
    	  titulo.setAlignment(Element.ALIGN_CENTER);
    	  documentNotifica.add(titulo);
    	  documentNotifica.add(new Phrase("\n"));        	

          Map.Entry<String, String> exp_tram = (Map.Entry)iterator_tram_exp.next();
          
          iterator_tram_exp.remove();
          
          String strQuery = "WHERE NUMEXP='" + (String)exp_tram.getValue() + "' AND TRAMITE=" + (String)exp_tram.getKey();
          IItemCollection collection = entitiesAPI.queryEntities("DPCR_ACUSES_NOTIFICA", strQuery);
          Iterator<IItem> it_acuses_notifica = collection.iterator();
          
          sIdent_doc = "";
          vIdDoc = new Vector<String>();
          

          while (it_acuses_notifica.hasNext())
          {

            iAcuse = (IItem)it_acuses_notifica.next();
            
            it_acuses_notifica.remove();
            
            if (iAcuse.getString("IDENT_DOC") != null) {
              sIdent_doc = iAcuse.getString("IDENT_DOC");
            } else {
              sIdent_doc = "";
            }
            if (!vIdDoc.contains(sIdent_doc)) {
              vIdDoc.add(sIdent_doc);
              String estado = iAcuse.getString("ESTADO");
              cct = asignarResponsableContexto(cct, iAcuse.getString("ID_RESPONSABLE"));
              
              IItem itemDocument = DocumentosUtil.getDocumento(entitiesAPI,Integer.parseInt(sIdent_doc));
              
              if ((estado.equals(Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_NOTIFICADA)))
              {
                NotificaUtil nu = new NotificaUtil(ConfigurationMgr.getVarGlobal(cct, "API_KEY_NOTIFICA"));
                byte[] contenido = null;
          	 
                logger.warn("voy a descargar el acuse de la notificacion notificada: "+ iAcuse.getString("ID_NOTIFICACION") + " con estado: " + estado);
	      		  try{
	    		      //LLAMADA AL SERVICIO WEB
	    			  contenido = nu.notifica_consultaCertificacionEnvio_contenido(iAcuse.getString("ID_NOTIFICACION"));
	    			  
	    		  }
	    		  catch(Exception e){
	    			  
	    			  	  error = true;

	    				  String cadenaError = "Error en el proceso scheduller de notificaciones(notifica_consultaCertificacionEnvio_contenido),";
	    						  cadenaError.concat("\nestado: NOTIFICADO OK");
	    				  		  cadenaError.concat("\nidNotificacion: ");
	    				  		  cadenaError.concat(iAcuse.getString("ID_NOTIFICACION"));
	    				  		  cadenaError.concat("\nentidad actual es: ");
	    				  		  cadenaError.concat(entidad_.getIdentificador());
	    				  		  cadenaError.concat(" - ");
	    				  		  cadenaError.concat( entidad_.getNombre());
	    				  		  cadenaError.concat("\nexcepcion: ");
	    				  		  cadenaError.concat(e.getMessage());
	    				  		  
	    				  String temaCorreo = "Error Notifica consultaCertificacion ";
	    				  		  temaCorreo.concat(entidad_.getIdentificador());
	    				  		  temaCorreo.concat(" - ");
	    				  		  temaCorreo.concat(iAcuse.getString("ID_NOTIFICACION"));	    				  		  
	    				          logger.error(cadenaError, e);
	    				          //enviarCorreoError(cct, new Exception(cadenaError), temaCorreo );  
	    				  
	    			  
	    		  }
               //Recuperacion del certificado de Notifica
                if (contenido != null)
                {
	                  String nombre = iAcuse.getString("ID_NOTIFICACION");
	                  int tramite = Integer.valueOf(iAcuse.getString("TRAMITE")).intValue();
	                  File file = new File(nombre);
	                  
	                  FileOutputStream fos = new FileOutputStream(file);
	                  fos.write(contenido);
	                  fos.close();
	                  
	                  String descripcion_documento = "";
	                  if (itemDocument.getString("DESTINO") != null)
	                    descripcion_documento = itemDocument.getString("DESCRIPCION").concat(" - certificado de Notifica"); else {
	                    throw new ISPACRuleException("El identificador de documento:" + sIdent_doc + " ya no existe en la base de datos, borrar de los envíos de Notifica");
	                  }
	                  IItem newdoc = genDocAPI.createTaskDocument(tramite, idTipDocNotifica);
	                  
	                  int docId = newdoc.getInt("ID");
	                  logger.warn("Hago transacción de documento auxiliar vacio en spac_dt_documentos docId=" + docId);
	                  newdoc.store(cct);
	                  logger.warn("Antes de pasar el documento a SIGEM: docId=" + docId);
	                  logger.warn("Antes de pasar el documento a SIGEM: tramite=" + tramite);
	                  IItem entityDocument1 = DocumentosUtil.anexaDocumento(cct, genDocAPI, tramite, docId, file, "pdf", descripcion_documento);
	                  logger.warn("Después de pasar el documento a SIGEM: entityDocument1.getKeyInteger()=" + entityDocument1.getKeyInteger());
	
	                  logger.warn("Después de realizarentityDocument1.store(cct);");
	                  file.delete();
	 
	                  logger.warn("Vamos a actualizar el estado de la notificación sIdent_doc=" + sIdent_doc);
	                  iAcuse.set("CORREO_CADUCIDAD_ENVIADA", 1);
	                  iAcuse.store(cct);
	                  logger.warn("Después de actualizar el estado de la notificación");
                  
                }
                

              }
              
              //La notificacion ha sido rehusada por consentimiento expreso del destinatario o ha caducado
              else
              {
            	  byte[] contenido = null;
            	  NotificaUtil nu = null;
            	  
            	  if ((estado.equals(Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_NOTIFICA_REHUSADA))){            	  
            		  nu = new NotificaUtil(ConfigurationMgr.getVarGlobal(cct, "API_KEY_NOTIFICA"));
            		  logger.warn("voy a descargar el acuse de la notificacion rehusada: "+ iAcuse.getString("ID_NOTIFICACION") + " con estado: " + estado);
            		  
            		  try{
            		      //LLAMADA AL SERVICIO WEB
            			  contenido = nu.notifica_consultaCertificacionEnvio_contenido(iAcuse.getString("ID_NOTIFICACION"));
            			  
            		  }
            		  catch(Exception e){
            			  
            			  try
            		      {
            				  error=true;
            				  
            				  String cadenaError = "Error en el proceso scheduller de notificaciones(notifica_consultaCertificacionEnvio_contenido),";
    						  cadenaError.concat("\nestado: REHUSADA");
    				  		  cadenaError.concat("\nidNotificacion: ");
    				  		  cadenaError.concat(iAcuse.getString("ID_NOTIFICACION"));
    				  		  cadenaError.concat("\nentidad actual es: ");
    				  		  cadenaError.concat(entidad_.getIdentificador());
    				  		  cadenaError.concat(" - ");
    				  		  cadenaError.concat( entidad_.getNombre());
    				  		  cadenaError.concat("\nexcepcion: ");
    				  		  cadenaError.concat(e.getMessage());
    				  		  
    				  		  String temaCorreo = "Error Notifica consultaCertificacion\n";
    				  		  temaCorreo.concat(entidad_.getIdentificador());
    				  		  temaCorreo.concat("-");
    				  		  temaCorreo.concat(iAcuse.getString("ID_NOTIFICACION"));	    				  		  
    				          logger.error(cadenaError, e);
    				          
    				          //enviarCorreoError(cct, new Exception(cadenaError), temaCorreo ); 
            		      }catch (Exception e1)
            			  {
            		    	  error=true;
            		    	  logger.error("ERROR AL ENVIAR CORREO DE error del proceso scheduller de notifica");
            		      }
            			  
            		  }
            	              	         
	                  //Recuperacion del certificado de Notifica
	                  if (contenido != null)
	                  {
	                    String nombre = iAcuse.getString("ID_NOTIFICACION");
	                    int tramite = Integer.valueOf(iAcuse.getString("TRAMITE")).intValue();
	                    File file = new File(nombre);
	                    
	                    FileOutputStream fos = new FileOutputStream(file);
	                    fos.write(contenido);
	                    fos.close();
	                    
	                    String descripcion_documento = "";
	                    if (itemDocument.getString("DESTINO") != null)
	                      descripcion_documento = itemDocument.getString("DESCRIPCION").concat(" - certificado de Notifica"); else {
	                      throw new ISPACRuleException("El identificador de documento:" + sIdent_doc + " ya no existe en la base de datos, borrar de los envíos de Notifica");
	                    }
	                    IItem newdoc = genDocAPI.createTaskDocument(tramite, idTipDocNotifica);
	                    
	                    int docId = newdoc.getInt("ID");
	                    logger.warn("Hago transacción de documento auxiliar vacio en spac_dt_dcoumetos docId=" + docId);
	                    newdoc.store(cct);
	                    logger.warn("Antes de pasar el documento a SIGEM: docId=" + docId);
	                    logger.warn("Antes de pasar el documento a SIGEM: tramite=" + tramite);
	                    IItem entityDocument1 = DocumentosUtil.anexaDocumento(cct, genDocAPI, tramite, docId, file, "pdf", descripcion_documento);
	                    logger.warn("Después de pasar el documento a SIGEM: entityDocument1.getKeyInteger()=" + entityDocument1.getKeyInteger());
	
	                    logger.warn("Después de realizarentityDocument1.store(cct);");
	                    file.delete();
	                    
	                    logger.warn("Vamos a actualizar el estado de la notificación sIdent_doc=" + sIdent_doc);
		  	            iAcuse.set("CORREO_CADUCIDAD_ENVIADA", 1);
		  	            iAcuse.store(cct);
		  	            logger.warn("Después de actualizar el estado de la notificación");
	                   
	                  }    
            	  }
            	  
                  
	              String destino = "";
	              if (itemDocument.getString("DESTINO") != null)
	                  destino = itemDocument.getString("DESTINO"); 
	              else
	                  throw new ISPACRuleException("El identificador de documento:" + sIdent_doc + " ya no existe en la base de datos, borrar de los envíos de Notifica");
	              
	              documentNotifica.add(new Phrase("- " + destino + "- " + validarEstadosNotificacion(estado)));
	              documentNotifica.add(new Phrase("\n"));
	              tieneParticipantesNotifica = true;
	              String infoPagRDE = "";
	              String infoPag = "";
	              if (itemDocument.getString("INFOPAG_RDE") != null) infoPagRDE = itemDocument.getString("INFOPAG_RDE"); else infoPagRDE = "";
	              if (itemDocument.getString("INFOPAG") != null) infoPag = itemDocument.getString("INFOPAG"); else infoPag = "";
	              String tipoRegistro = "";
	              if (itemDocument.getString("TP_REG") != null) tipoRegistro = itemDocument.getString("TP_REG"); else tipoRegistro = "";
	              String numRegistro = "";
	              if (itemDocument.getString("NREG") != null) numRegistro = itemDocument.getString("NREG"); else numRegistro = "";
	              String fechaRegistro = "";
	              if (itemDocument.getString("FREG") != null) fechaRegistro = itemDocument.getString("FREG"); else fechaRegistro = "";
	              String departamento = "";
	              if (itemDocument.getString("ORIGEN") != null) departamento = itemDocument.getString("ORIGEN"); else { departamento = "";
	              }
	              
	              File file = null;
	              if (StringUtils.isNotBlank(infoPagRDE)) {
	                file = DocumentosUtil.getFile(cct, infoPagRDE, null, null);
	              } else {
	                file = DocumentosUtil.getFile(cct, infoPag, null, null);
	              }
	                
	              if(null!=file){
	              
		              String pathFileTemp = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
		                
		              addGrayBand(file, pathFileTemp, infoPagRDE, tipoRegistro, numRegistro, fechaRegistro, departamento, entidad);
		               
		              String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
		                
		              File fileSello = new File(rutaFileName);
		              FileInputStream fis = new FileInputStream(fileSello);
		                
		              inputStreamNotificaciones.add(fis);
		              filePathNotificaciones.add(rutaFileName); 
	              
	              }           
	
	          
              }
	              
            }
          }
          
          if (estanTodasLasNotificacionesConAcuseDeReciboDescargado(cct, exp_tram.getKey())){
        	  
        	  if(null != iAcuse){
        		  
	        	  if (!enviadoAvisoDeNotificaATramite(cct, entitiesAPI, Integer.parseInt(exp_tram.getKey()), (String)exp_tram.getValue(), iAcuse.getString("ID_RESPONSABLE"), iAcuse.getString("ID_PROC"))) {
	        		  
	        			enviarAvisoDeNotificaATramite(cct, entitiesAPI, Integer.parseInt(exp_tram.getKey()), (String)exp_tram.getValue(), iAcuse.getString("ID_RESPONSABLE"), iAcuse.getString("ID_PROC"));
	        			
	        	  }
	        	  
        	  }
        	  
          }
          
          
          documentNotifica.close();
          
          //Control para que solo se generen una vez los documentos sellados
          if(!error){
          
		          File fileConcatenado = concatenaPdf(inputStreamNotificaciones, filePathNotificaciones, genDocAPI);
		          
		          if (fileConcatenado != null) {
		            IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(cct, Integer.parseInt((String)exp_tram.getKey()), idTipDoc, "Notificación usuarios caducados o rehusados Notifica", fileConcatenado, "pdf");
		            
		            entityDocument.set("ID_PLANTILLA", idPlantilla);
		            entityDocument.store(cct);
		          }
		          
		          if ((fileNotificaNombre != null) && (tieneParticipantesNotifica)) {
		            IItem entityDocumentListado = DocumentosUtil.generaYAnexaDocumento(cct, Integer.parseInt((String)exp_tram.getKey()), idTipDoc, "Listado usuarios caducados o rehusados Notifica", fileNotificaNombre, "pdf");
		            
		            entityDocumentListado.set("ID_PLANTILLA", idPlantilla);
		            entityDocumentListado.store(cct);
		
		          }
          
          }
          

        }
        
        
        

      }
      


    }
    catch (ISPACException e)
    {

      logger.error("Error1. " + e.getMessage(), e);
      throw new ISPACRuleException("Error. " + e.getMessage(), e);
    } catch (NumberFormatException e) {
      logger.error("Error2. " + e.getMessage(), e);
      throw new ISPACRuleException("Error. " + e.getMessage(), e);
    } catch (RemoteException e) {
      logger.error("Error3. " + e.getMessage(), e);
      throw new ISPACRuleException("Error. " + e.getMessage(), e);
    } catch (IOException e) {
      logger.error("Error4. " + e.getMessage(), e);
      throw new ISPACRuleException("Error. " + e.getMessage(), e);
    } catch (SQLException e) {
      logger.error("Error5. " + e.getMessage(), e);
      throw new ISPACRuleException("Error. " + e.getMessage(), e);
    } catch (DocumentException e) {
      logger.error("Error6. " + e.getMessage(), e);
      throw new ISPACRuleException("Error. " + e.getMessage(), e);
    } catch (Exception e) {
      logger.error("Error7. " + e.getMessage(), e);
      throw new ISPACRuleException("Error. " + e.getMessage(), e);
    }
    finally {
      try {
    	
    	if(null!=documentNotifica){
    		
	    	if(documentNotifica.isOpen())
	    		documentNotifica.close();
    	}
        
    	if(null!=inputStreamNotificaciones){
    	
	        for (int j = 0; j < inputStreamNotificaciones.size(); j++) {
	           ((FileInputStream)inputStreamNotificaciones.get(j)).close();
	        }
        
    	}
        
        System.gc();
      }
      catch (IOException e)
      {
        logger.error("Error finally " + e.getMessage(), e);
        throw new ISPACRuleException("Error. " + e.getMessage(), e);
      }
    }
  }
  


  void addGrayBand(File file, String pathFileTemp, String infoPagRDE, String tipoRegistro, String numRegistro, String fechaRegistro, String departamento, String entidad)
    throws Exception
  {
    float margen = Float.parseFloat(ISPACConfiguration.getInstance().getProperty("MARGIN_BAND"));
    float bandSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty("SIZE_BAND"));
    
    try
    {
      PdfReader readerInicial = new PdfReader(file.getAbsolutePath());
      int n = readerInicial.getNumberOfPages();
      int largo = (int)readerInicial.getPageSize(n).getHeight();
      


      bandSize = 15.0F;
      
      Image imagen = createBgImage(entidad);
      Document document = new Document();
      String ruta = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
      FileOutputStream fileOut = new FileOutputStream(ruta, true);
      
      PdfWriter writer = PdfWriter.getInstance(document, fileOut);
      document.open();
      Rectangle r = document.getPageSize();
      
      for (int i = 1; i <= n; i++) {
        PdfImportedPage page = writer.getImportedPage(readerInicial, i);
        Image image = Image.getInstance(page);
        
        image.setAbsolutePosition(bandSize, 0.0F);
        image.scaleAbsoluteWidth(r.getWidth() - bandSize);
        image.scaleAbsoluteHeight(r.getHeight());
        imagen.setRotationDegrees(90.0F);
        document.add(image);
        if (imagen != null) {
          for (int j = 0; j < largo; j = (int)(j + imagen.getWidth())) {
            imagen.setAbsolutePosition(0.0F, j);
            imagen.scaleAbsoluteHeight(bandSize);
            document.add(imagen);
          }
        }
        PdfContentByte over = writer.getDirectContent();
        getImagen(over, margen, margen, n, i, tipoRegistro, numRegistro, fechaRegistro, departamento, entidad);
        
        document.newPage();
      }
      
      document.close();
      
      fileOut.close();
      writer.close();
      readerInicial.close();
    }
    catch (ISPACException e) {
      logger.error("Error al añadir la banda lateral al PDF", e);
      throw new ISPACRuleException("Error. ", e);
    } catch (Exception exc) {
      logger.error("Error al añadir la banda lateral al PDF", exc);
      throw new ISPACException(exc);
    }
  }
  

  protected Image createBgImage(String entidad)
    throws ISPACException
  {
    String imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath(new StringBuilder("skinEntidad_").append(entidad).append(File.separator).toString(), "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty("imagen_fondo");
    

    try
    {
      File fondoURL = new File(imageFondoPath);
      Image fondo = null;
      if (fondoURL != null) {
        fondo = Image.getInstance(imageFondoPath);
        fondo.setAbsolutePosition(250.0F, 50.0F);
        fondo.scalePercent(70.0F);
      }
      
      return fondo;
    }
    catch (Exception e) {
      logger.error("Error al leer la imagen de fondo del PDF firmado: " + imageFondoPath, e);
      throw new ISPACException(e);
    }
  }
  
  protected void getImagen(PdfContentByte pdfContentByte, float margen, float x, int numberOfPages, int pageActual, String tipoReg, String numReg, String fecReg, String departamento, String entidad)
    throws ISPACException
  {
    try
    {
      String font = ISPACConfiguration.getInstance().getProperty("FONT_BAND");
      String encoding = ISPACConfiguration.getInstance().getProperty("ENCODING_BAND");
      float fontSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty("FONTSIZE_BAND"));
      
      BaseFont bf = BaseFont.createFont(font, encoding, false);
      pdfContentByte.beginText();
      pdfContentByte.setFontAndSize(bf, fontSize);
      
      BufferedReader br = new BufferedReader(new FileReader(getDataFile(entidad)));
      String sCadena = null;
      int i = 0;
      String scadenaReg = "";
      pdfContentByte.setTextMatrix(0.0F, 1.0F, -1.0F, 0.0F, x, margen);
      while ((sCadena = br.readLine()) != null) {
        if (i == 0) {
          scadenaReg = scadenaReg + sCadena + " " + tipoReg + " ";
        }
        if (i == 1) {
          scadenaReg = scadenaReg + sCadena + " " + numReg + " ";
        }
        if (i == 2) {
          scadenaReg = scadenaReg + sCadena + " " + fecReg + " ";
        }
        if (i == 3) {
          scadenaReg = scadenaReg + sCadena + " " + departamento;
        }
        i++;
      }
      pdfContentByte.showText(scadenaReg);
      
      pdfContentByte.endText();
      
      br.close();
    }
    catch (Exception e) {
      logger.error("Error al componer la imagen de la banda lateral", e);
      throw new ISPACException(e);
    }
  }


  protected File getDataFile(String entidad)
    throws ISPACException
  {
    String dataPath = ISPACConfiguration.getInstance().getProperty("config_" + entidad + "/" + "firma/datosFirmaRegistro.txt");
    if (StringUtils.isBlank(dataPath)) {
      dataPath = "config_" + entidad + "/" + "firma/datosFirmaRegistro.txt";
    }
    
    String basename = null;
    String ext = null;
    int dotIx = dataPath.lastIndexOf(".");
    if (dotIx > 0) {
      basename = dataPath.substring(0, dotIx);
      ext = dataPath.substring(dotIx);
    } else {
      basename = dataPath;
    }
    

    String dataFullPath = ConfigurationHelper.getConfigFilePath(basename + ext);
    if (StringUtils.isBlank(dataFullPath))
    {

      dataFullPath = ConfigurationHelper.getConfigFilePath(dataPath);
    }
    
    if (logger.isInfoEnabled()) {
      logger.warn("Texto de la banda lateraldel PDF: " + dataFullPath);
    }
    return new File(dataFullPath);
  }



  private File concatenaPdf(ArrayList<FileInputStream> inputStreamNotificaciones, ArrayList<String> filePathNotificaciones, IGenDocAPI genDocAPI)
    throws ISPACRuleException
  {
    File resultado = null;
    File file = null;
    
    try
    {
      PdfReader reader = null;
      PdfReader readerBlanco = null;
      
      Document document = null;
      PdfCopy writer = null;
      boolean primero = true;
      InputStream f = null;
      
      int pageOffset = 0;
      ArrayList<Object> master = new ArrayList<Object>();
      
      if (inputStreamNotificaciones.size() != 0) {
        Iterator<FileInputStream> inputStreamNotificacionesIterator = inputStreamNotificaciones.iterator();
        while (inputStreamNotificacionesIterator.hasNext()) {
          reader = new PdfReader((InputStream)inputStreamNotificacionesIterator.next());
          
          reader.consolidateNamedDestinations();
          int n = reader.getNumberOfPages();
          List<?> bookmarks = SimpleBookmark.getBookmark(reader);
          
          if (bookmarks != null) {
            if (pageOffset != 0)
              SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
            master.addAll(bookmarks);
          }
          
          pageOffset += n;
          
          if (primero)
          {
            document = new Document(reader.getPageSizeWithRotation(1));
            
            resultado = new File((String)filePathNotificaciones.get(0));
            
            writer = new PdfCopy(document, new FileOutputStream(resultado));
            
            writer.setViewerPreferences(128);
            
            document.open();
            primero = false;
          }
          


          document.newPage();
          
          for (int i = 0; i < n;) {
            i++;
            PdfImportedPage page = writer.getImportedPage(reader, i);
            writer.addPage(page);
          }
          
          if (n % 2 == 1) {
            document.newPage();
            
            file = PdfUtil.blancoPDF();
            f = new FileInputStream(file.getAbsolutePath());
            readerBlanco = new PdfReader(f);
            PdfImportedPage page = writer.getImportedPage(readerBlanco, 1);
            writer.addPage(page);
          }
          
          if (reader != null) {
            reader.close();
          }
          
          if (readerBlanco != null) {
            readerBlanco.close();
          }
          if (f != null) {
            f.close();
            f = null;
          }
          if (file != null) {
            file.delete();
            file = null;
          }
        }
      }
      

      if (!master.isEmpty())
        writer.setOutlines(master);
      if (inputStreamNotificaciones.size() != 0) document.close();
    }
    catch (Exception e) {
      logger.error("Error. " + e.getMessage(), e);
      throw new ISPACRuleException("Error validate. " + e.getMessage(), e);
    }
    return resultado;
  }
  
  public static void main (String args[]){
	  
	  NotifyStateNotificaSchedulerTask noti = new NotifyStateNotificaSchedulerTask();
	  Entidad ent = new Entidad();
	  ent.setCodigoINE("P1300000E");
	  ent.setDeh("212201");
	  ent.setDir3("L02000013");
	  ent.setIdentificador("005");
	  ent.setNombre("DIPUTACIÓN DE CIUDAD REAL");
	  ent.setNombreCorto("DIPUTACIÓN DE CIUDAD REAL");
	  ent.setNombreLargo("DIPUTACIÓN DE CIUDAD REAL");
	  ent.setSia("212201");
	  noti.execute(ent);
	  
  }
}