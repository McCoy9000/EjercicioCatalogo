package catalogo.web.servlets;

import java.io.IOException;

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

				Producto producto;

				int id = Integer.parseInt(request.getParameter("id"));

				// Se busca el producto correspondiente al id de producto que se nos env�a clickando en
				// el bot�n junto al producto
				productos.abrir();
				producto = productos.findById(id);
				productos.cerrar();

				if (producto != null) {
					// Se hace el proceso de a�adirlo al carrito
					productos.abrir();
					productosReservados.reutilizarConexion(productos);
					productos.iniciarTransaccion();
					try {
						productos.delete(producto);
						carrito.anadirAlCarrito(producto);
						productosReservados.insert(producto);
						productos.confirmarTransaccion();
					} catch (Exception e) {
						productos.deshacerTransaccion();
						log.info(e.getMessage());
						e.printStackTrace();
					}
					log.info("A�adido un producto al carrito");
					productos.cerrar();
				}
				// Se actualiza el cat�logo para eliminar el producto que se ha a�adido al carro
				productos.abrir();
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
