<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<h2>Checkout</h2>
</div>

<nav>
	<ul>
		<li><a onclick="window.open('?op=pagar', '_self'); window.open('${applicationScope.rutaBase}/factura', '_blank', 'width=400,height=400')">PAGAR ${sessionScope.numeroProductos} productos</a></li>
		<li>Total a pagar: ${sessionScope.precioTotal} € IVA incl.</li>
	</ul>
</nav>

<table id="catalogo">
	<thead>
		<tr>
			
			<th>Nombre producto</th>
			<th>Descripción</th>
			<th>Imagen</th>
			<th>Precio</th>
			<th>Acciones</th>
			
		</tr>
	</thead>
	
	<tbody>
		<c:forEach items="${sessionScope.productosCarritoArr}" var="producto">
			<tr id="carrito">
				<td>${producto.nombre}</td>
				<td>${producto.descripcion}</td>
				<td><img src="${applicationScope.rutaBase}/img/${producto.imagen}.jpg" height="128" width="128"/></td>
				<td>${producto.precio} €</td>
				<td><a href="?op=quitar&id=${producto.id}#carrito">Quitar</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>







<%@ include file="includes/pie.jsp"%>