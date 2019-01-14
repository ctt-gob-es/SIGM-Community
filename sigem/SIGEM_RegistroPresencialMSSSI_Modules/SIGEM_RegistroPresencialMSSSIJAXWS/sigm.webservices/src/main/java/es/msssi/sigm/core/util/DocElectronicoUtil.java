/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import core.tools.hasher.HashType;
import core.tools.hasher.Hasher;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.business.vo.ConfiguracionUsuarioVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoFichero;

public class DocElectronicoUtil  { 
	
	
    private static DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager;
    
	static {
		if (documentoElectronicoAnexoManager == null) {
			documentoElectronicoAnexoManager = IsicresManagerProvider.getInstance()
					.getDocumentoElectronicoAnexoManager();
		}
	}

	public static void saveDocument(UseCaseConf useCaseConf, int idBook, Integer fdrid, ElementoFichero elementoFichero,
			byte[] fileContent, String fileFormat, byte[] fileSignContent) throws SessionException, TecDocException {
		DocumentoElectronicoAnexoVO documentoElectronico = new DocumentoElectronicoAnexoVO();
		AxSf axsfNew = new AxSf();
		axsfNew.addAttributeName(Constants.FLD1_FIELD);

		CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
				useCaseConf.getSessionID());

		// Recuperamos los datos de conexion de la cache.
		AuthenticationUser user = (AuthenticationUser) cacheBag
				.get(HibernateKeys.HIBERNATE_Iuseruserhdr);

		UsuarioVO usuario = new UsuarioVO();
		usuario.setLoginName(useCaseConf.getUserName());
		usuario.setPassword(useCaseConf.getPassword());
		ConfiguracionUsuarioVO configuracionUsuario = new ConfiguracionUsuarioVO();
		configuracionUsuario.setIdEntidad(Constants.SIGEM_ENTIDAD_MSSSI);
		configuracionUsuario.setLocale(new Locale(Constants.SIGEM_LOCALE));
		configuracionUsuario.setSessionID(useCaseConf.getSessionID());
		usuario.setId(String.valueOf(user.getId()));
		usuario.setConfiguracionUsuario(configuracionUsuario);

		IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO = new IdentificadorDocumentoElectronicoAnexoVO();
		identificadorDocumentoElectronicoAnexoVO.setIdLibro((long) idBook);
		identificadorDocumentoElectronicoAnexoVO.setIdRegistro((long) fdrid);

		String fileCompleteName = elementoFichero.getNombre();
		
	    String codeName=(fileCompleteName);
	    codeName=StringUtils.abbreviate(codeName, 21);
		
		
		String fileExtension = FilenameUtils.getExtension(fileCompleteName);

		DocumentoElectronicoAnexoContenidoVO documentoElectronicoAnexoContenidoVO = new DocumentoElectronicoAnexoContenidoVO();
		documentoElectronicoAnexoContenidoVO.setContent(fileContent);

		
		TipoDocumentoAnexoEnumVO tipoDocumentoAnexo = TipoDocumentoAnexoEnumMSSSIVO.getValue(elementoFichero.getTipoDocumento());

		TipoValidezDocumentoAnexoEnumVO tipoValidez = TipoValidezDocumentoAnexoEnumMSSSIVO.getValue(elementoFichero.getValidezDocumento());

		String comentario = elementoFichero.getComentario();
		String mimeType = fileFormat;

		DocumentoElectronicoAnexoDatosFirmaVO datosFirma = new DocumentoElectronicoAnexoDatosFirmaVO();
		 String encodeBase64Hash = Base64.encodeBase64String(elementoFichero.getHashValue().getBytes());
		 datosFirma.setHash(encodeBase64Hash);
		 
		 
		List<DocumentoElectronicoAnexoVO> firmas = new ArrayList<DocumentoElectronicoAnexoVO>();

		if(fileSignContent != null){
			 DocumentoElectronicoAnexoVO documentoElectronicoFirma = new DocumentoElectronicoAnexoVO();
			
				IdentificadorDocumentoElectronicoAnexoVO idFirma = new IdentificadorDocumentoElectronicoAnexoVO();
				idFirma.setIdLibro((long) idBook);
				idFirma.setIdRegistro((long) fdrid);
			 				
				String fileCompleteNameFirma = Constants.FILE_NAME_FIRMA;
				
			    String codeNameFirma=(fileCompleteNameFirma);
			    codeNameFirma=StringUtils.abbreviate(codeNameFirma, 21);
			    
			    String fileExtensionFirma = FilenameUtils.getExtension(fileCompleteNameFirma);
			    
				DocumentoElectronicoAnexoContenidoVO documentoElectronicoAnexoContenidoVOFirma = new DocumentoElectronicoAnexoContenidoVO();
				documentoElectronicoAnexoContenidoVOFirma.setContent(fileSignContent);			    
				
				TipoDocumentoAnexoEnumVO tipoDocumentoAnexoFirma = TipoDocumentoAnexoEnumVO.FICHERO_TECNICO;

				TipoValidezDocumentoAnexoEnumVO tipoValidezFirma = TipoValidezDocumentoAnexoEnumVO.ORIGINAL;


				DocumentoElectronicoAnexoDatosFirmaVO datosFirmaFirma = new DocumentoElectronicoAnexoDatosFirmaVO();
				byte[] generateHash = null;
				try{
					generateHash = Hasher.generateHash(fileSignContent, HashType.SHA1);
				} catch (NoSuchAlgorithmException e) {		
				}
				String encodeBase64HashFirma = Base64.encodeBase64String(generateHash);
				datosFirmaFirma.setHash(encodeBase64HashFirma);
				
				
				documentoElectronicoFirma.setId(idFirma);
				documentoElectronicoFirma.setName(fileCompleteNameFirma);
				documentoElectronicoFirma.setCodeName(codeNameFirma);
				documentoElectronicoFirma.setExtension(fileExtensionFirma);
				documentoElectronicoFirma.setContenido(documentoElectronicoAnexoContenidoVOFirma);			 
				documentoElectronicoFirma.setTipoDocumentoAnexo(tipoDocumentoAnexoFirma);
				documentoElectronicoFirma.setTipoValidez(tipoValidezFirma);
				documentoElectronicoFirma.setDatosFirma(datosFirmaFirma);
			 
				// añadir firma
				firmas.add(documentoElectronicoFirma);
			 
			 
		}
		
		
		documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);
		documentoElectronico.setName(fileCompleteName);//obtener a partir del nombre
		documentoElectronico.setCodeName(codeName);//valor estatico
		documentoElectronico.setExtension(fileExtension);//obtener a partir del nombre
		documentoElectronico.setMimeType(mimeType);//obtener a partir de la peticion
		documentoElectronico.setComentario(comentario);//requerido
		documentoElectronico.setContenido(documentoElectronicoAnexoContenidoVO);//obtener a partir de peticion
		documentoElectronico.setTipoDocumentoAnexo(tipoDocumentoAnexo);//valor estatico
		documentoElectronico.setTipoValidez(tipoValidez);//requerido. explicar en la guia
		documentoElectronico.setDatosFirma(datosFirma);//para mas adelante
		documentoElectronico.setFirmas(firmas);//para mas adelante

		ConfiguracionCreateDocumentoElectronicoAnexoVO cfg = new ConfiguracionCreateDocumentoElectronicoAnexoVO();

		// seteamos el nombre de la carpeta/clasificador de sicres sobre el que
		// se guardaran
		String clasificador = null;
		cfg.setClasificador(clasificador);

		documentoElectronico = documentoElectronicoAnexoManager.create(documentoElectronico, cfg,
				usuario);

	}
       

	
}