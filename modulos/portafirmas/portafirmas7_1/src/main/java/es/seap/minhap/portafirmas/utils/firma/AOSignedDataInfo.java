/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.utils.firma;

/**
 * Clase copiada del proyecto eeutil
 * @author rus
 *
 */
public class AOSignedDataInfo{
	private String mimetype;

	/** Descripcion del tipo de dato. */
	//private String description;

	/** Extensi&oacute;n por defecto para el fichero. */
	//private String extension;

	/** Datos extra&iacute;dos. */
	private byte[] data;

	/**
	 * Crea un objeto de datos firmado.
	 * @param data Datos que se han extra&iacute;do de una firma.
	 */
	/*public AOSignedDataInfo(byte[] data) {
    this.data = data;

    MagicMatch match = null;
    try {
        match = Magic.getMagicMatch(data, true);
    } catch (MagicMatchNotFoundException e) {
        Logger.getLogger("es.gob.afirma").warning("Tipo de fichero no reconocido: "+e);
    } catch(Exception e) {
        Logger.getLogger("es.gob.afirma").warning("Ocurrio un error al analizar el fichero: "+e);
    }

    // Si se conoce el tipo de fichero, tomamos sus datos
    if(match != null) {
        this.mimetype = match.getMimeType();

        // Asignaremos descripciones concretas para determinados MimeTypes
        // Si es un texto plano, lo indicamos
        if(mimetype.equals("text/plain")) {
        	this.description = "Texto plano";
        	this.extension = match.getExtension();
        }
        // Si se identifica como Zip comprobamos si en realidad es un documento Ofimatico ODF u OOXML.
        else if(mimetype.equals("application/zip")) {
        	String tempMimeType = OfficeXMLAnalizer.getMimeType(data);
        	if(mimetype == null || mimetype.equals("application/zip")) {
        		this.description = match.getDescription();
        		this.extension = match.getExtension();
        	} else {
        		String ext = DataAnalizer.getExtension(tempMimeType);
        		this.description = "Documento " + ext.toUpperCase();
        		this.extension = ext;
        	}
        }
        // Si es cualquier otra cosa, usamos la descripcion asignada
        else {
        	this.description = mimetype.equals("text/plain") ? "Texto plano" : match.getDescription();

        }
    } else {
        this.mimetype = null;
        this.description = null;
    }
}*/

	/**
	 * Recupera los datos analizados.
	 * @return Datos analizados.
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * Recupera la descripcion del tipo de dato. Si no se conoce, devuelve "Desconocido".
	 * @return Descripci&oacute;n del tipo de dato.
	 */
//	public String getDataDescription() {
//		return this.description;    
//	}

	/**
	 * Recupera la extensi&oacute;n asignada por defecto a este tipo de dato. Si no se conoce
	 * esta extensi&oacute;n, se devolver&aacute; <code>null</code>.  
	 * @return Extension por defecto del tipo de dato.
	 */
//	public String getDataExtension() {
//		return this.extension;
//	}

	/**
	 * Recupera el identificador (MimeType) del tipo de dato.
	 * @return Tipo de dato.
	 */
	public String getDataMimeType() {
		return this.mimetype;    
	}

	public void setDataMimeType (String mimetype) {
		this.mimetype = mimetype;
	}

	public void setData (byte[] data) {
		this.data = data;
	}
}
