<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<c:url value="/resources/css/consultAnn.css" />	">
<link rel="stylesheet" href="<c:url value="/resources/css/font.css" />	">
<link rel="stylesheet" href="<c:url value="/resources/css/form.css" />">
<link rel="stylesheet"
	href="<c:url value='/resources/css/foundation.min.css'/>">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/foundation/foundation.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/datepicker-fr.js" />">
	
</script>


<title>Création d'une offre de job</title>
</head>
<body class="bg">
	<%@ include file="../templates/header.jsp"%>

	<div class="row">
		<div class="large-offset-2 large-8 columns">
			<div class="panel">
				<form:form id="addjobForm" modelAttribute="job" method="post"
					action="addjob" enctype="multipart/form-data">
					<h2>Créez votre offre d'emploi !</h2>

					<div>
						<form:input path="titre" placeholder="Titre de l'offre" maxlength="30"  required="required"/>
						<form:errors path="titre" />
					</div>
					<div>
						<form:textarea path="desc" 
							placeholder="Décrivez votre offre en quelque mots !" maxlength="500"/>
						<form:errors path="desc" />
					</div>

					<div>
						<form:input path="prix"
							placeholder="Salaire (Horaire/Journalier/Hebdomadaire/Mensuel... à préciser)" />
						<form:errors path="desc" />
					</div>
					
					<div>
						<form:input path="mail" type="email"
							placeholder="Mail a contacter" required="required"/>
						<form:errors path="mail" />
					</div>

<!-- 					<div> -->
<%-- 						<form:input path="date_fin" type="text" id="datepicker" --%>
<%-- 							placeholder="Fin de votre offre:" /> --%>
<!-- 					</div> -->


					<div>
						<input class="button success" type="submit" value="Ajouter" />
					</div>

				</form:form>
			</div>
		</div>

	</div>
</body>
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script>
	$(document)
			.ready(
					function() {

						var frm = $('#updateUtiForm');
						frm.submit(function(ev) {
							$.ajax({
								type : frm.attr('method'),
								url : frm.attr('action'),
								data : frm.serialize(),
								success : function(data) {
								}
							});

							ev.preventDefault();
						});

						$('#autreMail')
								.click(
										function() {
											document
													.getElementById("SecondeAdresseMail").innerHTML = " <input id='secondMail' name='mailAutre' type='text' placeholder='Autre addresse mail' />";
											var elem = document
													.getElementById("secondMail");
											elem.value = "${current_user.mailAutre}";
										});
						$('#maiMail')
								.click(
										function() {
											document
													.getElementById("SecondeAdresseMail").innerHTML = "";
										});

						$(function() {
							$("#datepicker").datepicker(
									$.datepicker.regional['fr']);
						});

					});
</script>
<script type="text/javascript">
	function getUrlParameter(sParam) {
		var sPageURL = window.location.search.substring(1);
		var sURLVariables = sPageURL.split('&');
		for (var i = 0; i < sURLVariables.length; i++) {
			var sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] == sParam) {
				return sParameterName[1];
			}
		}
	}
	
	var choix = getUrlParameter('type');
	
	if (choix == "give"){
		document
		.getElementById("select").selectedIndex = 1;
	}
	else if (choix == "need"){
		document
		.getElementById("select").selectedIndex = 0;
	}
	
</script>
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/foundation/foundation.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/datepicker-fr.js" />">
	
</script>
</html>
