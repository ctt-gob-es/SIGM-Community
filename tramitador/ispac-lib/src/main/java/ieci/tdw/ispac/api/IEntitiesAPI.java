package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.ispaclib.app.EntityApp;
import ieci.tdw.ispac.ispaclib.dao.entity.IEntityDef;
import ieci.tdw.ispac.ispaclib.entity.EntityResources;
import ieci.tdw.ispac.ispaclib.search.vo.SearchResultVO;

import java.util.List;
import java.util.Map;

public interface IEntitiesAPI {

    /**
     * Obtiene una entidad sin ning�n valor de registro a partir del identificador de la entidad.
     *
     * @param entityId identificador de la entidad
     * @return IItem registro vac�o de la entidad
     * @throws ISPACException
     */
    public IItem getEntity(int entityId) 
    	throws ISPACException;
    
    /**
     * Obtiene una entidad sin ning�n valor de registro a partir de la definici�n de la entidad.
     *
     * @param entityDef definici�n de la entidad
     * @return IItem registro vac�o de la entidad
     * @throws ISPACException
     */
    public IItem getEntity(IEntityDef entitydef) 
    	throws ISPACException;

    /**
     * Obtiene una entidad sin ning�n valor de registro a partir del nombre de la entidad.
     *
     * @param entityName nombre de la entidad
     * @return IItem registro vac�o de la entidad
     * @throws ISPACException
     */
    public IItem getEntity(String entityName) 
    	throws ISPACException;

    /**
     * Obtiene la descripci�n de un campo de una entidad catalogada. 
     * @param entityName Nombre de la entidad
     * @param fieldName Nombre del campo de la entidad
     * @return Property informaci�n del campo.
     * @throws ISPACException
     */
    public Property getEntityFieldProperty(String entityName, String fieldName) throws ISPACException;

    /**
     * Crea un nuevo registro para la entidad a partir del identificador de la entidad y
     * establece el n�mero de secuencia en el campo clave de la entidad.
     *
     * @param entityId identificador de la entidad
     * @return IItem registro de la entidad con clave
     * @throws ISPACException
     */
    public IItem createEntity(int entityId) 
    	throws ISPACException;
    
    /**
     * Crea un nuevo registro para la entidad a partir del nombre de la entidad
     * establece el n�mero de secuencia en el campo clave de la entidad y el
     * n�mero de expediente en el campo n�mero de expediente de la entidad
     * (SPAC_CT_ENTIDADES - CAMPO_NUMEXP) si tiene valor.
	 * 
     * @param entityname nombre de la entidad
     * @param numexp n�mero de expediente
     * @return IItem registro de la entidad con clave y n�mero de expediente si tiene
     * @throws ISPACException
	 */
    public IItem createEntity(String entityname, String numexp) 
    	throws ISPACException;
    
    /**
     * Obtiene el registro de una entidad.
     *
     * @param entityId identificador de la entidad
     * @param entityRegId identificador del registro de la entidad
     * @return IItem registro con datos de la entidad
     * @throws ISPACException
     */
    public IItem getEntity(int entityId, int entityRegId) 
    	throws ISPACException;

    
    /**
     * Obtiene el registro de una entidad.
     *
     * @param entityId nombre de la entidad
     * @param entityRegId identificador del registro de la entidad
     * @return IItem registro con datos de la entidad
     * @throws ISPACException
     */
    public IItem getEntity(String entityName, int entityRegId) 
	throws ISPACException;    
    
    /**
     * Obtiene el registro de una entidad.
     *
     * @param entityDef definici�n de la entidad
     * @param entityRegId identificador del registro de la entidad
     * @return IItem registro con datos de la entidad
     * @throws ISPACException
     */
    public IItem getEntity(IEntityDef entitydef, int entityRegId) 
    	throws ISPACException;

    /**
     * Obtiene una colecci�n de entidades resultado de una consulta.
     *
     * @param entityId identificador de la entidad
     * @param query b�squeda a realizar sobre los registros de la entidad
     * @return IItemCollection colecci�n de objetos EntityDAO resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntities(int entityId, String query)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n de entidades resultado de una consulta
     * bloqueando los registros de la tabla de la entidad.
     *
     * @param entityId identificador de la entidad
     * @param query b�squeda a realizar sobre los registros de la entidad
     * @return IItemCollection colecci�n de objetos EntityDAO resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntitiesForUpdate(int entityId, String query)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n de entidades resultado de una consulta.
     *
     * @param entityName Nombre de la entidad (tabla asociada)
     * @param query b�squeda a realizar sobre los registros de la entidad
     * @return IItemCollection colecci�n de objetos EntityDAO resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntities(String entityName, String query)
    	throws ISPACException;

    /**
     * @param entityName Nombre de la entidad (tabla asociada) 
     * @param query b�squeda a realizar sobre los registros de la entidad
     * @return n�mero de registro eliminados
     * @throws ISPACException
     */
    public int deleteEntities(String entityName, String query)
    	throws ISPACException;
    
    /**
     * 
     * @param entityName Nombre de la entidad
     * @param sqlBase consulta sql s�lo con la clausula select y where
     * @param order por lo que quiera ordenar el usuario, en caso de que se vaya a usuar la clausula order by
     * @param limit n�mero m�ximo de registros que va a devolver la consulta
     * @return Colecci�n de objetos EntityDAO resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntities(String entityName, String sqlBase, String order, int limit)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n de entidades resultado de una consulta.
     *
     * @param entityDef definici�n de la entidad
     * @param query b�squeda a realizar sobre los registros de la entidad
     * @return IItemCollection colecci�n de objetos EntityDAO resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntities(IEntityDef entityDef, String query)
    	throws ISPACException;
    
    /**
     * 
     * @param entityName Nombre de la entidad sobre la que se va a realizar la consulta
     * @param query b�squeda a realizar sobre los registros de la entidad
     * @return int n�mer de objetos EntityDAO que cumplen la consulta.
     * @throws ISPACException 
     */
    public int countResultQuery(String entityName, String query) throws ISPACException;

    /**
     * Obtiene el n�mero de entidades que cumplen la consulta.
     *
     * @param entityId identificador de la entidad
     * @param query b�squeda a realizar sobre los registros de la entidad
     * @return int n�mero de objetos EntityDAO que cumplen la consulta
     * @throws ISPACException
     */
    public int countEntities(int entityId, String query)
        throws ISPACException;

    /**
     * Obtiene el n�mero de entidades que intervienen en el proceso a partir de su n�mero de expediente.
     *
     * @param entityId identificador de la entidad
     * @param numexp n�mero de expediente
     * @return int n�mero de objetos EntityDAO existentes
     * @throws ISPACException
     */
    public int countProcessEntities(int entityId, String numexp)
        throws ISPACException;
    
    /**
     * Obtiene el n�mero de documentos asociados a la fase que est�n pendientes de firma.
     * @param stageId Identificador de la fase activa.
     * @return N�mero de documentos
     * @throws ISPACException si ocurre alg�n error.
     */
    public int countStageDocumentsInSignCircuit(int stageId) 
    	throws ISPACException;
    
    /**
     * Obtiene el n�mero de documentos asociados al tr�mite que est�n pendientes de firma.
     * @param taskId Identificador del tr�mite activo.
     * @return N�mero de documentos
     * @throws ISPACException si ocurre alg�n error.
     */
    public int countTaskDocumentsInSignCircuit(int taskId) 
    	throws ISPACException;
    
    /**
     * Obtiene el n�mero de documentos asociados al tr�mite que est�n pendientes de descarga
     * del portafirmas por errores en el procesamiento de eventos
     * @param taskId Identificador del tr�mite activo.
     * @return N�mero de documentos
     * @throws ISPACException si ocurre alg�n error.
     */
    public int countTaskDocumentsErrorPortafirmas(int taskId) 
    	throws ISPACException;

    /**
     * <p>Permite especificar una consulta sobre una o m&aacute;s entidades o tablas.
     * Para ello es preciso definir un mapa con el prefijo que se quiere asignar a
     * las tablas que intervienen la consulta. El resultado es una colecci&oacute;n de IItems
     * compuestos por el cada una de las filas resultado del join con las propiedades
     * calificadas seg&uacute;n el prefijo asociado con anterioridad
     * Los IItems resultantes de la consulta no son actualizables mediante el m�todo store().
     *  </p>
     * <br>
     * <p>
     * Ejemplo: Para realizar el siguiente join con el api de entidades.<br><br>
     * <tt>
     * SELECT * FROM SPAC_PROCESOS PROCESO, SPAC_FASES FASE WHERE PROCESO.ID = FASE.ID_EXP
     * </tt><br><br>
     * Se utilizar�a el siguiente c�digo.<br>
     * <pre>
     * tableentitymap.put("PROCESO","SPAC_PROCESOS");
     * tableentitymap.put("FASE","SPAC_FASES");
     *
     * IItemCollection consulta;
     * consulta=entityAPI.queryJoinEntities(tableentitymap," WHERE PROCESO.ID = FASE.ID_EXP ");
     *</pre>
     *<br><br>
     * Para acceder a las propiedades de los IItems resultado de la consulta
     * hay que calificarlas adecuadamente utilizando su prefijo
     * <br><br>
     * <pre>
     * consulta.getString("PROCESO:NUMEXP");
     * consulta.getString("FASE:NOMBRE");
     * </pre>
     *</p>
     * <p>
     * Es posible especificar para el join tanto tablas como entidades. Basta con asignar al
     * mapa de prefijos el nombre de la tabla o un objeto Integer o Long con el identificador
     * de la entidad.
     * </p> <br><br>
     * <pre>
     * tableentitymap.put("PROCESO","SPAC_PROCESOS");
     * tableentitymap.put("EXPED",new Integer(ISPACEntities.DT_ID_EXPEDIENTES);
     * tableentitymap.put("DATOS",new Integer(23);
     *</pre>
     *
     * @param tableentitymap mapa con la informaci�n necesaria de las tablas o entidades
     * 		  que intervienen en el join
     * @param query b�squeda a realizar sobre las entidades especificada mediante SQL
     * @return IItemCollection colecci�n de objetos resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntities(Map tableentitymap, String query)
    	throws ISPACException;

    /**
     * <p>Permite especificar una consulta sobre una o m&aacute;s entidades o tablas.
     * Para ello es preciso definir un mapa con el prefijo que se quiere asignar a
     * las tablas que intervienen la consulta. El resultado es una colecci&oacute;n de IItems
     * compuestos por el cada una de las filas resultado del join con las propiedades
     * calificadas seg&uacute;n el prefijo asociado con anterioridad</p>
     * <br>
     * <p>
     * Ejemplo: Para realizar el siguiente join con el api de entidades.<br><br>
     * <tt>
     * SELECT * FROM SPAC_PROCESOS PROCESO, SPAC_FASES FASE WHERE PROCESO.ID = FASE.ID_EXP
     * </tt><br><br>
     * Se utilizar�a el siguiente c�digo.<br>
     * <pre>
     * tableentitymap.put("PROCESO","SPAC_PROCESOS");
     * tableentitymap.put("FASE","SPAC_FASES");
     *
     * IItemCollection consulta;
     * consulta=entityAPI.queryJoinEntities(tableentitymap," WHERE PROCESO.ID = FASE.ID_EXP ");
     *</pre>
     *<br><br>
     * Para acceder a las propiedades de los IItems resultado de la consulta
     * hay que calificarlas adecuadamente utilizando su prefijo
     * <br><br>
     * <pre>
     * consulta.getString("PROCESO:NUMEXP");
     * consulta.getString("FASE:NOMBRE");
     * </pre>
     *</p>
     * <p>
     * Es posible especificar para el join tanto tablas como entidades. Basta con asignar al
     * mapa de prefijos el nombre de la tabla o un objeto Integer o Long con el identificador
     * de la entidad.
     * </p> <br><br>
     * <pre>
     * tableentitymap.put("PROCESO","SPAC_PROCESOS");
     * tableentitymap.put("EXPED",new Integer(ISPACEntities.DT_ID_EXPEDIENTES);
     * tableentitymap.put("DATOS",new Integer(23));
     * </pre>
     *
     * @param tableentitymap mapa con la informaci�n necesaria de las tablas o entidades
     * 		  que intervienen en el join
     * @param query b�squeda a realizar sobre las entidades especificada mediante SQL
     * @param distinct devuelve filas �nicas
     * @return IItemCollection colecci�n de objetos resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntities(Map tableentitymap, String query,boolean distinct)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n de entidades resultado de una consulta.
     *
     * @param tablename tabla de la entidad
     * @param keyproperty campo clave de la entidad
     * @param sequence secuencia de la entidad
     * @param query b�squeda a realizar sobre la entidad
     * @return IItemCollection colecci�n de objetos EntityDAO resultado de la consulta
     * @throws ISPACException
     */
    public IItemCollection queryEntities(String tablename,String keyproperty,String sequence,String query)
        throws ISPACException;

    /**
     * Obtiene una entidad asociada a un procedimiento.
     *
     * @param procedureId identificador del procedimiento
     * @param entityId identificador de la entidad
     * @return IItem objeto EntityProcedureDAO con la entidad del procedimiento
     * @throws ISPACException
     */
    public IItem getProcedureEntity(int procedureId, int entityId)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con las entidades asociadas a un procedimiento.
     *
     * @param procedureId identificador del procedimiento
     * @return IItemCollection colecci�n de objetos EntityProcedureDAO de entidades del procedimiento
     * @throws ISPACException
     */
    public IItemCollection getProcedureEntities(int procedureId)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con las entidades asociadas a un procedimiento
     * y que est�n visibles para la fase o tr�mite del procedimiento.
     *
     * @param procedureId identificador del procedimiento
     * @return IItemCollection colecci�n de objetos EntityProcedureDAO de entidades del procedimiento
     * @throws ISPACException
     */
    public IItemCollection getProcedureEntities(int procedureId, int stagePcdId, int taskPcdId)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con las entidades asociadas a una fase de un procedimiento.
     *
     * @param procedureId identificador del procedimiento
     * @param stageId identificador de la fase en el procedimiento
     * @return IItemCollection colecci�n de objetos EntityProcedureDAO de entidades de la fase en el procedimiento
     * @throws ISPACException
     */
    public IItemCollection getStageEntities(int procedureId, int stagePcdId)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con las entidades asociadas a un tr�mite de un procedimiento.
     *
     * @param procedureId identificador del procedimiento
     * @param taskId identificador del tr�mite en el procedimiento
     * @return IItemCollection colecci�n de objetos EntityProcedureDAO de entidades del tr�mite en el procedimiento
     * @throws ISPACException
     */
    public IItemCollection getTaskEntities(int procedureId, int taskId)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los registros de una entidad de un expediente concreto,
     * a partir del nombre de la entidad y el n�mero de expediente.
     *
     * @param entityname nombre de la entidad
     * @param numexp n�mero de expediente
     * @return IItemCollection collecci�n de objetos CTEntityDAO de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getEntities(String entityname, String numexp)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente.
     *
     * @param entityId identificador de la entidad
     * @param numexp n�mero de expediente
     * @return IItemCollection collecci�n de objetos CTEntityDAO de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getEntities(int entityId, String numexp)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los registros de una entidad de un expediente concreto,
     * a partir del nombre de la entidad y el n�mero de expediente, y una consulta para
     * seleccionar los registros.
     *
     * @param entityname nombre de la entidad
     * @param numexp n�mero de expediente
     * @param sqlQuery consulta a realizar para seleccionar los registros de entidades
     * @return IItemCollection collecci�n de objetos CTEntityDAO de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getEntities(String entityname, String numexp, String sqlQuery)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente, y una consulta para
     * seleccionar los registros.
     *
     * @param entityId identificador de la entidad
     * @param numexp n�mero de expediente
     * @param sqlQuery consulta a realizar para seleccionar los registros de entidades
     * @return IItemCollection collecci�n de objetos CTEntityDAO de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getEntities(int entityId, String numexp, String sqlQuery)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n ordenada con los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente, y una consulta para
     * seleccionar los registros.
     *
     * @param entityId identificador de la entidad
     * @param numexp n�mero de expediente
     * @param sqlQuery consulta a realizar para seleccionar los registros de entidades
     * @param order criterio de ordenaci�n para la consulta
     * @return CollectionDAO collecci�n de objetos CTEntityDAO de entidades del expediente
     */
    public IItemCollection getEntitiesWithOrder(int entityId, String numexp, String sqlQuery, String order)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK) y el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) de los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente.
     *
     * @param entityId identificador de la entidad
     * @param numexp n�mero de expediente
     * @return IItemCollection colecci�n de objetos de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getSchemeEntities(int entityId, String numexp)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK) y el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) de los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente.
     *
     * @param entityDef definici�n de la entidad
     * @param numexp n�mero de expediente
     * @return IItemCollection colecci�n de objetos de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getSchemeEntities(IEntityDef entitydef, String numexp)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK) y el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) de los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente, y una consulta para
     * seleccionar los registros.
     * Adem�s, se pueden a�adir propiedades extra que formen parte de la entidad en su representaci�n
     * para el esquema.
     *
     * @param entityId identificador de la entidad
     * @param numexp n�mero de expediente
     * @param query consulta a realizar para seleccionar los registros de entidades
     * @param extraprop propiedades a a�adir al esquema
     * @return CollectionDAO colecci�n de objetos de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getSchemeEntities(int entityId, String numexp, String query, Property[] extraprop)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK) y el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) de los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente, y una consulta para
     * seleccionar los registros.
     * Adem�s, se pueden a�adir propiedades extra que formen parte de la entidad en su representaci�n
     * para el esquema.
     *
     * @param entityDef definici�n de la entidad
     * @param numexp n�mero de expediente
     * @param query consulta a realizar para seleccionar los registros de entidades
     * @param extraprop propiedades a a�adir al esquema
     * @return CollectionDAO colecci�n de objetos de entidades del expediente
     * @throws ISPACException
     */
    public IItemCollection getSchemeEntities(IEntityDef entitydef, String numexp, String query, Property[] extraprop)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK) y el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) de los registros de una entidad de un expediente concreto,
     * a partir del identificador de la entidad y el n�mero de expediente, y una consulta para
     * seleccionar los registros.
     * Adem�s, se pueden a�adir propiedades extra que formen parte de la entidad en su representaci�n
     * para el esquema.
     *
     * @param entityDef definici�n de la entidad
     * @param numexp n�mero de expediente
     * @param query consulta a realizar para seleccionar los registros de entidades
     * @param extraprop propiedades a a�adir al esquema
     * @param orderBy Indica el campo por el que se quiere ordenar
     * @param desc Indica si se quiere ordernar descencientemente
     * @return CollectionDAO colecci�n de objetos de entidades del expediente ordenados por el campo orderBy
     * @throws ISPACException
     */
    public IItemCollection getSchemeEntities(IEntityDef entitydef, String numexp, String query, Property[] extraprop ,String orderBy , boolean desc)
    	throws ISPACException;
    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK), el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) y el estado para los documentos
     * con estado FINALIZADO de un expediente concreto, a partir n�mero de expediente.
     *
     * @param numexp n�mero de expediente
     * @return IItemCollection colecci�n de documentos asociados al expediente
     * @throws ISPACException
     */
    public IItemCollection getSchemeProcessDocuments(String numexp)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK), el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) y el estado para los documentos
     * asociados a una fase de un expediente concreto cuyo autor es el usuario conectado,
     * a partir n�mero de expediente y el idenfificador de la fase. 
     *
     * @param numexp n�mero de expediente
     * @param stageId identificador de la fase en el expediente
     * @return IItemCollection colecci�n de documentos asociados a la fase
     * @throws ISPACException
     */
    public IItemCollection getSchemeStageDocuments(String numexp, int stageId) 
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK), el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) y el estado para los documentos de un tipo
     * asociados a una fase de un expediente concreto cuyo autor es el usuario conectado,
     * a partir n�mero de expediente y el idenfificador de la fase. 
     *
     * @param numexp n�mero de expediente
     * @param stageId identificador de la fase en el expediente
     * @param docTypeId identificador del tipo de documento
     * @return IItemCollection colecci�n de documentos de un tipo asociados a la fase
     * @throws ISPACException
     */
    public IItemCollection getSchemeStageDocuments(String numexp, int stageId, int docTypeId)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK), el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) y el estado para los documentos
     * asociados a un tr�mite de un expediente concreto cuyo autor es el usuario conectado,
     * a partir n�mero de expediente y el idenfificador del tr�mite.
     *
     * @param numexp n�mero de expediente
     * @param taskId identificador del tr�mite en el expediente
     * @return IItemCollection colecci�n de documentos asociados al tr�mite
     * @throws ISPACException
     */
    public IItemCollection getSchemeTaskDocuments(String numexp, int taskId)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con el identificador (SPAC_CT_ENTIDADES - CAMPO_PK), el esquema
     * o campo diferenciador (SPAC_CT_ENTIDADES - SCHEMA_EXPR) y el estado para los documentos de un tipo
     * asociados a un tr�mite de un expediente concreto cuyo autor es el usuario conectado,
     * a partir n�mero de expediente y el idenfificador del tr�mite.
     *
     * @param numexp n�mero de expediente
     * @param taskId identificador del tr�mite en el expediente
     * @param docTypeId identificador del tipo de documento
     * @return IItemCollection colecci�n de documentos de un tipo asociados al tr�mite
     * @throws ISPACException
     */
    public IItemCollection getSchemeTaskDocuments(String numexp, int taskId,int documentId)
    	throws ISPACException;

    /**
     * Obtiene la descripci�n de una entidad cat�logada. 
     * Consultar <tt>SPAC_CT_ENTIDADES<tt> en el m�delo de datos.
     *
     * @param entityId identificador de la entidad
     * @return IItem definici�n de la entidad
     * @throws ISPACException
     */
    public IItem getCatalogEntity(int entityId)
    	throws ISPACException;
    
    /**
     * Obtiene la descripci�n de una entidad cat�logada. 
     * Consultar <tt>SPAC_CT_ENTIDADES<tt> en el m�delo de datos.
     *
     * @param entityName Nombre de la entidad (tabla asociada)
     * @return IItem definici�n de la entidad
     * @throws ISPACException
     */
    public IItem getCatalogEntity(String entityName)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con todas las descripciones de las entidades existentes en el cat�logo.
     * Consultar <tt>SPAC_CT_ENTIDADES<tt> en el m�delo de datos.
     *
     * @return IItemCollection colecci�n con todas las entidades catalogadas
     * @throws ISPACException
     */
    public IItemCollection getCatalogEntities()
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con las descripciones de las entidades existentes en el cat�logo
     * a partir de una consulta.
     * Consultar <tt>SPAC_CT_ENTIDADES<tt> en el m�delo de datos.
     *
     * @param query b�squeda a realizar sobre las entidades del cat�logo
     * @return IItemCollection colecci�n con las entidades catalogadas que cumplen la consulta
     * @throws ISPACException
     */
    public IItemCollection getCatalogEntities(String query)
    	throws ISPACException;

    
    // EXPEDIENTES //

    /**
     * Devuelve el registro de la entidad de expedientes.
     *
     * @param regentityId identificador del registro en la entidad de expedientes
     * @return IItem registro del expediente solicitado
     * @throws ISPACException
     */
    public IItem getExpedient(int regentityId) 
    	throws ISPACException;

    /**
     * Devuelve el registro de la entidad de expedientes 
     * a partir del n�mero de expediente.
     *
     * @param numexp n�mero de expediente
     * @return IItem registro del expediente solicitado
     * @throws ISPACException
     */
    public IItem getExpedient(String numexp) 
    	throws ISPACException;

    /**
     * Obtiene los registros de la entidad de expedietes
     * a partir del n�mero de registro.
     * 
     * @param regNum N�mero de registro.
     * @return Lista de expedientes.
     * @throws ISPACException si ocurre alg�n error.
     */
    public IItemCollection getExpedientsByRegNum(String regNum)
		throws ISPACException;
    
    /**
     * Obtiene los registros hist�ricos de la entidad de expedietes
     * a partir del n�mero de registro.
     * 
     * @param regNum N�mero de registro.
     * @return Lista de expedientes.
     * @throws ISPACException si ocurre alg�n error.
     */
    public IItemCollection getExpedientsByRegNumHistorico(String regNum)
		throws ISPACException;
    
    // INTERVINIENTES //
    
    /**
     * Devuelve el registro de la entidad de intervinientes.
     *
     * @param entityRegId identificador del registro en la entidad de intervinientes
     * @return IItem registro del interviniente solicitado
     * @throws ISPACException
     */
    public IItem getParticipant(int entityRegId) 
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los intervinientes asociados a un expediente concreto,
     * a partir del n�mero de expediente y una consulta para seleccionar los intervinientes.
     *
     * @param numexp n�mero de expediente
     * @param sqlQuery consulta a realizar para seleccionar los intervinientes
     * @param order criterio de ordenaci�n
     * @return IItemCollection colecci�n de intervientes del expediente
     * @throws ISPACException
     */
    public IItemCollection getParticipants(String numexp, String sqlQuery, String order) 
    	throws ISPACException;


    //   DOCUMENTOS //
    
    /**
     * Devuelve el registro de la entidad de documentos.
     *
     * @param entityRegId identificador del registro en la entidad de documentos
     * @return IItem registro del documento solicitado
     * @throws ISPACException
     */
    public IItem getDocument(int entityRegId) 
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con los documentos asociados a un expediente concreto,
     * a partir del n�mero de expediente y una consulta para seleccionar los documentos.
     *
     * @param numexp n�mero de expediente
     * @param sqlQuery consulta a realizar para seleccionar los documentos
     * @param order criterio de ordenaci�n
     * @return IItemCollection colecci�n de documentos del expediente
     * @throws ISPACException
     */
    public IItemCollection getDocuments(String numexp, String sqlQuery, String order)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los documentos asociados a un registro de una entidad.
     *
     * @param entityId identificador de la entidad en el cat�logo
     * @param entityRegId identificador del registro de la entidad
     * @return IItemCollection colecci�n de documentos del registro de la entidad
     * @throws ISPACException
     */
    public IItemCollection getEntityDocuments(int entityId, int entityRegId)
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los documentos generados con una determinada plantilla
     * y asociados a un expediente concreto que est�n asociados a tr�mites abiertos,  
     * a partir del n�mero de expediente y el identificador de la plantilla.
     * 
     * @param numexp n�mero de expediente
     * @param idTemplate identificador de la plantilla con la que se gener� el documento
     * @return IItemCollection colecci�n de documentos del expediente
     * @throws ISPACException
     */
	public IItemCollection getDocumentsOpenedTask(String numexp, Integer idTemplate)
		throws ISPACException;
    
    
    /**
     * Obtiene una colecci�n con los documentos generados con una determinada plantilla
     * y asociados a un expediente concreto,  
     * a partir del n�mero de expediente y el identificador de la plantilla.
     * 
     * @param numexp n�mero de expediente
     * @param idTemplate identificador de la plantilla con la que se gener� el documento
     * @return IItemCollection colecci�n de documentos del expediente
     * @throws ISPACException
     */
    public IItemCollection getDocuments(String numexp, int idTemplate) 
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los documentos creados en un tr�mite de un expediente,
     * a partir del n�mero de expediente y del identificador del tr�mite.
     * 
     * @param numexp n�mero del expediente
     * @param taskId identificador del tr�mite en el expediente
     * @return IItemCollection colecci�n de documentos creados en el tr�mite
     * @throws ISPACException
     */
    public IItemCollection getTaskDocuments(String numexp, int taskId) 
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los documentos creados en una fase de un expediente,
     * a partir del n�mero de expediente y del identificador de la fase.
     * 
     * @param numexp n�mero del expediente
     * @param stageId identificador de la fase en el expediente
     * @return IItemCollection colecci�n de documentos creados en la fase
     * @throws ISPACException
     */
    public IItemCollection getStageDocuments(String numexp, int stageId) 
    	throws ISPACException;
    
    /**
     * Obtiene una colecci�n con los documentos creados en una fase de un expediente,
     * a partir del identificador de la fase.
     * 
     * @param stageId identificador de la fase en el expediente
     * @return IItemCollection colecci�n de documentos creados en la fase
     * @throws ISPACException
     */
    public IItemCollection getStageDocuments(int stageId) throws ISPACException;
    
    // APLICACIONES QUE PRESENTAN LAS ENTIDADES //

    /**
     * Obtiene la aplicaci�n solicitada para la entidad y la devuelve debidamente cargada
     * con los datos del registro a partir del identificador suministrado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     *
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param appId identificador de la aplicaci�n si ya viene especificada
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getEntityApp(String numExp, int procedureId, int appId, int entityId, int entityRegId, String path, int urlKey)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n solicitada para la entidad y la devuelve debidamente cargada
     * con los datos del registro a partir del identificador suministrado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     *
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param appId identificador de la aplicaci�n si ya viene especificada
     * @param entitydef definici�n de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getEntityApp(String numExp, int procedureId, int appId, IEntityDef entitydef, int entityRegId, String path, int urlKey)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n asociada a la entidad para el procedimiento suministrado y
     * la devuelve debidamente cargada con los datos del registro indicado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     *
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @param params par�metros modificables (FRM_READONLY = formulario en s�lo lectura)
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getProcedureEntityApp(String numExp, int procedureId, int entityId, int entityRegId, String path, int urlKey, Map params)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n asociada a la entidad para el procedimiento suministrado y
     * la devuelve debidamente cargada con los datos del registro indicado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     *
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param entityDef definici�n de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @param params par�metros modificables (FRM_READONLY = formulario en s�lo lectura)
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getProcedureEntityApp(String numExp, int procedureId, IEntityDef entitydef, int entityRegId, String path, int urlKey, Map params)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n asociada a la entidad para la fase suministrada y
     * la devuelve debidamente cargada con los datos del registro indicado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     * 
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param stagePcdId identificador de la fase en el procedimiento para la que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @param params par�metros modificables (FRM_READONLY = formulario en s�lo lectura)
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getStageEntityApp(String numExp, int procedureId, int stagePcdId, int entityId, int entityRegId, String path, int urlKey, Map params)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n asociada a la entidad para la fase suministrada y
     * la devuelve debidamente cargada con los datos del registro indicado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     * 
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param stagePcdId identificador de la fase en el procedimiento para la que se busca la aplicaci�n
     * @param entityDef definici�n de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @param params par�metros modificables (FRM_READONLY = formulario en s�lo lectura)
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getStageEntityApp(String numExp, int procedureId, int stagePcdId, IEntityDef entitydef, int entityRegId, String path, int urlKey, Map params)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n asociada a la entidad para el tr�mite suministrado y
     * la devuelve debidamente cargada con los datos del registro indicado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     * 
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param taskPcdId identificador del tr�mite en el procedimiento para la que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @param noDefault condici�n de no obtener el formulario por defecto (ID_TRAMITE = 0)
     * @param params par�metros modificables (FRM_READONLY = formulario en s�lo lectura)
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getTaskEntityApp(String numExp, int procedureId, int taskPcdId, int entityId, int entityRegId, String path, int urlKey, boolean noDefault, Map params)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n asociada a la entidad para el tr�mite suministrado y
     * la devuelve debidamente cargada con los datos del registro indicado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     * 
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param taskPcdId identificador del tr�mite en el procedimiento para la que se busca la aplicaci�n
     * @param entityDef definici�n de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @param noDefault condici�n de no obtener el formulario por defecto (ID_TRAMITE = 0)
     * @param params par�metros modificables (FRM_READONLY = formulario en s�lo lectura)
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getTaskEntityApp(String numExp, int procedureId, int taskPcdId, IEntityDef entitydef, int entityRegId, String path, int urlKey, boolean noDefault, Map params)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n solicitada para la entidad.
	 * La aplicaci�n no contiene ning�n dato de registro
	 * ya que no se ha especificado el identificador. 
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     * 
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param appId identificador de la aplicaci�n si ya viene especificada
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getEntityApp(String numexp, int procedureId, int appId, int entityId, String path, int urlKey)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n solicitada para la fase suministrada.
	 * La aplicaci�n no contiene ning�n dato de registro
	 * ya que no se ha especificado el identificador. 
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     * 
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param stagePcdId identificador de la fase en el procedimiento para la que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    /*
    public EntityApp getStageEntityApp(String numExp, int procedureId, int stagePcdId, int entityId, String path, int urlKey)
    	throws ISPACException;
    */
    
    /**
     * Obtiene la aplicaci�n solicitada para el tr�mite suministrado.
	 * La aplicaci�n no contiene ning�n dato de registro
	 * ya que no se ha especificado el identificador. 
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     * 
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param taskPdcId identificador del tr�mite en el procedimiento para la que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @param noDefault condici�n de no obtener el formulario por defecto (id_tramite=0)
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    /*
    public EntityApp getTaskEntityApp(String numExp, int procedureId, int taskPcdId, int entityId, String path, int urlKey, boolean noDefault)
    	throws ISPACException;
    */
    
    /**
     * Obtiene la aplicaci�n asociada a la entidad de expediente
     * para el proceso con el n�mero de expediente suministrado y
     * la entrega rellenada con los datos del expediente.
     *
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @param urlKey clave de la entidad en la url
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getExpedientEntityApp(String numexp, int procedureId, String path, int urlKey)
    	throws ISPACException;
    
    /**
     * Obtiene la aplicaci�n solicitada para la entidad.
     * Adem�s, crea un nuevo registro para la entidad que es precisamente
     * al que har� referencia la aplicaci�n.
     *
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param appId identificador de la aplicaci�n si ya viene especificada
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    /*
    public EntityApp newEntityApp(int procedureId, int appId, int entityId, String path)
    	throws ISPACException;
    */

    /**
     * Busca la aplicaci�n por defecto para visualizar la entidad.
     * Adem�s, crea un nuevo registro para la entidad que es precisamente
     * al que har� referencia la aplicaci�n.
     *
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp newEntityApp(int entityId, String path)
    	throws ISPACException;

    /**
     * Busca la aplicaci�n definida en el procedimiento para visualizar la entidad.
     * Adem�s, crea una nuevo registro para la entidad que es precisamente
     * al que har� referencia la aplicaci�n.
     * 
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    /*
    public EntityApp newProcedureEntityApp(int procedureId, int entityId, String path)
    	throws ISPACException;
    */

    /**
     * Busca la aplicaci�n definida en la fase indicada para visualizar la entidad.
     * Adem�s, crea una nuevo registro para la entidad que es precisamente
     * al que har� referencia la aplicaci�n.
     * 
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param stagePcdId identificador de la fase en el procedimiento para la que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    /*
    public EntityApp newStageEntityApp(int procedureId, int stagePcdId, int entityId, String path)
    	throws ISPACException;
    */

    /**
     * Busca la aplicaci�n definida en el tr�mite indicado para visualizar la entidad.
     * Adem�s, crea una nuevo registro para la entidad que es precisamente
     * al que har� referencia la aplicaci�n.
     * 
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param taskPcdId dentificador del tr�mite en el procedimiento para el que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @return aplicaci�n solicitada
     * @see EntityApp
     * @throws ISPACException
     */
    /*
    public EntityApp newTaskEntityApp(int procedureId, int taskPcdId, int entityId, String path)
    	throws ISPACException;
    */

    /**
     * Obtiene la aplicaci�n asociada a la entidad para el procedimiento suministrado y
     * la devuelve debidamente cargada con los datos del registro indicado
     * y asociado al n�mero de expediente.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     *
     * @param numexp n�mero de expediente
     * @param procedureId identificador del procedimiento para el que se busca la aplicaci�n
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param entityRegId identificador del registro a cargar por la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getCatalogEntityApp(String numexp, int procedureId, int entityId, int entityRegId, String path)
    	throws ISPACException;

    /**
     * Obtiene la aplicaci�n asociada a la entidad y
     * la devuelve debidamente cargada con los datos del item suministrado.
     * La aplicaci�n permite el intercambio de datos entre
     * la capa de presentaci�n y los objetos de negocio.
     *
     * @param entityId identificador de la entidad para la que se busca la aplicaci�n
     * @param item registro con los datos a cargar en la aplicaci�n
     * @param path ruta de la aplicaci�n
     * @return aplicaci�n solicitada con los datos de la entidad
     * @see EntityApp
     * @throws ISPACException
     */
    public EntityApp getCatalogEntityApp(int entityId, IItem item, String path)
    	throws ISPACException;
    
    
    // TR�MITES //
    
    /**
     * Devuelve el registro de la entidad de tr�mites
     * a partir del identificador del tr�mite en el proceso.
     * 
     * @param taskProcessId identificador del tr�mite en el proceso
     * @return IItem registro del tr�mite solicitado
     * @throws ISPACException
     */
    public IItem getTask(int taskProcessId) 
    	throws ISPACException;

    /**
     * Devuelve el registro de la entidad de tr�mites.
     * 
     * @param taskId identificador del registro en la entidad de tr�mites
     * @return IItem registro del tr�mite solicitado
     * @throws ISPACException
     */
    public IItem getEntityTask(int taskId)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con los tr�mites del n�mero de expediente suministrado.
     *
     * @param numexp n�mero de expediente
     * @return CollectionDAO colecci�n de objetos CTEntityDAO de tr�mites del expediente
     * @throws ISPACException
     */
    public IItemCollection getExpedientTasks(String numexp)
    	throws ISPACException;

    /**
     * Obtiene una colecci�n con los tr�mites del n�mero de expediente suministrado
     * y para un determinada fase.
     *
     * @param numexp n�mero de expediente
     * @param stagepcd identificador de la fase en el procedimiento
     * @return CollectionDAO colecci�n de objetos CTEntityDAO de tr�mites del expediente
     * @throws ISPACException
     */
    public IItemCollection getStageTasks (String numexp, int stagepcd)
    	throws ISPACException;

    
    // ENTIDADES //

    /**
     * Obtiene el sustituto de un valor para una entidad de sustituci�n,
     * a partir del identificador de la entidad y el valor suministrado.
     * 
     * @param entityId identificador de la entidad de sustituci�n
     * @param value valor del sustituto
     * @return String sustituto para el valor suministrado
     * @throws ISPACException
     */
    public String getSubstitute(int entityId, String value) 
    	throws ISPACException;
    
    /**
     * Obtiene el sustituto de un valor para una entidad de sustituci�n,
     * a partir del nombre de la entidad y el valor suministrado.
     * 
     * @param entityname nombre de la entidad de sustituci�n
     * @param value valor del sustituto
     * @return String sustituto para el valor suministrado
     * @throws ISPACException
     */
    public String getSubstitute(String entityname, String value) 
    	throws ISPACException;
    
    /**
     * Obtiene el IItem del sustituto de un valor para una entidad de sustituci�n,
     * a partir del nombre de la entidad y el valor suministrado.
     * 
     * @param entityname nombre de la entidad de sustituci�n
     * @param value valor del sustituto
     * @return IItem registro de sustituto para el valor suministrado
     * @throws ISPACException
     */
    public IItem getSubstituteIItem(String entityname, String value) 
    	throws ISPACException;
    
    /**
     * Obtiene los recursos para las etiquetas de una determinada entidad.
     * 
     * @param entityId identificador de la entidad
     * @return Etiquetas de la entidad
     * @throws ISPACException si ocurre alg�n error
     */
    public EntityResources getEntityResources(int entityId) throws ISPACException;
    
    /**
     * Obtiene los recursos para las etiquetas de una determinada entidad en el idioma suministrado.
     * 
     * @param entityId identificador de la entidad
     * @param language idioma
     * @return IItemCollection colecci�n con las etiquetas de la entidad
     * @throws ISPACException
     */
    public IItemCollection getEntityResources(int entityId, String language) 
    	throws ISPACException;
    
    /**
     * Obtiene los recursos para las etiquetas de una determinada entidad en el idioma suministrado.
     * 
     * @param entityId identificador de la entidad
     * @param keys claves de los recursos
     * @param language idioma
     * @return IItemCollection colecci�n con las etiquetas de la entidad
     * @throws ISPACException
     */
    public IItemCollection getEntityResources(int entityId, String keys, String language) 
    	throws ISPACException;
    
    /**
     * Obtiene el recurso de una determinada entidad en el idioma suministrado.
     * 
     * @param entityId identificador de la entidad
     * @param key clave del recurso
     * @param language idioma
     * @return IItem EntityResourceDAO encontrado
     * @throws ISPACException
     */
	public IItem getEntityResource(int entityId, String key, String language) throws ISPACException;
    
    /**
     * Obtiene el recurso para la etiqueta de un campo de una determinada entidad en el idioma suministrado.
     * 
     * @param entityId identificador de la entidad
     * @param language idioma
     * @param key clave del campo de la entidad
     * @return String etiqueta para el campo de la entidad
     * @throws ISPACException
     */
    public String getEntityResourceValue(int entityId, String language, String key) 
    	throws ISPACException;
    
    /**
     * Obtiene los recursos no asociados a los campos de una determinada entidad en el idioma suministrado.
     * 
     * @param entityId identificador de la entidad
     * @param fieldKeys claves de los campos de la entidad
     * @param language idioma
     * @return IItemCollection colecci�n con los recursos de la entidad
     * @throws ISPACException
     */ 
    public IItemCollection getEntityOtherResources(int entityId, String fieldKeys, String language)
    	throws ISPACException;
    
    /**
     * Obtiene las claves de los recursos no asociados a los campos de una determinada entidad.
     * 
     * @param entityId identificador de la entidad
     * @param fieldKeys claves de los campos de la entidad
     * @return IItemCollection colecci�n con las claves
     * @throws ISPACException
     */ 
    public IItemCollection getEntityOtherResourceKeys(int entityId, String fieldKeys)
    	throws ISPACException;
    
    /**
     * Comprueba si para una entidad existe un recurso con la clave proporcionada
     * 
     * @param entityId identificador de la entidad
     * @param key clave
     * @return Cierto si el recurso existe, en caso contrario, falso.
     * @throws ISPACException
     */
    public boolean isEntityResourceKey(int entityId, String key)
    	throws ISPACException;
    
    /**
     * Obtiene los recursos para las etiquetas de varias entidades.
     * 
     * @param entitiesNames entidades de las que se van a obtener los recursos
     * @return Map etiquetas para los nombres de entidad
     * @throws ISPACException
     */
	public Map getEntitiesResourcesMap(String entitiesNames[]) 
		throws ISPACException;
    
	/**
	 * Obtiene la definici�n extendida para varias entidades
	 * con el nombre de la entidad obtenido a partir de los recursos.
	 *  
	 * @param entitiesCollection colecci�n de entidades
	 * @return lista de entidades con campos extra: 
	 * 		   ETIQUETA: Campo internacionalizado con el nombre de la etiqueta, 
	 * 		   la propiedad utilizada para obtener la etiqueta es 'NOMBRE'
	 */
	public List getEntitiesExtendedItemBean(IItemCollection entitiesCollection)
		throws ISPACException;
	
	/**
	 * Obtiene la definici�n extendida para varias entidades
	 * con un campo de la entidad obtenido a partir de los recursos.
	 * 
	 * @param strNameProperty campo de la entidad a obtener de los recursos
	 * @param entitiesCollection colecci�n de entidades
	 * @return entidades con campos extra: 
	 * 		   ETIQUETA: Campo internacionalizado con el nombre de la etiqueta, 
	 * 		   la propiedad utilizada para obtener la etiqueta se pasa por parametro
	 * @throws ISPACException
	 */
	public List getEntitiesExtendedItemBean(String keyNameProperty, IItemCollection entitiesCollection)
		throws ISPACException;
	
    /**
     * Clona el registro de una entidad asignando un nuevo n�mero de expediente y
     * copiando los datos de los campos suministrados en el nuevo registro de entidad.
     * 
     * @param entityId identificador de la entidad a clonar
     * @param keyId identificador del registro a clonar
     * @param newNumExp n�mero de expediente a establecer en la entidad clonada
     * @param idFieldsToClone identificadores de los campos de la definici�n de la entidad a clonar
     * @return cierto si la entidad se ha clonado correctamente
     * @throws ISPACException
     */
    public boolean cloneRegEntity(int entityId, int keyId, String newNumExp, String[] idFieldsToClone) 
    	throws ISPACException;
    
    /**
     * Copia el registro de una entidad en otro registro suministrado en funci�n de una
     * lista de campos a excluir de copiar y los identificadores de los campos a copiar.
     * 
     * @param entityId identificador de la entidad a copiar
     * @param keyId identificador del registro a clonar
     * @param item registro en el que se copian los datos
     * @param excludedList lista con los nombres de los campos que no se copian
     * @param idFieldsToCopy identificadores de los campos de la definici�n de la entidad a copiar
     * @throws ISPACException
     */
	public void copyRegEntityData(int entityId, int keyId, IItem item, List excludedList, String[] idFieldsToCopy) 
		throws ISPACException;
	
	/**
	 * Copia los datos de un registro en otro registro en funci�n de una
	 * lista de campos a excluir de copiar y una lista de campos a copiar.
	 * 
	 * @param itemSource registro fuente de los datos
	 * @param item registro en el que se copian los datos
	 * @param excludedList lista con los nombres de los campos que no se copian
	 * @param copyList lista con los nombres de los campos a copiar
	 * @throws ISPACException
	 */
	public void copyRegEntityData(IItem itemSource, IItem item, List excludedList, List copyList) 
		throws ISPACException;

	/**
	 * Clona los expedientes relacionados de un expediente
	 * en otro nuevo n�mero de expediente suministrado.
	 * 
	 * @param numexp n�mero de expediente del que se toman sus expedientes relacionados (padres e hijos en la relaci�n)
	 * @param newNumExp nuevo n�mero de expediente para el que se establecen los expedientes relacionados
	 * @throws ISPACException
	 */
	public void cloneRelatedExpedient(String numexp, String newNumExp) 
		throws ISPACException;
	
	
	/**
	 * Obtiene el formulario por defecto para la entidad
	 * @param entityId
	 * @return
	 * @throws ISPACException
	 */
	public Object getFormDefault(int entityId) throws ISPACException;
	
	/**
	 * 
	 * @param numexp: Expediente
	 * @param idPhasePcd: Fase dentro del procedimiento en la que esta el tr�mite
	 * @param idTaskPcd: Tr�mite dentro del procedimiento del que queremos obtener la informacion
	 * @return
	 * @throws ISPACException
	 */
    public IItemCollection getTasksExpInPhase(String numexp, int idPhasePcd, int idTaskPcd) throws ISPACException;

    /**
     * Comprueba si un tr�mite no se puede borrar
     * 
     * @param taskId Identificador del tr�mite
     * @return
     * @throws ISPACException
     */
	public boolean undeleteTask(int taskId) throws ISPACException;
	
	/**
	 * Elimina un documento a partir del identificador del documento.
	 * Esto conlleva eliminar el registro con los datos del documento,
	 * los documento f�sicos asociados (fichero original y fichero firmado)
	 * y cualquier referencia al documento en un proceso de firma.
	 * 
	 * @param documentId Identificador del documento
	 * @throws ISPACException
	 */
	public void deleteDocument(int documentId) throws ISPACException;
	
	/**
	 * Elimina el documento.
	 * Esto conlleva eliminar el registro con los datos del documento,
	 * los documento f�sicos asociados (fichero original y fichero firmado)
	 * y cualquier referencia al documento en un proceso de firma.
	 * 
	 * @param document Documento a borrar
	 * @throws ISPACException
	 */
	public void deleteDocument(IItem document) throws ISPACException;
	
	/**
	 * [dipucr-Felipe #1462]
	 * Simula el borrado de un documento insert�ndolo en la tabla de documentos borrados
	 *
	 * @param documentId Id del documento
	 * @throws ISPACException
	 */
	public void dipucrFakeDeleteDocument(int documentId) throws ISPACException;
	
	/**
	 * [dipucr-Felipe #1462]
	 * Simula el borrado de un documento insert�ndolo en la tabla de documentos borrados
	 *
	 * @param document Documento a borrar
	 * @throws ISPACException
	 */
	public void dipucrFakeDeleteDocument(IItem document) throws ISPACException;
	
	/**
	 * Elimina todos los documentos (incluso los f�sicos) para un expediente
	 * @param numExp : Expediente del que se quieren eliminar todos los documentos
	 * @throws ISPACException
	 */
	
	public void deleteAllDocumentsOfNumExp(String numExp)throws ISPACException;

	/**
	 * Desasocia el documento al registro de la entidad.
	 * 
	 * @param document Documento a desasociar
	 * @param deleteDataDocument Indicador para borrar el registro de SPAC_DT_DOCUMENTOS
	 * @throws ISPACException
	 */
	public void deleteDocumentFromRegEntity(IItem document, boolean deleteDataDocument) throws ISPACException;
	
	/**
	 * Elimina un tr�mite a partir del identificador del tr�mite.
	 * Esto conlleva eliminar toda la informaci�n del tr�mite en el procedimiento,
	 * incluyento los documentos generados.
	 * 
	 * @param documentId Identificador del tr�mite
	 * @throws ISPACException
	 */
	public void deleteTask(int taskId) throws ISPACException;
	
	 /**
     * Modifica la ordenacion de los valores de la tabla de validacion atendiendo al tipo de ordenacion que se realiza
     * @param entityId: Identificador de la tabla de validaci�n sobre la que se realizar� la ordenaci�n
     * @param tipoOrdenacion: Por valor o por sustituto
     * @return 
     * @throws ISPACException
     */
    
    public void orderValuesTblValidation(int entityId, String tipoOrdenacion) throws ISPACException;
    
    /**
     * 
     * @param entityName Nombre de la entidad
     * @param query Filtro para la consulta
     * @param order por lo que quiera ordenar el usuario, en caso de que se vaya a usuar la clausula order by
     * @return Objeto con la lista resultante de la b�squeda , el num m�x de registros que se pueden obtener 
     * en la b�squeda y el n�m total de registros que satisfacen la b�squeda
     * @throws ISPACException
     */
    
    public SearchResultVO getLimitedQueryEntities(String entityName, String query, String order) throws ISPACException;
	
    /**
	 * [dipucr-Felipe #1246]
	 * @param document
	 * @return true si el documento se ha enviado a firmas en el portafirmas
	 * @throws ISPACException 
	 */
	public boolean isDocumentPortafirmas(IItem document) throws ISPACException;
}