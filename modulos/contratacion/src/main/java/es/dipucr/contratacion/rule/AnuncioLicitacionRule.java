package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.client.beans.AnuncioLicitacionBean;
import es.dipucr.contratacion.client.beans.Documento;
import es.dipucr.contratacion.client.beans.Garantia;
import es.dipucr.contratacion.client.beans.PersonalContacto;
import es.dipucr.contratacion.client.beans.PliegoBean;
import es.dipucr.contratacion.client.beans.PublicacionesOficialesBean;
import es.dipucr.contratacion.client.beans.SobreElectronico;
import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.contratacion.common.ServiciosWebContratacionFunciones;
import es.dipucr.contratacion.objeto.BOP;
import es.dipucr.contratacion.objeto.DatosContrato;
import es.dipucr.contratacion.objeto.DatosEmpresa;
import es.dipucr.contratacion.objeto.DatosLicitacion;
import es.dipucr.contratacion.objeto.DatosTramitacion;
import es.dipucr.contratacion.objeto.DiariosOficiales;
import es.dipucr.contratacion.objeto.Peticion;
import es.dipucr.contratacion.objeto.Solvencia;
import es.dipucr.contratacion.resultadoBeans.Anuncio;
import es.dipucr.contratacion.resultadoBeans.ExpedientStateData;
import es.dipucr.contratacion.resultadoBeans.PlaceAskResult;
import es.dipucr.contratacion.resultadoBeans.Resultado;
import es.dipucr.contratacion.services.PlataformaContratacionProxy;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

public class AnuncioLicitacionRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(AnuncioLicitacionRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
				
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		//Resultado resultadoAnuncioLicitacion = envioAnuncioLicitacion(rulectx);
		
		//Resultado resultadoPliego = envioPliego(rulectx);
		
		//DipucrFuncionesComunes.envioDocumentosAdicionales(rulectx);
		
		//DipucrFuncionesComunes.acuerdoDictamen(rulectx);
		
		//envioEstadoExpediente(rulectx, resultadoAnuncioLicitacion, resultadoPliego);
		
		//logger.warn("TODO CORRECTOOOOO!!!!!!!!!!!!!!!!!!!!");
		
		return new Boolean(true);
	}

	private void envioEstadoExpediente(IRuleContext rulectx, Resultado resultadoAnuncio) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
				ClientContext cct = (ClientContext) rulectx.getClientContext();
			// --------------------------------------------------------------------			
			
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			try {
				String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
				
				//Petición
				//String publishedByUser = UsuariosUtil.getDni(cct);
				//String publishedByUser = "99001215S";
				String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
				if(publishedByUser==null || publishedByUser.equals("")){
					publishedByUser = UsuariosUtil.getDni(cct);
				}
				
				Resultado resultado = platContratacion.estadoExpediente(entidad, rulectx.getNumExp(), publishedByUser);
				PlaceAskResult placeResult = resultado.getPlaceAskResult();
				if(placeResult!=null){
					if(placeResult.getResultCode().equals("OK")){
						if(placeResult.getExpedientStateData()!=null){
							ExpedientStateData expediente = placeResult.getExpedientStateData();
							if(expediente.getAnuncios()!=null){
								for(int i = 0; i < expediente.getAnuncios().length; i++){
									Anuncio anuncio = expediente.getAnuncios()[i];
									if(anuncio.getUrlPDF()!=null){
										logger.warn("URL: "+anuncio.getUrlPDF());
										String nombreDoc = "";
										if(anuncio.getType().equals("DOC_CN")){
											nombreDoc = "Anuncio de Licitación";
										}
										if(anuncio.getType().equals("DOC_CD")){
											nombreDoc = "Anuncio de Pliego";
										}
										if(!nombreDoc.equals("")){
											DipucrFuncionesComunes.cargaAnuncioFirmado(rulectx, anuncio.getUrlPDF(), nombreDoc, resultadoAnuncio);
										}
										
									}
								}
							}
						}
					}
				}
				

			} catch (RemoteException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. ",e);
			} catch (ISPACException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. ",e);
			} 
		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
	}

	

	@SuppressWarnings("resource")
	private Resultado envioPliego(IRuleContext rulectx) throws ISPACRuleException {
		Resultado resultadoPliego = null;
		try{
			
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			
			PliegoBean pliego = new PliegoBean ();
			
			pliego.setNumExpediente(rulectx.getNumExp());
			
			DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
			if(datContrato!=null){
				pliego.setObjetoContrato(datContrato.getObjetoContrato());
				pliego.setProcContratacion(datContrato.getProcedimientoContratacion());
				pliego.setTipoContrato(datContrato.getTipoContrato());
				pliego.setSubTipoContrato(datContrato.getSubTipoContrato());
				pliego.setTipoTramitacion(datContrato.getTipoTramitacion());
				pliego.setTramitacionGasto(datContrato.getTramitacionGasto());
				pliego.setCpv(datContrato.getCpv());
				pliego.setValorEstimadoContrato(datContrato.getValorEstimadoContrato());
			}
			
			DatosTramitacion datosTramitacion = DipucrFuncionesComunes.getDatosTramitacion(rulectx, rulectx.getNumExp());
			if(datosTramitacion!=null){
				if(datosTramitacion.getPresentacionOfertas()!=null){
					pliego.setPresentacionOfertas(datosTramitacion.getPresentacionOfertas());
					pliego.setFechaPresentacionSolcitudesParticipacion(datosTramitacion.getPresentacionOfertas().getEndDate());
					pliego.setDuracionContrato(datosTramitacion.getDuracionContrato());
				}
			}			
			
			
			Peticion peticion = DipucrFuncionesComunes.getPeticion(rulectx);
			pliego.setPresupuestoConIva(peticion.getPresupuestoConIva());
			pliego.setPresupuestoSinIva(peticion.getPresupuestoSinIva());
			
			DatosEmpresa datEmpresa = DipucrFuncionesComunes.getDatosEmpresa(rulectx, rulectx.getNumExp());
			pliego.setClasificacion(datEmpresa.getClasificacion());
			pliego.setCondLicit(datEmpresa.getCondLicit());
			pliego.setReqDecl(datEmpresa.getTipoDeclaracion());
			
			DatosLicitacion datosLicitacion= DipucrFuncionesComunes.getDatosLicitacion(rulectx);
			//Falta por introducir la entidad 'Criterios de adjudicación'
			pliego.setCriterios(datosLicitacion.getCritAdj());
			pliego.setTipoPresentacionOferta(datosLicitacion.getTipoPresentacionOferta());	
			pliego.setApliPesu(datosLicitacion.getAplicacionPres());
			pliego.setCriterios(datosLicitacion.getCritAdj());
			pliego.setVarOfert(datosLicitacion.getVariantes());
			pliego.setFundacionPrograma(datosLicitacion.getFundacionPrograma());
			pliego.setFormulaRevisionPrecios(datosLicitacion.getRevisionPrecios());
			
			SobreElectronico [] sobreElect = DipucrFuncionesComunes.getSobreElec(rulectx);
			pliego.setSobreElect(sobreElect);
			
			Solvencia solvencia = DipucrFuncionesComunes.getSolvencia(rulectx);
			pliego.setSolvenciaEconomica(solvencia.getSolvenciaEconomica());
			pliego.setSolvenciaTecn(solvencia.getSolvenciaTecn());
			
			PersonalContacto[] persCon = DipucrFuncionesComunes.getPersonalContacto(rulectx);
			pliego.setPersonalContantoContratacion(persCon[0]);
			pliego.setPersonalContantoSecretaria(persCon[1]);
			
			//garantias
			Garantia[] garantia = DipucrFuncionesComunes.getGarantias(rulectx);
			pliego.setGarantia(garantia);
			
			//obtengo el id_tramite y id_fase
			String strQuery = "WHERE NOMBRE = 'Generación de Pliegos' AND NUMEXP='"+rulectx.getNumExp()+"' ORDER BY FECHA_CIERRE DESC";
	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, strQuery);
	        
	        @SuppressWarnings("unchecked")
			Iterator <IItem> it = collection.iterator();
	        if (it.hasNext())
	        {
	        	IItem doc = it.next();
	        	int nidstage = doc.getInt("ID_FASE_PCD");
	        	int idTask = doc.getInt("ID_TRAM_PCD");
	        	
	        	boolean encontradoPPT = false;
	        	boolean encontradoPCEA = false;
	        	
	        	// obtengo los documentos
				String query = "ID_FASE_PCD="+nidstage+" AND ID_TRAMITE_PCD="+idTask+" AND FFIRMA IS NOT NULL AND NUMEXP='"+rulectx.getNumExp()+"'";
				logger.warn("query "+query);
				IItemCollection docsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), query, "FAPROBACION DESC");
				@SuppressWarnings("unchecked")
				Iterator <IItem> docIterator = docsCollection.iterator();
				if(docIterator.hasNext()){
					while(docIterator.hasNext()){
						IItem docPres = docIterator.next();
						String infoPag = "";
						String extension = "";
						if(docPres.getString("INFOPAG_RDE")!=null){
							infoPag= docPres.getString("INFOPAG_RDE");
							extension = docPres.getString("EXTENSION_RDE");
						}
						else{
							if(docPres.getString("INFOPAG")!=null) infoPag= docPres.getString("INFOPAG");
							extension = docPres.getString("EXTENSION");
						}
						logger.warn("infoPag "+infoPag);
						File fichero = DocumentosUtil.getFile(cct, infoPag, null, null);
						logger.warn("fichero "+fichero);
						
						if(docPres.getString("NOMBRE").equals("Pliego de Prescripciones Técnicas") && !encontradoPPT){
							logger.warn("Pliego de Prescripciones Técnicas");
							Documento documentoPPT = new Documento();
						    documentoPPT.setMimeCode("application/octet-stream");
						    Calendar cal=Calendar.getInstance();
							cal.setTime(docPres.getDate("FFIRMA"));
						    documentoPPT.setFechaFirma(cal);
							try {

								FileInputStream fin = null;
								fin = new FileInputStream(fichero);
								long length = fichero.length();
								 
								if (length > Integer.MAX_VALUE) {
									throw new IOException("Tamaño del fichero excesivo: "  + length);
								}
//								Create the byte array
								byte[] bytes = new byte[(int)length];
								 
//								Reads the file content
								int offset = 0;
								int numRead = 0;
								while (offset < bytes.length
									   && (numRead=fin.read(bytes, offset, bytes.length-offset)) >= 0) {
									offset += numRead;
								}
								 
//								Just to check if file was read completely
								if (offset < bytes.length) {
									throw new IOException("No se ha podido leer completamente el fichero " + fichero.getName());
								}
								 
//								Close the input stream, all file contents are in the bytes variable
								fin.close();
								documentoPPT.setContenido(bytes);
							}
							catch(IOException e){
								logger.error(e.getMessage(), e);
								throw new ISPACRuleException("Error. ",e);
							}
							String nombre = docPres.getString("DESCRIPCION");
			 				nombre = DipucrFuncionesComunes.limpiarCaracteresEspeciales(nombre);
			 				logger.warn("*****************************"+nombre);
			 				documentoPPT.setNameDoc(nombre+"."+extension);
						    pliego.setDocumentoPPT(documentoPPT);
						    encontradoPPT = true;
						}
						if(docPres.getString("NOMBRE").equals("Pliego de Clausulas Económico - Administrativas") && !encontradoPCEA){
							//"Pliego de Clausulas Económico - Administrativas"
							logger.warn("Pliego de Clausulas Económico - Administrativas");
							Documento documentoPCAP= new Documento();
							documentoPCAP.setMimeCode("application/octet-stream");
							Calendar cal=Calendar.getInstance();
							cal.setTime(docPres.getDate("FFIRMA"));
							documentoPCAP.setFechaFirma(cal);
							try {
								
								FileInputStream fin = null;
								fin = new FileInputStream(fichero);
								long length = fichero.length();
								 
								if (length > Integer.MAX_VALUE) {
									throw new IOException("Tamaño del fichero excesivo: "  + length);
								}
//								Create the byte array
								byte[] bytes = new byte[(int)length];
								 
//								Reads the file content
								int offset = 0;
								int numRead = 0;
								while (offset < bytes.length
									   && (numRead=fin.read(bytes, offset, bytes.length-offset)) >= 0) {
									offset += numRead;
								}
								 
//								Just to check if file was read completely
								if (offset < bytes.length) {
									throw new IOException("No se ha podido leer completamente el fichero " + fichero.getName());
								}
								 
//								Close the input stream, all file contents are in the bytes variable
								fin.close();
								documentoPCAP.setContenido(bytes);
							}
							catch(IOException e){
								logger.error(e.getMessage(), e);
								throw new ISPACRuleException(e);
							}
							String nombre = docPres.getString("DESCRIPCION");
			 				nombre = DipucrFuncionesComunes.limpiarCaracteresEspeciales(nombre);
			 				logger.warn("*****************************"+nombre);
			 				documentoPCAP.setNameDoc(nombre+"."+extension);
							pliego.setDocumentoPCAP(documentoPCAP);
							encontradoPCEA = true;
						}
						fichero.delete();						
					}
					
					//Envio de la petición de pliego
					try {
						String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
						
						//Petición
						//String publishedByUser = UsuariosUtil.getDni(cct);
						//String publishedByUser = "99001215S";
						String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
						if(publishedByUser==null || publishedByUser.equals("")){
							publishedByUser = UsuariosUtil.getDni(cct);
						}
						resultadoPliego = platContratacion.envioPliegos(entidad, pliego, publishedByUser);

			
					} catch (RemoteException e) {
						logger.error(e.getMessage(), e);
						throw new ISPACRuleException("Error. ",e);
					} 
				}
				else{
					throw new ISPACRuleException("No existe los documentos Pliego de Clausulas Económico - Administrativas o Pliego de Prescripciones Técnicas o no están firmados");
				}
				
				
	        }
	        else{
	        	logger.error("No se ha podido mandar al pliego los documentos.");
				throw new ISPACRuleException("No se ha podido mandar al pliego los documentos.");
	        }

		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return resultadoPliego;
		
	}

	

//	private String quitarEspaciosBlancosAcentos(String nombre) {
//		String sCadenaSinBlancos = "";
//		logger.warn("Inicio: "+nombre);
//		for (int x=0; x < nombre.length(); x++) {
//			  if (nombre.charAt(x) != ' ')
//			    sCadenaSinBlancos += nombre.charAt(x);
//		}
//		logger.warn("sCadenaSinBlancos: "+sCadenaSinBlancos);
//		// sustitución de caracteres especiales
//		// Normalización "expandida"
//		if (!Normalizer.isNormalized(sCadenaSinBlancos, Normalizer.Form.NFD)) {
//			sCadenaSinBlancos = Normalizer.normalize(sCadenaSinBlancos, Normalizer.Form.NFD);
//		}
//		logger.warn("Normalización expandida: "+sCadenaSinBlancos);
//		// Este objeto debería guardarse para su posterior reutilización
//		Pattern isMPattern = Pattern.compile("\\p{IsM}");
//		 
//		// Esta es la parte que tendremos que hacer siempre
//		sCadenaSinBlancos = isMPattern.matcher(sCadenaSinBlancos).replaceAll("");
//
//		logger.warn("Sin espacios en blancos y acentos: "+sCadenaSinBlancos);
//	
//		return sCadenaSinBlancos;
//	}

	private Resultado envioAnuncioLicitacion(IRuleContext rulectx) throws ISPACRuleException {
		Resultado resultadoAnalisisPrevio = null;
		try{
		
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			// --------------------------------------------------------------------
			
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			
			AnuncioLicitacionBean anuncioLicitacion = new AnuncioLicitacionBean();
			
			//Num Expediente
			anuncioLicitacion.setNumexp(rulectx.getNumExp());
			
			DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
			if(datContrato!=null){
				anuncioLicitacion.setObjetoContrato(datContrato.getObjetoContrato());
				anuncioLicitacion.setProcContratacion(datContrato.getProcedimientoContratacion());
				anuncioLicitacion.setTipoContrato(datContrato.getTipoContrato());
				anuncioLicitacion.setSubTipoContrato(datContrato.getSubTipoContrato());
				anuncioLicitacion.setTipoTramitacion(datContrato.getTipoTramitacion());
				anuncioLicitacion.setTramitacionGasto(datContrato.getTramitacionGasto());
				anuncioLicitacion.setCpv(datContrato.getCpv());
				anuncioLicitacion.setValorEstimadoContrato(datContrato.getValorEstimadoContrato());
			}
			
			DatosTramitacion datosTramitacion = DipucrFuncionesComunes.getDatosTramitacion(rulectx, rulectx.getNumExp());
			if(datosTramitacion!=null){
				if(datosTramitacion.getPresentacionOfertas()!=null){
					anuncioLicitacion.setPresentacionOfertas(datosTramitacion.getPresentacionOfertas());
					anuncioLicitacion.setFechaPresentacionSolcitudesParticipacion(datosTramitacion.getPresentacionOfertas().getEndDate());
					anuncioLicitacion.setDuracionContrato(datosTramitacion.getDuracionContrato());
				}
			}			
			
			Peticion peticion = DipucrFuncionesComunes.getPeticion(rulectx);
			anuncioLicitacion.setPresupuestoConIva(peticion.getPresupuestoConIva());
			anuncioLicitacion.setPresupuestoSinIva(peticion.getPresupuestoSinIva());
			
			DatosEmpresa datEmpresa = DipucrFuncionesComunes.getDatosEmpresa(rulectx, rulectx.getNumExp());
			anuncioLicitacion.setClasificacion(datEmpresa.getClasificacion());
			anuncioLicitacion.setCondLicit(datEmpresa.getCondLicit());
			anuncioLicitacion.setReqDecl(datEmpresa.getTipoDeclaracion());
			
			DatosLicitacion datosLicitacion= DipucrFuncionesComunes.getDatosLicitacion(rulectx);
			anuncioLicitacion.setTipoPresentacionOferta(datosLicitacion.getTipoPresentacionOferta());	
			anuncioLicitacion.setApliPesu(datosLicitacion.getAplicacionPres());
			//Falta por introducir la entidad 'Criterios de adjudicación'
			anuncioLicitacion.setCriterios(datosLicitacion.getCritAdj());
			anuncioLicitacion.setVarOfert(datosLicitacion.getVariantes());
			anuncioLicitacion.setFundacionPrograma(datosLicitacion.getFundacionPrograma());
			anuncioLicitacion.setFormulaRevisionPrecios(datosLicitacion.getRevisionPrecios());
	
			PublicacionesOficialesBean publicacionesOficiales = null;			
			DiariosOficiales diariosOficiales =  DipucrFuncionesComunes.getDiariosOficiales(rulectx, "AnuncioLicitacionRule");
			BOP bop = DipucrFuncionesComunes.getBOP(rulectx);			
			if(diariosOficiales !=null || bop!=null){
				publicacionesOficiales = new PublicacionesOficialesBean();
				if(diariosOficiales.getDoue() !=null){
					publicacionesOficiales.setEnviarDOUE(diariosOficiales.getDoue().isPublicarDOUE());
				}
				if(diariosOficiales.getBoe() !=null){
					publicacionesOficiales.setEnviarBOE(diariosOficiales.getBoe().isPublicarBOE());
				}
				if(bop!=null){
					publicacionesOficiales.setNombreOtrosDiarios(bop.getNombreBOP());
					publicacionesOficiales.setFechaPubOtrosDiarios(bop.getFechaPublicacion());
					publicacionesOficiales.setPublishURLOtrosDiarios(bop.getUrlPublicacion());
				}
				
				anuncioLicitacion.setDiarios(publicacionesOficiales);
			}
			
			SobreElectronico [] sobreElect = DipucrFuncionesComunes.getSobreElec(rulectx);
			anuncioLicitacion.setSobreElect(sobreElect);
			
			Solvencia solvencia = DipucrFuncionesComunes.getSolvencia(rulectx);
			anuncioLicitacion.setSolvenciaEconomica(solvencia.getSolvenciaEconomica());
			anuncioLicitacion.setSolvenciaTecn(solvencia.getSolvenciaTecn());	
			
			//garantias
			Garantia[] garantia = DipucrFuncionesComunes.getGarantias(rulectx);
			anuncioLicitacion.setGarantia(garantia);
			
			PersonalContacto[] persCon = DipucrFuncionesComunes.getPersonalContacto(rulectx);
			anuncioLicitacion.setPersonalContactoContratacion(persCon[0]);
			anuncioLicitacion.setPersonalContactoSecretaria(persCon[1]);
			
			try {
				String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
				
				//Petición
				try {
					//String publishedByUser = UsuariosUtil.getDni(cct);
					//String publishedByUser = "99001215S";
					String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
					if(publishedByUser==null || publishedByUser.equals("")){
						publishedByUser = UsuariosUtil.getDni(cct);
					}
					resultadoAnalisisPrevio = platContratacion.envioPublicacionAnuncioLicitacion(entidad, anuncioLicitacion, publishedByUser);
				} catch (ISPACException e) {
					logger.error(e.getMessage(), e);
					throw new ISPACRuleException("Error. ",e);
				}
				

	
			} catch (RemoteException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. ",e);
			}
//			} catch (ISPACException e) {
//				logger.error(e.getMessage(), e);
//				throw new ISPACRuleException("Error. ",e);
//			} 
		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} 
		return resultadoAnalisisPrevio;
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean tienePliego = true;
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
			String strQuery = "WHERE (NOMBRE = 'Pliego de Clausulas Económico - Administrativas' OR NOMBRE ='Pliego de Prescripciones Técnicas') " +
					"AND NUMEXP='"+rulectx.getNumExp()+"' AND FAPROBACION IS NOT NULL";
	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, strQuery);

	        if (!(collection.toList().size() >= 2))
	        {
	        	tienePliego = false;
	        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contratación porque no existen " +
	        			"los documentos Pliego de Clausulas Económico - Administrativas y Pliego de Prescripciones Técnicas");
	        }
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} 
		return tienePliego;
	}

}
