/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : 프로그램이 돌아가는 main(String[] args) 가 있는 class
 */

package Han.SeungWoon.bookStore.ver1.main;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.menu._999_Login;
import Han.SeungWoon.bookStore.ver1.user.CustomerMap;
import Han.SeungWoon.bookStore.ver1.user.Host;
import Han.SeungWoon.bookStore.ver1.util.FileInput;
import Han.SeungWoon.bookStore.ver1.util.FileOutput;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;


public class Main {
	//System.getProperty("user.name") > 사용자 이름 가져오기
	public static final String FOLDER = ("C:\\Users\\" +System.getProperty("user.name")+"\\Documents\\workspace").trim(); //파일 저장 위치
	public static void main(String[] args) {
		ReadWrite readWrite = ReadWrite.getInstance();//문자 입출력
		_999_Login login = _999_Login.getInstance();//콘솔메뉴
		
//		Main ex = new Main();
//		ex.customerEx();
//		ex.stockEx();
		
		FileInput fileInput = new FileInput();
		fileInput.doing();

		login.doing();//콘솔 내용 실행
		
		Thread fileOutThread = new Thread() {
			@Override
			public void run() {
				FileOutput fileOut = new FileOutput();
				fileOut.doing();
			}
		};
		fileOutThread.run();
		
		Thread sleep = new Thread() {
			@Override 
			public void run() {
				for(int i=3; i>0;i--) {
					try {
						readWrite.writeln(i+"초 후 종료됩니다");
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		sleep.run();
		
		readWrite.closeAll();//문자입출력 종료
	}
	private void customerEx() {
		CustomerMap customer = CustomerMap.getInstance();
		customer.addCustomer("test1", "test1");
		customer.addCustomer("test2", "test2");
	}
	private void stockEx() {
		Host host = Host.getInstance();
		host.getStockTree().addBookTree(new Book("book1","writer1",5000,50));
		host.getStockTree().addBookTree(new Book("book2","writer2",1000,40));
		host.getStockTree().addBookTree(new Book("book3","writer3",3000,30));
		host.getStockTree().addBookTree(new Book("book4","writer4",4000,40));
		host.getStockTree().addBookTree(new Book("book5","writer5",5000,50));
		host.getStockTree().addBookTree(new Book("book6","writer6",6000,60));
	}
}
