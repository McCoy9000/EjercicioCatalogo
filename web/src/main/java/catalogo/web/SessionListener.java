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
		HttpSession session = se.getSession();
		session.setMaxInactiveInterval(900);
		CarritoDAO carrito = CarritoDAOFactory.getCarritoDAO();
		session.setAttribute("sessionBindingListener", new ObjectLock());
		session.setAttribute("carrito", carrito);

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

	}

}
