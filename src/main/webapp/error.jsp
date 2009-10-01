<html>
  <head><title>Authentication Error</title></head>
<body>

<p>You have entered an invalid username/password.<br>
Please <a href=".">try again</a><br>
If you continue to experience difficulties please contact the site administrator at webmaster@somesystem.com
</p>

<ul>
<li>User: <%= request.getRemoteUser() %></li>
<li><%= request.isUserInRole("guests") ? "In" : "Not in" %> role guests</li>
<li><%= request.isUserInRole("users") ? "In" : "Not in" %> role users</li>
<li><%= request.isUserInRole("administrators") ? "In" : "Not in" %> role administrators</li>
</ul>

</body>
</html>




