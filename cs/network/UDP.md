# CH3. Transport Layer - UDP
> User Datagram Protocol  

## Transport Layer Overview
* Transport layer의 SDU : segment
* Transport layer의 protocol : TCP, UDP

### Transport vs Network Layer
* Network Layer
	* logical communication between hosts across the Internet
	인터넷으로 연결되어 있는 호스트 사이의 logical communication
	* logical : source <-> end host
	* physical : 선으로 연결되어 있다. (Data link가 관리한다.)
	* end system : Application-transport-network-datalink-physical layer가 모두 있는 host를 이야기 한다.
*  Transport layer
	* logical communication between app processes running on different hosts(end systems)
	다른 호스트 상에서 실행되고 있는 앱 프로세스 사이의 logical communication
		* send side :  앱 메시지를 세그먼트로 변환하여 네트워크 계층으로 전달
		* rcv side :  세그먼트를 메지시로 다시 어셈블하여 어플리케이션 계층으로 전달
	* 하나 이상의 transport protocols이 사용가능하다.

### Transport Layer Protocols
1. TCP (Transmission Control Protocol)
	* connection-oriented and reliable
	* in-order delivery
	* congestion control
	* flow control
	* connection setup delay
2. UDP (User Datagram Protocol)
	* connectionless and unreliable
	* unordered delivery
	* no-frills extension of “best-effort”  IP
	* no setup delay -> fast
	* left traffic is sent across the network

### Primary Responsibilities of Transport Layer (트랜스포트 레이어의 주요 책임)

* TCP와 UDP에서 제공하는 공통 기능
	* port number로 어플리케이션 식별
		* multiplexing and demultiplexing
	* Error Detection (Checksum)
		* 각 레이어에 들어있는 PDU가 오류가 있는지 판별
* TCP만 제공하는 기능
	*  Segmentation Reassembly
	* 어플리케이션 사의의 개별적인 커뮤니케이션을 추적한다.
	* 세션을 형성
	* 안전한 전달
	* flow control
	* congestion control

### Multiplexing 
> source end system에서의 job  
* 각기 다른 어플리케이션으로부터 데이터를 수집한다. (socket을 통해서)
* 포트 넘버와 함께 segment를 만들고 네트워크 레이어에 전달한다.

### Demultiplexing
> destination end system의 job  
* 수신된 포트 넘버를 조사하여 어플리케이션 프로세스를 알아낸다.
* 세그먼트를 decapsulation하여  어플리케이션에 전달한다.

### Port Number
* 각 어플리케이션은 end system에 고유의 port number를 갖고 있습니다.
* port number는 다양한 어플리케이션 프로토콜을 구분한다.
* 16bit (65535)

* IANA (Internet Assigned Numbers Authority)가 포트넘버를 할당해 준다.
-> 다양한 주소를 할당하는 책임을 가진 기구.

### FYI : Application, Socket and TCP/IP
::녹음듣기::

* 서버는 많은 TCP connection을 동시에 지원합니다.
	* 이 서버를 concurrent server라 한다.
	* 각 connection은 자신만의 four tuple로 구분됩니다.
		* source ip address
		* source port
		* destination ip address
		* destination port

## UDP (User Datagram Protocol)
### Network (IP) Layer - Datagram Packet Switching
* 각각의 packet은 독립적으로 처리된다.
	* 패킷은 실제 경로를 취할 수 있다.
		* 경로를 정하는 건 각 패킷마다 필요하다
즉, 이전의 히스토리를 관리하지 않아서 현재 라우팅 결정에만 따른다.
이전결과와 반드시 같지 않다.
* Connectionless packet delivery
	* 패킷은 순서대로 전달되지 않는다. (Out-of-order)
	* 패킷은 중간에 유실될 수 있다.
목적지가 같아도 전달 순서와 상관없이 도착한다.
이동경로도 각각 다를 수 있다.
* Unreliable
* receiver는 순서를 다시 정하는 것을 필요로 한다.
* receiver는 유실된 패킷을 되찾는 것이 필요하다.

###  특징
1. Internet transport protocol의 특징을 그대로 쓴다.
2. Lost, out-of-drder
3. connectionless
:  sender와 receiver사의의 어떠한 연결도 없다.
4. Low Overhead
:  has small header(8 bytes)
   네트워크 관리 트래픽이 없다.
5. Congestion control, connection이 없어서 빠르다.
	 
#3-1/컴퓨터네트워크