<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h2>Meals</h2>
<table border="1">
    <tr>
        <td>description</td>
        <td>dateTime</td>
        <td>calories</td>
        <td>exceed</td>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.exceed ? '#FF4136' : '#2ECC40'}">
            <td>
                <c:out value="${meal.description}"/>
            </td>
            <td>
                <fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }"/>
            </td>
            <td>
                <c:out value="${meal.calories}"/>
            </td>
            <td>
                <c:out value="${meal.exceed}"/>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>