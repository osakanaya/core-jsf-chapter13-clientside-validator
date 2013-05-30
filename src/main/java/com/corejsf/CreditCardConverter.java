package com.corejsf;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("com.corejsf.CreditCard")
public class CreditCardConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	private String separator;

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public Object getAsObject(FacesContext context, UIComponent component,
			String newValue) {
		StringBuilder builder = new StringBuilder(newValue);

		int i = 0;
		while (i < builder.length()) {
			if (Character.isDigit(builder.charAt(i))) {
				i++;
			} else {
				builder.deleteCharAt(i);
			}
		}

		return new CreditCard(builder.toString());
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (!(value instanceof CreditCard)) {
			throw new ConverterException();
		}
		String v = ((CreditCard) value).toString();

		String sep = separator;
		if (sep == null) {
			sep = " ";
		}

		int[] boundaries = null;
		int length = v.length();
		if (length == 13)
			boundaries = new int[] { 4, 7, 10 };
		else if (length == 14)
			boundaries = new int[] { 5, 9 };
		else if (length == 15)
			boundaries = new int[] { 4, 10 };
		else if (length == 16)
			boundaries = new int[] { 4, 8, 12 };
		else if (length == 22)
			boundaries = new int[] { 6, 14 };
		else
			return v;
		
		StringBuilder result = new StringBuilder();
		int start = 0;
		for (int i = 0; i < boundaries.length; i++) {
			int end = boundaries[i];
			result.append(v.substring(start, end));
			result.append(sep);
			start = end;
		}
		
		result.append(v.substring(start));
		
		return result.toString();
	}
}
