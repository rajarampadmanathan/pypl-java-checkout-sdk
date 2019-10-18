package com.paypal;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
public class Credentials {
	static String clientId = "<<CLIENT_ID_HERE>>";
	static String secret = "<<CLIENT_SECRET_HERE>>";
    
    // Creating a sandbox environment
    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);
    
    // Creating a client for the environment
    static PayPalHttpClient client = new PayPalHttpClient(environment);
}