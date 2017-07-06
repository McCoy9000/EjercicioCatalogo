<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
	<h2>Mantenimiento de productos</h2>
</div>
<div class="container">
	<div class="table-responsive">
		<table id="productos" class="table table-hover">
			<thead>
				<tr>
					<th>Id</th>
					<th>Grupo de productos</th>
					<th>Nombre producto</th>
					<th>Descripción</th>
					<th>Imagen</th>
					<th>Precio</th>
					<th></th>
					</tr>
			</thead>
			<tbody>
				<c:forEach items="${applicationScope.productosArr}" var="producto">
					<tr>
						<td>${producto.id}</td>
						<td>${producto.groupId}</td>
						<td>${producto.nombre}</td>
						<td>${producto.descripcion}</td>
						<td><object data="${applicationScope.rutaBase}/img/${producto.imagen}.jpg" height="128" width="128" type="image/png">
							<img src="${applicationScope.rutaBase}/img/0.jpg" class="img-thumbnail" height="128" width="128"/></object></td>
						<td>${producto.precio} €</td>
						<td>
							<a class="btn btn-success" href="${applicationScope.rutaBase}/admin/productocrud?op=modificar&id=${producto.id}">Modificar</a>
							<a class="btn btn-danger" href="${applicationScope.rutaBase}/admin/productocrud?op=borrar&id=${producto.id}">Borrar</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
</div>
<%@ include file="includes/pie.jsp"%>
