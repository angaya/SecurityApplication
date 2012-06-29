package com.squaddigital.securityApp.net;

import java.util.ArrayList;

public class AgencyList {
	
	private ArrayList<String> name = new ArrayList<String>();
	private ArrayList<String> userName = new ArrayList<String>();
	private ArrayList<String> id = new ArrayList<String>();
	private ArrayList<String> street2 = new ArrayList<String>();
	private ArrayList<String> email = new ArrayList<String>();
	private String contactperson = new String();
	private ArrayList<String> street1 = new ArrayList<String>();
	private ArrayList<String> postalcode = new ArrayList<String>();
	private ArrayList<String> city = new ArrayList<String>();
	private ArrayList<String> country = new ArrayList<String>();
	private ArrayList<String> phone1 = new ArrayList<String>();
	private ArrayList<String> phone2 = new ArrayList<String>();
	private ArrayList<String> emergencynumber = new ArrayList<String>();
	private ArrayList<String> agencyname = new ArrayList<String>();
	private ArrayList<String> category = new ArrayList<String>();
	private ArrayList<String> logourl = new ArrayList<String>();
	private ArrayList<String> planname = new ArrayList<String>();
	private ArrayList<String> agencyid = new ArrayList<String>();
	private ArrayList<String> period = new ArrayList<String>();
	private ArrayList<String> price = new ArrayList<String>();
	private ArrayList<String> dollarPrice = new ArrayList<String>();
    private ArrayList<String> paymentMethod = new ArrayList<String>();
    private ArrayList<String> message = new ArrayList<String>();
    private ArrayList<String> status = new ArrayList<String>();
    private ArrayList<String> count = new ArrayList<String>();
    private ArrayList<String> phone = new ArrayList<String>();
    
    public ArrayList<String> getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone.add(phone);
	}
    
    public ArrayList<String> getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count.add(count);
	}
    
    public ArrayList<String> getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status.add(status);
	}
    
    public ArrayList<String> getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message.add(message);
	}
	
	public void setAgencyid(String agencyid) {
		this.agencyid.add(agencyid);
	}

	public ArrayList<String> getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price.add(price);
	}
	
	public ArrayList<String> getDollarPrice() {
		return dollarPrice;
	}

	public void setDollarPrice(String dollarPrice) {
		this.dollarPrice.add(dollarPrice);
	}
	
	
	public ArrayList<String> getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod.add(paymentMethod);
	}
	
	public ArrayList<String> getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period.add(period);
	}
	
	public ArrayList<String> getAgencyid() {
		return agencyid;
	}

	public ArrayList<String> getPlanname() {
		return planname;
	}

	public void setPlanname(String planname) {
		this.planname.add(planname);
	}

	public ArrayList<String> getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		logourl = logourl.replaceAll("\\s+", "%20");
		this.logourl.add(logourl);
	}
	public ArrayList<String> getId() {
		return id;
	}

	public void setId(String id) {
		this.id.add(id);  
	}

	public ArrayList<String> getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2.add(street2);
	}

	public ArrayList<String> getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email.add(email);   
	}

	public String getContactperson() {
		return contactperson;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public ArrayList<String> getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1.add(street1);
	}

	public ArrayList<String> getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode.add(postalcode);
	}

	public ArrayList<String> getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city.add(city);
	}

	public ArrayList<String> getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country.add(country);
	}

	public ArrayList<String> getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1.add(phone1);
	}

	public ArrayList<String> getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2.add(phone2);
	}

	public ArrayList<String> getEmergencynumber() {
		return emergencynumber;
	}

	public void setEmergencynumber(String emergencynumber) {
		this.emergencynumber.add(emergencynumber);
	}

	public ArrayList<String> getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName.add(userName);
	}
	
	public ArrayList<String> getName() {
		return name;
	}

	public void setName(String name) {
		this.name.add(name);
	}

	public ArrayList<String> getAgencyname() {
		return agencyname;
	}

	public void setWebsite(String agencyname) {
		this.agencyname.add(agencyname);
	}

	public ArrayList<String> getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category.add(category);
	}

}
