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

import catalogo.dal.CarritoDAO;
import catalogo.dal.ProductoDAO;
import catalogo.dal.UsuarioDAO;
import catalogo.tipos.Producto;
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
		ServletContext application = request.getServletContext();

		// Recogida de datos de la request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String op = request.getParameter("op");

		// Recogida de datos de aplicación y de sesión
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");
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
			// Si se desloguea se vacía el carrito y los productos vuelven a la base de datos

			productos.abrir();
			productos.iniciarTransaccion();
			if (!(carrito == null)) {

				for (Producto p : carrito.buscarTodosLosProductos()) {
					productos.insert(p);
				}
			}
			usuariosLogueados.remove(usuario);
			// Se invalida la sesión y se le envía al catálogo que es donde se le creará un nuevo carrito si no lo tiene
			session.invalidate();
			productos.confirmarTransaccion();
			productos.cerrar();

			catalogo.forward(request, response);

		} else if (yaLogueado) {
			// Si ya está logueado el login le deja pasar directamente a la página principal, el catálogo
			session.removeAttribute("errorLogin");
			catalogo.forward(request, response);

		} else if (sinDatos) {
			// Si no se rellenan los datos se le envía al jsp del login con el mensaje de error. Da el fallo de que un usuario
			// que entra por primera vez a esta página no ha podido rellenar aún ningún dato por lo que se le mostrará el mensaje
			// de error sin que haya interactuado con la página.
			session.setAttribute("errorLogin", "Debes rellenar todos los campos");
			login.forward(request, response);

		} else if (uInexistente) {
			// Si el username no existe en la base de datos se le reenvía a la jsp de login con el correspondiente mensaje de error
			session.setAttribute("errorLogin", "Usuario no encontrado");
			login.forward(request, response);

		} else if (esValido) {
			// Si nombre y contraseña son válidos se busca el usuario correspondiente en la base de datos para rellenar el resto de datos
			// como su id_roles
			log.info("Usuario " + usuario.getUsername() + " logueado");
			usuarios.abrir();
			usuario = usuarios.findByName(usuario.getUsername());
			usuarios.cerrar();
			usuariosLogueados.add(usuario);
			application.setAttribute("usuariosLogueados", usuariosLogueados);
			session.removeAttribute("errorLogin");
			session.setAttribute("logueado", "si");
			session.setAttribute("usuario", usuario);
			// Se le envía al catálogo donde se le proporcionará un carrito
			catalogo.forward(request, response);

		} else {
			// En principio la posibilidad que queda es que el usuario exista pero la password sea incorrecta
			session.setAttribute("errorLogin", "Contraseña incorrecta");
			login.forward(request, response);
		}
	}
}
