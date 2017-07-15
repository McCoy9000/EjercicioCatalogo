<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
   	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${sessionScope.producto.nombre}</title>
	<link rel="stylesheet" href="${applicationScope.rutaBase}/css/bootstrap.min.css"/>			
	<link rel="shortcut icon" href="${applicationScope.rutaBase}/img/favicon.png" type="image/png">
	<link href="https://fonts.googleapis.com/css?family=Special+Elite" rel="stylesheet">
	
</head>
<body style="background:#F5F6F7;">
<header class="container-fluid" style="background:white url(${applicationScope.rutaBase}/img/danger-stripes1.jpg) 0px 0px / 100%; padding:15px;">
</header>
<div id="producto" class="container-fluid">

<div class="row">
<div class="col-md-12"  style="background:url(${rutaBase}/img/${sessionScope.producto.groupId}.jpg) 10% no-repeat; vertical-align:bottom; padding-top:300px; margin:2em"><h1 style="color:#ECC007; font-family:'Special Elite', sans-serif; font-size:10vw; ">${sessionScope.producto.nombre}</h1></div>
</div>

<div class="row">
	<div class="col-xs-6 col-md-9"><p style="margin:2em">${producto.descripcion}</p>
	</div>
		
		
	<div class="col-xs-6 col-md-3" style="text-align:right">
	<div class="jumbotron"><h1 style="text-align:center; font-size:5vw;">${producto.precio}€</h1>
	</div></div>
</div>
</div>
<footer class="container-fluid" style="background:white url(${applicationScope.rutaBase}/img/danger-stripes1.jpg) 0px 0px / 100%; margin-top:15px; padding:15px;">
</footer>
</body>
</html>