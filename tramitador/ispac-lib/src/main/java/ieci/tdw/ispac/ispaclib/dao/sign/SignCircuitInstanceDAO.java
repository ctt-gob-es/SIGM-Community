package ieci.tdw.ispac.ispaclib.dao.sign;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.common.constants.SignCircuitStates;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispactx.TXConstants;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class SignCircuitInstanceDAO extends ObjectDAO
{
	private static final long serialVersionUID = 1L;
	
	static final String TABLENAME = "SPAC_CTOS_FIRMA";
	static final String IDSEQUENCE = "SPAC_SQ_ID_CTOS_FIRMA";
	static final String IDKEY = "ID";
	
	static final int MAX_DOCS_HISTORIC = 1000;

	/**
    *
	 * @throws ISPACException
	 */
	public SignCircuitInstanceDAO()	throws ISPACException {
		super(null);
	}

	/**
    *
	 * @param cnt
	 * @throws ISPACException
	 */
	public SignCircuitInstanceDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}

	/**
    *
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public SignCircuitInstanceDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id, null);
	}

	public String getTableName()
	{
		return TABLENAME;
	}

	protected String getDefaultSQL(int nActionDAO) throws ISPACException
    {
	    return " WHERE " + IDKEY + " = " + getInt(IDKEY);
    }

	public String getKeyName() throws ISPACException
	{
	    return IDKEY;
	}

	public void newObject(DbCnt cnt) throws ISPACException
	{
	    set(IDKEY,IdSequenceMgr.getIdSequence(cnt,IDSEQUENCE));
	}

	public static CollectionDAO getInstancedSingCircuit(DbCnt cnt, int instanceCircuitId) throws ISPACException {

	    String sql="WHERE ID_INSTANCIA_CIRCUITO = " + instanceCircuitId;
		CollectionDAO objlist=new CollectionDAO(SignCircuitInstanceDAO.class);
		objlist.query(cnt,sql);

		return objlist;
	}

	public static CollectionDAO getStepsByDocument(DbCnt cnt, int documentId) throws ISPACException {

	    String sql="WHERE ID_DOCUMENTO = " + documentId + " ORDER BY ID_PASO";
		CollectionDAO objlist=new CollectionDAO(SignCircuitInstanceDAO.class);
		objlist.query(cnt,sql);

		return objlist;
	}


	public static void deleteAllInstancedSingCircuitOfNumExp(DbCnt cnt , String numExp)throws ISPACException{

		TableJoinFactoryDAO tableJoinFactoryDAO = new TableJoinFactoryDAO();
		tableJoinFactoryDAO.addTable(TABLENAME, TABLENAME);
		tableJoinFactoryDAO.addTable("SPAC_DT_DOCUMENTOS", "SPAC_DT_DOCUMENTOS");
		String sql = " WHERE "
					+ TABLENAME + ".ID_DOCUMENTO = SPAC_DT_DOCUMENTOS.ID "
				   + " AND SPAC_DT_DOCUMENTOS.NUMEXP = '"+numExp+"'";

		IItemCollection itemcol= tableJoinFactoryDAO.queryTableJoin(cnt, sql, TABLENAME+":"+IDKEY).disconnect();

		//Recorremos el listado de circuitos de firma instanciados y los eliminamos.
		while(itemcol.next()){
			SignCircuitInstanceDAO instanceDAO = new SignCircuitInstanceDAO(cnt);
			instanceDAO.setKey(itemcol.value().get(TABLENAME+":"+IDKEY));
			instanceDAO.delete(cnt);
		}

	}
	/**
	 * Obtener los pasos de circuito de firma pendientes (de un expediente que no esté en la papelera)
	 * en función de la responsabilidad.
    *
    *
    * @param cnt
	 * @param resp
	 * @return
	 * @throws ISPACException
	 */
	public static CollectionDAO getCircuitSteps(DbCnt cnt, String resp) throws ISPACException {

		TableJoinFactoryDAO tableJoinFactoryDAO = new TableJoinFactoryDAO();
		tableJoinFactoryDAO.addTable(TABLENAME, TABLENAME);
		tableJoinFactoryDAO.addTable("SPAC_DT_DOCUMENTOS", "SPAC_DT_DOCUMENTOS");
		tableJoinFactoryDAO.addTable("SPAC_TBL_008", "SPAC_TBL_008");
		tableJoinFactoryDAO.addTable("SPAC_PROCESOS", "SPAC_PROCESOS");
		//[eCenpri-Felipe #382] 17.05.11 - Añadido JOIN a SPAC_EXPEDIENTES
		tableJoinFactoryDAO.addTable("SPAC_EXPEDIENTES", "SPAC_EXPEDIENTES");

		//Un circuito de firma sólo puede estar pendiente en un expediente ABIERTO
		String sql = " WHERE "
				   + DBUtil.addInResponsibleCondition(TABLENAME + ".ID_FIRMANTE", resp)
					+ " AND " + TABLENAME + ".ESTADO = " + SignCircuitStates.PENDIENTE
				   + " AND " + TABLENAME + ".ID_DOCUMENTO = SPAC_DT_DOCUMENTOS.ID "
				   + " AND SPAC_DT_DOCUMENTOS.ESTADOFIRMA = SPAC_TBL_008.VALOR"
				   + " AND SPAC_PROCESOS.ESTADO="+TXConstants.STATUS_OPEN
				   + " AND SPAC_PROCESOS.NUMEXP=SPAC_DT_DOCUMENTOS.NUMEXP"
				   + " AND SPAC_EXPEDIENTES.NUMEXP = SPAC_DT_DOCUMENTOS.NUMEXP"; //[eCenpri-Felipe #382]

		CollectionDAO objlist = tableJoinFactoryDAO.queryTableJoin(cnt, sql);
		return objlist;
	}

	//[dipucr-Felipe #958] Sobrecargamos el método
	public static CollectionDAO getHistorics(DbCnt cnt, String respId, Date init, Date end, int state) throws ISPACException {
		return getHistorics(cnt, respId, init, end, null, null, null, state);
	}
	
	//[dipucr-Felipe #958]
	@SuppressWarnings("unchecked")
	public static CollectionDAO getHistorics(DbCnt cnt, String respId, Date init, Date end, String numexp, String docName, String asunto, int state) throws ISPACException {

		String dateFilter = "";
		if (init != null){
//			dateFilter = " AND "  + TABLENAME + ".FECHA >= " + DBUtil.getToDateByBD(cnt, init);
			dateFilter = " AND SPAC_DT_DOCUMENTOS.FAPROBACION >= " + DBUtil.getToDateByBD(cnt, init);//[dipucr-Felipe #958]
		}
		if (end != null){

			// Añadir la última hora del día para las comparaciones en Oracle
			Date aux = DateUtils.setHours(end, 23);
			aux = DateUtils.setMinutes(aux, 59);
			aux = DateUtils.setSeconds(aux, 59);

//			dateFilter += " AND " + TABLENAME + ".FECHA <= " + DBUtil.getToTimestampByBD(cnt, new Timestamp(aux.getTime()));
			dateFilter += " AND SPAC_DT_DOCUMENTOS.FAPROBACION <= " + DBUtil.getToTimestampByBD(cnt, new Timestamp(aux.getTime()));//[dipucr-Felipe #958]
		}

		TableJoinFactoryDAO tableJoinFactoryDAO = new TableJoinFactoryDAO();
		tableJoinFactoryDAO.addTable(TABLENAME, TABLENAME);
		tableJoinFactoryDAO.addTable("SPAC_DT_DOCUMENTOS", "SPAC_DT_DOCUMENTOS");
		tableJoinFactoryDAO.addTable("SPAC_EXPEDIENTES", "SPAC_EXPEDIENTES");//[dipucr-Felipe #958]
		tableJoinFactoryDAO.addTable("SPAC_TBL_008", "SPAC_TBL_008");

		String filtro = " WHERE " + TABLENAME + ".ID_FIRMANTE = '" + DBUtil.replaceQuotes(respId)
				+ "' AND " + TABLENAME + ".ESTADO = " + state
				+ " AND " + TABLENAME + ".ID_DOCUMENTO = SPAC_DT_DOCUMENTOS.ID "
				+ " AND SPAC_EXPEDIENTES.NUMEXP = SPAC_DT_DOCUMENTOS.NUMEXP "
				+ dateFilter
				+ " AND SPAC_DT_DOCUMENTOS.ESTADOFIRMA = SPAC_TBL_008.VALOR";
		//INICIO [dipucr-Felipe #958]
		if (StringUtils.isNotEmpty(numexp)){
			filtro += " AND UPPER(SPAC_DT_DOCUMENTOS.NUMEXP) LIKE UPPER('%" + numexp + "%')";
		}
		if (StringUtils.isNotEmpty(docName)){
			filtro += " AND UPPER(SPAC_DT_DOCUMENTOS.NOMBRE) LIKE UPPER('%" + docName + "%')";
		}
		if (StringUtils.isNotEmpty(asunto)){
			filtro += " AND UPPER(SPAC_EXPEDIENTES.ASUNTO) LIKE UPPER('%" + asunto + "%')";
		}
		//INICIO [dipucr-Felipe #958]
		if (SignCircuitStates.RECHAZADO == state){
			filtro += " ORDER BY " + TABLENAME + ".FECHA DESC";
		}
		else{
			filtro += " AND SPAC_DT_DOCUMENTOS.FAPROBACION IS NOT NULL";
			filtro += " ORDER BY SPAC_DT_DOCUMENTOS.FAPROBACION DESC";
		}
		filtro += " LIMIT " + MAX_DOCS_HISTORIC;
		//FIN [dipucr-Felipe #958]

		CollectionDAO objlist = tableJoinFactoryDAO.queryTableJoin(cnt, filtro);
		
		//INICIO [dipucr-Felipe 3#266 #1023]
		//Adaptamos el histórico de firmas al histórico
		TableJoinFactoryDAO tableJoinFactoryDAOh = new TableJoinFactoryDAO();
		tableJoinFactoryDAOh.addTable(TABLENAME, TABLENAME);
		tableJoinFactoryDAOh.addTable("SPAC_DT_DOCUMENTOS_H", "SPAC_DT_DOCUMENTOS");
		tableJoinFactoryDAOh.addTable("SPAC_EXPEDIENTES_H", "SPAC_EXPEDIENTES");//[dipucr-Felipe #958]
		tableJoinFactoryDAOh.addTable("SPAC_TBL_008", "SPAC_TBL_008");
		
		CollectionDAO objlisth = tableJoinFactoryDAOh.queryTableJoin(cnt, filtro);
		
		//Unimos las dos listas
		objlist.toList().addAll(objlisth.toList());
		//FIN [dipucr-Felipe 3#266 #1023]
		
		return objlist;
	}

	/**
	 * Obtener el número de pasos de circuito de firma pendientes
	 * en función de la responsabilidad.
    *
    *
    * @param cnt
	 * @param resp
	 * @return
	 * @throws ISPACException
	 */
	public static int countCircuitSteps(DbCnt cnt, String resp) throws ISPACException {

		TableJoinFactoryDAO tableJoinFactoryDAO = new TableJoinFactoryDAO();
		tableJoinFactoryDAO.addTable(TABLENAME, TABLENAME);
		tableJoinFactoryDAO.addTable("SPAC_PROCESOS", "SPAC_PROCESOS");
		tableJoinFactoryDAO.addTable("SPAC_DT_DOCUMENTOS", "SPAC_DT_DOCUMENTOS");
	    //Un circuito de firma solo puede estar pendiente en un expediente abierto
	    String sql = " WHERE " + DBUtil.addInResponsibleCondition(TABLENAME+".ID_FIRMANTE", resp)
	    		   + " AND "+TABLENAME+".ESTADO = " + SignCircuitStates.PENDIENTE
	    		   + " AND SPAC_PROCESOS.ESTADO="+TXConstants.STATUS_OPEN
	    		   + " AND SPAC_DT_DOCUMENTOS.NUMEXP=SPAC_PROCESOS.NUMEXP"
	    		   + " AND " + TABLENAME + ".ID_DOCUMENTO = SPAC_DT_DOCUMENTOS.ID ";

	    return tableJoinFactoryDAO.countTableJoin(cnt, sql);
	}

	public static boolean isLastStep(DbCnt cnt, int instancedStepId) throws ISPACException {
       String sql = " WHERE ID_INSTANCIA_CIRCUITO = ( SELECT ID_INSTANCIA_CIRCUITO FROM SPAC_CTOS_FIRMA WHERE ID = " + instancedStepId + " ) AND ID_PASO > ( SELECT ID_PASO FROM SPAC_CTOS_FIRMA WHERE ID = " + instancedStepId + " )";
		CollectionDAO objlist=new CollectionDAO(SignCircuitInstanceDAO.class);
		int count = objlist.count(cnt,sql);
		return (count == 0);
	}

//	public static ObjectDAO nexStep(DbCnt cnt, int instancedStepId) throws ISPACException {
//	    String sql = " WHERE ID_INSTANCIA_CIRCUITO = ( SELECT ID_INSTANCIA_CIRCUITO FROM SPAC_CTOS_FIRMA WHERE ID = " + instancedStepId + " ) AND ID_PASO > ( SELECT ID_PASO FROM SPAC_CTOS_FIRMA WHERE ID = " + instancedStepId + " ) ORDER BY ID_PASO";
//		CollectionDAO objlist=new CollectionDAO(SignCircuitInstanceDAO.class);
//		objlist.query(cnt, sql);
//		if (objlist.next())
//			return objlist.value();
//		return null;
//	}

	public static SignCircuitInstanceDAO nexStep(DbCnt cnt, int instancedCircuitId) throws ISPACException {
       String sql = " WHERE ID_INSTANCIA_CIRCUITO=" + instancedCircuitId + " AND ESTADO=" + SignCircuitStates.SIN_INICIAR + " ORDER BY ID_PASO";
		CollectionDAO objlist = new CollectionDAO(SignCircuitInstanceDAO.class);
		objlist.query(cnt, sql);
		if (objlist.next()) {
			return (SignCircuitInstanceDAO) objlist.value();
		}
		return null;
	}

	public static boolean isResponsible(DbCnt cnt, String respId, int documentId) throws ISPACException {
		String sql = " WHERE ID_DOCUMENTO = " + documentId + " AND ID_FIRMANTE = '" + DBUtil.replaceQuotes(respId) + "'";
		CollectionDAO objlist=new CollectionDAO(SignCircuitInstanceDAO.class);
		return objlist.count(cnt, sql)>=1;
	}

}
