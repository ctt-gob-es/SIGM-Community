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

import es.msssi.dir3.api.dao.UnitDao;
import es.msssi.dir3.api.dao.impl.UnitDaoImpl;
import es.msssi.dir3.api.type.OperatorCriterionEnum;
import es.msssi.dir3.api.type.UOCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataUnitVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.UnitVO;

@ContextConfiguration({ "/jndi.xml", "/beans/cxf.xml", "/beans/fwktd-dir3-test-beans.xml",
    "/beans/dir3-api-test-applicationContext.xml" })
public class DatosBasicosUnidadDaoImplTest extends AbstractJUnit4SpringContextTests {

    protected static final String ID_UNIDAD_ORGANICA_EXISTENTE = "E04921901";
    protected static final String ID_UNIDAD_ORGANICA_NO_EXISTENTE = "XXXX";

    @Autowired
    private UnitDaoImpl datosBasicosUnidadDaoImpl;

    protected UnitDao getDatosBasicosUnidadOrganicaDao() {
	return datosBasicosUnidadDaoImpl;
    }

    @Test
    public void testDao() {
	Assert.assertNotNull(getDatosBasicosUnidadOrganicaDao());
    }

    @Test
    public void testCount() {
	int count = 0;
	try {
	    count = getDatosBasicosUnidadOrganicaDao().count(
		null);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(count > 0);
	System.out.print("Cantidad de unidad totales: " +
	    count);
    }

    @Test
    public void testCountUnidades() {

	// Test de consulta con criterios
	int count = 0;
	try {
	    List<Criterion<UOCriterionEnum>> criteria = new ArrayList<Criterion<UOCriterionEnum>>();
	    ;
	    criteria.add(new Criterion<UOCriterionEnum>(
		UOCriterionEnum.UO_ESTADO, OperatorCriterionEnum.IN, new String[] { "V" }));
	    count = getDatosBasicosUnidadOrganicaDao().count(
		criteria);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "No se han obtenido resultados", count > 0);
	System.out.print("Cantidad de unidades con criterios: " +
	    count);
    }

    @Test
    public void testExists() {
	try {
	    Assert.assertTrue(
		"No se han obtenido resultados ", getDatosBasicosUnidadOrganicaDao().exists(
		    ID_UNIDAD_ORGANICA_EXISTENTE));
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testGet() {
	UnitVO unidad = null;
	try {
	    unidad = getDatosBasicosUnidadOrganicaDao().get(
		ID_UNIDAD_ORGANICA_EXISTENTE);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	Assert.assertTrue(
	    "No se han obtenido resultados", unidad != null);
    }

    @Test
    public void testFindUnidades() {

	// Test de consulta simple
	ArrayList<UnitVO> unidades = null;
	// try {
	// unidades = getDatosBasicosUnidadOrganicaDao().find(
	// null);
	// }
	// catch (SQLException e) {
	// e.printStackTrace();
	// }
	// Assert.assertNotNull(
	// "Resultado nulo", unidades);
	// Assert.assertTrue(
	// "No se han obtenido resultados", unidades.size() > 0);

	// Test de consulta simple
	try {
	    unidades = (ArrayList<UnitVO>) getDatosBasicosUnidadOrganicaDao().find(
		createCriterios());
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}

	// new Criterios<CriterioUnidadOrganicaEnum>().addCriterio(new
	// Criterio<CriterioUnidadOrganicaEnum>(
	// CriterioUnidadOrganicaEnum.UO_NOMBRE, OperadorCriterioEnum.LIKE,
	// "MINISTERIO")).addCriterio(new Criterio<CriterioUnidadOrganicaEnum>(
	// CriterioUnidadOrganicaEnum.UO_ESTADO, "V"));
	Assert.assertNotNull(
	    "Resultado nulo", unidades);
	Assert.assertTrue(
	    "No se han obtenido resultados", unidades.size() > 0);
	System.out.print("Número de unidades: " +
	    unidades.size());
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
    public void testFindBasicData() {

	// Test de consulta simple
	ArrayList<BasicDataUnitVO> unidades = null;

	// Test de consulta simple
	try {
	    unidades =
		(ArrayList<BasicDataUnitVO>) getDatosBasicosUnidadOrganicaDao().findBasicData(
		    createCriterios());
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}

	Assert.assertNotNull(
	    "Resultado nulo", unidades);
	Assert.assertTrue(
	    "No se han obtenido resultados", unidades.size() > 0);
	System.out.print("Número de unidades: " +
	    unidades.size());

    }

    protected Criteria<UOCriterionEnum> createCriterios() {
	return new Criteria<UOCriterionEnum>()
	// .addCriterion(
	// new Criterion<UOCriterionEnum>(
	// UOCriterionEnum.UO_ID, OperatorCriterionEnum.EQUAL, "L01350081"))
	// .addCriterion(
	// new Criterion<UOCriterionEnum>(
	// UOCriterionEnum.UO_NOMBRE, OperatorCriterionEnum.LIKE, "Conséjo"))
	    .addCriterion(
		new Criterion<UOCriterionEnum>(
		    UOCriterionEnum.UO_ESTADO, OperatorCriterionEnum.IN, new String[] { "V" }))
	    // .addCriterion(
	    // new Criterion<UOCriterionEnum>(
	    // UOCriterionEnum.UO_ID_EXTERNO_FUENTE,
	    // OperatorCriterionEnum.NOTNULL, null))
	    // .addCriterion(
	    // new Criterion<UOCriterionEnum>(
	    // UOCriterionEnum.UO_ID_UNIDAD_ORGANICA_SUPERIOR,
	    // OperatorCriterionEnum.EQUAL, "LA9999999"))
	    // .addCriterion(
	    // new Criterion<UOCriterionEnum>(
	    // UOCriterionEnum.UO_NOMBRE_UNIDAD_ORGANICA_SUPERIOR,
	    // OperatorCriterionEnum.LIKE, "locál"))
	    .addOrderBy(
		UOCriterionEnum.UO_NOMBRE);
	// .setPageInfo(
	// new PageInfo(
	// 1, 10))
	/*
	 * CriterioUnidadOrganicaEnum.UO_NOMBRE, OperadorCriterioEnum.LIKE,
	 * "MINISTERIO")) .setPageInfo( new PageInfo( 22, 10)).addOrderBy(
	 * CriterioUnidadOrganicaEnum.UO_NOMBRE); * .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_ID, OperadorCriterioEnum.EQUAL,
	 * ID_UNIDAD_ORGANICA_EXISTENTE)) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_NIVEL_ADMINISTRACION,
	 * OperadorCriterioEnum.IN, new String[] { "1", "2", "3" }))
	 * .addCriterio(new Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_DESCRIPCION_NIVEL_ADMINISTRACION,
	 * OperadorCriterioEnum.LIKE, "Local")) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_INDICADOR_ENTIDAD_DERECHO_PUBLICO,
	 * OperadorCriterioEnum.EQUAL, "N")) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_ID_EXTERNO_FUENTE,
	 * OperadorCriterioEnum.EQUAL, "01330447"))
	 * 
	 * .addCriterio(new Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_ID_UNIDAD_ORGANICA_SUPERIOR,
	 * OperadorCriterioEnum.EQUAL, "L01330447")) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_NOMBRE_UNIDAD_ORGANICA_SUPERIOR,
	 * OperadorCriterioEnum.LIKE, "Oviedo")) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_ID_UNIDAD_ORGANICA_PRINCIPAL,
	 * OperadorCriterioEnum.EQUAL, "L01330447")) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_NOMBRE_UNIDAD_ORGANICA_PRINCIPAL,
	 * OperadorCriterioEnum.LIKE, "Oviedo")) // .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>( //
	 * CriterioUnidadOrganicaEnum.UO_ID_UNIDAD_ORGANICA_EDP, //
	 * OperadorCriterioEnum.EQUAL, // "L01330447")) // .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>( //
	 * CriterioUnidadOrganicaEnum.UO_NOMBRE_UNIDAD_ORGANICA_EDP, //
	 * OperadorCriterioEnum.LIKE, // "Oviedo")) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_NIVEL_JERARQUICO,
	 * OperadorCriterioEnum.IN, new int[] { 1, 2} ))
	 * 
	 * .addCriterio(new Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_ESTADO, OperadorCriterioEnum.IN, new
	 * String[] { "V", "X" })) .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>(
	 * CriterioUnidadOrganicaEnum.UO_DESCRIPCION_ESTADO,
	 * OperadorCriterioEnum.LIKE, "Vigente")) // .addCriterio(new
	 * Criterio<CriterioUnidadOrganicaEnum>( //
	 * CriterioUnidadOrganicaEnum.UO_FECHA_ALTA, //
	 * OperadorCriterioEnum.EQUAL_OR_LESS_THAN, // new Date())) //
	 * .addCriterio(new Criterio<CriterioUnidadOrganicaEnum>( //
	 * CriterioUnidadOrganicaEnum.UO_FECHA_BAJA, //
	 * OperadorCriterioEnum.EQUAL_OR_LESS_THAN, // new Date())) //
	 * .addCriterio(new Criterio<CriterioUnidadOrganicaEnum>( //
	 * CriterioUnidadOrganicaEnum.UO_FECHA_EXTINCION, //
	 * OperadorCriterioEnum.EQUAL_OR_LESS_THAN, // new Date())) //
	 * .addCriterio(new Criterio<CriterioUnidadOrganicaEnum>( //
	 * CriterioUnidadOrganicaEnum.UO_FECHA_ANULACION, //
	 * OperadorCriterioEnum.EQUAL_OR_LESS_THAN, // new Date()))
	 * .addOrderBy(CriterioUnidadOrganicaEnum.UO_ID)
	 * .addOrderBy(CriterioUnidadOrganicaEnum.UO_NOMBRE);
	 */
    }

}
