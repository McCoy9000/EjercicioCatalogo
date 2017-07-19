<%@ include file="includes/cabecera.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<div class="container" style="margin-top:2em">
<div class="row">
<div class="col-md-6">
	<form action="${applicationScope.rutaBase}/usuarioperfil" method="post">
		<fieldset class="form-group" style="display:none;">
			<label for="id">Id</label> 
			
			<input id="id" name="id" type="number" class="form-control"
			  required="required"  value="${sessionScope.usuarioMascara.id}" readonly="readonly">
		</fieldset>
		
		<fieldset class="form-group">
			<label for="nombre_completo">Nombre</label> 
			
			<input id="nombre_completo" name="nombre_completo" type="text" class="form-control"  
				placeholder="Nombre" value="${sessionScope.usuarioMascara.nombre_completo}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="apellidos">Apellidos</label> 
			
			<input id="apellidos" name="apellidos" type="text" class="form-control"  
				placeholder="Apellidos" value="${sessionScope.usuarioMascara.apellidos}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="username">Nombre de usuario</label> 
			
			<input id="username" name="username" type="text" class="form-control"  
				required="required" placeholder="Username" value="${sessionScope.usuarioMascara.username}"/>
		</fieldset>
		
		<fieldset class="form-group">
			<label for="rawpassword">Contraseña</label> 
			
			<input id="rawpassword" name="rawpassword" type="password" class="form-control"  
				required="required" placeholder="Contraseña"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="rawpassword2">Repita la contraseña</label> 
			
			<input id="rawpassword2" name="rawpassword2" type="password" class="form-control"  
				required="required" placeholder="Repita la contraseña"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="documento">Documento de identidad</label> 
			
			<input id="documento" name="documento" type="text" class="form-control"  
				placeholder="Documento de identidad" value="${sessionScope.usuarioMascara.documento}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="telefono">Teléfono</label> 
			
			<input id="telefono" name="telefono" type="text" class="form-control"  
				placeholder="Teléfono" value="${sessionScope.usuarioMascara.telefono}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="direccion">Dirección</label> 
			
			<input id="direccion" name="direccion" type="text" class="form-control"  
				placeholder="Dirección" value="${sessionScope.usuarioMascara.direccion}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="codigoPostal">Código postal</label> 
			
			<input id="codigoPostal" name="codigoPostal" type="text" class="form-control"  
				placeholder="Código postal" value="${sessionScope.usuarioMascara.codigoPostal}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="ciudad">Ciudad</label> 
			
			<input id="ciudad" name="ciudad" type="text" class="form-control"  
				placeholder="Ciudad" value="${sessionScope.usuarioMascara.ciudad}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="region">Región</label> 
			
			<input id="region" name="region" type="text" class="form-control"  
				placeholder="Región" value="${sessionScope.usuarioMascara.region}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="pais">País</label> 
			
			<input id="pais" name="pais" type="text" class="form-control"  
				placeholder="País" value="${sessionScope.usuarioMascara.pais}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="empresa">Empresa</label> 
			
			<input id="empresa" name="empresa" type="text" class="form-control"  
				placeholder="Empresa" value="${sessionScope.usuarioMascara.empresa}"/>
		</fieldset>
		
		<fieldset class="form-group">
			<input type="submit" class="btn btn-default" style="background:#ECC007; border:none;" value="MODIFICAR"  
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>/>
			
			
			<input type="hidden" name="opform" value="modificar" />
		</fieldset>
		
	</form>
	<div class="alert alert-danger" role="alert" <c:if test="${sessionScope.errorPerfil==null}">style="display:none"</c:if>><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> ${sessionScope.errorPerfil}</div>
	</div>

	</div>
	
</div>	

	
<%@ include file="includes/pie.jsp" %>