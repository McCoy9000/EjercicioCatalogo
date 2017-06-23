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

		// Tal y como est� declarado el filtro, doFilter se ejecutar� cuando se acceda a una zona dentro
		// de /admin/. En principio los enlaces a esta secci�n no es visible para los no administradores,
		// pero se podr�a modificar el css que lo oculta o introducir una de estas URLs directamente en
		// el navegador. Aqu� es donde entra en acci�n el filtro.

		// RECOPILACI�N DE LOS OBJETOS A ANALIZAR Y UTILIZAR
		
		// Casteo del objeto request en HttpServletRequest para poder obtener el objeto session.
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		// Creaci�n de un objeto usuario para introducir en �l, si lo hay, el usuario que viene en el 
		// objeto sesi�n y poder analizar sus permisos y darle acceso a unas secciones u otras
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		// DECLARACI�N DE BOOLEANAS PARA LA L�GICA DEL FILTRO
		
		// En principio se considera al usuario no administrador
		boolean esAdmin = false;
		// Si no es nuevo usuario el usuario no es null por lo que se le puede pedir el id_roles sin miedo al NullPointerException
		if (usuario != null) {
			esAdmin = usuario.getId_roles() == 1;
		}

		// L�GICA DEL FILTRO

		// Si no es administrador se le enviar� al login. Le meto el mensaje de error, pero al llegar al login, como no tiene datos
		// de logueo en un primer momento, se cambia este mensaje por el de 'Debes rellenar todos los campos' :-(
		// !esAdmin significa cualquier id_roles que no sea 1, el de administrador, por si se crean m�s en el futuro
		if (!esAdmin) {
			session.setAttribute("errorLogin", "No tienes permiso para acceder a esa secci�n");
			req.getRequestDispatcher("/login").forward(request, response);
			// else quiere decir que s� es Administrador por lo que se le deja v�a libre
		} else {

			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
}
