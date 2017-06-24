<%@ include file="includes/cabecera.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<div>
		<h2>Formulario de usuarios</h2>
	</div>
	<jsp:useBean id="usuario" scope="request"
		class="catalogo.tipos.Usuario" />

	<form action="${applicationScope.rutaBase}/admin/usuarioform" method="post">
		<fieldset>
			<label for="username">Nombre</label> 
			
			<input id="username" name="username"
			  required="required" value="${usuario.username}" 
			  
			  <c:if test="${param.op == 'modificar' or param.op == 'borrar'}">
			  	readonly="readonly"
			  </c:if>   
		  	/>
		</fieldset>
		
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			
		>
			<label for="password">Contraseña</label> <input type="password" id="pass"
				name="password" required="required"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
		>
			<label for="password2">Contraseña otra vez</label> <input type="password" id="password2"
				name="password2" required="required"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			
		>
			<label for="nombre_completo">Nombre completo</label> <input id="nombre_completo"
				name="nombre_completo" required="required"/>
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
		>
			<label for="id_roles">Permiso de administrador</label> 
			<input type="checkbox" id="id_roles" name="id_roles" value="1" />
		</fieldset>
		<fieldset>
			<input type="submit" value="${fn:toUpperCase(param.op)}" 
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>
			/>
			<input type="hidden" name="opform" value="${param.op}" />
		</fieldset>
	</form>

<%@ include file="includes/pie.jsp" %>