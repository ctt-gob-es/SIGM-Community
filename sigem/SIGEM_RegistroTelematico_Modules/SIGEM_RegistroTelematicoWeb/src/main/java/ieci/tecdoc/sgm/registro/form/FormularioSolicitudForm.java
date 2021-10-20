package ieci.tecdoc.sgm.registro.form;

import java.io.ByteArrayInputStream;







import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts.action.ActionForm;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

public class FormularioSolicitudForm extends ActionForm{
	private String documentoIdentidad;
	private String nombreSolicitante;
	private String emailSolicitante;
	private String organoDestinatario;
	private String datosEspecificos;
	
	public FormularioSolicitudForm(){
		this.documentoIdentidad = "";
		this.nombreSolicitante = "";
		this.emailSolicitante = "";
		this.organoDestinatario = "";
		this.datosEspecificos = "";
	}

	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	public void setDocumentoIdentidad(String documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}

	public String getEmailSolicitante() {
		return emailSolicitante;
	}

	public void setEmailSolicitante(String emailSolicitante) {
		this.emailSolicitante = emailSolicitante;
	}

	public String getNombreSolicitante() {
		return nombreSolicitante;
	}

	public void setNombreSolicitante(String nombreSolicitante) {
		this.nombreSolicitante = nombreSolicitante;
	}

	public String getOrganoDestinatario() {
		return organoDestinatario;
	}

	public void setOrganoDestinatario(String organoDestinatario) {
		this.organoDestinatario = organoDestinatario;
	}

	public String getDatosEspecificos() {
		return datosEspecificos;
	}

	public void setDatosEspecificos(String datosEspecificos) {
		this.datosEspecificos = datosEspecificos;
	}
	
	public void procesaDatosEspecificos(){
		
		DocumentBuilderFactory factory =
		DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String d_email_="";
		String emailSolicitante_="";
		String email_Solicitante_="";
		String nif_cif_="";
		String nif_="";
		String nif1_="";
		String nombre_="";
		String nombre1_="";
		String documentoIdentidad_="";
		String entidad_="";
		String nombreSolicitante_="";
		
		try {
			
			builder = factory.newDocumentBuilder();		
			StringBuilder xmlStringBuilder = new StringBuilder();
			String cabecera="<?xml version=";
			cabecera=cabecera.concat("\"");
			cabecera=cabecera.concat("1.0");
			cabecera=cabecera.concat("\"");
			cabecera=cabecera.concat("?>");
			xmlStringBuilder.append(cabecera);
			xmlStringBuilder.append("<formulario>");
			xmlStringBuilder.append(this.datosEspecificos);
			xmlStringBuilder.append("</formulario>");
			ByteArrayInputStream input = new ByteArrayInputStream( xmlStringBuilder.toString().getBytes("UTF-8"));
			Document doc = builder.parse(input);
			
			//Formulario del padron
			try{
				nif_=doc.getDocumentElement().getElementsByTagName("NIF").item(0).getTextContent();
			}catch(Exception e){				
			}
			
			//Formulario lic_obras
			try{
				nif1_=doc.getDocumentElement().getElementsByTagName("nif").item(0).getTextContent();
			}catch(Exception e){				
			}	
			
			//Formulario del BOP
			try{
				nif_cif_=doc.getDocumentElement().getElementsByTagName("NIF_CIF").item(0).getTextContent();
			}catch(Exception e){				
			}
			
			//Resto de formularios
			try{
				documentoIdentidad_=doc.getDocumentElement().getElementsByTagName("documentoIdentidad").item(0).getTextContent();
			}catch(Exception e){				
			}
			
			//Formulario del padron
			try{
				nombre_=doc.getDocumentElement().getElementsByTagName("Nombre").item(0).getTextContent();
			}catch(Exception e){				
			}
			
			//Formulario del BOP
			try{
				entidad_=doc.getDocumentElement().getElementsByTagName("Entidad").item(0).getTextContent();
			}catch(Exception e){				
			}				
			
			//Resto de formularios
			try{
				nombreSolicitante_=doc.getDocumentElement().getElementsByTagName("nombreSolicitante").item(0).getTextContent();
			}catch(Exception e){				
			}			
			
			//Formulario del padron
			try{				
				email_Solicitante_=doc.getDocumentElement().getElementsByTagName("Email_Solicitante").item(0).getTextContent();				
			}catch(Exception e){				
			}							
			
			//Formulario del BOP
			try{				
				emailSolicitante_=doc.getDocumentElement().getElementsByTagName("emailSolicitante").item(0).getTextContent();				
			}catch(Exception e){				
			}
			
			//Resto de formularios
			try{				
				d_email_=doc.getDocumentElement().getElementsByTagName("d_email").item(0).getTextContent();				
			}catch(Exception e){				
			}
			  
			//Captar el dni o nif
			if(!nif_cif_.equals(""))
				this.documentoIdentidad=nif_cif_;
			else if(!documentoIdentidad_.equals(""))
				this.documentoIdentidad=documentoIdentidad_;
			else if(!nif_.equals(""))
				this.documentoIdentidad=nif_;
			else if(!nif1_.equals(""))
				this.documentoIdentidad=nif1_;
			else
				this.documentoIdentidad="";
			
			//Captar el nombre o entidad
			if(!entidad_.equals(""))
				this.nombreSolicitante=entidad_;
			else if(!nombreSolicitante_.equals(""))
				this.nombreSolicitante=nombreSolicitante_;
			else if(!nombre_.equals(""))
				this.nombreSolicitante=nombre_;
			else
				this.nombreSolicitante="";
			
			//Captar el email
			if(!d_email_.equals(""))
				this.emailSolicitante=d_email_;
			else if(!emailSolicitante_.equals(""))
				this.emailSolicitante=emailSolicitante_;
			else if(!email_Solicitante_.equals(""))
				this.emailSolicitante=email_Solicitante_;
			else
				this.emailSolicitante="";			
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final static long serialVersionUID = 0;
}
