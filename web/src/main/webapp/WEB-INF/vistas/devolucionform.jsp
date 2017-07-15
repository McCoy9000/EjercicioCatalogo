<%@ include file="includes/cabecera.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<div class="container" style="margin-top:2em">
<div class="row">
<div class="col-md-6">
	<form action="${applicationScope.rutaBase}/admin/devolucionform" method="post">
		
		
		
		<fieldset class="form-group">
			<label for="mensaje">Mensaje</label> 
			
			<textarea rows="3" id="mensaje" name="mensaje" placeholder="mensaje"
				  class="form-control"></textarea>
		</fieldset>
		<fieldset class="form-group">
			<label for="importe">Importe</label>
			<div class="input-group"> 
			<div class="input-group-addon">€</div>
			<input type="number" id="importe" name="importe" placeholder="importe"
				 max="0" class="form-control"/>
			</div>
		</fieldset>
		
		
		<fieldset class="form-group">
			<input type="submit" class="btn btn-default" style="background:#ECC007; border:none;" value="${fn:toUpperCase(param.op)}"  
				<c:if test="${param.op == null or param.op == ''}">
			  		style="display:none;"
			  	</c:if>/>
		</fieldset>
	<input type="hidden" name="opform" value="${param.op}" />	
	</form>
	<div class="alert alert-danger" role="alert" <c:if test="${sessionScope.errorDevolucion==null}">style="display:none"</c:if>><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> ${sessionScope.errorDevolucion}</div>
	</div>
	
	</div>
	
</div>	

	
<%@ include file="includes/pie.jsp" %>