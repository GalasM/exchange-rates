package pl.galas.app.services;

import org.springframework.stereotype.Service;
import pl.galas.app.commons.CustomHttp2Client;
import pl.galas.app.commons.Utils;
import pl.galas.app.commons.UtilsDate;
import pl.galas.app.components.HttpRequestComponent;
import pl.galas.app.enums.CurrencyEnum;
import pl.galas.app.exceptions.CustomRequestException;
import pl.galas.app.interfaces.NbpService;
import pl.galas.app.models.ExchangeRatesDTO;
import pl.galas.app.models.ExchangeRatesResponse;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NbpServiceImpl implements NbpService {
    private final CustomHttp2Client httpClient;
    private final HttpRequestComponent httpRequestComponent;

    public NbpServiceImpl(CustomHttp2Client customHttp2Client,
                          HttpRequestComponent httpRequestComponent) {
        this.httpClient = customHttp2Client;
        this.httpRequestComponent = httpRequestComponent;
    }

    @Override
    public List<ExchangeRatesDTO> getExchangeRatesByDate(Date startDate, Date endDate, CurrencyEnum currency) {
        try {
            ExchangeRatesResponse response;
            /* Move date by day for first difference  */
            startDate = UtilsDate.moveDate(Calendar.DAY_OF_YEAR, -1, startDate);
            if (UtilsDate.itExceedsNumberOfDays(startDate, endDate)) {
                response = executeMultiRequest(startDate, endDate, currency);
            } else {
                response = executeRequest(startDate, endDate, currency);
            }
            return completeExchangeRates(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Get exchange rates error");
        }
    }

    private ExchangeRatesResponse executeMultiRequest(Date startDate, Date endDate, CurrencyEnum currency) {
        ExchangeRatesResponse response;
        Map<Date, Date> datesMap = UtilsDate.choppedDate(startDate, endDate);
        List<ExchangeRatesResponse> list = new ArrayList<>();
        datesMap.forEach((start, end) -> {
            try {
                list.add(executeRequest(start, end, currency));
            } catch (CustomRequestException e) {
                e.printStackTrace();
                throw new RuntimeException("Get exchange rates error");
            }
        });
        response = list.get(0);
        response.getRates().clear();
        for (ExchangeRatesResponse res : list) {
            response.getRates().addAll(res.getRates());
        }
        return response;
    }

    private ExchangeRatesResponse executeRequest(Date startDate, Date endDate, CurrencyEnum currency) throws CustomRequestException {
        return httpClient
                .sendRequest(httpRequestComponent.getExchangeRatesByDateRequest(
                        startDate,
                        endDate,
                        currency),
                        ExchangeRatesResponse.class);
    }

    private List<ExchangeRatesDTO> completeExchangeRates(ExchangeRatesResponse response) {
        if (response != null && !Utils.isNullOrEmpty(response.getRates())) {
            List<ExchangeRatesDTO> rates = response.getRates()
                    .stream()
                    .map(ExchangeRatesDTO::new)
                    .sorted(Comparator.comparing(ExchangeRatesDTO::getEffectiveDate))
                    .collect(Collectors.toList());
            calculateDifferences(rates);
            /* Remove additional rate */
            rates.remove(0);
            return rates;
        }
        return null;
    }

    private void calculateDifferences(List<ExchangeRatesDTO> rates) {
        MathContext mc = new MathContext(5, RoundingMode.HALF_EVEN);
        BigDecimal lastAsk = new BigDecimal(0, mc);
        BigDecimal lastBid = new BigDecimal(0, mc);
        for (ExchangeRatesDTO rate : rates) {
            if (lastAsk.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal currentAsk = new BigDecimal(rate.getAsk(), mc);
                rate.setAskDifference(currentAsk.subtract(lastAsk, mc).doubleValue());
            }
            if (lastBid.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal currentBid = new BigDecimal(rate.getBid(), mc);
                rate.setBidDifference(currentBid.subtract(lastBid, mc).doubleValue());
            }
            lastAsk = new BigDecimal(rate.getAsk(), mc);
            lastBid = new BigDecimal(rate.getBid(), mc);
        }
    }
}
