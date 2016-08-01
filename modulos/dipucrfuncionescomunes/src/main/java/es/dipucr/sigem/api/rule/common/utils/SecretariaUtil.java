package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class SecretariaUtil {

	private static final Logger logger = Logger.getLogger(SecretariaUtil.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getUrgencias(IRuleContext rulectx,
			IEntitiesAPI entitiesAPI) throws ISPACException {
		List list=null, numbers=null, sorted=null;
		IItem item=null;
		Integer number;
		int orden;
		String strOrden;
		
		try
		{
			//Obtención de la lista de propuestas y urgencias
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
			logger.info("strQuery Urgencias "+strQuery);
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        list = collection.toList();
	        
	        //Ordenación de la lista
	        numbers = new ArrayList();
	        Iterator it = list.iterator();
	        while (it.hasNext())
	        {
	        	item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        	if (strOrden != null)
	        	{
	        		orden = Integer.parseInt(strOrden); 
	        	}
	        	else
	        	{
	        		orden = Integer.MAX_VALUE;
	        	}
	        	numbers.add(new Integer(orden));
	        }
	        Collections.sort(numbers);
	        sorted = new ArrayList();
	        it = numbers.iterator();
	        while (it.hasNext())
	        {
	        	number = (Integer)it.next();
	        	orden = number.intValue();
	        	moveItem(list, sorted, orden);
	        }	        
		}
		catch(ISPACException e)
		{
			logger.error(e.getMessage(), e);
        	throw new ISPACException(e);
        }
		return sorted;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void moveItem(List origen, List destino, int orden) throws ISPACException 
	{
		IItem item = null;
		int n;
		String strOrden;

		try
		{
			boolean found = false;
			Iterator it = origen.iterator();
			while (!found && it.hasNext())
			{
				item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        	if (strOrden != null)
	        	{
	        		n = Integer.parseInt(strOrden); 
	        	}
	        	else
	        	{
	        		n = Integer.MAX_VALUE;
	        	}
				found = n==orden; 
			}
			if(found)
			{
				destino.add(item);
				it.remove();
			}
		}
		catch(ISPACException e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getPropuestas(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACException 
	{
		List list=null, numbers=null, sorted=null;
		IItem item=null;
		Integer number;
		int orden;
		String strOrden;
		
		try
		{
			//Obtención de la lista de propuestas y urgencias
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
			logger.info("strQuery Propuestas "+strQuery);
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        list = collection.toList();
	        
	        //Ordenación de la lista
	        numbers = new ArrayList();
	        Iterator it = list.iterator();
	        while (it.hasNext())
	        {
	        	item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        	if (strOrden != null)
	        	{
	        		orden = Integer.parseInt(strOrden); 
	        	}
	        	else
	        	{
	        		orden = Integer.MAX_VALUE;
	        	}
	        	numbers.add(new Integer(orden));
	        }
	        Collections.sort(numbers);
	        sorted = new ArrayList();
	        it = numbers.iterator();
	        while (it.hasNext())
	        {
	        	number = (Integer)it.next();
	        	orden = number.intValue();
	        	moveItem(list, sorted, orden);
	        }	        
		}
		catch(ISPACException e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        }
		return sorted;
	}
	
	public static String getOrganoSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String strOrgano = "";
		try
		{
			IItem sesion = getSesion(rulectx, numexp);
			strOrgano = sesion.getString("ORGANO"); 
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return strOrgano;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getNombreOrganoSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String tipo = "";
		try
		{
			String strTipo = getOrganoSesion(rulectx, numexp); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_ORGANOS", strQuery);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
		}
		catch(ISPACException e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return tipo;
	}
	
	public static String getAreaSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String strOrgano = "";
		try
		{
			IItem sesion = getSesion(rulectx, numexp);
			strOrgano = sesion.getString("AREA"); 
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return strOrgano;
	}

	@SuppressWarnings("rawtypes")
	public static String getNombreAreaSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String tipo = "";
		try
		{
			String strTipo = getAreaSesion(rulectx,numexp); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_AREAS", strQuery);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return tipo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void createPropuestaAprobacionActaAnterior(IRuleContext rulectx) throws ISPACException
	{
		try
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        //Voy a obtener el nombre la propuesta que le corresponde según el órgano resolutor
	      
	        IItemCollection itColprop = buscaTodosExpDelDepartamento(cct, rulectx.getNumExp(), "WHERE COD_PCD LIKE 'PROPUESTA-%'");
	        IItem itPropuestaScpa_ct_procedimiento = (IItem) itColprop.iterator().next();
	        String nombrePropuesta = itPropuestaScpa_ct_procedimiento.getString("NOMBRE");
	        //Creación del expediente
	        String strQuery = "WHERE NOMBRE='"+nombrePropuesta+"'";
	        IItemCollection coll = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
	        Iterator it = coll.iterator();
	        int nProcedure = 0;
	        IItem proc = null;
	        int n;
	        String cod_pcd = "";
	        while (it.hasNext()) {
	        	proc = (IItem)it.next();
	        	n = proc.getInt("ID");
	        	if ( n > nProcedure )
	        	{
	        		nProcedure = n;
	        		cod_pcd = proc.getString("COD_PCD");
	        	}
	        }
	        Map params = new HashMap();
			params.put("COD_PCD", cod_pcd);
	        int idExp = tx.createProcess(nProcedure, params);
			IProcess process = invesFlowAPI.getProcess(idExp);
			String numexp = process.getString("NUMEXP");	        
			IItem iProp = entitiesAPI.createEntity("SECR_PROPUESTA", numexp);

			//Inicialización de datos de la propuesta
			String strOrgano = getOrganoSesion(rulectx,null);
			iProp.set("ORIGEN", "0001");
			iProp.set("DESTINO", strOrgano);
			iProp.set("EXTRACTO", "Conocimiento y aprobación, si procede, del borrador del acta de la sesión anterior");
			iProp.store(cct);
			
			//Campo Asunto del expediente de la propuesta
        	String strEstado = getEstadoAdmPropuesta(rulectx);
        	IItem iExp  = ExpedientesUtil.getExpediente(cct, numexp);
	        if (iExp != null) 
	        {
	        	iExp.set("ASUNTO", "Propuesta aprobación acta anterior");
	        	iExp.set("ESTADOADM", strEstado);
	        	iExp.store(cct);
	        }
			
	        //Relación con expediente de sesión
	        strQuery = "WHERE NOMBRE='SPAC_EXP_RELACIONADOS'";
	        coll = entitiesAPI.queryEntities("SPAC_CT_ENTIDADES", strQuery);
	        it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem iExpRel = (IItem)it.next();
	        	int id = iExpRel.getInt("ID");
		        IItem iRelacion = entitiesAPI.createEntity(id);
		        iRelacion.set("NUMEXP_PADRE", numexp);
		        iRelacion.set("NUMEXP_HIJO", rulectx.getNumExp());
		        iRelacion.set("RELACION", "Sesión/Propuesta");
		        iRelacion.store(cct);
	        }
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
	}
	
	public static IItemCollection buscaTodosExpDelDepartamento (IClientContext cct, String numexpPadre) throws ISPACException{
		IItemCollection procedimientosDelDepartamento = null;
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			
			int id_pcd_actual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(id_pcd_actual);
			if(procedimiento != null){
				String org_rsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(org_rsltr)){
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE ORG_RSLTR = '" + org_rsltr + "' AND COD_PCD LIKE 'PROPUESTA-%'");
					
				}
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
			throw new ISPACException( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
		}
		return procedimientosDelDepartamento;
	}
	
	public static String getEstadoAdmPropuesta(IRuleContext rulectx) throws ISPACException
	{
		String strEstado = "PR";
		try
		{
			String strOrgano = getOrganoSesion(rulectx,null);
        	if (strOrgano.compareTo("PLEN")==0)
        	{
        		strEstado = "SEC_PL";
        	}
        	else if (strOrgano.compareTo("JGOB")==0)
        	{
        		strEstado = "SEC_JG";
        	}
        	else if (strOrgano.compareTo("COMI")==0)
        	{
        		strEstado = "SEC_CI";
        	}
        	else if (strOrgano.compareTo("MESA")==0)
        	{
        		strEstado = "SEC_MS";
        	}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return strEstado;
	}
	
	//el tipo de sesion de un organo colegiado
	@SuppressWarnings("rawtypes")
	public static String getSesion(IRuleContext rctx) throws ISPACRuleException {
		
		String sesion ="";
		
		IClientContext cct = rctx.getClientContext();
		
		try {
			logger.info("rctx.getNumExp() "+rctx.getNumExp());
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getEntities("SECR_SESION", rctx.getNumExp(), "");
			
			Iterator it = itemCollection.iterator();
	        IItem item = null;
	        
	        while (it.hasNext()) {
	        	
                item = ((IItem)it.next());
                sesion = item.getString("TIPO");
	        }
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		if(sesion == null) sesion = "ORD";
		return sesion;
	}
	
	@SuppressWarnings("rawtypes")
	public static IItem getSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		IItem sesion = null;
		try
		{
			if (numexp==null) numexp=rulectx.getNumExp();
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String strQuery = "WHERE NUMEXP='"+numexp+"'";
			IItemCollection sesiones = entitiesAPI.queryEntities("SECR_SESION", strQuery);
			Iterator it = sesiones.iterator();
			if (it.hasNext())
			{
				sesion = (IItem)it.next();
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return sesion;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getTipoSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String tipo = "";
		try
		{
			IItem sesion = getSesion(rulectx, numexp);
			String strTipo = sesion.getString("TIPO"); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_TIPOSESION", strQuery);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return tipo;
	}

	@SuppressWarnings("rawtypes")
	public static String createNumConvocatoria(IRuleContext rulectx, String organo, String area) throws ISPACException
	{
		String numconv = "?";
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);

	        String strQuery = "WHERE YEAR='"+String.valueOf(year)+"' and ORGANO='"+organo+"'";
	        if(organo.equals("COMI")){
	        	strQuery += " and AREA='"+area+"'";
	        }
	        logger.info("strQuery "+strQuery);
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_CONVOCATORIA", strQuery+" ORDER BY ID");
	        int numero = 0;
	        if(collection.toList().size() != 0){
	        	Iterator it = collection.iterator();
		        
		        IItem col=null;
		        
		        while (it.hasNext()) {
		        	int num = 0;
		        	col = (IItem)it.next();
		        	num = col.getInt("NUMERO");
		        	logger.info("num "+num);
		        	if(!it.hasNext()){
		        		numero = num;
		        	}
		        }
	        }
	        
	        
	        numero = numero + 1;
	        numconv = String.valueOf(numero) + "/" + String.valueOf(year);
	        
	        IItem iConv = entitiesAPI.createEntity("SECR_CONVOCATORIA", rulectx.getNumExp());
	        iConv.set("YEAR", year);
	        iConv.set("NUMERO", numero);
	        iConv.set("NUMEXP_ORIGEN", rulectx.getNumExp());
	        iConv.set("ORGANO", organo);
	        iConv.set("AREA", area);
	        iConv.store(cct);
	        
        	return numconv;
    		
        } 
		catch(ISPACException e)
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
	}
	
	//el tipo de sesion de un organo colegiado
	@SuppressWarnings("rawtypes")
	public static String getOrgano(IRuleContext rctx) throws ISPACRuleException {
		
		String sesion ="";
		
		IClientContext cct = rctx.getClientContext();
		
		try {
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getEntities("SECR_SESION", rctx.getNumExp(), "");
			
			Iterator it = itemCollection.iterator();
	        IItem item = null;
	        
	        while (it.hasNext()) {
	        	
                item = ((IItem)it.next());
                sesion = item.getString("ORGANO");
	        }
	        if(sesion==null) sesion="JGOB";
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return sesion;
	}
	
	@SuppressWarnings("rawtypes")
	public static Vector orderPropuestas(IItemCollection collection) throws ISPACRuleException {
		Iterator it = collection.iterator();
		Vector <IItem> res = new Vector <IItem> ();
		
		try{
			if(collection != null){
				int valor = collection.toList().size();
				if(!(valor==0)){
			        while (it.hasNext()) {
			        	IItem item = ((IItem)it.next());
			        	String desc = item.getString("DESCRIPCION");
			        	desc = desc.replaceFirst("Propuesta - ", "");
			        	logger.info("desc "+desc);
			        	char sNumPropuesta = desc.charAt(0);
			        	logger.info("sNumPropuesta "+sNumPropuesta);
			        	char sNumPropuestaDecima = desc.charAt(1);
			        	logger.info("sNumPropuestaDecima "+sNumPropuestaDecima);
			        	int val = 0;
			        	if(sNumPropuestaDecima != ' '){
			        		String numD = desc.substring(0, 2);
			        		val = Integer.parseInt(numD);
			        	}
			        	else{
			        		val = Integer.parseInt(sNumPropuesta+"");
			        	}
			        	if(res.size()<=val){
			        		res.setSize(val+1);
			        	}
			        	
			        	logger.info("val "+val);
			        	res.set(val, item);
			        }
				}
			}
		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
		return res;
	}
	
	@SuppressWarnings("rawtypes")
	public static Vector orderUrgencias(IItemCollection collection) throws ISPACRuleException {
		Iterator it = collection.iterator();
		Vector <IItem> res = new Vector <IItem> ();
		
		try{
			if(collection != null){
				if(!(collection.toList().size()==0)){
			        while (it.hasNext()) {
			        	IItem item = ((IItem)it.next());
			        	String desc = item.getString("DESCRIPCION");
		
			        	desc = desc.replaceFirst("Propuesta Urgencia - ", "");

			        	logger.info("desc "+desc);
			        	char sNumPropuesta = desc.charAt(0);
			        	logger.info("sNumPropuesta "+sNumPropuesta);
			        	char sNumPropuestaDecima = desc.charAt(1);
			        	logger.info("sNumPropuestaDecima "+sNumPropuestaDecima);
			        	int val = 0;
			        	if(sNumPropuestaDecima != ' '){
			        		String numD = desc.substring(0, 2);
			        		val = Integer.parseInt(numD);
			        	}
			        	else{
			        		val = Integer.parseInt(sNumPropuesta+"");
			        	}
			        	if(res.size()<=val){
			        		res.setSize(val+1);
			        	}
			        	
			        	logger.info("val "+val);
			        	res.set(val, item);
			        }
				}
			}
		}catch(Exception e)
		{
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        }
		return res;
	}

	@SuppressWarnings("unchecked")
	public static IItem obtenerPuntoDiaSesion(IRuleContext rulectx, String numExp) throws ISPACRuleException {
		IItem propuesta = null;
		try{
			
			
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlow = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlow.getEntitiesAPI();
			/****************************************************************************/
			String sQuery = "WHERE numexp='"+numExp+"'";
			IItemCollection itSecretaria = entitiesAPI.queryEntities("SECR_PROPUESTA", sQuery);
			Iterator<IItem> iteratorSec = itSecretaria.iterator();
			while(iteratorSec.hasNext()){
				propuesta = iteratorSec.next();
				
			}
		}
		catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return propuesta;
	}

	public static IItemCollection buscaTodosExpDelDepartamento (IClientContext cct, String numexpPadre, String sQuery) throws ISPACException{
		IItemCollection procedimientosDelDepartamento = null;
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			
			int id_pcd_actual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(id_pcd_actual);
			if(procedimiento != null){
				String org_rsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(org_rsltr)){
					String query = sQuery+" AND ORG_RSLTR = '" + org_rsltr + "'";
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, query);					
				}
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
			throw new ISPACException( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
		}
		return procedimientosDelDepartamento;
	}
	
	public static IItemCollection buscaTodosExp (IClientContext cct, String numexpPadre, String sQuery) throws ISPACException{
		IItemCollection procedimientosDelDepartamento = null;
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			
			int id_pcd_actual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(id_pcd_actual);
			if(procedimiento != null){
				String org_rsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(org_rsltr)){
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, sQuery);					
				}
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
			throw new ISPACException( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
		}
		return procedimientosDelDepartamento;
	}
	/**
	 * Ver si ese expediente es un expediente de Órganos Colegiados
	 * Junta Gobierno, Pleno, ... excluyendo las propuestas ya que todas
	 * tienen como código de procedimiento PROPUESTA-
	 * **/
	public static boolean esExpedienteProcedimientoOrganosColegiados(
			IRuleContext rulectx, String numexp) throws ISPACRuleException {
		boolean esOrganoColegiado = false;
		try{
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			/****************************************************************************/
			IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
			if(expediente!=null){
				String cod_pcd_expediente = expediente.getString("CODPROCEDIMIENTO");
				//Obtenemos el id del procedimiento de RESOLUCION-SECRETARIA
				IItem procedimiento = ProcedimientosUtil.getProcedimientoByCodPcd(rulectx, "RESOLUCION-SECRETARIA");
				if(procedimiento!=null){
					//padre de órganos colegiados
					int idProc_Resol_Secr = procedimiento.getInt("ID");
					//Con el cod de procedimiento del expediente compruebo que sea hijo del anterior y que no contega el código
					//de procedimiento PROPUESTA-
					if(!cod_pcd_expediente.contains("PROPUESTA-")){
						String consulta = "WHERE ID_PADRE="+idProc_Resol_Secr+" AND COD_PCD='"+cod_pcd_expediente+"'";
						IItem proced = ProcedimientosUtil.getProcedimientoByConsulta(rulectx, consulta);
						if(proced!=null){
							esOrganoColegiado = true;
						}
					}
				}
			}
			
			
		}catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la busqueda del tipo de procedimiento de Órgano Colegiados. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la busqueda del tipo de procedimiento de Órgano Colegiados. "+e.getMessage(),e);
		}
		return esOrganoColegiado;
	}

	public static String obtenerPlantillasCOMISesion(IRuleContext rulectx, String sesion, String area) throws ISPACRuleException {
		String nombrePlantilla = "";
		String nombreTabla = "";
		if(sesion.equals("ORD")){
			nombreTabla = "SECR_PLANTILLAS_SESION_ORD";
		}
		if(sesion.equals("EXTR")){
			nombreTabla = "SECR_PLANTILLAS_SESION_EXTR";
		}
		if(sesion.equals("EXUR")){
			nombreTabla = "SECR_PLANTILLAS_SESION_EXUR";
		}
		if(sesion.equals("CONS")){
			nombreTabla = "SECR_PLANTILLAS_SESION_CONS";
		}
		try {
			Iterator<IItem> resultadoPlantillas = ConsultasGenericasUtil.queryEntities(rulectx, nombreTabla, "VALOR='"+area+"'");
			while(resultadoPlantillas.hasNext()){
				IItem plantilla = resultadoPlantillas.next();
				nombrePlantilla = plantilla.getString("SUSTITUTO");
			}
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener las plantillas de la tabla: "+nombreTabla+" del área: "+area+ " sesion: "+sesion+" en el expediente:  "+rulectx.getNumExp()+" - "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre de la plantilla de la tabla: "+nombreTabla+" del área: "+area+ " sesion: "+sesion+" en el expediente:  "+rulectx.getNumExp()+" - "+e.getMessage(),e);
		}
		return nombrePlantilla;
	}
}

















