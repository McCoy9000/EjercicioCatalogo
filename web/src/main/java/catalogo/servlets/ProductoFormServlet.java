package catalogo.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import catalogo.dal.DAOException;
import catalogo.dal.ProductoDAO;
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
		session.removeAttribute("errorLogin");
		session.removeAttribute("errorSignup");
		session.removeAttribute("errorUsuario");
		
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		// Regocoger la opción elegida por el usuario en el formulario enviada por url
		String op = request.getParameter("opform");

		// Declaro aquí los dispatcher porque en un momento me dio un problema extraño por declararlos en el momento en que
		// los necesitaba
		RequestDispatcher rutaListado = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_FORMULARIO);

		// Declaración de las variables para construir el objeto con el que se trabajará e iniciarlas con los valores recogidos
		// del formulario

		Producto producto;

		int id;

		if (request.getParameter("id") != null) {
			try {
				id = Integer.parseInt(request.getParameter("id"));
			} catch (Exception e) {
				id = 0;
				log.info("Error al parsear id");
			}
		} else {
			id = 0;
		}

		int groupId;

		if (request.getParameter("id") != null) {
			try {
				groupId = Integer.parseInt(request.getParameter("groupId"));
			} catch (Exception e) {
				groupId = 0;
				log.info("Error al parsear groupId");
			}
		} else {
			groupId = 0;
		}

		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		Double precio;

		if (request.getParameter("precio") != null) {
			try {
				precio = Double.parseDouble(request.getParameter("precio"));
			} catch (Exception e) {
				precio = 0.0;
				log.info("Error al parsear precio");
			}
		} else {
			precio = 0.0;
		}

		// Lógica del servlet según la opción elegida por el usuario y enviada por el navegador
		// encapsulada en opform.
		if (op == null) {
			producto = new Producto(groupId, nombre, descripcion, precio);
			session.removeAttribute("errorProducto");
			rutaListado.forward(request, response);
		} else {

			switch (op) {
			case "alta":

				producto = new Producto(groupId, nombre, descripcion, precio);

				if (nombre == null || nombre == "") {
					session.setAttribute("errorProducto", "Debes introducir un nombre de producto");
					request.setAttribute("producto", producto);
					rutaFormulario.forward(request, response);
				} else if (precio <= 0.0 || precio == null) {
					session.setAttribute("errorProducto", "Debes introducir un precio mayor que 0");
					request.setAttribute("producto", producto);
					rutaFormulario.forward(request, response);
				} else {
					productos.abrir();
					if (productos != null && !productos.validar(producto)) {
						try {
							productos.insert(producto);
							session.removeAttribute("errorProducto");
							log.info("Producto dado de alta");
							rutaListado.forward(request, response);
						} catch (DAOException e) {
							session.setAttribute("errorProducto", "Error al dar de alta el producto. Inténtelo de nuevo");
							request.setAttribute("producto", producto);
							rutaFormulario.forward(request, response);
						} finally {
							productos.cerrar();
						}
					} else {
						session.setAttribute("errorProducto", "El producto ya existe");
						request.setAttribute("producto", producto);
						rutaFormulario.forward(request, response);
					}

				}
				break;
			case "modificar":

				productos.abrir();
				producto = new Producto(id, groupId, nombre, descripcion, precio);

				if (nombre == null || nombre == "") {
					session.setAttribute("errorProducto", "Debes introducir un nombre de producto");
					request.setAttribute("producto", producto);
					rutaFormulario.forward(request, response);
				} else {
					try {
						productos.update(producto);
						session.removeAttribute("errorProducto");
						log.info("Producto modificado");
					} catch (DAOException e) {
						log.info("Error al modificar el producto");
						log.info(e.getMessage());
						producto.setErrores("Error al modificar el producto");
						session.setAttribute("errorProducto", "Error al modificar el producto. Inténtelo de nuevo");
						request.setAttribute("producto", producto);
						rutaFormulario.forward(request, response);
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
					rutaFormulario.forward(request, response);
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
