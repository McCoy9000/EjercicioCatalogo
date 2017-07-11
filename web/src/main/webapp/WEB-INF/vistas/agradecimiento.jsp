<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<html>
		<head>
			<meta charset="utf-8">
   			<meta http-equiv="X-UA-Compatible" content="IE=edge">
    		<meta name="viewport" content="width=device-width, initial-scale=1">
			<link rel="stylesheet" href="${applicationScope.rutaBase}/css/bootstrap.min.css" />
			<link rel="shortcut icon" href="${applicationScope.rutaBase}/img/favicon.png" type="image/png">
			<title>Gracias por comprar en Driver</title>
		</head>
		<body style="background:url(${applicationScope.rutaBase}/img/pit-race.jpg) 0px 0px/cover no-repeat fixed ; padding:50px">
		<div class="container">
			<h1 style="color:white"><strong>Gracias por comprar en Driver</strong></h1>
			<a class="btn btn-default" style="background:#ECC007; border:none;" href="${applicationScope.rutaBase}/factura?op=factura">Imprimir factura</a>
		</div>
			<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
   			<script src="js/bootstrap.min.js"></script>
		</body>
</html>