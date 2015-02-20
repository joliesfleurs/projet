<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="<c:url value="/resources/js/vendor/jquery.js" />"></script>

<title>List</title>
<style>
.scroll {
	padding: 2px; /* équivalent cellpadding */
	display: inline-block;
	overflow-y: auto;
	width: 200px;
}

<style type="text/css"> 
  td { 
    word-wrap: break-word; 
  } 

</style>
</head>
<body class="bg">
	<%@ include file="../templates/header.jsp"%>
	<div class="row">
		<div class="large-10 columns">
			<div class="panel" style="text-align: center">
				<b><b>liste des éxpériences à l'étranger</b></b><br> <br>
				<div class="row" style="text-align: center">
					<table id="listeforums" class="display" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Titre</th>
								<th>description</th>
								<th>Date de publication</th>
								
								<th></th>
								<th></th>
							</tr>
							<tr></tr>
						</thead>
						<tfoot>

						</tfoot>
						<tbody id="lignes">
							<c:if test="${!empty forumList}">
								<c:forEach items="${forumList}" var="forum">
									<tr>
										<td><table border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="200" height="80"  colspan="2"><b><b>${forum.titre}</b></b></td>
												</tr>
											</table></td>

										<td>
											<table border="0" cellspacing="0" cellpadding="0">
												<tr>
											
													<td width="300" height="80" style="word-break:break-all;"><i>${forum.desc.toString()}</i>...</td>
												</tr>
											</table>
										</td>
										<td>
											<table border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="100" height="80" ><fmt:formatDate pattern="yyyy-MM-dd" 
         								   value="${forum.date_pub}"/></td>
												</tr>
											</table>


										</td>
									
										<td><a href="<c:url value='/forum/${forum.id}'/>"
											class="button round">Details</a></td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					
				</div>
			</div>
		</div>
	</div>
<a href="<c:url value="/forum/new"  />">Ajout un nouveau témoignage</a>
</body>
</html>