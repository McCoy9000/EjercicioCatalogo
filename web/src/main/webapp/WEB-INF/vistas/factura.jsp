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
		  <abbr title="Teléfono">T:</abbr> +34 666 666 666
		  <a href="mailto:#">facturacion@driver.com</a>
		</address>
	</div>
		
		
	<div class="col-xs-5 col-xs-offset-2 col-md-5 col-md-offset-2" style="text-align:right">
		<address>
			<strong>${sessionScope.usuarioFactura.nombre_completo} ${sessionScope.usuarioFactura.apellidos}</strong><br>
			${sessionScope.usuarioFactura.direccion}<br>
			${sessionScope.usuarioFactura.codigoPostal}<br>
			${sessionScope.usuarioFactura.region}, ${sessionScope.usuarioFactura.pais}<br>
			${sessionScope.usuarioFactura.documento}<br>
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
							<th style="width:28%"></th>
							<th>Producto</th>
							<th style="text-align:right">Precio</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${sessionScope.productosFactura}" var="producto">
							<tr>
								<td style="width:28%"></td>
								<td>${producto.nombre}</td>
								<td style="text-align:right">${producto.precio} €</td>
							</tr>
						</c:forEach>
						<tr>
							<td style="width:28%">IVA: </td>
							<td></td>
							<td style="text-align:right">${sessionScope.ivaFactura} €</td>
						</tr>
						<tr>
							<td style="width:28%"><strong>Total: </strong></td>
							<td></td>
							<td style="text-align:right"><strong>${sessionScope.precioFactura} €</strong></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>