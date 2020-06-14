/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : host의 정보를 담고 있는 class
 * 		stockTree(재고), sellingTree(구매자들이 구매중인 book들), selledTree(판매완료한 book들), refundTree(환불처리된책들)
 * 		sumSelling(총 판매금액)을 가지고 있음/ 
 */

package Han.SeungWoon.bookStore.ver1.user;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.data.BookTree;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class Host{
	transient ReadWrite readWrite = ReadWrite.getInstance();
	//host의 아이디와 비번 상수처리 
	public static final String ID = "host";		
	public static final String PASSWORD = "host";
	private StringBuilder message = new StringBuilder();	//메세지
	private int i=0;										//파일입출력 용 메세지 갯수체크
	//싱글톤 처리
	private static Host host = new Host();
	private Host() {
		 stockTree = new BookTree();
    	 sellingTree = new BookTree();
    	 selledTree = new BookTree();
    	 refundTree = new BookTree();
	}
	static public Host getInstance() {
		return host;
	}
		
	BookTree stockTree;		//재고
	BookTree sellingTree;	//구매자들이 구매중인 book들
	BookTree selledTree;		//구매자들에게 판매한 book들
	BookTree refundTree;		//환불된 book들
	
    
  
	//Tree 정리 -첫사람꺼만 2배로 수량이 들어가는 이상한 오류 있음
//    public void treeArrangement() {
//    	CustomerMap map = CustomerMap.getInstance();
//    	for(Customer customer : map.getMap().values()) {
//    		for(Book book : customer.getBuyingTree().getBookTree()) {
//    			sellingTree.addBookTree(book);
//    		}
//    		for(Book book : customer.getBuyedTree().getBookTree()) {
//    			selledTree.addBookTree(book);
//    		}
//    		for(Book book : customer.getRefundTree().getBookTree()) {
//    			refundTree.addBookTree(book);
//    		}
//    	}
//    }
    
	//get
	public BookTree getStockTree() {
		return stockTree;
	}
	public BookTree getSellingTree() {
		return sellingTree;
	}
	public BookTree getSelledTree() {
		return selledTree;
	}
	public BookTree getRefundTree() {
		return refundTree;
	}
	//메세지 관련
	public void getMessage() {//메세지 출력
		if(message.length()!=0) {
			readWrite.writeln("-------------알림-----------------");
			readWrite.write(message.toString());
			readWrite.writeln("---------------------------------");
			message.setLength(0);//message초기화
			i=0;
		}
	}
	public int getI() {
		return i;
	}
	public String MessageOut() {
		return message.toString();
	}
	public void addMessage(String str) {
		message.append(str+"\n");
		i++;
	}

	// 결산
	private int sumSelling=0;
	public int getSumSelling() {
		sumSelling=0;
		for(Book book : selledTree.getBookTree()) {
			sumSelling += book.getBookCount() * book.getBookPrice();
		}
		return sumSelling;
	}
}
