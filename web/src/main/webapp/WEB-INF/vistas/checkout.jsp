<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
	<h2>Checkout</h2>
</div>

<nav class="container">
	<ul class="list-inline">
		<li><a class="btn btn-default" href="?op=vaciarcarrito">Vaciar carrito</a>
		<li><a class="btn btn-primary" onclick="window.open('?op=pagar', '_self'); window.open('${applicationScope.rutaBase}/factura', '_blank', 'width=400,height=400')">PAGAR ${sessionScope.numeroProductos} producto<span <c:if test="${sessionScope.numeroProductos==1}">style="display:none;"</c:if>>s</span></a></li>
		<li>Total a pagar: ${sessionScope.precioTotal} € IVA incl.</li>
	</ul>
</nav>
<div class="container">
<div class="table-responsive">
<table class="table table-hover" id="catalogo">
	<thead>
		<tr>
			
			<th style="text-align:center">Nombre producto</th>
			<th style="text-align:center">Descripción</th>
			<th style="text-align:center">Imagen</th>
			<th style="text-align:center">Precio</th>
			<th style="text-align:center">Acción</th>
			
		</tr>
	</thead>
	
	<tbody>
		<c:forEach items="${sessionScope.productosCarritoArr}" var="producto">
			<tr id="carrito">
				<td style="text-align:center; vertical-align: middle;">${producto.nombre}</td>
				<td style="text-align:center; vertical-align: middle;">${producto.descripcion}</td>
				<td style="text-align:center; vertical-align: middle;"><img src="${applicationScope.rutaBase}/img/${producto.imagen}.jpg" height="128" width="128"/></td>
				<td style="text-align:center; vertical-align: middle;">${producto.precio} €</td>
				<td style="text-align:center; vertical-align: middle;"><a class="btn btn-default" href="?op=quitar&id=${producto.id}#carrito">Quitar</a></td>
			</tr>
		</c:forEach>
			<tr id="total">
				<td style="text-align:center"></td>
				<td style="text-align:center"></td>
				<th style="text-align:right;">TOTAL</th>
				<td style="text-align:center">${sessionScope.precioTotal} €</td>
				<td style="text-align:center"></td>
			</tr>
	</tbody>
</table>
</div>
</div>






<%@ include file="includes/pie.jsp"%>