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

		// Inicializar el DAO de usuarios y hacerlo accesible a través del ServletContext

		UsuarioDAO usuarios = UsuarioDAOFactory.getUsuarioDAO("jdbc:sqlite:" + realPath);

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

		// Inicializar el DAO de productos y hacerlo accesible a través del ServletContext

		ProductoDAO productos = ProductoDAOFactory.getProductoDAO("jdbc:sqlite:" + realPath);

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

		ProductoDAO productosReservados = ProductoDAOFactory.getProductoReservadoDAO("jdbc:sqlite:" + realPath);

		application.setAttribute("productosReservados", productosReservados);

		ProductoDAO productosVendidos = ProductoDAOFactory.getProductoVendidoDAO("jdbc:sqlite:" + realPath);

		application.setAttribute("productosVendidos", productosVendidos);

		// Inicializar el DAO de facturas y hacerlo accesible a través del ServletContext

		FacturaDAO facturas = FacturaDAOFactory.getFacturaDAO("jdbc:sqlite:" + realPath);

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

		// if (usuarios.findAll().length != 0) {
		// try {
		// usuarios.deleteUsuarios();
		// log.info("Tabla de usuarios borrada");
		// } catch (Exception e) {
		// e.printStackTrace();
		// log.info(e.getMessage());
		// log.info("No se pudo borrar la tabla de usuarios");
		// }
		// }

		Usuario usuario = new Usuario(1, "admin", admin, "admin");

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

		usuario = new Usuario(2, "mikel", mikel, "mikel");

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
			// if (productos.findAll().length != 0) {
			// productos.deleteProductos();
			// log.info("Borrada tabla de productos");
			// }

			if (productos.findAll().length == 0) {

				productos.insert(new Producto(1, "Coche test 1", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 2", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 3", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 4", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 5", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 6", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 7", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 8", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 9", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 10", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 11", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 12", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 13", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 14", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 15", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 16", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 17", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 18", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 19", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 20", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 21", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 22", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 23", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 24", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 25", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 26", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 27", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 28", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 29", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 30", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 31", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 32", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 33", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 34", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 35", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 36", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 37", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 38", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 39", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 40", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 41", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 42", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 43", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 44", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 45", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 46", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 47", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 48", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 49", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 50", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 51", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 52", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 53", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 54", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 55", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 56", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 57", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 58", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 59", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 60", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 61", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 62", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 63", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 64", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 65", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 66", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 67", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 68", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 69", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 70", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 71", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 72", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 73", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 74", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 75", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 76", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 77", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 78", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 79", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 80", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 81", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 82", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 83", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 84", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 85", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 86", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 87", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 88", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 89", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 90", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 91", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 92", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 93", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 94", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 95", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 96", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 97", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 98", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 99", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 100", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 101", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 102", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 103", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 104", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 105", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 106", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 107", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 108", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 109", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 110", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 111", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 112", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 113", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 114", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 115", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 116", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 117", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 118", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 119", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 120", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 121", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 122", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 123", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 124", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 125", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 126", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 127", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 128", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 129", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 130", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 131", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 132", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 133", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 134", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 135", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 136", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 137", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 138", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 139", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 140", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 141", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 142", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 143", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 144", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 145", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 146", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 147", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 148", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 149", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 150", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 151", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 152", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 153", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 154", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 155", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 156", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 157", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 158", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 159", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 160", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 161", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 162", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 163", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 164", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 165", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 166", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 167", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 168", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 169", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 170", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 171", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 172", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 173", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 174", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 175", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 176", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 177", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 178", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 179", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(3, "Coche test 180", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 181", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 182", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 183", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 184", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 185", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 186", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 187", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 188", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 189", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 190", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 191", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 192", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 193", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 194", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 195", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 196", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 197", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 198", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 199", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 200", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 201", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 202", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 203", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 204", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 205", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 206", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 207", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(2, "Coche test 208", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 209", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 210", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(2, "Coche test 211", "Cadillac", new BigDecimal("2000")));
				productos.insert(new Producto(3, "Coche test 212", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 213", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 214", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(3, "Coche test 215", "Charger", new BigDecimal("3000")));
				productos.insert(new Producto(4, "Coche test 216", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 217", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 218", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(4, "Coche test 219", "Challenger", new BigDecimal("4000")));
				productos.insert(new Producto(1, "Coche test 220", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 221", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 222", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 223", "Mustang", new BigDecimal("1000")));
				productos.insert(new Producto(1, "Coche test 224", "Mustang", new BigDecimal("1000")));

				log.info("Creados 224 productos de prueba");
			}

			productos.confirmarTransaccion();
		} catch (Exception e) {
			log.info("Error al crear productos de prueba");
			productos.deshacerTransaccion();
		}

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
