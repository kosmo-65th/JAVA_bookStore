/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : Book 을 담고 있는 BookTree 자료형 선언
 */

package Han.SeungWoon.bookStore.ver1.data;

import java.util.TreeSet;

import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class BookTree{//Book을 담아내는 TreeSet과 그걸 다룰 메서드들
	private TreeSet<Book> bookTree;
	transient ReadWrite readWrite = ReadWrite.getInstance();
	
	public BookTree() {//생성자
		bookTree = new TreeSet<Book>();
	}
	
	public TreeSet<Book> getBookTree(){
		return bookTree;
	}
	//Tree 출력
	public void showBookTree() {
		readWrite.writeln("++++++++++++++++ 책 목록 ++++++++++++++++");
		readWrite.writeln("번호\t책 이름\t지은이\t가격\t수량\t");
		for(Book book : bookTree) {
			readWrite.writeln(book.toString());
		}
		readWrite.writeln("++++++++++++++++++++++++++++++++++++++++");
	}
	public void showBookTree(boolean a) {
		for(Book book : bookTree) {
			readWrite.writeln(book.toString());
		}
	}
	//Tree에 book 추가
	public void addBookTree(Book book) {
		boolean check=false;
		//원래 Tree에 있는지 확인 후 있다면 새로 입력받은 book.getBookCount()만큼 갯수 추가하기
		for(Book book2 : bookTree) {
			if(book2.getNum() == book.getNum()) {
				book2.setBookCount(book2.getBookCount()+book.getBookCount());
				check = true;
			}
		}
		//원래 Tree에 없는 거였다면 bookTree에 추가하기
		if(check==false)bookTree.add(book.copy());
	}
	//Tree에 book 삭제 (번호 받아서)
	public void removeBookTree(int i) {
		for(Book book : bookTree) {
			if(book.getNum() == i ) removeBookTree(book);
		}
	}
	//Tree에 book 삭제(Book 받아서)
	public void removeBookTree(Book book) {
		Book book2 = checkOut(book);
		//tree에 있는 book2.bookCount값이 입력 book.bookCount값보다 많으면 삭제하지 않고 갯수만 줄임
		if(book2.getBookCount()>book.getBookCount()) {
			book2.setBookCount(book2.getBookCount()-book.getBookCount());
		}
		else {//tree에 있는 book2.bookCount값이 입력 book.bookCount값과 같으면 삭제
			bookTree.remove(book);
		}
	}
	//번호를 받아서 > Tree내  book 찾고 > Book.editAll을 통해 book내용수정
	public void editBookTree(int i) {
		boolean check = false;
		for(Book book : bookTree) {
			if(book.getNum() == i) {
				book.editAll();
				check = true;
			}
		}
		if(check == false)readWrite.writeln("입력하신 책코드를 가진 책이 존재하지 않습니다.");
	}
	//Tree내에 book의 코드값과 같은 book 이 존재하는지 확인
	public boolean check(Book book) {
		for(Book book2 : bookTree) {
			if(book2.getNum() == book.getNum()) return true;
		}return false;
	}
	//Tree내에 book의 코드값과 같은 book 이 있다면 그 book 반환
	//없을 시에는 null로 반환
	public Book checkOut(Book book) {
		for(Book book2 : bookTree) {
			if(book2.getNum() == book.getNum()) return book2;
		}return null;
	}
}

