package es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.impl;

import com.ieci.tecdoc.common.entity.AxXfEntity;
import com.ieci.tecdoc.common.entity.dao.DBEntityDAOFactory;
import com.ieci.tecdoc.common.isicres.AxXfPK;
import com.ieci.tecdoc.common.repository.helper.ISRepositoryDocumentHelper;
import com.ieci.tecdoc.common.repository.vo.ISRepositoryRetrieveDocumentVO;
import com.ieci.tecdoc.isicres.repository.RepositoryFactory;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao.ExportacionMirDAO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.ExportacionMirManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.InfoRegistroPageRepositoryVO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.List;


public class ExportacionMirManagerImpl implements ExportacionMirManager {
    /**
     * Manager para leer y actualizar la bandeja de entrada de intercambio
     * registral
     */
    protected ExportacionMirDAO exportacionMirDao;

    private static Logger logger = Logger.getLogger(ExportacionMirManagerImpl.class);
    private static int fieldImpreso = 1001;


    @Override
    public List<BandejaEntradaItemVO> getRegistroMir(int idBook) {
        Integer idLibro = idBook;
        return getExportacionMirDao().getRegistrosLibro(idLibro);


    }

    @Override
    public List<InfoRegistroPageRepositoryVO> getDocusRegistro(int idRegistro, int idLibro) {
        Integer codRegistro = idRegistro;
        Integer codLibro = idLibro;
        return getExportacionMirDao().getDocumentos(codRegistro, codLibro);
    }

    public ExportacionMirDAO getExportacionMirDao() {
        return exportacionMirDao;
    }

    public void setExportacionMirDao(ExportacionMirDAO exportacionMirDao) {
        this.exportacionMirDao = exportacionMirDao;
    }

    @Override
    public void getExportarDocuMir(BandejaEntradaItemVO registro, int idLibro, String ruta) {
        logger.info("ENTRADA A getExportarDocuMir");
        List<InfoRegistroPageRepositoryVO> idsPaginasRegistro = getDocusRegistro(registro.getId().intValue(), idLibro);

        byte[] file;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // Se cambia la fecha de registro por la fecha de modificación del registro
        // String fecha = sdf.format(registro.getFechaRegistro());
        String fecha = sdf.format(registro.getFechaModificacion());
        

        for (InfoRegistroPageRepositoryVO page : idsPaginasRegistro)
            try {

                String entity = "000";
                MultiEntityContextHolder.setEntity(entity);
                ISRepositoryRetrieveDocumentVO findVO = ISRepositoryDocumentHelper
                        .getRepositoryRetrieveDocumentVO(idLibro, registro.getId().intValue(), Integer.parseInt(page.getIdPageh()), MultiEntityContextHolder.getEntity(), true);


                logger.info("antes del error " + registro.getIdRegistro().toString());
                ISRepositoryRetrieveDocumentVO retrieveVO = RepositoryFactory.getCurrentPolicy().retrieveDocument(findVO);
                logger.info("ARCHIVO EXISTE DE " + registro.getIdRegistro().toString());
                if (retrieveVO != null) {
                    file = retrieveVO.getFileContent();
                    if (file.length != 0) {
                        logger.info("CREACION DIRECTORIO " + registro.getIdRegistro().toString());

                        File directorio;
                        String rutaDirectorioCompleto;
                        if (StringUtils.isNotEmpty(registro.getNumeroRegistroOrigen())) {
                            rutaDirectorioCompleto = ruta + registro.getIdRegistro().toString() + "_" + fecha + "_" + registro.getNumeroRegistroOrigen() + "/";

                        } else {
                            rutaDirectorioCompleto = ruta + registro.getIdRegistro().toString() + "_" + fecha + "/";
                        }
                        directorio = new File(rutaDirectorioCompleto);
                        directorio.setWritable(true);
                        directorio.mkdirs();

                        logger.info("DIRECTORIO CREADO " + registro.getIdRegistro().toString());


                        IOUtils.write(file, new FileOutputStream(new File(rutaDirectorioCompleto + Normalizer.normalize(page.getNombre(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""))));

                        logger.info("FICHERO GENERADO " + registro.getIdRegistro().toString());
                        logger.info("INICIO Marcamos como impreso " + registro.getIdRegistro().toString());

                        AxXfPK axxpk = new AxXfPK();
                        axxpk.setType(String.valueOf(idLibro));
                        axxpk.setFdrId(registro.getId().intValue());
                        axxpk.setFldId(fieldImpreso);


                        //borramos por si ya estaba marcado como no leido
                        try {
                            AxXfEntity axXfEntity = new AxXfEntity();
                            AxXfPK byPrimaryKey = axXfEntity.findByPrimaryKey(axxpk, entity);
                            if (null != byPrimaryKey) {
                                axXfEntity.remove(axxpk, entity);
                            }
                        } catch (Exception e) {
                            logger.error("No se puede borrar el marcado impreso" + registro.getIdRegistro());
                        }


                        int nextId = DBEntityDAOFactory.getCurrentDBEntityDAO()
                                .getNextIdForExtendedField(idLibro, entity);
                        String dataBaseType = DBEntityDAOFactory.getCurrentDBEntityDAO()
                                .getDataBaseType();
                        AxXfEntity axXfEntity2 = new AxXfEntity();
                        axXfEntity2.create(axxpk, nextId, "S", DBEntityDAOFactory
                                        .getCurrentDBEntityDAO().getDBServerDate(entity),
                                entity, dataBaseType);

                        logger.info("FIN Marcamos como impreso " + registro.getIdRegistro().toString());
                    }
                }
            } catch (Exception e) {
                logger.error("Error en MIR", e);
            }


    }


}
