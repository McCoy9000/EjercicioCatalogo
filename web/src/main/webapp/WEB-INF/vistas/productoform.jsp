<%@ include file="includes/cabecera.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- <div class="container">
	<h2>Formulario de productos</h2>
</div> -->
	
	<jsp:useBean id="producto" scope="request"
		class="catalogo.tipos.Producto" />

<div class="container" style="margin-top:2em">
<div class="row">
<div class="col-md-6">
	<form action="${applicationScope.rutaBase}/admin/productoform" method="post">
		<fieldset class="form-group" style="display:none;">
			<label for="id">Id</label> 
			
			<input id="id" name="id" type="number" class="form-control"
			  required="required"  value="${producto.id}" readonly="readonly">
		</fieldset>
		<fieldset class="form-group" 
			<c:if test="${param.op == 'borrar'}">
				style="display:none"
			</c:if>>
			<label for="groupId">Grupo de productos <span style="color:red">*</span></label> 
			<select id="groupId" name="groupId" class="form-control" 
				<c:if test="${param.op == 'modificar' or param.op == 'borrar'}">
					disabled="disabled"
			  	</c:if> 
			>
				<option>Nuevo grupo de productos</option>
				<c:forEach items="${applicationScope.catalogo}" var="grupo">
				<option <c:if test="${producto.groupId == grupo.groupId}">selected="selected"</c:if>>${grupo.groupId} - ${grupo.nombre}</option>
				</c:forEach>
				
			</select>
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
			</c:if>>
			<label for="descripcion">Descripción</label> 
			
			<textarea rows="3" id="descripcion" name="descripcion" placeholder="Descripción"
				  class="form-control">${producto.descripcion}</textarea>
		</fieldset>
		<fieldset class="form-group" 
			<c:if test="${param.op == 'borrar'}">
				style="display:none;"
			</c:if>>
			<label for="precio">Precio</label>
			<div class="input-group"> 
			<div class="input-group-addon">€</div>
			<input id="precio" name="precio" placeholder="Precio" value="${producto.precio}"
				 min="0" class="form-control"/>
			</div>
		</fieldset>
		<fieldset class="form-group"
			<c:if test="${param.op == 'borrar' or param.op == 'modificar'}">
				style="display:none;"
			</c:if>>
			<label for="cantidad">Cantidad</label>
			<div class="input-group">
			<input type="number" id="cantidad" name="cantidad" value="1"
				min="1" class="form-control"/>
			</div>
		</fieldset>
		
		<fieldset class="form-group">
			<input type="submit" class="btn btn-default" style="background:#ECC007; border:none;" value="${fn:toUpperCase(param.op)}"  
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>/>
			
			
			<input type="hidden" name="opform" value="${param.op}" />
		</fieldset>
		
	</form>
	<div class="alert alert-danger" role="alert" <c:if test="${sessionScope.errorProducto==null}">style="display:none"</c:if>><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> ${sessionScope.errorProducto}</div>
	</div>
	<div class="col-md-6" style="color:grey">
	<p>Seleccione un grupo de productos y una cantidad para dar de alta más productos dentro de un grupo.</p>
	<p>Seleccione "Nuevo grupo de productos" e introduzca un nombre, descripción, precio y cantidad para dar de 
	alta productos de un grupo aún no existente.</p>
	</div>
	</div>
	
</div>	

	
<%@ include file="includes/pie.jsp" %>