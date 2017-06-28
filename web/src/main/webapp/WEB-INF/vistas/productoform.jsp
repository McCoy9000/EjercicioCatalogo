<%@ include file="includes/cabecera.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container">
	<h2>Formulario de productos</h2>
</div>
	
	<jsp:useBean id="producto" scope="request"
		class="catalogo.tipos.Producto" />

<div class="container">
	<form action="${applicationScope.rutaBase}/admin/productoform" method="post">
		<fieldset class="form-group"
			<c:if test="${param.op == 'alta'}">
				style="display:none;"
			</c:if> 
		>
			<label for="id">Id</label> 
			
			<input id="id" name="id" type="number" class="form-control"
			  required="required"  value="${producto.id}" 
			  
				<c:if test="${param.op == 'modificar' or param.op == 'borrar'}">
					readonly="readonly"
			  	</c:if> 
		  	>
		</fieldset>
		<fieldset class="form-group" <c:if test="${param.op == 'borrar'}">
					style="display:none"
					</c:if>>
			<label for="groupId">GroupId</label> 
			<select id="groupId" name="groupId" class="form-control">
				<option <c:if test="${producto.groupId == '0'}">selected="selected"</c:if> >0</option>
				<option <c:if test="${producto.groupId == '1'}">selected="selected"</c:if> >1</option>
				<option <c:if test="${producto.groupId == '2'}">selected="selected"</c:if> >2</option>
				<option <c:if test="${producto.groupId == '3'}">selected="selected"</c:if> >3</option>
				<option <c:if test="${producto.groupId == '4'}">selected="selected"</c:if> >4</option>
			</select>
			<!-- <input <c:if test="${param.op == 'borrar'}">
					readonly="readonly"
					</c:if> id="groupId" name="groupId" type="number" min="0" class="form-control"
				required="required"  value="${producto.groupId}"/> -->
		</fieldset>
		<fieldset class="form-group">
			<label for="nombre">Nombre</label> 
			
			<input <c:if test="${param.op == 'borrar'}">
					readonly="readonly"
					</c:if> id="nombre" name="nombre" type="text" class="form-control"  
				required="required" placeholder="Nombre" value="${producto.nombre}"/>
		</fieldset>
		<fieldset class="form-group" 
			<c:if test="${param.op == 'borrar'}">
				style="display:none;"
			</c:if>
		>
			<label for="descripcion">Descripción</label> 
			
			<textarea rows="3" type="text" id="descripcion" name="descripcion" placeholder="Descripción"
				  class="form-control">${producto.descripcion}</textarea>
		</fieldset>
		
		<fieldset class="form-group" 
			<c:if test="${param.op == 'borrar'}">
				style="display:none;"
			</c:if>
		>
			<label for="precio">Precio</label>
			<div class="input-group"> 
			<div class="input-group-addon">€</div>
			<input id="precio" name="precio" placeholder="Precio" value="${producto.precio}"
				 min="0" class="form-control"/>
			</div>
		</fieldset>
		<fieldset class="form-group">
			<input type="submit" class="btn btn-default" value="${fn:toUpperCase(param.op)}"  
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>
			/>
			<p class="errores">${producto.errores}</p>
			
			<input type="hidden" name="opform" value="${param.op}" />
		</fieldset>
	</form>
</div>	

	
<%@ include file="includes/pie.jsp" %>