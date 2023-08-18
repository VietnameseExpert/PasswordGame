import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    //    public class Car {
//        String brand;
//    }
    public static boolean rule_11 (String password) throws IOException, InterruptedException {

//        gettodaydate -> yyyy-mm-dd

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
//        System.out.println(dateFormat.format(date));

        String API_URL = "https://neal.fun/api/password-game/wordle?date=" + dateFormat.format(date);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) { // successful
            System.out.println(response.body());

            ObjectMapper mapper = new ObjectMapper();
            Answer answer = mapper.readValue(response.body(), new TypeReference<Answer>() {
            });

            return password.contains(answer.getAnswer());
        }
        return false;
    }
}
