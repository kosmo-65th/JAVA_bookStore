/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : custom의 장바구니 메뉴
 */

package Han.SeungWoon.bookStore.ver1.menu;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.user.Host;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class _220_Customer_basket {
	ReadWrite readWrite = ReadWrite.getInstance();		//입출력
	Host host = Host.getInstance();				//host 정보
	//싱글톤
	private static _220_Customer_basket customer_basket = new _220_Customer_basket();
	private _220_Customer_basket(){}
	public static _220_Customer_basket getInstance() {
		return customer_basket;
	}
	
	public void doing() {//실행문
		menu :
		while(true) {
			switch(this.view()) {
			case 1 :
				add();
				break;
			case 2 :
				delete();
				break;
			case 3 :
				buy();
				break;
			case 4 :break menu;
			}
		}
	}
	
	public int view() {//장바구니 표출 후 메뉴 표출 및 메뉴번호 입력
		show();
		readWrite.writeln("================= 장바구니 =================");
		readWrite.writeln("1.추가\t2.삭제\t3.구매\t4.이전");
		readWrite.writeln("==========================================");
		readWrite.write("메뉴번호를 입력하세요 : ");
		return readWrite.readInt(4);
	}
	public void show() {//장바구니 목록 출력하게 하기
		readWrite.writeln("=============장바구니 목록=================");
		_999_Login.customer.getBasketTree().showBookTree();
	}
	
	public void add() {//장바구니에 담기
		host.getStockTree().showBookTree();//장바구니에 담을 수 있는 재고 출력
		
		while(true) {//책 코드 입력받아서 재고에 있을 시 장바구니에 추가
			Book book = readWrite.readCode("장바구니에 넣을 ", host.getStockTree());
			if(book == null) break;//0번 누르면 탈출
			else if(_999_Login.customer.getBasketTree().check(book)) {
				//이미 장바구니에 있는지 확인 있다면 갯수를 다시 입력받아서 저장
				Book book2 = _999_Login.customer.getBasketTree().checkOut(book);
				readWrite.writeln("장바구니에 이미 존재하는 책코드 입니다");
				readWrite.writeln("입력하실 갯수로 갯수를 변경하겠습니다.");
				int count = readWrite.readCount();
				if(count<=book.getBookCount()) {
					book2.setBookCount(count);
					readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
					readWrite.writeln("\t\t장바구니에 추가 되었습니다.");
					readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
				}
				else  readWrite.writeln("창고 잔여량보다 많이 입력하실 수 없습니다");
				//재고 갯수 넘게 입력시 코드 번호부터 다시 입력받게
			}
			else {//장바구니에 추가
				int count = readWrite.readCount();
				
				if(count<=book.getBookCount()) {//갯수는 재고의 갯수를 넘어설 수  없음.
					Book bookCart = book.copy();
					bookCart.setBookCount(count);// book의 copy를 통해 인스턴스 복제 후 bookCount 만 count입력받은 값으로 변경
					_999_Login.customer.getBasketTree().addBookTree(bookCart);// 복제된 book을 장바구니에 추가
					
					readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
					readWrite.writeln("\t\t장바구니에 추가 되었습니다.");
					readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
				}
				else readWrite.writeln("창고 잔여량보다 많이 입력하실 수 없습니다");
				//재고 갯수 넘게 입력시 코드 번호부터 다시 입력받게
			}
		}
	}
	public void delete() {// 코드 번호 입력받아서 장바구니에 있을 시 삭제
		while(true) {
			Book book = readWrite.readCode("장바구니에서 삭제하려는 ", _999_Login.customer.getBasketTree());
			if(book== null) break;//0번 누르면 탈출
			else {
				_999_Login.customer.getBasketTree().removeBookTree(book);
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
				readWrite.writeln("\t\t삭제 되었습니다.");
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
			}
		}
	}
	public void buy() {//장바구니에 있던 것을 구매
		while(true) {
			// 책코드를 입력받아서 장바구니에 있는지 확인
			Book book = readWrite.readCode("장바구니에서 구매하려는 ", _999_Login.customer.getBasketTree());
			if(book== null) break;//0번 누르면 탈출
			else if (book.getBookCount()>host.getStockTree().checkOut(book).getBookCount()) {
				//장바구니에 담아놓은 것을 누가 다 사버려서 재고량의 변동으로 구매할 수 없을 때를 위한 체크
				readWrite.writeln("재고량 변화로 인해 구매하실 수 없습니다.");
				_999_Login.customer.getBasketTree().removeBookTree(book);	//장바구니 목록에서 삭제
				readWrite.writeln("장바구니에서 해당 책 정보를 삭제하겠습니다");
			}
			else {//구매 중인 목록으로 가기
				host.getSellingTree().addBookTree(book);				//host의 SellingTree에 추가
				host.getStockTree().removeBookTree(book);					//host의재고에서 삭제하기
				_999_Login.customer.getBuyingTree().addBookTree(book);//구매중인목록에 추가
				host.addMessage(_999_Login.customer.getId()+"의 "+book.getNum()+"번 책에 대한 구매요청이 있습니다");
				_999_Login.customer.getBasketTree().removeBookTree(book);	//장바구니목록에서 삭제
				
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
				readWrite.writeln("\t\t구매 요청 되었습니다.");
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
			}
		}
	}
}
