package Han.SeungWoon.bookStore.ver1.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import Han.SeungWoon.bookStore.ver1.data.Book;
import Han.SeungWoon.bookStore.ver1.data.BookTree;
import Han.SeungWoon.bookStore.ver1.main.Main;
import Han.SeungWoon.bookStore.ver1.user.Customer;
import Han.SeungWoon.bookStore.ver1.user.CustomerMap;
import Han.SeungWoon.bookStore.ver1.user.Host;

public class FileOutput {
	BufferedWriter hostbw = null;
	OutputStreamWriter hostosw=null;
	FileOutputStream hostfos = null;
	
	BufferedWriter customerbw = null;
	OutputStreamWriter customerosw=null;
	FileOutputStream customerfos = null;
	
	File folder;
	
	public FileOutput() {
		try {
			folder = new File(Main.FOLDER);
			makeFolder();// 폴더 생성
		}catch(Exception e){
			ReadWrite.getInstance().writeln("폴더관련 에서 스트림 생성 실패했습니다");
		}
		try {
			hostfos= new FileOutputStream(Main.FOLDER+"\\Host.txt");
			hostosw = new OutputStreamWriter(hostfos, "UTF8");
			hostbw = new BufferedWriter (hostosw);
		}catch(Exception e){
			ReadWrite.getInstance().writeln("host관련 에서 스트림 생성 실패했습니다");
		}
		try {
			customerfos= new FileOutputStream(Main.FOLDER+"\\Customer.txt");
			customerosw = new OutputStreamWriter(customerfos, "UTF8");
			customerbw = new BufferedWriter (customerosw);
		}catch(Exception e){
			ReadWrite.getInstance().writeln("custom관련 에서스트림 생성 실패했습니다");
		}
	}
	public void doing() {
		
		Thread hostOut = new Thread() {
			@Override
			public void run() {
				HostOut();
			}
		};
		hostOut.run();
		
		Thread customOut = new Thread() {
			@Override
			public void run() {
				CustomOut();
			}
		};
		customOut.run();
		closeAll();
	}
	public void closeAll() {
		try {
			hostbw.close();
			hostosw.close();
			hostfos.close();
			customerbw.close();
			customerosw.close();
			hostfos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void HostOut() {
		Host host = Host.getInstance();
		try {
			outBookTree(hostbw,host.getStockTree());
			outBookTree(hostbw,host.getSellingTree());
			outBookTree(hostbw,host.getSelledTree());
			outBookTree(hostbw,host.getRefundTree());
			hostbw.append(Integer.toString(host.getI())+"\n");
			hostbw.append(host.MessageOut());
			hostbw.flush();
			ReadWrite.getInstance().writeln("host 내용 출력 완료");
		} catch (IOException e) {
			ReadWrite.getInstance().writeln("host 내용 출력 에러발생");
		}
	}
	
	public void CustomOut() {
		CustomerMap customerMap = CustomerMap.getInstance();
		try {
			customerbw.append(Integer.toString(customerMap.getMap().size())+"\n");
			for(Customer customer : customerMap.getMap().values()) {
				customerbw.append(customer.getId()+"\t");
				customerbw.append(customer.getPwd()+"\n");
				//장바구니
				outBookTree(customerbw,customer.getBasketTree());
				//구매중
				outBookTree(customerbw,customer.getBuyingTree());
				//구매완
				outBookTree(customerbw,customer.getBuyedTree());
				//환불
				outBookTree(customerbw,customer.getRefundTree());
				
				customerbw.flush();
				
			}ReadWrite.getInstance().writeln("custom 내용 출력 완료");
		} catch (IOException e) {
			ReadWrite.getInstance().writeln("custom 내용 출력 에러발생");
		}
	}
	private void outBookTree(BufferedWriter bw,BookTree bookTree) throws IOException {
		bw.append(Integer.toString(bookTree.getBookTree().size())+"\n");
		for(Book book : bookTree.getBookTree()) {
			bw.append(Integer.toString(book.getNum())+"\t");
			bw.append(book.getBookName()+"\t");
			bw.append(book.getBookWriter()+"\t");
			bw.append(book.getBookPrice()+"\t");
			bw.append(Integer.toString(book.getBookCount()));
			bw.append("\n");
		}
	}
	//폴더가 없을경우 폴더 생성
	public void makeFolder() {
		// 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
		if (!folder.exists()) {
			try{
				folder.mkdir();
				ReadWrite.getInstance().writeln("폴더 생성에 성공하였습니다");
			     //폴더 생성합니다.
		    } 
		    catch(Exception e){
		    	ReadWrite.getInstance().writeln("폴더 생성에 실패하였습니다");
			}    
		}
	}
}
