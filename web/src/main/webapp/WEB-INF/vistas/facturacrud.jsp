<%@ include file="includes/cabecera.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="container" style="margin-top:2em">
	<div class="table-responsive">
		<table id="facturas" class="table">
			<thead>
				<tr>
					<th>Número de factura</th>
					<th>Usuario</th>
					<th>Fecha</th>
					<th></th>
					</tr>
			</thead>
			<tbody>
				<c:forEach items="${applicationScope.facturasArr}" var="factura">
					<tr>
						<td>${factura.numero_factura}</td>
						<td>${factura.usuario}</td>
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
