<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Su factura</title>
	<link rel="stylesheet" href="${applicationScope.rutaBase}/css/bootstrap.min.css"/>			
	<link rel="shortcut icon" href="${applicationScope.rutaBase}/img/favicon.png" type="image/png">
</head>
<body style="margin:10px">
<div id="factura" class="container">

<div class="row">
<div class="col-xs-5 col-md-5">Factura ${factura.numero_factura}</div><div class="col-xs-5 col-xs-offset-2 col-md-5 col-md-offset-2" style="text-align:right">${factura.fecha}</div>
</div>
<br>
<div class="row">
	<div class="col-xs-5 col-md-5">
		<address>
		  <strong>Driver, Inc.</strong><br>
		  c/Kalea 9, 9ºB<br>
		  Bilbao, BI 48080<br>
		  A-12345678<br>
		  <abbr title="Phone">P:</abbr> +34 666 666 666
		  <a href="mailto:#">facturacion@driver.com</a>
		</address>
	</div>
		
		
	<div class="col-xs-5 col-xs-offset-2 col-md-5 col-md-offset-2" style="text-align:right">
		<address>
			<strong>${usuario.nombre_completo}</strong><br>
			c/del Usuario s/n<br>
			Usuariolandia, US 98080<br>
			98765432-X<br>
		</address>
	</div>
</div>
</div>
	
	<div class="container">
		<div class="col-md-12">
			<div class="table-responsive">
				<table class="table table-hover">
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
								<td style="text-align: right">${producto.precio} €</td>
							</tr>
						</c:forEach>
						<tr>
							<td>IVA: </td>
							<td style="text-align:right">${sessionScope.ivaFactura} €</td>
						</tr>
						<tr>
							<td>Total: </td>
							<td style="text-align:right">${sessionScope.precioFactura} €</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>