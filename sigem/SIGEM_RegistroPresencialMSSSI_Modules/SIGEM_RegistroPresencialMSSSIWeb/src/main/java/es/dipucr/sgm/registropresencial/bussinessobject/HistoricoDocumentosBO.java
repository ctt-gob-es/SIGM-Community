package es.dipucr.sgm.registropresencial.bussinessobject;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.entity.dao.DBEntityDAOFactory;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.utils.BBDDUtils;
import com.ieci.tecdoc.common.utils.ISicresSaveQueries;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.businessobject.IGenericBo;

public class HistoricoDocumentosBO implements IGenericBo, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(HistoricoDocumentosBO.class);
	
	public static final int TIPO_CARPETA = 0;
	public static final int TIPO_DOCUMENTO = 1;
	
	private static final int CREAR = 0;
	private static final int EDITAR = 1;
	private static final int BORRAR = 2;
	private static final int ADJUNTAR = 3;
	private static final int ESCANEAR = 4;
	private static final int COMPULSAR = 5;
	private static final int CONSULTAR = 6;
	private static final int MODIFICAR_METADATO = 7;
	
	public static void historicoDocCrearEvent(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc) {
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombreDoc, CREAR, null);
	}
	
	public static void historicoDocEditarEvent(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc, String nombreDocNuevo) {
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombreDoc, EDITAR, nombreDocNuevo);
	}
	
	public static void historicoDocBorrarEvent(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc) {
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombreDoc, BORRAR, null);
	}
	
	public static void historicoDocAjuntarEvent(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc) {
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombreDoc, ADJUNTAR, null);
	}
	
	public static void historicoDocEscanearEvent(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc) {
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombreDoc, ESCANEAR, null);
	}
	
	public static void historicoDocCompulsarEvent(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc) {
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombreDoc, COMPULSAR, null);
	}
	
	public static void historicoDocConsultarEvent(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc) {
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombreDoc, CONSULTAR, null);
	}
	
	public static void historicoDocCambioMetadato(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc, String nombreMetadato, String valorMetadatoViejo, String valorMetadatoNuevo) {
		if(StringUtils.isEmpty(nombreDoc)){
			nombreDoc = "";
		}
		if(StringUtils.isEmpty(nombreMetadato)){
			nombreMetadato = "";
		}
		if(StringUtils.isEmpty(valorMetadatoViejo)){
			valorMetadatoViejo = "";
		}
		if(StringUtils.isEmpty(valorMetadatoNuevo)){
			valorMetadatoNuevo = "";
		}
		
		String nombre = nombreDoc + " - " + nombreMetadato;
		if(StringUtils.isNotEmpty(nombre) && nombre.length() > 255){
			nombre = nombre.substring(0, 249);
			nombre += "(...)";
		}
		
		String valor = valorMetadatoViejo + " -> " + valorMetadatoNuevo;
		if(StringUtils.isNotEmpty(valor) && valor.length() > 255){
			valor = valor.substring(0, 249);
			valor += "(...)";
		}
		
		insertaHistoricoDoc(useCaseConf, numReg, bookId, tipo, nombre, MODIFICAR_METADATO, valor);
	}
	
	public static void insertaHistoricoDoc(UseCaseConf useCaseConf, String numReg, int bookId, int tipo, String nombreDoc, int accion, String valorNuevo) {
		
		Transaction tran = null;
		try{
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());			
			tran = session.beginTransaction();			
			
			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
			AuthenticationUser user = (AuthenticationUser) cacheBag.get(HibernateKeys.HIBERNATE_Iuseruserhdr);
			
			Integer updateAuditId = new Integer(DBEntityDAOFactory.getCurrentDBEntityDAO().getNextIdForScrModifDoc(user.getId(), useCaseConf.getEntidadId()));
			Date currentDate = BBDDUtils.getDateFromTimestamp(DBEntityDAOFactory.getCurrentDBEntityDAO().getDBServerDate(useCaseConf.getEntidadId()));
						
			ISicresSaveQueries.saveScrModifDoc(session, updateAuditId, user.getName(), currentDate, numReg, bookId, tipo, nombreDoc, valorNuevo, accion);
			
			if(session.isOpen()){
				HibernateUtil.commitTransaction(tran);
			}
			
		} catch (HibernateException e){
			LOGGER.error("ERROR al anotar en el historico de documentos. " + e.getMessage(), e);
			HibernateUtil.rollbackTransaction(tran);
		} catch (SessionException e) {
			LOGGER.error("ERROR al anotar en el historico de documentos. " + e.getMessage(), e);
			HibernateUtil.rollbackTransaction(tran);
		} catch (TecDocException e) {
			LOGGER.error("ERROR al anotar en el historico de documentos. " + e.getMessage(), e);
			HibernateUtil.rollbackTransaction(tran);
		} catch (SQLException e) {
			LOGGER.error("ERROR al anotar en el historico de documentos. " + e.getMessage(), e);
			HibernateUtil.rollbackTransaction(tran);
		} catch (Exception e) {
			LOGGER.error("ERROR al anotar en el historico de documentos. " + e.getMessage(), e);
			HibernateUtil.rollbackTransaction(tran);
		} 
	}
}
