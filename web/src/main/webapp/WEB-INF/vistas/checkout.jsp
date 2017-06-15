<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<h2>Checkout</h2>
</div>

<nav>
	<ul>
		<li>Total a pagar: ${sessionScope.precioTotal} €</li>
		<li><a href="?op=pagar">PAGAR ${sessionScope.numeroProductos} productos</a></li>
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
		<c:forEach items="${sessionScope.productosArr}" var="producto">
			<tr>
				<td>${producto.nombre}</td>
				<td>${producto.descripcion}</td>
				<td><img src="${applicationScope.rutaBase}/img/${producto.imagen}.jpg" height="128" width="128"/></td>
				<td>${producto.precio} €</td>
				<td><a href="?op=quitar&id=${producto.id}#catalogo">Quitar</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>







<%@ include file="includes/pie.jsp"%>