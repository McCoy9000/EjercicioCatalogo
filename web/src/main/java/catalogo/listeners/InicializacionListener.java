package catalogo.listeners;

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

		// Inicializar el DAO de usuarios y hacerlo accesible a trav�s del ServletContext

		UsuarioDAO usuarios = UsuarioDAOFactory.getUsuarioDAO();

		application.setAttribute("usuarios", usuarios);

		// Crear un array con todos los usuarios y dejarlo disponible en el ServletContext
		
		Usuario[] usuariosArr = null;
		
		usuarios.abrir();
		
		try {
			usuariosArr = usuarios.findAll();
		} catch (Exception e) {
			log.info(e.getMessage());
			log.info("No se pudo crear la lista de usuarios");
		}
		
		usuarios.cerrar();

		application.setAttribute("usuariosArr", usuariosArr);

		// Inicializar el DAO de productos y hacerlo accesible a trav�s del ServletContext

		ProductoDAO productos = ProductoDAOFactory.getProductoDAO();

		application.setAttribute("productos", productos);

		// Crear un array con todos los productos y dejarlo disponible en el ServletContext

		Producto[] productosArr = null;
		
		productos.abrir();
	
		try {
			productosArr = productos.findAll();
		} catch (Exception e) {
			log.info(e.getMessage());
			log.info("No se pudo crear la lista de productos disponibles");
		}
		productos.cerrar();

		application.setAttribute("productosArr", productosArr);

		// Inicializar el DAO de ProductosReservados y ProductosVendidos 
		// y hacerlos accesibles a trav�s del ServletContext

		ProductoDAO productosReservados = ProductoDAOFactory.getProductoReservadoDAO();

		application.setAttribute("productosReservados", productosReservados);

		ProductoDAO productosVendidos = ProductoDAOFactory.getProductoVendidoDAO();

		application.setAttribute("productosVendidos", productosVendidos);

		// Inicializar el DAO de facturas y hacerlo accesible a trav�s del ServletContext

		FacturaDAO facturas = FacturaDAOFactory.getFacturaDAO();
		
		application.setAttribute("facturas", facturas);

		// Inicializar una lista de los usuarios logueados y hacerla accesible a trav�s del ServletContext

		LinkedList<Usuario> usuariosLogueados = new LinkedList<>();

		application.setAttribute("usuariosLogueados", usuariosLogueados);

		// Vaciar la base de datos de usuarios y crear un usuario administrador y un usuario normal

		usuarios.abrir();
		
		if (usuarios.findAll().length != 0) {
			try{
				usuarios.deleteUsuarios();
				log.info("Tabla de usuarios borrada");
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				log.info("No se pudo borrar la tabla de usuarios");
			}
		}

		Usuario usuario = new Usuario(1, "admin", "admin", "admin");

		if (!usuarios.validar(usuario)) {
			try {
				usuarios.insert(usuario);
				log.info("Creado usuario administrador. Usuario: 'admin', Password: 'admin'");
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				log.info("No se pudo crear el usuario 'admin'");
			}
		}

		usuario = new Usuario(2, "mikel", "mikel", "mikel");

		if (!usuarios.validar(usuario)) {
			try {
				usuarios.insert(usuario);
				log.info("Creado usuario est�ndard. Usuario: 'mikel', Password: 'mikel'");
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				log.info("No se pudo crear el usuario 'mikel'");
			}
		}


		// Vaciar la base de datos de productos y rellenarla con 36 productos de prueba

		productos.reutilizarConexion(usuarios);
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
			log.info("Error al crear productos de prueba");
			productos.deshacerTransaccion();
		}


		// Establecer el contador de facturas al valor siguiente a la �ltima factura de la tabla

		facturas.reutilizarConexion(usuarios);
		
		try {
			Factura.siguienteFactura = facturas.getMaxId() + 1;
			log.info("Iniciado el contador de facturas con el valor del n�mero siguiente al de la �ltima factura emitida");
		} catch (Exception e) {
			log.info(e.getMessage());
			log.info("No se pudo establecer el valor del contador de facturas");
			throw new RuntimeException("ERROR FATAL. SUSPENDIENDO LA APLICACI�N. POR FAVOR, REVISE EL ESTADO DE LA APLICACI�N");
		}
		
		usuarios.cerrar(); // Cierro la conexi�n despu�s de todas las operaciones con la base de datos

		// Apuntar el ContextPath

		String path = servletContextEvent.getServletContext().getContextPath();
		application.setAttribute("rutaBase", path);
		log.info("Almacenada la ruta relativa de la aplicacion: " + path);
	}
}
