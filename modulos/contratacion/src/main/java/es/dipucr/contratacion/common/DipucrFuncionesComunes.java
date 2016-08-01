package es.dipucr.contratacion.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.dgpe.codice.common.cbclib.EmbeddedDocumentBinaryObjectType;
import org.jsoup.Jsoup;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;

import es.dipucr.contratacion.client.beans.AplicacionPresupuestaria;
import es.dipucr.contratacion.client.beans.Campo;
import es.dipucr.contratacion.client.beans.CondicionesLicitadores;
import es.dipucr.contratacion.client.beans.CriterioAdjudicacionMultCrit;
import es.dipucr.contratacion.client.beans.CriteriosAdjudicacion;
import es.dipucr.contratacion.client.beans.DatoDocumento;
import es.dipucr.contratacion.client.beans.Documento;
import es.dipucr.contratacion.client.beans.DuracionContratoBean;
import es.dipucr.contratacion.client.beans.EventoAperturaBean;
import es.dipucr.contratacion.client.beans.FormalizacionBean;
import es.dipucr.contratacion.client.beans.FundacionPrograma;
import es.dipucr.contratacion.client.beans.Garantia;
import es.dipucr.contratacion.client.beans.LicitadorBean;
import es.dipucr.contratacion.client.beans.OfertasRecibidas;
import es.dipucr.contratacion.client.beans.Periodo;
import es.dipucr.contratacion.client.beans.PersonaComite;
import es.dipucr.contratacion.client.beans.PersonalContacto;
import es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones;
import es.dipucr.contratacion.client.beans.ResultadoLicitacion;
import es.dipucr.contratacion.client.beans.SobreElectronico;
import es.dipucr.contratacion.client.beans.SolvenciaEconomica;
import es.dipucr.contratacion.client.beans.SolvenciaTecnica;
import es.dipucr.contratacion.client.beans.VariantesOfertas;
import es.dipucr.contratacion.objeto.BOE;
import es.dipucr.contratacion.objeto.BOP;
import es.dipucr.contratacion.objeto.DOUE;
import es.dipucr.contratacion.objeto.DatosContrato;
import es.dipucr.contratacion.objeto.DatosEmpresa;
import es.dipucr.contratacion.objeto.DatosLicitacion;
import es.dipucr.contratacion.objeto.DatosTramitacion;
import es.dipucr.contratacion.objeto.DiariosFechaOficiales;
import es.dipucr.contratacion.objeto.DiariosOficiales;
import es.dipucr.contratacion.objeto.Peticion;
import es.dipucr.contratacion.objeto.Solvencia;
import es.dipucr.contratacion.resultadoBeans.Anuncio;
import es.dipucr.contratacion.resultadoBeans.ExpedientStateData;
import es.dipucr.contratacion.resultadoBeans.Mensaje;
import es.dipucr.contratacion.resultadoBeans.OfficialPublicationResult;
import es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults;
import es.dipucr.contratacion.resultadoBeans.PlaceAskResult;
import es.dipucr.contratacion.resultadoBeans.PublicationResult;
import es.dipucr.contratacion.resultadoBeans.PublishErrorDetails;
import es.dipucr.contratacion.resultadoBeans.Resultado;
import es.dipucr.contratacion.resultadoBeans.VisualizationResult;
import es.dipucr.contratacion.services.PlataformaContratacionProxy;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrFuncionesComunes {
	
	public static final Logger logger = Logger.getLogger(DipucrFuncionesComunes.class);
	
	public static final String DOC_PIN = "Anuncio de Informacion Previa";  //Anuncio previo
	public static final String DOC_CN = "Anuncio de Licitación";	 //Anuncio de Licitación
	public static final String DOC_CD = "Anuncio de Pliego";	 //Pliego
	public static final String DOC_ADJ = "Anuncio Resultado Licitación";	 //Anuncio de adjudicacion
	public static final String DOC_GEN = "DOC_GEN";	 //Documentos generales
	
	
	public static void envioEstadoExpediente(IRuleContext rulectx, Resultado resultadoEnvio, String tipoLicitacion) throws ISPACRuleException {
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
				if(resultadoCorrecto(resultadoEnvio)){
				
					Resultado resultado = platContratacion.estadoExpediente(entidad, rulectx.getNumExp(), publishedByUser);
					//Resultado resultado = platContratacion.estadoExpediente("005", "DPCR2013/54503", publishedByUser);
					PlaceAskResult placeResult = resultado.getPlaceAskResult();
					if(placeResult!=null){
						if(placeResult.getResultCode().equals("OK")){
							if(placeResult.getExpedientStateData()!=null){
								ExpedientStateData expediente = placeResult.getExpedientStateData();
								if(expediente.getAnuncios()!=null){
									String nombreDoc = "";
									for(int i = 0; i < expediente.getAnuncios().length && nombreDoc.equals(""); i++){
										Anuncio anuncio = expediente.getAnuncios()[i];
										if(anuncio.getUrlPDF()!=null){
											logger.warn("URL: "+anuncio.getUrlPDF());
											
											ResultadoLicitacion licitacion = DipucrFuncionesComunes.getResultadoLicitacion(rulectx);
											
											if(anuncio.getType().equals("DOC_CN") && tipoLicitacion.equals("Licitacion")){
												nombreDoc = "Anuncio de Licitación";
											}
											if(anuncio.getType().equals("DOC_CD") && tipoLicitacion.equals("Pliego")){
												nombreDoc = "Anuncio de Pliego";
											}
											if(anuncio.getType().equals("DOC_CAN_ADJ") && licitacion.getCodigoAdjudicacion().getId().equals("8")){
												nombreDoc = "Anuncio de Adjudicación";
											}
											if(anuncio.getType().equals("DOC_FORM") && licitacion.getCodigoAdjudicacion().getId().equals("9")){
												nombreDoc = "Anuncio de Formalización";
											}
											if(anuncio.getType().equals("RENUNCIA") && licitacion.getCodigoAdjudicacion().getId().equals("5")){
												nombreDoc = "Anuncio de Renuncia";
											}
											if(anuncio.getType().equals("DESISTIMIENTO") && licitacion.getCodigoAdjudicacion().getId().equals("4")){
												nombreDoc = "Anuncio de Desistimiento";
											}
											if(anuncio.getType().equals("DESIERTO") && licitacion.getCodigoAdjudicacion().getId().equals("3")){
												nombreDoc = "Anuncio de Desierto";
											}
											if(!nombreDoc.equals("")){
												DipucrFuncionesComunes.cargaAnuncioFirmado(rulectx, anuncio.getUrlPDF(), nombreDoc, resultadoEnvio);
											}
											
										}
									}
								}
							}
							else{
								DipucrFuncionesComunes.errorPeticion(resultadoEnvio.getPublicacion(), rulectx, tipoLicitacion);
							}
						}
						else{
							DipucrFuncionesComunes.errorPeticion(resultadoEnvio.getPublicacion(), rulectx, tipoLicitacion);
						}
					}
				}
				else{
					DipucrFuncionesComunes.errorPeticion(resultadoEnvio.getPublicacion(), rulectx, tipoLicitacion);
				}

			} catch (RemoteException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. "+e.getMessage(),e);
			} catch (ISPACException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. "+e.getMessage(),e);
			} 
		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}
	

	private static boolean resultadoCorrecto(Resultado resultadoEnvio) {
		boolean resultado = false;
		PublicationResult publicacion = resultadoEnvio.getPublicacion();
		if(publicacion.getResultCode().equals("OK")) resultado=true;
		return resultado;
	}


	@SuppressWarnings({ "resource", "unchecked" })
	public static Documento getDocumento(IRuleContext rulectx, String nombreDoc, String nombreMostrarDoc) throws ISPACRuleException {
		Documento docAdj = null;
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
		

        	String query = "ID_TRAMITE="+rulectx.getTaskId()+" AND NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='"+nombreDoc+"'";
			IItemCollection docsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), query, "FAPROBACION DESC");
			Iterator <IItem> docIterator = docsCollection.iterator();
			if(docIterator.hasNext()){
				docAdj = new Documento();
				IItem docPres = docIterator.next();
				String infoPag = "";
				String extension = "";
				if(docPres.getString("INFOPAG_RDE")!=null){
					if(docPres.getString("INFOPAG_RDE")!=null) infoPag= docPres.getString("INFOPAG_RDE");
					if(docPres.getString("EXTENSION_RDE")!=null) extension= docPres.getString("EXTENSION_RDE");
				}
				else{
					if(docPres.getString("INFOPAG")!=null) infoPag= docPres.getString("INFOPAG");
					if(docPres.getString("EXTENSION")!=null) extension= docPres.getString("EXTENSION");
				}
				
				File fichero = DocumentosUtil.getFile(cct, infoPag, null, null);
				docAdj.setMimeCode("application/octet-stream");
				Calendar cal=Calendar.getInstance();
				cal.setTime(docPres.getDate("FFIRMA"));
				docAdj.setFechaFirma(cal);
				
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
				docAdj.setContenido(bytes);
				
				docAdj.setExpedientNumber(rulectx.getNumExp());
 				
 				String descripcion = DipucrFuncionesComunes.limpiarCaracteresEspeciales(nombreMostrarDoc);
 				logger.warn("descripcion "+descripcion);
 				docAdj.setDescripcion(descripcion+"."+extension+"");
 				

 				String nombre = nombreMostrarDoc.replace("-", " ");
 				nombre = nombreMostrarDoc.replace("º", "");
 				nombre = nombre.replace("_", " ");

 				docAdj.setNameDoc(nombre);
 				
 				//Obtengo el tipo de documento.
 				query = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
 				IItemCollection collectionDoc = entitiesAPI.queryEntities("CONTRATACION_DOC_ADICIONALES", query);
	        	Iterator<IItem> itDoc = collectionDoc.iterator();
        		if(itDoc.hasNext()){
        			IItem docTipo = itDoc.next();
        			String tipoDoc = docTipo.getString("TIPO_DOC");
        			String [] vtipoDoc = tipoDoc.split(" - ");
					if(vtipoDoc.length >1){
						docAdj.setIdTypeDoc(vtipoDoc[0]);
						docAdj.setTypeDoc(vtipoDoc[1]);
					}
        		}
 				

			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return docAdj;
	}
	
	/* Función que elimina acentos y caracteres especiales de
	* una cadena de texto.
	* @param input
	* @return cadena de texto limpia de acentos y caracteres especiales.
	*/
	public static String limpiarCaracteresEspeciales(String input) {

		 // Cadena de caracteres original a sustituir.
		 String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		 // Cadena de caracteres ASCII que reemplazarán los originales.
		 String ascii    = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		 
		 input = input.replace(" ", "");
		 input = input.replace("º", "");
		 input = input.replace("-", "");
		 input = input.replace("_", "");
		 input = input.replace("(", "");
		 input = input.replace(")", "");
		 String output = input;
	
		 for (int i=0; i<original.length(); i++) {
			 // Reemplazamos los caracteres especiales.
			 output = output.replace(original.charAt(i), ascii.charAt(i));
		 }
		 return output;
	}
	
	public static String limpiarCaracteres(String input) {

		 // Cadena de caracteres original a sustituir.
		 String original = "-";
		 // Cadena de caracteres ASCII que reemplazarán los originales.
		 String ascii    = "_";
		 
		 String output = input;
	
		 for (int i=0; i<original.length(); i++) {
			 // Reemplazamos los caracteres especiales.
			 output = output.replace(original.charAt(i), ascii.charAt(i));
		 }
		 return output;
	}


	
	public static ResultadoLicitacion getResultadoLicitacion(
			IRuleContext rulectx) throws ISPACRuleException {
		ResultadoLicitacion resLicitacion = new ResultadoLicitacion();
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection resLiciCollection = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			@SuppressWarnings("unchecked")
			Iterator <IItem> resLiciIterator = resLiciCollection.iterator();
			while(resLiciIterator.hasNext()){
				IItem iResultadoLicitacion = resLiciIterator.next();
				
				if(iResultadoLicitacion.getString("RES_LICITACION")!=null) {
					String critSolv = iResultadoLicitacion.getString("RES_LICITACION");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						resLicitacion.setCodigoAdjudicacion(new Campo(vcritSolv[0], vcritSolv[1]));
					}
				}
			}
		
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return resLicitacion;
	}
	
	public static PersonalContacto[] getPersonalContacto(IRuleContext rulectx) throws ISPACRuleException {
		PersonalContacto[] persCont = new PersonalContacto[2];
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Personal contacto contratacion
			persCont[0] = new PersonalContacto();
			//Personal contacto secretaria
			persCont[1] = new PersonalContacto();
			IItemCollection persContCollection = entitiesAPI.getEntities("CONTRATACION_PERS_CONTACTO", rulectx.getNumExp());
			@SuppressWarnings("unchecked")
			Iterator <IItem> persContIterator = persContCollection.iterator();
			while(persContIterator.hasNext()){
				IItem itPersCont = persContIterator.next();
				if(itPersCont.getString("NOMBRE")!=null){
					persCont[0].setNombreContacto(itPersCont.getString("NOMBRE"));
				}
				if(itPersCont.getString("EMAIL")!=null){
					persCont[0].setEmail(itPersCont.getString("EMAIL"));
				}
				if(itPersCont.getString("CALLE")!=null){
					persCont[0].setCalle(itPersCont.getString("CALLE"));
				}
				if(itPersCont.getString("CP")!=null){
					persCont[0].setCp(itPersCont.getInt("CP")+"");
				}
				if(itPersCont.getString("LOCALIDAD")!=null){
					persCont[0].setCiudad(itPersCont.getString("LOCALIDAD"));
				}
				if(itPersCont.getString("PROVINCIA")!=null){
					persCont[0].setProvincia(itPersCont.getString("PROVINCIA"));
				}
				if(itPersCont.getString("MOVIL")!=null){
					persCont[0].setTelefono(itPersCont.getString("MOVIL"));
				}
				Campo formatoDirec = new Campo("1", "Spanish Format");
				persCont[0].setCodFormatoDirec(formatoDirec);
				
				Campo pais = new Campo("ES", "España");
				persCont[0].setPais(pais);
				
				if(itPersCont.getString("LOCALIZACIONGEOGRAFICA")!=null) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						persCont[0].setLocalizacionGeografica(new Campo(vcritSolv[0], vcritSolv[1]));
					}
				}
				
				//SECRETARIA
				if(itPersCont.getString("NOMBRESECRE")!=null){
					persCont[1].setNombreContacto(itPersCont.getString("NOMBRESECRE"));
				}
				if(itPersCont.getString("EMAILSECRE")!=null){
					persCont[1].setEmail(itPersCont.getString("EMAILSECRE"));
				}
				if(itPersCont.getString("CALLESECRE")!=null){
					persCont[1].setCalle(itPersCont.getString("CALLESECRE"));
				}
				if(itPersCont.getString("CPSECRE")!=null){
					persCont[1].setCp(itPersCont.getString("CPSECRE"));
				}
				if(itPersCont.getString("LOCALIDADSECRE")!=null){
					persCont[1].setCiudad(itPersCont.getString("LOCALIDADSECRE"));
				}
				if(itPersCont.getString("PROVINCIASECRE")!=null){
					persCont[1].setProvincia(itPersCont.getString("PROVINCIASECRE"));
				}
				if(itPersCont.getString("MOVILSECRE")!=null){
					persCont[1].setTelefono(itPersCont.getString("MOVILSECRE"));
				}

				persCont[1].setCodFormatoDirec(formatoDirec);
				
				persCont[1].setPais(pais);
				
				if(itPersCont.getString("LOCALIZACIONGEOGRAFICASECRE")!=null) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICASECRE");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						persCont[1].setLocalizacionGeografica(new Campo(vcritSolv[0], vcritSolv[1]));
					}
				}
			
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return persCont;
	}
	
	@SuppressWarnings("unchecked")
	public static Garantia[] getGarantias(IRuleContext rulectx) throws ISPACRuleException {
		Garantia[] vgarantia = null;
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection garantiaCollection = entitiesAPI.getEntities("CONTRATACION_GARANTIAS", rulectx.getNumExp());
			Iterator <IItem> garantiaIterator = garantiaCollection.iterator();
			vgarantia = new Garantia [garantiaCollection.toList().size()];
			int i = 0;
			while(garantiaIterator.hasNext()){
				IItem garantiaPres = garantiaIterator.next();
				vgarantia[i] = new Garantia();
				if(garantiaPres.getString("AMOUNTRATE")!=null){
					vgarantia[i].setAmountRate(garantiaPres.getString("AMOUNTRATE"));
				}
				if(garantiaPres.getDate("CONSTITUTIONPERIOD")!=null){
					Calendar cal=Calendar.getInstance();
					cal.setTime(garantiaPres.getDate("CONSTITUTIONPERIOD"));
					vgarantia[i].setConstitutionPeriod(cal);
				}
				if(garantiaPres.getString("IMPORTEGARANTIA")!=null){
					vgarantia[i].setImporteGarantia(garantiaPres.getString("IMPORTEGARANTIA"));
				}
				if(garantiaPres.getString("PERIODOGARANTIA")!=null){
					vgarantia[i].setPeriodoGarantia(garantiaPres.getString("PERIODOGARANTIA"));
				}
				if(garantiaPres.getString("TIPO_GARANTIA")!=null) {
					String critSolv = garantiaPres.getString("TIPO_GARANTIA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						vgarantia[i].setTipoGarantia(new Campo(vcritSolv[0], vcritSolv[1]));
					}
				}
				if(garantiaPres.getString("PERIODUNITCODE")!=null) {
					String critSolv = garantiaPres.getString("PERIODUNITCODE");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						vgarantia[i].setTipoPeriodo(new Campo(vcritSolv[0], vcritSolv[1]));
					}
				}
				if(garantiaPres.getString("DESCRIPCION")!=null){
					String [] descripcion = {garantiaPres.getString("DESCRIPCION")};
					vgarantia[i].setDescripcion(descripcion);
				}
				i++;
			}
			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		
		return vgarantia;
		
	}
	
	@SuppressWarnings("unchecked")
	public static Solvencia getSolvencia(IRuleContext rulectx) throws ISPACRuleException {
		Solvencia solvencia = new Solvencia();
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			Vector <SolvenciaTecnica> vSolvenciaTecnica = new Vector<SolvenciaTecnica>();
			Vector <SolvenciaEconomica> vSolvenciaEconomica = new Vector<SolvenciaEconomica>();
			
			IItemCollection solvenciaCollection = entitiesAPI.getEntities("CONTRATACION_SOLVENCIA_TECN", rulectx.getNumExp());
			Iterator <IItem> solvenciaIterator = solvenciaCollection.iterator();
			while(solvenciaIterator.hasNext()){
				IItem solvenciaPres = solvenciaIterator.next();
				/**
				 * CRITERIOS DE SOLVENCIA TÉCNICA
				 * **/
				if(solvenciaPres.getString("CRIT_SOLVENCIA")!=null){
					SolvenciaTecnica solTecnica = new SolvenciaTecnica();
					//Criterio de solvencia
					if(solvenciaPres.getString("CRIT_SOLVENCIA")!=null) {
						String critSolv = solvenciaPres.getString("CRIT_SOLVENCIA");
						String [] vcritSolv = critSolv.split(" - ");
						if(vcritSolv.length >1){
							solTecnica.setCriterioSolvencia(new Campo(vcritSolv[0], vcritSolv[1]));
						}
					}
					//Descripción
					if(solvenciaPres.getString("DESCRIPCION")!=null){
						String [] descripcion = {solvenciaPres.getString("DESCRIPCION")};
						solTecnica.setDescripcion(descripcion);
					}
					//Valor Umbral Importe
					if(solvenciaPres.getString("VALORUMBRALIMPORTE")!=null) solTecnica.setValorUmbralImporte(solvenciaPres.getString("VALORUMBRALIMPORTE"));
					//Valor Umbral No Importe
					if(solvenciaPres.getString("VALORUMBRALNOIMPORTE")!=null) solTecnica.setValorUmbralNoImporte(solvenciaPres.getString("VALORUMBRALNOIMPORTE"));
					//Periodo de aplicabilidad del criterio
					if(solvenciaPres.getDate("PERIODODURACION")!=null){
						Date periodo = solvenciaPres.getDate("PERIODODURACION");
						Calendar calendar=Calendar.getInstance();
						calendar.setTime(periodo);
						solTecnica.setPeriodoDuracion(calendar);
					}
					//Expresión matemática
					if(solvenciaPres.getString("EXPMAT")!=null){
						String mate = solvenciaPres.getString("EXPMAT");
						String [] vmate = mate.split(" - ");
						if(vmate.length >1){
							solTecnica.setExpresEvaluarCriterioEvalucion(new Campo(vmate[0], vmate[1]));
						}
					}
					
					vSolvenciaTecnica.add(solTecnica);
				}
				/**
				 * CRITERIOS DE SOLVENCIA ECONÓMICA Y FINANCIERA
				 * **/
				if(solvenciaPres.getString("CRIT_SOLVENCIA_ECO")!=null){
					SolvenciaEconomica solEcon = new SolvenciaEconomica();
					//Criterio de solvencia
					if(solvenciaPres.getString("CRIT_SOLVENCIA_ECO")!=null) {
						String critSolv = solvenciaPres.getString("CRIT_SOLVENCIA_ECO");
						String [] vcritSolv = critSolv.split(" - ");
						if(vcritSolv.length >1){
							solEcon.setCriterioSolvencia(new Campo(vcritSolv[0], vcritSolv[1]));
						}
					}
					//Descripción
					if(solvenciaPres.getString("DESCRIPCION_ECO")!=null){
						String [] descripcion = {solvenciaPres.getString("DESCRIPCION_ECO")};
						solEcon.setDescripcion(descripcion);
					}
					//Valor Umbral Importe
					if(solvenciaPres.getString("VALORUMBRALIMPORTE_ECO")!=null) solEcon.setValorUmbralImporte(solvenciaPres.getString("VALORUMBRALIMPORTE_ECO"));
					//Valor Umbral No Importe
					if(solvenciaPres.getString("VALORUMBRALNOIMPORTE_ECO")!=null) solEcon.setValorUmbralNoImporte(solvenciaPres.getString("VALORUMBRALNOIMPORTE_ECO"));
					//Periodo de aplicabilidad del criterio
					if(solvenciaPres.getDate("PERIODODURACION_ECO")!=null){
						Date periodo = solvenciaPres.getDate("PERIODODURACION_ECO");
						Calendar calendar=Calendar.getInstance();
						calendar.setTime(periodo);
						solEcon.setPeriodoDuracion(calendar);
					}
					//Expresión matemática
					if(solvenciaPres.getString("EXPMAT_ECO")!=null){
						String mate = solvenciaPres.getString("EXPMAT_ECO");
						String [] vmate = mate.split(" - ");
						if(vmate.length >1){
							solEcon.setExpresEvaluarCriterioEvalucion(new Campo(vmate[0], vmate[1]));
						}
					}
					
					vSolvenciaEconomica.add(solEcon);
				}
			}
			SolvenciaTecnica [] solvenciaTecn = null;
			if(vSolvenciaTecnica.size()>0){
				solvenciaTecn= new SolvenciaTecnica[vSolvenciaTecnica.size()];
				vSolvenciaTecnica.toArray(solvenciaTecn);
			}
			solvencia.setSolvenciaTecn(solvenciaTecn);
			
			SolvenciaEconomica [] solvenciaEconomica = null;
			if(vSolvenciaEconomica.size()>0){
				solvenciaEconomica = new SolvenciaEconomica[vSolvenciaEconomica.size()];
				vSolvenciaEconomica.toArray(solvenciaEconomica);
			}
			solvencia.setSolvenciaEconomica(solvenciaEconomica);
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		
		return solvencia;
	}
	
	
	@SuppressWarnings("unchecked")
	public static SobreElectronico[] getSobreElec(IRuleContext rulectx) throws ISPACRuleException {
		SobreElectronico[] docPresentar;
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection docPresCollection = entitiesAPI.getEntities("CONTRATACION_DOC_PRESENTAR", rulectx.getNumExp());
			Iterator <IItem> docPresIterator = docPresCollection.iterator();
			docPresentar = new SobreElectronico[docPresCollection.toList().size()];
			int i = 0;
			while(docPresIterator.hasNext()){
				IItem docPres = docPresIterator.next();
				docPresentar[i] = new SobreElectronico();
				if(docPres.getString("NOMBRESOBRE")!=null) docPresentar[i].setIdSobre(docPres.getString("NOMBRESOBRE"));
				//descripcion
				if(docPres.getString("DESCRIPCION")!=null){
					String [] descripcion = {docPres.getString("DESCRIPCION")};
					docPresentar[i].setDescripcion(descripcion);
				}
				//Tipo de documento
				if(docPres.getString("TIPO_DOC")!=null) {
					String tipoOferta = docPres.getString("TIPO_DOC");
					String [] vtipoOferta = tipoOferta.split(" - ");
					if(vtipoOferta.length >1){
						docPresentar[i].setCodOferta(new Campo(vtipoOferta[0], vtipoOferta[1]));
					}
				}
				//Listado de documentos.
				int idDoc = docPres.getInt("ID");
				String strQuery="SELECT VALUE FROM CONTRATACION_DOC_PRESENTAR_S WHERE REG_ID = "+idDoc+" AND FIELD='LIST_DOC'";
		        ResultSet datos = cct.getConnection().executeQuery(strQuery).getResultSet();
		        if(datos!=null)
		      	{
		        	Vector <DatoDocumento> listDoc = new Vector<DatoDocumento>();
		        	while(datos.next()){
		        		String nombreDoc = "";
		          		if (datos.getString("VALUE")!=null) nombreDoc = datos.getString("VALUE");
		          		DatoDocumento documento = new DatoDocumento();
		          		documento.setNombre(nombreDoc);
		          		listDoc.add(documento);
		          	}
		        	
		        	DatoDocumento [] array = null;
		        	if(listDoc.size()>0){
		        		array = new DatoDocumento[listDoc.size()];
			        	listDoc.toArray(array);
		        	}
		        	docPresentar[i].setDoc(array);
		      	}
		        
		        //Evento apertura
		        EventoAperturaBean eventoApertura = new EventoAperturaBean();
		        if(docPres.getString("CALLE")!=null) eventoApertura.setCalle(docPres.getString("CALLE"));
		        if(docPres.getString("LUGAR")!=null) eventoApertura.setLugar(docPres.getString("LUGAR"));
		        if(docPres.getString("PROVINCIA")!=null) eventoApertura.setPoblacion(docPres.getString("PROVINCIA"));
		        if(docPres.getString("LOCALIDAD")!=null) eventoApertura.setLocalidad(docPres.getString("LOCALIDAD"));
		        if(docPres.getString("CP")!=null) eventoApertura.setCp(docPres.getString("CP"));
		        eventoApertura.setPais("SPAIN");
		        eventoApertura.setIdPais("ES");
		        eventoApertura.setDescripcion("Apertura de sobres");
		        if(docPres.getDate("F_APERTURA")!=null && docPres.getString("HORA_APERT")!=null){
		        	Calendar calendar = Calendar.getInstance();
		        	calendar.setTime(docPres.getDate("F_APERTURA"));
		        	//hora
		        	String hora = docPres.getString("HORA_APERT");
		        	logger.warn("hora "+hora);
		        	String [] vHora = hora.split(":"); 
		        	if(vHora.length>0){
		        		calendar.set(Calendar.HOUR, Integer.parseInt(vHora[0]));
		        		calendar.set(Calendar.MINUTE, Integer.parseInt(vHora[1]));
		        	}
		        	
		        	logger.warn("calendar "+calendar.toString());
		        	
		        	eventoApertura.setFechaApertura(calendar);
		        }
				
		        docPresentar[i].setEventoApertura(eventoApertura);
				i++;
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return docPresentar;
	}
	
	@SuppressWarnings("unchecked")
	public static DatosEmpresa getDatosEmpresa(IRuleContext rulectx, String numexpExp) throws ISPACRuleException {
		DatosEmpresa datosEmpresa = new DatosEmpresa();
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection datosEmpresaCollection = entitiesAPI.getEntities("CONTRATACION_INF_EMPRESA", numexpExp);
			Iterator <IItem> datosEmpresaIterator = datosEmpresaCollection.iterator();
			if(datosEmpresaIterator.hasNext()){
				IItem datEmp = datosEmpresaIterator.next();
				
				//Clasificacion
				int idCodiceEmpresa = datEmp.getInt("ID");
				String strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_INF_EMPRESA_S WHERE REG_ID = "+idCodiceEmpresa+"";
		        ResultSet datos = cct.getConnection().executeQuery(strQuery).getResultSet();
		        String field = "";
		        Vector<String[]> valores = new Vector<String[]> ();
		        String valorCPV;
		        if(datos!=null)
		      	{
		        	while(datos.next()){
		        		String [] vDatos = new String [2];
		          		if (datos.getString("FIELD")!=null) field = datos.getString("FIELD"); else field="";
		          		if (datos.getString("VALUE")!=null) valorCPV = datos.getString("VALUE"); else valorCPV="";
		          		vDatos[0] = field;
		          		vDatos[1] = valorCPV;
		          		valores.add(vDatos);
		          	}
		        	if(valores.size()>0){
		        		int iClas = 0;
		        		int iTipDecl = 0;
		        		Vector <Campo> valorClasificacion = new Vector<Campo>();
		        		Vector <RequisitfiDeclaraciones> valortipoDecla =  new Vector<RequisitfiDeclaraciones>();
		        		for(int i=0; i<valores.size(); i++){
			        		String[] aClasTipDecl = valores.get(i);
			        		if(aClasTipDecl!=null){
			        			if(aClasTipDecl[0].equals("CLAS_EMP")){
			        				String [] vClas = aClasTipDecl[1].split(" - ");
									if(vClas.length >0){
										Campo campoClasif = new Campo(vClas[0], vClas[1]);
										valorClasificacion.add(campoClasif);
										iClas ++;
									}
			        			}
			        			else{
			        				//"TIP_DECLARACION"
			        				String [] vTipoDecl = aClasTipDecl[1].split(" - ");
									if(vTipoDecl.length >0){
										RequisitfiDeclaraciones requ = new RequisitfiDeclaraciones();
										Campo campoTipDec = new Campo(vTipoDecl[0], vTipoDecl[1]);
										requ.setRequisitEspecifico(campoTipDec);
										valortipoDecla.add(requ);
										iTipDecl ++;
									}
			        			}
				        			
			        		}
			        		
			        	}
		        		if(iClas>0){
		        			Campo [] campoArray = null;
		        			if(valorClasificacion.size()>0){
		        				campoArray = new Campo[valorClasificacion.size()];
			        			valorClasificacion.toArray(campoArray);
		        			}
		        			datosEmpresa.setClasificacion(campoArray);
		        		}
		        		if(iTipDecl>0){
		        			RequisitfiDeclaraciones [] requisitfiDeclaraciones = null;
		        			if(valortipoDecla.size()>0){
		        				requisitfiDeclaraciones = new RequisitfiDeclaraciones[valortipoDecla.size()];
			        			valortipoDecla.toArray(requisitfiDeclaraciones);
		        			}
		        			datosEmpresa.setTipoDeclaracion(requisitfiDeclaraciones);
		        		}
		        	}
		        	
		      	}
		        /**
		         * INFORMACIÓN GENERAL A CUMPLIR
		         * **/
		        CondicionesLicitadores condLicit = new CondicionesLicitadores();
		        //Forma legal
		        Vector <String> forLegal = new Vector<String>();
		        if(datEmp.getString("LEGALFORM")!=null){
		        	forLegal.add(datEmp.getString("LEGALFORM"));
		        	String [] campoArray = null;
		        	if(forLegal.size()>0){
		        		campoArray = new String[forLegal.size()];
			        	forLegal.toArray(campoArray);
		        	}
		        	condLicit.setFormaLegal(campoArray);
		        }
		        //Situación personal
		        Vector<String> situPerso = new Vector<String>();
		        if(datEmp.getString("PERSONALSITUATION")!=null){
		        	situPerso.add(datEmp.getString("PERSONALSITUATION"));
		        	String [] campoArray = null;
		        	if(situPerso.size()>0){
		        		campoArray = new String[situPerso.size()];
			        	situPerso.toArray(campoArray);
		        	}
		        	condLicit.setSituacionPersonal(campoArray);
		        }
		        //Años de experiencia
		        if(datEmp.getString("OPERATINGYEARSQUANTITY")!=null)condLicit.setAnosExperiencia(datEmp.getString("OPERATINGYEARSQUANTITY"));
		        //Número mínimo de empleados
		        if(datEmp.getString("EMPLOYEEQUANTITY")!=null)condLicit.setNumMinEmpleados(datEmp.getString("EMPLOYEEQUANTITY"));
		        //Descripción
		        Vector<String> descripcion = new Vector<String>();
		        if(datEmp.getString("DESCRIPTION")!=null){
		        	descripcion.add(datEmp.getString("DESCRIPTION"));
		        	String [] campoArray = null;
		        	if(descripcion.size()>0){
		        		campoArray = new String[descripcion.size()];
			        	descripcion.toArray(campoArray);
		        	}
		        	condLicit.setDescripcion(campoArray);
		        }
		        datosEmpresa.setCondLicit(condLicit);
		        
		        /**
		         * REQUISITOS ESPECÍFICOS Y DECLARACIONES REQUERIDAS 
		         * **/
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		
		return datosEmpresa;
	}
	
	@SuppressWarnings("unchecked")
	public static BOP getBOP(IRuleContext rulectx) throws ISPACRuleException {
		BOP bop = null;
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Publicaciones Oficiales
			IItemCollection solBopCollection = entitiesAPI.getEntities("BOP_SOLICITUD", rulectx.getNumExp());
			Iterator <IItem> solBopIterator = solBopCollection.iterator();
			if(solBopIterator.hasNext()){
				bop = new BOP();
				IItem solBop = (IItem) solBopIterator.next();
				Date fecha_publicacion=new Date();
				if(solBop.getDate("FECHA_PUBLICACION")!=null)fecha_publicacion=solBop.getDate("FECHA_PUBLICACION");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(fecha_publicacion);
				bop.setFechaPublicacion(calendar);
				if(solBop.getString("NUM_ANUNCIO_BOP")!=null)bop.setNumAnuncio(solBop.getString("NUM_ANUNCIO_BOP"));
				//TODO acceder a la ruta del BOP
				bop.setUrlPublicacion("http://bop.sede.dipucr.es/");
			}

			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return bop;
	}
	
	public static DiariosFechaOficiales getFechaDiariosOficiales(IRuleContext rulectx, String numexp) throws ISPACRuleException {
		DiariosFechaOficiales diariosOficiales = new DiariosFechaOficiales();
		try{
			
			Iterator<IItem> itColl = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_PUBLIC_PLACE", "NUMEXP='"+numexp+"'");
			while(itColl.hasNext()){
				IItem place = itColl.next();
				if(place.getDate("ANUNCIO_LICITACION")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("ANUNCIO_LICITACION"));
			    	diariosOficiales.setAnunLicitacionPerfilContratante(calendar);
			    }
				if(place.getDate("ANUNCIO_FORMALIZACION")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("ANUNCIO_FORMALIZACION"));
			    	diariosOficiales.setAnunFormalizacionPerfilContratante(calendar);
			    }
				if(place.getDate("ANUNCIO_ADJUDICACION")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("ANUNCIO_ADJUDICACION"));
			    	diariosOficiales.setAnunAdjudicacionPerfilContratante(calendar);
			    }
			}
			itColl = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_DOUE", "NUMEXP='"+numexp+"'");
			while(itColl.hasNext()){
				IItem place = itColl.next();
				if(place.getDate("FECHA_PUBLICACION_ANUN_LIC_DOU")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_LIC_DOU"));
			    	diariosOficiales.setAnuncioLicitacionDOUE(calendar);
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_LIC_BOE")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_LIC_BOE"));
			    	diariosOficiales.setAnuncioLicitacionBOE(calendar);
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_FOR_DOU")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_FOR_DOU"));
			    	diariosOficiales.setAnuncioFormalizacionDOUE(calendar);
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_FOR_BOE")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_FOR_BOE"));
			    	diariosOficiales.setAnuncioFormalizacionBOE(calendar);
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_DOU")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_DOU"));
			    	diariosOficiales.setAnuncioAdjudicacionDOUE(calendar);
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_BO")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_BO"));
			    	diariosOficiales.setAnuncioAdjudicacionBOE(calendar);
			    }
			}
			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
			
		return diariosOficiales;
	}
			
	
	@SuppressWarnings("unchecked")
	public static DiariosOficiales getDiariosOficiales(IRuleContext rulectx, String nombrePublicacion) throws ISPACRuleException {
		DiariosOficiales diariosOficiales = new DiariosOficiales();
		
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Publicaciones Oficiales
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection collectionDoue = entitiesAPI.queryEntities("CONTRATACION_DOUE", strQuery);
			Iterator<IItem> itDoue = collectionDoue.iterator();
			
			if (itDoue.hasNext()) {				
				IItem iDiariosOficiales = (IItem) itDoue.next();
				if(iDiariosOficiales!=null){
					/**
					 * 
					 * Explicación
					 * https://contrataciondelestado.es/b2b/DGPE_PLACE_PublicacionB2B_DocumentoTecnico.v2.4.pdf
					 * Página 35 punto 3.2.1.7.2
					 * Permitirá publicarlo si no se ha enviado previamente el anuncio de licitación el DOUE
					 * 
					 * **/
					//Compruebo que no se haya mandado el anuncio de licitacion al DOUE si lo que se quiere publicar
					//es el resultado de la licitación
					String nombreColumnaDOUE = "";
					if(nombrePublicacion.equals("AnuncioLicitacionRule")){
						nombreColumnaDOUE = "PUBLICADOANUNCIOLICITACION";
					}
					if(nombrePublicacion.equals("AnuncioResultadoLicitacionFormalizacion")){
						nombreColumnaDOUE = "PUBLICADOANUNCIOFORM_DOUE";
					}
					if(nombrePublicacion.equals("AnuncioResultadoLicitacionAdjudicacion")){
						nombreColumnaDOUE = "PUBLICADOANUNCIOADJ_DOUE";
					}
					
					
					
					//if(nombrePublicacion.equals("AnuncioResultadoLicitacion") && codigoAdjudicacion.contains("Adjudicado")){
					if(!StringUtils.isEmpty(nombreColumnaDOUE)){
						if(iDiariosOficiales.getString(nombreColumnaDOUE) != null){
							DOUE doue = new DOUE();
							if(iDiariosOficiales.getString(nombreColumnaDOUE).equals("NO")){
								if(iDiariosOficiales.getString("PUB_ANUNCIO").equals("SI")){
									doue.setPublicarDOUE(true);
								}
								else{
									doue.setPublicarDOUE(false);
								}
								
							}
							else{
								doue.setPublicarDOUE(false);
							}
							diariosOficiales.setDoue(doue);
						}
					}				
						
					
					/**
					 * 
					 * Explicación
					 * https://contrataciondelestado.es/b2b/DGPE_PLACE_PublicacionB2B_DocumentoTecnico.v2.4.pdf
					 * Página 35 punto 3.2.1.7.2
					 * Permitirá publicarlo si no se ha enviado previamente el anuncio de licitación el DOUE
					 * 
					 * **/
					//Compruebo que no se haya mandado el anuncio de licitacion al DOUE si lo que se quiere publicar
					//es el resultado de la licitación
					String nombreColumnaBOE = "";
					if(nombrePublicacion.equals("AnuncioLicitacionRule")){
						nombreColumnaBOE = "PUBLICADOANUNCIOLICITACIONBOE";
					}
					if(nombrePublicacion.equals("AnuncioResultadoLicitacionFormalizacion")){
						nombreColumnaBOE = "PUBLICADOANUNCIOFORM_BOE";
					}
					if(nombrePublicacion.equals("AnuncioResultadoLicitacionAdjudicacion")){
						nombreColumnaDOUE = "PUBLICADOANUNCIOADJ_BOE";
					}
					if(!StringUtils.isEmpty(nombreColumnaBOE)){
						if(iDiariosOficiales.getString(nombreColumnaBOE) != null){
							BOE boe = new BOE();
							if(iDiariosOficiales.getString(nombreColumnaBOE).equals("NO")){
								if(iDiariosOficiales.getString("PUB_ANUNCIO_BOP").equals("SI")){
									boe.setPublicarBOE(true);
								}
								else{
									boe.setPublicarBOE(false);
								}
							}
							else{
								boe.setPublicarBOE(false);
							}
							diariosOficiales.setBoe(boe);
						}
					}
				}
				if(diariosOficiales.getBoe()!=null && diariosOficiales.getDoue()!=null){
					logger.warn("BOE: "+diariosOficiales.getBoe().isPublicarBOE());
					logger.warn("BOE: "+diariosOficiales.getDoue().isPublicarDOUE());
				}
				
			}
			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
			
		return diariosOficiales;
	}


	@SuppressWarnings("unchecked")
	public static DatosLicitacion getDatosLicitacion(IRuleContext rulectx) throws ISPACRuleException {
		DatosLicitacion datosLicitacion = new DatosLicitacion();
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Presentacion Oferta
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection collectionDatLic = entitiesAPI.queryEntities("CONTRATACION_DATOS_LIC", strQuery);
			Iterator<IItem> itDatLic = collectionDatLic.iterator();
			if (itDatLic.hasNext()) {
				IItem datosLic = (IItem) itDatLic.next();
				String presOferta = "";
				if(datosLic.getString("PRESENT_OFERTA")!=null)presOferta=datosLic.getString("PRESENT_OFERTA");
				String [] vspresOfert = presOferta.split(" - ");
				if(vspresOfert.length >1){
					datosLicitacion.setTipoPresentacionOferta(new Campo(vspresOfert[0], vspresOfert[1]));
				}
				
				//****Aplicacion presupuestaria
				
				int id = datosLic.getInt("ID");
	        	
	        	String consulta="SELECT VALUE FROM CONTRATACION_DATOS_LIC_S WHERE REG_ID = "+id+" AND FIELD = 'APLICAPRESUP'";
		        ResultSet datosApli = cct.getConnection().executeQuery(consulta).getResultSet();
		        String value = "";
		        if(datosApli!=null)
		      	{
		        	Vector<AplicacionPresupuestaria> vApli = new Vector<AplicacionPresupuestaria>();
		        	while(datosApli.next()){
		        		AplicacionPresupuestaria aplicPre = new AplicacionPresupuestaria();
		          		if (datosApli.getString("VALUE")!=null) value = datosApli.getString("VALUE"); else value="";
		          		String [] sAplicacion = value.split("-");
			        	if(sAplicacion.length > 0 && sAplicacion.length==3){
			        		aplicPre.setAplicPres(sAplicacion[0]);
							aplicPre.setAnualidad(sAplicacion[1]);
							aplicPre.setImporte(sAplicacion[2]);
			        	}
			        	else{
			        		rulectx.setInfoMessage("No está bien metido el dato de Aplicación Presupuestaria");
			        	}
			        	vApli.add(aplicPre);

		          	}
		        	if(vApli.size()>0){
		        		AplicacionPresupuestaria [] aAplic = new AplicacionPresupuestaria[vApli.size()];
			        	for(int j = 0; j < vApli.size(); j++){
			        		 aAplic[j] = vApli.get(j);
			        	}
			        	datosLicitacion.setAplicacionPres(aAplic);
		        	}
		        	 
		      	}
		       
				
				//****CriteriosAdjudicacion
				CriteriosAdjudicacion critAdjuficacion = new CriteriosAdjudicacion();
				//Procedimiento de adjudicación
				String proc_adj = "";
				if(datosLic.getString("PROC_ADJUDICACION")!=null) proc_adj = datosLic.getString("PROC_ADJUDICACION");
				String [] vproc_adj = proc_adj.split(" - ");
				if(vproc_adj.length >1){
					critAdjuficacion.setTipoAdjudicacion(new Campo(vproc_adj[0], vproc_adj[1]));
				}
				//Tipo Oferta:
				String tipo_oferta = "";
				if(datosLic.getString("EVA_ECO")!=null) tipo_oferta = datosLic.getString("EVA_ECO");
				String [] vtipo_oferta = tipo_oferta.split(" - ");
				if(vtipo_oferta.length >1){
					critAdjuficacion.setValoracionTipoOferta(new Campo(vtipo_oferta[0], vtipo_oferta[1]));
				}
				//Algoritmos de calculo de la ponderación
				String algoritmo = "";
				if(datosLic.getString("ALG_CALC_POND")!=null) algoritmo = datosLic.getString("ALG_CALC_POND");
				String [] valgoritmo = algoritmo.split(" - ");
				if(valgoritmo.length >1){
					critAdjuficacion.setCodigoAlgoritmo(new Campo(valgoritmo[0], valgoritmo[1]));
				}
				//Descripción condiciones de adjudicación
				Vector <String> descCondAdj = new Vector<String>();
				if(datosLic.getString("DESCRIPCION")!=null){
					descCondAdj.add(datosLic.getString("DESCRIPCION"));
					String [] campoArray = null;
					if(descCondAdj.size()>0){
						campoArray = new String[descCondAdj.size()];
						descCondAdj.toArray(campoArray);
					}
					critAdjuficacion.setDescripcion(campoArray);
				}
				//Descripción Comité Técnico
				Vector <String> descComi = new Vector<String>();
				if(datosLic.getString("DESCCOMITE")!=null){
					descComi.add(datosLic.getString("DESCCOMITE"));
					String [] campoArray = null;
					if(descComi.size()>0){
						campoArray = new String[descComi.size()];
						descComi.toArray(campoArray);
					}
					critAdjuficacion.setDescripcionComiteTecnico(campoArray);
				}
				//Descripción baja temeraria
				Vector <String> descTem = new Vector<String>();
				if(datosLic.getString("DESCRIPCION_BAJA")!=null){
					descTem.add(datosLic.getString("DESCRIPCION_BAJA"));
					String [] campoArray = null;
					if(descTem.size()>0){
						campoArray = new String[descTem.size()];
						descTem.toArray(campoArray);
					}					
					critAdjuficacion.setDescripcionBajaTemeraria(campoArray);
				}
				
				//Comité de expertos
				int idLic = datosLic.getInt("ID");
				strQuery="SELECT VALUE FROM CONTRATACION_DATOS_LIC_S WHERE REG_ID = "+idLic+" AND FIELD='COMITEEXPERTOS'";
		        ResultSet datos = cct.getConnection().executeQuery(strQuery).getResultSet();
		        if(datos!=null)
		      	{
		        	Vector<PersonaComite> comite = new Vector<PersonaComite>();
		        	while(datos.next()){
		        		String nombreDoc = "";
		          		if (datos.getString("VALUE")!=null) nombreDoc = datos.getString("VALUE");
		          		PersonaComite persona = new PersonaComite();
		          		persona.setNombre(nombreDoc);
		          		comite.add(persona);
		          	}
		        	PersonaComite [] campoArray = null;
		        	if(comite.size()>0){
		        		campoArray = new PersonaComite[comite.size()];
			        	comite.toArray(campoArray);
		        	}
		        	critAdjuficacion.setPersComite(campoArray);
		      	}
		        
		        //Falta por introducir la entidad 'Criterios de Adjudicacion'
		        getCriteriosAdjudicacion(rulectx, critAdjuficacion);
				datosLicitacion.setCritAdj(critAdjuficacion);
				
				//***********Variantes
				VariantesOfertas variantes = new VariantesOfertas();
				//Variantes en las Ofertas
				if(datosLic.getString("VARIANTEOFERTA")!=null){
					if(datosLic.getString("VARIANTEOFERTA").equals("SI")){
						variantes.setVarianteOferta(true);
					}
					else{
						variantes.setVarianteOferta(false);
					}
					
				}
				//Número de variantes
				if(datosLic.getString("NUM_VAR")!=null) variantes.setNumMaxVar(datosLic.getString("NUM_VAR"));
				datosLicitacion.setVariantes(variantes);
				//Elementos aceptados
				strQuery="SELECT VALUE FROM CONTRATACION_DATOS_LIC_S WHERE REG_ID = "+idLic+" AND FIELD='ELEM_ACEP'";
		        ResultSet datosElem = cct.getConnection().executeQuery(strQuery).getResultSet();
		        if(datosElem!=null)
		      	{
		        	Vector<String> descVar = new Vector<String>();
		        	while(datosElem.next()){
		        		String nombreDoc = "";
		          		if (datosElem.getString("VALUE")!=null) nombreDoc = datosElem.getString("VALUE");
		          		descVar.add(nombreDoc);
		          	}
		        	String [] campoArray = null;
		        	if(descVar.size()>0){
		        		campoArray = new String[descVar.size()];
			        	descVar.toArray(campoArray);
		        	}
		        	variantes.setDescVariantes(campoArray);
		      	}
		        datosLicitacion.setVariantes(variantes);
		        
		        //Programa de financiacion
		        String progr_fin = "";
		        FundacionPrograma fundacionPrograma = new FundacionPrograma();
				if(datosLic.getString("PROGRAMA_FINANCIACION")!=null) progr_fin = datosLic.getString("PROGRAMA_FINANCIACION");
				String [] vprogr_fin = progr_fin.split(" - ");
				if(vprogr_fin.length >1){
					fundacionPrograma.setProgramasFinanciacionCode(new Campo(vprogr_fin[0], vprogr_fin[1]));
				}
				
				//programa
				String programa = "";
				if(datosLic.getString("PROGRAMA")!=null) programa = datosLic.getString("PROGRAMA");
				fundacionPrograma.setPrograma(programa);
				
				//Formula de revisión de precios
				String revPrecios = "";
				if(datosLic.getString("REV_PRECIOS")!=null) revPrecios = datosLic.getString("REV_PRECIOS");
				datosLicitacion.setRevisionPrecios(revPrecios);
				
				datosLicitacion.setFundacionPrograma(fundacionPrograma);
			}
			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}	
			
		return datosLicitacion;
	}
	
	@SuppressWarnings("unchecked")
	private static void getCriteriosAdjudicacion(IRuleContext rulectx, CriteriosAdjudicacion critAdjuficacion) throws ISPACRuleException {
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Presentacion Oferta
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection collectionDatLic = entitiesAPI.queryEntities("CONTRATACION_CRIT_ADJ", strQuery);
			Iterator<IItem> itDatLic = collectionDatLic.iterator();
			int i = 0;
			if(collectionDatLic.toList().size()>0){
				CriterioAdjudicacionMultCrit[] vCriterio = new CriterioAdjudicacionMultCrit[collectionDatLic.toList().size()];
				while (itDatLic.hasNext()) {
					CriterioAdjudicacionMultCrit criterio = new CriterioAdjudicacionMultCrit();
					IItem datosLic = (IItem) itDatLic.next();
					
					criterio.setId(i+"");
					
					if(datosLic.getString("CRIT_ADJ")!=null){
						String crit_adj = datosLic.getString("CRIT_ADJ");
						String [] vcrit_adj = crit_adj.split(" - ");
						if(vcrit_adj.length >1){
							criterio.setCodCritAdj(new Campo(vcrit_adj[0], vcrit_adj[1]));
						}
					}
					
					if(datosLic.getString("DESCRIPCION")!=null){
						String [] campoArray = new String[1];
						campoArray[0] = datosLic.getString("DESCRIPCION");
						criterio.setDescripcion(campoArray);
					}
					
					if(datosLic.getString("PONDERACION")!=null){
						criterio.setPonderacion(datosLic.getString("PONDERACION"));
					}
					
					if(datosLic.getString("DESC_PONDERACION")!=null){
						String [] campoArray = new String[1];
						campoArray[0] = datosLic.getString("DESC_PONDERACION");
						criterio.setDescPonderacion(campoArray);
					}
					
					if(datosLic.getString("EXP_MAT")!=null){
						String [] campoArray = new String[1];
						campoArray[0] = datosLic.getString("EXP_MAT");
						criterio.setDescpCalculoExp(campoArray);
					}
					
					if(datosLic.getString("COD_EXP")!=null){
						String crit_adj = datosLic.getString("COD_EXP");
						String [] vcrit_adj = crit_adj.split(" - ");
						if(vcrit_adj.length >1){
							criterio.setExpresionCalc(new Campo(vcrit_adj[0], vcrit_adj[1]));
						}
					}
					
					if(datosLic.getInt("CANT_MIN")>0){
						criterio.setCantMin(datosLic.getInt("CANT_MIN")+"");
					}
					
					if(datosLic.getInt("CANT_MAX")>0){
						criterio.setCantMax(datosLic.getInt("CANT_MAX")+"");
					}
					
					if(datosLic.getInt("IMP_MINIMO")>0){
						criterio.setImpMin(datosLic.getInt("IMP_MINIMO")+"");
					}
					
					if(datosLic.getInt("IMP_MAXIMO")>0){
						criterio.setImpMax(datosLic.getInt("IMP_MAXIMO")+"");
					}
					
					if(datosLic.getString("DESC_PUJA_MIN")!=null){
						String [] campoArray = new String[1];
						campoArray[0] = datosLic.getString("DESC_PUJA_MIN");
						criterio.setDescripcionPujaMinSubastaElect(campoArray);
					}
					
					vCriterio[i] = criterio;
		
					i++;
				}
				critAdjuficacion.setCritAdjudicacionMultCrit(vCriterio);
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public static Peticion getPeticion(IRuleContext rulectx) throws ISPACRuleException {
		Peticion peticion = new Peticion();
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			
			//Importe de la petición
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection itcImporte = entitiesAPI.queryEntities("CONTRATACION_PETICION", strQuery);
			Iterator<IItem> iImporte = itcImporte.iterator();			
			if(iImporte.hasNext()){
				IItem itImporte = iImporte.next();
				peticion.setPresupuestoConIva(itImporte.getString("TOTAL"));
				peticion.setPresupuestoSinIva(itImporte.getString("PRESUPUESTO"));
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return peticion;
	}
	
	@SuppressWarnings("unchecked")
	public static DatosContrato getDatosContrato (IRuleContext rulectx, String numexp) throws ISPACRuleException{
		DatosContrato datosContrato = new DatosContrato();
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			
			
			String strQuery = "WHERE NUMEXP='" + numexp + "'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", strQuery);
			Iterator<IItem> it = collection.iterator();
			if (it.hasNext()) {
				IItem iDatosContrato = (IItem) it.next();
				
				//Número de contrato
				if(iDatosContrato.getString("NCONTRATO")!=null)datosContrato.setNumContrato(iDatosContrato.getString("NCONTRATO"));
				if(iDatosContrato.getString("CONT_SUJ_REG_ARMO")!=null){
					if(iDatosContrato.getString("CONT_SUJ_REG_ARMO").equals("SI"))datosContrato.setRegulacionArmonizada(true);
					else datosContrato.setRegulacionArmonizada(false);
				}
				
				//Organo contrtacion
				if(iDatosContrato.getString("ORGANO_CONTRATACION")!=null)datosContrato.setOrganoContratacion(iDatosContrato.getString("ORGANO_CONTRATACION"));
				
				//Provincia contrato
				if(iDatosContrato.getString("PROVINCIA_CONTRATO")!=null)datosContrato.setProvinciaContrato(iDatosContrato.getString("PROVINCIA_CONTRATO"));
				
				//procNegArticulo. Artículo y apartado de la LCAP por el que se aplica procedimiento negociado
				if(iDatosContrato.getString("PROCNEGARTICULO")!=null)datosContrato.setProcNegCausa(iDatosContrato.getString("PROCNEGARTICULO"));
				
				
				//Caracteristicas
				if(iDatosContrato.getString("CARACTERISTICA_BIENES_RENDCUEN")!=null)datosContrato.setCaracteristicasBienes(iDatosContrato.getString("CARACTERISTICA_BIENES_RENDCUEN"));
				
				//Objeto del contrato
				if(iDatosContrato.getString("OBJETO_CONTRATO")!=null)datosContrato.setObjetoContrato(iDatosContrato.getString("OBJETO_CONTRATO"));
				//precio estamado del contrado
				if(iDatosContrato.getString("PRECIO_ESTIMADO_CONTRATO")!=null)datosContrato.setValorEstimadoContrato(iDatosContrato.getString("PRECIO_ESTIMADO_CONTRATO"));
				
				//Procedimiento contratación
				String tipoProcBD = "";
				if(iDatosContrato.getString("PROC_ADJ")!=null)tipoProcBD=iDatosContrato.getString("PROC_ADJ");
				String [] vsTipoProc = tipoProcBD.split(" - ");
				if(vsTipoProc.length >1){
					datosContrato.setProcedimientoContratacion(new Campo(vsTipoProc[0], vsTipoProc[1]));
				}
				
				// Tipo de contrato
				String tipoContratoBD = "";
				if(iDatosContrato.getString("TIPO_CONTRATO")!=null)tipoContratoBD=iDatosContrato.getString("TIPO_CONTRATO");
				String [] vTipoContrato = tipoContratoBD.split(" - ");
				if(vTipoContrato.length >1){
					datosContrato.setTipoContrato(new Campo(vTipoContrato[0], vTipoContrato[1]));
				}
				
				boolean criteriosMultiples = false;
				if(iDatosContrato.getString("ABIERTO_CRITERIOS_MULTIPLES")!=null){
					if(iDatosContrato.getString("ABIERTO_CRITERIOS_MULTIPLES").equals("SI")){
						criteriosMultiples = true;
					}
					datosContrato.setCriteriosMultiples(criteriosMultiples);
				}
				
				// Subtipo de contrato
				String subTipoContratoBD = "";
				if(iDatosContrato.getString("CONTRATO_SUMIN")!=null)subTipoContratoBD=iDatosContrato.getString("CONTRATO_SUMIN");
				String [] vsUBTipoContrato = subTipoContratoBD.split(" - ");
				if(vsUBTipoContrato.length >1){
					datosContrato.setSubTipoContrato(new Campo(vsUBTipoContrato[0], vsUBTipoContrato[1]));
				}
				
				//Tipo de tramitacion en un procedimiento de licitacion
				String formaTramitacion = "";
				if(iDatosContrato.getString("FORMA_TRAMITACION")!=null)formaTramitacion=iDatosContrato.getString("FORMA_TRAMITACION");
				String [] vsFormaTramitacion= formaTramitacion.split(" - ");
				if(vsFormaTramitacion.length >1){
					datosContrato.setTipoTramitacion(new Campo(vsFormaTramitacion[0], vsFormaTramitacion[1]));
				}

				//Tramitacion Gasto
				String tramitacionGasto = "";
				if(iDatosContrato.getString("TRAM_GASTO")!=null)tramitacionGasto=iDatosContrato.getString("TRAM_GASTO");
				String [] vsTramitacionGasto= tramitacionGasto.split(" - ");
				if(vsTramitacionGasto.length >1){
					datosContrato.setTramitacionGasto(new Campo(vsTramitacionGasto[0], vsTramitacionGasto[1]));
				}
				
				//CPV
				int idCodiceEmpresa = iDatosContrato.getInt("ID");
				strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_DATOS_CONTRATO_S WHERE REG_ID = "+idCodiceEmpresa+"";
		        ResultSet datos = cct.getConnection().executeQuery(strQuery).getResultSet();
		        String field = "";
		        Vector<String[]> valores = new Vector<String[]> ();
		        String valorCPV;
		        if(datos!=null)
		      	{
		        	while(datos.next()){
		        		String [] vDatos = new String [2];
		          		if (datos.getString("FIELD")!=null) field = datos.getString("FIELD"); else field="";
		          		if (datos.getString("VALUE")!=null) valorCPV = datos.getString("VALUE"); else valorCPV="";
		          		vDatos[0] = field;
		          		vDatos[1] = valorCPV;
		          		valores.add(vDatos);
		          	}
		        	if(valores.size()>0){
		        		Campo [] cpv = new Campo [valores.size()];
		        		for(int i=0; i<valores.size(); i++){
			        		String[] aCpv = valores.get(i);
			        		String [] vCpv = aCpv[1].split(" - ");
							if(vCpv.length >0){
								Campo campoCPV = new Campo(vCpv[0], vCpv[1]);
								cpv[i] = campoCPV;
							}
			        	}
		        		datosContrato.setCpv(cpv);
		        	}
		        	
		      	}
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return datosContrato;
	}
	
	@SuppressWarnings("unchecked")
	public static String[] getDatosContratoPresupuesto (IRuleContext rulectx) throws ISPACRuleException{
		String[] datosContratoPres = new String[2];
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			
			
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", strQuery);
			Iterator<IItem> it = collection.iterator();
			if (it.hasNext()) {
				IItem iDatosContrato = (IItem) it.next();
				
				//presupuesto con impuestos
				if(iDatosContrato.getString("PRESUPUESTOCONIMPUESTO")!=null)datosContratoPres[0] = iDatosContrato.getString("PRESUPUESTOCONIMPUESTO");
				//presupuesto con impuestos
				if(iDatosContrato.getString("PRESUPUESTOSINIMPUESTO")!=null)datosContratoPres[1] = iDatosContrato.getString("PRESUPUESTOSINIMPUESTO");
				
			}
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} 
		
		return datosContratoPres;
	}
	
	@SuppressWarnings("unchecked")
	public static DatosTramitacion getDatosTramitacion(IRuleContext rulectx, String numexp) throws ISPACException {
		DatosTramitacion datosTramitacion = null;
		
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			IItem datosTram = rulectx.getItem();
			IItem iDatosBBDDTramin = null;
			
			String strQuery = "WHERE NUMEXP='" + numexp + "'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_TRAMIT", strQuery);
			Iterator<IItem> it = collection.iterator();
			if (it.hasNext()) {
				iDatosBBDDTramin = (IItem) it.next();
			}
			
			

			if (datosTram!=null || iDatosBBDDTramin!=null) {
				datosTramitacion = new DatosTramitacion();
				
				//INICIO Periodo de presentacion de ofertas
				/************************************************/
			    Periodo periodoPrestacionOfertas = null;
			    Date datePeriodoPrestacionOfertas = null;
			    if(datosTram !=null && datosTram.getDate("F_INICIO_PRES_PROP")!=null){
			    	datePeriodoPrestacionOfertas = datosTram.getDate("F_INICIO_PRES_PROP");
			    }
			    else{
			    	if (iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_INICIO_PRES_PROP")!=null){
			    		datePeriodoPrestacionOfertas = iDatosBBDDTramin.getDate("F_INICIO_PRES_PROP");
			    	}
			    }
			    if (datePeriodoPrestacionOfertas!=null){
			    	if(periodoPrestacionOfertas==null){
			    		periodoPrestacionOfertas = new Periodo();
			    	}
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(datePeriodoPrestacionOfertas);
			    	periodoPrestacionOfertas.setStartDate(calendar);
			    }
			    /************************************************/
			    Date dateEndDateperiodoPrestacionOfertas = null;
			    if(datosTram !=null && datosTram.getDate("F_FIN_PRES_PROP")!=null){
			    	dateEndDateperiodoPrestacionOfertas = datosTram.getDate("F_FIN_PRES_PROP");
			    }
			    else{
			    	 if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_FIN_PRES_PROP")!=null){
			    		 dateEndDateperiodoPrestacionOfertas =iDatosBBDDTramin.getDate("F_FIN_PRES_PROP");
			    	 }
			    }
			    if(dateEndDateperiodoPrestacionOfertas!=null){
			    	if(periodoPrestacionOfertas==null){
			    		periodoPrestacionOfertas = new Periodo();
			    	}
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(dateEndDateperiodoPrestacionOfertas);
			    	periodoPrestacionOfertas.setEndDate(calendar);
			    }
			    
			    /************************************************/
			    String duracion = null;
			    if(datosTram !=null && datosTram.getString("PERIODO")!=null){
			    	duracion = datosTram.getString("PERIODO");
				}
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("PERIODO")!=null){
					    	duracion = iDatosBBDDTramin.getString("PERIODO");
					}
			    }
			    if(duracion!=null){
			    	if(periodoPrestacionOfertas==null){
			    		periodoPrestacionOfertas = new Periodo();
			    	}
			    	periodoPrestacionOfertas.setDuracion(duracion);
			    }
			    /************************************************/
			    String cadeDurationMeasure = null;
			    if(datosTram !=null && datosTram.getString("PERIODUNITCODE")!=null) {
			    	cadeDurationMeasure = datosTram.getString("PERIODUNITCODE");
				}
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("PERIODUNITCODE")!=null) {
				    	cadeDurationMeasure = iDatosBBDDTramin.getString("PERIODUNITCODE");
					}
			    }
			    if(cadeDurationMeasure!=null){
			    	if(periodoPrestacionOfertas==null){
			    		periodoPrestacionOfertas = new Periodo();
			    	}
					String critSolv = cadeDurationMeasure;
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						periodoPrestacionOfertas.setDurationMeasure(new Campo(vcritSolv[0], vcritSolv[1]));
					}
			    }
			    /************************************************/
			    String fechaPres = null;
			    if(datosTram !=null && datosTram.getString("DESCRIPCION_FECHA_PRESENTACION")!=null){
			    	fechaPres = datosTram.getString("DESCRIPCION_FECHA_PRESENTACION");
				}
			    else{
			    	 if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("DESCRIPCION_FECHA_PRESENTACION")!=null){
					    	fechaPres = iDatosBBDDTramin.getString("DESCRIPCION_FECHA_PRESENTACION");
						}
			    }
			    
			    if(fechaPres!=null){
			    	if(periodoPrestacionOfertas==null){
			    		periodoPrestacionOfertas = new Periodo();
			    	}
			    	String [] descripcion = {fechaPres};
			    	periodoPrestacionOfertas.setDescription(descripcion);
			    }
			    
			    if(periodoPrestacionOfertas!=null){
			    	datosTramitacion.setPresentacionOfertas(periodoPrestacionOfertas);
			    }
			    
			    //FIN Periodo de presentacion de ofertas
			    /************************************************/
			    Date dApertura = null;
			    if(datosTram !=null && datosTram.getDate("F_APERT_PROPOS")!=null){
			    	dApertura = datosTram.getDate("F_APERT_PROPOS");
			    }
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_APERT_PROPOS")!=null){
				    	dApertura = iDatosBBDDTramin.getDate("F_APERT_PROPOS");
				    }
			    }
			    if(dApertura!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(dApertura);
				    datosTramitacion.setFechaAperturaProposiones(calendar);
			    }
			    /************************************************/
			    String textoAcuerdo = null;
			    if(datosTram !=null && datosTram.getString("TEXTO_ACUERDO")!=null){
			    	textoAcuerdo = datosTram.getString("TEXTO_ACUERDO");
				}
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("TEXTO_ACUERDO")!=null){
				    	textoAcuerdo = iDatosBBDDTramin.getString("TEXTO_ACUERDO");
					}
			    }
			    if(textoAcuerdo!=null){
			    	datosTramitacion.setTextoAcuerdo(textoAcuerdo);
			    }
			    /************************************************/
			    String estadoExpediente = null;
			    if(datosTram !=null && datosTram.getString("ESTADOEXPEDIENTE")!=null){
			    	estadoExpediente = datosTram.getString("ESTADOEXPEDIENTE");
			    }
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("ESTADOEXPEDIENTE")!=null){
				    	estadoExpediente = iDatosBBDDTramin.getString("ESTADOEXPEDIENTE");
				    }
			    }
			    if(estadoExpediente!=null){
			    	datosTramitacion.setEstadoExpediente(estadoExpediente);
			    }
			    /************************************************/
			    Date fAprobacion = null;
			    if(datosTram !=null && datosTram.getDate("F_APROBACION_PROYECTO")!=null){
			    	fAprobacion = datosTram.getDate("F_APROBACION_PROYECTO");
				}
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_APROBACION_PROYECTO")!=null){
				    	fAprobacion = iDatosBBDDTramin.getDate("F_APROBACION_PROYECTO");
					}
			    }
			    if(fAprobacion!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(fAprobacion);
			    	datosTramitacion.setFechaAprobacionProyecto(calendar);
			    }
			    /************************************************/
			    Date fechapro = null;
			    if(datosTram !=null && datosTram.getDate("F_APRO_EXP_CONT")!=null){
			    	fechapro = datosTram.getDate("F_APRO_EXP_CONT");
				}
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_APRO_EXP_CONT")!=null){
				    	fechapro = iDatosBBDDTramin.getDate("F_APRO_EXP_CONT");
					}
			    }
			    if(fechapro!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(fechapro);
			    	datosTramitacion.setFechaAprobacionExpedienteContratacion(calendar);
			    }
			    /************************************************/
			    Date fechaBOP = null;
			    if(datosTram !=null && datosTram.getDate("F_PUB_BOP_EXP_CONT")!=null){
			    	fechaBOP = datosTram.getDate("F_PUB_BOP_EXP_CONT");
				}
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_PUB_BOP_EXP_CONT")!=null){
				    	fechaBOP = iDatosBBDDTramin.getDate("F_PUB_BOP_EXP_CONT");
					}
			    }
			    if(fechaBOP!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(fechaBOP);
			    	datosTramitacion.setFechaBOPExpCont(calendar);
			    }
			    /************************************************/
			    String periodoContrato = null;
			    String peridounitcodeContrato = null;
			    //Duracion Contrato
			    DuracionContratoBean duracionContrato = null;
			    if(datosTram !=null && datosTram.getString("PERIODO_CONTRATO")!=null && datosTram.getString("PERIODUNITCODE_CONTRATO")!=null){
			    	periodoContrato = datosTram.getString("PERIODO_CONTRATO");
			    	peridounitcodeContrato = datosTram.getString("PERIODUNITCODE_CONTRATO");
			    }
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("PERIODO_CONTRATO")!=null && iDatosBBDDTramin.getString("PERIODUNITCODE_CONTRATO")!=null){
				    	periodoContrato = iDatosBBDDTramin.getString("PERIODO_CONTRATO");
				    	peridounitcodeContrato = iDatosBBDDTramin.getString("PERIODUNITCODE_CONTRATO");
				    }
			    }
			    if(periodoContrato!=null && peridounitcodeContrato!=null){
			    	duracionContrato = new DuracionContratoBean();
			    	duracionContrato.setDuracion(periodoContrato);
					String critSolv = peridounitcodeContrato;
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						duracionContrato.setDurationMeasure(new Campo(vcritSolv[0], vcritSolv[1]));
					}
			    }
			    /************************************************/
			  //En Diputación sólo adjudicaca a un licitador.
				//Pero esta preparado para varios
				LicitadorBean[] licitadores = new LicitadorBean[1];
				LicitadorBean licitador = new LicitadorBean();
				
				String dniAdj = null;
				if(datosTram !=null && datosTram.getString("NIF_ADJUDICATARIA")!=null){
					dniAdj = datosTram.getString("NIF_ADJUDICATARIA");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("NIF_ADJUDICATARIA")!=null){
						dniAdj = iDatosBBDDTramin.getString("NIF_ADJUDICATARIA");
					}
				}				
				if(dniAdj!=null){
					licitador.setIdentificador(dniAdj);
				}
				/************************************************/
				String tipoIdentif = null;
				if(datosTram !=null && datosTram.getString("TIPOIDENTIFICADOR")!=null) {
					tipoIdentif = datosTram.getString("TIPOIDENTIFICADOR");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("TIPOIDENTIFICADOR")!=null) {
						tipoIdentif = iDatosBBDDTramin.getString("TIPOIDENTIFICADOR");
					}
				}
				if(tipoIdentif != null){
					String critSolv = tipoIdentif;
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						licitador.setTipoIdentificador(vcritSolv[0]);
					}
				}
				/************************************************/
				String motivacion = null;
				if(datosTram !=null && datosTram.getString("MOTIVACION")!=null){
					motivacion = datosTram.getString("MOTIVACION");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("MOTIVACION")!=null){
						motivacion = iDatosBBDDTramin.getString("MOTIVACION");
					}
				}
				if(motivacion!=null){
					licitador.setMotivacion(motivacion);
				}
				/************************************************/
				String empAdjCont = null;
				if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("EMP_ADJ_CONT")!=null){
					empAdjCont = iDatosBBDDTramin.getString("EMP_ADJ_CONT");
				}
				else{
					if(datosTram !=null && datosTram.getString("EMP_ADJ_CONT")!=null){
						empAdjCont = datosTram.getString("EMP_ADJ_CONT");
					}
				}
				if(empAdjCont != null){
					licitador.setNombre(empAdjCont);
				}
				/************************************************/
				Date fechaAdj = null;
				if(datosTram !=null && datosTram.getDate("FECHA_ADJUDICACION")!=null){
					fechaAdj = datosTram.getDate("FECHA_ADJUDICACION");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("FECHA_ADJUDICACION")!=null){
						fechaAdj = iDatosBBDDTramin.getDate("FECHA_ADJUDICACION");
					}
				}
				if(fechaAdj != null){
					Calendar cal=Calendar.getInstance();
					cal.setTime(fechaAdj);
					licitador.setFechaAdjudicacion(cal);
				}
				/************************************************/
				Date fechaForm = null;
				if(datosTram !=null && datosTram.getDate("FECHA_FIN_FORMALIZACION")!=null){
					fechaForm = datosTram.getDate("FECHA_FIN_FORMALIZACION");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("FECHA_FIN_FORMALIZACION")!=null){
						fechaForm = iDatosBBDDTramin.getDate("FECHA_FIN_FORMALIZACION");
					}
				}
				if(fechaForm != null){
					Calendar cal=Calendar.getInstance();
					cal.setTime(fechaForm);
					licitador.setFechaFinFormalizacion(cal);
				}
				/************************************************/
				String importeCon = null;
				if(datosTram !=null && datosTram.getString("IMP_ADJ_CONIVA")!=null){
					importeCon = datosTram.getString("IMP_ADJ_CONIVA");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("IMP_ADJ_CONIVA")!=null){
						importeCon = iDatosBBDDTramin.getString("IMP_ADJ_CONIVA");
					}
				}
				if(importeCon != null){
					licitador.setImporteConImpuestos(importeCon);
				}
				/************************************************/
				String importeSin = null;
				if(datosTram !=null && datosTram.getString("IMP_ADJ_SINIVA")!=null){
					importeSin = datosTram.getString("IMP_ADJ_SINIVA");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("IMP_ADJ_SINIVA")!=null){
						importeSin = iDatosBBDDTramin.getString("IMP_ADJ_SINIVA");
					}
				}
				if(importeSin != null){
					licitador.setImporteSinImpuestos(importeSin);
				}
				/************************************************/
				String domicilio = null;
				if(datosTram !=null && datosTram.getString("DOMICILIO_NOTIF_ADJ")!=null){
					domicilio = datosTram.getString("DOMICILIO_NOTIF_ADJ");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("DOMICILIO_NOTIF_ADJ")!=null){
						domicilio = iDatosBBDDTramin.getString("DOMICILIO_NOTIF_ADJ");
					}
				}
				if(domicilio != null){
					licitador.setCalle(domicilio);
				}
				/************************************************/
				String cp = null;
				if(datosTram !=null && datosTram.getString("CP")!=null){
					cp = datosTram.getString("CP");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("CP")!=null){
						cp = iDatosBBDDTramin.getString("CP");
					}
				}
				if(cp != null){
					licitador.setCp(cp);
				}
				/************************************************/
				String justiDesc = null;
				
				if(datosTram !=null && datosTram.getString("JUSTIFICACION_DESCRIPCION")!=null){
					justiDesc = datosTram.getString("JUSTIFICACION_DESCRIPCION");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("JUSTIFICACION_DESCRIPCION")!=null){
						justiDesc = iDatosBBDDTramin.getString("JUSTIFICACION_DESCRIPCION");
					}
				}
				if(justiDesc!=null){
					licitador.setJustificacionDescripcion(justiDesc);
				}
				/************************************************/
				String justiProceso = null;
				if(datosTram !=null && datosTram.getString("JUSTIFICACION_PROCESO")!=null) {
					justiProceso = datosTram.getString("JUSTIFICACION_PROCESO");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("JUSTIFICACION_PROCESO")!=null) {
						justiProceso = iDatosBBDDTramin.getString("JUSTIFICACION_PROCESO");
					}
				}
				if(justiProceso!=null){
					String critSolv = justiProceso;					
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						licitador.setJustificacionProceso(new Campo(vcritSolv[0], vcritSolv[1]));
					}
				}
				/************************************************/
				if(licitador!=null){
					licitadores[0] = licitador;
					datosTramitacion.setLicitador(licitadores);
				}
				
				
				FormalizacionBean formalizacion = new FormalizacionBean();
				/************************************************/
				Date fContrato = null; 
				if(datosTram !=null && datosTram.getDate("F_CONTRATO")!=null){
					fContrato = datosTram.getDate("F_CONTRATO");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_CONTRATO")!=null){
						fContrato = iDatosBBDDTramin.getDate("F_CONTRATO");
					}
				}
				if(fContrato!=null){
					Calendar cal=Calendar.getInstance();
					cal.setTime(fContrato);
					formalizacion.setFechaContrato(cal);
				}
				/************************************************/
				Date fInicioCont = null;
				if(datosTram !=null && datosTram.getDate("FECHA_INICIO_CONTRATO")!=null){
					fInicioCont = datosTram.getDate("FECHA_INICIO_CONTRATO");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("FECHA_INICIO_CONTRATO")!=null){
						fInicioCont = iDatosBBDDTramin.getDate("FECHA_INICIO_CONTRATO");
					}
				}
				if(fInicioCont!=null){
					Calendar cal=Calendar.getInstance();
					cal.setTime(fInicioCont);
					formalizacion.setPeriodoValidezInicioContrato(cal);
					if(duracionContrato==null){
						duracionContrato = new DuracionContratoBean();
					}
					duracionContrato.setFechaInicio(cal);
				}
				/************************************************/
				Date fFinContrato = null;
				if(datosTram !=null && datosTram.getDate("FECHA_FIN_CONTRATO")!=null){
					fFinContrato = datosTram.getDate("FECHA_FIN_CONTRATO");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("FECHA_FIN_CONTRATO")!=null){
						fFinContrato = iDatosBBDDTramin.getDate("FECHA_FIN_CONTRATO");
					}
				}
				if(fFinContrato!=null){
					Calendar cal=Calendar.getInstance();
					cal.setTime(fFinContrato);
					formalizacion.setPeriodoValidezFinContrato(cal);
					if(duracionContrato==null){
						duracionContrato = new DuracionContratoBean();
					}
					duracionContrato.setFechaFinal(cal);
				}
				
				
				if(duracionContrato!=null){
					datosTramitacion.setDuracionContrato(duracionContrato);
				}
				/************************************************/
				String procentSubc = null;
				
				if(datosTram !=null && datosTram.getString("PORCENTAJE_SUBCONTRATACION")!=null){
					procentSubc = datosTram.getString("PORCENTAJE_SUBCONTRATACION");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("PORCENTAJE_SUBCONTRATACION")!=null){
						procentSubc = iDatosBBDDTramin.getString("PORCENTAJE_SUBCONTRATACION");
					}
				}
				if(procentSubc != null){
					formalizacion.setPorcentajeSubcontratacion(procentSubc);
				}
				/************************************************/
				String textoAcuerdoFormalizacion = null; 
				if(datosTram !=null && datosTram.getString("TEXTO_ACUERDO_FORMALIZACION")!=null){
					textoAcuerdoFormalizacion = datosTram.getString("TEXTO_ACUERDO_FORMALIZACION");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("TEXTO_ACUERDO_FORMALIZACION")!=null){
						textoAcuerdoFormalizacion = iDatosBBDDTramin.getString("TEXTO_ACUERDO_FORMALIZACION");
					}
				}
				if(textoAcuerdoFormalizacion!=null){
					formalizacion.setTextoAcuerdoFormalizacion(textoAcuerdoFormalizacion);
				}
				
				datosTramitacion.setFormalizacion(formalizacion);
				/************************************************/
				String numOfertas = null;
				String impOfertBaja = null;
				String impOfertAlta = null;
				if(datosTram !=null && (datosTram.getString("NUMOFERTAS")!=null || datosTram.getString("IMP_OFERTA_BAJA")!=null || datosTram.getString("IMP_OFERTA_ALTA")!=null)){
					if(datosTram.getString("NUMOFERTAS")!=null) numOfertas = datosTram.getString("NUMOFERTAS");
					if(datosTram.getString("IMP_OFERTA_BAJA")!=null) impOfertBaja = datosTram.getString("IMP_OFERTA_BAJA");
					if(datosTram.getString("IMP_OFERTA_ALTA")!=null) impOfertAlta = datosTram.getString("IMP_OFERTA_ALTA");
				}
				else{
					if(iDatosBBDDTramin!=null && (iDatosBBDDTramin.getString("NUMOFERTAS")!=null || iDatosBBDDTramin.getString("IMP_OFERTA_BAJA")!=null || iDatosBBDDTramin.getString("IMP_OFERTA_ALTA")!=null)){
						if(iDatosBBDDTramin.getString("NUMOFERTAS")!=null) numOfertas = iDatosBBDDTramin.getString("NUMOFERTAS");
						if(iDatosBBDDTramin.getString("IMP_OFERTA_BAJA")!=null) impOfertBaja = iDatosBBDDTramin.getString("IMP_OFERTA_BAJA");
						if(iDatosBBDDTramin.getString("IMP_OFERTA_ALTA")!=null) impOfertAlta = iDatosBBDDTramin.getString("IMP_OFERTA_ALTA");
					}
				}
				if(numOfertas!=null || impOfertBaja!=null || impOfertAlta!=null){
					OfertasRecibidas ofertasRecibidas = new OfertasRecibidas();
					if(numOfertas!=null) ofertasRecibidas.setNumOfertasRecibidas(numOfertas);
					if(impOfertBaja!=null) ofertasRecibidas.setOfertaMasBaja(impOfertBaja);
					if(impOfertAlta!=null) ofertasRecibidas.setOfertaMasAlta(impOfertAlta);
					datosTramitacion.setOfertasRecibidas(ofertasRecibidas);
				}
				/************************************************/
				String porroga = null;
				if(datosTram !=null && datosTram.getString("PRORROGA")!=null){
					porroga = datosTram.getString("PRORROGA");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("PRORROGA")!=null){
						porroga = iDatosBBDDTramin.getString("PRORROGA");
					}
				}
				if(porroga!=null){
					if(porroga.equals("SI"))datosTramitacion.setProrroga(true);
					else datosTramitacion.setProrroga(false);
				}
				/************************************************/
				int tiempoPorroga = 0;
				if(datosTram !=null && datosTram.getInt("TIEMPOPRORROGA")>0){
					tiempoPorroga = datosTram.getInt("TIEMPOPRORROGA");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getInt("TIEMPOPRORROGA")>0){
						tiempoPorroga = iDatosBBDDTramin.getInt("TIEMPOPRORROGA");
					}
				}
				if(tiempoPorroga>0){
					datosTramitacion.setTmpProrroga(tiempoPorroga);
				}
				/************************************************/
				Date fechaPubDoceExp = null;
				if(datosTram !=null && datosTram.getDate("F_PUB_DOCE_EXP_CONT")!=null){
					fechaPubDoceExp = datosTram.getDate("F_PUB_DOCE_EXP_CONT");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_PUB_DOCE_EXP_CONT")!=null){
						fechaPubDoceExp = iDatosBBDDTramin.getDate("F_PUB_DOCE_EXP_CONT");
					}
				}
				if(fechaPubDoceExp!=null){
					Calendar cal=Calendar.getInstance();
					cal.setTime(fechaPubDoceExp);
					datosTramitacion.setFechaBOPFormalizacion(cal);
				}
				/************************************************/
				String invitaciones = null;
				if(datosTram !=null && datosTram.getString("INVITACIONES_LICITAR")!=null){
					invitaciones = datosTram.getString("INVITACIONES_LICITAR");
				}
				if(invitaciones!=null){
					datosTramitacion.setInvitacioneLicitar(invitaciones);
				}
			}
		}catch(ISPACRuleException e){
			logger.error("Error. " + e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error. " + e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}

		return datosTramitacion;
	}
	
	@SuppressWarnings("unchecked")
	public static void imprimeResultadoAnalisisPrevio(Resultado resultadoAnalisisPrevio, IRuleContext rulectx, String nombreDoc, String nombrePeticion) throws ISPACException {
		
		File ficheroAnalisisPrevio = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/informacion.pdf");
		
		try {
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			Object connectorSession = genDocAPI.createConnectorSession();
			 /***********************************************************************/
			
			
			// A partir del objeto File creamos el fichero
			// físicamente
			if (ficheroAnalisisPrevio.createNewFile()) {
				
				logger.warn("El fichero se ha creado correctamente");
				Document documentResultado = new Document();
				PdfCopy.getInstance(documentResultado,new FileOutputStream(ficheroAnalisisPrevio));
				documentResultado.open();
				imprimedoc(resultadoAnalisisPrevio.getPublicacion(), documentResultado, nombrePeticion, rulectx);
				
				documentResultado.add(new Phrase("\n"));
				documentResultado.add(new Phrase("****************RESULTADO VISUALIZACION******************"));
				documentResultado.add(new Phrase("\n\n"));
				documentResultado.add(new Phrase("Resultado código: "+ resultadoAnalisisPrevio.getVisualizacion().getResultCode()));
				documentResultado.add(new Phrase("\n"));
				documentResultado.add(new Phrase("Descripción del error: "+ resultadoAnalisisPrevio.getVisualizacion().getResultCodeDescription()));
				documentResultado.add(new Phrase("\n"));
				documentResultado.add(new Phrase("Detalle del error: "+ resultadoAnalisisPrevio.getVisualizacion().getViewErrorDetails()));
				documentResultado.add(new Phrase("\n"));
				documentResultado.close();
				
				//Genero el doc en SIGEM
				if (ficheroAnalisisPrevio != null) {
					IItemCollection tipDoc = entitiesAPI.queryEntities("SPAC_CT_TPDOC","WHERE NOMBRE='"+nombreDoc+"'");
					Iterator<IItem> tipDocIterator = tipDoc.iterator();
					int idTipDoc = 0;
					if (tipDocIterator.hasNext())
						idTipDoc  = ((IItem) tipDocIterator.next()).getInt("ID");
					IItem entityDocument = genDocAPI.createTaskDocument(rulectx.getTaskId(), idTipDoc);
					entityDocument.set("EXTENSION", Constants._EXTENSION_PDF);
					entityDocument.set("DESCRIPCION",nombreDoc);
					entityDocument.store(cct);
					cct.endTX(true);
					int documentIdComparece = entityDocument.getKeyInt();
					InputStream inError = new FileInputStream(ficheroAnalisisPrevio);

					genDocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), documentIdComparece, inError,
							(int) ficheroAnalisisPrevio.length(), "application/pdf", nombreDoc+" "+nombrePeticion);
					
					inError.close();
					
					
				}
			}
			
			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		finally{
			ficheroAnalisisPrevio.delete();
		}
		
	}
	
	public static void imprimelogsVisualizacion(VisualizationResult docResultado) {
		logger.warn("*********************************************RESULTADO VISUALIZACION**************************************************");
		logger.warn("Resultado código: "+ docResultado.getResultCode());
		logger.warn("Descripción del error: "+ docResultado.getResultCodeDescription());
		logger.warn("Detalle del error: "+ docResultado.getViewErrorDetails());
	}
	
	@SuppressWarnings("unchecked")
	public static void imprimedoc(PublicationResult result, Document documentErrorComp, String nombrePublicacion, IRuleContext rulectx) throws ISPACRuleException{
		try {
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
			String imagePath = rutaImg+"/logoCabecera.png";
			File logoURL = new File(imagePath);
			if (logoURL != null) {
				Image logo = Image.getInstance(imagePath);
				documentErrorComp.add(logo);
			}

			documentErrorComp.add(new Phrase("\n"));

			documentErrorComp.add(new Phrase("ERRORES EN LA PLATAFORMA DE CONTRATACIÓN"));
			documentErrorComp.add(new Phrase("\n"));
			documentErrorComp.add(new Phrase("\n"));
			
			documentErrorComp.add(new Phrase("*************************RESULTADO DE LA PETICIÓN************************************************"));
			documentErrorComp.add(new Phrase("\n"));
			documentErrorComp.add(new Phrase("*Número de expediente de la licitación: "+ result.getExpedientNumber()));
			documentErrorComp.add(new Phrase("\n"));
			documentErrorComp.add(new Phrase("*Código de resultado de la publicación: "+ result.getResultCode()));
			documentErrorComp.add(new Phrase("\n"));
			documentErrorComp.add(new Phrase("*Descripción del código de Resultado: "+ result.getResultCodeDescription()));
			documentErrorComp.add(new Phrase("\n"));
			documentErrorComp.add(new Phrase("*Estado de la Licitación: " + result.getState()));
			documentErrorComp.add(new Phrase("\n"));
			documentErrorComp.add(new Phrase("*Visualización del detalle del expediente en que ha sido publicado: "+ result.getNoticeURL()));
			documentErrorComp.add(new Phrase("\n"));
			
			PublishErrorDetails errorres = result.getPublishErrorDetails();
			if(errorres!=null){
				Mensaje[] smsError = errorres.getMessage();
				for(int i=0; i < smsError.length; i++){
					documentErrorComp.add(new Phrase("**********ERROR "+i+"***********"));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("** "+smsError[i].getText()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("* "+smsError[i].getContext()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("* "+smsError[i].getLocation()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("* "+smsError[i].getRuta()));
					documentErrorComp.add(new Phrase("\n"));
				}
			}
			
	
			documentErrorComp.add(new Phrase("--------PUBLICACIONES---------"));
			documentErrorComp.add(new Phrase("\n"));
			documentErrorComp.add(new Phrase("*Resultado de Publicaciones en Diarios Oficiales."));
			documentErrorComp.add(new Phrase("\n"));
			OfficialPublicationResults resPublicaciones = result.getOfficialPublicationResults();
			if (resPublicaciones != null) {
				OfficialPublicationResult[] listpubliOficiales = resPublicaciones.getOfficialPublicationResult();
				for (int i=0; i< listpubliOficiales.length; i++) {
					documentErrorComp.add(new Phrase("**********ERROR "+i+"***********"));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("Diario Oficial donde se ha detectado el error: "+ listpubliOficiales[i].getPublishAgency()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("Codigo del error: "+ listpubliOficiales[i].getReturnCode()));
					documentErrorComp.add(new Phrase("\n"));
					
					//Compruebo que se haya publicado el Anuncio de Licitación en el DOUE, para insertarlo en la tabla 'CONTRATACION_DOUE'
					if(nombrePublicacion.equals("Anuncio de Licitación")){
						if(listpubliOficiales[i].getPublishAgency().contains("DOUE")){
							if(listpubliOficiales[i].getReturnCode().contains("OK")){
								//Actualizar el estado de la publicación
								String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
								IItemCollection collectionDoue = entitiesAPI.queryEntities("CONTRATACION_DOUE", strQuery);
								Iterator<IItem> itDoue = collectionDoue.iterator();								
								if (itDoue.hasNext()) {									
									IItem iDoue = (IItem) itDoue.next();
									iDoue.set("PUBLICADOANUNCIOLICITACION", "SI");
									iDoue.store(cct);
								}
							}
						}
					}
					
					documentErrorComp.add(new Phrase("Fecha de Publicacion: "+ listpubliOficiales[i].getPublishDate()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("URL de publicacion: "+ listpubliOficiales[i].getPublishURL()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("URL que envía el BOE para completar la publicación en dicho diario: "+ listpubliOficiales[i].getBoeConfirmationURL()));
					documentErrorComp.add(new Phrase("\n"));
				}
	
			}
	
			documentErrorComp.add(new Phrase("*Detalle Errores de Publicacion"));
			documentErrorComp.add(new Phrase("\n"));
			PublishErrorDetails publishErrorDetails = result.getPublishErrorDetails();
			if (publishErrorDetails != null) {
				Mensaje [] listMessage = publishErrorDetails.getMessage();
				for (int i = 0; i < listMessage.length; i++) {
					documentErrorComp.add(new Phrase("**********ERROR "+i+"***********"));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("Id error: " + listMessage[i].getId()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("Explicacion error: " + listMessage[i].getText()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("Localicación del ERROR: "+ listMessage[i].getLocation()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("Validación x la que se produce el error: "+ listMessage[i].getContext()));
					documentErrorComp.add(new Phrase("\n"));
					documentErrorComp.add(new Phrase("Xpath que referencia al componente donde se ha producido el error: "+ listMessage[i].getRuta()));
					documentErrorComp.add(new Phrase("\n"));
				}
			}
			documentErrorComp.add(new Phrase("* FIN Detalle Errores de Publicacion"));
			documentErrorComp.add(new Phrase("\n"));
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}
	
	public static void imprimelogs(PublicationResult result) {
		logger.warn("*************************RESULTADO DE LA PETICIÓN************************************************");
		logger.warn("*Número de expediente de la licitación: "+ result.getExpedientNumber());
		logger.warn("*Código de resultado de la publicación: "+ result.getResultCode());
		logger.warn("*Descripción del código de Resultado: "+ result.getResultCodeDescription());
		logger.warn("*Estado de la Licitación: " + result.getState());
		logger.warn("*Visualización del detalle del expediente en que ha sido publicado: "+ result.getNoticeURL());
		
		PublishErrorDetails errorres = result.getPublishErrorDetails();
		if(errorres!=null){
			Mensaje[] smsError = errorres.getMessage();
			for(int i=0; i < smsError.length; i++){
				logger.warn("**********ERROR***********");
				logger.warn("* "+smsError[i].getContext());
				logger.warn("* "+smsError[i].getText());
				logger.warn("* "+smsError[i].getLocation());
				logger.warn("* "+smsError[i].getRuta());
			}
		}
		

		logger.warn("--------PUBLICACIONES---------");
		logger.warn("*Resultado de Publicaciones en Diarios Oficiales.");
		OfficialPublicationResults resPublicaciones = result.getOfficialPublicationResults();
		if (resPublicaciones != null) {
			OfficialPublicationResult[] listpubliOficiales = resPublicaciones.getOfficialPublicationResult();
			for (int i=0; i< listpubliOficiales.length; i++) {

				logger.warn("Diario Oficial donde se ha detectado el error: "
						+ listpubliOficiales[i].getPublishAgency());
				logger.warn("Codigo del error: "
						+ listpubliOficiales[i].getReturnCode());
				logger.warn("Fecha de Publicacion: "
						+ listpubliOficiales[i].getPublishDate());
				logger.warn("URL de publicacion: "
						+ listpubliOficiales[i].getPublishURL());
				logger.warn("URL que envía el BOE para completar la publicación en dicho diario: "
						+ listpubliOficiales[i].getBoeConfirmationURL());
			}

		}

		logger.warn("*Detalle Errores de Publicacion");
		PublishErrorDetails publishErrorDetails = result.getPublishErrorDetails();
		if (publishErrorDetails != null) {
			Mensaje [] listMessage = publishErrorDetails.getMessage();
			for (int i = 0; i < listMessage.length; i++) {
				logger.warn("Id error: " + listMessage[i].getId());
				logger.warn("Explicacion error: " + listMessage[i].getText());
				logger.warn("Localicación del ERROR: "
						+ listMessage[i].getLocation());
				logger.warn("Validación x la que se produce el error: "
						+ listMessage[i].getContext());
				logger.warn("Xpath que referencia al componente donde se ha producido el error: "
						+ listMessage[i].getRuta());
			}
		}
		logger.warn("* FIN Detalle Errores de Publicacion");
		
	}


	@SuppressWarnings("unchecked")
	public static void errorPeticion(PublicationResult result, IRuleContext rulectx, String nombrePeticion) throws ISPACRuleException {
		
		File ficheroError = null;
		try {
			/**
			 * ALMACENAR LA INFORMACIÓN EN UN DOCUMENTO CON LOS ERRORES.
			 * **/
			ficheroError = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance()
					.newFileName(".pdf"));
			
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			Object connectorSession = genDocAPI.createConnectorSession();
			// --------------------------------------------------------------------
						
			
			// A partir del objeto File creamos el fichero
			// físicamente
			if (ficheroError.createNewFile()) {
				
				logger.warn("El fichero se ha creado correctamente");
				Document documentErrorComp = new Document();
				PdfCopy.getInstance(documentErrorComp,new FileOutputStream(ficheroError));
				documentErrorComp.open();
				
				
				
				
				DipucrFuncionesComunes.imprimelogs(result);
				
				DipucrFuncionesComunes.imprimedoc(result, documentErrorComp, nombrePeticion, rulectx);
				
				documentErrorComp.close();
				
				if (ficheroError != null) {
					IItemCollection tipDoc = entitiesAPI.queryEntities("SPAC_CT_TPDOC","WHERE NOMBRE='Error'");
					Iterator<IItem> tipDocIterator = tipDoc.iterator();
					int idTipDoc = 0;
					if (tipDocIterator.hasNext())
						idTipDoc  = ((IItem) tipDocIterator.next()).getInt("ID");
					IItem entityDocument = genDocAPI.createTaskDocument(rulectx.getTaskId(), idTipDoc);
					entityDocument.set("EXTENSION", Constants._EXTENSION_PDF);
					entityDocument.set("DESCRIPCION","Error - "+nombrePeticion);
					entityDocument.store(cct);
					cct.endTX(true);
					int documentIdComparece = entityDocument.getKeyInt();
					InputStream inError = new FileInputStream(ficheroError);

					genDocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), documentIdComparece, inError,
							(int) ficheroError.length(), "application/pdf", "Error - "+nombrePeticion);
					
					inError.close();
				}

			} else{
				logger.warn("No ha podido ser creado el fichero");
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		finally{
			if(ficheroError!=null){
				ficheroError.delete();
			}
		}
		
	}


	@SuppressWarnings("unchecked")
	public static void crearDocPeticionOK_PLACE(Resultado resultadoAnalisisPrevio, IRuleContext rulectx, String nombreDoc) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			Object connectorSession = genDocAPI.createConnectorSession();
			// --------------------------------------------------------------------
				
		
			VisualizationResult docResultado =  resultadoAnalisisPrevio.getVisualizacion();
			if(docResultado!=null){
				
				if (docResultado.getResultCode().equals("OK")) {
					
					DipucrFuncionesComunes.imprimelogsVisualizacion(docResultado);
					
					EmbeddedDocumentBinaryObjectType documento = docResultado.getDocument();
	
					// Anuncio de Informacion Previa
					String strQuery = "WHERE NOMBRE = '" + nombreDoc+ "'";
					IItemCollection collectionTPDOC = entitiesAPI.queryEntities("SPAC_CT_TPDOC",strQuery);
					Iterator<IItem> itTPDoc = collectionTPDOC.iterator();
					int tpdoc = 0;
					if (itTPDoc.hasNext()) {
						IItem tpd = (IItem) itTPDoc.next();
						tpdoc = tpd.getInt("ID");
					}
					int taskId = rulectx.getTaskId();
					IItem newdoc = genDocAPI.createTaskDocument(
							taskId, tpdoc);
	
					String rutaFileName = FileTemporaryManager
							.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance()
									.newFileName(".pdf");
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(rutaFileName);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
						throw new ISPACRuleException("Error. "+e.getMessage(),e);
					}
					fos.write(documento.getValue());
					fos.close();
					fos.flush();
	
					File file = new File(rutaFileName);
	
					FileInputStream in = new FileInputStream(file);
					int docId = newdoc.getInt("ID");
					IItem entityDoc = genDocAPI.attachTaskInputStream(connectorSession, taskId, docId, in, (int) file.length(), documento.getMimeCode(), nombreDoc);
					entityDoc.set("EXTENSION", "pdf");
					entityDoc.set("FFIRMA", new Date());
					entityDoc.store(cct);
					file.delete();
					
					DipucrFuncionesComunes.imprimeResultadoAnalisisPrevio(resultadoAnalisisPrevio,rulectx,
							"Información de Plataforma Contratación", nombreDoc);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}  catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void cargaAnuncioFirmado(IRuleContext rulectx, String urlAnuncio, String nombreDoc, Resultado resultadoAnuncio) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			Object connectorSession = genDocAPI.createConnectorSession();
			// --------------------------------------------------------------------
			
			//leo url 
			logger.warn("URL");
			logger.warn(urlAnuncio);
			String metaDocPLACE = "";
			URL urlmeta = new URL(urlAnuncio);
			URLConnection conexionmeta = urlmeta.openConnection();	

			conexionmeta.connect();
			urlmeta.openStream();
			
			InputStream is = conexionmeta.getInputStream();
	         BufferedReader br = new BufferedReader(new InputStreamReader(is));
	         char[] buffer = new char[1000];
	         int leido;
	         String contenidoURL = "";
	         while ((leido = br.read(buffer)) > 0) {
	        	 contenidoURL = new String(buffer, 0, leido);
	         }
	         logger.warn("CONTENIDO");
	         logger.warn(contenidoURL);
	         
	         String urlDocFinal = contenidoURL;
	         
	        org.jsoup.nodes.Document doc = Jsoup.parse(contenidoURL);
	        if(doc.getElementsByTag("meta").size()>0){
	        	metaDocPLACE = doc.getElementsByTag("meta").get(0).attr("content");
				//leo url 
				String direccionPLACE = "https://contrataciondelestado.es";
				String ruta = "";
				String[] vDirePLACE = metaDocPLACE.split("url=");
				if(vDirePLACE!=null && vDirePLACE.length>0){
					ruta = vDirePLACE[1];
					//Quitar las comillas que contiene la url
					ruta = ruta.substring(1, ruta.length()-1);
				}
				
				urlDocFinal = direccionPLACE+ruta;
	        }
	        
			URL url = new URL(urlDocFinal);
			URLConnection conexion = url.openConnection();	

			conexion.connect();		
			InputStream input = new BufferedInputStream(url.openStream());
			
			logger.warn(input);

			// Anuncio de Informacion Previa
			String strQuery = "WHERE NOMBRE = '" + nombreDoc+ "'";
			IItemCollection collectionTPDOC = entitiesAPI.queryEntities("SPAC_CT_TPDOC",strQuery);
			Iterator<IItem> itTPDoc = collectionTPDOC.iterator();
			int tpdoc = 0;
			if (itTPDoc.hasNext()) {
				IItem tpd = (IItem) itTPDoc.next();
				tpdoc = tpd.getInt("ID");
			}
			int taskId = rulectx.getTaskId();
			IItem newdoc = genDocAPI.createTaskDocument(taskId, tpdoc);

			String rutaFileName = FileTemporaryManager
					.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance()
							.newFileName(".pdf");
			logger.warn(rutaFileName);
			
			byte data[] = new byte[1024];

//			long total = 0;

			int count = 0;
			FileOutputStream fos = new FileOutputStream(rutaFileName);;
			while ((count = input.read(data)) != -1) {
//				total += count;
				fos.write(data, 0, count);
			}
			
			fos.close();
			fos.flush();
			

			File file = new File(rutaFileName);

			FileInputStream in = new FileInputStream(file);
			int docId = newdoc.getInt("ID");
			IItem entityDoc = genDocAPI.attachTaskInputStream(connectorSession, taskId, docId, in, (int) file.length(), "application/octet-stream", nombreDoc);
			entityDoc.set("EXTENSION", "pdf");
			entityDoc.set("FFIRMA", new Date());
			entityDoc.store(cct);
			file.delete();
			
			if(resultadoAnuncio!=null){
				PublicationResult publicationResultLicitacion = resultadoAnuncio.getPublicacion();
				
				if(publicationResultLicitacion.getResultCode().equals("OK")){					
					DipucrFuncionesComunes.imprimeResultadoAnalisisPrevio(resultadoAnuncio,rulectx,
							"Información de Plataforma Contratación", nombreDoc);
				}

				else{
					DipucrFuncionesComunes.errorPeticion(publicationResultLicitacion, rulectx, nombreDoc);
				}	
			}	
			
			input.close();
			is.close();	
			br.close();
			
		} catch (IOException e) {
			logger.error(" URL "+urlAnuncio);
			logger.error(e.getMessage(), e);
			//La url necesita contraseña
			
			if(resultadoAnuncio!=null){
				PublicationResult publicationResultLicitacion = resultadoAnuncio.getPublicacion();
				
				if(publicationResultLicitacion.getResultCode().equals("OK")){					
					DipucrFuncionesComunes.crearDocPeticionOK_PLACE(resultadoAnuncio, rulectx, nombreDoc);
				}

				else{
					DipucrFuncionesComunes.errorPeticion(publicationResultLicitacion, rulectx, nombreDoc);
				}	
			}	
			
		}  catch (ISPACException e) {
			logger.error("ERROR"+ e.getMessage());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}
	
	public static File getFile(IRuleContext rulectx, String infoPag, String nombreFichero) throws ISPACException{
		
		// API
		IGenDocAPI gendocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

		Object connectorSession = null;
		try {
			connectorSession = gendocAPI.createConnectorSession();
			File file = null;
			try{
				String extension = "pdf";
				
				//Se almacena documento
				//String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				String fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + nombreFichero +"."+ extension;
				
				OutputStream out = new FileOutputStream(fileName);
				gendocAPI.getDocument(connectorSession, infoPag, out);
								
				file = new File(fileName);
			
				return file;
			} catch (FileNotFoundException e) {
				throw new ISPACInfo("Error al intentar obtener el documento, no existe.", e);
			}
		}finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
    	}
	}

	@SuppressWarnings("unchecked")
	public static boolean setDatosTramitacion(String numexpPeticion,
			DatosTramitacion datosTramitacionInf, ClientContext cct, String numexpExpContratacion) throws ISPACRuleException {
		boolean resultado = false;
		 try{
		
			//--------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------
	   
			IItemCollection collectionTramit = entitiesAPI.getEntities("CONTRATACION_DATOS_TRAMIT", numexpPeticion);
			Iterator<IItem> itTra = collectionTramit.iterator();
			IItem datosTramitacion = null;
			
			if (itTra.hasNext()) {
				datosTramitacion = itTra.next();
				datosTramitacion.delete(cct);
			}

			datosTramitacion = entitiesAPI.createEntity("CONTRATACION_DATOS_TRAMIT", numexpPeticion);
			
			
			Date fecha = new Date();
			
			if(datosTramitacionInf.getDuracionContrato()!=null){
				DuracionContratoBean duracionContrato = datosTramitacionInf.getDuracionContrato();
				if(duracionContrato.getDuracion()!=null){
					datosTramitacion.set("PERIODO_CONTRATO", duracionContrato.getDuracion());
				}
				if(duracionContrato.getDurationMeasure()!=null){
					datosTramitacion.set("PERIODUNITCODE_CONTRATO", duracionContrato.getDurationMeasure().getId()+" - "+duracionContrato.getDurationMeasure().getValor());
				}
			}
			
			if(datosTramitacionInf.getEstadoExpediente()!=null){
				datosTramitacion.set("ESTADOEXPEDIENTE", datosTramitacionInf.getEstadoExpediente());
			}
	
			if(datosTramitacionInf.getFechaAprobacionProyecto()!=null){
				fecha = datosTramitacionInf.getFechaAprobacionProyecto().getTime();
				datosTramitacion.set("F_APROBACION_PROYECTO", fecha);
			}
			if(datosTramitacionInf.getFechaAprobacionExpedienteContratacion()!=null){
				fecha = datosTramitacionInf.getFechaAprobacionExpedienteContratacion().getTime();
				datosTramitacion.set("F_APRO_EXP_CONT", fecha);	
			}
			if(datosTramitacionInf.getFechaBOPExpCont()!=null){
				fecha = datosTramitacionInf.getFechaBOPExpCont().getTime();
				datosTramitacion.set("F_PUB_BOP_EXP_CONT", fecha);
			}
			
			//INICIO Periodo de presentacion de ofertas
		    if(datosTramitacionInf.getPresentacionOfertas()!=null){
		    	Periodo periodoPrestacionOfertas = datosTramitacionInf.getPresentacionOfertas();
		    	if(periodoPrestacionOfertas.getStartDate()!=null){
		    		datosTramitacion.set("F_INICIO_PRES_PROP", periodoPrestacionOfertas.getStartDate().getTime());
		    	}
		    	if(periodoPrestacionOfertas.getEndDate()!=null){
		    		datosTramitacion.set("F_FIN_PRES_PROP", periodoPrestacionOfertas.getEndDate().getTime());
		    	}
		    	if(periodoPrestacionOfertas.getDuracion()!=null){
		    		datosTramitacion.set("PERIODO", periodoPrestacionOfertas.getDuracion());
		    	}
		    	if(periodoPrestacionOfertas.getDurationMeasure()!=null){
		    		datosTramitacion.set("PERIODUNITCODE", periodoPrestacionOfertas.getDurationMeasure().getId()+" - "+periodoPrestacionOfertas.getDurationMeasure().getValor());
		    	}
		    	if(periodoPrestacionOfertas.getDescription()!=null){
		    		datosTramitacion.set("DESCRIPCION_FECHA_PRESENTACION", periodoPrestacionOfertas.getDescription());
		    	}
		    	
		    	
			}
		    //FIN Periodo de presentacion de ofertas
			
			
			if(datosTramitacionInf.getFechaAperturaProposiones()!=null){
				fecha = datosTramitacionInf.getFechaAperturaProposiones().getTime();
				datosTramitacion.set("F_APERT_PROPOS", fecha);
			}
			if(datosTramitacionInf.getTextoAcuerdo()!=null){
				datosTramitacion.set("TEXTO_ACUERDO", datosTramitacionInf.getTextoAcuerdo());
			}			
			
			
			for(int i = 0; i < datosTramitacionInf.getLicitador().length; i++){
				LicitadorBean licitador = datosTramitacionInf.getLicitador()[i];
				if(licitador.getIdentificador()!=null){
					datosTramitacion.set("NIF_ADJUDICATARIA", licitador.getIdentificador());
				}
				if(licitador.getTipoIdentificador()!=null){
					datosTramitacion.set("TIPOIDENTIFICADOR", licitador.getTipoIdentificador());
				}
				if(licitador.getNombre()!=null){
					datosTramitacion.set("EMP_ADJ_CONT", licitador.getNombre());
				}
				
				
				IItemCollection collectionTramitRepre = entitiesAPI.getEntities("CONTRATACION_DATOS_TRAMIT", numexpExpContratacion);
				Iterator<IItem> itTraRepre = collectionTramitRepre.iterator();
				
				if (itTraRepre.hasNext()) {
					IItem tramitacion = itTraRepre.next();
					datosTramitacion.set("REPRE_ADJUDICATARIA", tramitacion.getString("REPRE_ADJUDICATARIA"));
				}
				
				if(licitador.getFechaAdjudicacion()!=null){
					fecha = licitador.getFechaAdjudicacion().getTime();
					datosTramitacion.set("FECHA_ADJUDICACION", fecha);
				}
				if(licitador.getFechaFinFormalizacion()!=null){
					fecha = licitador.getFechaFinFormalizacion().getTime();
					datosTramitacion.set("FECHA_FIN_FORMALIZACION", fecha);
				}
				if(licitador.getImporteSinImpuestos()!=null){
					datosTramitacion.set("IMP_ADJ_SINIVA", licitador.getImporteSinImpuestos());
				}
				if(licitador.getImporteConImpuestos()!=null){
					datosTramitacion.set("IMP_ADJ_CONIVA", licitador.getImporteConImpuestos());
				}
				if(licitador.getCalle()!=null){
					datosTramitacion.set("DOMICILIO_NOTIF_ADJ", licitador.getCalle());
				}
				if(licitador.getCp()!=null){
					datosTramitacion.set("CP", licitador.getCp());
				}
				if(licitador.getMotivacion()!=null){
					datosTramitacion.set("MOTIVACION", licitador.getMotivacion());	
				}
				if(licitador.getJustificacionDescripcion()!=null){
					datosTramitacion.set("JUSTIFICACION_DESCRIPCION", licitador.getJustificacionDescripcion());
				}
				if(licitador.getJustificacionProceso()!=null){
					datosTramitacion.set("JUSTIFICACION_PROCESO", licitador.getJustificacionProceso().getId()+" - "+licitador.getJustificacionProceso().getValor());
				}
								
			}
			
			OfertasRecibidas ofertasRecibidas = datosTramitacionInf.getOfertasRecibidas();
			if(ofertasRecibidas!=null){
				if(ofertasRecibidas.getNumOfertasRecibidas()!=null){
					datosTramitacion.set("NUMOFERTAS",ofertasRecibidas.getNumOfertasRecibidas());
				}
				if(ofertasRecibidas.getOfertaMasBaja()!=null){
					datosTramitacion.set("IMP_OFERTA_BAJA", ofertasRecibidas.getOfertaMasBaja());
				}
				if(ofertasRecibidas.getOfertaMasAlta()!=null){
					datosTramitacion.set("IMP_OFERTA_ALTA", ofertasRecibidas.getOfertaMasAlta());
				}
			}

			
			FormalizacionBean formalizacion = datosTramitacionInf.getFormalizacion();
			if(formalizacion!=null){
				if(formalizacion.getFechaContrato()!=null){
					fecha = formalizacion.getFechaContrato().getTime();
					datosTramitacion.set("F_CONTRATO", fecha);
				}
				if(formalizacion.getPeriodoValidezInicioContrato()!=null){
					fecha = formalizacion.getPeriodoValidezInicioContrato().getTime();
					datosTramitacion.set("FECHA_INICIO_CONTRATO", fecha);
				}
				if(formalizacion.getPeriodoValidezFinContrato()!=null){
					fecha = formalizacion.getPeriodoValidezFinContrato().getTime();
					datosTramitacion.set("FECHA_FIN_CONTRATO", fecha);
				}
				if(formalizacion.getPorcentajeSubcontratacion()!=null){
					datosTramitacion.set("PORCENTAJE_SUBCONTRATACION", formalizacion.getPorcentajeSubcontratacion());
				}
				if(formalizacion.getTextoAcuerdoFormalizacion()!=null){
					datosTramitacion.set("TEXTO_ACUERDO_FORMALIZACION", formalizacion.getTextoAcuerdoFormalizacion());
				}
			}
			
			
	
			datosTramitacion.store(cct); 
			resultado = true;
			
		 } catch(Exception e) {
			 logger.error(e.getMessage(), e);
			 throw new ISPACRuleException("Error. "+e.getMessage(),e);
	     }
		return resultado;
	}
	
	public static void envioDocumentosAdicionales(IRuleContext rulectx) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
				ClientContext cct = (ClientContext) rulectx.getClientContext();
			// --------------------------------------------------------------------			
			
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			
			//Añadir la información adjunta
			
			Documento[] docAdicional = docInformacionAdicionalPliego(rulectx);
			
			if(docAdicional!=null){
				//Documentos con informaciona adicional
				for(int i = 0; i< docAdicional.length; i++){
					if(docAdicional[i]!=null){
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
							Resultado resultadoAnalisisPrevio = platContratacion.envioOtrosDocumentosLicitacion(entidad, docAdicional[i], publishedByUser);
							
							PublicationResult result = resultadoAnalisisPrevio.getPublicacion();
							
							if(result!=null){
								if(result.getResultCode()!=null){
									logger.warn("url. "+result.getNoticeURL());
									if(result.getResultCode().equals("OK")){					
										//DipucrFuncionesComunes.crearDocPeticionOK_PLACE(resultadoAnalisisPrevio, rulectx, "Anuncio de Pliego");
										logger.warn("Documento anexado");
									}
						
									else{
										logger.error("ERROR: Documento NO anexado "+docAdicional[i].getDescripcion());
									}
								}
								else{
									logger.error("ERROR: Documento NO anexado "+docAdicional[i].getDescripcion());
								}
								
							}
							else{
								logger.error("ERROR: Documento NO anexado "+docAdicional[i].getDescripcion());
							}
							
				
						} catch (RemoteException e) {
							logger.error(e.getMessage(), e);
							throw new ISPACRuleException("Error. "+e.getMessage(),e);
						} catch (ISPACException e) {
							logger.error(e.getMessage(), e);
							throw new ISPACRuleException("Error. "+e.getMessage(),e);
						} 

					}
				}
			}
		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
	}
	
	@SuppressWarnings({ "resource", "unchecked" })
	private static Documento[] docInformacionAdicionalPliego(IRuleContext rulectx) throws ISPACRuleException {
		
		Documento[] docAdicional = null;
		
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
		
			//Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        	
	        	IItem exp = entitiesAPI.getExpedient(numexpPetCont);
	        	
	        	//obtengo el id_tramite y id_fase
				String strQuery = "WHERE NOMBRE = 'Informe Necesidad Contrato' AND ID_PCD="+exp.getInt("ID_PCD");
		        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
		        
				Iterator <IItem> it = collection.iterator();
		        if (it.hasNext())
		        {
		        	IItem doc = it.next();
		        	int idFase = doc.getInt("ID_FASE");
		        	String idTtramiteBpm = doc.getString("ID_TRAMITE_BPM");
		        	
		        	String query = "NUMEXP='"+numexpPetCont+"' AND ID_FASE_PCD="+idFase+" AND ID_TRAMITE_PCD="+idTtramiteBpm+" AND NOMBRE NOT LIKE '%Informe Necesidad Contrato%' AND NOMBRE!='Pliego de Prescripciones Técnicas'";
		        	logger.warn("query "+query);
		 			IItemCollection docsCollection = entitiesAPI.getDocuments(numexpPetCont, query, "FDOC DESC");
		 			
		 			Iterator <IItem> docIterator = docsCollection.iterator();
		 			docAdicional = new Documento [docsCollection.toList().size()];
		 			int i = 0;
					while(docIterator.hasNext()){
						
						IItem docPres = docIterator.next();
						
						//Comprobar si esta firmado o no 
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
						File fichero = DocumentosUtil.getFile(cct, infoPag, null, null);
		 			
			 			Documento documentoAdicional = new Documento();
			 			documentoAdicional.setMimeCode("application/octet-stream");
			 		    Calendar cal=Calendar.getInstance();
			 			cal.setTime(docPres.getDate("FFIRMA"));
			 			documentoAdicional.setFechaFirma(cal);
			 	
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
		 				fichero.delete();
				 		fichero = null;
		 				documentoAdicional.setContenido(bytes);
		 				documentoAdicional.setExpedientNumber(rulectx.getNumExp());
		 				
		 				String descripcion = docPres.getString("DESCRIPCION");
		 				descripcion = DipucrFuncionesComunes.limpiarCaracteresEspeciales(descripcion);
		 				if(descripcion.length()>=50){
		 					descripcion = descripcion.substring(0, 49);
		 				}
		 				documentoAdicional.setDescripcion(descripcion+"."+extension);
		 				
		 				
		 				String nombre = docPres.getString("NOMBRE");
		 				descripcion = docPres.getString("DESCRIPCION");
		 				nombre = nombre.replace("-", " ");
		 				nombre = nombre.replace("_", " ");
		 				nombre = nombre.replace("º", "");
		 				descripcion = descripcion.replace("-", " ");
		 				descripcion = descripcion.replace("_", " ");
		 				descripcion = descripcion.replace("º", "");
		 				logger.warn("nombre "+nombre);
		 				logger.warn("descripcion "+descripcion);
		 				String nombreCompleto = nombre+" "+descripcion;
		 				if(nombreCompleto.length()>=50){
		 					nombreCompleto = nombreCompleto.substring(0, 49);
		 				}
		 				documentoAdicional.setIdTypeDoc("ZZZ");
		 				documentoAdicional.setTypeDoc("Otros documentos");
		 				
		 				documentoAdicional.setNameDoc(nombreCompleto);
		 				
		 				docAdicional[i] = documentoAdicional;
			 		    i++;
		 			}

		        }
	        	
	        	
	        }
	        
	       
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return docAdicional;
		
	}

	public static void acuerdoDictamen(IRuleContext rulectx, String nombreTipoDoc) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
				ClientContext cct = (ClientContext) rulectx.getClientContext();
				IInvesflowAPI invesFlowAPI = cct.getAPI();
				IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------			
			
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			String nombreDoc = DocumentosUtil.getTipoDocNombreByCodigo(cct, nombreTipoDoc);
			

        	String query = "ID_TRAMITE="+rulectx.getTaskId()+" AND NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='"+nombreDoc+"'";
			IItemCollection docsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), query, "FAPROBACION DESC");
			@SuppressWarnings("unchecked")
			Iterator <IItem> docIterator = docsCollection.iterator();
			if(docIterator.hasNext()){
				IItem docPres = docIterator.next();
				String descripcion = "";
				
				if(docPres.getString("DESCRIPCION")!=null) descripcion= docPres.getString("DESCRIPCION");
				if(descripcion.length()>=50){
					descripcion = descripcion.substring(0, 50);
				}
				
				//Añadir la información adjunta			
				Documento docAdicional = getDocumento(rulectx, nombreDoc, descripcion);
				
				if(docAdicional!=null){
					//Documentos con informaciona adicional
					//Envio de la petición de pliego
					try {
						String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
						logger.warn(entidad);
						//Petición
						//String publishedByUser = UsuariosUtil.getDni(cct);
						//String publishedByUser = "99001215S";
						String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
						if(publishedByUser==null || publishedByUser.equals("")){
							publishedByUser = UsuariosUtil.getDni(cct);
						}
						Resultado resultadoAnalisisPrevio = platContratacion.envioOtrosDocumentosLicitacion(entidad, docAdicional, publishedByUser);
						
						PublicationResult resultado = resultadoAnalisisPrevio.getPublicacion();
						
						if(!resultado.getResultCode().equals("OK")){					
							DipucrFuncionesComunes.errorPeticion(resultado, rulectx, nombreDoc);
						}

					} catch (RemoteException e) {
						logger.error(e.getMessage(), e);
						throw new ISPACRuleException("Error. "+e.getMessage(),e);
					} catch (ISPACException e) {
						logger.error(e.getMessage(), e);
						throw new ISPACRuleException("Error. "+e.getMessage(),e);
					} 
				}
			}
			
			
			
		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e1) {
			logger.error(e1.getMessage(), e1);
			throw new ISPACRuleException("Error. "+e1.getMessage(),e1);
		}
		
	}


}
