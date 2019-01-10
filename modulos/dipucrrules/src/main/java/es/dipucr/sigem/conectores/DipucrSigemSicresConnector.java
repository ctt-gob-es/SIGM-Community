package es.dipucr.sigem.conectores;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.sicres.vo.Organization;
import ieci.tdw.ispac.ispaclib.sicres.vo.Register;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterInfo;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterOffice;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterType;
import ieci.tdw.ispac.ispaclib.sicres.vo.ThirdPerson;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.DateUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.registro.DocumentInfo;
import ieci.tecdoc.sgm.core.services.registro.FieldInfo;
import ieci.tecdoc.sgm.core.services.registro.PersonInfo;
import ieci.tecdoc.sgm.core.services.registro.RegistroException;
import ieci.tecdoc.sgm.core.services.registro.ServicioRegistro;
import ieci.tecdoc.sgm.tram.sicres.DocumentInfoAdapter;
import ieci.tecdoc.sgm.tram.sicres.SigemSicresConnector;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.comparece.CompareceConfiguration;
import es.dipucr.sigem.api.rule.common.utils.CompareceUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

public class DipucrSigemSicresConnector extends SigemSicresConnector{
	
	private static final Logger logger = Logger.getLogger("loggerRegistro");
	
	/**
	 * Identificador del libro de salida por defecto.
	 */
	private static final int DEFAULT_OUTPUT_BOOK_ID = 2;

	/**
	 * Contexto de cliente.
	 */
	private ClientContext ctx = null;
	
	/**
	 * Servicio de acceso a SICRES.
	 */
	private ServicioRegistro servicioRegistro = null;

	public DipucrSigemSicresConnector(ClientContext ctx) throws ISPACException {
		super(ctx);
		this.ctx = ctx;
		
		try {
			// Servicio de Registro Presencial
			servicioRegistro = LocalizadorServicios.getServicioRegistro();
		} catch (SigemException e) {
			logger.error("Error al crear el servicio de registro", e);
			throw new ISPACException("Error al crear el servicio de registro", e);
		}
	}
	
	private static FieldInfo createFieldInfo(String fieldId, String value) {
		FieldInfo fieldInfo = new FieldInfo();
		fieldInfo.setFieldId(fieldId);
		fieldInfo.setValue(value);
		return fieldInfo;
	}

	private static RegisterInfo getRegisterInfo( ieci.tecdoc.sgm.core.services.registro.RegisterInfo regInfo) {
		RegisterInfo registerInfo = null;

		if (regInfo != null) {

			RegisterOffice regOffice = null;
			if (StringUtils.isNotBlank(regInfo.getOffice())) {
				regOffice = new RegisterOffice();
				regOffice.setCode(regInfo.getOffice());
				regOffice.setName(regInfo.getOfficeName());
			}

			registerInfo = new RegisterInfo(regOffice, regInfo.getNumber(), DateUtil.getCalendar(regInfo.getDate(), "dd-MM-yyyy HH:mm:ss"), RegisterType.SALIDA);
		}

		return registerInfo;
	}
	
	private DocumentInfo[] convertDocumentInfo(
			ieci.tdw.ispac.ispaclib.sicres.vo.DocumentInfo[] docs) {
		if (docs == null || docs.length == 0) {
			return null;
		}
		DocumentInfo[] result = new DocumentInfo[docs.length];
		for (int i = 0; i < docs.length; i++) {
			DocumentInfo doc = new DocumentInfoAdapter(docs[i]);
			result[i] = doc;
		}
		return result;
	}
	
	
	public RegisterInfo insertRegister(Register register) throws ISPACException {

		RegisterInfo registerInfo = null;

		try {

			if (register != null) {

				PersonInfo addressee = null;

				// Destinatario
				if ((register.getRegisterData() != null) && !ArrayUtils.isEmpty(register.getRegisterData().getParticipants())) {

					ThirdPerson destiny = register.getRegisterData().getParticipants()[0];
					addressee = new PersonInfo();
					addressee.setPersonId(destiny.getId());
					addressee.setPersonName(destiny.getName());
				}

				// Identificador del libro de salida
				int bookId = DEFAULT_OUTPUT_BOOK_ID;
				if (register.getOriginalRegister() != null) {
					bookId = TypeConverter.parseInt(register.getOriginalRegister().getBookId(),	DEFAULT_OUTPUT_BOOK_ID);
				}

				// Campos
				List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();

				// Oficina de registro
				RegisterOffice registerOffice = register.getOriginalRegister().getRegisterOffice();
				if (registerOffice != null) {
					fieldInfoList.add(createFieldInfo("5", registerOffice.getCode()));
				}

				// Origen del registro
				// fieldInfoList.add(createFieldInfo("7", origUnitCode)); //
				// Origen
				Organization orig = register.getOriginOrganization();
				if (orig != null) {
					fieldInfoList.add(createFieldInfo("7", orig.getCode()));
				}

				// Destino del registro
				// fieldInfoList.add(createFieldInfo("8", destUnitCode)); //
				// Destino
				Organization dest = register.getDestinationOrganization();
				if (dest != null) {
					fieldInfoList.add(createFieldInfo("8", dest.getCode()));
				}

				// Destinatario del registro
				if (addressee != null) {
					fieldInfoList.add(createFieldInfo("9", addressee.getPersonName()));
				}

				// Resumen del registro
				if (register.getRegisterData() != null) {
					fieldInfoList.add(createFieldInfo("13", register.getRegisterData().getSummary()));
				}
				
				// Está involucrado en cambio registral
				fieldInfoList.add(createFieldInfo("503", "0"));
				
				// No acompaña documentación física ni en otros soportes
				fieldInfoList.add(createFieldInfo("506", "1"));

				// Destinatarios
				PersonInfo[] personInfos = null;
				if (addressee != null) {
					personInfos = new PersonInfo[] { addressee };
				}

				// Documentos
				ieci.tdw.ispac.ispaclib.sicres.vo.DocumentInfo[] docs = register.getRegisterData().getInfoDocuments();

				DocumentInfo[] docInfos = convertDocumentInfo(docs);

				ieci.tecdoc.sgm.core.services.registro.RegisterInfo sgmRegisterInfo = servicioRegistro
						.createFolder(getUserInfo(), new Integer(bookId), (FieldInfo[]) fieldInfoList.toArray(new FieldInfo[fieldInfoList.size()]), personInfos,
								docInfos, getEntidad());

				registerInfo = getRegisterInfo(sgmRegisterInfo);
			}
		} catch (RegistroException e) {
			logger.warn("Error al insertar el registro", e);
		} catch (Exception e) {
			logger.error("Error al insertar el registro", e);
			throw new ISPACException("Error al insertar el registro", e);
		}

		return registerInfo;
	}
}
