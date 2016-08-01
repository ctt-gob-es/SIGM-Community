package es.dipucr.portafirmas.client;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.AssignJobToUser;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.AssignJobToUserResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteJobsResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteUsers;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteUsersResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedJobsResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedUsers;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedUsersResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.SeparateJobToUser;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.SeparateJobToUserResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedJobsResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedUsers;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedUsersResponse;
import _0.v2.admin.pfirma.cice.juntadeandalucia.PfirmaException;
import es.dipucr.portafirmas.common.ServiciosAdministracion;


public class PortaFirmasAdministracionClient {
	
	/**
	 * Logger de la clase
	 */
	
	protected static final Logger logger = Logger.getLogger(PortaFirmasAdministracionClient.class);
	

	/**
	 * Da de alta usuarios en la aplicacion
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public InsertEnhancedUsersResponse insertarNuevoUsuario (InsertEnhancedUsers usuariosAlta) throws RemoteException, PfirmaException{
		
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();

		return servAdministracion.altaUsuariosAplicacion(usuariosAlta);
	}
	
	/**
	 * updateEnhancedUsers: Modifica usuarios en la aplicación. Si alguno de los usuarios no 
	 * existiera o no estuviera vigente dará error.
	 * @return 
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public UpdateEnhancedUsersResponse modificarUsuarios (UpdateEnhancedUsers updateEnhancedUsers) throws RemoteException, PfirmaException{
		
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();
		return servAdministracion.modificarUsuarios(updateEnhancedUsers);
	}
	
	/**
	 * Elimina usuarios de la aplicación. Si algún usuario no existe dará error. Si el usuario tiene relaciones que 
	 * no pueden eliminarse (peticiones, autorizaciones sobre otros usuarios...) el usuario se modificará para que 
	 * quede como no vigente.
	 * @return DeleteUsersResponse
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public DeleteUsersResponse eliminarUsuarios (DeleteUsers deleteUsers) throws RemoteException, PfirmaException{
		
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();
		return servAdministracion.eliminarUsuarios(deleteUsers);
	}
	
	/**
	 * Crea nuevos cargos en la aplicación. Si alguno de los cargos existiera y estuviera vigente daría error. 
	 * En caso de existir y no estar vigente se modificaría para que lo estuviera. 
	 * @return InsertEnhancedJobsResponse Número de cargos insertados.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public InsertEnhancedJobsResponse insertarNuevoCargo(InsertEnhancedJobs insertEnhancedJobs) throws RemoteException, PfirmaException{
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();
		return servAdministracion.insertarNuevoCargo(insertEnhancedJobs);
	}

	/**
	 * Modifica cargos en la aplicación. Si alguno de los cargos no existiera o no estuviera vigente dará error.
	 * @return UpdateEnhancedJobsResponse Número de cargos modificados
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public UpdateEnhancedJobsResponse modificarCargos(UpdateEnhancedJobs updateEnhancedJobs) throws RemoteException, PfirmaException{
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();
		return servAdministracion.modificarCargos(updateEnhancedJobs);
	}
	
	/**
	 * Elimina cargos de la aplicación. Si algún cargo no existe dará error. Si el cargo tiene relaciones que no 
	 * pueden eliminarse (usuarios asociados, peticiones) el cargo se modificará para que quede como no vigente.
	 * @param deleteJobs Número de cargos borrados.
	 * @return DeleteJobsResponse
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public DeleteJobsResponse eliminarCargos(DeleteJobs deleteJobs) throws RemoteException, PfirmaException{
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();
		return servAdministracion.eliminarCargos(deleteJobs);
	}
	
	/**
	 * Asigna un cargo a un usuario para un determinado periodo de tiempo. Si ya existiera la relación usuariocargo, 
	 * se modificaría el periodo de vigencia.
	 * @param assignJobToUser 
	 * @return AssignJobToUserResponse; Indica si se ha podido hacer la asociación.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public AssignJobToUserResponse asignaCargoUsuario(AssignJobToUser assignJobToUser) throws RemoteException, PfirmaException{
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();
		return servAdministracion.asignaCargoUsuario(assignJobToUser);
	}
	
	/**
	 * Elimina la relación usuario-cargo.
	 * @param separateJobToUser 
	 * @return SeparateJobToUserResponse Indica si se ha eliminado la asociación.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * **/
	public SeparateJobToUserResponse separarUsuarioCargo(SeparateJobToUser separateJobToUser) throws RemoteException, PfirmaException{
		ServiciosAdministracion servAdministracion = new ServiciosAdministracion();
		return servAdministracion.separarUsuarioCargo(separateJobToUser);
	}
}
