package catalogo.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import catalogo.dal.CarritoDAO;
import catalogo.dal.CarritoDAOFactory;
import catalogo.dal.DAOException;
import catalogo.dal.FacturaDAO;
import catalogo.dal.FacturaDAOMySQL;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Carrito;
import catalogo.tipos.Factura;
import catalogo.tipos.Producto;
import catalogo.tipos.Usuario;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1598983717815749112L;
	private static Logger log = Logger.getLogger(Carrito.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = request.getServletContext();
		HttpSession session = request.getSession();
		String op = request.getParameter("op");
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");
		ProductoDAO productosVendidos = (ProductoDAO) application.getAttribute("productosVendidos");
		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");

		Producto producto;
		Producto[] listaProductosArr = null;
		Integer numeroProductos = 0;
		Double precioTotal = 0.0;

		try {
			listaProductosArr = carrito.buscarTodosLosProductos();
			numeroProductos = listaProductosArr.length;
			precioTotal = carrito.precioTotal();

		} catch (NullPointerException npe) {
			request.getRequestDispatcher("/login").forward(request, response);
		}

		session.setAttribute("productosArr", listaProductosArr);
		session.setAttribute("numeroProductos", numeroProductos);
		session.setAttribute("precioTotal", precioTotal);

		if (op == null) {

			try {
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
			} catch (NullPointerException npe) {
				carrito = CarritoDAOFactory.getCarritoDAO();
				session.setAttribute("carrito", carrito);
				session.setAttribute("productosArr", carrito.buscarTodosLosProductos());
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
			}

			request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);

		} else {

			switch (op) {
			case "pagar":
				Usuario usuario = (Usuario) session.getAttribute("usuario");
				if (usuario == null) {
					request.getRequestDispatcher("/login").forward(request, response);
				} else {
					FacturaDAO facturas = new FacturaDAOMySQL();
					Factura factura = new Factura(usuario.getId(), new Date());
					facturas.abrir();
					facturas.insert(factura);
					facturas.cerrar();
					productosVendidos.abrir();
					productosReservados.abrir();
					for (Producto p : carrito.buscarTodosLosProductos()) {
						productosReservados.delete(p);
						productosVendidos.insert(p);
					}
					productosReservados.cerrar();
					productosVendidos.cerrar();

					carrito = CarritoDAOFactory.getCarritoDAO();
					log.info("Carrito de la compra liquidado");
					application.setAttribute("productosReservados", productosReservados);
					application.setAttribute("productosVendidos", productosVendidos);

					session.setAttribute("carrito", carrito);
					session.setAttribute("productosArr", carrito.buscarTodosLosProductos());
					session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);

					request.getRequestDispatcher("/catalogo").forward(request, response);
				}
				break;

			case "quitar":
				int id = Integer.parseInt(request.getParameter("id"));
				producto = carrito.buscarPorId(id);

				if (producto != null) {

					productos.abrir();
					productos.iniciarTransaccion();
					try {
						carrito.quitarDelCarrito(id);
						productos.insert(producto);
						productos.confirmarTransaccion();
					} catch (Exception e) {
						productos.deshacerTransaccion();
					}
					productos.cerrar();

					try {
						productosReservados.abrir();
						productosReservados.delete(producto);
						productosReservados.cerrar();
					} catch (Exception e) {
						throw new DAOException("Error al eliminar de productosReservados", e);
					}
					log.info("Producto retirado del carro");
				}

				application.setAttribute("productos", productos);
				application.setAttribute("productosReservados", productosReservados);
				session.setAttribute("carrito", carrito);
				session.setAttribute("productosArr", carrito.buscarTodosLosProductos());
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
				session.setAttribute("precioTotal", carrito.precioTotal());

			default:
				request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
			}
		}
	}
}
