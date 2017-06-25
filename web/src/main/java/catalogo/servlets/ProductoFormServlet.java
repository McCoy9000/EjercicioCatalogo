package catalogo.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		// Regocoger la opci�n elegida por el usuario en el formulario enviada por url
		String op = request.getParameter("opform");

		// Declaro aqu� los dispatcher porque en un momento me dio un problema extra�o por declararlos en el momento en que
		// los necesitaba
		RequestDispatcher rutaListado = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_FORMULARIO);

		// Declaraci�n de las variables para construir el objeto con el que se trabajar� e iniciarlas con los valores recogidos
		// del formulario

		int id;

		if (request.getParameter("id") != null) {
			try {
				id = Integer.parseInt(request.getParameter("id"));
			} catch (Exception e) {
				id = 0;
				e.printStackTrace();
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
				e.printStackTrace();
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
				e.printStackTrace();
			}
		} else {
			precio = 0.0;
		}

		// Logica del servlet seg�n la opci�n elegida por el usuario y enviada por el navegador
		// encapsulada en opform.
		if (op == null) {
			rutaListado.forward(request, response);
		} else {

			Producto producto;

			switch (op) {
				case "alta":
		
					producto = new Producto(groupId, nombre, descripcion, precio);
		
					if (nombre == null || nombre == "") {
						producto.setErrores("El nombre de producto no puede estar vac�o");
						request.setAttribute("producto", producto);
						rutaFormulario.forward(request, response);
					} else if (precio == 0.0 || precio == null) {
						producto.setErrores("Debes introducir un precio v�lido superior a 0");
						request.setAttribute("producto", producto);
						rutaFormulario.forward(request, response);
					} else {
						productos.abrir();
						if (productos != null && !productos.validar(producto)) {
							try {
								productos.insert(producto);
								log.info("Producto dado de alta");
								rutaListado.forward(request, response);
							} catch (DAOException e) {
								producto.setErrores("Error al dar de alta el producto");
								request.setAttribute("producto", producto);
								rutaFormulario.forward(request, response);
							}
						} else {
							producto.setErrores("El producto ya existe");
							request.setAttribute("producto", producto);
							rutaFormulario.forward(request, response);
						}
						productos.cerrar();
		
					}
					break;
				case "modificar":
		
					producto = new Producto(id, groupId, nombre, descripcion, precio);
		
					if (nombre == null || nombre == "") {
						producto.setErrores("El nombre de producto no puede estar vac�o");
						request.setAttribute("producto", producto);
						rutaFormulario.forward(request, response);
					} else {
						try {
							productos.abrir();
							productos.update(producto);
							productos.cerrar();
							log.info("Producto modificado");
						} catch (DAOException e) {
							producto.setErrores("Error al modificar el producto");
							request.setAttribute("producto", producto);
							rutaFormulario.forward(request, response);
							return;
						}
						rutaListado.forward(request, response);
					}
					break;
				case "borrar":
		
					producto = new Producto(id, groupId, nombre, descripcion, precio);
		
					try {
						productos.abrir();
						productos.delete(producto);
						productos.cerrar();
						log.info("Producto borrado");
					} catch (DAOException e) {
						producto.setErrores("Error al modificar el producto");
						request.setAttribute("producto", producto);
						rutaFormulario.forward(request, response);
						return;
					}
					rutaListado.forward(request, response);
		
					break;
				default:
					rutaListado.forward(request, response);
			}
		}
	}
}