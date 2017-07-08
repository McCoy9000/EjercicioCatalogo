package catalogo.listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import catalogo.dal.CarritoDAO;
import catalogo.dal.CarritoDAOFactory;

@WebListener("/sesion")
public class SessionListener implements HttpSessionListener {

	private static Logger log = Logger.getLogger(InicializacionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {

		// Obtener el objeto sesión
		HttpSession session = se.getSession();
		// Darle un tiempo máximo de inactividad de 15 minutos
		session.setMaxInactiveInterval(900);
		// Obtener un DAO de carrito que le asigna un nuevo carrito a la sesión
		CarritoDAO carrito = CarritoDAOFactory.getCarritoDAO();
		session.setAttribute("carrito", carrito);
		session.setAttribute("numeroProductos", carrito.buscarTodosLosProductos().length);
		log.info("Carrito empaquetado en sesión y registrado como sessionBindingListener");

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

	}

}
