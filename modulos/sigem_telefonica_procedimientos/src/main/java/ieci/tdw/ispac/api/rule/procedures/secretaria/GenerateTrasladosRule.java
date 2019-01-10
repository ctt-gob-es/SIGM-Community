package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.util.Iterator;
import java.util.List;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateTrasladosRule implements IRule {

    private OpenOfficeHelper ooHelper = null;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            //----------------------------------------------------------------------------------------------

            //Obtener las propuestas y urgencias incluidas en la sesión
            List<?> listPropuestas = SecretariaUtil.getPropuestasYUrgencias(cct, rulectx.getNumExp());
            Iterator<?> it = listPropuestas.iterator();
            IItem iProp = null;

            //Para cada propuesta se generan los tags a incluir en el certificado de acuerdos/dictamen
            String extracto = "";
            String acuerdos = "";
            String dictamen = "";
            String numAcuerdo = "";
            String numDictamen = "";
            String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
            boolean esAcuerdo = strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0;
            
            while (it.hasNext()) {
                iProp = (IItem)it.next();
                String numexpOrigen = iProp.getString("NUMEXP_ORIGEN");
                
                //Sólo generamos el documento si hace falta
                if (ParticipantesUtil.tieneTrasladados(cct, numexpOrigen)) {
                    
                    if (iProp.get("EXTRACTO")!=null){
                        extracto = (String)iProp.get("EXTRACTO");
                        
                    } else{
                        extracto = "";
                    }                    
                    cct.setSsVariable("EXTRACTO", extracto);
                    
                    if (esAcuerdo) {
                        if (iProp.get("ACUERDOS")!=null){
                            acuerdos = (String)iProp.get("ACUERDOS");
                            
                        } else {
                            acuerdos = "";
                        }
                        acuerdos = acuerdos.replaceAll("\r\n", "\r");
                        cct.setSsVariable("ACUERDOS", acuerdos);
                        numAcuerdo = SecretariaUtil.getNumero(cct, numexpOrigen, "SECR_ACUERDO");
                        cct.setSsVariable("NUMERO_ACUERDO", numAcuerdo);
                        
                    } else {
                        
                        if (iProp.get("DICTAMEN")!=null){
                            dictamen = (String)iProp.get("DICTAMEN");
                        
                        } else {
                            dictamen = "";
                        }
                        dictamen = dictamen.replaceAll("\r\n", "\r");
                        cct.setSsVariable("DICTAMEN", dictamen);
                        numDictamen = SecretariaUtil.getNumero(cct, numexpOrigen, "SECR_DICTAMEN");
                        cct.setSsVariable("NUMERO_DICTAMEN", numDictamen);
                    }
                    
                    //Generación del documento
                    String strNombreDoc = "";
                    String strNombreDocCab = "";
                    String strNombreDocPie = "";
                    String strNumero = esAcuerdo? "NUMERO_ACUERDO":"NUMERO_DICTAMEN";
                    String strDescr = cct.getSsVariable(strNumero);
                    
                    if (esAcuerdo) {
                        strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-TrasAc");
                        strNombreDocCab = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-TrasAcCab");
                        strNombreDocPie = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-TrasAcPie");
                        
                    } else {
                        strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-TrasDi");
                        strNombreDocCab = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-TrasDiCab");
                        strNombreDocPie = DocumentosUtil.getNombreTipoDocByCod(cct, "Secr-TrasDiPie");
                    }
                    
                    CommonFunctions.generarDocumento(rulectx, strNombreDocCab, null);
                    CommonFunctions.generarDocumento(rulectx, strNombreDocPie, null);
                    CommonFunctions.concatenaPartes(rulectx, numexpOrigen, strNombreDocCab, strNombreDocPie, strNombreDoc, strDescr, ooHelper);
                    
                    cct.deleteSsVariable("EXTRACTO");
                    
                    if (esAcuerdo) {
                        cct.deleteSsVariable("ACUERDOS");                
                        cct.deleteSsVariable("NUMERO_ACUERDO");
                        
                    } else {
                        cct.deleteSsVariable("DICTAMEN");                
                        cct.deleteSsVariable("NUMERO_DICTAMEN");
                    }
                }
            }
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se han podido generar las certificaciones",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}