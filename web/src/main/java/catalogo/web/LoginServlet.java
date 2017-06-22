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

		// RECOPILACION DE LOS OBJETOS A ANALIZAR Y UTILIZAR
		HttpSession session = request.getSession();
		ServletContext application = request.getServletContext();

		// Recogida de datos de la request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String op = request.getParameter("op");

		// Recogida de datos de aplicaci�n y de sesi�n
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");
		@SuppressWarnings("unchecked")
		LinkedList<Usuario> usuariosLogueados = (LinkedList<Usuario>) application.getAttribute("usuariosLogueados");

		// Declaraci�n de un objeto usuario para trabajar sobre �l
		Usuario usuario;

		// Hasta que alguien no se loguea el objeto session no tiene un usuario asociado por lo que puede
		// ser nulo. Si no es nulo, se recoge, si lo es, se crea uno nuevo con los datos recogidos en la request
		if (session.getAttribute("usuario") != null) {

			usuario = (Usuario) session.getAttribute("usuario");

		} else
			usuario = new Usuario(username, password);

		// DECLARACI�N E INICIALIZACI�N DE LAS BOOLEANAS SOBRE LAS QUE SE BASAR� LA L�GICA DEL SERVLET

		boolean quiereSalir = ("logout").equals(op);
		boolean yaLogueado = ("si").equals(session.getAttribute("logueado"));
		// sinDatos puede significar que alguien ha intentado loguearse sin datos o que es la primera vez
		// que llega al servlet sin que se le hayan pedido datos a�n. Es la condici�n de partida de llegada
		// al servlet
		boolean sinDatos = username == null || username == "" || password == "" || password == null;
		boolean uInexistente = false;
		boolean esValido = false;
		usuarios.abrir();
		uInexistente = !usuarios.validarNombre(usuario);
		esValido = usuarios.validar(usuario);
		usuarios.cerrar();

		// Declaraci�n e inicializaci�n de los dispatcher ya que en un momento dado me daba problemas inicializarlos
		// directamente cuando son requeridos.
		RequestDispatcher login = request.getRequestDispatcher(RUTA_LOGIN);
		RequestDispatcher catalogo = request.getRequestDispatcher(RUTA_CATALOGO);

		// LOGICA DEL SERVLET SEG�N LOS VALORES DE LAS BOOLEANAS
		if (quiereSalir) {
			// Se invalida la sesi�n y se le env�a al cat�logo que es el punto de partida de la aplicaci�n
			session.invalidate();
			usuariosLogueados.remove(usuario);
			catalogo.forward(request, response);

		} else if (yaLogueado) {
			// Si ya est� logueado el login le deja pasar directamente a la p�gina principal, el cat�logo
			session.removeAttribute("errorLogin");
			catalogo.forward(request, response);

		} else if (sinDatos) {
			// Si no se rellenan los datos se le env�a al jsp del login con el mensaje de error. Da el fallo de que un usuario
			// que entra por primera vez a esta p�gina no ha podido rellenar a�n ning�n dato por lo que se le mostrar� el mensaje
			// de error sin que haya interactuado con la p�gina.
			session.setAttribute("errorLogin", "Debes rellenar todos los campos");
			login.forward(request, response);

		} else if (uInexistente) {
			// Si el username no existe en la base de datos se le reenv�a a la jsp de login con el correspondiente mensaje de error
			session.setAttribute("errorLogin", "Usuario no encontrado");
			login.forward(request, response);

		} else if (esValido) {
			// Si nombre y contrase�a son v�lidos se busca el usuario correspondiente en la base de datos para rellenar el resto de datos
			// como su id_roles y se almacena este usuario en el objeto session.
			log.info("Usuario " + usuario.getUsername() + " logueado");
			usuarios.abrir();
			usuario = usuarios.findByName(usuario.getUsername());
			usuarios.cerrar();
			usuariosLogueados.add(usuario);
			application.setAttribute("usuariosLogueados", usuariosLogueados);
			session.removeAttribute("errorLogin");
			session.setAttribute("logueado", "si");
			session.setAttribute("usuario", usuario);
			// Se le env�a al cat�logo
			catalogo.forward(request, response);

		} else {
			// En principio la posibilidad que queda es que el usuario exista pero la password sea incorrecta
			session.setAttribute("errorLogin", "Contrase�a incorrecta");
			login.forward(request, response);
		}
	}
}
