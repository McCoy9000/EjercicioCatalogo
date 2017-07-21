package catalogo.listeners;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import catalogo.constantes.Constantes;
import catalogo.dal.FacturaDAO;
import catalogo.dal.FacturaDAOFactory;
import catalogo.dal.ProductoDAO;
import catalogo.dal.ProductoDAOFactory;
import catalogo.dal.UsuarioDAO;
import catalogo.dal.UsuarioDAOFactory;
import catalogo.encriptacion.Encriptador;
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

		// Apuntar el ContextPath

		String path = application.getContextPath();
		application.setAttribute("rutaBase", path);
		log.info("Almacenada la ruta relativa de la aplicación: " + path);

		String realPath = application.getRealPath("/WEB-INF/db/driver.db");
		log.info("Almacenada la ruta real de la base de datos: " + realPath);
		// Configurar Log4j

		PropertyConfigurator.configure(InicializacionListener.class.getClassLoader().getResource("log4j.properties"));

		// Inicializar el DAO de usuarios y compradores y hacerlos accesibles a través del ServletContext

		UsuarioDAO usuarios = UsuarioDAOFactory.getUsuarioDAO();

		UsuarioDAO compradores = UsuarioDAOFactory.getCompradorDAO();

		application.setAttribute("usuarios", usuarios);

		application.setAttribute("compradores", compradores);

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

		// Inicializar el DAO de productos y hacerlo accesible a través del ServletContext

		ProductoDAO productos = ProductoDAOFactory.getProductoDAO();

		application.setAttribute("productos", productos);

		// Crear un array con todos los productos y dejarlo disponible en el ServletContext
		// Es necesario en producción para extraer el primer array de la base de datos. Aunque
		// el catálogo ya lo crea a su vez.
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
		// y hacerlos accesibles a través del ServletContext

		ProductoDAO productosReservados = ProductoDAOFactory.getProductoReservadoDAO();

		application.setAttribute("productosReservados", productosReservados);

		ProductoDAO productosVendidos = ProductoDAOFactory.getProductoVendidoDAO();

		application.setAttribute("productosVendidos", productosVendidos);

		// Inicializar el DAO de facturas y hacerlo accesible a través del ServletContext

		FacturaDAO facturas = FacturaDAOFactory.getFacturaDAO();

		application.setAttribute("facturas", facturas);

		// Inicializar una lista de los usuarios logueados y hacerla accesible a través del ServletContext

		LinkedList<Usuario> usuariosLogueados = new LinkedList<>();

		application.setAttribute("usuariosLogueados", usuariosLogueados);

		// Vaciar la base de datos de usuarios y crear un usuario administrador y un usuario normal

		Encriptador miEncriptador = null;
		byte[] encryptedadmin = null, encryptedmikel = null;
		String admin, mikel;

		try {
			miEncriptador = new Encriptador();
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e1) {
			e1.printStackTrace();
		}

		try {
			encryptedadmin = miEncriptador.cipher.doFinal(("admin").getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e1) {
			e1.printStackTrace();
		}

		try {
			encryptedmikel = miEncriptador.cipher.doFinal(("mikel").getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e1) {
			e1.printStackTrace();
		}

		admin = Base64.getMimeEncoder().encodeToString(encryptedadmin);

		mikel = Base64.getMimeEncoder().encodeToString(encryptedmikel);

		usuarios.abrir();

		Usuario usuario = new Usuario(1, "admin", "admin", "admin", admin);

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

		usuario = new Usuario(2, "Mikel", "Cuenca", "mikel", mikel);

		if (!usuarios.validar(usuario)) {
			try {
				usuarios.insert(usuario);
				log.info("Creado usuario estándard. Usuario: 'mikel', Password: 'mikel'");
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				log.info("No se pudo crear el usuario 'mikel'");
			}
		}

		// Vaciar la base de datos de productos y rellenarla con 224 productos de prueba

		productos.reutilizarConexion(usuarios);
		productos.iniciarTransaccion();

		try {

			if (productos.findAll().length == 0) {

				BigDecimal bd1000 = new BigDecimal("100000");
				BigDecimal bd2000 = new BigDecimal("200000");
				BigDecimal bd3000 = new BigDecimal("300000");
				BigDecimal bd4000 = new BigDecimal("400000");

				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(1, "Mustang '74", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(2, "Mustang '69", "", bd1000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(3, "Shelby GT 350CR", "", bd2000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(4, "Mustang '72", "", bd1000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(5, "Ford GT '92", "", bd3000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(6, "Ford GT '82", "", bd4000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(7, "Ford GT '90", "", bd3000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(8, "Ford GT '87", "", bd4000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(9, "Ford GT '91", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));
				productos.insert(new Producto(10, "Ford GT 40", "", bd3000));

				log.info("Creados 200 productos de prueba");
			}

			productos.confirmarTransaccion();
		} catch (Exception e) {
			log.info("Error al crear productos de prueba");
			productos.deshacerTransaccion();
		}

		// Poner el catálogo de productos disponible en aplicación

		application.setAttribute("catalogo", productos.getCatalogo());

		// Establecer el contador de facturas al valor siguiente a la última factura de la tabla

		facturas.reutilizarConexion(usuarios);

		try {
			Constantes.siguienteFactura = facturas.getMaxId() + 1;
			log.info("Iniciado el contador de facturas con el valor del número siguiente al de la última factura emitida");
		} catch (Exception e) {
			log.info(e.getMessage());
			log.info("No se pudo establecer el valor del contador de facturas");
			throw new RuntimeException("ERROR FATAL. SUSPENDIENDO LA APLICACIÓN. POR FAVOR, REVISE EL ESTADO DE LA APLICACIÓN");
		}

		usuarios.cerrar(); // Cierro la conexón después de todas las operaciones con la base de datos

	}
}
