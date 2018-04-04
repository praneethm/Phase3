package water;

 class Bottle{
	
	private int volume;
	private int capacity;
	
	
	public int getCapacity() {
		return capacity;
	}



	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}



	public static void main(String[] args) {
		
		
	}
	
	
	
	public void addWater(int quantity) {
		
		if((this.volume+quantity)<capacity) {
			volume+=quantity;
		}
		
	}
	
	public void removeWater(int quantity) {
		if((this.volume+quantity)>capacity) {
			this.volume=0;
		}
		else {
			
		}
		
	}
	
	

}

class WaterBottle extends Bottle{
	
}

product-
id
name


inventory
id-product
quantity


client
id-name


sales
id-client
id-product
quantiy

select unique(product), sum(quantity) from sales;


