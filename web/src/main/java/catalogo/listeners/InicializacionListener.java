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

		// Inicializar el DAO de usuarios y hacerlo accesible a través del ServletContext

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

		usuarios.abrir();

		if (usuarios.findAll().length != 0) {
			try {
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
				log.info("Creado usuario estándard. Usuario: 'mikel', Password: 'mikel'");
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
				productos.insert(new Producto(1, "Coche test 37", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 38", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 39", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 40", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 41", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 42", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 43", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 44", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 45", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 46", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 47", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 48", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 49", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 50", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 51", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 52", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 53", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 54", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 55", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 56", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 57", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 58", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 59", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 60", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 61", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 62", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 63", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 64", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 65", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 66", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 67", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 68", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 69", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 70", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 71", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 72", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 73", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 74", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 75", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 76", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 77", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 78", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 79", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 80", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 81", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 82", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 83", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 84", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 85", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 86", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 87", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 88", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 89", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 90", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 91", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 92", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 93", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 94", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 95", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 96", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 97", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 98", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 99", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 100", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 101", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 102", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 103", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 104", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 105", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 106", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 107", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 108", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 109", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 110", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 111", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 112", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 113", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 114", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 115", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 116", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 117", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 118", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 119", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 120", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 121", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 122", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 123", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 124", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 125", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 126", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 127", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 128", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 129", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 130", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 131", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 132", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 133", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 134", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 135", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 136", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 137", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 138", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 139", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 140", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 141", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 142", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 143", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 144", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 145", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 146", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 147", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 148", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 149", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 150", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 151", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 152", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 153", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 154", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 155", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 156", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 157", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 158", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 159", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 160", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 161", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 162", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 163", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 164", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 165", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 166", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 167", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 168", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 169", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 170", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 171", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 172", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 173", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 174", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 175", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 176", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 177", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 178", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 179", "Challenger", 4000.0));
				productos.insert(new Producto(3, "Coche test 180", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 181", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 182", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 183", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 184", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 185", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 186", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 187", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 188", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 189", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 190", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 191", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 192", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 193", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 194", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 195", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 196", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 197", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 198", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 199", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 200", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 201", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 202", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 203", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 204", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 205", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 206", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 207", "Mustang", 1000.0));
				productos.insert(new Producto(2, "Coche test 208", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 209", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 210", "Cadillac", 2000.0));
				productos.insert(new Producto(2, "Coche test 211", "Cadillac", 2000.0));
				productos.insert(new Producto(3, "Coche test 212", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 213", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 214", "Charger", 3000.0));
				productos.insert(new Producto(3, "Coche test 215", "Charger", 3000.0));
				productos.insert(new Producto(4, "Coche test 216", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 217", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 218", "Challenger", 4000.0));
				productos.insert(new Producto(4, "Coche test 219", "Challenger", 4000.0));
				productos.insert(new Producto(1, "Coche test 220", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 221", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 222", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 223", "Mustang", 1000.0));
				productos.insert(new Producto(1, "Coche test 224", "Mustang", 1000.0));

				log.info("Creados 188 productos de prueba");
			}

			productos.confirmarTransaccion();
		} catch (Exception e) {
			log.info("Error al crear productos de prueba");
			productos.deshacerTransaccion();
		}

		// Establecer el contador de facturas al valor siguiente a la última factura de la tabla

		facturas.reutilizarConexion(usuarios);

		try {
			Factura.siguienteFactura = facturas.getMaxId() + 1;
			log.info("Iniciado el contador de facturas con el valor del número siguiente al de la última factura emitida");
		} catch (Exception e) {
			log.info(e.getMessage());
			log.info("No se pudo establecer el valor del contador de facturas");
			throw new RuntimeException("ERROR FATAL. SUSPENDIENDO LA APLICACIÓN. POR FAVOR, REVISE EL ESTADO DE LA APLICACIÓN");
		}

		usuarios.cerrar(); // Cierro la conexón después de todas las operaciones con la base de datos

		// Apuntar el ContextPath

		String path = servletContextEvent.getServletContext().getContextPath();
		application.setAttribute("rutaBase", path);
		log.info("Almacenada la ruta relativa de la aplicación: " + path);
	}
}
