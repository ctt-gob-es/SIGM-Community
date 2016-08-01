package aww.sigem.expropiaciones.rule.generardoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.PropietariosUtil;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DipucrTablasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class DipucrGenerarDocBOPRule extends DipucrAutoGeneraDocIniTramiteRule {
	
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(DipucrGenerarDocBOPRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + DipucrGenerarDocBOPRule.class);

		IClientContext cct = rulectx.getClientContext();
		
		plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
		
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
		}
		refTablas = "%TABLA1%";

		logger.warn("FIN - " + DipucrGenerarDocBOPRule.class);
		return true;
	}
	public void insertaTabla(IRuleContext rulectx, XComponent xComponent, String refTabla, IEntitiesAPI entitiesAPI, String numexp){
		
		List contenido = generaListFincas(rulectx);
		int numColumnas = 0;
		//Saco el número de columnas
		if(contenido != null){
			numColumnas = ((List)contenido.get(0)).size();
		}
		try {
			DipucrTablasUtil.insertaTabla1(xComponent, contenido, numColumnas-1, false);
		} catch (ISPACException e) {
			logger.error("Error al generar al calcular la tabla. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al generar al calcular la tabla. " + e.getMessage(), e);
		}
		
	}
	
public List generaListFincas(IRuleContext rulectx){
		
		//Genera los datos de las filas de datos			
		//Lista de listas de filas
		List contenido = new ArrayList();
		
		try{
			
			//Genera los encabezados de la tabla
			List titulos = new ArrayList();
			
			titulos.add("Finca Num.");
			titulos.add("Propietario");
			titulos.add("Polígono");
			titulos.add("Parcela");
			titulos.add("Municipio de Parcela");
			titulos.add("Superficie de Parcela (m2)");
			titulos.add("Superficie a ocupar (m2)");
			titulos.add("Calificación");			
			
			
			//Obtener las fincas relacionadas con la Expropiacion
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			//Obtiene los números de expediente de las fincas
			String listado = "";
			String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator it = collection.iterator();
			IItem item = null;
			List expFincas = new ArrayList();
			
			logger.warn("Expediente de Expropiacion: " + rulectx.getNumExp());
			
			while (it.hasNext()) {
			   item = (IItem)it.next();
			   expFincas.add(item.getString("NUMEXP_HIJO"));
			   logger.warn("Expediente de Finca: " + item.getString("NUMEXP_HIJO"));			
			}
			
			//Si la lista de fincas está vacía no dibujar la tabla
			if(expFincas.isEmpty()){
				return contenido;
			}
			
			//Obtiene los datos de las fincas
			Iterator itExpFincas = expFincas.iterator();
			strQuery="WHERE NUMEXP = '" + itExpFincas.next() + "'";
			while (itExpFincas.hasNext()) {
				strQuery+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
			}
				
			
			logger.warn("Fincas a buscar: " + strQuery);
			
			/**
			 * #[Teresa - Ticket 200] INICIO SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/
			
			
			String clausulas = "ORDER BY MUNICIPIO, CASE WHEN NUM_POLIGONO < 'A' THEN LPAD(NUM_POLIGONO, 255, '0') ELSE NUM_POLIGONO END, " +
					"CASE WHEN NUM_PARCELA < 'A' THEN LPAD(NUM_PARCELA, 255, '0') ELSE NUM_PARCELA END";
			
			// 	IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + "ORDER BY MUNICIPIO, NUM_POLIGONO ASC, NUM_PARCELA ASC");
			
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + clausulas);
			
			/**
			 * #[Teresa - Ticket 200] FIN SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/
		
			//Si la lista de fincas está vacía no dibujar la tabla
			if(collectionFincas.toList().isEmpty()){
				return contenido;
			}
			
			Iterator itFincas = collectionFincas.iterator();

			//Cada fila individual
			List fila = null;
			//Añado la cabecera
			contenido.add(titulos);
			
			while (itFincas.hasNext()) {
				fila = new ArrayList();
				item = (IItem)itFincas.next();
				fila.add(item.getString("NUMEXP"));
				//Código que extrae una lista de propietarios
				fila.add(PropietariosUtil.propietariosFinca(entitiesAPI,item.getString("NUMEXP")));				
				fila.add(item.getString("NUM_POLIGONO"));
				fila.add(item.getString("NUM_PARCELA"));
				fila.add(item.getString("MUNICIPIO"));
				fila.add(item.getString("SUP_PARCELA"));
				fila.add(item.getString("SUP_EXPROPIADA"));
				fila.add(item.getString("APROVECHAMIENTO"));
				
				//Si hay algun nulo reemplazarlo por desconocido

				for (int i=0;i<fila.size();i++){
					if (fila.get(i) == null) {
						fila.set(i, "Desconocido");
					}
				}			
				
				contenido.add(fila);				
				logger.warn("Detalle de Expediente de Finca: " + item.getString("NUMEXP") + " Num Finca: " + item.getString("NUM_FINCA"));
			}
		}catch (ISPACRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ISPACException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contenido;
	}

}
