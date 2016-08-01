package es.dipucr.sigem.api.rule.procedures.expropiaciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.TablaUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrNotificacionExpropiadoPorParcelas implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(DipucrNotificacionExpropiadoPorParcelas.class);
	
	protected String STR_notificacionEXP = "Notificaciones Expropiado";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
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
			
			//Obtiene los números de expediente de los expropiados
			String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Expropiado/Expropiacion'";
			//logger.warn("strQuery: " + strQuery);
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator <IItem> it = collection.iterator();

			//Si la lista de fincas está vacía no dibujar la tabla
			if(!it.hasNext()){
				return "La lista de expropiados está vacía\n";
			}
			
			while (it.hasNext()) {
				IItem item = it.next();
				String expExpro = item.getString("NUMEXP_HIJO");
			   //logger.warn("Expediente expropiado: " + expExpro);	
			   //de cada expropiado obtengo el listado de fincas
			   strQuery = "WHERE NUMEXP_HIJO = '" + expExpro + "' AND RELACION = 'Expropiado/Finca'";
			   //logger.warn("strQuery "+strQuery);
			   IItemCollection collectionFinca = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			   Iterator itFinca = collectionFinca.iterator();
			   
			   //Obtiene los datos de las fincas de cada expropiado
				strQuery="WHERE NUMEXP = '" + ((IItem)itFinca.next()).getString("NUMEXP_PADRE") + "'";
				
				while (itFinca.hasNext()) {
					strQuery+=" OR NUMEXP = '" + ((IItem)itFinca.next()).getString("NUMEXP_PADRE") + "'";				 		
				}
				//logger.warn("strQuery: " + strQuery);	
				IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + "ORDER BY MUNICIPIO, NUM_POLIGONO ASC, NUM_PARCELA ASC");
				//Si la lista de fincas está vacía no dibujar la tabla
				if(collectionFincas.toList().isEmpty()){
					//logger.warn("La lista de fincas a expropiar está vacía");
					return "La lista de fincas a expropiar está vacía\n";
				}
				
				Iterator itFincas = collectionFincas.iterator();
				
				//Genera los datos de las filas de datos			
				//Lista de listas de filas
				
				//Cada fila individual
				List fila = null;
				
				String nombre = "";
				String destinoId = "";
				
				StringBuffer tabla = new StringBuffer();
				

				while (itFincas.hasNext()) {
					
					List contenido = new ArrayList();
					
					fila = new ArrayList();
					item = (IItem)itFincas.next();
					fila.add(item.getString("NUMEXP"));
					
					//Código que extrae una lista de propietarios
					List listExpropiado = propietariosFinca(cct, item.getString("NUMEXP"), expExpro,titulos);
					Iterator itlistExpropiado = listExpropiado.iterator();
					
					String expr = (String)itlistExpropiado.next();
					String [] expropiado = expr.split("->");
					//logger.warn("expropiado: " + expropiado);	
					
					fila.add(expropiado[0]);	
					nombre = expropiado[1];
					destinoId = expropiado[2];
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
					
					String textoTabla = TablaUtil.formateaTabla(titulos, contenido, 160, true, 1, true);
					
					tabla.append(textoTabla);
					tabla.append("\n");
				}
				
				setLongVariable(rulectx, "EXPRLISTAFINCASPROPIETARIO", tabla.toString());
				
				//creo el documento de las notificaciones
				IItem itemDocume = DocumentosUtil.generarDocumento(rulectx, STR_notificacionEXP, nombre);
				itemDocume.set("DESTINO", nombre);
				/**
				 * INICIO[Teresa] Ticket #106 añadir el destino_id al documento
				 * **/
				if(destinoId!=null && !destinoId.trim().toUpperCase().equals("null")){
					itemDocume.set("DESTINO_ID", destinoId);
				}
				/**
				 * FIN[Teresa] Ticket #106 añadir el destino_id al documento
				 * **/
				itemDocume.store(cct);

				deleteLongVariable(rulectx, "EXPRLISTAFINCASPROPIETARIO");
				fila = new ArrayList();		
			}			
			
			return new Boolean(true);
			
		} catch (Exception e) {
			logger.error("Se produjo una excepción. " + e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción. " + e.getMessage(), e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List propietariosFinca(IClientContext cct , String numExpFinca, String expExpro, List titulos) throws Exception{
		
		List expropiado = new ArrayList();
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
	
		IItem item = null;	
		IItem itemP = null;	
		
		//Obtiene los datos de los expropiados
		IItemCollection collectionExpropiados = ParticipantesUtil.getParticipantes (cct , expExpro, "", "");
		
		// Consulta que debe devolver tantos porcentajes como propietarios haya, ya que hay una entrada para cada uno.
		String strQueryPor = "WHERE NUMEXP = '" + numExpFinca + "'";
		IItemCollection collectionPorcentajes = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryPor);
		
		//Si la lista de expropiados está vacía devolver Desconocido
		if(collectionExpropiados.toList().isEmpty()){
			expropiado.add("Desconocido");
			return expropiado;
			//return "Desconocido";
		}
		
		Iterator itExpropiados = collectionExpropiados.iterator();
		Iterator itPorcentajes = collectionPorcentajes.iterator();
		
		String expropiados = "";
		String nombre = "";
		String id_ext = "";
		while (itExpropiados.hasNext()) {			 
			item = (IItem)itExpropiados.next();
			itemP = (IItem)itPorcentajes.next();
			expropiados+=item.getString("NOMBRE");
			nombre = item.getString("NOMBRE");
			/**
			 * INICIO[Teresa] Ticket #106 añadir el destino_id al documento
			 * **/
			id_ext = item.getString("ID_EXT");
			/**
			 * FIN[Teresa] Ticket #106 añadir el destino_id al documento
			 * **/
			//Descomentar esto para que salgan los datos completos de los expropiados
			
			expropiados+=" ";
			expropiados+=item.getString("DIRNOT");
			expropiados+=" ";
			expropiados+=item.getString("LOCALIDAD");
			expropiados+=" (";
			expropiados+=item.getString("C_POSTAL");
			expropiados+=") ";
			if (item.getString("CAUT") != null){
				expropiados+=item.getString("CAUT");
			}
			
			expropiados+=" ";
			String porcentaje = itemP.getString("PORCENTAJE_PROP");
			if (!porcentaje.equals("100,00")){
				expropiados+="Porcentaje de propiedad: ";
				expropiados+=porcentaje;
			}
			
			if(itExpropiados.hasNext()) {
				expropiados+=" ";	
				int [] anchos = TablaUtil.calcularAnchos(titulos, 160, true, 1, true);				
				
				for (int i=0; i<anchos[1]-2 ; i++) {
					expropiados+="-";	
				}
				expropiados+=" ";				
								
			}
			expropiado.add(expropiados+"->"+nombre+"->"+id_ext);
			expropiados = "";
			
		}			
		//añado al final el nombre de la persona a expropiar
		//Si no se encuentra, es desconocido
		return expropiado;
	}
	
	private void setLongVariable(IRuleContext rulectx, String nombre, String valor) throws ISPACRuleException
	{
		try
		{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			IItem entity = entitiesAPI.createEntity("TSOL_LONG_VARS", rulectx.getNumExp());
			entity.set("NOMBRE", nombre);
			entity.set("VALOR", valor);
			entity.store(cct);
		}
		catch(Exception e)
		{
			throw new ISPACRuleException(e); 	
		}
	}
	private void deleteLongVariable(IRuleContext rulectx, String nombre) throws ISPACRuleException
	{
		try
		{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' AND NOMBRE='" + nombre + "'";
			entitiesAPI.deleteEntities("TSOL_LONG_VARS", strQuery);
		}
		catch(Exception e)
		{
			throw new ISPACRuleException(e); 	
		}
	}
}

