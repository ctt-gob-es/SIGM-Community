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

package es.seap.minhap.portafirmas.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersCommentDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.ListComparador;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Comment;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;

@Controller
@RequestMapping("inbox/comment")
public class CommentController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RequestBO requestBO;


	@Resource(name = "messageProperties")
	private Properties messages;
	
	@Autowired
	private NoticeBO noticeBO;
	
	@Autowired
	private ApplicationVO applicationVO;
	

	@RequestMapping(method = RequestMethod.GET)
	public String loadComments(Model model, 
			@RequestParam(value = "currentRequest") final String currentRequest) {
		process(model, new UsersParameters(), currentRequest);
		return "comments";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String loadComments(@ModelAttribute("jobsParameters") UsersParameters jobsParameters, Model model, @RequestParam(value = "currentRequest") final String currentRequest) {
		process(model, jobsParameters, currentRequest);
		return "comments";
	}

	private void process(Model model, UsersParameters jobsParameters, String currentRequest) {

		try {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
					.getContext().getAuthentication();

			PfUsersDTO user = authorization.getUserDTO();
			PfGroupsDTO group = authorization.getGroup();
			
			// Si el remitente de la petición es un grupo y el usuario actual pertenece a ese grupo 
			String currentGroup = "null";
			if (group != null) {
				currentGroup = group.getPrimaryKeyString();
			}
			
			// Carga de los datos de la request
			PfRequestsDTO request = requestBO.queryRequestHash(currentRequest);
			
			List<User> users = cargarRemitentes(request);
			
			PfUsersDTO currentUser = obtenerUsuario(user, users);
			
			// Obtiene los comentarios de la petición visibles para el usuario autenticado
			List<AbstractBaseDTO> commentList = requestBO.queryCommentByRequest(request, user);
			List<Comment> comments = new ArrayList<Comment>(); 
			Comment comment = null;
			if (commentList != null) {
				ListComparador myComparator = new ListComparador("orderAscCommentByDate", 1);
				Collections.sort(commentList, myComparator);

				PfCommentsDTO pfCommentsDTO = null;
				for (AbstractBaseDTO obj : commentList) {
					pfCommentsDTO = (PfCommentsDTO) obj;
					comment = new Comment();
					comment.setPrimaryKey(pfCommentsDTO.getPrimaryKeyString());
					comment.setUserId(pfCommentsDTO.getPfUser().getCidentifier());
					comment.setUserName(pfCommentsDTO.getPfUser().getFullName());
					comment.setTcomment(pfCommentsDTO.getTcomment());
					comment.setFcreated(pfCommentsDTO.getFcreated());
					
					comments.add(comment);
				}
			}
			
			model.addAttribute("commentsList", comments);		
			model.addAttribute("currentUser", currentUser.getCidentifier());
			model.addAttribute("currentGroup", currentGroup);
			model.addAttribute("users", users);
			model.addAttribute("comment", new Comment());

		} catch (Throwable t) {
			log.error("Error al cargar comentarios: ", t);
		}
	}
	
	
	private List<User> cargarRemitentes(PfRequestsDTO request) {
		List<User> users = new ArrayList<User>();
		List<String> remitterList = new ArrayList<String>();
		// carga los remitentes de la petición
		User userWeb = null;
		if (request.getPfUsersRemitters() != null
				&& !request.getPfUsersRemitters().isEmpty()) {
			for (PfUsersRemitterDTO userRemitterDTO : request.getPfUsersRemitters()) {
				remitterList.add(userRemitterDTO.getPfUser().getCidentifier());
				userWeb = new User();
				userWeb.setNif(userRemitterDTO.getPfUser().getCidentifier());
				if (userRemitterDTO.getPfGroup() != null) {
					userWeb.setName(messages.getProperty("sender"));
					userWeb.setGroup(userRemitterDTO.getPfGroup().getPrimaryKeyString());
				} else {
					userWeb.setName(userRemitterDTO.getPfUser().getFullName() + " (" + messages.getProperty("sender") + ")");
					userWeb.setGroup("");
				}
						
				users.add(userWeb);
			}
		}

		// carga los firmantes de la petición
		for (PfSignLinesDTO signLineDTO : request.getPfSignsLinesList()) {
			for (PfSignersDTO signerDTO : signLineDTO.getPfSigners()) {
				if (!remitterList.contains(signerDTO.getPfUser().getCidentifier())) {
					userWeb = new User();
					userWeb.setNif(signerDTO.getPfUser().getCidentifier());
					userWeb.setName(signerDTO.getPfUser().getFullName() + " (" + messages.getProperty("signer") + ")");
					users.add(userWeb);
					
				}
				
			}
		}	
		return users;
	}
	
	private PfUsersDTO obtenerUsuario(PfUsersDTO user, List<User> users) {
		PfUsersDTO userReturn = user;
		PfUsersJobDTO pfUsersJobDTO = null;	
		Iterator<PfUsersJobDTO> itPfUserJobs = user.getPfUsersJobs().iterator();
		boolean encontrado = false;
		while (itPfUserJobs.hasNext() && !encontrado) {
			PfUsersJobDTO usersJobDTO = itPfUserJobs.next();
			if (Util.getInstance().isValidJob(usersJobDTO)) {
				pfUsersJobDTO = usersJobDTO;
			}
		}
		
		boolean usuarioEncontrado = false;
		boolean cargoEncontrado = false;
		boolean grupoEncontrado = false;
		if (pfUsersJobDTO !=null) {
			Iterator<User> itUsers = users.iterator();
			while (itUsers.hasNext()) {
				User usr = itUsers.next();
				String nif = usr.getNif();
				//messages.getProperty("sender")
				if (pfUsersJobDTO.getPfUserJob().getCidentifier().equals(nif)) {
					cargoEncontrado = true;
				} 
				if (user.getCidentifier().equals(nif)) {
					usuarioEncontrado= true;
				}
			
				
			}
			if (grupoEncontrado) {
			} else if (cargoEncontrado && !usuarioEncontrado) {
				userReturn = pfUsersJobDTO.getPfUserJob();
			}
	   }
		
		return userReturn;
		
	}
	
	@RequestMapping(value = "/loadComment", method = RequestMethod.GET)
	public @ResponseBody Comment load(@RequestParam(value = "currentRequest") final String currentRequest
			, @RequestParam(value = "primaryKey") final String primaryKey) {
		Comment comment = new Comment();
		try {
			// Carga de los datos de la request
			PfRequestsDTO request = requestBO.queryRequestHash(currentRequest);
			
			List<User> users = cargarRemitentes(request);
			
			if(!Util.esVacioONulo(primaryKey) && !primaryKey.equals("null")) {
				PfCommentsDTO pfCommentsDTO = requestBO.getCommentById(Long.parseLong(primaryKey));
				if (pfCommentsDTO != null) {
					comment.setPrimaryKey(pfCommentsDTO.getPrimaryKeyString());
					comment.setTcomment(pfCommentsDTO.getTcomment());
					
					if (pfCommentsDTO.getPfUsersComments() !=null) {
						Iterator<PfUsersCommentDTO> itComment = pfCommentsDTO.getPfUsersComments().iterator();
						while (itComment.hasNext()) {
							comment.getUsers().add(itComment.next().getPfUser().getCidentifier());
						}
					}
				}
				
			}
			
			comment.setSigners(users);

		} catch (Throwable t) {
			log.error("Error al cargar comentarios: ", t);
		}
		return comment;
	
	}
	
	
	/**
	 * Alta o modificación de una sede
	 * @param primaryKey
	 * @param serverCode
	 * @param serverDescription
	 * @param serverIsDefault
	 * @return
	 */
	@RequestMapping(value = "/saveComment", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> saveComment(
			@RequestParam(value = "currentRequest") final String currentRequest,
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "commentText") final String commentText,
			@RequestParam(value = "userComments") final String userComments) {
		ArrayList<String> errors = new ArrayList<String>();
		try {

			// Se recupera el usuario autenticado
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO pfUser = authorization.getUserDTO();
			
			//currentRequest
			PfRequestsDTO request = requestBO.queryRequestHash(currentRequest);
			
			// Se obtienen la información de la vista
			PfCommentsDTO comment = new PfCommentsDTO();
			if(!Util.esVacioONulo(primaryKey)) {
				comment = requestBO.getCommentById(Long.parseLong(primaryKey));
			}
			
			if (comment != null) {
				comment.setTcomment(commentText);
				
				checkComment(comment, errors);
				
				if(!errors.isEmpty()) {
					return errors;
				}
				List<String> users = new ArrayList<String>();
				List<User> userList = new ArrayList<User>();
				if(!Util.esVacioONulo(userComments)) {
					String[] userSplit = userComments.split(",");
					for (int i = 0; i < userSplit.length; i++) {
						if (!users.contains(userSplit[i])) {
							users.add(userSplit[i]);
						}
						
						User user = new User();
						user.setNif(userSplit[i]);
						userList.add(user);
					}
				}
				
				PfUsersDTO currentUser = obtenerUsuario(pfUser, userList);
				
				requestBO.insertRequestComment(request, comment, currentUser, users);
				
			}
			
			request.setPfComments(new HashSet<PfCommentsDTO>());
			request.getPfComments().add(comment);
			
			noticeBO.noticeNewComment(request, applicationVO.getEmail(), applicationVO.getSMS());
			
		} catch (Exception e) {
			log.error("Error al insertar el servidor: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Borrado de un servidor
	 * @param primaryKey
	 * @return
	 */
	
	
	@RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> deleteComment(
			@RequestParam(value = "currentRequest") final String currentRequest,
			@RequestParam(value = "primaryKey") final String primaryKey) {
		ArrayList<String> errors = new ArrayList<String>();
		try {

			// Se recupera el usuario autenticado
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO pfUser = authorization.getUserDTO();
			
			// Se obtiene el comentario a borrar
			PfRequestsDTO request = requestBO.queryRequestHash(currentRequest);
			
			PfCommentsDTO pfCommentsDTO = requestBO.getCommentById(Long.parseLong(primaryKey));
			
			if (pfCommentsDTO != null) {
				requestBO.removeRequestComment(pfCommentsDTO, request, pfUser);
			}
			
			
			
		} catch (Exception e) {
			log.error("Error al borrar el servidor: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	
	/**
	 * Chequea si un comentario es correcto, para que un comentario sea correcto deber&aacute; tener menos de
	 * mil caracteres y no debe estar vac&iacute;o, si el comentario no es correcto crea un mensaje de error
	 * para el usuario
	 */
	private void checkComment(PfCommentsDTO comment, ArrayList<String> errors) {
		//si el comentario tiene mas de mil carateres
		//se crea un mensaje de error
		if (comment.getTcomment().length() > 1000) {
			errors.add(messages.getProperty("errorTcommentLength"));
		//Si el mensaje est&aacute; vac&iacute;o
		//se crea un mensaje de error
		} else if (comment.getTcomment().equals("")) {
			errors.add(messages.getProperty("errorTcommentNull"));
			
		} 
		
	}
	
	/**
	 * Método que inserta usuarios del comentario
	 * @param primaryKey 
	 * @param userComment
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/deleteUsersComment")
	public void deleteUsersComment( @RequestParam(value = "primaryKey") final String primaryKey,
			 					 @RequestParam(value = "userComment") final String userComment,
								 final HttpServletResponse response) throws IOException {

		log.debug("deleteUsersComment");

		if(!Util.esVacioONulo(primaryKey)) {
			PfUsersCommentDTO pfUsersCommentDTO = requestBO.queryCommentByIdComment(Long.valueOf(primaryKey), userComment);
			if (pfUsersCommentDTO != null) {
				requestBO.removeUserComment(pfUsersCommentDTO);
			}
			
		}

		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");

	}
	

	/**
	 * Método que valida el certificado seleccionado por el usuario para firmar
	 * @param primaryKey 
	 * @param userComment
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/insertUsersComment")
	public void insertUsersComment( @RequestParam(value = "primaryKey") final String primaryKey,
								 @RequestParam(value = "userComment") final String userComment,
								 final HttpServletResponse response) throws IOException {

		String usersComment = "";
		log.debug("insertUsersComment");

		// Se recupera el usuario autenticado
		UserAuthentication authorization = 
				(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		String currectUser = authorization.getUserDTO().getPrimaryKeyString();
		
		PfCommentsDTO comment = new PfCommentsDTO();
		if(!Util.esVacioONulo(primaryKey)) {
			comment = requestBO.getCommentById(Long.parseLong(primaryKey));
		}
		
		PfUsersDTO pfUser = null;
		if(!Util.esVacioONulo(userComment)) {
			pfUser = requestBO.getUserOrJobByDNI(userComment);	
		}
		
		
		if (pfUser != null && comment != null) {
			PfUsersCommentDTO pfUsersCommentDTO = new PfUsersCommentDTO();
			pfUsersCommentDTO.setPfComment(comment);
			pfUsersCommentDTO.setPfUser(pfUser);
			pfUsersCommentDTO.setCcreated(currectUser);
			requestBO.insertUserComment(pfUsersCommentDTO);
		}
		

		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\", \"usersComment\": \"" + usersComment + "\"}");

	}
	

}