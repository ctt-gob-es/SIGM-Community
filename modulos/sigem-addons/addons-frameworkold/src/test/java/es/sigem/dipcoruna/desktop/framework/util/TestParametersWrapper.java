package es.sigem.dipcoruna.desktop.framework.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import es.sigem.dipcoruna.framework.service.util.ParametersWrapper;


public class TestParametersWrapper {

	@Test
	public void testSimple() {
		String argumento ="protocol1:param1=a&param2=b";
		ParametersWrapper parametersWrapper = new ParametersWrapper(argumento);
		
		assertEquals("protocol1", parametersWrapper.getProtocolo());
		assertTrue(parametersWrapper.existeParametro("param1"));
		assertTrue(parametersWrapper.existeParametro("param2"));
		assertFalse(parametersWrapper.existeParametro("param99"));
		
		assertEquals("a", parametersWrapper.getSimpleValue("param1"));
		assertEquals("b", parametersWrapper.getSimpleValue("param2"));
		assertEquals(null, parametersWrapper.getSimpleValue("param99"));
	}
	
	@Test
	public void testParametrosListas() {
		String argumento ="protocol1:param1=a1&param1=a2&param2&param3=";
		ParametersWrapper parametersWrapper = new ParametersWrapper(argumento);
		
		assertEquals("protocol1", parametersWrapper.getProtocolo());
		assertTrue(parametersWrapper.existeParametro("param1"));
		assertTrue(parametersWrapper.existeParametro("param2"));
		assertTrue(parametersWrapper.existeParametro("param3"));
		
		List<String> valuesParam1 = parametersWrapper.getValue("param1");
		assertNotNull(valuesParam1);
		assertEquals(2, valuesParam1.size());
		assertEquals("a1", valuesParam1.get(0));
		assertEquals("a2", valuesParam1.get(1));
		
		
		List<String> valuesParam2 = parametersWrapper.getValue("param2");
		assertNotNull(valuesParam2);
		assertEquals(1, valuesParam2.size());
		assertNull( valuesParam2.get(0));
		
		
		List<String> valuesParam3 = parametersWrapper.getValue("param3");
		assertNotNull(valuesParam3);
		assertEquals(1, valuesParam3.size());
		assertNull( valuesParam3.get(0));	
		
		
		assertNull(parametersWrapper.getSimpleValue("param2"));
		assertNull(parametersWrapper.getSimpleValue("param3"));		
	}
	
	
	@Test(expected = RuntimeException.class)
	public void testParametroNoValido() {
		new ParametersWrapper("protocol");
	}
	
	@Test
	public void testParametroVacio() {
		ParametersWrapper parametersWrapper = new ParametersWrapper("");
		
		assertEquals(null, parametersWrapper.getProtocolo());		
	}
}
	