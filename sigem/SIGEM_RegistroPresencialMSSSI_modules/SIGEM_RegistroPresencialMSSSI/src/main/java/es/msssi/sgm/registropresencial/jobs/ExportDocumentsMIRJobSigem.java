/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.jobs;


import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.ExportacionMirManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.msssi.sgm.registropresencial.beans.WebParameter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.FileReader;
import java.util.List;
import java.util.Properties;


public class ExportDocumentsMIRJobSigem extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(ExportDocumentsMIRJobSigem.class);
    private static ExportacionMirManager manager;

    /**
     * Constructor.
     */
    public ExportDocumentsMIRJobSigem() {
        super();
        if (manager == null) {
            manager = IsicresManagerProvider.getInstance().getExportacionManager();
        }
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("Inicio del job para exportar asientos MIR");

            //leemos el fichero properties para obtener el ID_LIBRO y la ruta
            Properties p = new Properties();

            String PATH_REPO = (String)WebParameter.getEntryParameter("PATH_REPO");
            p.load(new FileReader(PATH_REPO+"/config/SIGEM_RegistroPresencialMSSSIWeb/exportMIRJob.properties"));

            String ruta_imprimir = p.getProperty("registropresencial.jobs.mir.path");
            String id_libro= p.getProperty(String.valueOf("registropresencial.jobs.mir.book"));

            //descargamos los registros
            List<BandejaEntradaItemVO> registroMir = manager.getRegistroMir(Integer.parseInt(id_libro));
            //cogemos sus documentos y generamos la estructura de los directorios
            for (BandejaEntradaItemVO registro : registroMir) {
                //exportamos los documentos del registro
                manager.getExportarDocuMir(registro, Integer.parseInt(id_libro), ruta_imprimir);
            }
        } catch (Exception e) {
            logger.error("Error al exportar MIR", e);
        } finally {
            logger.info("Fin del job para exportar asientos MIR");
        }
    }
}

			
