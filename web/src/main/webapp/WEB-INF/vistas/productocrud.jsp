<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- <div class="container">
<h2>Mantenimiento de productos</h2>
</div> -->
<div class="container" style="margin-top:2em">
	<div class="table-responsive">
		<table id="productos" class="table">
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
						<td style="vertical-align: middle;">${producto.id}</td>
						<td style="vertical-align: middle;">${producto.groupId}</td>
						<td style="vertical-align: middle;">${producto.nombre}</td>
						<td style="vertical-align: middle;">${producto.descripcion}</td>
						<td style="vertical-align: middle;"><object data="${applicationScope.rutaBase}/img/${producto.imagen}.jpg" height="128" type="image/png">
							<img src="${applicationScope.rutaBase}/img/0.jpg" class="img-thumbnail" height="128" width="128"/></object></td>
						<td style="vertical-align: middle;">${producto.precio} €</td>
						<td style="vertical-align: middle;">
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
