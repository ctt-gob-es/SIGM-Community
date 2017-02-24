/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFFont;

import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;

import es.msssi.sgm.registropresencial.actions.SearchInputRegisterFormAction;
import es.msssi.sgm.registropresencial.actions.SearchOutputRegisterFormAction;
import es.msssi.sgm.registropresencial.actions.ValidationListAction;
import es.msssi.sgm.registropresencial.beans.ItemBean;
import es.msssi.sgm.registropresencial.beans.SearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.businessobject.SearchInputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.SearchOutputRegisterBo;

/**
 * Clase con utilidades para la exportación de listados.
 * 
 * @author cmorenog
 */
public class UtilExporter {
    // PDF
    /**
     * Constante correspondiente al título de la búsqueda de registros de
     * entrada.
     */
    public static final String TITULO_BUSQ_RENTRADA = "Listado de Registros de Entrada";
    /**
     * Constante correspondiente al título de la búsqueda de registros de
     * entrada.
     */
    public static final String TITULO_BUSQ_RSALIDA = "Listado de Registros de Salida";
    /** Constante correspondiente a la imagen del logo del documento. */
    public static final String LOGO = "/images/logoDoc.png";
    /** Constante correspondiente a la fuente de la paginación. */
    public static final Font PDFPAGINACION = FontFactory.getFont(
	"arial", 9, Font.NORMAL);
    /** Constante correspondiente a la fuente del título. */
    public static final Font FNTTITULO = FontFactory.getFont(
	"arial", "iso-8859-1", 13, java.awt.Font.BOLD);
    /** Constante correspondiente a la fuente de la cabecera. */
    public static final Font PDFFUENTECABECERATABLE = FontFactory.getFont(
	"Arial", "iso-8859-1", 11, Font.NORMAL, new Color(
	    255, 255, 255));
    /** Constante correspondiente al fondo de la cabecera. */
    public static final Color PDFCOLORFONDOCABECERATABLE = new Color(
	255, 165, 79);
    /** Constante correspondiente a la fuente de la celda. */
    public static final Font PDFFUENTECELDA = FontFactory.getFont(
	"Arial", "iso-8859-1", 8, java.awt.Font.LAYOUT_LEFT_TO_RIGHT);
    /** Constante correspondiente a la fuente de los criterios. */
    public static final Font PDFFUENTECRITERIOS = FontFactory.getFont(
	"Arial", "iso-8859-1", 9);
    /** Constante correspondiente a la fuente de la fecha. */
    public static final Font PDFFECHA = FontFactory.getFont(
	"arial", 9, Font.ITALIC, new Color(
	    0, 0, 0));
    /** Constante correspondiente al porcentaje del ancho. */
    public static final int PDFWIDTHPERCENTAGE = 100;
    /** Constante correspondiente al color rojo de la gama RGB de las celdas. */
    public static final int PDFCELLRGBCOLOR_R = 255;
    /** Constante correspondiente al color verde de la gama RGB de las celdas. */
    public static final int PDFCELLRGBCOLOR_G = 255;
    /** Constante correspondiente al color azul de la gama RGB de las celdas. */
    public static final int PDFCELLRGBCOLOR_B = 255;
    /** Constante correspondiente al alto fijo de las celdas de la cabecera. */
    public static final float PDFHEADERCELLFIXEDHEIGHT = 20f;

    /** Constante correspondiente al margen izquierdo del nuevo documento. */
    public static final int PDFROTATEDOCUMENTMARGINLEFT = 36;
    /** Constante correspondiente al margen derecho del nuevo documento. */
    public static final int PDFROTATEDOCUMENTMARGINRIGHT = 36;
    /** Constante correspondiente al margen superior del nuevo documento. */
    public static final int PDFROTATEDOCUMENTMARGINTOP = 94;
    /** Constante correspondiente al margen inferior del nuevo documento. */
    public static final int PDFROTATEDOCUMENTMARGINBOTTOM = 36;

    /** Constante correspondiente al ancho de la plantilla. */
    public static final int PDF_TEMPLATE_WIDTH = 35;
    /** Constante correspondiente al alto de la plantilla. */
    public static final int PDF_TEMPLATE_HEIGHT = 25;
    /** Constante correspondiente al número de columnas de tabla. */
    public static final int PDF_TABLE_COLUMNS = 3;
    /**
     * Constante correspondiente al ancho relativo de la primera columna de la
     * tabla.
     */
    public static final int PDF_TABLE_RELATIVE_WIDTH_1 = 20;
    /**
     * Constante correspondiente al ancho relativo de la segunda columna de la
     * tabla.
     */
    public static final int PDF_TABLE_RELATIVE_WIDTH_2 = 66;
    /**
     * Constante correspondiente al ancho relativo de la tercera columna de la
     * tabla.
     */
    public static final int PDF_TABLE_RELATIVE_WIDTH_3 = 14;
    /** Constante correspondiente al alto fijo de las celdas de la tabla. */
    public static final int PDF_TABLE_FIXED_HEIGHT = 35;
    /** Constante correspondiente al porcentaje de ancho de la plantilla. */
    public static final int PDF_TABLE_WIDTH_PERCENTAGE = 100;
    /** Constante correspondiente al relleno inferior de las celdas de la tabla. */
    public static final int PDF_TABLE_CELL_PADDING_BOTTOM = 4;
    /**
     * Constante correspondiente al espacio a guardar en el alto de la cabecera
     * para que no se solape con el resto de. documento.
     */
    public static final int PDF_TABLE_NEGATIVE_ROW_HEIGHT = 30;
    /** Constante correspondiente a un tamaño de fuente de 9pt. */
    public static final int FONTSIZE_9 = 9;
    /**
     * Constante correspondiente ancho relativo de la primera columna de la
     * tabla del título.
     */
    public static final int PDF_TITLE_WIDTH_1 = 70;
    /**
     * Constante correspondiente ancho relativo de la segunda columna de la
     * tabla del título.
     */
    public static final int PDF_TITLE_WIDTH_2 = 30;

    // EXCEL
    /** Constante correspondiente a la altura de la cabecera. */
    public static final short EXCELHEIGHTHEADERTABLE = (short) 9;
    /** Constante correspondiente a la anchura de la cabecera. */
    public static final short EXCELWEIGHTHEADERTABLE = HSSFFont.BOLDWEIGHT_BOLD;
    /** Constante correspondiente a la fuente de la cabecera. */
    public static final String EXCELFONTHEADERTABLE = "arial";
    /** Constante correspondiente al color de fondo de la cabecera. */
    public static final Color EXCELCOLORFONDOCABECERATABLE = new Color(
	250, 191, 143);
    /** Constante correspondiente a la altura del título. */
    public static final short EXCELHEIGHTTITLE = (short) 11;
    /** Constante correspondiente a la anchura del título. */
    public static final short EXCELWEIGHTTITLE = HSSFFont.BOLDWEIGHT_BOLD;
    /** Constante correspondiente a la fuente del título. */
    public static final String EXCELFONTTITLE = "arial";
    /** Constante correspondiente a la altura de la fecha. */
    public static final short EXCELHEIGHTDATE = (short) 9;
    /** Constante correspondiente a la anchura de la fecha. */
    public static final short EXCELWEIGHTDATE = HSSFFont.BOLDWEIGHT_NORMAL;
    /** Constante correspondiente a la altura de las celdas. */
    public static final short EXCELHEIGHTCELL = (short) 9;
    /** Constante correspondiente a la altura de las celdas. */
    public static final short EXCELWEIGHTCELL = HSSFFont.BOLDWEIGHT_NORMAL;
    /** Constante alto de un fila de la tabla. */
    public static final short EXCELHEIGHTROW = (short) 50;
    /** Constante alto de un fila de la tabla. */
    public static final int EXCELWEIGHCELL = 20;
    /** Constante tamaño imagen. */
    public static final double EXCELIMAGE = 0.75;

    /**
     * Constructor.
     */
    public UtilExporter() {

    }

    /**
     * Método que recoge el bean de la búsqueda de registros y devuelve una
     * lista con los criterios que se han rellenado.
     * 
     * @return Lista de criterios utilizados para la exportación. Si no hay
     *         criterios se devuelve una lista vacía.
     */
    public static List<ItemBean> putInputSearchCriteria() {
	List<ItemBean> result = new ArrayList<ItemBean>();
	ItemBean item;

	UseCaseConf useCaseConf =
	    (UseCaseConf) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.get(
		    Keys.J_USECASECONF);
	SearchInputRegisterFormAction action =
	    (SearchInputRegisterFormAction) FacesContext.getCurrentInstance().getViewRoot()
		.getViewMap().get(
		    "searchInputRegisterAction");
	SearchInputRegisterBean bean =
	    (SearchInputRegisterBean) ((SearchInputRegisterBo) action.getInputRegisterBo())
		.getSearchInputRegister();

	/* número de registro */
	if (bean.getFld1Value() != null &&
	    !"".equals(bean.getFld1Value())) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD1), bean.getFld1Value());
	    result.add(item);
	}

	/* fecha de registro */
	if (bean.getFld2ValueDesde() != null ||
	    bean.getFld2ValueHasta() != null) {
	    String texto = ResourceRP.getInstance(
		useCaseConf.getLocale()).getProperty(
		KeysRP.I18N_LABELFLD2);
	    String valor = "";
	    SimpleDateFormat format = new SimpleDateFormat(
		"dd/MM/yyyy");
	    /* fecha de registro (desde) */
	    if (bean.getFld2ValueDesde() != null) {
		valor += ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD2DESDE);

		valor += " " +
		    format.format(bean.getFld2ValueDesde()) + " ";
	    }

	    if (bean.getFld2ValueHasta() != null) {
		valor += ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD2HASTA);
		valor += " " +
		    format.format(bean.getFld2ValueHasta());
	    }
	    item = new ItemBean(
		texto, valor);
	    result.add(item);
	}

	/* usuario */
	if (bean.getFld3Value() != null &&
	    !bean.getFld3Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD3), bean.getFld3Value());
	    result.add(item);
	}
	ValidationListAction validationListBo = null;
	/* Oficina de Registro */
	if (bean.getFld5Value() != null &&
	    !bean.getFld5Value().isEmpty()) {
	    if (FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(
		"validationListAction") == null) {
		validationListBo = new ValidationListAction();
		validationListBo.create();
	    }
	    else {
		validationListBo =
		    (ValidationListAction) FacesContext.getCurrentInstance().getExternalContext()
			.getApplicationMap().get(
			    "validationListAction");
	    }
	    List<ScrOfic> listOficinas;
	    listOficinas = validationListBo.getListOficinas();
	    String txt = "";

	    for (ScrOfic ofic : listOficinas) {
		if (ofic.getId().toString().equals(
		    bean.getFld5Value())) {
		    txt = ofic.getCode() +
			" - " + ofic.getName();
		}
	    }
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD5), txt);
	    result.add(item);
	}

	/* Estado */
	if (bean.getFld6Value() != null &&
	    !bean.getFld6Value().isEmpty()) {
	    String valor = "";
	    if ("0".equals(bean.getFld6Value())) {
		valor = "Completo";
	    }
	    else if ("1".equals(bean.getFld6Value())) {
		valor = "Incompleto";
	    }
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD6), valor);
	    result.add(item);
	}

	/* Origen */
	if (bean.getFld7Value() != null) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD7), bean.getFld7Value().getCode() +
		    " " + bean.getFld7Value().getName());
	    result.add(item);
	}

	/* Destino */
	if (bean.getFld8Value() != null) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD8), bean.getFld8Value().getCode() +
		    " " + bean.getFld8Value().getName());
	    result.add(item);
	}

	/* Remitentes */
	if (bean.getFld9Value() != null &&
	    !bean.getFld9Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD9), bean.getFld9Value());
	    result.add(item);
	}

	/* Tipos de Asunto */
	if (bean.getFld16Value() != null) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD16), bean.getFld16Value().getCode() +
		    " " + bean.getFld16Value().getMatter());
	    result.add(item);
	}

	/* Resumen */
	if (bean.getFld17Value() != null &&
	    !bean.getFld17Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD17), bean.getFld17Value());
	    result.add(item);
	}

	/* Transporte */
	if (bean.getFld14Value() != null &&
	    !bean.getFld14Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD14), bean.getFld14Value());
	    result.add(item);
	}

	/* Número */
	if (bean.getFld15Value() != null &&
	    !bean.getFld15Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD15), bean.getFld15Value());
	    result.add(item);
	}
	
	/* es impreso */
	if (bean.getFld1001Value() != null &&
	    !bean.getFld1001Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    "fld1001label"), bean.getFld1001Value());
	    result.add(item);
	}
	return result;
    }

    /**
     * Método que recoge el bean de la búsqueda de registros y devuelve una
     * lista con los criterios que se han rellenado.
     * 
     * @return Lista de criterios utilizados para la exportación. Si no hay
     *         criterios se devuelve una lista vacía.
     */
    public static List<ItemBean> putOutputSearchCriteria() {
	List<ItemBean> result = new ArrayList<ItemBean>();
	ItemBean item;
	UseCaseConf useCaseConf =
	    (UseCaseConf) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.get(
		    Keys.J_USECASECONF);
	SearchOutputRegisterFormAction action =
	    (SearchOutputRegisterFormAction) FacesContext.getCurrentInstance().getViewRoot()
		.getViewMap().get(
		    "searchOutputRegisterAction");
	SearchOutputRegisterBean bean =
	    (SearchOutputRegisterBean) ((SearchOutputRegisterBo) action.getOutputRegisterBo())
		.getSearchOutputRegister();

	/* número de registro */
	if (bean.getFld1Value() != null &&
	    !"".equals(bean.getFld1Value())) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD1), bean.getFld1Value());
	    result.add(item);
	}

	/* fecha de registro */
	if (bean.getFld2ValueDesde() != null ||
	    bean.getFld2ValueHasta() != null) {
	    String texto = ResourceRP.getInstance(
		useCaseConf.getLocale()).getProperty(
		KeysRP.I18N_LABELFLD2);
	    String valor = "";
	    SimpleDateFormat format = new SimpleDateFormat(
		"dd/MM/yyyy");
	    /* fecha de registro (desde) */
	    if (bean.getFld2ValueDesde() != null) {
		valor += ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD2DESDE);

		valor += " " +
		    format.format(bean.getFld2ValueDesde()) + " ";
	    }

	    if (bean.getFld2ValueHasta() != null) {
		valor += ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD2HASTA);
		valor += " " +
		    format.format(bean.getFld2ValueHasta());
	    }
	    item = new ItemBean(
		texto, valor);
	    result.add(item);
	}

	/* usuario */
	if (bean.getFld3Value() != null &&
	    !bean.getFld3Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD3), bean.getFld3Value());
	    result.add(item);
	}
	ValidationListAction validationListBo = null;
	/* Oficina de Registro */
	if (bean.getFld5Value() != null &&
	    !bean.getFld5Value().isEmpty()) {
	    if (FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(
		"validationListAction") == null) {
		validationListBo = new ValidationListAction();
		validationListBo.create();
	    }
	    else {
		validationListBo =
		    (ValidationListAction) FacesContext.getCurrentInstance().getExternalContext()
			.getApplicationMap().get(
			    "validationListAction");
	    }
	    List<ScrOfic> listOficinas;
	    listOficinas = validationListBo.getListOficinas();
	    String txt = "";

	    for (ScrOfic ofic : listOficinas) {
		if (ofic.getId().toString().equals(
		    bean.getFld5Value())) {
		    txt = ofic.getCode() +
			" - " + ofic.getName();
		}
	    }
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD5), txt);
	    result.add(item);
	}

	/* Estado */
	if (bean.getFld6Value() != null &&
	    !bean.getFld6Value().isEmpty()) {
	    String valor = "";
	    if ("0".equals(bean.getFld6Value())) {
		valor = "Completo";
	    }
	    else if ("1".equals(bean.getFld6Value())) {
		valor = "Incompleto";
	    }
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD6), valor);
	    result.add(item);
	}

	/* Origen */
	if (bean.getFld7Value() != null) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD7), bean.getFld7Value().getCode() +
		    " " + bean.getFld7Value().getName());
	    result.add(item);
	}

	/* Destino */
	if (bean.getFld8Value() != null) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD8), bean.getFld8Value().getCode() +
		    " " + bean.getFld8Value().getName());
	    result.add(item);
	}

	/* Destinatarios */
	if (bean.getFld9Value() != null &&
	    !bean.getFld9Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD9D), bean.getFld9Value());
	    result.add(item);
	}

	/* Tipos de Asunto */
	if (bean.getFld12Value() != null) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD16), bean.getFld12Value().getCode() +
		    " " + bean.getFld12Value().getMatter());
	    result.add(item);
	}

	/* Resumen */
	if (bean.getFld13Value() != null &&
	    !bean.getFld13Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD17), bean.getFld13Value());
	    result.add(item);
	}

	/* Transporte */
	if (bean.getFld10Value() != null &&
	    !bean.getFld10Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD14), bean.getFld10Value());
	    result.add(item);
	}

	/* Número */
	if (bean.getFld11Value() != null &&
	    !bean.getFld11Value().isEmpty()) {
	    item = new ItemBean(
		ResourceRP.getInstance(
		    useCaseConf.getLocale()).getProperty(
		    KeysRP.I18N_LABELFLD15), bean.getFld11Value());
	    result.add(item);
	}
	return result;
    }
}