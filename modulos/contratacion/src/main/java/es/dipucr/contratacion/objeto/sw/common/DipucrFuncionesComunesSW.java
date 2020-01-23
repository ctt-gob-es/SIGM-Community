package es.dipucr.contratacion.objeto.sw.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionPostal;
import ieci.tecdoc.sgm.core.services.terceros.dto.Tercero;
import ieci.tecdoc.sgm.core.services.tramitacion.ServicioTramitacion;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.DocumentoExpediente;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.Adjudicatario;
import es.dipucr.contratacion.objeto.sw.AplicacionPresupuestaria;
import es.dipucr.contratacion.objeto.sw.BOP;
import es.dipucr.contratacion.objeto.sw.Campo;
import es.dipucr.contratacion.objeto.sw.CondicionesLicitadores;
import es.dipucr.contratacion.objeto.sw.CriterioAdjudicacionMultCrit;
import es.dipucr.contratacion.objeto.sw.CriteriosAdjudicacion;
import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.DatosEmpresa;
import es.dipucr.contratacion.objeto.sw.DatosLicitacion;
import es.dipucr.contratacion.objeto.sw.DatosTramitacion;
import es.dipucr.contratacion.objeto.sw.Departamento;
import es.dipucr.contratacion.objeto.sw.DepartamentosContacto;
import es.dipucr.contratacion.objeto.sw.DiariosOficiales;
import es.dipucr.contratacion.objeto.sw.Documento;
import es.dipucr.contratacion.objeto.sw.DuracionContratoBean;
import es.dipucr.contratacion.objeto.sw.EventoAperturaBean;
import es.dipucr.contratacion.objeto.sw.FormalizacionBean;
import es.dipucr.contratacion.objeto.sw.FundacionPrograma;
import es.dipucr.contratacion.objeto.sw.Garantia;
import es.dipucr.contratacion.objeto.sw.LicitadorBean;
import es.dipucr.contratacion.objeto.sw.Lote;
import es.dipucr.contratacion.objeto.sw.Lotes;
import es.dipucr.contratacion.objeto.sw.OfertasRecibidas;
import es.dipucr.contratacion.objeto.sw.OrganoAsistencia;
import es.dipucr.contratacion.objeto.sw.Periodo;
import es.dipucr.contratacion.objeto.sw.PersonaComite;
import es.dipucr.contratacion.objeto.sw.Peticion;
import es.dipucr.contratacion.objeto.sw.RequisitfiDeclaraciones;
import es.dipucr.contratacion.objeto.sw.SobreElectronico;
import es.dipucr.contratacion.objeto.sw.Solvencia;
import es.dipucr.contratacion.objeto.sw.SolvenciaEconomica;
import es.dipucr.contratacion.objeto.sw.SolvenciaTecnica;
import es.dipucr.contratacion.objeto.sw.VariantesOfertas;
import es.dipucr.contratacion.utils.Constantes;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;



public class DipucrFuncionesComunesSW {
	
	public static final String DOC_PIN = "Anuncio de Informacion Previa";  //Anuncio previo
	public static final String DOC_CN = "Anuncio de Licitación";	 //Anuncio de Licitación
	public static final String DOC_CD = "Anuncio de Pliego";	 //Pliego
	public static final String DOC_ADJ = "Anuncio Resultado Licitación";	 //Anuncio de adjudicacion
	public static final String DOC_GEN = "DOC_GEN";	 //Documentos generales
	public static final double CUANTIACONTRATISTAOBRAS = 40000;
	public static final double CUANTIACONTRATISTASERVICIOS = 15000;
	public static final double CUANTIACONTRATISTASUMINISTROS = 15000;
	
	public static final Logger LOGGER = Logger.getLogger(DipucrFuncionesComunesSW.class);
	
	public static final String DESC = " DESC";
	
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
	
	public static Campo getResultadoLicitacion(IClientContext cct, String numexp) throws ISPACRuleException {
		Campo campo = null;
		try{
			
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			IItemCollection resLiciCollection = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", numexp);
			@SuppressWarnings("unchecked")
			Iterator <IItem> resLiciIterator = resLiciCollection.iterator();
			while(resLiciIterator.hasNext()){
				IItem iResultadoLicitacion = resLiciIterator.next();
				
				if(iResultadoLicitacion.getString("RES_LICITACION")!=null) {
					String critSolv = iResultadoLicitacion.getString("RES_LICITACION");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
					}
				}
			}
		
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
		}
		return campo;
	}
	
	public static Documento[] docInformacionAdicionalPliego(IClientContext cct, String numexp, String organoContratacion) throws ISPACException {
		Documento[] docAdicional = null;
		try{
			// --------------------------------------------------------------------
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
		
			//Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexp+"' AND RELACION='Petición Contrato'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<?> itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = (IItem) itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        	
	        	IItem exp = entitiesAPI.getExpedient(numexpPetCont);
	        	
	        	//codigo del trámite -> inf-nec-cont-adm -> Informe razonado de la necesidad del contrato administrativo
	        	IItem tramite = TramitesUtil.getTramiteByCode(cct, numexp, "inf-nec-cont-adm");
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
				
				String nombreTipoPlantPPT = DocumentosUtil.getNombreTipoDocByCod(cct, "PPT");
				
		        if (it.hasNext())
		        {
		        	IItem doc = (IItem) it.next();
		        	int idFase = doc.getInt("ID_FASE");
		        	String idTtramiteBpm = doc.getString("ID_TRAMITE_BPM");
		        	
		        	String query = "NUMEXP='"+numexpPetCont+"' AND ID_FASE_PCD="+idFase+" AND ID_TRAMITE_PCD="+idTtramiteBpm+" AND NOMBRE!='"+nombreTipoPlantPPT+"'";
		        	LOGGER.warn("query "+query);
		 			IItemCollection docsCollection = entitiesAPI.getDocuments(numexpPetCont, query, "FDOC" + DESC);
		 			
		 			Iterator <?> docIterator = docsCollection.iterator();
		 			docAdicional = new Documento [docsCollection.toList().size()+1];
		 			int i = 0;
					while(docIterator.hasNext()){
						
						IItem docPres = (IItem) docIterator.next();
						
						String descripcion = "";						
						if(docPres.getString("DESCRIPCION")!=null) descripcion= docPres.getString("DESCRIPCION");
						if(descripcion.length()>=50){
							descripcion = descripcion.substring(0, 50);
						}
						
						Documento documentoAdicional = getDocumento(cct, docPres, descripcion, numexp);
						
						documentoAdicional.setExpedientNumber(numexp);
						documentoAdicional.setOrganoContratacion(organoContratacion);
						documentoAdicional.setIdTypeDoc("DOC_ADD_CD");
						documentoAdicional.setTypeDoc("Documento Adicional de Pliegos");
						docAdicional[i] = documentoAdicional;
			 		    i++;
		 			}
					//Decreto O Propuesta
					String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexp);
					IItem acuerdo = null; 
					if(StringUtils.isNotEmpty(numexpDecreto)){
						acuerdo = DecretosUtil.getDocDecretoByNumExpDecreto(cct, numexpDecreto);
					}
					else{
						String numExpPropuesta = SecretariaUtil.getUltimoNumexpPropuesta(cct, numexp);
						if(StringUtils.isNotEmpty(numExpPropuesta)){
							acuerdo = SecretariaUtil.getDocAcuerdoPlenJGByNumExpPropuesta(cct, numExpPropuesta);
						}
					}
					if(acuerdo!=null){
						String descripcion = "";						
						if(acuerdo.getString("DESCRIPCION")!=null) descripcion= acuerdo.getString("DESCRIPCION");
						if(descripcion.length()>=50){
							descripcion = descripcion.substring(0, 50);
						}
						
						Documento documentoAdicional = getDocumento(cct, acuerdo, descripcion, numexp);						
						documentoAdicional.setExpedientNumber(numexp);
						documentoAdicional.setOrganoContratacion(organoContratacion);
						documentoAdicional.setIdTypeDoc("DOC_ADD_CD");
						documentoAdicional.setTypeDoc("Documento Adicional de Pliegos");
						docAdicional[i] = documentoAdicional;
					}
		        }
		        
	        }
	        
		}catch(ISPACRuleException e){
			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
			throw e;
		} catch (ISPACException e) {
			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
			throw e;
		}
		return docAdicional;
	}

@SuppressWarnings("resource")
public static Documento getDocumento(IClientContext cct, IItem docPres, String nombreMostrarDoc, String numexp) throws ISPACRuleException {
	Documento docAdj = null;
	try{
		
		/************************************************************************/
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
//							Create the byte array
			byte[] bytes = new byte[(int)length];
			 
//							Reads the file content
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
				   && (numRead=fin.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
			 
//							Just to check if file was read completely
			if (offset < bytes.length) {
				throw new IOException("No se ha podido leer completamente el fichero " + fichero.getName());
			}
			 
//							Close the input stream, all file contents are in the bytes variable
			fin.close();
			docAdj.setContenido(bytes);
			docAdj.setMimeCode("application/octet-stream");
			
			docAdj.setExpedientNumber(numexp);
				
			String nombre = nombreMostrarDoc.replace("-", " ");
			nombre = nombre.replace("º", "");
			nombre = nombre.replace("_", " ");
			//nombre = DipucrFuncionesComunes.quitarExtensionNombre(nombre);
			nombre = DipucrFuncionesComunesSW.limpiarCaracteresEspeciales(nombre);

			docAdj.setNameDoc(nombre+"."+extension+"");
			
			//Obtengo el tipo de documento.
			String query = "WHERE NUMEXP='"+numexp+"'";
			IItemCollection collectionDoc = entitiesAPI.queryEntities("CONTRATACION_DOC_ADICIONALES", query);
        	Iterator<?> itDoc = collectionDoc.iterator();
    		if(itDoc.hasNext()){
    			IItem docTipo = (IItem)itDoc.next();
    			String tipoDoc = docTipo.getString("TIPO_DOC");
    			String [] vtipoDoc = tipoDoc.split(" - ");
				if(vtipoDoc.length >1){
					docAdj.setIdTypeDoc(vtipoDoc[0]);
					docAdj.setTypeDoc(vtipoDoc[1]);
				}
    		}			
		}
	}catch(ISPACRuleException e){
		LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
		throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
	} catch (ISPACException e) {
		LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
		throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
	} catch (FileNotFoundException e) {
		LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
		throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
	} catch (IOException e) {
		LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
		throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
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

	public static DepartamentosContacto getPersonalContacto(IClientContext cct, String numexp) throws ISPACRuleException {

		DepartamentosContacto personal = new DepartamentosContacto();
		
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			//Personal contacto contratacion
			Departamento persContratacion = new Departamento();
			//Personal contacto secretaria
			Departamento persSecretaria = new Departamento();
			IItemCollection persContCollection = entitiesAPI.getEntities("CONTRATACION_PERS_CONTACTO", numexp);
			Iterator <?> persContIterator = persContCollection.iterator();
			
			while(persContIterator.hasNext()){
				IItem itPersCont = (IItem) persContIterator.next();
				if(itPersCont.getString("NOMBRE")!=null){
					persContratacion.setNombreContacto(itPersCont.getString("NOMBRE"));
				}
				if(itPersCont.getString("EMAIL")!=null){
					persContratacion.setEmail(itPersCont.getString("EMAIL"));
				}
				if(itPersCont.getString("CALLE")!=null){
					persContratacion.setCalle(itPersCont.getString("CALLE"));
				}
				if(itPersCont.getString("CP")!=null){
					persContratacion.setCp(itPersCont.getInt("CP")+"");
				}
				if(itPersCont.getString("LOCALIDAD")!=null){
					persContratacion.setCiudad(itPersCont.getString("LOCALIDAD"));
				}
				if(itPersCont.getString("PROVINCIA")!=null){
					persContratacion.setProvincia(itPersCont.getString("PROVINCIA"));
				}
				if(itPersCont.getString("MOVIL")!=null){
					persContratacion.setTelefono(itPersCont.getString("MOVIL"));
				}
				Campo formatoDirec = new Campo();
				formatoDirec.setId("1");
				formatoDirec.setValor("Spanish Format");
				persContratacion.setCodFormatoDirec(formatoDirec);
				
				Campo pais = new Campo();
				pais.setId("ES");
				pais.setValor("España");
				persContratacion.setPais(pais);
				
				if(itPersCont.getString("LOCALIZACIONGEOGRAFICA")!=null) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						persContratacion.setLocalizacionGeografica(campo);
					}
				}
				personal.setPersonalContactoContratacion(persContratacion);
				
				//SECRETARIA
				if(itPersCont.getString("NOMBRESECRE")!=null){
					persSecretaria.setNombreContacto(itPersCont.getString("NOMBRESECRE"));
				}
				if(itPersCont.getString("EMAILSECRE")!=null){
					persSecretaria.setEmail(itPersCont.getString("EMAILSECRE"));
				}
				if(itPersCont.getString("CALLESECRE")!=null){
					persSecretaria.setCalle(itPersCont.getString("CALLESECRE"));
				}
				if(itPersCont.getString("CPSECRE")!=null){
					persSecretaria.setCp(itPersCont.getString("CPSECRE"));
				}
				if(itPersCont.getString("LOCALIDADSECRE")!=null){
					persSecretaria.setCiudad(itPersCont.getString("LOCALIDADSECRE"));
				}
				if(itPersCont.getString("PROVINCIASECRE")!=null){
					persSecretaria.setProvincia(itPersCont.getString("PROVINCIASECRE"));
				}
				if(itPersCont.getString("MOVILSECRE")!=null){
					persSecretaria.setTelefono(itPersCont.getString("MOVILSECRE"));
				}

				persSecretaria.setCodFormatoDirec(formatoDirec);
				
				persSecretaria.setPais(pais);
				
				if(itPersCont.getString("LOCALIZACIONGEOGRAFICASECRE")!=null) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICASECRE");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						persSecretaria.setLocalizacionGeografica(campo);
					}
				}
				personal.setPersonalContactoSecretaria(persSecretaria);
				//ORGANO ASISTENCIA
				Departamento personaOAsistencia = new Departamento();
				if(StringUtils.isNotEmpty(itPersCont.getString("EMAIL_OA"))){
					personaOAsistencia.setEmail(itPersCont.getString("EMAIL_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("EMAIL_CM_OA"))){
					personaOAsistencia.setEmail(itPersCont.getString("EMAIL_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("CALLE_OA"))){
					personaOAsistencia.setCalle(itPersCont.getString("CALLE_OA"));
					personaOAsistencia.setCodFormatoDirec(formatoDirec);					
					personaOAsistencia.setPais(pais);
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("CALLE_CM_OA"))){
					personaOAsistencia.setCalle(itPersCont.getString("CALLE_CM_OA"));
					personaOAsistencia.setCodFormatoDirec(formatoDirec);					
					personaOAsistencia.setPais(pais);
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("NUMERO_OA"))){
					personaOAsistencia.setNumeroEdificio(itPersCont.getString("NUMERO_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("NUMERO_CM_OA"))){
					personaOAsistencia.setNumeroEdificio(itPersCont.getString("NUMERO_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("CP_OA"))){
					personaOAsistencia.setCp(itPersCont.getString("CP_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("CP_CM_OA"))){
					personaOAsistencia.setCp(itPersCont.getString("CP_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIDAD_OA"))){
					personaOAsistencia.setCiudad(itPersCont.getString("LOCALIDAD_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIDAD_CM_OA"))){
					personaOAsistencia.setCiudad(itPersCont.getString("LOCALIDAD_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("PROVINCIA_OA"))){
					personaOAsistencia.setProvincia(itPersCont.getString("PROVINCIA_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("PROVINCIA_CM_OA"))){
					personaOAsistencia.setProvincia(itPersCont.getString("PROVINCIA_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("MOVIL_OA"))){
					personaOAsistencia.setTelefono(itPersCont.getString("MOVIL_OA"));
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("MOVIL_CM_OA"))){
					personaOAsistencia.setTelefono(itPersCont.getString("MOVIL_CM_OA"));
				}
				
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIZACIONGEOGRAFICA_OA"))) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICA_OA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						personaOAsistencia.setLocalizacionGeografica(campo);
					}
				}
				if(StringUtils.isNotEmpty(itPersCont.getString("LOCALIZACIONGEOGRAFICA_CM_OA"))) {
					String critSolv = itPersCont.getString("LOCALIZACIONGEOGRAFICA_CM_OA");
					String [] vcritSolv = critSolv.split(" - ");
					if(vcritSolv.length >1){
						Campo campo = new Campo();
						campo.setId(vcritSolv[0]);
						campo.setValor(vcritSolv[1]);
						personaOAsistencia.setLocalizacionGeografica(campo);
					}
				}
				personal.setPersonalContactoOrganoAsistencia(personaOAsistencia);
			}
			
		} catch(ISPACRuleException e){
			LOGGER.error("Numexp. " + numexp + " Error. " + e.getMessage(),e);
			throw new ISPACRuleException("Numexp. " + numexp + " Error. " + e.getMessage(),e);
			
		} catch (ISPACException e) {
			LOGGER.error("Numexp. " + numexp + " Error. " + e.getMessage(),e);
			throw new ISPACRuleException("Numexp. " + numexp + " Error. " + e.getMessage(),e);
		}
		
		return personal;
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
							aplicPre.setImporte(sAplicacion[2].replace(",", "."));
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
				peticion.setServicioResponsable(itemPeticion.getString("SERVICIO_RESPONSABLE"));
				peticion.setIva(itemPeticion.getString("IVA"));
				peticion.setPresupuestoIva(itemPeticion.getString("IMPORTEIVA"));
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
	
	public static DiariosOficiales getDiariosOficiales(IClientContext cct, String numexp) throws ISPACRuleException {
		DiariosOficiales diariosOficiales = new DiariosOficiales();
		try{
			boolean insertado = false;
			Iterator<IItem> itColl = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_PUBLIC_PLACE", "NUMEXP = '" + numexp + "'");
			while(itColl.hasNext()){
				IItem place = itColl.next();
				if(place.getDate("ANUNCIO_LICITACION")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("ANUNCIO_LICITACION"));
			    	diariosOficiales.setAnunLicitacionPerfilContratante(calendar);
			    	insertado = true;
			    }
				if(place.getDate("ANUNCIO_FORMALIZACION")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("ANUNCIO_FORMALIZACION"));
			    	diariosOficiales.setAnunFormalizacionPerfilContratante(calendar);
			    	insertado = true;
			    }
				if(place.getDate("ANUNCIO_ADJUDICACION")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("ANUNCIO_ADJUDICACION"));
			    	diariosOficiales.setAnunAdjudicacionPerfilContratante(calendar);
			    	insertado = true;
			    }
			}
			itColl = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_DOUE", "NUMEXP='"+numexp+"'");
			while(itColl.hasNext()){
				IItem place = itColl.next();
				if(place.getDate("FECHA_PUBLICACION_ANUN_LIC_DOU")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_LIC_DOU"));
			    	diariosOficiales.setAnuncioLicitacionDOUE(calendar);
			    	insertado = true;
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_LIC_BOE")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_LIC_BOE"));
			    	diariosOficiales.setAnuncioLicitacionBOE(calendar);
			    	insertado = true;
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_FOR_DOU")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_FOR_DOU"));
			    	diariosOficiales.setAnuncioFormalizacionDOUE(calendar);
			    	insertado = true;
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_FOR_BOE")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_FOR_BOE"));
			    	diariosOficiales.setAnuncioFormalizacionBOE(calendar);
			    	insertado = true;
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_DOU")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_DOU"));
			    	diariosOficiales.setAnuncioAdjudicacionDOUE(calendar);
			    	insertado = true;
			    }
				if(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_BO")!=null){
			    	Calendar calendar = Calendar.getInstance();
			    	calendar.setTime(place.getDate("FECHA_PUBLICACION_ANUN_ADJ_BO"));
			    	diariosOficiales.setAnuncioAdjudicacionBOE(calendar);
			    	insertado = true;
			    }
				if(place.getString("DIRECTIVA")!=null) {
					String directiva = place.getString("DIRECTIVA");
					String [] vdirectiva = directiva.split(" - ");
					if(vdirectiva.length >1){
						Campo campo = new Campo();
						campo.setId(vdirectiva[0]);
						campo.setValor(vdirectiva[1]);
						diariosOficiales.setContratoSujetoRegArmon(campo);
						insertado = true;
					}
				}
				if(place.getString("ADJPYME")!=null){
					if(place.getString("ADJPYME").equals("SI")){
						diariosOficiales.setAdjudicatarioPYME(new Boolean(true));
						insertado = true;
					}
					if(place.getString("ADJPYME").equals("NO")){
						diariosOficiales.setAdjudicatarioPYME(new Boolean(false));
						insertado = true;
					}
				}
				if(place.getString("PUBLICADOANUNCIOADJ_BOE")!=null){
					boolean boeAdj = false;
					if(place.getString("PUBLICADOANUNCIOADJ_BOE").equals("SI")){						
						boeAdj = true;
						
					}
					diariosOficiales.setBoeAdjudicacion(boeAdj);
					insertado = true;
				}
				if(place.getString("PUBLICADOANUNCIOADJ_DOUE")!=null){
					boolean douAdj = false;
					if(place.getString("PUBLICADOANUNCIOADJ_DOUE").equals("SI")){						
						douAdj = true;
						
					}
					diariosOficiales.setDoueAdjudicacion(douAdj);
					insertado = true;
				}
				if(place.getString("PUBLICADOANUNCIOFORM_BOE")!=null){
					boolean boeForm = false;
					if(place.getString("PUBLICADOANUNCIOFORM_BOE").equals("SI")){						
						boeForm = true;						
					}
					diariosOficiales.setBoeFormalizacion(boeForm);
					insertado = true;
				}
				if(place.getString("PUBLICADOANUNCIOFORM_DOUE")!=null){
					boolean doueFormal = false;
					if(place.getString("PUBLICADOANUNCIOFORM_DOUE").equals("SI")){						
						doueFormal = true;
						
					}
					diariosOficiales.setDoueFormalizacion(doueFormal);
				}
			}
			if(!insertado){
				diariosOficiales = null;
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
	
	public static DatosTramitacion getDatosTramitacion(IClientContext cct, String numexp, IItem datosTram) throws ISPACException {

		DatosTramitacion datosTramitacion = null;
		
		try{
			boolean insertado = false; 
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
			    	insertado = true;
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
				    insertado = true;
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
			    	insertado = true;
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
			    	insertado = true;
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
			    	insertado = true;
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
			    	insertado = true;
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
			    	insertado = true;
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
					insertado = true;
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
				//Numero del contrato
				DatosContrato datosContrato = DipucrFuncionesComunesSW.getDatosContrato(cct, numexp);
				if(datosContrato!=null){
					if(StringUtils.isNotEmpty(datosContrato.getNumContrato()))formalizacion.setNumContrato(datosContrato.getNumContrato());	
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
					insertado = true;
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
				insertado = true;
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
					insertado = true;
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
					insertado = true;
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
					insertado = true;
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
					insertado = true;
				}
				/************************************************/
				String invitaciones = null;
				if(datosTram !=null && datosTram.getString("INVITACIONES_LICITAR")!=null){
					invitaciones = datosTram.getString("INVITACIONES_LICITAR");
				}
				if(invitaciones!=null){
					datosTramitacion.setInvitacioneLicitar(invitaciones);
					insertado = true;
				}
			}
			if(!insertado){
				datosTramitacion = null;
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
	
	public static DatosContrato getDatosContrato (IClientContext cct, String numexp) throws ISPACRuleException{
		DatosContrato datosContrato = new DatosContrato();
		
		try{
			boolean insertado = false;
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
				if(iDatosContrato.getString("NCONTRATO")!=null){
					datosContrato.setNumContrato(iDatosContrato.getString("NCONTRATO"));
					insertado = true;
				}
				if(iDatosContrato.getString("CONT_SUJ_REG_ARMO")!=null){
					insertado = true;
					if(iDatosContrato.getString("CONT_SUJ_REG_ARMO").equals("SI"))datosContrato.setRegulacionArmonizada(true);
					else datosContrato.setRegulacionArmonizada(false);
				}

				//Organo contrtacion
				if(iDatosContrato.getString("ORGANO_CONTRATACION")!=null){
					datosContrato.setOrganoContratacion(iDatosContrato.getString("ORGANO_CONTRATACION"));
					insertado = true;
				}
				
				//Provincia contrato
				String lugarEjecucionContrato="";
				if(iDatosContrato.getString("PROVINCIA_CONTRATO")!=null)lugarEjecucionContrato = iDatosContrato.getString("PROVINCIA_CONTRATO");
				LOGGER.warn("lugarEjecucionContrato "+lugarEjecucionContrato);
				String [] vlugarEjecucionContrato = lugarEjecucionContrato.split(" - ");
				if(vlugarEjecucionContrato.length >1){
					insertado = true;
					Campo campo = new Campo();
					campo.setId(vlugarEjecucionContrato[0]);
					campo.setValor(vlugarEjecucionContrato[1]);
					datosContrato.setProvinciaContrato(campo);
				}
				
				//procNegArticulo. Artículo y apartado de la LCAP por el que se aplica procedimiento negociado
				if(iDatosContrato.getString("PROCNEGARTICULO")!=null){
					datosContrato.setProcNegCausa(iDatosContrato.getString("PROCNEGARTICULO"));
					insertado = true;
				}
				
				//Caracteristicas
				if(iDatosContrato.getString("CARACTERISTICA_BIENES_RENDCUEN")!=null){
					datosContrato.setCaracteristicasBienes(iDatosContrato.getString("CARACTERISTICA_BIENES_RENDCUEN"));
					insertado = true;
				}
				
				//Objeto del contrato
				if(iDatosContrato.getString("OBJETO_CONTRATO")!=null){
					datosContrato.setObjetoContrato(iDatosContrato.getString("OBJETO_CONTRATO"));
					insertado = true;
				}
				//precio estamado del contrado
				if(iDatosContrato.getString("PRECIO_ESTIMADO_CONTRATO")!=null){
					datosContrato.setValorEstimadoContrato(iDatosContrato.getString("PRECIO_ESTIMADO_CONTRATO"));
					insertado = true;
				}
				
				if(StringUtils.isNotEmpty(iDatosContrato.getString("NUEVA_LEY"))){
					if(iDatosContrato.getString("NUEVA_LEY").equals("SI")){
						datosContrato.setNuevaLey(true);
					}
					else{
						datosContrato.setNuevaLey(false);
					}
				}
				
				//Procedimiento contratación
				String tipoProcBD = "";
				if(iDatosContrato.getString("PROC_ADJ")!=null)tipoProcBD=iDatosContrato.getString("PROC_ADJ");
				String [] vsTipoProc = tipoProcBD.split(" - ");
				if(vsTipoProc.length >1){
					Campo campo = new Campo();
					campo.setId(vsTipoProc[0]);
					campo.setValor(vsTipoProc[1]);
					datosContrato.setProcedimientoContratacion(campo);
					insertado = true;
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
					insertado = true;
				}
				
				boolean criteriosMultiples = false;
				if(iDatosContrato.getString("ABIERTO_CRITERIOS_MULTIPLES")!=null){
					if(iDatosContrato.getString("ABIERTO_CRITERIOS_MULTIPLES").equals("SI")){
						criteriosMultiples = true;
					}
					datosContrato.setCriteriosMultiples(criteriosMultiples);
					insertado = true;
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
					insertado = true;
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
					insertado = true;
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
					insertado = true;
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
		          		insertado = true;
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
								insertado = true;
							}
			        	}
		        		datosContrato.setCpv(cpv);
		        	}
		        	
		      	}
			}
			if(!insertado){
				datosContrato = null;
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

	public static Lotes getLotes(IClientContext cct, String numexp) throws ISPACRuleException {
		Lotes lotes = new Lotes();
		Lote[] vLotes = null;
		try{
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			Iterator<IItem> itDatosContratos = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_DATOS_CONTRATO", "NUMEXP='"+numexp+"'");
			while(itDatosContratos.hasNext()){
				IItem datosContrato = itDatosContratos.next();				
				
				if(datosContrato.getString("LOTES")!=null && datosContrato.getString("LOTES").equals("SI")){
					lotes.setTieneLotes(true);
					
					if(datosContrato.getString("LOTES_OFERTAR")!=null){
						String lotesOfert = datosContrato.getString("LOTES_OFERTAR");
						String [] vcritSolv = lotesOfert.split(" - ");
						if(vcritSolv.length >1){
							lotes.setSeDebeOfertar(new Campo(vcritSolv[0], vcritSolv[1]));
						}
					}
					if(datosContrato.getString("LOTES_NUMERO")!=null){
						lotes.setNumLotes(Integer.parseInt(datosContrato.getString("LOTES_NUMERO")));
					}
					if(datosContrato.getString("LOTES_NUM_OFERTAR")!=null){
						lotes.setNumeroMaximoPresentacion(Integer.parseInt(datosContrato.getString("LOTES_NUM_OFERTAR")));
					}
					
					if(datosContrato.getString("LOTES_NUM_MAX")!=null){
						lotes.setNumeroMaximoAdjudicacion(Integer.parseInt(datosContrato.getString("LOTES_NUM_MAX")));
					}
					if(datosContrato.getString("LOTES_PODER")!=null){
						lotes.setDetallePosibilidadAdjudicacion(datosContrato.getString("LOTES_PODER"));
					}					
					
					IItemCollection itcollection = ExpedientesRelacionadosUtil.getExpedientesByRelacion(entitiesAPI, numexp, "Lote - %");
					vLotes = new Lote[itcollection.toList().size()];
					Iterator<?> itRelacionadosLotes = itcollection.iterator();
					int j = 0;
					while(itRelacionadosLotes.hasNext()){
						IItem itemRelLotes = (IItem) itRelacionadosLotes.next();
						String numHijoLote = null;
						if(StringUtils.isNotEmpty(itemRelLotes.getString("NUMEXP_HIJO"))) numHijoLote = itemRelLotes.getString("NUMEXP_HIJO");
						if(StringUtils.isNotEmpty(numHijoLote)){
							Iterator<?> itLote = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_LOTES", "NUMEXP='"+numHijoLote+"'");
							while(itLote.hasNext()){
								Lote lote = new Lote();
								IItem itemLote = (IItem) itLote.next();
								if(itemLote.getInt("ID")>0){
									int idCodiceEmpresa = itemLote.getInt("ID");
									String strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_LOTES_S WHERE REG_ID = "+idCodiceEmpresa+"";
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
							        		lote.setCpv(cpv);
							        	}
							        	
							      	}
								}						
								if(StringUtils.isNotEmpty(itemLote.getString("DESCRIPCION"))) lote.setDescripcion(itemLote.getString("DESCRIPCION"));
								if(itemLote.getInt("NUM_LOTE")>0)lote.setIdLote(itemLote.getInt("NUM_LOTE")+"");
								
								Campo pais = new Campo();
								pais.setId("ES");
								pais.setValor("España");
								lote.setLugarEjecucionContratoPais(pais);						
								lote.setLugarEjecucionContratoPaisNUTS(pais);
								//Provincia contrato
								String strQuery = "WHERE NUMEXP='" + numexp + "'";
								IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_CONTRATO", strQuery);
								Iterator<?> it = collection.iterator();
								
								if (it.hasNext()) {
									IItem iDatosContrato = (IItem) it.next();
									String lugarEjecucionContrato="";
									if(StringUtils.isNotEmpty(iDatosContrato.getString("PROVINCIA_CONTRATO")))lugarEjecucionContrato = iDatosContrato.getString("PROVINCIA_CONTRATO");
									LOGGER.warn("lugarEjecucionContrato "+lugarEjecucionContrato);
									String [] vlugarEjecucionContrato = lugarEjecucionContrato.split(" - ");
									if(vlugarEjecucionContrato.length >1){
										Campo campo = new Campo();
										campo.setId(vlugarEjecucionContrato[0]);
										campo.setValor(vlugarEjecucionContrato[1]);
										lote.setLugarEjecucionContratoProvincia(campo);
									}
									
								}
								
								if(StringUtils.isNotEmpty(itemLote.getString("IMPORTE_CONIVA")))lote.setPresupuestoConIva(Double.parseDouble(itemLote.getString("IMPORTE_CONIVA")));
								if(StringUtils.isNotEmpty(itemLote.getString("IMPORTE_SINIVA")))lote.setPresupuestoSinIva(Double.parseDouble(itemLote.getString("IMPORTE_SINIVA")));
								
								lote.setDatosTramitacion(DipucrFuncionesComunesSW.getDatosTramitacion(cct, numHijoLote, null));
								
								vLotes[j] = lote;
								j++;
								
							}
						}
						
					}
					lotes.setLotes(vLotes);
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
		
		return lotes;
	}
	
	
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
	public static void obtenerParticipantesCPV(ClientContext cct, int cpv, String numExpHijo) throws ISPACRuleException {
		try{
			//DPCR2018/1
//    		String [] vNumexp = numExpHijo.split("/");
//    		String year = "";
//    		if(vNumexp.length>0){
//    			year = vNumexp[0];
//    		}
			String strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_DATOS_CONTRATO_S WHERE REG_ID = "+cpv+"";
	        ResultSet datos = cct.getConnection().executeQuery(strQuery).getResultSet();
	        String valorCPV = "";
//	        boolean inserta = false;
	        if(datos!=null)
	      	{
	        	while(datos.next()){
	          		if (datos.getString("VALUE")!=null) valorCPV = datos.getString("VALUE");
	          		strQuery="SELECT FIELD,REG_ID,VALUE FROM CONTRATACION_CMENOR_CPV_S WHERE VALUE = '"+valorCPV+"'";
	    	        ResultSet datosRegContra = cct.getConnection().executeQuery(strQuery).getResultSet();
	    	        if(datosRegContra!=null){
	    	        	while(datosRegContra.next()){
	    	        		int regIdRegContra = 0;
	    	        		if (datosRegContra.getInt("REG_ID")>0) regIdRegContra = datosRegContra.getInt("REG_ID");
	    	        		
	    	        		strQuery = "ID=" + regIdRegContra;
	    	        		Iterator<IItem> itRegContr = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_CMENOR_CPV", strQuery);
	    	    			if (itRegContr.hasNext()) {
	    	    				IItem iContMenorCPV = itRegContr.next();
	    	    				String numexpRegContMenor = iContMenorCPV.getString("NUMEXP");
	    	    				ParticipantesUtil.importarParticipantes(cct, cct.getAPI().getEntitiesAPI(), numexpRegContMenor, numExpHijo);
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
			LOGGER.error("Error en el numero expediente "+numExpHijo+" - "+e.getMessage(),e);
			throw new ISPACRuleException("Error en el numero expediente "+numExpHijo+" - "+e.getMessage(),e);
		} catch (SQLException e) {
			LOGGER.error("Error en el numero expediente "+numExpHijo+" - "+e.getMessage(),e);
			throw new ISPACRuleException("Error en el numero expediente "+numExpHijo+" - "+e.getMessage(),e);
		}
	}
	
	public static Documento[] docInformacionAdicionalPliego(ClientContext cct, String numexp) throws ISPACRuleException {
		
		Documento[] docAdicional = null;
		
		try{
			// --------------------------------------------------------------------
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
		
			//Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexp+"' AND RELACION='Petición Contrato'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<?> itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = (IItem) itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        	
	        	IItem exp = entitiesAPI.getExpedient(numexpPetCont);
	        	
	        	//codigo del trámite -> inf-nec-cont-adm -> Informe razonado de la necesidad del contrato administrativo
	        	IItem tramite = TramitesUtil.getTramiteByCode(cct, numexp, "inf-nec-cont-adm");
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
						
						Documento documentoAdicional = DipucrFuncionesComunesSW.getDocumento(cct, docPres, descripcion, numexp);

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
			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Expediente. "+numexp +" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Expediente. "+numexp +" - "+ e.getMessage(), e);
		}
		return docAdicional;
		
	}

	public static void comprobarDatosEnvioPLACE(IClientContext cct, String numexp) throws ISPACException, DatatypeConfigurationException {
		comprobarNombreAdjudicatarioIgualNombreParticipante(cct, numexp);				
	}

	private static void comprobarNombreAdjudicatarioIgualNombreParticipante(IClientContext cct, String numexp) throws ISPACException, DatatypeConfigurationException {
		DatosTramitacion datosTramitacion = DipucrFuncionesComunesSW.getDatosTramitacion(cct, numexp, null);
		LicitadorBean[] licitadores = DipucrFuncionesComunesSW.getLicitadores(cct, numexp);
		
		String empresaAdj = null;
		String NIFAdjudic = null;
		boolean participantesAdj = false;
		String nombreLicitadorParticipante = "";
		if(licitadores!=null && datosTramitacion!=null){
			//Compruebo al adjudicatario
			if(datosTramitacion.getLicitador()!=null && datosTramitacion.getLicitador().length==1){
				if(StringUtils.isEmpty(datosTramitacion.getLicitador()[0].getTipoIdentificador())){
					throw new ISPACRuleException(" Falta por rellenar -> Pestaña Datos de Tramitación/NIF de la Adjudicataria del contrato hay que especificar si es CIF; NIE; NIF; OTROS");
				}
				if(StringUtils.isEmpty(datosTramitacion.getLicitador()[0].getIdentificador())){
					throw new ISPACRuleException(" Falta por rellenar -> Pestaña Datos de Tramitación/NIF de la Adjudicataria del contrato hay que especificar el identificador");
				}
				if(StringUtils.isEmpty(datosTramitacion.getLicitador()[0].getNombre())){
					throw new ISPACRuleException(" Falta por rellenar -> Pestaña Datos de Tramitación/Empresa Adjudicataria del Contrato");
				}
				NIFAdjudic = datosTramitacion.getLicitador()[0].getIdentificador();
				empresaAdj = datosTramitacion.getLicitador()[0].getNombre();
				for(int i=0; i<licitadores.length; i++){
					LicitadorBean licitador = licitadores[i];
					if(licitador!=null && StringUtils.isNotEmpty(licitador.getIdentificador()) && StringUtils.isNotEmpty(licitador.getNombre())){
						nombreLicitadorParticipante = licitador.getNombre();
						if(licitador.getIdentificador().equals(NIFAdjudic)){
							if(licitador.getNombre().equals(empresaAdj)){
								participantesAdj = true;									
							}
						}
					}
					if(datosTramitacion.getLicitador()[0].getFechaAdjudicacion()==null){						
						LOGGER.error("Error en el calculo de la fecha de adjudicacion es nula.");
						throw new DatatypeConfigurationException("Falta por introducir -> Pestaña Datos de Tramitación / Fecha Adjudicación es nula.");
					}
					if(datosTramitacion.getLicitador()[0].getImporteConImpuestos()!=null){
						try{
							new BigDecimal(datosTramitacion.getLicitador()[0].getImporteConImpuestos());
						} catch (NumberFormatException e) {
							LOGGER.error("Valor del importe licitadorBean.getImporteConImpuestos(). (Formato del importe para los decimales con punto, ejemplo 61746.69)"+datosTramitacion.getLicitador()[0].getImporteConImpuestos()+" - "+ e.getMessage(), e);
							throw new NumberFormatException("Pestaña Datos de Tramitación  - Importe de Adjudicación (con IVA). (Formato del importe para los decimales con punto, ejemplo 61746.69)"+datosTramitacion.getLicitador()[0].getImporteConImpuestos()+" - "+ e.getMessage());
						}
					}else{
						LOGGER.error("Pestaña Datos de Tramitación - Adjudicatario - importe con IVA");
						throw new NullPointerException("Falta el campo Pestaña 'Datos de Tramitación' -> Importe de adjudicación (con IVA)");
					}
					if(datosTramitacion.getLicitador()[0].getImporteSinImpuestos()!=null){
						try{
							new BigDecimal(datosTramitacion.getLicitador()[0].getImporteSinImpuestos());
						} catch (NumberFormatException e) {
							LOGGER.error("Valor del importe licitadorBean.getImporteSinImpuestos(). (Formato del importe para los decimales con punto, ejemplo 61746.69)"+datosTramitacion.getLicitador()[0].getImporteSinImpuestos()+" - "+ e.getMessage(), e);
							throw new NumberFormatException("Pestaña Datos de Tramitación  - Importe de Adjudicación (sin IVA) (Formato del importe para los decimales con punto, ejemplo 61746.69)."+datosTramitacion.getLicitador()[0].getImporteSinImpuestos()+" - "+ e.getMessage());
						}
					}
					else{
						LOGGER.error("Pestaña Datos de Tramitación - Adjudicatario - - sin IVA es nulo");
						throw new NullPointerException("Falta el campo Pestaña 'Datos de Tramitación' -> Importe de adjudicación (sin IVA)");
					}
					if(datosTramitacion.getLicitador()[0].getNombre()==null || datosTramitacion.getLicitador()[0].getNombre()==""){
						throw new NullPointerException("Falta el campo Pestaña 'Datos de Tramitación' -> 'Empresa Adjudicataria del Contrato'");
					}
					if(datosTramitacion.getLicitador()[0].getIdentificador()==null || datosTramitacion.getLicitador()[0].getTipoIdentificador()==null || datosTramitacion.getLicitador()[0].getTipoIdentificador()==""){
						throw new NullPointerException("Falta el campo Pestaña 'Datos de Tramitación' -> 'NIF de la Adjudicataria'");
					}
					if(datosTramitacion.getLicitador()[0].getCp()==null){
						throw new NullPointerException("Falta el campo Pestaña 'Datos de Tramitación' -> 'ADJUDICACIÓN / C.P'");
					}
					if(datosTramitacion.getLicitador()[0].getPais()==null){
						throw new NullPointerException("Falta el campo Pestaña 'Datos de Tramitación' -> 'ADJUDICACIÓN / Pais'");
					}
					if(datosTramitacion.getLicitador()[0].getNUTS()==null){
						throw new NullPointerException("Falta el campo Pestaña 'Datos de Tramitación' -> 'ADJUDICACIÓN / Ciudad'");
					}
				}
			}
		}
		if(!participantesAdj){
			throw new ISPACRuleException("El adjudicatario tiene que tener el mismo nombre en los participantes. CIF - "+NIFAdjudic+ " -> "+ " *Pestaña Datos de Tramitación/Empresa Adjudicataria del contrato - "+empresaAdj+ " *Pestaña Participantes/Nombre - "+nombreLicitadorParticipante);
		}
	}

	public static void creacionLicitador(IClientContext cct,int idCtProcedimientoNuevo, String numexp,Adjudicatario adjudicatario, String entidad) throws ISPACRuleException {
		try{
			IItem licitador = ExpedientesRelacionadosUtil.iniciaExpedienteRelacionadoHijo(cct, idCtProcedimientoNuevo, numexp, Constantes.RELACION_PLICA, false, null);
			Tercero tercero = new Tercero();
			DireccionPostal direccionPostal = new DireccionPostal();
			if(StringUtils.isNotEmpty(adjudicatario.getNombre())){
				licitador.set("ASUNTO", adjudicatario.getNombre());
				licitador.set("IDENTIDADTITULAR", adjudicatario.getNombre());
				tercero.setNombre(adjudicatario.getNombre());
			}
			if(StringUtils.isNotEmpty(adjudicatario.getIdentificador())){
				licitador.set("NIFCIFTITULAR", adjudicatario.getIdentificador());
				tercero.setIdentificacion(adjudicatario.getIdentificador());
			}
			if(StringUtils.isNotEmpty(adjudicatario.getMail()))licitador.set("DIRECCIONTELEMATICA", adjudicatario.getMail());
			if(StringUtils.isNotEmpty(adjudicatario.getDireccionPostal())){
				licitador.set("DOMICILIO", adjudicatario.getDireccionPostal());
				direccionPostal.setDireccionPostal(adjudicatario.getDireccionPostal());
			}
			if(StringUtils.isNotEmpty(adjudicatario.getMunicipio())){
				licitador.set("CIUDAD", adjudicatario.getMunicipio());
				direccionPostal.setMunicipio(adjudicatario.getMunicipio());
			}
			if(StringUtils.isNotEmpty(adjudicatario.getProvincia())){
				licitador.set("REGIONPAIS", adjudicatario.getProvincia());
				direccionPostal.setProvincia(adjudicatario.getProvincia());
			}
			if(StringUtils.isNotEmpty(adjudicatario.getCodigoPostal())){
				licitador.set("CPOSTAL", adjudicatario.getCodigoPostal());
				direccionPostal.setCodigoPostal(adjudicatario.getCodigoPostal());
			}
			if(StringUtils.isNotEmpty(adjudicatario.getTelefono())){
				licitador.set("TFNOFIJO", adjudicatario.getTelefono());
				direccionPostal.setTelefono(adjudicatario.getTelefono());
			}					
			licitador.store(cct);	
			tercero.setDireccionPostalPredeterminada(direccionPostal);		
			
			ParticipantesUtil.insertarParticipanteByNIFValidadoNoValidado(cct, licitador.getString("NUMEXP"), adjudicatario.getIdentificador(), ParticipantesUtil._TIPO_INTERESADO, 
					ParticipantesUtil._TIPO_PERSONA_JURIDICA, adjudicatario.getMail(), null, tercero);
			
			if(adjudicatario.getDocumento()!=null){
				int idTramite = TramitesUtil.crearTramite((ClientContext) cct, "Anex-Doc", licitador.getString("NUMEXP"));
				ServicioTramitacion oServicio = LocalizadorServicios.getServicioTramitacion();
				DocumentoExpediente[] docsExpediente = new DocumentoExpediente[1];
				docsExpediente[0] = adjudicatario.getDocumento();
				boolean anexaTramite = oServicio.anexarDocsTramite(entidad, licitador.getString("NUMEXP"), idTramite, null, new Date(), docsExpediente);
				if(anexaTramite){
					TramitesUtil.cerrarTramite(cct, idTramite, numexp);
				}
				
			}
		} catch (ISPACException e) {
			LOGGER.error("Error Entidad " +entidad + "; Expediente "+ numexp +" - " + e.getMessage(),e);
			throw new ISPACRuleException("Error Entidad " + entidad + "; Expediente "+ numexp +" - " + e.getMessage(),e);
		} catch (SigemException e) {
			LOGGER.error("Error Entidad " + entidad + "; Expediente "+ numexp +" - " + e.getMessage(),e);
			throw new ISPACRuleException("Error Entidad " + entidad + "; Expediente "+ numexp +" - " + e.getMessage(),e);
		} 	
	}
	
}