<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
<h2>Mantenimiento de usuarios</h2>
</div>
<div class="container">
	<div class="table-responsive">
		<table id="usuarios" class="table">
			<thead>
				<tr>
					<th>Rol</th>
					<th>Nombre de usuario</th>
					<th>Contraseña</th>
					<th>Nombre completo</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${applicationScope.usuariosArr}" var="usuario">
					<tr>
						<td>${usuario.id_roles}</td>	
						<td>${usuario.username}</td>
						<td>${usuario.password}</td>
						<td>${usuario.nombre_completo}</td>
						<td>
							<a class="btn btn-success" href="${applicationScope.rutaBase}/admin/usuariocrud?op=modificar&id=${usuario.id}">Modificar</a>
							<a class="btn btn-danger" href="${applicationScope.rutaBase}/admin/usuariocrud?op=borrar&id=${usuario.id}">Borrar</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<%@ include file="includes/pie.jsp"%>
