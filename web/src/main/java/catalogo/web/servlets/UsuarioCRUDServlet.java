package catalogo.web.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import catalogo.dal.UsuarioDAO;
import catalogo.tipos.Usuario;

@WebServlet("/admin/usuariocrud")
public class UsuarioCRUDServlet extends HttpServlet {
	static final String RUTA_FORMULARIO = "/WEB-INF/vistas/usuarioform.jsp";
	static final String RUTA_LISTADO = "/WEB-INF/vistas/usuariocrud.jsp";
	static final String RUTA_SERVLET_LISTADO = "/admin/usuariocrud";

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();
		
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");

		String op = request.getParameter("op");

		if (op == null) {

			usuarios.abrir();
			Usuario[] usuariosArr = usuarios.findAll();
			usuarios.cerrar();

			application.setAttribute("usuariosArr", usuariosArr);

			request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);

		} else {

			Usuario usuario;

			switch (op) {
			case "modificar":
			case "borrar":
				int id;
				try {
					id = Integer.parseInt(request.getParameter("id"));
				} catch (Exception e) {
					e.printStackTrace();
					request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);
					break;
				}
				usuarios.abrir();
				try {
					usuario = usuarios.findById(id);
				} catch (Exception e) {
					e.printStackTrace();
					usuarios.cerrar();
					request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);
					break;
				}
				usuarios.cerrar();
				request.setAttribute("usuario", usuario);
			case "alta":
				request.getRequestDispatcher(RUTA_FORMULARIO).forward(request, response);
				break;
			default:
				request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);
			}
		}
	}

}
