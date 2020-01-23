package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.Serializable;
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

	private static final Logger LOGGER = Logger.getLogger(SecretariaUtil.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getUrgencias(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACException {
		
		List list = null, numbers = null, sorted = null;
		IItem item = null;
		Integer number;
		int orden;
		String strOrden;
		
		try {
			//Obtención de la lista de propuestas y urgencias
			String strQuery = " WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
			LOGGER.info("strQuery Urgencias "+strQuery);
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        list = collection.toList();
	        
	        //Ordenación de la lista
	        numbers = new ArrayList();
	        Iterator it = list.iterator();
	        while (it.hasNext()) {
	        	item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        	if (strOrden != null) {
	        		orden = Integer.parseInt(strOrden); 
	        	} else {
	        		orden = Integer.MAX_VALUE;
	        	}
	        	numbers.add(new Integer(orden));
	        }
	        
	        Collections.sort(numbers);
	        sorted = new ArrayList();
	        it = numbers.iterator();
	        
	        while (it.hasNext()){
	        	number = (Integer)it.next();
	        	orden = number.intValue();
	        	moveItem(list, sorted, orden);
	        }
	        
		} catch(ISPACException e) {
			LOGGER.error(e.getMessage(), e);
        	throw new ISPACException(e);
        }
		
		return sorted;
	}
	
	private static void moveItem(List<?> origen, List<Serializable> destino, int orden) throws ISPACException {
		IItem item = null;
		int n;
		String strOrden;

		try {
			boolean found = false;
			Iterator<?> it = origen.iterator();
		
			while (!found && it.hasNext()) {
				item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        
	        	if (strOrden != null) {
	        		n = Integer.parseInt(strOrden); 
	        	} else {
	        		n = Integer.MAX_VALUE;
	        	}
				found = n==orden; 
			}
			
			if(found) {
				destino.add(item);
				it.remove();
			}
		} catch(ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getPropuestas(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACException {
		List list=null, numbers=null, sorted=null;
		IItem item=null;
		Integer number;
		int orden;
		String strOrden;
		
		try {
			//Obtención de la lista de propuestas y urgencias
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
			LOGGER.info("strQuery Propuestas "+strQuery);
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        list = collection.toList();
	        
	        //Ordenación de la lista
	        numbers = new ArrayList();
	        Iterator it = list.iterator();
	        
	        while (it.hasNext()) {
	        	item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        	
	        	if (strOrden != null) {
	        		orden = Integer.parseInt(strOrden); 
	        	} else {
	        		orden = Integer.MAX_VALUE;
	        	}
	        	numbers.add(new Integer(orden));
	        }
	        
	        Collections.sort(numbers);
	        sorted = new ArrayList();
	        it = numbers.iterator();
	        
	        while (it.hasNext()) {
	        	number = (Integer)it.next();
	        	orden = number.intValue();
	        	moveItem(list, sorted, orden);
	        }	        
		} catch(ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        }
		
		return sorted;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Serializable> getPropuestasYUrgencias(IClientContext cct, String numexp) throws ISPACException {
        
        List<?> list=null;
        List<Integer> numbers=null;
        List<Serializable> sorted=null;
        IItem item=null;
        Integer number;
        int orden;
        String strOrden;

        try {
        	IInvesflowAPI invesflowAPI = cct.getAPI();
        	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
        	
            //Obtención de la lista de propuestas y urgencias
            String strQuery = "WHERE NUMEXP = '" + numexp + "' ORDER BY ORDEN ASC";
            IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
            list = collection.toList();
            collection = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
            list.addAll(collection.toList());
            
            //Ordenación de la lista
            numbers = new ArrayList<Integer>();
            Iterator<?> it = list.iterator();
            
            while (it.hasNext()) {
                item = (IItem)it.next();
                strOrden = item.getString("ORDEN");
                
                if (strOrden != null) {
                    orden = Integer.parseInt(strOrden);
                    
                } else {
                    orden = Integer.MAX_VALUE;
                }
                numbers.add(new Integer(orden));
            }
            Collections.sort(numbers);
            sorted = new ArrayList<Serializable>();
            it = numbers.iterator();
            
            while (it.hasNext()) {
                number = (Integer)it.next();
                orden = number.intValue();
                moveItem(list, sorted, orden);
            }
            
        } catch(ISPACException e) {
            LOGGER.error("Error al obtener las propuestas. " +numexp + " - " + e.getMessage(), e);
            throw new ISPACException("Error al obtener las propuestas. " + numexp + " - " + e.getMessage(), e);
        }
        
        return sorted;
    }
	
	public static String getOrganoSesion(IRuleContext rulectx, String numexp)  throws ISPACException {
		String strOrgano = "";
		
		try {
			IItem sesion = getSesion(rulectx, numexp);
			strOrgano = sesion.getString("ORGANO"); 
		} catch(Exception e) {
			LOGGER.error("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return strOrgano;
	}
	
	public static String getNombreOrganoSesion(IRuleContext rulectx, String numexp)  throws ISPACException {
		String tipo = "";
		
		try {
			String strTipo = SecretariaUtil.getOrganoSesion(rulectx, numexp); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_ORGANOS", strQuery);
	        Iterator<?> it = coll.iterator();
	        
	        if (it.hasNext()) {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
	        
		} catch(Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
		
		return tipo;
	}
	
	public static String getAreaSesion(IRuleContext rulectx, String numexp)  throws ISPACException {
		String strOrgano = "";
		
		try {
			IItem sesion = getSesion(rulectx, numexp);
			strOrgano = sesion.getString("AREA");
			
		} catch(Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
		return strOrgano;
	}

	public static String getNombreAreaSesion(IRuleContext rulectx, String numexp)  throws ISPACException {
		String tipo = "";

		try {
			String strTipo = SecretariaUtil.getAreaSesion(rulectx,numexp); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        
	        String strQuery = "WHERE VALOR = '" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_AREAS", strQuery);
	        Iterator<?> it = coll.iterator();
	        
	        if (it.hasNext()) {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
	        
		} catch(Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
		
		return tipo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void createPropuestaAprobacionActaAnterior(IRuleContext rulectx) throws ISPACException {
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        //Voy a obtener el nombre la propuesta que le corresponde según el órgano resolutor
	      
	        IItemCollection itColprop = buscaTodosExpDelDepartamento(cct, rulectx.getNumExp(), "WHERE COD_PCD LIKE 'PROPUESTA-%'");
	        IItem itPropuestaScpaCtProcedimiento = (IItem) itColprop.iterator().next();
	        String nombrePropuesta = itPropuestaScpaCtProcedimiento.getString("NOMBRE");
	        //Creación del expediente
	        String strQuery = "WHERE NOMBRE = '"+nombrePropuesta+"'";
	        IItemCollection coll = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
	        Iterator it = coll.iterator();
	        int nProcedure = 0;
	        IItem proc = null;
	        int n;
	        String codPcd = "";
	    
	        while (it.hasNext()) {
	        	proc = (IItem)it.next();
	        	n = proc.getInt("ID");
	        
	        	if ( n > nProcedure ) {
	        		nProcedure = n;
	        		codPcd = proc.getString("COD_PCD");
	        	}
	        }
	        
	        Map params = new HashMap();
			params.put("COD_PCD", codPcd);
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
	        
        	if (iExp != null) {
	        	iExp.set(ExpedientesUtil.ASUNTO, "Propuesta aprobación acta anterior");
	        	iExp.set(ExpedientesUtil.ESTADOADM, strEstado);
	        	iExp.store(cct);
	        }
			
	        //Relación con expediente de sesión
        	ExpedientesRelacionadosUtil.relacionaExpedientes(cct, numexp, rulectx.getNumExp(), "Sesión/Propuesta");

		} catch (Exception e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
	}
	
	public static IItemCollection buscaTodosExpDelDepartamento (IClientContext cct, String numexpPadre) throws ISPACException{
		IItemCollection procedimientosDelDepartamento = null;
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			
			int idPcdActual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(idPcdActual);
			if(procedimiento != null){
				String orgRsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(orgRsltr)){
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE ORG_RSLTR = '" + orgRsltr + "' AND COD_PCD LIKE 'PROPUESTA-%'");					
				}
			}
		}
		catch(Exception e){
			LOGGER.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
			throw new ISPACException( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
		}
		
		return procedimientosDelDepartamento;
	}
	
	public static String getEstadoAdmPropuesta(IRuleContext rulectx) throws ISPACException {
		String strEstado = "PR";
		try {
			String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
			if (strOrgano.compareTo("PLEN")==0) {
        		strEstado = "SEC_PL";
        		
        	} else if (strOrgano.compareTo("JGOB")==0) {
        		strEstado = "SEC_JG";
        		
        	} else if (strOrgano.compareTo("COMI")==0) {
        		strEstado = "SEC_CI";
        		
        	} else if (strOrgano.compareTo("MESA")==0) {
        		strEstado = "SEC_MS";
        	}
			
		} catch (Exception e) {
            LOGGER.error("Error. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
            throw new ISPACException("Error . " + rulectx.getNumExp() + " - " + e.getMessage(), e);
        }
		
		return strEstado;
	}
	
	//el tipo de sesion de un organo colegiado
	@SuppressWarnings("rawtypes")
	public static String getSesion(IRuleContext rctx) throws ISPACRuleException {
		
		String sesion ="";
		
		IClientContext cct = rctx.getClientContext();
		
		try {
			LOGGER.info("rctx.getNumExp() "+rctx.getNumExp());
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
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		if(sesion == null){
			sesion = "ORD";
		}
		return sesion;
	}
	
	public static IItem getSesion(IRuleContext rulectx, String numexp)  throws ISPACException {
		IItem sesion = null;
		
		try {
			if (null == numexp){
				numexp=rulectx.getNumExp();
			}
			
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String strQuery = "WHERE NUMEXP = '"+numexp+"'";
			IItemCollection sesiones = entitiesAPI.queryEntities("SECR_SESION", strQuery);
			Iterator<?> it = sesiones.iterator();
			
			if (it.hasNext()) {
				sesion = (IItem)it.next();
			}
		} catch(Exception e) {
            LOGGER.error("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
		
		return sesion;
	}
	
	public static String getTipoSesion(IRuleContext rulectx, String numexp)  throws ISPACException {
		String tipo = "";
		
		try {
			IItem sesion = getSesion(rulectx, numexp);
			String strTipo = sesion.getString("TIPO"); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR = '" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_TIPOSESION", strQuery);
	        Iterator<?> it = coll.iterator();
	        
	        if (it.hasNext()) {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
	        
		 } catch(Exception e) {
			 LOGGER.error("Error . "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACException("Error. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		 }
		
		return tipo;
	}

	public static String createNumConvocatoria(IRuleContext rulectx, String organo, String area) throws ISPACException {
		String numconv = "?";
		
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);

	        String strQuery = "WHERE YEAR = '" + String.valueOf(year) + "' and ORGANO = '" + organo + "'";
	        if("COMI".equals(organo)){
	        	strQuery += " and AREA = '" + area + "'";
	        }
	        
	        LOGGER.info("strQuery "+strQuery);
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_CONVOCATORIA", strQuery + " ORDER BY ID");
	        int numero = 0;
	        
	        if(collection.toList().size() != 0){
	        	Iterator<?> it = collection.iterator();
		        
		        IItem col=null;
		        while (it.hasNext()) {
		        	int num = 0;
		        	col = (IItem)it.next();
		        	num = col.getInt("NUMERO");
		        	LOGGER.info("num "+num);
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
    		
        } catch(ISPACException e) {
			LOGGER.error(e.getMessage(), e);
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
	        
	        if(sesion == null){
	        	sesion = "JGOB";
	        }
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		return sesion;
	}
	
	
	/*public static Vector orderPropuestas(IItemCollection collection) throws ISPACRuleException {
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
	}*/
	
	@SuppressWarnings("unchecked")
	public static Vector<IItem> orderPropuestas(IRuleContext rulectx) throws ISPACRuleException {
	
		Vector <IItem> res = new Vector <IItem> ();

		try{
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();		
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------
			
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
			IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
			Iterator<IItem> it = collection.iterator();
			int ordenPropuesta = 1;

			while(it.hasNext()){
				IItem prop = it.next();
				String numexpOrigen = prop.getString("NUMEXP_ORIGEN");
				IItemCollection itColDoc = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), "numexp='"+rulectx.getNumExp()+"' and descripcion like '%"+numexpOrigen+"%'", "");
				Iterator<IItem> itProcProp = itColDoc.iterator();

				if(itProcProp.hasNext()){
					IItem docProp = itProcProp.next();
					
					if(res.size()<=ordenPropuesta){
						res.setSize(ordenPropuesta+1);
					}
					res.set(ordenPropuesta, docProp);
				}
				ordenPropuesta ++;
			}
		}catch(Exception e) {
			LOGGER.error("Error al ordenar las propuestas. "+e.getMessage(), e);
        	throw new ISPACRuleException("Error al ordenar las propuestas. "+e.getMessage(), e);
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

			        	LOGGER.info("desc "+desc);
			        	char sNumPropuesta = desc.charAt(0);
			        	LOGGER.info("sNumPropuesta "+sNumPropuesta);
			        	char sNumPropuestaDecima = desc.charAt(1);
			        	LOGGER.info("sNumPropuestaDecima "+sNumPropuestaDecima);
			        	int val = 0;
			        	if(sNumPropuestaDecima != ' '){
			        		String numD = desc.substring(0, 2);
			        		val = Integer.parseInt(numD);
			        	} else{
			        		val = Integer.parseInt(sNumPropuesta+"");
			        	}
			        	
			        	if(res.size()<=val){
			        		res.setSize(val+1);
			        	}
			        	
			        	LOGGER.info("val "+val);
			        	res.set(val, item);
			        }
				}
			}
		} catch(Exception e) {
			
        	if (e instanceof ISPACRuleException) {
			    throw new ISPACRuleException(e);
        	}
        	LOGGER.error(e.getMessage(), e);
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
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
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
			
			int idPcdActual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(idPcdActual);
			
			if(procedimiento != null){
				String orgRsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(orgRsltr)){
					String query = sQuery + " AND ORG_RSLTR = '" + orgRsltr + "'";
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, query);					
				}
			}
		} catch(Exception e){
			LOGGER.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
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
			
			int idPcdActual = ExpedientesUtil.getExpediente(cct, numexpPadre).getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimiento = procedureAPI.getProcedureById(idPcdActual);
			if(procedimiento != null){
				String orgRsltr = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				if(StringUtils.isNotEmpty(orgRsltr)){
					procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, sQuery);					
				}
			}
		} catch(Exception e){
			LOGGER.error("Error al recuperar el expediente hijo. " + e.getMessage(), e);
			throw new ISPACException( "Error al recuperar el expediente hijo. " + e.getMessage(), e);
		}
		return procedimientosDelDepartamento;
	}
	/**
	 * Ver si ese expediente es un expediente de Órganos Colegiados
	 * Junta Gobierno, Pleno, ... excluyendo las propuestas ya que todas
	 * tienen como código de procedimiento PROPUESTA-
	 * **/
	public static boolean esExpedienteProcedimientoOrganosColegiados( IRuleContext rulectx, String numexp) throws ISPACRuleException {
		boolean esOrganoColegiado = false;
		try{
			/****************************************************************************/
			IClientContext cct = rulectx.getClientContext();
			/****************************************************************************/
			IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
			
			if(expediente!=null){
				String codPcdExpediente = expediente.getString(ExpedientesUtil.CODPROCEDIMIENTO);
				//Obtenemos el id del procedimiento de RESOLUCION-SECRETARIA
				IItem procedimiento = ProcedimientosUtil.getProcedimientoByCodPcd(rulectx, "RESOLUCION-SECRETARIA");
				
				if(procedimiento!=null){
					//padre de órganos colegiados
					int idProcResolSecr = procedimiento.getInt("ID");
					//Con el cod de procedimiento del expediente compruebo que sea hijo del anterior y que no contega el código
					//de procedimiento PROPUESTA-
					if(!codPcdExpediente.contains("PROPUESTA-")){
						String consulta = "WHERE ID_PADRE = " + idProcResolSecr + " AND COD_PCD = '" + codPcdExpediente + "'";
						IItem proced = ProcedimientosUtil.getProcedimientoByConsulta(rulectx, consulta);
						
						if(proced != null){
							esOrganoColegiado = true;
						}
					}
				}
			}
		} catch (ISPACRuleException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la busqueda del tipo de procedimiento de Órgano Colegiados. "+e.getMessage(),e);
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la busqueda del tipo de procedimiento de Órgano Colegiados. "+e.getMessage(),e);
		}
		
		return esOrganoColegiado;
	}
	
	public static IItem getDocAcuerdoPlenJGByNumExpPropuesta(IClientContext cct, String numExpPropuesta) throws ISPACRuleException{
		IItem acuerdo = null;
		try{
			Vector<String> colOrgaCol = ExpedientesRelacionadosUtil.getExpRelacionadosHijos(cct.getAPI().getEntitiesAPI(), numExpPropuesta);
			for (int i=0; i<colOrgaCol.size(); i++){
				IItem expediente = ExpedientesUtil.getExpediente(cct, colOrgaCol.get(i));
				//Sesión de Pleno -> CR-SECR-03
				//Sesión de Junta de Gobierno -> CR-SECR-05
				if(expediente.getString("CODPROCEDIMIENTO").equals("CR-SECR-03") || expediente.getString("CODPROCEDIMIENTO").equals("CR-SECR-05")){
					IItemCollection icDoc = DocumentosUtil.getDocumentos(cct, expediente.getString("NUMEXP"), "DESCRIPCION LIKE '%" + numExpPropuesta + "%'", "");
					Iterator<?> docNotificaciones;
					
					if (icDoc.next()) {
						String tipoPropuesta = "";
						String numeroPropuesta = "";
						String esUrgencia = "";
						docNotificaciones = icDoc.iterator();
						
						if (docNotificaciones.hasNext()) {
							String descripcion = ((IItem) docNotificaciones.next()).getString("DESCRIPCION");
							String[] vectorDescripcion = descripcion.split(" . ");
							
							if (vectorDescripcion.length > 1) {
								tipoPropuesta = vectorDescripcion[0];
								numeroPropuesta = vectorDescripcion[1];
							}
							if ("PROPUESTA URGENCIA".equalsIgnoreCase(tipoPropuesta)) {
								esUrgencia = ".- Urgencia Certificado";
							} else{
								esUrgencia = ".-Certificado";
							}

							String consulta = "((UPPER(DESCRIPCION) LIKE UPPER('" + numeroPropuesta + esUrgencia + "%')";

							/*if (StringUtils.isNotEmpty(nombre)){
								consulta += "OR UPPER(DESCRIPCION) LIKE UPPER('"
										+ numeroPropuesta
										+ "%"
										+ esUrgencia2
										+ "%" + nombre + "%')";
							}*/

							consulta += ") AND ESTADOFIRMA IN ('02','03')) OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF'))";
							IItemCollection icDoc2 = DocumentosUtil.getDocumentos( cct, expediente.getString("NUMEXP"), consulta, " CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION DESC");
							Iterator<?> it2 = icDoc2.iterator();
						
							while (it2.hasNext()) {
								acuerdo = (IItem) it2.next();
							}
						}
					}					
				}
			}
		} catch (ISPACRuleException e) {
			LOGGER.error("Error al obtenerDocAcuerdoPlenJGByNumExpPropuesta en la propuesta."+numExpPropuesta+" - "+e.getMessage(),e);
			throw new ISPACRuleException("Error al obtenerDocAcuerdoPlenJGByNumExpPropuesta en la propuesta."+numExpPropuesta+" - "+e.getMessage(),e);
		} catch (ISPACException e) {
			LOGGER.error("Error al obtenerDocAcuerdoPlenJGByNumExpPropuesta en la propuesta."+numExpPropuesta+" - "+e.getMessage(),e);
			throw new ISPACRuleException("Error al obtenerDocAcuerdoPlenJGByNumExpPropuesta en la propuesta."+numExpPropuesta+" - "+e.getMessage(),e);
		}
		
		return acuerdo;
	}
	
	public static String getUltimoNumexpPropuesta(IClientContext cct, String numexp) {
        return getNumexpPropuesta(cct, numexp, QueryUtils.EXPRELACIONADOS.ORDER_DESC);
    }
    
    public static String getPrimerNumexpPropuesta(IClientContext cct, String numexp) {
        return getNumexpPropuesta(cct, numexp, QueryUtils.EXPRELACIONADOS.ORDER_ASC);
    }
        
    public static String getNumexpPropuesta(IClientContext cct, String numexp, String orden) {
        String numexpPropuesta = "";
        
        try{
            //Obtenemos el expediente de decreto
            IItemCollection expRelacionadosPadreCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE " + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" + numexp + "' " + orden);
            Iterator<?> expRelacionadosPadreIterator = expRelacionadosPadreCollection.iterator();
            boolean encontrado = false;
            
            while (expRelacionadosPadreIterator.hasNext() && !encontrado){
                
                IItem expRel = (IItem)expRelacionadosPadreIterator.next();
                String numexpRel = "";
                if(StringUtils.isNotEmpty(expRel.getString(ExpedientesRelacionadosUtil.NUMEXP_HIJO)))numexpRel=expRel.getString(ExpedientesRelacionadosUtil.NUMEXP_HIJO);
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexpRel);
                
                if(null != expediente){
                    String nombreProc = "";
                    if(StringUtils.isNotEmpty(expediente.getString(ExpedientesUtil.NOMBREPROCEDIMIENTO)))nombreProc=expediente.getString(ExpedientesUtil.NOMBREPROCEDIMIENTO);
                    
                    if(nombreProc.trim().toUpperCase().contains(Constants.CONSTANTES.PROPUESTA)){
                    	numexpPropuesta = numexpRel;
                        encontrado = true;
                    }
                }
            }
        } catch (ISPACException e ){
            LOGGER.error("ERROR al recuperar el expediente de propuesta relacionado con el expediente: " + numexp + ". " + e.getMessage(), e);
        }
        return numexpPropuesta;
    }

	public static String obtenerPlantillasCOMISesion(IRuleContext rulectx, String sesion, String area) throws ISPACRuleException {
		String nombrePlantilla = "";
		String nombreTabla = "";
		
		if("ORD".equals(sesion)){
			nombreTabla = "SECR_PLANTILLAS_SESION_ORD";
		} else if("EXTR".equals(sesion)){
			nombreTabla = "SECR_PLANTILLAS_SESION_EXTR";
		} else if("EXUR".equals(sesion)){
			nombreTabla = "SECR_PLANTILLAS_SESION_EXUR";
		} else if("CONS".equals(sesion)){
			nombreTabla = "SECR_PLANTILLAS_SESION_CONS";
		}
		
		try {
			Iterator<IItem> resultadoPlantillas = ConsultasGenericasUtil.queryEntities(rulectx, nombreTabla, "VALOR='"+area+"'");
			
			while(resultadoPlantillas.hasNext()){
				IItem plantilla = resultadoPlantillas.next();
				nombrePlantilla = plantilla.getString("SUSTITUTO");
			}
		} catch (ISPACRuleException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener las plantillas de la tabla: "+nombreTabla+" del área: "+area+ " sesion: "+sesion+" en el expediente:  "+rulectx.getNumExp()+" - "+e.getMessage(),e);
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el nombre de la plantilla de la tabla: "+nombreTabla+" del área: "+area+ " sesion: "+sesion+" en el expediente:  "+rulectx.getNumExp()+" - "+e.getMessage(),e);
		}
		
		return nombrePlantilla;
	}
	
	public static String getNumero(IClientContext cct, String numExp, String strTabla) throws ISPACException {
		
		String numAcuerdo = "?";
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		String strQuery = "WHERE NUMEXP_ORIGEN = '" + numExp + "'";
		IItemCollection itemCollection = entitiesAPI.queryEntities(strTabla, strQuery);
		Iterator<?> it = itemCollection.iterator();
		
		if (it.hasNext()) {
			IItem iAcuerdo = (IItem)it.next();
			numAcuerdo = iAcuerdo.getString("NUMERO") + "/" + iAcuerdo.getString("YEAR");
			
		} else {
			throw new ISPACInfo("Se ha producido un error al obtener el número de acuerdo o dictamen.");
		}
		
		return numAcuerdo;
	}
}