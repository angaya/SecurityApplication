package com.squaddigital.securityApp.net;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	public static AgencyList agencyList = null;

	public static AgencyList getSitesList() {
		return agencyList;
	}

	public static void setSitesList(AgencyList agencyList) {
		MyXMLHandler.agencyList = agencyList;
	}

	/** Called when tag starts ( ex:- <name>AndroidPeople</name> 
	 * -- <name> )*/
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		//if (localName.equals("agency") || localName.equals("agencyPlan") )
		if (localName.equals("agency"))
		{
			/** Start */ 
			agencyList = new AgencyList();
		}
		else if(localName.equals("agencyPlan")){
			agencyList = new AgencyList();
		} else if(localName.equals("compareplan")){
			agencyList = new AgencyList();
		} else if (localName.equals("paymentmethod")){
			agencyList = new AgencyList();
		} else if (localName.equals("registration")){
			agencyList = new AgencyList();
		} else if (localName.equals("login")){
			agencyList = new AgencyList();
		} else if (localName.equals("Client")){
			agencyList = new AgencyList();
		} else if (localName.equals("contact")){   
			agencyList = new AgencyList();
		} else if (localName.equals("changepassword")){
			agencyList = new AgencyList();
		} else if (localName.equals("details")){
			agencyList = new AgencyList();
		} else if (localName.equals("alarm")){
			agencyList = new AgencyList();
		}
			
		 
	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		/** set value */ 
		if (localName.equalsIgnoreCase("name"))
			agencyList.setName(currentValue);
		else if (localName.equalsIgnoreCase("agencyname"))
			agencyList.setWebsite(currentValue);
		else if (localName.equalsIgnoreCase("id"))
			agencyList.setId(currentValue);
		else if (localName.equalsIgnoreCase("logourl"))
			agencyList.setLogourl(currentValue);
		else if (localName.equalsIgnoreCase("email"))
			agencyList.setEmail(currentValue);
		else if (localName.equalsIgnoreCase("contactperson"))
			agencyList.setContactperson(currentValue);
		else if (localName.equalsIgnoreCase("street1"))
			agencyList.setStreet1(currentValue);
		else if (localName.equalsIgnoreCase("street2"))
			agencyList.setStreet2(currentValue);
		else if (localName.equalsIgnoreCase("postalcode"))
			agencyList.setPostalcode(currentValue);
		else if (localName.equalsIgnoreCase("city"))
			agencyList.setCity(currentValue);
		else if (localName.equalsIgnoreCase("country"))
			agencyList.setCountry(currentValue);
		else if (localName.equalsIgnoreCase("phone1"))
			agencyList.setPhone1(currentValue);
		else if (localName.equalsIgnoreCase("phone2"))
			agencyList.setPhone2(currentValue);
		else if (localName.equalsIgnoreCase("emergencynumber"))
			agencyList.setEmergencynumber(currentValue);
		else if (localName.equalsIgnoreCase("plan_name"))
			agencyList.setPlanname(currentValue);
		else if (localName.equalsIgnoreCase("period"))
			agencyList.setPeriod(currentValue);
		else if (localName.equalsIgnoreCase("price"))
			agencyList.setPrice(currentValue);
		else if (localName.equalsIgnoreCase("dollarPrice"))
			agencyList.setDollarPrice(currentValue);
		else if (localName.equalsIgnoreCase("agencyid"))
			agencyList.setAgencyid(currentValue);
		else if (localName.equalsIgnoreCase("message"))
			agencyList.setMessage(currentValue);
		else if (localName.equalsIgnoreCase("status"))
			agencyList.setStatus(currentValue);
		else if (localName.equalsIgnoreCase("count"))
			agencyList.setCount(currentValue);
		else if (localName.equalsIgnoreCase("phone"))
			agencyList.setPhone(currentValue);
		else if(localName.equalsIgnoreCase("agency_id"))
			agencyList.setAgencyid(currentValue);

	}

	/** Called to get tag characters ( ex:- <name>AndroidPeople</name> 
	 * -- to get AndroidPeople Character ) */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}

}
