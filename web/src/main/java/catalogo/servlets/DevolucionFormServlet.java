package catalogo.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import catalogo.dal.FacturaDAO;
import catalogo.dal.ProductoDAO;
import catalogo.dal.UsuarioDAO;
import catalogo.tipos.Factura;
import catalogo.tipos.Producto;
import catalogo.tipos.Usuario;

@WebServlet("/admin/devolucionform")
public class DevolucionFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CheckoutServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = request.getServletContext();
		HttpSession session = request.getSession();

		session.removeAttribute("errorDevolucion");

		UsuarioDAO usuarios = (UsuarioDAO) application.getAttribute("usuarios");

		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		ProductoDAO productosVendidos = (ProductoDAO) application.getAttribute("productosVendidos");

		FacturaDAO facturas = (FacturaDAO) application.getAttribute("facturas");

		String op = request.getParameter("opform");

		String mensaje = request.getParameter("mensaje");

		BigDecimal importe = null;

		if ((request.getParameter("importe") != null) && (request.getParameter("importe") != "")) {
			try {
				importe = new BigDecimal(request.getParameter("importe"));
			} catch (NumberFormatException nfe) {
				session.setAttribute("errorDevolucion", "Debes introducir un importe negativo para hacer una devolución");
				request.getRequestDispatcher("/WEB-INF/vistas/devolucionform.jsp?op=devolucion").forward(request, response);
				return;
			}

			if (BigDecimal.ZERO.compareTo(importe) < 0) {
				session.setAttribute("errorDevolucion", "Debe introducir un importe negativo para hacer una devolución");
				request.getRequestDispatcher("/WEB-INF/vistas/devolucionform.jsp?op=devolucion").forward(request, response);
				return;
			}

		} else {
			session.setAttribute("errorDevolucion", "Debe introducir un importe negativo para hacer una devolución");
			request.getRequestDispatcher("/WEB-INF/vistas/devolucionform.jsp?op=devolucion").forward(request, response);
			return;
		}

		usuarios.abrir();
		Usuario usuario = usuarios.findById(1);
		usuarios.cerrar();

		Factura factura = new Factura(usuario.getId(), new Date());

		Producto producto = new Producto(0, mensaje, "", importe);

		switch (op) {

		case "devolucion":

			productos.abrir();
			productosVendidos.reutilizarConexion(productos);
			facturas.reutilizarConexion(productos);
			int i = 0;
			int j = 0;

			productos.iniciarTransaccion();
			try {
				i = facturas.insert(factura);
				j = productos.insert(producto);
				producto.setPrecio(importe);
				productosVendidos.insert(productos.findById(j));
				productos.delete(j);
				facturas.insertFacturaProducto(i, j);
				productos.confirmarTransaccion();
				log.info("Devolución realizada");
			} catch (Exception e) {
				session.setAttribute("errorDevolucion", "Error al hacer la devolución. Inténtelo de nuevo");
				productos.deshacerTransaccion();
				log.info("Error al hacer la devolución");
				e.printStackTrace();
				request.getRequestDispatcher("/WEB-INF/vistas/devolucionform.jsp?op=devolucion").forward(request, response);
				return;
			} finally {
				productos.cerrar();
			}

			request.getRequestDispatcher("/admin/facturacrud").forward(request, response);

		}
	}

}
