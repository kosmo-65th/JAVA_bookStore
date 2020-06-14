package Han.SeungWoon.bookStore.ver1.menu;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.data.BookTree;
import Han.SeungWoon.bookStore.ver1.user.Customer;
import Han.SeungWoon.bookStore.ver1.user.CustomerMap;
import Han.SeungWoon.bookStore.ver1.user.Host;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class _130_Host_Menu_OrderId {
	ReadWrite readWrite = ReadWrite.getInstance();	//문자 입출력
	CustomerMap customerMap = CustomerMap.getInstance();
	Host host = Host.getInstance();
	//싱글톤
	private static _130_Host_Menu_OrderId host_Menu = new _130_Host_Menu_OrderId();
	private _130_Host_Menu_OrderId(){}
	public static _130_Host_Menu_OrderId getInstance() {
		return host_Menu;
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
	public void orderList() {//현재 주문중인 리스트 표출(아이디별)
		CustomerMap customerMap = CustomerMap.getInstance();
		for(Customer customer : customerMap.getMap().values()) {
			BookTree buying = customer.getBuyingTree();
			if(buying.getBookTree().size()>0) {
				readWrite.writeln("***"+customer.getId()+"의 주문상품***");
				buying.showBookTree(true);
			}
		}
	}
	public void approval() {//결제승인
		orderList();//현재 주문중인 리스트 표출
		run : 
		while(true) {
			readWrite.write("결제승인할 아이디를 입력하세요[뒤로가기 0] : ");
			String id = readWrite.read();
			boolean check=customerMap.getMap().containsKey(id);
			if(id.equals("0"))break;
			else if(check==false)readWrite.writeln("없는아이디 입니다 다시입력하십니오");
			else if(check) {
				Customer customer = customerMap.getMap().get(id);
				while(true) {
					Book book =readWrite.readCode("승인하려는 ", customer.getBuyingTree());
					if(book==null)break run;// 0 입력시 탈출
					else {
						host.getSellingTree().removeBookTree(book);		//host의 sellingTree에서 제거
						host.getSelledTree().addBookTree(book);			//host의 selledTree에 추가
						customer.getBuyedTree().addBookTree(book);		//custoemr 구매한tree에 추가
						customer.addMessage(book.getNum()+"번 책의 구매처리가 승인되었습니다");
						customer.getBuyingTree().removeBookTree(book);	//customer 구매중tree에서 제거
						readWrite.writeln("승인처리 되었습니다");
					}
				}
			}
		}
	}
		
		

	public void cancel() {//구매중인 목록 취소시키기(결제취소)
		orderList();
		run : 
		while(true) {
			readWrite.write("결제승인할 아이디를 입력하세요[뒤로가기 0] : ");
			String id = readWrite.read();
			boolean check=customerMap.getMap().containsKey(id);
			if(id.equals("0"))break;
			else if(check==false)readWrite.writeln("없는아이디 입니다 다시입력하십니오");
			else if(check) {
				Customer customer = customerMap.getMap().get(id);
				Book book =readWrite.readCode("승인하려는 ", customer.getBuyingTree());
				if(book==null)break run;// 0 입력시 탈출
				else {
					host.getSellingTree().removeBookTree(book);		//host의 sellingTree에서 제거
					host.getStockTree().addBookTree(book);			//host 재고에 다시 추가
					customer.addMessage(book.getNum()+"번 책의 구매처리가 취소되었습니다");
					customer.getBuyingTree().removeBookTree(book);	//customer 구매중tree에서 제거
					readWrite.writeln("취소처리 되었습니다");
				}
			}
		}
	}
	
	public void sum() {//host의 판매완료된 리스트를 보고 계산해서 총 판매금액 정산하기
		System.out.println("총 판매 금액 : "+host.getSumSelling());
	}
}
