/*
 * Author : 한승운
 * Date : 2020.05.12
 * 설명 : <id, CustomerImpl> 를 자료형으로 가진 HashMap을 선언한 클래스
 * 		모든 손님 정보를 담고 있고, 싱글톤 처리가 되어있음.
 */

package Han.SeungWoon.bookStore.ver1.user;

import java.util.HashMap;

public class CustomerMap {//고객의 정보를 담고 있는 HashMap
	//싱글톤
	private static CustomerMap customerMap= new CustomerMap();
	private CustomerMap() {}
	public static CustomerMap getInstance() {
		return customerMap;
	}
	//실제 정보를 담고 있는 Hashmap 인 map
	private HashMap<String, Customer> map = new HashMap<String, Customer>();
	
	//get,add,remove,edit
	public HashMap<String, Customer> getMap(){
		return map;
	}
	public void addCustomer(String id, String pwd) {
		map.put(id, new Customer(id,pwd));
	}
	public void removeCustomer(String id) {
		map.remove(id);
	}
}
