package catalogo.servlets;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
import catalogo.encriptacion.Encriptador;
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

		// RECOPILACIÓN DE LOS OBJETOS A ANALIZAR Y UTILIZAR
		HttpSession session = request.getSession();
		ServletContext application = request.getServletContext();
		// Borrado de errores en sesión por si llegan aquí desde los formularios CRUD
		session.removeAttribute("errorProducto");
		session.removeAttribute("errorUsuario");
		session.removeAttribute("errorSignup");
		session.removeAttribute("errorLogin");
		// Recogida de datos de la request
		String username, rawpassword, password = null;

		Encriptador miEncriptador = null;
		byte[] encryptedpass = null;

		if (request.getParameter("username") != null) {
			username = request.getParameter("username").trim();
		} else {
			username = request.getParameter("username");
		}

		if (request.getParameter("password") != null) {
			rawpassword = request.getParameter("password").trim();
		} else {
			rawpassword = request.getParameter("password");
		}

		try {
			miEncriptador = new Encriptador();
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (rawpassword != null) {

			try {
				encryptedpass = miEncriptador.cipher.doFinal(rawpassword.getBytes());
			} catch (IllegalBlockSizeException | BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			password = Base64.getMimeEncoder().encodeToString(encryptedpass);
		}

		String op = request.getParameter("op");

		// Recogida de datos de aplicación y de sesión
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");
		// @SuppressWarnings("unchecked")
		// LinkedList<Usuario> usuariosLogueados = (LinkedList<Usuario>) application.getAttribute("usuariosLogueados");

		// Declaración de un objeto usuario para trabajar sobre él
		Usuario usuario;

		// Hasta que alguien no se loguea el objeto session no tiene un usuario asociado por lo que puede
		// ser nulo. Si no es nulo, se recoge, si lo es, se crea uno nuevo con los datos recogidos en la request
		if (session.getAttribute("usuario") != null) {
			usuario = (Usuario) session.getAttribute("usuario");
		} else
			usuario = new Usuario(username, password);

		// DECLARACIÓN E INICIALIZACIÓN DE LAS BOOLEANAS SOBRE LAS QUE SE BASARÁ LA LÓGICA DEL SERVLET

		boolean quiereSalir = ("logout").equals(op);
		boolean yaLogueado = ("si").equals(session.getAttribute("logueado"));
		boolean yaEnUsuariosLogueados = false;

		// if (usuario.getUsername() != null && !usuariosLogueados.isEmpty()) {
		// for(Usuario u: usuariosLogueados) {
		// if(usuario.getUsername().equals(u.getUsername())) {
		// yaEnUsuariosLogueados = true;
		// }
		// }
		// }
		// sinDatos puede significar que alguien ha intentado loguearse sin datos o que es la primera vez
		// que llega al servlet sin que se le hayan pedido datos aún. Es la condición de partida de llegada
		// al servlet
		boolean sinDatos = username == null || username == "" || password == "" || password == null;
		boolean uInexistente = false;
		boolean esValido = false;
		usuarios.abrir();
		uInexistente = !usuarios.validarNombre(usuario);
		esValido = usuarios.validar(usuario);
		usuarios.cerrar();

		// Declaración e inicialización de los dispatcher ya que en un momento dado me daba problemas inicializarlos
		// directamente cuando son requeridos.
		RequestDispatcher login = request.getRequestDispatcher(RUTA_LOGIN);
		RequestDispatcher catalogo = request.getRequestDispatcher(RUTA_CATALOGO);

		// LÓGICA DEL SERVLET SEGÚN LOS VALORES DE LAS BOOLEANAS
		if (quiereSalir) {
			// Se invalida la sesión y se le envía al catálogo que es el punto de partida de la aplicación
			session.invalidate();
			// usuariosLogueados.remove(usuario);
			session = request.getSession();
			// session.setAttribute("usuariosLogueados", usuariosLogueados);
			catalogo.forward(request, response);

		} else if (yaLogueado) {
			// Si ya está logueado el login le deja pasar directamente a la página principal, el catálogo
			session.removeAttribute("errorLogin");
			catalogo.forward(request, response);

		} else if (sinDatos) {
			// Si no se rellenan los datos se le envía al jsp del login con el mensaje de error. Da el fallo de que un usuario
			// que entra por primera vez a esta página no ha podido rellenar aún ningún dato por lo que se le mostrará el mensaje
			// de error sin que haya interactuado con la página.
			login.forward(request, response);

		} else if (uInexistente) {
			// Si el username no existe en la base de datos se le reenvía a la jsp de login con el correspondiente mensaje de error
			session.setAttribute("errorLogin", "Usuario no encontrado");
			login.forward(request, response);

		} else if (esValido) {
			// Si el usuario ya está logueado no deja volver a loguearse con el mismo usuario
			if (yaEnUsuariosLogueados) {
				session.setAttribute("errorLogin", "Este usuario ya está logueado");
				login.forward(request, response);
				// Si nombre y contraseña son válidos se busca el usuario correspondiente en la base de datos para rellenar el resto de datos
				// como su id_roles y se almacena este usuario en el objeto session.
			} else {
				log.info("Usuario " + usuario.getUsername() + " logueado");
				usuarios.abrir();
				usuario = usuarios.findByName(usuario.getUsername());
				usuarios.cerrar();
				// usuariosLogueados.add(usuario);
				// application.setAttribute("usuariosLogueados", usuariosLogueados);
				session.removeAttribute("errorLogin");
				session.setAttribute("logueado", "si");
				session.setAttribute("usuario", usuario);
				// Se le envía al catálogo
				catalogo.forward(request, response);
			}

		} else {
			// En principio, la posibilidad que queda es que el usuario exista pero la password sea incorrecta
			session.setAttribute("errorLogin", "Contraseña incorrecta");
			login.forward(request, response);
		}
	}
}
