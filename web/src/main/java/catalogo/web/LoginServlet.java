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

import catalogo.dal.UsuariosDAL;
import catalogo.tipos.Carrito;
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

		String nombre = request.getParameter("nombre");
		String password = request.getParameter("password");
		String op = request.getParameter("op");
		Usuario usuario;
		UsuariosDAL usuarios = (UsuariosDAL) application.getAttribute("usuarios");
		LinkedList<Usuario> usuariosLogueados = (LinkedList<Usuario>) application.getAttribute("usuariosLogueados");
		
		if (session.getAttribute("usuario") != null) {

			usuario = (Usuario) session.getAttribute("usuario");

		} else
			usuario = new Usuario(nombre, password);

		boolean yaLogueado = ("si").equals(session.getAttribute("logueado"));
		boolean sinDatos = nombre == null || nombre == "" || password == "" || password == null;
		boolean uInexistente = !((UsuariosDAL) usuarios).validarNombre(usuario);
		boolean esValido = usuarios.validar(usuario);
		boolean quiereSalir = ("logout").equals(op);

		RequestDispatcher login = request.getRequestDispatcher(RUTA_LOGIN);
		log.info(request.getContextPath());
		log.info(RUTA_LOGIN);
		RequestDispatcher catalogo = request.getRequestDispatcher(RUTA_CATALOGO);

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
			usuario = usuarios.buscarPorId(usuario.getNombre());
			usuariosLogueados.add(usuario);
			application.setAttribute("usuariosLogueados", usuariosLogueados);
			session.removeAttribute("errorLogin");
			session.setAttribute("logueado", "si");
			session.setAttribute("usuario", usuario);
			Carrito carrito = new Carrito();
			session.setAttribute("carrito", carrito);
			catalogo.forward(request, response);

		} else {

			session.setAttribute("errorLogin", "Contraseña incorrecta");
			login.forward(request, response);
		}
	}
}
