package catalogo.servlets;

import java.io.File;
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
import org.apache.commons.io.FilenameUtils;

import catalogo.constantes.Constantes;

@WebServlet("/admin/imagenproducto")
public class ImagenProductoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
			ServletContext application = request.getServletContext();
			HttpSession session = request.getSession();
			
			session.removeAttribute("errorImagen");
			
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
					return;
		        } else {
		        
			        for (FileItem item : items) {
			            if (item.isFormField()) {
			                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
			                grupoProductos = Integer.parseInt(item.getString().split("\\ - ")[0]);
			                
			            } else {
			                // Process form file field (input type="file").
			                String fieldName = item.getFieldName();
			                String fileName = FilenameUtils.getName(item.getName());
			                InputStream fileContent = item.getInputStream();
			                byte[] buffer = new byte[fileContent.available()];
			                fileContent.read(buffer);
			                System.out.println(realPath);
			                File foto = new File(realPath + File.separator + grupoProductos + ".jpg");
			                OutputStream outStream = new FileOutputStream(foto);
			                outStream.write(buffer);
			                fileContent.close();
			                outStream.close();
			            }
			        }
		        }
		        
		    } catch (FileUploadException e) {
		        session.setAttribute("errorImagen", "Error al subir archivo de imagen. ¿Está seguro de haber seleccionado un archivo JPG para la subida?");
				request.getRequestDispatcher(Constantes.RUTA_FORMULARIO_IMAGEN_PRODUCTOS + "?op=subir").forward(request, response);
		        throw new ServletException("Cannot parse multipart request.", e);
		    }
			request.getRequestDispatcher(Constantes.RUTA_LISTADO_PRODUCTO).forward(request, response);
			}
	}
}


