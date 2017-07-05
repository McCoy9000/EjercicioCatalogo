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
import catalogo.tipos.Factura;

@WebServlet("admin/facturaCRUD")
public class FacturaCRUDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final String RUTA_FORMULARIO = "/WEB-INF/vistas/facturaform.jsp";
	static final String RUTA_LISTADO = "/WEB-INF/vistas/facturacrud.jsp";
	static final String RUTA_SERVLET_LISTADO = "/admin/facturacrud";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();

		FacturaDAO facturas = (FacturaDAO) application.getAttribute("facturas");

		HttpSession session = request.getSession();

		String op = request.getParameter("op");

		if (op == null) {

			facturas.abrir();

			Factura[] facturasArr = facturas.findAll();

			application.setAttribute("facturas", facturasArr);

			request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);

		} else {

		}
	}

}
