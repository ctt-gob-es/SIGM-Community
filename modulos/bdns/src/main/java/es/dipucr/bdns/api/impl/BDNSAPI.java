package es.dipucr.bdns.api.impl;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import es.dipucr.bdns.dao.AnuncioBopBdnsDAO;

public class BDNSAPI {

	/** Logger de la clase. */
	private static final Logger LOGGER = Logger.getLogger(BDNSAPI.class);
	
	/** Constantes **/
	public final static String DIR3_DIPUCR = "L02000013";
	public final static String CONTADOR_PETICION_BDNS_NAME = "num_request_bdns";

	/**
	 * Creación de un nuevo anuncio
	 * 
	 * @param cct
	 * @param idAnuncio
	 * @param idPeticion
	 * @param timestamp
	 * @throws ISPACException
	 */
	public static void nuevoAnuncioBop(IClientContext cct, int idAnuncio,
			String idPeticion, String timestamp) throws ISPACException {

		DbCnt cnt = cct.getConnection();

		try {
			AnuncioBopBdnsDAO anuncioDao = new AnuncioBopBdnsDAO(cnt);
			anuncioDao.createNew(cnt, idAnuncio);
			anuncioDao.set("ID_PETICION", idPeticion);
			anuncioDao.set("FECHA", timestamp);
			anuncioDao.store(cnt);

		} catch (Exception e) {
			String error = "Error en BdnsAPI:nuevoAnuncio("	+ idAnuncio + ", " + idPeticion + ")";
			LOGGER.error(error, e);
			throw new ISPACException(error, e);
		} finally {
//			cct.releaseConnection(cnt);
		}
	}

	/**
	 * Devuelve todos los anuncios
	 * 
	 * @param instanceCircuitId
	 * @return
	 * @throws ISPACException
	 */
	public static IItemCollection getAnunciosBop(IClientContext cct)
			throws ISPACException {
		DbCnt cnt = cct.getConnection();
		try {
			return AnuncioBopBdnsDAO.getAnuncios(cnt).disconnect();
		} catch (Exception e) {
			String error = "Error en BdnsAPI:getAnunciosBop";
			LOGGER.error(error, e);
			throw new ISPACException(error, e);
		} finally {
//			cct.releaseConnection(cnt);
		}
	}

	/**
	 * Borra un anuncio bop de la BBDD
	 * 
	 * @param cct
	 * @param idAnuncio
	 * @throws ISPACException
	 */
	public static void borrarAnuncioBop(IClientContext cct, int idAnuncio)
			throws ISPACException {
		DbCnt cnt = cct.getConnection();
		try {
			AnuncioBopBdnsDAO anuncioDao = new AnuncioBopBdnsDAO(cnt, idAnuncio);
			anuncioDao.delete(cnt);
		} catch (Exception e) {
			String error = "Error en BdnsAPI:borrarAnuncioBop(" + idAnuncio + ")";
			LOGGER.error(error, e);
			throw new ISPACException(error, e);
		} finally {
//			cct.releaseConnection(cnt);
		}
	}
	
	/**
     * Devuelve el id de la nueva petición realizada
	 * @param cct 
     * @param entitiesAPI
     * @return
     * @throws ISPACException 
     */
	public static String getIdPeticion(ClientContext cct, IEntitiesAPI entitiesAPI) throws ISPACException {

		String strQuery = "WHERE VALOR = '" + CONTADOR_PETICION_BDNS_NAME + "'";
        IItemCollection itemCollection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
        String sIdPeticion = null;
        
        if (itemCollection.next()){
        	IItem item = (IItem) itemCollection.iterator().next();
        	int numPeticion = Integer.valueOf(item.getString("SUSTITUTO"));
        	sIdPeticion = DIR3_DIPUCR + "-" + numPeticion;
        	item.set("SUSTITUTO", String.valueOf(numPeticion + 1));
        	item.store(cct);
        }
        else{
        	throw new ISPACException("No existe ningún registro " + CONTADOR_PETICION_BDNS_NAME 
        			+ " en la tabla BOP_VLDTBL_CONTADORES");
        }
        return sIdPeticion;
	}
}
