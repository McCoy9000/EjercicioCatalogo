<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<html>
		<head>
			<meta charset="utf-8">
   			<meta http-equiv="X-UA-Compatible" content="IE=edge">
    		<meta name="viewport" content="width=device-width, initial-scale=1">
			<link rel="stylesheet" href="${applicationScope.rutaBase}/css/bootstrap.min.css" />
			<title>Debes loguearte</title>
		</head>
		<body>
		<div class="container">
			<h1>Debes loguearte para poder finalizar tu compra</h1>
			<input class="btn btn-primary" type="button" value="Cerrar esta ventana" onclick="self.close()">
		</div>
			<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    		<script src="js/bootstrap.min.js"></script>
		</body>
</html>