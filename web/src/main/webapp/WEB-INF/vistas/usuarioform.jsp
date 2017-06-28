<%@ include file="includes/cabecera.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<jsp:useBean id="usuario" scope="request"
		class="catalogo.tipos.Usuario" />
	<div class="container">
		<h2>Formulario de usuarios</h2>
	</div>

<div class="container">
	<form action="${applicationScope.rutaBase}/admin/usuarioform" method="post">
		
		<fieldset style="display:none;">
			<label for="id">Id</label> 
			
			<input id="id" name="id" type="number" class="form-control"
			  required="required" value="${usuario.id}"/>
		</fieldset>
		<fieldset>
			<label for="username">Username</label> 
			
			<input id="username" name="username" class="form-control"
			  required="required" placeholder="Username" value="${usuario.username}" 
			  
			  <c:if test="${param.op == 'borrar'}">
			  	readonly="readonly"
			  </c:if>   
		  	/>
		</fieldset>
		
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			
		>
			<label for="password">Contraseņa</label> 
			<input type="password" id="pass" class="form-control"
				name="password" placeholder="Password"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
		>
			<label for="password2">Contraseņa otra vez</label> 
			<input type="password" id="password2" class="form-control"
				name="password2" placeholder="Password"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			
		>
			<label for="nombre_completo">Nombre completo</label> 
			<input id="nombre_completo" class="form-control"
				name="nombre_completo" placeholder="Nombre completo" value="${usuario.nombre_completo}"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
		>
			<label for="id_roles">Permiso de administrador</label> 
			<input type="checkbox" id="id_roles" name="id_roles" value="1" />
		</fieldset>
		<fieldset>
			<input type="submit" class="btn btn-default" value="${fn:toUpperCase(param.op)}" 
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>
			/>
			<input type="hidden" name="opform" value="${param.op}" />
		</fieldset>
	</form>
</div>
<%@ include file="includes/pie.jsp" %>