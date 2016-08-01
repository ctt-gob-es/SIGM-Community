package aww.sigem.expropiaciones.rule.planificador;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

/**
 * Generar el día y la hora para cada levantamiento
 */
public class GenerarDiaHoraOcupacionRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(GenerarDiaHoraOcupacionRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try {

			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String expQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collExp = entitiesAPI.queryEntities("EXPR_EEF", expQuery);
			Iterator itExp = collExp.iterator();
			
			IItem itemExp = null;
			if (itExp.hasNext()) {
				itemExp = (IItem) itExp.next();
				String fechas = itemExp.getString("OCUPACION_DIAS_SELECCIONAD");
				if (fechas==null){
					rulectx.setInfoMessage("EL campo Días seleccionados ocupación de Expediente de Expropiacion Forzosa no debe estar vacío.");				
					return false;
				}
			}
		
		} catch (Exception e) {
			logger.error("Se ha producido una excepcion",e);
			throw new ISPACRuleException(e);
		}

		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla GenerarDiaHoraOcupacionRule");
			
			// Hora inicio: 9:30; Hora fin: 14:30; Total 5 horas, 300 min
			int minutosDia = 300;
			int margenDia = 30;
			
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			// Obtengo el expediente
			String expQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collExp = entitiesAPI.queryEntities("EXPR_EEF", expQuery);
			Iterator itExp = collExp.iterator();
			
			// Obtengo la lista de Dias
			String fechas = null;
			int numeroDias = 0;
			int intervalo = 0;
			int margenEntreFinca = 1;
			
			IItem itemExp = null;
			if (itExp.hasNext()) {
				itemExp = (IItem) itExp.next();
				fechas = itemExp.getString("OCUPACION_DIAS_SELECCIONAD");
				numeroDias = itemExp.getInt("OCUPACION_DIAS_NECESARIOS");
				intervalo = itemExp.getInt("INTERVALO_MUNICIPIOS");	
				
				// Si el intervalo es menor que 0 -> No se ha inicializado el campo por lo que se asignan 30 minutos.
				if (intervalo < 0){
					intervalo = 30;
					itemExp.set("INTERVALO_MUNICIPIOS", 30);
					itemExp.store(cct);
				}
				
			} else throw new ISPACRuleException("Se ha producido un error. EXPEDIENTE EXPROPIACION FORZOSA No encontrado");
	
			if(fechas != null ) {
				String[] listaFechas = fechas.split(";");
				
				if(listaFechas.length != numeroDias) throw new  ISPACRuleException("Se ha producido un error. El número de fechas debe ser el mismo que el número de días.");
			
				//Obtiene los números de expediente de las fincas
				String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Finca/Expropiacion'";
				IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
				Iterator it = collection.iterator();
				IItem item = null;
				List expFincas = new ArrayList();
				
				//logger.warn("Expediente de Expropiacion: " + rulectx.getNumExp());

				if(!it.hasNext()){
					logger.error("No se encontraron Fincas");
					return null;
				}
				
				while (it.hasNext()) {
				   item = (IItem)it.next();
				   expFincas.add(item.getString("NUMEXP_HIJO"));
				   //logger.warn("Expediente de Finca: " + item.getString("NUMEXP_HIJO"));			
				}
				
				Iterator itExpFincas = expFincas.iterator();
				
				String whereOrdenFinca="WHERE NUMEXP = '" + itExpFincas.next() + "'";
				while (itExpFincas.hasNext()) {
					whereOrdenFinca+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
				}
				
				String clausulas = "ORDER BY MUNICIPIO";
		
				IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", whereOrdenFinca + clausulas);
				
				ArrayList listaOrden = new ArrayList();			
				//Iterator itFincas = expFincas.iterator();
				
				Iterator itFincas = collectionFincas.iterator();
				IItem itemFincaMunicipio = null;
				
				//logger.warn("Fincas extraidas de la consulta " +collectionFincas.toList().size());
				
				//Sacamos los propietarios
				while(itFincas.hasNext()) {
					
					itemFincaMunicipio = (IItem)itFincas.next();
					//Obtener numPropietarios
					int numPropietarios = 0;
					String filaListaOrden = "";
					String expFinca = itemFincaMunicipio.getString("NUMEXP");
					
					String municipioParajeFinca=itemFincaMunicipio.getString("MUNICIPIO");
					
					if (municipioParajeFinca == null)
						municipioParajeFinca = "";
					
					int indiceSeparador = municipioParajeFinca.indexOf("-");
					String municipioFinca = "";
					
					// Si encuentra el separador se debe quedar solo con el municipio.
					if (indiceSeparador!=-1)
						municipioFinca = municipioParajeFinca.substring(0, indiceSeparador);
					else
						municipioFinca = municipioParajeFinca.substring(0, municipioParajeFinca.length());
					
					
					//logger.warn("Finca: " + expFinca + ",municipio " + municipioFinca);
					
					//logger.warn("Finca: " + expFinca);
					String fincaQuery = "WHERE NUMEXP_PADRE = '" + expFinca + "' AND RELACION = 'Expropiado/Finca'";
					IItemCollection collPropietarios = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", fincaQuery);
					Iterator itPropietarios = collPropietarios.iterator();
					filaListaOrden += municipioFinca.trim() +",";
					
					
					IItem itemProp = null;
					while(itPropietarios.hasNext()) {
						numPropietarios++;
						itemProp = (IItem) itPropietarios.next();
						//Sacamos los datos del propietario
						String numexpProp = itemProp.getString("NUMEXP_HIJO");
						String propsQuery = "WHERE NUMEXP = '" + numexpProp + "'";
						IItemCollection collDataPropietarios = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", propsQuery);
						Iterator itDataPropietarios = collDataPropietarios.iterator();
						if(itDataPropietarios.hasNext()) {
							// Sacamos el nombre del propietario
							IItem dataPropietario = (IItem) itDataPropietarios.next();
							String nombre = dataPropietario.getString("NOMBRE");
							filaListaOrden += nombre;
							if(itPropietarios.hasNext()) {
								filaListaOrden += ",";
							}
						}
					}
					// Añadimos expediente finca y numeroPropietarios por Finca
					filaListaOrden += ";;;;%%%%" + expFinca + ";;;;%%%%" + numPropietarios; 
					listaOrden.add(filaListaOrden);
					//logger.warn("Fila Lista Orden: " + filaListaOrden);
				}
				
				//logger.warn("Lista sin ordenar:");
				printLista(listaOrden);
				
				//Ordenamos
				//logger.warn("Ordenando Lista");
				Collections.sort(listaOrden);
				
				//logger.warn("Lista ordenada:");
				printLista(listaOrden);
							
				List citas = new ArrayList();
				Iterator itListaOrden = listaOrden.iterator();
				String municipioFincaAnterior = "";
				// Para cada fecha proporcionada
				for(int i = 0; i<listaFechas.length; i++) {
					//logger.warn("Fecha "+ i + ": " + listaFechas[i]);
					
					int minutosHoy = 0; 
										
					// Obtener numPropietarios de cada finca
					while((minutosHoy < (minutosDia + margenDia)) && itListaOrden.hasNext()) {
					
						//Obtener los datos de la lista ordenada
						String filaLista = (String) itListaOrden.next();
						
						String municipio = filaLista.substring(0,filaLista.indexOf(","));
						//logger.warn("municipio " + municipio);
						
						if (municipioFincaAnterior.equals("")){
							municipioFincaAnterior = municipio;
							//logger.warn("Inicializar municipioFincaAnterior a " + municipio);
						}
						
						if (!municipioFincaAnterior.trim().equals(municipio.trim()) && !municipio.trim().equals("Desconocido".trim())){
							minutosHoy +=intervalo;
							municipioFincaAnterior = municipio;
						}
						
						
						filaLista = filaLista.substring(filaLista.indexOf(","));
						
						String[] datosFilaLista = filaLista.split(";;;;%%%%");
						String listaPropietarios = datosFilaLista[0];
						String numExpFinca = datosFilaLista[1];
						int numPropietarios = Integer.parseInt(datosFilaLista[2]);
						
						//logger.warn("Finca: " + numExpFinca);
						// Aumento el tiempo necesario para esta finca 
						
						if(numPropietarios <1) {
							minutosHoy += margenEntreFinca;
							logger.error("No se encontraron propietarios");
						} else {
							//logger.warn("Esta finca tiene " + numPropietarios + " propietarios.");
						}
						
						// Si nº numPropietarios >= 1 y <= 3 -> minutosTotal += 5
						if(numPropietarios >= 1 && numPropietarios <=3) {
							minutosHoy += margenEntreFinca;
							//logger.warn("Le asignamos 5 minutos.");
						}
						// Si nº numPropietarios > 3 y <= 10 -> minutosTotal += 10
						if(numPropietarios > 3 && numPropietarios <= 10) {
							minutosHoy += margenEntreFinca * 2;
							//logger.warn("Le asignamos 10 minutos.");
						}
						// Si nº numPropietarios > 10 -> minutosTotal += 15
						if(numPropietarios > 10) {
							minutosHoy += margenEntreFinca * 3;
							//logger.warn("Le asignamos 15 minutos.");
						}
					
						// Calculo la cita
						citas.add(new String[] {numExpFinca,listaFechas[i],calcularHora(minutosHoy) });
						
					}	
					//logger.warn("Fin Fecha "+ i + ": " + listaFechas[i]);
					
					if(!itListaOrden.hasNext()) {
						//logger.warn("Todas las fincas asignadas");
						break;
					}
					
				}
				
				// Almaceno las citas
				
				Iterator itCitas = citas.iterator();
				while(itCitas.hasNext()) {
					String [] cita = (String[])itCitas.next();
					//logger.warn("Finca: " + cita[0] + "; Fecha: " + cita[1] + "; Hora: " + cita[2]);
					
					// Busco la finca
					String fincaQuery = "WHERE NUMEXP = '" + cita[0] + "'";
					IItemCollection collFinca = entitiesAPI.queryEntities("EXPR_FINCAS", fincaQuery);
					Iterator itFinca = collFinca.iterator();
					
					// Y la actualizo con los datos de la cita
					IItem itemFinca = null;
					if (itFinca.hasNext()) {
						itemFinca = (IItem) itFinca.next();
						itemFinca.set("LEV_OCUPACION_FECHA", cita[1]);
						itemFinca.set("LEV_OCUPACION_HORA", cita[2]);
						itemFinca.store(rulectx.getClientContext());
					} else throw new ISPACRuleException("Se ha producido un error. FINCA No encontrada");
					
				}
				
			} else {
				throw new  ISPACRuleException("Se ha producido un error. Debe seleccionar las fechas para generar las citas de actas previas.");
			}		
			
			//logger.warn("Fin de ejecución regla GenerarDiaHoraOcupacionRule");
			return null;
		
		} catch (Exception e) {
			logger.error("Se ha producido una excepcion",e);
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	private String calcularHora(int minutosHoy) {
		Calendar hoy = Calendar.getInstance();
		hoy.set(Calendar.HOUR_OF_DAY, 9);
		hoy.set(Calendar.MINUTE, 30);
		hoy.add(Calendar.MINUTE, minutosHoy);
		String hora = "";
		if (hoy.get(Calendar.HOUR_OF_DAY)<10)
			hora += "0" + hoy.get(Calendar.HOUR_OF_DAY);
		else
			hora += hoy.get(Calendar.HOUR_OF_DAY);
				
		hora +=":";
		if (hoy.get(Calendar.MINUTE)<10)
			hora += "0" + hoy.get(Calendar.MINUTE);
		else
			hora += hoy.get(Calendar.MINUTE);
		return hora;
	}
	
	private void printLista(List lista) {
		Iterator it = lista.iterator();
		//Sacamos los propietarios
		while(it.hasNext()) {
			String elemento = (it.next()).toString();
			logger.warn(elemento);
		}
	}


}
