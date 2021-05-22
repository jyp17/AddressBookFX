package application;

public class Contact {
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private int id;
	
	public Contact() {
		this.firstName = "";
		this.lastName = "";
		this.address = "";
		this.phone = "";
	}
	
	public Contact(int id) {
		this.firstName = "";
		this.lastName = "";
		this.address = "";
		this.phone = "";
		this.id = id;
	}
	
	public Contact(String firstName, String lastName, String address, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
	}
	
	public Contact(String firstName, String lastName, String address, String phone, int id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId() {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.firstName + " " + this.lastName + "\n" + this.address + "\n" + this.phone;
	}
}
