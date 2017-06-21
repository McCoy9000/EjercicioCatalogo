package catalogo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/factura")
public class FacturaServlet extends HttpServlet {

	private static final long serialVersionUID = -374351075129665738L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String op = request.getParameter("op");

		if (op == null) {
			request.getRequestDispatcher("/WEB-INF/vistas/agradecimiento.jsp").forward(request, response);
		} else {
			switch (op) {
			case "factura":
				request.getRequestDispatcher("/WEB-INF/vistas/factura.jsp").forward(request, response);
				break;
			}
		}

	}
}
