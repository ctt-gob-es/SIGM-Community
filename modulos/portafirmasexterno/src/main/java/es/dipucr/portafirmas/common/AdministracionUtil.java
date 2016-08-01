package es.dipucr.portafirmas.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;

import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.AssignJobToUserResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedUsersResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Job;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Seat;
import _0.v2.admin.pfirma.cice.juntadeandalucia.PfirmaException;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.EnhancedJob;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.EnhancedUserJobAssociated;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.EnhancedUserJobAssociatedList;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedUserJobAssociatedToUser;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedUserJobAssociatedToUserResponse;
import es.dipucr.sigem.api.rule.common.utils.TercerosUtil;
public class AdministracionUtil {
	
	private static final Logger logger = Logger.getLogger(AdministracionUtil.class);
	
	@SuppressWarnings("unchecked")
	public static boolean relacionarCargoConUsuario(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACRuleException {
		boolean asignado = false;
		try {
		
			//String direccionPortaFirmaExterno = DipucrCommonFunctions.getVarGlobal(Configuracion.DIRECCION_PORTAFIRMASEXTERNOADMIN);
			String direccionPortaFirmaExterno = ServiciosWebPortaFirmasFunciones.getDireccionSWAdmin();
			
			AdminServiceStub admin = new AdminServiceStub(direccionPortaFirmaExterno);
			
			_0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication authentication = Configuracion.getAuthenticationAdminPADES();
			
			//Obtención de los firmante para dar de alta
			IItemCollection itCollectionFirmantes = entitiesAPI.queryEntities("FIRMA_DOC_EXTERNO", "WHERE NUMEXP='"+rulectx.getNumExp()+"'");
			Iterator<IItem> itFirmantes = itCollectionFirmantes.iterator();
			while (itFirmantes.hasNext()) {
				IItem iFirmante = (IItem) itFirmantes.next();
				//COMPRUEBO QUE HAYAN INSERTADO UN CARGO
				if(iFirmante.getString("CARGO")!=null){
					String dniNombre = ""; 
					if(iFirmante.getString("DNI")!=null) dniNombre = iFirmante.getString("DNI");
					String [] vDniNombre = dniNombre.split(" - ");
					String dni = "";
					if(vDniNombre!=null && vDniNombre.length>0){
						dni = vDniNombre[0];
					}
					
					String cargo = "";
					if(iFirmante.getString("CARGO")!=null) cargo = iFirmante.getString("CARGO");
					
					String descripcioncargo = "";
					if(iFirmante.getString("DESCRIPCION_CARGO")!=null) descripcioncargo = iFirmante.getString("DESCRIPCION_CARGO");
					
					cargo = "DCR - "+cargo;
					descripcioncargo = "DCR - "+descripcioncargo;
					
					Job job = new Job();
					job.setIdentifier(cargo);
					job.setDescription(descripcioncargo);
					
					//COmprobamos que este dado de alta el cargo
					boolean cargoCreado = false;
					try{
						admin.insertEnhancedJobs(CreacionObjetosPortafirmas.crearObjetoInsertEnhancedJobs(authentication, Configuracion.getSeatAdmin(), job));
						cargoCreado = true;
						
					}catch (RemoteException e) {
						if(e.getMessage().contains("Ya existe un cargo con identificador") || e.getMessage().contains("Ya existe un cargo con descripción")){
							cargoCreado = true;
							logger.warn(e.getMessage(), e);
						}
						else{
							logger.error(e.getMessage(), e);
							throw new ISPACRuleException("Error. "+e.getMessage(),e);
						}
						
						
					}
					if(cargoCreado){
						try{
							AssignJobToUserResponse relacionUsuarioCargo = admin.assignJobToUser(CreacionObjetosPortafirmas.crearObjetoAssignJobToUser(authentication, cargo, dni));
							asignado = relacionUsuarioCargo.getAssigned();
						}catch(RemoteException e){
							if(e.getMessage().contains("está asociado a otro cargo")){
								//String direccionPortaFirmaExternoConsulta = DipucrCommonFunctions.getVarGlobal(Configuracion.DIRECCION_PORTAFIRMASEXTERNO_CONSULTA);
								String direccionPortaFirmaExternoConsulta = ServiciosWebPortaFirmasFunciones.getDireccionSWConsulta();
								
								QueryServiceStub consulta = new QueryServiceStub(direccionPortaFirmaExternoConsulta);
								
								
								//Comprobamos cual es el cargo asociado al dni.
								QueryEnhancedUserJobAssociatedToUser queryEnhancedUserJobAssociatedToUser = new QueryEnhancedUserJobAssociatedToUser();
								queryEnhancedUserJobAssociatedToUser.setAuthentication(Configuracion.getAuthenticationConsultaPADES());
								queryEnhancedUserJobAssociatedToUser.setUserIdentifier(dni);
								try {
									QueryEnhancedUserJobAssociatedToUserResponse respuesta = consulta.queryEnhancedUserJobAssociatedToUser(queryEnhancedUserJobAssociatedToUser);
									EnhancedUserJobAssociatedList listado = respuesta.getEnhancedUserJobAssociatedList();
									EnhancedUserJobAssociated [] vListado = listado.getEnhancedUserJobAssociated();
									for (int j = 0; j < vListado.length; j++) {
										EnhancedUserJobAssociated usuarioCargo = vListado[j];
										EnhancedJob cargoAsociado = usuarioCargo.getEnhancedJob();
										
										//Eliminamos la relación entre el usuario y el cargo que tiene asignado.
										/**
										 * Número de Incidencia: 73163
										 * Se ha detectado un bug en la operación separateJobToUser que será subsanado en la próxima versión del Port@firmas.

										 * **/
										//SeparateJobToUserResponse separacion = admin.separateJobToUser(CreacionObjetosPortafirmas.crearObjetoSeparateJobToUser(authentication, cargoAsociado.getJob().getIdentifier(), dni));
										//if(separacion.getSeparated()){
											iFirmante.set("CARGO", cargoAsociado.getJob().getIdentifier());
											iFirmante.set("DESCRIPCION_CARGO", cargoAsociado.getJob().getDescription());
											iFirmante.store(rulectx.getClientContext());
											AssignJobToUserResponse relacionUsuarioCargo = admin.assignJobToUser(CreacionObjetosPortafirmas.crearObjetoAssignJobToUser(authentication, cargoAsociado.getJob().getIdentifier(), dni));
											asignado = relacionUsuarioCargo.getAssigned();
										//}
									}

								} catch (_0.v2.query.pfirma.cice.juntadeandalucia.PfirmaException e1) {
									logger.error(e.getMessage(), e);
									throw new ISPACRuleException("Error. "+e.getMessage(),e);
								}
							}
							else{
								logger.error(e.getMessage(), e);
								throw new ISPACRuleException("Error. "+e.getMessage(),e);
							}
						}
					}
					
				}
			}
	
			
			
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (_0.v2.admin.pfirma.cice.juntadeandalucia.PfirmaException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return asignado;
	}

	
	@SuppressWarnings("unchecked")
	public static boolean comprobarExisteUsuarioPortaFirmasExterno(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACRuleException {
		boolean respuestaMetodo = true;
		StringBuffer mensaje = new StringBuffer();
		try {
			//String direccionPortaFirmaExterno = DipucrCommonFunctions.getVarGlobal(Configuracion.DIRECCION_PORTAFIRMASEXTERNOADMIN);
			String direccionPortaFirmaExterno = ServiciosWebPortaFirmasFunciones.getDireccionSWAdmin();
			AdminServiceStub ws = new AdminServiceStub(direccionPortaFirmaExterno);

			ClientContext cct = (ClientContext) rulectx.getClientContext();
			_0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication authentication = Configuracion.getAuthenticationAdminPADES();
			
			Seat seat = Configuracion.getSeatAdmin();			
			
			//Obtención de los firmante para dar de alta
			IItemCollection itCollectionFirmantes = entitiesAPI.queryEntities("FIRMA_DOC_EXTERNO", "WHERE NUMEXP='"+rulectx.getNumExp()+"'");
			Iterator<IItem> itFirmantes = itCollectionFirmantes.iterator();
			while (itFirmantes.hasNext()) {
				IItem iFirmante = (IItem) itFirmantes.next();
				String dniNombre = iFirmante.getString("DNI"); 
				String [] vDniNombre = dniNombre.split(" - ");
				String dni = "";
				if(vDniNombre!=null && vDniNombre.length>0){
					dni = vDniNombre[0];
				}
				IThirdPartyAdapter[] datosTercero = TercerosUtil.getDatosTerceroByNif(cct, dni);
				if(datosTercero==null){
					mensaje.append("El firmante no esta dado de alta en la BBDD de registro de ALSIGM.");
					respuestaMetodo = false;
				}
				
				_0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.User user = new _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.User();
				user.setIdentifier(dni);
				user.setName(datosTercero[0].getNombre());
				user.setSurname1(datosTercero[0].getPrimerApellido());
				user.setSurname2(datosTercero[0].getSegundoApellido());
				
				//Insertar nuevo Usuario.
				//COmprobamos que este dado de alta el cargo
				InsertEnhancedUsersResponse resultado = null;
				try{
					resultado = ws.insertEnhancedUsers(CreacionObjetosPortafirmas.crearObjetoInsertEnhancedUsers(authentication, seat, user));
					
				}catch (RemoteException e) {
					if(e.getMessage().contains("Ya existe un usuario con identificador")){
						logger.warn(e.getMessage(), e);
					}
					else{
						logger.error(e.getMessage(), e);
						throw new ISPACRuleException("Error. "+e.getMessage(),e);
					}
					
					
				}
				if(resultado!=null){
					if (resultado.getEnhancedUsersInserted().bitCount()==1) {
						mensaje.append("Contacte con el usuario "+dniNombre+" para que acceda al portafirmas y añada su email.");
					}
					else{
						logger.warn("El usuario ya esta dado de alta. "+dniNombre);
					}
					
				}
				
			}	
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (PfirmaException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (AxisFault e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		if(mensaje.length()!=0){
			rulectx.setInfoMessage(mensaje.toString());
		}
		return respuestaMetodo;
	}
}
