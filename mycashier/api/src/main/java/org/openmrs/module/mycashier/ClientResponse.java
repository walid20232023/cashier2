package org.openmrs.module.mycashier;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.ParseException;

public class ClientResponse {
	
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
	
	private Integer id;
	
	private String name;
	
	private String firstnames;
	
	private Date birthDate;
	
	private Integer age;
	
	private String sex;
	
	private String address;
	
	private String telephone;
	
	private String assurance1;
	
	private String assurance2;
	
	private String assurance3;
	
	// Constructeurs
	public ClientResponse() {
	}
	
	// Getters and Setters
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFirstnames() {
		return firstnames;
	}
	
	public void setFirstnames(String firstnames) {
		this.firstnames = firstnames;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public void setAssurance1(String assurance1) {
		this.assurance1 = assurance1;
	}
	
	public void setAssurance2(String assurance2) {
		this.assurance2 = assurance2;
	}
	
	public void setAssurance3(String assurance3) {
		this.assurance3 = assurance3;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getAssurance1() {
		return assurance1;
	}
	
	public String getAssurance2() {
		return assurance2;
	}
	
	public String getAssurance3() {
		return assurance3;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public static ClientResponse clientToResponse(Client client) {
		
		ClientResponse clientResponse = new ClientResponse();
		
		clientResponse.setId(client.getId());
		clientResponse.setName(client.getName());
		clientResponse.setFirstnames(client.getFirstnames());
		clientResponse.setBirthDate(client.getBirthDate());
		clientResponse.setSex(client.getSex());
		
		if (client.getAssuranceList() != null && !client.getAssuranceList().isEmpty()) {
			clientResponse.setAssurance1(client.getAssuranceList().get(0).getName());
		} else {
			clientResponse.setAssurance1(null);
		}
		
		if (client.getAssuranceList() != null && client.getAssuranceList().size() > 1) {
			clientResponse.setAssurance2(client.getAssuranceList().get(1).getName());
		} else {
			clientResponse.setAssurance2(null);
		}
		
		if (client.getAssuranceList() != null && client.getAssuranceList().size() > 2) {
			clientResponse.setAssurance3(client.getAssuranceList().get(2).getName());
		} else {
			clientResponse.setAssurance3(null);
		}
		
		return clientResponse;
		
	}
	
	public static Date convertStringToDate(String dateString) throws ParseException {
		return DATE_FORMAT.parse(dateString);
	}
	
}
