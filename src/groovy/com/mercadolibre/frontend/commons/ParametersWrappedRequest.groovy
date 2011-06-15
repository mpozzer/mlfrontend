package com.mercadolibre.frontend.commons

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class ParametersWrappedRequest extends HttpServletRequestWrapper {
		
    private final Map<String, String[]> modifiableParameters;
		
	public ParametersWrappedRequest(final HttpServletRequest request) {
	  super(request);
	  modifiableParameters = new TreeMap<String, String[]>();
	}
	
	public void addParameter(String name, String value) {
	  String[] values = [value];
	  modifiableParameters.put(name, values);
	}
	
	@Override
	public String getParameter(final String name) {
	  String[] strings = getParameterMap().get(name);
	  if (strings != null) {
	    return strings[0];
	  }
	  return null;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
	  Map<String, String[]> allParameters = new HashMap<String, String[]>();
	  allParameters.putAll(super.getParameterMap());
	  allParameters.putAll(modifiableParameters);
	  return Collections.unmodifiableMap(allParameters);
	}
	
	@Override
	public Enumeration<String> getParameterNames() {
	  return Collections.enumeration(getParameterMap().keySet());
	}
	
	@Override
	public String[] getParameterValues(final String name) {
	  return getParameterMap().get(name);
	}
		
}
