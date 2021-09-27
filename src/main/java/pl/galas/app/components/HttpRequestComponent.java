package pl.galas.app.components;

import org.springframework.stereotype.Component;
import pl.galas.app.enums.CurrencyEnum;
import pl.galas.app.exceptions.CustomRequestException;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Date;

@Component
public class HttpRequestComponent {

    private final UrlComponent urlComponent;

    public HttpRequestComponent(UrlComponent urlComponent) {
        this.urlComponent = urlComponent;
    }

    public HttpRequest getExchangeRatesByDateRequest(Date startDate, Date endDate, CurrencyEnum currency) throws CustomRequestException {
        return createRequest(urlComponent.getExchangeRatesByDateUrl(startDate, endDate, currency));
    }

    private HttpRequest createRequest(String url) throws CustomRequestException {
        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomRequestException("Create request error");
        }
    }


}
