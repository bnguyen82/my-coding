package com.javaranch.auth;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * LoginModule implementation.
 * 
 * Authentication logic for this LoginModule is that the username should be equal to the password.
 * 
 * @author Rahul Bhattacharjee
 * 
 */
public class RanchLoginModule implements LoginModule{

	private Subject subject;
	private CallbackHandler callbackHandler;
	//private Map state;
	//private Map options;

	public boolean abort() throws LoginException {
		return true;
	}

	public boolean commit() throws LoginException {
		return true;
	}

	public void initialize(Subject subject, CallbackHandler handler, Map state, Map options) {
		this.subject = subject;
		this.callbackHandler = handler;
		//this.state = state;
		//this.options = options;
	}

	public boolean login() throws LoginException {
		boolean returnValue = true;

		if(callbackHandler == null){
			throw new LoginException("No callback handler supplied.");
		}

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("Username");
		callbacks[1] = new PasswordCallback("Password", false);

		try {
			callbackHandler.handle(callbacks);
			String userName = ((NameCallback)callbacks[0]).getName();
			char [] passwordCharArray = ((PasswordCallback)callbacks[1]).getPassword();
			String password = new String(passwordCharArray);

	        //==> Authentication.
			returnValue = userName.equals(password);

		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new LoginException("IOException occured.");
		} catch (UnsupportedCallbackException e) {
			e.printStackTrace();
			throw new LoginException("UnsupportedCallbackException encountered.");
		}

	   return returnValue;
	}

	public boolean logout() throws LoginException {
		return true;
	}

}
