package myobj.item;

/*
	이 클래스를 상속받아 아이템을 3개 만들어보세요
	※ Item클래스는 수정하지 말것
*/


abstract public class Item {

	String name;
	int price;
	
	public Item(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
	// final을 붙인 메서드는 자식 클래스에서 오버라이드를 금지시킨다
	final public void info() {
		System.out.printf("이름: %s\n", name);
		System.out.printf("가격: %d원\n", price);
	}
	
	abstract public void use();
}

