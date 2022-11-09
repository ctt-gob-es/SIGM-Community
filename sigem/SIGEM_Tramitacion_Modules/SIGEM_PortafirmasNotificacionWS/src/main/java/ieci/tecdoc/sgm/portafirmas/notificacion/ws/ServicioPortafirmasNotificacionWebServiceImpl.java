package ieci.tecdoc.sgm.portafirmas.notificacion.ws;

import ieci.tdw.ispac.audit.business.vo.AuditContext;
import ieci.tdw.ispac.audit.context.AuditContextHolder;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.ConstantesServicios;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.tramitacion.ServicioTramitacion;
import ieci.tecdoc.sgm.tram.sign.PortafirmasMinhapProcessSignConnector;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;

import juntadeandalucia.cice.pfirma.advice.v2_0.AdviceService;
import juntadeandalucia.cice.pfirma.advice.v2_0.PfirmaException;
import juntadeandalucia.cice.pfirma.type.v2.Authentication;
import juntadeandalucia.cice.pfirma.type.v2.Comment;
import juntadeandalucia.cice.pfirma.type.v2.Request;
import juntadeandalucia.cice.pfirma.type.v2.SignLine;
import juntadeandalucia.cice.pfirma.type.v2.Signature;
import juntadeandalucia.cice.pfirma.type.v2.Signer;
import juntadeandalucia.cice.pfirma.type.v2.User;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServicioPortafirmasNotificacionWebServiceImpl
		implements AdviceService {

	private static final Logger logger = Logger.getLogger(ServicioPortafirmasNotificacionWebServiceImpl.class);

	/** Nombre del servicio. */
	private static final String SERVICE_NAME = ConstantesServicios.SERVICE_PROCESSING;

	/**
    * Primer firmante
    */
	public static final String SIGN_TYPE_FIRSTSIGNER = "PRIMER FIRMANTE";

	/**
    * Estado aceptado
    */
	public static final String ACEPTED_STATUS = "ACEPTADO";
	
	/**
    * Estado rechazado
    */
	public static final String REJECTED_STATUS = "RECHAZADO";
	
	/**
	 * [dipucr-Felipe #1246] Estados de Portafirmas
	 */
	public static final String SIGNED_STATUS = "FIRMADO";
	public static final String RETURNED_STATUS = "DEVUELTO";

	/**
    * Permite obtener el servicio de tramitacion
    * @return servicio de tramitacion
    * @throws SOAPException
    * @throws SigemException
    */
	private ServicioTramitacion getServicioTramitacion() throws SOAPException, SigemException {
		StringBuffer sbImpl = new StringBuffer(SERVICE_NAME).append(".").append("SIGEM.API");
		return LocalizadorServicios.getServicioTramitacion(sbImpl.toString());
	}

	/**
    * Comprueba si una peticion esta finalizada. Una peticion esta finalizada en dos casos:
    *
    * - Si es primer firmante: uno de los firmantes ha firmado
    * - Si es paralela o en cascada: han firmado todos los firmantes
    *
    * @param request peticion a comprobar
    * @return booleano indicando si la peticion esta finalizada
    */
	private boolean checkFinishedRequest(Request request){

		boolean ret = false;
		String reference = "Referencia " + request.getReference().getValue() + " ,";

		if (ACEPTED_STATUS.equals(request.getRequestStatus().getValue().toString())){
			ret = true;
			logger.debug(reference + " " + ACEPTED_STATUS + " true");
		}

		if ((request.getSignLineList())!=null&&(request.getSignLineList().getValue()!=null)){
			if (SIGN_TYPE_FIRSTSIGNER.equals(request.getSignType().getValue().value())){
				for (SignLine signLine:request.getSignLineList().getValue().getSignLine()){
					Signer signer = signLine.getSignerList().getValue().getSigner().get(0);
					if (SIGNED_STATUS.equals(signer.getState().getValue().getIdentifier().getValue())){
						ret = true;
						logger.debug(reference + SIGN_TYPE_FIRSTSIGNER + " " + SIGNED_STATUS + " true");
						break;
					}
				}
			} else {
				ret = true;
				for (SignLine signLine:request.getSignLineList().getValue().getSignLine()){
					Signer signer = signLine.getSignerList().getValue().getSigner().get(0);
					if (!SIGNED_STATUS.equals(signer.getState().getValue().getIdentifier().getValue())){
						ret = false;
						logger.debug(reference + SIGNED_STATUS + " false");
						break;
					}
				}
			}
		}
		
		logger.debug(reference + ret);
		return ret;
	}
	
	/**
	* [dipucr-Felipe #1246] 
    * Comprueba si una peticion esta rechazada.
    * Una peticion esta finalizada si cualquiera de los firmantes ha rechazado
    *
    * @param request peticion a comprobar
    * @return booleano indicando si la peticion esta finalizada
    */
	private boolean checkRejectedRequest(Request request){
		
		boolean ret = false;
		String reference = "Referencia " + request.getReference().getValue() + " ,";

		if (REJECTED_STATUS.equals(request.getRequestStatus().getValue().toString())){
			ret = true;
			logger.debug(reference + REJECTED_STATUS);
		}
		else if ((request.getSignLineList())!=null&&(request.getSignLineList().getValue()!=null)){
			for (SignLine signLine:request.getSignLineList().getValue().getSignLine()){
				Signer signer = signLine.getSignerList().getValue().getSigner().get(0);
				if (RETURNED_STATUS.equals(signer.getState().getValue().getIdentifier().getValue())){
					ret = true;
					logger.debug(reference + REJECTED_STATUS + "  true");
					break;
				}
			}
		}
		
		return ret;
	}

	/**
    * {@inheritDoc}
    * @see juntadeandalucia.cice.pfirma.advice.v2_0.AdviceService#updateRequestStatus(juntadeandalucia.cice.pfirma.type.v2.Authentication, juntadeandalucia.cice.pfirma.type.v2.Request, java.util.List)
    */
	public Boolean updateRequestStatus(Authentication authentication,
			Request request, List<Signature> signature) throws PfirmaException {

		if (logger.isInfoEnabled()){
			logger.info("ServicioPortafirmasNotificacionWebServiceImpl.updateRequestStatus - begin - id: " + request.getIdentifier().getValue());
		}

		//INICIO [dipucr-Felipe #1246]		
		String reference = request.getReference().getValue();
		String referenceLogs = "Referencia " + reference + " ,";
		logger.warn("Se va a procesar el documento " + reference);
		
		boolean bFirmada = checkFinishedRequest(request);
		logger.warn(referenceLogs + " Petición firmada?: "+bFirmada);
		boolean bRechazada = checkRejectedRequest(request);
		logger.warn(referenceLogs + " Petición rechazada?:" + bRechazada);
		
		if (bFirmada || bRechazada){//FIN [dipucr-Felipe #1246]

			if (logger.isInfoEnabled()){
				logger.info("ServicioPortafirmasNotificacionWebServiceImpl.updateRequestStatus - finished request - referencia:" + referenceLogs +" id: " + request.getIdentifier().getValue());
			}

			logger.debug(referenceLogs);
			
			String [] referenceValues = reference.split(PortafirmasMinhapProcessSignConnector.REFERENCE_SEPARATOR);
			String numExp = referenceValues[0];
			logger.debug(referenceLogs + " NumExp" + numExp);

			String documentId = referenceValues[1];
			logger.debug(referenceLogs + " DocumentId: " + documentId);
			
			String entidadId = referenceValues[2];
			logger.debug(referenceLogs + " EntidadId: " + entidadId);

			HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

			try {
				setAuditContext(servletRequest);
				ServicioTramitacion servicioTramitacion = getServicioTramitacion();
				//INICIO [dipucr-Felipe #1246]
				if (bFirmada){
					logger.debug(referenceLogs + " Vamos a recibir el documento firmado");
					servicioTramitacion.recibirDocumentoFirmado(entidadId, numExp, documentId);
					logger.debug(referenceLogs + " Tenemos el documento firmado");
				}
				else if (bRechazada){
					logger.debug(referenceLogs + " Vamos a procesar la firma rechazada");

					List<Comment> listComments = request.getCommentList().getValue().getComment();
					
					User user = null;
					String motivo = "";
					logger.debug(referenceLogs + " Hay motivo de rechazo? " + listComments.size());
					
					if (listComments.size() > 0){//Hay motivo de rechazo
						Comment comment = listComments.get(listComments.size() - 1);//último
						user = (User) comment.getUser();
						logger.debug(referenceLogs + " User: " + user.getName().getValue());
						motivo = comment.getTextComment();
						logger.debug(referenceLogs + " Motivo de rechazo: " + motivo);
					}
					else{
						for (SignLine signLine:request.getSignLineList().getValue().getSignLine()){
							Signer signer = signLine.getSignerList().getValue().getSigner().get(0);

							logger.debug(referenceLogs + " Signer: " + signer);

							if (RETURNED_STATUS.equals(signer.getState().getValue().getIdentifier().getValue())){
								user = (User) signer.getUserJob().getValue();
								logger.debug(referenceLogs + " User: " + user.getName().getValue());

								break;
							}
						}
					}
					
					PortafirmasMinhapProcessSignConnector processSignConnector = new PortafirmasMinhapProcessSignConnector(entidadId);
					
					String idUser = "";
					String nombre = "";
					String ape1 = "";
					String ape2 = "";
					
					if(user != null){
						if(user.getIdentifier() != null){
							idUser = StringUtils.defaultString(user.getIdentifier().getValue());
						}					
						if(user.getName() != null){
							nombre = StringUtils.defaultString(user.getName().getValue());
						}
						if(user.getSurname1() != null){
							ape1 = StringUtils.defaultString(user.getSurname1().getValue());
						}
						if(user.getSurname2() != null){
							ape2 = StringUtils.defaultString(user.getSurname2().getValue());
						}
					}
					String nombreCompleto = processSignConnector.getUserCompleteName(nombre, ape1, ape2);
					
					logger.debug(referenceLogs + " Usuario que rechaza: " + idUser + ": " + nombre + ", " + ape1 + ", " + ape2 + ". " + nombreCompleto);
					
					logger.debug(referenceLogs + " Se va a rechazar el documento");
					servicioTramitacion.rechazarDocumento(entidadId, numExp, documentId, motivo, idUser, nombreCompleto);
					logger.debug(referenceLogs + " Documento rechazado");
				}
				//FIN [dipucr-Felipe #1246]
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return Boolean.FALSE;
			}
		}

		if (logger.isInfoEnabled()){
			logger.info("ServicioPortafirmasNotificacionWebServiceImpl.updateRequestStatus - end referencia:" + referenceLogs +" id:" + request.getIdentifier().getValue());		}

		return Boolean.TRUE;
	}

	/**
    * Establecer el contexto de auditoria para la tramitacion de expedientes.
    *
    * @param request
    */
	private void setAuditContext(HttpServletRequest request) {

		// Auditoria
		AuditContext auditContext = new AuditContext();

		auditContext.setUserHost(request.getRemoteHost());
		auditContext.setUserIP(request.getRemoteAddr());
		auditContext.setUser("PORTAFIRMAS_NOTIFICACION_WS");
		auditContext.setUserId("SYSTEM");

		// Aniadir en el ThreadLocal el objeto AuditContext
		AuditContextHolder.setAuditContext(auditContext);
	}
}
