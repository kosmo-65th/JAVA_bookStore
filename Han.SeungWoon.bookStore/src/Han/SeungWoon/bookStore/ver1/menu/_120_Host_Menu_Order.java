/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : host의 주문관리 메뉴
 */

package Han.SeungWoon.bookStore.ver1.menu;

import java.util.Collection;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.user.Customer;
import Han.SeungWoon.bookStore.ver1.user.CustomerMap;
import Han.SeungWoon.bookStore.ver1.user.Host;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class _120_Host_Menu_Order {
	ReadWrite readWrite = ReadWrite.getInstance();		//입출력
	Host host = Host.getInstance();				//host 정보

	//싱글톤
	private static _120_Host_Menu_Order host_menu_order = new _120_Host_Menu_Order();
	private _120_Host_Menu_Order(){}
	public static _120_Host_Menu_Order getInstance() {
		return host_menu_order;
	}
	
	public void doing() {//동작부분
		menu :
		while(true) {
			switch(view()) {
			case 1 : orderList();
				break;
			case 2 : approval();
				break;
			case 3 : cancel();
				break;
			case 4 : sum();
				break;
			case 5 : break menu;
			}
		}
	}
	
	public int view() {//메뉴 표출 및 메뉴번호 받기
		readWrite.writeln("================= 주문관리 =================");
		readWrite.writeln("1.주문목록\t2.결제승인\t3.결제취소\t4.결산\t5.이전");
		readWrite.writeln("==========================================");
		readWrite.write("메뉴번호를 입력하세요 : ");
        return readWrite.readInt(5);
	}
	public void orderList() {//현재 주문중인 리스트 표출
		host.getSellingTree().showBookTree();
	}
	public void approval() {//결제승인
		orderList();//현재 주문중인 리스트 표출
		
		while(true) {
			//책 코드를 입력받고 sellingTree에 있는지 확인
			Book book =readWrite.readCode("승인하려는 ", host.getSellingTree());
			CustomerMap customerMap = CustomerMap.getInstance();
			if(book==null)break;// 0 입력시 탈출
			else {
				host.getSelledTree().addBookTree(book);			//host의 selledTree에 추가
				//손님마다 접근해서 방금 처리된 책 지우기
				//손님리스트 > 손님 > 구매중인tree > 책코드랑 확인하고  있으면 tree에서 제거
				Collection<Customer> customerCol= customerMap.getMap().values();
				for(Customer customer : customerCol) {
					for(Book book2 : customer.getBuyingTree().getBookTree()) {
						if(book2.getNum() == book.getNum()) {
							customer.getBuyingTree().removeBookTree(book2);	//customer buytree에서 삭제
							customer.getBuyedTree().addBookTree(book2);		//customer buyedtree에 추가
							customer.addMessage(book.getNum()+"번 책의 구매처리가 승인되었습니다");
							break;
						}
					}
				}
				readWrite.writeln(book.getNum()+"번 책의 승인처리가 완료되었습니다");//삭제 후 완료 됬다고 표출 후 다시 코드 입력받기
				host.getSellingTree().removeBookTree(book);		//host의 sellingTree에서 제거
				readWrite.writeln("승인처리 되었습니다");
			}
		}
	}

	public void cancel() {//구매중인 목록 취소시키기(결제취소)
		orderList();
		while(true) {
			CustomerMap customerMap = CustomerMap.getInstance();
			//책 번호 입력받고 SellingTree에 있는지 검사
			Book book =readWrite.readCode("취소하려는 ", host.getSellingTree());
	        if(book ==null)break ; //0 입력시 탈출
	        else {
	        	host.getStockTree().addBookTree(book);			//재고에 취소된 분량만큼 다시 추가
	        	//손님마다 접근해서 방금 처리된 책 지우기
				//손님리스트 > 손님 > 구매중인tree > 책코드랑 확인하고  있으면 tree에서 제거
				Collection<Customer> customerCol= customerMap.getMap().values();
				for(Customer customer : customerCol) {
					for(Book book2 : customer.getBuyingTree().getBookTree()) {
						if(book2.getNum() == book.getNum()) {
							customer.getBuyingTree().removeBookTree(book2);
							customer.addMessage(book.getNum()+"번 책의 구매가 취소되었습니다");
							break;
						}
					}
				}
				readWrite.writeln(book.getNum()+"번 책의 취소처리가 완료되었습니다");//취소 후 완료 됬다고 표출 후 다시 코드 입력받기
				host.getSellingTree().removeBookTree(book);	//판매중인 목록에서 지우기
				readWrite.writeln("취소처리 되었습니다");
	        }
		}
	}
	
	public void sum() {//host의 판매완료된 리스트를 보고 계산해서 총 판매금액 정산하기
		System.out.println("총 판매 금액 : "+host.getSumSelling());
	}
}
