/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.actions;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.ApplicationContext;

import es.msssi.sgm.registropresencial.beans.ibatis.User;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.UserDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.ExcelUtils;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Action que carga usuarios
 *  
 */
public class LoadUsersAction extends GenericActions {
	
	
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(LoadUsersAction.class.getName());
    private UserDAO userDAO = null;
    private static ApplicationContext appContext;
    
     static {
	appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
       }
    
    /**
     * Constructor.
     */
    public LoadUsersAction() {
    	userDAO = (UserDAO) appContext.getBean("userDAO");
    }
    
    
    public void handleFileUpload(FileUploadEvent event) {
        
		LOG.error("Iniciando carga masiva de usuarios.");
    	String warnMessage = "", infoMessage = "";
		String userUser = "";
		try {
						
			byte[] excelDocument = event.getFile().getContents();
			List<User> listUser = ExcelUtils.getListUserFromByteArray(excelDocument);
			
			LOG.error("Numero de usuarios a registrar "+listUser.size());
			 
			for(User user: listUser){
				userUser = user.getUserUser();
				if(userDAO.existUser(user)){
					warnMessage += "Usuario '"+userUser+"' ya existe.<br/>";
					continue;	
				}
				
				LOG.info("Insertando usuario... "+user);
				int userId = userDAO.insertUser(user);
				if(userId!= -1)
					infoMessage += "Usuario '"+userUser+"' insertado correctamente.<br/>";
				else
					warnMessage += "Error insertando usuario '"+userUser+"'.<br/>";
			}
			

			if(!warnMessage.isEmpty()){				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, warnMessage, ""));
			}

			if(listUser.size() == 0)
				infoMessage = "No se ha cargado ningún usuario";
			else 
				infoMessage = "Usuarios cargados.<br/>"+infoMessage;
				
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, infoMessage, ""));
	    
		    LOG.error("warnMessage: "+warnMessage);
		    LOG.error("infoMessage: "+infoMessage);
		}
		catch (IOException e) {
		    LOG.error("ERROR-warnMessage: "+warnMessage);
		    LOG.error("ERROR-infoMessage: "+infoMessage);
		    LOG.error(
			ErrorConstants.INSERT_USERS_ERROR_MESSAGE, e);
		    Utils.redirectToErrorPage(
			null, null, e);			
		}
		
		 
    }    
 
    


		/* ALTA DE USUARIO
		
		/////////////////// insertUser
		User user = new User();
		user.setUserUser("unuevo5");
		user.setUserPassword("unuevo5");
		
		Departament userDepartament = new Departament();
		userDepartament.setDepartamentId(110);
		user.setUserDepartament(userDepartament);
		
		AdminUtils admUtils = new AdminUtils();
		
		/////////////////// insertPermissions
		List<Permission> permissions = null;
		permissions = admUtils.setDefaultPerms();
		user.setPermissions(permissions);

		/////////////////// insertProfiles
		List<Profile> profiles = null;
		profiles = admUtils.setDefaultProfile(user);
		user.setProfiles(profiles);
		
		/////////////////// insertUserData
		user.setUserName("user Nuevo 5");		
		user.setUserSurname1("apellido nuevo5 1");
		user.setUserSurname2("apellido nuevo5 2");
		
		/////////////////// insertUserPerm
		user.setUserGenericPermissions(SIGMADMINConstants.GENERIC_PERMS_USER_WITH_IR);		
		
		uService.insertUser(user);
		*/
 
}