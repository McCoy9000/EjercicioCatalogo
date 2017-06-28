<%@ include file="includes/cabecera.jsp"%>

<div class="container">
	<h2>Login</h2>
</div>

<div class="container">
<form action="login" method="post">
	<fieldset class="form-group">
		<label for="username">Username</label>
		<input id="username" class="form-control" name="username" placeholder="Username" maxlength="16"/>
	</fieldset>
	<fieldset class="form-group">
		<label for="password">Contraseña</label>
		<input id="password" class="form-control" name="password" type="password" placeholder="Contraseña" maxlength="25"/>
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
	<p class="errores">${sessionScope.errorLogin }</p>
</div>
<%@ include file="includes/pie.jsp"%>