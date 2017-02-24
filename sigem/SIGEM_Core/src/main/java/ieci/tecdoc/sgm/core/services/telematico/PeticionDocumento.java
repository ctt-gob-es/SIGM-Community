package ieci.tecdoc.sgm.core.services.telematico;

/**
 * Bean con la información de un documento adjunto a la solicitud de registro.
 *
 * @author IECISA
 *
 */
public class PeticionDocumento
{
   private String code;
   private String location;
   private String extension;
   private String fileName;
   private String idioma;
   private byte[] fileContent;
   private String fileContentBase64;
   private String sizeFile;
   private String signature;
   private String hash;

   public PeticionDocumento()
   {
      code = null;
      location = null;
      extension = null;
      fileName = null;
      idioma = null;
      fileContent = null;
      fileContentBase64 = null;
      sizeFile = null;
      signature = null;
      hash = null;
   }

   /**
    * Establece el código del documento.
    * @param code Código del documento.
    */
   public void setCode(String code)
   {
      this.code = code;
   }

   /**
    * Establece la ruta a la ubicación en el sistema de archivos del documento
    * antes de ser dado de alta en Invesdoc.
    * @param location Ruta en el sistema de archivos del servidor.
    */
   public void setLocation(String location)
   {
        this.location = location;
   }

   /**
    * Establece la extensión del documento.
    * @param extension Extensión del documento.
    */
   public void setExtension(String extension)
   {
      this.extension = extension;
   }

   /**
    * Establece el nombre del fichero local del documento.
    * @param fileName Nombre en local del documento.
    */
   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   /**
    * Devuelve el código del documento.
    * @return String Código del documento.
    */
   public String getCode()
   {
      return code;
   }

   /**
    * Devuelve la ruta temporal del documento en el sistema de archivos antes de
    * ser dado de alta en Invesdoc.
    * @return String Ruta temporal del documento.
    */
   public String getLocation()
   {
      return location;
   }

   /**
    * Devuelve la extensión del documento.
    * @return String Extensión del documento.
    */
   public String getExtension()
   {
      return extension;
   }

   /**
    * Devuelve el nombre del fichero local del documento.
    * @return String Nombre del documento en local.
    */
   public String getFileName() {
       return fileName;
   }

   /**
    * Devuelve el idioma del documento.
    * @return String Idioma del documento.
    */
   public String getIdioma() {
	   return idioma;
   }

   /**
    * Establedce el idioma del documento
    * @param idioma Idioma del documento
    */
   public void setIdioma(String idioma) {
	   this.idioma = idioma;
   }
   /**
    * Devuelve el contenido del documento.
    * @return byte[] contenido del documento.
    */
	public byte[] getFileContent() {
		return fileContent;
	}
	/**
	    * Establedce el contenido del documento
	    * @param fileContent contenido del documento
	    */
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	
	/**
    * Devuelve el tamano del documento.
    * @return String sizeFile del documento.
    */
   public String getSizeFile() {
	   return sizeFile;
   }

   /**
    * Establece el tamano del documento
    * @param sizeFile tamano del documento
    */
   public void setSizeFile(String sizeFile) {
	   this.sizeFile = sizeFile;
   }
   
	/**
    * Devuelve la firma del documento
    * @return String firma del documento.
    */
	public String getSignature() {
		return signature;
	}

	/**
    * Establece la firma del documento
    * @param signature documento
    */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	/**
    * Devuelve el hash del documento
    * @return String hash del documento.
    */
	public String getHash() {
		return hash;
	}

	/**
    * Establece la hash del documento
    * @param hash documento
    */
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	/**
    * Devuelve el contenido del documento en base64
    * @return String contenido del documento.
    */
	public String getFileContentBase64() {
		return fileContentBase64;
	}

	/**
    * Establece el contenido del documento en base64
    * @param fileContentBase64 documento
    */
	public void setFileContentBase64(String fileContentBase64) {
		this.fileContentBase64 = fileContentBase64;
	}
	
	
}