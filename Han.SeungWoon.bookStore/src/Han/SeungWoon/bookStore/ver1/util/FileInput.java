package Han.SeungWoon.bookStore.ver1.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.data.BookTree;
import Han.SeungWoon.bookStore.ver1.main.Main;
import Han.SeungWoon.bookStore.ver1.user.Customer;
import Han.SeungWoon.bookStore.ver1.user.CustomerMap;
import Han.SeungWoon.bookStore.ver1.user.Host;

public class FileInput {
	BufferedReader hostbr = null;
	InputStreamReader hostisr = null;
	FileInputStream hostfis = null;
	boolean hostCheck = true;
	
	BufferedReader customerbr = null;
	InputStreamReader customerisr = null;
	FileInputStream customerfis = null;
	boolean customerCheck = true;
	
	Host host = Host.getInstance();
	ReadWrite readWrite = ReadWrite.getInstance();
	
	//생성자
	public FileInput() {
		try {
			hostfis= new FileInputStream(Main.FOLDER+"\\Host.txt");
			hostisr= new InputStreamReader(hostfis, "UTF8");
			hostbr = new BufferedReader (hostisr);
		}catch(Exception e){
			hostCheck=false;
		}
		try {
			customerfis= new FileInputStream(Main.FOLDER+"\\Customer.txt");
			customerisr= new InputStreamReader(customerfis, "UTF8");
			customerbr = new BufferedReader (customerisr);
		}catch(Exception e){
			customerCheck=false;
		}
	}
	public void doing() {
		Thread hostReadThread_ = new Thread() {
			@Override 
			public void run(){
				if(getHostCheck()) {
					hostRead();
					readWrite.writeln("host파일을 읽어왔습니다");
				}
				else {
					readWrite.writeln("host파일이 존재하지 않습니다");
				}
			}
		};
		hostReadThread_.run();
		
		Thread customerReadThread_ = new Thread() {
			@Override
			public void run() {
				if(getCustomerCheck()) {
					customRead();
					readWrite.writeln("customer파일을 읽어왔습니다.");
				}
				else {
					readWrite.writeln("Customer 파일이 존재하지 않습니다");
				}
			}
		};
		customerReadThread_.run();
	}
	//get
	public boolean getHostCheck() {
		return hostCheck;
	}
	public boolean getCustomerCheck() {
		return customerCheck;
	}
	//host 읽는 함수
	public void hostRead() {
		try {
			getBookTree(hostbr, host.getStockTree());
			getBookTree(hostbr, host.getSellingTree());
			getBookTree(hostbr, host.getSelledTree());
			getBookTree(hostbr, host.getRefundTree());
			int n = Integer.parseInt(hostbr.readLine());
			for(int i=0; i<n; i++) {
				host.addMessage(hostbr.readLine());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			hostbr.close();
			hostisr.close();
			hostfis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//custom 읽는 함수
	public void customRead() {
		CustomerMap customerMap = CustomerMap.getInstance();
		try {
			int count = Integer.parseInt(customerbr.readLine());
			for(int i=0; i<count; i++) {
				//아이디 비번
				String str = customerbr.readLine();
				String[] sarr = str.split("\t");
				String id = sarr[0];
				String pwd = sarr[1];
				customerMap.addCustomer(id, pwd);
				Customer customer =customerMap.getMap().get(id);
				//장바구니
				getBookTree(customerbr,customer.getBasketTree());
				//구매중
				getBookTree(customerbr,customer.getBuyingTree());
				//구매완료
				getBookTree(customerbr,customer.getBuyedTree());
				//환불
				getBookTree(customerbr,customer.getRefundTree());
				//가지고온 손님정보들로 host 정보 구성
//				host.treeArrangement();
			}
			
		}catch(IOException e) {
			
		}catch(Exception e) {}
		try {
			customerbr.close();
			customerisr.close();
			customerfis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void getBookTree(BufferedReader br,BookTree bookTree) throws NumberFormatException, IOException  {
		int count2 = Integer.parseInt(br.readLine());
		for(int j=0; j<count2; j++) {
			String sbook = br.readLine();
			String[] _sbook = sbook.split("\t");
			int bookNum = Integer.parseInt(_sbook[0]);
			String bookName = _sbook[1];
			String bookWriter = _sbook[2];
			int bookPrice = Integer.parseInt(_sbook[3]);
			int bookCount = Integer.parseInt(_sbook[4]);
			bookTree.addBookTree(new Book(bookNum, bookName,bookWriter, bookPrice, bookCount));
		}
	}
}
