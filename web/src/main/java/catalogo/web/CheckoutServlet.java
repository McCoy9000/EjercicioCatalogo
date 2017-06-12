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

import catalogo.dal.ProductoDAO;
import catalogo.tipos.Carrito;
import catalogo.tipos.Producto;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Carrito.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		ServletContext application = request.getServletContext();
		HttpSession session = request.getSession();
		String op = request.getParameter("op");
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		Carrito carrito = (Carrito) session.getAttribute("carrito");
		
		Producto producto;
		Producto[] listaProductosArr = null;
		Integer numeroProductos = 0;
		Double precioTotal = 0.0;
		
		try {
		listaProductosArr = carrito.buscarTodosLosProductos();
		numeroProductos = listaProductosArr.length;
		precioTotal = carrito.precioTotal();
		
		} catch (NullPointerException npe){
			request.getRequestDispatcher("/login");
		}
		
		session.setAttribute("productosArr", listaProductosArr);
		
		session.setAttribute("numeroProductos", numeroProductos);
		
		session.setAttribute("precioTotal", precioTotal);
		
		if (op == null){
			
			try {
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
			} catch (NullPointerException npe) {
				carrito = new Carrito();
				session.setAttribute("carrito", carrito);
				session.setAttribute("productosArr", carrito.buscarTodosLosProductos());
				session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
			}
			
			request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
			
		} else {
			
			switch (op) {
				case "pagar":
					log.info("Carrito de la compra liquidado");
					carrito = new Carrito();
					session.setAttribute("carrito", carrito);
					session.setAttribute("productosArr", carrito.buscarTodosLosProductos());
					session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
					request.getRequestDispatcher("/catalogo").forward(request, response);
					break;
				case "quitar":
					log.info("Producto retirado del carro");
					int id = Integer.parseInt(request.getParameter("id"));
					producto = carrito.buscarPorId(id);
					carrito.quitarDelCarrito(id);
					productos.abrir();
					productos.insert(producto);
					productos.cerrar();
					application.setAttribute("productos", productos);
					session.setAttribute("carrito", carrito);
					session.setAttribute("productosArr", carrito.buscarTodosLosProductos());
					session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
					session.setAttribute("precioTotal", carrito.precioTotal());
				default:
					request.getRequestDispatcher("/WEB-INF/vistas/checkout.jsp").forward(request, response);
			}
			
		}
		
	}

}
