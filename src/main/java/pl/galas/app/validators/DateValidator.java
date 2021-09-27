package pl.galas.app.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import pl.galas.app.models.DatesForm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateValidator {
    private static final String DATE_OF_FIRST_QUOTATION = "2002/01/02";

    public boolean isValidForm(DatesForm form, BindingResult result) {
        if(form == null) {
            if(result != null)
                result.rejectValue("startDate", "form.error", "Form can not be null");
            return false;
        }
        if(form.getStartDate() == null || form.getEndDate() == null) {
            if(result != null)
                result.rejectValue("startDate", "form.error", "Date can not be null");
            return false;
        }
        if(form.getStartDate().after(form.getEndDate())) {
            if(result != null)
                result.rejectValue("startDate", "form.error", "Wrong date was given");
            return false;
        }
        if(form.getStartDate().before(getDateLastQuotation())) {
            if (result != null)
                result.rejectValue("startDate", "form.error", "Date before the first quotation");
            return false;
        }
        return true;
    }

    public boolean isValidForm(Date startDate) {
        if(startDate == null) {
            return false;
        }
        if(startDate.after(new Date())) {
            return false;
        }
        return !startDate.before(getDateLastQuotation());
    }

    private Date getDateLastQuotation() {

        try {
            return new SimpleDateFormat("yyyy/MM/dd").parse(DATE_OF_FIRST_QUOTATION);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date parse error");
        }
    }
}
