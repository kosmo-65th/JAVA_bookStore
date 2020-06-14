/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : Book 자료형 class
 */
package Han.SeungWoon.bookStore.ver1.data;

import java.util.HashSet;
import java.util.Random;

import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class Book implements Comparable<Book>,Cloneable{
	private int bookNum;			//책번호 -> Random() 사용
	private String bookName; 		//책명
	private String bookWriter;		//책지은이
	private int bookPrice;			//가격
	private int bookCount;			//수량
	
	transient ReadWrite readWrite = ReadWrite.getInstance();//문자입출력, 직렬화에서 제외
	
	//넘버링과 생성자
	//>new book을 하게되면 번호가 랜덤하게 배정되고, private static인 numbering에 그 번호를 저장해둠.
	//새로운 번호를 배정할 때마다 numbering에 있는 번호인지 확인해보고 배정하게됨.
	private static HashSet<Integer> numbering = new HashSet<Integer>();
    private Random random = new Random();
    public Book() {
        this.bookNum = getNumbering();
    }
    private int getNumbering() {//번호 배정 메서드
        while(true) {
            int temp = random.nextInt(9999);
            if(numbering.contains(temp)==false) {
            	numbering.add(temp);
                return temp;
            }else continue;
        }
    }
	//매개변수를 통한 생성자
	public Book(String bookName, String bookWriter, int bookPrice, int bookCount) {
		this();
		this.bookName = bookName;
		this.bookWriter = bookWriter;
		this.bookPrice = bookPrice;
		this.bookCount = bookCount;
	}
	public Book(int num, String bookName, String bookWriter, int bookPrice, int bookCount) {
		this.bookNum = num;
		this.bookName = bookName;
		this.bookWriter = bookWriter;
		this.bookPrice = bookPrice;
		this.bookCount = bookCount;
	}

	//get,set
	public int getNum() {
		return bookNum;
	}
	public String getBookName() {
		return bookName;
	}
	public String getBookWriter() {
		return bookWriter;
	}
	public int getBookPrice() {
		return bookPrice;
	}
	public int getBookCount() {
		return bookCount;
	}
	public void setBookName(String stockName) {
		this.bookName = stockName;
	}
	public void setBookWriter(String stockCategory) {
		this.bookWriter = stockCategory;
	}
	public void setBookPrice(int stockPrice) {
		this.bookPrice = stockPrice;
	}
	public void setBookCount(int stockCount) {
		this.bookCount = stockCount;
	}
	//Book을 생성할때마다 자동적으로 코드를 배정하기 때문에 생성자말고 인스턴스 생성을 하기 위한 copy
	//clone()은 try catch를 해줘야하기 때문에 다른 곳에서 사용하기 편하게 copy라는 메서드를 제작 
	public Book copy() {
		Book book=null ;
		try {
			book = (Book)this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return book;
	}
	//내용을 전체적으로 바꿀 때 사용
	public void editAll() {
		readWrite.write("책 이름 : ");
		setBookName(readWrite.read());
		readWrite.write("지은이 : ");
		setBookWriter(readWrite.read());
		setBookPrice(readWrite.readPrice());
		setBookCount(readWrite.readCount());
	}
	//BookTree에서 같은 book인지 체크를 위한 equals 오버라이딩
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Book) {
			Book stock = (Book)obj;
			if(stock.getNum() == this.bookNum)return true;
			else return false;
		}return false;
	}
	//TreeSet 정렬을 위한 override
	@Override
	public int compareTo(Book book) {
		return  bookNum - book.getNum(); //번호순정렬(오름차순)
	}
	//Object clone() 오버라이딩 - implements Cloneable
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	//출력을 위한 toString 재정의
	@Override
	public String toString() {
		return bookNum +"\t"+bookName +"\t"+bookWriter +"\t"+bookPrice+"\t"+bookCount;
	}
}
