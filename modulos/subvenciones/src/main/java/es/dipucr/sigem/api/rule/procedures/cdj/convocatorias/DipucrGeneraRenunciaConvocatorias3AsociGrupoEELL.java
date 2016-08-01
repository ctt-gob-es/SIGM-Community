package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.util.Calendar;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.ParagraphVertAlign;
import com.sun.star.text.TableColumnSeparator;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraRenunciaConvocatorias3AsociGrupoEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrGeneraRenunciaConvocatorias3AsociGrupoEELL.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = "%TABLA1%,%TABLA2%,%TABLA3%";

        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        String numexp = "";
        try {
            cct.setSsVariable("ANIO", "" + Calendar.getInstance().get(Calendar.YEAR));
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("ANIO");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {

        ArrayList<String[]> datosRenuncia1 = new ArrayList<String[]>();
        ArrayList<String[]> datosRenuncia2 = new ArrayList<String[]>();
        ArrayList<String[]> datosRenuncia3 = new ArrayList<String[]>();

        try {
            IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES,
                    "WHERE NUMEXP IN ( SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = '" + numexp + "') AND ESTADOADM = 'JF' ORDER BY IDENTIDADTITULAR");

            for (Object oItem : expedientesCollection.toList()) {
                IItem expediente = (IItem) oItem;

                Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                if (expResolucion.hasNext()) {
                    IItem resolucion = (IItem) expResolucion.next();
                    if (StringUtils.isNotEmpty(resolucion.getString("RENUNCIA1")) && "SI".equals(resolucion.getString("RENUNCIA1"))) {
                        String[] datoRenuncia = { expediente.getString("IDENTIDADTITULAR"), resolucion.getString("NOMBREGRUPOASOCIACION"),
                                resolucion.getString("CIFGRUPOASOCIACION"), "" + new DecimalFormat("#,##0.00").format(resolucion.getDouble("IMPORTE")) };
                        datosRenuncia1.add(datoRenuncia);
                    }

                    if (StringUtils.isNotEmpty(resolucion.getString("RENUNCIA2")) && "SI".equals(resolucion.getString("RENUNCIA2"))) {
                        String[] datoRenuncia = { expediente.getString("IDENTIDADTITULAR"), resolucion.getString("NOMBREGRUPOASOCIACION2"),
                                resolucion.getString("CIFGRUPOASOCIACION2"), "" + new DecimalFormat("#,##0.00").format(resolucion.getDouble("IMPORTE2")) };
                        datosRenuncia2.add(datoRenuncia);
                    }
                    
                    if (StringUtils.isNotEmpty(resolucion.getString("RENUNCIA3")) && "SI".equals(resolucion.getString("RENUNCIA3"))) {
                        String[] datoRenuncia = { expediente.getString("IDENTIDADTITULAR"), resolucion.getString("NOMBREGRUPOASOCIACION3"),
                                resolucion.getString("CIFGRUPOASOCIACION3"), "" + new DecimalFormat("#,##0.00").format(resolucion.getDouble("IMPORTE3")) };
                        datosRenuncia3.add(datoRenuncia);
                    }
                }
            }

            if ("%TABLA1%".equals(refTabla)) {

                int numFilas = datosRenuncia1.size();

                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");

                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                    xTable.initialize(numFilas + 1, 4);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);

                    colocaColumnas(xTable);

                    setHeaderCellText(xTable, "A1", "AYUNTAMIENTO");
                    setHeaderCellText(xTable, "B1", "GRUPO / ASOCIACIÓN");
                    setHeaderCellText(xTable, "C1", "C.I.F.");
                    setHeaderCellText(xTable, "D1", "SUBVENCIÓN");

                    int i = 0;
                    for (String[] datoRenuncia : datosRenuncia1) {
                        i++;
                        setCellText(xTable, "A" +  (i + 1), datoRenuncia[0]);
                        setCellText(xTable, "B" +  (i + 1), datoRenuncia[1]);
                        setCellText(xTable, "C" +  (i + 1), datoRenuncia[2]);
                        setCellText(xTable, "D" +  (i + 1), datoRenuncia[3]);
                    }
                }
            }
            if ("%TABLA2%".equals(refTabla)) {
                int numFilas = datosRenuncia2.size();

                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");

                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                    xTable.initialize(numFilas + 1, 4);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);

                    colocaColumnas(xTable);

                    setHeaderCellText(xTable, "A1", "AYUNTAMIENTO");
                    setHeaderCellText(xTable, "B1", "GRUPO / ASOCIACIÓN");
                    setHeaderCellText(xTable, "C1", "C.I.F.");
                    setHeaderCellText(xTable, "D1", "SUBVENCIÓN");

                    int i = 0;
                    for (String[] datoRenuncia : datosRenuncia2) {
                        i++;
                        setCellText(xTable, "A" +  (i + 1), datoRenuncia[0]);
                        setCellText(xTable, "B" +  (i + 1), datoRenuncia[1]);
                        setCellText(xTable, "C" +  (i + 1), datoRenuncia[2]);
                        setCellText(xTable, "D" +  (i + 1), datoRenuncia[3]);
                    }
                }
            }
            
            if ("%TABLA3%".equals(refTabla)) {
                int numFilas = datosRenuncia3.size();

                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");

                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                    xTable.initialize(numFilas + 1, 4);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);

                    colocaColumnas(xTable);

                    setHeaderCellText(xTable, "A1", "AYUNTAMIENTO");
                    setHeaderCellText(xTable, "B1", "GRUPO / ASOCIACIÓN");
                    setHeaderCellText(xTable, "C1", "C.I.F.");
                    setHeaderCellText(xTable, "D1", "SUBVENCIÓN");

                    int i = 0;
                    for (String[] datoRenuncia : datosRenuncia3) {
                        i++;
                        setCellText(xTable, "A" +  (i + 1), datoRenuncia[0]);
                        setCellText(xTable, "B" +  (i + 1), datoRenuncia[1]);
                        setCellText(xTable, "C" +  (i + 1), datoRenuncia[2]);
                        setCellText(xTable, "D" +  (i + 1), datoRenuncia[3]);
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    private void setHeaderCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(cellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName));

        // Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));
        xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short) 60));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short) 60));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
        xCPS.setPropertyValue("BackColor", Integer.valueOf(0xC0C0C0));

        // Texto de la celda
        xCellText.setString(strText);
    }

    private void setCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(cellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

        // Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short) 0));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short) 0));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

        // Texto de la celda
        xCellText.setString(strText);
    }

    private void colocaColumnas(XTextTable xTextTable) {

        XPropertySet xPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);

        // Get table Width and TableColumnRelativeSum properties values
        int iWidth;
        try {
            iWidth = (Integer) xPS.getPropertyValue("Width");

            short sTableColumnRelativeSum = (Short) xPS.getPropertyValue("TableColumnRelativeSum");

            // Get table column separators
            Object xObj = xPS.getPropertyValue("TableColumnSeparators");

            TableColumnSeparator[] xSeparators = (TableColumnSeparator[]) UnoRuntime.queryInterface(TableColumnSeparator[].class, xObj);

            // Calculamos el tamaño que le queremos dar a la celda
            // Se empieza colocando de la última a la primera
            double dRatio = (double) sTableColumnRelativeSum / (double) iWidth;
            double dRelativeWidth = (double) 15000 * dRatio;

            // Last table column separator position
            double dPosition = sTableColumnRelativeSum - dRelativeWidth;

            // Set set new position for all column separators
            // Número de separadores
            int i = xSeparators.length - 1;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            i--;
            dRelativeWidth = (double) 15000 * dRatio;
            dPosition -= dRelativeWidth;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            i--;
            dRelativeWidth = (double) 40000 * dRatio;
            dPosition -= dRelativeWidth;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            // Do not forget to set TableColumnSeparators back! Otherwise, it
            // doesn't work.
            xPS.setPropertyValue("TableColumnSeparators", xSeparators);
        } catch (UnknownPropertyException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (WrappedTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (PropertyVetoException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
