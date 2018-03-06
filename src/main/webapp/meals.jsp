<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h2>Meals</h2>

<form method="post">
    <input type="hidden" name="action" value="<c:out value="${empty editMeal ? 'add' : 'update'}" />"/>
    <input type="hidden" id="id" name="id" value="<c:out value="${empty editMeal ? '' : editMeal.id}" />"/>
    <input type="datetime-local" id="dateTime" name="dateTime" placeholder="date"
           value="<c:out value="${empty editMeal ? '' : editMeal.dateTime}"/>">
    <input type="text" id="description" name="description" placeholder="description"
           value="<c:out value="${empty editMeal ? '' : editMeal.description}"/>"/>
    <input type="text" id="calories" name="calories" placeholder="calories"
           value="<c:out value="${empty editMeal ? '' : editMeal.calories}"/>"/>
    <p><input type="submit" value="<c:out value="${empty editMeal ? 'add' : 'update'}"/>"/>
</form>

<table border="1">
    <tr>
        <td>id</td>
        <td>description</td>
        <td>dateTime</td>
        <td>calories</td>
        <td>exceed</td>
        <td></td>
        <td></td>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.exceed ? '#FF4136' : '#2ECC40'}">
            <td>
                <c:out value="${meal.id}"/>
            </td>
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
            <td>
                <form method="post">
                    <a href="#" onclick="parentNode.submit();">edit</a>
                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" name="id" value=<c:out value="${meal.id}"/>>
                </form>
            </td>
            <td>
                <form method="post">
                    <a href="#" onclick="parentNode.submit();">delete</a>
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="id" value=<c:out value="${meal.id}"/>>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>