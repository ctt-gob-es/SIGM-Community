package ieci.tdw.ispac.api.rule.procedures.secretaria;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class InicializaParticipantesRule implements IRule {

    private static final Logger LOGGER = Logger.getLogger(InicializaParticipantesRule.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Sólo actualizo la lista de participantes si está vacía
            IItemCollection collAllParts = ParticipantesUtil.getParticipantes(cct, rulectx.getNumExp());
            Iterator<?> itAllParts = collAllParts.iterator();
            
            if (!itAllParts.hasNext()) {
                //Obtenemos los miembros del órgano correspondiente
                String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
                
                String strQuery = "WHERE NOMBRE = '" + strOrgano + "'";               
                IItemCollection collMiembros = entitiesAPI.queryEntities("SECR_ORGANO", strQuery);
                Iterator<?> itMiembros = collMiembros.iterator();
                
                while(itMiembros.hasNext()) {
                    IItem iMiembro = (IItem)itMiembros.next();
                    String strNumExp = iMiembro.getString("NUMEXP");
                    
                    //Obtención de los datos del expediente del miembro
                    IItem iExpediente = ExpedientesUtil.getExpediente(cct, strNumExp);
                    
                    if (null == iExpediente) {
                        LOGGER.warn("No se encuentra el expediente asociado al miembro. Numexp = " + strNumExp);
                        
                    } else {
                        //Sólo se admiten integrantes en activo
                        //Comprobamos estado del expediente y fecha de cese
                        boolean enActivo = checkFechas(rulectx, iExpediente);
                        
                        //En comisiones informativas comprobamos también el área
                        boolean mismaArea = checkArea(rulectx, iExpediente);
                        
                        if (enActivo && mismaArea) {
                            
                            //La entidad de participantes siempre será la número 3
                            IItem item = entitiesAPI.createEntity(3);
                            item.set(ParticipantesUtil.NUMEXP, rulectx.getNumExp());
                            item.set(ParticipantesUtil.NDOC, iExpediente.getString(ExpedientesUtil.NIFCIFTITULAR));
                            item.set(ParticipantesUtil.NOMBRE, iExpediente.getString(ExpedientesUtil.IDENTIDADTITULAR));
                            item.set(ParticipantesUtil.IDDIRECCIONPOSTAL, iExpediente.getString(ExpedientesUtil.IDDIRECCIONPOSTAL));
                            item.set(ParticipantesUtil.DIRNOT, iExpediente.getString(ExpedientesUtil.DOMICILIO));
                            item.set(ParticipantesUtil.C_POSTAL, iExpediente.getString(ExpedientesUtil.CPOSTAL));
                            item.set(ParticipantesUtil.LOCALIDAD, iExpediente.getString(ExpedientesUtil.CIUDAD));
                            item.set(ParticipantesUtil.CAUT, iExpediente.getString(ExpedientesUtil.REGIONPAIS));
                            item.set(ParticipantesUtil.DIRECCIONTELEMATICA, iExpediente.getString(ExpedientesUtil.DIRECCIONTELEMATICA));
                            item.set(ParticipantesUtil.TFNO_FIJO, iExpediente.getString(ExpedientesUtil.TFNOFIJO));
                            item.set(ParticipantesUtil.TFNO_MOVIL, iExpediente.getString(ExpedientesUtil.TFNOMOVIL));
                            item.set(ParticipantesUtil.TIPO_PERSONA, iExpediente.getString(ExpedientesUtil.TIPOPERSONA));
                            item.set(ParticipantesUtil.TIPO_DIRECCION, iExpediente.getString(ExpedientesUtil.TIPODIRECCIONINTERESADO));
                            item.set(ParticipantesUtil.ASISTE, "SI");
                            item.store(cct);
                        }
                    }
                }
            }
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            LOGGER.error("No se ha podido inicializar la lista de miembros. "+rulectx.getNumExp()+" - "+e.getMessage(),e);
            throw new ISPACRuleException("No se ha podido inicializar la lista de miembros. "+rulectx.getNumExp()+" - "+e.getMessage(),e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
    
    private boolean checkFechas(IRuleContext rulectx, IItem iExpediente) throws ISPACException {
        boolean enActivo = true;
        
        try {
            String fcierre = iExpediente.getString(ExpedientesUtil.FCIERRE); 
            
            if (fcierre!=null && fcierre.length()>0) {
                enActivo = false;
            }
            
            String strNumExp = iExpediente.getString(ExpedientesUtil.NUMEXP);
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            IItemCollection collInfo = entitiesAPI.getEntities("SECR_MIEMBRO", strNumExp);
            Iterator<?> itInfo = collInfo.iterator();
            
            if (itInfo.hasNext()) {
                IItem iInfo = (IItem)itInfo.next();
                Date fnombr = iInfo.getDate("FECHA_NOMBR");
                Date fcese = iInfo.getDate("FECHA_CESE");
                Date fnow = new Date();
                
                if ( (fnombr!=null && fnombr.after(fnow)) || (fcese!=null  && fcese.before(fnow))  ) {
                    enActivo = false;
                }
            }
        } catch(ISPACException e) {
            throw new ISPACException(e);
        }
        return enActivo;
    }
    
    private boolean checkArea(IRuleContext rulectx, IItem iExpediente) throws ISPACException {
        boolean mismaArea = true;
        
        try {
            //En Comisión Informativa tenemos que comprobar que el Integrante
            //pertenezca al área de la Comisión
            String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
            
            if (strOrgano.compareTo("COMI") == 0) {
                IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
                String strNumExp = iExpediente.getString(ExpedientesUtil.NUMEXP);
                //Esto no funciona porque aun no están consolidados los datos
                //String strArea = CommonFunctions.getAreaSesion(rulectx, null);
                IItem sesion = rulectx.getItem();
                
                if (sesion != null) {
                    String strArea = sesion.getString("AREA");
                    String strQuery = "WHERE NUMEXP = '" + strNumExp + "' AND NOMBRE = '" + strArea + "'";
                    IItemCollection coll = entitiesAPI.queryEntities("SECR_AREAS", strQuery);
                    Iterator<?> it = coll.iterator();
                    
                    if (!it.hasNext()) {
                        mismaArea = false;
                    }
                    
                } else {
                    //Parece que aun no se ha rellenado el campo area en la 
                    //entidad sesion. Por tanto no metemos ningún participante.
                    mismaArea = false;
                }
            }
        } catch(ISPACException e) {
            throw new ISPACException(e);
        }
        
        return mismaArea;
    }
    
}