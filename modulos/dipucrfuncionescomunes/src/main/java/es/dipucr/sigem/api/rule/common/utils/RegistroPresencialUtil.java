package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IRegisterAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.registro.DocumentInfo;
import ieci.tecdoc.sgm.core.services.registro.FieldInfo;
import ieci.tecdoc.sgm.core.services.registro.PersonInfo;
import ieci.tecdoc.sgm.core.services.registro.RegisterQueryInfo;
import ieci.tecdoc.sgm.core.services.registro.RegisterWithPagesInfo;
import ieci.tecdoc.sgm.core.services.registro.ServicioRegistro;
import ieci.tecdoc.sgm.core.services.registro.UserInfo;
import ieci.tecdoc.sgm.core.services.repositorio.ContenedorDocumento;
import ieci.tecdoc.sgm.core.services.repositorio.DocumentoInfo;
import ieci.tecdoc.sgm.core.services.repositorio.ServicioRepositorioDocumentosTramitacion;
import ieci.tecdoc.sgm.core.services.telematico.Registro;
import ieci.tecdoc.sgm.core.services.telematico.RegistroDocumento;
import ieci.tecdoc.sgm.core.services.telematico.RegistroDocumentos;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;
import ieci.tecdoc.sgm.entidades.EntidadesManager;
import ieci.tecdoc.sgm.entidades.exception.EntidadException;
import ieci.tecdoc.sgm.rde.exception.GuidIncorrectoExcepcion;
import ieci.tecdoc.sgm.rde.exception.RepositorioDocumentosExcepcion;
import ieci.tecdoc.sgm.registro.exception.RegistroExcepcion;
import ieci.tecdoc.sgm.registro.util.database.RegistroDocumentoDatos;
import ieci.tecdoc.sgm.registropresencial.utils.RBUtil;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;
import es.dipucr.sigem.api.rule.common.AccesoBBDDeTramitacion;


/*
fld1	=> Número de registro
fld2	=> Fecha de registro
fld3	=> Usuario
fld4	=> Fecha de trabajo
fld5	=> Oficina de registro
fld6	=> Estado
fld7	=> Origen
fld8	=> Destino
fld9	=> Remitentes
fld10	=> Nº. registro original
fld11	=> Tipo de registro original
fld12	=> Fecha de registro original
fld13	=> Registro original
fld14	=> Tipo de transporte
fld15	=> Número de transporte
fld16	=> Tipo de asunto
fld17	=> Resumen
fld18	=> Comentario
fld19	=> Referencia de Expediente
fld20	=> Fecha del documento
*/

public class RegistroPresencialUtil {
	
	private static final Logger LOGGER = Logger.getLogger(RegistroPresencialUtil.class);
				
	public static boolean modificaDestinoRegistroEntrada(IClientContext cct, String nreg, String codDepartamento) throws ISPACException{
		boolean resultado = false;
		
		try {
			resultado = modificaRegistroEntrada(cct, nreg, "8", codDepartamento, "", "");
			
		} catch (ISPACException e) {
			LOGGER.error("Error al modificar el destino: " + codDepartamento + " del registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al modificar el destino: " + codDepartamento + " del registro: " + nreg + ". " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al modificar el destino: " + codDepartamento + " del registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al modificar el destino: " + codDepartamento + " del registro: " + nreg + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	public static boolean modificaInteresadoRegistroEntrada(IClientContext cct, String nreg, String nif, String nombre) throws ISPACException{
		boolean resultado = false;
		String nombreCompleto = nif + " " + nombre;
		
		try {
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			AccesoBBDDRegistro registro = new AccesoBBDDRegistro(entidad);
			registro.modificaInteresadoRegistroEntrada(nreg, nombreCompleto);
			
			AccesoBBDDeTramitacion eTramitacion = new AccesoBBDDeTramitacion(entidad);
			eTramitacion.modificaInteresadoRegistroEntrada(nreg, nif, nombre);
			
		} catch (ISPACException e) {
			String error = "Error al modificar el interesado: " + nombreCompleto + " del registro: " + nreg + ". " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACException(error, e);
		} catch (Exception e) {
			String error = "Error al modificar el destino: " + nombreCompleto + " del registro: " + nreg + ". " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACException(error, e);
		}
		return resultado;
	}
	
	public static boolean modificaResumenRegistroEntrada(IClientContext cct, String nreg, String resumen) throws ISPACException{
		boolean resultado = false;
		try {
			if(StringUtils.isEmpty(resumen)){
				resumen = "";
			} else{
				if(resumen.length() > 250){
					resumen = resumen.substring(0, 249);
				}
				resultado = modificaRegistroEntrada(cct, nreg, "17", resumen, "", "");
			}			
		} catch (ISPACException e) {
			LOGGER.error("Error al modificar el resumen: " + resumen + " del registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al modificar el resumen: " + resumen + " del registro: " + nreg + ". " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al modificar el resumen: " + resumen + " del registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al modificar el resumen: " + resumen + " del registro: " + nreg + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	public static boolean modificaComentarioRegistroEntrada(IClientContext cct, String nreg, String comentario) throws ISPACException{
		boolean resultado = false;
		try {
			if(StringUtils.isEmpty(comentario)){
				comentario = "";
			} else{
				if(comentario.length() > 250){
					comentario = comentario.substring(0, 249);
				}
				resultado = modificaRegistroEntrada(cct, nreg, "18", comentario, "", "");
			}			
		} catch (ISPACException e) {
			LOGGER.error("Error al modificar el comentario: " + comentario + " del registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al modificar el comentario: " + comentario + " del registro: " + nreg + ". " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al modificar el comentario: " + comentario + " del registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al modificar el comentario: " + comentario + " del registro: " + nreg + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	public static boolean modificaRegistroEntrada(IClientContext cct, String nreg, String fieldId, String value, String registryUserName, String registryUserPassword) throws ISPACException{

		boolean resultado = false; 
		
		ServicioRepositorioDocumentosTramitacion servicioRde = null;
		ServicioRegistroTelematico servicioRegistroTelematico = null;
		ServicioRegistro servicioRegistro = null;
		
		// Formateador de fechas en formato largo
		SimpleDateFormat longFormatter = null; 
		
		// Formateador de fechas en formato largo
		SimpleDateFormat shortFormatter = null;
		
		Entidad entidad = null;
		Integer fdrid = null;
		
		try {
			servicioRde = LocalizadorServicios.getServicioRepositorioDocumentosTramitacion();
			servicioRegistroTelematico = LocalizadorServicios.getServicioRegistroTelematico();
			servicioRegistro = LocalizadorServicios.getServicioRegistro();
						
			longFormatter = new SimpleDateFormat(RBUtil.getInstance(null).getProperty(ieci.tecdoc.sgm.registropresencial.utils.Keys.I18N_DATE_LONGFORMAT));
			longFormatter.setLenient(false);
			
			shortFormatter = new SimpleDateFormat(RBUtil.getInstance(null).getProperty(ieci.tecdoc.sgm.registropresencial.utils.Keys.I18N_DATE_SHORTFORMAT));
			shortFormatter.setLenient(false);
			
			ieci.tecdoc.sgm.entidades.beans.Entidad ent = EntidadesManager.obtenerEntidad(EntidadesAdmUtil.obtenerEntidad(cct));
			entidad = new Entidad();
			entidad.setIdentificador(ent.getIdentificador());
			entidad.setNombre(ent.getNombreLargo());
			
			if(StringUtils.isEmpty(registryUserName))
				registryUserName = "REGISTRO_TELEMATICO";
			if(StringUtils.isEmpty(registryUserPassword))
				registryUserPassword = "*";
			
			// Información del usuario de registro presencial
	    	UserInfo user = new UserInfo();
	    	user.setUserName(registryUserName);
	    	user.setPassword(registryUserPassword);
			
			
			RegisterWithPagesInfo registroWithPagesInfo = null;
			try{
				registroWithPagesInfo = servicioRegistro.getInputFolderForNumber(user, IRegisterAPI.BOOK_TYPE_INPUT, nreg, entidad);
			}
			catch(Exception e){
				LOGGER.error("Error al recuperar el registro de entrada: " + nreg + ". ", e);
			}
			
			if(registroWithPagesInfo != null) {
				
				for(int i=0; i< registroWithPagesInfo.getRqInfo().length; i++){
					if("FDRID".equalsIgnoreCase(((RegisterQueryInfo)registroWithPagesInfo.getRqInfo()[i]).getFolderName())){
						fdrid = new Integer(Integer.parseInt(((RegisterQueryInfo)registroWithPagesInfo.getRqInfo()[i]).getValue()));
					}
				}
				if(fdrid == null || fdrid.intValue() == 0){
					LOGGER.warn("No se ha podido actualizar el destino del registro: " + nreg + ". ");
				} else{				
					Registro registro = servicioRegistroTelematico.obtenerRegistro("", nreg, entidad);
					// Obtener los ficheros del registro telemático
			        RegistroDocumentos documentos = servicioRegistroTelematico.obtenerDocumentosRegistro(registro.getRegistryNumber(), entidad);
					
					// Información de los intervinientes
			    	PersonInfo[] personInfos = getPersons(registro);
					FieldInfo [] fieldInfo = getFieldInfo(fieldId, value);
					DocumentInfo [] documentInfo = getDocuments(documentos, entidad, servicioRde);
					
					//INICIO [dipucr-Felipe #1014/#1101]
					//Para los documentos eliminados de la eTramitacion, tenemos que hacer lo siguiente 
					RegistroDocumentoDatos rdd = new RegistroDocumentoDatos();

					for(DocumentInfo doc: documentInfo){
						//Actualizamos el contenido del documento
						if (doc.getDocumentContent().length <= 0) {
							rdd.load(registro.getRegistryNumber(), doc.getDocumentName(), entidad.getIdentificador());
							DocumentoInfo docInfo = servicioRde.retrieveDocument("", rdd.getGuid(), entidad);
							doc.setDocumentContent(docInfo.getContent());
						}
					}
					//FIN [dipucr-Felipe #1014/#1101]
					
					servicioRegistro.updateFolder(user, new Integer(IRegisterAPI.BOOK_TYPE_INPUT), fdrid, fieldInfo, personInfos, documentInfo, entidad);
					resultado = true;
				}
			}
		} catch (SigemException e) {
			LOGGER.error("Error al instanciar el servicio al modificar el registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al instanciar el servicio al modificar el registro: " + nreg + ". " + e.getMessage(), e);
		} catch (EntidadException e) {
			LOGGER.error("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
		} catch (RegistroExcepcion e) {
			LOGGER.error("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
		} catch (GuidIncorrectoExcepcion e) {
			LOGGER.error("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
		} catch (RepositorioDocumentosExcepcion e) {
			LOGGER.error("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar la entidad para modificar el registro: " + nreg + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	protected static FieldInfo[] getFieldInfo(String fieldId, String value) {
    	FieldInfo fieldInfo = new FieldInfo();
    	fieldInfo.setFieldId(fieldId);
    	fieldInfo.setValue(value);
    	return new FieldInfo[] {fieldInfo};
    }
	
	protected static DocumentInfo[] getDocuments(RegistroDocumentos documentos, Entidad entidad, ServicioRepositorioDocumentosTramitacion servicioRde) 
    		throws SigemException {
    	
    	DocumentInfo[] docs = new DocumentInfo[documentos.count()];
    	
    	for (int i = 0; i < documentos.count(); i++) {
    		
    		// Información del documento en el registro telemático
    		RegistroDocumento documento = documentos.get(i);
    		
    		// Información del contenedor del documento
    		ContenedorDocumento contenedor = servicioRde.retrieveDocumentInfo("", documento.getGuid(), entidad);
    		
    		// Información del documento en el registro presencial
    		docs[i] = new DocumentInfo();
    		docs[i].setDocumentName(documento.getCode());
    		docs[i].setExtension(contenedor.getExtension());
    		docs[i].setDocumentContent(contenedor.getContent());
    		docs[i].setFileName(documento.getRegistryNumber() + "_" + documento.getCode() + "_" + i);
    		docs[i].setPageName(documento.getCode());
    	}
    	
    	return docs;
    }
	
	protected static PersonInfo[] getPersons(Registro registro) {
		
		int nameMaxLength = 80;	
		
		PersonInfo[] persons = null;
		
		if (registro != null) {
			
			// Componer el nombre del interviniente
			String personName = StringUtils.trim(
					StringUtils.defaultString(registro.getSenderId(), "")
					+ " " 
					+ StringUtils.defaultString(registro.getName(), ""));
			
			if (personName.length() > nameMaxLength) {
			
				String person1Name = StringUtils.trim(StringUtils.substringBeforeLast(
						StringUtils.substring(personName, 0, nameMaxLength), " "));
				String person2Name = StringUtils.trim(StringUtils.substringAfterLast(
						personName, person1Name));

				persons = new PersonInfo[] { 
						getPersonInfo(person1Name), 
						getPersonInfo(person2Name) 
				};

			} else {
				persons = new PersonInfo[] { getPersonInfo(personName) };
			}
		}
		
		return persons;
	}
	
	protected static PersonInfo getPersonInfo(String personName) {
		PersonInfo person = new PersonInfo();
		person.setPersonName(personName);
		return person;
	}
	
	public static String getCodDepartamentoById(IClientContext cct, int idDepartamento) throws ISPACException{
		AccesoBBDDRegistro acc;
		Entidad entidad = null;
		String codDep = "";
		try {
			ieci.tecdoc.sgm.entidades.beans.Entidad ent = EntidadesManager.obtenerEntidad(EntidadesAdmUtil.obtenerEntidad(cct));
			entidad = new Entidad();
			entidad.setIdentificador(ent.getIdentificador());
			entidad.setNombre(ent.getNombreLargo());
			
			acc = new AccesoBBDDRegistro(entidad.getIdentificador());
			codDep = acc.getCodeDepByIdDep(idDepartamento);
		} catch (ISPACException e) {
			LOGGER.error("Error al obtener el código del departamento con id: " + idDepartamento + ". " + e.getMessage(), e);
			throw new ISPACException("Error al obtener el código del departamento con id: " + idDepartamento + ". " + e.getMessage(), e);
		} catch (EntidadException e) {
			LOGGER.error("Error al recuperar la entidad para obtener el departamento con id: " + idDepartamento + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar la entidad para obtener el departamento con id: " + idDepartamento + ". " + e.getMessage(), e);
		}
		return codDep;
	}
}
