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
<div>
<table>
	<tr>
		<td>Factura ${factura.id}</td>
		<td>${factura.fecha}</td>
	</tr>
	<tr>
		<td>Driver, S.A.</td>
		<td>${usuario.nombre_completo}</td>
	</tr>
	<tr>
		<td>A-12345678</td>
		<td>98765432-X</td>
	</tr>
	<tr>
		<td>c/ Kalea, 4 - 9ºB</td>
		<td>c/ del Usuario</td>
	</tr>
	
</table>
</div>
<div>
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
				<td>${producto.precio / 1.21} €</td>
				</tr>
		</c:forEach>
		<tr>
			<td>I.V.A.</td>
			<td>${sessionScope.precioFactura - (sessionScope.precioFactura / 1.21)} €</td>
		</tr>
		<tr>
			<td>Total: </td>
			<td>${sessionScope.precioFactura} €</td>
		</tr>
	</tbody>
</table>
</div>


</body>
</html>