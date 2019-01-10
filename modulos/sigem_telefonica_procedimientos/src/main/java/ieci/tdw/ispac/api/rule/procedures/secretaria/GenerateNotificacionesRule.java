package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil.ConstantesTagsSsVariables;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateNotificacionesRule implements IRule {

    private OpenOfficeHelper ooHelper = null;
    
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

            //Obtener las propuestas y urgencias incluidas en la sesión
            List<?> listPropuestas = SecretariaUtil.getPropuestasYUrgencias(cct, rulectx.getNumExp());
            Iterator<?> it = listPropuestas.iterator();
            IItem iProp = null;

            //Para cada propuesta se generan los tags a incluir en el documento de notificación
            String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
            boolean esAcuerdo = strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0;
            String extracto = "";
            String acuerdos = "";
            String dictamen = "";
            SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
            String fecha = dateformat.format(new Date());
            String numAcuerdo = "";
            String numDictamen = "";
            
            while (it.hasNext()) {
                iProp = (IItem)it.next();
                String numexpOrigen = iProp.getString("NUMEXP_ORIGEN");
                
                if (iProp.get("EXTRACTO")!=null) {
                    extracto = (String)iProp.get("EXTRACTO"); 
                    
                } else {
                    extracto = "";
                }
                cct.setSsVariable("EXTRACTO", extracto);
                cct.setSsVariable("FECHA", fecha);
                
                if (esAcuerdo) {
                    
                    if (iProp.get("ACUERDOS")!=null) {
                        acuerdos = (String)iProp.get("ACUERDOS"); 
                        
                    } else {
                        acuerdos = "";
                    }
                    acuerdos = acuerdos.replaceAll("\r\n", "\r");
                    cct.setSsVariable("ACUERDOS", acuerdos);
                    numAcuerdo = createNumero(rulectx, numexpOrigen, "SECR_ACUERDO"); 
                    cct.setSsVariable("NUMERO_ACUERDO", numAcuerdo);
                    
                } else {
                    if (iProp.get("DICTAMEN")!=null) {
                        dictamen = (String)iProp.get("DICTAMEN");
                        
                    } else {
                        dictamen = "";
                    }
                    dictamen = dictamen.replaceAll("\r\n", "\r");
                    cct.setSsVariable("DICTAMEN", dictamen);
                    numDictamen = createNumero(rulectx, numexpOrigen, "SECR_DICTAMEN"); 
                    cct.setSsVariable("NUMERO_DICTAMEN", numDictamen);
                }
                
                //Se genera una notificación por cada participante de la propuesta
                IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, numexpOrigen, "(ROL != 'TRAS' OR ROL IS NULL)", ParticipantesUtil.ID);
                Iterator<?> itParticipante = participantes.iterator();
                
                while (itParticipante.hasNext()) {
                    IItem participante = (IItem)itParticipante.next();

                    DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                    
                    //Generación del documento
                    String strNombreDoc = "";
                    String strNombreDocCab = "";
                    String strNombreDocPie = "";
                    String strNombreDocAcu = "";
                    String strNumero = esAcuerdo? "NUMERO_ACUERDO" : "NUMERO_DICTAMEN";
                    String strDescr = cct.getSsVariable(strNumero) + " - " + cct.getSsVariable(ConstantesTagsSsVariables.NOMBRE);
                    
                    if (esAcuerdo) {
                        strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifAc");
                        strNombreDocCab = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifAcCab");
                        strNombreDocPie = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifAcPie");
                        strNombreDocAcu = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-AcuseAc");
                        
                    } else {
                        strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifDi");
                        strNombreDocCab = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifDiCab");
                        strNombreDocPie = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-NotifDiPie");
                        strNombreDocAcu = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-AcuseDi");
                    }
                    
                    CommonFunctions.generarDocumento(rulectx, strNombreDocCab, null);
                    CommonFunctions.generarDocumento(rulectx, strNombreDocPie, null);
                    CommonFunctions.concatenaPartes(rulectx, numexpOrigen, strNombreDocCab, strNombreDocPie, strNombreDoc, strDescr, ooHelper);

                    //Generación del acuse de recibo
                    CommonFunctions.generarDocumento(rulectx, strNombreDocAcu, strDescr);
                    
                    DocumentosUtil.borraParticipanteSsVariable(cct);
                }
                
                cct.deleteSsVariable("EXTRACTO");
                cct.deleteSsVariable("FECHA");
                
                if (esAcuerdo) {
                    cct.deleteSsVariable("ACUERDOS");
                    cct.deleteSsVariable("NUMERO_ACUERDO");
                    
                } else {
                    cct.deleteSsVariable("DICTAMEN");
                    cct.deleteSsVariable("NUMERO_DICTAMEN");
                }
                
                //Guardo el número de acuerdo en la propuesta de origen
                String strNumero = esAcuerdo? numAcuerdo:numDictamen;
                IItemCollection props = entitiesAPI.getEntities("SECR_PROPUESTA", numexpOrigen);
                Iterator<?> itProps = props.iterator();
                
                if (itProps.hasNext()) {
                    IItem propuesta = (IItem)itProps.next();
                    propuesta.set("NUM_ACUERDO", strNumero);
                    propuesta.store(cct);
                }
            }
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido generar las notificaciones",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }

    private String createNumero(IRuleContext rulectx, String numExp, String strTabla) throws ISPACException {
        String numAcuerdo = "?";
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            //Primero se comprueba si el número de acuerdo ya existe
            String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND NUMEXP_ORIGEN = '" + numExp + "'";
            IItemCollection collection = entitiesAPI.queryEntities(strTabla, strQuery);
            Iterator<?> it = collection.iterator();
            
            if (it.hasNext()) {
                //Existe, se devuelve el numero como está
                IItem iAcuerdo = (IItem)it.next();
                numAcuerdo = iAcuerdo.getString("NUMERO") + "/" + iAcuerdo.getString("YEAR");
                
            } else {
                //No existe, hay que crearlo
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
    
                strQuery = "WHERE YEAR = '" + year + "'";
                collection = entitiesAPI.queryEntities(strTabla, strQuery);
                int numero = collection.toList().size();
                numero = numero + 1;
                numAcuerdo = numero + "/" + year;
                
                IItem iAcuerdo = entitiesAPI.createEntity(strTabla, rulectx.getNumExp());
                iAcuerdo.set("YEAR", year);
                iAcuerdo.set("NUMERO", numero);
                iAcuerdo.set("NUMEXP_ORIGEN", numExp);
                iAcuerdo.store(cct);
            }
            return numAcuerdo;
            
        }  catch(ISPACException e) {
            throw new ISPACException(e);
        }
    }
}