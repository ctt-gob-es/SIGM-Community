package es.dipucr.contratacion.rule.doc;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

public class FormattedDateDocPeticionRule implements IRule{
	
	private Logger logger = Logger.getLogger(FormattedDateDocPeticionRule.class);
	
	private final String PATTERN = "EEEE, d 'de' MMMM 'de' yyyy";
	private final String INPUT_PATTERN = "dd'/'MM'/'yyyy";
	
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

    @SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
        	//Definición del formato de fecha
        	String pattern = rulectx.get("pattern");
        	if (StringUtils.isEmpty(pattern))
        		pattern = PATTERN;
        	String locale = rulectx.get("locale");
        	SimpleDateFormat dateformat=null;
        	SimpleDateFormat inputDateformat=null;
    		if (StringUtils.isEmpty(locale)){
    			dateformat=new SimpleDateFormat(pattern,new Locale("es"));
    			inputDateformat=new SimpleDateFormat(INPUT_PATTERN,new Locale("es"));
    		}else{
    	    	if (mMonths.get(locale) == null)
    	    		throw new ISPACRuleException("Valor de locale '" + locale + "' desconocido");
                DateFormatSymbols dateFormat = getDateFormatSymbols(locale);
                dateformat=new SimpleDateFormat(pattern, dateFormat);
                inputDateformat=new SimpleDateFormat(INPUT_PATTERN, dateFormat);
    		}

    		//Obtención de la fecha
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        String strDate = new String();
	        String strEntity = rulectx.get("entity");
	        String strProperty = rulectx.get("property");
	        String nombredoc = rulectx.get("nombredoc");
	        if ( ! StringUtils.isEmpty(strEntity) && 
	        	 ! StringUtils.isEmpty(strProperty) )
	        {
	        	//Calculo el número de expediente de la petición
	 	        String numexpPeticion = "";
	 	        String consulta = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato'";
	 	        logger.warn("consulta "+consulta);
	 	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consulta);
	 	        
	 	        Iterator<IItem> it = collection.iterator();
	 	        if (it.hasNext())
	 	        {
	 	        	numexpPeticion = ((IItem)it.next()).getString("NUMEXP_PADRE");
	 	        }
	        	
		        String strQuery = "WHERE NUMEXP = '" + numexpPeticion + "' AND NOMBRE LIKE '%"+nombredoc+"%'";
		        logger.warn("strQuery "+strQuery);
		        collection = entitiesAPI.queryEntities(strEntity, strQuery);
		        it = collection.iterator();
		        IItem item = null;
		        if (it.hasNext()) {
	            	item = ((IItem)it.next());
	            	strDate=item.getString(strProperty);
		        }
	        }
	        
	        logger.warn("fecha. "+strDate);
    		
    		//Escritura de la fecha
        	if ( ! StringUtils.isEmpty(strDate) )
        	{
        		
        		logger.warn("fecha. resultado "+inputDateformat.parse(strDate));
        		return dateformat.format(inputDateformat.parse(strDate));
        	}
        	else
        	{
        		//Inicio [eCenpri-Felipe #244]
        		//Si no encuentra la fecha indicada mira la propiedad wait
    	        String strWait = rulectx.get("wait");
    	        if ( ! StringUtils.isEmpty(strWait) && strWait.equals("true")){
    	        	String tag = "<ispactag rule='FormattedDateRule' entity='" + strEntity + 
    	        		"' property='" + strProperty +"' pattern=\"" + pattern + "\" wait='true' />";
    	        	return tag;
    	        }
    	        else{
	        		//Si no encuentra datos y no aparece la propiedad wait a true,
    	        	//entonces escribe la fecha actual
	        		return dateformat.format(new Date());
    	        }
        		//Fin [eCenpri-Felipe #244]
        	}
            
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
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
    }
}


