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

import catalogo.dal.DALException;
import catalogo.dal.ProductosDAL;
import catalogo.tipos.Producto;

@WebServlet("/admin/productoform")
public class ProductoFormServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ProductoFormServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();

		String op = request.getParameter("opform");

		RequestDispatcher rutaListado = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(ProductoCRUDServlet.RUTA_FORMULARIO);

		Integer id;
		String nombre = "";

		if (request.getParameter("id") == null) {
			id = 0;
		} else {
			try {
				id = Integer.parseInt(request.getParameter("id"));
			} catch (NumberFormatException e) {
				id = Producto.siguienteId;
			}
		}
		
		Integer groupId;
		
		if (request.getParameter("groupId")== null) {
			groupId = 0;
		} else {
			try {
				groupId = Integer.parseInt(request.getParameter("groupId"));
			} catch (NumberFormatException e) {
				groupId = 0;
			}
		}
		
		nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		Double precio;
		int imagen;

		if (request.getParameter("imagen") == "") {
			imagen = 0;
		} else if (request.getParameter("imagen") == null) {
			imagen = 0;
		} else {
			try {
				imagen = Integer.parseInt(request.getParameter("imagen"));
			} catch (NumberFormatException e) {
				imagen = 0;
			}
		}

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

		if (op == null) {
			rutaListado.forward(request, response);
			return;
		}

		Producto producto = new Producto(groupId, nombre, descripcion, precio, imagen);
		producto.setId(id);

		ProductosDAL productos = (ProductosDAL) application.getAttribute("productos");

		switch (op) {
		case "alta":

			if (nombre == null || nombre == "") {
				producto.setErrores("El nombre de producto no puede estar vacío");
				request.setAttribute("producto", producto);
				rutaFormulario.forward(request, response);
			} else {
				if (productos != null && !productos.validar(producto)) {

					productos.alta(producto);
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
			if (nombre == null || nombre == "") {
				producto.setErrores("El nombre de producto no puede estar vacío");
				request.setAttribute("producto", producto);
				rutaFormulario.forward(request, response);
			} else {
				try {
					productos.modificar(producto);
					log.info("Producto modificado");
				} catch (DALException de) {
					producto.setErrores(de.getMessage());
					request.setAttribute("producto", producto);
					rutaFormulario.forward(request, response);
					return;
				}
				rutaListado.forward(request, response);
			}
			break;
		case "borrar":
			try {
				productos.borrar(producto);
				log.info("Producto borrado");
			} catch (DALException de) {
				producto.setErrores(de.getMessage());
				request.setAttribute("producto", producto);
				rutaFormulario.forward(request, response);
				return;
			}
			rutaListado.forward(request, response);

			break;
		}
	}
}
