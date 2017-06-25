package catalogo.servlets;

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
import catalogo.dal.FacturaDAO;
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

		// Se recogen los objetos ServletContext y HttpSession
		ServletContext application = request.getServletContext();
		HttpSession session = request.getSession();
		//Se recoge el parametro op enviado por el cliente
		String op = request.getParameter("op");
		// Se recogen los DAOs a utilizar tanto del ServletContext como de la sesi�n
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");
		ProductoDAO productosVendidos = (ProductoDAO) application.getAttribute("productosVendidos");
		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");
		//Se declara un producto gen�rico para trabajar con el
		Producto producto;
		//Se obtienen datos para mostrar en la jsp de checkout
		Producto[] productosCarritoArr = carrito.buscarTodosLosProductos();
		Integer numeroProductos = productosCarritoArr.length;
		Double precioTotal = carrito.precioTotal();
		//Se introducen estos datos en el objeto session para mostrarlos en la jsp
		session.setAttribute("productosCarritoArr", productosCarritoArr);
		session.setAttribute("numeroProductos", numeroProductos);
		session.setAttribute("precioTotal", precioTotal);
		
		//LOGICA DEL SERVLET
		//Si no hay productos en el carrito se devuelve al usuario al cat�logo
		if (numeroProductos == 0) {
			request.getRequestDispatcher("/catalogo").forward(request, response);
		} else {
		//Si viene sin opci�n, se le muestra la jsp sin llevar a cabo ninguna acci�n
			if (op == null) {

				request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
			} else {
		//Diferentes opciones seg�n la variable op
				switch (op) {
					case "pagar":
		//Si la opci�n es pagar se obtiene el usuario almacenado en sesi�n
						Usuario usuario = (Usuario) session.getAttribute("usuario");
		//Si no hay un usuario identificado se le insta a loguearse
						if (usuario == null) {
							request.getRequestDispatcher("/login").forward(request, response);
						} else {
		//Si hay un usuario identificado se obtiene el DAO de facturas almacenado en el ServletContext
							FacturaDAO facturas = (FacturaDAO) application.getAttribute("facturas");
		//Se inicializa una factura con el id del usuario y la fecha actual
							Factura factura = new Factura(usuario.getId(), new Date());
		// Abrir conexion para todas las tablas e iniciar transacci�n
							productosReservados.abrir();
							productosVendidos.reutilizarConexion(productosReservados);
							facturas.reutilizarConexion(productosReservados);
							productosReservados.iniciarTransaccion();
		//Se declara el array de productos en la factura y el precio total para mostrar en la jsp
		//de factura
							Producto[] productosFactura = null;
							Double precioFactura = 0.0;

							try {
		//Para obtener el id de factura se utiliza la key obtenida de la operaci�n de insertar la 
		//factura en su correspondiente tabla
								int id_factura = facturas.insert(factura);
		//Se efectua la operaci�n de eliminar los productos del carrito de la tabla productosReservados,
		//a�adirlos a productos vendidos y a la tabla productos_facturas junto con el id de su factura
								for (Producto p : carrito.buscarTodosLosProductos()) {
									productosReservados.delete(p);
									productosVendidos.insert(p);
									facturas.insertFacturaProducto(id_factura, p.getId());
								}
		//Una vez insertada, se obtiene la factura completa para almacenarla en sesi�n.
								factura = facturas.findById(id_factura);
		//Se rellena el array de productos de la factura
								productosFactura = facturas.findProductoByFacturaId(id_factura);
		//Se calcula el precio total de la factura
								precioFactura = facturas.getPrecioTotal(id_factura);
		//Si todas las operaciones han salido, se confirma la transacci�n y se obtiene un nuevo carrito
								productosReservados.confirmarTransaccion();
								log.info("Carrito de la compra liquidado");
								carrito = CarritoDAOFactory.getCarritoDAO();
							} catch (Exception e) {
								productosReservados.deshacerTransaccion();
								log.info(e.getMessage());
								e.printStackTrace();
							}
		//Se cierra la conexi�n con la BDD
							productosReservados.cerrar();
	
		//La factura obtenida se almacena en sesi�n para mostrarla en la jsp. Si la operaci�n falla,
		//aparecer� una factura en blanco con n�mero 0
							session.setAttribute("factura", factura);
							session.setAttribute("productosFactura", productosFactura);
							session.setAttribute("precioFactura", precioFactura);
		//Se almacena el nuevo carrito en sesi�n. Si la operaci�n ha fallado este carrito ser� el
		//antiguo
							session.setAttribute("carrito", carrito);
							session.setAttribute("productosCarritoArr", carrito.buscarTodosLosProductos());
							session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
							session.setAttribute("precioTotal", carrito.precioTotal());
		//Se le env�a a la p�gina principal
							request.getRequestDispatcher("/catalogo").forward(request, response);
						}
						break;
	
					case "quitar":
		//Si la opci�n es quitar se obtiene el id del producto a quitar que viene en la request a partir
		//del link que pulsa el usuario y corresponde a cada producto de la tabla
						int id;
						
						try {
							id = Integer.parseInt(request.getParameter("id"));
						} catch (Exception e) {
		//Si la operaci�n falla se le devuelve al checkout que no habr� cambiado
							request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
							break;
						}
		//Se busca este producto en el carrito. Tambi�n podr�a buscarse en la tabla productosReservados
						producto = carrito.buscarPorId(id);
		//Se comprueba que el producto no es null por si alguien mete otro id en la barra de direcciones
						if (producto != null) {
		//Se abre la conexi�n con la BDD para las dos tablas a usar y se inicia la transacci�n
							productos.abrir();
							productosReservados.reutilizarConexion(productos);
							productos.iniciarTransaccion();
							try {
		//Se elimina el producto del carrito y de la tabla productosReservados y se inserta en productos
								carrito.quitarDelCarrito(id);
								productosReservados.delete(producto);
								productos.insert(producto);
								productos.confirmarTransaccion();
								log.info("Producto retirado del carro");
							} catch (Exception e) {
								productos.deshacerTransaccion();
								log.info(e.getMessage());
								e.printStackTrace();
							}
							productos.cerrar();
		//El carrito y sus datos se vuelven a introducir en sesion		
							session.setAttribute("carrito", carrito);
							session.setAttribute("productosCarritoArr", carrito.buscarTodosLosProductos());
							session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
							session.setAttribute("precioTotal", carrito.precioTotal());
	
						}
		//Se vuelve a comprobar si el carrito est� vac�o para enviar al usuario al cat�logo
						if (carrito.buscarTodosLosProductos().length == 0) {
							request.getRequestDispatcher("/catalogo").forward(request, response);
							break;
						}
		//Si entrase con cualquier otra opci�n, se le muestra la p�gina de checkout
					default:
						request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
				}
			}
		}
	}
}
