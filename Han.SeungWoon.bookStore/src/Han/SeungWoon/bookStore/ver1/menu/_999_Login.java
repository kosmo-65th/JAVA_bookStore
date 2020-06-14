/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : 콘솔의 로그인 기능
 */

package Han.SeungWoon.bookStore.ver1.menu;

import Han.SeungWoon.bookStore.ver1.user.Customer;
import Han.SeungWoon.bookStore.ver1.user.CustomerMap;
import Han.SeungWoon.bookStore.ver1.user.Host;
import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class _999_Login {
	ReadWrite readWrite = ReadWrite.getInstance(); //문자 입출력
	//싱글톤
	private static _999_Login login = new _999_Login();
	private _999_Login(){}
	public static _999_Login getInstance() {
		return login;
	}
	// 게스트가 로그인 할 시 게스트의 정보를 담을 변수
	public static Customer customer;
	_100_Host_Menu host_Menu = _100_Host_Menu.getInstance();//하위메뉴
	_200_CustomerMenu customerMenu = _200_CustomerMenu.getInstance(); // 하위메뉴
	
	public void doing() {//login 실행문
		menu : 
		while(true) {
			switch(menu()) {
				case 1 : 
					hostLogin();
					Host.getInstance().getMessage();
					host_Menu.doing();
					break;
				case 2 : 
					guestLogin();
					customer.getMessage();
					customerMenu.doing();
					break;
				case 3 : login.signUp();
					break;
				case 4 : break menu;
				default : break;
			}
		}
	}
	
	public void guestLogin(){//custom로그인
		String id,pwd;
        while(true) {
        	//id, pwd 입력받기
            readWrite.write("고객 ID : ");
            id = readWrite.read();
            readWrite.write("고객 PW : ");
            pwd = readWrite.read();
            
            //받은 id로 > customerMap의 map<아이디, custom내용>에 있는지 확인
            //id(key)로 custom내용(value)를 받음 > customImpl.pwd와 pwd가 같은지 확인후 같으면 로그인
            //> 로그인이 되면 customerMenu로 넘어가고, 아니면 어떤 부분이 틀렸는지 지적 후 다시 ID랑 PW를 입력받음
            CustomerMap customerMap= CustomerMap.getInstance();
            if(customerMap.getMap().containsKey(id)) {
                if(customerMap.getMap().get(id).getPwd().equals(pwd)) {
                	readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
                	readWrite.writeln("\t\t로그인 되었습니다.");
                	readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
                    customer = customerMap.getMap().get(id);
                    break;
                }
                else {
                	readWrite.writeln("비밀번호가 틀렸습니다.");
                }
            }else readWrite.writeln("아이디가 틀렸습니다.");
        }
	}
	
	public void hostLogin() {//host 로그인
		String id,pwd;
        while(true) {
        	//아이디 비번 입력받기
        	readWrite.write("관리자 ID : ");
            id = readWrite.read();
            readWrite.write("관리자 PW : ");
            pwd = readWrite.read();
            //host의 ID,PW는 public final 처리 했기 때문에 접근해서 같은지 확인
            if(Host.ID.equals(id)) {
                if(Host.PASSWORD.equals(pwd)) {
                	readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
                	readWrite.writeln("\t\t로그인 되었습니다.");
                	readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
                    break;
                }
                else {
                	readWrite.writeln("비밀번호가 틀렸습니다.");
                }
            }else readWrite.writeln("아이디가 틀렸습니다.");
        }
	}
	public int menu() {//메뉴 표출 및 메뉴 번호 입력받기
		while(true) {
			readWrite.writeln("================= 로그인 =================");
			readWrite.writeln("  1.관리자    2.고객    3.회원가입     4.종료");
			readWrite.writeln("==========================================");
	        readWrite.write("메뉴번호를 입력하세요 : ");
	        return readWrite.readInt(4);
		}
	}
	
	public void signUp() {// 회원가입
		String id,pwd;
		readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
		readWrite.writeln("\t\t회원가입");
		readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
        //아이디 비번 입력받기
		readWrite.write("ID : ");
        id = readWrite.read();
        readWrite.write("PW : ");
        pwd = readWrite.read();
        
        CustomerMap customerMap = CustomerMap.getInstance();
        //CustomerMap.map 에서 이미 있는 아이디면 이전메뉴로 돌아가고
        //없는 아이디면 map에 추가해서 받은 정보로 CustomerImpl을 생성
        if(customerMap.getMap().containsKey(id)) {
        	 readWrite.writeln("이미 존재하는 아이디 입니다. 회원가입할 수 없습니다.");
        }else {
        	customerMap.addCustomer(id, pwd);
        	Host.getInstance().addMessage(id+"님이 회원가입하셨습니다");
        	readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
    		readWrite.writeln("\t회원가입에 성공하였습니다");
    		readWrite.writeln("++++++++++++++++++++++++++++++++++++++++++");
        }
	}
}
