package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

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
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class GenerateAdjudicacionProvisionalRule implements IRule {

    private OpenOfficeHelper ooHelper = null;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------

            
            String numExp = rulectx.getNumExp();
            String strCodTpDoc = "Adj provisional";
            String strTemplateName = "Adjudicación provisional";
            
            
            //Obtención de los participantes con rol licitador
            int nLicitadores = 0;
            String strQuery = "WHERE ROL = 'LIC' AND NUMEXP = '"+numExp+"' ORDER BY ID";
            IItemCollection licitadores = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
            Iterator<?> it = licitadores.iterator();
            
            while (it.hasNext()) {
                it.next();
                nLicitadores++;
            }
            
            //Creación de variables de sesión para las plantillas
            int taskId = rulectx.getTaskId();
            ITask iTask = invesFlowAPI.getTask(taskId);
            String strTaskName = iTask.getString("NOMBRE");
            cct.setSsVariable("NOMBRE_TRAMITE", strTaskName);

            //Generación del documento a partir de la plantilla
            String strTpDocName = DocumentosUtil.getNombreTipoDocByCod(cct, strCodTpDoc);
            CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, "previo");

            //Abro el documento con OppenOffice para añadir la tabla
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strTemplateName+" - previo", " ID DESC");
            File file1 = DocumentosUtil.getFile(cct, strInfoPag);
            //String cnt = "uno:socket,host=localhost,port=8100;urp;StarOffice.NamingService";
            //ooHelper = OpenOfficeHelper.getInstance(cnt);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
            XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
            XText xText = xTextDocument.getText();
            
            //Busca la posición de la tabla y coloca el cursor ahí
            //Usaremos el localizador %TABLA1%
            XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
            XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
            xSearchDescriptor.setSearchString("%TABLA1%");
            XInterface xSearchInterface = null;
            XTextRange xSearchTextRange = null;
            xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
            
            if (xSearchInterface != null) {
                //Cadena encontrada, la borro antes de insertar la tabla
                xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                xSearchTextRange.setString("");
            
                //Inserta una tabla de 5 columnas y tantas filas como licitadores haya mas una de cabecera
                XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
                xTable.initialize(nLicitadores + 1, 5);
                XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                xText.insertTextContent(xSearchTextRange, xTextContent, false);
    
                //adjustColumnsWidth(xTable);
                
                //Rellena la cabecera de la tabla
                setHeaderCellText(xTable, "A1", "EMPRESA");                
                setHeaderCellText(xTable, "B1", "Criterio 1");                
                setHeaderCellText(xTable, "C1", "Criterio 2");                
                setHeaderCellText(xTable, "D1", "Criterio 3");                
                setHeaderCellText(xTable, "E1", "Total");
                
                //Rellena la tabla con los datos de las solicitudes
                strQuery = "WHERE ROL = 'LIC' AND NUMEXP = '"+numExp+"' ORDER BY ID";
                licitadores = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
                it = licitadores.iterator();
                int nLicitador = 0;
                String strNombreLicitador = null;
                
                while (it.hasNext()) {
                    IItem licitador = (IItem)it.next();    
                    nLicitador++;
                    strNombreLicitador = licitador.getString("NOMBRE");
                    setCellText(xTable, "A" + (nLicitador+1), strNombreLicitador);
                }
            }    

            //Busca la posición de la tabla y coloca el cursor ahí
            //Usaremos el localizador %TABLA2%
            xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
            xSearchDescriptor = xSearchable.createSearchDescriptor();
            xSearchDescriptor.setSearchString("%TABLA2%");
            xSearchInterface = null;
            xSearchTextRange = null;
            xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
            
            if (xSearchInterface != null) {
                //Cadena encontrada, la borro antes de insertar la tabla
                xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                xSearchTextRange.setString("");
            
                //Inserta una tabla de 4 columnas y tantas filas como licitadores haya mas una de cabecera
                XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
                xTable.initialize(nLicitadores + 1, 4);
                XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                xText.insertTextContent(xSearchTextRange, xTextContent, false);
    
                //adjustColumnsWidth(xTable);
                
                //Rellena la cabecera de la tabla
                setHeaderCellText(xTable, "A1", "Empresas");                
                setHeaderCellText(xTable, "B1", "Oferta");                
                setHeaderCellText(xTable, "C1", "Características\nTécnicas");                
                setHeaderCellText(xTable, "D1", "Total");                
                
                //Rellena la tabla con los datos de las solicitudes
                strQuery = "WHERE ROL = 'LIC' AND NUMEXP = '"+numExp+"' ORDER BY ID";
                licitadores = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
                it = licitadores.iterator();
                int nLicitador = 0;
                String strNombreLicitador = null;
                
                while (it.hasNext()) {
                    IItem licitador = (IItem)it.next();    
                    nLicitador++;
                    strNombreLicitador = licitador.getString("NOMBRE");
                    setCellText(xTable, "A" + (nLicitador+1), strNombreLicitador);
                }
            }
            
            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            File file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();
            
            //Guarda el resultado en gestor documental
            strQuery = "WHERE NOMBRE = '"+strTpDocName+"'";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            it = collection.iterator();
            int tpdoc = 0;
            
            if (it.hasNext()) {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
            }
            
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream in = new FileInputStream(file);
            int docId = newdoc.getInt("ID");
            Object connectorSession = gendocAPI.createConnectorSession();
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strTemplateName);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            entityDoc.store(cct);
            file.delete();
            
            //Borra el documento previo del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" + " AND DESCRIPCION = '" + strTemplateName + " - previo'" ;
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            
            while (it.hasNext()) {
                IItem doc = (IItem)it.next();
                entitiesAPI.deleteDocument(doc);
            }
            
            //Borra las variables de sesion utilizadas
            cct.deleteSsVariable("NOMBRE_TRAMITE");

            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido generar el Informe del servicio.",e);
            
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }

    private void setHeaderCellText(XTextTable xTextTable, String CellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException  {
        XCell xCell = xTextTable.getCellByName(CellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(CellName));

        //Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", new String("Calibri"));
        xTPS.setPropertyValue("CharHeight", new Float(8.0));    
        xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
        

        //Texto de la celda
        xCellText.setString(strText);
    }    

    private void setCellText(XTextTable xTextTable, String CellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(CellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

        //Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", new String("Arial"));
        xTPS.setPropertyValue("CharHeight", new Float(10.0));    
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.LEFT);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

        //Texto de la celda
        xCellText.setString(strText);
    }    
}
