package catalogo.web;

import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import catalogo.dal.ProductosDAL;
import catalogo.dal.ProductosDALFactory;
import catalogo.dal.UsuariosDAL;
import catalogo.dal.UsuariosDALFactory;
import catalogo.tipos.Producto;
import catalogo.tipos.Usuario;

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

		UsuariosDAL usuarios = UsuariosDALFactory.getUsuariosDAL();

		log.info("Base de datos de usuarios inicializada");

		application.setAttribute("usuarios", usuarios);
		
		Usuario[] usuariosArr = usuarios.buscarTodosLosUsuarios();
		
		application.setAttribute("usuariosArr", usuariosArr);

		
		// Inicializar la base de datos de productos y hacerla accesible a través del ServletContext

		ProductosDAL productos = ProductosDALFactory.getProductosDAL();

		log.info("Base de datos de productos inicializada");

		application.setAttribute("productos", productos);
		
		Producto[] productosArr = productos.buscarTodosLosProductos();
		
		application.setAttribute("productosArr", productosArr);

		
		//Inicializar una lista de los usuarios logueados y hacerla accesible a través del ServletContext
		
		LinkedList<Usuario> usuariosLogueados = new LinkedList<>();
		
		log.info("Lista de usuarios logueados actualizada");
		
		application.setAttribute("usuariosLogueados", usuariosLogueados);
		
		
		// Crear un usuario administrador y un usuario normal

		Usuario admin = new Usuario("admin", "admin", true);

		if (!usuarios.validar(admin)) {

			usuarios.alta(admin);

			log.info("Creado usuario administrador. Usuario: 'admin', Password: 'admin'");

		}
		
		Usuario mikel = new Usuario("mikel", "mikel");
		
		if (!usuarios.validar(mikel)) {
			
			usuarios.alta(mikel);
			
			log.info("Creado usuario estándard. Usuario: 'mikel', Password: 'mikel'");
		}

		
		// Rellenar la base de datos de productos

		if (productos.buscarTodosLosProductos().length == 0) {

			productos.alta(new Producto("Producto de prueba 1", "Descripcion de producto de prueba 1", 100.0, 0));
			productos.alta(new Producto("Producto de prueba 2", "Descripcion de producto de prueba 2", 100.0, 1));
			productos.alta(new Producto("Producto de prueba 3", "Descripcion de producto de prueba 3", 100.0, 2));
			productos.alta(new Producto("Producto de prueba 4", "Descripcion de producto de prueba 4", 100.0, 3));
			productos.alta(new Producto("Producto de prueba 5", "Descripcion de producto de prueba 5", 100.0, 4));
			productos.alta(new Producto("Producto de prueba 6", "Descripcion de producto de prueba 6", 100.0, 0));
			productos.alta(new Producto("Producto de prueba 7", "Descripcion de producto de prueba 7", 100.0, 1));
			productos.alta(new Producto("Producto de prueba 8", "Descripcion de producto de prueba 8", 100.0, 2));
			productos.alta(new Producto("Producto de prueba 9", "Descripcion de producto de prueba 9", 100.0, 3));
			log.info("Creados 9 productos de prueba");
		}
	}
}
