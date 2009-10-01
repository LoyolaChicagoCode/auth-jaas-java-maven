<%@ page %>

<h2>Authentication Information</h2>

<ul>
<li>Mechanism: <%= request.getAuthType() %></li>
<li>User: <%= request.getRemoteUser() %></li>
<li><%= request.isUserInRole("guests") ? "In" : "Not in" %> role guests</li>
<li><%= request.isUserInRole("users") ? "In" : "Not in" %> role users</li>
<li><%= request.isUserInRole("administrators") ? "In" : "Not in" %> role administrators</li>
</ul>





