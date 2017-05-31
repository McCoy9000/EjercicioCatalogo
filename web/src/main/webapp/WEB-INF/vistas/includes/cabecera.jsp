<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Euskaliexpress</title>
<link rel="stylesheet" href="/css/estilos.css" />
<script src="js/funciones.js"></script>
</head>
<body>
	<header>
		<h1>EuskAliExpress</h1>
		<p>Tu tienda para comprar cualquier producto</p>
		<p class="bienvenido">Bienvenido ${sessionScope.usuario.nombre}</p>
	</header>
	<nav>
		<ul>
		 	 <li><a href="/catalogo">Catálogo</a></li>
		 	 <li><a href="/login">Login</a></li>
			 <li><a href="/login?op=logout">Logout</a></li>
			 
		</ul>
		<ul <c:if test="${usuario.admin != 'true'}">
style = "display:none"
</c:if>>
		
		
			<li><a href="/admin/productocrud">Mantenimiento de productos</a></li>
			<li><a href="/admin/productoform?op=alta">Alta de productos</a></li>
				<!--<c:if test="${param.op == 'alta'}">
			  		style="display:none;"
			  	</c:if>-->
			  
			 <li><a href="/admin/usuariocrud">Mantenimiento de usuarios</a></li>
			 <li><a href="/admin/usuarioform?op=alta">Alta de usuarios</a></li>
		</ul>
	</nav>