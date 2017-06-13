package catalogo.web;

import java.io.IOException;
import java.util.LinkedList;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginServlet.class);

	private final String RUTA = "/WEB-INF/vistas";
	private final String RUTA_LOGIN = RUTA + "/login.jsp";
	private final String RUTA_CATALOGO = "/catalogo";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(1800);
		ServletContext application = request.getServletContext();

		// Recogida de datos de la request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String op = request.getParameter("op");

		// Recogida de datos de aplicación y de sesión
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");
		@SuppressWarnings("unchecked")
		LinkedList<Usuario> usuariosLogueados = (LinkedList<Usuario>) application.getAttribute("usuariosLogueados");
		Usuario usuario;

		if (session.getAttribute("usuario") != null) {

			usuario = (Usuario) session.getAttribute("usuario");

		} else
			usuario = new Usuario(username, password);

		// Declaración e inicialización de las booleanas que representan las diferentes posibilidades de entrada
		boolean yaLogueado = ("si").equals(session.getAttribute("logueado"));
		boolean sinDatos = username == null || username == "" || password == "" || password == null;
		boolean uInexistente = false;

		usuarios.abrir();
		uInexistente = !((UsuarioDAO) usuarios).validarNombre(usuario);
		usuarios.cerrar();

		boolean esValido = false;
		usuarios.abrir();
		esValido = usuarios.validar(usuario);
		usuarios.cerrar();

		boolean quiereSalir = ("logout").equals(op);

		// Declaración e inicialización de los dispatcher ya que en un momento dado me daba problemas inicializarlos
		// directamente cuando son requeridos.
		RequestDispatcher login = request.getRequestDispatcher(RUTA_LOGIN);
		RequestDispatcher catalogo = request.getRequestDispatcher(RUTA_CATALOGO);

		// Lógica del servlet según opciones
		if (quiereSalir) {

			usuariosLogueados.remove(usuario);
			session.invalidate();
			catalogo.forward(request, response);

		} else if (yaLogueado) {

			session.removeAttribute("errorLogin");
			catalogo.forward(request, response);

		} else if (sinDatos) {

			session.setAttribute("errorLogin", "Debes rellenar todos los campos");
			login.forward(request, response);

		} else if (uInexistente) {

			session.setAttribute("errorLogin", "Usuario no encontrado");
			login.forward(request, response);

		} else if (esValido) {

			log.info("Usuario logueado");
			usuarios.abrir();
			usuario = usuarios.findByName(usuario.getUsername()); // Esta línea permite recoger el dato de si tiene permiso de admin o no
			usuarios.cerrar();
			usuariosLogueados.add(usuario);
			application.setAttribute("usuariosLogueados", usuariosLogueados);
			session.removeAttribute("errorLogin");
			session.setAttribute("logueado", "si");
			session.setAttribute("usuario", usuario);
			// Carrito carrito = new Carrito();
			// session.setAttribute("carrito", carrito);
			catalogo.forward(request, response);

		} else {

			session.setAttribute("errorLogin", "Contraseña incorrecta");
			login.forward(request, response);
		}
	}
}
