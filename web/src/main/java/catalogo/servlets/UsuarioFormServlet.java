package catalogo.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import catalogo.dal.DAOException;
import catalogo.dal.UsuarioDAO;
import catalogo.tipos.Usuario;

@WebServlet("/admin/usuarioform")
public class UsuarioFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(UsuarioFormServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();
		HttpSession session = request.getSession();
		session.removeAttribute("errorLogin");
		session.removeAttribute("errorSignup");
		session.removeAttribute("errorProducto");
		session.removeAttribute("errorUsuario");
		
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");

		String op = request.getParameter("opform");

		Usuario usuario;

		int id;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (Exception e) {
			id = 0;
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String nombre_completo = request.getParameter("nombre_completo");
		int id_roles;
		try {
			id_roles = Integer.parseInt(request.getParameter("id_roles"));
		} catch (Exception e) {
			id_roles = 2;
		}

		RequestDispatcher rutaListado = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_FORMULARIO);

		if (op == null) {
			usuario = new Usuario(id_roles, nombre_completo, password, username);
			session.removeAttribute("errorUsuario");
			rutaListado.forward(request, response);
		} else {

			switch (op) {

			case "alta":

				usuario = new Usuario(id_roles, nombre_completo, password, username);
				if (password != null && password != "" && password.equals(password2)) {
					usuarios.abrir();
					try {
						usuarios.insert(usuario);
						session.removeAttribute("errorUsuario");
						log.info("Usuario " + usuario.getUsername() + " dado de alta");
					} catch (DAOException e) {
						// Si falla el insert se coge la excepción que lanza y se le reenvía al formulario con el objeto
						// usuario que traía metido en la request
						session.setAttribute("errorUsuario", "Error al dar de alta al usuario. Inténtelo de nuevo");
						request.setAttribute("usuario", usuario);
						log.info("Error al insertar el usuario " + usuario.getUsername());
						rutaFormulario.forward(request, response);
						break;
					} finally {
						usuarios.cerrar();
					}
					session.removeAttribute("errorUsuario");
					rutaListado.forward(request, response);
				} else {
					session.setAttribute("errorUsuario", "Las contraseñas deben ser iguales");
					request.setAttribute("usuario", usuario);
					rutaFormulario.forward(request, response);
				}
				break;

			case "modificar":
				usuario = new Usuario(id, id_roles, nombre_completo, password, username);
				if (!("admin").equals(usuario.getUsername())) {
					if (password != null && password != "" && password.equals(password2)) {
						usuarios.abrir();
						try {
							usuarios.update(usuario);
							session.removeAttribute("errorUsuario");
							log.info("Usuario modificado");
						} catch (DAOException e) {
							session.setAttribute("errorUsuario", "Error al modificar el usuario. Inténtelo de nuevo");
							request.setAttribute("usuario", usuario);
							log.info("Error al modificar el usuario");
							rutaFormulario.forward(request, response);
							break;
						} finally {
							usuarios.cerrar();
						}
						session.removeAttribute("errorUsuario");
						rutaListado.forward(request, response);
					} else {
						session.setAttribute("errorUsuario", "Las contraseñas deben ser iguales");
						request.setAttribute("usuario", usuario);
						rutaFormulario.forward(request, response);
					}
				} else {
					session.setAttribute("errorUsuario", "Por el momento no es posible modificar el usuario 'admin'");
					request.setAttribute("usuario", usuario);
					rutaFormulario.forward(request, response);
				}
				break;

			case "borrar":
				usuario = new Usuario(id, id_roles, nombre_completo, password, username);
				if (!("admin").equals(usuario.getUsername())) {
					usuarios.abrir();
					try {
						usuarios.delete(usuario);
						session.removeAttribute("errorUsuario");
						log.info("Usuario borrado");
					} catch (DAOException e) {
						session.setAttribute("errorUsuario", "Error al borrar el usuario. Inténtelo de nuevo");
						request.setAttribute("usuario", usuario);
						log.info("Error al borrar el usuario " + usuario.getUsername());
						rutaFormulario.forward(request, response);
						break;
					} finally {
						usuarios.cerrar();
					}
					session.removeAttribute("errorUsuario");
					rutaListado.forward(request, response);
					break;
				} else {
					session.setAttribute("errorUsuario", "Por el momento no es posible borrar el usuario 'admin'");
					request.setAttribute("usuario", usuario);
					rutaFormulario.forward(request, response);
				}
				break;
			default:
				session.removeAttribute("errorUsuario");
				rutaListado.forward(request, response);
			}
		}
	}

}
