/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import es.msssi.dir3.api.dao.OfficeDao;
import es.msssi.dir3.api.dao.impl.OfficeDaoImpl;
import es.msssi.dir3.api.type.OfficeCriterionEnum;
import es.msssi.dir3.api.type.OperatorCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataOfficeVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.OfficeVO;

@ContextConfiguration({ "/jndi.xml", "/beans/cxf.xml", "/beans/fwktd-dir3-test-beans.xml",
    "/beans/dir3-api-test-applicationContext.xml" })
public class DatosBasicosOficinaDaoImplTest extends AbstractJUnit4SpringContextTests {

    protected static final String ID_OFICINA_EXISTENTE = "O00001474";
    protected static final String ID_OFICINA_NO_EXISTENTE = "XXXX";

    protected static final String ID_UNIDAD_ORGANICA_EXISTENTE = "L01330447";
    protected static final String ID_UNIDAD_ORGANICA_NO_EXISTENTE = "XXXX";

    @Autowired
    private OfficeDaoImpl datosBasicosOficinaDaoImpl;

    protected OfficeDao getDatosBasicosOficinaDao() {
	return datosBasicosOficinaDaoImpl;
    }

    @Test
    public void testDao() {
	Assert.assertNotNull(getDatosBasicosOficinaDao());
    }

    @Test
    public void testCount() {
	int count = 0;
	try {
	    count = getDatosBasicosOficinaDao().count(
		null);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(count > 0);
	System.out.print("Cantidad de oficinas totales: " +
	    count);
    }

    @Test
    public void testCountOficinas() {

	// Test de consulta con criterios
	int count = 0;
	try {
	    List<Criterion<OfficeCriterionEnum>> criteria =
		new ArrayList<Criterion<OfficeCriterionEnum>>();
	    ;
	    criteria
		.add(new Criterion<OfficeCriterionEnum>(
		    OfficeCriterionEnum.OFICINA_ESTADO, OperatorCriterionEnum.IN,
		    new String[] { "V" }));
	    count = getDatosBasicosOficinaDao().count(
		criteria);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "No se han obtenido resultados", count > 0);
	System.out.print("Cantidad de oficinas con criterios: " +
	    count);
    }

    @Test
    public void testExists() {
	try {
	    Assert.assertTrue(
		"No se han obtenido resultados ", getDatosBasicosOficinaDao().exists(
		    ID_OFICINA_EXISTENTE));
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testGet() {
	OfficeVO oficina = null;
	try {
	    oficina = getDatosBasicosOficinaDao().get(
		ID_OFICINA_EXISTENTE);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "No se han obtenido resultados", oficina != null);
    }

    @Test
    public void testFindOficinas() {

	// Test de consulta simple
	ArrayList<OfficeVO> oficinas = null;
	// oficinas= getDatosBasicosOficinaDao().find(null);
	// Assert.assertNotNull("Resultado nulo", oficinas);
	// Assert.assertTrue("No se han obtenido resultados", oficinas.size() >
	// 0);

	// Test de consulta simple
	try {
	    oficinas =
		(ArrayList<OfficeVO>) getDatosBasicosOficinaDao().find(
		    new Criteria<OfficeCriterionEnum>()
			.addCriterion(new Criterion<OfficeCriterionEnum>(
			    OfficeCriterionEnum.OFICINA_ESTADO, "V"))
		// .addCriterion(
		// new Criterion<OfficeCriterionEnum>(
		// OfficeCriterionEnum.OFICINA_NOMBRE,
		// OperatorCriterionEnum.LIKE,
		// "AYUNTAMIENTO")).setPageInfo(
		// new PageInfo(
		// 1, 10)).addOrderBy(
		// OfficeCriterionEnum.OFICINA_ID)
		    );
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}

	// Assert.assertNotNull(
	// "Resultado nulo", oficinas);
	// Assert.assertTrue(
	// "No se han obtenido resultados", oficinas.size() > 0);
	System.out.print("Número de oficinas: " +
	    oficinas.size());
	// Test de consulta con criterios
	/*
	 * List<DatosBasicosOficinaVO> oficinas =
	 * getDatosBasicosOficinaDao().findOficinas(createCriterios());
	 * Assert.assertNotNull("Resultado nulo", oficinas);
	 * Assert.assertTrue("No se han obtenido resultados", oficinas.size() >
	 * 0);
	 * 
	 * // Test de consulta con criterios oficinas =
	 * getDatosBasicosOficinaDao().findOficinas(new
	 * Criterios<CriterioOficinaEnum>() .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_INDICADOR_ADHESION_SIR,
	 * OperadorCriterioEnum.EQUAL, "S")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.OFICINA_ESTADO,
	 * OperadorCriterioEnum.IN, new String[] { "V", "X" })) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_ID_UNIDAD_RELACIONADA,
	 * OperadorCriterioEnum.IN, new String[] { ID_UNIDAD_ORGANICA_EXISTENTE,
	 * "E00004101" })) .addOrderBy(CriterioOficinaEnum.OFICINA_ID)
	 * .addOrderBy(CriterioOficinaEnum.OFICINA_NOMBRE));
	 * 
	 * Assert.assertNotNull("Resultado nulo", oficinas);
	 * Assert.assertTrue("No se han obtenido resultados", oficinas.size() >
	 * 0);
	 */
    }

    @Test
    public void testBasicDataOfficeVO() {

	// Test de consulta simple
	ArrayList<BasicDataOfficeVO> oficinas = null;
	// oficinas= getDatosBasicosOficinaDao().find(null);
	// Assert.assertNotNull("Resultado nulo", oficinas);
	// Assert.assertTrue("No se han obtenido resultados", oficinas.size() >
	// 0);

	// Test de consulta simple
	try {
	    oficinas = (ArrayList<BasicDataOfficeVO>) getDatosBasicosOficinaDao().findBasicData(
		null
	    // new Criteria<OfficeCriterionEnum>()
	    // .addCriterion(
	    // new Criterion<OfficeCriterionEnum>(
	    // OfficeCriterionEnum.OFICINA_ESTADO, "V"))
	    // .addCriterion(
	    // new Criterion<OfficeCriterionEnum>(
	    // OfficeCriterionEnum.OFICINA_NOMBRE, OperatorCriterionEnum.LIKE,
	    // "AYUNTAMIENTO")).setPageInfo(
	    // new PageInfo(
	    // 1, 10)).addOrderBy(
	    // OfficeCriterionEnum.OFICINA_ID)
		);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}

	// Assert.assertNotNull(
	// "Resultado nulo", oficinas);
	// Assert.assertTrue(
	// "No se han obtenido resultados", oficinas.size() > 0);
	System.out.print("Número de oficinas: " +
	    oficinas.size());

    }

    protected void assertDatosBasicosOficina(
	OfficeVO oficina) {

	Assert.assertNotNull(oficina);

	Assert.assertEquals(
	    ID_OFICINA_EXISTENTE, oficina.getId());
	Assert.assertEquals(
	    "BIBLIOTECA NACIONAL", oficina.getName());
	Assert.assertEquals(
	    "000004", oficina.getExternalId());

	Assert.assertEquals(
	    "E04584101", oficina.getResponsibleUnitId());
	/*
	 * Assert.assertEquals("MINISTERIO DE CULTURA",
	 * oficina.getNombreUnidadResponsable());
	 * Assert.assertNull(oficina.getNivelAdministracion());
	 * 
	 * Assert.assertEquals("S", oficina.getIndicadorAdhesionSIR());
	 * Assert.assertEquals("S", oficina.getIndicadorOficinaRegistro());
	 * Assert.assertEquals("N", oficina.getIndicadorOficinaInformacion());
	 * Assert.assertEquals("N", oficina.getIndicadorOficinaTramitacion());
	 * Assert.assertEquals("N", oficina.getIndicadorRegistroElectronico());
	 * Assert.assertEquals("N",
	 * oficina.getIndicadorIntercambioSinRestriccion());
	 * Assert.assertEquals("N",
	 * oficina.getIndicadorIntercambioLocalEstatal());
	 * Assert.assertEquals("N",
	 * oficina.getIndicadorIntercambioLocalAutonomicoRestringido());
	 * Assert.assertEquals("N",
	 * oficina.getIndicadorIntercambioLocalAutonomicoGeneral());
	 * Assert.assertEquals("N",
	 * oficina.getIndicadorIntercambioLocalLocalRestringido());
	 * Assert.assertEquals("N",
	 * oficina.getIndicadorIntercambioLocalLocalGeneral());
	 * Assert.assertEquals("N",
	 * oficina.getIndicadorIntercambioAytoAytoRestringido());
	 * 
	 * Assert.assertEquals("V", oficina.getEstado());
	 * Assert.assertEquals("Vigente", oficina.getDescripcionEstado());
	 */
	Assert.assertNull(oficina.getCreationDate());
	Assert.assertNull(oficina.getExtinctionDate());
	Assert.assertNull(oficina.getAnnulationDate());
    }

    protected Criteria<OfficeCriterionEnum> createCriterios() {
	// return null;
	return new Criteria<OfficeCriterionEnum>()
	    .addCriterion(
		new Criterion<OfficeCriterionEnum>(
		    OfficeCriterionEnum.OFICINA_ESTADO, OperatorCriterionEnum.IN, new String[] {
			"V", "X" }))
	    .addCriterion(
		new Criterion<OfficeCriterionEnum>(
		    OfficeCriterionEnum.OFICINA_ID, "O00000643"))
	    .addCriterion(
		new Criterion<OfficeCriterionEnum>(
		    OfficeCriterionEnum.OFICINA_NOMBRE, OperatorCriterionEnum.LIKE, "Registró"))
	    .addCriterion(
		new Criterion<OfficeCriterionEnum>(
		    OfficeCriterionEnum.OFICINA_ID_UNIDAD_RESPONSABLE, OperatorCriterionEnum.EQUAL,
		    "L01280565"))
	    .addCriterion(
		new Criterion<OfficeCriterionEnum>(
		    OfficeCriterionEnum.OFICINA_ID_EXTERNO_FUENTE, OperatorCriterionEnum.NULL, null));
	/*
	 * return new Criterios<CriterioOficinaEnum>()
	 * 
	 * .addCriterio(new Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_ID, OperadorCriterioEnum.EQUAL,
	 * ID_OFICINA_EXISTENTE)) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.OFICINA_NOMBRE,
	 * OperadorCriterioEnum.LIKE, "NACIONAL")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_ID_EXTERNO_FUENTE,
	 * OperadorCriterioEnum.EQUAL, "000004"))
	 * 
	 * .addCriterio(new Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_ID_UNIDAD_RESPONSABLE,
	 * OperadorCriterioEnum.EQUAL, "E04584101")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_NOMBRE_UNIDAD_RESPONSABLE,
	 * OperadorCriterioEnum.LIKE, "CULTURA")) // .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( //
	 * CriterioOficinaEnum.OFICINA_NIVEL_ADMINISTRACION, //
	 * OperadorCriterioEnum.IN, // new String[] { "", ""})) //
	 * .addCriterio(new Criterio<CriterioOficinaEnum>( //
	 * CriterioOficinaEnum .OFICINA_DESCRIPCION_NIVEL_ADMINISTRACION, //
	 * OperadorCriterioEnum.LIKE, // ""))
	 * 
	 * .addCriterio(new Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_INDICADOR_ADHESION_SIR,
	 * OperadorCriterioEnum.EQUAL, "S")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_INDICADOR_OFICINA_REGISTRO,
	 * OperadorCriterioEnum.EQUAL, "S")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_INDICADOR_OFICINA_INFORMACION ,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_INDICADOR_OFICINA_TRAMITACION ,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_INDICADOR_REGISTRO_ELECTRONICO ,
	 * OperadorCriterioEnum.EQUAL, "N"))
	 * 
	 * .addCriterio(new Criterio<CriterioOficinaEnum>( CriterioOficinaEnum
	 * .OFICINA_INDICADOR_INTERCAMBIO_SIN_RESTRICCION,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.
	 * OFICINA_INDICADOR_INTERCAMBIO_LOCAL_ESTATAL,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.
	 * OFICINA_INDICADOR_INTERCAMBIO_AUTONOMICO_RESTRINGIDO,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.
	 * OFICINA_INDICADOR_INTERCAMBIO_AUTONOMICO_GENERAL,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.
	 * OFICINA_INDICADOR_INTERCAMBIO_LOCAL_RESTRINGIDO,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.
	 * OFICINA_INDICADOR_INTERCAMBIO_LOCAL_GENERAL,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( CriterioOficinaEnum.
	 * OFICINA_INDICADOR_INTERCAMBIO_AYTO_AYTO_RESTRINGIDO,
	 * OperadorCriterioEnum.EQUAL, "N"))
	 * 
	 * .addCriterio(new Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_ESTADO, OperadorCriterioEnum.IN, new
	 * String[] { "V", "X" })) .addCriterio(new
	 * Criterio<CriterioOficinaEnum>(
	 * CriterioOficinaEnum.OFICINA_DESCRIPCION_ESTADO,
	 * OperadorCriterioEnum.LIKE, "Vigente")) // .addCriterio(new
	 * Criterio<CriterioOficinaEnum>( //
	 * CriterioOficinaEnum.OFICINA_FECHA_CREACION, //
	 * OperadorCriterioEnum.EQUAL_OR_LESS_THAN, // new Date())) //
	 * .addCriterio(new Criterio<CriterioOficinaEnum>( //
	 * CriterioOficinaEnum.OFICINA_FECHA_EXTINCION, //
	 * OperadorCriterioEnum.EQUAL_OR_LESS_THAN, // new Date())) //
	 * .addCriterio(new Criterio<CriterioOficinaEnum>( //
	 * CriterioOficinaEnum.OFICINA_FECHA_ANULACION, //
	 * OperadorCriterioEnum.EQUAL_OR_LESS_THAN, // new Date()))
	 * .addOrderBy(CriterioOficinaEnum.OFICINA_ID)
	 * .addOrderBy(CriterioOficinaEnum.OFICINA_NOMBRE);
	 */
    }
}
