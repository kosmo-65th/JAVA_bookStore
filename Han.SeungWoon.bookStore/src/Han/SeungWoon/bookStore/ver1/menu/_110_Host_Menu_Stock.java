/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : host의 재고관리 메뉴
 */

package Han.SeungWoon.bookStore.ver1.menu;

import java.util.Collection;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.user.Customer;
import Han.SeungWoon.bookStore.ver1.user.CustomerMap;
import Han.SeungWoon.bookStore.ver1.user.Host;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class _110_Host_Menu_Stock {
	ReadWrite readWrite = ReadWrite.getInstance();	//문자 입출력
	Host host = Host.getInstance();			//hostImpl의 정보
	//싱글톤
	private static _110_Host_Menu_Stock host_Menu_Stock = new _110_Host_Menu_Stock();
	private _110_Host_Menu_Stock() {}
	public static _110_Host_Menu_Stock getInstance() {
		return host_Menu_Stock;
	}
	
	public void doing() {//실행문
		menu :
		while(true) {
			switch(this.view()) {
			case 1 :
				list();
				break;
			case 2 :
				add();
				break;
			case 3 :
				edit();
				break;
			case 4 :
				delete();
				break;
			case 5 : break menu;
			}
		}
	}
	
	public int view() {//메뉴 출력하고 번호 받기
		 readWrite.writeln("================= 재고관리 =================");
		 readWrite.writeln("    1.목록    2.추가    3.수정    4.삭제    5.이전");
		 readWrite.writeln("==========================================");
		 readWrite.write("메뉴번호를 입력하세요 : ");
         return readWrite.readInt(5);
	}
	
	public void list() {//재고 리스트 출력
		host.getStockTree().showBookTree();
	}
	public void add() {//재고에 book 추가
	   String name, writer;
       int price, count;
       
       readWrite.write("책 이름 :");
       name =readWrite.read();
       readWrite.write("글쓴이 :");
       writer = readWrite.read();
       price = readWrite.readPrice();
       count = readWrite.readCount();
       
       host.getStockTree().addBookTree(new Book(name, writer,price,count));
       readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
       readWrite.writeln("        책이 등록되었습니다.");
       readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
	}
	public void edit() {//재고에 있는 책 코드 입력받아서
		//있으면 수정하고 다시 코드 입력해서 계속 수정할 수 있게함
		//없으면 다시 코드 입력시키게 하고 or 0입력하면 이전메뉴로
		while(true) {
			Book book = readWrite.readCode("수정하려는 ", host.getStockTree());
			if(book==null)break;
			else {
				boolean check = false;//손님들 장바구니에서 수정이 이루어진 경우에만 출력문 출력되게 하기 위한 변수
				//수정작업에 들어간 책코드를 손님들 장바구니에서 삭제하기
				//손님들> 손님>basketTree > 책코드를 가지고 있을 경우 삭제 
				CustomerMap customerMap = CustomerMap.getInstance();
				Collection<Customer> customerCol= customerMap.getMap().values();
				for(Customer customer : customerCol) {
					for(Book book2 : customer.getBasketTree().getBookTree()) {
						if(book2.getNum() == book.getNum()) {
							customer.addMessage(book2.getNum()+"번 책의 정보변경으로 장바구니에서 삭제되었습니다.");
							customer.getBasketTree().removeBookTree(book2);
							check =true;
							break;
						}
					}
				}
				book.editAll();	//입력받은 책코드의 정보 수정
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
				readWrite.writeln(book.getNum()+"번 책 정보가 수정되었습니다.");
			    if(check==true)readWrite.writeln("수정된 책이 손님들의 장바구니에 있을 경우 장바구니에서 삭제하였습니다");
			    readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
			}
		}
	}
	public void delete() {
		//책코드를 입력받아서 있으면 재고에서 삭제
		//없으면 다시 입력하게, 0입력하면 이전메뉴
		while(true) {
			boolean check = false;
			Book book = readWrite.readCode("삭제하려는 ", host.getStockTree());
			if(book==null)break;
			else {
				host.getStockTree().removeBookTree(book);	//재고에서 삭제
				//손님중 삭제한 책을 장바구니에 가지고 있을 경우 장바구니에서 삭제 
				CustomerMap customerMap = CustomerMap.getInstance();
				Collection<Customer> customerCol= customerMap.getMap().values();
				for(Customer customer : customerCol) {
					for(Book book2 : customer.getBasketTree().getBookTree()) {
						if(book2.getNum() == book.getNum()) {
							customer.addMessage(book2.getNum()+"번 책의 정보변경으로 장바구니에서 삭제되었습니다.");
							customer.getBasketTree().removeBookTree(book2);
							break;
						}
					}
				}
				readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
			    readWrite.writeln(book.getNum()+"번 책이 삭제되었습니다.");
			    if(check==true)readWrite.writeln("삭제된 책이 손님들의 장바구니에 있을경우  장바구니에서 삭제하였습니다");
			    readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
			}
		}
	}
}
