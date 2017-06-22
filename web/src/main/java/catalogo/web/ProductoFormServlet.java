package catalogo.web;

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
import catalogo.dal.IpartekDAO;
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

		IpartekDAO dao = (IpartekDAO) application.getAttribute("dao");
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		// Regocoger la opción elegida por el usuario en el formulario enviada por url
		String op = request.getParameter("opform");

		// Declaro aquí los dispatcher porque en un momento me dio un problema extraño por declararlos en el momento en que
		// los necesitaba
		RequestDispatcher rutaListado = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_FORMULARIO);

		// Declaración de las variables para construir el objeto con el que se trabajará e iniciarlas con los valores recogidos
		// del formulario

		int id = 0;

		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			id = 0;
		}

		int groupId;

		if (request.getParameter("groupId") == null) {
			groupId = 0;
		} else {
			try {
				groupId = Integer.parseInt(request.getParameter("groupId"));
			} catch (NumberFormatException e) {
				groupId = 0;
			}
		}

		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		Double precio;

		if (request.getParameter("precio") == "") {
			precio = 0.0;
		} else if (request.getParameter("precio") == null) {
			precio = 0.0;
		} else {
			try {
				precio = Double.parseDouble(request.getParameter("precio"));
			} catch (NumberFormatException e) {
				precio = 0.0;
			}
		}

		// Logica del servlet según la opción elegida por el usuario y enviada por el navegador
		// encapsulada en opform.
		if (op == null) {
			rutaListado.forward(request, response);
			return;
		}

		Producto producto;


		switch (op) {
		case "alta":

			producto = new Producto(groupId, nombre, descripcion, precio);

			if (nombre == null || nombre == "") {
				producto.setErrores("El nombre de producto no puede estar vacío");
				request.setAttribute("producto", producto);
				rutaFormulario.forward(request, response);
			} else {
				if (productos != null && !productos.validar(producto)) {
					productos.abrir();
					productos.insert(producto);
					productos.cerrar();
					log.info("Producto dado de alta");
					rutaListado.forward(request, response);
				} else {
					producto.setErrores("El producto ya existe");
					request.setAttribute("producto", producto);
					rutaFormulario.forward(request, response);
				}
			}
			break;
		case "modificar":

			producto = new Producto(id, groupId, nombre, descripcion, precio);

			if (nombre == null || nombre == "") {
				producto.setErrores("El nombre de producto no puede estar vacío");
				request.setAttribute("producto", producto);
				rutaFormulario.forward(request, response);
			} else {
				try {
					productos.abrir();
					productos.update(producto);
					productos.cerrar();
					log.info("Producto modificado");
				} catch (DAOException e) {
					producto.setErrores(e.getMessage());
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
				producto.setErrores(e.getMessage());
				request.setAttribute("producto", producto);
				rutaFormulario.forward(request, response);
				return;
			}
			rutaListado.forward(request, response);

			break;
		}
	}
}
