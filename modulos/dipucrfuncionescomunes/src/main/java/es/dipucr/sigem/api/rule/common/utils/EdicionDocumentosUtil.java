package es.dipucr.sigem.api.rule.common.utils;

import java.util.Random;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import org.apache.log4j.Logger;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;

public class EdicionDocumentosUtil {

	private static final Logger logger = Logger.getLogger(DocumentosUtil.class);
	
	/**
	 * Inserta un salto de página en un fichero Word abierto previamente en OpenOffice.
	 *
	 * @param xComponent Fichero abierto en OpenOffice.
	 * @throws ISPACException Debido a errores en la API de SIGEM.
	 */	
	public static void insertaSaltoDePagina(XComponent xComponent) throws ISPACException{
		//No se puede insertar un salto de página directamente.
		//Los saltos de página son una propiedad de los párrafos
		//Así que inserto un nuevo párrafo y luego le ajusto la propiedad
		
		try{
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextCursor xTextCursor = xText.createTextCursor();
		    xTextCursor.gotoRange(xText.getEnd(),false);

		    // Inserto un salto de línea (nuevo párrafo)
			xText.insertControlCharacter(xTextCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);

			//Le digo a este nuevo párrafo que comience con salto de página
			XPropertySet xProps = (XPropertySet) UnoRuntime.queryInterface(
			XPropertySet.class, xTextCursor);
			xProps.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.PAGE_BEFORE_value));
		}
		catch(Exception e){
			logger.error("Error al insertar el salto de página. " + e.getMessage(), e);
			throw new ISPACException(e);
		}
	}
	
	/**
	 * Concatena el contenido de un fichero Word a otro ya abierto previamente en OpenOffice.
	 *
	 * @param xComponent Fichero abierto en OpenOffice.
	 * @param file Path del fichero a concatenar.
	 * @param ooHelper Variable privada de clase OpenOfficeHelper.
	 * @throws ISPACException Debido a errores en la API de SIGEM.
	 */	
	public static void Concatena(XComponent xComponent , String file, OpenOfficeHelper ooHelper)throws ISPACException
	{
		// La función ooHelper.concatFiles mete saltos de página cuando quiere. 
		// Yo quiero concatenar a continuación así que no la puedo usar y tengo
		// que hacer un apaño a mano. Más info en:
		//      http://www.oooforum.org/forum/viewtopic.phtml?t=28376
		//ooHelper.concatFiles(xComponent, file);
		try
		{
			//Obtengo el texto del documento y muevo el cursor al final del mismo
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextCursor xTextCursor = xText.createTextCursor();
		    xTextCursor.gotoRange(xText.getEnd(),false);

		    //Marco el comienzo del nuevo documento con un bookmark
		    //Para anclar el bookmark uso un caracter, por ejemplo 'X'
		    xText.insertString(xTextCursor, "X", false);
		    xTextCursor.goLeft((short)1, false);
		    Random rand = new Random(); //No hace falta pero así aseguro que no se repiten bookmarks
		    String bookmarkName = "dummyBookmark" + String.valueOf(rand.nextInt());
		    insertBookmark(xTextDocument, xTextCursor, bookmarkName);
		    xTextCursor.gotoRange(xText.getEnd(),false);
		    
		    //Inserto el documento. Esto añadirá un salto de página
		    XDocumentInsertable xDocInsert = (XDocumentInsertable)UnoRuntime.queryInterface(XDocumentInsertable.class, xTextCursor);
			xDocInsert.insertDocumentFromURL(file, null);
			//Esperamos un tiempo a que concluya la operación
			//para evitar problemas de sincronización.
			//Thread.sleep(3000);
			
			//Recupero el bookmark y tomo su ancla, que se sitúa sobre el caracter 'X'
			XBookmarksSupplier xBookmarksSupplier = (XBookmarksSupplier)UnoRuntime.queryInterface(XBookmarksSupplier.class, xComponent);
			XNameAccess xNamedBookmarks = xBookmarksSupplier.getBookmarks();
			Object foundBookmark = xNamedBookmarks.getByName(bookmarkName);
			XTextContent xFoundBookmark = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, foundBookmark);
			XTextRange xAncla = xFoundBookmark.getAnchor();
			
			//Coloco el ViewCursor en el ancla
			XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent); 
			XController xController = xModel.getCurrentController(); 
			XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController); 
			XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor(); 
			xViewCursor.gotoRange(xAncla, false);
			
		    //Comienzo el nuevo documento en una línea nueva
			//Meto el salto de linea entre el salto de sección y el principio de la nueva sección
			//porque de este modo protejo la hoja de estilos del primer párrafo del documento
			xViewCursor.goRight((short)2,false);
			xText.insertControlCharacter(xViewCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
			xViewCursor.goLeft((short)3,false);
			
			//Pulso 'delete' por medio de un dispatcher para borrar el salto de página
			//El primer delete borra la 'X' y con ello el bookmark
			//El segundo delete borra el salto de página
			//(el método deletePageBreak en realidad no tiene nada que ver con saltos de página)
			//ooHelper.deletePageBreak(xComponent, xViewCursor);
			//ooHelper.deletePageBreak(xComponent, xViewCursor);
			xViewCursor.goRight((short)2,true);
			xViewCursor.setString("");
//			logger.warn("************ Insertar J.");
//			xText.insertString(xViewCursor, "J", false);
			
			//Devuelvo el viewcursor al final del documento
			xTextCursor.gotoEnd(false);
			xViewCursor.gotoRange(xTextCursor, false);
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}
	
	private static void insertBookmark(XTextDocument xTextDocument, XTextRange xTextRange, String name)throws ISPACException{
		try{
			//Creo un bookmark vacio
			XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
			Object xObject = xDocMSF.createInstance("com.sun.star.text.Bookmark");

			//Asigno el nombre al bookmark
			XNamed xNameAccess = (XNamed) UnoRuntime.queryInterface(XNamed.class, xObject);
			xNameAccess.setName(name);

			//Inserto el bookmark en el sitio indicado
			XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xNameAccess);
			xTextDocument.getText().insertTextContent(xTextRange, xTextContent, false);
		}
		catch(Exception e){
			throw new ISPACException(e);
		}
	}
	
	/**
	 * Devuelve el número de caracteres que tiene el documento
	 * 
	 * @param xComponent Fichero abierto en OpenOffice.
	 * @return Número de caracteres del documento
	 * @throws ISPACException Debido a errores en la API de SIGEM.
	 */
	public static Integer getNumeroCaracteres(XComponent xComponent) throws ISPACException{
		try{
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XPropertySet propTextDocument = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, xTextDocument);

		    logger.warn("Número de caracteres: " + propTextDocument.getPropertyValue("CharacterCount"));
//		    logger.warn("Número de palabras: " + propTextDocument.getPropertyValue("WordCount"));
		    return (Integer) propTextDocument.getPropertyValue("CharacterCount");
		}
		catch(Exception e){
			throw new ISPACException(e);
		}
	}
}
