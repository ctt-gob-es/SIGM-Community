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

package es.seap.minhap.portafirmas.ws.mobile.util;

import es.seap.minhap.portafirmas.utils.Constants;

public class MobileConstants {

	// Filtros para la solicitud del listado de peticiones
	public static final String ORDER_ATTRIBUTE_FILTER = "orderAttribute";
	public static final String SEARCH_FILTER = "searchFilter";
	public static final String LABEL_FILTER = "labelFilter";
	public static final String APPLICATION_FILTER = "applicationFilter";
	public static final String INIT_DATE_FILTER = "initDateFilter";
	public static final String END_DATE_FILTER = "endDateFilter";
	public static final String ORDER_ASC_DESC = "orderAscDesc";

	// Ordenación de la lista
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	// Estado solicitud de peticiones
	public static final String UNRESOLVED = "unresolved";
	public static final String SIGNED = "signed";
	public static final String REJECTED = "rejected";

	// Tipos de petición
	public static final String SIGN_TYPE = "firma";
	public static final String APPROVAL_TYPE = "vistobueno";

	// Códigos y mensajes de error
	public static final String COD_000 = "COD_000";
	public static final String MESSAGE_000 = "Error inesperado. Consulte con el administrador";
	public static final String COD_001 = "COD_001";
	public static final String MESSAGE_001 = "Error al validar el certificado del usuario";
	public static final String COD_002 = "COD_002";
	public static final String MESSAGE_002 = "Error al intentar autenticar al usuario";
	public static final String COD_003 = "COD_003";
	public static final String MESSAGE_003 = "La etiqueta de petición solicitada no se encuentra en el sistema";
	public static final String COD_004 = "COD_004";
	public static final String MESSAGE_004 = "Error al recuperar el documento a firmar";
	public static final String COD_005 = "COD_005";
	public static final String MESSAGE_005 = "Error al comprobar si el documento existe en el sistema";
	public static final String COD_006 = "COD_006";
	public static final String MESSAGE_006 = "Error al generar el hash del documento";
	public static final String COD_007 = "COD_007";
	public static final String MESSAGE_007 = "Error al generar el CVE del documento";
	public static final String COD_008 = "COD_008";
	public static final String MESSAGE_008 = "Error al recuperar la firma solicitada";
	public static final String COD_009 = "COD_009";
	public static final String MESSAGE_009 = "Plataforma SIM no configurada en Portafirmas. No podrá recibir notificaciones PUSH.";
	public static final String COD_010 = "COD_010";
	public static final String MESSAGE_010 = "Error al generar sello de tiempo";
	public static final String COD_011 = "COD_011";
	public static final String MESSAGE_011 = "La firma obtenida no es válida";
	public static final String COD_012 = "COD_012";
	public static final String MESSAGE_012 = "El usuario del certificado no existe en el sistema";
	public static final String COD_013 = "COD_013";
	public static final String MESSAGE_013 = Constants.MSG_BLOCK_REQUEST_ERROR;
	public static final String COD_014 = "COD_014";
	public static final String MESSAGE_014 = "Error generando petición de acceso a Cl@ve";
	public static final String COD_015 = "COD_015";
	public static final String MESSAGE_015 = "No se ha podido validar el token de la SAML Response";
	public static final String COD_016 = "COD_016";
	public static final String MESSAGE_016 = "Credenciales Erróneas. Usuario no válido";
	public static final String COD_017 = "COD_017";
	public static final String MESSAGE_017 = "No se ha podido autenticar el usuario en Portafirmas";
	public static final String COD_018 = "COD_018";
	public static final String MESSAGE_018 = "El parámetro SAML Response es obligatorio";
	//Tipos de archivo	
	public static final String MIME_TCN= "text/tcn";
	public static final String MIME_PDF= "application/pdf";
}
