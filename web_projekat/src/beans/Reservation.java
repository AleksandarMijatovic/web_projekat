package beans;

public class Reservation {
	int id;
	private Apartment appartment;
	private Guest guest;
	private long startDate;
	private int daysForStay = 1;
	private double price;
	private String message;
	
	private ReStatus status;
	
	public Reservation() {}
	
	public Reservation(int id, Apartment appartment, long startDate, int daysForStay, double price, String message,
			ReStatus status, Guest guest) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.daysForStay = daysForStay;
		this.price = price;
		this.message = message;
		this.status = status;
		this.appartment = appartment;
		this.guest = guest;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public Apartment getAppartment() {
		return appartment;
	}

	public void setAppartment(Apartment appartment) {
		this.appartment = appartment;
	}

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}

	
	public long getStartDate() {
		return startDate;
	}


	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}


	public int getDaysForStay() {
		return daysForStay;
	}


	public void setDaysForStay(int daysForStay) {
		this.daysForStay = daysForStay;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	

	public ReStatus getStatus() {
		return status;
	}


	public void setStatus(ReStatus status) {
		this.status = status;
	}
	
}
