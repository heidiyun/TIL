# Chapter-2 Application layer
> 사용자가 네트워크에 접근할 때 가장 먼저 거치는 layer  
* Basic functions 
	* 네트워크에 접근할 수 있는 수단
	* 통신을 데이터 네트워크를 통해 전송 될 수 있는 데이터로 변환하는 수단
* Application programs
	* 어플리케이션 레이어에서 동작하는 프로그램
	* 네트워크를 통해 통신 할 수 있지만 네트워크 코어 장치 용 소프트웨어는 필요하지 않다. 
	즉, 네트워크 장비와는 무관하게 만들어질 수 있다.
::Host - router - switch - repeater - switch - router - host::
host가 application layer에 해당하는 것인데 router 에서 network 역할을 다 해주고 있으므로 네트워크 장비와는 독립적으로 만들어질 수 있다.
host는 상대 host만 고려하면 된다.

## Overview
### Process, Api, Socket
* Process : 호스트에서 동작중인 어플리케이션 프로그램을 말한다.
* Api : process와 OS간의 application programming interface
* Socket : process와 network 간의 network programming interface

Device Driver : 디바이스 차원의 소프트웨어
하드웨어와 소프트웨어를 연결시켜주는 중간 다리

네트워크 카드에서 들어오는 이벤트는 불확실성이 크고, 시간이 지연되면 데이터가 손실 될 수 있으므로 우선순위를 높게 배정한다.

### FYI : sk_buff structure for sockets
* data : 데이터가 쓰여질 헤더의 첫번째 주소

## Application Architectures
* Client-to-Server(CS)
* Peer-to-Peer(P2P)

### Client-Server Architectures
> 정보가 비대칭  
* Server
	* 모든 정보는 server에 저장되어 있다.
	* 서버는 항상 켜져있어야 한다. (Always-on)
	* 서버의 ip 주소는 고정. (Permanent)
	* 확장을 위한 data centers 
* Clients
	* server와 통신하여 정보를 받아올 수 있다.
	* 간헐적으로 연결됨.
	* 가변 ip 주소 (dynamic)
	* 다른 클라이언트와 통신할 수 업삳.

### P2P Architectures
> 정보가 대칭. 서로가 동등하다.  
각각의 peer는 서버/클라이언트 역할을 다 수행한다.
* always-on server가 없다.
* peer간의 직접적 통신이 가능하다.
* peer는 정보를 요청할 수도 있고, 정보를 제공해줄 수도 있다.
* peer는 간헐적으로 연결되며 ip가 바뀔 수 있다.
* 관리가 복잡하다. (DHT - Distributed Hash Tables)
	* 정보를 누가 갖고있는지 볼 수 있는 방법론

## Transport Service
### Needs
* Application layer는 transport layer에 대한 interface만 갖고 있다.
* ip는 datagram-packet switching을 사용하고 있어서 정보 전달에 있어 신뢰할 수 업식 떄문에 transport layer가 필요하다.

### 선택 조건
1. Data integrity(loss)
: 손실의 여부
2. Timing (delay)
: 전달 속도
3. Throughput (bandwidth)
: 데이터 양
	* 어떤 앱은 효과적으로 처리할 수 있는 최소한의 양을 요구한다.
	* 어떤 앱은 얻을 수 있는 모든 처리량을 요구한다.
4. Security
Encryption, data integrity…

### 프로토콜 종류
* TCP (Transmission Control Protocol) 
* UDP (User Datagram Protocol)

### UDP
전송 중에 데이터가 손실 될 수 있어 신뢰성이 떨어진다.
전송자도 데이터가 잘 갔는지 알 수 없고
응답자도 데이터가 온전하게 왔는지 알 수 없다.
(Datagram packet 사용)
* connectionless
* unreliable data transfer
* 오버헤드가 작고 싸다.
* 빠르다.

제공하지 않는 기능
	* reliablility
	* flow control
	* congestion control
	* timing
	* throughput guarantee
	* security
	* connection setup

### TCP
전송자도 데이터가 잘 갔는지 알 수 있고
응답자도 데이터가 온전하게 왔다고 신뢰할 수 있다.
* connection-oriented
* reliable transport
* 오버헤드가 크고 비싸다. 
* 속도가 느리다.
* congestion control : 오버헤드가 발생할 때 전송자를 조절한다.
* flow control

제공하지 않는 기능
	* timing -> 속도가 느림
	* minimum throughput guarantee : 최소 처리량 보장x
	* security

### UDP/TCP를 결정하는 기준
1. Data integrity (loss)
2. Timing (delay)
3. Throughput (bandwidth)


## WEB and HTTP
::HTTP: Web 페이지를 교환해서 볼 수 있게 해주는 Application layer 프로토콜::

### Web Page
* object들로 구성.
*  참조 된 여러 객체들을 포함하는 기본 html 파일로 구성됩니다.

### Object
* html 파일, jpeg 이미지, 음성 파일 등이 될 수도 있다.

### URL (Uniform Resource Locator)
* 각각의 object는 url로 접근가능합니다.
### URI  (Uniform Resource Identifier)
* 각각의 obejct는 uri로 식별할 수 있습니다.

http는 클라이언트와 서버간의 object 배치방법을 논하는 프로토콜입니다.

### HTTP
> Hyper Text Transfer Protocol  

* 웹의 어플리케이션 레이어 프로토콜
* Client-Server model에서 사용된다.
	* client (browser) : web object를 요청하고 받으며 display한다.
	* server(Web server) : http 프로토콜을 사용해서 요청에 대한 응답으로 objects를 보낸다.
	
#### HTTP Connections
##### HTTP runs on TCP
* HTTP : application layer
* TCP : transport layer

1. sender와 receiver사이의 connection 수립
2. 데이터의 신뢰성을 보장하기 위해, 데이터가 성공적으로 전달됐는지 확인해야 한다.
즉, 서버에서 잘 받았다는 것을 전달해줘야 한다.
(Response)
3. 데이터 전송이 완료 되었으면 connection을 끊는다.

##### non-persistent HTTP
* 최대 하나의 객체가 TCP connection을 통해서 전송된다.
* 그리고 connection  이 끊긴다.
* 다수의 object를 다운로드 하려면 다수의 연결이 있어야 된다.
운영체제는 각각의 TCP  connection을 관리하기위해 overhead한다.

* RTT (Round Trip Time) 왕복시간
	* 클라이언트에서 서버 그리고 서버에서 클라이언트로 걸리는 시간
* HTTP response time 
	* TCP 연결을 위해서 하나의 RTT가 소요
	* HTTP 요청과 응답을 교환할 때 하나의 RTT 소요

* 하나의 오브젝트를 받아오는데 걸리는 시간
	* 2 * RTT + file transmission time


##### persistant HTTP
* 다수의 object를 하나의 TCP connection을 통해 전송할 수 있다.
* pros 
cpu와 메모리를 덜 쓰고 RTT가 적게 걸리고 밀집이 덜하다
* cons 
클라이언트는 모든 데이터를 받았으면 연결을 끊을 수 있다.
서버는 연결을 계속 유지하고 있다.
다른 클라이언트는 사용 불 가능하다.

### Http Message - Format
> ordinary ASCII text로 쓰여짐.  
> 인간이 읽을 수 있다.  

### IOT (Internet Of Things)

#### HTTP  Cookies : User-Server Interaction
HTTP는 ‘stateless’ 성격을 가진다. 
* 그래서 web server는 client의 요청을 기억하지 못한다.
* 수천 개의 동시 TCP 연결을 처리해야 하는 간단하고 고성능 웹 서버를 제공한다.
* HTTP Cookie란?
	* 사용자가 웹사이트를 방문하는 동안 웹사이트로부터 전송되어지고 유저의 웹 브라우저에 저장된 데이터들의 조각.
	*  쿠키는 웹 사이트의 신뢰할 수 있는 메커니즘으로 설계되었다.
	* Set-Cookie :  해당 요청이 쿠키에 저장되었다고 응답
	* Cookie :  요청에 쿠키를 제공한다.
	* cookie file은 user’s host에 저장되고 user’s browser에 의해 관리된다.
	* web site의 back-end database (웹 사이트에 내장되어 있는 어플리케이션?)
즉, 요청이 성공하면 응답으로 생성된 아이디를 준다.
다음 요청때 생성된 아이디를 보내주면 접근할 수 있다.
서버와 클라이언트는 서로 쿠키관리가 필요하다. 
쿠키를 통해서 응답 속도를 빠르게 할 수 있다.
* 쿠키는 언제 사용되나요?
	* authorization
	* shopping carts
	* recommendations
	* user session state (Web e-mail)
* 어떻게 state를 유지하나요
	* 프로토콜 endpoints : 여러 트랜잭션을 통해 보낸 사람과 받는 사람의 상태를 유지한다.
	* cookies :  http 메시지가 state를 운반합니다.

* 쿠키와 사생활 보안
	* 쿠키는 허락을 구한다 사이트에 사용자에 대해서 알아가도 되냐고
	* 사용자는 이름과 이메일을 사이트에 제공해야 한다.

###  Proxy Server (Web Cache)
* 원본 웹 서버를 대신하여 HTTP 요청을 만족 시키는 네트워크 entity
* 원본 웹 서버의 관여없이 클라이언트의 요청을 만족시키는 것이 목표다.
* 프록시 서버는 원 서버로부터의 카피를 유지하기 위해 자신만의 저장공간을 갖고 있다.
* 프록시서버는 서버와 클라이언트 역할 모두를 수행한다.
	* 클라이언트가 요청한 데이터의 카피가 있을 때는 서버 역할
	* 클라이언트가 요청한 데이터의 카피가 없을 때에는 원 서버에 요청을 보내는 클라이언트 역할
* 프록시 서버를 클라이언트와 근접한 곳에 놓으면 응답 속도가 빨라진다.
::42page 과제 겸 정리하기::

#### 프록시 서버는 주로 ISP에 의해 설치된다. (Internet Service Provider)
인터넷에 접속하는 수단을 제공하는 주체를 가리키는 말.
* Company
* portal
* university
* residential ISP
#### 프록시 서버를 왜 쓰나요
* 클라이언트 요청의 응답 속도를 줄이기 위해
* 기관의 액세스 링크에서 트래픽을 줄이기 위해
* 열악한 콘텐츠 제공 업체가 효과적으로 콘텐츠를 제공할 수 있습니다. ::(왜?):: -> P2P파일 공유처럼..?

### Conditional GET
* 캐시가 업데이트 된 상황이라면  server에서 object를 보내주지 않고 웹 브라우저의 캐시로 처리하라고 응답을 한다.

### HTTP 의 보안문제
#### privacy
* HTTP 메시지는 ASCII code로 쓰여진다. 그래서 누구나 메시지를 볼 수 있다.
#### Integrity (진실성)
* HTTP 메시지가 사용될 때는 암호화 되지 않는다.
* 그래서 보안이 낮다.
#### Authentication
* 지금 연결이 되어 있는 상대가 누구인지 모른다.
* 사용자 이름과 암호로 인증하기 위한 메시지가 악의적인 사용자에 의해 뺏길 수 있다.	

## DNS (Domain Name Server)
> The Internet’s Directory Service  









 
#3-1/컴퓨터네트워크