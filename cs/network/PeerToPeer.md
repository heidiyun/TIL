# Application layer - Peer-To-Peer Model
## Client-Server Architecture
* 반드시 서버가 필요하다.
* 업로드/분배 하는 보든 파일은 관리된다.
* 불법 유통 파일을 찾기 쉽다.
* 서버에 연결되는 클라이언트의 수가 많아질 수록 다운로드의 문제가 생긴다.

### Server
* always-on-host
* 고정 IP 주소
* scaling을 위한 data center가 있다.

### Client
* 서버와 연결됨
연결이 지속되지 않고 필요할 때만.
* 가변 IP 주소
* 다른 클라이언트와 직접적으로 연결될 수 없음

## Peer-To-Peer Architecture
* 서버가 없다. 
* 업로드가 쉽다
* 중앙집중 서버가 없어서 다운로드 속도에 문제가 없다.
* 파일 공유에 대한 관리가 없다.
(불법 유통을 막기가 힘듬.)

### P2P network is an overlay network
> 사용가능한 주소 이상의 프로그램 작동가능하게 하는 기법  
* 피어는 데이터를 교환하기 위해 logical direct connection을 가지고 있다.
* 피어들간의 링크는 하나 또는 그 이상의 physical IP link로 이루어져 있다.

### No always-on server
피어는 연결을 필요할 때만 맺을 수 있고, 가변 IP 주소를 갖는다.

### Arbitrary end systems can directly communicate
> 임의의 end system이 직접적으로 연결을 맺을 수 있다.  
> 즉, 피어들이 직접적으로 연결을 맺을 수 있다.  

## DHT (Distributed Hash Table)
### Simple Database
* Key - Value pairs
	* key를 통해 databae를 탐색한다.
	
### Distributed Database on P2P
P2P -> a distributed network database for file search
	    파일 검색을 위한 분산 네트워크 데이터베이스
* key :  filename
* value : 파일 컨텐츠를 갖고 있는 피어의 IP 주소

### Hash Function
> 길이가 다른 데이터를 처리하기 어려워서 일정한 길이를 갖는 hash key로 변환하는 것.  
*  키의 숫자 표현
	* original key : 문자 & 가변길이
	* hash key :  숫자 & 고정길이
* hash key는 중복되면 안된다.

### DHT
* key value pairs는 수많은 피어들에 분산되어 있다.
* 어떤 피어든 key를 통해 데이터베이스를 쿼리할 수 있다.
* 각 피어는 전체 피어를 다 알지 못하고 일부만 안다.
* 피어는 아무런 경고 없이 나갔다 들어왔다를 반복한다. 
	* peer churn
 
### DHT - Assign key-value pairs to peers
* 각 피어는 다른 피어와 중복되지 않는 고유 ID를 가진다.
	* 아이디는 처음 접속할 때 임의적으로 만들어진다.
	* 아이디의 비트 수는 키의 비트수와 같다.
	* 즉 내 아이디가  m bit라면 key도 m bit
	* 아이디는 0~2(m-1)까지 가질 수 있다.

* 키 벨류 페어는 가장 인접한 ID를 가진 피어에게 할당된다.
* 같은 파일을 가진 피어가 많을 수록 다운로드 속도가 빠르다.
* 파일을 조각조각 동시에 받을 수 있기 때문이다.

## Circular DHT
* Successor and Predecessor
	* 다음에 있는 피어 , 이전에 있는 피어
	* 피어는 id의 순서로 원을 형성하고 있다.
	* 각 피어는 successor와 predecessor만을 알고있다.
* Complexity for Query Resolutions 
	* N개의 피어가 있다면 쿼리를 처리하기 위한 평균 message는 N개에 비례한다.
	* 즉, 피어가 많을수록 해당 key를 찾아가기 위해 쿼리하는 횟수가 많아진다.
	* O(N)
	
### Shortcut Mechanism
* 해당 피어가 다른 피어에게 응답을 받으면 그 피어의 존재를 알고 있다.
* 쿼리할 때 이 short cuts를 이용한다.
* O(log N)
*  shortcuts가 없으면 sucessor를 이용하여 쿼리한다.

### Peer Churn
* Handling peer churn
	* 각피어는 두 개의 successor의 주소까지 알 수 있다.
		* Immediate successor
		* Next successor
	* 각 피어는 주기적으로 두 개의 successor의 존재를 확인한다.
		* ping message를 보낸다.
#### Leaving Peer
		* immediate successor가 나갔으면 next successor를 immediate successor로 등록한다.
		* immediate successor가 된 피어에게 누가 너의 immediate successor냐고 물어본 후, next successor로 등록한다.
#### Joining Peer
* 피어가 접속할 때 기존 피어들 (well-known peers)을 알 수 있다.
* P2P네트워크 운영자는 well-known peers를 웹페이지에 게시합니다.
* 그럼 기존 피어에게 조인 메시지를 보내면 쿼리합니다.
* 쿼리하다가 범위에 맞는 피어를 만나면 그 피어가 조인하려는 피어에게 JOIN-ACK 메시지를 보냅니다. (해당 피어의 successor/predeccessor 정보와 함께)


## P2P Systems and Applications
* Types of centralized and distributed network architecture
(중앙집중과 분산 네트워크 구조)
	* 각 노드(피어)는 서버/ 클라이언트 역할을 모두 한다.
	* 모든 피어는 동일한 능력과 책임을 가지고 있다.
	* 모든 통신은 대칭(symmetric) 및 분산(decentralized)입니다.
* 운영은 관리되지 않는 자발적 참여자들에 의해 수행된다.

### P2P Applications

### BitTorrent
* 현재 인터넷 트래픽의 20-50퍼센트는 BitTorrent이다.
* 클라이언트 소프트웨어 프로그램이 필요하다.

* Hybrid architecture
	* tracker-based centralized architecture
		* 서버는 ‘.torrent’ 파일을 관리한다.  (tracker의 주소와 파일의 정보가 든 파일)
		* 서버를 중간에 놓아서 사용. 주로 사용한다.
	* trackless decentralized architecture
		* Kademlia DHT 사용.
		
위의 두가지 방식을 동시에 사용하지 않고 둘 중 하나만 채택해서 사용한다.

#### Three Entities
1. Tracker (서버)
swarms를 관리하는 책임을 가짐.
2. Seeders 
파일에 대한 모든 피스를 소유하고 있는 피어
3. Leechers
파일이 없거나 일부분만을 소유하고 있는 피어
#### Swarm
* 피어 집단
: 해시 된 파일 디렉토리 파일 조각 파일 정보 등의 콘텐츠를 동일한 파일 식별자로 공유한다.

#### BitTorrent Operation
1. seeder가 자신이 같고 있는 완전한 정보를 공유하기 원하면, 
seeder는 트래커에게 이 사실을 알리고 torrent file을 웹서버에 올린다.
2. 정보를 다운로드 받고 싶어하는 클라이언트가 웹서버에 검색한다.
3. 토렌트 파일에 있는 정보로 클라이언트는 트래커와 TCP connection을 맺는다. 
트래커는 해당 콘텐츠를 갖고 있는 swram의 존재를 확인하고, swram을 찾으면 그 스왐에 대한 정보를 클라이언트에게 제공한다.
4. 클라이언트는 피어와 소통하기 시작함 ( 트래커의 관여 없이)
5. 피어는 파일을 누가 소유하고 있는 지 등에 대한 정볼르 트래커에게 전송한다.







#3-1/컴퓨터네트워크