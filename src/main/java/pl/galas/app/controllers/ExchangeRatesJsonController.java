package pl.galas.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.galas.app.commons.UtilsDate;
import pl.galas.app.enums.CurrencyEnum;
import pl.galas.app.interfaces.NbpService;
import pl.galas.app.models.ExchangeRatesDTO;
import pl.galas.app.validators.DateValidator;

import java.util.Date;
import java.util.List;

@Controller
public class ExchangeRatesJsonController {
    private final NbpService nbpService;
    private final DateValidator dateValidator;

    public ExchangeRatesJsonController(NbpService nbpService, DateValidator dateValidator) {
        this.nbpService = nbpService;
        this.dateValidator = dateValidator;
    }

    @GetMapping(value = "/usd-json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRates(@RequestParam("startDate") String startDateStr) {
        try {
            Date startDate = UtilsDate.stringToDate(startDateStr);
            if (!dateValidator.isValidForm(startDate))
                return ResponseEntity.badRequest().body("Invalid date");
            List<ExchangeRatesDTO> rates = nbpService.getExchangeRatesByDate(startDate, new Date(), CurrencyEnum.USD);
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
