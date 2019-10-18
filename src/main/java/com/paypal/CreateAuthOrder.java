package com.paypal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.braintreepayments.http.HttpResponse;
import com.paypal.PayPalClient;
import com.paypal.orders.AddressPortable;
import com.paypal.orders.AmountBreakdown;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Item;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Money;
import com.paypal.orders.Name;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.orders.ShippingDetails;

/*
*
*1. Import the PayPal SDK client that was created in `Set up the Server SDK`.
*This step extends the SDK client.  It's not mandatory to extend the client, you can also inject it.
*/
public class CreateAuthOrder extends PayPalClient {

  //2. Set up your server to receive a call from the client
  /**
   *Method to create order
   *
   *@param debug true = print response data
   *@return HttpResponseOrder response received from API
   *@throws IOException Exceptions from API if any
   */
  public HttpResponse<Order> createOrder(boolean debug) throws IOException {
    OrdersCreateRequest request = new OrdersCreateRequest();
    request.prefer("return=representation");
    request.requestBody(buildRequestBody());
    //3. Call PayPal to set up the transaction
    HttpResponse<Order> response = client().execute(request);
    if (debug) {
      if (response.statusCode() == 201) {
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Status: " + response.result().status());
        System.out.println("Order ID: " + response.result().id());
        System.out.println("Intent: " + response.result().intent());
        System.out.println("Links: ");
        for (LinkDescription link : response.result().links()) {
          System.out.println("\t" + link.rel() + ": " + link.href() + "\tCall Type: " + link.method());
        }
        System.out.println("Total Amount: " + response.result().purchaseUnits().get(0).amount().currencyCode()
            + " " + response.result().purchaseUnits().get(0).amount().value());
      }
    }
    return response;
  }

  /**
   *Method to generate sample create order body with AUTHORIZE intent
   *
   *@return OrderRequest with created order request
   */
  private OrderRequest buildRequestBody() {
    OrderRequest orderRequest = new OrderRequest();
    orderRequest.intent("AUTHORIZE");

    ApplicationContext applicationContext = new ApplicationContext().brandName("EXAMPLE INC").landingPage("BILLING")
        .shippingPreference("SET_PROVIDED_ADDRESS");
    orderRequest.applicationContext(applicationContext);

    List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<PurchaseUnitRequest>();
    PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().referenceId("PUHF")
        .description("Sporting Goods").customId("CUST-HighFashions").softDescriptor("HighFashions")
        .amount(new AmountWithBreakdown().currencyCode("USD").value("230.00")
            .breakdown(new AmountBreakdown().itemTotal(new Money().currencyCode("USD").value("180.00"))
                .shipping(new Money().currencyCode("USD").value("30.00"))
                .handling(new Money().currencyCode("USD").value("10.00"))
                .taxTotal(new Money().currencyCode("USD").value("20.00"))
                .shippingDiscount(new Money().currencyCode("USD").value("10.00"))))
        .items(new ArrayList<Item>() {
          {
            add(new Item().name("T-shirt").description("Green XL").sku("sku01")
                .unitAmount(new Money().currencyCode("USD").value("90.00"))
                .tax(new Money().currencyCode("USD").value("10.00")).quantity("1")
                .category("PHYSICAL_GOODS"));
            add(new Item().name("Shoes").description("Running, Size 10.5").sku("sku02")
                .unitAmount(new Money().currencyCode("USD").value("45.00"))
                .tax(new Money().currencyCode("USD").value("5.00")).quantity("2")
                .category("PHYSICAL_GOODS"));
          }
        })
        .shipping(new ShippingDetails().name(new Name().fullName("John Doe"))
            .addressPortable(new AddressPortable().addressLine1("123 Townsend St").addressLine2("Floor 6")
                .adminArea2("San Francisco").adminArea1("CA").postalCode("94107").countryCode("US")));
    purchaseUnitRequests.add(purchaseUnitRequest);
    orderRequest.purchaseUnits(purchaseUnitRequests);
    return orderRequest;
  }


  /**
   *This is the driver function that invokes the createOrder function to create
   *a sample order.
   */
  public static void main(String args[]) {
    try {
      new CreateAuthOrder().createOrder(true);
    } catch (com.braintreepayments.http.exceptions.HttpException e) {
      System.out.println(e.getLocalizedMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}