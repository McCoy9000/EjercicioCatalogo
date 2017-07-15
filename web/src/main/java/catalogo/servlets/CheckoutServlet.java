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

import catalogo.dal.CarritoDAO;
import catalogo.dal.CarritoDAOFactory;
import catalogo.dal.FacturaDAO;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Articulo;
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

		// Se recogen los objetos ServletContext y HttpSession
		ServletContext application = request.getServletContext();
		HttpSession session = request.getSession();
		// Borrado de errores en sesión por si llegan aquí desde los formularios CRUD
		session.removeAttribute("errorProducto");
		session.removeAttribute("errorUsuario");
		session.removeAttribute("errorLogin");
		session.removeAttribute("errorSignup");
		//Se recoge el parametro op enviado por el cliente
		String op = request.getParameter("op");
		// Se recogen los DAOs a utilizar tanto del ServletContext como de la sesión
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");
		ProductoDAO productosVendidos = (ProductoDAO) application.getAttribute("productosVendidos");
		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");
		//Se declara un producto genérico para trabajar con él
		Producto producto;
		//Se obtienen datos para mostrar en la jsp de checkout
		Articulo [] articulosCarritoArr = carrito.getCatalogoArticulos();
		Producto[] productosCarritoArr = carrito.buscarTodosLosProductos();
		Integer numeroProductos = productosCarritoArr.length;
		BigDecimal precioTotal = carrito.precioTotal();
		//Se introducen estos datos en el objeto session para mostrarlos en la jsp
		session.setAttribute("articulosCarritoArr", articulosCarritoArr);
		session.setAttribute("productosCarritoArr", productosCarritoArr);
		session.setAttribute("numeroProductos", numeroProductos);
		session.setAttribute("precioTotal", precioTotal);
		
		//LÓGICA DEL SERVLET
		//Si no hay productos en el carrito se devuelve al usuario al catálogo
		if (numeroProductos == 0) {
			request.getRequestDispatcher("/catalogo").forward(request, response);
		} else {
		//Si viene sin opción, se le muestra la jsp sin llevar a cabo ninguna acción
			if (op == null) {

				request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
			} else {
		//Diferentes opciones según la variable op
				switch (op) {
					case "vaciarcarrito":
		//Si la opción es vaciar el carrito, comprobamos si el carrito tiene productos
						if (carrito.buscarTodosLosProductos().length != 0) {
							// Vaciar los productos del carrito y de la tabla de productos_reservados y reinsertarlos
							// en la tabla general de productos
							productos.abrir();
							productosReservados.reutilizarConexion(productos);
							productos.iniciarTransaccion();
				
							try {
								for (Producto p : carrito.buscarTodosLosProductos()) {
									carrito.quitarDelCarrito(p.getId());
									productosReservados.delete(p);
									productos.insert(p);
								}
								productos.confirmarTransaccion();
								log.info("Vaciado carrito");
							} catch (Exception e) {
								productos.deshacerTransaccion();
								log.info(e.getMessage());
								log.info("Error al vaciar el carrito");
							} finally {
								productos.cerrar();
							}
		//Se almacena el nuevo carrito en sesión. Si la operación ha fallado este carrito será el
		//antiguo
						session.setAttribute("carrito", carrito);
						session.setAttribute("productosCarritoArr", carrito.buscarTodosLosProductos());
						session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
						session.setAttribute("precioTotal", carrito.precioTotal());
		//Se le envía a la página principal
						}
						request.getRequestDispatcher("/catalogo").forward(request, response);
						break;
				
					case "pagar":
		//Si la opción es pagar se obtiene el usuario almacenado en sesión
						Usuario usuario = (Usuario) session.getAttribute("usuario");
		//Si no hay un usuario identificado se le insta a loguearse
						if (usuario == null) {
							request.getRequestDispatcher("/login").forward(request, response);
						} else {
		//Si hay un usuario identificado se obtiene el DAO de facturas almacenado en el ServletContext
							FacturaDAO facturas = (FacturaDAO) application.getAttribute("facturas");
		//Se inicializa una factura con el id del usuario y la fecha actual
							Factura factura = new Factura(usuario.getId(), new Date());
		
		//Se declara el array de productos en la factura, el precio total y el usuario para mostrar en la jsp
		//de factura
							Producto[] productosFactura = null;
							BigDecimal ivaFactura = BigDecimal.ZERO;
							BigDecimal precioFactura = BigDecimal.ZERO;
							Usuario usuarioFactura = null;
		// Abrir conexión para todas las tablas e iniciar transacción
							productosReservados.abrir();
							productosVendidos.reutilizarConexion(productosReservados);
							facturas.reutilizarConexion(productosReservados);
							productosReservados.iniciarTransaccion();
							try {
		//Para obtener el id de factura se utiliza la key obtenida de la operación de insertar la 
		//factura en su correspondiente tabla
								int id_factura = facturas.insert(factura);
		//Se efectua la operación de eliminar los productos del carrito de la tabla productosReservados,
		//añadirlos a productosVendidos y a la tabla productos_facturas junto con el id de su factura
								for (Producto p : carrito.buscarTodosLosProductos()) {
									productosReservados.delete(p);
									productosVendidos.insert(p);
									facturas.insertFacturaProducto(id_factura, p.getId());
								}
		//Una vez insertada, se obtiene la factura completa para almacenarla en sesión.
								factura = facturas.findById(id_factura);
		//Se rellena el array de productos de la factura
								productosFactura = facturas.findProductoByFacturaId(id_factura);
		//Se calcula el IVA total de la factura
								ivaFactura = facturas.getIvaTotal(id_factura);								
		//Se calcula el precio total de la factura
								precioFactura = facturas.getPrecioTotal(id_factura);
		//El usuario de la factura es quien está haciendo la compra
								usuarioFactura = usuario;
		//Si todas las operaciones han salido, se confirma la transacción y se obtiene un nuevo carrito
								productosReservados.confirmarTransaccion();
								log.info("Carrito de la compra liquidado");
								carrito = CarritoDAOFactory.getCarritoDAO();
							} catch (Exception e) {
								productosReservados.deshacerTransaccion();
								log.info("Error al liquidar el carrito de la compra");
								e.printStackTrace();
							} finally {
		//Se cierra la conexión con la BDD
								productosReservados.cerrar();
								facturas.cerrar();
							}
		//La factura obtenida se almacena en sesión para mostrarla en la jsp. Si la operación falla,
		//aparecerá una factura en blanco con número 0
							session.setAttribute("factura", factura);
							session.setAttribute("productosFactura", productosFactura);
							session.setAttribute("ivaFactura", ivaFactura);
							session.setAttribute("precioFactura", precioFactura);
							session.setAttribute("usuarioFactura", usuarioFactura);
		//Se almacena el nuevo carrito en sesión. Si la operación ha fallado este carrito será el
		//antiguo
							session.setAttribute("carrito", carrito);
							session.setAttribute("productosCarritoArr", carrito.buscarTodosLosProductos());
							session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
							session.setAttribute("precioTotal", carrito.precioTotal());
		//Se le envía a la página principal
							request.getRequestDispatcher("/catalogo").forward(request, response);
						}
						break;
	
					case "quitar":
		//Si la opción es quitar se obtiene el id del producto a quitar que viene en la request a partir
		//del link que pulsa el usuario y corresponde a cada producto de la tabla
						int id;
						
						try {
							id = Integer.parseInt(request.getParameter("id"));
						} catch (Exception e) {
		//Si la operación falla se le devuelve al checkout que no habrá cambiado
							request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
							break;
						}
		//Se busca este producto en el carrito. También podría buscarse en la tabla productosReservados
						producto = carrito.buscarPorId(id);
		//Se comprueba que el producto no es null por si alguien mete otro id en la barra de direcciones
						if (producto != null) {
		//Se abre la conexión con la BDD para las dos tablas a usar y se inicia la transacción
							productos.abrir();
							productosReservados.reutilizarConexion(productos);
							productos.iniciarTransaccion();
							try {
		//Se elimina el producto del carrito y de la tabla productosReservados y se inserta en productos
								carrito.quitarDelCarrito(id);
								productosReservados.delete(producto);
								productos.insert(producto);
								productos.confirmarTransaccion();
								log.info("Producto retirado del carrito");
							} catch (Exception e) {
								productos.deshacerTransaccion();
								log.info(e.getMessage());
								e.printStackTrace();
							} finally {
								productos.cerrar();
							}
		//El carrito y sus datos se vuelven a introducir en sesión		
							session.setAttribute("carrito", carrito);
							session.setAttribute("articulosCarritoArr", carrito.getCatalogoArticulos());
							session.setAttribute("productosCarritoArr", carrito.buscarTodosLosProductos());
							session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
							session.setAttribute("precioTotal", carrito.precioTotal());
	
						}
		//Se vuelve a comprobar si el carrito está vacío para enviar al usuario al catálogo
						if (carrito.buscarTodosLosProductos().length == 0) {
							request.getRequestDispatcher("/catalogo").forward(request, response);
							break;
						}
		//Si entrase con cualquier otra opción, se le muestra la página de checkout
					default:
						request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
				}
			}
		}
	}
}
