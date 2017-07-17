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

		String op = request.getParameter("op");

		if (op == null) {

			FacturaMascara[] facturasUsuarioArr = null;

			if (usuario != null) {
				facturas.abrir();
				facturasUsuarioArr = facturas.findMasksByUserId(usuario.getId());
				facturas.cerrar();
			}
			session.setAttribute("facturasUsuarioArr", facturasUsuarioArr);
			request.getRequestDispatcher("/WEB-INF/vistas/facturasusuario.jsp").forward(request, response);
			return;
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
					return;
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
			}

		}

	}

}
