package es.sigem.dipcoruna.desktop.scan.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;


public class TestUploadFilesServerResponseParser {

    @Test
    public void testSinErrores1() {
        final String response = "#\n#Resultado de la carga de documentos\n#\noks=\"KimqVykAEQWchfUBosdt.pdf\"\nerrors=";
        final UploadFilesServerResponseParser parser = new UploadFilesServerResponseParser(response);

        assertFalse(parser.huboError());
        assertTrue(parser.getErrors().isEmpty());
    }

    @Test
    public void testSinErrores2() {
        final String response = "#\n#Resultado de la carga de documentos\n#\nerrors=\nxxxxxxxxxxxxxxx";
        final UploadFilesServerResponseParser parser = new UploadFilesServerResponseParser(response);

        assertFalse(parser.huboError());
        assertTrue(parser.getErrors().isEmpty());
    }


    @Test
    public void testConErrores1() {
        final String response = "#\n#Resultado de la carga de documentos\n#\noks=\nerrors=\"KimqVykAEQWchfUBosdt.pdf\"\nxxxxxxxxxxxxxxx";
        final UploadFilesServerResponseParser parser = new UploadFilesServerResponseParser(response);

        assertTrue(parser.huboError());
        assertEquals(1, parser.getErrors().size());
    }

    @Test
    public void testConErrores2() {
        final String response = "#\n#Resultado de la carga de documentos\n#\noks=\nerrors=\"fichero1.pdf\",\"fichero2.pdf\"\nxxxxxxxxxxxxxxx";
        final UploadFilesServerResponseParser parser = new UploadFilesServerResponseParser(response);

        assertTrue(parser.huboError());
        final List<String> errors = parser.getErrors();

        assertEquals(2, errors.size());
        assertEquals("\"fichero1.pdf\"", errors.get(0));
        assertEquals("\"fichero2.pdf\"", errors.get(1));
    }
}
