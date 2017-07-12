<%@ include file="includes/cabecera.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<jsp:useBean id="usuario" scope="request"
		class="catalogo.tipos.Usuario" />
	
	<div class="container">
		<h2>Formulario de usuarios</h2>
	</div>

<div class="container">
<div class="row">
<div class="col-md-6">
	<form action="${applicationScope.rutaBase}/admin/usuarioform" method="post">
		
		<fieldset class="form-group" style="display:none;">
			<label for="id">Id</label> 
			
			<input id="id" name="id" type="number" class="form-control"
			  required="required" value="${usuario.id}"/>
		</fieldset>
		<fieldset class="form-group">
			<label for="username">Nombre de usuario <span style="color:red">*</span></label> 
			
			<input id="username" name="username" class="form-control"
			  required="required" placeholder="Nombre de usuario" value="${usuario.username}" 
			  
			  <c:if test="${param.op == 'borrar'}">
			  	readonly="readonly"
			  </c:if>   
		  	/>
		</fieldset>
		
		<fieldset class="form-group" <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			<c:if test="${param.op == 'alta' or param.op =='modificar'}">
			required="required"
			</c:if>
		>
			<label for="password">Contraseña <span style="color:red">*</span></label> 
			<input type="password" id="password" class="form-control"
				name="password" placeholder="Contraseña"/>
		</fieldset>
		<fieldset class="form-group" <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			<c:if test="${param.op == 'alta' or param.op =='modificar'}">
			required="required"
			</c:if>
		>
			<label for="password2">Repita la contraseña <span style="color:red">*</span></label> 
			<input type="password" id="password2" class="form-control"
				name="password2" placeholder="Contraseña"/>
		</fieldset>
		<fieldset class="form-group" <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			
		>
			<label for="nombre_completo">Nombre</label> 
			<input id="nombre_completo" class="form-control"
				name="nombre_completo" placeholder="Nombre" value="${usuario.nombre_completo}"/>
		</fieldset>
		<fieldset class="form-group" <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			
		>
			<label for="apellidos">Apellidos</label> 
			<input id="apellidos" class="form-control"
				name="apellidos" placeholder="Apellidos" value="${usuario.apellidos}"/>
		</fieldset>

		<fieldset class="form-group" <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
		>
			<label for="id_roles">Permiso de administrador</label> 
			<input type="checkbox" id="id_roles" name="id_roles" value="1" />
		</fieldset>
		<fieldset class="form-group">
			<input type="submit" class="btn btn-default" style="background:#ECC007; border:none;" value="${fn:toUpperCase(param.op)}" 
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>
			/>
			<input type="hidden" name="opform" value="${param.op}" />
		</fieldset>
	</form>
	<div class="alert alert-danger" role="alert" <c:if test="${sessionScope.errorUsuario==null}">style="display:none"</c:if>><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> ${sessionScope.errorUsuario}</div>
</div>
</div>
</div>
<%@ include file="includes/pie.jsp" %>