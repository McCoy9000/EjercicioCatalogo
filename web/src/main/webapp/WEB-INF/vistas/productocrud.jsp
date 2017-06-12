<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<h2>Mantenimiento de productos</h2>
</div>

<table>
	<thead>
		<tr>
			<th>Id</th>
			<th>GroupId</th>
			<th>Nombre producto</th>
			<th>Descripción</th>
			<th>Imagen</th>
			<th>Precio</th>
			<th>Operaciones</th>
			</tr>
	</thead>
	<tbody>
		<c:forEach items="${applicationScope.productosArr}" var="producto">
			<tr>
				<td>${producto.id}</td>
				<td>${producto.groupId}</td>
				<td>${producto.nombre}</td>
				<td>${producto.descripcion}</td>
				<td><img src="/img/${producto.imagen}.jpg" height="128" width="128"/></td>
				<td>${producto.precio} €</td>
				<td>
					<a href="?op=modificar&nombre=${producto.nombre}">Modificar</a>
					<a href="?op=borrar&nombre=${producto.nombre}">Borrar</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<%@ include file="includes/pie.jsp"%>
