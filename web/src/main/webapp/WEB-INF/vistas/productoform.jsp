<%@ include file="includes/cabecera.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div>
	<h2>Formulario de productos</h2>
</div>
	
	<jsp:useBean id="producto" scope="request"
		class="catalogo.tipos.Producto" />

	<form action="${applicationScope.rutaBase}/admin/productoform" method="post">
		<fieldset>
			<label for="id" 
			 <c:if test="${param.op == 'alta'}">
			  	style="display:none"
			  </c:if> >Id</label> 
			
			<input id="id" name="id"
			  required="required"  value="${producto.id}" 
			  
			  <c:if test="${param.op == 'modificar' or param.op == 'borrar'}">
			  	readonly="readonly"
			  </c:if> 
			  <c:if test="${param.op == 'alta'}">
			  	style="display:none"
			  </c:if>  
		  	/>
		</fieldset>
		<fieldset>
			<label for="groupId">GroupId</label> 
			
			<input id="groupId" name="groupId"
			  required="required"  value="${producto.groupId}"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			readonly="readonly"
			</c:if>>
			<label for="nombre">Nombre</label> <input id="nombre"
				name="nombre" value="${producto.nombre}"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			>
			<label for="descripcion">Descripcion</label> <input id="descripcion"
				name="descripcion" value="${producto.descripcion}"/>
		</fieldset>
		
		<!--<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			>
			<label for="imagen">Imagen</label> 
			<select name="imagen">
				<option value="0">Sin imagen</option>
				<option value="1">Ford Mustang</option>
				<option value="2">Chevrolet Eldorado</option>
				<option value="3">Dodge Charger</option>
				<option value="4">Dodge Challenger</option>
			</select>
		</fieldset>  -->
		
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			>
			<label for="precio">Precio</label> <input id="precio"
				name="precio" value="${producto.precio}"/>
		</fieldset>
		<fieldset>
			<input type="submit" value="${fn:toUpperCase(param.op)}" 
			
			
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>
			
			
			/>
			<p class="errores">${producto.errores}</p>
			
			<input type="hidden" name="opform" value="${param.op}" />
		</fieldset>
	</form>
	
	<!--<c:if test="${param.op == 'borrar'}">
		<script>
			document.forms[0].onsubmit = confirmarBorrado;
		</script>
	</c:if>-->
	
<%@ include file="includes/pie.jsp" %>