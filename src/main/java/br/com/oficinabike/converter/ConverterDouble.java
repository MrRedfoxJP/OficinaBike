package br.com.oficinabike.converter;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.com.oficinabike.util.JSFUtil;

@FacesConverter(value = "converterDouble")
public class ConverterDouble implements Converter {

    private DecimalFormat formatoMonetario = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {

            if (value == null || value.trim().equals("")) {
                return 0.00;
            }
            value = formatoMonetario.format(formatoMonetario.parse(value));
            Boolean matcher = Pattern.matches("^\\$?[0-9]+(.[0-9]{3})*(\\,[0-9]{2})?$", value);
            if (matcher) {
                return formatoMonetario.parse(value);
            } else {
                JSFUtil.addErrorMessage("Formato Invalido, Ex: 1234,56");
                throw new ConverterException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0D;
    }

    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object == null || object.toString().trim().equals("") || object.toString().trim().equals("0.00") || object.toString().trim().equals("0,00")) {
            return "0,00";
        } else {
            formatoMonetario.setMaximumFractionDigits(2);
            return formatoMonetario.format(Double.parseDouble(object.toString().trim()));
        }
    }

}
