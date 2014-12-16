<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<link rel="stylesheet"
	href="<c:url value="/resources/css/header.css" />	">
<link rel="stylesheet"
	href="<c:url value="/resources/css/foundation.min.css" />	">
	<link rel="stylesheet"
	href="<c:url value="/resources/css/form.css" />	">
<script src="<c:url value="/resources/js/vendor/jquery.js" />"></script>
<script src="<c:url value="/resources/js/foundation.min.js" />"></script>

<nav class="top-bar" data-topbar role="navigation">
	<section class="top-bar-section">

		<ul class="title-area">
			<li class="name">
				<h1>
					<a href="<c:url value="/" />">Accueil L1C</a>
				</h1>
			</li>
		</ul>
		<ul class="left">
			<li><a href="#">Mon compte</a></li>
			<li class="has-dropdown"><a href="#">Covoiturage</a>
				<ul class="dropdown">
					<li><a href="<c:url value="/annonce/new"  />">Cr�ez votre
							covoit'</a></li>
					<li><a href="<c:url value="/annonce/list" />">Trouvez un
							covoit' </a></li>
				</ul></li>
			<li class="has-dropdown"><a href="#">Petites annonce</a>
				<ul class="dropdown">
					<li><a href="<c:url value="/annonce/new"  />">Cr�ez votre
							petite annonce</a></li>
					<li><a href="<c:url value="/annonce/list" />">Recherche </a></li>
				</ul></li>

			<li><a href="#">Job</a></li>
			<li><a href="#">Forum</a></li>
		</ul>
		<ul class="right">
			<li class="has-form"><input type="text" placeholder="Search"></li>
			<li class="has-dropdown"><a href="#">ListeCat�gorie</a>
				<ul class="dropdown">

					<c:forEach items="${catList}" var="cat">


						<li><a
							href="categorie/annonceByCat?idCatSelect=<c:out value='${cat.id}' />"><c:out
									value='${cat.lib}' /> </a></li>
					</c:forEach>

				</ul></li>
			<li class="has-form"><a href="#" class="alert button expand">Search</a>
		</ul>


	</section>
</nav>
<script>
	$(document).foundation();
</script>