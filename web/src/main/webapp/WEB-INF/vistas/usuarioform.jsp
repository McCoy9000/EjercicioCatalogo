<%@ include file="includes/cabecera.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<div>
		<h2>Formulario de usuarios</h2>
	</div>
	<jsp:useBean id="usuario" scope="request"
		class="catalogo.tipos.Usuario" />

	<form action="/admin/usuarioform" method="post">
		<fieldset>
			<label for="nombre">Nombre</label> 
			
			<input id="nombre" name="nombre"
			  required="required" value="${usuario.nombre}" 
			  
			  <c:if test="${param.op == 'modificar' or param.op == 'borrar'}">
			  	readonly="readonly"
			  </c:if>   
		  	/>
		</fieldset>
		
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
			
		>
			<label for="pass">Contraseña</label> <input type="password" id="pass"
				name="pass" />
		</fieldset>
		<fieldset <c:if test="${param.op == 'borrar'}">
			style="display:none;"
			</c:if>
		>
			<label for="pass2">Contraseña otra vez</label> <input type="password" id="pass2"
				name="pass2" />
		</fieldset>
		<fieldset>
			<input type="submit" value="${fn:toUpperCase(param.op)}" 
			
			
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>
			
			
			/>
			<p class="errores">${usuario.errores}</p>
			
			<input type="hidden" name="opform" value="${param.op}" />
		</fieldset>
	</form>
	
	<!--<c:if test="${param.op == 'borrar'}">
		<script>
			document.forms[0].onsubmit = confirmarBorrado;
		</script>
	</c:if>-->
	
<%@ include file="includes/pie.jsp" %>