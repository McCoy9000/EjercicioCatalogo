<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<h2>Catálogo</h2>
</div>
<nav>
	<ul>
	<li>${sessionScope.numeroProductos} productos en el carrito</li>
	<li><a href="/checkout">Checkout</a></li>
	</ul>
</nav>

<table>
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
		<c:forEach items="${applicationScope.productosArr}" var="producto">
			<tr>
				<td>${producto.nombre}</td>
				<td>${producto.descripcion}</td>
				<td><img src="/img/${producto.imagen}.jpg" height="128" width="128"/></td>
				<td>${producto.precio} €</td>
				<td><a href="/catalogo?op=anadir&id=${producto.id}">Añadir al carro</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<%@ include file="includes/pie.jsp"%>
