package com.corejsf;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.corejsf.util.Messages;

@FacesValidator("com.corejsf.CreditCard")
public class CreditCardValidator implements Validator, Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private String arg;

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null)
			return;

		String cardNumber;
		if (value instanceof CreditCard) {
			cardNumber = value.toString();
		} else {
			cardNumber = getDigitsOnly(value.toString());
		}

		if (!luhnCheck(cardNumber)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					getErrorMessage(value, context), null);
			throw new ValidatorException(message);
		}
	}
	
	public String getErrorMessage(Object value, FacesContext context) {
		Object[] params = new Object[] { value };
		if (message == null) {
			return Messages.getString("com.corejsf.messages", "badLuhnCheck", params);
		} else {
			Locale locale = context.getViewRoot().getLocale();
			MessageFormat formatter = new MessageFormat(message, locale);
			
			return formatter.format(params);
		}
	}

	private static boolean luhnCheck(String cardNumber) {
		int sum = 0;

		for (int i = cardNumber.length() - 1; i >= 0; i -= 2) {
			sum += Integer.parseInt(cardNumber.substring(i, i + 1));
			if (i > 0) {
				int d = 2 * Integer.parseInt(cardNumber.substring(i - 1, i));
				if (d > 9)
					d -= 9;
				sum += d;
			}
		}

		return sum % 10 == 0;
	}

	private static String getDigitsOnly(String s) {
		StringBuilder builder = new StringBuilder();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				builder.append(c);
			}
		}

		return builder.toString();
	}

}
