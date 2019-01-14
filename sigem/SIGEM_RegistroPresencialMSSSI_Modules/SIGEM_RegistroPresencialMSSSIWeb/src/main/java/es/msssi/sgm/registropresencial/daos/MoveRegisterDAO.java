/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que realiza el paso de un registro de un libro a otro.
 * 
 * @author cmorenog
 */
public class MoveRegisterDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(MoveRegisterDAO.class.getName());

    /**
     * mueve un registro de un libro a otro.
     * 
     * @param fdridOriginRegister
     *            registro a mover.
     * @param bookIdOrigin
     *            libro origen.
     * @param bookIdDestination
     *            libro destino.
     */
    @SuppressWarnings("unchecked")
    public void moveRegisterBook(Integer fdridOriginRegister,
	    Integer bookIdOrigin, Integer bookIdDestination) {
	LOG.trace("Entrando en MoveRegisterDAO.moveRegisterBook()");
	Integer idDestinationRegister = null;
	HashMap<String, Object> sqlParameters = null;
	List<Integer> idsDocList = null;
	List<Axpageh> axpagehList = null;
	List<Integer> idsExtList = null;
	Integer idExtDestination = null;
	Integer idDocDestination = null;
	try {
	    LOG.debug("COPIA DE REGISTRO CON ID: "+ fdridOriginRegister + " DEL LIBRO: "
		    +bookIdOrigin + " AL LIBRO: "+bookIdDestination);
	    // 1. Buscamos el id del nuevo registro destino
	    idDestinationRegister = (Integer) getSqlMapClientTemplate().queryForObject("MoveRegister.selectIdNextDestRegisterBook",
			bookIdDestination);
	    LOG.debug("ID DESTINATARIO: "+ idDestinationRegister);
	    // 2. Aumentamos 1 el id para el siguiente registro del libro destino
	    getSqlMapClientTemplate().update("MoveRegister.updateIdNextRegisterBook", bookIdDestination);
	    
	    sqlParameters = new HashMap<String, Object>();
	    sqlParameters.put("idDestinationBook", bookIdDestination);
	    sqlParameters.put("idDestinationRegister", idDestinationRegister);
	    sqlParameters.put("idOriginBook", bookIdOrigin);
	    sqlParameters.put("idOriginRegister", fdridOriginRegister);
	    
	    // 3. insert en el libro destino con el id dado y copiando el registro origen
	    getSqlMapClientTemplate().insert("MoveRegister.insertDestinationRegASF", sqlParameters);
	    
	    // 4. insert en las tablas AXFDRH con los datos origen
	    getSqlMapClientTemplate().insert("MoveRegister.insertDestinationRegAFDRH", sqlParameters);
	   
	    // 5. Recogemos los IDs de los documentos origen
	    idsDocList = (ArrayList<Integer>) getSqlMapClientTemplate().queryForList("MoveRegister.selectIdDocsOriginRegisterBook", sqlParameters);
	    
	    // 6. Por cada documento origen...
	    for (Integer idDocOrigin: idsDocList){
		// 6.1. Buscamos el id destino del documento
		idDocDestination = (Integer) getSqlMapClientTemplate().queryForObject("MoveRegister.selectIdNextDocDestRegisterBook",
				bookIdDestination);
		
		// 6.2. Aumentamos 1 el id para el siguiente documento del registro del libro destino
		getSqlMapClientTemplate().update("MoveRegister.updateIdNextDocRegisterBook", bookIdDestination);
	   
		
		// 6.3. insert en la tabla AXFDRH con los datos origen
		 sqlParameters.put("idDocOrigin", idDocOrigin);
		 sqlParameters.put("idDocDestination", idDocDestination);
		 getSqlMapClientTemplate().insert("MoveRegister.insertDestinationRegADOCH", sqlParameters);

		// 6.4. insert en la tabla AXPAGEH con los datos origen
		 getSqlMapClientTemplate().insert("MoveRegister.insertDestinationRegAPAGEH", sqlParameters);
		 
		// 6.5. Buscamos los ids de los fichero de volumen
		 axpagehList = (ArrayList<Axpageh>)getSqlMapClientTemplate().queryForList("MoveRegister.selectAxpageh", sqlParameters);
		 
		// 6.6. Buscamos los ids de los fichero de volumen
		 for (Axpageh page:axpagehList){
		     sqlParameters.put("idPagVol", page.getFileId());
		     // 6.6.1. modificamos el fichero en el volumen
		     getSqlMapClientTemplate().update("MoveRegister.updateVolFile", sqlParameters);
		     // 6.6.2. modificamos el repositorio de ficheros
		     getSqlMapClientTemplate().update("MoveRegister.updatePageRepository", sqlParameters);
		 }
		 
		 // 6.7. insertamos la informacion de la pagina del repositorio
		 getSqlMapClientTemplate().update("MoveRegister.updatePageInfo", sqlParameters);
		 
		 // 6.8. Borramos la informacion de la pagina origen del repositorio
		 getSqlMapClientTemplate().delete("MoveRegister.deletePageInfo", sqlParameters);
	    }
	    
	    // 7. Guardar el contador de IDS de las paginas
	    getSqlMapClientTemplate().insert("MoveRegister.insertDestinationRegAXNID", sqlParameters);
		
	    // 8. Modificamos los movimientos de modificaciones
	    getSqlMapClientTemplate().update("MoveRegister.updateDestinationModifReg", sqlParameters);
	    
	    // 9. Modificamos los interesados
	    getSqlMapClientTemplate().update("MoveRegister.updateDestinationRegInt", sqlParameters);
	    
	    // 10. modificamos el intercambio registral
	    getSqlMapClientTemplate().update("MoveRegister.updateDestinationRegExregIn", sqlParameters);

	    // 11. insertamos los datos extendidos
	    idsExtList = (ArrayList<Integer>)getSqlMapClientTemplate().queryForList("MoveRegister.selectIdsOriginRegisterBook", sqlParameters);

	    //12. Por cada dato extendido...
	    for (Integer idExtOrigen:idsExtList){
		// 12.1. Buscamos el id destino del extendido
		idExtDestination = (Integer)getSqlMapClientTemplate().queryForObject("MoveRegister.selectIdNextExtDestRegisterBook", bookIdDestination);
		
		// 12.2. Aumentamos 1 el id para el siguiente documento del registro del libro destino
		getSqlMapClientTemplate().update("MoveRegister.updateIdNextExtDestRegisterBook", bookIdDestination);
	   
		// 12.3. Insertamos el extendido
		sqlParameters.put("idExtOrigin", idExtOrigen);
		sqlParameters.put("idExtDestination", idExtDestination);
		getSqlMapClientTemplate().insert("MoveRegister.insertDestinationRegAXF", sqlParameters);
	    }
	    LOG.debug("FIN DEL PROCESO DE COPIA");
	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.MOVE_REGISTER_ERROR, exception);
	    Utils.redirectToErrorPage(null, null, exception);
	}
    }

}