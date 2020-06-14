package Han.SeungWoon.bookStore.ver1.data;

public interface Code {
	//상수
		static final int SHOP_LOGIN =999;				//로그인
		
		//관리자
		static final int HOST_MENU = 100;				//관리자
		
		//관리자 - 재고관리
		static final int HOST_GOODS_MENU = 110;			//재고 관리
		static final int HOST_GOODS_LIST = 111;			//상품 리스트
		static final int HOST_GOODS_ADD = 112;			//상품 추가
		static final int HOST_GOODS_UPDATE = 113;		//상품 수정
		static final int HOST_GOODS_DELETE = 114;		//상품 삭제

		//관리자 - 주문관리(책별)
		static final int MANAGE_ORDER_MENU = 120;		//주문관리
		static final int MANAGE_ORDER_LIST = 121;		//주문목록
		static final int MANAGER_ORDER_CONFIRM = 122;	//구매
		static final int MANAGER_ORDER_CANCEL = 123;	//환불
		static final int MANAGER_SALE_TOTAL = 124;		//결산
		
		//관리자 - 주문관리(id별)
		static final int MANAGE_ORDERID_MENU = 120;		//주문관리
		static final int MANAGE_ORDERID_LIST = 121;		//주문목록
		static final int MANAGER_ORDERID_CONFIRM = 122;	//구매
		static final int MANAGER_ORDERID_CANCEL = 123;	//환불
		static final int MANAGER_SALEID_TOTAL = 124;	//결산
		//---------------------------------------------------
		
		//손님
		static final int CUSTOMER_MENU = 200;		
		
		//고객 - 상품목록 메뉴
		static final int CUSTOMER_GOODS_MENU = 210;	

		//고객 - 장바구니 메뉴
		static final int CUSTOMER_CART_LIST = 220;		//장바구니 목록
		static final int CUSTOMER_CART_ADD = 221;		//장바구니 추가
		static final int CUSTOMER_CART_DELETE = 222;	//장바구니 삭제
		static final int CUSTOMER_CART_BUY = 223;		//장바구니 구매
		
		//고객- 바로 구매
		static final int CUSTOMER_NOWBUY = 230;
		
		//고객 - 환불
		static final int CUSTOMER_REFUND = 240;
		
		//--------------------------------------------------
		static final int CUSTOMER_REGISTER =300;
}
