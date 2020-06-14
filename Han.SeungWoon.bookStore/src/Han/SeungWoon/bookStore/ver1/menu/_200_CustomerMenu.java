/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : custom의 고객메뉴
 */

package Han.SeungWoon.bookStore.ver1.menu;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.user.Host;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class _200_CustomerMenu {
	ReadWrite readWrite = ReadWrite.getInstance();	//문자 입출력
	Host host = Host.getInstance();			//host정보
	_220_Customer_basket customer_basket = _220_Customer_basket.getInstance();//하위메뉴
	
	//싱글톤
	private static _200_CustomerMenu customerMenu = new _200_CustomerMenu();
	private _200_CustomerMenu(){}
	public static _200_CustomerMenu getInstance() {
		return customerMenu;
	}
	
	public void doing() {//실행문
		menu :
		while(true) {
			switch(this.view()) {
			case 1 :
				customer_basket.doing();
				break;
			case 2 :
				buy();
				break;
			case 3 :
				refund();
				break;
			case 4 :
				buying();
				break;
			case 5 :break menu;
			}
		}
	}
	
	public int view() {//메뉴 표출 후 번호받기
		readWrite.writeln("================= 고객 메뉴 =================");
		readWrite.writeln("1.장바구니 2.바로구매  3.환불  4.구매중인것  5.로그아웃");
		readWrite.writeln("==========================================");
		readWrite.write("메뉴번호를 입력하세요 : ");
        return readWrite.readInt(5);
	}
	
	public void buy() {//직접구매
		host.getStockTree().showBookTree();//구매 가능한 재고 표출
		
		while(true) {
			//책 코드 입력받고 stock에 있는지 확인
			Book book =readWrite.readCode("구매하려는 ", host.getStockTree());
			if(book ==null)break; // 0 입력시 탈출
			else {
				while(true) {
					readWrite.write("수량을 입력하세요");
					int count = readWrite.readCount(); //수량 입력받기
					if(count<=book.getBookCount()) {	//stock에 있는 권수 보다 입력받은 값이 많은지 체크
						//손님 구매중 올리기
						Book bookcustomer = book.copy();
						bookcustomer.setBookCount(count);
						_999_Login.customer.getBuyingTree().addBookTree(bookcustomer);
						//주인 구매중 올리기
						host.getSellingTree().addBookTree(bookcustomer);
						//주인에게 메세지
						host.addMessage(_999_Login.customer.getId()+"의 "+book.getNum()+"번 책에 대한 구매요청이 있습니다");
						//구매한 량만큼 주인 stock 줄이기(재고 전부 살경우 재고에서 삭제) 
						host.getStockTree().removeBookTree(bookcustomer);
						
						readWrite.writeln("+++++구매처리가 완료되었습니다++++++++++");
						break;
					}
					else readWrite.writeln("재고보다 많이 구매하실 수 없습니다.");//stock에 있는 권수 보다 입력받은 값이 많으면 표출
				}
			}
		}
	}
	public void refund() {//환불 
		//구매했던 tree 표출
		_999_Login.customer.getBuyedTree().showBookTree();
		
		while(true) {
			//책코드 입력받고 buyedTree에 존재하는지 체크
			Book book = readWrite.readCode("환불하려는 ", _999_Login.customer.getBuyedTree());
			if(book == null)break;//0입력시 탈출
			else {
				_999_Login.customer.getRefundTree().addBookTree(book);//refundTree에 추가
				host.getStockTree().addBookTree(book);					//재고에 환불받은양 다시 추가하기
				host.getSelledTree().removeBookTree(book);					//host의 판매된 상품에서 제거
				host.getRefundTree().addBookTree(book);				//host의 환불된 목록에 추가
				
				host.addMessage(_999_Login.customer.getId()+"이 "+book.getNum()+"번 책에 대해 환불처리하였습니다");
				
				
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
				readWrite.writeln(book.getNum()+"번 책이 환불 처리 되었습니다.	");
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
				_999_Login.customer.getBuyedTree().removeBookTree(book);	//buyedTree에서 제거
			}
		}
	}
	public void buying() {
		readWrite.writeln("+++++구매중인 목록++++++++++");
		_999_Login.customer.getBuyingTree().showBookTree();
	}
}
