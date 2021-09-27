package pl.galas.app.commons;

import org.springframework.stereotype.Component;
import pl.galas.app.exceptions.CustomRequestException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class CustomHttp2Client {
    private HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public <T> T sendRequest(HttpRequest request, Class<T> clazz) throws CustomRequestException {
        try {
            HttpResponse<T> response = httpClient.send(request, new JsonBodyHandler<>(clazz));
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomRequestException("Send request error");
        }
    }
}
