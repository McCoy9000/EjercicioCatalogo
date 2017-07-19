package catalogo.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import catalogo.constantes.Constantes;
import catalogo.dal.DireccionDAO;
import catalogo.dal.UsuarioDAO;
import catalogo.tipos.Direccion;
import catalogo.tipos.Usuario;
import catalogo.tipos.UsuarioMascara;

@WebServlet("/usuarioperfil")
public class UsuarioPerfilServlet extends HttpServlet {

	private static final long serialVersionUID = -1675125190483219932L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext application = request.getServletContext();
		
		HttpSession session = request.getSession();
		
		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");
		
		DireccionDAO direcciones = (DireccionDAO) application.getAttribute("direcciones");
		
		int id = ((Usuario) session.getAttribute("usuario")).getId();
		
		String username = request.getParameter("username");
		
		String password = request.getParameter("password");
		
		String nombre_completo = request.getParameter("nombre_completo");
		
		String apellidos = request.getParameter("apellidos");
		
		String documento = request.getParameter("documento");
		
		String telefono = request.getParameter("telefono");
		
		String direccion = request.getParameter("direccion");

		String codigoPostal = request.getParameter("codigoPostal");

		String ciudad = request.getParameter("ciudad");

		String region = request.getParameter("region");

		String pais = request.getParameter("pais");
		
		Date fechaDeNacimiento = new Date(request.getParameter("fechaDeNacimiento"));
		
		String empresa = request.getParameter("empresa");
		
		int id_roles = 2;
		
		String op = request.getParameter("op");
		
		if (op == null) {
			usuarios.abrir();
			UsuarioMascara usuarioMascara = usuarios.findMaskById(id);
			usuarios.cerrar();
									
			session.setAttribute("usuarioMascara", usuarioMascara);
			
			
			request.getRequestDispatcher(Constantes.RUTA_PERFIL_USUARIO).forward(request, response);
			return;
		} else {
			switch (op) {
			case "actualizar":
				
				Usuario usuario = new Usuario(id_roles, nombre_completo, apellidos, username, password);
				usuarios.abrir();
				boolean usuarioExistente = usuarios.validarNombre(usuario);
				
				if (usuarioExistente) {
					session.setAttribute("errorPerfil", "El nombre de usuario ya existe");
					request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PERFIL_USUARIO).forward(request, response);
				} else
				
				usuario = usuarios.findById(id);
				usuario.setUsername(username);
				usuario.setPassword(password);
				
				
			}
		}
	}

}
