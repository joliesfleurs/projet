<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.4.7/css/foundation.min.css">
	
<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.4/css/jquery.dataTables.css">
<!-- jQuery -->
<script type="text/javascript" charset="utf8" src="//code.jquery.com/jquery-1.10.2.min.js"></script>
<!-- DataTables -->
<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
	    $('#listeAnnonces').DataTable({
	    	"lengthMenu": [5, 10, 20, 50],
	   		 "pageLength": 5
	    });
	});
</script>


<title>List</title>
</head>
<body class="bg">
<%@ include file="../templates/header.jsp" %>

	<table id="listeAnnonces" class="display" cellspacing="0" width="100%">
		<thead>
			<tr><th>Image</th><th>Titre</th><th>Cat&eacute;gorie</th><th>Date</th><th></th></tr>
		</thead>
		<tfoot>
			<tr><th>Image</th><th>Titre</th><th>Cat&eacute;gorie</th><th>Date</th><th></th></tr>
		</tfoot>
		<tbody>

			<c:if test="${!empty annList}">
		 		<c:forEach items="${annList}" var="ann">
		 			<tr>
		 				<td>
		 					<img height="100" width="auto" src="<c:url value="/resources/img/chat.png" />">
 						</td>
 						<td>${ann.titre}</td>
 						<td>${ann.categorie.lib }</td>
 						<td>${ann.date_fin.toString().substring(0,10) }</td>
 						<td><a href="<c:url value='/annonce/${ann.id}' />" class="button round">Voir l'annonce</a></td>
 					</tr>
		 		</c:forEach>
		 	</c:if>
		 	
		 </tbody>
	</table>
 		
<%-- 	<c:if test="${!empty annList}"> --%>
<%-- 		<c:forEach items="${annList}" var="ann"> --%>

<!-- 			<div class="panel"> -->
<!-- 				<div class="row"> -->
<!-- 					<div class="small-2 medium-2 large-2 columns"> -->

<!-- 						<img height="100" width="auto" -->
<%-- 							src="<c:url value="/resources/img/chat.png" />"> --%>

<!-- 					</div> -->
<!-- 					<div class="small-4 medium-4 large-3 columns"> -->
<%-- 						<span> ${ann.titre} </span> --%>
<!-- 					</div> -->
<!-- 					<div class="small-4 medium-4 large-3 columns"> -->
<%-- 						<span> ${ann.categorie.lib } </span> --%>
<!-- 					</div> -->

<!-- 					<div class="small-2 medium-2 large-2 columns"> -->
<%-- 						<span> ${ann.date_fin } </span> --%>
<!-- 					</div> -->
<!-- 					<div class="small-2 medium-2 large-2 columns"> -->
<%-- 						<a href="<c:url value='/annonce/${ann.id}' />" class="button round">Voir l'annonce</a> --%>
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<%-- 		</c:forEach> --%>
<%-- 	</c:if> --%>



	<a href="ann_form">Ajout d'une nouvelle annonce</a>
</body>
</html>
