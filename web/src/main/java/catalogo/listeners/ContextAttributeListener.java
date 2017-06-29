package catalogo.listeners;

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
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {

		// Obtener el objeto application y los DAOs asociados de productos y productosReservados

		if (("carritoAbandonado").equals(event.getName())) {

			ServletContext application = event.getServletContext();

			// @SuppressWarnings("unchecked")
			// LinkedList<Usuario> usuariosLogueados = (LinkedList<Usuario>) application.getAttribute("usuariosLogueados");
			// Usuario usuario = (Usuario) application.getAttribute("usuario");

			// if(usuario != null) {
			// application.removeAttribute("usuario");
			// if(usuariosLogueados.contains(usuario)) {
			// usuariosLogueados.remove(usuario);
			// }
			// }

			CarritoDAO carrito = (CarritoDAO) application.getAttribute("carritoAbandonado");

			if (carrito.buscarTodosLosProductos().length != 0) {
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
					log.info("Vaciado carrito abandonado");
				} catch (Exception e) {
					productos.deshacerTransaccion();
					log.info(e.getMessage());
					log.info("Error al vaciar el carrito abandonado");
				}
				productos.cerrar();
			}
		}
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {

	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {

		// Obtener el objeto application y los DAOs asociados de productos y productosReservados

		if (("carritoAbandonado").equals(event.getName())) {

			ServletContext application = event.getServletContext();

			// @SuppressWarnings("unchecked")
			// LinkedList<Usuario> usuariosLogueados = (LinkedList<Usuario>) application.getAttribute("usuariosLogueados");
			// Usuario usuario = (Usuario) application.getAttribute("usuario");

			// if (usuario != null) {
			// application.removeAttribute("usuario");
			// if (usuariosLogueados.contains(usuario)) {
			// usuariosLogueados.remove(usuario);
			// }
			// }

			CarritoDAO carrito = (CarritoDAO) application.getAttribute("carritoAbandonado");

			if (carrito.buscarTodosLosProductos().length != 0) {
				ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
				ProductoDAO productosReservados = (ProductoDAO) application.getAttribute("productosReservados");

				// Vaciar los productos del carrito, que se registran en la tabla general de productos_reservados,
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
					log.info("Vaciado carrito abandonado");
				} catch (Exception e) {
					productos.deshacerTransaccion();
					log.info(e.getMessage());
					log.info("Error al vaciar el carrito abandonado");
				}
				productos.cerrar();
			}
		}
	}
}
