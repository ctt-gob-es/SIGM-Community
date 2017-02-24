/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package ieci.tecdoc.sgm.core.services.telematico;

import java.util.ArrayList;

/**
 * Contenedor de documentos adjuntos a la solicitud de registro.
 * 
 * @author IECISA
 *
 */
public class Solicitantes
{
   private ArrayList solicitantes;

   public Solicitantes()
   {
	   solicitantes = new ArrayList();
   }
   
   /**
    * Devuelve el número de documentos contenidos en la colección.
    * @return int Número de documentos de la colección.
    */   
   public int count()
   {
      return solicitantes.size();
   }
   
   /**
    * Devuelve el documento de la posición indicada.
    * @param index Posición del documento dentro de la colección.
    * @return RequestDocument Documento solicitado.
    */   
   public Solicitante get(int index)
   {
      return (Solicitante)solicitantes.get(index);
   }
   
   /**
    * Añade un nuevo documento adjunto a la solicitud de registro a la colección.
    * @param document Nuevo documento a añadir a la colección.
    */   
   public void add(Solicitante solicitante) 
   {
	   solicitantes.add(solicitante);
   }
    
}