package pl.galas.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.galas.app.enums.CurrencyEnum;
import pl.galas.app.interfaces.NbpService;
import pl.galas.app.models.DatesForm;
import pl.galas.app.models.ExchangeRatesDTO;
import pl.galas.app.validators.DateValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ExchangeRatesViewController {
    private final NbpService nbpService;
    private final DateValidator dateValidator;

    public ExchangeRatesViewController(NbpService nbpService, DateValidator dateValidator) {
        this.nbpService = nbpService;
        this.dateValidator = dateValidator;
    }

    @GetMapping("/")
    public String indexGet(@ModelAttribute("datesForm") DatesForm form) {
        return "index";
    }

    @PostMapping("/usd")
    public String ratesUSDPost(@Valid @ModelAttribute("datesForm") DatesForm form, BindingResult result, Model model) {
        if (!dateValidator.isValidForm(form, result))
            return "index";
        if (result.hasErrors())
            return "index";
        List<ExchangeRatesDTO> rates = nbpService.getExchangeRatesByDate(form.getStartDate(), form.getEndDate(), CurrencyEnum.USD);
        model.addAttribute("rates", rates);
        return "ratesUsd";
    }
}
