<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="container" style="margin-top:2em">
	<ul class="list-inline">
		<li><a class="btn btn-default" style="background:#ECC007; border:none;" href="?op=vaciarcarrito"><strong>Vaciar carrito</strong></a>
		<li><a class="btn btn-default" style="background:#ECC007; border:none;" onclick="window.open('?op=pagar', '_self'); window.open('${applicationScope.rutaBase}/factura', '_blank', 'width=500,height=400')"><strong>PAGAR ${sessionScope.numeroProductos} producto<span <c:if test="${sessionScope.numeroProductos==1}">style="display:none;"</c:if>>s</span></strong></a></li>
		<li class="bg-success" style="padding:5px"><strong>Total a pagar:</strong> ${sessionScope.precioTotal} € IVA incl.</li>
	</ul>
</nav>
<div class="container">
<div class="table-responsive">
<table class="table" id="checkout">
	<thead>
		<tr>
			
			<th style="text-align:center">Nombre producto</th>
			<th style="text-align:center">Imagen</th>
			<th style="text-align:center">Precio</th>
			<th style="text-align:center">Cantidad</th>
			<th style="text-align:center"></th>
			
		</tr>
	</thead>
	
	<tbody>
		<c:forEach items="${sessionScope.articulosCarritoArr}" var="producto">
			<tr id="carrito">
				<td style="text-align:center; vertical-align: middle;">${producto.nombre}</td>
				<td style="text-align:center; vertical-align: middle;"><img src="${applicationScope.rutaBase}/img/${producto.imagen}.jpg" height="128"/></td>
				<td style="text-align:center; vertical-align: middle;">${producto.precio} €</td>
				<td style="text-align:center; vertical-align: middle;">${producto.cantidad}</td>
				<td style="text-align:center; vertical-align: middle;"><a class="btn btn-default" href="?op=quitar&id=${producto.id}#carrito">Quitar</a></td>
			</tr>
		</c:forEach>
			<tr id="total">
				<td style="text-align:center"></td>
				<th style="text-align:right;">TOTAL</th>
				<td style="text-align:center">${sessionScope.precioTotal} €</td>
				<td style="text-align:center"></td>
				<td></td>
			</tr>
	</tbody>
</table>
</div>
</div>






<%@ include file="includes/pie.jsp"%>