package catalogo.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import catalogo.dal.DALException;
import catalogo.dal.UsuarioYaExistenteDALException;
import catalogo.dal.UsuariosDAL;
import catalogo.tipos.Usuario;

@WebServlet("/admin/usuarioform")
public class UsuarioFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(UsuarioFormServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();

		String op = request.getParameter("opform");

		String nombre = request.getParameter("nombre");
		String pass = request.getParameter("pass");
		String pass2 = request.getParameter("pass2");
		String admin = request.getParameter("admin");
		boolean isAdmin = false;
			if (("si").equals(admin)) 
				isAdmin = true;
		
		
		RequestDispatcher rutaListado = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_FORMULARIO);

		if (op == null) {
			rutaListado.forward(request, response);
			return;
		}

		Usuario usuario = new Usuario(nombre, pass, isAdmin);

		UsuariosDAL usuarios = (UsuariosDAL) application.getAttribute("usuarios");

		switch (op) {
		case "alta":
			if (pass != null && pass != "" && pass.equals(pass2)) {
				try{
				usuarios.alta(usuario);
				log.info("Usuario dado de alta");
				} catch (UsuarioYaExistenteDALException uyede) {
					usuario.setErrores("Usuario ya existente");
					request.setAttribute("usuario", usuario);
					rutaFormulario.forward(request, response);
				}
				rutaListado.forward(request, response);
			} else {
				usuario.setErrores("Las contraseñas deben ser iguales y no estar vacías");
				request.setAttribute("usuario", usuario);
				rutaFormulario.forward(request, response);
			}

			break;
		case "modificar":
			if (pass != null && pass != "" && pass.equals(pass2)) {
				try {
					usuarios.modificar(usuario);
					log.info("Usuario modificado");
				} catch (DALException de) {
					usuario.setErrores(de.getMessage());
					request.setAttribute("usuario", usuario);
					rutaFormulario.forward(request, response);
					
				}
				rutaListado.forward(request, response);
			} else {
				usuario.setErrores("Las contraseñas no coinciden");
				request.setAttribute("usuario", usuario);
				rutaFormulario.forward(request, response);
			}

			break;
		case "borrar":
			
			if (!("admin").equals(usuario.getNombre())) {
			usuarios.borrar(usuario);
			log.info("Usuario borrado");
			rutaListado.forward(request, response);
			} else {
			usuario.setErrores("No es posible borrar el usuario administrador. Sólo modificarlo.");	
			rutaListado.forward(request, response);
			}
			break;
		}
	}

}
