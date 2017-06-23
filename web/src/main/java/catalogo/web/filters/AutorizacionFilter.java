package catalogo.web.filters;

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

@WebFilter("/admin/*")
public class AutorizacionFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// Tal y como está declarado el filtro, doFilter se ejecutará cuando se acceda a una zona dentro
		// de /admin/. En principio los enlaces a esta sección no es visible para los no administradores,
		// pero se podría modificar el css que lo oculta o introducir una de estas URLs directamente en
		// el navegador. Aquí es donde entra en acción el filtro.

		// RECOPILACIÓN DE LOS OBJETOS A ANALIZAR Y UTILIZAR
		
		// Casteo del objeto request en HttpServletRequest para poder obtener el objeto session.
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		// Creación de un objeto usuario para introducir en él, si lo hay, el usuario que viene en el 
		// objeto sesión y poder analizar sus permisos y darle acceso a unas secciones u otras
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		// DECLARACIÓN DE BOOLEANAS PARA LA LÓGICA DEL FILTRO
		
		// En principio se considera al usuario no administrador
		boolean esAdmin = false;
		// Si no es nuevo usuario el usuario no es null por lo que se le puede pedir el id_roles sin miedo al NullPointerException
		if (usuario != null) {
			esAdmin = usuario.getId_roles() == 1;
		}

		// LÓGICA DEL FILTRO

		// Si no es administrador se le enviará al login. Le meto el mensaje de error, pero al llegar al login, como no tiene datos
		// de logueo en un primer momento, se cambia este mensaje por el de 'Debes rellenar todos los campos' :-(
		// !esAdmin significa cualquier id_roles que no sea 1, el de administrador, por si se crean más en el futuro
		if (!esAdmin) {
			session.setAttribute("errorLogin", "No tienes permiso para acceder a esa sección");
			req.getRequestDispatcher("/login").forward(request, response);
			// else quiere decir que sí es Administrador por lo que se le deja vía libre
		} else {

			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
}
