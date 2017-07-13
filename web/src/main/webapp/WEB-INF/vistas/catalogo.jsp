<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- <div class="container">
	<h2>Catálogo</h2>
</div> -->
<nav class="container" style="margin-top:2em">
	<ul class="list-inline">
	<li><a class="btn btn-default" style="background:#ECC007; border:#ECC007;" href="${applicationScope.rutaBase}/checkout"><strong>CHECKOUT &#128722; (${sessionScope.numeroProductos})</strong></a></li>
	</ul>
</nav>
<div class="container">
<div class="table-responsive">
<table id="catalogo" class="table">
	<thead>
		<tr>
			
			<th>Nombre producto</th>
			<th>Descripción</th>
			<th>Imagen</th>
			<th>Precio</th>
			<th>Stock</th>
			<th></th>
			
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${applicationScope.catalogo}" var="articulo">
			<tr id="${articulo.groupId}">
				<td style="vertical-align: middle;">${articulo.nombre}</td>
				<td style="vertical-align: middle;">${articulo.descripcion}</td>
				<td style="vertical-align: middle;"><object data="${applicationScope.rutaBase}/img/${articulo.imagen}.jpg" height="128" width="128" type="image/png">
					<img src="${applicationScope.rutaBase}/img/0.jpg" class="img-thumbnail" height="128" width="128"/></object></td>
				<td style="vertical-align: middle;">${articulo.precio} €</td>
				<td style="vertical-align: middle;">${articulo.cantidad}</td>
				<td id="anadir" style="text-align:center; vertical-align:middle;">
					<form action="${applicationScope.rutaBase}/catalogo#${articulo.groupId}" method="post">
						<input type="number" max="${articulo.cantidad}" min="1" id="cantidad" name="cantidad" value="1"/>
						<input type="hidden" id ="groupId" name="groupId" value="${articulo.groupId}"/>
						<input type="hidden" id="op" name="op" value="anadir"/>
						<input type="submit" class="btn btn-default" style="background:#ECC007; border:none;" value="AÑADIR"/>
					</form>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</div>
<%@ include file="includes/pie.jsp"%>
