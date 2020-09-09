package beans;

public class Comment {
	private int id;
	private String guest;
	private Apartment forApartment;
	private String text;
	private int grade;
	private boolean visibleForGuest = false;

	public Comment() {
		
	}

	public Comment(int id, String guest, Apartment forApartment, String text, int grade) {
		super();
		this.id = id;
		this.guest = guest;
		this.forApartment = forApartment;
		this.text = text;
		this.grade = grade;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public Apartment getForApartment() {
		return forApartment;
	}

	public void setForApartment(Apartment forApartment) {
		this.forApartment = forApartment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public boolean isVisibleForGuest() {
		return visibleForGuest;
	}

	public void setVisibleForGuest(boolean visibleForGuest) {
		this.visibleForGuest = visibleForGuest;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
