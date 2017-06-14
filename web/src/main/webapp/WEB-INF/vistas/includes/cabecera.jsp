<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
	<html>
		<head>
			<meta charset="UTF-8" />
			<title>Driver</title>
			<link rel="stylesheet" href="${applicationScope.rutaBase}/css/estilos.css" />
		</head>
		
		<body>
	
			<header>
				<h1>Driver</h1>
				<h3>Venta de coches de segunda mano</h3>
				<p class="bienvenido">Bienvenido ${sessionScope.usuario.username}</p>
			</header>
			
			<nav>
				<ul>
				 	 <li><a href="${applicationScope.rutaBase}/catalogo">Catálogo</a></li>
				 	 <li><a href="${applicationScope.rutaBase}/login">Login</a></li>
					 <li><a href="${applicationScope.rutaBase}/login?op=logout">Logout</a></li>
					 
				</ul>
				
				<ul <c:if test="${sessionScope.usuario.id_roles != '1'}">
						style = "display:none"
					</c:if>
				>
					<li><a href="${applicationScope.rutaBase}/admin/productocrud">Mantenimiento de productos</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/productoform?op=alta">Alta de productos</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/usuariocrud">Mantenimiento de usuarios</a></li>
					<li><a href="${applicationScope.rutaBase}/admin/usuarioform?op=alta">Alta de usuarios</a></li>
				</ul>
			</nav>