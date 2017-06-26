<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<h2>Catálogo</h2>
</div>
<nav>
	<ul>
	<li><a href="${applicationScope.rutaBase}/checkout">Checkout &#128722; (${sessionScope.numeroProductos})</a></li>
	</ul>
</nav>

<table>
	<thead>
		<tr>
			
			<th>Nombre producto</th>
			<th>Descripción</th>
			<th>Imagen</th>
			<th>Precio</th>
			<th>Stock</th>
			<th>Añadir</th>
			
			</tr>
	</thead>
	<tbody>
		<c:forEach items="${applicationScope.catalogo}" var="articulo">
			<tr id="${articulo.groupId}">
				<td>${articulo.nombre}</td>
				<td>${articulo.descripcion}</td>
				<td><object data="${applicationScope.rutaBase}/img/${articulo.imagen}.jpg" height="128" width="128" type="image/png">
					<img src="${applicationScope.rutaBase}/img/0.jpg" height="128" width="128"/></object></td>
				<td>${articulo.precio} €</td>
				<td>${articulo.cantidad}</td>
				<td><form action="${applicationScope.rutaBase}/catalogo#${articulo.groupId}" method="post">
					<input type="number" id="cantidad" name="cantidad" value="1"/>
					<input type="hidden" id ="groupId" name="groupId" value="${articulo.groupId}"/>
					<input type="hidden" id="op" name="op" value="anadir"/>
					<input type="submit" value="Añadir al carro"/>
					</form>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<%@ include file="includes/pie.jsp"%>
