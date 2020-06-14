/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : 관리자 메뉴
 */

package Han.SeungWoon.bookStore.ver1.menu;

import Han.SeungWoon.bookStore.ver1.util.ReadWrite;

public class _100_Host_Menu {
	ReadWrite readWrite = ReadWrite.getInstance();
	
	private static _100_Host_Menu host_Menu = new _100_Host_Menu();
	private _100_Host_Menu(){}
	public static _100_Host_Menu getInstance() {
		return host_Menu;
	}
	
	_110_Host_Menu_Stock host_Menu_Stock = _110_Host_Menu_Stock.getInstance();
	_120_Host_Menu_Order host_Menu_Order = _120_Host_Menu_Order.getInstance();
	_130_Host_Menu_OrderId host_Menu_OrderId = _130_Host_Menu_OrderId.getInstance();
	
	public void doing() {
		menu :
		while(true) {
			switch(view()) {
			case 1 : 
				host_Menu_Stock.doing();
				break;
			case 2 :
				host_Menu_Order.doing();
				break;
			case 3 : 
				host_Menu_OrderId.doing();
				break;
			case 4 : break menu;
			}
		}
	}
	
	public int view() {
		readWrite.writeln("================= 관리자메뉴 =================");
		readWrite.writeln("1.재고관리  2.주문관리(책별) 3.주문관리(아이디별) 4.로그아웃");
		readWrite.writeln("==========================================");
		readWrite.write("메뉴번호를 입력하세요 : ");
        return readWrite.readInt(4);
	}
}

