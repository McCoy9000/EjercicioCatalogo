package catalogo.web;

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

import catalogo.dal.UsuarioDAO;
import catalogo.tipos.Usuario;

@WebServlet("/alta")
public class AltaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(AltaServlet.class);

	private final String RUTA = "/WEB-INF/vistas/";
	private final String RUTA_LOGIN = RUTA + "login.jsp";
	private final String RUTA_ALTA = RUTA + "signup.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		ServletContext application = request.getServletContext();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String nombre_completo = request.getParameter("nombre_completo");
		int id_roles = 2;

		Usuario usuario = new Usuario(id_roles, nombre_completo, password, username);

		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");

		boolean nombreDemasiadoLargo = false;

		if (username != null) {

			nombreDemasiadoLargo = username.length() > 16;
		}

		boolean usuarioExistente = false;

		usuarios.abrir();

		usuarioExistente = usuarios.validarNombre(usuario);

		usuarios.cerrar();

		boolean sinDatos = username == null || username == "" || password == null || password == "" || password2 == null || password2 == "";
		boolean passIguales = true;

		if (password != null) {

			passIguales = password.equals(password2);
		}

		boolean esCorrecto = false;

		if (!sinDatos) {

			esCorrecto = !usuarioExistente && passIguales;
		}

		RequestDispatcher login = request.getRequestDispatcher(RUTA_LOGIN);
		RequestDispatcher alta = request.getRequestDispatcher(RUTA_ALTA);

		if (sinDatos) {

			session.setAttribute("errorSignup", "Debes rellenar todos los campos");
			alta.forward(request, response);

		} else if (nombreDemasiadoLargo) {

			session.setAttribute("errorSignup", "El nombre de usuario debe tener un máximo de 16 caracteres");
			alta.forward(request, response);

		} else if (usuarioExistente) {

			session.setAttribute("errorSignup", "Usuario ya existente");
			alta.forward(request, response);

		} else if (!passIguales) {

			session.setAttribute("errorSignup", "Las contraseñas no coinciden");
			alta.forward(request, response);

		} else if (esCorrecto) {

			session.removeAttribute("errorSignup");

			usuarios.abrir();

			usuarios.insert(usuario);

			usuarios.cerrar();

			log.info("Usuario dado de alta");
			login.forward(request, response);

		} else {

			session.setAttribute("errorSignup", "Inténtalo de nuevo, por favor");
			alta.forward(request, response);
		}
	}
}
