package catalogo.web.listeners;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import catalogo.dal.CarritoDAO;
import catalogo.dal.ProductoDAO;
import catalogo.tipos.Producto;

@WebListener("/aplicacion")
public class ContextAttributeListener implements Serializable, ServletContextAttributeListener {

	private static Logger log = Logger.getLogger(InicializacionListener.class);
	private static final long serialVersionUID = 734423032861834388L;

	public ContextAttributeListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {

		log.info("ServletContextAttributeEvent disparado: attributeAdded");
		// Obtener el objeto application y los DAOs asociados conexión, de productos y productosReservados

		if (("carritoAbandonado").equals(event.getName())) {

			ServletContext application = event.getServletContext();

			CarritoDAO carrito = (CarritoDAO) application.getAttribute("carritoAbandonado");

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
				}
				productos.confirmarTransaccion();
			} catch (Exception e) {
				productos.deshacerTransaccion();
			}

			productos.cerrar();

			log.info("Vaciado carrito abandonado");
		}
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {

	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		
		log.info("ServletContextAttributeEvent disparado: attributeReplaced");
		// Obtener el objeto application y los DAOs asociados conexión, de productos y productosReservados

		if (("carritoAbandonado").equals(event.getName())) {

			ServletContext application = event.getServletContext();

			CarritoDAO carrito = (CarritoDAO) application.getAttribute("carritoAbandonado");

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
				}
				productos.confirmarTransaccion();
			} catch (Exception e) {
				productos.deshacerTransaccion();
			}

			productos.cerrar();

			log.info("Vaciado carrito abandonado");
		}
	}

}
