<%@ include file="WEB-INF/vistas/includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container">
	<h2>Catálogo</h2>
</div>
<nav class="container">
	<ul class="list-inline">
	<li><a class="btn btn-primary" href="${applicationScope.rutaBase}/checkout">CHECKOUT &#128722; (${sessionScope.numeroProductos})</a></li>
	</ul>
</nav>
<div class="container">
<div class="table-responsive">
<table id="catalogo" class="table table-hover">
	<thead>
		<tr>
			
			<th>Nombre producto</th>
			<th>Descripción</th>
			<th>Imagen</th>
			<th>Precio</th>
			<th>Stock</th>
			<th>Añadir a mi carrito</th>
			
			</tr>
	</thead>
	<tbody>
		<c:forEach items="${applicationScope.catalogo}" var="articulo">
			<tr id="${articulo.groupId}">
				<td>${articulo.nombre}</td>
				<td>${articulo.descripcion}</td>
				<td><object data="${applicationScope.rutaBase}/img/${articulo.imagen}.jpg" height="128" width="128" type="image/png">
					<img src="${applicationScope.rutaBase}/img/0.jpg" class="img-thumbnail" height="128" width="128"/></object></td>
				<td>${articulo.precio} €</td>
				<td>${articulo.cantidad}</td>
				<td id="anadir" style="text-align:center;">
					<form action="${applicationScope.rutaBase}/catalogo#${articulo.groupId}" method="post">
						<input type="number" max="${articulo.cantidad}" min="1" id="cantidad" name="cantidad" value="1"/>
						<input type="hidden" id ="groupId" name="groupId" value="${articulo.groupId}"/>
						<input type="hidden" id="op" name="op" value="anadir"/>
						<input type="submit" class="btn btn-primary" value="AÑADIR"/>
					</form>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</div>
<%@ include file="WEB-INF/vistas/includes/pie.jsp"%>