package catalogo.servlets;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import catalogo.constantes.Constantes;
import catalogo.dal.FacturaDAO;
import catalogo.tipos.Factura;
import catalogo.tipos.FacturaMascara;
import catalogo.tipos.Producto;
import catalogo.tipos.Usuario;

@WebServlet("/admin/facturacrud")
public class FacturaCRUDServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();

		FacturaDAO facturas = (FacturaDAO) application.getAttribute("facturas");

		HttpSession session = request.getSession();

		session.removeAttribute("errorFactura");
		
		String op = request.getParameter("op");

		if (op == null) {

			facturas.abrir();

			FacturaMascara[] facturasArr = facturas.findAllMasks();

			application.setAttribute("facturasArr", facturasArr);

			request.getRequestDispatcher(Constantes.RUTA_LISTADO_FACTURA).forward(request, response);

		} else {
			
			switch (op) {
			
			case "ver":
				
				Factura factura;
				Producto[] productosFactura;
				BigDecimal ivaFactura;
				BigDecimal precioFactura;
				Usuario usuarioFactura;
				
				int id = 0;
				
				try {
					id = Integer.parseInt(request.getParameter("id"));
				} catch (Exception e) {
					session.setAttribute("errorFactura", "Error al recuperar la factura. Inténtelo de nuevo");
					request.getRequestDispatcher(Constantes.RUTA_ERROR_FACTURA).forward(request, response);
				}
				
				facturas.abrir();
				
				factura = facturas.findById(id);
				
				productosFactura = facturas.findProductoByFacturaId(id);
				
				ivaFactura = facturas.getIvaTotal(id);
				
				precioFactura = facturas.getPrecioTotal(id);
				
				usuarioFactura = facturas.findUserByFacturaId(id);
				
				facturas.cerrar();
				
				session.setAttribute("factura", factura);
				session.setAttribute("productosFactura", productosFactura);
				session.setAttribute("ivaFactura", ivaFactura);
				session.setAttribute("precioFactura", precioFactura);
				session.setAttribute("usuarioFactura", usuarioFactura);
				request.getRequestDispatcher(Constantes.RUTA_FACTURA_FACTURA).forward(request, response);
				break;
			case "devolucion":
				request.getRequestDispatcher("/WEB-INF/vistas/devolucionform.jsp").forward(request, response);
				break;
				
			}

		}
	}

}
