package pl.galas.app.interfaces;

import pl.galas.app.enums.CurrencyEnum;
import pl.galas.app.models.ExchangeRatesDTO;

import java.util.Date;
import java.util.List;

public interface NbpService {
    List<ExchangeRatesDTO> getExchangeRatesByDate(Date startDate, Date endDate, CurrencyEnum currency);
}
