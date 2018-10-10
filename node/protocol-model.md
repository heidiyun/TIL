# 네트워크 계층 모델
## OSI 7 계층 (표준)
> 국제표준화기구(ISO)에서 개발한 모델  
> 컴퓨터 네트워크 프로토콜 디자인과 통신을 계층으로 나누어 설명.  
   
Application	
- - - -
Presentation
- - - -
Session
- - - -
Transport    
- - - -
Network      
- - - -
Link 
- - - -
Physical     

## Internet Protocol Suite 
> 통신규약(프로토콜)의 집합.  
>  Internet Protocol Suite 중에서 TCP/IP가 가장 많이 쓰인다.  


 Application
- - - -
TCP / UDP
- - - -
IP - 컴퓨터의 주소
- - - -
Ethernet : HW 주소 / MAC 주소 :  6 Byte

### IP 주소 - 컴퓨터의 주소
1. Public IP :  외부에서 접속이 가능하다.
2. Private IP : 외부에서 접속이 불가능하다.

IP주소가 할당할 수 있는 개수는 한정적이다.
-> 공유기를 통해서 쉽게 네트워크를 생성할 수 있다. (라우터)
220.78.187.95 : public IP
192.168.11.xx : private IP

![](protocol-model/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202018-10-08%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%202.50.10.png)

* ISP(인터넷 서비스 프로바이더) 업체
->  KT – 동적 IP / 고정 IP – 컴퓨터 1대 

## OSI 7 계층과 TCP/IP
국제 표준은 OSI 7 계층입니다.
그러나 TCP/IP가 그 이전부터 널리 쓰이고 있어 사실상 표준이라고 생각하면 됩니다.
공식적으로 표준은 아니지만 사람들에게 더 많이 쓰이고 유용하여 사실상 표준이라고 칭하는 것을 De facto 라고 합니다.

![](protocol-model/BE541BD6-1338-4B35-8D8E-4E359FC0C320.png)
![TCP/IP와 OSI 7계층](https://t1.daumcdn.net/cfile/tistory/1401E90D4BC20EA733)

## 통신
서버 : 서비스를 제공하는 프로세스 (public IP)
클라이언트 : 서비스를 이용하는 프로세스 (private, public IP)
### port
서버 안에서 서비스를 제공하는 프로세스의 주소 - 포트(port) 
16bit : 0 ~ 65535

서비스의 종류에 따라서 약속된 포트도 있습니다. (Well-known port)
1. 80 : web server
2. 22 : SSH
3. 21 : FTP

### DNS(Domain Name Service)
> domain 주소를 IP주소로 변경해주는 작업을 합니다.  

### socket
![](protocol-model/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202018-10-08%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%202.56.46.png)
![socket](https://user-images.githubusercontent.com/38517815/46729036-2b2d2980-ccbf-11e8-8e46-4f4d0787f0be.png)
클라이언트가 서버에 서비스를 요청하면 그 요청을  server socket이 처리합니다.
요청을 받아들이게 되면 connect가 되고 server socket이 accept 명령을 호출하여  클라이언트와 통신할 새로운 socket을 생성합니다. 새로 생성된 socket이 클라이언트 socket과 통신하게 되는데 이것을 session이라 부릅니다.




#node