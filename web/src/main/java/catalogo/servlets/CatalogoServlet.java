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

		// RECOGIDA DE DATOS Y CONSTRUCCI�N DE OBJETOS

		ServletContext application = getServletContext();
		HttpSession session = request.getSession();

		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");

		// Generar el cat�logo. El cat�logo es un array en el que cada elemento es, a su vez, el primer elemento de la lista de productos
		// de un determinado grupo de productos.

		productos.abrir();
		application.setAttribute("catalogo", productos.getCatalogo());
		productos.cerrar();

		// Recoger el carrito asociado a la sesi�n o, en caso de que no exista (porque el usuario haya entrado directamente al cat�logo desde
		// URL), crearlo.

		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");

		if (carrito == null) {

			carrito = CarritoDAOFactory.getCarritoDAO();
		}

		// Recoger la opci�n con la que llega el usuario
		String op = request.getParameter("op");

		// LOGICA DEL SERVLET

		if (op == null) {

			// Si se llega al cat�logo sin opciones el carrito que o se ha recogido o se ha creado se empaqueta
			// en el objeto sesi�n
			session.setAttribute("carrito", carrito);
			session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);

			request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);

		} else {

			switch (op) {

			case "anadir":
				// Se recoge el groupId que nos env�an clickando en el bot�n junto al producto
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
					log.info("Error al parsear la cantidad. Se utilizar� 1 por defecto como precauci�n");
				}
				
				log.info("Cantidad recogida: " + cantidad);
				
				// Se busca la lista de productos correspondiente al groupId
	
				productos.abrir();
				
				List<Producto> grupoProductos = productos.getAlmacen().get(groupId);

				// Se declara un producto gen�rico para rellenarlo con productos de la BBDD e ir
				// trabajando con �l
				
				Producto productoAleatorio;

				// Se reutiliza la conexi�n abierta para el DAO de productosReservados y se
				// inicia la transacci�n
				
				productosReservados.reutilizarConexion(productos);
				
				productos.iniciarTransaccion();

				// Tantas veces como se ha indicado en cantidad o hasta que no queden productos del 
				// tipo solicitado se retira un producto de la tabla de productos de ese tipo y se
				// a�ade tanto al carrito como a la tabla de productosReservados
				
				try {

					int i = 0;
					for (i = 0; i < cantidad && i < grupoProductos.size(); i++) {
						productoAleatorio = grupoProductos.get(i);
						productos.delete(productoAleatorio);
						carrito.anadirAlCarrito(productoAleatorio);
						productosReservados.insert(productoAleatorio);
					}

					productos.confirmarTransaccion();
					log.info("A�adidos " + i + " productos al carrito");

				} catch (Exception e) {
						productos.deshacerTransaccion();
						for (Producto p : carrito.buscarTodosLosProductos()) {
							carrito.quitarDelCarrito(p.getId());
						}
						log.info(e.getMessage());
						e.printStackTrace();
				}
				
				// Se actualiza el cat�logo en aplicaci�n para que no aparezcan los productos que 
				// est�n en el carrito y se cierra la conexi�n
				
				application.setAttribute("catalogo", productos.getCatalogo());
	
				productos.cerrar();
				
				//Se actualiza el carrito a trav�s del DAO y el valor n�mero de productos en la sesi�n
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
