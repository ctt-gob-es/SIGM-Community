/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.dir3.api.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import es.msssi.dir3.api.vo.ContactsOFIVO;
import es.msssi.dir3.api.vo.ContactsUNOVO;
import es.msssi.dir3.api.vo.ContactsUOVO;
import es.msssi.dir3.api.vo.HistoriesVO;
import es.msssi.dir3.api.vo.OfficesVO;
import es.msssi.dir3.api.vo.RelationshipsOFIUOSIRVO;
import es.msssi.dir3.api.vo.RelationshipsOFIUOVO;
import es.msssi.dir3.api.vo.ServicesVO;
import es.msssi.dir3.api.vo.UnitsVO;
import es.msssi.dir3.core.errors.DIR3ErrorCode;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.errors.ErrorConstants;

/**
 * Implementa métodos que convierten xml provenientes del dco en beans.
 * 
 * @author cmorenog
 * 
 */
public class XmlDcoToObject {
	private static final Logger logger = Logger.getLogger(XmlDcoToObject.class);
	private static XmlDcoToObject instance = new XmlDcoToObject();
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	public Marshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	/**
	 * Constructor protegido para evitar creación de instancias desde otras
	 * clases.
	 */
	protected XmlDcoToObject() {
	}

	/**
	 * Obtiene la instancía única de la clase.
	 * 
	 * @return La instancia única de la clase.
	 */
	public static XmlDcoToObject getInstance() {
		return instance;
	}

	/**
	 * Devuelve un objeto "Oficinas" a partir de un Xml de Oficinas de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de Oficinas de DCO.
	 * @return Un objeto "Oficinas" a partir de un Xml de Oficinas de DCO.
	 * @throws DIR3Exception .
	 */
	public OfficesVO getOfficesFromXmlFile(String xmlFilePath) throws DIR3Exception {
		FileInputStream is = null;
		OfficesVO officesVO = null;
		
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTOOFFICE_ERROR, fileNotFoundException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOOFFICE_ERROR, fileNotFoundException);
				}

				try {
					officesVO = (OfficesVO) getUnmarshaller().unmarshal( new StreamSource(is));
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTOOFFICE_ERROR, xmlMappingException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOOFFICE_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTOOFFICE_ERROR, iOException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOOFFICE_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error(ErrorConstants.CONVERT_XMLTOOFFICE_ERROR, iOException);
					}
				}
			}
		}
		return officesVO;
	}

	/**
	 * Devuelve un objeto "contactosOFI" a partir de un Xml de los contactos de
	 * los Organismos de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de contactos de Organismos de DCO.
	 * @return Un objeto "ContactsUO" a partir de un Xml de contactos de
	 *         Organismos de DCO.
	 * @throws DIR3Exception .
	 */
	public ContactsOFIVO getContactsOFIFromXmlFile(String xmlFilePath) throws DIR3Exception {
		FileInputStream is = null;
		ContactsOFIVO ContactsOFIVO = null;
		
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, fileNotFoundException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, fileNotFoundException);
				}

				try {
					ContactsOFIVO = (ContactsOFIVO) getUnmarshaller() .unmarshal(new StreamSource(is));
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, xmlMappingException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, iOException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error( ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, iOException);
					}
				}
			}
		}
		return ContactsOFIVO;
	}

	/**
	 * Devuelve un objeto "ServicesVO" a partir de un Xml de las oficinas de
	 * DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de servicios de oficinas de DCO.
	 * @return Un objeto "ServicesVO" a partir de un Xml de servicios de
	 *         oficinas de DCO.
	 * @throws DIR3Exception .
	 */
	public ServicesVO getServicesFromXmlFile(String xmlFilePath) throws DIR3Exception {
		FileInputStream is = null;
		ServicesVO servicesVO = null;
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTOSERVICES_ERROR, fileNotFoundException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOSERVICES_ERROR, fileNotFoundException);
				}

				try {
					servicesVO = (ServicesVO) getUnmarshaller().unmarshal( new StreamSource(is));
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTOSERVICES_ERROR, xmlMappingException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOSERVICES_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTOSERVICES_ERROR, iOException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOSERVICES_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error(ErrorConstants.CONVERT_XMLTOSERVICES_ERROR,iOException);
					}
				}
			}
		}
		return servicesVO;
	}

	/**
	 * Devuelve un objeto "contactosUO" a partir de un Xml de los contactos de
	 * los Organismos de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de contactos de Organismos de DCO.
	 * @return Un objeto "ContactsUO" a partir de un Xml de contactos de
	 *         Organismos de DCO.
	 * @throws DIR3Exception .
	 */
	public ContactsUOVO getContactsUOFromXmlFile(String xmlFilePath)throws DIR3Exception {
		FileInputStream is = null;
		ContactsUOVO ContactsUOVO = null;
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR,fileNotFoundException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR,fileNotFoundException);
				}

				try {
					ContactsUOVO = (ContactsUOVO) getUnmarshaller().unmarshal(new StreamSource(is));
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, xmlMappingException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR,iOException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error( ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, iOException);
					}
				}
			}
		}
		return ContactsUOVO;
	}

	/**
	 * Devuelve un objeto "Organismos" a partir de un Xml de Organismos de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de Organismos de DCO.
	 * @return Un objeto "Organismos" a partir de un Xml de Organismos de DCO.
	 * @throws DIR3Exception .
	 */
	public UnitsVO getUnitsFromXmlFile(String xmlFilePath) throws DIR3Exception {
		FileInputStream is = null;
		UnitsVO unitsVO = null;
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTOUNITS_ERROR, fileNotFoundException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOUNITS_ERROR, fileNotFoundException);
				}

				try {
					unitsVO = (UnitsVO) getUnmarshaller().unmarshal( new StreamSource(is));
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTOUNITS_ERROR, xmlMappingException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOUNITS_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTOUNITS_ERROR, iOException);
					new DIR3Exception(DIR3ErrorCode.CONVERT_XMLTOUNITS_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error(ErrorConstants.CONVERT_XMLTOUNITS_ERROR, iOException);
					}
				}
			}
		}
		return unitsVO;
	}

	/**
	 * Devuelve un objeto "HistoriesVO" a partir de un Xml de los históricos de
	 * los Organismos de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de histórico de Organismos y oficinas de
	 *            DCO.
	 * @return Un objeto "HistoriesVO" a partir de un Xml de históricos de
	 *         Organismos de DCO.
	 */
	public HistoriesVO getHistoriesFromXmlFile(String xmlFilePath) {
		FileInputStream is = null;
		HistoriesVO historiesVO = null;

		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTOHISTORIC_ERROR, fileNotFoundException);
				}

				try {
					historiesVO = (HistoriesVO) getUnmarshaller().unmarshal( new StreamSource(is));
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTOHISTORIC_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTOHISTORIC_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error( ErrorConstants.CONVERT_XMLTOHISTORIC_ERROR, iOException);
					}
				}
			}
		}
		return historiesVO;
	}

	/**
	 * Devuelve un objeto "RelationshipsOFIUOVO" a partir de un Xml de las
	 * relaciones físicas las oficinas de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de las relaciones físicas de oficinas de
	 *            DCO.
	 * @return Un objeto "RelationshipsOFIUOVO" a partir de un Xml de las
	 *         relaciones físicas de oficinas de DCO.
	 * @throws DIR3Exception .
	 */
	public RelationshipsOFIUOVO getRelationsFromXmlFile(String xmlFilePath) throws DIR3Exception {
		FileInputStream is = null;
		RelationshipsOFIUOVO relationshipsOFIUOVO = null;
		
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTORELATIONS_ERROR,fileNotFoundException);
					new DIR3Exception(
							DIR3ErrorCode.CONVERT_XMLTORELATIONS_ERROR,fileNotFoundException);
				}
				try {
					relationshipsOFIUOVO = (RelationshipsOFIUOVO) getUnmarshaller().unmarshal(new StreamSource(is));
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTORELATIONS_ERROR, xmlMappingException);
					new DIR3Exception(
							DIR3ErrorCode.CONVERT_XMLTORELATIONS_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTORELATIONS_ERROR, iOException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTORELATIONS_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error( ErrorConstants.CONVERT_XMLTORELATIONS_ERROR, iOException);
					}
				}
			}
		}
		return relationshipsOFIUOVO;
	}

	/**
	 * Devuelve un objeto "RelationshipsOFIUOVO" a partir de un Xml de las
	 * relaciones SIR las oficinas de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de las relaciones SIR de oficinas de DCO.
	 * @return Un objeto "RelationshipsOFIUOVO" a partir de un Xml de las
	 *         relaciones SIR de oficinas de DCO.
	 * @throws DIR3Exception .
	 */
	public RelationshipsOFIUOVO getRelationsSIRFromXmlFile(String xmlFilePath) throws DIR3Exception {
		FileInputStream is = null;
		RelationshipsOFIUOVO relationshipsOFIUOVO = null;
		RelationshipsOFIUOSIRVO relationshipsOFIUOSIRVO = null;
		
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTORELATIONS_ERROR, fileNotFoundException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTORELATIONS_ERROR, fileNotFoundException);
				}
				try {
					relationshipsOFIUOSIRVO = (RelationshipsOFIUOSIRVO) getUnmarshaller().unmarshal(new StreamSource(is));
					if (relationshipsOFIUOSIRVO != null && relationshipsOFIUOSIRVO.getRelationships() != null) {
						relationshipsOFIUOVO = new RelationshipsOFIUOVO();	
						relationshipsOFIUOVO.setRelationships(relationshipsOFIUOSIRVO.getRelationships());
					}
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTORELATIONS_ERROR, xmlMappingException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTORELATIONS_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTORELATIONS_ERROR, iOException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTORELATIONS_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error( ErrorConstants.CONVERT_XMLTORELATIONS_ERROR, iOException);
					}
				}
			}
		}
		return relationshipsOFIUOVO;
	}

	/**
	 * Devuelve un objeto "contactosUO" a partir de un Xml de los contactos de
	 * los Organismos de DCO.
	 * 
	 * @param xmlFilePath
	 *            Path al fichero XML de contactos de Organismos de DCO.
	 * @return Un objeto "ContactsUO" a partir de un Xml de contactos de
	 *         Organismos de DCO.
	 * @throws DIR3Exception .
	 */
	public ContactsUOVO getContactsUNOFromXmlFile(String xmlFilePath) throws DIR3Exception {
		FileInputStream is = null;
		ContactsUOVO contactsUOVO = null;
		ContactsUNOVO contactsUNOVO = null;
		
		if (null != xmlFilePath) {
			try {
				try {
					is = new FileInputStream(xmlFilePath);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, fileNotFoundException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, fileNotFoundException);
				}
				try {
					contactsUNOVO = (ContactsUNOVO) getUnmarshaller().unmarshal(new StreamSource(is));
					if (contactsUNOVO != null && contactsUNOVO.getContacts() != null) {
						contactsUOVO = new ContactsUOVO();
						contactsUOVO.setContacts(contactsUNOVO.getContacts());
					}
				} catch (XmlMappingException xmlMappingException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, xmlMappingException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, xmlMappingException);
				} catch (IOException iOException) {
					logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, iOException);
					new DIR3Exception( DIR3ErrorCode.CONVERT_XMLTOCONTACTS_ERROR, iOException);
				}
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException iOException) {
						logger.error(ErrorConstants.CONVERT_XMLTOCONTACTS_ERROR, iOException);
					}
				}
			}
		}
		return contactsUOVO;
	}

}
