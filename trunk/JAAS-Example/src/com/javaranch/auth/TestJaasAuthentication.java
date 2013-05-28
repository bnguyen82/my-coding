package com.javaranch.auth;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Rahul Bhattacharjee
 *
 */
public class TestJaasAuthentication {

	public static void main(String[] args) {

		String userName = System.getProperty("user");
		String password = System.getProperty("pass");

		boolean loginStatus = true;

		CallbackHandler handler = new RanchCallbackHandler(userName,password);

		try {

			LoginContext loginContext = new LoginContext("RanchLogin" , handler);
			loginContext.login();

		} catch (LoginException e) {
			loginStatus = false;
		}

		if(loginStatus){
			System.out.println("Login Successful.");
		}else{
			System.out.println("Login Failed.");
		}
	}

}
