package catalogo.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import catalogo.constantes.Constantes;
import catalogo.dal.UsuarioDAO;
import catalogo.tipos.Usuario;
import catalogo.tipos.UsuarioMascara;

@WebServlet("/admin/usuariocrud")
public class UsuarioCRUDServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();
		
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");

		HttpSession session = request.getSession();
		// Borrado de errores en sesión por si llegan aquí desde los formularios CRUD
		session.removeAttribute("errorProducto");
		session.removeAttribute("errorUsuario");
		session.removeAttribute("errorLogin");
		session.removeAttribute("errorSignup");

		String op = request.getParameter("op");

		if (op == null) {

			usuarios.abrir();
			UsuarioMascara[] usuariosArr = usuarios.findAllMasks();
			usuarios.cerrar();

			application.setAttribute("usuariosArr", usuariosArr);

			request.getRequestDispatcher(Constantes.RUTA_LISTADO_USUARIO).forward(request, response);

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
					request.getRequestDispatcher(Constantes.RUTA_LISTADO_USUARIO).forward(request, response);
					break;
				}
				usuarios.abrir();
				try {
					usuario = usuarios.findById(id);
				} catch (Exception e) {
					e.printStackTrace();
					usuarios.cerrar();
					request.getRequestDispatcher(Constantes.RUTA_LISTADO_USUARIO).forward(request, response);
					break;
				}
				usuarios.cerrar();
				request.setAttribute("usuario", usuario);
			case "alta":
				request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_USUARIO).forward(request, response);
				break;
			default:
				request.getRequestDispatcher(Constantes.RUTA_LISTADO_USUARIO).forward(request, response);
			}
		}
	}

}
