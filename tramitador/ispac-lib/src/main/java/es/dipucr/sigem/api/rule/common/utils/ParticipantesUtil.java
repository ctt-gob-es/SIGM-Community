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
	protected static final Logger LOGGER = Logger.getLogger(ParticipantesUtil.class);
	
	public static final String _TIPO_TRASLADO = "TRAS";
	public static final String _TIPO_INTERESADO = "INT";
	public static final String _TIPO_LICITADOR = "LIC";
	
	public static final String _TIPO_PERSONA_FISICA = "F";
	public static final String _TIPO_PERSONA_JURIDICA = "J";
	
	public static final String RECURSO_AYTOS_ADM_PUBL = "Aytos.Adm.Publ.";
	public static final String RECURSO_PERSONAS_FISICAS_EMPR = "Pers.Fis.-Empr.";
	
	public static final String TIPO_DIRECCION_TELEMATICA = "T";
	
	public static final String SPAC_DT_INTERVINIENTES = "SPAC_DT_INTERVINIENTES";
	public static final String SPAC_DT_INTERVINIENTES_H = "SPAC_DT_INTERVINIENTES_H";
	
	//Mapeo de las columnas de la tablas SPAC_DT_INTERVINIENTES y SPAC_DT_INTERVINIENTES_H
	public static final String ID = "ID";
	public static final String NUMEXP = "NUMEXP";
	public static final String NOMBRE = "NOMBRE";
	public static final String TIPO = "TIPO";
	public static final String DIRNOT = "DIRNOT";
	public static final String C_POSTAL = "C_POSTAL";
	public static final String LOCALIDAD = "LOCALIDAD";
	public static final String CAUT = "CAUT";
	public static final String ID_EXT = "ID_EXT";
	public static final String NDOC = "NDOC";
	public static final String ROL = "ROL";
	public static final String TIPO_PERSONA = "TIPO_PERSONA";
	public static final String TIPO_DIRECCION = "TIPO_DIRECCION";
	public static final String DIRECCIONTELEMATICA = "DIRECCIONTELEMATICA";
	public static final String EMAIL = "EMAIL";
	public static final String IDDIRECCIONPOSTAL = "IDDIRECCIONPOSTAL";
	public static final String TFNO_FIJO = "TFNO_FIJO";
	public static final String TFNO_MOVIL = "TFNO_MOVIL";	
	public static final String DECRETO_NOTIFICADO = "DECRETO_NOTIFICADO";
	public static final String DECRETO_TRASLADADO = "DECRETO_TRASLADADO";
	public static final String ACUSE_GENERADO = "ACUSE_GENERADO";
	public static final String RECURSO = "RECURSO";
	public static final String OBSERVACIONES = "OBSERVACIONES";
	public static final String FECHA_ACUSE = "FECHA_ACUSE";
	public static final String MOTIVO_ACUSE = "MOTIVO_ACUSE";
	public static final String RECURSO_TEXTO = "RECURSO_TEXTO";
	public static final String ASISTE = "ASISTE";
	public static final String TIPO_PODER = "TIPO_PODER";
	public static final String FECHA_INI = "FECHA_INI";
	public static final String FECHA_FIN = "FECHA_FIN";
	public static final String SOLICITAR_OFERTA = "SOLICITAR_OFERTA";
	public static final String SOLICITADA_OFERTA = "SOLICITADA_OFERTA";
	public static final String CCC = "CCC";
	public static final String DIR3 = "DIR3";

	
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
	public static boolean insertarParticipanteByNIF(IRuleContext rulectx, String numexp,
			String nif, String tipoRelacion, String tipoPersona, String email, String recurso)
		throws ISPACException{
		
		return insertarParticipanteByNIF(rulectx.getClientContext(), numexp, nif, tipoRelacion, tipoPersona, email, recurso);		
	}
		
	@SuppressWarnings("unchecked")
	public static boolean insertarParticipanteByNIF(IClientContext cct, String numexp,
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
			LOGGER.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
		} catch (EntidadesException e) {
			LOGGER.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
		} catch (SigemException e) {
			LOGGER.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
		}
		if (terceros.size() == 0){ //[eCenpri-Felipe #632]
			Log.error("El participante con NIF/CIF " + nif + " no existe en la BBDD de Terceros.");
			return false;
		}
		else{
			return insertarParticipante(cct, numexp, terceros.get(0), tipoRelacion, tipoPersona, email, recurso);
		}
	}
	
	public static boolean insertarParticipanteByNIFValidadoNoValidado(IClientContext cct, String numexp,
			String nif, String tipoRelacion, String tipoPersona, String email, String recurso, Tercero tercero)
		throws ISPACException{
	ServicioTerceros servicioTerceros;
	List<Tercero> terceros;
	String entityId = null;

	try{
		servicioTerceros = LocalizadorServicios.getServicioTerceros();
		
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		if (info != null)
			entityId = info.getOrganizationId();
		
		terceros = servicioTerceros.lookup(entityId, nif, true);
	} catch (TercerosException e) {
		LOGGER.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
		throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
	} catch (EntidadesException e) {
		LOGGER.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
		throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
	} catch (SigemException e) {
		LOGGER.error("Error al recuperar el participante con NIF " + nif + ". " + e.getMessage(), e);
		throw new ISPACException("Error al recuperar el participante con id " + nif + ". " + e.getMessage(), e);
	}
	if (terceros.size() == 0){ 
		return insertarParticipanteNoValidado(cct, numexp, tercero, tipoRelacion, tipoPersona, email, recurso);
	}
	else{
		return insertarParticipante(cct, numexp, terceros.get(0), tipoRelacion, tipoPersona, email, recurso);
	}
}
	protected static boolean insertarParticipanteNoValidado(IClientContext cct, String numexp,
			Tercero tercero, String tipoRelacion, String tipoPersona, String email, String recurso)
		throws ISPACRuleException
	{

	try {
		//----------------------------------------------------------------------------------------------
        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        		    
        IItem nuevoParticipante = null;

		//Si no existe el tercero no hacemos nada
		if(tercero != null){
			//Comprobamos si existe ya el participante, si existe no hacemos nada

	    	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexp,  ParticipantesUtil.ID_EXT + " = '" + tercero.getIdExt()+"'", "");
		
	    	//Si el tercero no está ya insertado
	    	if(!participantes.iterator().hasNext()){
	    		cct.beginTX();
		        nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);
		        
		        if(StringUtils.isNotEmpty(tercero.getIdExt()))nuevoParticipante.set(ParticipantesUtil.ID_EXT, tercero.getIdExt());
		        if(StringUtils.isNotEmpty(tipoRelacion))nuevoParticipante.set(ParticipantesUtil.ROL, tipoRelacion);
		        if(StringUtils.isNotEmpty(tipoPersona))nuevoParticipante.set(ParticipantesUtil.TIPO_PERSONA, tipoPersona);
		        if(StringUtils.isNotEmpty(tercero.getIdentificacion()))nuevoParticipante.set(ParticipantesUtil.NDOC, tercero.getIdentificacion());
		        if(StringUtils.isNotEmpty(tercero.getNombreCompleto()))nuevoParticipante.set(ParticipantesUtil.NOMBRE, tercero.getNombreCompleto());
		        
		        if (null != tercero.getDireccionPostalPredeterminada()){ 
		        	if(StringUtils.isNotEmpty(tercero.getDireccionPostalPredeterminada().getDireccionPostal()))nuevoParticipante.set(ParticipantesUtil.DIRNOT, tercero.getDireccionPostalPredeterminada().getDireccionPostal());		        
		        	if(StringUtils.isNotEmpty(tercero.getDireccionPostalPredeterminada().getCodigoPostal()))nuevoParticipante.set(ParticipantesUtil.C_POSTAL, tercero.getDireccionPostalPredeterminada().getCodigoPostal());
		        	if(StringUtils.isNotEmpty(tercero.getDireccionPostalPredeterminada().getMunicipio()))nuevoParticipante.set(ParticipantesUtil.LOCALIDAD, tercero.getDireccionPostalPredeterminada().getMunicipio());
		        	if(StringUtils.isNotEmpty(tercero.getDireccionPostalPredeterminada().getProvincia()))nuevoParticipante.set(ParticipantesUtil.CAUT, tercero.getDireccionPostalPredeterminada().getProvincia());
		        	if(StringUtils.isNotEmpty(tercero.getDireccionPostalPredeterminada().getTelefono()))nuevoParticipante.set(ParticipantesUtil.TFNO_FIJO, tercero.getDireccionPostalPredeterminada().getTelefono());
		        }
		        
	            nuevoParticipante.set(ParticipantesUtil.TIPO_DIRECCION, "T");	            
	            //Debido a que en el registro telemático web no funciona la opción de recuperar el correo electrónico, lo introducimos manualmente
	            if (StringUtils.isNotEmpty(email)){
		            nuevoParticipante.set(ParticipantesUtil.EMAIL, email);
		            nuevoParticipante.set(ParticipantesUtil.DIRECCIONTELEMATICA, email);
	    		}
	            if (StringUtils.isNotEmpty(recurso)){
	            	nuevoParticipante.set(ParticipantesUtil.RECURSO, recurso);
	            }
	            
	            try{
	            	nuevoParticipante.store(cct);
	            	cct.endTX(true);
	            }
	            catch(Exception e){
	            	LOGGER.error("Error al guardar el participante con NIF/CIF " + tercero.getIdentificacion() + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
	            	cct.endTX(false);
		    		return false;//[eCenpri-Felipe #632]
	            }
			}//Fin si !existe ya en el expediente
		}//Fin si existe el tercero en la BBDD de terceros
		else{ 
        	LOGGER.error("El participante es nulo. Expediente: " + numexp + ". ");
    		return false;
    	}
	} catch (ISPACException e) {
		LOGGER.error("Error al guardar el participante con NIF/CIF " + tercero.getIdentificacion() + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
		return false;
	}
	return true;
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
			LOGGER.error("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
		} catch (EntidadesException e) {
			LOGGER.error("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
		} catch (SigemException e) {
			LOGGER.error("Error al recuperar el participante con id " + idParticipante + ". " + e.getMessage(), e);
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
		String email = itemParticipante.getString(ParticipantesUtil.EMAIL);
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
		return insertarParticipante(rulectx.getClientContext(), numexp, tercero, tipoRelacion, tipoPersona, email, recurso);

	}
		
		
		
	protected static boolean insertarParticipante(IClientContext cct, String numexp,
				Tercero tercero, String tipoRelacion, String tipoPersona, String email, String recurso)
			throws ISPACRuleException
		{

		try {
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        		    
	        IItem nuevoParticipante = null;
	
			//Si no existe el tercero no hacemos nada
			if(tercero != null && tercero.getIdExt()!= null){
				//Comprobamos si existe ya el participante, si existe no hacemos nada
	
		    	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexp,  ParticipantesUtil.ID_EXT + " = '" + tercero.getIdExt()+"'", "");
			
		    	//Si el tercero no está ya insertado
		    	if(!participantes.iterator().hasNext()){
		    		cct.beginTX();
			        nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);
			        
			        nuevoParticipante.set(ParticipantesUtil.ID_EXT, tercero.getIdExt());
			        nuevoParticipante.set(ParticipantesUtil.ROL, tipoRelacion);
			        nuevoParticipante.set(ParticipantesUtil.TIPO_PERSONA, tipoPersona);
			        nuevoParticipante.set(ParticipantesUtil.NDOC, tercero.getIdentificacion());
			        nuevoParticipante.set(ParticipantesUtil.NOMBRE, tercero.getNombreCompleto());
			        if (null != tercero.getDireccionPostalPredeterminada()){ //[eCenpri-Felipe #474]
				        nuevoParticipante.set(ParticipantesUtil.DIRNOT, tercero.getDireccionPostalPredeterminada().getDireccionPostal());		        
			            nuevoParticipante.set(ParticipantesUtil.C_POSTAL, tercero.getDireccionPostalPredeterminada().getCodigoPostal());
			            nuevoParticipante.set(ParticipantesUtil.LOCALIDAD, tercero.getDireccionPostalPredeterminada().getMunicipio());
			            nuevoParticipante.set(ParticipantesUtil.CAUT, tercero.getDireccionPostalPredeterminada().getProvincia());
			            nuevoParticipante.set(ParticipantesUtil.TFNO_FIJO, tercero.getDireccionPostalPredeterminada().getTelefono());
			        }
		            nuevoParticipante.set(ParticipantesUtil.TIPO_DIRECCION, "T");	            
		            //Debido a que en el registro telemático web no funciona la opción de recuperar el correo electrónico, lo introducimos manualmente
		            if (StringUtils.isNotBlank(email)){
			            nuevoParticipante.set(ParticipantesUtil.EMAIL, email);
			            nuevoParticipante.set(ParticipantesUtil.DIRECCIONTELEMATICA, email);
		    		}
		            //INICIO [eCenpri-Felipe/Agus #635]
		            if (StringUtils.isNotBlank(recurso)){
		            	nuevoParticipante.set(ParticipantesUtil.RECURSO, recurso);
		            }
		            //FIN [eCenpri-Felipe/Agus #635]
		            
		            try{
		            	nuevoParticipante.store(cct);
		            	cct.endTX(true);
		            }
		            catch(Exception e){
		            	LOGGER.error("Error al guardar el participante con NIF/CIF " + tercero.getIdentificacion() + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
		            	cct.endTX(false);
			    		return false;//[eCenpri-Felipe #632]
		            }
				}//Fin si !existe ya en el expediente
			}//Fin si existe el tercero en la BBDD de terceros
			else{ //INICIO [eCenpri-Felipe #632]
            	LOGGER.error("El participante con NIF/CIF " + tercero.getIdentificacion() + " no existe en la BBDD de Terceros. Expediente: " + numexp + ". ");
	    		return false;
	    	}
		} catch (ISPACException e) {
			LOGGER.error("Error al guardar el participante con NIF/CIF " + tercero.getIdentificacion() + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
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
			LOGGER.error("Error al recuperar los participantes de los hijos con estado administrativo: " + estadoAdm + ", del expediente: " + numexp + ". " + e.getMessage(), e);	
			throw new ISPACRuleException("Error al recuperar los participantes de los hijos con estado administrativo: " + estadoAdm + ", del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return partExpedientes;
		
	}

	public static void importarParticipantes(ClientContext cct, IEntitiesAPI entitiesAPI, String expOrigen, String expDestino) throws ISPACRuleException{
		try{
			IItemCollection participantes = recuperaParticipanteRelacionadosByEstadoAdm(cct, expOrigen, ExpedientesUtil.EstadoADM.AP);
			
			
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
			
			Iterator<?> it = participantes.iterator();
			
			IItem nuevoParticipante = null;
	        IItem participante = null;
			String id_ext = "";
	        String dni = "";
	        String nombre = "";
	    	IItemCollection participantes2 = null;
			Iterator<?> it2 = null;
			boolean existe = false;
	
		    while (it.hasNext()) {
		    	nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, expDestino);
		        participante = (IItem)it.next();
		        		        	
		        //MQE Comprobamos que no exista ya el participante
		        id_ext = participante.getString(ParticipantesUtil.ID_EXT);
		        dni = participante.getString(ParticipantesUtil.NDOC);
		        nombre = participante.getString(ParticipantesUtil.NOMBRE);
	        	participantes2 = ParticipantesUtil.getParticipantes(cct, expDestino);
				it2 = participantes2.iterator();
				existe = false;
								
				while (it2.hasNext()){
					IItem participante2 = (IItem)it2.next();
					//MQE si el id_ext es nulo lo consideramos como que no existe
					if(id_ext != null && !id_ext.equals("")){
						//MQE si el id_ext es igual es el mismo participante
						if(participante2.getString(ParticipantesUtil.ID_EXT)!= null && participante2.getString(ParticipantesUtil.ID_EXT).equals(id_ext)) existe = true;
					}
					//MQE si el dni es nulo lo consideramos como que no existe
					if(dni != null && !dni.equals("")){
						//MQE si el dni es igual es el mismo participante
						if(participante2.getString(ParticipantesUtil.NDOC)!= null && participante2.getString(ParticipantesUtil.NDOC).equals(dni)) existe = true;
					}
					//MQE si el nombre es igual es el mismo participante
					if(participante2.getString(ParticipantesUtil.NOMBRE)!= null && participante2.getString(ParticipantesUtil.NOMBRE).toUpperCase().equals(nombre.toUpperCase())) existe = true;										
				}//Fin while it2
				if(!existe){	
			        nuevoParticipante.set(ParticipantesUtil.ID_EXT, participante.getString(ParticipantesUtil.ID_EXT));
			        nuevoParticipante.set(ParticipantesUtil.ROL, participante.getString(ParticipantesUtil.ROL));
			        nuevoParticipante.set(ParticipantesUtil.TIPO, participante.getString(ParticipantesUtil.TIPO));
			        nuevoParticipante.set(ParticipantesUtil.TIPO_PERSONA, participante.getString(ParticipantesUtil.TIPO_PERSONA));
			        nuevoParticipante.set(ParticipantesUtil.NDOC, participante.getString(ParticipantesUtil.NDOC));
			        nuevoParticipante.set(ParticipantesUtil.NOMBRE, participante.getString(ParticipantesUtil.NOMBRE));
			        nuevoParticipante.set(ParticipantesUtil.DIRNOT, participante.getString(ParticipantesUtil.DIRNOT));
			        nuevoParticipante.set(ParticipantesUtil.EMAIL, participante.getString(ParticipantesUtil.EMAIL));
		            nuevoParticipante.set(ParticipantesUtil.C_POSTAL, participante.getString(ParticipantesUtil.C_POSTAL));
		            nuevoParticipante.set(ParticipantesUtil.LOCALIDAD, participante.getString(ParticipantesUtil.LOCALIDAD));
		            nuevoParticipante.set(ParticipantesUtil.CAUT, participante.getString(ParticipantesUtil.CAUT));
		            nuevoParticipante.set(ParticipantesUtil.TFNO_FIJO, participante.getString(ParticipantesUtil.TFNO_FIJO));
		            nuevoParticipante.set(ParticipantesUtil.TFNO_MOVIL, participante.getString(ParticipantesUtil.TFNO_MOVIL));
		            nuevoParticipante.set(ParticipantesUtil.TIPO_DIRECCION, participante.getString(ParticipantesUtil.TIPO_DIRECCION));
		            nuevoParticipante.set(ParticipantesUtil.DIRECCIONTELEMATICA, participante.getString(ParticipantesUtil.DIRECCIONTELEMATICA));
		            nuevoParticipante.set(ParticipantesUtil.IDDIRECCIONPOSTAL, participante.getString(ParticipantesUtil.IDDIRECCIONPOSTAL));
		            nuevoParticipante.set(ParticipantesUtil.RECURSO, participante.getString(ParticipantesUtil.RECURSO));
		            nuevoParticipante.set(ParticipantesUtil.OBSERVACIONES, participante.getString(ParticipantesUtil.OBSERVACIONES));
		            nuevoParticipante.set(ParticipantesUtil.ASISTE, participante.getString(ParticipantesUtil.ASISTE));
		            nuevoParticipante.set(ParticipantesUtil.TIPO_PODER, participante.getString(ParticipantesUtil.TIPO_PODER));
		            nuevoParticipante.set(ParticipantesUtil.FECHA_INI, participante.getString(ParticipantesUtil.FECHA_INI));
		            nuevoParticipante.set(ParticipantesUtil.FECHA_FIN, participante.getString(ParticipantesUtil.FECHA_FIN));
		            nuevoParticipante.set(ParticipantesUtil.SOLICITAR_OFERTA, participante.getString(ParticipantesUtil.SOLICITAR_OFERTA));
		            nuevoParticipante.set(ParticipantesUtil.CCC, participante.getString(ParticipantesUtil.CCC));
		            nuevoParticipante.set(ParticipantesUtil.DIR3, participante.getString(ParticipantesUtil.DIR3));
		            
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
	
		interviniente_nuevo.set(ParticipantesUtil.ID, interviniente_viejo.getInt(ParticipantesUtil.ID));
		interviniente_nuevo.set(ParticipantesUtil.ID_EXT, interviniente_viejo.getString(ParticipantesUtil.ID_EXT));
		interviniente_nuevo.set(ParticipantesUtil.NUMEXP, interviniente_viejo.getString(ParticipantesUtil.NUMEXP));
		interviniente_nuevo.set(ParticipantesUtil.ROL, interviniente_viejo.getString(ParticipantesUtil.ROL));
		interviniente_nuevo.set(ParticipantesUtil.TIPO, interviniente_viejo.getString(ParticipantesUtil.TIPO));
		interviniente_nuevo.set(ParticipantesUtil.TIPO_PERSONA,	interviniente_viejo.getString(ParticipantesUtil.TIPO_PERSONA));
		interviniente_nuevo.set(ParticipantesUtil.NDOC, interviniente_viejo.getString(ParticipantesUtil.NDOC));
		interviniente_nuevo.set(ParticipantesUtil.NOMBRE, interviniente_viejo.getString(ParticipantesUtil.NOMBRE));
		interviniente_nuevo.set(ParticipantesUtil.DIRNOT, interviniente_viejo.getString(ParticipantesUtil.DIRNOT));
		interviniente_nuevo.set(ParticipantesUtil.EMAIL, interviniente_viejo.getString(ParticipantesUtil.EMAIL));
		interviniente_nuevo.set(ParticipantesUtil.C_POSTAL,	interviniente_viejo.getString(ParticipantesUtil.C_POSTAL));
		interviniente_nuevo.set(ParticipantesUtil.LOCALIDAD, interviniente_viejo.getString(ParticipantesUtil.LOCALIDAD));
		interviniente_nuevo.set(ParticipantesUtil.CAUT, interviniente_viejo.getString(ParticipantesUtil.CAUT));
		interviniente_nuevo.set(ParticipantesUtil.TFNO_FIJO, interviniente_viejo.getString(ParticipantesUtil.TFNO_FIJO));
		interviniente_nuevo.set(ParticipantesUtil.TFNO_MOVIL, interviniente_viejo.getString(ParticipantesUtil.TFNO_MOVIL));
		interviniente_nuevo.set(ParticipantesUtil.TIPO_DIRECCION, interviniente_viejo.getString(ParticipantesUtil.TIPO_DIRECCION));
		interviniente_nuevo.set(ParticipantesUtil.DIRECCIONTELEMATICA, interviniente_viejo.getString(ParticipantesUtil.DIRECCIONTELEMATICA));
		interviniente_nuevo.set(ParticipantesUtil.IDDIRECCIONPOSTAL, interviniente_viejo.getString(ParticipantesUtil.IDDIRECCIONPOSTAL));
		interviniente_nuevo.set(ParticipantesUtil.DECRETO_NOTIFICADO, interviniente_viejo.getString(ParticipantesUtil.DECRETO_NOTIFICADO));
		interviniente_nuevo.set(ParticipantesUtil.DECRETO_TRASLADADO, interviniente_viejo.getString(ParticipantesUtil.DECRETO_TRASLADADO));
		interviniente_nuevo.set(ParticipantesUtil.ACUSE_GENERADO, interviniente_viejo.getString(ParticipantesUtil.ACUSE_GENERADO));
		interviniente_nuevo.set(ParticipantesUtil.RECURSO, interviniente_viejo.getString(ParticipantesUtil.RECURSO));
		interviniente_nuevo.set(ParticipantesUtil.OBSERVACIONES, interviniente_viejo.getString(ParticipantesUtil.OBSERVACIONES));
		interviniente_nuevo.set(ParticipantesUtil.FECHA_ACUSE, interviniente_viejo.getDate(ParticipantesUtil.FECHA_ACUSE));
		interviniente_nuevo.set(ParticipantesUtil.MOTIVO_ACUSE, interviniente_viejo.getString(ParticipantesUtil.MOTIVO_ACUSE));
		interviniente_nuevo.set(ParticipantesUtil.RECURSO_TEXTO, interviniente_viejo.getString(ParticipantesUtil.RECURSO_TEXTO));
		interviniente_nuevo.set(ParticipantesUtil.ASISTE, interviniente_viejo.getString(ParticipantesUtil.ASISTE));
		interviniente_nuevo.set(ParticipantesUtil.TIPO_PODER, interviniente_viejo.getString(ParticipantesUtil.TIPO_PODER));
		interviniente_nuevo.set(ParticipantesUtil.FECHA_INI, interviniente_viejo.getDate(ParticipantesUtil.FECHA_INI));
		interviniente_nuevo.set(ParticipantesUtil.FECHA_FIN, interviniente_viejo.getDate(ParticipantesUtil.FECHA_FIN));
		interviniente_nuevo.set(ParticipantesUtil.SOLICITAR_OFERTA, interviniente_viejo.getString(ParticipantesUtil.SOLICITAR_OFERTA));
		interviniente_nuevo.set(ParticipantesUtil.SOLICITADA_OFERTA, interviniente_viejo.getString(ParticipantesUtil.SOLICITADA_OFERTA));
		interviniente_nuevo.set(ParticipantesUtil.CCC, interviniente_viejo.getString(ParticipantesUtil.CCC));
		interviniente_nuevo.set(ParticipantesUtil.DIR3, interviniente_viejo.getString(ParticipantesUtil.DIR3));
	
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
		
		if(StringUtils.isNotEmpty(numexp)) consulta += " WHERE NUMEXP = '" + numexp + "' ";

		if(StringUtils.isNotEmpty(sqlQuery)){
			if(StringUtils.isNotEmpty(numexp)) consulta += " AND ";
			else consulta += " WHERE ";
			consulta += "  ( " + sqlQuery + " ) ";
		}
		if(StringUtils.isNotEmpty(order)){
			consulta += " ORDER BY " + order;
		}
		
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
			LOGGER.error("Error al recuperar los participantes. consulta: " + consulta + ". " + e.getMessage(), e);
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

	public static IItemCollection getParticipantesByRol(IClientContext cct, String numexp, String rol) {
		return getParticipantesByRol(cct, numexp, rol, "");
	}
		
	public static IItemCollection getParticipantesByRol(IClientContext cct, String numexp, String rol, String orden) {
		IItemCollection partCol = null;
		try {
			partCol = ParticipantesUtil.getParticipantes(cct, numexp, ParticipantesUtil.ROL + " = '" + rol + "'", orden);
		} catch (ISPACException e) {
			LOGGER.error("ERROR al recuperar los participantes del expediente: " + numexp + " , y rol: " + rol + ". " + e.getMessage(), e);
			ArrayList<IItem> part = new ArrayList<IItem>();
			partCol = new ListCollection(part);			
		}
		
		return partCol;
	}
	//[eCenpri-Mayu] - FIN - Se eliminan dependencias cíclicas.
	
	public static boolean existeParticipante(IClientContext cct, String numexp, String nifParticipante) {
		boolean existe = false;
		try{
			IItemCollection nuevoParticipanteCol = ParticipantesUtil.getParticipantes( cct, numexp, ParticipantesUtil.NDOC + " = '" + nifParticipante + "'", "");
			
			Iterator<?> nuevoParticipanteIt = nuevoParticipanteCol.iterator();
			existe = nuevoParticipanteIt.hasNext();
		} catch( ISPACException e){
			LOGGER.error("ERROR al comprobar si el participante: " + nifParticipante + " existe en el expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return existe;
	}
	
	public static boolean tieneTrasladados(IClientContext cct, String numExp) throws ISPACException {
        boolean tiene = false;

        IItemCollection participantes = ParticipantesUtil.getParticipantesByRol(cct, numExp, ParticipantesUtil._TIPO_TRASLADO);
        Iterator<?> itParticipantes = participantes.iterator();
        
        if (itParticipantes.hasNext()) {
            tiene = true;
        }
        
        return tiene;
    }
}
