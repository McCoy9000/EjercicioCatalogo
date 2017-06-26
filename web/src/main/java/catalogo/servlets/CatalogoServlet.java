package catalogo.servlets;

import java.io.IOException;
import java.util.List;

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
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Producto;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

	private static final long serialVersionUID = -2040781710356406128L;
	private static Logger log = Logger.getLogger(CatalogoServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// RECOGIDA DE DATOS Y CONSTRUCCIÓN DE OBJETOS

		ServletContext application = getServletContext();
		HttpSession session = request.getSession();

		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");

		// Generar el catálogo. El catálogo es un array en el que cada elemento es, a su vez, el primer elemento de la lista de productos
		// de un determinado grupo de productos.

		productos.abrir();
		application.setAttribute("catalogo", productos.getCatalogo());
		productos.cerrar();

		// Recoger el carrito asociado a la sesión o, en caso de que no exista (porque el usuario haya entrado directamente al catálogo desde
		// URL), crearlo.

		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");

		if (carrito == null) {

			carrito = CarritoDAOFactory.getCarritoDAO();
		}

		// Recoger la opción con la que llega el usuario
		String op = request.getParameter("op");

		// LÓGICA DEL SERVLET

		if (op == null) {

			// Si se llega al catálogo sin opciones el carrito que o se ha recogido o se ha creado se empaqueta
			// en el objeto sesión
			session.setAttribute("carrito", carrito);
			session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);

			request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);

		} else {

			switch (op) {

			case "anadir":
				// Se recoge el groupId que nos envían clickando en el botón junto al producto
				int groupId;
				try {
					 groupId = Integer.parseInt(request.getParameter("groupId"));
				} catch (Exception e) {
					request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);
					break;
				}
				
				// Se recoge la cantidad solicitada y, en caso de error, se utiliza 1
				
				int cantidad = 1;
				try {
					cantidad = Integer.parseInt(request.getParameter("cantidad"));
				} catch (Exception e) {
					e.printStackTrace();
					log.info("Error al parsear la cantidad. Se utilizará 1 por defecto como precaución");
				}
				
				log.info("Cantidad recogida: " + cantidad);
				
				// Se busca la lista de productos correspondiente al groupId
	
				productos.abrir();
				
				List<Producto> grupoProductos = productos.getAlmacen().get(groupId);

				// Se declara un producto genérico para rellenarlo con productos de la BBDD e ir
				// trabajando con él
				
				Producto productoAleatorio;

				// Se reutiliza la conexión abierta para el DAO de productosReservados y se
				// inicia la transacción
				
				productosReservados.reutilizarConexion(productos);
				
				if (grupoProductos != null) {

				productos.iniciarTransaccion();

				// Tantas veces como se ha indicado en cantidad o hasta que no queden productos del 
				// tipo solicitado se retira un producto de la tabla de productos de ese tipo y se
				// añade tanto al carrito como a la tabla de productos reservados

						
					try {
	
						int i = 0;
						for (i = 0; i < cantidad && i < grupoProductos.size(); i++) {
							productoAleatorio = grupoProductos.get(i);
							productos.delete(productoAleatorio);
							carrito.anadirAlCarrito(productoAleatorio);
							productosReservados.insert(productoAleatorio);
						}
	
						productos.confirmarTransaccion();
						log.info("Añadidos " + i + " productos al carrito");
	
					} catch (Exception e) {
							productos.deshacerTransaccion();
							for (Producto p : carrito.buscarTodosLosProductos()) {
								carrito.quitarDelCarrito(p.getId());
							}
							log.info("Error al añadir productos al carrito");
							e.printStackTrace();
					}
				
				}
				// Se actualiza el catálogo en aplicación para que no aparezcan los productos que 
				// están en el carrito y se cierra la conexión
				
				application.setAttribute("catalogo", productos.getCatalogo());
	
				productos.cerrar();
				
				//Se actualiza el carrito a través del DAO y el valor número de productos en la sesión
				session.setAttribute("carrito", carrito);
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);

				request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);

				break;

			default:

				request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);

			}
		}
	}

}
