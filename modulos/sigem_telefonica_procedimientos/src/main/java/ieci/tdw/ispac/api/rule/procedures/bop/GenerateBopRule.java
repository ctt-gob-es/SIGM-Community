package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.sun.star.awt.FontSlant;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.style.TabAlign;
import com.sun.star.style.TabStop;
import com.sun.star.text.ReferenceFieldPart;
import com.sun.star.text.ReferenceFieldSource;
import com.sun.star.text.XPageCursor;
import com.sun.star.text.XReferenceMarksSupplier;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XRefreshable;
import com.sun.star.view.XPrintable;

import es.dipucr.sigem.api.rule.common.bop.BOPConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

public class GenerateBopRule implements IRule {
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(GenerateBopRule.class);
    
    protected String strEntity = "BOP_PUBLICACION";
    protected String strDocBopGeneral = "BOP - General";
    protected String strDocAnuncio = "BOP - Anuncio publicado";
    
    //INICIO [eCenpri-Felipe #593]
    public static final String MARCA_FIN_INDICE = "[FIN_INDICE]";
    public static final String TEXTO_NUM_ANUNCIO = "Anuncio número ";
    //FIN [eCenpri-Felipe #593]
    
    private OpenOfficeHelper ooHelper = null;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        ArrayList<String> sumariosAnuncios = new ArrayList<String>();
        ArrayList<String> entidadesAnuncios = new ArrayList<String>();
        ArrayList<String> clasificacionesEntidad = new ArrayList<String>();
        ArrayList<String> administracionesEntidad = new ArrayList<String>();
        int nAnuncio = 0;
        Float charHeight = null;
        Integer charColor = null;
        Integer paraBottomMargin_aux = null;
        short paraBottomMargin = 0;
        Integer paraTopMargin_aux = null;
        short paraTopMargin = 0;
//        String templateFilename = Messages.getString("cTemplate_bop"); [dipucr-Felipe #1337]
        
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------

            //Obtiene los datos de la publicación
            IItemCollection bops = entitiesAPI.getEntities(strEntity, rulectx.getNumExp());
            Iterator<?> it = bops.iterator();
            IItem bop = null;
            
            if(it.hasNext()) {
                bop = (IItem)it.next();
            }
            
            String fecha = bop.getString("FECHA");
            Date fechaDate = bop.getDate("FECHA"); 
            LOGGER.warn("Fecha: " + fecha);
            cct.setSsVariable("FECHA", fecha);
            String numBop = bop.getString("NUM_BOP");
            LOGGER.warn("Número de BOP: " + numBop);
            cct.setSsVariable("NUM_BOP", numBop);
            String numPag = bop.getString("NUM_PAGINA");
            LOGGER.warn("Número de página: " + numPag);
            cct.setSsVariable("NUM_PAGINA", numPag);
            String numAnun = bop.getString("NUM_ANUNCIO");
            LOGGER.warn("Número de anuncio: " + numAnun);
            cct.setSsVariable("NUM_ANUNCIO", numAnun);
            
            //INICIO [dipucr-Felipe #1337]
            IItem itemDocumento = DocumentosUtil.generarDocumento(rulectx, strDocBopGeneral, "borrar");
            String strInfoPag = itemDocumento.getString(DocumentosUtil.INFOPAG);
            File fileBop = DocumentosUtil.getFile(cct, strInfoPag, null, null);
            String templateFilename = fileBop.getPath();
            //FIN [dipucr-Felipe #1337]
            
            //Carga la plantilla con el BOP
            LOGGER.warn("Abriendo plantilla: " + templateFilename);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + templateFilename);

            //Variables de acceso al documento
            XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
            XText xText = xTextDocument.getText();
            XTextCursor xTextCursor = xText.createTextCursor();
            XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
            
            //Reemplazo de etiquetas (número de BOP y fecha)
            XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier)UnoRuntime.queryInterface(XTextFieldsSupplier.class, xComponent);            
            XNameAccess xNamedFieldMasters = xTextFieldsSupplier.getTextFieldMasters();
            //Campo número BOP
            Object fieldMaster1 = xNamedFieldMasters.getByName( "com.sun.star.text.fieldmaster.User.field_num_bop");
            XPropertySet xPropertySet1 = (XPropertySet)UnoRuntime.queryInterface( XPropertySet.class, fieldMaster1);
            xPropertySet1.setPropertyValue("Content", "Número " + numBop);
            //Campo fecha
            Object fieldMaster2 = xNamedFieldMasters.getByName( "com.sun.star.text.fieldmaster.User.field_fecha_bop");
            XPropertySet xPropertySet2 = (XPropertySet)UnoRuntime.queryInterface( XPropertySet.class, fieldMaster2);
            xPropertySet2.setPropertyValue("Content", getFecha(rulectx, fechaDate));
            //Refresco los campos
            XEnumerationAccess xEnumeratedFields = xTextFieldsSupplier.getTextFields();            
            XRefreshable xRefreshable = (XRefreshable)UnoRuntime.queryInterface( XRefreshable.class, xEnumeratedFields);
            xRefreshable.refresh();
            
            
            //Obtención de los anuncios a publicar
            File file = null;
            String strQueryAux = "WHERE NUMEXP='" + rulectx.getNumExp() + "' ORDER BY ORDEN";
            IItemCollection anuncios = entitiesAPI.queryEntities("BOP_ANUNCIOS", strQueryAux);
            it = anuncios.iterator();
            String texto = null;
            boolean primerAnuncio = true;
            
            CommonFunctions.establecerNumeroPagina(xComponent, numPag);

            //Inserción de los anuncios
            //-------------------------
            while (it.hasNext()) {
                IItem anuncio = (IItem)it.next();
                IItem doc = getDocument(rulectx, anuncio);

                if (doc != null) {
                    LOGGER.warn("Insertando anuncio...");
                    LOGGER.warn("SUMARIO: " + anuncio.getString("SUMARIO"));
                    sumariosAnuncios.add(anuncio.getString("SUMARIO"));
                    LOGGER.warn("ENTIDAD: " + anuncio.getString("ENTIDAD"));
                    entidadesAnuncios.add(anuncio.getString("ENTIDAD"));
                    LOGGER.warn("CLASIFICACIÓN: " + anuncio.getString("CLASIFICACION"));
                    clasificacionesEntidad.add(anuncio.getString("CLASIFICACION"));
                    // [eCenpri-Felipe #451] Se obtiene la administración de la BBDD
                    // Se estaba haciendo a pelo
                    String clasificacion = anuncio.getString("CLASIFICACION");
                    String administracion = BopUtils.getGrupoAdministracion(rulectx, clasificacion);
                    LOGGER.warn("ADMINISTRACIÓN: " + administracion);
                    administracionesEntidad.add(administracion);
                    
                    // FIN [eCenpri-Felipe #451]
                    if (primerAnuncio) {
                        primerAnuncio = false;
                        
                    } else {
                        LibreOfficeUtil.insertaSaltoDePagina(xComponent);
                    }
//                    file = CommonFunctions.getFile(rulectx, doc.getString(DocumentosUtil.INFOPAG));
                    file = DocumentosUtil.getFile(cct, doc.getString(DocumentosUtil.INFOPAG), null, null); //[eCenpri-Felipe #28 #95]
                    LOGGER.warn("InfoPag del documento a concatenar: " + doc.getString(DocumentosUtil.INFOPAG));
                    CommonFunctions.InsertarBookmark(xComponent, "Anuncio" + nAnuncio);
//                    CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
                    this.ConcatenaAnuncio //[eCenpri-Felipe #28 #95]
                        (xComponent, "file://" + file.getPath());
                    file.delete();
//                    texto = "Número " + numAnun; //[eCenpri-Felipe #593]
                    texto = TEXTO_NUM_ANUNCIO + numAnun;
                    LOGGER.warn("NÚMERO DE ANUNCIO: " + texto);
                    
                    //INICIO [eCenpri-Felipe #593] Actualizamos el número de anuncio en la BBDD
                    IItemCollection col = entitiesAPI.getEntities("BOP_SOLICITUD", anuncio.getString("NUMEXP_ANUNCIO"));
                    IItem itemSolicitud = (IItem) col.iterator().next();
                    itemSolicitud.set("NUM_BOLETIN", Integer.valueOf(numBop));
                    itemSolicitud.set("NUM_ANUNCIO_BOP", Integer.valueOf(numAnun));
                    itemSolicitud.store(cct);
                    //FIN [eCenpri-Felipe #593]
                    
                    numAnun = String.valueOf(Integer.parseInt(numAnun) + 1);

                    CommonFunctions.insertaTextoNegrita(xComponent, texto);
                    nAnuncio++;
                }
            }
            
            // Introducir el índice
            // --------------------

            //Se recuperan las referencias para escribirlas como números de página
            //Estos pasos son para acceder a la lista de referencias
            XReferenceMarksSupplier xRefSupplier = (XReferenceMarksSupplier) UnoRuntime.queryInterface(
                 XReferenceMarksSupplier.class, xTextDocument);
            XNameAccess xMarks = (XNameAccess) UnoRuntime.queryInterface(XNameAccess.class,
                 xRefSupplier.getReferenceMarks());
            String[] aNames = xMarks.getElementNames();

            if (aNames.length > 0) {
                //Vuelvo al principio del documento
                 xTextCursor.gotoRange(xText.getStart(),false);

                 //Modifico los tabuladores para que salgan como puntitos y alineados a la derecha
                 XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
                 TabStop ts[] = new TabStop[1];
                 ts[0] = new TabStop();
                 ts[0].Position = 16800; //16.8cm
                 ts[0].Alignment = TabAlign.RIGHT;
                 ts[0].FillChar = '.';
                 //Antes de guardarlo, me quedo con la configuración original para poder restaurarla
                 TabStop oldTs[] = (TabStop[])xTPS.getPropertyValue("ParaTabStops");
                 xTPS.setPropertyValue("ParaTabStops", ts);

                 LOGGER.warn("Número de anuncios: " + nAnuncio);
                for (int j = 0; j < nAnuncio; j ++) {
                    // Ver si hay que meter el nombre de la administración

                    charHeight = (Float) xTPS.getPropertyValue("CharHeight");
                    charColor = (Integer) xTPS.getPropertyValue("CharColor");
                    paraBottomMargin_aux = (Integer) xTPS.getPropertyValue("ParaBottomMargin");
                    paraBottomMargin = paraBottomMargin_aux.shortValue();
                    paraTopMargin_aux = (Integer) xTPS.getPropertyValue("ParaTopMargin");
                    paraTopMargin = paraTopMargin_aux.shortValue();
                    
                    xTPS.setPropertyValue("CharPosture", FontSlant.NONE);    
                    xTPS.setPropertyValue("CharFontName", new String("Gill Sans MT"));
                    xTPS.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
                    xTPS.setPropertyValue("CharHeight", new Float(18.0));    
                    xTPS.setPropertyValue("CharColor", new Integer(0xFFB143));    
                    xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
                    xTPS.setPropertyValue("ParaBottomMargin", new Short((short)700));
                    xTPS.setPropertyValue("ParaTopMargin", new Short((short)400));
                    
                    if (j == 0) {
                        LOGGER.warn("Primera administración: " + administracionesEntidad.get(0).toString().toUpperCase());
                        xText.insertString(xTextCursor, administracionesEntidad.get(0).toString().toUpperCase() + "\r", false);
                    } else {
                        if (!administracionesEntidad.get(j).equals(administracionesEntidad.get(j - 1))) {
                            LOGGER.warn("Insertar administración: " + administracionesEntidad.get(j).toString().toUpperCase());
                            xText.insertString(xTextCursor, administracionesEntidad.get(j).toString().toUpperCase() + "\r", false);
                        }
                    }
                    
                    xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.LEFT);
                    xTPS.setPropertyValue("ParaTopMargin", new Short(paraTopMargin));

                    // Ver si hay que meter el nombre de la clasificación
                    
                    xTPS.setPropertyValue("CharFontName", new String("Gill Sans MT"));
                    xTPS.setPropertyValue("ParaBottomMargin", new Short((short)200));
                    xTPS.setPropertyValue("CharHeight", new Float(16.0));    
                    xTPS.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));
                    xTPS.setPropertyValue("CharColor", charColor);    

                    if (j == 0) {
                        LOGGER.warn("Primera clasificación: " + clasificacionesEntidad.get(0).toString().toUpperCase());
                        xText.insertString(xTextCursor, clasificacionesEntidad.get(0).toString().toUpperCase() + "\r", false);
                        
                    } else {
                        if (!clasificacionesEntidad.get(j).equals(clasificacionesEntidad.get(j - 1))) {
                            LOGGER.warn("Insertar clasificación: " + clasificacionesEntidad.get(j).toString().toUpperCase());
                            xText.insertString(xTextCursor, clasificacionesEntidad.get(j).toString().toUpperCase() + "\r", false);
                        }
                    }
                    
                    // Ver si hay que meter el nombre de la entidad
                    
                    xTPS.setPropertyValue("CharFontName", new String("Trebuchet MS"));
                    xTPS.setPropertyValue("CharHeight", new Float(12.0));    
                    xTPS.setPropertyValue("ParaBottomMargin", new Short(paraBottomMargin));

                    if (j == 0) {
                        LOGGER.warn("Primer nombre de entidad: " + entidadesAnuncios.get(0).toString().toUpperCase());
                        xText.insertString(xTextCursor, entidadesAnuncios.get(0).toString().toUpperCase() + "\r", false);
                        
                    } else {
                        if (!entidadesAnuncios.get(j).equals(entidadesAnuncios.get(j - 1))) {
                            LOGGER.warn("Insertar nombre de entidad: " + entidadesAnuncios.get(j).toString().toUpperCase());
                            xText.insertString(xTextCursor, entidadesAnuncios.get(j).toString().toUpperCase() + "\r", false);
                        }
                    }
                    
                    xTPS.setPropertyValue("CharHeight", charHeight);    
                    xTPS.setPropertyValue("CharPosture", FontSlant.ITALIC);    
                    xTPS.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
                    
                    // Meter entidades
                    
                    XPropertySet xFieldProps = (XPropertySet) UnoRuntime.queryInterface( XPropertySet.class, xDocMSF.createInstance("com.sun.star.text.textfield.GetReference"));
                    
                     LOGGER.warn("Índice del anuncio " + j);
                    for (int i = 0; i < aNames.length; i ++) {
                        
                         LOGGER.warn("Buscando... Referencia " + aNames[i]);
                        if (("Anuncio" + j).equals(aNames[i])) {
                             LOGGER.warn("Encontrada referencia: " + aNames[i]);
                            xFieldProps.setPropertyValue("SourceName", aNames[i]);
                            break;
                        }
                    }
                    
                    //Le digo que es una referencia (puede ser una nota, bookmark, etc)
                    xFieldProps.setPropertyValue ("ReferenceFieldSource", new Short( ReferenceFieldSource.REFERENCE_MARK));
                    //Le digo que quiero escribir la referencia como número de página
                    xFieldProps.setPropertyValue("ReferenceFieldPart", new Short (ReferenceFieldPart.PAGE));
                    //Obtengo el contenido para luego insertarlo
                    XTextContent xRefContent = (XTextContent) UnoRuntime.queryInterface( XTextContent.class, xFieldProps);                 
                    LOGGER.warn("Contenido de la referencia: " + xRefContent.toString()); 
                     //Línea del indice con un \t al final
                    LOGGER.warn("Sumario del anuncio: " + sumariosAnuncios.get(j));
                     xText.insertString(xTextCursor, sumariosAnuncios.get(j) + "\t", false);
                     //Aquí va el número de página
                     xText.insertTextContent(xTextCursor, xRefContent, false);
                     //Y termino la linea
                     xText.insertString(xTextCursor, "\r", false);
                }
                
                 //Restauro los tabuladores
                 xTPS.setPropertyValue("ParaTabStops", oldTs);
            }

             //Actualizo las referencias
            XRefreshable xRefresh = (XRefreshable)UnoRuntime.queryInterface( XRefreshable.class, xTextDocument);
            xRefresh.refresh();
            
            //INICIO [eCenpri-Felipe #593]
            //Marcamos el fin del índice con texto en color blanco
            XPropertySet xTextProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
//            int color = Color.WHITE.getRGB(); //No funciona
            int color = new Color(254,254,254).getRGB();
            xTextProps.setPropertyValue("CharColor", Integer.valueOf(color));
            xText.insertString(xTextCursor, MARCA_FIN_INDICE, false);
            //FIN [eCenpri-Felipe #593]
            
            // Imprimir los anuncios en ficheros por separado
            // ----------------------------------------------
            
            LOGGER.warn("Generando los ficheros individuales para los anuncios...");

            String strPrintAnuncios = BOPConfiguration.getInstance().getProperty("cPrintAnuncios_bop");
            LOGGER.warn("cPrintAnuncios_bop: " + strPrintAnuncios);
            
            if (strPrintAnuncios.compareToIgnoreCase("SI")!=0) {
                LOGGER.warn("Cancelar impresión de anuncios.");
                
            } else {
                LOGGER.warn("Proceder a la impresión de anuncios");
                XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent); 
                XController xController = xModel.getCurrentController(); 
                XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController); 
                XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor(); 
                int pagInicio = 0;
                int pagFin = 0;
                String pagInicioString = null;
                String pagFinString = null;
    
                //Estos pasos son para acceder a la lista de referencias
                XReferenceMarksSupplier xRefSupplier1 = (XReferenceMarksSupplier) UnoRuntime.queryInterface(
                     XReferenceMarksSupplier.class, xTextDocument);
                XNameAccess xMarks1 = (XNameAccess) UnoRuntime.queryInterface(XNameAccess.class,
                     xRefSupplier1.getReferenceMarks());
    
                for (int k = 0; k < nAnuncio; k ++) {
                    LOGGER.warn("Generando el fichero para el anuncio " + (k + 1) + "...");
                    if (pagFin != 0) {
                        pagInicio = pagFin + 1;
                        LOGGER.warn("Página de inicio: " + pagInicio);
    
                        if (xMarks1.hasByName("Anuncio" + (k + 1))) {
                            Object foundBookmark = xMarks1.getByName("Anuncio" + (k + 1));
                            LOGGER.warn("Encontrada la referencia al Anuncio" + (k + 1));
                            XTextContent xFoundBookmark = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, foundBookmark);
                            XTextRange xAncla = xFoundBookmark.getAnchor();
                            xViewCursor.gotoRange(xAncla, false);
                            XPageCursor xPageCursor = (XPageCursor)UnoRuntime.queryInterface(XPageCursor.class, xViewCursor);
                            pagFin = xPageCursor.getPage() - 1;
                            
                        } else {
                            LOGGER.warn("No encontrada la referencia al Anuncio" + (k + 1) + ". Se va al final del documento.");
                            xViewCursor.gotoEnd(false);
                            pagFin = -1;
                        }
                        LOGGER.warn("Página de fin: " + pagFin);
                        
                    } else {
                        if (xMarks1.hasByName("Anuncio0")) {
                            Object foundBookmark1 = xMarks1.getByName("Anuncio0");
                            LOGGER.warn("Encontrada la referencia al Anuncio0");
                            XTextContent xFoundBookmark1 = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, foundBookmark1);
                            XTextRange xAncla1 = xFoundBookmark1.getAnchor();
                            xViewCursor.gotoRange(xAncla1, false);
                            XPageCursor xPageCursor1 = (XPageCursor)UnoRuntime.queryInterface(XPageCursor.class, xViewCursor);
                            pagInicio = xPageCursor1.getPage();
                            
                        } else {
                            LOGGER.warn("No encontrada la referencia al Anuncio0. Se va al final del documento.");
                            xViewCursor.gotoEnd(false);
                            pagInicio = -1;
                        }
                        LOGGER.warn("Página de inicio: " + pagInicio);
    
                        if (xMarks1.hasByName("Anuncio1")) {
                            Object foundBookmark2 = xMarks1.getByName("Anuncio1");
                            LOGGER.warn("Encontrada la referencia al Anuncio1");
                            XTextContent xFoundBookmark2 = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, foundBookmark2);
                            XTextRange xAncla2 = xFoundBookmark2.getAnchor();
                            xViewCursor.gotoRange(xAncla2, false);
                            XPageCursor xPageCursor2 = (XPageCursor)UnoRuntime.queryInterface(XPageCursor.class, xViewCursor);
                            pagFin = xPageCursor2.getPage() - 1;
                            
                        } else {
                            LOGGER.warn("No encontrada la referencia al Anuncio1. Se va al final del documento.");
                            xViewCursor.gotoEnd(false);
                            pagFin = -1;
                        }
                        LOGGER.warn("Página de fin: " + pagFin);
                    }
                    
                    if (pagInicio == -1) {
                        pagInicioString = new String("");
                        
                    } else {
                        pagInicioString = String.valueOf(pagInicio);
                    }
                    
                    if (pagFin == -1) {
                        pagFinString = new String("");
                        
                    } else {
                        pagFinString = String.valueOf(pagFin);
                    }
    
                    LOGGER.warn("Abrir diágolo de impresoras");
                    XPrintable xPrintable = (XPrintable)UnoRuntime.queryInterface(XPrintable.class, xComponent);
                    PropertyValue[] printerDesc = new PropertyValue[1];
                    printerDesc[0] = new PropertyValue();
                    printerDesc[0].Name = "Name";
                    printerDesc[0].Value = "cups-PDF"; 
               
                    LOGGER.warn("Set printer");
                    xPrintable.setPrinter(printerDesc); 
                    
                    PropertyValue[] printOpts = new PropertyValue[2];
                    printOpts[0] = new PropertyValue();
                    printOpts[0].Name = "Pages";
                    printOpts[0].Value = new String(pagInicioString + "-" + pagFinString);
                    printOpts[1] = new PropertyValue();
                    printOpts[1].Name = "Wait";
                    printOpts[1].Value = Boolean.TRUE;
               
                    LOGGER.warn("Imprimir...");
                    String strTemplateName = templateFilename;
                    int index1 = templateFilename.lastIndexOf("/");
                    int index2 = templateFilename.lastIndexOf(".");
                    
                    if (index1>0 && index2>0) {
                        strTemplateName = templateFilename.substring(index1+1,index2);
                    }
                    
                    LOGGER.warn("Nombre de plantilla: " + strTemplateName);
                    String strPrintfileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + strTemplateName  + ".pdf";
                    File filePrint = new File(strPrintfileName);
                    if (filePrint.exists()) {
                        filePrint.delete();
                    }
                    xPrintable.print(printOpts);
                    LOGGER.warn("Impreso el rango de páginas: " + pagInicioString + "-" + pagFinString);
    
                    //Abrir el fichero con el anuncio
                    LOGGER.warn("Impreso el fichero: " + strPrintfileName);
                    
                    //Espera a que la impresión a PDF esté disponible
                    espera(strPrintfileName);
                    
                    //Guarda el resultado en gestor documental
                    String strQuery = "WHERE NOMBRE = '" + strDocAnuncio + "'";
                    LOGGER.warn("Query: " + strQuery);
                    IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
                    it = collection.iterator();
                    int tpdoc = 0;
                    if (it.hasNext()) {
                        IItem tpd = (IItem)it.next();
                        tpdoc = tpd.getInt("ID");
                        LOGGER.warn("Documento guardado en el gestor documental (ID): " + tpdoc);
                    }
                    
                    IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
                    FileInputStream in = new FileInputStream(filePrint);
                    int docId = newdoc.getInt("ID");
                    Object connectorSession = gendocAPI.createConnectorSession();
                    LOGGER.warn("file length: " + filePrint.length());
                    LOGGER.warn("getTaskId: " + rulectx.getTaskId());
                    IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)filePrint.length(), "application/pdf", sumariosAnuncios.get(k).toString());
                    entityDoc.set(DocumentosUtil.EXTENSION, "pdf");
                    entityDoc.store(cct);
                    in.close();
                    filePrint.delete();
                }
            }
            
            //Pasos finales. Salvar y actualizar base de datos
            //------------------------------------------------
            
            //Guarda el resultado en repositorio temporal
            String fileNameODT = FileTemporaryManager.getInstance().newFileName(".odt");
            fileNameODT = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameODT;
            File fileODT = new File(fileNameODT);
            //OpenOfficeHelper.saveDocument(xComponent,"file://" + fileODT.getPath(),"");
            PropertyValue[] arguments = new PropertyValue[1];
            arguments[0] = new PropertyValue();
            arguments[0].Name = "Overwrite";
            arguments[0].Value = Boolean.TRUE;
            XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, xComponent);
            xStorable.storeAsURL("file://" + fileODT.getPath(), arguments);
            /*
            //String fileNameDOC = FileTemporaryManager.getInstance().newFileName(".doc");
            //fileNameDOC = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameDOC;
            //File fileDOC = new File(fileNameDOC);
            //OpenOfficeHelper.saveDocument(xComponent,"file://" + fileDOC.getPath(),"MS Word 97");
            */ 
            //file1.delete();
            
            //Guarda el resultado en gestor documental
            String strQuery = "WHERE NOMBRE = '" + strDocBopGeneral + "'";
            LOGGER.warn("Query: " + strQuery);
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            it = collection.iterator();
            int tpdoc = 0;
            if (it.hasNext()) {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
                LOGGER.warn("Documento guardado en el gestor documental (ID): " + tpdoc);
            }
            IItem newodt = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream inODT = new FileInputStream(fileODT);
            int odtId = newodt.getInt("ID");
            Object connectorSession = gendocAPI.createConnectorSession();
            IItem entityOdt = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), odtId, inODT, (int)fileODT.length(), "application/vnd.oasis.opendocument.text", strDocBopGeneral);
            entityOdt.set(DocumentosUtil.EXTENSION, "odt");
            entityOdt.store(cct);
            fileODT.delete();            
                        
            //Borra los documentos intermedios del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '%borrar%'";
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            while (it.hasNext()) {
                IItem doc = (IItem)it.next();
                LOGGER.warn("Borrar (ID): " + doc.getString("ID"));
                LOGGER.warn("Borrar (NUMEXP): " + doc.getString("NUMEXP"));
                LOGGER.warn("Borrar (NOMBRE): " + doc.getString("NOMBRE"));
                LOGGER.warn("Borrar (DESCRIPCION): " + doc.getString(DocumentosUtil.DESCRIPCION));
                entitiesAPI.deleteDocument(doc);
            }
            
            cct.deleteSsVariable("FECHA");
            cct.deleteSsVariable("NUM_BOP");
            cct.deleteSsVariable("NUM_PAGINA");
            cct.deleteSsVariable("NUM_ANUNCIO");

            // Meter en la base de datos el número del BOP 
            IItemCollection collection4 = null;
            Iterator<?> it4 = null;
            IItem item4 = null;
            String strQuery4 = null;

            strQuery4 = "WHERE VALOR = 'num_bop'";
            collection4 = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery4);
            it4 = collection4.iterator();
            if (it4.hasNext()){
                item4 = (IItem)it4.next();
            }
            item4.set("SUSTITUTO", numBop);
            item4.store(cct);
            LOGGER.warn("Número del BOP almacenado: " + numBop);

            // Meter en la base de datos la fecha del BOP 
            IItemCollection collection5 = null;
            Iterator<?> it5 = null;
            IItem item5 = null;
            String strQuery5 = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

            strQuery5 = "WHERE VALOR = 'fecha_ultimo_bop'";
            collection5 = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery5);
            it5 = collection5.iterator();
            if (it5.hasNext()){
                item5 = (IItem)it5.next();
            }
            item5.set("SUSTITUTO", df.format(fechaDate));
            item5.store(cct);
            LOGGER.warn("Fecha del BOP almacenada: " + fecha);

            // Meter en la base de datos el último número de anuncio 
            IItemCollection collection2 = null;
            Iterator<?> it2 = null;
            IItem item2 = null;
            String strQuery2 = null;

            strQuery2 = "WHERE VALOR = 'num_anuncio'";
            collection2 = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery2);
            it2 = collection2.iterator();
            if (it2.hasNext()){
                item2 = (IItem)it2.next();
            }
            numAnun = String.valueOf(Integer.parseInt(numAnun) - 1);
            item2.set("SUSTITUTO", numAnun);
            item2.store(cct);
            LOGGER.warn("Número de anuncio almacenado: " + numAnun);
            
            //INICIO [eCenpri-Felipe #302#40]
//            IItemCollection col = entitiesAPI.getEntities("BOP_PUBLICACION", rulectx.getNumExp());
//            IItem itemBop = (IItem) col.iterator().next();
//            itemBop.set("NUM_ULTIMO_ANUNCIO", numAnun);
//            itemBop.store(cct);
            //FIN [eCenpri-Felipe #302#40]

            // Meter en la base de datos el último número de página 
            IItemCollection collection3 = null;
            Iterator<?> it3 = null;
            IItem item3 = null;
            String strQuery3 = null;

            strQuery3 = "WHERE VALOR = 'num_pagina'";
            collection3 = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery3);
            it3 = collection3.iterator();
            if (it3.hasNext()){
                item3 = (IItem)it3.next();
            }
            int numPaginas = Integer.parseInt(numPag) + getNumPages(xComponent) - 1;
            numPag = String.valueOf(numPaginas);
            item3.set("SUSTITUTO", numPag);
            item3.store(cct);
            LOGGER.warn("Número de página almacenado: " + numPag);

            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido crear el BOP general. " + e.getMessage(),e);
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }

    private IItem getDocument(IRuleContext rulectx, IItem anuncio) throws ISPACException {
        IItem doc = null;
        try {
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            String strId = anuncio.getString("IDDOC");
            if (StringUtils.isEmpty(strId)) {
                return null;
            }
            String strQuery = "WHERE ID='"+strId+"'";
            IItemCollection docs = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            Iterator<?> it = docs.iterator();
            if (it.hasNext()) {
                doc = (IItem)it.next();
            }
            
        } catch(Exception e) {
            throw new ISPACException(e);
        }
        return doc;
    }
    
    private int getNumPages(XComponent xComponent) throws ISPACException {
        int nPages = 0;
        
        try {
            XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
            XText xText = xTextDocument.getText();
            XTextCursor xTextCursor = xText.createTextCursor();
            xTextCursor.gotoRange(xText.getEnd(),false);

            XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent); 
            XController xController = xModel.getCurrentController(); 
            XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController); 
            XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor(); 
            xViewCursor.gotoRange(xTextCursor, false);
            
            XPageCursor xPageCursor = (XPageCursor)UnoRuntime.queryInterface(XPageCursor.class, xViewCursor);
            nPages = (int)xPageCursor.getPage();
            
        } catch(Exception e) {
            throw new ISPACException(e);
        }
        return nPages;
    }
    
    
    private void espera(String fileName) {
        File file = new File(fileName);
        int intento = 0;
        
        try {
            LOGGER.warn("Esperando 1 s fijo...");
            Thread.sleep(1000);

            while (intento < 20 && file.length() == 0) {
                LOGGER.warn("Esperando 1 s...");
                Thread.sleep(1000);
                intento ++;
            }
            long fileSize = file.length();
            long prevSize = 0;
            intento = 0;
            while (intento < 20 && prevSize != fileSize) {
                LOGGER.warn("Esperando 1 s... Size="+fileSize);
                Thread.sleep(1000);
                prevSize = fileSize;
                fileSize = file.length();
                intento ++;
            }
            
        } catch (Exception e) {
            LOGGER.warn("Excepción en la espera: " + e.getMessage());
        }
    }
    
    private String getFecha(IRuleContext rulectx, Date fecha)  throws ISPACException {
        String strFecha = "";
        try {
            String pattern = "EEEE, d 'de' MMMM 'de' yyyy";
            SimpleDateFormat dateformat = new SimpleDateFormat(pattern, new Locale("es"));
            strFecha = dateformat.format(fecha);
            
        } catch(Exception e) {
            throw new ISPACException(e);
        }
        return strFecha;
    }
    
    /**
     * [eCenpri-Felipe #28]
     * Los anuncios serán a partir de ahora ODT
     * Función muy similar al concatenaODT de DipucrCommonFunctions pero sin el salto de párrafo
     * que se hace para conservar el estilo. Se deja una linea al inicio de los anuncios
     * @param xComponent
     * @param fileName
     * @throws ISPACException
     */
    public void ConcatenaAnuncio(XComponent xComponent , String fileName) throws ISPACException{
        try{
            
            XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
            XText xText = xTextDocument.getText();
            
            // create a text cursor
            XTextCursor xTextCursor = xText.createTextCursor();
            xTextCursor.gotoRange(xText.getEnd(),false);
            
            XDocumentInsertable xDocInsert = (XDocumentInsertable)UnoRuntime.queryInterface(XDocumentInsertable.class, xTextCursor);
            xDocInsert.insertDocumentFromURL(fileName, null);
            
        } catch(Exception e){
            
            throw new ISPACException(e);
        }
    }
    
}
