package es.dipucr.sigem.api.rule.procedures.rrhh;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.webempleado.model.mapping.InfoTrienios;
import es.dipucr.webempleado.services.trienios.CumplimientoTrieniosServiceProxy;


/**
 * [eCenpri-Agustin ticket #635]
 * Regla que realiza el correpondiente apunte final en la tabla trienios y vida laboral para el reconocimiento del trienio
 * y cerrar tanto el trámite como el expediente
 * @author Agustin
 * @since 12.11.2012
 */
public class DipucrReconocimientoTrieniosApunteBddRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(DipucrReconocimientoTrieniosApunteBddRule.class);	
	
	String numExp="";
	String fechaDecreto="";
	String numDecreto="";
	String extracto="";
	String anio="";
	String expRel="";
	String message="";
	IItem taskAux = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
		
	}
	
	/**
	 * 
	 *  Compruebo si existe el tramite de notificaciones, ya que si no existe, si realizamos el apunte 
	 *  suma un trienio mas, y este apareceria en la tabla de la notificacion.
	 * 
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		boolean existeTramiteNotificaciones = false;
		
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getStageTasks(rulectx.getNumExp(), rulectx.getStageProcedureId());			
			
			if (itemCollection!=null){			
				Iterator<?> iteratorCollection = itemCollection.iterator();
				IItem task = null;				
				while (iteratorCollection.hasNext()){
					task = (IItem) iteratorCollection.next();					
					if (task.getString("NOMBRE").equalsIgnoreCase("Notificacion decreto trienios")){
						existeTramiteNotificaciones = true;
					}
				}	
			}
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar si existe el tramite de notificacion de trienios", e);
	    } 	
		
		rulectx.setInfoMessage("Antes de generar el trámite de apunte de los trienios, debe generar el trámite: Notificacion decreto trienios.");
		
		return existeTramiteNotificaciones;
		
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.warn("Entra en DipucrReconocimientoTrieniosApunteBddRule-->execute");
		
		CumplimientoTrieniosServiceProxy ctsp = new CumplimientoTrieniosServiceProxy();
		String info_salida="";

		try{
						
			InfoTrienios aux=null;
			String nombre="";
			String nif="";
			int numero_trienios = 0;
			String grupo ="";
			Date fechaTrienioNuevo = null;
			
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************						
			numExp = rulectx.getNumExp();
			expRel = this.dameExpRel(entitiesAPI);
			this.cargaDatosDecreto(entitiesAPI);
			
			//*********************************************
			//Llamada al servicio web de web empleado para obtener trabajadores que cumplen trienio
			//*********************************************
			Object[] listaTrieniosFuncionarios = ctsp.listaCumplimientoTrienios(0,ExpedientesUtil.dameFechaInicioExp(cct, numExp));		
			Object[] listaTrieniosLaborales = ctsp.listaCumplimientoTrienios(1,ExpedientesUtil.dameFechaInicioExp(cct, numExp));			
				
			
			//*********************************************
			//Llamada al servicio web de web empleado para insertar apunte de vida laboral y trienio
			//*********************************************
			if(listaTrieniosFuncionarios!=null)
			{
				logger.warn("Hay " + listaTrieniosFuncionarios.length + " trabajadores funcionarios que cumplen trienio este mes.");
				int contadorFuncionarios=0;
				
				for (int i = 0; i < listaTrieniosFuncionarios.length; i++) {
		    			    		
		    		aux = (InfoTrienios)listaTrieniosFuncionarios[i]; 
		    		
			    	//Tomamos los valores		
					nif = aux.getDni();
					
					if (ParticipantesUtil.existeParticipante(cct, numExp, nif)){
						
						contadorFuncionarios++;
					
						logger.warn("nif: "+nif);											
						
						nombre = aux.getNombre_completo();				
						fechaTrienioNuevo = aux.getFechaTrienio().getTime();	
						numero_trienios =  aux.getNumTrienios();
						grupo = aux.getGrupo();
						SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
						String fechaTrieniosNuevo=formatoDeFecha.format(fechaTrienioNuevo);
						
						logger.warn("FUNCIONARIOS");
						logger.warn("Se inserta vida laboral para el nif: "+nif);
						logger.warn("nif: "+nombre);
						logger.warn("---- DATOS VIDA ADMINISTRATIVA ----");
						logger.warn("nif: "+nif);					
						logger.warn("fechaDecreto: "+this.fechaDecreto);
						logger.warn("numero_trienios: "+numero_trienios);
						logger.warn("fechaTrienioNuevo: "+fechaTrieniosNuevo);
						logger.warn("numExp relacionado de decreto: "+expRel);
						logger.warn("grupo: "+grupo);
						logger.warn("Esta es la fecha del TRIENIO...: "+fechaTrienioNuevo.toString());
						
						//info_salida = info_salida.concat("\n");
						//info_salida = info_salida.concat(nif);
						//info_salida = info_salida.concat("--");
						//info_salida = info_salida.concat(nombre);
						//info_salida = info_salida.concat("--");
						//info_salida = info_salida.concat(numero_trienios);
						
			    		ctsp.insertarVidaAdministrativa(nif, this.fechaDecreto, Integer.toString(numero_trienios), fechaTrieniosNuevo, this.expRel, grupo);
			    		
			    		logger.warn("Se inserta trienio para el nif: "+nif);
			    		logger.warn("nif: "+nombre);
			    		logger.warn("---- DATOS TRIENIO ----");
						logger.warn("nif: "+nif);
						logger.warn("numero_trienios: "+numero_trienios);
						logger.warn("fechaTrienioNuevo: "+fechaTrieniosNuevo);
						logger.warn("fechaDecreto: "+this.fechaDecreto);
						logger.warn("numExp relacionado de decreto: "+expRel);
			    		
			    		ctsp.insertarApunteTrienio(nif,  Integer.toString(numero_trienios), fechaTrieniosNuevo, this.fechaDecreto, grupo, this.expRel);
						
			    		//info_salida = info_salida.concat("Funcionario--"+nif+"--"+nombre+"--"+numero_trienios+" trienios\n");
		    		
					}
				}				
				
				logger.warn("Saco mensaje de funcionarios");
				info_salida = info_salida.concat("Apunte de " + contadorFuncionarios + "trabajadores FUNCIONARIOS.");
				rulectx.setInfoMessage(info_salida);
			}
	    	
	    	if(listaTrieniosLaborales!=null)
	    	{
	    		logger.warn("Hay " + listaTrieniosLaborales.length + "trabajadores laborales que cumplen trienio este mes.");
	    		int contadorLaborales=0;
	    		for (int i = 0; i < listaTrieniosLaborales.length; i++) {
		    		
		    		aux = (InfoTrienios)listaTrieniosLaborales[i]; 
		    		
		    		nif = aux.getDni();
					logger.warn("nif: "+nif);
					
					if (ParticipantesUtil.existeParticipante(cct, numExp, nif)){
					
						contadorLaborales++;
						
						nombre = aux.getNombre_completo();				
						fechaTrienioNuevo = aux.getFechaTrienio().getTime();	
						numero_trienios =  aux.getNumTrienios();
						grupo = aux.getGrupo();
						SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
						String fechaTrieniosNuevo=formatoDeFecha.format(fechaTrienioNuevo);				
			    		
						logger.warn("LABORALES");
						logger.warn("Se inserta vida laboral para el nif: "+nif);
						logger.warn("nif: "+nombre);
						logger.warn("---- DATOS VIDA ADMINISTRATIVA ----");
						logger.warn("nif: "+nif);
						logger.warn("fechaDecreto: "+this.fechaDecreto);
						logger.warn("numero_trienios: "+numero_trienios);
						logger.warn("fechaTrienioNuevo: "+fechaTrieniosNuevo);
						logger.warn("numExp relacionado de decreto: "+expRel);
						logger.warn("grupo: "+grupo);
						
						ctsp.insertarVidaAdministrativa(nif, this.fechaDecreto, Integer.toString(numero_trienios), fechaTrieniosNuevo, this.expRel, this.dameGrupoLaborales(grupo));
			    		
						logger.warn("Se inserta trienio para el nif: "+nif);
						logger.warn("nif: "+nombre);
			    		logger.warn("---- DATOS TRIENIO ----");
						logger.warn("nif: "+nif);
						logger.warn("numero_trienios: "+numero_trienios);
						logger.warn("numero_trienios: "+fechaTrieniosNuevo);
						logger.warn("fechaDecreto: "+this.fechaDecreto);
						logger.warn("numExp relacionado de decreto: "+expRel);
						ctsp.insertarApunteTrienio(nif, Integer.toString(numero_trienios), fechaTrieniosNuevo, this.fechaDecreto, this.dameGrupoLaborales(grupo), this.expRel);
											
						//info_salida = info_salida.concat("Laboral--"+nif+"--"+nombre+"--"+numero_trienios+" trienios\n");
					}
		    			    	
	    		}
		    	
		    	logger.warn("Saco mensaje de laborales");
				info_salida = info_salida.concat("\nApunte de " + contadorLaborales + "trabajadores LABORALES.");
				rulectx.setInfoMessage(info_salida);
				
	    	}
	    	
	    	//Mensaje que comenta que ha ido bien el proceso
			logger.warn("info_salida:\n"+info_salida);					
			logger.warn(rulectx.getInfoMessage());
			logger.warn("Sale de DipucrReconocimientoTrieniosApunteBddRule-->execute");
			
			// Obtener el identificador del trámite
			int taskId = rulectx.getTaskId();
			
			boolean ongoingTX = cct.ongoingTX();
			if (!ongoingTX) {
					cct.beginTX();
			}
			
			if (taskId > 0){			
				taskAux = entitiesAPI.getTask(taskId);
				taskAux.set("OBSERVACIONES", info_salida);
				taskAux.store(cct);	
			}
			
			if (ongoingTX) {
				cct.endTX(true);
			}			
						
			return null;
		}
		catch (Exception e) {
			logger.error("Error el apunte del trienio y vida laboral en la base de datos de personal. " + e.getMessage(), e);
			throw new ISPACRuleException("Error el apunte del trienio y vida laboral en la base de datos de personal. " + e.getMessage(), e);
		}
		
		//return new Boolean(true);
	}
	
	private String dameGrupoLaborales(String grupo){
		
		if(grupo!=null){
		
			if(grupo.equals("A1"))
				return "I";
			else if(grupo.equals("A2"))
				return "II";
			else if(grupo.equals("C1"))
				return "III";
			else if(grupo.equals("C2"))
				return "IV";
			else if(grupo.equals("E"))
				return "V";
		}
		
		return " ";
		
	}
	
    private String dameExpRel(IEntitiesAPI entitiesAPI){
    	
    	try {
    	
	    	String expeRel="";
	    	String sqlQueryPart = "WHERE NUMEXP_PADRE = '"+this.numExp+"'";
	    	IItemCollection expRel;
			
	    	expRel = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
			
	    	if (expRel.iterator().hasNext()){
	    		IItem iExpRel = (IItem)expRel.iterator().next();
	    		expeRel = iExpRel.getString("NUMEXP_HIJO");
	    	}
	    	
	    	return expeRel;
    	
    	} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
    	
    	return null;
    	
    }
    
    private void cargaDatosDecreto(IEntitiesAPI entitiesAPI){
    	
    	try {
	    	String sqlQueryPart = "WHERE NUMEXP = '"+expRel+"'";
	    	IItemCollection decreto;
			
			decreto = entitiesAPI.queryEntities("SGD_DECRETO", sqlQueryPart);
			
	    	if (decreto.iterator().hasNext())
	    	{
	    		IItem iDecreto = (IItem)decreto.iterator().next();
	    		fechaDecreto = iDecreto.getString("FECHA_DECRETO");
	    		numDecreto = iDecreto.getString("NUMERO_DECRETO");
	    		extracto = iDecreto.getString("EXTRACTO_DECRETO");
	    		anio = iDecreto.getString("ANIO");
	    		
	    		logger.warn("-------CARGAR DATOS DECRETO TRIENIOS-----");
	    		logger.warn("fechaDecreto:"+fechaDecreto);
	    		logger.warn("numDecreto:"+numDecreto);
	    		logger.warn("anio:"+anio);
	    	}
    	
    	} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
    }
}
