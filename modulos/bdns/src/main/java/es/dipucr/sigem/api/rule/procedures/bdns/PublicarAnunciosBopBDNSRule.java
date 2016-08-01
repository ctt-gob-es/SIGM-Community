package es.dipucr.sigem.api.rule.procedures.bdns;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;

import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.bdns.boletinoficial.client.BDNSBopClient;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

/**
 * @author [dipucr-Felipe #304]
 * @date 07/07/2016
 * Remite la fecha de publicación de los anuncios de la BDNS
 */
public class PublicarAnunciosBopBDNSRule implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(PublicarAnunciosBopBDNSRule.class);
	
	/** Constantes **/
	public static String BOP_SEDE_URL_ANUNCIO_VARNAME = "BOP_SEDE_URL_ANUNCIO";
	public static String URL_ANUNCIO_IDDOC_TAGNAME = "[ID_DOCUMENTO]";
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			String strQuery = null;
			IItemCollection collection = null;
			
			//Obtenemos los datos del boletín actual
	        strQuery = "WHERE NUMEXP='" + numexp + "'";
	        collection = entitiesAPI.queryEntities("BOP_PUBLICACION", strQuery);
	        IItem itemBop = (IItem) collection.iterator().next();
	        Date dFechaPublicacion = itemBop.getDate("FECHA");
	        
	        //Obtenemos el código cotejo del documento de BOP firmado
	        collection = BopUtils.getBopsFirmados(rulectx);
			IItem itemDocBopFirmado = (IItem) collection.iterator().next();
			String cve = itemDocBopFirmado.getString("COD_COTEJO");
			
			//Obtenemos los documentos generados y sus datos de la solicitud, que estén en la BDNS
			strQuery = "WHERE DOC.NUMEXP = '" + numexp + "' "
					+ "AND SOL.ID_DOCUMENTO = DOC.ID "
					+ "AND SOL.NUMEXP = BDNS.NUMEXP "
					+ "ORDER BY SOL.NUM_ANUNCIO_BOP";

			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable("BOP_SOLICITUD", "SOL");
			factory.addTable("SPAC_DT_DOCUMENTOS", "DOC");
			factory.addTable("BOP_SOLICITUD_BDNS", "BDNS");

			CollectionDAO collectionJoin = factory.queryTableJoin(cct.getConnection(), strQuery);
			collectionJoin.disconnect();
			
			BDNSBopClient client = new BDNSBopClient();
			
			String urlBaseAnuncio = ConfigurationMgr.getVarGlobal(cct, BOP_SEDE_URL_ANUNCIO_VARNAME);

			//Recorremos los registros recuperados
			while (collectionJoin.next()){
				
				IItem itemDatosAnuncio = (IItem) collectionJoin.value();
				
				String idPeticion = BDNSAPI.getIdPeticion(cct, entitiesAPI);
				String idAnuncio = itemDatosAnuncio.getString("BDNS:ID_ANUNCIO");
				String numexpAnuncio = itemDatosAnuncio.getString("SOL:NUMEXP");
				String idAnuncioBOP = itemDatosAnuncio.getString("SOL:NUM_ANUNCIO_BOP");
				String idDocumento = itemDatosAnuncio.getString("DOC:ID");
				String urlAnuncio = urlBaseAnuncio.replace(URL_ANUNCIO_IDDOC_TAGNAME, idDocumento);
				
				client.publicarAnuncio(idPeticion, idAnuncio, dFechaPublicacion, cve, idAnuncioBOP, urlAnuncio, numexpAnuncio);
				logger.warn("Publicado anuncio " + idAnuncio + " del expediente " + numexpAnuncio + ". URL: "  + urlAnuncio);
			}
			
		}
		catch(Exception e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
