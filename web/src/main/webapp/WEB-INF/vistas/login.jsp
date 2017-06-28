<%@ include file="includes/cabecera.jsp"%>

<div class="container">
	<h2>Login</h2>
</div>

<div class="container">
<form action="login" method="post">
	<fieldset>
		<label for="username">Username</label>
		<input id="username" name="username" placeholder="Username" maxlength="16"/>
	</fieldset>
	<fieldset>
		<label for="password">Password</label>
		<input id="password" name="password" type="password" placeholder="Password" maxlength="16"/>
		</fieldset>
	<fieldset>
		<input type="submit" class="btn btn-default" value="LOGIN"/>
	</fieldset>
</form>
</div>

<div class="container">
	<a class="btn btn-primary" href="${applicationScope.rutaBase}/alta">SIGN UP!</a>
</div>

<div class="container">
	<p class="errores">${sessionScope.errorLogin }</p>
</div>
<%@ include file="includes/pie.jsp"%>