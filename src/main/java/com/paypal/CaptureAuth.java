package com.paypal;
//88B78740MU712650J

import java.io.IOException;

import org.json.JSONObject;

import com.braintreepayments.http.HttpResponse;
import com.braintreepayments.http.serializer.Json;
import com.paypal.PayPalClient;
import com.paypal.orders.OrderRequest;
import com.paypal.payments.AuthorizationsCaptureRequest;
import com.paypal.payments.Capture;
import com.paypal.payments.LinkDescription;

/*
*
*1. Import the PayPal SDK client that was created in `Set up the Server SDK`.
*This step extends the SDK client. It's not mandatory to extend the client, you can also inject it.
*/
public class CaptureAuth extends PayPalClient {

  //2. Set up your server to receive a call from the client
  /**
   *Method to capture order after authorization
   *
   *@param authId Authorization ID from authorizeOrder response
   *@param debug  true = print response data
   *@return HttpResponseCapture response received from API
   *@throws IOException Exceptions from API if any
   */
  public HttpResponse<Capture> captureAuth(String authId, boolean debug) throws IOException {
    AuthorizationsCaptureRequest request = new AuthorizationsCaptureRequest(authId);
    request.requestBody(buildRequestBody());
    //3. Call PayPal to capture an authorization.
    HttpResponse<Capture> response = client().execute(request);
    //4. Save the capture ID to your database for future reference.
    if (debug) {
      System.out.println("Status Code: " + response.statusCode());
      System.out.println("Status: " + response.result().status());
      System.out.println("Capture ID: " + response.result().id());
      System.out.println("Links: ");
      for (LinkDescription link : response.result().links()) {
        System.out.println("\t" + link.rel() + ": " + link.href() + "\tCall Type: " + link.method());
      }
      System.out.println("Full response body:");
      System.out.println(new JSONObject(new Json()
              .serialize(response.result())).toString(4));
    }
    return response;
  }

  /**
   *Create an empty body for capture request
   *
   *@return OrderRequest request with empty body
   */
  public OrderRequest buildRequestBody() {
    return new OrderRequest();
  }

  /**
   *This function uses the captureOrder function to
   *capture on authorization. Replace the authorization ID with
   *the valid authorization ID.
   *
   *@param args
   */
  public static void main(String[] args) {
    try {
      new CaptureAuth().captureAuth("88B78740MU712650J", true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}