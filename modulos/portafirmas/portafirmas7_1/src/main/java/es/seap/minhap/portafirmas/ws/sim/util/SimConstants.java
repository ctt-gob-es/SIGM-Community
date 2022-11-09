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

package es.seap.minhap.portafirmas.ws.sim.util;

public class SimConstants {
	
	/*REGISTRO DE USUARIOS SIM*/
	public static final String REG_SIM_3006_COD = "3006";
	public static final String REG_SIM_3006_MSG = "La petición no está construída correctamente. Faltan campo obligatorios.";
	
	public static final String REG_SIM_3007_COD = "3007";
	public static final String REG_SIM_3007_MSG = "Se ha producido un error procesando la petición.";
	
	public static final String REG_SIM_3008_COD = "3008";
	public static final String REG_SIM_3008_MSG = "El servicio no está relacionado con la aplicación del usuario/password.";
	
	public static final String REG_SIM_3009_COD = "3009";
	public static final String REG_SIM_3009_MSG = "El servicio no existe o se encuentra inactivo.";
	
	public static final String REG_SIM_3010_COD = "3010";
	public static final String REG_SIM_3010_MSG = "El usuario/password no pertenece a ninguna aplicación.";
	
	public static final String REG_SIM_3011_COD = "3011";
	public static final String REG_SIM_3011_MSG = "Token de sesión inexistente o incorrecto.";
	
	public static final String REG_SIM_0000_COD = "0000";	
	public static final String REG_SIM_0000_MSG = "Petición procesada correctamente.";
	
	/*ENVÍO DE MENSAJES SIM*/
	public static final String ENV_MSG_SIM_0000_COD = "0000";	
	public static final String ENV_MSG_SIM_0000_MSG = "Todo correcto.";
	
	public static final String ENV_MSG_SIM_3001_COD = "3001";	
	public static final String ENV_MSG_SIM_3001_MSG = "Error en la petición";
	
	public static final String ENV_MSG_SIM_3002_COD = "3002";	
	public static final String ENV_MSG_SIM_3002_MSG = "El usuario/password no corresponden a ningun aplicación.";
	
	public static final String ENV_MSG_SIM_3005_COD = "3005";	
	public static final String ENV_MSG_SIM_3005_MSG = "El usuario indicado no se corresponde con el identificador de la notificación.";
	
	public static final String ENV_MSG_SIM_3013_COD = "3013";	
	public static final String ENV_MSG_SIM_3013_MSG = "No se ha encontrado el identificador de dispositivo.";
	
	public static final String ENV_MSG_SIM_0020_COD = "0020";	
	public static final String ENV_MSG_SIM_0020_MSG = "La petición no está construída correctamente.";
	
	public static final String ENV_MSG_SIM_0098_COD = "0098";	
	public static final String ENV_MSG_SIM_0098_MSG = "Servicio incorrecto. No está asignado a ninguna aplicación.";
	
	public static final String ENV_MSG_SIM_0099_COD = "0099";	
	public static final String ENV_MSG_SIM_0099_MSG = "Error desconocido general.";
	
}
