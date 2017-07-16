package catalogo.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import catalogo.constantes.Constantes;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Articulo;
import catalogo.tipos.Producto;

@WebServlet("/admin/productocrud")
public class ProductoCRUDServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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

			Articulo[] productosArr = productos.getCatalogo();

			productos.cerrar();

			application.setAttribute("productosArr", productosArr);

			request.getRequestDispatcher(Constantes.RUTA_LISTADO_PRODUCTO).forward(request, response);

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
					request.getRequestDispatcher(Constantes.RUTA_LISTADO_PRODUCTO).forward(request, response);
					break;
				}

				productos.abrir();
				try {
					producto = productos.findById(id);
				} catch (Exception e) {
					e.printStackTrace();
					productos.cerrar();
					request.getRequestDispatcher(Constantes.RUTA_LISTADO_PRODUCTO).forward(request, response);
					break;
				}
				productos.cerrar();
				request.setAttribute("producto", producto);
			case "alta":
				request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_PRODUCTO).forward(request, response);
				break;
			default:
				request.getRequestDispatcher(Constantes.RUTA_LISTADO_PRODUCTO).forward(request, response);
			}
		}
	}

}
