package com.paypal;

import java.io.IOException;

import com.braintreepayments.http.HttpResponse;
import com.paypal.PayPalClient;
import com.paypal.orders.Capture;
import com.paypal.orders.Customer;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.PurchaseUnit;

/*
*
*1. Import the PayPal SDK client that was created in `Set up the Server SDK`.
*This step extends the SDK client.  It's not mandatory to extend the client, you can also inject it.
*/
public class CaptureOrder extends PayPalClient {

  //2. Set up your server to receive a call from the client
  /**
   *Method to capture order after creation. Pass a valid, approved order ID
   *an argument to this method.
   *
   *@param orderId Authorization ID from authorizeOrder response
   *@param debug   true = print response data
   *@return HttpResponseCapture response received from API
   *@throws IOException Exceptions from API if any
   */
  public HttpResponse<Order> captureOrder(String orderId, boolean debug) throws IOException {
    OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
    request.requestBody(buildRequestBody());
    //3. Call PayPal to capture an order
    HttpResponse<Order> response = client().execute(request);
    //4. Save the capture ID to your database. Implement logic to save capture to your database for future reference.
    if (debug) {
      System.out.println("Status Code: " + response.statusCode());
      System.out.println("Status: " + response.result().status());
      System.out.println("Order ID: " + response.result().id());
      System.out.println("Links: ");
      for (LinkDescription link : response.result().links()) {
        System.out.println("\t" + link.rel() + ": " + link.href());
      }
      System.out.println("Capture ids:");
      for (PurchaseUnit purchaseUnit : response.result().purchaseUnits()) {
        for (Capture capture : purchaseUnit.payments().captures()) {
          System.out.println("\t" + capture.id());
        }
      }
      System.out.println("Buyer: ");
      Customer buyer = response.result().payer();
      System.out.println("\tEmail Address: " + buyer.emailAddress());
      System.out.println("\tName: " + buyer.name().fullName());
      System.out.println("\tPhone Number: " + buyer.phone().countryCode() + buyer.phone().nationalNumber());
    }
    return response;
  }

  /**
   *Creating empty body for capture request.
   *You can set the payment source if required.
   *
   *@return OrderRequest request with empty body
   */
  public OrderRequest buildRequestBody() {
    return new OrderRequest();
  }

  /**
   *Driver function to invoke capture payment on order.
   *Replace the order ID with the valid, approved order ID.
   *
   *@param args
   */
  public static void main(String[] args) {
    try {
      new CaptureOrder().captureOrder("3K925514JF0230126", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}