package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class AvisoEmailModDocMiemOrgColegRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(AvisoEmailModDocMiemOrgColegRule.class);
	
	private static Map <String, String[]> mMonths = null;
	private static Map <String, String[]> mWeekDays = null;
	static{
		mMonths = new HashMap<String, String[]>();
		mMonths.put("gl", new String[]{"xaneiro", "febreiro", "marzo", "abril", "maio", "xuño", "xullo", "agosto", "setembro", "outubro", "novembro", "decembro"});
		mMonths.put("eu", new String[]{"urtarrila", "otsaila", "martxoa", "apirila", "maiatza", "ekaina", "uztaila", "abuztua", "iraila", "urria", "azaroa", "abendua"});
		mMonths.put("ca", new String[]{"gener", "febrer", "març", "abril", "maig", "juny", "juliol", "agost", "setembre", "octubre", "novembre", "desembre"});

		mWeekDays = new HashMap<String, String[]>();
		mWeekDays.put("gl", new String[]{"","domingo", "luns", "martes", "mércores", "xoves", "venres", "sábado" });
		mWeekDays.put("eu", new String[]{"","igandea","astelehena", "asteartea", "asteazkena", "osteguna", "ostirala", "larunbata"});
		mWeekDays.put("ca", new String[]{"","diumenge", "dilluns", "dimarts", "dimecres", "dijous", "divendres", "dissabte"});
	
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			/***********************************************************************/
			String numexp = rulectx.getNumExp();
			String squery = "WHERE NUMEXP='"+numexp+"'";
			IItemCollection itemCollectionTramite = entitiesAPI.queryEntities(SpacEntities.SPAC_TRAMITES, squery);
			if(itemCollectionTramite.toList().size()>0){
				if(rulectx.getItem()!=null){
					IItem doc = rulectx.getItem();
					if(doc.getString("NOMBRE").equals("Documentación de Propuesta")){
						String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp()+ "'";
				        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
						Iterator<IItem> itExp = itemCollection.iterator();
						while(itExp.hasNext()){
							IItem itSesion = itExp.next();
							String numexpSesion = itSesion.getString("NUMEXP_HIJO");
							//consultaSQL = "(NUMEXP='"+numexpSesion+"' AND (FECHA_CIERRE IS NOT NULL AND NOMBRE LIKE '%Convocatoria%')";
							
							//Compruebo que sea un expediente de secretaría
							if(esExpedienteOrganosColegiados(entitiesAPI, numexpSesion)){
							
								consultaSQL = "(NUMEXP='"+numexpSesion+"' AND FECHA_CIERRE IS NOT NULL AND NOMBRE LIKE '%Convocatoria%')";
								
								IItemCollection itCTramites = TramitesUtil.getTramites(cct, numexpSesion, consultaSQL, "");
								Iterator<IItem> iTramite = itCTramites.iterator();
								if(iTramite.hasNext()){
									//Compruebo que fecha actual no se mayor que la de la sesión
									consultaSQL = "(NUMEXP='"+numexpSesion+"' AND NOMBRE LIKE '%Acta%')";
									IItemCollection itCTramitesActa = TramitesUtil.getTramites(cct, numexpSesion, consultaSQL, "");
									Iterator<IItem> iTramiteActa = itCTramitesActa.iterator();
									if(!iTramiteActa.hasNext()){
										logger.warn("MODIFICADO");
										
										IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexpSesion, "", "ID");
										String organo = getOrgano(rulectx, numexpSesion);
										IItem sesion = getSesion(rulectx, numexpSesion);
										String strDate=sesion.getString("FECHA");
		
										String pattern = "dd 'de' MMMM 'de' yyyy";
										String input = "dd'/'MM'/'yyyy";
										String locale = rulectx.get("locale");
										SimpleDateFormat dateformat=null;
							        	SimpleDateFormat inputDateformat=null;
							    		if (StringUtils.isEmpty(locale)){
							    			dateformat=new SimpleDateFormat(pattern,new Locale("es"));
							    			inputDateformat=new SimpleDateFormat(input,new Locale("es"));
							    		}else{
							    	    	if (mMonths.get(locale) == null)
							    	    		throw new ISPACRuleException("Valor de locale '" + locale + "' desconocido");
							                DateFormatSymbols dateFormat = getDateFormatSymbols(locale);
							                dateformat=new SimpleDateFormat(pattern, dateFormat);
							                inputDateformat=new SimpleDateFormat(input, dateFormat);
							    		}
							    		String  fechaSesion = "";
							    		if(inputDateformat!=null) fechaSesion = dateformat.format(inputDateformat.parse(strDate));
										String hora = "";
										if(sesion.getString("HORA")!=null)hora = sesion.getString("HORA");
										String direccionTelematica = "";
										if(ConfigurationMgr.getVarGlobal(cct, "DIRECCION_ORGANOS_COLEGIADOS")!=null)direccionTelematica = ConfigurationMgr.getVarGlobal(cct, "DIRECCION_ORGANOS_COLEGIADOS");
										
										if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
											for (int i=0; i<participantes.toList().size(); i++)
											{
												IItem participante = (IItem) participantes.toList().get(i);
												if (participante!=null)
												{
													logger.warn("Cierre convocatoria. Participante: " + participante.getString("NOMBRE"));
													String emailNotif = participante.getString("DIRECCIONTELEMATICA");
													if (emailNotif != null)
													{
														StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
														while (tokens.hasMoreTokens()) 
														{
															String strDestinatario = tokens.nextToken();	
													        logger.warn("Cierre convocatoria. email: " + strDestinatario);
															if (participante!=null)
															{
													        	if (!strDestinatario.equals("")) 
													        	{
															        logger.warn("Enviar correo...");
															        String strAsunto = "Novedades en "+organo;
															        IItem propuestaAviso = SecretariaUtil.obtenerPuntoDiaSesion(rulectx, rulectx.getNumExp());
															        
															        String extractoPropuestaAviso = "";	
															        if(propuestaAviso!=null){
															        															        
																        if(propuestaAviso.getString("EXTRACTO")!=null)extractoPropuestaAviso = propuestaAviso.getString("EXTRACTO");
																        if(extractoPropuestaAviso.equals("")){
																        	IItem expediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
																        	extractoPropuestaAviso = expediente.getString("ASUNTO");
																        }
															        }
															        else{
															        	IItem expediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
															        	extractoPropuestaAviso = expediente.getString("ASUNTO");
															        }

															        
															        //INICIO calculo del numero de la propuesta
															        String strQuery = "WHERE NUMEXP = '" + numexpSesion + "' ORDER BY ORDEN ASC";
															        IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
															        List<IItem> listPropuestas = collection.toList();
															        Iterator<IItem> it = listPropuestas.iterator();
															        int numPropuesta = 1;
															        boolean encontrada = false;
															        while(it.hasNext() && !encontrada){
															        	IItem prop = it.next();
															        	if(prop!=null){
															        		if(prop.getString("EXTRACTO")!=null){
															        			String extProp = prop.getString("EXTRACTO");
															        			if(extProp.equals(extractoPropuestaAviso)){
															        				encontrada = true;
															        			}
															        			else{
															        				numPropuesta ++;
															        			}
															        		}
															        	}
															        }
															        //FIN
															        
															     
															        String strContenido = "En el expediente relativo al punto nº "+numPropuesta+" "
															        		+ "del Orden del Día la sesión de "+organo+" "
															        		+ "que se celebrará el día "+ fechaSesion +" a las "+hora+", denominado '"+extractoPropuestaAviso+"'"
															        		+ ", se ha modificado, eliminado o incorporado un nuevo documento. <br/>"
															        		+ "Si dispone de firma electrónica, puede acceder al expediente pulsando "
															        		+ ""+direccionTelematica+".";
		
															        
													        		/**
													 				* INICIO
																	* ##Ticket #147 SIGEM Documento que especifique a los notificados.
																	* **/
													        		// Eliminar el fichero temporal una vez enviado por correo 
															        try{
															        	MailUtil.enviarCorreo(rulectx.getClientContext(), emailNotif, strAsunto, strContenido);
															        	
													        		} catch(Exception e) {
													                	throw new ISPACRuleException("Error al enviar el correo electrónico a la dirección: "+emailNotif, e);
													                }
															        /**
													 				* FIN
																	* ##Ticket #147 SIGEM Documento que especifique a los notificados.
																	* **/
															        logger.warn("Enviado");
														        }								        	
														    }
														}
													}						
												}						
											}
										}
									}
								}
							}
						}
						
					}
				}
			}
			
		}catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}catch (TransformerFactoryConfigurationError e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		return new Boolean(true);
	}
	
	@SuppressWarnings("unchecked")
	private boolean esExpedienteOrganosColegiados(IEntitiesAPI entitiesAPI,String numexpSesion) throws ISPACRuleException {
		boolean esSesion = false;

		try {
			String strQuery = "WHERE id in (select id_padre from spac_ct_procedimientos where cod_pcd in ("
					+ "select codprocedimiento from spac_expedientes where numexp='"+numexpSesion+"'))";
			IItemCollection coll = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
			Iterator<IItem> it = coll.iterator();
			while(it.hasNext()){
				IItem item = it.next();
				if(item.getString("COD_PCD").equals("RESOLUCION-SECRETARIA")){
					esSesion = true;
				}
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		return esSesion;
	}

	private DateFormatSymbols getDateFormatSymbols(String locale) {
	    	
	    	DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
	    	dateFormatSymbols.setMonths((String[]) mMonths.get(locale));
	    	dateFormatSymbols.setWeekdays((String[]) mWeekDays.get(locale));
	    	return dateFormatSymbols;
		}
		

	@SuppressWarnings("unchecked")
	private IItem getSesion(IRuleContext rulectx, String numexpSesion) throws ISPACException 
	{
		IItem iSesion = null;
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		String strQuery = "WHERE NUMEXP='"+numexpSesion+"'";
		IItemCollection itemCollection = entitiesAPI.queryEntities("SECR_SESION", strQuery);
		Iterator<IItem> it = itemCollection.iterator();
		if (it.hasNext())
		{
			iSesion = (IItem)it.next();
		}
		return iSesion;
	}
	
	/**
	*INICIO
	* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
	*contenido de los email que se envían
	**/
	private String getOrgano (IRuleContext rulectx, String numexpSesion){
		String organo = "";
		try {
			organo = SecretariaUtil.getNombreOrganoSesion(rulectx, numexpSesion);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		
		return organo;
	}
	
	@SuppressWarnings("unchecked")
	public static String getOrgano(IRuleContext rctx) {
		
		String sesion ="";
		
		IClientContext cct = rctx.getClientContext();
		
		try {
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getEntities("SECR_SESION", rctx.getNumExp(), "");
			
			Iterator<IItem> it = itemCollection.iterator();
	        IItem item = null;
	        
	        while (it.hasNext()) {
	        	
                item = ((IItem)it.next());
                sesion = item.getString("ORGANO");
	        }
	        if(sesion==null) sesion="JGOB";
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		return sesion;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
