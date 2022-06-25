package example.aws.api.dynamodb.product.sdk.app;


import com.amazonaws.opensdk.config.ConnectionConfiguration;
import com.amazonaws.opensdk.config.TimeoutConfiguration;
import example.aws.api.dynamodb.product.sdk.DynamoDBProductSdk;
import example.aws.api.dynamodb.product.sdk.model.PostApiRootRequest;
import example.aws.api.dynamodb.product.sdk.model.PostApiRootResult;
import example.aws.api.dynamodb.product.sdk.model.ProductModel;
import example.aws.api.dynamodb.product.sdk.model.ProductModelResponse;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    private DynamoDBProductSdk sdkClient;

    App(){
        initSdk();
    }

    public static void main( String[] args ) {
        App client = new App();
        sendPost(client);
    }

    private static void sendPost(App client) {
        Scanner in = new Scanner(System.in);
        System.out.print("product_id: ");
        Integer id = Integer.parseInt(in.nextLine());
        System.out.print("name: ");
        String name = in.nextLine();
        System.out.print("picture_url: ");
        String pictureUrl = in.nextLine();
        System.out.print("price: ");
        Double price = Double.parseDouble(in.nextLine());

        ProductModelResponse response = client.getResultByPostInputBody(id, name, pictureUrl, price);

        System.out.println("received response.");
        System.out.println("statusCode: " + response.getStatusCode());
        System.out.println("message: " + response.getMessage());
        System.out.println("body: " + formJsonString(response));
    }

    private static String formJsonString(ProductModelResponse response){
        return "{\n" +
                "\t\"statusCode\": " + response.getStatusCode() + ",\n" +
                "\t\"message\": \"" + response.getMessage() + "\",\n" +
                "\t\"body\": " + "{\n" +
                "\t\t\"product_id\": " + response.getBody().getProductId().getN() + ",\n" +
                "\t\t\"name\": \"" + response.getBody().getName().getS() + "\",\n" +
                "\t\t\"picture_url: \"" + response.getBody().getPictureUrl().getS() + "\",\n" +
                "\t\t\"price\": " + response.getBody().getPrice().getN() + "\n" +
                "\t}\n" +
                "}\n";
    }

    // The configuration settings are for illustration purposes and may not be a recommended best practice.
    private void initSdk() {

        sdkClient = DynamoDBProductSdk.builder()
                .connectionConfiguration(
                        new ConnectionConfiguration()
                                .maxConnections(100)
                                .connectionMaxIdleMillis(1000))
                .timeoutConfiguration(
                        new TimeoutConfiguration()
                                .httpRequestTimeout(3000)
                                .totalExecutionTimeout(10000)
                                .socketTimeout(2000))
                .build();

    }
    // Calling shutdown is not necessary unless you want to exert explicit control of this resource.
    public void shutdown() {
        sdkClient.shutdown();
    }

    public ProductModelResponse getResultByPostInputBody(Integer id, String name, String pictureUrl, Double price) {
        PostApiRootResult postResult = sdkClient.postApiRoot(
                new PostApiRootRequest().productModel(
                        new ProductModel()
                                .productId(id)
                                .name(name)
                                .pictureUrl(pictureUrl)
                                .price(price)
                ));
        return postResult.getProductModelResponse();
    }


}
