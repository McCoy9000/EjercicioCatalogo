<%@ include file="includes/cabecera.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="producto" scope="request"
		class="catalogo.tipos.Producto" />

<div class="container" style="margin-top:2em">
<div class="row">
<div class="col-md-6">
	<form action="${applicationScope.rutaBase}/admin/imagenproducto" method="post" enctype="multipart/form-data">
		
		<fieldset class="form-group" 
			<c:if test="${param.op == 'borrar'}">
				style="display:none"
			</c:if>>
			<label for="groupId">Producto</label> 
			<select id="groupId" name="groupId" class="form-control">
				
				<c:forEach items="${applicationScope.catalogo}" var="grupo">
				<option <c:if test="${producto.groupId == grupo.groupId}">selected="selected"</c:if>>${grupo.groupId} - ${grupo.nombre}</option>
				</c:forEach>
				
			</select>
		</fieldset>
		<fieldset class="form-group">
			<label for="imagen">Imagen</label> 
			
			<input id="imagen" name="imagen" type="file"/>
			<p class="help-block">Seleccione una imagen JPG</p>		
		</fieldset>
		
		<fieldset class="form-group">
			<input type="submit" class="btn btn-default" style="background:#ECC007; border:none;" value="${fn:toUpperCase(param.op)}"  
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>/>
			
			

		</fieldset>
		
	</form>
	<div class="alert alert-danger" role="alert" <c:if test="${sessionScope.errorImagen==null}">style="display:none"</c:if>><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> ${sessionScope.errorImagen}</div>
	</div>
	
	<div class="col-md-6" style="color:grey;">
	<p>Tras modificar la imagen de un producto ya existente, es posible que su navegador no actualice inmediatamente a la nueva imagen.</p><p>Para verla, pulse F12 y, a continuación, botón derecho del ratón sobre el botón de refresco de página 
	del navegador y elija "Vaciar caché y recargar de manera forzada". Después, pulse de nuevo F12 para cerrar la consola de desarrollador.</p>
	</div>
	</div>
	
</div>	

	
<%@ include file="includes/pie.jsp" %>