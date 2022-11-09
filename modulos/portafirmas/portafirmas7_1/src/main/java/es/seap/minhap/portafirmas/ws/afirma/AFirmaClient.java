/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.ws.afirma;

/*import org.apache.axis.Handler;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;*/


public class AFirmaClient {
	/*
	private static final Logger logger = Logger.getLogger(AFirmaClient.class);
	   private static final String CABECERA = "[TestClient]:";
	   //Ruta donde se encuentran los ficheros de entrada a los servicios web
	   private static final String RUTA_XML_ENTRADA = "webservices.rutaXml";
	   private static final String XML_ENTRADA_PREFIX = "webservices.";
	   private static String servicio;
	   private static String end_point;
	   private static String ruta_trusted_cacerts;
	   private static String password_trusted_cacerts;
	   private static String type_trusted_cacerts;
	   private static String authorizationMethod = null;
	   private static String authorizationKeyStorePath = null;
	   private static String authorizationKeyStoreType = null;
	   private static String authorizationKeyStorePassword = null;
	   private static String authorizationKeyStoreCertAlias = null;
	   private static String authorizationKeyStoreCertPassword = null;
	   private static String authorizationName = null;
	   private static String authorizationPassword = null;
	   private static String authorizationPasswordType;

	   private static void init(Configuration config) {

		  // TODO Meter el nombre del servicio en un fichero de configuración
		  servicio = "DSSAfirmaVerify";
		  end_point = config.getString(ConstantsSigner.AFIRMA5_ENDPOINT);
		  ruta_trusted_cacerts = config.getString(ConstantsSigner.AFIRMA5_TRUSTSTORE);
		  password_trusted_cacerts = config.getString(AuthenticatorConstants.AUTHENTICATOR_AFIRMA5_TRUSTEDSTORE_PASS);
//	      type_trusted_cacerts = p.getProperty("com.trustedstore.type");
//		  type_trusted_cacerts = config.getString("com.trustedstore.type");
		  // TODO Esta es una prueba temporal
		  type_trusted_cacerts = "jks";
		  authorizationMethod = config.getString(ConstantsSigner.AFIRMA5_SECURITY_OPTION);
		  authorizationKeyStorePath = config.getString(ConstantsSigner.AFIRMA5_SECURITY_KEYSTORE);
		  authorizationKeyStoreType = config.getString(ConstantsSigner.AFIRMA5_SECURITY_KEYSTORE_TYPE);
		  authorizationKeyStorePassword = config.getString(ConstantsSigner.AFIRMA5_SECURITY_KEYSTORE_PASS);
		  authorizationKeyStoreCertAlias = config.getString(ConstantsSigner.AFIRMA5_SECURITY_CERT_ALIAS);
		  authorizationKeyStoreCertPassword = config.getString(ConstantsSigner.AFIRMA5_SECURITY_CERT_PASS);
		  authorizationName = config.getString(ConstantsSigner.AFIRMA5_SECURITY_USER);
		  authorizationPassword = config.getString(ConstantsSigner.AFIRMA5_SECURITY_PASSWORD);
		  authorizationPasswordType = config.getString(ConstantsSigner.AFIRMA5_SECURITY_PASS_TYPE);
	   }

	   private static void setSystemParameters() {
	      System.setProperty("javax.net.ssl.trustStore", ruta_trusted_cacerts);
	      System.setProperty("javax.net.ssl.trustStorePassword", password_trusted_cacerts);
	      System.setProperty("javax.net.ssl.trustStoreType", type_trusted_cacerts);
	   }

	   private static Properties generateHandlerProperties() {
	      Properties config = new Properties();
	      config.setProperty("security.mode", authorizationMethod != null? authorizationMethod : "");
	      config.setProperty("security.usertoken.user", authorizationName != null? authorizationName : "");
	      config.setProperty("security.usertoken.password", authorizationPassword != null? authorizationPassword : "");
	      if (authorizationPasswordType.equals("PasswordText")) {
	    	  config.setProperty("security.usertoken.passwordType", "Text");
	      } else {
	    	  config.setProperty("security.usertoken.passwordType", "");
	      }
	      config.setProperty("security.keystore.location", authorizationKeyStorePath != null? authorizationKeyStorePath : "");
	      config.setProperty("security.keystore.type", authorizationKeyStoreType != null? authorizationKeyStoreType : "");
	      config.setProperty("security.keystore.password", authorizationKeyStorePassword != null? authorizationKeyStorePassword : "");
	      config.setProperty("security.keystore.cert.alias", authorizationKeyStoreCertAlias != null? authorizationKeyStoreCertAlias : "");
	      config.setProperty("security.keystore.cert.password", authorizationKeyStoreCertPassword != null? authorizationKeyStoreCertPassword : "");
	      return config;
	   }

	   public void DSSAFirmaVerify(Configuration config) {
	      try {

	         init(config);
	         setSystemParameters();
	         
	       //Se configura del endponit del servicio
	         String endpoint = end_point + servicio;
	         
	         Properties clientHandlerInitProperties = generateHandlerProperties();
	         Handler reqHandler = new ClientHandler(clientHandlerInitProperties); 
	         
	         String servicioDSS=servicio;

			 //Configuracion especifica para los perfiles DSS
			 //Servicio de Validacion y Actualizacion de Firma
				if(servicio.equals("DSSAfirmaVerify"))
					servicioDSS = "verify";
			//Servicio de Firma Delegada
	         	else if(servicio.equals("DSSAfirmaSign"))
					servicioDSS = "sign";
			//Servicio de Registro de Firmas
				else if(servicio.equals("DSSAfirmaArchiveSubmit"))
					servicioDSS = "archiveSubmit";
			//Servicio de obtencion de firmas
			  	else if(servicio.equals("DSSAfirmaArchiveRetrieval"))
					servicioDSS = "archiveRetrieval";

			//Se crea el servicio
			Service  service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress( new java.net.URL(endpoint) );
			call.setOperationName(new QName("http://soapinterop.org/", servicioDSS));
//			call.setTimeout(new Integer(prop.getProperty("webservices.timeout")));
			call.setTimeout(new Integer("-1"));
	        call.setClientHandlers(reqHandler, null);
	         
	       //Configuracion de la ruta al mensaje XML de entrada al WS
	        String xmlEntrada = "validarFirma.xml";

	        if (xmlEntrada != null) {
	        	XmlAfirma xmlAfirma = new XmlAfirma();
//	        	xmlAfirma.generateRequest();
//	            String paramIn = leeFichero(xmlEntrada);
	        	String paramIn = 
	        					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
									"<dss:VerifyRequest Profile=\"urn:afirma:dss:1.0:profile:XSS\"" + 
														"xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\"" + 
														"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
														"xsi:schemaLocation=\"urn:oasis:names:tc:dss:1.0:core:schema" +
														"http://docs.oasis-open.org/dss/v1.0/oasis-dss-core-schema-v1.0-os.xsd\">" +
										"<dss:InputDocuments>" +
											"<dss:Document ID=\"1232017104073\">" +
												"<dss:Base64XML>" +
													"<![CDATA[PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48QUZJUk1BPg0KCTxDT05URU5UIElkPSJTaWduZWREYXRhRWxlbWVudC1CQ0RDQUJIQUdGRkRIMCIgZW5jb2Rpbmc9ImJhc2U2NCIgaGFzaEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI3NoYTEiIG5vbWJyZUZpY2hlcm89InNlY3VyaXR5Q29uZmlndXJhdGlvbi5wcm9wZXJ0aWVzIj5vRVdMWHp5K2pzR1kxTGpPQ3cxRGMxNG4zb1E9PC9DT05URU5UPg0KPGRzOlNpZ25hdHVyZSB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyIgSWQ9IlNpZ25hdHVyZS1CQ0RDQUJIQUdGRkRIMCI+PGRzOlNpZ25lZEluZm8gSWQ9IlNpZ25lZEluZm8tQkNEQ0FCSEFHRkZESDAiPjxkczpDYW5vbmljYWxpemF0aW9uTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvVFIvMjAwMS9SRUMteG1sLWMxNG4tMjAwMTAzMTUjV2l0aENvbW1lbnRzIi8+PGRzOlNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNyc2Etc2hhMSIvPjxkczpSZWZlcmVuY2UgSWQ9IlNpZ25lZFByb3BlcnRpZXMtUmVmZXJlbmNlLUJDRENBQkhBR0ZGREgwIiBUeXBlPSJodHRwOi8vdXJpLmV0c2kub3JnLzAxOTAzI1NpZ25lZFByb3BlcnRpZXMiIFVSST0iI1NpZ25lZFByb3BlcnRpZXMtQkNEQ0FCSEFHRkZESDAiPjxkczpUcmFuc2Zvcm1zPjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy9UUi8yMDAxL1JFQy14bWwtYzE0bi0yMDAxMDMxNSNXaXRoQ29tbWVudHMiLz48L2RzOlRyYW5zZm9ybXM+PGRzOkRpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNzaGExIi8+PGRzOkRpZ2VzdFZhbHVlPjRyV3RHcjJNR24wVE5kNjg0Z291UmhmRmhDUT08L2RzOkRpZ2VzdFZhbHVlPjwvZHM6UmVmZXJlbmNlPjxkczpSZWZlcmVuY2UgSWQ9IlNpZ25lZERhdGFFbGVtZW50LVJlZmVyZW5jZS1CQ0RDQUJIQUdGRkRIMCIgVVJJPSIjU2lnbmVkRGF0YUVsZW1lbnQtQkNEQ0FCSEFHRkZESDAiPjxkczpUcmFuc2Zvcm1zPjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biNXaXRoQ29tbWVudHMiLz48L2RzOlRyYW5zZm9ybXM+PGRzOkRpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNzaGExIi8+PGRzOkRpZ2VzdFZhbHVlPjZodG9PSTZFVVJiSFJNdmJmd1hHcWpsTGppND08L2RzOkRpZ2VzdFZhbHVlPjwvZHM6UmVmZXJlbmNlPjxkczpSZWZlcmVuY2UgVVJJPSIja2V5SW5mby1CQ0RDQUJIQUdGRkRIMCI+PGRzOlRyYW5zZm9ybXM+PGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnL1RSLzIwMDEvUkVDLXhtbC1jMTRuLTIwMDEwMzE1I1dpdGhDb21tZW50cyIvPjwvZHM6VHJhbnNmb3Jtcz48ZHM6RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI3NoYTEiLz48ZHM6RGlnZXN0VmFsdWU+RHVuaFRzVjlkSytiWUZLNTRzRWZDcDhLRjFJPTwvZHM6RGlnZXN0VmFsdWU+PC9kczpSZWZlcmVuY2U+PC9kczpTaWduZWRJbmZvPjxkczpTaWduYXR1cmVWYWx1ZSBJZD0iU2lnbmF0dXJlVmFsdWUtQkNEQ0FCSEFHRkZESDAiPmlDTDV6YkNHQzJSVVgzc3ZCZm1TT3RQQ25HZkpmLzlZVVNIYzAxb0c0MDlONzZxaDZaK3dmVFRlc2ZQYitpdDV0bHNaSnMxWld0QithQTFLcnd5M0xGa1hVRlEvQnZCQXpoZDhxOGp6Mjd5N2FPUU9TSC9zU1JJcnFjZXorK1ZzczZ2SldnMVgwZEh4YkVlNFprMFptYUt0enBiNHNLWHo2ZGlBTDBTdXczZz08L2RzOlNpZ25hdHVyZVZhbHVlPjxkczpLZXlJbmZvIElkPSJrZXlJbmZvLUJDRENBQkhBR0ZGREgwIiB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+PGRzOktleVZhbHVlPjxkczpSU0FLZXlWYWx1ZT48ZHM6TW9kdWx1cz5vUnY5T0lkRXlRT0VqS29TNm5BT09temt0Z2hrNFR6OUZILzY4bGN6YklFeHZ2ZTFidWFOUnQxU2JSbG5mSlNuVERwUWE2UFBmTWJUZjcwKy9FVE84dXFIOEhwK2FEeGtzb0FXRXJkaUx4dWR2cXl5TmRDa1VibGo1UHcwcndUZVFEVXNvVzRHMytRQ0NvSisyUE02TkV2SmRzNlVVRzZPUmtPc2p5VGxRUlU9PC9kczpNb2R1bHVzPjxkczpFeHBvbmVudD5BUUFCPC9kczpFeHBvbmVudD48L2RzOlJTQUtleVZhbHVlPjwvZHM6S2V5VmFsdWU+PGRzOlg1MDlEYXRhPjxkczpYNTA5Q2VydGlmaWNhdGU+TUlJRGpEQ0NBdldnQXdJQkFnSUJEVEFOQmdrcWhraUc5dzBCQVFzRkFEQ0J6VEVMTUFrR0ExVUVCaE1DUlZNeEVEQU9CZ05WQkFnVEIxTkZWa2xNVEVFeEVEQU9CZ05WQkFjVEIxTkZWa2xNVEVFeElqQWdCZ05WQkFvVEdWUkZURlpGVGxRZ1NVNVVSVkpCUTFSSlZrRXVJRk11UVM0eE1qQXdCZ05WQkFzVEtVUkZVRUZTVkVGTlJVNVVUeUJFUlNCQlJFMUpUa2xUVkZKQlEwbFBUa1ZUSUZCVlFreEpRMEZUTVIwd0d3WURWUVFERkJSRFFTQkVSVk5CVWxKUFRFeFBJRUJHU1ZKTlFURWpNQ0VHQ1NxR1NJYjNEUUVKQVJZVWFtRXVjbTl0WVc1QWRHVnNkbVZ1ZEM1amIyMHdIaGNOTURneE1qRXhNVGt6TVRJMFdoY05NRGt4TWpFeE1Ua3pNVEkwV2pDQm96RUxNQWtHQTFVRUJoTUNSVk14RURBT0JnTlZCQWdUQjFORlZrbE1URUV4RURBT0JnTlZCQWNUQjFORlZrbE1URUV4SERBYUJnTlZCQW9URTFSRlRGWkZUbFFnU1U1VVJWSkJRMVJKVmtFeER6QU5CZ05WQkFzV0JrQkdTVkpOUVRFY01Cb0dBMVVFQXhNVFUwVlNWa2xFVDFJZ1JFVWdVRkpWUlVKQlV6RWpNQ0VHQ1NxR1NJYjNEUUVKQVJZVWFtRXVjbTl0WVc1QWRHVnNkbVZ1ZEM1amIyMHdnWjh3RFFZSktvWklodmNOQVFFQkJRQURnWTBBTUlHSkFvR0JBS0ViL1RpSFJNa0RoSXlxRXVwd0RqcHM1TFlJWk9FOC9SUi8rdkpYTTJ5Qk1iNzN0VzdtalViZFVtMFpaM3lVcDB3NlVHdWp6M3pHMDMrOVB2eEV6dkxxaC9CNmZtZzhaTEtBRmhLM1lpOGJuYjZzc2pYUXBGRzVZK1Q4Tks4RTNrQTFMS0Z1QnQva0FncUNmdGp6T2pSTHlYYk9sRkJ1amtaRHJJOGs1VUVWQWdNQkFBR2pnYU13Z2FBd0NRWURWUjBUQkFJd0FEQWRCZ05WSFE0RUZnUVVsUkVrOUdCdTYwVjJkbDhiUkIySFRxb1p5eG93Q3dZRFZSMFBCQVFEQWdHR01CRUdDV0NHU0FHRytFSUJBUVFFQXdJQUJ6QTBCZ2xnaGtnQmh2aENBUWdFSnhZbGFIUjBjRHBjWEhSbGJIWmxiblF1WkdWellYSnliMnhzYnk1amIyMXdiMjVsYm5SbGN6QWVCZ2xnaGtnQmh2aENBUTBFRVJZUGVHTmhJR05sY25ScFptbGpZWFJsTUEwR0NTcUdTSWIzRFFFQkN3VUFBNEdCQUFOaitRS0lkK1RwSi9UdWRybGhtNiswMThLaUtNRTh5cmFFUkY5SC9IWmNDR0gzdUErazlZM0NNSWZKcENha1l2bDJ1K2RsMmdsTVdiOVRIelIwSklVcGNPRGNOeEo5OHAzNjNkR2ZiZ204dUVqNGVCTnpOWjFzOE82SFlOUlNBdSt1cVBIbEJNSFBURGVJdUpIMjZoQzZ5YnB5YVpJWVNtdEZGelE3V1drdTwvZHM6WDUwOUNlcnRpZmljYXRlPjxkczpYNTA5SXNzdWVyU2VyaWFsPjxkczpYNTA5SXNzdWVyTmFtZT5FTUFJTD1qYS5yb21hbkB0ZWx2ZW50LmNvbSxDTj1DQSBERVNBUlJPTExPIEBGSVJNQSxPVT1ERVBBUlRBTUVOVE8gREUgQURNSU5JU1RSQUNJT05FUyBQVUJMSUNBUyxPPVRFTFZFTlQgSU5URVJBQ1RJVkEuIFMuQS4sTD1TRVZJTExBLFNUPVNFVklMTEEsQz1FUzwvZHM6WDUwOUlzc3Vlck5hbWU+PGRzOlg1MDlTZXJpYWxOdW1iZXI+MTM8L2RzOlg1MDlTZXJpYWxOdW1iZXI+PC9kczpYNTA5SXNzdWVyU2VyaWFsPjwvZHM6WDUwOURhdGE+PC9kczpLZXlJbmZvPjxkczpPYmplY3Q+PHhhZGVzOlF1YWxpZnlpbmdQcm9wZXJ0aWVzIHhtbG5zOnhhZGVzPSJodHRwOi8vdXJpLmV0c2kub3JnLzAxOTAzL3YxLjMuMiMiIFRhcmdldD0iI1NpZ25hdHVyZS1CQ0RDQUJIQUdGRkRIMCI+PHhhZGVzOlNpZ25lZFByb3BlcnRpZXMgSWQ9IlNpZ25lZFByb3BlcnRpZXMtQkNEQ0FCSEFHRkZESDAiPjx4YWRlczpTaWduZWRTaWduYXR1cmVQcm9wZXJ0aWVzPjx4YWRlczpTaWduaW5nVGltZT4yMDA5LTAxLTE1VDExOjU3OjQ1KzAxOjAwPC94YWRlczpTaWduaW5nVGltZT48eGFkZXM6U2lnbmluZ0NlcnRpZmljYXRlPjx4YWRlczpDZXJ0Pjx4YWRlczpDZXJ0RGlnZXN0PjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc2hhMSIvPjxkczpEaWdlc3RWYWx1ZT4wMzlVQXdHVlZUcFFoOE9kSEVHUVh2RVk1QTQ9PC9kczpEaWdlc3RWYWx1ZT48L3hhZGVzOkNlcnREaWdlc3Q+PHhhZGVzOklzc3VlclNlcmlhbD48ZHM6WDUwOUlzc3Vlck5hbWU+RU1BSUw9amEucm9tYW5AdGVsdmVudC5jb20sQ049Q0EgREVTQVJST0xMTyBARklSTUEsT1U9REVQQVJUQU1FTlRPIERFIEFETUlOSVNUUkFDSU9ORVMgUFVCTElDQVMsTz1URUxWRU5UIElOVEVSQUNUSVZBLiBTLkEuLEw9U0VWSUxMQSxTVD1TRVZJTExBLEM9RVM8L2RzOlg1MDlJc3N1ZXJOYW1lPjxkczpYNTA5U2VyaWFsTnVtYmVyPjEzPC9kczpYNTA5U2VyaWFsTnVtYmVyPjwveGFkZXM6SXNzdWVyU2VyaWFsPjwveGFkZXM6Q2VydD48L3hhZGVzOlNpZ25pbmdDZXJ0aWZpY2F0ZT48L3hhZGVzOlNpZ25lZFNpZ25hdHVyZVByb3BlcnRpZXM+PC94YWRlczpTaWduZWRQcm9wZXJ0aWVzPjxVbnNpZ25lZFByb3BlcnRpZXMgeG1sbnM9Imh0dHA6Ly91cmkuZXRzaS5vcmcvMDE5MDMvdjEuMy4yIyI+PFVuc2lnbmVkU2lnbmF0dXJlUHJvcGVydGllcyB4bWxucz0iaHR0cDovL3VyaS5ldHNpLm9yZy8wMTkwMy92MS4zLjIjIj48U2lnbmF0dXJlVGltZVN0YW1wIElkPSJTaWduYXR1cmVUaW1lU3RhbXAiIHhtbG5zPSJodHRwOi8vdXJpLmV0c2kub3JnLzAxOTAzL3YxLjMuMiMiPjxDYW5vbmljYWxpemF0aW9uTWV0aG9kIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIiBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnL1RSLzIwMDEvUkVDLXhtbC1jMTRuLTIwMDEwMzE1Ii8+PEVuY2Fwc3VsYXRlZFRpbWVTdGFtcCBFbmNvZGluZz0iaHR0cDovL3VyaS5ldHNpLm9yZy8wMTkwMy92MS4yLjIjREVSIiB4bWxucz0iaHR0cDovL3VyaS5ldHNpLm9yZy8wMTkwMy92MS4zLjIjIj5NSUlINGdZSktvWklodmNOQVFjQ29JSUgwekNDQjg4Q0FRTXhDekFKQmdVckRnTUNHZ1VBTUdrR0N5cUdTSWIzRFFFSkVBRUVvRm9FV0RCV0FnRUJCZ2tyQm9FTm9sd0tBZ0V3SVRBSkJnVXJEZ01DR2dVQUJCUkRrU1B0ckxuNytySWR2MnlXeXNYaWE5T1F2Z0lIQkdDRFRWR3o4QmdQTWpBd09UQXhNVFV4TURVM05EVmFNQWtDQVFxQUFRR0JBUUdnZ2dSdE1JSUVhVENDQTlLZ0F3SUJBZ0lCQnpBTkJna3Foa2lHOXcwQkFRVUZBRENCelRFTE1Ba0dBMVVFQmhNQ1JWTXhFREFPQmdOVkJBZ1RCMU5GVmtsTVRFRXhFREFPQmdOVkJBY1RCMU5GVmtsTVRFRXhJakFnQmdOVkJBb1RHVlJGVEZaRlRsUWdTVTVVUlZKQlExUkpWa0V1SUZNdVFTNHhNakF3QmdOVkJBc1RLVVJGVUVGU1ZFRk5SVTVVVHlCRVJTQkJSRTFKVGtsVFZGSkJRMGxQVGtWVElGQlZRa3hKUTBGVE1SMHdHd1lEVlFRREZCUkRRU0JFUlZOQlVsSlBURXhQSUVCR1NWSk5RVEVqTUNFR0NTcUdTSWIzRFFFSkFSWVVhbUV1Y205dFlXNUFkR1ZzZG1WdWRDNWpiMjB3SGhjTk1EZ3hNRE13TURjME1qQTVXaGNOTURrd09ESTJNRGMwTWpBNVdqQ0JuakVMTUFrR0ExVUVCaE1DUlZNeEVEQU9CZ05WQkFnVEIxTkZWa2xNVEVFeEVEQU9CZ05WQkFjVEIxTkZWa2xNVEVFeEhEQWFCZ05WQkFvVEUxUkZURlpGVGxRZ1NVNVVSVkpCUTFSSlZrRXhEekFOQmdOVkJBc1dCa0JHU1ZKTlFURVhNQlVHQTFVRUF4TU9WRk5CSUVSRlUwRlNVazlNVEU4eEl6QWhCZ2txaGtpRzl3MEJDUUVXRkdwaExuSnZiV0Z1UUhSbGJIWmxiblF1WTI5dE1JR2ZNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0R05BRENCaVFLQmdRREtPQXhhYitHbnREMzJaUy9TaHRDc2tUSW94OVNrNElkZzluVkZITklSM1FNSVJmZ0o1dzhVTlVHbW1oeFdobzNJcTh6RnB1SVNyYWQva1ZFVUdRRG9yazJucDZ2UUtwYjNrNVZBaUVCb2VlTUFUMEFVOVFsczc0WVRrd3luUWdMUm9OWU00Ujk3TEEyek9VeXBKd1liQ0I4a0pLczE3SmxXM094aERENTF6UUlEQVFBQm80SUJoRENDQVlBd0RBWURWUjBUQVFIL0JBSXdBREFkQmdOVkhRNEVGZ1FVQVVCNHZjZ2ZhdnRPUEY3VEZaenRZazh4TDFjd2dmb0dBMVVkSXdTQjhqQ0I3NEFVT2xNZVFHbDBwZzBkUWVuSXZlNy9IK2ZzMmNXaGdkT2tnZEF3Z2MweEN6QUpCZ05WQkFZVEFrVlRNUkF3RGdZRFZRUUlFd2RUUlZaSlRFeEJNUkF3RGdZRFZRUUhFd2RUUlZaSlRFeEJNU0l3SUFZRFZRUUtFeGxVUlV4V1JVNVVJRWxPVkVWU1FVTlVTVlpCTGlCVExrRXVNVEl3TUFZRFZRUUxFeWxFUlZCQlVsUkJUVVZPVkU4Z1JFVWdRVVJOU1U1SlUxUlNRVU5KVDA1RlV5QlFWVUpNU1VOQlV6RWRNQnNHQTFVRUF4UVVRMEVnUkVWVFFWSlNUMHhNVHlCQVJrbFNUVUV4SXpBaEJna3Foa2lHOXcwQkNRRVdGR3BoTG5KdmJXRnVRSFJsYkhabGJuUXVZMjl0Z2dFQk1EUUdDV0NHU0FHRytFSUJDQVFuRmlWb2RIUndPbHhjZEdWc2RtVnVkQzVrWlhOaGNuSnZiR3h2TG1OdmJYQnZibVZ1ZEdWek1CNEdDV0NHU0FHRytFSUJEUVFSRmc5NFkyRWdZMlZ5ZEdsbWFXTmhkR1V3RFFZSktvWklodmNOQVFFRkJRQURnWUVBTEVCaU9sQUs2STFvVFFNWGJ5QldLUUYyQWVkSThtOGtScFV3RUZURmJVVUNjaTBEMWxaNkJseFYzVlQ3K2o4czJmMkJtSHBzRmEvZmhJNHFQU2pwWW9laXMwNTB3Mmk1OWVaL3dYQWR5S2xoV25wTWRLL3dwMkptaHh3THgvRFRXMmo5a0hESFJheC9Jem1NOVV2eGxHN252cVVMUHA0TjVmWm1rRDZtaWl3eGdnTGZNSUlDMndJQkFUQ0IwekNCelRFTE1Ba0dBMVVFQmhNQ1JWTXhFREFPQmdOVkJBZ1RCMU5GVmtsTVRFRXhFREFPQmdOVkJBY1RCMU5GVmtsTVRFRXhJakFnQmdOVkJBb1RHVlJGVEZaRlRsUWdTVTVVUlZKQlExUkpWa0V1SUZNdVFTNHhNakF3QmdOVkJBc1RLVVJGVUVGU1ZFRk5SVTVVVHlCRVJTQkJSRTFKVGtsVFZGSkJRMGxQVGtWVElGQlZRa3hKUTBGVE1SMHdHd1lEVlFRREZCUkRRU0JFUlZOQlVsSlBURXhQSUVCR1NWSk5RVEVqTUNFR0NTcUdTSWIzRFFFSkFSWVVhbUV1Y205dFlXNUFkR1ZzZG1WdWRDNWpiMjBDQVFjd0NRWUZLdzREQWhvRkFLQ0NBV0V3R2dZSktvWklodmNOQVFrRE1RMEdDeXFHU0liM0RRRUpFQUVFTUNNR0NTcUdTSWIzRFFFSkJERVdCQlRUTW1hVzBYMTFPdERnQ3lKR3YwYmxaS0tNOGpDQ0FSd0dDeXFHU0liM0RRRUpFQUlNTVlJQkN6Q0NBUWN3Z2ZVd2dmSUVGS1EyempKdXFkRTRMUHMrTURlVm5FSE5JUnJqTUlIWk1JSFRwSUhRTUlITk1Rc3dDUVlEVlFRR0V3SkZVekVRTUE0R0ExVUVDQk1IVTBWV1NVeE1RVEVRTUE0R0ExVUVCeE1IVTBWV1NVeE1RVEVpTUNBR0ExVUVDaE1aVkVWTVZrVk9WQ0JKVGxSRlVrRkRWRWxXUVM0Z1V5NUJMakV5TURBR0ExVUVDeE1wUkVWUVFWSlVRVTFGVGxSUElFUkZJRUZFVFVsT1NWTlVVa0ZEU1U5T1JWTWdVRlZDVEVsRFFWTXhIVEFiQmdOVkJBTVVGRU5CSUVSRlUwRlNVazlNVEU4Z1FFWkpVazFCTVNNd0lRWUpLb1pJaHZjTkFRa0JGaFJxWVM1eWIyMWhia0IwWld4MlpXNTBMbU52YlFJQkJ6QU5NQXNHQ1NzR2dRMmlYQW9DQVRBTkJna3Foa2lHOXcwQkFRRUZBQVNCZ0RkcEdwclVXSUZGWWtLV0QzRGdzMzFHQy9wYUNmY1JQMERrS1pNWGtvMzRIeGxGWXIraU9vWWhDUG1VcFNKOHlhNmtSTWlXTTdCSk1JOXllUzRuUjk4UllCQS9MS0NMMm1STWV0dGFQSGJuN3MzYVF0SWg4RWcvMEtieXJmZVU5cVYrQUZ1ZkNSam5rQ2JXWk5ydHQ0dWJmem01SnVZQ1JUV3o1c2lHWWN6VjwvRW5jYXBzdWxhdGVkVGltZVN0YW1wPjwvU2lnbmF0dXJlVGltZVN0YW1wPjwvVW5zaWduZWRTaWduYXR1cmVQcm9wZXJ0aWVzPjwvVW5zaWduZWRQcm9wZXJ0aWVzPjwveGFkZXM6UXVhbGlmeWluZ1Byb3BlcnRpZXM+PC9kczpPYmplY3Q+PC9kczpTaWduYXR1cmU+PC9BRklSTUE+]]>" +
												"</dss:Base64XML>" +
											"</dss:Document>" +
										"</dss:InputDocuments>" +
										"<dss:OptionalInputs>" +
											"<dss:ClaimedIdentity>" +
												"<dss:Name>tester</dss:Name>" +
											"</dss:ClaimedIdentity>" +
											"<afxp:ReturnReadableCertificateInfo xmlns:afxp=\"urn:afirma:dss:1.0:profile:XSS:schema\"/>" +
											"<afxp:AdditionalReportOption xmlns:afxp=\"urn:oasis:names:tc:dss:1.0:profiles:XSS\">" +
												"<afxp:IncludeProperties>" +
													"<afxp:IncludeProperty Type=\"urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp\"/>" +
												"</afxp:IncludeProperties>" +
											"</afxp:AdditionalReportOption>" +
											"<vr:ReturnVerificationReport xmlns:vr=\"urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#\">" +
												"<vr:ReportOptions>" +
													"<vr:IncludeCertificateValues>true</vr:IncludeCertificateValues>" +
													"<vr:ReportDetailLevel>urn:oasis:names:tc:dss:1.0:reportdetail:allDetails</vr:ReportDetailLevel>" +
												"</vr:ReportOptions>" +
											"</vr:ReturnVerificationReport>" +
										"</dss:OptionalInputs>" +
										"<dss:SignatureObject>" +
											"<dss:SignaturePtr WhichDocument=\"1232017104073\"/>" +
										"</dss:SignatureObject>" +
								   "</dss:VerifyRequest>";
	            logger.info(AFirmaClient.CABECERA + paramIn.toString());
	            String ret = (String) call.invoke(new Object[] {paramIn});
	            logger.error(AFirmaClient.CABECERA + " resultado" + ret);
	        } else {
	            logger.error(AFirmaClient.CABECERA + "No se ha encontrado el servicio " + servicio + " en la configuracion.");
	        }
	      } catch (Exception e) {
	         logger.error(AFirmaClient.CABECERA + e.toString());
	         e.printStackTrace();
	      }
	   }

	   public static String leeFichero(String urlFichero) {
	      try {
	    	 File f = new File("", urlFichero);
	    	 System.out.println("EXISTE = " + f.exists());
	         BufferedReader in  = new BufferedReader(new FileReader(urlFichero));
	         String leidoAux = "";
	         String fichero = "";
	         leidoAux = in.readLine();
	         while (leidoAux != null) {
	            fichero += leidoAux + "\n";
	            leidoAux = in.readLine();
	         }
	         if ((fichero != null) && (fichero.trim().length() > 0)) {
	            return fichero.toString();
	         }
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	      return null;
	   }
	   */
}
