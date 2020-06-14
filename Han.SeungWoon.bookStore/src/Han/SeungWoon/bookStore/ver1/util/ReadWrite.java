/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : 문자 입출력 관련 클래스
 */

package Han.SeungWoon.bookStore.ver1.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.data.BookTree;

public class ReadWrite {//프로그램의 문자 총괄 입출력관리 - 속도를 위해 BufferedReader,BufferedWriter 이용
	//싱글톤처리 - 모든 클래스에서 거의 사용하기 때문
	private static ReadWrite readWrite = new ReadWrite();
	private ReadWrite() {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
	}
	public static ReadWrite getInstance() {
		return readWrite;
	}
	
	public BufferedReader br;
	public BufferedWriter bw;
	
	public int readInt(int n) {// 메뉴 번호받는 메서드
		//n이라는 변수를 통해 메뉴 번호를 몇번까지 받을지 정할 수 있다.
		int i=0;
		try {
			read :
			while(true) {
				int str = Integer.parseInt(br.readLine());//번호 입력받기
				for(i=1; i<=n; i++) {
					if(str == i){
						break read;
					}
				}System.out.print("잘못 입력하셨습니다 다시 입력하십시오 : ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {//문자 입력하면 exception 처리
			readWrite.writeln("문자는 넣으실 수 없습니다.");
		}
		return i;
	}
	
	public String read() { // 문자 입력받기 - 클래스마다 try- catch를 하기 번거로워서 try catch 하는 메서드로 만들어버림
		String str=null;
		try {
			str = br.readLine();
			if(str.contains(" ")) {
				str = str.replace("\n", " ");
				readWrite.writeln("탭키를 띄어쓰기로 변환 했습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public int readPrice() {//가격 입력받기
		int price=0;
		while(true) {
			try {
				System.out.print("가격  : ");
				price = Integer.parseInt(readWrite.read());
				if (price<0 || price>1000000) {// 가격이 음수나 100만원 이상이면 에러로 던짐
					throw new PriceException();
				}
				else break;
			}catch (NumberFormatException e){//정수 아닌수 입력받으면 exception 처리
				readWrite.writeln("가격은 정수여야 합니다 ");
			}catch (PriceException e) {
				readWrite.writeln(e.getMessage());
			}
		}
		return price;
	}
	public int readCount() {//갯수 입력받기
		int count=0;
		while(true) {
			try {
				System.out.print("갯수 : ");
				count = Integer.parseInt(readWrite.read());
				if (count<=0 || count>1000000) {
					throw new CountException();//갯수가 1~999999개에서 벗어나면 에러로 던짐
				}
				else break;
			}catch (NumberFormatException e){
				readWrite.writeln("갯수은 정수여야 합니다 ");
			}catch (CountException e) {
				readWrite.writeln(e.getMessage());
			}
		}
		return count;
	}
	
	public Book readCode(String str, BookTree bookTree) {
		//책 코드 입력받고 Book 자료형으로 return
		//0 입력시 null로 return 됨.
		//bookTree 매개변수는 입력받은 코드를 어디에서 찾을 것인지 
		Book ans=null;
		read :
		while(true) {
			write(str+"책 코드를 입력하세요. [이전:0] : ");//매개변수 str을 이용해서 어떤 책코드를 입력하라고 할지 출력하기
			try{
				int code = Integer.parseInt(read());
				if(code ==0) return null;
				for(Book book : bookTree.getBookTree()) {
					if(book.getNum() == code) {
						ans = book;
						break read;
					}
				}writeln("없는 코드 번호 입니다 다시 입력하세요");
			}catch (Exception e) {
				writeln("신발 코드는 0~9999 사이의 숫자입니다");
			}
		}return ans;
	}
	
	public synchronized void write(String a) {// System.out.print 과 동일
		try {
			bw.append(a);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void writeln(String a) {// System.out.println 과 동일
		try {
			bw.append(a);
			bw.append("\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeAll() {//프로그램 마무리로 싱글톤 객체 종료할때 사용
		try {
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
@SuppressWarnings("serial")
class PriceException extends Exception{// 가격이 음수나 100만원 이상이면 에러
	public PriceException() {
		super("가격은 0~100만원 사이입니다");
	}
}
@SuppressWarnings("serial")
class CountException extends Exception{//갯수가 1~999999개에서 벗어나면 에러
	public CountException() {
		super("갯수은 1~999999개 사이입니다");
	}
}
