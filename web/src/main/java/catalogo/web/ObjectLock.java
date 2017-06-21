package catalogo.web;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import catalogo.dal.CarritoDAO;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Carrito;
import catalogo.tipos.Producto;

public class ObjectLock implements Serializable, HttpSessionBindingListener {

	private static Logger log = Logger.getLogger(Carrito.class);
	private static final long serialVersionUID = -8310835588974617600L;

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		log.info("valueBound funciona");

	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {

		HttpSession session = event.getSession();
		ServletContext application = session.getServletContext();

		ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");

		productosReservados.abrir();
		productos.abrir();

		for (Producto p : carrito.buscarTodosLosProductos()) {
			productosReservados.delete(p);
			productos.insert(p);
		}

		productosReservados.cerrar();
		productos.cerrar();

		log.info("valueUnbound funciona");
	}

}
