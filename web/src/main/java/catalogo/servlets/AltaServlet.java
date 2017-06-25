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

		// RECOGIDA DE DATOS Y OBJETOS PARA TRABAJAR SOBRE ELLOS

		// Se recogen los objetos sesión y aplicación
		HttpSession session = request.getSession();
		ServletContext application = request.getServletContext();
		// Se obtiene el conjunto de usuarios extraído de la BBDD e introducido en el objeto application en el
		// listener de la aplicación
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");
		// Se recogen los valores de los atributos de usuario introducidos en el formulario de alta
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String nombre_completo = request.getParameter("nombre_completo");
		// id_roles se asigna directamente como usuario estándar
		int id_roles = 2;
		// Se crea un objeto usuario con el que trabajar a partir de esos datos
		Usuario usuario = new Usuario(id_roles, nombre_completo, password, username);

		// Se declara e inicializan las booleanas a partir de las cuales se desarrollará la lógica del servlet
		boolean nombreDemasiadoLargo = false;
		if (username != null) {
			nombreDemasiadoLargo = username.length() > 16;
		}
		boolean usuarioExistente = false;
		// Se considera que el usuario ya existe sólo con que coincida el username, de ahí el método validarNombre()
		usuarios.abrir();
		try {
			usuarioExistente = usuarios.validarNombre(usuario);
		} catch (Exception e) {
			usuarioExistente = true;
			e.printStackTrace();
		}
		usuarios.cerrar();
		boolean sinDatos = username == null || username == "" || password == null || password == "" || password2 == null || password2 == "";
		// Se considera que en un principio, aun sin datos, ambas pass son diferentes
		boolean passIguales = false;
		if (password != null) {
			if (password.equals(password2)) {
				passIguales = true;
			}
		}
		
		boolean esCorrecto = false;
		if (!sinDatos) {
			esCorrecto = !usuarioExistente && passIguales;
		}

		// Declaro los dispatcher aquí porque en un momento me dieron un extraño error al declararlos en el momento de necesitarlos
		RequestDispatcher login = request.getRequestDispatcher(RUTA_LOGIN);
		RequestDispatcher alta = request.getRequestDispatcher(RUTA_ALTA);

		// LOGICA DEL SERVLET
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

			log.info("Usuario " + usuario.getUsername() + " dado de alta");
			login.forward(request, response);

		} else {

			session.setAttribute("errorSignup", "Inténtalo de nuevo, por favor");
			alta.forward(request, response);
		}
	}
}
