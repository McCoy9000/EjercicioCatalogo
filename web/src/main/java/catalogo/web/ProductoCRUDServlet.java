package catalogo.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import catalogo.dal.IpartekDAO;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Producto;

@WebServlet("/admin/productocrud")
public class ProductoCRUDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final String RUTA_FORMULARIO = "/WEB-INF/vistas/productoform.jsp";
	static final String RUTA_LISTADO = "/WEB-INF/vistas/productocrud.jsp";
	static final String RUTA_SERVLET_LISTADO = "/admin/productocrud";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();

		IpartekDAO dao = (IpartekDAO) application.getAttribute("dao");
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		String op = request.getParameter("op");

		if (op == null) {

			productos.abrir();

			Producto[] productosArr = productos.findAll();

			productos.cerrar();

			application.setAttribute("productosArr", productosArr);

			request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);

		} else {

			Producto producto;

			switch (op) {
			case "modificar":
			case "borrar":
				int id = Integer.parseInt(request.getParameter("id"));
				productos.abrir();
				producto = productos.findById(id);
				productos.cerrar();
				request.setAttribute("producto", producto);
			case "alta":
				request.getRequestDispatcher(RUTA_FORMULARIO).forward(request, response);
				break;
			default:
				request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);
			}
		}
	}

}
