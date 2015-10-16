<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en-us">
    <head>
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css" />" />
    </head>
    <body>
        <h1>Error Details</h1>
        <table>
            <tr>
                <td class="label"><label>Message</label></td>
                <td>${error.message}</td>
            </tr>
            <tr>
                <td class="label"><label>Stack Trace</label></td>
                <td>
                    <pre style="white-space:pre-wrap;"><c:forEach var="stackTraceElement" items="${error.stackTrace}">${stackTraceElement}<br/></c:forEach></pre>
                </td>
            </tr>
        </table>
        <a href="<c:url value="/index.jsp" />">Go Back</a>
    </body>
</html>
