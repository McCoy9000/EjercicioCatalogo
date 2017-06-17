<%@ include file="includes/cabecera.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
<h2>Mantenimiento de usuarios</h2>
</div>

<table>
	<thead>
		<tr>
			<th>Rol</th>
			<th>Username</th>
			<th>Contraseña</th>
			<th>Nombre completo</th>
			<th>Operaciones</th>
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
					<a href="?op=modificar&username=${usuario.username}">Modificar</a>
					<a href="?op=borrar&username=${usuario.username}">Borrar</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>


<%@ include file="includes/pie.jsp"%>
