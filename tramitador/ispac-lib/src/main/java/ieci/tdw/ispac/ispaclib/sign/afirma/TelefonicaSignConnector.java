package ieci.tdw.ispac.ispaclib.sign.afirma;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

/**
 */
public class TelefonicaSignConnector{
	
	private static final Logger LOGGER = Logger.getLogger(TelefonicaSignConnector.class);

	
	/**
	 * Constructor.
	 * 
	 */
	public TelefonicaSignConnector() {
		super();
	}

	
	
	/**
	 * Comprobamos si es el caso específico de la firma del presidente de un Decreto.
	 * De ser así se actualizan los campos específicos del decreto anio, fecha_decreto y numero_decreto
	 * @throws ISPACException
	 */
	public static String esFirmaPresidente(SignDocument signDocument, IClientContext clientContext) throws ISPACException {
		
		String numExp = signDocument.getNumExp();
		int docId = signDocument.getItemDoc().getInt("ID");
		
		IEntitiesAPI entitiesAPI = clientContext.getAPI().getEntitiesAPI();
		
		String sqlQuery = "WHERE NUMEXP = '"+numExp+"'";
		IItemCollection expedientes = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", sqlQuery);
		IItem expediente = null;
		
		
		if(expedientes.next()){
			expediente = (IItem)expedientes.toList().get(0);
			// Si el estado es Esperando firmas y traslado
			if (expediente.getString("ESTADOADM") != null && expediente.getString("ESTADOADM").equalsIgnoreCase("EF")){
				// Comprobar que hay circuito de firmas para el documento, y que son dos pasos (presidente y fedatario)
				//sqlQuery = "WHERE ID_DOCUMENTO = "+docId+" ORDER BY ID_PASO ASC";
				// Entidad Decreto
				IItemCollection decretos = entitiesAPI.getEntities("SGD_DECRETO", numExp);
				// Si aún no se ha creado la entidad Decreto, aunque esto nunca debe de suceder
				IItem decreto=null;
				if(decretos==null || decretos.toList().size()==0){
					decreto = entitiesAPI.createEntity("SGD_DECRETO","");
					decreto.set("NUMEXP", numExp);
				}else if (decretos!=null && decretos.toList().size()==1){
					// Sólo hay un registro para la entidad Decreto
					decreto = (IItem)decretos.iterator().next();
				}else {
			        throw new ISPACRuleException("Error al seleccionar el registro Decreto");
				}
				
				//***************************
				//Inicio [eCenpri-Felipe #244]
				//Dependiendo si es Presidente o Fedatario escribimos sus datos en el Decreto
				GregorianCalendar calendar = new GregorianCalendar();
				Date fecha = calendar.getTime();
				sqlQuery = "WHERE ID_DOCUMENTO = "+docId+" ORDER BY ID_PASO ASC";
				IItemCollection ctosFirmaCollection = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA", sqlQuery);
				// Comprobar que hay circuito de firmas para el documento, y que son dos pasos (presidente y fedatario)
				if(ctosFirmaCollection.next() && ctosFirmaCollection.toList().size()==2){	
					
					IItem cto_firma_presidente = (IItem)ctosFirmaCollection.toList().get(0);
					IItem cto_firma_fedatario = (IItem)ctosFirmaCollection.toList().get(1);
					
					//Si el presidente todavía no ha firmado, se actualiza el presidente
					//Si aparece ay como firmado, se actualiza el fedatario
					if(cto_firma_presidente!=null && cto_firma_presidente.getInt("ESTADO") != 2){
						String nombreFirmante = cto_firma_presidente.getString("NOMBRE_FIRMANTE");
						decreto.set("NOMBRE_PRESIDENTE", nombreFirmante);
						decreto.set("FECHA_PRESIDENTE", fecha);
					}
					else{
						String nombreFirmante = cto_firma_fedatario.getString("NOMBRE_FIRMANTE");
						decreto.set("NOMBRE_FEDATARIO", nombreFirmante);
						decreto.set("FECHA_FEDATARIO", fecha);
					}
					decreto.store(clientContext);
				}
				//Fin [eCenpri-Felipe #244]
				//*************************
				
				// Si aún no tiene asignado NUMERO_DECRETO es que aún no ha firmado el presidente
				if (decreto.getInt("NUMERO_DECRETO")<=0){
					// Comprobamos el estado de firma del presidente: 0: Sin solicitar firma, 1: Solicitado, 2: Firmado
						GregorianCalendar gc = new GregorianCalendar();
						// Con el año, ya podemos calcular el número de decreto
						// Sólo si no se ha introducido anteriormente número de año, introducir año + numDecreto + CodCotejo
							int anio = 0;
							int numDecreto = 0;
							anio = gc.get(Calendar.YEAR);
							decreto.set("ANIO", anio);
							Date fechaDecreto = gc.getTime();
							decreto.set("FECHA_DECRETO", fechaDecreto);
							String sqlQueryDecreto = "WHERE ANIO = "+anio+" ORDER BY NUMERO_DECRETO DESC";
							IItemCollection decretosInAnio = entitiesAPI.queryEntities("SGD_DECRETO", sqlQueryDecreto);
							if (decretosInAnio==null || decretosInAnio.toList().size()==0){
								numDecreto=1;
							}else{
								numDecreto = ((IItem)decretosInAnio.iterator().next()).getInt("NUMERO_DECRETO")+1;
							}
							if (numDecreto>0)decreto.set("NUMERO_DECRETO", numDecreto);
							decreto.store(clientContext);
							String sNumDecreto = anio+"/"+numDecreto;
							return sNumDecreto;
				}
			}
		}	
		
		return null;
		
	}
	
	
	/**
	 * 
	 * @param x09Cert Certificado
	 * @return El campo DN del certificado correspondiente al firmante
	 * @throws CertificateException 
	 */
	public static String getNombreFirmanteFromCertificado (String x09Cert) throws CertificateException{
		String firmante="";
		String issuer="";
		if(StringUtils.isNotBlank(x09Cert)){
			String x509CertString = "-----BEGIN CERTIFICATE-----\n" + x09Cert+ "\n-----END CERTIFICATE-----";
			ByteArrayInputStream bais = new ByteArrayInputStream((byte[])x509CertString.getBytes());
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate x509cer = (X509Certificate)cf.generateCertificate(bais);
			
			firmante = x509cer.getSubjectDN().getName();
		    issuer = x509cer.getIssuerDN().getName();

		    if(StringUtils.isNotBlank(firmante)){
		        //Emisor FNMT
		        if (issuer.indexOf("FNMT")>0){
		    		int posNombre = firmante.indexOf("NOMBRE");
		    	    int posGuion = firmante.indexOf("-");

		    		if(posNombre>0 && posGuion>0) {
		    			firmante = firmante.substring(posNombre + 7, posGuion -1);
		    		} else {
		    			firmante = firmante.substring(firmante.indexOf("=") + 1);
		    		}
		    	//Emisor DNIE
		        }else if (issuer.indexOf("DNIE")>0){
		    		int posParentesis = firmante.indexOf("(");
		    					
		    		if(posParentesis>0) {
		    			firmante = firmante.substring(firmante.indexOf("=") + 2, posParentesis -1);
		    		} else {
		    			firmante = firmante.substring(firmante.indexOf("=") + 2);
		    		}
		        //Emisor otros
		        }else{
		    		int posNombre = firmante.indexOf("NOMBRE");
		    	    int posGuion = firmante.indexOf("-");
		    					
		    		if(posNombre>0 && posGuion>0) {
		    			firmante = firmante.substring(posNombre + 7, posGuion -1);
		    		} else {
		    			int posComa = firmante.indexOf(",");
		    						
		    			if(posComa>0) {
		    				firmante = firmante.substring(firmante.indexOf("=") + 1, posComa);
		    			} else {
		    				firmante = firmante.substring(firmante.indexOf("=") + 1);
		    			}
		    		}
		        }
			}
		}
		
		return firmante;
	}
	
	
	/**
	 * 
	 * @param String codCotejo
	 * @return El campo codCotejo separado cada cinco caracteres por un espacio en blanco
	 *  
	 */
	public static String getCodCotejoBlancos (String codCotejo){
		
		String codCotejoBlancos = codCotejo.substring(0, 5) + " "  + codCotejo.substring(5, 10) + " "
								+ codCotejo.substring(10, 15) + " "  + codCotejo.substring(15, 20); 
		
		return codCotejoBlancos;
	}
	
	
	/**
	 * 
	 * @param SignDocument signDocument
	 * @return True: El documento es de "Decreto" 
	 *  
	 */
	public static boolean isDocDecreto (SignDocument signDocument){
		
		String nombreDoc = "";
		try {
			nombreDoc = signDocument.getItemDoc().getString("NOMBRE");
		} catch (ISPACException e) {
			LOGGER.error("ERROR. " + e.getMessage(), e);
			return false;
		}
		
		if (nombreDoc.equals("Decreto")){
			return true;
		}
		
		return false;
		
		
	}
	
    
	
}
