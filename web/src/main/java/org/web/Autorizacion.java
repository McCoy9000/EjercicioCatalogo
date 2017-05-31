package org.web;

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

import org.tipos.Usuario;

@WebFilter("/autorizacion")
public class Autorizacion implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		HttpSession session = req.getSession();

		Usuario usuario = null;

		if (session != null) {

			usuario = (Usuario) session.getAttribute("usuario");

		}

		boolean nuevoUsuario = usuario == null;
		boolean esAdmin = false;

		if (!nuevoUsuario) {

			esAdmin = usuario.isAdmin();

		}

		if (!esAdmin) {

			session.setAttribute("errorLogin", "No tienes permiso para acceder a esa sección");
			(req.getRequestDispatcher("/login")).forward(request, response);

		} else {

			chain.doFilter(request, response);

		}
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
