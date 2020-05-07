#  Chapter-1 Protocol
## 프로토콜의 여러가지 정의
* Society
	* 에티켓
	* 각 국가간의 약속된 규약
* Science
	* 실험을 수행하기 위해 사전 정의 된 서면적 절차 방법
	* 임상 시험 계획 서류
* Communications
	* 통신 및 컴퓨터 네트워킹에서 데이터가 전송되는 방식을 결정하는 규정의 집약.

### Communication Protocol
1. 성공적인 커뮤니케이션을 위해서는 상대방이 있어야 한다.
둘간의 정보교환을 성공적으로 하는 것이 커뮤니케이션의 목적이다.
* 주제
* 통신 수단
* 통신 시간

통신 단체가 정보를 성공적으로 교환하기 위해 정한 협약을 프로토콜이라고 합니다.

Network Protocol 이란.
컴퓨터 간의 정보 교환을 성공적으로 하기 위해 정한 협약

###  Protocol의 3가지 요소
1. Syntax : 데이터 형식
전기적 신호를 나타내는 signal level
2. Semantics : 의미 절차
메시지 전송 및 수신시 수행되는 작업
커뮤니케이션시 어떤 행위에 대한 일련의 절차를 정의한 것.
3. Timing : 메시지의 순서
Sequence number (시간에 대한 정보를 포함)

### Layer의 필요성
* protocol은 같이 레이어 간의 정보 교환을 위한 약속
* interface는 서로 다른 레이어를 연결시켜주는 것.

레이어에서 가장 중요한 개념은 ::Independence::
각 레이어는 직접적으로 연결되어 있지 않아서 각 레이어를 연결시켜주는 매개체가 필요하다. 그것이 바로 interface

### Layerd Protocol Reference Model
* Layerd Model 사용의 이점
1. 프로토콜 설계를 쉽게 할 수 있다.
2. 경쟁을 통해서 이익을 취한다.
3. 한 layer가 변한다고 해서 다른레이어에게 영향을 주지 않는다.
4. 공통 언어를 제공한다.
5. 프로토콜을 배우고 이해하기가 쉽다.

### 성공정인 통신을 위한 3가지 조건
1. 레이어 개수가 같아야 한다. (Peer-to-peer protocol)
2. 각 레이어의 프로토콜이 일치해야 한다.
3. 각 레이어를 연결해주는 인터페이스가 같아야 한다.

표준화된 레퍼런스 프로토콜 아키텍처
1. 어떤 장치든 간에 모든 디바이스와 통신할 수 있다.
2. 모든 공급 업체는 시장성있는 제품을 제공할 수 있다.
3. 공급업체와 독립적으로 통신 장치를 선택할 수 있다.

### OSI(Open Systems Interconnection)
* iso에서 만듬.
* 이론적으로만 제공한다.
* 7개의 레이어
### TCP/IP protocol suite
* de facto / 사실상 표준
* 빠르게 사람들에게 배포되었다.
* 5개의 레이어
* RFC(Request For Comment)

#### OSI 7 Layer
<1,2,3은 OSI 7 layer의 고유 기능>
1. Application
OSI 환경에 접근하기 위한 어플리케이션을 의미한다.
즉, 사용자가 보는 화면!
2. Presentation
콘텐츠를 압축하고 암호화하는 것.
어플리케이션을 보여주는 것
3. Session
어플리케이션 사이의 동작을 제어한다.
즉, 송출자와 응답자 사이의 제어 기능을 제공한다.

<4~7은 공통기능>
4. Transport
상대방 컴퓨터에서 데이터가 사용될 수 있도록
모으고/ 분배
정보가 전달되는 형식을 결정
5. Network
응답 컴퓨터와 전송 컴퓨터사이에 전달을 담당한다.
직접전인 링크가 필요가 없다.
6. Data Link
네트워크 장비 간의 정보전달을 책임진다.
하나의 구리선, 무선. 선 자체에서 정보를 전달하는 책임
7. Physical Layer
전기적 신호로 바꿔서 보내주는 것
선 자체에서 보내주는 신호

왜 TCP/IP에서는 Application으로 묶어놨을까?

#### TCP/IP
TCP(Transmission Control Protocol)
UDP(User Datagram Protocol)
SDN(Software defined networking) 소프트웨어 정의 네트워킹

정보를 전달하는 쪽 - Encapsulation
캡슐 안에 있는 것은 건들이지 않는다.
각 레이어에서 만들어진 정보는 수정하지 않는다.
위의 레이어에서 내려온 정보의 단위 - SDU(Service Data Unit)
해당하는 레이어에서 처리할 syntax에 해당하는 header를 붙여준다.
 이 헤더를 붙인 정보를 PDU라고 부른다. (Protocol Data Unit)

각 헤더에 syntax에 정의 된 것 중에 하나가 type이라는 것이 있다.
SDU만 해당하는 프로토콜에 보내준다.
PDU정보를 보면 자기 레이어에 해당하는 헤더에서 필요한 정보를 빼내서 본다.

#3-1/컴퓨터네트워크