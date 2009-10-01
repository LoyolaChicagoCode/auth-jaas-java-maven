/*
   Copyright 2009 Konstantin Laufer (laufer AT cs DOT luc DOT edu)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package edu.luc.cs.laufer.jaas;

/**
 * Db4oLoginModule.java @author laufer Apr 25, 2009
 * This is a JAAS login module that relies on the db4o object database for user storage.
 * It is designed for servlet containers such as Jetty.
 * It it loosely based on Newton Whitman's func4 but uses a local file
 * instead of a client-server connection.
 */

// TODO consider merging with func4 to allow both file and client connections
// TODO add optional class options in case user and role classes are known
//      (for query optimization)
// TODO consider posting to db4o and Jetty communities

import java.util.ArrayList;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.mortbay.jetty.plus.jaas.spi.AbstractLoginModule;
import org.mortbay.jetty.plus.jaas.spi.UserInfo;
import org.mortbay.jetty.security.Credential;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.StoredClass;
import com.db4o.ext.StoredField;
import com.db4o.query.Query;

@SuppressWarnings("unchecked")
public class Db4oFileLoginModule extends AbstractLoginModule {

	// instance variables for storing module options

	private String dbFilename;
	private String userNameField;
	private String passwordField;
	private String userRoleField;
	private String roleNameField;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.mortbay.jetty.plus.jaas.spi.AbstractLoginModule#initialize(javax.
	 * security.auth.Subject, javax.security.auth.callback.CallbackHandler,
	 * java.util.Map, java.util.Map)
	 */
	@Override
	public void initialize(final Subject subject,
			final CallbackHandler callbackHandler, final Map sharedState,
			final Map options) {
		super.initialize(subject, callbackHandler, sharedState, options);
		setDbFilename((String) options.get("dbFilename"));
		setUserNameField((String) options.get("userNameField"));
		setPasswordField((String) options.get("passwordField"));
		setUserRoleField((String) options.get("userRoleField"));
		setRoleNameField((String) options.get("roleNameField"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.mortbay.jetty.plus.jaas.spi.AbstractLoginModule#getUserInfo(java.
	 * lang.String)
	 */
	@Override
	public UserInfo getUserInfo(final String username) throws Exception {
		if (username == null)
			throw new IllegalArgumentException("username == null");
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getDbFilename());
			// 1) look for the first matching user object
			final Query userQuery = db.query();
			userQuery.descend(getUserNameField()).constrain(username);
			final Object user = userQuery.execute().next();
			// 2) extract password field
			final StoredClass scu = db.ext().storedClass(user);
			final StoredField sfu = scu.storedField(getPasswordField(), scu);
			final String password = (String) sfu.get(user);
			// 3) extract roles
			final Query rolesQuery = userQuery.descend(getUserRoleField());
			final ObjectSet results = rolesQuery.execute();
			final ArrayList<String> roles = new ArrayList<String>();
			for (final Object p : (Iterable) results.next()) {
				final StoredClass sc = db.ext().storedClass(p);
				final StoredField sf = sc.storedField(getRoleNameField(), sc);
				roles.add((String) sf.get(p));
			}
			// 4) return resulting UserInfo object
			return new UserInfo(username, Credential.getCredential(password),
					roles);
		} catch (final Exception e) {
			throw new LoginException(e.toString());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	private String getDbFilename() {
		return dbFilename;
	}

	private void setDbFilename(final String dbFilename) {
		this.dbFilename = dbFilename;
	}

	private void setUserNameField(final String userNameField) {
		this.userNameField = userNameField;
	}

	private String getUserNameField() {
		return userNameField;
	}

	private void setPasswordField(final String passwordField) {
		this.passwordField = passwordField;
	}

	private String getPasswordField() {
		return passwordField;
	}

	private void setUserRoleField(final String userRoleField) {
		this.userRoleField = userRoleField;
	}

	private String getUserRoleField() {
		return userRoleField;
	}

	private void setRoleNameField(final String roleNameField) {
		this.roleNameField = roleNameField;
	}

	private String getRoleNameField() {
		return roleNameField;
	}
}
