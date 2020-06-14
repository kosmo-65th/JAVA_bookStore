/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : 손님의 class - id, pwd, buyingTree(구매중), basketTree(장바구니), buyedTree(구매완료한), refundTree(환불처리한)
 * 		의 자료들을 가지고 있음.
 */
package Han.SeungWoon.bookStore.ver1.user;

import Han.SeungWoon.bookStore.ver1.data.BookTree;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class Customer {
	//손님의 각자 정보
	private ReadWrite readWrite = ReadWrite.getInstance();
	
	private String id;	//아이디
    private String pwd;	//비번
    
    private BookTree buyingTree;	//구매중인 book들
    private BookTree basketTree;	//장바구니에 담겨있는 book들
    private BookTree buyedTree;		//구매했던 book들
    private BookTree refundTree;	//환불했던 book들
    
    private StringBuilder message = new StringBuilder();	//메세지
    
    //생성자
    public Customer() {
    	buyingTree = new BookTree();
    	basketTree= new BookTree();
    	buyedTree = new BookTree();
    	refundTree = new BookTree();
    }
    public Customer(String id, String pwd) {
    	this();
    	this.id = id;
    	this.pwd = pwd;
    }
    //get,set
    public String getId() {
        return id;
    }
    public String getPwd() {
        return pwd;
    }
    public BookTree getBuyingTree() {
		return buyingTree;
	}
	public BookTree getBasketTree() {
		return basketTree;
	}
	public BookTree getBuyedTree() {
		return buyedTree;
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
		}
	}
	public void addMessage(String str) {
		message.append(str+"\n");
	}
	//override 관련
	
	
    @Override
    public int hashCode() {//customerMap 구현을 위한 같은거 구분
    	//id >>String 의 상수값으로 형변환 후 hashCode 값을 읽어 같은지를 구분하게됨.
        return new String(id).hashCode();
    }
    @Override
    public boolean equals(Object obj) {//customerMap 구현을 위한 같은거 구분
        if(obj instanceof Customer) {
        	Customer account = (Customer)obj;
            if(account.getId().equals(id)) return true;
            else return false;
        }else return false;
    }
}
