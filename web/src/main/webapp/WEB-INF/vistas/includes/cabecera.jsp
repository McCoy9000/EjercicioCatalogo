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
			<script>
			$(document).ready( function () {
			    $('#facturas').DataTable();
			} );
			</script>
			<link href="https://fonts.googleapis.com/css?family=Special+Elite" rel="stylesheet">
		</head>
		
		<body style="background:#F5F6F7; padding-bottom:10px">
			<div id="img-pit" style="background:url(${applicationScope.rutaBase}/img/tire-track1.jpg) 0px 0px/100% no-repeat fixed ; padding-bottom:10px">

			<header class="container">
	
				<div class="row">
				<div class="col-xs-10 col-md-11 col-lg-12">
				<h1 style="color:#ECC007; font-family:'Special Elite', sans-serif; font-size:19vw; display:inline-block; margin-top:50px;">DRIVER</h1>
				</div>

				</div>
				<div class="row">
				<p style="color:#ECC007; display:inline-block; margin-left:15px;"><strong>¡Bienvenido<span <c:if test="${sessionScope.usuario.username==null}">style="display:none"</c:if>> </span>${sessionScope.usuario.username}!</strong></p>
				</div>
			

			<div class="row">
				<div class="col-xs-1 col-md-1 col-lg-1">
				<div class="dropdown">
				<button class="btn btn-default dropdown-toggle" type="button" id="dropdownLogin" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" style="background:#ECC007; border:solid grey 1px;">
					<span class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu" aria-labelledby="dropdownLogin">
				<li><a class="btn btn-default" style="border:none;" href="${applicationScope.rutaBase}/login">LOGIN o ALTA</a></li>
				<li role="separator" class="divider"></li>
				<li><a class="btn btn-default" style="border:none;" href="${applicationScope.rutaBase}/catalogo">CATALOGO</a></li>
				<li role="separator" class="divider"></li>
				<li><a class="btn btn-default" style="border:none;" href="${applicationScope.rutaBase}/usuarioperfil?op=ver">MI PERFIL</a></li>
				<li role="separator" class="divider"></li>
				<li><a class="btn btn-default" style="border:none;" href="${applicationScope.rutaBase}/facturasusuario">MIS FACTURAS</a></li>
				<li role="separator" class="divider"></li>
				<li><a class="btn btn-default" style="border:none;" href="${applicationScope.rutaBase}/login?op=logout">CERRAR SESIÓN</a></li>
				</ul>
				</div>
				</div>
			
			
			<div class="col-xs-1 col-xs-offset-1 col-md-1 col-lg-1" <c:if test="${sessionScope.usuario.id_roles != '1'}">
						 style="display:none;"
					</c:if>>
				<div class="dropdown">
				<button class="btn btn-default dropdown-toggle" type="button" id="dopdownProductos" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" style="background:#ECC007; border:solid grey 1px;" >
				<strong>ADMINISTRACION</strong>
				<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu" aria-labelledby="dropdownLogin">
				<li class="dropdown-header">USUARIOS</li>
					<li><a href="${applicationScope.rutaBase}/admin/usuariocrud">Mantenimiento</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/usuarioform">Alta</a></li>
				<li role="separator" class="divider"></li>
				<li class="dropdown-header">PRODUCTOS</li>
					<li><a href="${applicationScope.rutaBase}/admin/productocrud">Mantenimiento</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/productoform">Alta</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/imagenproducto?op=subir">Subir imagen</a></li>
				<li role="separator" class="divider"></li>
				<li class="dropdown-header">FACTURAS</li>
					<li><a href="${applicationScope.rutaBase}/admin/facturacrud">Mantenimiento</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/facturacrud?op=devolucion">Devoluciones</a></li>
				</ul>
				</div>
				</div>
				

			
			</div>
			</header>
			</div>
			