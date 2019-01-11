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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.msssi.sgm.registropresencial.beans.Configuration;
import es.msssi.sgm.registropresencial.beans.FieldsCopyInputRegEnum;
import es.msssi.sgm.registropresencial.beans.FieldsCopyOutputRegEnum;
import es.msssi.sgm.registropresencial.businessobject.ConfigurationBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.ResourceRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Action para cambiar la configuracion del usuario.
 * 
 * @author cmorenog
 */
public class ConfigurationAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ConfigurationAction.class);
    private Configuration configuration = null;
    private ConfigurationBo configurationBo;
    private UsuarioVO usuario;
    private static List <SelectItem> fieldInputReg = null;
    private static List <SelectItem> fieldOutputReg = null;
    private static ResourceRP resourceRP = null;

    /**
     * Se obtienen los valores de configuracion del usuario.
     */
    @PostConstruct
    public void create() {
	LOG.trace("Entrando en ConfigurationAction.create()");
	init();
	if (resourceRP == null){
	    resourceRP = ResourceRP.getInstance(useCaseConf.getLocale());
	}   

	if (fieldInputReg == null){
	    fieldInputReg = new ArrayList<SelectItem>();
	    fieldInputReg.add(new SelectItem("fld7", resourceRP.getProperty(FieldsCopyInputRegEnum.FLD7.getName())));//origen
	    fieldInputReg.add(new SelectItem("fld8",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD8.getName())));//destino
	    fieldInputReg.add(new SelectItem("fld9",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD9.getName())));//interesado
	    fieldInputReg.add(new SelectItem("fld10",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD10.getName())));//numero registro original
	    fieldInputReg.add(new SelectItem("fld11",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD11.getName())));//tipo registro original
	    fieldInputReg.add(new SelectItem("fld12",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD12.getName())));//fecha registro original
	    fieldInputReg.add(new SelectItem("fld14",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD14.getName())));//transporte
	    fieldInputReg.add(new SelectItem("fld15",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD15.getName())));//numero transporte
	    fieldInputReg.add(new SelectItem("fld16",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD16.getName())));//asunto
	    fieldInputReg.add(new SelectItem("fld17",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD17.getName())));//resumen
	    fieldInputReg.add(new SelectItem("fld18",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD18.getName())));//comentario
	    fieldInputReg.add(new SelectItem("fld19",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD19.getName())));//expediente
	    fieldInputReg.add(new SelectItem("fld501",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD501.getName())));//expone
	    fieldInputReg.add(new SelectItem("fld502",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD502.getName())));//solicita
	    fieldInputReg.add(new SelectItem("fld507",resourceRP.getProperty(FieldsCopyInputRegEnum.FLD507.getName())));//observaciones
	    fieldInputReg.add(new SelectItem("sopDoc",resourceRP.getProperty(FieldsCopyInputRegEnum.SOPDOC.getName())));//soporte documentacion
	}
	
	if (fieldOutputReg == null){
	    fieldOutputReg = new ArrayList<SelectItem>();
	    fieldOutputReg.add(new SelectItem("fld7",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD7.getName())));//origen
	    fieldOutputReg.add(new SelectItem("fld8",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD8.getName())));//destino
	    fieldOutputReg.add(new SelectItem("fld9",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD9.getName())));//interesado
	    fieldOutputReg.add(new SelectItem("fld10",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD10.getName())));//transporte
	    fieldOutputReg.add(new SelectItem("fld11",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD11.getName())));//numero transporte
	    fieldOutputReg.add(new SelectItem("fld12",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD12.getName())));//asunto
	    fieldOutputReg.add(new SelectItem("fld13",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD13.getName())));//resumen
	    fieldOutputReg.add(new SelectItem("fld14",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD14.getName())));//comentario
	    fieldOutputReg.add(new SelectItem("fld501",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD501.getName())));//expone
	    fieldOutputReg.add(new SelectItem("fld502",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD502.getName())));//solicita
	    fieldOutputReg.add(new SelectItem("fld507",resourceRP.getProperty(FieldsCopyOutputRegEnum.FLD507.getName())));//observaciones
	    fieldOutputReg.add(new SelectItem("sopDoc",resourceRP.getProperty(FieldsCopyOutputRegEnum.SOPDOC.getName())));//soporte documentacion
	}
	try {
	    usuario = (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("USERVO");
	   
	    configurationBo = new ConfigurationBo();
	    configuration = configurationBo.getConfigurationUser(Integer.valueOf(usuario.getId()));
	    if (configuration == null){
		configuration = new Configuration();
		configuration.setIdUser(Integer.valueOf(usuario.getId()));
	    }
	}
	catch (RPGenericException rpGenericException) {
	    LOG.error(ErrorConstants.GET_CONFIGURATION_ERROR_MESSAGE, rpGenericException);
	    Utils.redirectToErrorPage(rpGenericException, null, null);
	}
    }

    /**
     * guardar configuracion del usuario.
     */
    public void update() {
	LOG.trace("Entrando en ConfigurationAction.update()");
	init();
	try {
	    configurationBo.updateConfigurationUser(configuration);
	    FacesContext.getCurrentInstance().getExternalContext()
		.getSessionMap().put(KeysRP.CONFIGURATIONUSER, configuration);
	    Utils.navigate("inicio.xhtml");
	}
	catch (RPGenericException rpGenericException) {
	    LOG.error(ErrorConstants.UPDATE_CONFIGURATION_ERROR_MESSAGE + ". Código: "
		    + rpGenericException.getCode().getCode() + " . Mensaje: "
		    + rpGenericException.getShortMessage());
	    Utils.redirectToErrorPage(rpGenericException, null, null);
	}
    }

    /**
     * Obtiene el valor del parámetro configuration.
     * 
     * @return configuration valor del campo a obtener.
     */
    public Configuration getConfiguration() {
        return configuration;
    }
    /**
     * Guarda el valor del parámetro configuration.
     * 
     * @param configuration
     *            valor del campo a guardar.
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    
    /**
     * Obtiene el valor del parámetro fieldInputReg.
     * 
     * @return fieldInputReg valor del campo a obtener.
     */
    public List<SelectItem> getFieldInputReg() {
        return fieldInputReg;
    }
    
    /**
     * Obtiene el valor del parámetro fieldOutputReg.
     * 
     * @return fieldOutputReg valor del campo a obtener.
     */
    public List<SelectItem> getFieldOutputReg() {
        return fieldOutputReg;
    }
}