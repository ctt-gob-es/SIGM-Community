package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.util.ListCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.EntidadesException;
import ieci.tecdoc.sgm.core.services.terceros.ServicioTerceros;
import ieci.tecdoc.sgm.core.services.terceros.TercerosException;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionElectronica;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionPostal;
import ieci.tecdoc.sgm.core.services.terceros.dto.Tercero;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [ecenpri-Felipe #444]
 * Clase con funciones comunes para decretos
 * @since 21.09.2011
 * @author Felipe
 */
public class ParticipantesUtil {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(ParticipantesUtil.class);
	
	public static final String _TIPO_TRASLADO = "TRAS";
	public static final String _TIPO_INTERESADO = "INT";
	
	public static final String _TIPO_PERSONA_FISICA = "F";
	public static final String _TIPO_PERSONA_JURIDICA = "J";
	
	/**
	 * Inserta un participante por su NIF. Sin recurso
	 * @param rulectx
	 * @param nif
	 * @param tipoRelacion
	 * @param tipoPersona
	 * @param email
	 * @return
	 * @throws ISPACException
	 */
	public static boolean insertarParticipanteByNIF(IRuleContext rulectx, String numexp,
			String nif, String tipoRelacion, String tipoPersona, String email)
		throws ISPACException
	{
		return insertarParticipanteByNIF(rulectx, numexp, nif, tipoRelacion, tipoPersona, email, null);
	}
	
	/**
	 * Inserta un participante por su NIF
	 * @param rulectx
	 * @param nif
	 * @param tipoRelacion
	 * @param tipoPersona
	 * @param email
	 * @param recurso [eCenpri-Felipe/Agus #635]
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings("unchecked")
	public static boolean insertarParticipanteByNIF(IRuleContext rulectx, String numexp,
			String nif, String tipoRelacion, String tipoPersona, String email, String recurso)
		throws ISPACException{
		ServicioTerceros servicioTerceros;
		List<Tercero> terceros;//[dipucr-Felipe] Corregido error [Manu] dependencias cíclicas
		String entityId = null;

		try{
			servicioTerceros = LocalizadorServicios.getServicioTerceros();
			
			OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
			if (info != null)
				entityId = info.getOrganizationId();
			
			terceros = servicioTerceros.lookup(entityId, nif, true);
		} catch (TercerosException e) {
			logger.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
		} catch (EntidadesException e) {
			logger.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
		} catch (SigemException e) {
			logger.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
		}
		if (terceros.size() == 0){ //[eCenpri-Felipe #632]
			Log.error("El participante con NIF/CIF " + nif + " no existe en la BBDD de Terceros.");
			return false;
		}
		else{
			return insertarParticipante(rulectx, numexp, terceros.get(0), tipoRelacion, tipoPersona, email, recurso);
		}
	}

	/**
	 * Inserta un participante por su id. Sin recurso
	 * @param rulectx
	 * @param idParticipante
	 * @param tipoRelacion
	 * @param tipoPersona
	 * @param email
	 * @return
	 * @throws ISPACException
	 */
	public static boolean insertarParticipanteById(IRuleContext rulectx, String numexp,
			String idParticipante, String tipoRelacion, String tipoPersona, String email)
		throws ISPACException
	{
		return insertarParticipanteById(rulectx, numexp, idParticipante, tipoRelacion, tipoPersona, email, null);
	}
	
	/**
	 * Inserta un participante por su id
	 * @param rulectx
	 * @param idParticipante
	 * @param tipoRelacion
	 * @param tipoPersona
	 * @param email
	 * @param recurso [eCenpri-Felipe/Agus #635]
	 * @return
	 * @throws ISPACException
	 */
	public static boolean insertarParticipanteById(IRuleContext rulectx, String numexp,
			String idParticipante, String tipoRelacion, String tipoPersona, String email, String recurso)
		throws ISPACException
	{
		ServicioTerceros servicioTerceros;
		Tercero tercero;
		String entityId = null;
		
		try {
			servicioTerceros = LocalizadorServicios.getServicioTerceros();
			OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
			if (info != null)
				entityId = info.getOrganizationId();
			tercero = servicioTerceros.lookupById(entityId,idParticipante);
		} catch (TercerosException e) {
			logger.error("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
		} catch (EntidadesException e) {
			logger.error("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
		} catch (SigemException e) {
			logger.error("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
		}
		return insertarParticipante(rulectx, numexp, tercero, tipoRelacion, tipoPersona, email, recurso);
	}
	
	/**
	 * Busca el participante por codigo en la tabla DPCR_ID_PARTICIPANTES. Sin recurso
	 * Una vez recuperado el id y el mail llama al método "insertarParticipanteById"
	 * @param rulectx
	 * @param codigo
	 * @param tipoRelacion
	 * @param tipoPersona
	 * @return
	 * @throws ISPACException
	 */
	public static boolean insertarParticipanteByCodigo(IRuleContext rulectx, String numexp,
			String codigo, String tipoRelacion, String tipoPersona)
		throws ISPACException
	{
		return insertarParticipanteByCodigo(rulectx, numexp, codigo, tipoRelacion, tipoPersona, null);
	}
	
	/**
	 * Busca el participante por codigo en la tabla DPCR_ID_PARTICIPANTES
	 * Una vez recuperado el id y el mail llama al método "insertarParticipanteById"
	 * @param rulectx
	 * @param codigo
	 * @param tipoRelacion
	 * @param tipoPersona
	 * @param recurso [eCenpri-Felipe/Agus #635]
	 * @return
	 * @throws ISPACException
	 */
	public static boolean insertarParticipanteByCodigo(IRuleContext rulectx, String numexp,
			String codigo, String tipoRelacion, String tipoPersona, String recurso)
		throws ISPACException
	{
		IItem itemParticipante = recuperarDatosParticipante(rulectx, codigo);
		String idParticipante = itemParticipante.getString("ID_PARTICIPANTE");
		String email = itemParticipante.getString("EMAIL");
		return insertarParticipanteById(rulectx, numexp, idParticipante, tipoRelacion, tipoPersona, email, recurso);
	}
	
	
	/**
	 * Inserta el participante en el expediente pasado como parametro
	 * @param rulectx
	 * @param tercero
	 * @param idParticipante
	 * @param tipoRelacion
	 * @param tipoPersona
	 * @param email
	 * @param recurso [eCenpri-Felipe/Agus #635]
	 * @return
	 * @throws ISPACRuleException
	 */
	protected static boolean insertarParticipante(IRuleContext rulectx, String numexp,
			Tercero tercero, String tipoRelacion, String tipoPersona, String email, String recurso)
		throws ISPACRuleException
	{
		
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        		    
	        IItem nuevoParticipante = null;
	
			//Si no existe el tercero no hacemos nada
			if(tercero != null && tercero.getIdExt()!= null){
				//Comprobamos si existe ya el participante, si existe no hacemos nada
	
		    	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexp,  "ID_EXT = '" + tercero.getIdExt()+"'", "");
			
		    	//Si el tercero no está ya insertado
		    	if(!participantes.iterator().hasNext()){
		    		cct.beginTX();
			        nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);
			        
			        nuevoParticipante.set("ID_EXT", tercero.getIdExt());
			        nuevoParticipante.set("ROL", tipoRelacion);
			        nuevoParticipante.set("TIPO_PERSONA", tipoPersona);
			        nuevoParticipante.set("NDOC", tercero.getIdentificacion());
			        nuevoParticipante.set("NOMBRE", tercero.getNombreCompleto());
			        if (null != tercero.getDireccionPostalPredeterminada()){ //[eCenpri-Felipe #474]
				        nuevoParticipante.set("DIRNOT", tercero.getDireccionPostalPredeterminada().getDireccionPostal());		        
			            nuevoParticipante.set("C_POSTAL", tercero.getDireccionPostalPredeterminada().getCodigoPostal());
			            nuevoParticipante.set("LOCALIDAD", tercero.getDireccionPostalPredeterminada().getMunicipio());
			            nuevoParticipante.set("CAUT", tercero.getDireccionPostalPredeterminada().getProvincia());
			            nuevoParticipante.set("TFNO_FIJO", tercero.getDireccionPostalPredeterminada().getTelefono());
			        }
		            nuevoParticipante.set("TIPO_DIRECCION", "T");	            
		            //Debido a que en el registro telemático web no funciona la opción de recuperar el correo electrónico, lo introducimos manualmente
		            if (StringUtils.isNotBlank(email)){
			            nuevoParticipante.set("EMAIL", email);
			            nuevoParticipante.set("DIRECCIONTELEMATICA", email);
		    		}
		            //INICIO [eCenpri-Felipe/Agus #635]
		            if (StringUtils.isNotBlank(recurso)){
		            	nuevoParticipante.set("RECURSO", recurso);
		            }
		            //FIN [eCenpri-Felipe/Agus #635]
		            
		            try{
		            	nuevoParticipante.store(cct);
		            	cct.endTX(true);
		            }
		            catch(Exception e){
		            	logger.error("Error al guardar el participante con NIF/CIF " + tercero.getIdentificacion() + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
		            	cct.endTX(false);
			    		return false;//[eCenpri-Felipe #632]
		            }
				}//Fin si !existe ya en el expediente
			}//Fin si existe el tercero en la BBDD de terceros
			else{ //INICIO [eCenpri-Felipe #632]
            	logger.error("El participante con NIF/CIF " + tercero.getIdentificacion() + " no existe en la BBDD de Terceros. Expediente: " + numexp + ". ");
	    		return false;
	    	}
		} catch (ISPACException e) {
			logger.error("Error al guardar el participante con NIF/CIF " + tercero.getIdentificacion() + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
			return false;
		}//FIN [eCenpri-Felipe #632]
		return true;
	}
	
	/**
	 * Recupera los datos del participante de la tabla DPCR_ID_PARTICIPANTES
	 * @param rulectx
	 * @param codigo
	 * @return
	 */
	public static IItem recuperarDatosParticipante(IRuleContext rulectx, String codigo)
		throws ISPACException
	{
		
        IItem itemParticipante = null;
		
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        		    
	        String strQuery = "WHERE CODIGO = '" + codigo + "'";
	        IItemCollection collection = entitiesAPI.queryEntities("DPCR_ID_PARTICIPANTES", strQuery);
	        
	        if (collection.next()){
	        	itemParticipante = (IItem)collection.iterator().next();
	        }
		}
		catch (Exception e) {
			throw new ISPACException(e);
		}
	        
		return itemParticipante;
	}
	
	private static IItemCollection recuperaParticipanteRelacionadosByEstadoAdm(IClientContext cct, String numexp, String estadoAdm) throws ISPACException{
		
		IItemCollection partExpedientes = null;		
		try{
			StringBuffer consulta = new StringBuffer(" WHERE ");
			consulta.append(" (NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE NUMEXP IN ");
			consulta.append(" (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = '"+numexp+"')");
			consulta.append(" AND ESTADOADM = '"+estadoAdm+"')");
			consulta.append(" OR ");
			consulta.append(" NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE NUMEXP IN ");
			consulta.append(" (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = '"+numexp+"')");
			consulta.append(" AND ESTADOADM = '"+estadoAdm+"'))");
			consulta.append(" ORDER BY NOMBRE");
			
			partExpedientes = ParticipantesUtil.queryParticipantes(cct, consulta.toString());
		} catch (ISPACException e) {
			logger.error("Error al recuperar los participantes de los hijos con estado administrativo: " + estadoAdm + ", del expediente: " + numexp + ". " + e.getMessage(), e);	
			throw new ISPACRuleException("Error al recuperar los participantes de los hijos con estado administrativo: " + estadoAdm + ", del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return partExpedientes;
		
	}

	@SuppressWarnings("rawtypes")
	public static void importarParticipantes(ClientContext cct, IEntitiesAPI entitiesAPI, String expOrigen, String expDestino) throws ISPACRuleException{
		try{
			IItemCollection participantes = recuperaParticipanteRelacionadosByEstadoAdm(cct, expOrigen, "AP");
			
			
		/*	probar
			
			SELECT * FROM SPAC_DT_INTERVINIENTES WHERE 
			(NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE NUMEXP IN 
			(SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = 'DPCR2014/32750') AND ESTADOADM = 'AP')) AND ROL='INT'

			SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP IN  (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE NUMEXP IN 
			(SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = 'DPCR2014/32750')) AND nombreprocedimiento='Licitador'
			
			anexa mal los particantes*/

			if(participantes == null || participantes.toList().size() == 0){
				participantes = ParticipantesUtil.getParticipantes( cct, expOrigen);
			}
			
			Iterator it = participantes.iterator();
			
			IItem nuevoParticipante = null;
	        IItem participante = null;
			String id_ext = "";
	        String dni = "";
	        String nombre = "";
	    	IItemCollection participantes2 = null;
			Iterator it2 = null;
			boolean existe = false;
	
		    while (it.hasNext()) {
		    	nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, expDestino);
		        participante = (IItem)it.next();
		        		        	
		        //MQE Comprobamos que no exista ya el participante
		        id_ext = participante.getString("ID_EXT");
		        dni = participante.getString("NDOC");
		        nombre = participante.getString("NOMBRE");
	        	participantes2 = ParticipantesUtil.getParticipantes(cct, expDestino);
				it2 = participantes2.iterator();
				existe = false;
								
				while (it2.hasNext()){
					IItem participante2 = (IItem)it2.next();
					//MQE si el id_ext es nulo lo consideramos como que no existe
					if(id_ext != null && !id_ext.equals("")){
						//MQE si el id_ext es igual es el mismo participante
						if(participante2.getString("ID_EXT")!= null && participante2.getString("ID_EXT").equals(id_ext)) existe = true;
					}
					//MQE si el dni es nulo lo consideramos como que no existe
					if(dni != null && !dni.equals("")){
						//MQE si el dni es igual es el mismo participante
						if(participante2.getString("NDOC")!= null && participante2.getString("NDOC").equals(dni)) existe = true;
					}
					//MQE si el nombre es igual es el mismo participante
					if(participante2.getString("NOMBRE")!= null && participante2.getString("NOMBRE").toUpperCase().equals(nombre.toUpperCase())) existe = true;										
				}//Fin while it2
				if(!existe){	
			        nuevoParticipante.set("ID_EXT", participante.getString("ID_EXT"));
			        nuevoParticipante.set("ROL", participante.getString("ROL"));
			        nuevoParticipante.set("TIPO", participante.getString("TIPO"));
			        nuevoParticipante.set("TIPO_PERSONA", participante.getString("TIPO_PERSONA"));
			        nuevoParticipante.set("NDOC", participante.getString("NDOC"));
			        nuevoParticipante.set("NOMBRE", participante.getString("NOMBRE"));
			        nuevoParticipante.set("DIRNOT", participante.getString("DIRNOT"));
			        nuevoParticipante.set("EMAIL", participante.getString("EMAIL"));
		            nuevoParticipante.set("C_POSTAL", participante.getString("C_POSTAL"));
		            nuevoParticipante.set("LOCALIDAD", participante.getString("LOCALIDAD"));
		            nuevoParticipante.set("CAUT", participante.getString("CAUT"));
		            nuevoParticipante.set("TFNO_FIJO", participante.getString("TFNO_FIJO"));
		            nuevoParticipante.set("TFNO_MOVIL", participante.getString("TFNO_MOVIL"));
		            nuevoParticipante.set("TIPO_DIRECCION", participante.getString("TIPO_DIRECCION"));
		            nuevoParticipante.set("DIRECCIONTELEMATICA", participante.getString("DIRECCIONTELEMATICA"));
		            nuevoParticipante.set("IDDIRECCIONPOSTAL", participante.getString("IDDIRECCIONPOSTAL"));
		            nuevoParticipante.set("RECURSO", participante.getString("RECURSO"));
		            nuevoParticipante.set("OBSERVACIONES", participante.getString("OBSERVACIONES"));
		            nuevoParticipante.set("ASISTE", participante.getString("ASISTE"));
		            nuevoParticipante.set("TIPO_PODER", participante.getString("TIPO_PODER"));
		            nuevoParticipante.set("FECHA_INI", participante.getString("FECHA_INI"));
		            nuevoParticipante.set("FECHA_FIN", participante.getString("FECHA_FIN"));
		            nuevoParticipante.set("SOLICITAR_OFERTA", participante.getString("SOLICITAR_OFERTA"));
		            nuevoParticipante.set("CCC", participante.getString("CCC"));	   
		            
		            try{
		            	nuevoParticipante.store(cct);
		            }
		            catch(Exception e){
		            	Log.error("Error al guardar el participante con id :"+id_ext);
		            }
				}
		    }
		}catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido importar los participantes.",e);
        }
	}
	
	public static void copiaDtIntervinientes(IClientContext cct,  String tabla, IItem interviniente_viejo, String numexp) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		IItem interviniente_nuevo = entitiesAPI.createEntity(tabla, numexp);
	
		interviniente_nuevo.set("ID", interviniente_viejo.getInt("ID"));
		interviniente_nuevo.set("ID_EXT", interviniente_viejo.getString("ID_EXT"));
		interviniente_nuevo.set("NUMEXP", interviniente_viejo.getString("NUMEXP"));
		interviniente_nuevo.set("ROL", interviniente_viejo.getString("ROL"));
		interviniente_nuevo.set("TIPO", interviniente_viejo.getString("TIPO"));
		interviniente_nuevo.set("TIPO_PERSONA",	interviniente_viejo.getString("TIPO_PERSONA"));
		interviniente_nuevo.set("NDOC", interviniente_viejo.getString("NDOC"));
		interviniente_nuevo.set("NOMBRE", interviniente_viejo.getString("NOMBRE"));
		interviniente_nuevo.set("DIRNOT", interviniente_viejo.getString("DIRNOT"));
		interviniente_nuevo.set("EMAIL", interviniente_viejo.getString("EMAIL"));
		interviniente_nuevo.set("C_POSTAL",	interviniente_viejo.getString("C_POSTAL"));
		interviniente_nuevo.set("LOCALIDAD", interviniente_viejo.getString("LOCALIDAD"));
		interviniente_nuevo.set("CAUT", interviniente_viejo.getString("CAUT"));
		interviniente_nuevo.set("TFNO_FIJO", interviniente_viejo.getString("TFNO_FIJO"));
		interviniente_nuevo.set("TFNO_MOVIL", interviniente_viejo.getString("TFNO_MOVIL"));
		interviniente_nuevo.set("TIPO_DIRECCION", interviniente_viejo.getString("TIPO_DIRECCION"));
		interviniente_nuevo.set("DIRECCIONTELEMATICA", interviniente_viejo.getString("DIRECCIONTELEMATICA"));
		interviniente_nuevo.set("IDDIRECCIONPOSTAL", interviniente_viejo.getString("IDDIRECCIONPOSTAL"));
		interviniente_nuevo.set("DECRETO_NOTIFICADO", interviniente_viejo.getString("DECRETO_NOTIFICADO"));
		interviniente_nuevo.set("DECRETO_TRASLADADO", interviniente_viejo.getString("DECRETO_TRASLADADO"));
		interviniente_nuevo.set("ACUSE_GENERADO", interviniente_viejo.getString("ACUSE_GENERADO"));
		interviniente_nuevo.set("RECURSO", interviniente_viejo.getString("RECURSO"));
		interviniente_nuevo.set("OBSERVACIONES", interviniente_viejo.getString("OBSERVACIONES"));
		interviniente_nuevo.set("FECHA_ACUSE", interviniente_viejo.getDate("FECHA_ACUSE"));
		interviniente_nuevo.set("MOTIVO_ACUSE", interviniente_viejo.getString("MOTIVO_ACUSE"));
		interviniente_nuevo.set("RECURSO_TEXTO", interviniente_viejo.getString("RECURSO_TEXTO"));
		interviniente_nuevo.set("ASISTE", interviniente_viejo.getString("ASISTE"));
		interviniente_nuevo.set("TIPO_PODER", interviniente_viejo.getString("TIPO_PODER"));
		interviniente_nuevo.set("FECHA_INI", interviniente_viejo.getDate("FECHA_INI"));
		interviniente_nuevo.set("FECHA_FIN", interviniente_viejo.getDate("FECHA_FIN"));
		interviniente_nuevo.set("SOLICITAR_OFERTA", interviniente_viejo.getString("SOLICITAR_OFERTA"));
		interviniente_nuevo.set("SOLICITADA_OFERTA", interviniente_viejo.getString("SOLICITADA_OFERTA"));
		interviniente_nuevo.set("CCC", interviniente_viejo.getString("CCC"));
	
		interviniente_nuevo.store(cct);
	}
	
	
	public static IItemCollection getParticipantes(IClientContext cct, String numexp)throws ISPACException{
		IItemCollection resultado;
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		resultado = entitiesAPI.getParticipants(numexp, "", "");
		
		if(resultado == null || resultado.toList().size() == 0){
			resultado = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES_H, numexp, null);	        
		}

		return resultado;
	}
	
	public static IItemCollection getParticipantes(IClientContext cct, String numexp, String sqlQuery, String order)throws ISPACException{
		IItemCollection resultado;

		String consulta = "";
		
		if(StringUtils.isNotEmpty(numexp)) consulta += "WHERE NUMEXP = '" + numexp + "' ";

		if(StringUtils.isNotEmpty(sqlQuery)){
			if(StringUtils.isNotEmpty(numexp)) consulta += " AND ";
			else consulta += " WHERE ";
			consulta += "  ( " + sqlQuery + " ) ";
		}
		if(StringUtils.isNotEmpty(order)) consulta += " ORDER BY " + order;
		
		resultado = ParticipantesUtil.queryParticipantes(cct, consulta);

		return resultado;
	}
	
	public static IItemCollection queryParticipantes(IClientContext cct, String consulta) throws ISPACException{
		ListCollection resultado = null;
		ArrayList<IItem> part = new ArrayList<IItem>();
		
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			IItemCollection participantes = (ListCollection) entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, consulta);
			for(Object participante : participantes.toList()){
				part.add((IItem)participante);
			}
			
			IItemCollection participantes_h = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES_H, consulta);
			for(Object participante : participantes_h.toList()){
				part.add((IItem)participante);
			}
			
			resultado = new ListCollection(part);
		}
		catch(Exception e){
			logger.error("Error al recuperar los participantes. consulta: " + consulta + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar los participantes. " + e.getMessage(), e);
		}
		
		return resultado;
	}
	
	protected static DireccionPostal[] getDireccionesPostales(IPostalAddressAdapter[] adapters) {
		DireccionPostal[] dirs = null;
		
		if (adapters != null) {
			dirs = new DireccionPostal[adapters.length];
			for (int i = 0; i < dirs.length; i++) {
				dirs[i] = getDireccionPostal(adapters[i]);
			}
		}
		
		return dirs;
	}
	
	protected static DireccionPostal getDireccionPostal(IPostalAddressAdapter adapter) {
		DireccionPostal dir = null;
		
		if (adapter != null) {
			dir = new DireccionPostal();
			dir.setId(adapter.getId());
			dir.setDireccionPostal(adapter.getDireccionPostal());
			dir.setTipoVia(adapter.getTipoVia());
			dir.setVia(adapter.getVia());
			dir.setBloque(adapter.getBloque());
			dir.setPiso(adapter.getPiso());
			dir.setPuerta(adapter.getPuerta());
			dir.setCodigoPostal(adapter.getCodigoPostal());
			dir.setPoblacion(adapter.getPoblacion());
			dir.setMunicipio(adapter.getMunicipio());
			dir.setProvincia(adapter.getProvincia());
			dir.setComunidadAutonoma(adapter.getComunidadAutonoma());
			dir.setPais(adapter.getPais());
			dir.setTelefono(adapter.getTelefono());
		}

		return dir;
	}
	
	protected static DireccionElectronica[] getDireccionesElectronicas(IElectronicAddressAdapter[] adapters) {
		DireccionElectronica[] dirs = null;
		
		if (adapters != null) {
			dirs = new DireccionElectronica[adapters.length];
			for (int i = 0; i < dirs.length; i++) {
				dirs[i] = getDireccionElectronica(adapters[i]);
			}
		}
		
		return dirs;
	}

	protected static DireccionElectronica getDireccionElectronica(IElectronicAddressAdapter adapter) {
		DireccionElectronica dir = null;
		
		if (adapter != null) {
			dir = new DireccionElectronica();
			dir.setId(adapter.getId());
			dir.setTipo(adapter.getTipo());
			dir.setDireccion(adapter.getDireccion());
		}
		
		return dir; 
	}
	
	//[eCenpri-Mayu] - FIN - Se eliminan dependencias cíclicas.
}
