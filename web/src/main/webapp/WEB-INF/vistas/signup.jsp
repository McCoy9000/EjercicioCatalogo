<%@ include file="includes/cabecera.jsp"%>
<div class="container">
	<h2>Alta</h2>
</div>
<div class="container">
<div class="row">
<div class="col-md-6">
<form action="alta" method="post">
<fieldset class="form-group">
<label for="username">Nombre de usuario</label>
<input id="username" class="form-control" name="username" placeholder="Nombre de usuario" maxlength="16" required="required"/>
</fieldset>
<fieldset class="form-group">
<label for="password">Contraseņa</label>
<input id="password" class="form-control" name="password" type="password" placeholder="Contraseņa" maxlength="16" required="required"/>
</fieldset>
<fieldset class="form-group">
<label for="password2">Repita la contraseņa</label>
<input id="password2" class="form-control" name="password2" type="password" placeholder="Contraseņa" maxlength="16" required="required"/>
</fieldset>
<fieldset class="form-group">
<label for="nombre_completo">Nombre completo</label>
<input id="nombre_completo" class="form-control" name="nombre_completo" placeholder="Nombre completo" maxlength="25"/>
</fieldset>
<fieldset class="form-group">
<input type="submit" class="btn btn-default" style="background:#ECC007; border:none;"  value="ALTA">
</fieldset>
</form>
<div id="error" class="alert alert-danger" role="alert" <c:if test="${sessionScope.errorSignup==null}">style="display:none"</c:if>><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> ${sessionScope.errorSignup}</div>
</div>
</div>
</div>


<%@ include file="includes/pie.jsp"%>