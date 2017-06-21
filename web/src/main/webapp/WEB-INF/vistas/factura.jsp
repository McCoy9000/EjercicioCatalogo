<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Su factura</title>
</head>
<body>

<h1>Factura ${factura.id}</h1>
<h1>${factura.fecha}</h1>

<table>
	<thead>
		<tr>
			<th>Producto</th>
			<th>Precio</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${sessionScope.productosFactura}" var="producto">
				<tr>
				<td>${producto.nombre}</td>
				<td>${producto.precio} €</td>
				</tr>
		</c:forEach>
	</tbody>
</table>

<h2>Total: ${sessionScope.precioFactura}</h2>

</body>
</html>