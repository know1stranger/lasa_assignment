<%@ page session="false" isErrorPage="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>
<html lang="en">

<body>
	<div class="container">
		<h1>Error Page</h1>
		<p>${exception.message}</p>
		<c:if test="${appException!=null}">
			<p>Oops we got something here..!</p>
			<div>Error serving request path: ${appException.requestUrl}</div>
			<div>Exception Cause: ${appException.causeMessage}</div>
			<div>Reason: ${appException.reason}</div>
		</c:if>
	</div>
</body>
</html>