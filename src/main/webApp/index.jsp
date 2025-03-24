<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Point" %>
<!DOCTYPE html>
<html lang="ru-RU">

<head>
  <meta charset="UTF-8">
  <title>Лабораторная работа №2</title>
  <link href="css/style.css" rel="stylesheet">
</head>

<body>
<form method="POST" id="dataform">
  <div class="basic-container">
    <header class="header">
      <div class="name">Ибрагимова Айгуль Ильгизовна P3211</div>
      <div class="isu">ИСУ: 408675</div>
      <div class="var">Вариант: 21040</div>
    </header>
    <div class="main">
      <div class="graph">
        <%@include file="svg/image.svg"%>>
      </div>
      <div class="main-block">
        <form action="controller" method="post" id="coordinates">
          <div class="inputX">
            <label>Введите X:</label>
            <input type="text" name="xValue" min="1" max="5" required>
          </div>
          <div class="inputY">
            <label>Выберите Y:</label>
            <input class = "buttonY" type ="button" name="yValue" value="-3" >
            <input class = "buttonY" type ="button" name="yValue" value="-2" >
            <input class = "buttonY" type ="button" name="yValue" value="-1" >
            <input class = "buttonY" type ="button" name="yValue" value="0" >
            <input class = "buttonY" type ="button" name="yValue" value="1" >
            <input class = "buttonY" type ="button" name="yValue" value="2" >
            <input class = "buttonY" type ="button" name="yValue" value="3" >
            <input class = "buttonY" type ="button" name="yValue" value="4" >
            <input class = "buttonY" type ="button" name="yValue" value="5" >
          </div>

          <div class="inputR">
            <label>Выберите R:</label>
            <label><input type="checkbox" name="rValue" value="1" /> 1</label>
            <label><input type="checkbox" name="rValue" value="2" /> 2</label>
            <label><input type="checkbox" name="rValue" value="3" /> 3</label>
            <label><input type="checkbox" name="rValue" value="4" /> 4</label>
            <label><input type="checkbox" name="rValue" value="5" /> 5</label>
            </div>

          <button class="main-block_button" style="bottom: 15px;" id="submit">
            <a href="result.jsp">Проверить</a>
          </button>
        </form>
      </div>
      <div class="result-table" border="2" id="result_table">
        <table class="result" id="result">
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
          <tbody id="body">
          <%
              Object sessionData = application.getAttribute("sessionData");
              if (sessionData instanceof ArrayList<?>) {
                  @SuppressWarnings("unchecked")
                  List<Point> contents = new java.util.ArrayList<>(((ArrayList<Point>) sessionData));

                  if (contents == null || contents.isEmpty()) {
          %>
                      <tr>
                          <td colspan="6">Нет данных для отображения</td>
                      </tr>
          <%
                  } else {
                      for (int i = 0; i < contents.size(); i = i + 2) {
                          Point point = contents.get(i);
          %>
                          <tr>
                              <td><%= point.x() %></td>
                              <td><%= point.y() %></td>
                              <td><%= point.r() %></td>
                              <td><%= point.isHit() %></td>
                              <td><%= point.time() %></td>
                              <td><%= point.dur() %></td>
                          </tr>
          <%
                      }
                  }
              }
          %>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.2/jquery.validate.min.js"></script>
  <script src="//cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
  <script src="js/script.js"></script>
  <script charset="utf-8" type="text/javascript">
  </script>
</form>
</body>
</html>