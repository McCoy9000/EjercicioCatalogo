package catalogo.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import catalogo.constantes.Constantes;
import catalogo.dal.DAOException;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Articulo;
import catalogo.tipos.Producto;

@WebServlet("/admin/productoform")
public class ProductoFormServlet extends HttpServlet {

	private static final long serialVersionUID = 3997952646417125446L;
	private static Logger log = Logger.getLogger(ProductoFormServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Recoger el objeto application del ServletContext
		ServletContext application = getServletContext();
		HttpSession session = request.getSession();

		// Borrar los errores que puedan venir en sesión
		session.removeAttribute("errorProducto");

		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		// Recoger la opción elegida por el usuario en el formulario enviada por url
		String op = request.getParameter("opform");

		// Declaro aquí los dispatcher porque en un momento me dio un problema extraño por declararlos en el momento en que
		// los necesitaba
		RequestDispatcher rutaListado = request.getRequestDispatcher(Constantes.RUTA_SERVLET_LISTADO_PRODUCTO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO);

		// Declaración de las variables para construir el objeto con el que se trabajará e iniciarlas con los valores recogidos
		// del formulario

		Producto producto;

		int id;

		if (request.getParameter("id") != null) {
			try {
				id = Integer.parseInt(request.getParameter("id"));
			} catch (Exception e) {
				id = 0;
			}
		} else {
			id = 0;
		}

		int groupId = 0;
		int k = 0;

		if (request.getParameter("id") != null) {
			if (!(request.getParameter("groupId").equals("Nuevo grupo de productos"))) {
				try {
					groupId = Integer.parseInt(request.getParameter("groupId").split("\\ - ")[0]);
				} catch (Exception e) {
					groupId = 0;
				}
			} else {
				productos.abrir();
				Iterator<Integer> it = productos.getAlmacen().keySet().iterator();
				while (it.hasNext()) {
					if ((k = (Integer) it.next()) > groupId) {
						groupId = k;
					}
					groupId += 1;
				}
			}
		} else {
			groupId = 0;
		}

		String nombre, descripcion;

		if (request.getParameter("groupId") != null) {
			if (!(request.getParameter("groupId").equals("Nuevo grupo de productos"))) {
				nombre = request.getParameter("groupId").split("\\ - ")[1];
			} else {
				if (request.getParameter("nombre") != null) {
					nombre = request.getParameter("nombre").trim();
				} else {
					nombre = request.getParameter("nombre");
				}
			}
		} else {
			if (request.getParameter("nombre") != null) {
				nombre = request.getParameter("nombre").trim();
			} else {
				nombre = request.getParameter("nombre");
			}
		}


		if (request.getParameter("descripcion") != null) {
			descripcion = request.getParameter("descripcion").trim();
		} else {
			descripcion = request.getParameter("descripcion");
		}

		BigDecimal precio;

		if (request.getParameter("precio") != null) {
			try {
				precio = new BigDecimal(request.getParameter("precio"));
			} catch (Exception e) {
				precio = BigDecimal.ZERO;
				log.info("Error al parsear precio");
			}
		} else {
			precio = BigDecimal.ZERO;
		}

		int cantidad;

		if (request.getParameter("cantidad") != null) {
			try {
				cantidad = Integer.parseInt(request.getParameter("cantidad"));
			} catch (Exception e) {
				cantidad = 1;
				log.info("Error al parsear la cantidad");
			}
		} else {
			cantidad = 1;
		}

		productos.abrir();

		Articulo[] catalogo = productos.getCatalogo();
		application.setAttribute("catalogo", catalogo);

		productos.cerrar();

		// Lógica del servlet según la opción elegida por el usuario y enviada por el navegador
		// encapsulada en opform.
		if (op == null) {
			producto = new Producto(groupId, nombre, descripcion, precio);
			session.removeAttribute("errorProducto");
			request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO + "?op=alta").forward(request, response);
		} else {

			switch (op) {
			case "alta":

				producto = new Producto(groupId, nombre, descripcion, precio);
				productos.abrir();

				if (nombre == null || nombre == "") {
					session.setAttribute("errorProducto", "Debes introducir un nombre de producto");
					request.setAttribute("producto", producto);
					rutaFormulario.forward(request, response);
				} else if (precio.compareTo(BigDecimal.ZERO) == -1 || precio == null) {
					session.setAttribute("errorProducto", "Debes introducir un precio mayor que 0");
					request.setAttribute("producto", producto);
					request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO + "?op=alta").forward(request, response);
				} else {

					productos.iniciarTransaccion();

					try {
						for (int i = 0; i < cantidad; i++) {
							productos.insert(producto);
						}
						productos.confirmarTransaccion();
						session.removeAttribute("errorProducto");
						log.info("Producto(s) dado(s) de alta");
						rutaListado.forward(request, response);
					} catch (DAOException e) {
						productos.deshacerTransaccion();
						session.setAttribute("errorProducto", "Error al dar de alta el producto. Inténtelo de nuevo");
						request.setAttribute("producto", producto);
						request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO + "?op=alta").forward(request, response);
					} finally {
						productos.cerrar();
					}
				}
				break;
			case "modificar":

				// Aquí hay que declarar un nuevo producto con los datos recogidos del formulario.
				producto = new Producto(id, groupId, nombre, descripcion, precio);

				if (nombre == null || nombre == "") {
					session.setAttribute("errorProducto", "Debes introducir un nombre de producto");
					request.setAttribute("producto", producto);
					request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO + "?op=modificar").forward(request, response);
				} else {
					productos.abrir();
					List<Producto> grupoProductos = productos.getAlmacen().get(groupId);
					productos.iniciarTransaccion();
					
					try {
						for(Producto p : grupoProductos) {
							producto.setId(p.getId());;
							productos.update(producto);
						}
						productos.confirmarTransaccion();
						session.removeAttribute("errorProducto");
						log.info("Producto modificado");
					} catch (DAOException e) {
						productos.deshacerTransaccion();
						log.info("Error al modificar el producto");
						log.info(e.getMessage());
						session.setAttribute("errorProducto", "Error al modificar el producto. Inténtelo de nuevo");
						request.setAttribute("producto", producto);
						request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO + "?op=modificar").forward(request, response);
						break;
					} finally {
						productos.cerrar();
					}
					session.removeAttribute("errorProducto");
					rutaListado.forward(request, response);
				}
				break;
			case "borrar":

				productos.abrir();
				producto = productos.findById(id);

				try {
					productos.delete(producto);
					session.removeAttribute("errorProducto");
					log.info("Producto borrado");
				} catch (DAOException e) {
					log.info("Error al borrar el producto");
					log.info(e.getMessage());
					session.setAttribute("errorProducto", "Error al borrar el producto. Inténtelo de nuevo");
					request.setAttribute("producto", producto);
					request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO + "?op=borrar").forward(request, response);
					break;
				} finally {
					productos.cerrar();
				}
				session.removeAttribute("errorProducto");
				rutaListado.forward(request, response);

				break;
			default:
				session.removeAttribute("errorProducto");
				rutaListado.forward(request, response);
			}
		}
	}
}
