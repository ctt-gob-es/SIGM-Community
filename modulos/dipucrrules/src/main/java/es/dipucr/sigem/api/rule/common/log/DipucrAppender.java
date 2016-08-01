package es.dipucr.sigem.api.rule.common.log;

import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class DipucrAppender extends RollingFileAppender
{
	@SuppressWarnings("deprecation")
	public synchronized void doAppend(LoggingEvent event){
	    if (this.closed) {
	      LogLog.error("Attempted to append to closed appender named [" + this.name + "].");
	      return;
	    }

	    if (!(isAsSevereAsThreshold(event.getLevel()))) {
	      return;
	    }

	    Filter f = this.headFilter;

	    while (f != null) {
	      switch (f.decide(event))
	      {
	      case -1:
	        return;
	      case 1:
	        break;
	      case 0:
	        f = f.next;
	      }
	    }

	    String entidad = "---";
	    try{
	    	String entityId = "";
	    	OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
	    	if (info != null) {
				entityId = info.getOrganizationId();
			}
	    	if(StringUtils.isNotEmpty(entityId)){  		
	    		entidad = entityId;
	    	}
	    	else entidad = "---";
	    }
	    catch(Exception e){
	    	LogLog.error(e.getMessage(), e);
	    	entidad="ERROR";
	    }
	    
	    event = new LoggingEvent(Category.class.getName(),
	    		Logger.getInstance(event.getLoggerName()),
	    		event.getLevel(),
	    		"[" + entidad + "] - " +event.getRenderedMessage(), 
	    		(event.getThrowableInformation()!=null)?event.getThrowableInformation().getThrowable(): null);
	    append(event);
	  }
}
