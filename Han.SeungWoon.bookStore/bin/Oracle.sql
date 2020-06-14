
/* Drop Tables */
DROP TABLE buyed CASCADE CONSTRAINTS;
DROP TABLE buying CASCADE CONSTRAINTS;
DROP TABLE refund CASCADE CONSTRAINTS;
DROP TABLE ShoppingCart CASCADE CONSTRAINTS;
DROP TABLE Stock CASCADE CONSTRAINTS;
DROP TABLE Book CASCADE CONSTRAINTS;
DROP TABLE Customer CASCADE CONSTRAINTS;

/* Drop Sequence */
DROP SEQUENCE bookNum_seq;
DROP SEQUENCE shoppingCart_seq;
DROP SEQUENCE buying_seq;
DROP SEQUENCE buyed_seq;
DROP SEQUENCE refund_seq;



/* Create Tables */
-- 책
CREATE TABLE Book
(
	-- 책번호
	bookNum number(4,0) NOT NULL PRIMARY KEY,
	-- 책이름
	bookName varchar2(50) NOT NULL,
	-- 글쓴이
	bookWriter varchar2(50) NOT NULL,
	-- 책가격
	bookPrice number(7,0) NOT NULL
);
-- 손님
CREATE TABLE Customer
(
	-- 아이디
	id varchar2(50)PRIMARY KEY ,
	-- 비밀번호
	password varchar2(50) NOT NULL
	
);
-- 구매완료 목록
CREATE TABLE buyed
(
	-- 책권수
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0),
	-- 아이디
	id varchar2(50) NOT NULL CONSTRAINT buyed_id_const REFERENCES Customer(id),
	-- 책번호
	bookNum number(4,0) NOT NULL CONSTRAINT buyed_booknum_const REFERENCES Book(bookNum),
	-- 구매완료번호
	buyedKey number(4,0) PRIMARY KEY
	
);
-- 쇼핑카트
CREATE TABLE ShoppingCart
(
	-- 책권수
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0),
	-- 아이디
	id varchar2(50) NOT NULL CONSTRAINT cart_id_const REFERENCES Customer(id),
	-- 책번호
	bookNum number(4,0) NOT NULL CONSTRAINT cart_booknum_const REFERENCES Book(bookNum),
	-- 쇼핑카트번호
	shoppingKey number(4,0) PRIMARY KEY
	
);

-- 구매중인 목록
CREATE TABLE buying
(
	--책권수
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0) ,
	-- 아이디
	id varchar2(50) NOT NULL CONSTRAINT buying_id_const REFERENCES Customer(id),
	-- 책번호
	bookNum number(4,0) NOT NULL  CONSTRAINT buying_booknum_const REFERENCES Book(bookNum),
	-- 구매번호
	buyingKey number(4,0) PRIMARY KEY
	
);

-- 환불테이블
CREATE TABLE refund
(
	--책권수
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0) ,
	-- 아이디
	id varchar2(50) NOT NULL CONSTRAINT refund_id_const REFERENCES Customer(id),
	-- 책번호
	bookNum number(4,0) NOT NULL CONSTRAINT refund_booknum_const REFERENCES Book(bookNum),
	-- 환불번호
	refundKey number(4,0) PRIMARY KEY
);

-- 재고
CREATE TABLE Stock
(
    -- 책번호
	bookNum number(4,0) PRIMARY KEY,
	-- 책이름
	bookName varchar2(50) NOT NULL,
	-- 글쓴이
	bookWriter varchar2(50) NOT NULL,
	-- 책가격
	bookPrice number(7,0) NOT NULL,
	--책권수
	bookCount number(4,0)  NOT NULL CONSTRAINT stock_count_const CHECK(bookCount>=0)
	
);

/*   트리거    */
--Book 생성 트리거
CREATE OR REPLACE TRIGGER trigger_book
AFTER INSERT
ON Stock
FOR EACH ROW
BEGIN 
    INSERT INTO book
    VALUES (:new.bookNum,:new.bookName,:new.bookWriter,:new.bookPrice);
END;
/
--재고 0개되면 제거 하는 트리거
CREATE OR REPLACE TRIGGER stock_del_trigger
AFTER UPDATE
ON stock
FOR EACH ROW
BEGIN 
    DELETE stock
    WHERE bookNum = :new.bookNum
    AND bookCount = 0;
END;
/
--구매 트리거(구매하면 재고 감소)
CREATE OR REPLACE TRIGGER buying_trigger
BEFORE INSERT
ON buying
FOR EACH ROW
BEGIN 
    UPDATE stock
    SET bookCount = bookCount -:new.bookCount
    WHERE bookNum = :new.bookNum;
END;
/
--구매중 0개 되면 삭제되는 트리거
CREATE OR REPLACE TRIGGER buying_del_trigger
AFTER UPDATE
ON buying
FOR EACH ROW
BEGIN 
    DELETE buying
    WHERE buyingKey = :new.buyingKey
    AND bookCount = 0;
END;
/
--구매완료 트리거(구매중 목록 제거)
CREATE OR REPLACE TRIGGER buyed_trigger
BEFORE INSERT
ON buyed
FOR EACH ROW
BEGIN 
    UPDATE buying
    SET bookCount = bookCount -:new.bookCount
    WHERE bookNum = :new.bookNum
        AND id = :new.id;
END;
/
-- 구매완료 0개되면 제거
CREATE OR REPLACE TRIGGER buyed_del_trigger
AFTER UPDATE
ON buyed
FOR EACH ROW
BEGIN 
    DELETE buyed
    WHERE buyedKey = :new.buyedKey
    AND bookCount = 0;
END;
/
--환불 트리거
CREATE OR REPLACE TRIGGER refund_trigger
BEFORE INSERT
ON refund
FOR EACH ROW
BEGIN 
    UPDATE buyed
    SET bookCount = bookCount -:new.bookCount
    WHERE bookNum = :new.bookNum
        AND id = :new.id;
END;
/
--환불 0개되면 제거하는 트리거
CREATE OR REPLACE TRIGGER refund_del_trigger
AFTER UPDATE
ON refund
FOR EACH ROW
BEGIN 
    DELETE refund
    WHERE refundKey = :new.refundKey
    AND bookCount = 0;
END;
/



commit;

/*          시퀸스     */
--책번호

CREATE SEQUENCE bookNum_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 2000;
--장바구니번호

CREATE SEQUENCE shoppingCart_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;
--구매중 번호

CREATE SEQUENCE buying_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;
--구매완료 번호

CREATE SEQUENCE buyed_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;
--환불번호

CREATE SEQUENCE refund_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;

commit;
/*      프로시저            */
--결산
SET serveroutput on;
CREATE OR REPLACE PROCEDURE settlement
IS
    p_settlement Number(10);
BEGIN 
    SELECT sum(b.bookcount*d.bookprice) "결산액"
    INTO p_settlement
    FROM buyed b
    JOIN book d
    ON b.booknum = d.booknum;
    
    dbms_output.put_line('결산액 : ' ||p_settlement);
END;
/
    
    
    

/*          책 넣기 및 가입           */
--가입
INSERT INTO customer
VALUES('test1', 'test1');
INSERT INTO customer
VALUES('test2', 'test2');
INSERT INTO customer
VALUES('test3', 'test3');
--재고에 책넣기
INSERT INTO stock
VALUES(bookNum_seq.nextval, '흥부와 놀부', '작자미상', 5000, 30);
INSERT INTO stock
VALUES(bookNum_seq.nextval, '흥부와 놀부1', '작자미상2', 6000, 40);
INSERT INTO stock
VALUES(bookNum_seq.nextval, '흥부와 놀부2', '작자미상3', 7000, 50);
INSERT INTO stock
VALUES(bookNum_seq.nextval, '흥부와 놀부3', '작자미상4', 8000, 60);
--장바구니에 책넣기
INSERT INTO shoppingCart
VALUES(3, 'test1', 1000, shoppingCart_seq.nextval);
INSERT INTO shoppingCart --아이디 없음
VALUES(3, 'test4', 1000, shoppingCart_seq.nextval);
INSERT INTO shoppingCart --재고에 없는 책
VALUES(3, 'test1', 1005, shoppingCart_seq.nextval);

--구매하기
INSERT INTO buying
VALUES(1, 'test1', 1000, buying_seq.nextval);
INSERT INTO buying -- 재고 초과 구매
VALUES(40, 'test1', 1000, buying_seq.nextval);
INSERT INTO buying -- 재고에 없는 책 구매
VALUES(1, 'test1', 1005, buying_seq.nextval);
INSERT INTO buying -- 회원이 아닌사람이 구매
VALUES(1, 'test5', 1000, buying_seq.nextval);

--구매완료 처리
INSERT INTO buyed
VALUES(1,'test1',1000,buyed_seq.nextval);

--결산
EXECUTE settlement;


/*          표 보기            */
--손님
SELECT *
FROM customer;
--책
SELECT *
FROM book;
--재고
SELECT *
FROM stock;
--장바구니
SELECT *
FROM shoppingCart;
--구매중
SELECT *
FROM buying;
--구매완료
SELECT *
FROM buyed;
--환불
SELECT *
FROM refund;

      
