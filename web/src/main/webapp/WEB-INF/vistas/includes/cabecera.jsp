<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
	<html lang="en">
		<head>
			<meta charset="utf-8">
   			<meta http-equiv="X-UA-Compatible" content="IE=edge">
    		<meta name="viewport" content="width=device-width, initial-scale=1">
			<title>Driver</title>
			<link rel="shortcut icon" href="${applicationScope.rutaBase}/img/favicon.png" type="image/png">
			<link rel="stylesheet" href="${applicationScope.rutaBase}/css/datatables.min.css"/>
			<script src="${applicationScope.rutaBase}/js/datatables.min.js"></script>
			<script>
			$(document).ready( function () {
			    $('#catalogo').DataTable();
			} );
			</script>
			<script>
			$(document).ready( function () {
			    $('#productos').DataTable();
			} );
			</script>
			<script>
			$(document).ready( function () {
			    $('#usuarios').DataTable();
			} );
			</script>
			<link href="https://fonts.googleapis.com/css?family=Cabin+Sketch" rel="stylesheet">
		</head>
		
		<body style="padding-bottom:10px">
		<div id="img-pit" style="background:url(${applicationScope.rutaBase}/img/pit-race.jpg) 0px 0px/100% no-repeat fixed ; padding-bottom:10px">
			<header class="container">
				<h1 style="color:white; font-family: 'Cabin Sketch', cursive;font-size:25vw; line-height:80%; display:inline-block;"><strong>DRIVER</strong></h1>
				<p style="color:white; display:inline-block;"><strong>¡Bienvenido<span <c:if test="${sessionScope.usuario.username==null}">style="display:none"</c:if>> </span>${sessionScope.usuario.username}!</strong></p>
			</header>
			
			<nav class="container">
				<ul class="list-inline">
				 	 <li><a class="btn btn-default" href="${applicationScope.rutaBase}/catalogo">CATALOGO</a></li>
				 	 <li><a class="btn btn-primary" href="${applicationScope.rutaBase}/login">LOGIN</a></li>
					 <li><a class="btn btn-default" href="${applicationScope.rutaBase}/login?op=logout">LOGOUT</a></li>
					 
				</ul>
			</nav>
			<nav class="container" <c:if test="${sessionScope.usuario.id_roles != '1'}">
						style="display:none;"
					</c:if>>
			<div class="dropdown" style="display:inline-block">
				<button class="btn btn-default dropdown-toggle" type="button" id="dopdownProductos" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				PRODUCTOS
				<span class="caret"></span>
				</button>
				<ul class="dropdown-menu" aria-labelledby="dropdownProductos">
					<li><a href="${applicationScope.rutaBase}/admin/productocrud">Mantenimiento</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/productoform">Alta</a></li>
				</ul>
			</div>
			<div class="dropdown" style="display:inline-block">
				<button class="btn btn-default dropdown-toggle" type="button" id="dopdownUsuarios" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				USUARIOS
				<span class="caret"></span>
				</button>
				<ul class="dropdown-menu" aria-labelledby="dropdownUsuarios">
					<li><a href="${applicationScope.rutaBase}/admin/usuariocrud">Mantenimiento</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/usuarioform">Alta</a></li>
				</ul>
			</div>
			<div class="dropdown" style="display:inline-block">
				<button class="btn btn-default dropdown-toggle" type="button" id="dopdownFacturas" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				FACTURAS
				<span class="caret"></span>
				</button>
				<ul class="dropdown-menu" aria-labelledby="dropdownFacturas">
					<li><a href="${applicationScope.rutaBase}/admin/facturacrud">Mantenimiento</a></li>
					<!-- <li><a href="${applicationScope.rutaBase}/admin/facturaform">Alta</a></li> -->
				</ul>
			</div>
			</nav>
			
			
			</div>