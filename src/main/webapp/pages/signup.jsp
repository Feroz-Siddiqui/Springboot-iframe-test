<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!doctype html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author"
	content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
<meta name="generator" content="Hugo 0.84.0">
<title>Login here</title>



<link href="<c:url value='/app_resources/bootstrap.min.css' />"
	rel="stylesheet" />
<link href="<c:url value='/app_resources/dropzone.css' />"
	rel="stylesheet" />
<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}

html, body {
	height: 100%;
}

body {
	display: flex;
	align-items: center;
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #f5f5f5;
}

.form-signin {
	width: 100%;
	max-width: 330px;
	padding: 15px;
	margin: auto;
}

.form-signin .checkbox {
	font-weight: 400;
}

.form-signin .form-floating:focus-within {
	z-index: 2;
}

.form-signin input[type="email"] {
	margin-bottom: -1px;
	border-bottom-right-radius: 0;
	border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
	margin-bottom: 10px;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
}
</style>


</head>
<body class="text-center">


	<div class="col-lg-8 mx-auto p-3 py-md-5">
		<header class="d-flex align-items-center pb-3 mb-5 border-bottom">
			<a href="/"
				class="d-flex align-items-center text-dark text-decoration-none">
				<svg xmlns="http://www.w3.org/2000/svg" width="40" height="32"
					class="me-2" viewBox="0 0 118 94" role="img">
					<title>Bootstrap</title><path fill-rule="evenodd"
						clip-rule="evenodd"
						d="M24.509 0c-6.733 0-11.715 5.893-11.492 12.284.214 6.14-.064 14.092-2.066 20.577C8.943 39.365 5.547 43.485 0 44.014v5.972c5.547.529 8.943 4.649 10.951 11.153 2.002 6.485 2.28 14.437 2.066 20.577C12.794 88.106 17.776 94 24.51 94H93.5c6.733 0 11.714-5.893 11.491-12.284-.214-6.14.064-14.092 2.066-20.577 2.009-6.504 5.396-10.624 10.943-11.153v-5.972c-5.547-.529-8.934-4.649-10.943-11.153-2.002-6.484-2.28-14.437-2.066-20.577C105.214 5.894 100.233 0 93.5 0H24.508zM80 57.863C80 66.663 73.436 72 62.543 72H44a2 2 0 01-2-2V24a2 2 0 012-2h18.437c9.083 0 15.044 4.92 15.044 12.474 0 5.302-4.01 10.049-9.119 10.88v.277C75.317 46.394 80 51.21 80 57.863zM60.521 28.34H49.948v14.934h8.905c6.884 0 10.68-2.772 10.68-7.727 0-4.643-3.264-7.207-9.012-7.207zM49.948 49.2v16.458H60.91c7.167 0 10.964-2.876 10.964-8.281 0-5.406-3.903-8.178-11.425-8.178H49.948z"
						fill="currentColor"></path></svg> <span class="fs-4">Starter
					template</span>
			</a>
		</header>

		<main>
			<h1>Get started with Bootstrap</h1>

			<div class="row mb-5">
				<div class="col-md-12 d-flex justify-content-center">
					<div id="profile_dropzone" style="background:gray;height:150px; width:150px;"></div>

				</div>
			</div>
			<form id="signupform" method="post" action="/signup">
			<div class="form">


				<div class="form-content">


					<div class="row">


						<div class="col-md-6">
							<div class="form-group mb-2">
								<input type="text" class="form-control" name ="firstname"
									placeholder="Your Name *" value="" />
							</div>
							<div class="form-group mb-2">
							<input type="text" class="form-control" name ="lastname"
									placeholder="Your Name *" value="" />
							</div>
						</div>
						
						
						<div class="col-md-6 pb-2">
							<div class="form-group mb-2">
								<input type="text" class="form-control"
									placeholder="Your Password *" value="" name ="password" />
							</div>
							<div class="form-group mb-2">
								<input type="text" class="form-control"
									placeholder="Confirm Password *" value="" />
							</div>
						</div>
					</div>
					<button type="submit" class="btnSubmit">Submit</button>
				</div>
				
				
			</div>
			</form>
		</main>
		<footer class="pt-5 my-5 text-muted border-top"> Created by
			the Bootstrap team &middot; &copy; 2021 </footer>
	</div>

	<script src="<c:url value="/app_resources/jquery-3.6.3.min.js"/>"></script>
	<script src="<c:url value="/app_resources/bootstrap.bundle.min.js"/>"></script>
	<script src="<c:url value="/app_resources/dropzone-min.js"/>"></script>
	<script src="<c:url value="/app_resources/signup.js"/>"></script>


</body>

</html>