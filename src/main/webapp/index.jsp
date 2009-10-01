<%@ page %>

<html>
  <head><title>Protected: Index Page</title></head>
<body>

<h1>Main Menu</h1>

<ul>
  <li><a href="date.jsp">check current date and time</a></li>
  <li><a href="admin.jsp">admin page</a></li>
  <li><a href="logout.jsp">log out</a></li>
</ul>

<%@ include file="authinfo.jsp" %>

</body>
</html>




