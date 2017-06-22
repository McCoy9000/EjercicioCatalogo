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
import catalogo.dal.FacturaDAOFactory;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Factura;
import catalogo.tipos.Producto;
import catalogo.tipos.Usuario;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1598983717815749112L;
	private static Logger log = Logger.getLogger(CheckoutServlet.class);

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
		Producto[] productosCarritoArr = carrito.buscarTodosLosProductos();
		Integer numeroProductos = productosCarritoArr.length;
		Double precioTotal = carrito.precioTotal();

		session.setAttribute("productosCarritoArr", productosCarritoArr);
		session.setAttribute("numeroProductos", numeroProductos);
		session.setAttribute("precioTotal", precioTotal);

		if (numeroProductos == 0) {
			request.getRequestDispatcher("/catalogo").forward(request, response);
		} else {

			if (op == null) {

				request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);

			} else {

				switch (op) {
				case "pagar":

					Usuario usuario = (Usuario) session.getAttribute("usuario");

					if (usuario == null) {

						request.getRequestDispatcher("/login").forward(request, response);
					} else {

						FacturaDAO facturas = FacturaDAOFactory.getFacturaDAO();
						Factura factura = new Factura(usuario.getId(), new Date());
						// TODO transaccion
						productosReservados.abrir();
						productosVendidos.reutilizarConexion(productosReservados);
						facturas.reutilizarConexion(productosReservados);
						productosReservados.iniciarTransaccion();

						Producto[] productosFactura = null;
						Double precioFactura = 0.0;
						try {
							int id_factura = facturas.insert(factura);

							for (Producto p : carrito.buscarTodosLosProductos()) {
								productosReservados.delete(p);
								productosVendidos.insert(p);
								facturas.insertFacturaProducto(id_factura, p.getId());
							}
							factura = facturas.findById(id_factura);

							productosFactura = facturas.findProductoByFacturaId(id_factura);

							precioFactura = facturas.getPrecioTotal(id_factura);

							productosReservados.confirmarTransaccion();

							log.info("Carrito de la compra liquidado");
						} catch (Exception e) {
							productosReservados.deshacerTransaccion();

							e.printStackTrace();
						}

						productosReservados.cerrar();

						carrito = CarritoDAOFactory.getCarritoDAO();

						session.setAttribute("factura", factura);
						session.setAttribute("productosFactura", productosFactura);
						session.setAttribute("precioFactura", precioFactura);

						session.setAttribute("carrito", carrito);
						session.setAttribute("productosCarritoArr", carrito.buscarTodosLosProductos());
						session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
						session.setAttribute("precioTotal", carrito.precioTotal());

						request.getRequestDispatcher("/catalogo").forward(request, response);
					}
					break;

				case "quitar":
					int id = Integer.parseInt(request.getParameter("id"));
					producto = carrito.buscarPorId(id);

					if (producto != null) {
						// TODO transaccion
						productos.abrir();
						productosReservados.abrir();
						productos.iniciarTransaccion();
						productosReservados.iniciarTransaccion();
						try {
							carrito.quitarDelCarrito(id);
							productos.insert(producto);
							productosReservados.delete(producto);
							productos.confirmarTransaccion();
							productosReservados.confirmarTransaccion();
							log.info("Producto retirado del carro");
						} catch (Exception e) {
							productos.deshacerTransaccion();
							productosReservados.deshacerTransaccion();
							throw new DAOException("Error al eliminar de productosReservados", e);
						}
						productos.cerrar();
						productosReservados.cerrar();

//						application.setAttribute("productos", productos);
//						application.setAttribute("productosReservados", productosReservados);
						session.setAttribute("carrito", carrito);
						session.setAttribute("productosCarritoArr", carrito.buscarTodosLosProductos());
						session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
						session.setAttribute("precioTotal", carrito.precioTotal());

					}

					if (carrito.buscarTodosLosProductos().length == 0) {
						request.getRequestDispatcher("/catalogo").forward(request, response);
						break;
					}
				default:
					request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
				}
			}
		}
	}
}
