package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.DateUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [dipucr-Felipe #1061]
 * Clase para la generación del libro de extractos 
 * @author Felipe
 * @since 19.02.2014
 */
public class LibroExtractos
{
	
	/**
	 * Variables (Parámetros)
	 */
	protected Date dFechaIniDec = null;
	protected Date dFechaFinDec = null;
	protected int iInicioDec = Integer.MIN_VALUE;
	protected int iFinDec = Integer.MIN_VALUE;
	protected int iPrimerDecreto = Integer.MIN_VALUE;
	protected int iUltimoDecreto = Integer.MIN_VALUE;
	protected ArrayList<IItem> listDecretos = null;
	protected IEntitiesAPI entitiesAPI = null;
	protected IInvesflowAPI invesFlowAPI = null;
	
	//Históricos
	boolean primero = true;
	/**
	 * Validación y obtención de los parámetros
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
	public boolean validarFechas(IRuleContext rulectx) throws ISPACRuleException {
		
		String sNumexp = null;
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    invesFlowAPI = cct.getAPI();
	  	    entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
	  	    
			sNumexp = rulectx.getNumExp();
			
			//Recuperamos los parámetros de filtrado
			IItemCollection colParametros = entitiesAPI.getEntities("SGB_BUSQUEDA_DECRETOS", sNumexp);
			if (!colParametros.iterator().hasNext()){
				rulectx.setInfoMessage("Es necesario rellenar los parámetros de busqueda en la " +
						"pestaña Búsqueda decretos");
				return false;
			}
			IItem itemParametros = (IItem)colParametros.iterator().next();
			
			dFechaIniDec = itemParametros.getDate("FECHA_DECRETO");
			dFechaFinDec = itemParametros.getDate("FECHA_DECRETO_FIN");
			iInicioDec = itemParametros.getInt("DECRETO_INICIO");
			iFinDec = itemParametros.getInt("DECRETO_FIN");
			
			if (null == dFechaIniDec && null == dFechaFinDec
					&& iInicioDec == Integer.MIN_VALUE && iFinDec == Integer.MIN_VALUE){
				rulectx.setInfoMessage("Es necesario incluir algún parámetro de búsqueda");
				return false;
			}
			
			//Controlamos que haya algún decreto en el periodo seleccionado
			listDecretos = calcularDecretosPeriodo(rulectx.getClientContext());
			if (listDecretos.size() == 0){
				rulectx.setInfoMessage("No hay ningún decreto en el periodo seleccionado");
				return false;
			}
			
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error al realizar las validaciones", e);
		}
		return true;
	}
	
	/**
	 * Recupera la lista de decretos que cumplan las condiciones del filtro
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<IItem> calcularDecretosPeriodo(IClientContext cct)
			throws ISPACException {
		
		StringBuffer sbQuery = new StringBuffer(" WHERE ");
        sbQuery.append(" FECHA_DECRETO IS NOT NULL");
        sbQuery.append(" AND NUMERO_DECRETO IS NOT NULL");
        if (null != dFechaIniDec){
        	sbQuery.append(" AND FECHA_DECRETO >= DATE('" + dFechaIniDec + "') ");
        }
        if (null != dFechaFinDec){
        	sbQuery.append(" AND FECHA_DECRETO < DATE('" + FechasUtil.addDias(dFechaFinDec, 1) + "')");
        }
        if (iInicioDec != Integer.MIN_VALUE){
        	sbQuery.append(" AND NUMERO_DECRETO >= " + iInicioDec);
		}
		if (iFinDec != Integer.MIN_VALUE){
			sbQuery.append(" AND NUMERO_DECRETO <= " + iFinDec);
		}
		sbQuery.append(" ORDER BY ANIO ASC, NUMERO_DECRETO ASC");
		
		IItemCollection decretosCollection = entitiesAPI.
    			queryEntities(Constants.TABLASBBDD.SGD_DECRETO, sbQuery.toString());
		
		return ((ArrayList<IItem>) decretosCollection.toList());
	}
	
	
	/**
	 * Formatear el documento inicial de diligencia
	 * @param xComponent
	 * @param bRechazados 
	 * @throws ISPACException 
	 */
	public void generarTextosDocumento(IRuleContext rulectx, XComponent xComponent) throws ISPACException {
		
		int iDia = Integer.MIN_VALUE;
		int iMes = Integer.MIN_VALUE;
		int iAnyo = Integer.MIN_VALUE;
		String sMes = null;
		Calendar c = null;
		
		XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		
		//Buscamos la cadena de parámetros
		XReplaceable xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
		XReplaceDescriptor xReplaceDescriptor = (XReplaceDescriptor) xReplaceable.createReplaceDescriptor();
        
		//Primer decreto y último decreto
		iPrimerDecreto = listDecretos.get(0).getInt("NUMERO_DECRETO");
		iUltimoDecreto = listDecretos.get(listDecretos.size() - 1).getInt("NUMERO_DECRETO");
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DECRETO_INICIO]", String.valueOf(iPrimerDecreto));
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DECRETO_FIN]", String.valueOf(iUltimoDecreto));
		
		//Fecha inicio
		if (null != dFechaIniDec){
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_INICIO]", 
					FechasUtil.getFormattedDate(dFechaIniDec));
			c = DateUtil.getCalendar(dFechaIniDec);
			//Dia de la fecha inicio
			iDia = c.get(Calendar.DAY_OF_MONTH);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_INICIO]", String.valueOf(iDia));
			//Mes de la fecha inicio
			iMes = c.get(Calendar.MONTH) + 1;
			sMes = DipucrCommonFunctions.getNombreMes(iMes);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_INICIO]", sMes);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES]", sMes);
			//Año de la fecha inicio
			iAnyo = c.get(Calendar.YEAR);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ANYO]", String.valueOf(iAnyo));
		}
		else{
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ANYO]", "");
		}
		
		//Fecha fin
		if (null != dFechaFinDec){
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_FIN]", 
					FechasUtil.getFormattedDate(dFechaFinDec));
			c = DateUtil.getCalendar(dFechaFinDec);
			//Dia de la fecha inicio
			iDia = c.get(Calendar.DAY_OF_MONTH);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_FIN]", String.valueOf(iDia));
			//Mes de la fecha inicio
			iMes = c.get(Calendar.MONTH) + 1;
			sMes = DipucrCommonFunctions.getNombreMes(iMes);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_FIN]", sMes);
		}
		else{
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_FIN]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_FIN]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_FIN]", "");
		}
		
		//Metemos los decretos uno a uno, para no saturar el OpenOffice
		//Nos situamos donde la lista de extractos
		xReplaceDescriptor.setSearchString("[LIBRO_EXTRACTOS]");
		Object oEncontrado = xReplaceable.findFirst(xReplaceDescriptor);
		
		if (null != oEncontrado){
	        XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, oEncontrado);
	        XText xText = xTextDocument.getText();
	        XTextCursor xTextCursor = xText.createTextCursorByRange(xTextRange);
	        
	        //Reemplazamos
	        xReplaceDescriptor.setReplaceString("");
	        xReplaceable.replaceAll(xReplaceDescriptor);
			
			//Vamos insertando decretos
			IItem itemDecreto = null;
			for (int i = 0; i < listDecretos.size(); i++){
				itemDecreto = (IItem) listDecretos.get(i);
				meterExtracto(rulectx, xText, xTextCursor, itemDecreto);
				xText.insertString(xTextCursor, "\r\r", false);
			}
		}
		
	}
	
	/**
	 * 
	 * @param replaceable
	 * @param replaceDescriptor
	 * @param searchString
	 * @param replaceString
	 */
	private void reemplazarCadena(XReplaceable xReplaceable,
			XReplaceDescriptor xReplaceDescriptor, String searchString, String replaceString) {
		
		//Buscamos
		xReplaceDescriptor.setSearchString(searchString);
		//Reemplazamos
        xReplaceDescriptor.setReplaceString(replaceString);
        xReplaceable.replaceAll(xReplaceDescriptor);
	}
	
	/**
	 * Inserta el extracto de decreto en el documento OpenOffice
	 * @param rulectx
	 * @param xText
	 * @param xTextCursor
	 * @param itemDecreto
	 * @throws ISPACException
	 */
	private void meterExtracto(IRuleContext rulectx, XText xText, XTextCursor xTextCursor, IItem itemDecreto) throws ISPACException{
		
		String sNumDecreto = null;
		try{
			XPropertySet xTextProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
			
			StringBuffer sbTexto = new StringBuffer();
			
			//Número de decreto (negrita)
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));
			sbTexto.append("Decreto número ");
			sNumDecreto = itemDecreto.getString("ANIO") + "/" + itemDecreto.getString("NUMERO_DECRETO");
			sbTexto.append(sNumDecreto);
		    xText.insertString(xTextCursor, sbTexto.toString(), false);
			
			//Resto de los datos
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
		    //Número de expediente
		    sbTexto = new StringBuffer();
			String numexpDecreto = itemDecreto.getString("NUMEXP");
			if (StringUtils.isNotEmpty(numexpDecreto)){
				sbTexto.append(", Nº Expediente " + numexpDecreto);
			}
			
			//Fecha
			Date dFechaDecreto = itemDecreto.getDate("FECHA_DECRETO");
	        if (null != dFechaDecreto){
	        	sbTexto.append(" (" + FechasUtil.getFormattedDate(dFechaDecreto) + ")");
	        }
	        sbTexto.append(".");
	        
	        //Extracto
	        String extracto = itemDecreto.getString("EXTRACTO_DECRETO");
	        if (StringUtils.isNotEmpty(extracto)){
	        	if (!esRechazado(rulectx, itemDecreto)){
	        		sbTexto.append("\rExtracto: " + extracto + ".");
	        	}
	        	else{
	        		sbTexto.append("\rANULADO.");
	        	}
	        }
	        sbTexto.append("\r");
	        xText.insertString(xTextCursor, sbTexto.toString(), false);
		}
		catch(Exception e){
			throw new ISPACException
				("Error al insertar el extracto del decreto " +	sNumDecreto, e);
		}
        
	}
	
	/**
	 * Comprueba si el decreto está rechazado
	 * @param rulectx
	 * @param item
	 * @return
	 * @throws ISPACRuleException
	 */
	private boolean esRechazado(IRuleContext rulectx, IItem item) throws ISPACRuleException 
	{
		boolean bRechazado = false;
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	
	        String strQuery = "WHERE NUMEXP = '" + item.getString("NUMEXP") + "'";
	        IItemCollection collection = entitiesAPI.queryEntities("SGD_RECHAZO_DECRETO", strQuery);
	        bRechazado = (collection != null) && (collection.toList().size() > 0);
			return bRechazado;
		}
		catch (Exception e) 
		{
			throw new ISPACRuleException("Error en esRechazado, confecionando listado con los expedientes de Decretos.", e);
		}     
	}
	
}