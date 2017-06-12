package catalogo.web;

import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import catalogo.dal.ProductosDAL;
import catalogo.dal.ProductosDALFactory;
import catalogo.dal.UsuarioDAO;
import catalogo.dal.UsuariosDALFactory;
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

		// Inicializar la base de datos de usuarios y hacerla accesible a través del ServletContext

		UsuarioDAO usuarios = UsuariosDALFactory.getUsuarioDAO();

		log.info("Base de datos de usuarios inicializada");

		application.setAttribute("usuarios", usuarios);

		usuarios.abrir();

		Usuario[] usuariosArr = usuarios.findAll();

		usuarios.cerrar();

		application.setAttribute("usuariosArr", usuariosArr);

		// Inicializar la base de datos de productos y hacerla accesible a través del ServletContext

		ProductosDAL productos = ProductosDALFactory.getProductosDAL();

		log.info("Base de datos de productos inicializada");

		application.setAttribute("productos", productos);

		Producto[] productosArr = productos.buscarTodosLosProductos();

		application.setAttribute("productosArr", productosArr);

		// Inicializar una lista de los usuarios logueados y hacerla accesible a través del ServletContext

		LinkedList<Usuario> usuariosLogueados = new LinkedList<>();

		log.info("Lista de usuarios logueados actualizada");

		application.setAttribute("usuariosLogueados", usuariosLogueados);

		// Crear un usuario administrador y un usuario normal

		Usuario admin = new Usuario(1, "admin", "admin", "admin");

		if (!usuarios.validar(admin)) {

			usuarios.abrir();

			usuarios.insert(admin);

			usuarios.cerrar();

			log.info("Creado usuario administrador. Usuario: 'admin', Password: 'admin'");

		}

		Usuario mikel = new Usuario(2, "mikel", "mikel", "mikel");

		if (!usuarios.validar(mikel)) {

			usuarios.abrir();

			usuarios.insert(mikel);

			usuarios.cerrar();

			log.info("Creado usuario estándard. Usuario: 'mikel', Password: 'mikel'");
		}

		// Rellenar la base de datos de productos si está vacía

		if (productos.buscarTodosLosProductos().length == 0) {

			productos.alta(new Producto(0, "Producto de prueba 1", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 2", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 3", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(1, "Producto de prueba 4", "Descripcion de producto de prueba", 200.0, 2));
			productos.alta(new Producto(1, "Producto de prueba 5", "Descripcion de producto de prueba", 200.0, 2));
			productos.alta(new Producto(1, "Producto de prueba 6", "Descripcion de producto de prueba", 200.0, 2));
			productos.alta(new Producto(2, "Producto de prueba 7", "Descripcion de producto de prueba", 300.0, 3));
			productos.alta(new Producto(2, "Producto de prueba 8", "Descripcion de producto de prueba", 300.0, 3));
			productos.alta(new Producto(2, "Producto de prueba 9", "Descripcion de producto de prueba", 300.0, 3));
			productos.alta(new Producto(3, "Producto de prueba 10", "Descripcion de producto de prueba", 400.0, 4));
			productos.alta(new Producto(3, "Producto de prueba 11", "Descripcion de producto de prueba", 400.0, 4));
			productos.alta(new Producto(3, "Producto de prueba 12", "Descripcion de producto de prueba", 400.0, 4));
			productos.alta(new Producto(0, "Producto de prueba 13", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 14", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 15", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 16", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 17", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 18", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 19", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(1, "Producto de prueba 20", "Descripcion de producto de prueba", 200.0, 2));
			productos.alta(new Producto(1, "Producto de prueba 21", "Descripcion de producto de prueba", 200.0, 2));
			productos.alta(new Producto(1, "Producto de prueba 22", "Descripcion de producto de prueba", 200.0, 2));
			productos.alta(new Producto(1, "Producto de prueba 23", "Descripcion de producto de prueba", 200.0, 2));
			productos.alta(new Producto(2, "Producto de prueba 24", "Descripcion de producto de prueba", 300.0, 3));
			productos.alta(new Producto(2, "Producto de prueba 25", "Descripcion de producto de prueba", 300.0, 3));
			productos.alta(new Producto(2, "Producto de prueba 26", "Descripcion de producto de prueba", 300.0, 3));
			productos.alta(new Producto(2, "Producto de prueba 27", "Descripcion de producto de prueba", 300.0, 3));
			productos.alta(new Producto(3, "Producto de prueba 28", "Descripcion de producto de prueba", 400.0, 4));
			productos.alta(new Producto(3, "Producto de prueba 29", "Descripcion de producto de prueba", 400.0, 4));
			productos.alta(new Producto(3, "Producto de prueba 30", "Descripcion de producto de prueba", 400.0, 4));
			productos.alta(new Producto(3, "Producto de prueba 31", "Descripcion de producto de prueba", 400.0, 4));
			productos.alta(new Producto(0, "Producto de prueba 32", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 33", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 34", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 35", "Descripcion de producto de prueba", 100.0, 1));
			productos.alta(new Producto(0, "Producto de prueba 36", "Descripcion de producto de prueba", 100.0, 1));
			log.info("Creados 36 productos de prueba");
		}

		// Apuntar el ContextPath

		String path = servletContextEvent.getServletContext().getContextPath();
		application.setAttribute("rutaBase", path);
		log.info("Almacenada la ruta relativa de la aplicación:" + path);
	}
}
