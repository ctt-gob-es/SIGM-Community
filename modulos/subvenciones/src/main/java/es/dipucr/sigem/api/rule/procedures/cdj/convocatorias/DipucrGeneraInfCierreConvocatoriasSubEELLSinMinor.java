package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraInfCierreConvocatoriasSubEELLSinMinor extends DipucrAutoGeneraDocIniTramiteRule{

    public static final Logger LOGGER = Logger
            .getLogger(DipucrGeneraInfCierreConvocatoriasSubEELLSinMinor.class);
    
    private IRuleContext ruleContext;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {    
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try{
            IClientContext cct = rulectx.getClientContext();
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
            setRuleContext(rulectx);
            refTablas = "%TABLA1%,%TABLA2%";
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());

        return true;
    }
    
    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        try {
            cct.setSsVariable("ANIO", "" + Calendar.getInstance().get(Calendar.YEAR));
            
            String numexpConvocatoria = getRuleContext().getNumExp();
                         
            //Obtenemos el asunto de la convocatoria
            String convocatoria = "";
            IItem expConv = ExpedientesUtil.getExpediente(cct, numexpConvocatoria);
            if(expConv != null){
                convocatoria = expConv.getString("ASUNTO");
            }
            //Obtenemos el expediente de decreto
            IItemCollection expRelacionadosPadreCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" +numexpConvocatoria+"' ORDER BY SUBSTR(SPLIT_PART(NUMEXP_HIJO, '/',1), 5)::INT DESC, SPLIT_PART(NUMEXP_HIJO, '/',2)::INT ASC");
            Iterator<?> expRelacionadosPadreIterator = expRelacionadosPadreCollection.iterator();
            String numexpDecreto = "";
            boolean encontrado = false;
            while (expRelacionadosPadreIterator.hasNext() && !encontrado){
                IItem expRel = (IItem)expRelacionadosPadreIterator.next();
                String numexpRel = expRel.getString("NUMEXP_HIJO");
                String nombreProc = "";
                IItem expProc = ExpedientesUtil.getExpediente(cct, numexpRel);
                if(expProc != null){
                    nombreProc = expProc.getString("NOMBREPROCEDIMIENTO");                
                    if(nombreProc.trim().toUpperCase().contains("DECRETO")){
                        numexpDecreto = numexpRel;
                        encontrado = true;
                    }
                }
            }
            
            IItemCollection decretoCollection = cct.getAPI().getEntitiesAPI().getEntities(Constants.TABLASBBDD.SGD_DECRETO, numexpDecreto);
            Iterator<?> decretoIterator = decretoCollection.iterator();
            String numDecreto = "";
            Date fechaDecreto = new Date();
            if(decretoIterator.hasNext()){
                IItem decreto = (IItem)decretoIterator.next();
                numDecreto = decreto.getInt("ANIO")+"/" +decreto.getInt("NUMERO_DECRETO");
                fechaDecreto = decreto.getDate("FECHA_DECRETO");
            }
            
            //Obtenemos el número de boletín y la fecha
            IItemCollection expRelacionadosPadreCollectionBop = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" +numexpConvocatoria+"' ORDER BY SUBSTR(SPLIT_PART(NUMEXP_HIJO, '/',1), 5)::INT DESC, SPLIT_PART(NUMEXP_HIJO, '/',2)::INT ASC");
            Iterator<?> expRelacionadosPadreIteratorBop = expRelacionadosPadreCollectionBop.iterator();
            String numexpBoletin = "";
            encontrado = false;
            while (expRelacionadosPadreIteratorBop.hasNext() && !encontrado){
                IItem expRel = (IItem)expRelacionadosPadreIteratorBop.next();
                String numexpRel = expRel.getString("NUMEXP_HIJO");
                String nombreProc = "";
                IItem expProc = ExpedientesUtil.getExpediente(cct, numexpRel);
                if(expProc != null){
                    nombreProc = expProc.getString("NOMBREPROCEDIMIENTO");
                    if(nombreProc.trim().toUpperCase().contains("BOP")){
                        numexpBoletin = numexpRel;
                        encontrado = true;
                    }
                }
            }            
            IItemCollection boletinCollection = cct.getAPI().getEntitiesAPI().getEntities("BOP_SOLICITUD", numexpBoletin);
            Iterator<?> boletinIterator = boletinCollection.iterator();
            int numBoletin = 0;
            Date fechaBoletin = new Date();
            if(boletinIterator.hasNext()){
                IItem boletin = (IItem)boletinIterator.next();                
                fechaBoletin = boletin.getDate("FECHA_PUBLICACION");
                //Obtenemos el número de boletín
                IItemCollection boletinesCollection = cct.getAPI().getEntitiesAPI().queryEntities("BOP_PUBLICACION", "WHERE FECHA='" + fechaBoletin + "'");
                Iterator<?> boletinesIterator = boletinesCollection.iterator();
                if(boletinesIterator.hasNext()){
                    numBoletin = ((IItem)boletinesIterator.next()).getInt("NUM_BOP");
                }
            }
            
            //Recuperamos el número de informes generados ya
            String consulta = "WHERE NUMEXP  = '" +numexpConvocatoria+"' " + "AND ID_TPDOC IN (SELECT ID FROM SPAC_CT_TPDOC WHERE NOMBRE='" +tipoDocumento+"')";
            IItemCollection justificacionesCollection = DocumentosUtil.queryDocumentos(cct, consulta);

            cct.setSsVariable("NUM_DECRETO", numDecreto);
            cct.setSsVariable("FECHA_DECRETO", new SimpleDateFormat("dd/MM/yyyy").format(fechaDecreto));
            
            cct.setSsVariable("NUM_BOLETIN", "" + numBoletin);
            cct.setSsVariable("FECHA_BOLETIN", new SimpleDateFormat("dd/MM/yyyy").format(fechaBoletin));
            
            cct.setSsVariable("NUM_INFORME", "" +(justificacionesCollection.toList().size()+1));
            cct.setSsVariable("CONVOCATORIA", convocatoria);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("ANIO");            
            cct.deleteSsVariable("NUM_DECRETO");
            cct.deleteSsVariable("FECHA_DECRETO");            
            cct.deleteSsVariable("NUM_BOLETIN");
            cct.deleteSsVariable("FECHA_BOLETIN");
            cct.deleteSsVariable("NUM_INFORME");
            cct.deleteSsVariable("CONVOCATORIA");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String ayuntamiento = "";
        String cif = "";
        String proyecto = "";
        
        try{
            IClientContext cct = rulectx.getClientContext();
            //Recuperamos los expedientes a justificar
            if ("%TABLA1%".equals(refTabla)){
                IItemCollection expedientesCollection = ExpedientesUtil.queryExpedientes(cct, "WHERE NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = '" +numexp+"') AND ESTADOADM='JI' ORDER BY IDENTIDADTITULAR");
                   Iterator<?> expedientesIterator = expedientesCollection.iterator();
                   
                   //Vamos a calcular el número de filas, son una por cada proyecto                   
                   int numFilas = expedientesCollection.toList().size();
    
                //Busca la posición de la tabla y coloca el cursor ahí
                //Usaremos el localizador %TABLA1%
                   
                XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
                XTextTable xTable = null;
                
                if (xSearchInterface != null){
                    //Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");
                    
                    //Inserta una tabla de 4 columnas y tantas filas
                    //como nuevas liquidaciones haya mas una de cabecera
                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
                    
                    //Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
                    xTable.initialize(numFilas + 1, 5);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);
    
                    colocaColumnas1(xTable);
    
                    //Rellena la cabecera de la tabla                
                    setHeaderCellText(xTable, "A1", "AYUNTAMIENTO");    
                    setHeaderCellText(xTable, "B1", "C.I.F");                
                    setHeaderCellText(xTable, "C1", "PROYECTO / ACTIVIDAD");                
                    setHeaderCellText(xTable, "D1", "SUBVENCIÓN");
                    setHeaderCellText(xTable, "E1", "DEVOLUCIÓN");
                    int i=0;
                    while(expedientesIterator.hasNext()){
                            
                        IItem expediente = (IItem) expedientesIterator.next();
                        ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                        cif = expediente.getString("NIFCIFTITULAR");
                        
                        if(ayuntamiento == null){
                            ayuntamiento = "";
                        }
                        if(cif == null){
                            cif = "";
                        }
                        proyecto = "";                
                        
                        IItem solicitud = (IItem) entitiesAPI.getEntities("DPCR_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator().next();
                        proyecto = solicitud.getString("FINALIDAD");
                    
                        if(proyecto == null){
                            proyecto = "";
                        }
                    
                        double importe = 0;
                        double devolucion = 0;
                           
                        Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                        if(expResolucion.hasNext()){
                            IItem resolucion = (IItem) expResolucion.next();
                            importe = resolucion.getDouble("IMPORTE");
                            
                            try{
                                devolucion = resolucion.getDouble("DEVOLUCION");
                            } catch(ISPACException e){
                                LOGGER.debug("El campo devolución es vacío. " + e.getCustomMessagesFile(), e);
                                devolucion = 0;
                            }
                            if(devolucion < 0){
                                devolucion = 0;
                            }
                            
                        }                                          
                        i++;    
                        setCellText(xTable, "A" + (i+1), ayuntamiento);
                        setCellText(xTable, "B" + (i+1), cif);
                        setCellText(xTable, "C" + (i+1), proyecto);
                        setCellText(xTable, "D" + (i+1), "" + new DecimalFormat("#,##0.00").format(importe));
                        setCellText(xTable, "E" + (i+1), "" + new DecimalFormat("#,##0.00").format(devolucion));
                    }//fin For listaExp
                }//fin if (xSearchInterface != null)
            }
            
            //Recuperamos los expedientes a justificar
            if ("%TABLA2%".equals(refTabla)){
                IItemCollection expedientesCollection = ExpedientesUtil.queryExpedientes(cct, "WHERE NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = '" +numexp+"') AND ESTADOADM='RC' ORDER BY IDENTIDADTITULAR");
                   Iterator<?> expedientesIterator = expedientesCollection.iterator();
                   
                   //Vamos a calcular el número de filas, son una por cada proyecto                   
                   int numFilas = expedientesCollection.toList().size();
    
                //Busca la posición de la tabla y coloca el cursor ahí
                //Usaremos el localizador %TABLA1%
                   
                XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
                XTextTable xTable = null;
                
                if (xSearchInterface != null){
                    //Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");
                    
                    //Inserta una tabla de 4 columnas y tantas filas
                    //como nuevas liquidaciones haya mas una de cabecera
                    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                    xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
                    
                    //Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
                    xTable.initialize(numFilas + 1, 4);
                    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                    xText.insertTextContent(xSearchTextRange, xTextContent, false);
    
                    colocaColumnas2(xTable);
    
                    //Rellena la cabecera de la tabla                
                    setHeaderCellText(xTable, "A1", "AYUNTAMIENTO");    
                    setHeaderCellText(xTable, "B1", "C.I.F");                
                    setHeaderCellText(xTable, "C1", "PROYECTO / ACTIVIDAD");                
                    setHeaderCellText(xTable, "D1", "MOTIVO RECHAZO");
                    int i=0;
                    while(expedientesIterator.hasNext()){
                            
                        IItem expediente = (IItem) expedientesIterator.next();
                        ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                        cif = expediente.getString("NIFCIFTITULAR");
                        
                        if(ayuntamiento == null){
                            ayuntamiento = "";
                        }
                        if(cif == null){
                            cif = "";
                        }
                        proyecto = "";                
                        
                        IItem solicitud = (IItem) entitiesAPI.getEntities("DPCR_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator().next();
                        proyecto = solicitud.getString("FINALIDAD");
                    
                        if(proyecto == null){
                            proyecto = "";
                        }
                    
                        String motivoRechazo = "";
                           
                        Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expediente.getString("NUMEXP")).iterator();
                        if(expResolucion.hasNext()){
                            IItem resolucion = (IItem) expResolucion.next();
                            motivoRechazo = resolucion.getString("MOTIVO_RECHAZO");
                            
                            if(StringUtils.isEmpty(motivoRechazo)){
                                motivoRechazo = "";
                            }
                        }
                        
                        i++;    
                        setCellText(xTable, "A" + (i+1), ayuntamiento);
                        setCellText(xTable, "B" + (i+1), cif);
                        setCellText(xTable, "C" + (i+1), proyecto);
                        setCellText(xTable, "D" + (i+1), "" + motivoRechazo);
                    }//fin For listaExp
                }//fin if (xSearchInterface != null)
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    private void setHeaderCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(cellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName));

        //Propiedades        
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));    
        xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
        xCPS.setPropertyValue("BackColor", Integer.valueOf(0xC0C0C0));
        
        //Texto de la celda
        xCellText.setString(strText);
    }    

    private void setCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException { 
        XCell xCell = xTextTable.getCellByName(cellName);        
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

        //Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));    
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

        //Texto de la celda
        xCellText.setString(strText);
    }
  
    private void colocaColumnas1(XTextTable xTextTable){
        
        XPropertySet xPS = ( XPropertySet ) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);
         
        // Get table Width and TableColumnRelativeSum properties values
        int iWidth;
        try {
            iWidth = ( Integer ) xPS.getPropertyValue( "Width" );
            
            short sTableColumnRelativeSum = ( Short ) xPS.getPropertyValue( "TableColumnRelativeSum" );
             
            // Get table column separators
            Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );
             
            TableColumnSeparator[] xSeparators = ( TableColumnSeparator[] )UnoRuntime.queryInterface(
                TableColumnSeparator[].class, xObj );

            
            //Calculamos el tamaño que le queremos dar a la celda
            //Se empieza colocando de la última a la primera
            double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
            double dRelativeWidth = ( double ) 16000 * dRatio;
            
            // Last table column separator position
            double dPosition = sTableColumnRelativeSum - dRelativeWidth;
             
            // Set set new position for all column separators        
            //Número de separadores
            int i = xSeparators.length - 1;
            xSeparators[i].Position = (short) Math.ceil( dPosition );
            
            i--;            
            dRelativeWidth = ( double ) 16000 * dRatio;
            dPosition -= dRelativeWidth;                    
            xSeparators[i].Position = (short) Math.ceil( dPosition );
            
            i--;            
            dRelativeWidth = ( double ) 40000 * dRatio;
            dPosition -= dRelativeWidth;                    
            xSeparators[i].Position = (short) Math.ceil( dPosition );
            
            i--;            
            dRelativeWidth = ( double ) 13000 * dRatio;
            dPosition -= dRelativeWidth;                    
            xSeparators[i].Position = (short) Math.ceil( dPosition );                        
            
            // Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
            xPS.setPropertyValue( "TableColumnSeparators", xSeparators );    
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

    private void colocaColumnas2(XTextTable xTextTable){
    
    XPropertySet xPS = ( XPropertySet ) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);
     
    // Get table Width and TableColumnRelativeSum properties values
    int iWidth;
    try {
        iWidth = ( Integer ) xPS.getPropertyValue( "Width" );
        
        short sTableColumnRelativeSum = ( Short ) xPS.getPropertyValue( "TableColumnRelativeSum" );
         
        // Get table column separators
        Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );
         
        TableColumnSeparator[] xSeparators = ( TableColumnSeparator[] )UnoRuntime.queryInterface(
            TableColumnSeparator[].class, xObj );

        
        //Calculamos el tamaño que le queremos dar a la celda
        //Se empieza colocando de la última a la primera
        double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
        double dRelativeWidth = ( double ) 35000 * dRatio;
        
        // Last table column separator position
        double dPosition = sTableColumnRelativeSum - dRelativeWidth;
         
        // Set set new position for all column separators        
        //Número de separadores
        int i = xSeparators.length - 1;
        xSeparators[i].Position = (short) Math.ceil( dPosition );
        
        i--;            
        dRelativeWidth = ( double ) 35000 * dRatio;
        dPosition -= dRelativeWidth;                    
        xSeparators[i].Position = (short) Math.ceil( dPosition );
        
        i--;            
        dRelativeWidth = ( double ) 13000 * dRatio;
        dPosition -= dRelativeWidth;                    
        xSeparators[i].Position = (short) Math.ceil( dPosition );                        
        
        // Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
        xPS.setPropertyValue( "TableColumnSeparators", xSeparators );    
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

    private IRuleContext getRuleContext() {
        return ruleContext;
    }

    private void setRuleContext(IRuleContext ruleContext) {
        this.ruleContext = ruleContext;
    }
}