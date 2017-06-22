<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<html>
		<head>
			<meta charset="UTF-8"/>
			<link rel="stylesheet" href="${applicationScope.rutaBase}/css/estilos.css" />
			<title>Debes loguearte</title>
		</head>
		<body>
			<h1>Debes loguearte para poder finalizar tu compra</h1>
			<input type="button" value="Cerrar esta ventana" onclick="self.close()">
		</body>
</html>