<%@ include file="includes/cabecera.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
<h2>Mantenimiento de usuarios</h2>
</div>

<div class="container">
	<div class="table-responsive">
		<table class="table table:hover">
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
							<a class="btn btn-success" href="?op=modificar&id=${usuario.id}">Modificar</a>
							<a class="btn btn-danger" href="?op=borrar&id=${usuario.id}">Borrar</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<%@ include file="includes/pie.jsp"%>
