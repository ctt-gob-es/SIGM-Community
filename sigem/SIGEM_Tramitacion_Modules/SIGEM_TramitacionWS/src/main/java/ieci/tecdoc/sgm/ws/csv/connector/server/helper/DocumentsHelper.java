package ieci.tecdoc.sgm.ws.csv.connector.server.helper;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class DocumentsHelper {

	/**
	 * Comprobar si existe un documento que tenga asignado el CSV recibido.
	 *
	 * @param context
	 *            Contexto de tramitación.
	 * @param csv
	 *            CSV del documento
	 * @return Cierto si existe un documento con el CSV asignado en su Código de
	 *         Cotejo, en caso contrario, retorna falso.
	 * @throws ISPACException
	 *             Si se produce algún error.
	 */
	public static boolean existeDocumento(IClientContext context, String csv)
			throws ISPACException {

		boolean existe = false;

		// Consulta
		String sqlQuery = "WHERE COD_COTEJO = '" + DBUtil.replaceQuotes(csv)
				+ "'";
		
		//INICIO [dipucr-Felipe #204] Recuperar documentos del histórico
//		int count = context.getAPI().getEntitiesAPI().countEntities(
//				SpacEntities.SPAC_DT_DOCUMENTOS, sqlQuery);
		int count = DocumentosUtil.queryDocumentos(context, sqlQuery).toList().size();
		//FIN [dipucr-Felipe #204]
		
		if (count > 0) {
			existe = true;
		}

		return existe;
	}
	
	/**
	 * Comprobar si existe un documento que tenga asignado el CSV recibido y valor en el campo INFOPAG_RDE_ORIGINAL.
	 *
	 * @param context
	 *            Contexto de tramitación.
	 * @param csv
	 *            CSV del documento
	 * @return Cierto si existe un documento con el CSV asignado en su Código de
	 *         Cotejo, en caso contrario, retorna falso.
	 * @throws ISPACException
	 *             Si se produce algún error.
	 */
	public static boolean existeDocumentoOriginal(IClientContext context, String csv) throws ISPACException {

		boolean existe = false;

		// Consulta
		String sqlQuery = "WHERE COD_COTEJO = '" + DBUtil.replaceQuotes(csv) + "'";
		IItemCollection documents = DocumentosUtil.queryDocumentos(context, sqlQuery);
		
		if (documents.next()) {

			IItem document = documents.value();

			// Obtener el identificador del documento original en el repositorio
			String guid = document.getString("INFOPAG_RDE_ORIGINAL");

			existe = StringUtils.isNotEmpty(guid);
		}

		return existe;
	}

	/**
	 * Obtener el contenido del documento que tenga asignado el CSV recibido.
	 *
	 * @param context
	 *            Contexto de tramitación.
	 * @param csv
	 *            CSV del documento
	 * @return Contenido del documento.
	 * @throws ISPACException
	 *             Si se produce algún error.
	 */
	public static byte[] getContenidoDocumento(IClientContext context,
			String csv) throws ISPACException {

		// Contenido del documento
		byte[] content = null;

		// Consulta
		String sqlQuery = "WHERE COD_COTEJO = '" + DBUtil.replaceQuotes(csv)
				+ "'";
		//INICIO [dipucr-Felipe #204] Recuperar documentos del histórico
//		IItemCollection documents = context.getAPI().getEntitiesAPI()
//				.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, sqlQuery);
		IItemCollection documents = DocumentosUtil.queryDocumentos(context, sqlQuery);
		//FIN [dipucr-Felipe #204] Recuperar documentos del histórico
		
		if (documents.next()) {

			IItem document = documents.value();

			// Obtener el identificador del documento en el repositorio
			String guid = document.getString("INFOPAG_RDE");
			if (StringUtils.isBlank(guid)) {
				guid = document.getString("INFOPAG");
			}

			// Obtener el contenido del documento
			content = ieci.tdw.ispac.services.helpers.DocumentsHelper
					.getContenidoDocumento(context, guid);
		}

		return content;
	}
	
	/**
	 * Obtener el contenido del documento original que tenga asignado el CSV recibido.
	 *
	 * @param context
	 *            Contexto de tramitación.
	 * @param csv
	 *            CSV del documento
	 * @return Contenido del documento.
	 * @throws ISPACException
	 *             Si se produce algún error.
	 */
	public static byte[] getContenidoDocumentoOriginal(IClientContext context, String csv) throws ISPACException {

		// Contenido del documento
		byte[] content = null;

		// Consulta
		String sqlQuery = "WHERE COD_COTEJO = '" + DBUtil.replaceQuotes(csv) + "'";
		IItemCollection documents = DocumentosUtil.queryDocumentos(context, sqlQuery);
		
		if (documents.next()) {

			IItem document = documents.value();

			// Obtener el identificador del documento original en el repositorio
			String guid = StringUtils.defaultString(document.getString("INFOPAG_RDE_ORIGINAL"));
			
			// Obtener el contenido del documento
			content = ieci.tdw.ispac.services.helpers.DocumentsHelper.getContenidoDocumento(context, guid);
		}

		return content;
	}

}
