package es.dipucr.contratacion.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

import es.dipucr.contratacion.objeto.BOE;
import es.dipucr.contratacion.objeto.BOP;
import es.dipucr.contratacion.objeto.DOUE;
import es.dipucr.contratacion.objeto.DatosContrato;
import es.dipucr.contratacion.objeto.DatosEmpresa;
import es.dipucr.contratacion.objeto.DatosLicitacion;
import es.dipucr.contratacion.objeto.DatosTramitacion;
import es.dipucr.contratacion.objeto.DiariosFechaOficiales;
import es.dipucr.contratacion.objeto.DiariosOficiales;
import es.dipucr.contratacion.objeto.FormalizacionBean;
import es.dipucr.contratacion.objeto.OfertasRecibidas;
import es.dipucr.contratacion.objeto.Peticion;
import es.dipucr.contratacion.objeto.ResultadoLicitacion;
import es.dipucr.contratacion.objeto.Solvencia;
import es.dipucr.contratacion.services.PlataformaContratacionStub.AplicacionPresupuestaria;
import es.dipucr.contratacion.services.PlataformaContratacionStub.Campo;
import es.dipucr.contratacion.services.PlataformaContratacionStub.CondicionesLicitadores;
import es.dipucr.contratacion.services.PlataformaContratacionStub.CriterioAdjudicacionMultCrit;
import es.dipucr.contratacion.services.PlataformaContratacionStub.CriteriosAdjudicacion;
import es.dipucr.contratacion.services.PlataformaContratacionStub.Documento;
import es.dipucr.contratacion.services.PlataformaContratacionStub.DuracionContratoBean;
import es.dipucr.contratacion.services.PlataformaContratacionStub.EventoAperturaBean;
import es.dipucr.contratacion.services.PlataformaContratacionStub.FundacionPrograma;
import es.dipucr.contratacion.services.PlataformaContratacionStub.Garantia;
import es.dipucr.contratacion.services.PlataformaContratacionStub.LicitadorBean;
import es.dipucr.contratacion.services.PlataformaContratacionStub.OrganoAsistencia;
import es.dipucr.contratacion.services.PlataformaContratacionStub.Periodo;
import es.dipucr.contratacion.services.PlataformaContratacionStub.PersonaComite;
import es.dipucr.contratacion.services.PlataformaContratacionStub.PersonalContacto;
import es.dipucr.contratacion.services.PlataformaContratacionStub.RequisitfiDeclaraciones;
import es.dipucr.contratacion.services.PlataformaContratacionStub.SobreElectronico;
import es.dipucr.contratacion.services.PlataformaContratacionStub.SolvenciaEconomica;
import es.dipucr.contratacion.services.PlataformaContratacionStub.SolvenciaTecnica;
import es.dipucr.contratacion.services.PlataformaContratacionStub.VariantesOfertas;
import es.dipucr.sigem.api.rule.common.comparece.CompareceConfiguration;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class DipucrFuncionesComunes {
	
	public static final Logger LOGGER = Logger.getLogger(DipucrFuncionesComunes.class);
	
	public static final String DOC_PIN = "Anuncio de Informacion Previa";  //Anuncio previo
	public static final String DOC_CN = "Anuncio de Licitación";	 //Anuncio de Licitación
	public static final String DOC_CD = "Anuncio de Pliego";	 //Pliego
	public static final String DOC_ADJ = "Anuncio Resultado Licitación";	 //Anuncio de adjudicacion
	public static final String DOC_GEN = "DOC_GEN";	 //Documentos generales
	public static final double CUANTIACONTRATISTAOBRAS = 40000;
	public static final double CUANTIACONTRATISTASERVICIOS = 15000;
	public static final double CUANTIACONTRATISTASUMINISTROS = 15000;
	
	public static final String DESC = " DESC";
	
	
	
//	public static void envioEstadoExpediente(IRuleContext rulectx, Resultado resultadoEnvio, String tipoLicitacion) throws ISPACRuleException {
//		try{
//			// --------------------------------------------------------------------
//				ClientContext cct = (ClientContext) rulectx.getClientContext();
//			// --------------------------------------------------------------------			
//			
//			try {
//				PlataformaContratacionStub platContratacion = new PlataformaContratacionStub(ServiciosWebContratacionFunciones.getDireccionSW());
//				
//				String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
//				
//				//Petición
//				//String publishedByUser = UsuariosUtil.getDni(cct);
//				//String publishedByUser = "99001215S";
//				String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
//				if(publishedByUser==null || publishedByUser.equals("")){
//					publishedByUser = UsuariosUtil.getDni(cct);
//				}
//				if(resultadoCorrecto(resultadoEnvio)){
//				
//					EstadoExpediente estadoExpediente = new EstadoExpediente();
//					estadoExpediente.setEntidad(entidad);
//					estadoExpediente.setNumexp(rulectx.getNumExp());
//					estadoExpediente.setPublishedByUser(publishedByUser);
//					DatosContrato datosContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
//					estadoExpediente.setOrganoContratacion(datosContrato.getOrganoContratacion());
//					EstadoExpedienteResponse resultadoEstExp = platContratacion.estadoExpediente(estadoExpediente);
//					//Resultado resultado = platContratacion.estadoExpediente("005", "DPCR2013/54503", publishedByUser);
//					Resultado resultado = resultadoEstExp.get_return();
//					PlaceAskResult placeResult = resultado.getPlaceAskResult();
//					if(placeResult!=null){
//						if(placeResult.getResultCode().equals("OK")){
//							if(placeResult.getExpedientStateData()!=null){
//								ExpedientStateData expediente = placeResult.getExpedientStateData();
//								if(expediente.getAnuncios()!=null){
//									String nombreDoc = "";
//									for(int i = 0; i < expediente.getAnuncios().length && nombreDoc.equals(""); i++){
//										Anuncio anuncio = expediente.getAnuncios()[i];
//										if(anuncio.getUrlPDF()!=null){
//											LOGGER.warn("URL: "+anuncio.getUrlPDF());
//											
//											ResultadoLicitacion licitacion = DipucrFuncionesComunes.getResultadoLicitacion(rulectx);
//											
//											if(anuncio.getType().equals("DOC_CN") && tipoLicitacion.equals("Licitacion")){
//												nombreDoc = "Anuncio de Licitación";
//											}
//											if(anuncio.getType().equals("DOC_CD") && tipoLicitacion.equals("Pliego")){
//												nombreDoc = "Anuncio de Pliego";
//											}
//											if(anuncio.getType().equals("DOC_CAN_ADJ") && licitacion.getCodigoAdjudicacion().getId().equals("8")){
//												nombreDoc = "Anuncio de Adjudicación";
//											}
//											if(anuncio.getType().equals("DOC_FORM") && licitacion.getCodigoAdjudicacion().getId().equals("9")){
//												nombreDoc = "Anuncio de Formalización";
//											}
//											if(anuncio.getType().equals("RENUNCIA") && licitacion.getCodigoAdjudicacion().getId().equals("5")){
//												nombreDoc = "Anuncio de Renuncia";
//											}
//											if(anuncio.getType().equals("DESISTIMIENTO") && licitacion.getCodigoAdjudicacion().getId().equals("4")){
//												nombreDoc = "Anuncio de Desistimiento";
//											}
//											if(anuncio.getType().equals("DESIERTO") && licitacion.getCodigoAdjudicacion().getId().equals("3")){
//												nombreDoc = "Anuncio de Desierto";
//											}
//											if(!nombreDoc.equals("")){
//												DipucrFuncionesComunes.cargaAnuncioFirmado(rulectx, anuncio.getUrlPDF(), nombreDoc, resultadoEnvio);
//											}
//											
//										}
//									}
//								}
//							}
//							else{
//								DipucrFuncionesComunes.errorPeticion(resultadoEnvio.getPublicacion(), rulectx, tipoLicitacion, "Error");
//							}
//						}
//						else{
//							DipucrFuncionesComunes.errorPeticion(resultadoEnvio.getPublicacion(), rulectx, tipoLicitacion, "Error");
//						}
//					}
//				}
//				else{
//					DipucrFuncionesComunes.errorPeticion(resultadoEnvio.getPublicacion(), rulectx, tipoLicitacion, "Error");
//				}
//
//			} catch (RemoteException e) {
//				LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//				throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			} catch (ISPACException e) {
//				LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//				throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			} catch (PlataformaContratacionDatatypeConfigurationExceptionException e) {
//				LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//				throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			} catch (PlataformaContratacionJAXBExceptionException e) {
//				LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//				throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			} catch (PlataformaContratacionMalformedURLExceptionException e) {
//				LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//				throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			} 
//		}
//		catch(ISPACRuleException e){
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		}
//		
//	}
	

//	private static boolean resultadoCorrecto(Resultado resultadoEnvio) {
//		boolean resultado = false;
//		if(resultadoEnvio!=null && resultadoEnvio.getPublicacion()!=null){
//			PublicationResult publicacion = resultadoEnvio.getPublicacion();
//			if(publicacion!=null && publicacion.getResultCode()!=null){
//				if(publicacion.getResultCode().equals("OK")) resultado=true;
//			}
//		}
//				
//		return resultado;
//	}


	@SuppressWarnings({ "resource", "unchecked" })
	public static Documento getDocumento(IRuleContext rulectx, IItem docPres, String nombreMostrarDoc) throws ISPACRuleException {
		Documento docAdj = null;
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/

			if(docPres!=null){
				docAdj = new Documento();
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
				if(docPres.getDate("FFIRMA")!=null){
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(docPres.getDate("FFIRMA"));
					docAdj.setFechaFirma(calendar);
				}
				
				
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
				docAdj.setContenido(new DataHandler(bytes,"application/octet-stream"));
				docAdj.setMimeCode("application/octet-stream");
				
				docAdj.setExpedientNumber(rulectx.getNumExp());
 				
 				String nombre = nombreMostrarDoc.replace("-", " ");
 				nombre = nombreMostrarDoc.replace("º", "");
 				nombre = nombre.replace("_", " ");

 				docAdj.setNameDoc(nombre+"."+extension+"");
 				
 				//Obtengo el tipo de documento.
 				String query = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
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
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		}
		
		return docAdj;
	}
	
	
	public static String quitarExtensionNombre(String nombre){
		
		String nombreSinExtension = nombre;
		
		if(nombre.contains(".")){
			nombreSinExtension = nombre.substring(0,nombre.lastIndexOf("."));
		}	
		
		return nombreSinExtension;
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
		 input = input.replace(".", "");
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
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						resLicitacion.setCodigoAdjudicacion(campo);
					}
				}
			}
		
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		}
		return resLicitacion;
	}

	@Deprecated
	public static PersonalContacto[] getPersonalContacto(IRuleContext rulectx) throws ISPACRuleException {
		return getPersonalContacto(rulectx.getClientContext(), rulectx.getNumExp());
	}
	
	public static PersonalContacto[] getPersonalContacto(IClientContext cct, String numexp) throws ISPACRuleException {

		PersonalContacto[] persCont = new PersonalContacto[3];
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Personal contacto contratacion
			persCont[0] = new PersonalContacto();
			//Personal contacto secretaria
			persCont[1] = new PersonalContacto();
			IItemCollection persContCollection = entitiesAPI.getEntities("CONTRATACION_PERS_CONTACTO", numexp);
			Iterator <?> persContIterator = persContCollection.iterator();
			
			while(persContIterator.hasNext()){
				IItem itPersCont = (IItem) persContIterator.next();
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
				Campo formatoDirec = new Campo();
				formatoDirec.setId("1");
				formatoDirec.setValor("Spanish Format");
				persCont[0].setCodFormatoDirec(formatoDirec);
				
				Campo pais = new Campo();
				pais.setId("ES");
				pais.setValor("España");
				persCont[0].setPais(pais);
				
				if(itPersCont.getString("LOCALIZACIONGEOGRAFICA")!=null) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						persCont[0].setLocalizacionGeografica(campo);
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
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						persCont[1].setLocalizacionGeografica(campo);
					}
				}
				
				//ORGANO ASISTENCIA
				persCont[2] = new PersonalContacto();
				if(StringUtils.isNotEmpty(itPersCont.getString("EMAIL_OA"))){
					persCont[2].setEmail(itPersCont.getString("EMAIL_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("EMAIL_CM_OA"))){
					persCont[2].setEmail(itPersCont.getString("EMAIL_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("CALLE_OA"))){
					persCont[2].setCalle(itPersCont.getString("CALLE_OA"));
					persCont[2].setCodFormatoDirec(formatoDirec);					
					persCont[2].setPais(pais);
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("CALLE_CM_OA"))){
					persCont[2].setCalle(itPersCont.getString("CALLE_CM_OA"));
					persCont[2].setCodFormatoDirec(formatoDirec);					
					persCont[2].setPais(pais);
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("NUMERO_OA"))){
					persCont[2].setNumeroEdificio(itPersCont.getString("NUMERO_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("NUMERO_CM_OA"))){
					persCont[2].setNumeroEdificio(itPersCont.getString("NUMERO_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("CP_OA"))){
					persCont[2].setCp(itPersCont.getString("CP_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("CP_CM_OA"))){
					persCont[2].setCp(itPersCont.getString("CP_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIDAD_OA"))){
					persCont[2].setCiudad(itPersCont.getString("LOCALIDAD_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIDAD_CM_OA"))){
					persCont[2].setCiudad(itPersCont.getString("LOCALIDAD_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("PROVINCIA_OA"))){
					persCont[2].setProvincia(itPersCont.getString("PROVINCIA_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("PROVINCIA_CM_OA"))){
					persCont[2].setProvincia(itPersCont.getString("PROVINCIA_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("MOVIL_OA"))){
					persCont[2].setTelefono(itPersCont.getString("MOVIL_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("MOVIL_CM_OA"))){
					persCont[2].setTelefono(itPersCont.getString("MOVIL_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIZACIONGEOGRAFICA_OA"))) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICA_OA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						persCont[2].setLocalizacionGeografica(campo);
					}
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIZACIONGEOGRAFICA_CM_OA"))) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICA_CM_OA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						persCont[2].setLocalizacionGeografica(campo);
					}
				}
			}
			
		} catch(ISPACRuleException e){
			LOGGER.error("Numexp. " + numexp + " Error. " + e.getMessage(),e);
			throw new ISPACRuleException("Numexp. " + numexp + " Error. " + e.getMessage(),e);
			
		} catch (ISPACException e) {
			LOGGER.error("Numexp. " + numexp + " Error. " + e.getMessage(),e);
			throw new ISPACRuleException("Numexp. " + numexp + " Error. " + e.getMessage(),e);
		}
		
		return persCont;
	}
	
	@Deprecated
	public static Garantia[] getGarantias(IRuleContext rulectx) throws ISPACRuleException {
		return getGarantias(rulectx.getClientContext(), rulectx.getNumExp());
	}
	
	public static Garantia[] getGarantias(IClientContext cct, String numexp) throws ISPACRuleException {
		Garantia[] vgarantia = null;
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection garantiaCollection = entitiesAPI.getEntities("CONTRATACION_GARANTIAS", numexp);
			Iterator <?> garantiaIterator = garantiaCollection.iterator();
			vgarantia = new Garantia [garantiaCollection.toList().size()];
			int i = 0;
			while(garantiaIterator.hasNext()){
				IItem garantiaPres = (IItem) garantiaIterator.next();
				vgarantia[i] = new Garantia();
				if(garantiaPres.getString("AMOUNTRATE")!=null){
					vgarantia[i].setAmountRate(garantiaPres.getString("AMOUNTRATE"));
				}
				if(garantiaPres.getDate("CONSTITUTIONPERIOD")!=null){
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(garantiaPres.getDate("CONSTITUTIONPERIOD"));
					vgarantia[i].setConstitutionPeriod(calendar);
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
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						vgarantia[i].setTipoGarantia(campo);
					}
				}
				if(garantiaPres.getString("PERIODUNITCODE")!=null) {
					String critSolv = garantiaPres.getString("PERIODUNITCODE");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						vgarantia[i].setTipoPeriodo(campo);
					}
				}
				if(garantiaPres.getString("DESCRIPCION")!=null){
					String [] descripcion = {garantiaPres.getString("DESCRIPCION")};
					vgarantia[i].setDescripcion(descripcion);
				}
				i++;
			}
			
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
		
		return vgarantia;
		
	}
	
	@Deprecated
	public static Solvencia getSolvencia(IRuleContext rulectx) throws ISPACRuleException {
		return getSolvencia(rulectx.getClientContext(), rulectx.getNumExp());
	}
	
	public static Solvencia getSolvencia(IClientContext cct, String numexp) throws ISPACRuleException {

		Solvencia solvencia = new Solvencia();
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			Vector <SolvenciaTecnica> vSolvenciaTecnica = new Vector<SolvenciaTecnica>();
			Vector <SolvenciaEconomica> vSolvenciaEconomica = new Vector<SolvenciaEconomica>();
			
			IItemCollection solvenciaCollection = entitiesAPI.getEntities("CONTRATACION_SOLVENCIA_TECN", numexp);
			Iterator <?> solvenciaIterator = solvenciaCollection.iterator();
			while(solvenciaIterator.hasNext()){
				IItem solvenciaPres = (IItem) solvenciaIterator.next();
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
							Campo campo = new Campo();
							campo.setId(vcritSolv[0]);
							campo.setValor(vcritSolv[1]);
							solTecnica.setCriterioSolvencia(campo);
							
							if(solvenciaPres.getString("EVIDENCE_CRIT_SOLVENCIA")!=null) {
								String critSolvTecn= solvenciaPres.getString("EVIDENCE_CRIT_SOLVENCIA");
								String [] vcritSolvTecn = critSolvTecn.split(" - ");
								if(vcritSolvTecn.length >1){
									Campo evidencia = new Campo();
									evidencia.setId(vcritSolvTecn[0]);
									evidencia.setValor(vcritSolvTecn[1]);
									solTecnica.setCriterioSolvenciaAcreditarRequisito(evidencia);
								}
							}
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
						Calendar calendar=Calendar.getInstance();
						calendar.setTime(solvenciaPres.getDate("PERIODODURACION"));
						solTecnica.setPeriodoDuracion(calendar);
					}
					//Expresión matemática
					if(solvenciaPres.getString("EXPMAT")!=null){
						String mate = solvenciaPres.getString("EXPMAT");
						String [] vmate = mate.split(" - ");
						if(vmate.length >1){
							Campo campo = new Campo();
							campo.setId(vmate[0]);
							campo.setValor(vmate[1]);
							solTecnica.setExpresEvaluarCriterioEvalucion(campo);
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
							Campo campo = new Campo();
							campo.setId(vcritSolv[0]);
							campo.setValor(vcritSolv[1]);
							solEcon.setCriterioSolvencia(campo);
							if(solvenciaPres.getString("EVIDENCE_CRIT_SOLVENCIA_ECO")!=null) {
								String critSolvEco= solvenciaPres.getString("EVIDENCE_CRIT_SOLVENCIA_ECO");
								String [] vcritSolvEco = critSolvEco.split(" - ");
								if(vcritSolvEco.length >1){
									Campo evidencia = new Campo();
									evidencia.setId(vcritSolvEco[0]);
									evidencia.setValor(vcritSolvEco[1]);
									solEcon.setCriterioSolvenciaAcreditarRequisito(evidencia);
								}
							}
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
						Calendar calendar=Calendar.getInstance();
						calendar.setTime(solvenciaPres.getDate("PERIODODURACION_ECO"));
						solEcon.setPeriodoDuracion(calendar);
					}
					//Expresión matemática
					if(solvenciaPres.getString("EXPMAT_ECO")!=null){
						String mate = solvenciaPres.getString("EXPMAT_ECO");
						String [] vmate = mate.split(" - ");
						if(vmate.length >1){
							Campo campo = new Campo();
							campo.setId(vmate[0]);
							campo.setValor(vmate[1]);
							solEcon.setExpresEvaluarCriterioEvalucion(campo);
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
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
		
		return solvencia;
	}
	
	@Deprecated
	public static SobreElectronico[] getSobreElec(IRuleContext rulectx) throws ISPACRuleException {
		return getSobreElec(rulectx.getClientContext(), rulectx.getNumExp());
	}
		
	public static SobreElectronico[] getSobreElec(IClientContext cct, String numexp) throws ISPACRuleException {
		SobreElectronico[] docPresentar;
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection docPresCollection = entitiesAPI.getEntities("CONTRATACION_DOC_PRESENTAR", numexp);
			Iterator <?> docPresIterator = docPresCollection.iterator();
			docPresentar = new SobreElectronico[docPresCollection.toList().size()];
			int i = 0;
			while(docPresIterator.hasNext()){
				IItem docPres = (IItem) docPresIterator.next();
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
						Campo campo = new Campo();
						campo.setId(vtipoOferta[0]);
						campo.setValor(vtipoOferta[1]);
						docPresentar[i].setCodOferta(campo);
					}
				}				
				
				//Listado de documentos.
				int idDoc = docPres.getInt("ID");
				String strQuery="SELECT VALUE FROM CONTRATACION_DOC_PRESENTAR_S WHERE REG_ID = "+idDoc+" AND FIELD='LIST_DOC'";
		        ResultSet datos = cct.getConnection().executeQuery(strQuery).getResultSet();
		        if(datos!=null)
		      	{
		        	Vector <Documento> listDoc = new Vector<Documento>();
		        	while(datos.next()){
		        		String nombreDoc = "";
		          		if (datos.getString("VALUE")!=null) nombreDoc = datos.getString("VALUE");
		          		Documento documento = new Documento();
		          		documento.setNameDoc(nombreDoc);
		          		listDoc.add(documento);
		          	}
		        	
		        	Documento [] array = null;
		        	if(listDoc.size()>0){
		        		array = new Documento[listDoc.size()];
			        	listDoc.toArray(array);
		        	}
		        	docPresentar[i].setDoc(array);
		      	}
		        if(StringUtils.isNotEmpty(docPres.getString("PRESEN_SOBRE"))){
		        	docPresentar[i].setPresentacionSobre(docPres.getString("PRESEN_SOBRE"));
		        }
		        if(StringUtils.isNotEmpty(docPres.getString("FIRMA"))){
		        	if(docPres.getString("FIRMA").equals("SI")){
		        		docPresentar[i].setFirmadoSobreRepresentado(true);
		        	}
		        	else{
		        		docPresentar[i].setFirmadoSobreRepresentado(false);
		        	}
		        	
		        }
		        if(StringUtils.isNotEmpty(docPres.getString("CIFRADO"))){
		        	if(docPres.getString("CIFRADO").equals("SI")){
		        		docPresentar[i].setEncriptadoSobre(true);
		        	}
		        	else{
		        		docPresentar[i].setEncriptadoSobre(false);
		        	}
		        }
		    		        
		        if(StringUtils.isNotEmpty(docPres.getString("ANONIMO"))){
		        	if(docPres.getString("ANONIMO").equals("SI")){
		        		docPresentar[i].setIdentificarLicitador(true);
		        	}
		        	else{
		        		docPresentar[i].setIdentificarLicitador(false);
		        	}
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
		        	LOGGER.warn("hora "+hora);
		        	String [] vHora = hora.split(":"); 
		        	if(vHora.length>0){
		        		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(vHora[0]));
		        		calendar.set(Calendar.MINUTE, Integer.parseInt(vHora[1]));
		        	}
		        	
		        	LOGGER.warn("calendar "+calendar.getTime());
		        	
		        	eventoApertura.setFechaApertura(calendar);
		        	
		        	if(docPres.getString("TIPO_EVENTO")!=null) {
						String tipoOferta = docPres.getString("TIPO_EVENTO");
						String [] vtipoOferta = tipoOferta.split(" - ");
						if(vtipoOferta.length >1){
							Campo campo = new Campo();
							campo.setId(vtipoOferta[0]);
							campo.setValor(vtipoOferta[1]);
							eventoApertura.setTipoEvento(campo);
						}
					}
		        }
				
		        docPresentar[i].setEventoApertura(eventoApertura);
				i++;
			}
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (SQLException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
		return docPresentar;
	}
	
	@Deprecated
	public static DatosEmpresa getDatosEmpresa(IRuleContext rulectx, String numexp) throws ISPACRuleException {
		return getDatosEmpresa(rulectx.getClientContext(), numexp);
	}
		
	public static DatosEmpresa getDatosEmpresa(IClientContext cct, String numexp) throws ISPACRuleException {

		DatosEmpresa datosEmpresa = new DatosEmpresa();
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection datosEmpresaCollection = entitiesAPI.getEntities("CONTRATACION_INF_EMPRESA", numexp);
			Iterator <?> datosEmpresaIterator = datosEmpresaCollection.iterator();
			if(datosEmpresaIterator.hasNext()){
				IItem datEmp = (IItem) datosEmpresaIterator.next();
				
				//Clasificacion
				int idCodiceEmpresa = datEmp.getInt("ID");
				String strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_INF_EMPRESA_S WHERE REG_ID = "+idCodiceEmpresa+"";
		        ResultSet datos = cct.getConnection().executeQuery(strQuery).getResultSet();
		        String field = "";
		        Vector<String[]> valores = new Vector<String[]> ();
		        String valorCPV;
		        
		        if(datos!=null) {
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
										Campo campo = new Campo();
										campo.setId(vClas[0]);
										campo.setValor(vClas[1]);
										valorClasificacion.add(campo);
										iClas ++;
										
										if(datEmp.getString("EVIDENCE_CLAS_EMP")!=null) {
											String critSolv = datEmp.getString("EVIDENCE_CLAS_EMP");
											String [] vcritSolv = critSolv.split(" - ");
											if(vcritSolv.length >1){
												Campo evidencia = new Campo();
												evidencia.setId(vcritSolv[0]);
												evidencia.setValor(vcritSolv[1]);
												datosEmpresa.setClasificacionEvidence(evidencia);
											}
										}
									}
			        			}
			        			//REQUISITOS ESPECÍFICOS Y DECLARACIONES REQUERIDAS
			        			else{
			        				//Capacidad para contratar -> "TIP_DECLARACION"
			        				String [] vTipoDecl = aClasTipDecl[1].split(" - ");
									if(vTipoDecl.length >0){
										RequisitfiDeclaraciones requ = new RequisitfiDeclaraciones();
										Campo campo = new Campo();
										campo.setId(vTipoDecl[0]);
										campo.setValor(vTipoDecl[1]);
										requ.setRequisitEspecifico(campo);
										if(datEmp.getString("EVIDENCE_CAPAC_CONT")!=null) {
											String critSolv = datEmp.getString("EVIDENCE_CAPAC_CONT");
											String [] vcritSolv = critSolv.split(" - ");
											if(vcritSolv.length >1){
												Campo evidencia = new Campo();
												evidencia.setId(vcritSolv[0]);
												evidencia.setValor(vcritSolv[1]);
												requ.setRequisitEspecificoAcreditarRequisito(evidencia);
											}
										}
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

		        //Años de experiencia
		        if(datEmp.getString("OPERATINGYEARSQUANTITY")!=null){
		        	condLicit.setAnosExperiencia(datEmp.getString("OPERATINGYEARSQUANTITY"));
		        	
		        	if(datEmp.getString("EVIDENCE_OPERATINGYEARSQUANTIT")!=null) {
						String critSolv = datEmp.getString("EVIDENCE_OPERATINGYEARSQUANTIT");
						String [] vcritSolv = critSolv.split(" - ");
						if(vcritSolv.length >1){
							Campo evidencia = new Campo();
							evidencia.setId(vcritSolv[0]);
							evidencia.setValor(vcritSolv[1]);
							condLicit.setAnosExperienciaAcreditarRequisito(evidencia);
						}
					}
		        }

		        //Número mínimo de empleados
		        if(datEmp.getString("EMPLOYEEQUANTITY")!=null){
		        	condLicit.setNumMinEmpleados(datEmp.getString("EMPLOYEEQUANTITY"));
		        	if(datEmp.getString("EVIDENCE_EMPLOYEEQUANTITY")!=null) {
						String critSolv = datEmp.getString("EVIDENCE_EMPLOYEEQUANTITY");
						String [] vcritSolv = critSolv.split(" - ");
						if(vcritSolv.length >1){
							Campo evidencia = new Campo();
							evidencia.setId(vcritSolv[0]);
							evidencia.setValor(vcritSolv[1]);
							condLicit.setNumMinEmpleadosAcreditarRequisito(evidencia);
						}
					}
		        }
		        
		        //Habilitación empresarial o profesional -> Título habilitante 
		        if(datEmp.getString("TITULO_HABILITANTE")!=null){
		        	condLicit.setHabilitacionEmpresarial(datEmp.getString("TITULO_HABILITANTE"));
		        	
		        	if(datEmp.getString("EVIDENCE_TITULO_HABILITANTE")!=null) {
						String critSolv = datEmp.getString("EVIDENCE_TITULO_HABILITANTE");
						String [] vcritSolv = critSolv.split(" - ");
						if(vcritSolv.length >1){
							Campo evidencia = new Campo();
							evidencia.setId(vcritSolv[0]);
							evidencia.setValor(vcritSolv[1]);
							condLicit.setHabilitacionEmpresarialAcreditarRequisito(evidencia);
						}
					}
		        }
		        
		        //Requerimiento CCVV
		        if(datEmp.getString("CVV_DESCRIPCION")!=null){
		        	condLicit.setRequerCVVVA(datEmp.getString("CVV_DESCRIPCION"));
		        	
		        	if(datEmp.getString("EVIDENCE_CVV")!=null) {
						String critSolv = datEmp.getString("EVIDENCE_CVV");
						String [] vcritSolv = critSolv.split(" - ");
						if(vcritSolv.length >1){
							Campo evidencia = new Campo();
							evidencia.setId(vcritSolv[0]);
							evidencia.setValor(vcritSolv[1]);
							condLicit.setRequerCVVVAcreditarRequisito(evidencia);
						}
					}
		        }
		        
		        datosEmpresa.setCondLicit(condLicit);
		        
		        /**
		         * REQUISITOS ESPECÍFICOS Y DECLARACIONES REQUERIDAS 
		         * **/
			}
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (SQLException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
		return datosEmpresa;
	}
	
	@Deprecated
	public static BOP getBOP(IRuleContext rulectx) throws ISPACRuleException {
		return getBOP(rulectx.getClientContext(), rulectx.getNumExp());
	}
		
	public static BOP getBOP(IClientContext cct, String numexp) throws ISPACRuleException {

		BOP bop = null;
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Publicaciones Oficiales
			IItemCollection solBopCollection = entitiesAPI.getEntities("BOP_SOLICITUD", numexp);
			Iterator <?> solBopIterator = solBopCollection.iterator();
			if(solBopIterator.hasNext()){
				bop = new BOP();
				IItem solBop = (IItem) solBopIterator.next();
				Date fecha_publicacion=new Date();
				if(solBop.getDate("FECHA_PUBLICACION")!=null){
					fecha_publicacion=solBop.getDate("FECHA_PUBLICACION");
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(fecha_publicacion);
					bop.setFechaPublicacion(calendar);
				}
				
				if(solBop.getString("NUM_ANUNCIO_BOP")!=null)bop.setNumAnuncio(solBop.getString("NUM_ANUNCIO_BOP"));
				//TODO acceder a la ruta del BOP
				bop.setUrlPublicacion("http://bop.sede.dipucr.es/");
			}
			
		} catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
			
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
		return bop;
	}
	
	@Deprecated	
	public static DiariosFechaOficiales getFechaDiariosOficiales(IRuleContext rulectx, String numexp) throws ISPACRuleException {
		return getFechaDiariosOficiales(rulectx.getClientContext(), numexp);
	}
		
	public static DiariosFechaOficiales getFechaDiariosOficiales(IClientContext cct, String numexp) throws ISPACRuleException {
		DiariosFechaOficiales diariosOficiales = new DiariosFechaOficiales();
		try{
			
			Iterator<IItem> itColl = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_PUBLIC_PLACE", "NUMEXP = '" + numexp + "'");
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
			itColl = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_DOUE", "NUMEXP='"+numexp+"'");
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
				if(place.getString("DIRECTIVA")!=null) {
					String directiva = place.getString("DIRECTIVA");
					String [] vdirectiva = directiva.split(" - ");
					if(vdirectiva.length >1){
						Campo campo = new Campo();
						campo.setId(vdirectiva[0]);
						campo.setValor(vdirectiva[1]);
						diariosOficiales.setContratoSujetoRegArmon(campo);
					}
				}
				if(place.getString("ADJPYME")!=null){
					if(place.getString("ADJPYME").equals("SI")){
						diariosOficiales.setAdjudicatarioPYME(new Boolean(true));
					}
					if(place.getString("ADJPYME").equals("NO")){
						diariosOficiales.setAdjudicatarioPYME(new Boolean(false));
					}
				}
			}
			
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
			
		return diariosOficiales;
	}
			
	@Deprecated
	public static DiariosOficiales getDiariosOficiales(IRuleContext rulectx, String nombrePublicacion) throws ISPACRuleException {
		return getDiariosOficiales(rulectx.getClientContext(), rulectx.getNumExp(), nombrePublicacion);
	}

	public static DiariosOficiales getDiariosOficiales(IClientContext cct, String numexp, String nombrePublicacion) throws ISPACRuleException {

		DiariosOficiales diariosOficiales = new DiariosOficiales();
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Publicaciones Oficiales
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
			IItemCollection collectionDoue = entitiesAPI.queryEntities("CONTRATACION_DOUE", strQuery);
			Iterator<?> itDoue = collectionDoue.iterator();
			
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
					LOGGER.warn("BOE: "+diariosOficiales.getBoe().isPublicarBOE());
					LOGGER.warn("BOE: "+diariosOficiales.getDoue().isPublicarDOUE());
				}
				
			}
			
		} catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
			
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
			
		return diariosOficiales;
	}

	@Deprecated
	public static DatosLicitacion getDatosLicitacion(IRuleContext rulectx) throws ISPACRuleException {
		return getDatosLicitacion(rulectx.getClientContext(), rulectx.getNumExp());
	}
	
	public static DatosLicitacion getDatosLicitacion(IClientContext cct, String numexp) throws ISPACRuleException {
		DatosLicitacion datosLicitacion = new DatosLicitacion();
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Presentacion Oferta
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
			IItemCollection collectionDatLic = entitiesAPI.queryEntities("CONTRATACION_DATOS_LIC", strQuery);
			Iterator<?> itDatLic = collectionDatLic.iterator();
			if (itDatLic.hasNext()) {
				IItem datosLic = (IItem) itDatLic.next();
				String presOferta = "";
				if(StringUtils.isNotEmpty(datosLic.getString("PRESENT_OFERTA")))presOferta=datosLic.getString("PRESENT_OFERTA");
				String [] vspresOfert = presOferta.split(" - ");
				if(vspresOfert.length >1){
					Campo campo = new Campo();
					campo.setId(vspresOfert[0]);
					campo.setValor(vspresOfert[1]);
					datosLicitacion.setTipoPresentacionOferta(campo);
					//1 - Electrónica
					if(campo.getId().equals("1")){
						if(StringUtils.isNotEmpty(datosLic.getString("IDENT_OA")) && StringUtils.isNotEmpty(datosLic.getString("NOMBRE_OA"))){
							//Organo de Asistencia
							OrganoAsistencia organoAsistencia = new OrganoAsistencia();					
							organoAsistencia.setIdentificacion(datosLic.getString("IDENT_OA"));
							organoAsistencia.setNombreOrgAsist(datosLic.getString("NOMBRE_OA"));					
							datosLicitacion.setOrganoAsistencia(organoAsistencia);
						}
						else{
							if(StringUtils.isNotEmpty(datosLic.getString("IDENT_CM_OA")) && StringUtils.isNotEmpty(datosLic.getString("NOMBRE_CM_OA"))){
								//Organo de Asistencia
								OrganoAsistencia organoAsistencia = new OrganoAsistencia();					
								organoAsistencia.setIdentificacion(datosLic.getString("IDENT_CM_OA"));
								organoAsistencia.setNombreOrgAsist(datosLic.getString("NOMBRE_CM_OA"));					
								datosLicitacion.setOrganoAsistencia(organoAsistencia);
							}
						}
					}					
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
//			        		rulectx.setInfoMessage("No está bien metido el dato de Aplicación Presupuestaria");
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
					Campo campo = new Campo();
					campo.setId(vproc_adj[0]);
					campo.setValor(vproc_adj[1]);
					critAdjuficacion.setTipoAdjudicacion(campo);
				}
				//Tipo Oferta:
				String tipo_oferta = "";
				if(datosLic.getString("EVA_ECO")!=null) tipo_oferta = datosLic.getString("EVA_ECO");
				String [] vtipo_oferta = tipo_oferta.split(" - ");
				if(vtipo_oferta.length >1){
					Campo campo = new Campo();
					campo.setId(vtipo_oferta[0]);
					campo.setValor(vtipo_oferta[1]);
					critAdjuficacion.setValoracionTipoOferta(campo);
				}
				//Algoritmos de calculo de la ponderación
				String algoritmo = "";
				if(datosLic.getString("ALG_CALC_POND")!=null) algoritmo = datosLic.getString("ALG_CALC_POND");
				String [] valgoritmo = algoritmo.split(" - ");
				if(valgoritmo.length >1){
					Campo campo = new Campo();
					campo.setId(valgoritmo[0]);
					campo.setValor(valgoritmo[1]);
					critAdjuficacion.setCodigoAlgoritmo(campo);
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
		        getCriteriosAdjudicacion(cct, numexp, critAdjuficacion);
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
					Campo campo = new Campo();
					campo.setId(vprogr_fin[0]);
					campo.setValor(vprogr_fin[1]);
					fundacionPrograma.setProgramasFinanciacionCode(campo);
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
			
		} catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
			
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp +" - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
			
		} catch (SQLException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}	
			
		return datosLicitacion;
	}
	
	private static void getCriteriosAdjudicacion(IClientContext cct, String numexp, CriteriosAdjudicacion critAdjuficacion) throws ISPACRuleException {
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Presentacion Oferta
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
			IItemCollection collectionDatLic = entitiesAPI.queryEntities("CONTRATACION_CRIT_ADJ", strQuery);
			Iterator<?> itDatLic = collectionDatLic.iterator();
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
							Campo campo = new Campo();
							campo.setId(vcrit_adj[0]);
							campo.setValor(vcrit_adj[1]);
							criterio.setCodCritAdj(campo);
						}
					}
					if(datosLic.getString("CRIT_ADJ_SUB")!=null){
						String crit_adj = datosLic.getString("CRIT_ADJ_SUB");
						String [] vcrit_adj = crit_adj.split(" - ");
						if(vcrit_adj.length >1){
							Campo campo = new Campo();
							campo.setId(vcrit_adj[0]);
							campo.setValor(vcrit_adj[1]);
							criterio.setSubCodCritAdj(campo);
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
							Campo campo = new Campo();
							campo.setId(vcrit_adj[0]);
							campo.setValor(vcrit_adj[1]);
							criterio.setExpresionCalc(campo);
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
					if(StringUtils.isNotEmpty(datosLic.getString("TEXTO_VALOR"))){
						criterio.setLicitadorIntroducevalor(new Boolean(true));
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
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
	}

	@Deprecated
	public static Peticion getPeticion(IRuleContext rulectx) throws ISPACRuleException {
		return getPeticion(rulectx.getClientContext(), rulectx.getNumExp());
	}
	
	public static Peticion getPeticion(IClientContext cct, String numexp) throws ISPACRuleException {
		Peticion peticion = new Peticion();
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			
			//Importe de la petición
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
			IItemCollection itColPeticion = entitiesAPI.queryEntities("CONTRATACION_PETICION", strQuery);
			Iterator<?> iPeticion = itColPeticion.iterator();			
			if(iPeticion.hasNext()){
				IItem itemPeticion = (IItem) iPeticion.next();
				peticion.setPresupuestoConIva(itemPeticion.getString("TOTAL"));
				peticion.setPresupuestoSinIva(itemPeticion.getString("PRESUPUESTO"));
				peticion.setObjetoContrato(itemPeticion.getString("MOTIVO_PETICION"));
			}
			
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
			
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
		return peticion;
	}
	
	@Deprecated
	public static DatosContrato getDatosContrato (IRuleContext rulectx, String numexp) throws ISPACRuleException{
		return getDatosContrato(rulectx.getClientContext(), numexp);
	}
		
	public static DatosContrato getDatosContrato (IClientContext cct, String numexp) throws ISPACRuleException{
		DatosContrato datosContrato = new DatosContrato();
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			String strQuery = "WHERE NUMEXP='" + numexp + "'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", strQuery);
			Iterator<?> it = collection.iterator();
			
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
				String lugarEjecucionContrato="";
				if(iDatosContrato.getString("PROVINCIA_CONTRATO")!=null)lugarEjecucionContrato = iDatosContrato.getString("PROVINCIA_CONTRATO");
				LOGGER.warn("lugarEjecucionContrato "+lugarEjecucionContrato);
				String [] vlugarEjecucionContrato = lugarEjecucionContrato.split(" - ");
				if(vlugarEjecucionContrato.length >1){
					Campo campo = new Campo();
					campo.setId(vlugarEjecucionContrato[0]);
					campo.setValor(vlugarEjecucionContrato[1]);
					datosContrato.setProvinciaContrato(campo);
				}
				
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
					Campo campo = new Campo();
					campo.setId(vsTipoProc[0]);
					campo.setValor(vsTipoProc[1]);
					datosContrato.setProcedimientoContratacion(campo);
				}
				
				// Tipo de contrato
				String tipoContratoBD = "";
				if(iDatosContrato.getString("TIPO_CONTRATO")!=null)tipoContratoBD=iDatosContrato.getString("TIPO_CONTRATO");
				String [] vTipoContrato = tipoContratoBD.split(" - ");
				if(vTipoContrato.length >1){
					Campo campo = new Campo();
					campo.setId(vTipoContrato[0]);
					campo.setValor(vTipoContrato[1]);
					datosContrato.setTipoContrato(campo);
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
					Campo campo = new Campo();
					campo.setId(vsUBTipoContrato[0]);
					campo.setValor(vsUBTipoContrato[1]);
					datosContrato.setSubTipoContrato(campo);
				}
				
				//Tipo de tramitacion en un procedimiento de licitacion
				String formaTramitacion = "";
				if(iDatosContrato.getString("FORMA_TRAMITACION")!=null)formaTramitacion=iDatosContrato.getString("FORMA_TRAMITACION");
				String [] vsFormaTramitacion= formaTramitacion.split(" - ");
				if(vsFormaTramitacion.length >1){
					Campo campo = new Campo();
					campo.setId(vsFormaTramitacion[0]);
					campo.setValor(vsFormaTramitacion[1]);
					datosContrato.setTipoTramitacion(campo);
				}

				//Tramitacion Gasto
				String tramitacionGasto = "";
				if(iDatosContrato.getString("TRAM_GASTO")!=null)tramitacionGasto=iDatosContrato.getString("TRAM_GASTO");
				String [] vsTramitacionGasto= tramitacionGasto.split(" - ");
				if(vsTramitacionGasto.length >1){
					Campo campo = new Campo();
					campo.setId(vsTramitacionGasto[0]);
					campo.setValor(vsTramitacionGasto[1]);
					datosContrato.setTramitacionGasto(campo);
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
								Campo campo = new Campo();
								campo.setId(vCpv[0]);
								campo.setValor(vCpv[1]);
								cpv[i] = campo;
							}
			        	}
		        		datosContrato.setCpv(cpv);
		        	}
		        	
		      	}
			}
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (SQLException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}
		
		return datosContrato;
	}
	
	@Deprecated
	public static String[] getDatosContratoPresupuesto (IRuleContext rulectx) throws ISPACRuleException{
		return getDatosContratoPresupuesto(rulectx.getClientContext(), rulectx.getNumExp());
	}

	public static String[] getDatosContratoPresupuesto (IClientContext cct, String numexp) throws ISPACRuleException{

		String[] datosContratoPres = new String[2];
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", strQuery);
			Iterator<?> it = collection.iterator();
			if (it.hasNext()) {
				IItem iDatosContrato = (IItem) it.next();
				
				//presupuesto con impuestos
				if(iDatosContrato.getString("PRESUPUESTOCONIMPUESTO")!=null)datosContratoPres[0] = iDatosContrato.getString("PRESUPUESTOCONIMPUESTO");
				//presupuesto con impuestos
				if(iDatosContrato.getString("PRESUPUESTOSINIMPUESTO")!=null)datosContratoPres[1] = iDatosContrato.getString("PRESUPUESTOSINIMPUESTO");
				
			}
			
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
			
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} 
		
		return datosContratoPres;
	}
	
	@Deprecated
	public static DatosTramitacion getDatosTramitacion(IRuleContext rulectx, String numexp) throws ISPACException {
		return getDatosTramitacion(rulectx.getClientContext(), numexp, rulectx.getItem());
	}
		
	public static DatosTramitacion getDatosTramitacion(IClientContext cct, String numexp, IItem datosTram) throws ISPACException {

		DatosTramitacion datosTramitacion = null;
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			IItem iDatosBBDDTramin = null;
			
			String strQuery = "WHERE NUMEXP='" + numexp + "'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_TRAMIT", strQuery);
			Iterator<?> it = collection.iterator();
			if (it.hasNext()) {
				iDatosBBDDTramin = (IItem) it.next();
			}

			if (datosTram!=null || iDatosBBDDTramin!=null) {
				datosTramitacion = new DatosTramitacion();
				
				//INICIO Periodo de presentacion de ofertas
				/************************************************/
			    Periodo periodoPrestacionOfertas = null;
			    Calendar startCalendar=null;
			    if(datosTram !=null && datosTram.getDate("F_INICIO_PRES_PROP")!=null){
			    	startCalendar=Calendar.getInstance();
			    	Date fInicio = datosTram.getDate("F_INICIO_PRES_PROP");
			    	startCalendar.setTime(fInicio);
			    	DateFormat formatoHora = new SimpleDateFormat("HH:mm");
			    	String hora = formatoHora.format(fInicio);
			    	LOGGER.warn("hora "+hora);
		        	String [] vHora = hora.split(":"); 
		        	if(vHora.length>0){
		        		startCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(vHora[0]));
		        		startCalendar.set(Calendar.MINUTE, Integer.parseInt(vHora[1]));
		        	}		        	
		        	LOGGER.warn("calendar "+startCalendar.getTime());
			    
			    } else{
			    	if (iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_INICIO_PRES_PROP")!=null){
			    		startCalendar=Calendar.getInstance();
			    		Date fInicio = iDatosBBDDTramin.getDate("F_INICIO_PRES_PROP");
			    		startCalendar.setTime(fInicio);
				    	DateFormat formatoHora = new SimpleDateFormat("HH:mm");
				    	String hora = formatoHora.format(fInicio);
				    	LOGGER.warn("hora "+hora);
			        	String [] vHora = hora.split(":"); 
			        	if(vHora.length>0){
			        		startCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(vHora[0]));
			        		startCalendar.set(Calendar.MINUTE, Integer.parseInt(vHora[1]));
			        	}			        	
			        	LOGGER.warn("calendar "+startCalendar.getTime());
			    	}
			    }
			    
			    if (startCalendar!=null){
			    	if(periodoPrestacionOfertas==null){
			    		periodoPrestacionOfertas = new Periodo();
			    	}
			    	periodoPrestacionOfertas.setStartCalendar(startCalendar);
			    }
			    /************************************************/
			    Calendar dateEndDateperiodoPrestacionOfertas = null;
			    if(datosTram !=null && datosTram.getDate("F_FIN_PRES_PROP")!=null){
			    	dateEndDateperiodoPrestacionOfertas = Calendar.getInstance();
			    	Date fInicio = datosTram.getDate("F_FIN_PRES_PROP");
			    	dateEndDateperiodoPrestacionOfertas.setTime(fInicio);
			    	DateFormat formatoHora = new SimpleDateFormat("HH:mm");
			    	String hora = formatoHora.format(fInicio);
			    	LOGGER.warn("hora "+hora);
		        	String [] vHora = hora.split(":"); 
		        	if(vHora.length>0){
		        		dateEndDateperiodoPrestacionOfertas.set(Calendar.HOUR_OF_DAY, Integer.parseInt(vHora[0]));
		        		dateEndDateperiodoPrestacionOfertas.set(Calendar.MINUTE, Integer.parseInt(vHora[1]));
		        	}			        	
		        	LOGGER.warn("calendar "+dateEndDateperiodoPrestacionOfertas.getTime());
			    	
			    	/*dateEndDateperiodoPrestacionOfertas.setTime(datosTram.getDate("F_FIN_PRES_PROP"));
			    	dateEndDateperiodoPrestacionOfertas.set(Calendar.HOUR, datosTram.getDate("F_FIN_PRES_PROP").getHours());
			    	dateEndDateperiodoPrestacionOfertas.set(Calendar.MINUTE,  datosTram.getDate("F_FIN_PRES_PROP").getMinutes());*/
			    }
			    else{
			    	 if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_FIN_PRES_PROP")!=null){
			    		dateEndDateperiodoPrestacionOfertas = Calendar.getInstance();
			    	    Date fInicio = iDatosBBDDTramin.getDate("F_FIN_PRES_PROP");
				    	dateEndDateperiodoPrestacionOfertas.setTime(fInicio);
				    	DateFormat formatoHora = new SimpleDateFormat("HH:mm");
				    	String hora = formatoHora.format(fInicio);
				    	LOGGER.warn("hora "+hora);
			        	String [] vHora = hora.split(":"); 
			        	if(vHora.length>0){
			        		dateEndDateperiodoPrestacionOfertas.set(Calendar.HOUR_OF_DAY, Integer.parseInt(vHora[0]));
			        		dateEndDateperiodoPrestacionOfertas.set(Calendar.MINUTE, Integer.parseInt(vHora[1]));
			        	}			        	
			        	LOGGER.warn("calendar "+dateEndDateperiodoPrestacionOfertas.getTime());
			    		 
			    		 
			    		 /*dateEndDateperiodoPrestacionOfertas.setTime(iDatosBBDDTramin.getDate("F_FIN_PRES_PROP"));
			    		 dateEndDateperiodoPrestacionOfertas.set(Calendar.HOUR_OF_DAY, iDatosBBDDTramin.getDate("F_FIN_PRES_PROP").getHours());
					     dateEndDateperiodoPrestacionOfertas.set(Calendar.MINUTE,  iDatosBBDDTramin.getDate("F_FIN_PRES_PROP").getMinutes());*/
			    	 }
			    }
			    if(dateEndDateperiodoPrestacionOfertas!=null){
			    	if(periodoPrestacionOfertas==null){
			    		periodoPrestacionOfertas = new Periodo();
			    	}
			    	periodoPrestacionOfertas.setEndCalendar(dateEndDateperiodoPrestacionOfertas);
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
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						periodoPrestacionOfertas.setDurationMeasure(campo);
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
			    Calendar dApertura = null;
			    if(datosTram !=null && datosTram.getDate("F_APERT_PROPOS")!=null){
			    	dApertura = Calendar.getInstance();
			    	dApertura.setTime(datosTram.getDate("F_APERT_PROPOS"));
			    }
			    else{
			    	if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("F_APERT_PROPOS")!=null){
			    		dApertura = Calendar.getInstance();
			    		dApertura.setTime(iDatosBBDDTramin.getDate("F_APERT_PROPOS"));
				    }
			    }
			    if(dApertura!=null){
				    datosTramitacion.setFechaAperturaProposiones(dApertura);
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
			    	Calendar fechaAprobacionProyecto = Calendar.getInstance();
			    	fechaAprobacionProyecto.setTime(fAprobacion);
			    	datosTramitacion.setFechaAprobacionProyecto(fechaAprobacionProyecto);
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
			    	Calendar fechaAprobacionExpedienteContratacion = Calendar.getInstance();
			    	fechaAprobacionExpedienteContratacion.setTime(fechapro);
			    	datosTramitacion.setFechaAprobacionExpedienteContratacion(fechaAprobacionExpedienteContratacion);
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
			    	Calendar fechaBOPExpCont = Calendar.getInstance();
			    	fechaBOPExpCont.setTime(fechaBOP);
			    	datosTramitacion.setFechaBOPExpCont(fechaBOPExpCont);
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
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						duracionContrato.setDurationMeasure(campo);
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
				Calendar fechaAdj = null;
				if(datosTram !=null && datosTram.getDate("FECHA_ADJUDICACION")!=null){
					fechaAdj = Calendar.getInstance();
					fechaAdj.setTime(datosTram.getDate("FECHA_ADJUDICACION"));
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("FECHA_ADJUDICACION")!=null){
						fechaAdj = Calendar.getInstance();
						fechaAdj.setTime(iDatosBBDDTramin.getDate("FECHA_ADJUDICACION"));
					}
				}
				if(fechaAdj != null){
					licitador.setFechaAdjudicacion(fechaAdj);
				}
				/************************************************/
				Calendar fechaForm = null;
				if(datosTram !=null && datosTram.getDate("FECHA_FIN_FORMALIZACION")!=null){
					fechaForm = Calendar.getInstance();
					fechaForm.setTime(datosTram.getDate("FECHA_FIN_FORMALIZACION"));
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("FECHA_FIN_FORMALIZACION")!=null){
						fechaForm = Calendar.getInstance();
						fechaForm.setTime(iDatosBBDDTramin.getDate("FECHA_FIN_FORMALIZACION"));
					}
				}
				if(fechaForm != null){
					licitador.setFechaFinFormalizacion(fechaForm);
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
				String pais = null;
				if(datosTram !=null && datosTram.getString("PAIS_ADJ")!=null){
					pais = datosTram.getString("PAIS_ADJ");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("PAIS_ADJ")!=null){
						pais = iDatosBBDDTramin.getString("PAIS_ADJ");
					}
				}
				if(StringUtils.isNotEmpty(pais)){
					String [] vcritSolv = pais.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						licitador.setPais(campo);
					}
				}
				/************************************************/
				String nuts = null;
				if(datosTram !=null && datosTram.getString("NUTS_ADJ")!=null){
					nuts = datosTram.getString("NUTS_ADJ");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("NUTS_ADJ")!=null){
						nuts = iDatosBBDDTramin.getString("NUTS_ADJ");
					}
				}
				if(StringUtils.isNotEmpty(nuts)){
					String [] vcritSolv = nuts.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						licitador.setNUTS(campo);
					}
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
				String numero = null;
				if(datosTram !=null && datosTram.getString("NUM_CALLE_ADJ")!=null){
					numero = datosTram.getString("NUM_CALLE_ADJ");
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getString("NUM_CALLE_ADJ")!=null){
						numero = iDatosBBDDTramin.getString("NUM_CALLE_ADJ");
					}
				}
				if(cp != null){
					licitador.setNumeroVia(numero);
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
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						licitador.setJustificacionProceso(campo);
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
					Calendar fechaContrato = Calendar.getInstance();
					fechaContrato.setTime(fContrato);
					formalizacion.setFechaContrato(fechaContrato);
				}
				/************************************************/
				Calendar fInicioCont = null;
				if(datosTram !=null && datosTram.getDate("FECHA_INICIO_CONTRATO")!=null){
					fInicioCont = Calendar.getInstance();
					fInicioCont.setTime(datosTram.getDate("FECHA_INICIO_CONTRATO"));
				}
				else{
					if(iDatosBBDDTramin!=null && iDatosBBDDTramin.getDate("FECHA_INICIO_CONTRATO")!=null){
						fInicioCont = Calendar.getInstance();
						fInicioCont.setTime(iDatosBBDDTramin.getDate("FECHA_INICIO_CONTRATO"));
					}
				}
				if(fInicioCont!=null){
					formalizacion.setPeriodoValidezInicioContrato(fInicioCont);
					if(duracionContrato==null){
						duracionContrato = new DuracionContratoBean();
					}
					duracionContrato.setFechaInicio(fInicioCont);
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
					Calendar periodoValidezFinContrato = Calendar.getInstance();
					periodoValidezFinContrato.setTime(fFinContrato);
					formalizacion.setPeriodoValidezFinContrato(periodoValidezFinContrato);
					if(duracionContrato==null){
						duracionContrato = new DuracionContratoBean();
					}
					duracionContrato.setFechaFinal(periodoValidezFinContrato);
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
		} catch(ISPACRuleException e){
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. " + numexp + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Expediente. " + numexp + " - " + e.getMessage(), e);
		}

		return datosTramitacion;
	}
	
//	@SuppressWarnings("unchecked")
//	public static void imprimeResultadoAnalisisPrevio(Resultado resultadoAnalisisPrevio, IRuleContext rulectx, String nombreDoc, String nombrePeticion) throws ISPACException {
//		
//		File ficheroAnalisisPrevio = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/informacion.pdf");
//		
//		try {
//			
//			/************************************************************************/
//			ClientContext cct = (ClientContext) rulectx.getClientContext();
//			IInvesflowAPI invesFlowAPI = cct.getAPI();
//			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
//			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
//			Object connectorSession = genDocAPI.createConnectorSession();
//			 /***********************************************************************/
//			
//			
//			// A partir del objeto File creamos el fichero
//			// físicamente
//			if (ficheroAnalisisPrevio.createNewFile()) {
//				
//				LOGGER.warn("El fichero se ha creado correctamente");
//				Document documentResultado = new Document();
//				PdfCopy.getInstance(documentResultado,new FileOutputStream(ficheroAnalisisPrevio));
//				documentResultado.open();
//				imprimedoc(resultadoAnalisisPrevio.getPublicacion(), documentResultado, nombrePeticion, rulectx);
//				
//				documentResultado.add(new Phrase("\n"));
//				documentResultado.add(new Phrase("****************RESULTADO VISUALIZACION******************"));
//				documentResultado.add(new Phrase("\n\n"));
//				documentResultado.add(new Phrase("Resultado código: "+ resultadoAnalisisPrevio.getVisualizacion().getResultCode()));
//				documentResultado.add(new Phrase("\n"));
//				documentResultado.add(new Phrase("Descripción del error: "+ resultadoAnalisisPrevio.getVisualizacion().getResultCodeDescription()));
//				documentResultado.add(new Phrase("\n"));
//				documentResultado.add(new Phrase("Detalle del error: "+ resultadoAnalisisPrevio.getVisualizacion().getViewErrorDetails()));
//				documentResultado.add(new Phrase("\n"));
//				documentResultado.close();
//				
//				//Genero el doc en SIGEM
//				if (ficheroAnalisisPrevio != null) {
//					IItemCollection tipDoc = entitiesAPI.queryEntities("SPAC_CT_TPDOC","WHERE NOMBRE='"+nombreDoc+"'");
//					Iterator<IItem> tipDocIterator = tipDoc.iterator();
//					int idTipDoc = 0;
//					if (tipDocIterator.hasNext())
//						idTipDoc  = tipDocIterator.next().getInt("ID");
//					IItem entityDocument = genDocAPI.createTaskDocument(rulectx.getTaskId(), idTipDoc);
//					entityDocument.set("EXTENSION", Constants._EXTENSION_PDF);
//					entityDocument.set("DESCRIPCION",nombreDoc);
//					entityDocument.store(cct);
//					cct.endTX(true);
//					int documentIdComparece = entityDocument.getKeyInt();
//					InputStream inError = new FileInputStream(ficheroAnalisisPrevio);
//
//					genDocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), documentIdComparece, inError,
//							(int) ficheroAnalisisPrevio.length(), "application/pdf", nombreDoc+" "+nombrePeticion);
//					
//					inError.close();
//					
//					
//				}
//			}
//			
//			
//		}catch(ISPACRuleException e){
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (FileNotFoundException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (IOException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (DocumentException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (ISPACException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		}
//		finally{
//			ficheroAnalisisPrevio.delete();
//		}
//		
//	}
	
//	public static void imprimelogsVisualizacion(VisualizationResult docResultado) {
//		LOGGER.warn("*********************************************RESULTADO VISUALIZACION**************************************************");
//		LOGGER.warn("Resultado código: "+ docResultado.getResultCode());
//		LOGGER.warn("Descripción del error: "+ docResultado.getResultCodeDescription());
//		LOGGER.warn("Detalle del error: "+ docResultado.getViewErrorDetails());
//	}
	
//	@SuppressWarnings("unchecked")
//	public static void imprimedoc(PublicationResult result, Document documentErrorComp, String nombrePublicacion, IRuleContext rulectx) throws ISPACRuleException{
//		try {
//			
//			/************************************************************************/
//			ClientContext cct = (ClientContext) rulectx.getClientContext();
//			IInvesflowAPI invesFlowAPI = cct.getAPI();
//			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
//			 /***********************************************************************/
//			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
//			String imagePath = rutaImg+"/logoCabecera.png";
//			File logoURL = new File(imagePath);
//			if (logoURL != null) {
//				Image logo = Image.getInstance(imagePath);
//				documentErrorComp.add(logo);
//			}
//
//			if(!result.getResultCode().equals("OK") && !result.getResultCodeDescription().equals("OK")){
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("ERRORES EN LA PLATAFORMA DE CONTRATACIÓN"));
//				
//			}
//			
//			documentErrorComp.add(new Phrase("\n"));
//			documentErrorComp.add(new Phrase("\n"));
//			
//			documentErrorComp.add(new Phrase("*************************RESULTADO DE LA PETICIÓN************************************************"));
//			if(StringUtils.isNotEmpty(result.getExpedientNumber())){
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("*Número de expediente de la licitación: "+ result.getExpedientNumber()));
//			}
//			
//			if(StringUtils.isNotEmpty(result.getResultCode())){
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("*Código de resultado de la publicación: "+ result.getResultCode()));
//			}
//			
//			
//			if(StringUtils.isNotEmpty(result.getResultCodeDescription())){
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("*Descripción del código de Resultado: "+ result.getResultCodeDescription()));
//			}
//			
//			if(StringUtils.isNotEmpty(result.getState())){
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("*Estado de la Licitación: " + result.getState()));
//			}
//			
//			if(StringUtils.isNotEmpty(result.getNoticeURL())){
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("*Visualización del detalle del expediente en que ha sido publicado: "+ result.getNoticeURL()));
//				documentErrorComp.add(new Phrase("\n"));
//			}
//			
//			
//			PublishErrorDetails errorres = result.getPublishErrorDetails();
//			if(errorres!=null){
//				Mensaje[] smsError = errorres.getMessage();
//				for(int i=0; i < smsError.length; i++){
//					documentErrorComp.add(new Phrase("**********ERROR "+i+"***********"));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("** "+smsError[i].getText()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("* "+smsError[i].getContext()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("* "+smsError[i].getLocation()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("* "+smsError[i].getRuta()));
//					documentErrorComp.add(new Phrase("\n"));
//				}
//			}			
//			OfficialPublicationResults resPublicaciones = result.getOfficialPublicationResults();
//			if (resPublicaciones != null) {
//				documentErrorComp.add(new Phrase("--------PUBLICACIONES---------"));
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("*Resultado de Publicaciones en Diarios Oficiales."));
//				documentErrorComp.add(new Phrase("\n"));
//				OfficialPublicationResult[] listpubliOficiales = resPublicaciones.getOfficialPublicationResult();
//				for (int i=0; i< listpubliOficiales.length; i++) {
//					documentErrorComp.add(new Phrase("**********ERROR "+i+"***********"));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("Diario Oficial donde se ha detectado el error: "+ listpubliOficiales[i].getPublishAgency()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("Codigo del error: "+ listpubliOficiales[i].getReturnCode()));
//					documentErrorComp.add(new Phrase("\n"));
//					
//					//Compruebo que se haya publicado el Anuncio de Licitación en el DOUE, para insertarlo en la tabla 'CONTRATACION_DOUE'
//					if(nombrePublicacion.equals("Anuncio de Licitación")){
//						if(listpubliOficiales[i].getPublishAgency().contains("DOUE")){
//							if(listpubliOficiales[i].getReturnCode().contains("OK") || listpubliOficiales[i].getReturnCode().contains("PEND_DOUE")){
//								//Actualizar el estado de la publicación
//								String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
//								IItemCollection collectionDoue = entitiesAPI.queryEntities("CONTRATACION_DOUE", strQuery);
//								Iterator<IItem> itDoue = collectionDoue.iterator();								
//								if (itDoue.hasNext()) {									
//									IItem iDoue = itDoue.next();
//									iDoue.set("PUBLICADOANUNCIOLICITACION", "SI");
//									iDoue.store(cct);
//								}
//							}
//						}
//					}
//					
//					documentErrorComp.add(new Phrase("Fecha de Publicacion: "+ listpubliOficiales[i].getPublishDate()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("URL de publicacion: "+ listpubliOficiales[i].getPublishURL()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("URL que envía el BOE para completar la publicación en dicho diario: "+ listpubliOficiales[i].getBoeConfirmationURL()));
//					documentErrorComp.add(new Phrase("\n"));
//				}
//	
//			}
//			
//			PublishErrorDetails publishErrorDetails = result.getPublishErrorDetails();
//			if (publishErrorDetails != null) {
//				documentErrorComp.add(new Phrase("\n"));
//				documentErrorComp.add(new Phrase("*Detalle Errores de Publicacion"));
//				documentErrorComp.add(new Phrase("\n"));
//				Mensaje [] listMessage = publishErrorDetails.getMessage();
//				for (int i = 0; i < listMessage.length; i++) {
//					documentErrorComp.add(new Phrase("**********ERROR "+i+"***********"));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("Id error: " + listMessage[i].getId()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("Explicacion error: " + listMessage[i].getText()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("Localicación del ERROR: "+ listMessage[i].getLocation()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("Validación x la que se produce el error: "+ listMessage[i].getContext()));
//					documentErrorComp.add(new Phrase("\n"));
//					documentErrorComp.add(new Phrase("Xpath que referencia al componente donde se ha producido el error: "+ listMessage[i].getRuta()));
//					documentErrorComp.add(new Phrase("\n"));
//				}
//			}
//
//		} catch (DocumentException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (MalformedURLException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (IOException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (ISPACException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		}
//		
//	}
//	
//	public static void imprimelogs(PublicationResult result) {
//		LOGGER.warn("*************************RESULTADO DE LA PETICIÓN************************************************");
//		LOGGER.warn("*Número de expediente de la licitación: "+ result.getExpedientNumber());
//		LOGGER.warn("*Código de resultado de la publicación: "+ result.getResultCode());
//		LOGGER.warn("*Descripción del código de Resultado: "+ result.getResultCodeDescription());
//		LOGGER.warn("*Estado de la Licitación: " + result.getState());
//		LOGGER.warn("*Visualización del detalle del expediente en que ha sido publicado: "+ result.getNoticeURL());
//		
//		PublishErrorDetails errorres = result.getPublishErrorDetails();
//		if(errorres!=null){
//			Mensaje[] smsError = errorres.getMessage();
//			for(int i=0; i < smsError.length; i++){
//				LOGGER.warn("**********ERROR***********");
//				LOGGER.warn("* "+smsError[i].getContext());
//				LOGGER.warn("* "+smsError[i].getText());
//				LOGGER.warn("* "+smsError[i].getLocation());
//				LOGGER.warn("* "+smsError[i].getRuta());
//			}
//		}
//		
//
//		LOGGER.warn("--------PUBLICACIONES---------");
//		LOGGER.warn("*Resultado de Publicaciones en Diarios Oficiales.");
//		OfficialPublicationResults resPublicaciones = result.getOfficialPublicationResults();
//		if (resPublicaciones != null) {
//			OfficialPublicationResult[] listpubliOficiales = resPublicaciones.getOfficialPublicationResult();
//			for (int i=0; i< listpubliOficiales.length; i++) {
//
//				LOGGER.warn("Diario Oficial donde se ha detectado el error: "
//						+ listpubliOficiales[i].getPublishAgency());
//				LOGGER.warn("Codigo del error: "
//						+ listpubliOficiales[i].getReturnCode());
//				LOGGER.warn("Fecha de Publicacion: "
//						+ listpubliOficiales[i].getPublishDate());
//				LOGGER.warn("URL de publicacion: "
//						+ listpubliOficiales[i].getPublishURL());
//				LOGGER.warn("URL que envía el BOE para completar la publicación en dicho diario: "
//						+ listpubliOficiales[i].getBoeConfirmationURL());
//			}
//
//		}
//
//		LOGGER.warn("*Detalle Errores de Publicacion");
//		PublishErrorDetails publishErrorDetails = result.getPublishErrorDetails();
//		if (publishErrorDetails != null) {
//			Mensaje [] listMessage = publishErrorDetails.getMessage();
//			for (int i = 0; i < listMessage.length; i++) {
//				LOGGER.warn("Id error: " + listMessage[i].getId());
//				LOGGER.warn("Explicacion error: " + listMessage[i].getText());
//				LOGGER.warn("Localicación del ERROR: "
//						+ listMessage[i].getLocation());
//				LOGGER.warn("Validación x la que se produce el error: "
//						+ listMessage[i].getContext());
//				LOGGER.warn("Xpath que referencia al componente donde se ha producido el error: "
//						+ listMessage[i].getRuta());
//			}
//		}
//		LOGGER.warn("* FIN Detalle Errores de Publicacion");
//		
//	}
//
//	public static void errorPeticion(PublicationResult result, IRuleContext rulectx, String nombrePeticion, String nombretipoDoc) throws ISPACRuleException {
//		
//		File ficheroError = null;
//		try {
//			/**
//			 * ALMACENAR LA INFORMACIÓN EN UN DOCUMENTO CON LOS ERRORES.
//			 * **/
//			ficheroError = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance()
//					.newFileName(".pdf"));
//			
//			// --------------------------------------------------------------------
//			ClientContext cct = (ClientContext) rulectx.getClientContext();
//			// --------------------------------------------------------------------
//						
//			
//			// A partir del objeto File creamos el fichero
//			// físicamente
//			if (ficheroError.createNewFile()) {
//				
//				LOGGER.warn("El fichero se ha creado correctamente");
//				Document documentErrorComp = new Document();
//				PdfCopy.getInstance(documentErrorComp,new FileOutputStream(ficheroError));
//				documentErrorComp.open();				
//				
//				DipucrFuncionesComunes.imprimelogs(result);
//				
//				DipucrFuncionesComunes.imprimedoc(result, documentErrorComp, nombrePeticion, rulectx);
//				
//				documentErrorComp.close();
//				
//				if (ficheroError != null) {
//					
//					int idTipDoc = DocumentosUtil.getTipoDoc(cct, nombretipoDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);
//					
//					DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, nombrePeticion, ficheroError, DocumentosUtil.Extensiones.PDF);
//				
//				}
//
//			} else{
//				LOGGER.warn("No ha podido ser creado el fichero");
//			}
//			
//		} catch (IOException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (DocumentException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (ISPACException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		}
//		finally{
//			if(ficheroError!=null){
//				ficheroError.delete();
//			}
//		}
//		
//	}
//
//	public static void crearDocPeticionOK_PLACE(Resultado resultadoAnalisisPrevio, IRuleContext rulectx, String nombreDoc) throws ISPACRuleException {
//		try{
//		
//			VisualizationResult docResultado =  resultadoAnalisisPrevio.getVisualizacion();
//			if(docResultado!=null){
//				
//				if (docResultado.getResultCode().equals("OK")) {
//					
//					DipucrFuncionesComunes.imprimelogsVisualizacion(docResultado);
//					
//					EmbeddedDocumentBinaryObjectType documento = docResultado.getDocument();					
//					
//					File fileAnuncio = cargaDocumentoAnuncio (documento, rulectx.getNumExp());
//					if(null!=fileAnuncio){
//						int idTipoDoc = DocumentosUtil.getTipoDoc(rulectx.getClientContext(), nombreDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);
//						
//						IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), idTipoDoc, nombreDoc, fileAnuncio, DocumentosUtil.Extensiones.PDF);
//						entityDoc.set("FFIRMA", new Date());
//						entityDoc.store(rulectx.getClientContext());
//						fileAnuncio.delete();
//						
//						DipucrFuncionesComunes.imprimeResultadoAnalisisPrevio(resultadoAnalisisPrevio,rulectx,"Información de Plataforma Contratación", nombreDoc);
//					}					
//				}
//			}
//		}  catch (ISPACException e) {
//			LOGGER.error("Error en el número de expediente. "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//			throw new ISPACRuleException("Error en el número de expediente. "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//		}
//		
//	}
	
//	private static File cargaDocumentoAnuncio(EmbeddedDocumentBinaryObjectType documento, String numexp) throws ISPACRuleException {
//		File fileAnuncio = null;		
//		try {
//			String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName(".pdf");
//			FileOutputStream fos = null;
//			fos = new FileOutputStream(rutaFileName);
//			
//			DataHandler data = documento.getValue();
//			data.writeTo(fos);
//			fos.close();
//			fos.flush();
//			
//			fileAnuncio = new File(rutaFileName);
//			
//		} catch (IOException e) {
//			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
//		} catch (ISPACException e) {
//			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
//		}		
//		return fileAnuncio;
//	}


//	@SuppressWarnings("unchecked")
//	public static void cargaAnuncioFirmado(IRuleContext rulectx, String urlAnuncio, String nombreDoc, Resultado resultadoAnuncio) throws ISPACRuleException {
//		try{
//			// --------------------------------------------------------------------
//			ClientContext cct = (ClientContext) rulectx.getClientContext();
//			IInvesflowAPI invesFlowAPI = cct.getAPI();
//			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
//			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
//			Object connectorSession = genDocAPI.createConnectorSession();
//			// --------------------------------------------------------------------
//			
//			//leo url 
//			LOGGER.warn("URL");
//			LOGGER.warn(urlAnuncio);
//			String metaDocPLACE = "";
//			URL urlmeta = new URL(urlAnuncio);
//			URLConnection conexionmeta = urlmeta.openConnection();	
//
//			conexionmeta.connect();
//			urlmeta.openStream();
//			
//			InputStream is = conexionmeta.getInputStream();
//	         BufferedReader br = new BufferedReader(new InputStreamReader(is));
//	         char[] buffer = new char[1000];
//	         int leido;
//	         String contenidoURL = "";
//	         while ((leido = br.read(buffer)) > 0) {
//	        	 contenidoURL = new String(buffer, 0, leido);
//	         }
//	         LOGGER.warn("CONTENIDO");
//	         LOGGER.warn(contenidoURL);
//	         
//	         String urlDocFinal = contenidoURL;
//	         
//	        org.jsoup.nodes.Document doc = Jsoup.parse(contenidoURL);
//	        if(doc.getElementsByTag("meta").size()>0){
//	        	metaDocPLACE = doc.getElementsByTag("meta").get(0).attr("content");
//				//leo url 
//				String direccionPLACE = "https://contrataciondelestado.es";
//				String ruta = "";
//				String[] vDirePLACE = metaDocPLACE.split("url=");
//				if(vDirePLACE!=null && vDirePLACE.length>0){
//					ruta = vDirePLACE[1];
//					//Quitar las comillas que contiene la url
//					ruta = ruta.substring(1, ruta.length()-1);
//				}
//				
//				urlDocFinal = direccionPLACE+ruta;
//	        }
//	        
//			URL url = new URL(urlDocFinal);
//			URLConnection conexion = url.openConnection();	
//
//			conexion.connect();		
//			InputStream input = new BufferedInputStream(url.openStream());
//			
//			LOGGER.warn(input);
//
//			// Anuncio de Informacion Previa
//			String strQuery = "WHERE NOMBRE = '" + nombreDoc+ "'";
//			IItemCollection collectionTPDOC = entitiesAPI.queryEntities("SPAC_CT_TPDOC",strQuery);
//			Iterator<IItem> itTPDoc = collectionTPDOC.iterator();
//			int tpdoc = 0;
//			if (itTPDoc.hasNext()) {
//				IItem tpd = itTPDoc.next();
//				tpdoc = tpd.getInt("ID");
//			}
//			int taskId = rulectx.getTaskId();
//			IItem newdoc = genDocAPI.createTaskDocument(taskId, tpdoc);
//
//			String rutaFileName = FileTemporaryManager
//					.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance()
//							.newFileName(".pdf");
//			LOGGER.warn(rutaFileName);
//			
//			byte data[] = new byte[1024];
//
////			long total = 0;
//
//			int count = 0;
//			FileOutputStream fos = new FileOutputStream(rutaFileName);;
//			while ((count = input.read(data)) != -1) {
////				total += count;
//				fos.write(data, 0, count);
//			}
//			
//			fos.close();
//			fos.flush();
//			
//
//			File file = new File(rutaFileName);
//
//			FileInputStream in = new FileInputStream(file);
//			int docId = newdoc.getInt("ID");
//			IItem entityDoc = genDocAPI.attachTaskInputStream(connectorSession, taskId, docId, in, (int) file.length(), "application/octet-stream", nombreDoc);
//			entityDoc.set("EXTENSION", "pdf");
//			entityDoc.set("FFIRMA", new Date());
//			entityDoc.store(cct);
//			file.delete();
//			
//			if(resultadoAnuncio!=null){
//				PublicationResult publicationResultLicitacion = resultadoAnuncio.getPublicacion();
//				
//				if(publicationResultLicitacion.getResultCode().equals("OK")){					
//					DipucrFuncionesComunes.imprimeResultadoAnalisisPrevio(resultadoAnuncio,rulectx,
//							"Información de Plataforma Contratación", nombreDoc);
//				}
//
//				else{
//					DipucrFuncionesComunes.errorPeticion(publicationResultLicitacion, rulectx, nombreDoc, "Error");
//				}	
//			}	
//			
//			input.close();
//			is.close();	
//			br.close();
//			
//		} catch (IOException e) {
//			LOGGER.error(" URL "+urlAnuncio);
//			LOGGER.error(e.getMessage(), e);
//			//La url necesita contraseña
//			
//			if(resultadoAnuncio!=null){
//				PublicationResult publicationResultLicitacion = resultadoAnuncio.getPublicacion();
//				
//				if(publicationResultLicitacion.getResultCode().equals("OK")){					
//					DipucrFuncionesComunes.crearDocPeticionOK_PLACE(resultadoAnuncio, rulectx, nombreDoc);
//				}
//
//				else{
//					DipucrFuncionesComunes.errorPeticion(publicationResultLicitacion, rulectx, nombreDoc, "Error");
//				}	
//			}	
//			
//		}  catch (ISPACException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		}
//		
//	}
	
//	public static File getFile(IRuleContext rulectx, String infoPag, String nombreFichero) throws ISPACException{
//		
//		// API
//		IGenDocAPI gendocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();
//
//		Object connectorSession = null;
//		try {
//			connectorSession = gendocAPI.createConnectorSession();
//			File file = null;
//			try{
//				String extension = "pdf";
//				
//				//Se almacena documento
//				//String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
//				String fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + nombreFichero +"."+ extension;
//				
//				OutputStream out = new FileOutputStream(fileName);
//				gendocAPI.getDocument(connectorSession, infoPag, out);
//								
//				file = new File(fileName);
//			
//				return file;
//			} catch (FileNotFoundException e) {
//				throw new ISPACInfo("Error al intentar obtener el documento, no existe.", e);
//			}
//		}finally {
//			if (connectorSession != null) {
//				gendocAPI.closeConnectorSession(connectorSession);
//			}
//    	}
//	}

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
		    	if(periodoPrestacionOfertas.getStartCalendar()!=null){
		    		datosTramitacion.set("F_INICIO_PRES_PROP", (periodoPrestacionOfertas.getStartCalendar().getTime()));
		    	}
		    	if(periodoPrestacionOfertas.getEndCalendar()!=null){
		    		datosTramitacion.set("F_FIN_PRES_PROP", periodoPrestacionOfertas.getEndCalendar().getTime());
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
			 LOGGER.error("Expediente. "+numexpPeticion +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+numexpPeticion +" - "+ e.getMessage(), e);
	     }
		return resultado;
	}
	
//	@SuppressWarnings("unchecked")
//	public static void envioDocumentosAdicionales(IRuleContext rulectx) throws ISPACRuleException {
//		File fichero = null;
//		try{
//			// --------------------------------------------------------------------
//				ClientContext cct = (ClientContext) rulectx.getClientContext();
//				IInvesflowAPI invesFlowAPI = cct.getAPI();
//				IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
//			// --------------------------------------------------------------------			
//			
//			PlataformaContratacionStub platContratacion = new PlataformaContratacionStub(ServiciosWebContratacionFunciones.getDireccionSW());
//			int timeout = 15 * 60 * 1000; // 3 minutos
//			platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.SO_TIMEOUT, new Integer(timeout));
//			platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(timeout));
//			
//			//Añadir la información adjunta
//			
//			es.dipucr.contratacion.services.PlataformaContratacionStub.Documento[] docAdicional = docInformacionAdicionalPliego(rulectx);
//			
//			fichero = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName(".pdf"));
//			
//			if (docAdicional!=null && docAdicional.length>0 && fichero.createNewFile()) {
//				LOGGER.warn("El fichero se ha creado correctamente");
//				Document documentInforme = new Document();
//				PdfCopy.getInstance(documentInforme,new FileOutputStream(fichero));
//				documentInforme.open();
//				
//				insertarImagenes(rulectx, documentInforme);
//				
//				documentInforme.add(new Phrase("\n"));
//
//				documentInforme.add(new Phrase("INFORME SOBRE LOS DOCUMENTOS ANEXADOS DEL EXPEDIENTE DE CONTRATACIÓN"));
//				documentInforme.add(new Phrase("\n"));
//				documentInforme.add(new Phrase("\n"));				
//				
//				if(docAdicional!=null){
//					//Documentos con informaciona adicional
//					for(int i = 0; i< docAdicional.length; i++){
//						if(docAdicional[i]!=null){
//							
//							documentInforme.add(new Phrase(" - DOCUMENTO "+ i + "- Nombre: "+docAdicional[i].getNameDoc()));
//							documentInforme.add(new Phrase("\n"));
//							
//							//Envio de la petición de pliego
//							try {
//								String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
//								
//								//Petición
//								//String publishedByUser = UsuariosUtil.getDni(cct);
//								//String publishedByUser = "99001215S";
//								String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
//								if(publishedByUser==null || publishedByUser.equals("")){
//									publishedByUser = UsuariosUtil.getDni(cct);
//								}
//								EnvioOtrosDocumentosLicitacion envioOtrosDocumentosLicitacion = new EnvioOtrosDocumentosLicitacion ();
//								es.dipucr.contratacion.services.PlataformaContratacionStub.Documento param = docAdicional[i];
//								envioOtrosDocumentosLicitacion.setDocumentoAdicional(param);;
//								envioOtrosDocumentosLicitacion.setEntidad(entidad);
//								envioOtrosDocumentosLicitacion.setPublishedByUser(publishedByUser);
//								DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
//								envioOtrosDocumentosLicitacion.setOrganoContratacion(datContrato.getOrganoContratacion());
//								EnvioOtrosDocumentosLicitacionResponse envioOtrosDocumentosLicitacionResponse = platContratacion.envioOtrosDocumentosLicitacion(envioOtrosDocumentosLicitacion);
//								
//								Resultado resultadoAnalisisPrevio = envioOtrosDocumentosLicitacionResponse.get_return();
//								
//								PublicationResult result = resultadoAnalisisPrevio.getPublicacion();
//								
//								if(result!=null){
//									documentInforme.add(new Phrase("Código. "+result.getResultCode()+" - "+result.getResultCodeDescription()));
//									documentInforme.add(new Phrase("\n"));
//									if(result.getResultCode()!=null){
//										LOGGER.warn("url. "+result.getNoticeURL());
//										if(result.getResultCode().equals("OK")){					
//											//DipucrFuncionesComunes.crearDocPeticionOK_PLACE(resultadoAnalisisPrevio, rulectx, "Anuncio de Pliego");
//											LOGGER.warn("Documento anexado "+docAdicional[i].getDescripcion());
//										}						
//										else{
//											LOGGER.error("ERROR: Documento NO anexado "+docAdicional[i].getDescripcion());
//										}
//									}
//									else{
//										LOGGER.error("ERROR: Documento NO anexado "+docAdicional[i].getDescripcion());
//									}
//									
//								}
//								else{
//									documentInforme.add(new Phrase("Error al enviar el documento por los servicios web. "+docAdicional[i].getNameDoc()));
//									documentInforme.add(new Phrase("\n"));
//									LOGGER.error("ERROR: Documento NO anexado "+docAdicional[i].getDescripcion());
//								}
//								
//					
//							} catch (RemoteException e) {
//								LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//								throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//							} catch (ISPACException e) {
//								LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//								throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//							} catch (PlataformaContratacionDatatypeConfigurationExceptionException e) {
//								LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//								throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//							} catch (PlataformaContratacionJAXBExceptionException e) {
//								LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//								throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//							} catch (PlataformaContratacionUnsupportedEncodingExceptionException e) {
//								LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//								throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//							} catch (PlataformaContratacionMalformedURLExceptionException e) {
//								LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//								throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//							} 
//
//						}
//					}
//					
//					documentInforme.close();
//					
//					if (fichero != null) {
//						IItemCollection tipDoc = entitiesAPI.queryEntities("SPAC_CT_TPDOC","WHERE NOMBRE='Información de Plataforma Contratación'");
//						Iterator<IItem> tipDocIterator = tipDoc.iterator();
//						int idTipDoc = 0;
//						if (tipDocIterator.hasNext())
//							idTipDoc  = tipDocIterator.next().getInt("ID");
//						
//						DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, "Informe documentos expedientes", fichero, Constants._EXTENSION_PDF);
//					}
//				}
//			}
//
//		}
//		catch(ISPACRuleException e){
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (ISPACException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (FileNotFoundException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (DocumentException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		} catch (IOException e) {
//			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
//		}
//		finally{
//			if(fichero!=null){
//				fichero.delete();
//			}
//		}
//	}
		
		public static void insertarImagenes(IRuleContext rulectx, Document documentInforme) throws ISPACRuleException {
			String entidad = EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext());
			
			String imageLogoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_LOGO_PATH_DIPUCR);
			String imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_FONDO_PATH_DIPUCR);
//			String imagePiePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_PIE_PATH_DIPUCR);

			try {
				File logoURL = new File(imageLogoPath);
				if (logoURL != null) {
					Image logo = Image.getInstance(imageLogoPath);
					logo.scalePercent(50);
					documentInforme.add(logo);
				}
				
				File fondoURL = new File(imageFondoPath);
				if(fondoURL != null){
					Image fondo = Image.getInstance(imageFondoPath);
					fondo.setAbsolutePosition(250, 50);
					fondo.scalePercent(70);
					documentInforme.add(fondo);
					
				}

				//TODO: Es imagen fija, no pie, y el texto se superpone
//				File pieURL = new File(imagePiePath);
//				if(pieURL != null){
//					Image pie = Image.getInstance(imagePiePath);
//					pie.setAbsolutePosition(documentInforme.getPageSize().getWidth() - 550, 15);
//					pie.scalePercent(80);
//					documentInforme.add(pie);
//				}
			} catch (DocumentException e) {
				LOGGER.error("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			} catch (MalformedURLException e) {
				LOGGER.error("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			} catch (IOException e) {
				LOGGER.error("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al insertarImagenes. "+rulectx.getNumExp()+ "  - "+e.getMessage(), e);
			}
		}
	
	public static Documento[] docInformacionAdicionalPliego(IRuleContext rulectx) throws ISPACRuleException {
		
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
	        Iterator<?> itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = (IItem) itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        	
	        	IItem exp = entitiesAPI.getExpedient(numexpPetCont);
	        	
	        	//codigo del trámite -> inf-nec-cont-adm -> Informe razonado de la necesidad del contrato administrativo
	        	IItem tramite = TramitesUtil.getTramiteByCode(cct, rulectx.getNumExp(), "inf-nec-cont-adm");
	        	String nombreTramite = "";
	        	String queryNombre = "";
	        	// Para que funcione con el procedimiento de Peticion de contratación y Tramitación de Contrato 
	        	if(StringUtils.isNotEmpty(tramite.getString("NOMBRE"))){
	        		nombreTramite = tramite.getString("NOMBRE");
	        		queryNombre = "(NOMBRE='Informe Necesidad Contrato' OR NOMBRE='"+nombreTramite+"')";
	        	}
	        	else{
	        		queryNombre = "(NOMBRE = 'Informe Necesidad Contrato')";
	        	}
	        	
	        	//obtengo el id_tramite y id_fase
				String strQuery = "WHERE "+queryNombre+" AND ID_PCD="+exp.getInt("ID_PCD");
		        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
		        
				Iterator <?> it = collection.iterator();
		        if (it.hasNext())
		        {
		        	IItem doc = (IItem) it.next();
		        	int idFase = doc.getInt("ID_FASE");
		        	String idTtramiteBpm = doc.getString("ID_TRAMITE_BPM");
		        	
		        	String query = "NUMEXP='"+numexpPetCont+"' AND ID_FASE_PCD="+idFase+" AND ID_TRAMITE_PCD="+idTtramiteBpm+" AND NOMBRE NOT LIKE '%Informe Necesidad Contrato%' AND NOMBRE!='Pliego de Prescripciones Técnicas'";
		        	LOGGER.warn("query "+query);
		 			IItemCollection docsCollection = entitiesAPI.getDocuments(numexpPetCont, query, "FDOC" + DESC);
		 			
		 			Iterator <?> docIterator = docsCollection.iterator();
		 			docAdicional = new Documento [docsCollection.toList().size()];
		 			int i = 0;
					while(docIterator.hasNext()){
						
						IItem docPres = (IItem) docIterator.next();
						
						String descripcion = "";
						
						if(docPres.getString("DESCRIPCION")!=null) descripcion= docPres.getString("DESCRIPCION");
						if(descripcion.length()>=50){
							descripcion = descripcion.substring(0, 50);
						}
						
						Documento documentoAdicional = DipucrFuncionesComunes.getDocumento(rulectx, docPres, descripcion);

						documentoAdicional.setIdTypeDoc("DOC_ADD_CD");
						documentoAdicional.setTypeDoc("Documento Adicional de Pliegos");
						
						//Expedientes de la antigua ley el código es:
						//documentoAdicional.setIdTypeDoc("ZZZ");
						//documentoAdicional.setTypeDoc("Otros documentos");

		 				
		 				docAdicional[i] = documentoAdicional;
			 		    i++;
		 			}

		        }
	        	
	        }	        
	       
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+rulectx.getNumExp() +" - "+ e.getMessage(), e);
		}
		return docAdicional;
		
	}

//	public static void docAdicionalesPLACE(IRuleContext rulectx, String nombreTipoDoc) throws ISPACRuleException {
//		try{
//			// --------------------------------------------------------------------
//				ClientContext cct = (ClientContext) rulectx.getClientContext();
//				IInvesflowAPI invesFlowAPI = cct.getAPI();
//				IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
//			// --------------------------------------------------------------------			
//			
//			PlataformaContratacionStub platContratacion = new PlataformaContratacionStub(ServiciosWebContratacionFunciones.getDireccionSW());
//			int timeout = 3 * 60 * 1000; // 3 minutos
//			platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.SO_TIMEOUT, new Integer(timeout));
//			platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(timeout));
//			String nombreDoc = DocumentosUtil.getTipoDocNombreByCodigo(cct, nombreTipoDoc);
//			
//
//        	String query = "ID_TRAMITE="+rulectx.getTaskId()+" AND NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='"+nombreDoc+"'";
//			IItemCollection docsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), query, "FDOC" + DESC);
//			@SuppressWarnings("unchecked")
//			Iterator <IItem> docIterator = docsCollection.iterator();
//			if(docIterator.hasNext()){
//				IItem docPres = docIterator.next();
//				String descripcion = "";
//				
//				if(docPres.getString("DESCRIPCION")!=null) descripcion= docPres.getString("DESCRIPCION");
//				if(descripcion.length()>=50){
//					descripcion = descripcion.substring(0, 50);
//				}
//				
//				//Añadir la información adjunta			
//				es.dipucr.contratacion.services.PlataformaContratacionStub.Documento docAdicional = getDocumento(rulectx, docPres, descripcion);
//				
//				if(docAdicional!=null){
//					//Documentos con informaciona adicional
//					//Envio de la petición de pliego
//					try {
//						String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
//						LOGGER.warn(entidad);
//						//Petición
//						//String publishedByUser = UsuariosUtil.getDni(cct);
//						//String publishedByUser = "99001215S";
//						String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
//						if(publishedByUser==null || publishedByUser.equals("")){
//							publishedByUser = UsuariosUtil.getDni(cct);
//						}
//						EnvioOtrosDocumentosLicitacion envioOtrosDocumentosLicitacion = new EnvioOtrosDocumentosLicitacion();
//						envioOtrosDocumentosLicitacion.setDocumentoAdicional(docAdicional);
//						envioOtrosDocumentosLicitacion.setEntidad(entidad);
//						envioOtrosDocumentosLicitacion.setPublishedByUser(publishedByUser);
//						DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
//						envioOtrosDocumentosLicitacion.setOrganoContratacion(datContrato.getOrganoContratacion());
//						EnvioOtrosDocumentosLicitacionResponse envioOtrosDocumentosLicitacionResponse = platContratacion.envioOtrosDocumentosLicitacion(envioOtrosDocumentosLicitacion);
//						
//						Resultado resultadoAnalisisPrevio = envioOtrosDocumentosLicitacionResponse.get_return();
//						
//						PublicationResult resultado = resultadoAnalisisPrevio.getPublicacion();
//
//						if(!resultado.getResultCode().equals("OK") ){					
//							DipucrFuncionesComunes.errorPeticion(resultado, rulectx, nombreDoc, "Error");
//						}
//
//					} catch (RemoteException e) {
//						LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//						throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//					} catch (ISPACException e) {
//						LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//						throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//					} catch (PlataformaContratacionDatatypeConfigurationExceptionException e) {
//						LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//						throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//					} catch (PlataformaContratacionJAXBExceptionException e) {
//						LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//						throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//					} catch (PlataformaContratacionUnsupportedEncodingExceptionException e) {
//						LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//						throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//					} catch (PlataformaContratacionMalformedURLExceptionException e) {
//						LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//						throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//					} 
//				}
//			}
//			
//			
//			
//		}
//		catch(ISPACRuleException e){
//			LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//			throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//		} catch (ISPACException e) {
//			LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//			throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//		} catch (AxisFault e) {
//			LOGGER.error("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//			throw new ISPACRuleException("Error en el numero expedienre "+rulectx.getNumExp()+" - "+e.getMessage(),e);
//		}
//		
//	}


	public static void obtenerParticipantesCPV(IRuleContext rulectx, int cpv, String numExpHijo) throws ISPACRuleException {
		try{
			//DPCR2018/1
//    		String [] vNumexp = numExpHijo.split("/");
//    		String year = "";
//    		if(vNumexp.length>0){
//    			year = vNumexp[0];
//    		}
			String strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_DATOS_CONTRATO_S WHERE REG_ID = "+cpv+"";
	        ResultSet datos = rulectx.getClientContext().getConnection().executeQuery(strQuery).getResultSet();
	        String valorCPV = "";
//	        boolean inserta = false;
	        if(datos!=null)
	      	{
	        	while(datos.next()){
	          		if (datos.getString("VALUE")!=null) valorCPV = datos.getString("VALUE");
	          		strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_CMENOR_CPV_S WHERE VALUE = '"+valorCPV+"'";
	    	        ResultSet datosRegContra = rulectx.getClientContext().getConnection().executeQuery(strQuery).getResultSet();
	    	        if(datosRegContra!=null){
	    	        	while(datosRegContra.next()){
	    	        		int regIdRegContra = 0;
	    	        		if (datosRegContra.getInt("REG_ID")>0) regIdRegContra = datosRegContra.getInt("REG_ID");
	    	        		
	    	        		strQuery = "ID=" + regIdRegContra;
	    	        		Iterator<IItem> itRegContr = ConsultasGenericasUtil.queryEntities(rulectx.getClientContext(), "CONTRATACION_CMENOR_CPV", strQuery);
	    	    			if (itRegContr.hasNext()) {
	    	    				IItem iContMenorCPV = itRegContr.next();
	    	    				String numexpRegContMenor = iContMenorCPV.getString("NUMEXP");
	    	    				ParticipantesUtil.importarParticipantes((ClientContext) rulectx.getClientContext(), rulectx.getClientContext().getAPI().getEntitiesAPI(), numexpRegContMenor, numExpHijo);
	    	    				//Esto que esta comentado es porque por ahora no se quiere que se quite de los participantes los que superen la cuantia
	    	    				//por lo tanto, si se quisiera en un futuro con descomentar sería suficiente
	    	    				/**
	    	    				IItemCollection itParticipantesCPV = ParticipantesUtil.getParticipantes(rulectx.getClientContext(), numexpRegContMenor);
	    	    				Iterator<IItem> iterParticipantes = itParticipantesCPV.iterator();
	    	    				DatosContrato datosContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, numExpHijo);
	    	    				while(iterParticipantes.hasNext()){
	    	    					IItem participante = iterParticipantes.next();
	    	    					String cif = "";
	    	    					if(StringUtils.isNotEmpty(participante.getString(ParticipantesUtil.NDOC))){
	    		    					cif = participante.getString(ParticipantesUtil.NDOC);
	    		    					double cantiaAdj = 0.0;
	    		    					String squery = "NUMEXP IN (SELECT NUMEXP FROM CONTRATACION_DATOS_CONTRATO WHERE PROC_ADJ LIKE '"+datosContrato.getProcedimientoContratacion().getId()+" - %' "
	    		    							+ "AND TIPO_CONTRATO LIKE '"+datosContrato.getTipoContrato().getId()+" - %') AND NIF_ADJUDICATARIA='"+cif+"' AND NUMEXP LIKE '"+year+"/%'";
	    		    					Iterator<IItem> itDatosTrami = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_DATOS_TRAMIT", squery);
	    		    					while(itDatosTrami.hasNext()){
	    		    						IItem datosTrami = itDatosTrami.next();
	    		    						String numexpCIF = "";
	    		    						if(StringUtils.isNotEmpty(datosTrami.getString("NUMEXP"))){
	    		    							numexpCIF = datosTrami.getString("NUMEXP");
	    		    						}
	    		    						if(datosTrami!=null && StringUtils.isNotEmpty(datosTrami.getString("IMP_ADJ_SINIVA"))){
	    		    							String adjSinIVA = datosTrami.getString("IMP_ADJ_SINIVA");
	    		    							try{
	    		    								Double impSinIVA = Double.parseDouble(adjSinIVA);
	    		    								cantiaAdj += impSinIVA;
	    		    							}
	    		    							catch (NumberFormatException e) {
	    		    								throw new ISPACRuleException("Error en el campo Datps de Tramitacion/Importe SIN IVA"+numexpCIF+" -> "+adjSinIVA+"; ");
	    		    							}
	    		    						}
	    		    					}
	    		    					if(datosContrato.getTipoContrato().getValor().equals("Obras") && cantiaAdj<=DipucrFuncionesComunes.CUANTIACONTRATISTAOBRAS){
	    		    						inserta = true;
	    	    						}
	    	    						else{
	    	    							if(datosContrato.getTipoContrato().getValor().equals("Servicios") && cantiaAdj<=DipucrFuncionesComunes.CUANTIACONTRATISTASERVICIOS){
	    	    								inserta = true;
	    		    						}
	    	    							else{
	    	    								if(datosContrato.getTipoContrato().getValor().equals("Suministros") && cantiaAdj<=DipucrFuncionesComunes.CUANTIACONTRATISTASUMINISTROS){
	    	    									inserta = true;
	    			    						}
	    	    							}
	    	    						}
	    		    					if(inserta){
	    		    						ParticipantesUtil.insertarParticipanteByNIF(rulectx, numExpHijo, cif, 
	    		    								participante.getString(ParticipantesUtil.ROL),participante.getString(ParticipantesUtil.TIPO_PERSONA), participante.getString(ParticipantesUtil.EMAIL));
	    		    					}
	    		    					inserta = false;	    		    					
	    	    					}
	    	    				}
	    	    			**/
	    	    			}
	    	        	}
	    	        }
	        	}
	      	}
		}
        catch (ISPACException e) {
			LOGGER.error("Error en el numero expediente "+rulectx.getNumExp()+" - "+e.getMessage(),e);
			throw new ISPACRuleException("Error en el numero expediente "+rulectx.getNumExp()+" - "+e.getMessage(),e);
		} catch (SQLException e) {
			LOGGER.error("Error en el numero expediente "+rulectx.getNumExp()+" - "+e.getMessage(),e);
			throw new ISPACRuleException("Error en el numero expediente "+rulectx.getNumExp()+" - "+e.getMessage(),e);
		}
	}

	@Deprecated
	public static LicitadorBean[] getLicitadores(IRuleContext rulectx) throws ISPACRuleException {
		return getLicitadores(rulectx.getClientContext(), rulectx.getNumExp());
	}
	
	public static LicitadorBean[] getLicitadores(IClientContext cct, String numexp) throws ISPACRuleException {
		LicitadorBean[] licitadores = null;
		try{
			IItemCollection partCol = ParticipantesUtil.getParticipantesByRol(cct, numexp, ParticipantesUtil._TIPO_INTERESADO);
			Iterator<?> partIt = partCol.iterator();
			if(partIt.hasNext()){
				int i = 0;
				licitadores = new LicitadorBean[partCol.toList().size()];
				while(partIt.hasNext()){
					LicitadorBean licitador = new LicitadorBean();
					IItem part = (IItem) partIt.next();
					if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.NOMBRE))) {
						licitador.setNombre(part.getString(ParticipantesUtil.NOMBRE));
					}
					if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.NDOC))) {
						licitador.setIdentificador(part.getString(ParticipantesUtil.NDOC));
						//Tipo de identificador: 
						//cac-place-ext:TendererStatus/cac:TendererParty/cac:PartyIdentification/cbc:ID@schemName que podrá tomar los valores 'NIF, 'UTE' u 'OTROS'
						licitador.setTipoIdentificador("NIF");
					}
					if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.DIRNOT))) {
						licitador.setCalle(part.getString(ParticipantesUtil.DIRNOT));
					}
					if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.C_POSTAL))) {
						licitador.setCp(part.getString(ParticipantesUtil.C_POSTAL));
					}
					if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.LOCALIDAD))) {
						licitador.setPoblacion(part.getString(ParticipantesUtil.LOCALIDAD));
					}
					if (StringUtils.isNotEmpty(part.getString(ParticipantesUtil.DIRECCIONTELEMATICA))) {
						licitador.setEmail(part.getString(ParticipantesUtil.DIRECCIONTELEMATICA));
					}
					licitadores[i] = licitador;
					i++;
				}				
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en el numero expediente " + numexp + " - " + e.getMessage(),e);
			throw new ISPACRuleException("Error en el numero expediente " + numexp + " - " + e.getMessage(),e);
		}
		return licitadores;
	}

	@Deprecated
	public static Documento getDocumentoPliegos(IRuleContext rulectx, String codTipoDoc, String organoContratacion) throws ISPACRuleException {
		return getDocumentoPliegos(rulectx.getClientContext(), rulectx.getNumExp(), codTipoDoc, organoContratacion);
	}
	
	public static Documento getDocumentoPliegos(IClientContext cct, String numexp, String codTipoDoc, String organoContratacion) throws ISPACRuleException {
		Documento documentoPCAP = null;
		try{
			String nombreTipoDoc = DocumentosUtil.getNombreTipoDocByCod(cct, codTipoDoc);
			
			IItem itemTramite = TramitesUtil.getTramiteByCode(cct, numexp, "pliegos");
			
			String strQuery = TramitesUtil.NOMBRE+" = '" + itemTramite.getString("NOMBRE") + "' AND " + TramitesUtil.NUMEXP + " = '" + numexp + "'";
	        IItemCollection collection = TramitesUtil.getTramites(cct, numexp, strQuery, TramitesUtil.FECHA_CIERRE + DESC);
	        
	        @SuppressWarnings("unchecked")
			Iterator <IItem> it = collection.iterator();
	        if (it.hasNext())
	        {
	        	IItem doc = it.next();
				// obtengo los documentos
				int nidstage = doc.getInt("ID_FASE_PCD");
	        	int idTask = doc.getInt("ID_TRAM_PCD");
				
				String query = "ID_FASE_PCD = " + nidstage + " AND ID_TRAMITE_PCD = " + idTask + " AND FFIRMA IS NOT NULL AND NUMEXP = '" + numexp + "'";
				LOGGER.warn("query " + query);
				IItemCollection docsCollection = DocumentosUtil.getDocumentos(cct, numexp, query, DocumentosUtil.FAPROBACION + DESC);

				Iterator <?> docIterator = docsCollection.iterator();
				if(docIterator.hasNext()){
					boolean encontradoPCEA = false;
					while(docIterator.hasNext()){
						IItem docPres = (IItem) docIterator.next();
						String infoPag = "";
						String extension = "";
						if(docPres.getString(DocumentosUtil.INFOPAG_RDE)!=null){
							infoPag= docPres.getString(DocumentosUtil.INFOPAG_RDE);
							extension = docPres.getString(DocumentosUtil.EXTENSION_RDE);
						}
						else{
							if(docPres.getString(DocumentosUtil.INFOPAG)!= null) infoPag= docPres.getString(DocumentosUtil.INFOPAG);
							extension = docPres.getString(DocumentosUtil.EXTENSION);
						}
						LOGGER.warn("infoPag "+infoPag);
						File fichero = DocumentosUtil.getFile(cct, infoPag, null, null);
						LOGGER.warn("fichero "+fichero);
						
						if(docPres.getString(DocumentosUtil.NOMBRE).equals(nombreTipoDoc) && !encontradoPCEA){
							//"Pliego de Clausulas Económico - Administrativas"
							LOGGER.warn(nombreTipoDoc);
							documentoPCAP= new Documento();
							//documentoPCAP.setMimeCode("application/octet-stream");
							documentoPCAP.setMimeCode(MimetypeMapping.getMimeType(extension));
							if(docPres.getDate(DocumentosUtil.FFIRMA)!=null){
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(docPres.getDate("FFIRMA"));
								documentoPCAP.setFechaFirma(calendar);
							}
							
							FileInputStream fin = null;
							
							try {
								fin = new FileInputStream(fichero);
								long length = fichero.length();
								 
								if (length > Integer.MAX_VALUE) {
									throw new IOException("Tamaño del fichero excesivo: "  + length);
								}
		//						Create the byte array
								byte[] bytes = new byte[(int)length];
								 
		//						Reads the file content
								int offset = 0;
								int numRead = 0;
								while (offset < bytes.length
									   && (numRead=fin.read(bytes, offset, bytes.length-offset)) >= 0) {
									offset += numRead;
								}
								 
		//						Just to check if file was read completely
								if (offset < bytes.length) {
									throw new IOException("No se ha podido leer completamente el fichero " + fichero.getName());
								}
								 
		//						Close the input stream, all file contents are in the bytes variable
								fin.close();
								//documentoPCAP.setContenido(new DataHandler(bytes,"application/octet-stream"));
								documentoPCAP.setContenido(new DataHandler(bytes, MimetypeMapping.getMimeType(extension)));
								documentoPCAP.setMimeCode(MimetypeMapping.getMimeType(extension));
								
							} catch(IOException e){
								LOGGER.error("Error: " + numexp + ". " + e.getMessage(), e);
								throw new ISPACRuleException("Error: " + numexp + ". " + e.getMessage(), e);
								
							} finally {
								if(null != fin){
									try{
										fin.close();
									} catch(Exception e){
										LOGGER.error("ERROR al cerrar el FileInputStream. " + e.getMessage(), e);
									}
								}
							}
							
							String nombre = docPres.getString(DocumentosUtil.DESCRIPCION);
			 				nombre = DipucrFuncionesComunes.limpiarCaracteresEspeciales(nombre);
			 				LOGGER.warn("*****************************"+nombre);
			 				documentoPCAP.setNameDoc(nombre+"."+extension);
			 				
			 				if(codTipoDoc.equals("PCEA")){
			 					documentoPCAP.setIdTypeDoc("DOC_PCAP");
			 				}
			 				else{
			 					documentoPCAP.setIdTypeDoc("DOC_PPT");
			 				}
			 				
			 				documentoPCAP.setExpedientNumber(numexp);
			 				documentoPCAP.setOrganoContratacion(organoContratacion);
							encontradoPCEA = true;
						}
						fichero.delete();		
					}
				}
			}
		} catch (ISPACException e) {
			LOGGER.error("Error numexp " +numexp + " - " + e.getMessage(),e);
			throw new ISPACRuleException("Error numexp " + numexp + " - " + e.getMessage(),e);
		} 
		return documentoPCAP;
	}
	
	public static byte[] toBytes(DataHandler handler) throws IOException {
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    handler.writeTo(output);
	    return output.toByteArray();
	}


	public static es.dipucr.contratacion.objeto.sw.Documento convertirDoc_SW(Documento doc) throws IOException{
		es.dipucr.contratacion.objeto.sw.Documento docRes = new es.dipucr.contratacion.objeto.sw.Documento();
		docRes.setBuyerProfileId(doc.getBuyerProfileId());
		docRes.setContenido(toBytes(doc.getContenido()));
		docRes.setDescripcion(doc.getDescripcion());
		docRes.setExpedientNumber(doc.getExpedientNumber());
		docRes.setFechaFirma(doc.getFechaFirma());
		docRes.setIdTypeDoc(doc.getIdTypeDoc());
		docRes.setMimeCode(doc.getMimeCode());
		docRes.setNameDoc(doc.getNameDoc());
		docRes.setOrganoContratacion(doc.getOrganoContratacion());
		docRes.setTypeDoc(doc.getTypeDoc());
		docRes.setUrlDocument(doc.getUrlDocument());
		return docRes;
	}
}