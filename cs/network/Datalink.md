# 12. Datalink
## Overview
### 용어
	* nodes : hosts and routers (데이터 링크 프로토콜을 실행하는 장치)
	* links : communication path를 따라 인접한 노드와 연결된 communication 
		* 통신 경로상의 인접한 노드들을 연결하는 통신 채널.  
	* channels
		* 유선
		* 무선
	* frame : 링크를 통해 전달되는 unit
		* datagram이 encapsulated 되었다.
		
### Network and Data link layers
* 공통점 : 실제 데이터를 옮기는 일을 한다.
* 차이점 : 보도 범위가 다르다.
	* network : 완전한 소스와 데스티네이션 endsystem 사이
	* data link : 물리적으로 연결된 네트워크 디바이스 사이
		* 네트워크 장비만큼 data link가 생긴다. 
		* error 회복이 빠르다.
	
### 데이터 링크 계층은 공통 로컬 미디어를 통해 프레임을 안정적으로 전달하기위한 수단을 제공합니다.

### Datalink layer services
* framing 
	* upper layer가 미디어에 접근가능하게 한다.
* media access control (MAC)
	* 데이터가 미디어에 배치되고 미디어에서 수신되는 방식을 제어합니다.
* error dection and correction
	* 리시버에서 발견하고 수정한다.
	* ARQ mechanisms과 함께 동작한다.
* flow control
	* 인접한 송신 노드와 수신 노드 사이의 흐름 제어 페이싱 (pacing).

## Datalink layer implementation
* adaptor(NIC) or chip에서 구현됨.
* link는 소프트웨어와 하드웨어가 연결되어 있는 곳이다.

## Error detection and Correction
* 리시버는 전달된 frames에서 에러의 존재를 확인할 수 있다.
* 데이터 재전송이 가능할 때 적합하다.
* data 뒤에 checksum을 붙인다.

### Error detection
* 무선 네트워크나 long propagation delay가 있는 환경에서 적합하지 않다.
* Error correction은 error가 발생했을 때 수정할 수 있다.
	* error correction = error detection + error correction
* frame 대신 codeword로 받는다.

## Parity Check for Error Detection
###  single bit parity
* 짝수 - 1 홀수 - 0
* 두 군데 error가 발생했는데 전과 같이 짝수/홀수가 맞아서 error  detection이 안될 수 있다.
* error가 잘 발생하지 않는 환겨에서 주로 쓰인다. (data rate도 작다.) -> 집전화

### Two dimensional parity
* 행과 열로 나눠서 오류가 발생한 1bit를 찾는다.


## Cyclic Redundancy Check (CRC, 중복검사)
* LAN에서 주로 사용한다. (무선랜,  ethernet)
* XOR 을 기반으로 합니다.

## Multiple Access Protocols
* Multiple Access의 문제점
	* single shared-media technology
		* 이더넷, 무선랜
	* 동시에 두 개 이상의 신호가 오면 충돌이 발생한다.
		* 한 번에 하나의 스테이션만 신호를 보낼 수 있게 해야 한다.

### multiple access protocol 분류
* 채널 나누는 프로토콜
	* 채널을 작은 조각들로 나눈다.
	* TDMA, FDMA, CDMA
* random access protocol
	*  채널을 나누지 않고  충돌을 허용한다.
	* 충돌이 발생하면 회복한다.
		* CSMA/CM ( 이더넷)
		* CSMA/CA (무선랜)
* taking tuns protocols
	* 노드는 교대하지만 더 많은 노드를 보내려면 더 긴 전환이 필요할 수 있습니다.
		* polling
		* token passing


## Channel Partitioning protocol
### TDMA (Time Division Multiple Access)
* 시간을 time frames로 나눈다.
* 각 시간 frame은 N개의 time slot으로 나뉩니다. 
	* TDM time frame은 frame과 다르다.
* 각 time slot은 N개의 노드중에 하나에 할당됩니다.
	* 노드는 전송률이 평균 R / N bps로 제한됩니다.
	* 노드는 자신의 순서를 기다려야 합니다. 
#### 단점
* 각각이 보낼 수 있는 최대 양이 R/N으로 제한된다.
* 모든 노드가 항상 데이터를 전송하는 것은 아니므로 리소스가 낭비된다.

### FDMA (Frequency division multiple access)
* R bps 채널은 다른 주파수들로 나뉘어진다.
	* 각 노드의 bandwith는 R/N
* 각 frequency는 N개의 노드 중 하나에 항당됩니다. 
단점 : 전송 노드가 하나더라고 노드는 R/N 대역폭으로 한정됩니다. 

## Random Access Protocols
* 전송중인 노드는 항상 모든 채널을 독점해서 전송한다.
* 두 개 이상의 노드가 전송하면 collision이 일어난다. 
	* 충돌이 발생하면, 각 노드는 반복적으로 자신의 frame을 재전송하려고 시도한다.

### CSMA/CD (carrier sense multiple access)
* basic operation
	* 채널이 사용중인지 확인한다.
	* 채널이 idle하면 station은 전송할 수 있다.
	* ACK를 기다린다. 
	* ACK가 없으면 충돌이 된 것으로 감지하고 station은 재전송한다.
* ALOHA의 utilization을 능가한다.

#### CSMA approaches
* non-persistent CSMA : 일정 시간을 기다린 후 다시 재전송한다.
* 1-persistent CSMA -> 이더넷에서 사용됨 : idle해질 때 까지 상태를 추적하다가, idle해지면 즉각적으로 재전송한다. 그러나 지금 보고있는 node가 이거 하나만은 아니라서 충돌이 일어날 가능성이 높다.
* p-persistent CSMA :

#### Binary exponential backoff
* algorithm
	* m 번의 collision이후
	*  0 ~ 2(m)-1 중에 랜덤하게 고른다.
		* m 이 10 이상이면 1023이 최댓값
		* m이 16이 되면 error 보고를 포기한다.
	* NIC waits K*512 bits
	
## ARP (Address Resolution Protocol)
### MAC Address (LAN, physical, ethernet)
* 48bits ( 6bytes)
* Two parts
	* OUI (Organizational Unique Identifier) - 24 bits
		* IEEE에 의해서 관리된다.
	* Vendor Assigned Part - 24bits
		* 기관들에 의해서 관리된다. 
#### Mac Addresses are mapped with IP Addresses
* Unicast MAC Address
* 어떤 이더넷 주소든 첫 시작 바이트는 01이다.
* ip multicast address가 23bits보다 적을 때 이더넷 맥 어드레스로 복사된다.

### Adress Resolution Problem
#### MAC and IP Addresses
* ip address
	* NIC에 logically하게 할당되어서 호스트가 다른 네트워크로 들어가게 되면 바뀐다.
* mac address
	* NIC ROM안에 물리적으로 지정된 주소이다.
		* 호스트가 다른 네트워크로 들어가게 되도 바뀌지 않는다.
* Each interface has both IP and MAC addresses
* MAC address -> ARP -> IP address -> DNS -> Name

#### ARP
* LAN media에 놓여진 frame을 위한 것.
	* 이것은 ip address가 아닌 destination MAC address를 알아햐 한다.
* Two Functions of ARP
	* ip address -> MAC
	* 캐쉬에 맵핑된 것을 유지한다.

### ARP Message Format
* ethernet encapsulation

### Proxy ARP
*  라우터가 ARP Request의 boradcast를 막는다.
* proxy ARP는 로컬 네트워크 외부에 있는 호스트에 대해서 응답할 수 있도록 해줍니다.

## Ethernet
* bus : 모든 노드는 같은  충돌 도메인에 존재한다.
* start 
	* active switch가 중앙에 존재한다.
	* each sopke runs a ethernet protocol (nodes do not collide with each other)

### Feature
* Connectionless
	* sending NIC와 receiving NIC 사이에 handshake 과정이 필요없다.
	* unreliable
		* receiving NIC는 ACK를 보내지 않는다.
	* dropped frame is lost
		* TCP가 보완해준다. 
* half-duplex
	* 한번에 하나의 station만 전송할 수 있다.
* Ethernets’ MAC protocol
  
## Switched LANs
layer 1(physical) -> repeaters, hubs
layer 2(data link) -> switch, bridge
layer3(network) -> router

### Ethernet Switch
* link layer 장치는 이더넷 프레임을 저장하고 forward합니다.
	* incoming frame’s MAC address를 기반으로
	* 선택적으로 forward outgoing link에게
* 호스트는 스위치의 존재를 모른다.
* plug & play - 스위치는 꽂으면 일다 돌아간다.
* self-learning - plug and play을 동작하게 할 수 있는 기반
* 스위치는 구성할 필요가 없다.

### forwarding and filtering

#### Switch table by self-learing
* 스위치는 어떤 인퍼테이스를 통해 어떤 호스트에 도달할 수 있는지를 학습합니다.
	* source MAC address를 사용해서 학습합니다.
* 스위치는 동시에 여러개의 전송을 만들어 낼 수 있다. ( full-duplex)
	* 정확한 경로가 있어서 충돌이 일어날 이유가 없으니까 
* 보내는선/받는선이 따로 있어서 양방향 보내고 받는게 동시에 가능하다.
* 스위치가 없는 전통적 방식에서는 한번에 하나의 시그널만 전송할 수 있다.


#### Swiched LANs
* 스위치가 한 개 있을 때만 정확하게 작동한다.
	* 스위치가 여러개 연결되어 있을 때는 스위치 넘어 호스트가 어디있는지 모르니까 적용할 수 없다.

### Spanning Tree Protocol
* 네트워크의 모든 dest사이에는 하나의 logical한 경로만 존재한다.
	* multiple broadcast storms
	* loop
	* duplicate frames
* Spanning Tree Algorithm (STA)
	* 어떤 스위치 포트를 구성해야 할 지 알 수 있다.

### Switches vs Routers
* 둘다 store - forward 진행
	* 라우터 : ip address로 포워딩 진행
	* 스위치 : mac address로 포워딩 진행
* 둘 다 forwarding table을 가지고 있다.
	* 라우터 : 라우팅 알고리즘과  ip address를 이용
	* 스위치 : flooding, self-learning, mac address 이용
* 브로드캐스트
	* 라우터 : 라우터에의해 브로드캐스트 프레임이 막힌다.
	* 스위치 : 브로드캐스를 모든 스위치 포트에게 전달한다.

## Virtual LANs
> LAN에서 subLAN을 만드는 기법  

* 스위치를 서포트해주는 logical subnet
* VLAN안에서 독점적인 braodcast를 제공해준다. 














#3-1/컴퓨터네트워크#