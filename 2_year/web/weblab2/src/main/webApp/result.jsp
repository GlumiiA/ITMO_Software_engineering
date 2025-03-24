<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Point" %>
<!DOCTYPE html>
<html>
<head>
    <title>results</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
<div class="header">
    <h1>Результат</h1>
</div>
<div class="container">
    <div class="results-holder">
    <table border="1" cellspacing="0" width="50%" align="center">
            <thead>
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Попадание</th>
                <th>Текущее время</th>
                <th>Время выполнения</th>
            </tr>
            </thead>
            <tbody>
            <%
              Object sessionData = application.getAttribute("sessionData");
              if (sessionData instanceof ArrayList<?>) {
                  @SuppressWarnings("unchecked")
                  ArrayList<Point> contents = new java.util.ArrayList<>(((ArrayList<Point>) sessionData));
                  Point point = contents.get(contents.size() - 1);
              %>
                      <tr>
                          <td><%=point.x()%></td>
                          <td><%=point.y()%></td>
                          <td><%=point.r()%></td>
                          <td><%=point.isHit()%></td>
                          <td><%=point.time()%></td>
                          <td><%=point.dur()%></td>
                      </tr>
              <%
                   }
              %>
            </tbody>
        </table>
    </div>
    <div class="button-holder">
        <a href="<%=request.getContextPath()%>/index.jsp">
            <button id="back-button">назад</button>
        </a>
    </div>
</div>
</body>
</html>