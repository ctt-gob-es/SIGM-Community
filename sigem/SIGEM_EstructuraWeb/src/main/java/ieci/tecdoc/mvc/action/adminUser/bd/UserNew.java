/*
 * Created on 30-mar-2005
 *
 */
package ieci.tecdoc.mvc.action.adminUser.bd;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorFactory;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.mvc.action.BaseAction;
import ieci.tecdoc.mvc.dto.access.UserConnectedDTO;
import ieci.tecdoc.mvc.error.MvcError;
import ieci.tecdoc.mvc.form.adminUser.bd.UserForm;
import ieci.tecdoc.mvc.service.adminUser.ServiceCertificate;
import ieci.tecdoc.mvc.util.Constantes;
import ieci.tecdoc.mvc.util.SessionHelper;
import ieci.tecdoc.seo.core.exception.IeciTdException;
import ieci.tecdoc.seo.idoc.admin.api.ObjFactory;
import ieci.tecdoc.seo.idoc.admin.api.user.AplicacionPerfil;
import ieci.tecdoc.seo.idoc.admin.api.user.Permission;
import ieci.tecdoc.seo.idoc.admin.api.user.Permissions;
import ieci.tecdoc.seo.idoc.admin.api.user.User;
import ieci.tecdoc.seo.idoc.admin.api.user.UserAccess;
import ieci.tecdoc.seo.idoc.admin.api.user.UserDefs;
import ieci.tecdoc.seo.idoc.admin.api.user.UserProfiles;
import ieci.tecdoc.seo.idoc.admin.internal.UserProfileImpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Antonio Mar�a
 *  
 */
public class UserNew extends BaseAction {

    /*
     * (non-Javadoc)
     * 
     * @see ieci.tecdoc.mvc.action.BaseAction#executeLogic(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    private static Logger logger = Logger.getLogger(UserNew.class);
    int userId;
    UserConnectedDTO userDTO;
    
    protected ActionForward executeLogic(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	String entidad=SessionHelper.getEntidad(request);

        UserForm userForm = (UserForm) form;
        userId = getUserId(request);
        
        String submitted = request.getParameter("submitted");

        if (submitted == null) {
            
            String objId = request.getParameter("idPadre"); // Id del departamento al que pertenece
            int idDept = Integer.parseInt(objId);
            UserAccess userAccess = ObjFactory.createUserAccess();
            boolean userCanCreateUser = userAccess.userCanCreateUser(userId, idDept, entidad);
            if (!userCanCreateUser)
                throw new IeciTdException(String.valueOf(MvcError.EC_NOT_CAN_CREATE_USER), null);
            
            userForm.setIdoc("standard");
            userForm.setUser("none");
            userDTO = new UserConnectedDTO();
            iniciaBasico(userDTO);
            request.setAttribute("userDTO", userDTO);
            
            request.setAttribute("listaAplicaciones", userForm.getListaAplicaciones());
                        
            return mapping.findForward("ok");

        } else {
            String objId = request.getParameter("idPadre"); // Id del
                                                            // departamento al
                                                            // que pertenece
            int idDept = 0;
            if (objId != null && !objId.equals(""))
                idDept = Integer.parseInt(objId);

            if (logger.isDebugEnabled()) {
                logger.debug("Creando usuario colgando de: " + idDept);
            }
            
            String dni = userForm.getDni();
    		if(StringUtils.isNotEmpty(dni)){
    			dni = dni.replace(" " , "").toUpperCase();
    			userForm.setDni(dni);
    		}
    		
            crearUsuarioPortafirmas(entidad, userForm); 

            User user = ObjFactory.createUser(userId, idDept);
            
            guardarDatos(user, userForm, entidad);
            
            request.setAttribute("tipo", "Usuario");

            byte tipo = Constantes.DEPARTAMENT;
            String objTipo = String.valueOf(tipo);

            request.setAttribute("id", objId);
            request.setAttribute("deptToken", objTipo);

            return mapping.findForward("success");
        }
    }

    private void crearUsuarioPortafirmas(String entidadId, UserForm userForm) throws ISPACException, IeciTdException {
    	ProcessSignConnector portafirmasSignConnector = ProcessSignConnectorFactory.getInstance(entidadId).getProcessSignConnector();

    	//Solo tiene un campo para el apellido, no podemos saber el segundo apellido
		try {
			if(null != userForm && StringUtils.isNotEmpty(userForm.getDni()) && !portafirmasSignConnector.existeUsuarioPortafirmas(userForm.getDni())){
				portafirmasSignConnector.crearUsuarioPortafirmas(entidadId, userForm.getDni(), StringUtils.defaultString(userForm.getNombrePersonal()), StringUtils.defaultString(userForm.getApellidos()), "", StringUtils.defaultString(userForm.getEmail()));
			}
		} catch (ISPACException e) {
			logger.error("ERROR al crear el usuario: " + StringUtils.defaultString(userForm.getDni()) + " - " + StringUtils.defaultString(userForm.getNombrePersonal()) + " " + StringUtils.defaultString(userForm.getApellidos()) + " - " + StringUtils.defaultString(userForm.getEmail()), e);
			throw new IeciTdException( String.valueOf(MvcError.EC_ERROR_PORTAFIRMAS), null);
		}
	}

	/**
     * @param userDTO2
     */
    private void iniciaBasico(UserConnectedDTO userDTO) {
       Map<String, Integer> profiles = new TreeMap<String, Integer>();
       profiles.put(String.valueOf(UserDefs.PRODUCT_IDOC), new Integer(UserDefs.PROFILE_NONE) );
       profiles.put(String.valueOf(UserDefs.PRODUCT_USER), new Integer(UserDefs.PROFILE_NONE) );
       profiles.put(String.valueOf(UserDefs.PRODUCT_VOLUME), new Integer(UserDefs.PROFILE_NONE) );
       userDTO.setProfiles(profiles);
        
    }

    
    /*
     * @author Antonio Mar�a
     *return (Integer) profiles.get( String.valueOf(UserDefs.PRODUCT_IDOC));
        }
        public Integer getUserProfile()
        {
            return (Integer) profiles.get( String.valueOf(UserDefs.PRODUCT_USER));
        }
        public Integer getVolProfile()
        {
            return (Integer) profiles.get( String.valueOf(UserDefs.PRODUCT_VOLUME));
     */
    
    /**
     * @param dept
     * @param deptForm
     */
    public void guardarDatos(User user, UserForm userForm, String entidad) throws Exception {
        
        // General
        user.setName(userForm.getNombre());
        user.setPassword(userForm.getPwd());
        user.setPwdmbc(userForm.isPwdmbc());
        user.setPwdvpcheck(userForm.isPwdvpcheck());
        user.setDescription(userForm.getDescripcion());

        //Datos Personales
        user.getUserDataImpl().setCargo(userForm.getCargo());
        user.getUserDataImpl().setEmail(userForm.getEmail());
        user.getUserDataImpl().setIdCert(userForm.getIdCert());
        user.getUserDataImpl().setTfnoMovil(userForm.getTfnoMovil());
        user.getUserDataImpl().setApellidos(userForm.getApellidos());
		user.getUserDataImpl().setNombre(userForm.getNombrePersonal());
		//[Manu Ticket#175] Crear un campo con dni en el usuario
		
		user.getUserDataImpl().setAsignaNumDecreto(userForm.getAsignaNumDecreto());
		
		user.getUserDataImpl().setDni(userForm.getDni());
        
        //Almacenamos el usuario en BD
        user.store(entidad);

        int id = user.getId();
        user.load(id, entidad);
        user.setDescription(userForm.getDescripcion());


        user.resetProfiles();
        UserProfiles profiles=user.getProfiles();
        Collection<?> listaPerfilesAplicacion=userForm.getListaAplicaciones();
        for (Iterator<?> iter = listaPerfilesAplicacion.iterator(); iter.hasNext();) {
			AplicacionPerfil element = (AplicacionPerfil) iter.next();
			int idAplicacion=Integer.parseInt(element.getIdetificador());
			profiles.add(new UserProfileImpl(id,idAplicacion,element.getPerfil()));
		}
       
        
        /*
        // Sistema

        UserProfile profile = profiles
                .getProductProfile(UserDefs.PRODUCT_SYSTEM);
        if (userForm.isSystemSuperuser())
            profile.setProfile(UserDefs.PROFILE_SUPERUSER);
        else
            profile.setProfile(UserDefs.PROFILE_NONE);


        // Administrador de Usuarios
        profile = profiles.getProductProfile(UserDefs.PRODUCT_USER);
        String perfiles = userForm.getUser();
        if (perfiles.equals("superuser"))
            profile.setProfile(UserDefs.PROFILE_SUPERUSER);
        else if (perfiles.equals("manager"))
            profile.setProfile(UserDefs.PROFILE_MANAGER);
        else if (perfiles.equals("standard"))
            profile.setProfile(UserDefs.PROFILE_STANDARD);
        else if (perfiles.equals("none"))
            profile.setProfile(UserDefs.PROFILE_NONE);

        // InvesDoc
        profile = profiles.getProductProfile(UserDefs.PRODUCT_IDOC);
        perfiles = userForm.getIdoc();
        if (perfiles.equals("superuser"))
            profile.setProfile(UserDefs.PROFILE_SUPERUSER);
        else if (perfiles.equals("manager"))
            profile.setProfile(UserDefs.PROFILE_MANAGER);
        else if (perfiles.equals("standard"))
            profile.setProfile(UserDefs.PROFILE_STANDARD);
        else if (perfiles.equals("none"))
            profile.setProfile(UserDefs.PROFILE_NONE);
        
         // Administrador de vol�menes
        profile = profiles.getProductProfile(UserDefs.PRODUCT_VOLUME);
        if (userForm.isVolumeSuperuser())
            profile.setProfile(UserDefs.PROFILE_SUPERUSER);
        else
            profile.setProfile(UserDefs.PROFILE_NONE);       
        
        */
        
        
        // Permisos
        int permisos = 0;

        if (userForm.isIdocConsulta())
            permisos += UserDefs.PERMISSION_QUERY;
        if (userForm.isIdocModificacion())
            permisos += UserDefs.PERMISSION_UPDATE;
        if (userForm.isIdocCreacion())
            permisos += UserDefs.PERMISSION_CREATION;
        if (userForm.isIdocBorrado())
            permisos += UserDefs.PERMISSION_DELETION;
        if (userForm.isIdocImpresion())
            permisos += UserDefs.PERMISSION_PRINTING;

        Permissions perms = user.getPermissions();
        Permission perm = perms.getProductPermission(UserDefs.PRODUCT_IDOC); // Permisos
                                                                             // sobre
                                                                             // InvesDoc
        perm.setPermission(permisos);
        
        user.store(entidad);// Guardar cambios
        
        if (useCertificate()){
        	String idCert = userForm.getIdCert();
        	ServiceCertificate service = ServiceCertificate.getInstance(); 
            service.addIdCert(id,idCert,null, entidad) ; // A�adir Certificado digital	
        }
        
        
        if (logger.isDebugEnabled()) {
            logger.debug("Usuario " + user.getName() + " creado");
        }

    }
    
}