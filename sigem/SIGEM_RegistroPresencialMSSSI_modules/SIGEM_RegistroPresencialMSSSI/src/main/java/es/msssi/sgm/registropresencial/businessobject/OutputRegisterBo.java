/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.AxSfIn;
import com.ieci.tecdoc.common.isicres.AxSfOut;
import com.ieci.tecdoc.common.isicres.AxXf;
import com.ieci.tecdoc.isicres.audit.helper.ISicresAuditHelper;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPOutputRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPOutputRegisterException;

/**
 * Clase que implementa IGenericBo que contiene los métodos relacionados con los
 * registros de salida.
 * 
 * @author cmorenog
 */
public class OutputRegisterBo implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OutputRegisterBo.class.getName());
    private static final int FIELD14 = 14;
    private static final int FIELD21 = 21;
    private static final int FIELD22 = 22;
    private static final int STATIC3 = 3;
    private static final int FIELD501 = 501;
    private static final int FIELD502 = 502;
    private static final String FLD2 = "fld2";
    private static final String FLD4 = "fld4";
    private static final String FLD6 = "fld6";
    private static final String FLD7 = "fld7";
    private static final String FLD8 = "fld8";
    private static final String FLD10 = "fld10";
    private static final String FLD11 = "fld11";
    private static final String FLD12 = "fld12";
    private static final String FLD13 = "fld13";
    private static final String FLD9 = "fld9";
    private static final String FLD21 = "fld21";
    private static final String FLD22 = "fld22";
    private static final String FLD503 = "fld503";
    private static final String FLD504 = "fld504";
    private static final String FLD505 = "fld505";
    private static final String FLD506 = "fld506";
    private static final int FIELD507 = 507;
    private static final int FIELD1003 = 1003;
    private static final String FLD1003 = "fld1003";
    private static final int FIELD1004 = 1004;
    private static final String FLD1004 = "fld1004";

    /**
     * Mapea la información proveniente de outputtregister a un axsf.
     * 
     * @param outputRegisterBean
     *            Bean a mapear.
     * @return newAxSF Bean mapeado.
     */
    private static AxSf mappingOutputRegisterToAxSF(OutputRegisterBean outputRegisterBean) {
	LOG.trace("Entrando en OutputRegisterBo.mappingOutputRegisterToAxSF()");
	AxSf newAxSF = new AxSfOut();
	if (outputRegisterBean.getFld2() != null) {
	    newAxSF.addAttributeName(FLD2);
	    newAxSF.addAttributeValue(FLD2, outputRegisterBean.getFld2());
	}
	if (outputRegisterBean.getFld4() != null) {
	    newAxSF.addAttributeName(FLD4);
	    newAxSF.addAttributeValue(FLD4, outputRegisterBean.getFld4());
	}
	if (outputRegisterBean.getFld6() != null) {
	    newAxSF.addAttributeName(FLD6);
	    newAxSF.addAttributeValue(FLD6, outputRegisterBean.getFld6());
	}
	if (outputRegisterBean.getFld7() != null) {
	    newAxSF.addAttributeName(FLD7);
	    newAxSF.addAttributeValue(FLD7, outputRegisterBean.getFld7().getId());
	}
	if (outputRegisterBean.getFld8() != null) {
	    newAxSF.addAttributeName(FLD8);
	    newAxSF.addAttributeValue(FLD8, outputRegisterBean.getFld8().getId());
	}
	if (outputRegisterBean.getFld10() != null && !"".equals(outputRegisterBean.getFld10())) {
	    newAxSF.addAttributeName(FLD10);
	    newAxSF.addAttributeValue(FLD10, outputRegisterBean.getFld10());
	}
	if (outputRegisterBean.getFld11() != null && !"".equals(outputRegisterBean.getFld11())) {
	    newAxSF.addAttributeName(FLD11);
	    newAxSF.addAttributeValue(FLD11, outputRegisterBean.getFld11());
	}
	if (outputRegisterBean.getFld12() != null
		&& !"".equals(String.valueOf(outputRegisterBean.getFld12()))) {
	    newAxSF.addAttributeName(FLD12);
	    newAxSF.addAttributeValue(FLD12, outputRegisterBean.getFld12().getId());
	}
	if (outputRegisterBean.getFld13() != null && !"".equals(outputRegisterBean.getFld13())) {
	    newAxSF.addAttributeName(FLD13);
	    newAxSF.addAttributeValue(FLD13, outputRegisterBean.getFld13());
	}
	if (outputRegisterBean.getFld14() != null && !"".equals(outputRegisterBean.getFld14())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD14);
	    axXf.setText(outputRegisterBean.getFld14());
	    newAxSF.addExtendedField(FIELD14, axXf);
	}
	if (outputRegisterBean.getFld21() != null && !"".equals(outputRegisterBean.getFld21())) {
	    newAxSF.addAttributeName(FLD21);
	    newAxSF.addAttributeValue(FLD21, outputRegisterBean.getFld21());
	}
	if (outputRegisterBean.getFld22() != null && !"".equals(outputRegisterBean.getFld22())) {
	    newAxSF.addAttributeName(FLD22);
	    newAxSF.addAttributeValue(FLD22, outputRegisterBean.getFld22());
	}
	if (outputRegisterBean.getFld503() != null && !"".equals(outputRegisterBean.getFld503())) {
	    newAxSF.addAttributeName(FLD503);
	    newAxSF.addAttributeValue(FLD503, outputRegisterBean.getFld503());
	}
	if (outputRegisterBean.getFld504() != null && !"".equals(outputRegisterBean.getFld504())) {
	    newAxSF.addAttributeName(FLD504);
	    newAxSF.addAttributeValue(FLD504, outputRegisterBean.getFld504());
	}
	if (outputRegisterBean.getFld505() != null && !"".equals(outputRegisterBean.getFld505())) {
	    newAxSF.addAttributeName(FLD505);
	    newAxSF.addAttributeValue(FLD505, outputRegisterBean.getFld505());
	}
	if (outputRegisterBean.getFld506() != null && !"".equals(outputRegisterBean.getFld506())) {
	    newAxSF.addAttributeName(FLD506);
	    newAxSF.addAttributeValue(FLD506, outputRegisterBean.getFld506());
	}
	if (outputRegisterBean.getFld501() != null && !"".equals(outputRegisterBean.getFld501())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD501);
	    axXf.setText(outputRegisterBean.getFld501());
	    newAxSF.addExtendedField(FIELD501, axXf);
	}
	if (outputRegisterBean.getFld502() != null && !"".equals(outputRegisterBean.getFld502())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD502);
	    axXf.setText(outputRegisterBean.getFld502());
	    newAxSF.addExtendedField(FIELD502, axXf);
	}
	if (outputRegisterBean.getFld507() != null && !"".equals(outputRegisterBean.getFld507())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD507);
	    axXf.setText(outputRegisterBean.getFld507());
	    newAxSF.addExtendedField(FIELD507, axXf);
	}
	if (outputRegisterBean.getFld1003() != null ) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD1003);
	    axXf.setText(String.valueOf(outputRegisterBean.getFld1003()));
	    newAxSF.addExtendedField(FIELD1003, axXf);
	}
	if (outputRegisterBean.getFld1004() != null ) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD1004);
	    axXf.setText(outputRegisterBean.getFld1004());
	    newAxSF.addExtendedField(FIELD1004, axXf);
	}
	return newAxSF;
    }

    /**
     * Mapea la información proveniente de la modificación a un axsf. Recoge
     * solo los campos modificados.
     * 
     * @param fieldsModify
     *            campos a modificar.
     * @return newAxSF campos modificados.
     */
    private static AxSf mappingOutputRegisterModifyToAxSF(HashMap<String, Object> fieldsModify) {
	LOG.trace("Entrando en outputRegisterBo.mappingOutputRegisterModifyToAxSF()");
	AxSf newAxSF = new AxSfOut();
	Iterator<Entry<String, Object>> it = fieldsModify.entrySet().iterator();
	while (it.hasNext()) {
	    Entry<String, Object> field = (Entry<String, Object>) it.next();
	    int idKey = Integer.valueOf(field.getKey().substring(STATIC3)).intValue();
	    if (idKey == FIELD14 || idKey == FIELD501 || idKey == FIELD502 || idKey == FIELD507 
		    || idKey == FIELD1003  || idKey == FIELD1004) {
		AxXf axXf = new AxXf();
		axXf.setFldId(idKey);
		axXf.setText((String) field.getValue());
		newAxSF.addExtendedField(idKey, axXf);
	    }
	    else {
		newAxSF.addAttributeName(field.getKey());
		newAxSF.addAttributeValue(field.getKey(), field.getValue());
	    }
	}
	return newAxSF;
    }

    /**
     * Carga un registro de salida.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param book
     *            Libro donde se encuentra el registro.
     * @param fdrid
     *            Id del registro.
     * @return outputRegisterBean registro cargado.
     * 
     * @throws RPOutputRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro de salida.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public OutputRegisterBean loadOutputRegisterBean(UseCaseConf useCaseConf, ScrRegstate book,
	    Integer fdrid) throws RPOutputRegisterException, RPGenericException {
	LOG.trace("Entrando en OutputRegisterBo.loadOutputRegisterBean()");
	AxSfOut axsf = null;
	List<Interesado> interesados;
	OutputRegisterBean outputRegisterBean = new OutputRegisterBean();
	try {
	    axsf =
		    (AxSfOut) FolderSession.getBookFolder(useCaseConf.getSessionID(), book.getId(),
			    fdrid, useCaseConf.getLocale(), useCaseConf.getEntidadId());
	    InterestedBo interestedBo = new InterestedBo();
	    interesados = interestedBo.getAllInterested(book.getId(), fdrid, useCaseConf);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPOutputRegisterException(
		    RPOutputRegisterErrorCode.LOAD_OUTPUT_REGISTER_ERROR,
		    ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	outputRegisterBean.setFdrid(fdrid);
	outputRegisterBean.setFld1(axsf.getAttributeValueAsString("fld1"));
	outputRegisterBean.setFld2((Date) axsf.getAttributeValue(FLD2));
	outputRegisterBean.setFld3(axsf.getAttributeValueAsString("fld3"));
	outputRegisterBean.setFld4((Date) axsf.getAttributeValue(FLD4));
	outputRegisterBean.setFld5(axsf.getFld5());
	outputRegisterBean.setFld5Name(axsf.getFld5Name());
	outputRegisterBean.setFld6(axsf.getAttributeValueAsString(FLD6));
	outputRegisterBean.setFld6Name(RBUtil.getInstance(useCaseConf.getLocale()).getProperty(
		"book.fld6." + axsf.getAttributeValueAsString(FLD6)));
	outputRegisterBean.setFld7(axsf.getFld7());
	outputRegisterBean.setFld7Name(axsf.getFld7Name());
	outputRegisterBean.setFld8(axsf.getFld8());
	outputRegisterBean.setFld8Name(axsf.getFld8Name());
	outputRegisterBean.setFld9(axsf.getAttributeValueAsString("fld9"));
	outputRegisterBean.setFld10(axsf.getAttributeValueAsString("fld10"));
	if (axsf.getAttributeValueAsString("fld10") != null && !"".equals(axsf.getAttributeValueAsString("fld10"))){
	    TransportBo transportBo = new TransportBo ();
	    
	    outputRegisterBean.setFld10Name(transportBo.getScrttNameById(useCaseConf,axsf.getAttributeValueAsString("fld10")));
	}
	outputRegisterBean.setFld11(axsf.getAttributeValueAsString("fld11"));
	outputRegisterBean.setFld12(axsf.getFld12());
	outputRegisterBean.setFld12Name((axsf.getFld12() != null) ? axsf.getFld12().getCode()
		: null);
	outputRegisterBean.setFld13(axsf.getAttributeValueAsString("fld13"));
	outputRegisterBean.setFld14((axsf.getAxxf() != null) ? axsf.getAxxf().getText() : null);
	outputRegisterBean.setFieldsMod(new HashMap<String, Object>());
	outputRegisterBean.setInteresados(interesados);
	outputRegisterBean.setFld21((axsf.getExtendedFields().get(FIELD21) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD21)).getText() : null);
	outputRegisterBean.setFld22((axsf.getExtendedFields().get(FIELD22) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD22)).getText() : null);
	outputRegisterBean.setFld503((axsf.getAttributeValueAsString("fld503") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld503")) : null);
	outputRegisterBean.setFld504((axsf.getAttributeValueAsString("fld504") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld504")) : null);
	outputRegisterBean.setFld505((axsf.getAttributeValueAsString("fld505") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld505")) : null);
	outputRegisterBean.setFld506((axsf.getAttributeValueAsString("fld506") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld506")) : null);
	outputRegisterBean.setFld501((axsf.getExtendedFields().get(FIELD501) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD501)).getText() : null);
	outputRegisterBean.setFld502((axsf.getExtendedFields().get(FIELD502) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD502)).getText() : null);
	outputRegisterBean.setFld507((axsf.getExtendedFields().get(FIELD507) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD507)).getText() : null);
	outputRegisterBean.setFld1003((axsf.getExtendedFields().get(FIELD1003) != null) ? Integer.parseInt(((AxXf) axsf
		.getExtendedFields().get(FIELD1003)).getText()) : null);
	outputRegisterBean.setFld1004((axsf.getExtendedFields().get(FIELD1004) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD1004)).getText() : null);
	// Auditamos el acceso al registro
	ISicresAuditHelper.auditarAccesoRegistro(useCaseConf.getSessionID(), book.getId(),
		outputRegisterBean.getFdrid(), axsf, book.getIdocarchhdr().getName(), 2);
	return outputRegisterBean;
    }

    /**
     * Guarda o actualiza un directorio.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param bookId
     *            Id del libro.
     * @param fldid
     *            Id de registro.
     * @param files
     *            Ficheros.
     * @param outputRegisterBean
     *            Bean con los datos de salida de un registro.
     * @param inter
     *            Listado de interesados.
     * @param documents
     *            Documentos.
     * @return fldid Id de registro guardado o actualizado.
     * 
     * @throws RPOutputRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro de entrada.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public static Integer saveOrUpdateFolder(UseCaseConf useCaseConf, Integer bookId, Integer fldid,
	    List<?> files, OutputRegisterBean outputRegisterBean, List<Interesado> inter,
	    Map<?, ?> documents) throws RPGenericException, RPOutputRegisterException {
	LOG.trace("Entrando en OutputRegisterBo.saveOrUpdateFolder()");
	boolean updateDate = false;
	Integer varFldid = fldid;
	Integer modifySystemDate =
		Integer.valueOf(BookSession.invesicresConf(useCaseConf.getEntidadId())
			.getModifySystemDate());
	Integer launchDistOutRegister =
		Integer.valueOf(BookSession.invesicresConf(useCaseConf.getEntidadId())
			.getDistSRegister());
	if (modifySystemDate.intValue() == 1) {
	    updateDate = true;
	}
	AxSf newAxSF = new AxSfOut();
	newAxSF.setLiteralBookType(RBUtil.getInstance(useCaseConf.getLocale()).getProperty(
		Keys.I18N_BOOKUSECASE_NODE_OUTBOOK_NAME));
	newAxSF.setLocale(useCaseConf.getLocale());
	if (fldid == null) {
	    newAxSF = mappingOutputRegisterToAxSF(outputRegisterBean);
	    if (inter != null && inter.size() > 0) {
		outputRegisterBean.setFld9(inter.get(0).getNombre() + " "
			+ inter.get(0).getSapellido());
	    } else {
		outputRegisterBean.setFld9(null);
	    }
	    try {
		varFldid =
			FolderSession.createNewFolder(useCaseConf.getSessionID(), bookId, newAxSF,
				inter, launchDistOutRegister, useCaseConf.getLocale(),
				useCaseConf.getEntidadId());
		InterestedBo interestedBo = new InterestedBo();
		interestedBo.addInterested(inter, bookId, varFldid, useCaseConf);
	    }
	    catch (ValidationException validationException) {
		LOG.error(ErrorConstants.CREATE_OUTPUT_REGISTER_ERROR_MESSAGE, validationException);
		throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	    }
	    catch (BookException bookException) {
		LOG.error(ErrorConstants.CREATE_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
		throw new RPOutputRegisterException(
			RPOutputRegisterErrorCode.CREATE_OUTPUT_REGISTER_ERROR,
			ErrorConstants.CREATE_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	    }
	    catch (SessionException sessionException) {
		LOG.error(ErrorConstants.CREATE_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
		throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
			ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	    }
	}
	else {
	    newAxSF = mappingOutputRegisterModifyToAxSF(outputRegisterBean.getFieldsMod());
	    if (inter != null && inter.size() > 0) {
		outputRegisterBean.setFld9(inter.get(0).getNombre() + " "
			+ inter.get(0).getSapellido());
	    }else {
		outputRegisterBean.setFld9(null);
	    }
	    try {
		FolderSession.updateFolder(useCaseConf.getSessionID(), bookId, fldid, newAxSF,
			inter, documents, updateDate, launchDistOutRegister,
			useCaseConf.getLocale(), useCaseConf.getEntidadId());
		InterestedBo interestedBo = new InterestedBo();
		interestedBo.addInterested(inter, bookId, fldid, useCaseConf);
	    }
	    catch (ValidationException validationException) {
		LOG.error(ErrorConstants.UPDATE_OUTPUT_REGISTER_ERROR_MESSAGE, validationException);
		throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	    }
	    catch (BookException bookException) {
		LOG.error(ErrorConstants.UPDATE_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
		throw new RPOutputRegisterException(
			RPOutputRegisterErrorCode.UPDATE_OUTPUT_REGISTER_ERROR,
			ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	    }
	    catch (SessionException sessionException) {
		LOG.error(ErrorConstants.UPDATE_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
		throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
			ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	    }
	}
	return varFldid;
    }

    /**
     * Copia un registro de salida.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param book
     *            Libro donde copiar el registro.
     * @param fdrid
     *            Id del registro.
     * @return Bean copiado.
     * 
     * @throws RPOutputRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro de salida.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public OutputRegisterBean copyOutputRegisterBean(UseCaseConf useCaseConf, ScrRegstate book,
	    Integer fdrid) throws RPGenericException, RPOutputRegisterException {
	LOG.trace("Entrando en OutputRegisterBo.copyOutputRegisterBean()");
	AxSfOut axsf = null;
	OutputRegisterBean outputRegisterBean = new OutputRegisterBean();
	try {
	    axsf =
		    (AxSfOut) FolderSession.getBookFolder(useCaseConf.getSessionID(), book.getId(),
			    fdrid, useCaseConf.getLocale(), useCaseConf.getEntidadId());
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPOutputRegisterException(
		    RPOutputRegisterErrorCode.GET_OUTPUT_REGISTER_ERROR,
		    ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}

	outputRegisterBean.setFld7(axsf.getFld7());
	outputRegisterBean.setFld7Name(axsf.getFld7Name());
	outputRegisterBean.setFld8(axsf.getFld8());
	outputRegisterBean.setFld8Name(axsf.getFld8Name());
	outputRegisterBean.setFld9(axsf.getAttributeValueAsString(FLD9));
	outputRegisterBean.setFld10(axsf.getAttributeValueAsString(FLD10));
	outputRegisterBean.setFld11(axsf.getAttributeValueAsString(FLD11));
	outputRegisterBean.setFld12(axsf.getFld12());
	outputRegisterBean.setFld12Name((axsf.getFld12() != null) ? axsf.getFld12().getCode()
		: null);
	outputRegisterBean.setFld14((axsf.getAxxf() != null) ? axsf.getAxxf().getText() : null);
	outputRegisterBean.setFld13(axsf.getAttributeValueAsString(FLD13));
	outputRegisterBean.setFld21((axsf.getExtendedFields().get(FIELD21) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD21)).getText() : null);
	outputRegisterBean.setFld22((axsf.getExtendedFields().get(FIELD22) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD22)).getText() : null);
	outputRegisterBean.setFld504((axsf.getAttributeValueAsString("fld504") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld504")) : null);
	outputRegisterBean.setFld505((axsf.getAttributeValueAsString("fld505") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld505")) : null);
	outputRegisterBean.setFld506((axsf.getAttributeValueAsString("fld506") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld506")) : null);
	outputRegisterBean.setFld501((axsf.getExtendedFields().get(FIELD501) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD501)).getText() : null);
	outputRegisterBean.setFld502((axsf.getExtendedFields().get(FIELD502) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD502)).getText() : null);

	return outputRegisterBean;
    }
    /**
     * Guarda o actualiza campos directorio.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param bookId
     *            Id del libro.
     * @param fldid
     *            Id de registro.
     * @param inputRegisterBean
     *            Bean con los datos de entrada de un registro.
     * @return fldid Id de registro guardado o actualizado.
     * 
     * @throws RPInputRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro de entrada.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public Integer updateOnlyFolder(UseCaseConf useCaseConf, Integer bookId, Integer fldid,
	    OutputRegisterBean outputRegisterBean) throws RPGenericException,
	    RPInputRegisterException {
	LOG.trace("Entrando en InputRegisterBo.saveOrUpdateFolder()");
	boolean updateDate = false;
	Integer varFldid = fldid;
	Integer modifySystemDate =
		Integer.valueOf(BookSession.invesicresConf(useCaseConf.getEntidadId())
			.getModifySystemDate());
	Integer launchDistOutRegister =
		Integer.valueOf(BookSession.invesicresConf(useCaseConf.getEntidadId())
			.getDistSRegister());
	if (modifySystemDate.intValue() == 1) {
	    updateDate = true;
	}
	AxSf newAxSF = new AxSfOut();
	newAxSF.setLiteralBookType(RBUtil.getInstance(useCaseConf.getLocale()).getProperty(
		Keys.I18N_BOOKUSECASE_NODE_INBOOK_NAME));
	newAxSF.setLocale(useCaseConf.getLocale());

	newAxSF = mappingOutputRegisterModifyToAxSF(outputRegisterBean.getFieldsMod());
	try {
	    FolderSession.updateFolder(useCaseConf.getSessionID(), bookId, fldid, newAxSF, outputRegisterBean.getInteresados(),
		    null, updateDate, launchDistOutRegister, useCaseConf.getLocale(),
		    useCaseConf.getEntidadId());

	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.UPDATE_OUTPUT_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.UPDATE_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPInputRegisterException(
		    RPInputRegisterErrorCode.UPDATE_INPUT_REGISTER_ERROR,
		    ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.UPDATE_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	return varFldid;
    }
}