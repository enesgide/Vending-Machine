package vendingmachine;

public class Cash {

	private String name;
	private Double value;
	private Integer amount;

	public Cash() {
		this.name = null;
		this.value = null;
		this.amount = null;
	}

	public Cash(String name, Double value, Integer amount) {
		this.name = name;
		this.value = value;
		this.amount = amount;
	}

	public Cash duplicate() {
		return new Cash(this.name, this.value, this.amount);
	}

	public Integer getAmount() {
		return this.amount;
	}

	public String getName() {
		return this.name;
	}

	public Double getValue() {
		return this.value;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
