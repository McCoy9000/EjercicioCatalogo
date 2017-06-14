package catalogo.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import catalogo.tipos.Usuario;

@WebFilter("/autorizacion")
public class AutorizacionFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		//Tal y como est� declarado el filtro, doFilter se ejecutar� cuando se acceda a una zona dentro de /admin/
		
		//Casteo del objeto request en HttpServletRequest para poder obtener el objeto session.
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		//Creaci�n de un objeto usuario para introducir en el, si lo hay, el usuario que viene en el objeto sesi�n
		Usuario usuario = null;
			if (session != null) {
				usuario = (Usuario) session.getAttribute("usuario");
			}

		//Declaraci�n de booleanas para la l�gica de la aplicaci�n
		//Si la sesi�n era null o no ten�a objeto usuario dentro se considera al usuario un nuevo usuario
		boolean esNuevoUsuario = usuario == null;
		//En principio se considera al usuario no administrador
		boolean esAdmin = false;
		//Si no es nuevo usuario el usuario no es null por lo que se le puede pedir el id_roles sin miedo al NullPointerException	
			if (!esNuevoUsuario) {
				esAdmin = usuario.getId_roles() == 1;
			}

		//L�gica del servlet filter
			
		//Si no es administrador se le enviar� al login. Le meto el mensaje de error, pero al llegar al login, como no tiene datos
		//de logueo en un primer momento, se cambia este mensaje por el de 'Debes rellenar todos los campos' :-(
		//!esAdmin significa cualquier id_roles que no sea 1, el de administrador, por si se crean m�s en el futuro
		if (!esAdmin) {

			session.setAttribute("errorLogin", "No tienes permiso para acceder a esa secci�n");
			req.getRequestDispatcher("/login").forward(request, response);
		//else quiere decir que s� es Administrador por lo que se le deja v�a libre
		} else {

			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
}
