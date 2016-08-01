package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene un listado con los expedientes de Decretos.
 * Recibe el parámetro tipoExtracto que indica si hay que marcar los expedientes de Decretos para que no vuelvan a ser
 * utilizados en futuros extractos (Extracto de creación "CRE") o no (Extracto de selección "SEL") 
 *
 */
public class ExtractoDecretosTagRule implements IRule {
	
//	private static final String LISTADO_VACIO = "No se han encontrado decretos con los criterios indicados";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * [dipucr-Felipe #1061]
	 * Evitamos que se utilice esta regla, pues puede tags de tamaños muy grandes
	 * pueden provocar caídas del tomcat
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return "Esta regla es antigua y ya no se utiliza, pues puede provocar problemas en la aplicación." +
				" Utilice el procedimiento Libro de Extractos. Si no lo localiza, consulte con el administrador.";
	}
	
//	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
//        try{
//        	
//			//----------------------------------------------------------------------------------------------
//	        ClientContext cct = (ClientContext) rulectx.getClientContext();
//	        IInvesflowAPI invesFlowAPI = cct.getAPI();
//	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
//	        //----------------------------------------------------------------------------------------------
//	
//	        String listDecretos = "";  //Listado de decretos 
//	        //Obtener la fecha y/o número de Decretos para la búsqueda
//	        String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
//	        
//	        IItemCollection collection = entitiesAPI.queryEntities("SGB_BUSQUEDA_DECRETOS", strQuery);
//	        Iterator it = collection.iterator();
//	        IItem item = null;
//	        IItem itemNoIncluido = null;
//
//	        //Obtenemos el parámetro de la plantilla (cre: creación, sel: selección)
//	        String tipoExtracto = rulectx.get("TIPOEXTRACTO");
//	        
//	        //Obtenemos los parámetros recibidos del formulario
//	        Date dFechaIniDec = null;
//			Date dFechaFinDec = null;	        
//	        int iInicioDec = -1;
//	        int iFinDec = -1;
//			
//	        if (it.hasNext()) {
//                item = ((IItem)it.next());
//                dFechaIniDec = item.getDate("FECHA_DECRETO");
//                dFechaFinDec = item.getDate("FECHA_DECRETO_FIN");
//                iInicioDec = item.getInt("DECRETO_INICIO");
//                iFinDec = item.getInt("DECRETO_FIN");
//	        
//	        	//Primero buscar en la tabla del procedimiento Decretos general los expedientes sin incluir en un Extracto de Decretos
//	        	strQuery = "WHERE (INCLUIDO_EXTRACTO IS NULL OR INCLUIDO_EXTRACTO = '0') AND FECHA_DECRETO IS NOT NULL" +
//	        			" AND NUMERO_DECRETO IS NOT NULL";
//	        	
//		        if (dFechaIniDec != null){
//		        	strQuery += " AND FECHA_DECRETO >= '" + dFechaIniDec +"'";
//		        }
//		        if (dFechaFinDec != null){
//		        	strQuery += " AND FECHA_DECRETO <= '" + dFechaFinDec +"'";
//		        }		        	
//		        if (iInicioDec >= 0){
//		        	strQuery += " AND NUMERO_DECRETO >= '" + iInicioDec +"'";
//		        }
//		        if (iFinDec >= 0){
//		        	strQuery += " AND NUMERO_DECRETO <= '" + iFinDec +"'";
//		        }
//	        	
//		        strQuery += " ORDER BY ANIO, NUMERO_DECRETO";
//		        		        
//	        	IItemCollection collectionNoIncluidos = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
//		        Iterator itNoIncluidos = collectionNoIncluidos.iterator();
//
//		        Date fecha = null;
//		        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            	while (itNoIncluidos.hasNext()) {
//            		itemNoIncluido = ((IItem)itNoIncluidos.next());
//	                if (listDecretos.length() > 0){
//	                	listDecretos += "\n\n";
//	                }
//	                listDecretos += " - Decreto número "+itemNoIncluido.getString("ANIO")+"/"+itemNoIncluido.getString("NUMERO_DECRETO");
//	                
//	                if (itemNoIncluido.getString("NUMEXP") != null && itemNoIncluido.getString("NUMEXP") != "")
//	                	listDecretos += ", Nº Expediente: " + itemNoIncluido.getString("NUMEXP");
//	                
//	                fecha=itemNoIncluido.getDate("FECHA_DECRETO");
//	                if (fecha != null){
//	                	listDecretos += " (" + formatter.format(fecha)+")";
//	                }
//                	
//	                listDecretos += ":";
//	                
//	                
//	                if (itemNoIncluido.getString("EXTRACTO_DECRETO") != null && itemNoIncluido.getString("EXTRACTO_DECRETO") != "")
//	                {
//	                	if (!esRechazado(rulectx, itemNoIncluido))
//	                	{
//	                		listDecretos += "\nExtracto: " + itemNoIncluido.getString("EXTRACTO_DECRETO")+".";
//	                	}
//	                	else
//	                	{
//	                		listDecretos += "\nANULADO.";
//	                	}
//	                }
//	                
//	                listDecretos += "\n";
//	                
//	                if (tipoExtracto != null && tipoExtracto.equalsIgnoreCase("CRE")){
//	                	//Marcamos el decreto como ya incluido en un Extracto de decretos
//	                	itemNoIncluido.set("INCLUIDO_EXTRACTO", 1);
//	                	itemNoIncluido.store(rulectx.getClientContext());
//	                }
//            	}
//		        
//	        }//(it.hasNext()) {
//	        
//	        if (listDecretos.equals("")){
//	        	//String mensaje = LISTADO_VACIO;
//	        	//throw new ISPACInfo(mensaje);
//	        	listDecretos = LISTADO_VACIO;
//	        }
//	        return listDecretos;
//	        
//	    } catch (Exception e) {
//	        throw new ISPACRuleException("Error confecionando listado con los expedientes de Decretos.", e);
//	    }     
//	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
//	private boolean esRechazado(IRuleContext rulectx, IItem item) throws ISPACRuleException 
//	{
//		boolean bRechazado = false;
//		try
//		{
//	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
//	
//	        String strQuery = "WHERE NUMEXP = '" + item.getString("NUMEXP") + "'";
//	        IItemCollection collection = entitiesAPI.queryEntities("SGD_RECHAZO_DECRETO", strQuery);
//	        bRechazado = (collection != null) && (collection.toList().size() > 0);
//			return bRechazado;
//		}
//		catch (Exception e) 
//		{
//			throw new ISPACRuleException("Error en esRechazado, confecionando listado con los expedientes de Decretos.", e);
//		}     
//	}
}
