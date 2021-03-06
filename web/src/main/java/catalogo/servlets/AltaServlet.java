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

import catalogo.constantes.Constantes;
import catalogo.dal.UsuarioDAO;
import catalogo.encriptacion.Encriptador;
import catalogo.tipos.Usuario;

@WebServlet("/alta")
public class AltaServlet extends HttpServlet {

	private static final long serialVersionUID = -325036965413542652L;
	
	private static Logger log = Logger.getLogger(AltaServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// RECOGIDA DE DATOS Y OBJETOS PARA TRABAJAR SOBRE ELLOS

		// Se recogen los objetos sesión y aplicación
		HttpSession session = request.getSession();
		ServletContext application = request.getServletContext();
		// Borrado de errores en sesión por si llegan aquí desde los formularios CRUD
		session.removeAttribute("errorSignup");
		// Se obtiene el conjunto de usuarios extraído de la BBDD e introducido en el objeto application en el
		// listener de la aplicación
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");
		// Se recogen los valores de los atributos de usuario introducidos en el formulario de alta
		String username, rawpassword, rawpassword2, password = null, password2 = null, nombre_completo, apellidos;

		Encriptador miEncriptador = null;
		byte[] encryptedpass = null, encryptedpass2 = null;

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

		if (request.getParameter("password2") != null) {
			rawpassword2 = request.getParameter("password2").trim();
		} else {
			rawpassword2 = request.getParameter("password2");
		}

		try {
			miEncriptador = new Encriptador();
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e1) {
			e1.printStackTrace();
		}

		if (rawpassword != null) {
			try {
				encryptedpass = miEncriptador.cipher.doFinal(rawpassword.getBytes());
			} catch (IllegalBlockSizeException | BadPaddingException e1) {
				e1.printStackTrace();
			}
			password = Base64.getMimeEncoder().encodeToString(encryptedpass);
		}

		if (rawpassword2 != null) {
			try {
				encryptedpass2 = miEncriptador.cipher.doFinal(rawpassword2.getBytes());
			} catch (IllegalBlockSizeException | BadPaddingException e1) {
				e1.printStackTrace();
			}
			password2 = Base64.getMimeEncoder().encodeToString(encryptedpass2);
		}

		if (request.getParameter("nombre_completo") != null) {
			nombre_completo = request.getParameter("nombre_completo").trim();
		} else {
			nombre_completo = request.getParameter("nombre_completo");
		}

		if (request.getParameter("apellidos") != null) {
			apellidos = request.getParameter("apellidos").trim();
		} else {
			apellidos = request.getParameter("apellidos");
		}

		// id_roles se asigna directamente como usuario estándar
		int id_roles = 2;
		// Se crea un objeto usuario con el que trabajar a partir de esos datos
		Usuario usuario = new Usuario(id_roles, nombre_completo, apellidos, username, password);
		
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

		// Declaro los dispatcher aquí porque en un momento me dieron un extraño error al declararlos en el punto que los necesitaba
		RequestDispatcher login = request.getRequestDispatcher(Constantes.RUTA_LOGIN);
		RequestDispatcher alta = request.getRequestDispatcher(Constantes.RUTA_ALTA);

		// LÓGICA DEL SERVLET
		if (sinDatos) {

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
