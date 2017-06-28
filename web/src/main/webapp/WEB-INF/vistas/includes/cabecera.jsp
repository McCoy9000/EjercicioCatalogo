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
			<link rel="stylesheet" href="${applicationScope.rutaBase}/css/bootstrap.min.css"/>
		</head>
		
		<body>
	
			<header class="container">
				<h1>Driver</h1>
				<h3>Venta de coches de segunda mano</h3>
				<p class="bienvenido">Bienvenido ${sessionScope.usuario.username}</p>
			</header>
			
			<nav class="container">
				<ul class="list-inline">
				 	 <li><a class="btn btn-default" href="${applicationScope.rutaBase}/catalogo">CATALOGO</a></li>
				 	 <li><a class="btn btn-primary" href="${applicationScope.rutaBase}/login">LOGIN</a></li>
					 <li><a class="btn btn-default" href="${applicationScope.rutaBase}/login?op=logout">LOGOUT</a></li>
					 
				</ul>
				
				<ul class="list-inline" <c:if test="${sessionScope.usuario.id_roles != '1'}">
						style = "display:none"
					</c:if>
				>
					<li><a class="btn btn-default" href="${applicationScope.rutaBase}/admin/productocrud">MANTENIMIENTO DE PRODUCTOS</a></li>
					<li><a class="btn btn-default" href="${applicationScope.rutaBase}/admin/productoform?op=alta">ALTA DE PRODUCTOS</a></li>
					<li><a class="btn btn-default" href="${applicationScope.rutaBase}/admin/usuariocrud">MANTENIMIENTO DE USUARIOS</a></li>
					<li><a class="btn btn-default" href="${applicationScope.rutaBase}/admin/usuarioform?op=alta">ALTA DE USUARIOS</a></li>
				</ul>
			</nav>