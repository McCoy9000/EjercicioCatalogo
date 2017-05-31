<%@ include file="includes/cabecera.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
<h2>Mantenimiento de usuarios</h2>
</div>

<table>
	<thead>
		<tr>
			<th>Usuario</th>
			<th>Contraseña</th>
			<th>Administrador</th>
			<th>Operaciones</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${applicationScope.usuariosArr}" var="usuario">
			<tr>
				<td>${usuario.nombre}</td>
				<td>${usuario.pass}</td>
				<td>${usuario.admin}</td>
				<td>
					<a href="?op=modificar&id=${usuario.nombre}">Modificar</a>
					<a href="?op=borrar&id=${usuario.nombre}">Borrar</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>


<%@ include file="includes/pie.jsp"%>
