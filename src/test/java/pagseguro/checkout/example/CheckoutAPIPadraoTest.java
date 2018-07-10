package pagseguro.checkout.example;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Created by andvicente on 28/02/17.
 */
public class CheckoutAPIPadraoTest {

    @BeforeEach
    public void antesTestes(){
        RestAssured.baseURI = "https://ws.sandbox.pagseguro.uol.com.br";
        RestAssured.basePath = "/v2/";
    }

    @Test
    public void createCheckout(){

        Map<String, String> checkout = getCheckoutParameters();
        ValidatableResponse response =
                given().
                        contentType("application/x-www-form-urlencoded").
                        formParams(checkout).
                when().
                        post("checkout").
                then().
                        log().
                        all().
                        statusCode(200);

        String checkoutCode = response.extract().body().xmlPath().getString("checkout.code");
        System.out.println("URL Checkout: https://pagseguro.uol.com.br/v2/checkout/payment.html?code="+checkoutCode);

    }

    private Map<String, String> getCheckoutParameters() {
        Map <String,String> checkout = new HashMap<>();
        checkout.put("email","xxxxx@xxxxx.com");
        checkout.put("token","xxxxxxxx");
        checkout.put("currency","BRL");
        checkout.put("itemId1","0001");
        checkout.put("itemDescription1","Produto PagSeguroI");
        checkout.put("itemAmount1","2.99");
        checkout.put("itemQuantity1","1");
        checkout.put("itemWeight1","1000");
        checkout.put("itemId2","0002");
        checkout.put("itemDescription2","Produto PagSeguroII");
        checkout.put("itemAmount2","2.50");
        checkout.put("itemQuantity2","2");
        checkout.put("itemWeight2","750");
        checkout.put("reference","REF1234");
        checkout.put("senderName","Jose Comprador");
        checkout.put("senderAreaCode","99");
        checkout.put("senderPhone","99999999");
        checkout.put("senderEmail","comprador@uol.com.br");
        checkout.put("shippingType","1");
        checkout.put("shippingAddressStreet","Av. PagSeguro");
        checkout.put("shippingAddressNumber","9999");
        checkout.put("shippingAddressComplement","99o andar");
        checkout.put("shippingAddressDistrict","Jardim Internet");
        checkout.put("shippingAddressPostalCode","99999999");
        checkout.put("shippingAddressCity","Cidade Exemplo");
        checkout.put("shippingAddressState","SP");
        checkout.put("shippingAddressCountry","BRL");
        return checkout;
    }
}
