<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
	<h2>Mantenimiento de facturas</h2>
</div>
<div class="container">
	<div class="table-responsive">
		<table id="facturas" class="table table-hover">
			<thead>
				<tr>
					<th>Id</th>
					<th>Número de factura</th>
					<th>Id de usuario</th>
					<th>Fecha</th>
					<th></th>
					</tr>
			</thead>
			<tbody>
				<c:forEach items="${applicationScope.facturasArr}" var="factura">
					<tr>
						<td>${factura.id}</td>
						<td>${factura.numero_factura}</td>
						<td>${factura.id_usuarios}</td>
						<td>${factura.fecha}</td>
						<td>
							<a class="btn btn-success" onclick="window.open('${applicationScope.rutaBase}/admin/facturacrud?op=ver&id=${factura.id}', '_blank', 'width=400,height=400')">Ver</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
</div>
<%@ include file="includes/pie.jsp"%>
