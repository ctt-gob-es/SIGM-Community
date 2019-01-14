package es.sigem.dipcoruna.desktop.framework.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.sigem.dipcoruna.framework.service.util.VersionUtil;

public class TestVersionUtil {

	@Test
	public void test() {
		assertEquals(0, VersionUtil.compareVersiones("1.0.0", "1.0.0"));
		assertEquals(0, VersionUtil.compareVersiones("0.0.1", "0.0.1"));

		assertEquals(-1, VersionUtil.compareVersiones("0.0.0", "0.0.1"));
		assertEquals(1, VersionUtil.compareVersiones("0.0.1", "0.0.0"));

		assertEquals(1, VersionUtil.compareVersiones("0.1.0", "0.0.0"));
		assertEquals(-1, VersionUtil.compareVersiones("0.0.0", "0.1.0"));

		assertEquals(1, VersionUtil.compareVersiones("1.0.0", "0.1.1"));
		assertEquals(-1, VersionUtil.compareVersiones("98.99.99", "99.0.0"));

		assertEquals(1, VersionUtil.compareVersiones("1.0.0-RC0", "0.1.1"));
		assertEquals(1, VersionUtil.compareVersiones("1.0.0", "1.0.0-RC9"));
		assertEquals(1, VersionUtil.compareVersiones("1.0.0-RC1", "1.0.0-RC0"));
		assertEquals(1, VersionUtil.compareVersiones("1.0.0-RC10", "1.0.0-RC9"));
		assertEquals(-1, VersionUtil.compareVersiones("1.0.0-RC0", "1.0.0-RC1"));
		assertEquals(0, VersionUtil.compareVersiones("1.0.0-RC2", "1.0.0-RC2"));


		assertEquals(0, VersionUtil.compareVersiones("1.0.0-RC1-SNAPSHOT", "1.0.0-RC1-Otra cosa"));
		assertEquals(1, VersionUtil.compareVersiones("1.0.0-RC2-SNAPSHOT", "1.0.0-RC1-SNAPSHOT"));
		assertEquals(1, VersionUtil.compareVersiones("1.0.0", "1.0.0-RC9-SNAPSHOT"));


	}
}
