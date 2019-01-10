package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public class FormattedDateRule implements IRule{
    
    private static final String PATTERN = "EEEE, d 'de' MMMM 'de' yyyy";
    private static final String INPUT_PATTERN = "dd'/'MM'/'yyyy";
    
    private static Map<String, String[]> mMonths = null;
    private static Map<String, String[]> mWeekDays = null;
    
    static{
        mMonths = new HashMap<String, String[]>();
        mMonths.put("gl", new String[]{"xaneiro", "febreiro", "marzo", "abril", "maio", "xuño", "xullo", "agosto", "setembro", "outubro", "novembro", "decembro"});
        mMonths.put("eu", new String[]{"urtarrila", "otsaila", "martxoa", "apirila", "maiatza", "ekaina", "uztaila", "abuztua", "iraila", "urria", "azaroa", "abendua"});
        mMonths.put("ca", new String[]{"gener", "febrer", "març", "abril", "maig", "juny", "juliol", "agost", "setembre", "octubre", "novembre", "desembre"});

        mWeekDays = new HashMap<String, String[]>();
        mWeekDays.put("gl", new String[]{"","domingo", "luns", "martes", "mércores", "xoves", "venres", "sábado" });
        mWeekDays.put("eu", new String[]{"","igandea","astelehena", "asteartea", "asteazkena", "osteguna", "ostirala", "larunbata"});
        mWeekDays.put("ca", new String[]{"","diumenge", "dilluns", "dimarts", "dimecres", "dijous", "divendres", "dissabte"});
    
    }
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //Definición del formato de fecha
            String pattern = rulectx.get("pattern");
            
            if (StringUtils.isEmpty(pattern)){
                pattern = PATTERN;
            }
            
            String locale = rulectx.get("locale");
            SimpleDateFormat dateformat=null;
            SimpleDateFormat inputDateformat=null;
            
            if (StringUtils.isEmpty(locale)){
                dateformat=new SimpleDateFormat(pattern,new Locale("es"));
                inputDateformat=new SimpleDateFormat(INPUT_PATTERN,new Locale("es"));
            
            } else {
                if (mMonths.get(locale) == null) {
                    throw new ISPACRuleException("Valor de locale '" + locale + "' desconocido");
                }
                
                DateFormatSymbols dateFormat = getDateFormatSymbols(locale);
                dateformat=new SimpleDateFormat(pattern, dateFormat);
                inputDateformat=new SimpleDateFormat(INPUT_PATTERN, dateFormat);
            }

            //Obtención de la fecha
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            String strDate = "";
            String strEntity = rulectx.get("entity");
            String strProperty = rulectx.get("property");
            
            if ( !StringUtils.isEmpty(strEntity) && !StringUtils.isEmpty(strProperty) ) {
                IItemCollection collection = entitiesAPI.getEntities(strEntity, rulectx.getNumExp());
                Iterator<?> it = collection.iterator();
                IItem item = null;
                
                if (it.hasNext()) {
                    item = (IItem)it.next();
                    strDate = item.getString(strProperty);
                }
            }
            
            //Escritura de la fecha
            if ( !StringUtils.isEmpty(strDate) ) {
                return dateformat.format(inputDateformat.parse(strDate));
                
            } else {
                //Inicio [eCenpri-Felipe #244]
                //Si no encuentra la fecha indicada mira la propiedad wait
                String strWait = rulectx.get("wait");
                
                if ( ! StringUtils.isEmpty(strWait) && "true".equals(strWait)){
                    return "<ispactag rule='FormattedDateRule' entity='" + strEntity + "' property='" + strProperty +"' pattern=\"" + pattern + "\" wait='true' />";
                } else {
                    //Si no encuentra datos y no aparece la propiedad wait a true,
                    //entonces escribe la fecha actual
                    return dateformat.format(new Date());
                }
                //Fin [eCenpri-Felipe #244]
            }
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }
    }

    private DateFormatSymbols getDateFormatSymbols(String locale) {
        
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        dateFormatSymbols.setMonths((String[]) mMonths.get(locale));
        dateFormatSymbols.setWeekdays((String[]) mWeekDays.get(locale));
        return dateFormatSymbols;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}


