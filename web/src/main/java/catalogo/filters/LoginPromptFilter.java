package catalogo.filters;

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

@WebFilter("/factura")
public class LoginPromptFilter implements Filter {

	public LoginPromptFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		if (session.getAttribute("usuario") == null) {
			request.getRequestDispatcher("/WEB-INF/vistas/loginprompt.jsp").forward(request, response);
		} else {
			chain.doFilter(request, response);
		}

	}

	@Override
	public void destroy() {
	}

}
