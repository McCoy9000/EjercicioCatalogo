package catalogo.web;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import catalogo.dal.CarritoDAO;
import catalogo.dal.CarritoDAOFactory;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Producto;

@WebListener("/sesion")
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		session.setMaxInactiveInterval(1800);
		CarritoDAO carrito = CarritoDAOFactory.getCarritoDAO();
		session.setAttribute("carrito", carrito);

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

		HttpSession session = se.getSession();
		ServletContext application = session.getServletContext();

		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");

		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");
		productos.abrir();
		productos.iniciarTransaccion();
		if (!(carrito == null)) {

			for (Producto p : carrito.buscarTodosLosProductos()) {
				productos.insert(p);
			}
		}

		productos.confirmarTransaccion();
		productos.cerrar();
	}

}
