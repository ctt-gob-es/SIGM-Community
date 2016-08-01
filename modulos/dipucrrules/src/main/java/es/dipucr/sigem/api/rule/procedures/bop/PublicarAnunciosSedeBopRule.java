package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sede.services.bop.enviarBOPSedeWS.AnuncioBOP;
import es.dipucr.sede.services.bop.enviarBOPSedeWS.EnviarBOPSedeWSProxy;
import es.dipucr.sede.services.bop.enviarBOPSedeWS.HeaderBOP;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * 
 * @author [eCenpri-Felipe #593 #828]
 * @date 24.01.2013
 * @propósito Publica los anuncios del BOP en la sede electrónica de la Diputación
 */
public class PublicarAnunciosSedeBopRule implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(PublicarAnunciosSedeBopRule.class);
	
	private static final String EMAIL_SUBJECT_VAR_NAME = "BOP_PUB_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "BOP_PUB_EMAIL_CONTENT";
	private static final String ENVIAR_MAILS_REMITENTE = "BOP_SEDE_ENVIAR_MAILS";
	
	private static final int COD_ERROR_SEDE_BOP = -1;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			String strQuery = null;
			IItemCollection collection = null;
			
			IItem itemDatosAnuncio = null;
			Date dFechaBop = null;
			int iNumBop = Integer.MIN_VALUE;
			int idDocumento = Integer.MIN_VALUE;
			String infopag = null;
			String sumario = null;
			int iNumAnuncio = Integer.MIN_VALUE;
			int iNumPagina = Integer.MIN_VALUE;
			String entidad = null;
			String clasificacion = null;
			String categoria = null; //[eCenpri-Felipe #917]
			String emailInteresado = null;
//			String nreg = null;
			Date freg = null;
			int idBOP = Integer.MIN_VALUE; //Identificador del BOP en la sede
			int resultWS = Integer.MIN_VALUE;
			
			StringBuffer sbError = null;
	        //----------------------------------------------------------------------------------------------
			
			//***************************************
			//Enviamos el BOP a la sede
			//***************************************
			
			//Servicio web de la sede del BOP
			EnviarBOPSedeWSProxy wsSede = new EnviarBOPSedeWSProxy();
			
			//Obtenemos los datos del boletín actual
	        strQuery = "WHERE NUMEXP='" + numexp + "'";
	        collection = entitiesAPI.queryEntities("BOP_PUBLICACION", strQuery);
	        IItem itemBop = (IItem) collection.iterator().next();
	        //Número de BOP
			iNumBop = itemBop.getInt("NUM_BOP");
			
			//Obtenemos el documento de BOP del primer trámite para obtener su id
			collection = BopUtils.getBopsFirmados(rulectx);
			IItem itemDocBop = (IItem) collection.iterator().next();
			int idDocumentoBOP = itemDocBop.getKeyInt();
			
			//Obtenemos la fecha BOP formateada
			dFechaBop = itemBop.getDate("FECHA");
			String sFechaBop = FechasUtil.getFormattedDate(dFechaBop);
			
			//Obtenemos las páginas inicial y final para obtener la diferencia
			int iPaginaInicial = itemBop.getInt("NUM_PAGINA");
			int iPaginaFinal = itemBop.getInt("NUM_ULTIMA_PAGINA");
			int iNumPaginasBop = iPaginaFinal - iPaginaInicial + 1;
			
			HeaderBOP objetoBOP = new HeaderBOP(idDocumentoBOP, sFechaBop, iNumBop, iNumPaginasBop);
			idBOP = wsSede.publicarHeaderBOPSede(objetoBOP);
			
			//Controlamos si ha habido errores al enviar el BOP
			if (idBOP == COD_ERROR_SEDE_BOP){
				sbError = new StringBuffer();
				sbError.append("Se ha producido un error al enviar el documento BOP a la sede. Consulte con su administrador.");
				sbError.append("\nDETALLE DEL BOP - idDocumento:" + idDocumento + " Fecha:" + sFechaBop
						+ " Num.BOP:" + iNumBop + " Num.páginas:" + iNumPaginasBop);
				throw new ISPACRuleException(sbError.toString());
			}
			
			//***************************************
			//Enviamos la lista de anuncios a la sede
			//***************************************
						
			//Obtenemos los documentos generados y sus datos de la solicitud
			//[dipucr-Felipe #1088] Modificamos la query para que use el nuevo id de documento
			strQuery = "WHERE DOC.NUMEXP = '" + numexp + "' "
					+ "AND SOL.ID_DOCUMENTO = DOC.ID "
					+ "AND SOL.NUMEXP = EXP.NUMEXP "
					+ "ORDER BY SOL.NUM_ANUNCIO_BOP";

			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable("BOP_SOLICITUD", "SOL");
			factory.addTable("SPAC_DT_DOCUMENTOS", "DOC");
			factory.addTable("SPAC_EXPEDIENTES", "EXP");

			CollectionDAO collectionJoin = factory.queryTableJoin(cct.getConnection(), strQuery);
			collectionJoin.disconnect();
			
			ArrayList<AnuncioBOP> listAnuncios = new ArrayList<AnuncioBOP>();
			AnuncioBOP anuncio = null;
			
			ArrayList<Map<String, String>> listEmails = new ArrayList<Map<String,String>>();

			//Controlamos si se deben enviar los correos
			String sEnviarMails = ConfigurationMgr.getVarGlobal(cct, ENVIAR_MAILS_REMITENTE);
			boolean bEnviarMails = (StringUtils.isNotEmpty(sEnviarMails) && sEnviarMails.equals(Constants.VALIDACION.SI));
			
			//Recorremos los registros recuperados
			while (collectionJoin.next()){
				
				itemDatosAnuncio = (IItem) collectionJoin.value();
				
				idDocumento = itemDatosAnuncio.getInt("DOC:ID");
				infopag = itemDatosAnuncio.getString("DOC:INFOPAG");
				
				sumario = itemDatosAnuncio.getString("SOL:SUMARIO");
				iNumAnuncio = itemDatosAnuncio.getInt("SOL:NUM_ANUNCIO_BOP");
				iNumPagina = itemDatosAnuncio.getInt("SOL:NUM_PAGINA");
				entidad = itemDatosAnuncio.getString("SOL:ENTIDAD");
				clasificacion = itemDatosAnuncio.getString("SOL:CLASIFICACION");
				categoria = itemDatosAnuncio.getString("SOL:CATEGORIA"); //[eCenpri-Felipe #917]
				
				anuncio = new AnuncioBOP(idDocumento, idBOP, sumario, iNumAnuncio, iNumPagina, clasificacion, entidad, categoria);
				listAnuncios.add(anuncio);
				
				if (bEnviarMails){
					//Correo al responsable
					emailInteresado = itemDatosAnuncio.getString("EXP:DIRECCIONTELEMATICA");
					
					if (StringUtils.isNotEmpty(emailInteresado)){
	//					nreg = itemDatosAnuncio.getString("EXP:NREG");
						freg = itemDatosAnuncio.getDate("EXP:FREG");
						
						Map<String,String> variables = new HashMap<String,String>();
						variables.put("EMAIL", emailInteresado);
						variables.put("INFOPAG", infopag);
						variables.put("NUM_BOP", String.valueOf(iNumBop));
						variables.put("FECHA_BOP", FechasUtil.getFormattedDate(dFechaBop, "d 'de' MMMM 'de' yyyy"));
						variables.put("NUM_ANUNCIO", String.valueOf(iNumAnuncio));
						variables.put("SUMARIO", sumario);
						variables.put("FREG", FechasUtil.getFormattedDate(freg, "d 'de' MMMM 'de' yyyy"));
						
						listEmails.add(variables);
					}
				}
			}
			
			AnuncioBOP[] arrAnuncios = new AnuncioBOP[listAnuncios.size()];
			resultWS = wsSede.publicarAnunciosBOPSede(listAnuncios.toArray(arrAnuncios));
			
			//Controlamos si ha habido errores al enviar el BOP
			if (resultWS == COD_ERROR_SEDE_BOP){
				sbError = new StringBuffer();
				sbError.append("Se ha producido un error al enviar los anuncios del BOP a la sede. Consulte con su administrador.");
				sbError.append("\nQUERY: " + strQuery);
				sbError.append("Número de anuncios enviados: " + listAnuncios.size());
				throw new ISPACRuleException(sbError.toString());
			}
			
			
			//***************************************
			//Si todo ha ido bien, enviamos los correos a los usuarios
			//***************************************
			if (bEnviarMails){
				Map<String,String> variables = null;
				File fileAnuncio = null;
				String nombreAnuncio = null;
				for (int i = 0; i < listEmails.size(); i++){
					variables = listEmails.get(i);
					
					//Obtenemos el fichero a partir del infopag
					fileAnuncio = DocumentosUtil.getFile(cct, variables.get("INFOPAG"), null, null);
					nombreAnuncio = "Anuncio número " + variables.get("NUM_ANUNCIO") + "." + Constants._EXTENSION_PDF;
					
					//Envío del correo
					MailUtil.enviarCorreoConAcusesYVariables(rulectx, variables.get("EMAIL"), EMAIL_SUBJECT_VAR_NAME, 
							EMAIL_CONTENT_VAR_NAME, variables, fileAnuncio, nombreAnuncio, entidad, false);
				}
			}
			
		}
		catch(Exception e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
