package pl.galas.app.components;

import org.springframework.stereotype.Component;
import pl.galas.app.commons.Const;
import pl.galas.app.commons.UtilsDate;
import pl.galas.app.enums.CurrencyEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UrlComponent {
    public String getExchangeRatesByDateUrl(Date startDate, Date endDate, CurrencyEnum currency) {
        Map<String, String> params = new HashMap<>();
        params.put("startDate", UtilsDate.getDateYYYYMMDD(startDate));
        params.put("endDate", UtilsDate.getDateYYYYMMDD(endDate));
        params.put("currency", currency.name());
        return completePathParam(Const.NBP_EXCHANGE_RATES_BY_DATE, params);
    }

    private String completePathParam(String url, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String param = "{" + entry.getKey() + "}";
            if (url.contains(param))
                url = url.replace(param, entry.getValue());
        }
        return url;
    }
}
