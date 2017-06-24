package catalogo.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");

		String op = request.getParameter("opform");

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String nombre_completo = request.getParameter("nombre_completo");
		int id_roles;
		if (request.getParameter("id_roles") == null) {
			id_roles = 2;
		} else {
			try {
				id_roles = Integer.parseInt(request.getParameter("id_roles"));
			} catch (Exception e) {
				id_roles = 2;
				e.printStackTrace();
			}
		}

		RequestDispatcher rutaListado = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_FORMULARIO);

		if (op == null) {
			rutaListado.forward(request, response);
			return;
		} else {

			Usuario usuario = new Usuario(id_roles, nombre_completo, password, username);

			switch (op) {

				case "alta":
					if (password != null && password != "" && password.equals(password2)) {
						try {
							usuarios.abrir();
							usuarios.insert(usuario);
							usuarios.cerrar();
							log.info("Usuario " + usuario.getUsername() + " dado de alta");
						} catch (DAOException e) {
							// Si falla el insert se coge la excepción que lanza y se le reenvía al formulario con el objeto
							// usuario que traía metido en la request
							request.setAttribute("usuario", usuario);
							e.printStackTrace();
							rutaFormulario.forward(request, response);
						}
						rutaListado.forward(request, response);
					} else {
						request.setAttribute("usuario", usuario);
						rutaFormulario.forward(request, response);
					}
					break;

				case "modificar":
					if (!("admin").equals(usuario.getUsername())) {
						if (password != null && password != "" && password.equals(password2)) {
							try {
								usuarios.abrir();
								usuarios.update(usuario);
								usuarios.cerrar();
								log.info("Usuario modificado");
							} catch (DAOException e) {
								request.setAttribute("usuario", usuario);
								e.printStackTrace();
								rutaFormulario.forward(request, response);
		
							}
							rutaListado.forward(request, response);
						} else {
							request.setAttribute("usuario", usuario);
							rutaFormulario.forward(request, response);
						}
					} else {
						request.setAttribute("usuario", usuario);
						rutaFormulario.forward(request, response);
					}
					break;
		
				case "borrar":
					if (!("admin").equals(usuario.getUsername())) {
						try {
							usuarios.abrir();
							usuarios.delete(usuario);
							usuarios.cerrar();
							log.info("Usuario borrado");
							rutaListado.forward(request, response);
						} catch (DAOException e) {
							request.setAttribute("usuario", usuario);
							e.printStackTrace();
							rutaFormulario.forward(request, response);
						}
					} else {
						rutaListado.forward(request, response);
					}
					break;
				default: 
					rutaListado.forward(request, response);
			}
		}
	}

}
