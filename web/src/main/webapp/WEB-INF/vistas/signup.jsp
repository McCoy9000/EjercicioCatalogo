<%@ include file="includes/cabecera.jsp"%>
<div>
	<h2>Alta</h2>
</div>

<form action="alta" method="post">
<fieldset>
<label for="username">Username</label>
<input id="username" name="username" placeholder="Username" maxlength="16"/>
</fieldset>
<fieldset>
<label for="password">Password</label>
<input id="password" name="password" type="password" placeholder="Password" maxlength="16"/>
</fieldset>
<fieldset>
<label for="password2">Repita el password</label>
<input id="password2" name="password2" type="password" placeholder="Password" maxlength="16"/>
</fieldset>
<fieldset>
<label for="nombre_completo">Nombre completo</label>
<input id="nombre_completo" name="nombre_completo" placeholder="Nombre completo" maxlength="25"/>
</fieldset>
<fieldset>
<input type="submit" value="ALTA">
</fieldset>
</form>
<div>
<p class="errores">${sessionScope.errorSignup }</p>
</div>
<%@ include file="includes/pie.jsp"%>