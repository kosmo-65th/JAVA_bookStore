
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
-- å
CREATE TABLE Book
(
	-- å��ȣ
	bookNum number(4,0) NOT NULL PRIMARY KEY,
	-- å�̸�
	bookName varchar2(50) NOT NULL,
	-- �۾���
	bookWriter varchar2(50) NOT NULL,
	-- å����
	bookPrice number(7,0) NOT NULL
);
-- �մ�
CREATE TABLE Customer
(
	-- ���̵�
	id varchar2(50)PRIMARY KEY ,
	-- ��й�ȣ
	password varchar2(50) NOT NULL
	
);
-- ���ſϷ� ���
CREATE TABLE buyed
(
	-- å�Ǽ�
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0),
	-- ���̵�
	id varchar2(50) NOT NULL CONSTRAINT buyed_id_const REFERENCES Customer(id),
	-- å��ȣ
	bookNum number(4,0) NOT NULL CONSTRAINT buyed_booknum_const REFERENCES Book(bookNum),
	-- ���ſϷ��ȣ
	buyedKey number(4,0) PRIMARY KEY
	
);
-- ����īƮ
CREATE TABLE ShoppingCart
(
	-- å�Ǽ�
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0),
	-- ���̵�
	id varchar2(50) NOT NULL CONSTRAINT cart_id_const REFERENCES Customer(id),
	-- å��ȣ
	bookNum number(4,0) NOT NULL CONSTRAINT cart_booknum_const REFERENCES Book(bookNum),
	-- ����īƮ��ȣ
	shoppingKey number(4,0) PRIMARY KEY
	
);

-- �������� ���
CREATE TABLE buying
(
	--å�Ǽ�
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0) ,
	-- ���̵�
	id varchar2(50) NOT NULL CONSTRAINT buying_id_const REFERENCES Customer(id),
	-- å��ȣ
	bookNum number(4,0) NOT NULL  CONSTRAINT buying_booknum_const REFERENCES Book(bookNum),
	-- ���Ź�ȣ
	buyingKey number(4,0) PRIMARY KEY
	
);

-- ȯ�����̺�
CREATE TABLE refund
(
	--å�Ǽ�
	bookCount number(4,0) NOT NULL CHECK(bookCount>=0) ,
	-- ���̵�
	id varchar2(50) NOT NULL CONSTRAINT refund_id_const REFERENCES Customer(id),
	-- å��ȣ
	bookNum number(4,0) NOT NULL CONSTRAINT refund_booknum_const REFERENCES Book(bookNum),
	-- ȯ�ҹ�ȣ
	refundKey number(4,0) PRIMARY KEY
);

-- ���
CREATE TABLE Stock
(
    -- å��ȣ
	bookNum number(4,0) PRIMARY KEY,
	-- å�̸�
	bookName varchar2(50) NOT NULL,
	-- �۾���
	bookWriter varchar2(50) NOT NULL,
	-- å����
	bookPrice number(7,0) NOT NULL,
	--å�Ǽ�
	bookCount number(4,0)  NOT NULL CONSTRAINT stock_count_const CHECK(bookCount>=0)
	
);

/*   Ʈ����    */
--Book ���� Ʈ����
CREATE OR REPLACE TRIGGER trigger_book
AFTER INSERT
ON Stock
FOR EACH ROW
BEGIN 
    INSERT INTO book
    VALUES (:new.bookNum,:new.bookName,:new.bookWriter,:new.bookPrice);
END;
/
--��� 0���Ǹ� ���� �ϴ� Ʈ����
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
--���� Ʈ����(�����ϸ� ��� ����)
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
--������ 0�� �Ǹ� �����Ǵ� Ʈ����
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
--���ſϷ� Ʈ����(������ ��� ����)
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
-- ���ſϷ� 0���Ǹ� ����
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
--ȯ�� Ʈ����
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
--ȯ�� 0���Ǹ� �����ϴ� Ʈ����
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

/*          ������     */
--å��ȣ

CREATE SEQUENCE bookNum_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 2000;
--��ٱ��Ϲ�ȣ

CREATE SEQUENCE shoppingCart_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;
--������ ��ȣ

CREATE SEQUENCE buying_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;
--���ſϷ� ��ȣ

CREATE SEQUENCE buyed_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;
--ȯ�ҹ�ȣ

CREATE SEQUENCE refund_seq
START WITH 1000
INCREMENT BY 1
MAXVALUE 9000;

commit;
/*      ���ν���            */
--���
SET serveroutput on;
CREATE OR REPLACE PROCEDURE settlement
IS
    p_settlement Number(10);
BEGIN 
    SELECT sum(b.bookcount*d.bookprice) "����"
    INTO p_settlement
    FROM buyed b
    JOIN book d
    ON b.booknum = d.booknum;
    
    dbms_output.put_line('���� : ' ||p_settlement);
END;
/
    
    
    

/*          å �ֱ� �� ����           */
--����
INSERT INTO customer
VALUES('test1', 'test1');
INSERT INTO customer
VALUES('test2', 'test2');
INSERT INTO customer
VALUES('test3', 'test3');
--��� å�ֱ�
INSERT INTO stock
VALUES(bookNum_seq.nextval, '��ο� ���', '���ڹ̻�', 5000, 30);
INSERT INTO stock
VALUES(bookNum_seq.nextval, '��ο� ���1', '���ڹ̻�2', 6000, 40);
INSERT INTO stock
VALUES(bookNum_seq.nextval, '��ο� ���2', '���ڹ̻�3', 7000, 50);
INSERT INTO stock
VALUES(bookNum_seq.nextval, '��ο� ���3', '���ڹ̻�4', 8000, 60);
--��ٱ��Ͽ� å�ֱ�
INSERT INTO shoppingCart
VALUES(3, 'test1', 1000, shoppingCart_seq.nextval);
INSERT INTO shoppingCart --���̵� ����
VALUES(3, 'test4', 1000, shoppingCart_seq.nextval);
INSERT INTO shoppingCart --��� ���� å
VALUES(3, 'test1', 1005, shoppingCart_seq.nextval);

--�����ϱ�
INSERT INTO buying
VALUES(1, 'test1', 1000, buying_seq.nextval);
INSERT INTO buying -- ��� �ʰ� ����
VALUES(40, 'test1', 1000, buying_seq.nextval);
INSERT INTO buying -- ��� ���� å ����
VALUES(1, 'test1', 1005, buying_seq.nextval);
INSERT INTO buying -- ȸ���� �ƴѻ���� ����
VALUES(1, 'test5', 1000, buying_seq.nextval);

--���ſϷ� ó��
INSERT INTO buyed
VALUES(1,'test1',1000,buyed_seq.nextval);

--���
EXECUTE settlement;


/*          ǥ ����            */
--�մ�
SELECT *
FROM customer;
--å
SELECT *
FROM book;
--���
SELECT *
FROM stock;
--��ٱ���
SELECT *
FROM shoppingCart;
--������
SELECT *
FROM buying;
--���ſϷ�
SELECT *
FROM buyed;
--ȯ��
SELECT *
FROM refund;

      
