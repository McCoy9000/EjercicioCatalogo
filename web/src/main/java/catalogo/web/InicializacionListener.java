package catalogo.web;

import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import catalogo.dal.FacturaDAO;
import catalogo.dal.FacturaDAOFactory;
import catalogo.dal.IpartekDAO;
import catalogo.dal.IpartekDAOFactory;
import catalogo.dal.ProductoDAO;
import catalogo.dal.ProductoDAOFactory;
import catalogo.dal.UsuarioDAO;
import catalogo.dal.UsuarioDAOFactory;
import catalogo.tipos.Factura;
import catalogo.tipos.Producto;
import catalogo.tipos.Usuario;

@WebListener("/inicializacion")
public class InicializacionListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(InicializacionListener.class);

	public InicializacionListener() {

	}

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {

		ServletContext application = servletContextEvent.getServletContext();

		// Configurar Log4j

		PropertyConfigurator.configure(InicializacionListener.class.getClassLoader().getResource("log4j.properties"));

		// Inicializar un DAO genérico para conexiones y transacciones y hacerlo accesible a través del Servlet Context

		IpartekDAO dao = IpartekDAOFactory.getIpartekDAO();

		application.setAttribute("dao", dao);

		// Inicializar el DAO de usuarios y hacerlo accesible a través del ServletContext

		UsuarioDAO usuarios = UsuarioDAOFactory.getUsuarioDAO();

		application.setAttribute("usuarios", usuarios);

		// Crear un array con todos los usuarios y dejarlo disponible en el ServletContext

		usuarios.abrir();
		Usuario[] usuariosArr = usuarios.findAll();
		usuarios.cerrar();

		application.setAttribute("usuariosArr", usuariosArr);

		// Inicializar el DAO de productos y hacerlo accesible a través del ServletContext

		ProductoDAO productos = ProductoDAOFactory.getProductoDAO();

		application.setAttribute("productos", productos);

		// Crear un array con todos los productos y dejarlo disponible en el ServletContext

		productos.abrir();
		Producto[] productosArr = productos.findAll();
		productos.cerrar();

		application.setAttribute("productosArr", productosArr);

		// Inicializar el DAO de ProductosReservados y ProductosVendidos y hacerlos accesibles a través del ServletContext

		ProductoDAO productosReservados = ProductoDAOFactory.getProductoReservadoDAO();

		application.setAttribute("productosReservados", productosReservados);

		ProductoDAO productosVendidos = ProductoDAOFactory.getProductoVendidoDAO();

		application.setAttribute("productosVendidos", productosVendidos);

		// Inicializar el DAO de facturas y hacerlo accesible a través del ServletContext

		FacturaDAO facturas = FacturaDAOFactory.getFacturaDAO();

		// Inicializar una lista de los usuarios logueados y hacerla accesible a través del ServletContext

		LinkedList<Usuario> usuariosLogueados = new LinkedList<>();

		application.setAttribute("usuariosLogueados", usuariosLogueados);

		// Vaciar la base de datos de usuarios y crear un usuario administrador y un usuario normal

		usuarios.abrir(); // Abro la conexión para todas las operaciones con la base de datos

		if (usuarios.findAll().length != 0)
			usuarios.deleteUsuarios();

		usuarios.cerrar();

		Usuario usuario = new Usuario(1, "admin", "admin", "admin");

		if (!usuarios.validar(usuario)) {
			usuarios.abrir();
			usuarios.insert(usuario);
			usuarios.cerrar();

			log.info("Creado usuario administrador. Usuario: 'admin', Password: 'admin'");
		}

		usuario = new Usuario(2, "mikel", "mikel", "mikel");

		if (!usuarios.validar(usuario)) {

			usuarios.abrir();
			usuarios.insert(usuario);
			usuarios.cerrar();

			log.info("Creado usuario estándard. Usuario: 'mikel', Password: 'mikel'");
		}

		// Vaciar la base de datos de productos y rellenarla con 36 productos de prueba

		productos.abrir();
		productos.iniciarTransaccion();

		try {
			if (productos.findAll().length != 0) {
				productos.deleteProductos();
				log.info("Borrada tabla de productos");
			}

			if (productos.findAll().length == 0) {

				productos.insert(new Producto(1, "Coche test 1", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 2", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 3", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 4", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 5", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 6", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 7", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 8", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 9", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 10", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 11", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 12", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 13", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 14", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 15", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 16", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 17", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 18", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 19", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 20", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 21", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 22", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 23", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 24", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 25", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 26", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 27", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 28", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 29", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 30", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 31", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 32", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 33", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 34", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 35", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 36", "Mustang", 1000.0));

				log.info("Creados 36 productos de prueba");
			}

			productos.confirmarTransaccion();
		} catch (Exception e) {
			productos.deshacerTransaccion();
		}

		productos.cerrar();

		// Establecer el contador de facturas al valor siguiente a la última factura de la tabla

		facturas.abrir();

		Factura.siguienteFactura = facturas.getMaxId() + 1;

		facturas.cerrar(); // Cierro la conexión después de todas las operaciones con la base de datos

		// Apuntar el ContextPath

		String path = servletContextEvent.getServletContext().getContextPath();
		application.setAttribute("rutaBase", path);
		log.info("Almacenada la ruta relativa de la aplicacion: " + path);
	}
}
