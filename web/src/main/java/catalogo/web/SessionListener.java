package catalogo.web;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import catalogo.dal.CarritoDAO;
import catalogo.dal.CarritoDAOFactory;

@WebListener("/sesion")
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		
		// Obtener el objeto sesión 		
		HttpSession session = se.getSession();
		// Darle un tiempo máximo de inactividad de 15 minutos
		session.setMaxInactiveInterval(900);
		// Obtener un DAO de carrito que le asigna un nuevo carrito a la sesión
		CarritoDAO carrito = CarritoDAOFactory.getCarritoDAO();
		session.setAttribute("carrito", carrito);
		// Almacenar en la sesión un objeto tipo HttpSessionBindingListener bajo el nombre sessionBindingListener
		// con los métodos a ejecutar al expirar la sesión justo antes de destruirla
		session.setAttribute("sessionBindingListener", new ObjectLock());
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

	}

}
