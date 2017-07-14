package catalogo.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import catalogo.constantes.Constantes;
import catalogo.dal.ProductoDAO;

@WebServlet("/admin/imagenproducto")
public class ImagenProductoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ImagenProductoServlet.class);

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
			ServletContext application = request.getServletContext();
			HttpSession session = request.getSession();
			
			session.removeAttribute("errorImagen");
			
			ProductoDAO productos = (ProductoDAO) application.getAttribute("productos");
			productos.abrir();
			application.setAttribute("catalogo", productos.getCatalogo());
			productos.cerrar();
			
			String realPath = application.getRealPath("/img/");
			String op = request.getParameter("op");
			
			
			int grupoProductos = 0;
			if (("subir").equals(op)) {
				request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_IMAGEN_PRODUCTOS).forward(request, response);
				return;
			} else {	
			try {
		        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		        if(items == null) {
					request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_IMAGEN_PRODUCTOS + "?op=subir").forward(request, response);
					return;
		        } else {
		        
			        for (FileItem item : items) {
			            if (item.isFormField()) {
			                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
			                grupoProductos = Integer.parseInt(item.getString().split("\\ - ")[0]);
			                
			            } else {
			                // Process form file field (input type="file").
			            	InputStream fileContent = null;
			            	OutputStream outStream = null;
			            	try {
			            	fileContent = item.getInputStream();
			                byte[] buffer = new byte[fileContent.available()];
			                fileContent.read(buffer);
			                
			                File foto = new File(realPath + File.separator + grupoProductos + ".jpg");
			                outStream = new FileOutputStream(foto);
			                //Tras haber modificado cualquier imagen o haber subido una nueva, al intentar sobreescribir
			                //una imagen ya existente, el constructor de FileOutputStream lanza una excepción
			                //FileNotFoundException por estar el archivo de imagen bloqueado por el sistema de mapping
			                //de archivos de windows. Es necesario esperar al garbage collector para poder lanzar el 
			                //método sin errores.
			                outStream.write(buffer);
			                log.info("Imagen cargada con éxito.");
			                } catch (FileNotFoundException fnfe) {
			                	fnfe.printStackTrace();
			                	log.info("Error al sobreescribir el archivo de imagen. Esperando al garbage collector.");
			    		        session.setAttribute("errorImagen", "Error al sobreescribir el archivo de imagen. Inténtelo de nuevo más tarde");
			    				request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_IMAGEN_PRODUCTOS + "?op=subir").forward(request, response);
			                	return;
			                } finally {
			                // "un archivo con una seccion asignada a usuario abierta"
			                fileContent.close();
			                if (outStream != null) {
			                	outStream.close();
			                }
			                System.gc();
			                }
			            }
			        }
		        }
		        
		    } catch (FileUploadException e) {
		        session.setAttribute("errorImagen", "Error al subir archivo de imagen. ¿Está seguro de haber seleccionado un archivo JPG para la subida?");
				request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_IMAGEN_PRODUCTOS + "?op=subir").forward(request, response);
				e.printStackTrace();
				return;
		    }
			request.getRequestDispatcher(Constantes.RUTA_LISTADO_PRODUCTO).forward(request, response);
			}
	}
}


