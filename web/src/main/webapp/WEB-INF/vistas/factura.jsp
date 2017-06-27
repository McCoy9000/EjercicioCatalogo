<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
			<meta charset="utf-8">
   			<meta http-equiv="X-UA-Compatible" content="IE=edge">
    		<meta name="viewport" content="width=device-width, initial-scale=1">
			<title>Su factura</title>
			<link rel="stylesheet" href="${applicationScope.rutaBase}/css/bootstrap.min.css"/>			
</head>
<body>
<div id="factura">
<div>
<table>
	<thead>
	<tr>
		<th style="text-align: left">Factura ${factura.id}</th>
		<th style="text-align: right">${factura.fecha}</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td>Driver, S.A.</td>
		<td style="text-align: right">${usuario.nombre_completo}</td>
	</tr>
	<tr>
		<td>A-12345678</td>
		<td style="text-align: right">98765432-X</td>
	</tr>
	<tr>
		<td>c/ Kalea, 4 - 9ºB</td>
		<td style="text-align: right">c/ del Usuario s/n</td>
	</tr>
	</tbody>
</table>
</div>


<div>
<table>
	<thead>
		<tr>
			<th>Producto</th>
			<th style="text-align: right">Precio</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${sessionScope.productosFactura}" var="producto">
				<tr>
				<td>${producto.nombre}</td>
				<td style="text-align: right">${producto.precio} €</td>
				</tr>
		</c:forEach>
		
		<tr>
			<td>Total: </td>
			<td style="text-align: right">${sessionScope.precioFactura} €</td>
		</tr>
	</tbody>
</table>
</div>
</div>

</body>
</html>