package es.dipucr.sigem.dao;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcedure;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.TreeItemBean;
import ieci.tdw.ispac.ispaclib.catalog.procedure.IPcdElement;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DpcrFasesProcDAO extends ObjectDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8507525184522041324L;

	static final String TABLENAME 	= "DPCR_FASES_PROC";
	//static final String IDSEQUENCE
	static final String IDKEY 		= "ID_PCD";

	static final Property[] TABLECOLUMNS=
	{
        new Property(0,"ID_FASE",Types.NUMERIC),
        new Property(1,"COUNT","COUNT(ID_FASE)",Types.NUMERIC),
        new Property(2,"NOMBRE",Types.VARCHAR),
        new Property(3,"NOMBRE_PROC",Types.VARCHAR),
        new Property(4,"NVERSION",Types.NUMERIC),
        new Property(5,"ID_PCD",Types.NUMERIC),
        new Property(6,"ID_PADRE",Types.NUMERIC)
	};

	/**
	 *
	 * @throws ISPACException
	 */
	public DpcrFasesProcDAO() throws ISPACException {
		super(TABLECOLUMNS);
	}

	/**
	 *
	 * @param cnt
	 * @throws ISPACException
	 */
	public DpcrFasesProcDAO(DbCnt cnt) throws ISPACException {
		super(cnt, TABLECOLUMNS);
	}

	/**
	 *
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public DpcrFasesProcDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id, TABLECOLUMNS);
	}

	public String getTableName() {
		return TABLENAME;
	}


	protected void newObject(DbCnt cnt) throws ISPACException {
		throw new ISPACException("Es una vista, no se pueden hacer inserciones");
	}

	public String getKeyName() throws ISPACException {
		return IDKEY;
	}

	protected String getDefaultSQL(int nActionDAO) throws ISPACException {
		return " WHERE 1 = 2";
	}
	
	public static CollectionDAO getStages(IClientContext ctx, String resp, int idProcedure)	throws ISPACException{
		
		DbCnt cnt = ctx.getConnection();
		List<Object> id_pcd_padres = null;
		CollectionDAO objlist = null;
		
		try	{
			IInvesflowAPI invesFlowAPI = ctx.getAPI();
			IProcedure iProcedure = invesFlowAPI.getProcedure(idProcedure);

			if(iProcedure.getInt("TIPO")==IPcdElement.TYPE_SUBPROCEDURE){
				IItemCollection itemcol=invesFlowAPI.getCatalogAPI().queryCTEntities(ICatalogAPI.ENTITY_P_TASK, "where id_cttramite in (select id from spac_ct_tramites where id_subproceso="+idProcedure+")");
		    	//Obtenemos la lista de padres
		    	id_pcd_padres= new ArrayList<Object>();
		    	while(itemcol.next()){
	
		    		id_pcd_padres.add(((IItem)itemcol.value()).get("ID_PCD"));
		    	}
			}
			StringBuffer sql = new StringBuffer();
		    sql.append(" WHERE ESTADO = 1 AND ID_PCD = ")
			   .append(idProcedure)
			   .append(addAndInResponsibleOrExistPermissionCondition(resp , id_pcd_padres))
			   .append(" GROUP BY ID_FASE, NOMBRE ORDER BY ID_FASE");
		    
			objlist = new CollectionDAO(DpcrFasesProcDAO.class);
			objlist.query(cnt, sql.toString());
		}
		catch (ISPACException ie){
			throw new ISPACException("Error en WLWorklist:getStages("
					+ idProcedure + ")", ie);
		}
		finally{
			ctx.releaseConnection(cnt);
		}

		return objlist;
	}
	
	public static CollectionDAO getAllStages(IClientContext ctx, String resp) throws ISPACException{
		return getAllStages(ctx, resp, null);
	}
	
	//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
	public static CollectionDAO getAllStages(IClientContext ctx, String resp, String anio) throws ISPACException{
		
		DbCnt cnt = ctx.getConnection();
		CollectionDAO objlist = null;
		
		try	{
			StringBuffer sql = new StringBuffer();
			StringBuffer consulta = new StringBuffer();
		    			
		    if (StringUtils.isNotBlank(resp) && !Responsible.SUPERVISOR.equalsIgnoreCase(resp)){
		    	consulta.append(" ( ")
				   .append(DBUtil.addInResponsibleCondition("ID_RESP", resp))
				   .append(" OR ( ")
				   .append("SELECT COUNT(*) FROM SPAC_PERMISOS SPC_PERMS WHERE ( ")
				   .append("(SPC_PERMS.TP_OBJ = ").append(ISecurityAPI.PERMISSION_TPOBJ_PROCEDURE)
				   .append(" AND ( SPC_PERMS.ID_OBJ = ID_PCD ) ) ")
				   .append(") AND ")
				   .append(DBUtil.addInResponsibleCondition("SPC_PERMS.ID_RESP", resp))
				   .append(") > 0 ) ");
		    }
		    if(StringUtils.isNotEmpty(anio)){
			    if(StringUtils.isNotEmpty(consulta.toString())){
			    	consulta.append(" AND ");		    	
			    }
		    	consulta.append(" EXTRACT(YEAR FROM FAPERTURA) = '" + anio + "' ");
		    }
		    
		    if(StringUtils.isNotEmpty(consulta.toString())){
		    	sql.append(" WHERE ");
		    	sql.append(consulta);
		    }
			sql.append(" GROUP BY ID_FASE, NOMBRE, ID_PCD, NOMBRE_PROC, NVERSION, ID_PADRE ");
			sql.append("ORDER BY NOMBRE_PROC, ID_PADRE, ID_PCD, NVERSION, ID_FASE");
		    
			objlist = new CollectionDAO(DpcrFasesProcDAO.class);
			objlist.query(cnt, sql.toString());
		    
		}
		catch (ISPACException ie){
			throw new ISPACException("Error en DpcrFasesProcDAO:getALLStages()", ie);
		}
		finally{
			ctx.releaseConnection(cnt);
		}

		return objlist;
	}
	
	public static TreeItemBean getProcedureTree(ClientContext ctx, String resp)
			throws ISPACException {

		IItemCollection itemcol = getAllStages(ctx, resp).disconnect();

		// Obtenemos el árbol de ItemBeans
		return CollectionBean.getBeanTree(itemcol, "ID_PCD", "ID_PADRE");
	}
	
    public static String addAndInResponsibleOrExistPermissionCondition(String resp , List<Object> id_pcd_padres)
	throws ISPACException{
		String sql = addInResponsibleOrExistPermissionConditionPcdSubPcd(resp, id_pcd_padres);

		if (StringUtils.isNotBlank(sql)) {
			sql = " AND " + sql;
		}

		return sql;
	}
    
    public static String addInResponsibleOrExistPermissionConditionPcdSubPcd(String resp , List<Object> id_pcd_padres)
	throws ISPACException{
		String sql = " ";

		// Obtener la condición SQL de responsabilidad y de permisos
		// siempre que la responsabilidad no sea de Supervisor
		if (StringUtils.isNotBlank(resp) && !Responsible.SUPERVISOR.equalsIgnoreCase(resp)) {

			String sqlResponsibles = DBUtil.addInResponsibleCondition("ID_RESP", resp);

			// Añadir la responsabilidad y consultar los permisos asignados
			sql = getSqlInResponsibleOrExistPermissionCondition(sqlResponsibles, DBUtil.addInResponsibleCondition("SPC_PERMS.ID_RESP", resp),id_pcd_padres);
		}

		return sql;
	}
    
    protected static String getSqlInResponsibleOrExistPermissionCondition(String inResponsibleCondition, String inPermResponsibleCondition, List<Object> id_pcd_padres)
	throws ISPACException{
		StringBuffer sql = new StringBuffer();
		String cond="ID_PCD";

		if(id_pcd_padres!=null  && id_pcd_padres.size()>0){
			cond=((Integer) id_pcd_padres.get(0)).toString();
			for(int i=1; i<id_pcd_padres.size();i++)
			{
				cond+=" OR  SPC_PERMS.ID_OBJ="+id_pcd_padres.get(i)+" ";
			}
		}
		sql.append(" ( ")
		   .append(inResponsibleCondition)
		   .append(" OR ( ")
		   .append("SELECT COUNT(*) FROM SPAC_PERMISOS SPC_PERMS WHERE ( ")
		   // Procedimiento
		   .append("(SPC_PERMS.TP_OBJ = ").append(ISecurityAPI.PERMISSION_TPOBJ_PROCEDURE)
		   .append(" AND ( SPC_PERMS.ID_OBJ ="+cond+" ) ) ")
		   .append(") AND ")
		   .append(inPermResponsibleCondition)
		   .append(") > 0 ) ");

		return sql.toString();
	}
}


