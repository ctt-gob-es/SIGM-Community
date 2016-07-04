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
import com.ieci.tecdoc.common.isicres.AxXf;
import com.ieci.tecdoc.isicres.audit.helper.ISicresAuditHelper;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterException;

/**
 * Clase que implementa IGenericBo que contiene los métodos relacionados con los
 * registros de entrada.
 * 
 * @author cmorenog
 */
public class InputRegisterBo implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(InputRegisterBo.class.getName());
    private static final int FIELD18 = 18;
    private static final int FIELD1001 = 1001;
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
    private static final String FLD9 = "fld9";
    private static final String FLD10 = "fld10";
    private static final String FLD11 = "fld11";
    private static final String FLD12 = "fld12";
    private static final String FLD13 = "fld13";
    private static final String FLD14 = "fld14";
    private static final String FLD15 = "fld15";
    private static final String FLD16 = "fld16";
    private static final String FLD17 = "fld17";
    private static final String FLD19 = "fld19";
    private static final String FLD21 = "fld21";
    private static final String FLD22 = "fld22";
    private static final String FLD503 = "fld503";
    private static final String FLD504 = "fld504";
    private static final String FLD505 = "fld505";
    private static final String FLD506 = "fld506";
    private static final String FLD18 = "fld18";
    private static final int FIELD507 = 507;
    private static final int FIELD1003 = 1003;
    private static final String FLD1003 = "fld1003";
    private static final int FIELD1004 = 1004;
    private static final String FLD1004 = "fld1004";

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
     * @param inputRegisterBean
     *            Bean con los datos de entrada de un registro.
     * @param inter
     *            Listado de interesados.
     * @param documents
     *            Documentos.
     * @return fldid Id de registro guardado o actualizado.
     * 
     * @throws RPInputRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro de entrada.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    synchronized
    public static Integer saveOrUpdateFolder(UseCaseConf useCaseConf, Integer bookId, Integer fldid,
	    List<?> files, InputRegisterBean inputRegisterBean, List<Interesado> inter,
	    Map<?, ?> documents) throws RPGenericException, RPInputRegisterException {
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
	AxSf newAxSF = new AxSfIn();
	newAxSF.setLiteralBookType(RBUtil.getInstance(useCaseConf.getLocale()).getProperty(
		Keys.I18N_BOOKUSECASE_NODE_INBOOK_NAME));
	newAxSF.setLocale(useCaseConf.getLocale());

	if (fldid == null) {
	    newAxSF = mappingInputRegisterToAxSF(inputRegisterBean);
	    if (inter != null && inter.size() > 0) {
		inputRegisterBean.setFld9(inter.get(0).getNombre() + " "
			+ inter.get(0).getSapellido());
	    } else {
		inputRegisterBean.setFld9(null);
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
		LOG.error(ErrorConstants.CREATE_INPUT_REGISTER_ERROR_MESSAGE, validationException);
		throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	    }
	    catch (BookException bookException) {
		LOG.error(ErrorConstants.CREATE_INPUT_REGISTER_ERROR_MESSAGE, bookException);
		throw new RPInputRegisterException(
			RPInputRegisterErrorCode.CREATE_INPUT_REGISTER_ERROR,
			ErrorConstants.CREATE_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	    }
	    catch (SessionException sessionException) {
		LOG.error(ErrorConstants.CREATE_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
		throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
			ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	    }
	}
	else {
	    newAxSF = mappingInputRegisterModifyToAxSF(inputRegisterBean.getFieldsMod());
	    if (inter != null && inter.size() > 0) {
		inputRegisterBean.setFld9(inter.get(0).getNombre() + " "
			+ inter.get(0).getSapellido());
	    } else {
		inputRegisterBean.setFld9(null);
	    }
	    try {
		FolderSession.updateFolder(useCaseConf.getSessionID(), bookId, fldid, newAxSF,
			inter, documents, updateDate, launchDistOutRegister,
			useCaseConf.getLocale(), useCaseConf.getEntidadId());

		InterestedBo interestedBo = new InterestedBo();
		interestedBo.addInterested(inter, bookId, fldid, useCaseConf);
	    }
	    catch (ValidationException validationException) {
		LOG.error(ErrorConstants.UPDATE_INPUT_REGISTER_ERROR_MESSAGE, validationException);
		throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	    }
	    catch (BookException bookException) {
		LOG.error(ErrorConstants.UPDATE_INPUT_REGISTER_ERROR_MESSAGE, bookException);
		throw new RPInputRegisterException(
			RPInputRegisterErrorCode.UPDATE_INPUT_REGISTER_ERROR,
			ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	    }
	    catch (SessionException sessionException) {
		LOG.error(ErrorConstants.UPDATE_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
		throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
			ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	    }
	}
	return varFldid;
    }

    /**
     * Mapea la información proveniente de inputregister a un axsf.
     * 
     * @param inputRegisterBean
     *            Bean a mapear.
     * @return newAxSF Bean mapeado.
     */
    private static AxSf mappingInputRegisterToAxSF(InputRegisterBean inputRegisterBean) {
	LOG.trace("Entrando en InputRegisterBo.mappingInputRegisterToAxSF()");
	AxSf newAxSF = new AxSfIn();
	if (inputRegisterBean.getFld2() != null) {
	    newAxSF.addAttributeName(FLD2);
	    newAxSF.addAttributeValue(FLD2, inputRegisterBean.getFld2());
	}
	if (inputRegisterBean.getFld4() != null) {
	    newAxSF.addAttributeName(FLD4);
	    newAxSF.addAttributeValue(FLD4, inputRegisterBean.getFld4());
	}
	if (inputRegisterBean.getFld6() != null) {
	    newAxSF.addAttributeName(FLD6);
	    newAxSF.addAttributeValue(FLD6, inputRegisterBean.getFld6());
	}
	if (inputRegisterBean.getFld7() != null) {
	    newAxSF.addAttributeName(FLD7);
	    newAxSF.addAttributeValue(FLD7, inputRegisterBean.getFld7().getId());
	}
	if (inputRegisterBean.getFld8() != null) {
	    newAxSF.addAttributeName(FLD8);
	    newAxSF.addAttributeValue(FLD8, inputRegisterBean.getFld8().getId());
	}
	if (inputRegisterBean.getFld10() != null && !"".equals(inputRegisterBean.getFld10())) {
	    newAxSF.addAttributeName(FLD10);
	    newAxSF.addAttributeValue(FLD10, inputRegisterBean.getFld10());
	}
	if (inputRegisterBean.getFld11() != null
		&& !"".equals(String.valueOf(inputRegisterBean.getFld11()))) {
	    newAxSF.addAttributeName(FLD11);
	    newAxSF.addAttributeValue(FLD11, inputRegisterBean.getFld11());
	}
	if (inputRegisterBean.getFld12() != null) {
	    newAxSF.addAttributeName(FLD12);
	    newAxSF.addAttributeValue(FLD12, inputRegisterBean.getFld12());
	}
	if (inputRegisterBean.getFld13() != null) {
	    newAxSF.addAttributeName(FLD13);
	    newAxSF.addAttributeValue(FLD13, inputRegisterBean.getFld13().getId());
	}
	if (inputRegisterBean.getFld14() != null && !"".equals(inputRegisterBean.getFld14())) {
	    newAxSF.addAttributeName(FLD14);
	    newAxSF.addAttributeValue(FLD14, inputRegisterBean.getFld14());
	}
	if (inputRegisterBean.getFld15() != null && !"".equals(inputRegisterBean.getFld15())) {
	    newAxSF.addAttributeName(FLD15);
	    newAxSF.addAttributeValue(FLD15, inputRegisterBean.getFld15());
	}
	if (inputRegisterBean.getFld16() != null
		&& !"".equals(String.valueOf(inputRegisterBean.getFld16()))) {
	    newAxSF.addAttributeName(FLD16);
	    newAxSF.addAttributeValue(FLD16, inputRegisterBean.getFld16().getId());
	}
	if (inputRegisterBean.getFld17() != null && !"".equals(inputRegisterBean.getFld17())) {
	    newAxSF.addAttributeName(FLD17);
	    newAxSF.addAttributeValue(FLD17, inputRegisterBean.getFld17());
	}
	if (inputRegisterBean.getFld19() != null && !"".equals(inputRegisterBean.getFld19())) {
	    newAxSF.addAttributeName(FLD19);
	    newAxSF.addAttributeValue(FLD19, inputRegisterBean.getFld19());
	}
	if (inputRegisterBean.getFld18() != null && !"".equals(inputRegisterBean.getFld18())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD18);
	    axXf.setText(inputRegisterBean.getFld18());
	    newAxSF.addExtendedField(FIELD18, axXf);
	}
	if (inputRegisterBean.getFld21() != null && !"".equals(inputRegisterBean.getFld21())) {
	    newAxSF.addAttributeName(FLD21);
	    newAxSF.addAttributeValue(FLD21, inputRegisterBean.getFld21());
	}
	if (inputRegisterBean.getFld22() != null && !"".equals(inputRegisterBean.getFld22())) {
	    newAxSF.addAttributeName(FLD22);
	    newAxSF.addAttributeValue(FLD22, inputRegisterBean.getFld22());
	}
	if (inputRegisterBean.getFld503() != null && !"".equals(inputRegisterBean.getFld503())) {
	    newAxSF.addAttributeName(FLD503);
	    newAxSF.addAttributeValue(FLD503, inputRegisterBean.getFld503());
	}
	if (inputRegisterBean.getFld504() != null && !"".equals(inputRegisterBean.getFld504())) {
	    newAxSF.addAttributeName(FLD504);
	    newAxSF.addAttributeValue(FLD504, inputRegisterBean.getFld504());
	}
	if (inputRegisterBean.getFld505() != null && !"".equals(inputRegisterBean.getFld505())) {
	    newAxSF.addAttributeName(FLD505);
	    newAxSF.addAttributeValue(FLD505, inputRegisterBean.getFld505());
	}
	if (inputRegisterBean.getFld506() != null && !"".equals(inputRegisterBean.getFld506())) {
	    newAxSF.addAttributeName(FLD506);
	    newAxSF.addAttributeValue(FLD506, inputRegisterBean.getFld506());
	}
	if (inputRegisterBean.getFld501() != null && !"".equals(inputRegisterBean.getFld501())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD501);
	    axXf.setText(inputRegisterBean.getFld501());
	    newAxSF.addExtendedField(FIELD501, axXf);
	}
	if (inputRegisterBean.getFld502() != null && !"".equals(inputRegisterBean.getFld502())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD502);
	    axXf.setText(inputRegisterBean.getFld502());
	    newAxSF.addExtendedField(FIELD502, axXf);
	}
	if (inputRegisterBean.getFld507() != null && !"".equals(inputRegisterBean.getFld507())) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD507);
	    axXf.setText(inputRegisterBean.getFld507());
	    newAxSF.addExtendedField(FIELD507, axXf);
	}
	if (inputRegisterBean.getFld1003() != null ) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD1003);
	    axXf.setText(String.valueOf(inputRegisterBean.getFld1003()));
	    newAxSF.addExtendedField(FIELD1003, axXf);
	}
	if (inputRegisterBean.getFld1004() != null ) {
	    AxXf axXf = new AxXf();
	    axXf.setFldId(FIELD1004);
	    axXf.setText(inputRegisterBean.getFld1004());
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
    private static AxSf mappingInputRegisterModifyToAxSF(HashMap<String, Object> fieldsModify) {
	LOG.trace("Entrando en InputRegisterBo.mappingInputRegisterModifyToAxSF()");
	AxSf newAxSF = new AxSfIn();
	Iterator<Entry<String, Object>> it = fieldsModify.entrySet().iterator();
	while (it.hasNext()) {
	    Entry<String, Object> field = (Entry<String, Object>) it.next();
	    int idKey = Integer.valueOf(field.getKey().substring(STATIC3)).intValue();
	    if (idKey == FIELD18 || idKey == FIELD1001 || idKey == FIELD501 || idKey == FIELD502 || idKey == FIELD507 
		    || idKey == FIELD1003 || idKey == FIELD1004) {
		AxXf axXf = new AxXf();
		axXf.setFldId(idKey);
		if (field.getValue() instanceof Integer){
		    axXf.setText(String.valueOf((Integer) field.getValue()));
		} else {
		    axXf.setText((String) field.getValue());
		}
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
     * Copia un registro de entrada.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param book
     *            Libro donde copiar el registro.
     * @param fdrid
     *            Id del registro.
     * @return Bean copiado.
     * 
     * @throws RPInputRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro de entrada.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public InputRegisterBean copyInputRegisterBean(UseCaseConf useCaseConf, ScrRegstate book,
	    Integer fdrid) throws RPGenericException, RPInputRegisterException {
	LOG.trace("Entrando en InputRegisterBo.copyInputRegisterBean()");
	AxSfIn axsf = null;
	InputRegisterBean inputRegisterBean = new InputRegisterBean();
	try {
	    axsf =
		    (AxSfIn) FolderSession.getBookFolder(useCaseConf.getSessionID(), book.getId(),
			    fdrid, useCaseConf.getLocale(), useCaseConf.getEntidadId());
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPInputRegisterException(RPInputRegisterErrorCode.GET_INPUT_REGISTER_ERROR,
		    ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}

	inputRegisterBean.setFld7(axsf.getFld7());
	inputRegisterBean.setFld7Name(axsf.getFld7Name());
	inputRegisterBean.setFld8(axsf.getFld8());
	inputRegisterBean.setFld8Name(axsf.getFld8Name());
	inputRegisterBean.setFld9(axsf.getAttributeValueAsString(FLD9));
	inputRegisterBean.setFld10(axsf.getAttributeValueAsString(FLD10));
	inputRegisterBean.setFld11((axsf.getAttributeValueAsString(FLD11) != null) ? Integer
		.valueOf(axsf.getAttributeValueAsString(FLD11)) : null);
	inputRegisterBean.setFld12((Date) axsf.getAttributeValue(FLD12));
	inputRegisterBean.setFld13(axsf.getFld13());
	inputRegisterBean
		.setFld13Name((axsf.getFld13() != null) ? axsf.getFld13().getName() : null);
	inputRegisterBean.setFld14(axsf.getAttributeValueAsString(FLD14));
	inputRegisterBean.setFld15(axsf.getAttributeValueAsString(FLD15));
	inputRegisterBean.setFld16(axsf.getFld16());
	inputRegisterBean
		.setFld16Name((axsf.getFld16() != null) ? axsf.getFld16().getCode() : null);
	inputRegisterBean.setFld17(axsf.getAttributeValueAsString(FLD17));
	inputRegisterBean.setFld19(axsf.getAttributeValueAsString(FLD19));
	inputRegisterBean.setFld21((axsf.getExtendedFields().get(FIELD21) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD21)).getText() : null);
	inputRegisterBean.setFld22((axsf.getExtendedFields().get(FIELD22) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD22)).getText() : null);
	inputRegisterBean.setFld18((axsf.getAxxf() != null) ? axsf.getAxxf().getText() : null);
	inputRegisterBean.setFld504((axsf.getAttributeValueAsString("fld504") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld504")) : null);
	inputRegisterBean.setFld505((axsf.getAttributeValueAsString("fld505") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld505")) : null);
	inputRegisterBean.setFld506((axsf.getAttributeValueAsString("fld506") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld506")) : null);
	inputRegisterBean.setFld501((axsf.getExtendedFields().get(FIELD501) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD501)).getText() : null);
	inputRegisterBean.setFld502((axsf.getExtendedFields().get(FIELD502) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD502)).getText() : null);

	return inputRegisterBean;
    }

    /**
     * Carga un registro de entrada.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param book
     *            Libro donde se encuentra el registro.
     * @param fdrid
     *            Id del registro.
     * @return inputRegisterBean registro cargado.
     * 
     * @throws RPInputRegisterException
     *             si se ha producido algún error al tratar la información del
     *             registro de entrada.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public InputRegisterBean loadInputRegisterBean(UseCaseConf useCaseConf, ScrRegstate book,
	    Integer fdrid) throws RPInputRegisterException, RPGenericException {
	LOG.trace("Entrando en InputRegisterBo.loadInputRegisterBean()");
	AxSfIn axsf = null;
	List<Interesado> interesados;
	InputRegisterBean inputRegisterBean = new InputRegisterBean();
	try {
	    axsf =
		    (AxSfIn) FolderSession.getBookFolder(useCaseConf.getSessionID(), book.getId(),
			    fdrid, useCaseConf.getLocale(), useCaseConf.getEntidadId());

	    InterestedBo interestedBo = new InterestedBo();
	    interesados = interestedBo.getAllInterested(book.getId(), fdrid, useCaseConf);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPInputRegisterException(RPInputRegisterErrorCode.LOAD_INPUT_REGISTER_ERROR,
		    ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	inputRegisterBean.setFdrid(fdrid);
	inputRegisterBean.setFld1(axsf.getAttributeValueAsString("fld1"));
	inputRegisterBean.setFld2((Date) axsf.getAttributeValue(FLD2));
	inputRegisterBean.setFld3(axsf.getAttributeValueAsString("fld3"));
	inputRegisterBean.setFld4((Date) axsf.getAttributeValue(FLD4));
	inputRegisterBean.setFld5(axsf.getFld5());
	inputRegisterBean.setFld5Name(axsf.getFld5Name());
	inputRegisterBean.setFld6(axsf.getAttributeValueAsString(FLD6));
	inputRegisterBean.setFld6Name(RBUtil.getInstance(useCaseConf.getLocale()).getProperty(
		"book.fld6." + axsf.getAttributeValueAsString(FLD6)));
	inputRegisterBean.setFld7(axsf.getFld7());
	inputRegisterBean.setFld7Name(axsf.getFld7Name());
	inputRegisterBean.setFld8(axsf.getFld8());
	inputRegisterBean.setFld8Name(axsf.getFld8Name());
	inputRegisterBean.setFld9(axsf.getAttributeValueAsString("fld9"));
	inputRegisterBean.setFld10(axsf.getAttributeValueAsString(FLD10));
	inputRegisterBean.setFld11((axsf.getAttributeValueAsString(FLD11) != null) ? Integer
		.valueOf(axsf.getAttributeValueAsString(FLD11)) : null);
	inputRegisterBean.setFld12((Date) axsf.getAttributeValue("fld12"));
	inputRegisterBean.setFld13(axsf.getFld13());
	inputRegisterBean
		.setFld13Name((axsf.getFld13() != null) ? axsf.getFld13().getName() : null);
	inputRegisterBean.setFld14(axsf.getAttributeValueAsString("fld14"));
	if (axsf.getAttributeValueAsString("fld14") != null && !"".equals(axsf.getAttributeValueAsString("fld14"))){
	    TransportBo transportBo = new TransportBo ();
	    
	    inputRegisterBean.setFld14Name(transportBo.getScrttNameById(useCaseConf,axsf.getAttributeValueAsString("fld14")));
	}
	inputRegisterBean.setFld15(axsf.getAttributeValueAsString("fld15"));
	inputRegisterBean.setFld16(axsf.getFld16());
	inputRegisterBean
		.setFld16Name((axsf.getFld16() != null) ? axsf.getFld16().getCode() : null);
	inputRegisterBean.setFld17(axsf.getAttributeValueAsString("fld17"));
	inputRegisterBean.setFld19(axsf.getAttributeValueAsString("fld19"));
	inputRegisterBean.setFld18((axsf.getAxxf() != null) ? axsf.getAxxf().getText() : null);
	inputRegisterBean.setFld21((axsf.getExtendedFields().get(FIELD21) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD21)).getText() : null);
	inputRegisterBean.setFld22((axsf.getExtendedFields().get(FIELD22) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD22)).getText() : null);
	inputRegisterBean.setFld503((axsf.getAttributeValueAsString("fld503") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld503")) : null);
	inputRegisterBean.setFld504((axsf.getAttributeValueAsString("fld504") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld504")) : null);
	inputRegisterBean.setFld505((axsf.getAttributeValueAsString("fld505") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld505")) : null);
	inputRegisterBean.setFld506((axsf.getAttributeValueAsString("fld506") != null) ? Integer
		.parseInt(axsf.getAttributeValueAsString("fld506")) : null);
	inputRegisterBean.setFld501((axsf.getExtendedFields().get(FIELD501) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD501)).getText() : null);
	inputRegisterBean.setFld502((axsf.getExtendedFields().get(FIELD502) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD502)).getText() : null);
	inputRegisterBean.setFld507((axsf.getExtendedFields().get(FIELD507) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD507)).getText() : null);
	inputRegisterBean.setFld1003((axsf.getExtendedFields().get(FIELD1003) != null) ? Integer.parseInt(((AxXf) axsf
		.getExtendedFields().get(FIELD1003)).getText()) : null);
	inputRegisterBean.setFld1004((axsf.getExtendedFields().get(FIELD1004) != null) ? ((AxXf) axsf
		.getExtendedFields().get(FIELD1004)).getText() : null);
	inputRegisterBean.setFieldsMod(new HashMap<String, Object>());
	inputRegisterBean.setInteresados(interesados);
	// Auditamos el acceso al registro
	ISicresAuditHelper.auditarAccesoRegistro(useCaseConf.getSessionID(), book.getId(),
		inputRegisterBean.getFdrid(), axsf, book.getIdocarchhdr().getName(), 1);
	return inputRegisterBean;
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
	    InputRegisterBean inputRegisterBean) throws RPGenericException,
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
	AxSf newAxSF = new AxSfIn();
	newAxSF.setLiteralBookType(RBUtil.getInstance(useCaseConf.getLocale()).getProperty(
		Keys.I18N_BOOKUSECASE_NODE_INBOOK_NAME));
	newAxSF.setLocale(useCaseConf.getLocale());

	newAxSF = mappingInputRegisterModifyToAxSF(inputRegisterBean.getFieldsMod());
	try {
	    FolderSession.updateFolder(useCaseConf.getSessionID(), bookId, fldid, newAxSF, inputRegisterBean.getInteresados(),
		    null, updateDate, launchDistOutRegister, useCaseConf.getLocale(),
		    useCaseConf.getEntidadId());

	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.UPDATE_INPUT_REGISTER_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.UPDATE_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	    throw new RPInputRegisterException(
		    RPInputRegisterErrorCode.UPDATE_INPUT_REGISTER_ERROR,
		    ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.UPDATE_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
		    ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
	}
	return varFldid;
    }

}