<%@ include file="includes/cabecera.jsp"%>

<div class="container">
	<h2>Login</h2>
</div>

<div class="container">
<form action="login" method="post">
	<fieldset class="form-group">
		<label for="username">Nombre de usuario</label>
		<input id="username" class="form-control" name="username" placeholder="Nombre de usuario" maxlength="16" required="required"/>
	</fieldset>
	<fieldset class="form-group">
		<label for="password">Contraseña</label>
		<input id="password" class="form-control" name="password" type="password" placeholder="Contraseña" maxlength="25" required="required"/>
		</fieldset>
	<fieldset class="form-group">
		<input type="submit" class="btn btn-default" value="LOGIN"/>
	</fieldset>
</form>
</div>

<div class="container">
	<a class="btn btn-primary" href="${applicationScope.rutaBase}/alta">¡ALTA!</a>
</div>

<div class="container">
	<div class="bg-danger" <c:if test="${sessionScope.errorLogin==null}">style="display:none"</c:if>><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>${sessionScope.errorLogin}</div>
</div>
<%@ include file="includes/pie.jsp"%>