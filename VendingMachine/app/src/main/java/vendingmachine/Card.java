package vendingmachine;

public class Card {

	private String name;
	private String number;

	public Card() {
		this.name = null;
		this.number = null;
	}

	public Card(String name, String number) {
		this.name = name;
		this.number = number;
	}

	public String getName() {
		return this.name;
	}

	public String getNumber() {
		return this.number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
