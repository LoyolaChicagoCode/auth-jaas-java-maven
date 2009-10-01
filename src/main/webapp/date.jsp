<%@ page %>

<html>
  <head><title>Protected: Date</title></head>
<body>

<h2>Current Date</h2>

<p>It is now <%= new java.util.Date() %>.</p>

<p><a href=".">main menu</a></p>

<%@ include file="authinfo.jsp" %>

</body>
</html>




