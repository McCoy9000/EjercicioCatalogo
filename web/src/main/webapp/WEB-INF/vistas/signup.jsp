<%@ include file="includes/cabecera.jsp"%>
<div class="container">
	<h2>Alta</h2>
</div>
<div class="container">
<form action="alta" method="post">
<fieldset class="form-group">
<label for="username">Username</label>
<input id="username" class="form-control" name="username" placeholder="Username" maxlength="16"/>
</fieldset>
<fieldset class="form-group">
<label for="password">Contraseña</label>
<input id="password" class="form-control" name="password" type="password" placeholder="Password" maxlength="16"/>
</fieldset>
<fieldset class="form-group">
<label for="password2">Repita la contraseña</label>
<input id="password2" class="form-control" name="password2" type="password" placeholder="Password" maxlength="16"/>
</fieldset>
<fieldset class="form-group">
<label for="nombre_completo">Nombre completo</label>
<input id="nombre_completo" class="form-control" name="nombre_completo" placeholder="Nombre completo" maxlength="25"/>
</fieldset>
<fieldset class="form-group">
<input type="submit" class="btn btn-primary" value="ALTA">
</fieldset>
<p class="errores">${sessionScope.errorSignup }</p>
</form>
</div>


<%@ include file="includes/pie.jsp"%>