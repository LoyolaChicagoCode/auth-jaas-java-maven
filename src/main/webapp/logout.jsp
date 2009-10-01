<html>
  <head><title>Protected: Logout</title></head>
<body>
<h2>Logout page</h2>

<% session.invalidate(); %>

<p>You are now logged out.</p>

<p><a href=".">Log back in</a></p>

<%@ include file="authinfo.jsp" %>

</body>
</html>





