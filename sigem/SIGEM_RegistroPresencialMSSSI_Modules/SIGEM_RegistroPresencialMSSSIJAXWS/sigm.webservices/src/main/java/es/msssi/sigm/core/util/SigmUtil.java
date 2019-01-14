/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util;

import ieci.tecdoc.sgm.registropresencial.autenticacion.User;
import ieci.tecdoc.sgm.registropresencial.register.RegisterServicesUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import sigm.dao.dataaccess.domain.InputRegisterReportsCert;
import sigm.dao.dataaccess.domain.OutputRegisterReportsCert;
import sigm.dao.dataaccess.domain.XtField;
import sigm.dao.dataaccess.service.SIGMServiceManager;
import sigm.dao.exception.DaoException;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.entity.AxXfEntity;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Iuseruserhdr;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrDistregstate;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.isicres.AxDoch;
import com.ieci.tecdoc.common.isicres.AxPageh;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.AxSfIn;
import com.ieci.tecdoc.common.isicres.AxSfOut;
import com.ieci.tecdoc.common.isicres.AxSfQuery;
import com.ieci.tecdoc.common.isicres.AxSfQueryResults;
import com.ieci.tecdoc.common.isicres.AxXf;
import com.ieci.tecdoc.common.isicres.AxXfPK;
import com.ieci.tecdoc.common.isicres.DtrFdrResults;
import com.ieci.tecdoc.common.isicres.UpdHisFdrResults;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrDocument;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrField;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrFile;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrPage;
import com.ieci.tecdoc.idoc.utils.CryptoUtils;
import com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil;
import com.ieci.tecdoc.isicres.session.distribution.DistributionSession;
import com.ieci.tecdoc.isicres.session.folder.FolderFileSession;
import com.ieci.tecdoc.isicres.session.folder.FolderHistSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;

import es.ieci.tecdoc.fwktd.sir.core.vo.TrazabilidadVO;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.business.vo.enums.EstadoDistribucionEnum;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralEntradaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralSalidaVO;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Representante;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.WebParameter;
import es.msssi.sgm.registropresencial.businessobject.InterestedBo;
import es.msssi.sigm.core.connector.PfeConnector;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.core.util.sigm.AxXfEntityMSSSI;
import es.msssi.sigm.ws.beans.Acuse;
import es.msssi.sigm.ws.beans.DistribucionRegistro;
import es.msssi.sigm.ws.beans.Documento;
import es.msssi.sigm.ws.beans.Documentos;
import es.msssi.sigm.ws.beans.Fichero;
import es.msssi.sigm.ws.beans.FullInfoRegister;
import es.msssi.sigm.ws.beans.HistoricoDistribucion;
import es.msssi.sigm.ws.beans.HistoricoIntercambioRegistral;
import es.msssi.sigm.ws.beans.HistoricoIntercambioRegistralEntrada;
import es.msssi.sigm.ws.beans.HistoricoIntercambioRegistralSalida;
import es.msssi.sigm.ws.beans.HistoricoMovimiento;
import es.msssi.sigm.ws.beans.IntercambioRegistralEntrada;
import es.msssi.sigm.ws.beans.IntercambioRegistralSalida;
import es.msssi.sigm.ws.beans.MovimientoRegistro;
import es.msssi.sigm.ws.beans.PeticionBusqueda;
import es.msssi.sigm.ws.beans.Registro;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoDatos;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoDatosInteresados;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoRepresentante;
import es.msssi.sigm.ws.beans.jaxb.solicitud.InteresadoFisico;
import es.msssi.sigm.ws.beans.jaxb.solicitud.InteresadoJuridico;
import es.msssi.sigm.ws.beans.jaxb.solicitud.RepresentanteFisico;
import es.msssi.sigm.ws.beans.jaxb.solicitud.RepresentanteJuridico;

public class SigmUtil {
	
	static Logger log = Logger.getLogger(SigmUtil.class.getName());

    
    private static final String SELECT_CA_ID = "SELECT ID FROM SCR_CA WHERE CODE = ?";
	
	public static final int NUM_REGISTRO = 1;
	public static final int FECHA_REGISTRO = 2;
	
	// ENTRADA/SALIDA
	private static final int OFICINA_REGISTRO_IN_OUT = 5;
	private static final int ORG_ORIGEN_IN_OUT = 7;
	private static final int ORG_DESTINO_IN_OUT = 8;
	private static final int FECHA_ORIGINAL_IN_OUT = 12;
	
	private static final int TIPO_REGISTRO_ORIGINAL = 11;	//ENTRADA/SALIDA : 1-2
	
	
	// ENTRADA
	private static final int REG_ORIGINAL_IN = 13;
	public static final int TRANSPORTE_IN = 14;
	public static final int NUM_TRANSPORTE_IN = 15;
	private static final int ASUNTO_IN = 16;
	private static final int RESUMEN_IN = 17;
//	private static final int REF_EXPEDIENTE_IN = 19;
//	private static final int FECHA_DOCUMENTACION_IN = 20;
	
	// SALIDA
	// no tiene:  REG_ORIGINAL_OUT = 9;
	private static final int TRANSPORTE_OUT = 10;
	private static final int NUM_TRANSPORTE_OUT = 11;
	private static final int ASUNTO_OUT = 12;
	private static final int RESUMEN_OUT = 13;
//	private static final int FECHA_DOCUMENTACION_OUT = 15;

	private static final String TIPO_LIBRO_ENTRADA = "E";
	private static final String TIPO_LIBRO_SALIDA = "S";
/*	
	public static List<FlushFdrField> crearDataRegister(ElementoSolicitud elementoSolicitud){
		

		List<FlushFdrField> dataRegister = new ArrayList<FlushFdrField>();
		FlushFdrField field = null;
		
		ElementoDatos datos = elementoSolicitud.getDatos();
//		
		
		if(datos.getOficina() != null){
			field = new FlushFdrField();
			field.setFldid(OFICINA_REGISTRO_IN_OUT);
			field.setCtrlid(OFICINA_REGISTRO_IN_OUT);
			field.setValue(datos.getOficina());	//999 (REGISTRO ELECTRONICO)
			dataRegister.add(field);			
		}
		
		if(datos.getOrigen() != null){
			field = new FlushFdrField();
			field.setFldid(ORG_ORIGEN_IN_OUT);
			field.setCtrlid(ORG_ORIGEN_IN_OUT);
			field.setValue(datos.getOrigen()); // EA0008480
			dataRegister.add(field);
		}
		
		
		field = new FlushFdrField();
		field.setFldid(ORG_DESTINO_IN_OUT);
		field.setCtrlid(ORG_DESTINO_IN_OUT);
//		field.setValue(datos.getOrganoDestino());
		field.setValue("MS62602062");
		dataRegister.add(field);
		
		field = new FlushFdrField();
		field.setFldid(TIPO_REGISTRO_ORIGINAL);
		field.setCtrlid(TIPO_REGISTRO_ORIGINAL);
//		if(TIPO_LIBRO_ENTRADA.equals(datos.getLibro()))
		field.setValue("ENTRADA");
		dataRegister.add(field);

		
//		field = new FlushFdrField();
//		field.setFldid(FECHA_ORIGINAL_IN_OUT);
//		field.setCtrlid(FECHA_ORIGINAL_IN_OUT);
//		field.setValue("24-02-2015 10:10:10");
//		dataRegister.add(field);

		
		
		
		int regOriginal, transporte, resumen, asunto;
		
		if(Integer.parseInt(datos.getTipoRegistro()) == 6){//TIPO_LIBRO_ENTRADA){
			regOriginal = REG_ORIGINAL_IN;
			transporte = TRANSPORTE_IN;
			resumen = RESUMEN_IN;
			asunto = ASUNTO_IN;
		}else{
//			regOriginal = REG_ORIGINAL_OUT;
			transporte = TRANSPORTE_OUT;
			resumen = RESUMEN_OUT;						
			asunto = ASUNTO_OUT;
		}
		
		field = new FlushFdrField();
		field.setFldid(asunto);
		field.setCtrlid(asunto);
		field.setValue("TSUB"); // datos.getAsunto;
		dataRegister.add(field);
		
		
		// CAMPOS EXTENDIDOS BASE SIGEM
		// Si es entrada RESUMEN_IN si no RESUMEN_OUT
		field = new FlushFdrField();
		field.setFldid(resumen);
		field.setCtrlid(resumen);
		field.setValue("ESTO ES UN RESUMEN"); // datos.getResumen();
		dataRegister.add(field);
		
		
		
		
//		field = new FlushFdrField();
//		field.setFldid(ORG_ORIGEN_IN_OUT);
//		field.setCtrlid(ORG_ORIGEN_IN_OUT);
//		field.setValue("EA0008480");
////		field.setValue(datos.getOrganoOrigen());
//		dataRegister.add(field);
		
		
//		field = new FlushFdrField();
//		field.setFldid(OFICINA_REGISTRO_IN_OUT);
//		field.setCtrlid(OFICINA_REGISTRO_IN_OUT);
//		field.setValue("999");
//		dataRegister.add(field);
		
//		// 6,7 (ENTRADA/SALIDA)
//		if(datos.getLibro() != null){
//			field = new FlushFdrField();
//			field.setFldid(FullInfoRegister.BOOK_REG_ELETRONICO_IN ORG_ORIGEN_IN_OUT);
//			field.setCtrlid(ORG_ORIGEN_IN_OUT);
//	//		field.setValue("EA0008480");
//			field.setValue(datos.getOrganoOrigen());
//			dataRegister.add(field);
//		}
		
		
//		// Si es entrada COMENTARIO_IN si no COMENTARIO_OUT
//		field = new FlushFdrField();
//		field.setFldid(COMENTARIO_IN);
//		field.setCtrlid(COMENTARIO_IN);
//		field.setValue("ESTO ES UN COMENTARIO");
//		dataRegister.add(field);
//		
//		field = new FlushFdrField();
//		field.setFldid(FECHA_DOCUMENTACION_IN);
//		field.setCtrlid(FECHA_DOCUMENTACION_IN);
//		field.setValue("24-02-2015 10:10:10");
////		field.setValue("MS62602062");
//		dataRegister.add(field);
//
		
		return dataRegister;
				
	}
	 

	private static void crearDataInteresados(String libro, Integer registerId, UseCaseConf useCaseConf, String elementoInteresados) {
		

		InterestedBo interestedBo = new InterestedBo();
		
		Interesado interesado = new Interesado();
		
		
//		elementoInteresados
		
		List<Interesado> interesados = new ArrayList<Interesado>();
		interesado.setTipo("P");
		interesado.setTipodoc("2");
		interesado.setNombre("AJ2");
		interesado.setPapellido("apellliddddooooSSSS");
		interesado.setSapellido("1DOOOSSS apellliddddoooo");
		interesado.setDocIdentidad("122275223W");

		
		Representante representanteInteresado = new Representante();
		representanteInteresado.setNombre("Repre Nombre");
		representanteInteresado.setTipo("P");
		representanteInteresado.setTipodoc("2");
		representanteInteresado.setDocIdentidad("111275223W");
		representanteInteresado.setPapellido("Repre Apell");
		
	
		interesado.setRepresentante(representanteInteresado);
		interesados.add(interesado);
		
		interestedBo.addInterested(interesados, Integer.parseInt(libro), registerId, useCaseConf);			
		
		LOG.debug("Salida interested: "+ interestedBo);		
	}
	*/
	
	/*
	
	public static InfoRegister procesarPeticionRegistro(ElementoSolicitud elementoSolicitud){
		
			LOG.debug("Procesando peticion...");
			
			String nuevoCampo = "CAMPO_NUEVO_31";
			Map<String, String> extendendField = new HashMap<String, String>();
			

			//procesar campos extendidos
			
			int idField =0;
			
			LOG.debug("Procesando CAMPOS EXTENDIDOS...");

			try{
				XtField xtField = SIGMServiceManager.getXtfieldService().getByName(nuevoCampo);
				if(xtField == null){
					xtField = new XtField(nuevoCampo);
					SIGMServiceManager.getXtfieldService().insert(xtField);
					
					LOG.debug("Campo nuevo no encontrado. Id nuevo: "+ xtField.getFldid());
					
				}else
				{
					LOG.debug("No insertamos el registro nuevo en la tabla XtField");
				}
//					SIGMServiceManager.getXtfieldService().insert(new XtField("NUEVO"+new Date().getTime()));
				
				idField = xtField.getFldid();
				
			
			}catch(Exception e){
				e.printStackTrace();
				LOG.error(e);				
			}				
			
		
		extendendField.put(String.valueOf(idField), "VALOR "+nuevoCampo);

		LOG.debug("Procesando DATOS BASICOS A REGISTRAR...");
		
		List<FlushFdrField> listDataRegister = SigmUtil.crearDataRegister(elementoSolicitud);
		
		LOG.debug("Procesando CREAR REGISTRO...");
		
		String libro = String.valueOf(elementoSolicitud.getDatos().getTipoRegistro());
		
		String entidad = Constants.SIGEM_ENTIDAD_MSSSI;
	    User applicationUser = new User();
	    applicationUser.setUserName(Constants.SIGEM_USER_TEST_ECM);		
	    applicationUser.setPassword(Constants.SIGEM_PASSWORD_TEST_ECM);
	    applicationUser.setLocale(new Locale(Constants.SIGEM_LOCALE));
		   
		
		InfoRegister infoRegister = SigmConnector.registrar(applicationUser, entidad, libro, listDataRegister, extendendField);				
		
		Integer registerId = Integer.valueOf(infoRegister.getFolderId());

		
		LOG.debug("Procesando INTERESADOS...");

		
		UseCaseConf useCaseConf = new UseCaseConf();
		useCaseConf.setEntidadId(Constants.SIGEM_ENTIDAD_MSSSI);
		useCaseConf.setLocale(new Locale(Constants.SIGEM_LOCALE));
		useCaseConf.setUserName(Constants.SIGEM_USER_REGISTRO_ELECTRONICO);
		useCaseConf.setPassword(Constants.SIGEM_PASSWORD);
		
		
//		 elementoSolicitud.getDatos().getInteresados()
		String elementoInteresados = "";
		SigmUtil.crearDataInteresados(libro, registerId, useCaseConf, elementoInteresados);
		
				
		return infoRegister;
		
	}
	*/

/*
	public static void procesarPeticionConsulta() {

	    //Buscar registros
	    List searchFields = new ArrayList() ;
	    
	    AxSfQueryField queryField = null;
	    
		//OFICINA DE REGISTRO
	    queryField = new AxSfQueryField();
	    queryField.setFldId(String.valueOf(5));
	    queryField.setBookId(6);
	    queryField.setValue("999");
	    queryField.setOperator("=");
	    searchFields.add(queryField);
	    
//		ORGANO ORIGEN
	    queryField = new AxSfQueryField();
	    queryField.setFldId(String.valueOf(3));
	    queryField.setBookId(6);
	    queryField.setValue("SIGEM");
//	    queryField.setValue("REGISTRO_ELECTRONICO");
	    	    
	    queryField.setOperator("=");
	    searchFields.add(queryField);
	    
	    
		//NUMERO REGISTRO
//	    queryField = new AxSfQueryField();
//	    queryField.setFldId(String.valueOf(1));
//	    queryField.setBookId(6);
////	    queryField.setValue("201599900000411");	    
////	    queryField.setValue("201599900000447"); // CONTIENE EL CAMPO EXTENDIDO 7777
//	    queryField.setValue("201599900000450"); // CONTIENE VARIOS CAMPO EXTENDIDOS

//	    queryField = new AxSfQueryField();
//	    queryField.setFldId(String.valueOf(17));
//	    queryField.setBookId(6);
////	    queryField.setValue("201599900000411");	    
////	    queryField.setValue("201599900000447"); // CONTIENE EL CAMPO EXTENDIDO 7777
//	    queryField.setValue("TEST"); // CONTIENE VARIOS CAMPO EXTENDIDOS
//	    
//	    queryField.setOperator("=");
//	    searchFields.add(queryField);
//	    
//	    queryField = new AxSfQueryField();
//	    queryField.setFldId(String.valueOf(6));
//	    queryField.setBookId(6);
//	    queryField.setValue("1"); // Completo-1, Incompleto-0
//	    queryField.setOperator("=");
//	    searchFields.add(queryField);
	    

		String libro = "6";
		
		String entidad = Constants.SIGEM_ENTIDAD_MSSSI;
	    User applicationUser = new User();
	    applicationUser.setUserName(Constants.SIGEM_USER);		
	    applicationUser.setPassword(Constants.SIGEM_PASSWORD);
	    applicationUser.setLocale(new Locale(Constants.SIGEM_LOCALE));
	    
	    FullInfoRegister[] buscarRegistros = SigmConnector.buscarRegistros(applicationUser, entidad, libro, searchFields);
	    
    	System.out.println("Registros encontrados: "+ buscarRegistros.length);
    	
	    for(FullInfoRegister info: buscarRegistros){
	    	System.out.println(info);
	    }
	    
	    
	    // consultar datos extendidos a tabla XT-FIELD
	    		
	}
	
	public static  void getCompleteRegister(){
		String entidad = Constants.SIGEM_ENTIDAD_MSSSI;
	    User applicationUser = new User();
	    applicationUser.setUserName(Constants.SIGEM_USER_TEST_ECM);		
	    applicationUser.setPassword(Constants.SIGEM_PASSWORD_TEST_ECM);
	    applicationUser.setLocale(new Locale(Constants.SIGEM_LOCALE));
		 
		
		
//		InfoRegister infoRegister = SigmConnector.registrar(applicationUser, entidad, libro, listDataRegister, extendendField);				
//		
//		Integer registerId = Integer.valueOf(infoRegister.getFolderId());

	    //Buscar registros
	    
	    List searchFields = new ArrayList() ;
	    
	    AxSfQueryField queryField = null;
	    
		//OFICINA DE REGISTRO
	    queryField = new AxSfQueryField();
	    queryField.setFldId(String.valueOf(5));
	    queryField.setBookId(6);
	    queryField.setValue("999");
	    queryField.setOperator("=");
	    searchFields.add(queryField);
//	    
////		ORGANO ORIGEN
	    queryField = new AxSfQueryField();
	    queryField.setFldId(String.valueOf(3));
	    queryField.setBookId(6);
	    queryField.setValue("ECM");
//	    queryField.setValue("REGISTRO_ELECTRONICO");
	    	    
	    queryField.setOperator("=");
	    searchFields.add(queryField);
		
	    //NUMERO DE REGISTRO
//	    queryField = new AxSfQueryField();
//	    queryField.setFldId(String.valueOf(1));
//	    queryField.setBookId(6);
//	    queryField.setValue("201599900000462");
////	    queryField.setValue("REGISTRO_ELECTRONICO");
//	    	    
//	    queryField.setOperator("=");
//	    searchFields.add(queryField);
	    
	    
	    //FECHAS
	    
//	    GregorianCalendar fecha = new GregorianCalendar();
//	    fecha.set(
//		Calendar.HOUR_OF_DAY, 0);
//	    fecha.set(
//		Calendar.MINUTE, 0);
//	    fecha.set(
//		Calendar.SECOND, 0);
//	    Date fromDate = fecha.getTime();
//	    fecha.set(
//		Calendar.HOUR_OF_DAY, 23);
//	    fecha.set(
//		Calendar.MINUTE, 0);
//	    fecha.set(
//		Calendar.SECOND, 0);
//	    Date toDate = fecha.getTime();
//	    
//	    System.out.println("From: "+fromDate.toString());
//	    System.out.println("To: "+toDate.toString());
	    queryField = new AxSfQueryField();
	    queryField.setFldId(String.valueOf(2));
	    queryField.setBookId(6);
	    List valor = new ArrayList<Date>();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    try {
//	    ((List<Date>) valor).add((Date)fromDate);
//	    ((List<Date>) valor).add((Date)toDate);
//			valor.add(fromDate);
//			valor.add(toDate);
			valor.add(sdf.parse("26-05-2015"));
			valor.add(sdf.parse("29-06-2015"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    queryField.setValue(valor);
	    	    
	    queryField.setOperator(Keys.QUERY_BETWEEN_TEXT_VALUE);
	    searchFields.add(queryField);
		
	    
	    
//	    QUERY_BETWEEN_TEXT_VALUE = "..";
		
		
	    
	    FullInfoRegister[] buscarRegistros = SigmConnector.buscarRegistros(applicationUser, entidad, "6", searchFields);
	    
	    
		LOG.debug("Procesando DOCUMENTOS...");
		
		
		/// BUSCAR DOCUMENTOS-ALTERNATIVA
		
		UseCaseConf useCaseConf = new UseCaseConf();
		useCaseConf.setEntidadId("000");
		useCaseConf.setLocale(new Locale("es"));
		useCaseConf.setUserName("sigem");
		useCaseConf.setPassword("sigem");		
//		
//		
//		
//	    User applicationUser = new User();
//	    applicationUser.setUserName(Constants.SIGEM_USER);		
//	    applicationUser.setPassword(Constants.SIGEM_PASSWORD);
//	    applicationUser.setLocale(new Locale(Constants.SIGEM_LOCALE));		
		String sessionID = null;
		BooksBo bo = new BooksBo();
//		try {
			try {
				sessionID = Login.login(applicationUser, "000");
				
				useCaseConf.setSessionID(sessionID);
	//
				bo.openBook(useCaseConf, 6);
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ValidationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//					
 catch (RPGenericException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RPBookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		int idBook = 6;

	    SearchInputRegisterBean searchInputRegister = new SearchInputRegisterBean();
	    GregorianCalendar fecha = new GregorianCalendar();
//	    fecha.setTime(searchInputRegister.getFld2ValueDesde());
	    fecha.set(Calendar.HOUR_OF_DAY, 0);
	    fecha.set(Calendar.MINUTE, 0);
	    fecha.set(Calendar.SECOND, 0);
	    fecha.set(Calendar.YEAR, 2015);
	    fecha.set(Calendar.MONTH, 4);
	    fecha.set(Calendar.DAY_OF_MONTH, 10);
	    Date fromDate = fecha.getTime();
	    searchInputRegister.setFld2ValueDesde(fromDate);
	    
	    fecha.set(Calendar.HOUR_OF_DAY, 23);
	    fecha.set(Calendar.MINUTE, 59);
	    fecha.set(Calendar.SECOND, 0);
	    fecha.set(Calendar.YEAR, 2015);
	    fecha.set(Calendar.MONTH, 5);
	    fecha.set(Calendar.DAY_OF_MONTH, 1);
	    
	    
	    
	    
	    Date toDate = fecha.getTime();
	    searchInputRegister.setFld2ValueHasta(toDate);	    
	    searchInputRegister.setFld3Value("ECM");
	    searchInputRegister.setFld9Value("apellliddddooooSSSS");
	    
	    
		AxSfQuery axsfQuery = new AxSfQuery();
		axsfQuery.setBookId(idBook);
		axsfQuery.setPageSize(Integer.parseInt("10"));
		axsfQuery.addField(searchInputRegister.fieldtoQuery("fld1", idBook));
//			     fecha de registro 
//		axsfQuery.addField(searchInputRegister.fieldtoQuery("fld2", idBook));
//			     usuario 
//		axsfQuery.addField(searchInputRegister.fieldtoQuery("fld3", idBook));		
		
		axsfQuery.addField(searchInputRegister.fieldtoQuery("fld9", idBook));	
		
		List<Integer> bookIds = new ArrayList<Integer>();
		bookIds.add(idBook);
		
		int size;
		try {
			size = FolderSession.openRegistersQuery(
					useCaseConf.getSessionID(), axsfQuery, bookIds, 0, useCaseConf.getEntidadId());
		} catch (BookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			    queryResults.setPageSize	

		
	    String orderBy = "";

		orderBy = "FLD1";
		orderBy += " ASC ";
		
		AxSfQueryResults queryResults = null;
		try {
			queryResults = FolderSession.navigateRegistersQuery(
			useCaseConf.getSessionID(), idBook,
			com.ieci.tecdoc.common.isicres.Keys.QUERY_ALL, useCaseConf.getLocale(),
			useCaseConf.getEntidadId(), orderBy);
			
			queryResults.setPageSize(3);
		} catch (BookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<RowSearchInputRegisterBean> registersList =
			    new ArrayList<RowSearchInputRegisterBean>();
		registersList = loadQueryResulttoList(
			    queryResults, useCaseConf.getLocale());		
		
		
		System.out.println("hemos salido de esta funciooooooonnnn");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///
		
		

//		Integer bookId = 6;
//		int folderId = 4305;
//		
//		UseCaseConf useCaseConf = new UseCaseConf();
//		useCaseConf.setEntidadId(Constants.SIGEM_ENTIDAD_MSSSI);
//		useCaseConf.setLocale(new Locale(Constants.SIGEM_LOCALE));
//		useCaseConf.setUserName(Constants.SIGEM_USER_REGISTRO_ELECTRONICO);
//		useCaseConf.setPassword(Constants.SIGEM_PASSWORD);
//		
//	    AxSf axsf = null;
//		try {
//			
//			axsf = FolderSession.getBookFolder(
//			    useCaseConf.getSessionID(), bookId, folderId, useCaseConf.getLocale(),
//			    useCaseConf.getEntidadId());
//		    boolean permShowDocuments = SecuritySession.permisionShowDocuments(
//		    		useCaseConf.getSessionID(), axsf);
//		    int i = 20;
//		    
//		} catch (BookException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SessionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ValidationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}*/

	/**
	 * Crea un adjunto a partir de un documento en byte[]
	 * @param nombre
	 * @param contenidoDocumento
	 * @return
	 * @throws SigmWSException 
	 */
	public static Map<String, Object> createAttachedDocumentMap(String nombre, byte[] contenidoDocumento) throws SigmWSException {
		
		    Map<String, Object> result = new HashMap<String, Object>();
		    List<FlushFdrPage> pages = new ArrayList<FlushFdrPage>();
//		    String fileFullName = FilenameUtils.getFullPath(nombre);
		    String fileExtension = FilenameUtils.getExtension(nombre);;
		    String docName = FilenameUtils.getBaseName(nombre);
		    
		    FlushFdrFile documentDataToUpload = new FlushFdrFile();
		    documentDataToUpload.setBuffer(contenidoDocumento);
		    documentDataToUpload.setExtension(fileExtension); 
		    documentDataToUpload.setFileNameFis(nombre);
		    documentDataToUpload.setFileNameLog(nombre);

		    FlushFdrPage documentPageToUpload = new FlushFdrPage();
		    documentPageToUpload.setPageName(docName);
		    documentPageToUpload.setFile(documentDataToUpload);
		    pages.add(documentPageToUpload);

		    FlushFdrDocument documentToUpload = new FlushFdrDocument();
		    documentToUpload.setDocumentName(nombre);
		    documentToUpload.setPages(pages);
		    result.put(nombre, documentToUpload);
		    
			return result;
		}


	public static Map<?, ?> createAttachedDocumentMap(Map<String, Fichero> adjuntos) {
		
		List<FlushFdrPage> pages = null;	
		Map<String, Object> result = new HashMap<String, Object>();		
		
		for (Map.Entry<String, Fichero> entry : adjuntos.entrySet()) {
			
			String nombre = entry.getKey();
			Fichero fichero = entry.getValue();
		    byte[] contenidoDocumento = fichero.getContenido();
			
			pages = new ArrayList<FlushFdrPage>();
			
		    String fileExtension = FilenameUtils.getExtension(nombre);
		    String docName = FilenameUtils.getBaseName(nombre);
		    
		    FlushFdrFile documentDataToUpload = new FlushFdrFile();
			documentDataToUpload.setBuffer(contenidoDocumento);
		    documentDataToUpload.setExtension(fileExtension); 
		    documentDataToUpload.setFileNameFis(nombre);
		    documentDataToUpload.setFileNameLog(nombre);

		    FlushFdrPage documentPageToUpload = new FlushFdrPage();
		    documentPageToUpload.setPageName(docName);
		    documentPageToUpload.setFile(documentDataToUpload);
		    pages.add(documentPageToUpload);

		    FlushFdrDocument documentToUpload = new FlushFdrDocument();
		    documentToUpload.setDocumentName(nombre);
		    documentToUpload.setPages(pages);
		    result.put(nombre, documentToUpload);		    
		}

		return result;
	}
	
	public static void createAttachedDocumentMap(Map<String, Object> mapDocuments, String nombre) throws SigmWSException {
		
	    List<FlushFdrPage> pages = new ArrayList<FlushFdrPage>();

	    byte[] contenidoDocumento = null;

	    //TODO: Leer ruta de temporales


	    nombre = "d:/test2.spdf";
	    
	    File fileDoc = new File(nombre);
	    
	    try {
	    	contenidoDocumento = FileUtils.readFileToByteArray(fileDoc);
		} catch (IOException e) {
			throw new SigmWSException("err.fichero",e);
		}
	    
//	    String fileFullName = FilenameUtils.getFullPath(nombre);
	    String fileExtension = FilenameUtils.getExtension(nombre);;
	    String docName = FilenameUtils.getBaseName(nombre);
	    
	    FlushFdrFile documentDataToUpload = new FlushFdrFile();
	    documentDataToUpload.setBuffer(contenidoDocumento);
	    documentDataToUpload.setExtension(fileExtension); 
	    documentDataToUpload.setFileNameFis(nombre);
	    documentDataToUpload.setFileNameLog(nombre);

	    FlushFdrPage documentPageToUpload = new FlushFdrPage();
	    documentPageToUpload.setPageName(docName);
	    documentPageToUpload.setFile(documentDataToUpload);
	    pages.add(documentPageToUpload);

	    FlushFdrDocument documentToUpload = new FlushFdrDocument();
	    documentToUpload.setDocumentName(nombre);
	    documentToUpload.setPages(pages);
	    mapDocuments.put(nombre, documentToUpload);
	
	}
	
	

	   @SuppressWarnings("unchecked")
	    public static List<RowSearchInputRegisterBean> loadQueryResulttoList(
		AxSfQueryResults queryResults, Locale locale) {
		List<RowSearchInputRegisterBean> data = new ArrayList<RowSearchInputRegisterBean>();
		AxSfIn axSfIn;
		RowSearchInputRegisterBean bean;
		for (Iterator<AxSfIn> it = queryResults.getResults().iterator(); it.hasNext();) {
		    axSfIn = (AxSfIn) it.next();
		    bean = new RowSearchInputRegisterBean();
		    bean.setFdrid(new Integer(
			axSfIn.getAttributeValueAsString("fdrid")));
		    bean.setFld1(axSfIn.getAttributeValueAsString("fld1"));
		    bean.setFld2((axSfIn.getAttributeValue("fld2") != null)
			? (Date) axSfIn.getAttributeValue("fld2") : null);
		    bean.setFld3(axSfIn.getAttributeValueAsString("fld3"));
		    bean.setFld5(axSfIn.getFld5());
		    bean.setFld5Name(axSfIn.getFld5Name());
		    bean.setFld6(axSfIn.getAttributeValueAsString("fld6"));
		    bean.setFld6Name(RBUtil.getInstance(
			locale).getProperty(
			"book.fld6." +
			    axSfIn.getAttributeValueAsString("fld6")));
		    bean.setFld7(axSfIn.getFld7());
		    bean.setFld7Name(axSfIn.getFld7Name());
		    bean.setFld8(axSfIn.getFld8());
		    bean.setFld8Name(axSfIn.getFld8Name());
		    bean.setFld9(axSfIn.getAttributeValueAsString("fld9"));
		    bean.setFld13(axSfIn.getFld13());
		    bean.setFld13Name((axSfIn.getFld13() != null)
			? axSfIn.getFld13().getName() : null);
		    bean.setFld16(axSfIn.getFld16());
		    bean.setFld16Name((axSfIn.getFld16() != null)
			? axSfIn.getFld16().getCode() : null);
		    bean.setFld17(axSfIn.getAttributeValueAsString("fld17"));
		    bean.setFld19(axSfIn.getAttributeValueAsString("fld19"));
		    data.add(bean);
		}
		return data;
	    }


	public static User getUser(String userName, String userPassword, String userLocale) {
		User applicationUser = new User();
	    applicationUser.setUserName(userName);		
	    applicationUser.setPassword(userPassword);
	    applicationUser.setLocale(new Locale(userLocale));		
		return applicationUser;
	}


	public static UseCaseConf getUseCaseConf(String user, String password, String entidad,
			String local) {
		UseCaseConf useCaseConf = new UseCaseConf();
		useCaseConf.setEntidadId(entidad);
		useCaseConf.setLocale(new Locale(local));
		useCaseConf.setUserName(user);
		useCaseConf.setPassword(password);
		return useCaseConf;
				
	}


	public static AxSfQuery getFormattedQuery(PeticionBusqueda request, int idBook) throws SigmWSException {

		String numeroRegistro = "";//numero de registro
		String interesados = "";//apellliddddooooSSSS";
		String fromString = "";//2015-05-30 10:00:00";//yyyy-MM-dd HH:mm:ss
		String toString = "";//2015-06-02 23:00:00";//yyyy-MM-dd HH:mm:ss
		String usuario = "";//ECM";
	    String estado = ""; //"COMPLETO"
	    String asunto = ""; //"TBEC BECA"
	    String origen = ""; // EA0008480
	    String destino = ""; // MS62602062
		
		// VALLORES DE LA PETICIÓN
	    numeroRegistro = request.getNumeroRegistro();
		usuario = request.getUsuario();
		fromString = request.getFechaDesde();
		toString = request.getFechaHasta();
		interesados = request.getInteresados();
		estado = request.getEstadoRegistro();
		asunto = request.getAsunto();
		origen = request.getOrigen();
		destino = request.getDestino();
		
		AxSfQuery axsfQuery = new AxSfQuery();
    	try {
		
			// REGISTRO ENTRADA
	    	if(idBook == Constants.REGISTRO_ELECTRONICO_ENTRADA)
	    	{
			    SearchInputRegisterBean searchInputRegister = new SearchInputRegisterBean();
			    
			    if(numeroRegistro !=null && !numeroRegistro.isEmpty()){
			    	searchInputRegister.setFld1Value(numeroRegistro);
			    }
	
			    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_LONG);
			    
			    if(fromString !=null && !fromString.isEmpty()){
			    	searchInputRegister.setFld2ValueDesde(sdf.parse(fromString));
			    }
	//	    	ScrOfic scrOficByCode = getScrOficByCode(origen);		    
			    if(toString !=null && !toString.isEmpty()){
			    	searchInputRegister.setFld2ValueHasta(sdf.parse(toString));
			    }
		
			    if(usuario !=null && !usuario.isEmpty()){
			    	searchInputRegister.setFld3Value(usuario);
			    }
			    
			    if(estado !=null && !estado.isEmpty()){
			    	searchInputRegister.setFld6Value(estado);
			    }
			    if(origen !=null && !origen.isEmpty()){
			    	ScrOrg scrOrg = getScrOrgByCode(origen);
			    	searchInputRegister.setFld7Value(scrOrg);
			    }
			    if(destino !=null && !destino.isEmpty()){
			    	ScrOrg scrOrg = getScrOrgByCode(destino);
			    	searchInputRegister.setFld8Value(scrOrg);
			    }
			    if(interesados !=null && !interesados.isEmpty()){
			    	searchInputRegister.setFld9Value(interesados);
			    }
			    	
			    if(asunto !=null && !asunto.isEmpty()){
	//						"TBEC");		    	
			    	 
			    	ScrCa scrCa = getScrCaByCode(asunto);
			    	searchInputRegister.setFld16Value(scrCa);
			    }
			    
			    
				axsfQuery.setBookId(idBook);
	//			axsfQuery.setPageSize(Integer.parseInt("10"));
				
				if(numeroRegistro !=null && !numeroRegistro.isEmpty())
				    axsfQuery.addField(searchInputRegister.fieldtoQuery("fld1", idBook)); // numero registro
		    	
			    if((fromString !=null && !fromString.isEmpty()) || (toString !=null && !toString.isEmpty()))
			    	axsfQuery.addField(searchInputRegister.fieldtoQuery("fld2", idBook)); // fecha de registro
			    
			    if(usuario !=null && !usuario.isEmpty())
			    	axsfQuery.addField(searchInputRegister.fieldtoQuery("fld3", idBook)); // usuario
				
				if(estado !=null && !estado.isEmpty())
			    	axsfQuery.addField(searchInputRegister.fieldtoQuery("fld6", idBook)); // estado
				
				if(origen !=null && !origen.isEmpty())
			    	axsfQuery.addField(searchInputRegister.fieldtoQuery("fld7", idBook)); // origen
				
				if(destino !=null && !destino.isEmpty())
			    	axsfQuery.addField(searchInputRegister.fieldtoQuery("fld8", idBook)); // destino
				 
			    if(interesados !=null && !interesados.isEmpty())
			    	axsfQuery.addField(searchInputRegister.fieldtoQuery("fld9", idBook)); // remitentes
			    
				if(asunto !=null && !asunto.isEmpty())
			    	axsfQuery.addField(searchInputRegister.fieldtoQuery("fld16", idBook)); // asunto
				
		    
	    	} // REGISTRO SALIDA
	    	else if(idBook == Constants.REGISTRO_ELECTRONICO_SALIDA){
			    SearchOutputRegisterBean searchOutputRegister = new SearchOutputRegisterBean();
			    
			    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_LONG);
		
			    if(numeroRegistro !=null && !numeroRegistro.isEmpty()){
			    	searchOutputRegister.setFld1Value(numeroRegistro);
			    }
			    
			    
			    if(fromString !=null && !fromString.isEmpty()){
						searchOutputRegister.setFld2ValueDesde(sdf.parse(fromString));
			    }
	//	    	ScrOfic scrOficByCode = getScrOficByCode(origen);		    
			    if(toString !=null && !toString.isEmpty()){
			    	searchOutputRegister.setFld2ValueHasta(sdf.parse(toString));
			    }
		
			    if(usuario !=null && !usuario.isEmpty()){
			    	searchOutputRegister.setFld3Value(usuario);
			    }
			    
			    if(estado !=null && !estado.isEmpty()){
			    	searchOutputRegister.setFld6Value(estado);
			    }
			    if(origen !=null && !origen.isEmpty()){
			    	ScrOrg scrOrg = getScrOrgByCode(origen);
			    	searchOutputRegister.setFld7Value(scrOrg);
			    }
			    if(destino !=null && !destino.isEmpty()){
			    	ScrOrg scrOrg = getScrOrgByCode(destino);
			    	searchOutputRegister.setFld8Value(scrOrg);
			    }
			    if(interesados !=null && !interesados.isEmpty()){
			    	searchOutputRegister.setFld9Value(interesados);
			    }		    	
			    if(asunto !=null && !asunto.isEmpty()){
			    	ScrCa scrCa = getScrCaByCode(asunto);
			    	searchOutputRegister.setFld12Value(scrCa);
			    }
			    
				axsfQuery.setBookId(idBook);
				
				if(numeroRegistro !=null && !numeroRegistro.isEmpty())
				    axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld1", idBook)); // numero registro
		    	
			    if((fromString !=null && !fromString.isEmpty()) || (toString !=null && !toString.isEmpty()))
			    	axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld2", idBook)); // fecha de registro
			    
			    if(usuario !=null && !usuario.isEmpty())
			    	axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld3", idBook)); // usuario
				
				if(estado !=null && !estado.isEmpty())
			    	axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld6", idBook)); // estado
				
				if(origen !=null && !origen.isEmpty())
			    	axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld7", idBook)); // origen
				
				if(destino !=null && !destino.isEmpty())
			    	axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld8", idBook)); // destino
				 
			    if(interesados !=null && !interesados.isEmpty())
			    	axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld9", idBook)); // remitentes
			    
				if(asunto !=null && !asunto.isEmpty())
			    	axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld12", idBook)); // asunto
			    
	    	}
			
    	} catch (ParseException e) {
    		throw new SigmWSException("err.parser.time", e);
    	}
    	
    	return axsfQuery;
	}


	/**
	 * Obtiene el objeto ScrCA a partir del codigo del tipo de asunto
	 * @param asunto
	 * @return ScrCA
	 * @throws SigmWSException 
	 */
	public static ScrCa getScrCaByCode(String asunto) throws SigmWSException {
		
		ScrCa result = null;
    	
    	HibernateUtil hibernateUtil = null;
    	Transaction tran = null;
    	try {
    	    hibernateUtil = new HibernateUtil();
    	    Session session = HibernateUtil.currentSession(Constants.SIGEM_ENTIDAD_MSSSI);
    	    tran = session.beginTransaction();
    	    
    		StringBuffer query = new StringBuffer();
    		query.append(" FROM ");    		
    		query.append(" com.ieci.tecdoc.common.invesicres.ScrCa ");
    		query.append(" scr WHERE scr.code=?");
    		List list = session.find(query.toString(), new Object[] { asunto },
    				new Type[] { Hibernate.STRING });


    		if (list != null && !list.isEmpty()) {
    			result = (ScrCa) list.get(0);
    		}
 
    	    HibernateUtil.commitTransaction(tran);
    	}
    	catch (HibernateException sqlException) {
			throw new SigmWSException("err.sigm.hibernate", sqlException);
    	}

    	finally {
    	    HibernateUtil.closeSession(Constants.SIGEM_ENTIDAD_MSSSI);
    	}
		
    	return result;
	
	
	}

	/**
	 * Obtiene el objecto oficina a partir del codigo de oficina
	 * @param code codigo de oficina
	 * @return ScrOfic
	 * @throws SigmWSException
	 */
	public static ScrOfic getScrOficByCode(String code) throws SigmWSException {
		ScrOfic scrOfic = null;

		HibernateUtil hibernateUtil = null;
		Transaction tran = null;
		try {
			hibernateUtil = new HibernateUtil();
			Session session = HibernateUtil.currentSession(Constants.SIGEM_ENTIDAD_MSSSI);
			tran = session.beginTransaction();

			StringBuffer query = new StringBuffer();
			query.append("FROM ");
			query.append(" com.ieci.tecdoc.common.invesicres.ScrOfic ");
			query.append(" scr WHERE scr.code=?");
			List list = session.find(query.toString(), new Object[] { code },
					new Type[] { Hibernate.STRING });

			if (list != null && !list.isEmpty()) {
				scrOfic = (ScrOfic) list.get(0);
			}

			HibernateUtil.commitTransaction(tran);
		} catch (HibernateException sqlException) {
			throw new SigmWSException("err.sigm.hibernate", sqlException);
		}

		finally {
			HibernateUtil.closeSession(Constants.SIGEM_ENTIDAD_MSSSI);
		}
		
		return scrOfic;
	}

	/**
	 * Obtiene el objeto organismo a partir del codigo de organismo
	 * @param code codigo de organismo
	 * @return ScrOrg
	 * @throws SigmWSException
	 */
	public static ScrOrg getScrOrgByCode(String code) throws SigmWSException{
		ScrOrg result = null;
		
    	HibernateUtil hibernateUtil = null;
    	Transaction tran = null;
    	try {
    	    hibernateUtil = new HibernateUtil();
    	    Session session = HibernateUtil.currentSession(Constants.SIGEM_ENTIDAD_MSSSI);
    	    tran = session.beginTransaction();
    	    
			StringBuffer query = new StringBuffer();
			query.append("FROM ");
			query.append(" com.ieci.tecdoc.common.invesicres.ScrOrg ");
			query.append(" scr WHERE scr.code=?");
			List list = session.find(query.toString(), new Object[] { code },
					new Type[] { Hibernate.STRING });
	
	
			if (list != null && !list.isEmpty()) {
				result = (ScrOrg) list.get(0);
			}
	
		    HibernateUtil.commitTransaction(tran);
		}
		catch (HibernateException sqlException) {
			throw new SigmWSException("err.sigm.hibernate", sqlException);
		}
	
		finally {
		    HibernateUtil.closeSession(Constants.SIGEM_ENTIDAD_MSSSI);
		}
    	
		return result;
	}


	/**
	 * Rellena la información de respuesta a la busqueda de registros
	 * @param locale
	 * @param sessionID
	 * @param idBook
	 * @param queryResults
	 * @param listRegistro
	 * @throws BookException
	 * @throws SessionException
	 * @throws ValidationException
	 */
	public static void fillQueryResult(Locale locale, String sessionID, int idBook, AxSfQueryResults queryResults,
			List<Registro> listRegistro) throws BookException, SessionException, ValidationException {
 		
		Registro registro = null;
		
		AxSf axSf = null;
		for (Iterator<AxSf> it = queryResults.getResults().iterator(); it.hasNext();) {
			
			if(idBook == Constants.REGISTRO_ELECTRONICO_ENTRADA){
				axSf = (AxSfIn) it.next();
			} else if(idBook == Constants.REGISTRO_ELECTRONICO_SALIDA) {
				axSf = (AxSfOut) it.next();
			}
			
		    registro = new Registro();
		    Integer fdrid = new Integer(axSf.getAttributeValueAsString("fdrid"));
		 
			registro.setNumeroRegistro(axSf.getAttributeValueAsString("fld1"));
			registro.setFechaRegistro((axSf.getAttributeValue("fld2") != null) ? SigmUtil.formatTimeStampInString((Date) axSf.getAttributeValue("fld2")) : null);
			registro.setEstadoRegistro(RBUtil.getInstance(locale).getProperty("book.fld6." +axSf.getAttributeValueAsString("fld6")));			
			registro.setOrigen(axSf.getFld7Name());
			registro.setDestino(axSf.getFld8Name());
			
			List<AxDoch> docs = null;	
			docs = FolderFileSession.getBookFolderDocsWithPages(sessionID, idBook, fdrid, Constants.SIGEM_ENTIDAD_MSSSI);
			
			List<Documento> documentos = null;
			if(docs!=null && docs.size()!=0){
				log.debug("Documentos: "+docs.size());
				documentos = new ArrayList<Documento>();
				Documento docXml = null;
				for(AxDoch doc: docs){
					List pages = doc.getPages();
					if(pages.size() == 0)
						continue;
					
					AxPageh page = (AxPageh) pages.get(0);
					log.debug("Nombre completo: "+doc.getName());
					docXml = new Documento();
					docXml.setNombre(doc.getName());
					documentos.add(docXml);
				}
				
				Documentos docsXml = new Documentos();
				docsXml.setDocumento(documentos);
				registro.setDocumentos(docsXml);
				
			}					
    		listRegistro.add(registro);				
		}
	}


//	public FullInfoRegister procesarPeticionRegistro(PeticionRegistro request) throws SigmWSException {
//		
//		
//		return consultRegisterInfo;
//		
//		// Llamar a SIGEM para registrar
//
//////		SigmConnector.registrarAhora(listDataRegister, interesados, adjuntos);
////		InfoRegister infoRegister = SigmConnector.registrarAhora(listDataRegister);
////				
//		
//		
//		
//	}




	public static OutputRegisterBean crearOutputDataRegister(ElementoDatos datos) throws SigmWSException {
		
		OutputRegisterBean bean = new OutputRegisterBean();

		//+MAPEAR DATOS DE ENTRADA
		if(datos.getOficina() != null) {
			ScrOfic scrOficByCode = getScrOficByCode(datos.getOficina()); //"999"
			if(scrOficByCode == null)
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Oficina de registro inválida"});
			bean.setFld5(scrOficByCode);
		}
		
		if(datos.getOrigen() != null) {
			ScrOrg scrOrgByCodeOrigen = getScrOrgByCode(datos.getOrigen()); //
			if(scrOrgByCodeOrigen == null)
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Origen inválido"});
			bean.setFld7(scrOrgByCodeOrigen);
		}
		
		if(datos.getDestino() != null) {
			ScrOrg scrOrgByCode = getScrOrgByCode(datos.getDestino()); //"MS62602062"
			if(scrOrgByCode == null)
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Destino inválido"});
			
			bean.setFld8(scrOrgByCode);
		}
		
		if(datos.getTipoTransporte() != null) bean.setFld10(datos.getTipoTransporte());
		if(datos.getNumeroTransporte() != null) bean.setFld11(datos.getNumeroTransporte());
		if(datos.getTipoAsunto() != null) {
			ScrCa scrCaByCode = getScrCaByCode(datos.getTipoAsunto()); //"TBEC"
			if(scrCaByCode == null)
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Asunto inválido"});
			bean.setFld12(scrCaByCode);			
		}
		
		if(datos.getResumen() != null) bean.setFld13(datos.getResumen());
		

		//+MAPEAR DATOS DE INTERESADOS				
		if(datos.getInteresados() !=null ){
			ElementoDatosInteresados elementoDatosInteresados = datos.getInteresados();
			bean.setInteresados(getInteresadosFromRequest(elementoDatosInteresados));
		}
		
		return bean;

	}


	public static InputRegisterBean crearInputDataRegister(ElementoDatos datos) throws SigmWSException {
		
		
		InputRegisterBean bean = new InputRegisterBean();
		try {
			if(datos.getOficina() != null) {
				ScrOfic scrOficByCode = getScrOficByCode(datos.getOficina()); //"999"
				if(scrOficByCode == null)
					throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Oficina de registro inválida"});
				bean.setFld5(scrOficByCode);			
			}
			
			if(datos.getOrigen() != null) {
				ScrOrg scrOrgByCodeOrigen = getScrOrgByCode(datos.getOrigen()); //
				if(scrOrgByCodeOrigen == null)
					throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Origen inválido"});
				
				bean.setFld7(scrOrgByCodeOrigen);
			}
			
			if(datos.getDestino() != null) {
				ScrOrg scrOrgByCode = getScrOrgByCode(datos.getDestino()); //"MS62602062"
				if(scrOrgByCode == null)
					throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Destino inválido"});
				bean.setFld8(scrOrgByCode);
			}
			
			
			if(datos.getTipoRegistroOriginal() != null){
				int tipoRO = datos.getTipoRegistroOriginal().equals(Constants.REGISTRO_ENTRADA) ? Constants.REGISTRO_ENTRADA_BBDD : Constants.REGISTRO_SALIDA_BBDD;
				bean.setFld11(tipoRO);			
			}
	
	
			if(datos.getFechaRegistroOriginal()!=null){
				
			    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT);
				bean.setFld12(sdf.parse(datos.getFechaRegistroOriginal()));
					
			}
			
			//TODO: Pendiente
	//		if(datos.getRegistroOriginal)
	//		bean.setFld13(tipoRO);			
			
			
			if(datos.getTipoTransporte() != null) bean.setFld14(datos.getTipoTransporte());
			if(datos.getNumeroTransporte() != null) bean.setFld15(datos.getNumeroTransporte());
			if(datos.getTipoAsunto() != null) {
				ScrCa scrCaByCode = getScrCaByCode(datos.getTipoAsunto()); //"TBEC"
				if(scrCaByCode == null)
					throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Asunto inválido"});
				bean.setFld16(scrCaByCode);			
			}
			
			if(datos.getResumen() != null) bean.setFld17(datos.getResumen());
			
			if(datos.getRefExpediente() != null) bean.setFld19(datos.getRefExpediente());
			
	
			//+MAPEAR DATOS DE INTERESADOS		
			if(datos.getInteresados() !=null ){
				ElementoDatosInteresados elementoDatosInteresados = datos.getInteresados();
				bean.setInteresados(getInteresadosFromRequest(elementoDatosInteresados));
			}
			
		} catch (ParseException e) {
			throw new SigmWSException("err.parser.time", e);
		}


		return bean;
	}

	private static List<Interesado> getInteresadosFromRequest(
			ElementoDatosInteresados elementoDatosInteresados) {

		Interesado interesado = null;
		List<Interesado> interesados = null;		
		interesados = new ArrayList<Interesado>();
		
		//INTERESADOS FISICOS
		List<InteresadoFisico> listInteresadoFisico = elementoDatosInteresados.getInteresadoFisico();
		for(InteresadoFisico elemento: listInteresadoFisico){
			log.debug("Interesado-Tipo: Fisico");								
			interesado = new Interesado();
			interesado.setTipo(Constants.TIPO_PERSONA_FISICA);
			interesado.setTipodoc(elemento.getTipoDocumento());
			interesado.setDocIdentidad(elemento.getNumeroDocumento());
			interesado.setNombre(elemento.getNombre());
			interesado.setPapellido(elemento.getPrimerApellido());
			interesado.setSapellido(elemento.getSegundoApellido());
			
			if(elemento.getRepresentante() != null){
				ElementoRepresentante elementoRepresentante = elemento.getRepresentante();
				
				Representante representanteInteresado = new Representante();
				
				if(elementoRepresentante.getRepresentanteFisico() != null){					
					log.debug("Representante-Tipo: Fisico");					
					RepresentanteFisico representante = elementoRepresentante.getRepresentanteFisico();
					representanteInteresado.setTipo(Constants.TIPO_PERSONA_FISICA);
					representanteInteresado.setTipodoc(representante.getTipoDocumento());
					representanteInteresado.setDocIdentidad(representante.getNumeroDocumento());
					representanteInteresado.setNombre(representante.getNombre());
					representanteInteresado.setPapellido(representante.getPrimerApellido());
					representanteInteresado.setSapellido(representante.getSegundoApellido());
				} else if(elementoRepresentante.getRepresentanteJuridico() != null){
					log.debug("Representante-Tipo: Juridico");
					RepresentanteJuridico representante = elementoRepresentante.getRepresentanteJuridico();
					representanteInteresado.setTipo(Constants.TIPO_PERSONA_JURIDICA);
					representanteInteresado.setTipodoc(representante.getTipoDocumento());
					representanteInteresado.setDocIdentidad(representante.getNumeroDocumento());
					representanteInteresado.setRazonSocial(representante.getRazonSocial());
				}
				interesado.setRepresentante(representanteInteresado);
			}

			interesados.add(interesado);
		}
		
		//INTERESADOS JURIDICOS
		List<InteresadoJuridico> listInteresadoJuridico = elementoDatosInteresados.getInteresadoJuridico();
		for(InteresadoJuridico elemento: listInteresadoJuridico){
			log.debug("Interesado-Tipo: Juridico");								
			interesado = new Interesado();
			interesado.setTipo(Constants.TIPO_PERSONA_JURIDICA);
			interesado.setTipodoc(elemento.getTipoDocumento());
			interesado.setDocIdentidad(elemento.getNumeroDocumento());
			interesado.setRazonSocial(elemento.getRazonSocial());
			
			if(elemento.getRepresentante() != null){
				ElementoRepresentante elementoRepresentante = elemento.getRepresentante();
				
				Representante representanteInteresado = new Representante();
				
				if(elementoRepresentante.getRepresentanteFisico() != null){					
					log.debug("Representante-Tipo: Fisico");					
					RepresentanteFisico representante = elementoRepresentante.getRepresentanteFisico();
					representanteInteresado.setTipo(Constants.TIPO_PERSONA_FISICA);
					representanteInteresado.setTipodoc(representante.getTipoDocumento());
					representanteInteresado.setDocIdentidad(representante.getNumeroDocumento());
					representanteInteresado.setNombre(representante.getNombre());
					representanteInteresado.setPapellido(representante.getPrimerApellido());
					representanteInteresado.setSapellido(representante.getSegundoApellido());
				} else if(elementoRepresentante.getRepresentanteJuridico() != null){
					log.debug("Representante-Tipo: Juridico");
					RepresentanteJuridico representante = elementoRepresentante.getRepresentanteJuridico();
					representanteInteresado.setTipo(Constants.TIPO_PERSONA_JURIDICA);
					representanteInteresado.setTipodoc(representante.getTipoDocumento());
					representanteInteresado.setDocIdentidad(representante.getNumeroDocumento());
					representanteInteresado.setRazonSocial(representante.getRazonSocial());
				}
				interesado.setRepresentante(representanteInteresado);
			}

			interesados.add(interesado);
		}

		return interesados;
		
	}	

	/*
	private static List<Interesado> getInteresadosFromRequest(
			List<ElementoDatoInteresado> listInteresado) {

		Interesado interesado = null;
		List<Interesado> interesados = null;		
		interesados = new ArrayList<Interesado>();
		
		for(ElementoDatoInteresado elemento: listInteresado ){
			interesado = new Interesado();
			interesado.setTipodoc(elemento.getTipoDocumento());
			interesado.setDocIdentidad(elemento.getNumeroDocumento());
			
			LOG.debug("Interesado-Tipo: "+elemento.getTipo());
			
			if(elemento.getTipo().equals(TipoPersona.P)){
				interesado.setTipo(TipoPersona.P.value());
				interesado.setNombre(elemento.getNombre());
				interesado.setPapellido(elemento.getPrimerApellido());
				interesado.setSapellido(elemento.getSegundoApellido());
			} else if(elemento.getTipo().equals(TipoPersona.J)){
				interesado.setTipo(TipoPersona.J.value());
				interesado.setDocIdentidad(elemento.getNumeroDocumento());
				interesado.setRazonSocial(elemento.getRazonSocial());
			}
			
			if(elemento.getRepresentante() != null){
				ElementoRepresentante representante = elemento.getRepresentante();
				LOG.debug("Representante-Tipo: "+representante.getTipo());
				
				Representante representanteInteresado = new Representante();
				if(representante.getTipo().equals(TipoPersona.P)){
					representanteInteresado.setTipo(TipoPersona.P.value());
					representanteInteresado.setNombre(representante.getNombre());
					representanteInteresado.setPapellido(representante.getPrimerApellido());
					representanteInteresado.setSapellido(representante.getSegundoApellido());
				} else if(representante.getTipo().equals(TipoPersona.J)){
					representanteInteresado.setTipo(TipoPersona.J.value());
					representanteInteresado.setDocIdentidad(representante.getNumeroDocumento());
					representanteInteresado.setRazonSocial(representante.getRazonSocial());
				}
				
				interesado.setRepresentante(representanteInteresado);
			}
			
			interesados.add(interesado);		
		}
		return interesados;
		
	}	
	*/
	public static FullInfoRegister consultRegisterInfo(Integer bookID,
			AuthenticationUser user, AxSf axsf, Locale locale)
			throws BookException, SessionException, TecDocException {
		 
		
		FullInfoRegister fullInfoRegister = new FullInfoRegister();
		
		fullInfoRegister.setFdrid(axsf.getAttributeValueAsString(FullInfoRegister.FDRID));
		fullInfoRegister.setNumRegistro(axsf.getAttributeValueAsString(FullInfoRegister.NUM_REGISTRO));
		fullInfoRegister.setFechaRegistro(axsf.getAttributeValue(FullInfoRegister.FECHA_REGISTRO) != null ? (Date) axsf.getAttributeValue(FullInfoRegister.FECHA_REGISTRO) : null);		
		fullInfoRegister.setOficinaRegistro(axsf.getAttributeValueAsString(FullInfoRegister.OFICINA_REGISTRO_IN_OUT));
		fullInfoRegister.setEstado(axsf.getAttributeValueAsString(FullInfoRegister.ESTADO_REGISTRO_IN_OUT));
		fullInfoRegister.setOrganoOrigen(axsf.getAttributeValueAsString(FullInfoRegister.ORG_ORIGEN_IN_OUT));
		fullInfoRegister.setOrganoDestino(axsf.getAttributeValueAsString(FullInfoRegister.ORG_DESTINO_IN_OUT));
		
		if(bookID.toString().equals(FullInfoRegister.BOOK_REG_ELETRONICO_IN)){
			fullInfoRegister.setTrasporte(axsf.getAttributeValueAsString(FullInfoRegister.TRANSPORTE_IN));
			fullInfoRegister.setNumTransporte(axsf.getAttributeValueAsString(FullInfoRegister.NUM_TRANSPORTE_IN));
			fullInfoRegister.setAsunto(axsf.getAttributeValueAsString(FullInfoRegister.ASUNTO_IN));
			fullInfoRegister.setResumen(axsf.getAttributeValueAsString(FullInfoRegister.RESUMEN_IN));

			fullInfoRegister.setFechaOriginal(axsf.getAttributeValue(FullInfoRegister.FECHA_ORIGINAL_IN_OUT) != null ?  (Date) axsf.getAttributeValue(FullInfoRegister.FECHA_ORIGINAL_IN_OUT) : null);
			//SON EXTENDIDOS
//			fullInfoRegister.setComentario(axsf.getAttributeValueAsString(FullInfoRegister.COMENTARIO_IN));
//			fullInfoRegister.setExpediente(axsf.getAttributeValueAsString(FullInfoRegister.REF_EXPEDIENTE_IN));
//			fullInfoRegister.setFechaDocumentacion((Date) axsf.getAttributeValue(FullInfoRegister.FECHA_DOCUMENTACION_IN));			
			
		}else if(bookID.toString().equals(FullInfoRegister.BOOK_REG_ELETRONICO_OUT)){
			fullInfoRegister.setTrasporte(axsf.getAttributeValueAsString(FullInfoRegister.TRANSPORTE_OUT));
			fullInfoRegister.setNumTransporte(axsf.getAttributeValueAsString(FullInfoRegister.NUM_TRANSPORTE_OUT));
			fullInfoRegister.setAsunto(axsf.getAttributeValueAsString(FullInfoRegister.ASUNTO_OUT));
			fullInfoRegister.setResumen(axsf.getAttributeValueAsString(FullInfoRegister.RESUMEN_OUT));
			//SON EXTENDIDOS
			fullInfoRegister.setComentario(axsf.getAttributeValueAsString(FullInfoRegister.COMENTARIO_OUT));
			fullInfoRegister.setFechaDocumentacion((Date) axsf.getAttributeValue(FullInfoRegister.FECHA_DOCUMENTACION_OUT));			
			fullInfoRegister.setFechaDocumentacion((axsf.getAttributeValue(FullInfoRegister.FECHA_DOCUMENTACION_OUT) != null) ?  (Date) axsf.getAttributeValue(FullInfoRegister.FECHA_DOCUMENTACION_OUT) : null);			
		}
		
		fullInfoRegister.setBookTipo(bookID.toString());
 
		
//		registerInfo.setBookId(bookID.toString());
//		registerInfo.setDate(getRegisterDate(axsf, "fld2", longFormatter));
//		registerInfo.setfFolderId(axsf.getAttributeValueAsString("fdrid"));
//		registerInfo.setNumber(axsf.getAttributeValueAsString("fld1"));
//		registerInfo.setOffice(scrofic.getCode());
//		registerInfo.setOfficeName(scrofic.getName());
//		registerInfo.setState(axsf.getAttributeValueAsString("fld6"));
//		registerInfo.setUserName(user.getName());
//		registerInfo.setWorkDate(getRegisterDate(axsf, "fld4", shortFormatter));
		
		
		// Leer campos extendidos BASE
		
//		if(!axsf.getExtendedFields().isEmpty()){
//			fullInfoRegister.addExtendido(value, key);
//			fullInfoRegister.setComentario(axsf.getExtendedFields().get(18)getExtendedFields().get(FullInfoRegister.COMENTARIO_IN));
//			fullInfoRegister.setExpediente(axsf.getAttributeValueAsString(FullInfoRegister.REF_EXPEDIENTE_IN));
//			fullInfoRegister.setFechaDocumentacion((Date) axsf.getAttributeValue(FullInfoRegister.FECHA_DOCUMENTACION_IN));			
//			
//		}
//			fullInfoRegister.setComentario(axsf.getAttributeValueAsString(FullInfoRegister.COMENTARIO_IN));
//			fullInfoRegister.setExpediente(axsf.getAttributeValueAsString(FullInfoRegister.REF_EXPEDIENTE_IN));
//			fullInfoRegister.setFechaDocumentacion((Date) axsf.getAttributeValue(FullInfoRegister.FECHA_DOCUMENTACION_IN));			
//		Object object = axsf.getExtendedFields().get(18);
		
		Iterator entries = axsf.getExtendedFields().keySet().iterator();;
		while(entries.hasNext()) {
			Integer extField = (Integer) entries.next(); 
			AxXf axXf = (AxXf)axsf.getExtendedFields().get(extField);
			fullInfoRegister.addExtendido(String.valueOf(axXf.getFldId()), axXf.getText());
//		    System.out.println("Campo extendido: " + extField);
//		    System.out.println("Valor: " + axXf.getText());
//		    System.out.println("Fecha: " + axXf.getTimeStamp());
		}
		

		return fullInfoRegister;

	}


	/**
	 * Rellena los datos de respuesta para una consulta en detalle
	 * @param useCaseConf
	 * @param sessionID
	 * @param idBook
	 * @param tipoRegistro
	 * @param queryResults
	 * @param listRegistro
	 * @throws BookException
	 * @throws SessionException
	 * @throws ValidationException
	 */
	public static void fillQueryDetailResult(UseCaseConf useCaseConf, String sessionID,
			int idBook, String tipoRegistro, AxSfQueryResults queryResults, List<Registro> listRegistro) throws BookException, SessionException, ValidationException {
		
		Registro registro = null;		
		AxSf axSf = null;
		 
		if(tipoRegistro.equals(Constants.REGISTRO_ENTRADA)){
			axSf = (AxSfIn) queryResults.getResults().iterator().next();
		} else if(tipoRegistro.equals(Constants.REGISTRO_SALIDA)) {
			axSf = (AxSfOut) queryResults.getResults().iterator().next();
		}
					    
		registro = new Registro();
		
	    Integer fdrid = new Integer(axSf.getAttributeValueAsString(Registro.FDRID));
		    
		registro.setNumeroRegistro(axSf.getAttributeValueAsString(Registro.NUM_REGISTRO));
		registro.setFechaRegistro( (axSf.getAttributeValue(Registro.FECHA_REGISTRO) != null) ? SigmUtil.formatTimeStampInString((Date) axSf.getAttributeValue(Registro.FECHA_REGISTRO)) : null);

		//FD3-USUARIO Y DF4-FECHATRABAJO
		
		ScrOfic oficina = (ScrOfic) axSf.getFld5();
		registro.setOficina(oficina.getName());
		registro.setEstadoRegistro(RBUtil.getInstance(useCaseConf.getLocale()).getProperty("book.fld6." +axSf.getAttributeValueAsString(Registro.ESTADO_REGISTRO)));			
		registro.setOrigen(axSf.getFld7Name());
		registro.setDestino(axSf.getFld8Name());
		
		if(tipoRegistro.equals(Constants.REGISTRO_ENTRADA)){
			registro.setNumeroRegistroOriginal(axSf.getAttributeValueAsString(Registro.NUM_REG_ORIGINAL));
			registro.setTipoRegistroOriginal(axSf.getAttributeValueAsString(Registro.TIPO_REG_ORIGINAL));

			registro.setFechaRegistroOriginal(axSf.getAttributeValue(Registro.FECHA_REG_ORIGINAL) != null ? SigmUtil.formatTimeStampInString((Date)axSf.getAttributeValue(Registro.FECHA_REG_ORIGINAL)): null);
			
			registro.setTipoTransporte(axSf.getAttributeValueAsString(Registro.TRANSPORTE_IN));
			registro.setNumeroTransporte(axSf.getAttributeValueAsString(Registro.NUM_TRANSPORTE_IN));
			registro.setTipoAsunto(axSf.getAttributeValueAsString(Registro.ASUNTO_IN));			
			registro.setResumen(axSf.getAttributeValueAsString(Registro.RESUMEN_IN));
			
			// CAMPO EXTENDIDO
			if(axSf.getExtendedFields().get(Registro.COMENTARIO_POS_IN) != null){
				registro.setComentario(((AxXf)axSf.getExtendedFields().get(Registro.COMENTARIO_POS_IN)).getText());					
			}
			if(axSf.getExtendedFields().get(Registro.REF_EXPEDIENTE_POS_IN) != null){
				registro.setRefExpediente(((AxXf)axSf.getExtendedFields().get(Registro.REF_EXPEDIENTE_POS_IN)).getText());
			}
		
		}else if(tipoRegistro.equals(Constants.REGISTRO_SALIDA)){
			registro.setTipoTransporte(axSf.getAttributeValueAsString(Registro.TRANSPORTE_OUT));
			registro.setNumeroTransporte(axSf.getAttributeValueAsString(Registro.NUM_TRANSPORTE_OUT));
			registro.setTipoAsunto(axSf.getAttributeValueAsString(Registro.ASUNTO_OUT));			
			registro.setResumen(axSf.getAttributeValueAsString(Registro.RESUMEN_OUT));
			
			// CAMPO EXTENDIDO
			if(axSf.getExtendedFields().get(Registro.COMENTARIO_POS_OUT) != null){
				registro.setComentario(((AxXf)axSf.getExtendedFields().get(Registro.COMENTARIO_POS_OUT)).getText());					
			}
		}
		    
		
	    /////////////////////////////////////
		// INTERESADOS
	    /////////////////////////////////////
    	log.debug("Obteniendo Interesados");		
		ElementoDatosInteresados elemInteresados = new ElementoDatosInteresados();

		fillInteresados(elemInteresados, idBook, fdrid, useCaseConf); 
		    	
		registro.setInteresados(elemInteresados);
			
		
    	log.debug("Obteniendo HISTÓRICO IR");		
	    /////////////////////////////////////
		// HISTORICO DE INTERCAMBIO REGISTRAL
	    /////////////////////////////////////
		HistoricoIntercambioRegistral listHist = new HistoricoIntercambioRegistral();
		
		fillHistoricoIR(listHist, idBook, fdrid, oficina.getId()); 
    	
		registro.setHistoricoIntercambioRegistral(listHist);
			
	    ////////////////////////////
		// HISTORICO DE DISTRIBUCIÓN
	    ////////////////////////////
    	log.debug("Obteniendo HISTÓRICO DE DISTRIBUCIÓN");
		List<DistribucionRegistro> listDist = new ArrayList<DistribucionRegistro>();
		
		fillHistoricoDistribucion(listDist, useCaseConf, idBook, fdrid); 
	    
		if(listDist != null && !listDist.isEmpty())
			registro.setHistoricoDistribucion(new HistoricoDistribucion(listDist));

		
	    ////////////////////////////
		// HISTORICO DE MOVIMIENTOS
	    ////////////////////////////
	    log.debug("Obteniendo HISTÓRICO DE MOVIMIENTOS");		    
		List<MovimientoRegistro> listMov = new ArrayList<MovimientoRegistro>();
		
		fillHistoricoMovimiento(listMov, useCaseConf, idBook, fdrid, axSf, registro.getNumeroRegistro()); 

		if(listMov != null && !listMov.isEmpty())
			registro.setHistoricoMovimiento(new HistoricoMovimiento(listMov));
			
			
			
	    /////////////////////////////////////
		// DOCUMENTOS
	    /////////////////////////////////////
			
		List<AxDoch> docs = null;	
		docs = FolderFileSession.getBookFolderDocsWithPages(sessionID, idBook, fdrid, useCaseConf.getEntidadId());
		
		List<Documento> documentos = null;
		if(docs.size()!=0){
			log.debug("Documentos: "+docs.size());
			documentos = new ArrayList<Documento>();
			Documento docXml = null;
			for(AxDoch doc: docs){
				List pages = doc.getPages();
				if(pages.size()!=0){
					AxPageh page = (AxPageh)pages.get(0);
					log.debug("Nombre completo: "+page.getName());
					docXml = new Documento();
					docXml.setNombre(page.getName());
					documentos.add(docXml);	
				}
				log.debug("Documento sin páginas. ID: "+doc.getId()+"; doc: "+doc.getName());
			}
			
			Documentos docsXml = new Documentos();
			docsXml.setDocumento(documentos);
			registro.setDocumentos(docsXml);
			
		}					
		listRegistro.add(registro);				
		
	}		

	private static void fillInteresados(ElementoDatosInteresados elemInteresados, int idBook,
			Integer fdrid, UseCaseConf useCaseConf) {
		InterestedBo interestedBo = new InterestedBo();
		List<Interesado> interesados = interestedBo.getAllInterested(idBook, fdrid, useCaseConf);
		
		
		if(interesados != null){

			List<InteresadoFisico> interesadoFisico = new ArrayList<InteresadoFisico>();
			List<InteresadoJuridico> interesadoJuridico = new ArrayList<InteresadoJuridico>();
			
			for(Interesado item: interesados){
				
				if(item.getTipo().equals(Constants.TIPO_PERSONA_FISICA)){
					InteresadoFisico elemInteresado = new InteresadoFisico();
					elemInteresado.setNumeroDocumento(item.getDocIdentidad());
					elemInteresado.setTipoDocumento(item.getTipodoc());
					elemInteresado.setNombre(item.getNombre());
					elemInteresado.setPrimerApellido(item.getPapellido());
					elemInteresado.setSegundoApellido(item.getSapellido());
					
					Representante rep = item.getRepresentante();
					if(rep != null){
						ElementoRepresentante elemRepresentante = new ElementoRepresentante();
						if(rep.getTipo().equals(Constants.TIPO_PERSONA_FISICA)){
							RepresentanteFisico repItem = new RepresentanteFisico();
							repItem.setNombre(rep.getNombre());
							repItem.setPrimerApellido(rep.getPapellido());
							repItem.setSegundoApellido(rep.getSapellido());
							repItem.setNumeroDocumento(rep.getDocIdentidad());
							repItem.setTipoDocumento(rep.getTipodoc());
							elemRepresentante.setRepresentanteFisico(repItem);
							
						} else if(rep.getTipo().equals(Constants.TIPO_PERSONA_JURIDICA)){
							RepresentanteJuridico repItem = new RepresentanteJuridico();
							repItem.setNumeroDocumento(rep.getDocIdentidad());
							repItem.setTipoDocumento(rep.getTipodoc());
							repItem.setRazonSocial(rep.getRazonSocial());							
							elemRepresentante.setRepresentanteJuridico(repItem);
						}
						
						elemInteresado.setRepresentante(elemRepresentante);
						
					}
					interesadoFisico.add(elemInteresado);
				} 
				else if(item.getTipo().equals(Constants.TIPO_PERSONA_JURIDICA)){
					InteresadoJuridico elemInteresado = new InteresadoJuridico();
					elemInteresado.setNumeroDocumento(item.getDocIdentidad());
					elemInteresado.setTipoDocumento(item.getTipodoc());
					elemInteresado.setRazonSocial(item.getRazonSocial());
					
					Representante rep = item.getRepresentante();
					if(rep != null){
						ElementoRepresentante elemRepresentante = new ElementoRepresentante();
						if(rep.getTipo().equals(Constants.TIPO_PERSONA_FISICA)){
							RepresentanteFisico repItem = new RepresentanteFisico();
							repItem.setNombre(rep.getNombre());
							repItem.setPrimerApellido(rep.getPapellido());
							repItem.setSegundoApellido(rep.getSapellido());
							repItem.setNumeroDocumento(rep.getDocIdentidad());
							repItem.setTipoDocumento(rep.getTipodoc());
							elemRepresentante.setRepresentanteFisico(repItem);
							
						} else if(rep.getTipo().equals(Constants.TIPO_PERSONA_JURIDICA)){
							RepresentanteJuridico repItem = new RepresentanteJuridico();
							repItem.setNumeroDocumento(rep.getDocIdentidad());
							repItem.setTipoDocumento(rep.getTipodoc());
							repItem.setRazonSocial(rep.getRazonSocial());							
							elemRepresentante.setRepresentanteJuridico(repItem);
						}
						
						elemInteresado.setRepresentante(elemRepresentante);
					
					}
					interesadoJuridico.add(elemInteresado);
				}
			}
			if (interesadoFisico.size()!= 0)
				elemInteresados.setInteresadoFisico(interesadoFisico);
			if (interesadoJuridico.size()!= 0)
				elemInteresados.setInteresadoJuridico(interesadoJuridico);
			
		}
	}

	private static void fillHistoricoIR(HistoricoIntercambioRegistral listHist, int idBook,
					Integer fdrid, Integer idOficina) {
		
	    IntercambioRegistralManager intercambioManager = IsicresManagerProvider.getInstance().getIntercambioRegistralManager();

	    //ENTRADA
			List<IntercambioRegistralEntradaVO> IREntradaVO = null;
			IREntradaVO = intercambioManager.getHistorialIntercambioRegistralEntrada(String.valueOf(idBook), String.valueOf(fdrid), String.valueOf(idOficina));
			    

			HistoricoIntercambioRegistralEntrada histIREntr = null;
		    List<IntercambioRegistralEntrada> listIREntrada;
			if(IREntradaVO.size() !=0 )
			{
				histIREntr = new HistoricoIntercambioRegistralEntrada();
				listIREntrada = new ArrayList<IntercambioRegistralEntrada>();  
				IntercambioRegistralEntrada IREntrada = null;
			
			    for(IntercambioRegistralEntradaVO entrada: IREntradaVO){
			    	IREntrada = new IntercambioRegistralEntrada();

			    	IREntrada.setFechaIntercambio(entrada.getFechaIntercambio().toString());
			    	IREntrada.setEstado(entrada.getEstado().getName());
			    	IREntrada.setFechaEstado(entrada.getFechaEstado().toString());
			    	IREntrada.setUsuario(entrada.getUsername());
			    	
			    	log.debug("FECHA INTERCAMBIO: "+entrada.getFechaIntercambio());
			    	log.debug("ESTADO: "+entrada.getEstado().getName());
			    	log.debug("FECHA ESTADO: "+entrada.getFechaEstado());
			    	log.debug("USUARIO: "+entrada.getUsername());
			    	String IREntradas = "";
			    	
				    List<TrazabilidadVO> listTrazabilidadVO = entrada.getTrazas();
				    for(TrazabilidadVO item: listTrazabilidadVO){
				    	log.debug("CODIGO: "+item.getCodigo());
				    	log.debug("DESCRIPCIÓN: "+item.getDescripcion());
				    	log.debug("COD. ERROR: "+item.getCodigoError());
				    	log.debug("COD. ERROR SERVICIO: "+item.getCodigoErrorServicio());
				    	log.debug("COD. INTERCAMBIO: "+item.getCodigoIntercambio());
				    	log.debug("ENT. REG. ORIGEN: "+item.getCodigoEntidadRegistralOrigen());
				    	log.debug("ENT. REG. DESTINO: "+item.getCodigoEntidadRegistralDestino());
				    	log.debug("ESTADO (DESC): "+EstadoTrazabilidadEnum.getDescripcion(item.getCodigoEstado()));
				    	log.debug("CODIGO NODO: "+item.getCodigoNodo());
				    	log.debug("MOTIVO RECHAZO: "+item.getMotivoRechazo());
				    	log.debug("FECHA ALTA: "+item.getFechaAlta());
				    	log.debug("FECHA MODIF.: "+item.getFechaModificacion());
				    	
				    	IREntradas += "CODIGO: "+entrada.getFechaIntercambio();
				    	IREntradas += "CODIGO: "+item.getCodigo();
				    	IREntradas += "DESCRIPCIÓN: "+item.getDescripcion();
				    	IREntradas += "COD. ERROR: "+item.getCodigoError();
				    	IREntradas += "COD. ERROR SERVICIO: "+item.getCodigoErrorServicio();
				    	IREntradas += "COD. INTERCAMBIO: "+item.getCodigoIntercambio();
				    	IREntradas += "ENT. REG. ORIGEN: "+item.getCodigoEntidadRegistralOrigen();
				    	IREntradas += "ENT. REG. DESTINO: "+item.getCodigoEntidadRegistralDestino();
				    	IREntradas += "ESTADO (DESC): "+EstadoTrazabilidadEnum.getDescripcion(item.getCodigoEstado());
				    	IREntradas += "CODIGO NODO: "+item.getCodigoNodo();
				    	IREntradas += "MOTIVO RECHAZO: "+item.getMotivoRechazo();
				    	IREntradas += "FECHA ALTA: "+item.getFechaAlta();
				    	IREntradas += "FECHA MODIF.: "+item.getFechaModificacion();
				    	
				    }
				    
				    IREntrada.setTrazasIREntrada(IREntradas);
				    listIREntrada.add(IREntrada);
				    
			    }
				histIREntr.setIntercambioRegistralEntrada(listIREntrada);
	    	}
			
	    //SALIDA
			List<IntercambioRegistralSalidaVO> IRSalidaVO = null;
			IRSalidaVO = intercambioManager.getHistorialIntercambioRegistralSalida(
			    		String.valueOf(idBook), String.valueOf(fdrid), String.valueOf(idOficina));
			    
			
			HistoricoIntercambioRegistralSalida histIRSal = null;
		    List<IntercambioRegistralSalida> listIRSalida;
			if(IRSalidaVO.size() !=0 )
			{
				histIRSal = new HistoricoIntercambioRegistralSalida();
				listIRSalida = new ArrayList<IntercambioRegistralSalida>();  
				IntercambioRegistralSalida IRSalida = null;
			
			    for(IntercambioRegistralSalidaVO salida: IRSalidaVO){
			    	IRSalida = new IntercambioRegistralSalida();
			    	IRSalida.setFechaIntercambio(salida.getFechaIntercambio().toString());
			    	IRSalida.setOficina(salida.getNombreOfic());
			    	IRSalida.setTipoOrigen(salida.getTipoOrigen().toString());
			    	IRSalida.setEntidadDestino(salida.getCodeTramunit()+":"+salida.getNameTramunit());
			    	IRSalida.setUnidadDestino(salida.getCodeEntity()+":"+salida.getNameEntity());
			    	IRSalida.setEstado(salida.getEstado().getName());
			    	IRSalida.setFechaEstado(salida.getFechaEstado().toString());
		    	
			    	log.debug("FECHA INTERCAMBIO: "+salida.getFechaIntercambio());
			    	log.debug("OFICINA: "+salida.getNombreOfic());
			    	log.debug("TIPO ORIGEN: "+salida.getTipoOrigen());
			    	log.debug("ENTIDAD DESTINO: "+salida.getCodeTramunit()+
			    			" VALOR: "+salida.getNameTramunit());
			    	log.debug("UNIDAD DESTINO: "+salida.getCodeEntity()+
			    			" VALOR: "+salida.getNameEntity());
//			    	LOG.debug("UNIDAD DESTINO: "+salida.GETgetFechaEstado());
			    	log.debug("ESTADO: "+salida.getEstado().getName());
			    	log.debug("FECHA ESTADO: "+salida.getFechaEstado());
			    	
				    List<TrazabilidadVO> listTrazabilidadVO = salida.getTrazas();
				    for(TrazabilidadVO item: listTrazabilidadVO){
				    	log.debug("CODIGO: "+item.getCodigo());
				    	log.debug("DESCRIPCIÓN: "+item.getDescripcion());
				    	log.debug("COD. ERROR: "+item.getCodigoError());
				    	log.debug("COD. ERROR SERVICIO: "+item.getCodigoErrorServicio());
				    	log.debug("COD. INTERCAMBIO: "+item.getCodigoIntercambio());
				    	log.debug("ENT. REG. ORIGEN: "+item.getDescripcionEntidadRegistralOrigen());
				    	log.debug("ENT. REG. DESTINO: "+item.getDescripcionEntidadRegistralDestino());
				    	log.debug("ESTADO (DESC): "+EstadoTrazabilidadEnum.getDescripcion(item.getCodigoEstado()));
				    	log.debug("CODIGO NODO: "+item.getCodigoNodo());
				    	log.debug("MOTIVO RECHAZO: "+item.getMotivoRechazo());
				    	log.debug("FECHA ALTA: "+item.getFechaAlta());
				    	log.debug("FECHA MODIF.: "+item.getFechaModificacion());
				    }

//				    IRSalida.setTrazasIREntrada(IRSalida);
				    listIRSalida.add(IRSalida);
				    
			    }
				histIRSal.setIntercambioRegistralSalida(listIRSalida);
			    
			}
			
			if(histIREntr != null || histIRSal != null){					
//				listHist = new HistoricoIntercambioRegistral();
				listHist.setHistoricoIntercambioRegistralEntrada(histIREntr);
				listHist.setHistoricoIntercambioRegistralSalida(histIRSal);
			}
					
		
	}


	private static void fillHistoricoMovimiento(List<MovimientoRegistro> listMov,
			UseCaseConf useCaseConf, int idBook, Integer fdrid, AxSf axSf, String numeroRegistro) throws BookException, SessionException, ValidationException {

		List listUpdHisFdrResults = null;
		listUpdHisFdrResults = FolderHistSession.getUpdHisFdrResults(useCaseConf.getSessionID(), 
				useCaseConf.getLocale(), idBook,  fdrid, axSf, numeroRegistro , useCaseConf.getEntidadId());

		if(listUpdHisFdrResults.size() != 0){
			
//			listMov = new ArrayList<MovimientoRegistro>();
			
		    MovimientoRegistro mov = null;
		    
			for(Object obj: listUpdHisFdrResults){
				UpdHisFdrResults item = (UpdHisFdrResults) obj;
				log.debug("Mov: "+item.getScrModifReg().getUsr()+" ; "+item.getScrModifReg().getModifDate()+" ; "+item.getNameFld()+" ; "+item.getValue()+" ; "+item.getOldvalue());
				
				mov = new MovimientoRegistro();
				mov.setUsuario(item.getScrModifReg().getUsr());
				mov.setFecha(item.getScrModifReg().getModifDate().toString());
				mov.setCampo(item.getNameFld());
				mov.setValorAntiguo(item.getOldvalue());
				mov.setValorNuevo(item.getValue());
				
				listMov.add(mov);
			}
		}
	}


	private static void fillHistoricoDistribucion(List<DistribucionRegistro> listDist,  UseCaseConf useCaseConf, int idBook, int fdrid) throws BookException, SessionException, ValidationException {
		
		
    	List<DtrFdrResults> listDtrFdrResults = null;
    	listDtrFdrResults =
    	    DistributionSession.getDtrFdrResults(
    		useCaseConf.getSessionID(), idBook, fdrid, useCaseConf.getEntidadId(),
    		useCaseConf.getUseLdap().booleanValue());	    	
    	
		if(listDtrFdrResults.size() != 0){						
		
//			listDist = new ArrayList<DistribucionRegistro>();				
			DistribucionRegistro dist = null;
		
			for(DtrFdrResults item: listDtrFdrResults){
				dist = new DistribucionRegistro();
				dist.setFechaDistribucion(item.getScrDistReg().getDistDate().toString());
				dist.setOrigenDistribucion(item.getSourceDescription());
				dist.setDestinoDistribucion(item.getTargetDescription());
				dist.setEstado(EstadoDistribucionEnum.getEnum(item.getScrDistReg().getState()).getName());
				dist.setFechaEstado(item.getScrDistReg().getStateDate().toString());
				dist.setComentario(item.getScrDistReg().getMessage());
		    	log.debug("FECHA DISTRIBUCIÓN: "+item.getScrDistReg().getDistDate());
		    	log.debug("ORIGEN DISTRIBUCIÓN: "+item.getSourceDescription());
		    	log.debug("DESTINO DISTRIBUCIÓN: "+item.getTargetDescription());
		    	log.debug("ESTADO: "+EstadoDistribucionEnum.getEnum(item.getScrDistReg().getState()).getName());
		    	log.debug("FECHA ESTADO: "+item.getScrDistReg().getStateDate());
		    	log.debug("COMENTARIO: "+item.getScrDistReg().getMessage());
		    	
		    	String estadosDistribucion = "";
		    	//TRAZAS-ESTADO
			    for(Object obj: item.getScrDistRegState()){
			    	ScrDistregstate itemState = (ScrDistregstate)obj;
			    	
			    	log.debug("ESTADO: "+EstadoDistribucionEnum.getEnum(itemState.getState()).getName());
			    	log.debug("FECHA DISTRIBUCIÓN: "+itemState.getStateDate());
			    	log.debug("USUARIO: "+itemState.getUsername());
			    	 
			    	estadosDistribucion +="ESTADO: "+EstadoDistribucionEnum.getEnum(itemState.getState()).getName();
			    	estadosDistribucion +="FECHA DISTRIBUCIÓN: "+itemState.getStateDate();
			    	estadosDistribucion +="USUARIO: "+itemState.getUsername();
			    }
			    dist.setEstadosDistribucion(estadosDistribucion);
			    listDist.add(dist);
			    
			}	 
		}
		
	}


	public List<XtField> recuperarValorXtfield(String entidad, String libro, int fdrid) throws SigmWSException  {

		List<XtField> listXtfield = new ArrayList<XtField>();

		List<AxXf> listAxxf = recuperarCamposExtendidos(entidad, libro, fdrid);

		log.debug("Campos extendidos (NO SIGEM). Actualizando valor...: ");
		try {
			for (AxXf item : listAxxf) {
				XtField xtField;
					xtField = SIGMServiceManager.getXtfieldService().getById(item.getFldId());
					if (xtField != null) {
						log.debug("id/nombre/descripcion/seccion campo: " + item.getFldId() + "; "
								+ xtField.getName() + "; " + xtField.getDescripcion() + "; "
								+ xtField.getSeccion());
						xtField.setValor(item.getText());
						log.debug("Valor campo: " + xtField.getValor());
						listXtfield.add(xtField);
					}
			}
		
		} catch (DaoException e) {
			throw new SigmWSException("err.dao.camposExtendidos", new String[]{String.valueOf(fdrid)}, e);
		}

		return listXtfield;
	}

    private List<AxXf> recuperarCamposExtendidos(String entidad, String libro, int fdrid) throws SigmWSException {

    	// RECUPERAR LISTA DE CAMPOS EXTENDIDOS (REGISTROS) PARA UN FDRID
    	AxXfEntity axXfEntity = null;
		List<AxXf> listAxxf = new ArrayList<AxXf>();
		
		AxXfEntityMSSSI axxfMSSSI = new AxXfEntityMSSSI();
		List<Integer> listFrdid = new ArrayList<Integer>();
		try {
			listFrdid = axxfMSSSI.findByFrdid(fdrid, entidad, libro);
			
			log.debug("Num. campos extendidos asociados a registro (fdrid="+fdrid+") : "+ listFrdid.size());
			
			for(Integer field: listFrdid){
				axXfEntity = new AxXfEntity();
				
				AxXfPK pk = new AxXfPK(libro, fdrid, field); 
				
				axXfEntity.findByPrimaryKey(pk, entidad);
				axXfEntity.load(entidad);
				AxXf axsfAux = axXfEntity.getAxXf();
//    				fullInfoRegister.addExtendido(field.toString(), axsfAux.getText());
				listAxxf.add(axsfAux);
				
			}
		} catch (Exception e) {
			throw new SigmWSException("err.sigm.bbdd.camposExtendidos", new String[]{String.valueOf(fdrid)}, e);
		}

		return listAxxf;
    }


	public static List<FlushFdrField> toFlushfdrfield(InputRegisterBean inputRegisterBean, OutputRegisterBean outputRegisterBean) {
		
		List<FlushFdrField> listFlushFdrField = new ArrayList<FlushFdrField>();		
		FlushFdrField field;

		// SOLO ENTRADA
		if(inputRegisterBean != null){
			
			if(inputRegisterBean.getFld5() != null ){
				field = new FlushFdrField();
				field.setFldid(OFICINA_REGISTRO_IN_OUT);
				field.setCtrlid(OFICINA_REGISTRO_IN_OUT);
				field.setValue(inputRegisterBean.getFld5().getCode());	//999 (REGISTRO ELECTRONICO)
				listFlushFdrField.add(field);			
			}
			
			if(inputRegisterBean.getFld7() != null){
				field = new FlushFdrField();
				field.setFldid(ORG_ORIGEN_IN_OUT);
				field.setCtrlid(ORG_ORIGEN_IN_OUT);
				field.setValue(inputRegisterBean.getFld7().getCode());
				listFlushFdrField.add(field);
			}
			
			if(inputRegisterBean.getFld8() != null ){
				field = new FlushFdrField();
				field.setFldid(ORG_DESTINO_IN_OUT);
				field.setCtrlid(ORG_DESTINO_IN_OUT);
				field.setValue(inputRegisterBean.getFld8().getCode());
				listFlushFdrField.add(field);
			}
			if(inputRegisterBean.getFld11() != null){				
				field = new FlushFdrField();
				field.setFldid(TIPO_REGISTRO_ORIGINAL);
				field.setCtrlid(TIPO_REGISTRO_ORIGINAL);
				Integer fld11 = inputRegisterBean.getFld11();
				String validacion = fld11 == 1 ? Constants.REGISTRO_ENTRADA_TEXT : Constants.REGISTRO_SALIDA_TEXT;
				field.setValue(validacion);  
				listFlushFdrField.add(field);		
			}
			
			if(inputRegisterBean.getFld12() != null){				
				field = new FlushFdrField();
				field.setFldid(FECHA_ORIGINAL_IN_OUT);
				field.setCtrlid(FECHA_ORIGINAL_IN_OUT);
			    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT);
				field.setValue(sdf.format(inputRegisterBean.getFld12()));
				listFlushFdrField.add(field);		
			}
//			if(inputRegisterBean.getFld13() != null){				
//				field = new FlushFdrField();
//				field.setFldid(REG_ORIGINAL_IN);
//				field.setCtrlid(REG_ORIGINAL_IN);
//				field.setValue(String.valueOf(inputRegisterBean.getFld13()));  
//				listFlushFdrField.add(field);		
//			}
			if(inputRegisterBean.getFld14() != null){
				field = new FlushFdrField();
				field.setFldid(TRANSPORTE_IN);
				field.setCtrlid(TRANSPORTE_IN);
				field.setValue(inputRegisterBean.getFld14());
				listFlushFdrField.add(field);						
			}
			if(inputRegisterBean.getFld15() != null){
				field = new FlushFdrField();
				field.setFldid(NUM_TRANSPORTE_IN);
				field.setCtrlid(NUM_TRANSPORTE_IN);
				field.setValue(inputRegisterBean.getFld15());
				listFlushFdrField.add(field);						
			}
			if(inputRegisterBean.getFld16() != null){
				field = new FlushFdrField();
				field.setFldid(ASUNTO_IN);
				field.setCtrlid(ASUNTO_IN);
				field.setValue(inputRegisterBean.getFld16().getCode());
				listFlushFdrField.add(field);						
			}
			if(inputRegisterBean.getFld17() != null){
				field = new FlushFdrField();
				field.setFldid(RESUMEN_IN);
				field.setCtrlid(RESUMEN_IN);
				field.setValue(inputRegisterBean.getFld17());
				listFlushFdrField.add(field);						
			}
			
			//NO: if(inputRegisterBean.getFld18() != null)
//			if(inputRegisterBean.getFld19() != null){
//				field = new FlushFdrField();
//				field.setFldid(REF_EXPEDIENTE_IN);
//				field.setCtrlid(REF_EXPEDIENTE_IN);
//				field.setValue(inputRegisterBean.getFld19());
//				listFlushFdrField.add(field);						
//			}
//			if(inputRegisterBean.getFld20() != null){
//				field = new FlushFdrField();
//				field.setFldid(FECHA_DOCUMENTACION_IN);
//				field.setCtrlid(FECHA_DOCUMENTACION_IN);
//				field.setValue(inputRegisterBean.getFld20());
//				listFlushFdrField.add(field);						
//			}
			
		}else if(outputRegisterBean != null){
			if(outputRegisterBean.getFld5() != null){
				field = new FlushFdrField();
				field.setFldid(OFICINA_REGISTRO_IN_OUT);
				field.setCtrlid(OFICINA_REGISTRO_IN_OUT);
				field.setValue(outputRegisterBean.getFld5().getCode());	//999 (REGISTRO ELECTRONICO)
				listFlushFdrField.add(field);			
			}
			
			if(outputRegisterBean.getFld7() != null){
				field = new FlushFdrField();
				field.setFldid(ORG_ORIGEN_IN_OUT);
				field.setCtrlid(ORG_ORIGEN_IN_OUT);
				field.setValue(outputRegisterBean.getFld7().getCode());
				listFlushFdrField.add(field);
			}
			
			if(outputRegisterBean.getFld8() != null){
				field = new FlushFdrField();
				field.setFldid(ORG_DESTINO_IN_OUT);
				field.setCtrlid(ORG_DESTINO_IN_OUT);
				field.setValue(outputRegisterBean.getFld8().getCode());
				listFlushFdrField.add(field);
			}
			
			if(outputRegisterBean.getFld10() != null){
				field = new FlushFdrField();
				field.setFldid(TRANSPORTE_OUT);
				field.setCtrlid(TRANSPORTE_OUT);
				field.setValue(outputRegisterBean.getFld10());
				listFlushFdrField.add(field);						
			}
			if(outputRegisterBean.getFld11() != null){
				field = new FlushFdrField();
				field.setFldid(NUM_TRANSPORTE_OUT);
				field.setCtrlid(NUM_TRANSPORTE_OUT);
				field.setValue(outputRegisterBean.getFld11());
				listFlushFdrField.add(field);						
			}
			
			if(outputRegisterBean.getFld12() != null){
				field = new FlushFdrField();
				field.setFldid(ASUNTO_OUT);
				field.setCtrlid(ASUNTO_OUT);
				field.setValue(outputRegisterBean.getFld12().getCode());
				listFlushFdrField.add(field);						
			}
			if(outputRegisterBean.getFld13() != null){
				field = new FlushFdrField();
				field.setFldid(RESUMEN_OUT);
				field.setCtrlid(RESUMEN_OUT);
				field.setValue(outputRegisterBean.getFld13());
				listFlushFdrField.add(field);						
			}
			
			//NO: if(outputRegisterBean.getFld14() != null)
			
		}
		
		return listFlushFdrField;
	}


	/**
	 * Obtiene el fdrid a partir del número de registro
	 * @param useCaseConf
	 * @param idBook
	 * @param numeroRegistro
	 * @return
	 * @throws BookException
	 * @throws SessionException
	 * @throws ValidationException
	 */
	public static Integer getFdrdid(UseCaseConf useCaseConf, int idBook, String numeroRegistro) throws BookException, SessionException, ValidationException {
		AxSfQuery axsfQuery = new AxSfQuery();
		Integer fdrid = null;
		
    	if(idBook == Constants.REGISTRO_ELECTRONICO_ENTRADA)
    	{
		    SearchInputRegisterBean searchInputRegisterBean = new SearchInputRegisterBean();
		    searchInputRegisterBean.setFld1Value(numeroRegistro);					 
	    	axsfQuery.addField(searchInputRegisterBean.fieldtoQuery(AxSf.FLD1_FIELD, idBook)); 
    	} else {
    		SearchOutputRegisterBean searchOutputRegisterBean = new SearchOutputRegisterBean();
    		searchOutputRegisterBean.setFld1Value(numeroRegistro);			 
	    	axsfQuery.addField(searchOutputRegisterBean.fieldtoQuery(AxSf.FLD1_FIELD, idBook)); 	    		
    	}
			
    	axsfQuery.setBookId(idBook);			
			
		List<Integer> bookIds = new ArrayList<Integer>();
		bookIds.add(idBook);

    	// Lanzar la consulta
    	int size = FolderSession.openRegistersQuery(useCaseConf.getSessionID(), axsfQuery, bookIds,
					Constants.REPORT_OPTION_0, useCaseConf.getEntidadId());

		if (size != 0) {
			AxSfQueryResults queryResults = null;
			queryResults = FolderSession.navigateRegistersQuery(useCaseConf.getSessionID(),
					idBook, com.ieci.tecdoc.common.isicres.Keys.QUERY_ALL,
					useCaseConf.getLocale(), useCaseConf.getEntidadId(), null);

			AxSf axSf = null;
			for (Iterator<AxSf> it = queryResults.getResults().iterator(); it.hasNext();) {
			
				if(idBook == Constants.REGISTRO_ELECTRONICO_ENTRADA){
					axSf = (AxSfIn) it.next();
				} else if(idBook == Constants.REGISTRO_ELECTRONICO_SALIDA) {
					axSf = (AxSfOut) it.next();
				}
						
			
				fdrid = new Integer(axSf.getAttributeValueAsString("fdrid"));
			}
			
		}
		return fdrid;
	}


	public Acuse generarAcuse(UseCaseConf useCaseConf, String tipoRegistro, int idBook, String numeroRegistro,
			Integer fdrid) throws SigmWSException, DaoException, BookException, SessionException, ValidationException {
		
		Acuse result = null;
		List<XtField> listXtfield = new ArrayList<XtField>();
		
		// RECUPERAMOS CAMPOS EXTENDIDOS			
		listXtfield = recuperarValorXtfield(Constants.SIGEM_ENTIDAD_MSSSI,String.valueOf(idBook),fdrid);
		
		 
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(Constants.JASPER_PARAM_IDBOOK, idBook);
	    params.put(Constants.JASPER_PARAM_MAXREPORTREGISTERS, Constants.MAX_REPORT_REGISTER);
	    params.put(Constants.JASPER_PARAM_FDRID, fdrid);
			
		String reportTemplateName = "";
		String reportName = "";
		String reportExpression = "";
		Map<String,Object> mapReport = null;
		ReportUtil reportUtil = new ReportUtil();
		
		if(tipoRegistro.equalsIgnoreCase(Constants.REGISTRO_ENTRADA)){
			reportTemplateName = Constants.IR_REPORT_CERTIFICATE_TEMPLATE_NAME;					
			reportName = Constants.IR_REPORT_CERTIFICATE_NAME +numeroRegistro+ Constants.FILE_NAME_EXTENSION_PDF;					
			InputRegisterReportsCert inputRegisterReportsCert = SIGMServiceManager.getInputRegisterReportsCertService().getByParams(params);
			mapReport = reportUtil.fillInputRegisterReportsList(inputRegisterReportsCert);
		}
		else
		{
			reportTemplateName = Constants.OR_REPORT_CERTIFICATE_TEMPLATE_NAME;					
			reportName = Constants.OR_REPORT_CERTIFICATE_NAME +numeroRegistro+ Constants.FILE_NAME_EXTENSION_PDF;					
			OutputRegisterReportsCert outputRegisterReportsCert = SIGMServiceManager.getOutputRegisterReportsCertService().getByParams(params);
			mapReport = reportUtil.fillOutputRegisterReportsList(outputRegisterReportsCert);
		}
		
		reportExpression = Constants.XML_EXPRESION_ROOT;

		// DATOS EXTENDIDOS
		reportUtil.addExtendedFields(mapReport, listXtfield);

		
		// DOCUMENTOS
		reportUtil.addDocuments(mapReport, useCaseConf.getSessionID(), idBook, fdrid, Constants.SIGEM_ENTIDAD_MSSSI);
		
		// CREAMOS EL XML CON LOS DATOS PARA LA PLANTILLA
		Document docXml = reportUtil.createXML(mapReport, fdrid, idBook, useCaseConf);
		 
		String pathReports = Constants.PATH_REPO+ (String) WebParameter.getEntryParameter(Constants.PATH_REPORTS);
		
		log.debug("Path de las plantillas: "+pathReports);
		
		Map<Object, Object> paramsReport = new HashMap<Object, Object>();
		
		// CREAMOS EL INFORME
		byte[] pdfReport = reportUtil.buildReportToPdf(docXml,paramsReport,pathReports,reportTemplateName,reportExpression);

		// SE FIRMA
		result = PfeConnector.signReport(pdfReport, reportName);
		
		return result;
		
	}
	
    /**
     * Transforma un objeto de fecha Date en un String con formato
     * \"yyyy-MM-dd hh:mm:ss.SSS\".
     * 
     * @param date
     *            objeto a convertir.
     * @return dateFormatted fecha convertida.
     */
    public static String formatTimeStampInString(Date date) {
		String dateFormatted = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
		dateFormatted = simpleDateFormat.format(date);
		return dateFormatted;
    }


	public static void validateNombreFichero(String nombre) throws SigmWSException {

		int idxExtension = nombre.lastIndexOf(Constants.POINT_CHAR);
		if (idxExtension == -1 || idxExtension == nombre.length() - 1)
			throw new SigmWSException("err.validacion.files.noExtension", new String[] { nombre });

		if (idxExtension > RegisterServicesUtil.DOCUMENT_NAME_MAX_LENGTH - 1) {
			throw new SigmWSException("err.validacion.files.nameFileMax", new String[] { nombre,  String.valueOf(RegisterServicesUtil.DOCUMENT_NAME_MAX_LENGTH) });
		}
	}


	/**
	 * Valida el usuario y password de un usuario en SIGEM
	 * @param user
	 * @param passToValidate
	 * @param entidad
	 * @throws SigmWSException
	 */
	public static void validarUsuario(String user, String passToValidate, String entidad)
			throws SigmWSException {

		
		Transaction tran = null;
		try {
			Session session = HibernateUtil.currentSession(entidad);
			tran = session.beginTransaction();

			// Recuperamos el usuario
			List list = ISicresQueries.getUserUserHdrByName(session, user);

			if (list.size() == 0) {
				throw new SigmWSException("login.noValid");
			}

			Iuseruserhdr userHdr = (Iuseruserhdr) session.load(Iuseruserhdr.class,
					((Iuseruserhdr) list.get(0)).getId());

			String encPwd1 = CryptoUtils.encryptPassword(passToValidate, userHdr.getId());

			if (!encPwd1.equals(userHdr.getPassword())) {
				throw new SigmWSException("login.pass.noValid");
			}

			HibernateUtil.commitTransaction(tran);
//		} catch (SigmWSException e) {
//			throw e;
		} catch (HibernateException hE) {
			try {
				HibernateUtil.commitTransaction(tran);
			} catch (HibernateException cE) {
				log.warn("No se puede validar al usuario [" + user + "] password [" + passToValidate + "]");
				throw new SigmWSException("err.sigm.sql", cE);
			}
		} catch (Exception e) {
			throw new SigmWSException("login.pass.noValid", e);
		} finally {
			HibernateUtil.closeSession(entidad);
		}

	}
}

