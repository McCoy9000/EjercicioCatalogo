package catalogo.servlets;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
import catalogo.tipos.UsuarioMascara;

@WebServlet("/usuarioperfil")
public class UsuarioPerfilServlet extends HttpServlet {

	private static final long serialVersionUID = -1675125190483219932L;

	private static Logger log = Logger.getLogger(CheckoutServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = request.getServletContext();

		HttpSession session = request.getSession();

		session.removeAttribute("errorPerfil");

		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");

		int id = ((Usuario) session.getAttribute("usuario")).getId();

		String username = request.getParameter("username");

		Encriptador miEncriptador = null;
		byte[] encryptedpass = null, encryptedpass2 = null;

		String password = null;

		String password2 = null;

		String rawpassword = request.getParameter("rawpassword");

		String rawpassword2 = request.getParameter("rawpassword2");

		if (request.getParameter("rawpassword") != null) {
			rawpassword = request.getParameter("rawpassword").trim();
		} else {
			rawpassword = request.getParameter("rawpassword");
		}

		if (request.getParameter("rawpassword2") != null) {
			rawpassword2 = request.getParameter("rawpassword2").trim();
		} else {
			rawpassword2 = request.getParameter("rawpassword2");
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

		String nombre_completo = request.getParameter("nombre_completo");

		String apellidos = request.getParameter("apellidos");

		String documento = request.getParameter("documento");

		String telefono = request.getParameter("telefono");

		String direccion = request.getParameter("direccion");

		String codigoPostal = request.getParameter("codigoPostal");

		String ciudad = request.getParameter("ciudad");

		String region = request.getParameter("region");

		String pais = request.getParameter("pais");

		String empresa = request.getParameter("empresa");

		int id_roles = 2;

		String op = request.getParameter("op");

		String opform = request.getParameter("opform");

		if (("ver").equals(op)) {
			usuarios.abrir();
			UsuarioMascara usuarioMascara = usuarios.findMaskById(id);
			usuarios.cerrar();

			session.setAttribute("usuarioMascara", usuarioMascara);

			request.getRequestDispatcher(Constantes.RUTA_PERFIL_USUARIO).forward(request, response);
			return;
		} else {
			switch (opform) {
			case "formulario":
				usuarios.abrir();
				UsuarioMascara usuarioMascara = usuarios.findMaskById(id);
				usuarios.cerrar();

				session.setAttribute("usuarioMascara", usuarioMascara);

				request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PERFIL_USUARIO).forward(request, response);
				return;

			case "modificar":

				Usuario usuario = new Usuario(id_roles, nombre_completo, apellidos, username, password);
				usuarios.abrir();
				boolean usuarioExistente = usuarios.validarNombre(usuario) && usuario.getNombre_completo() != nombre_completo;

				if (usuarioExistente) {
					log.info("pasa por usuario existente");
					session.setAttribute("errorPerfil", "El nombre de usuario ya existe");
					request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PERFIL_USUARIO).forward(request, response);
					return;
				} else if (password != null && password != "" && password.equals(password2)) {

					usuario = usuarios.findById(id);
					usuario.setUsername(username);
					usuario.setPassword(password);
					usuario.setNombre_completo(nombre_completo);
					usuario.setApellidos(apellidos);
					usuario.setDocumento(documento);
					usuario.setTelefono(telefono);
					usuario.setDireccion(direccion);
					usuario.setCodigoPostal(codigoPostal);
					usuario.setCiudad(ciudad);
					usuario.setRegion(region);
					usuario.setPais(pais);
					usuario.setEmpresa(empresa);

					usuarios.update(usuario);

					usuarios.findMaskById(id);
					usuarios.cerrar();

					session.setAttribute("usuarioMascara", usuario);
					session.removeAttribute("errorPerfil");
					request.getRequestDispatcher(Constantes.RUTA_PERFIL_USUARIO).forward(request, response);
					return;
				} else {
					session.setAttribute("errorPerfil", "Las contraseñas no coinciden");
					request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PERFIL_USUARIO).forward(request, response);
					return;
				}
			}
		}
	}

}
