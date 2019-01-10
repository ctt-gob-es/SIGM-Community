package ieci.tdw.ispac.ispacweb.session;



import ieci.tdw.ispac.ispacweb.session.ISPACScheduler;
import junit.framework.TestCase;

/**
 * Test unitarios de la clase ISPACScheduler.
 * 
 * @author Iecisa
 * @version $Revision$
 * 
 */
public class ISPACSchedulerTest extends TestCase {

	public void testRun() {
		ISPACScheduler task = new ISPACScheduler();
		task.run();
	}
}
