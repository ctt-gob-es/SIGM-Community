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

public class DipucrDatosResolAprobConvocatoriasDatos3AsociGrupoEELL extends DipucrAutoGeneraDocIniTramiteRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrDatosResolAprobConvocatoriasDatos3AsociGrupoEELL.class);

    protected String texto1;
    protected String texto2;
    protected String texto3;
    protected String texto4;
    protected String texto5;
    protected String texto6;

    protected int hayRechazados1 = 0;
    protected int hayRechazados2 = 0;
    protected int hayRechazados3 = 0;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }

            refTablas = "%TABLA1%,%TABLA2%,%TABLA3%,%TABLA4%,%TABLA5%,%TABLA6%";

            texto1 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
            texto2 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";
            texto3 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
            texto4 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";
            texto5 = "Una vez comprobada y verificada la documentación remitida por los solicitantes, conforme a lo dispuesto en las bases de la convocatoria, procede el otorgamiento de las siguientes subvenciones:";
            texto6 = "No procede el otorgamiento de la subvención a los solicitantes que seguidamente se indican, por los motivos que se señalan en cada caso:";

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
            numexp = rulectx.getNumExp();

            double importeTotal = 0;
            String textoRechazo = "";
            String textoRechazo2 = "";
            String textoRechazo3 = "";

            // Obtenemos los expedientes relacionados y aprobados, ordenados por
            // ayuntamiento
            IItemCollection expRelacionadosCollection = cct.getAPI().getEntitiesAPI()
                    .queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" + rulectx.getNumExp() + "'");
            Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
            String query = "";
            while (expRelacionadosIterator.hasNext()) {
                String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                query += "'" + numexpHijo + "',";
            }

            if (query.length() > 0) {
                query = query.substring(0, query.length() - 1);
            }
            IItemCollection expedientesCollection = cct.getAPI().getEntitiesAPI()
                    .queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query + ") AND ESTADOADM='RS' ORDER BY IDENTIDADTITULAR");
            Iterator<?> expedientesIterator = expedientesCollection.iterator();

            while (expedientesIterator.hasNext()) {
                IItem expediente = (IItem) expedientesIterator.next();
                Iterator<?> resolucionIterator = cct.getAPI().getEntitiesAPI().getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                if (resolucionIterator.hasNext()) {
                    IItem resolucion = (IItem) resolucionIterator.next();
                    importeTotal += resolucion.getDouble("IMPORTE");
                    importeTotal += resolucion.getDouble("IMPORTE2");
                    importeTotal += resolucion.getDouble("IMPORTE3");
                } else {
                    importeTotal += 0;
                }
            }

            IItemCollection expedientesRechazadosCollection = cct.getAPI().getEntitiesAPI()
                    .queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query + ") AND ESTADOADM IN ('RC', 'RS')");
            Iterator<?> expedientesRechazadosIterator = expedientesRechazadosCollection.iterator();
            while (expedientesRechazadosIterator.hasNext()) {
                IItem expedienteRechazado = (IItem) expedientesRechazadosIterator.next();
                Iterator<?> rechazadoIterator = cct.getAPI().getEntitiesAPI().getEntities("DPCR_RESOL_SOL_CONV_SUB", expedienteRechazado.getString("NUMEXP"))
                        .iterator();
                if (rechazadoIterator.hasNext()) {
                    IItem rechazado = (IItem) rechazadoIterator.next();
                    if (StringUtils.isNotEmpty(rechazado.getString("MOTIVO_RECHAZO") )) {
                        textoRechazo = texto2;
                        hayRechazados1++;
                    }
                    if (StringUtils.isNotEmpty(rechazado.getString("MOTIVO_RECHAZO2") )) {
                        textoRechazo2 = texto4;
                        hayRechazados2++;
                    }
                    if (StringUtils.isNotEmpty(rechazado.getString("MOTIVO_RECHAZO3") )) {
                        textoRechazo3 = texto6;
                        hayRechazados3++;
                    }
                }
            }

            cct.setSsVariable("IMPORTE", "" + new DecimalFormat("#,##0.00").format(importeTotal));
            cct.setSsVariable("TEXTO1", texto1);
            cct.setSsVariable("TEXTO2", textoRechazo);
            cct.setSsVariable("TEXTO3", texto3);
            cct.setSsVariable("TEXTO4", textoRechazo2);
            cct.setSsVariable("TEXTO5", texto5);
            cct.setSsVariable("TEXTO6", textoRechazo3);
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("ANIO");
            cct.deleteSsVariable("IMPORTE");
            cct.deleteSsVariable("TEXTO1");
            cct.deleteSsVariable("TEXTO2");
            cct.deleteSsVariable("TEXTO3");
            cct.deleteSsVariable("TEXTO4");
            cct.deleteSsVariable("TEXTO5");
            cct.deleteSsVariable("TEXTO6");

        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String ayuntamiento = "";
        String nreg = "";
        String cifAsociacion = "";
        String nombreAsociacion = "";
        String motivoDenegacion = "";

        ArrayList<String> expedientesResolucion = new ArrayList<String>();

        try {
            if ("%TABLA1%".equals(refTabla)) {
                // Obtenemos los expedientes relacionados y aprobados, ordenados
                // por ayuntamiento
                IItemCollection expRelacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"
                        + numexp + "'");
                Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
                String query = "";
                while (expRelacionadosIterator.hasNext()) {
                    String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                    expedientesResolucion.add(numexpHijo);
                    query += "'" + numexpHijo + "',";
                }

                if (query.length() > 0) {
                    query = query.substring(0, query.length() - 1);
                }
                IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query
                        + ") AND ESTADOADM='RS' AND NUMEXP IN (SELECT NUMEXP FROM DPCR_RESOL_SOL_CONV_SUB WHERE CIFGRUPOASOCIACION IS NOT NULL OR CIFGRUPOASOCIACION != '') ORDER BY NREG");
                Iterator<?> expedientesIterator = expedientesCollection.iterator();

                int numFilas = expedientesCollection.toList().size();

                // Busca la posición de la tabla y coloca el cursor ahí
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");

                    // Inserta una tabla de 4 columnas y tantas filas
                    // como nuevas liquidaciones haya mas una de cabecera
                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                    // Añadimos 3 filas más para las dos de la cabecera de la
                    // tabla y uno para la celda final
                    xTable.initialize(numFilas + 1, 5);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);

                    colocaColumnas1(xTable);

                    // Rellena la cabecera de la tabla
                    setHeaderCellText(xTable, "A1", "NÚM. REGISTRO");
                    setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                    setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACIÓN");
                    setHeaderCellText(xTable, "D1", "C.I.F");
                    setHeaderCellText(xTable, "E1", "IMPORTE");

                    int i = 0;
                    while (expedientesIterator.hasNext()) {
                        i++;
                        IItem expediente = (IItem) expedientesIterator.next();
                        ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                        nreg = expediente.getString("NREG");
                        nombreAsociacion = "";

                        double importe = 0;

                        Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                        if (expResolucion.hasNext()) {
                            IItem resolucion = (IItem) expResolucion.next();
                            importe = resolucion.getDouble("IMPORTE");
                            cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION");
                            nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION");
                        }

                        setCellText(xTable, "A" +  (i + 1), nreg);
                        setCellText(xTable, "B" +  (i + 1), ayuntamiento);
                        setCellText(xTable, "C" +  (i + 1), nombreAsociacion);
                        setCellText(xTable, "D" +  (i + 1), cifAsociacion);
                        setCellText(xTable, "E" +  (i + 1), "" + new DecimalFormat("#,##0.00").format(importe));
                    }
                }
            } else if ("%TABLA2%".equals(refTabla)) {
                // Obtenemos los expedientes relacionados y aprobados, ordenados
                // por ayuntamiento
                IItemCollection expRelacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"
                        + numexp + "'");
                Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
                String query = "";
                while (expRelacionadosIterator.hasNext()) {
                    String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                    expedientesResolucion.add(numexpHijo);
                    query += "'" + numexpHijo + "',";
                }

                if (query.length() > 0) {
                    query = query.substring(0, query.length() - 1);
                }
                IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query
                        + ") AND ESTADOADM IN ('RC', 'RS') ORDER BY IDENTIDADTITULAR");
                Iterator<?> expedientesIterator = expedientesCollection.iterator();

                int numFilas = hayRechazados1;

                // Busca la posición de la tabla y coloca el cursor ahí
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");

                    if (expedientesIterator.hasNext() || hayRechazados1 > 0) {

                        // Inserta una tabla de 4 columnas y tantas filas
                        // como nuevas liquidaciones haya mas una de cabecera
                        XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                        Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                        XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                        // Añadimos 3 filas más para las dos de la cabecera de
                        // la tabla y uno para la celda final
                        xTable.initialize(numFilas + 1, 5);
                        XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                        xText.insertTextContent(xSearchTextRange, xTextContent, false);

                        colocaColumnas2(xTable);

                        // Rellena la cabecera de la tabla
                        setHeaderCellText(xTable, "A1", "NÚM. REGISTRO");
                        setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                        setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACIÓN");
                        setHeaderCellText(xTable, "D1", "C.I.F");
                        setHeaderCellText(xTable, "E1", "MOTIVO DENEGACIÓN");

                        int i = 0;
                        while (expedientesIterator.hasNext()) {

                            IItem expediente = (IItem) expedientesIterator.next();
                            ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                            nreg = expediente.getString("NREG");

                            if (motivoDenegacion == null){
                                motivoDenegacion = "";
                            }
                            if (ayuntamiento == null){
                                ayuntamiento = "";
                            }

                            motivoDenegacion = "";
                            Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                            if (expResolucion.hasNext()) {
                                IItem resolucion = (IItem) expResolucion.next();
                                motivoDenegacion = resolucion.getString("MOTIVO_RECHAZO");

                                if (StringUtils.isNotEmpty(motivoDenegacion)) {
                                    i++;

                                    cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION");
                                    nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION");

                                    setCellText(xTable, "A" +  (i + 1), nreg);
                                    setCellText(xTable, "B" +  (i + 1), ayuntamiento);
                                    setCellText(xTable, "C" +  (i + 1), nombreAsociacion);
                                    setCellText(xTable, "D" +  (i + 1), cifAsociacion);
                                    setCellText(xTable, "E" +  (i + 1), motivoDenegacion);
                                }
                            }
                        }
                    }
                }
            }
         // Banda o Grupo 2
            if ("%TABLA3%".equals(refTabla)) {
                // Obtenemos los expedientes relacionados y aprobados, ordenados
                // por ayuntamiento
                IItemCollection expRelacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"
                        + numexp + "'");
                Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
                String query = "";
                while (expRelacionadosIterator.hasNext()) {
                    String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                    expedientesResolucion.add(numexpHijo);
                    query += "'" + numexpHijo + "',";
                }

                if (query.length() > 0) {
                    query = query.substring(0, query.length() - 1);
                }
                IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query
                        + ") AND ESTADOADM='RS' AND NUMEXP IN (SELECT NUMEXP FROM DPCR_RESOL_SOL_CONV_SUB WHERE CIFGRUPOASOCIACION2 IS NOT NULL OR CIFGRUPOASOCIACION2 != '') ORDER BY NREG");
                Iterator<?> expedientesIterator = expedientesCollection.iterator();

                int numFilas = expedientesCollection.toList().size();

                // Busca la posición de la tabla y coloca el cursor ahí
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");

                    // Inserta una tabla de 4 columnas y tantas filas
                    // como nuevas liquidaciones haya mas una de cabecera
                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                    // Añadimos 3 filas más para las dos de la cabecera de la
                    // tabla y uno para la celda final
                    xTable.initialize(numFilas + 1, 5);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);

                    colocaColumnas1(xTable);

                    // Rellena la cabecera de la tabla
                    setHeaderCellText(xTable, "A1", "NÚM. REGISTRO");
                    setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                    setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACIÓN");
                    setHeaderCellText(xTable, "D1", "C.I.F");
                    setHeaderCellText(xTable, "E1", "IMPORTE");

                    int i = 0;
                    while (expedientesIterator.hasNext()) {
                        i++;
                        IItem expediente = (IItem) expedientesIterator.next();
                        ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                        nreg = expediente.getString("NREG");
                        nombreAsociacion = "";

                        double importe = 0;

                        Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                        if (expResolucion.hasNext()) {
                            IItem resolucion = (IItem) expResolucion.next();
                            importe = resolucion.getDouble("IMPORTE2");
                            cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION2");
                            nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION2");
                        }

                        setCellText(xTable, "A" +  (i + 1), nreg);
                        setCellText(xTable, "B" +  (i + 1), ayuntamiento);
                        setCellText(xTable, "C" +  (i + 1), nombreAsociacion);
                        setCellText(xTable, "D" +  (i + 1), cifAsociacion);
                        setCellText(xTable, "E" +  (i + 1), "" + new DecimalFormat("#,##0.00").format(importe));
                    }
                }
            } else if ("%TABLA4%".equals(refTabla)) {
                // Obtenemos los expedientes relacionados y aprobados, ordenados
                // por ayuntamiento
                IItemCollection expRelacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"
                        + numexp + "'");
                Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
                String query = "";
                while (expRelacionadosIterator.hasNext()) {
                    String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                    expedientesResolucion.add(numexpHijo);
                    query += "'" + numexpHijo + "',";
                }

                if (query.length() > 0) {
                    query = query.substring(0, query.length() - 1);
                }
                IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query
                        + ") AND ESTADOADM IN ('RC', 'RS') ORDER BY IDENTIDADTITULAR");
                Iterator<?> expedientesIterator = expedientesCollection.iterator();

                int numFilas = hayRechazados2;

                // Busca la posición de la tabla y coloca el cursor ahí
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");
                    if (expedientesIterator.hasNext() || hayRechazados2 > 0) {

                        // Inserta una tabla de 4 columnas y tantas filas
                        // como nuevas liquidaciones haya mas una de cabecera
                        XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                        Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                        XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                        // Añadimos 3 filas más para las dos de la cabecera de
                        // la tabla y uno para la celda final
                        xTable.initialize(numFilas + 1, 5);
                        XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                        xText.insertTextContent(xSearchTextRange, xTextContent, false);

                        colocaColumnas2(xTable);

                        // Rellena la cabecera de la tabla
                        setHeaderCellText(xTable, "A1", "NÚM. REGISTRO");
                        setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                        setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACIÓN");
                        setHeaderCellText(xTable, "D1", "C.I.F");
                        setHeaderCellText(xTable, "E1", "MOTIVO DENEGACIÓN");

                        int i = 0;
                        while (expedientesIterator.hasNext()) {
                            IItem expediente = (IItem) expedientesIterator.next();
                            ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                            nreg = expediente.getString("NREG");

                            if (motivoDenegacion == null){
                                motivoDenegacion = "";
                            }
                            if (ayuntamiento == null){
                                ayuntamiento = "";
                            }

                            motivoDenegacion = "";
                            Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                            if (expResolucion.hasNext()) {
                                IItem resolucion = (IItem) expResolucion.next();
                                motivoDenegacion = resolucion.getString("MOTIVO_RECHAZO2");
                                if (StringUtils.isNotEmpty(motivoDenegacion)) {
                                    i++;

                                    cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION2");
                                    nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION2");

                                    setCellText(xTable, "A" +  (i + 1), nreg);
                                    setCellText(xTable, "B" +  (i + 1), ayuntamiento);
                                    setCellText(xTable, "C" +  (i + 1), nombreAsociacion);
                                    setCellText(xTable, "D" +  (i + 1), cifAsociacion);
                                    setCellText(xTable, "E" +  (i + 1), motivoDenegacion);
                                }
                            }
                        }
                    }
                }
            }// Banda o Grupo 3
            if ("%TABLA5%".equals(refTabla)) {
                // Obtenemos los expedientes relacionados y aprobados, ordenados
                // por ayuntamiento
                IItemCollection expRelacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"
                        + numexp + "'");
                Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
                String query = "";
                while (expRelacionadosIterator.hasNext()) {
                    String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                    expedientesResolucion.add(numexpHijo);
                    query += "'" + numexpHijo + "',";
                }

                if (query.length() > 0) {
                    query = query.substring(0, query.length() - 1);
                }
                IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query
                        + ") AND ESTADOADM='RS' AND NUMEXP IN (SELECT NUMEXP FROM DPCR_RESOL_SOL_CONV_SUB WHERE CIFGRUPOASOCIACION3 IS NOT NULL OR CIFGRUPOASOCIACION3 != '') ORDER BY NREG");
                Iterator<?> expedientesIterator = expedientesCollection.iterator();

                int numFilas = expedientesCollection.toList().size();

                // Busca la posición de la tabla y coloca el cursor ahí
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");

                    // Inserta una tabla de 4 columnas y tantas filas
                    // como nuevas liquidaciones haya mas una de cabecera
                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                    // Añadimos 3 filas más para las dos de la cabecera de la
                    // tabla y uno para la celda final
                    xTable.initialize(numFilas + 1, 5);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);

                    colocaColumnas1(xTable);

                    // Rellena la cabecera de la tabla
                    setHeaderCellText(xTable, "A1", "NÚM. REGISTRO");
                    setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                    setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACIÓN");
                    setHeaderCellText(xTable, "D1", "C.I.F");
                    setHeaderCellText(xTable, "E1", "IMPORTE");

                    int i = 0;
                    while (expedientesIterator.hasNext()) {
                        i++;
                        IItem expediente = (IItem) expedientesIterator.next();
                        ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                        nreg = expediente.getString("NREG");
                        nombreAsociacion = "";

                        double importe = 0;

                        Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                        if (expResolucion.hasNext()) {
                            IItem resolucion = (IItem) expResolucion.next();
                            importe = resolucion.getDouble("IMPORTE3");
                            cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION3");
                            nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION3");
                        }

                        setCellText(xTable, "A" +  (i + 1), nreg);
                        setCellText(xTable, "B" +  (i + 1), ayuntamiento);
                        setCellText(xTable, "C" +  (i + 1), nombreAsociacion);
                        setCellText(xTable, "D" +  (i + 1), cifAsociacion);
                        setCellText(xTable, "E" +  (i + 1), "" + new DecimalFormat("#,##0.00").format(importe));
                    }
                }
            } else if ("%TABLA6%".equals(refTabla)) {
                // Obtenemos los expedientes relacionados y aprobados, ordenados
                // por ayuntamiento
                IItemCollection expRelacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"
                        + numexp + "'");
                Iterator<?> expRelacionadosIterator = expRelacionadosCollection.iterator();
                String query = "";
                while (expRelacionadosIterator.hasNext()) {
                    String numexpHijo = ((IItem) expRelacionadosIterator.next()).getString("NUMEXP_HIJO");
                    expedientesResolucion.add(numexpHijo);
                    query += "'" + numexpHijo + "',";
                }

                if (query.length() > 0) {
                    query = query.substring(0, query.length() - 1);
                }
                IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" + query
                        + ") AND ESTADOADM IN ('RC', 'RS') ORDER BY IDENTIDADTITULAR");
                Iterator<?> expedientesIterator = expedientesCollection.iterator();

                int numFilas = hayRechazados3;

                // Busca la posición de la tabla y coloca el cursor ahí
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");
                    if (expedientesIterator.hasNext() || hayRechazados3 > 0) {

                        // Inserta una tabla de 4 columnas y tantas filas
                        // como nuevas liquidaciones haya mas una de cabecera
                        XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                        Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                        XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);

                        // Añadimos 3 filas más para las dos de la cabecera de
                        // la tabla y uno para la celda final
                        xTable.initialize(numFilas + 1, 5);
                        XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                        xText.insertTextContent(xSearchTextRange, xTextContent, false);

                        colocaColumnas2(xTable);

                        // Rellena la cabecera de la tabla
                        setHeaderCellText(xTable, "A1", "NÚM. REGISTRO");
                        setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                        setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACIÓN");
                        setHeaderCellText(xTable, "D1", "C.I.F");
                        setHeaderCellText(xTable, "E1", "MOTIVO DENEGACIÓN");

                        int i = 0;
                        while (expedientesIterator.hasNext()) {
                            IItem expediente = (IItem) expedientesIterator.next();
                            ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                            nreg = expediente.getString("NREG");

                            if (motivoDenegacion == null){
                                motivoDenegacion = "";
                            }
                            if (ayuntamiento == null){
                                ayuntamiento = "";
                            }

                            motivoDenegacion = "";
                            Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                            if (expResolucion.hasNext()) {
                                IItem resolucion = (IItem) expResolucion.next();
                                motivoDenegacion = resolucion.getString("MOTIVO_RECHAZO3");
                                if (StringUtils.isNotEmpty(motivoDenegacion)) {
                                    i++;

                                    cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION3");
                                    nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION3");

                                    setCellText(xTable, "A" +  (i + 1), nreg);
                                    setCellText(xTable, "B" +  (i + 1), ayuntamiento);
                                    setCellText(xTable, "C" +  (i + 1), nombreAsociacion);
                                    setCellText(xTable, "D" +  (i + 1), cifAsociacion);
                                    setCellText(xTable, "E" +  (i + 1), motivoDenegacion);
                                }
                            }
                        }
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

    private void colocaColumnas1(XTextTable xTextTable) {

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

    private void colocaColumnas2(XTextTable xTextTable) {

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
            double dRelativeWidth = (double) 30000 * dRatio;

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
            dRelativeWidth = (double) 30000 * dRatio;
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
