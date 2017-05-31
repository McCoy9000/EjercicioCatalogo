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

import catalogo.dal.ProductosDAL;
import catalogo.tipos.Carrito;
import catalogo.tipos.Producto;


@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(CatalogoServlet.class);

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext application = getServletContext();

		ProductosDAL productos = (ProductosDAL) application.getAttribute("productos");

		Producto[] productosArr = productos.buscarTodosLosProductos();

		application.setAttribute("productosArr", productosArr);
		
		HttpSession session = request.getSession();
		
		Carrito carrito = (Carrito) session.getAttribute("carrito");
		
			if (carrito == null) {
				
				carrito = new Carrito();
				
			}
			
		String op = request.getParameter("op");
		
		if (op == null) {
			
			log.info("op == null en el cat�logo");
			
			session.setAttribute("productosCarrito", carrito.getListaProductos().size());
			
			session.setAttribute("carrito", carrito);
			
			session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
			
			request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);
						
		} else {
			
			switch(op){

				case "logout":
							
				log.info("Ha pasado por el logout de Cat�logo");
				
				session.invalidate();
				
				session = request.getSession();
				
				carrito = new Carrito();
				
				session.setAttribute("carrito", carrito);
				
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
				
				request.getRequestDispatcher("/WEB-INF/vistas/catalogo.jsp").forward(request, response);
				
				break;
				
				case "anadir":
						
				Producto producto;
				
				Integer idmap = Integer.parseInt(request.getParameter("id"));
				
				log.info("recoge la id");
				
				producto = productos.buscarPorId(idmap);
					
				carrito.anadirAlCarrito(producto);
						
				log.info("a�ade un objeto al carrito");
					
				session.setAttribute("productosCarrito", carrito.getListaProductos().size());
					
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
