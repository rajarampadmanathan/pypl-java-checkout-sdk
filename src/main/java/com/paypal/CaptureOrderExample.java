package com.paypal;

import java.io.IOException;

import com.braintreepayments.http.HttpResponse;
import com.braintreepayments.http.exceptions.HttpException;
import com.paypal.orders.*;

public class CaptureOrderExample {
	public static void main(String[] args) {
		Order order = null;
		OrdersCaptureRequest request = new OrdersCaptureRequest("APPROVED-ORDER-ID");

		try {
			// Call API with your client and get a response for your call
			HttpResponse<Order> response = Credentials.client.execute(request);

			// If call returns body in response, you can get the de-serialized version by
			// calling result() on the response
			order = response.result();
			System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
			order.purchaseUnits().get(0).payments().captures().get(0).links()
					.forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
		} catch (IOException ioe) {
			if (ioe instanceof HttpException) {
				// Something went wrong server-side
				HttpException he = (HttpException) ioe;
				System.out.println(he.getMessage());
				he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
			} else {
				// Something went wrong client-side
			}
		}
	}
}