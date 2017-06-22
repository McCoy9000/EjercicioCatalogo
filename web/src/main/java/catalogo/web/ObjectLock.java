package catalogo.web;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import catalogo.dal.CarritoDAO;
import catalogo.dal.IpartekDAO;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Carrito;
import catalogo.tipos.Producto;

public class ObjectLock implements Serializable, HttpSessionBindingListener {

	private static Logger log = Logger.getLogger(Carrito.class);
	private static final long serialVersionUID = -8310835588974617600L;

	@Override
	public void valueBound(HttpSessionBindingEvent event) {

	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {

		// Obtener el objeto session y el DAO de carrito asociado
		HttpSession session = event.getSession();

		CarritoDAO carrito = (CarritoDAO) session.getAttribute("carrito");

		// Obtener el objeto application y los DAOs asociados conexión, de productos y productosReservados
		ServletContext application = session.getServletContext();

		IpartekDAO dao = (IpartekDAO) application.getAttribute("dao");
		ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
		ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");

		// Vaciar los productos del carrito, que se registran en la tabla general de productos_reservados
		// en la tabla general de productos
		productos.abrir();
		productosReservados.reutilizarConexion(productos);
		productos.iniciarTransaccion();

		try {
			for (Producto p : carrito.buscarTodosLosProductos()) {
				productosReservados.delete(p);
				productos.insert(p);
				productos.confirmarTransaccion();
			}
		} catch (Exception e) {
			productos.deshacerTransaccion();
		}

		productos.cerrar();

		log.info("Vaciado carrito abandonado");
	}

}
