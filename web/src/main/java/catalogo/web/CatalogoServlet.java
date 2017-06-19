package catalogo.web;

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
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Carrito;
import catalogo.tipos.Producto;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

	private static final long serialVersionUID = -2040781710356406128L;
	private static Logger log = Logger.getLogger(CatalogoServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();

		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		// Generar el catálogo. El catálogo es un array en el que cada elemento es, a su vez, el primer elemento de la lista de productos
		// de un determinado grupo de productos.

		productos.abrir();
		application.setAttribute("catalogo", productos.getCatalogo());
		productos.cerrar();

		HttpSession session = request.getSession();

		// Recoger el carrito asociado a la sesión o, en caso de que no exista (porque el usuario haya entrado directamente al catálogo desde
		// URL), crearlo.

		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");

		if (carrito == null) {

			carrito = CarritoDAOFactory.getCarritoDAO();
		}

		// Lógica del servlet según la opción con la que haya llegado el usuario

		String op = request.getParameter("op");

		if (op == null) {

			session.setAttribute("carrito", carrito);
			session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);

			request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);

		} else {

			switch (op) {

			case "logout":
				session.setAttribute("carrito", carrito);
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
				request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);
				break;

			case "anadir":

				Producto producto;

				int id = Integer.parseInt(request.getParameter("id"));

				productos.abrir();

				producto = productos.findById(id);

				if (producto != null) {

					productos.iniciarTransaccion();
					try {
						productos.delete(producto);
						carrito.anadirAlCarrito(producto);
						productos.confirmarTransaccion();
					} catch (Exception e) {
						productos.deshacerTransaccion();
					}

					log.info("Añadido un producto al carrito");
				}

				application.setAttribute("catalogo", productos.getCatalogo());
				productos.cerrar();

				application.setAttribute("productos", productos);
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
