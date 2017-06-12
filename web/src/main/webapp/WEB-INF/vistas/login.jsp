<%@ include file="includes/cabecera.jsp"%>

<div>
	<h2>Login</h2>
</div>


<form action="login" method="post">
	<fieldset>
		<label for="username">Username</label>
		<input id="username" name="username"/>
	</fieldset>
	<fieldset>
		<label for="password">Password</label>
		<input id="password" name="password" type="password"/>
		</fieldset>
	<fieldset>
		<input type="submit" value="LOGIN"/>
	</fieldset>
</form>

<div class="signup">
	<a href="${applicationScope.rutaBase}/alta">SIGN UP!</a>
</div>

<div>
	<p class="errores">${sessionScope.errorLogin }</p>
</div>
<%@ include file="includes/pie.jsp"%>