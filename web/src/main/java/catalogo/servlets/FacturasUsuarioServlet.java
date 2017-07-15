package catalogo.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import catalogo.dal.FacturaDAO;
import catalogo.tipos.FacturaMascara;
import catalogo.tipos.Usuario;

@WebServlet("/facturasusuario")
public class FacturasUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext application = request.getServletContext();
		HttpSession session = request.getSession();
		
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		FacturaDAO facturas = (FacturaDAO) application.getAttribute("facturas");
		
		FacturaMascara[] facturasUsuarioArr = null;
		
		if (usuario != null) {
			facturas.abrir();
			facturasUsuarioArr = facturas.findMasksByUserId(usuario.getId());
			facturas.cerrar();
		}
		session.setAttribute("facturasUsuarioArr", facturasUsuarioArr);
		
		request.getRequestDispatcher("/WEB-INF/vistas/facturasusuario.jsp").forward(request, response);
		
	}

}
