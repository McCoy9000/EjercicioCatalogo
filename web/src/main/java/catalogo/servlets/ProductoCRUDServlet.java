package catalogo.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		HttpSession session = request.getSession();
		// Borrado de errores en sesión por si llegan aquí desde los formularios CRUD
		session.removeAttribute("errorProducto");
		session.removeAttribute("errorUsuario");
		session.removeAttribute("errorLogin");
		session.removeAttribute("errorSignup");

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
				int id;
				try {
					id = Integer.parseInt(request.getParameter("id"));
				} catch (Exception e) {
					e.printStackTrace();
					request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);
					break;
				}
				
				productos.abrir();
				try {
					producto = productos.findById(id);
				} catch (Exception e) {
					e.printStackTrace();
					productos.cerrar();
					request.getRequestDispatcher(RUTA_LISTADO).forward(request, response);
					break;
				}
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
