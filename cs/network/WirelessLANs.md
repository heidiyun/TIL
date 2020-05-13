# Wireless LANs
* IEEE 802.11 


## IEEE 802.11 Architecture
> Wireless Networks  

### Elements of a wireless network
* wireless hosts
	* 어플리케이션을 실행시킨다.
	* non-mobile or mobile
* base station (BS)
	* 보통 유선으로 연결되어있다.
	* relay - 아리아에서 유선 네트워크와 무선 호스트사이에서 패킷을 전송한다.
	* Access Pointes(AP)
* wireless link
	* 일반적으로 모바일을 BS에 연결하는데 쓰입니다.
	*  backbone link 처럼 쓰입니다.
	* multiple access protocol
	* various data rates, transmission distance

### Two types of wireless network architecture (무선 네트워크 구조의 두가지 타입)
* infrastructure mode
	* 모바일을 유선 네트워크로 연결시켜준다.
		* 모바일은 BS를 통해서만 소통가능하다.
		* 모바일 사이에는 직접적인 연결이 없다.
	* hand off
		* 모바일은 BS를 옮겨다닌다.
* ad-hoc mode
	* BS가 없다.
	* 노드는 그들 스스로 네트워크를 형성한다.
	* 노드는 link coverage안에서 직접적으로 소통할 수 있다.
	* 메세지는 다른 노드를 통해서 전달된다.
		* 라우팅이 필요 할 수도 잇습니다. (MANET, Mobile Ad-hoc Network)
			* 노드 간의 거리가 멀면 중간 무선장치를 거쳐서 전달한다. 
### ISM (industrial, scientific, and medical) bands
* 무선 전파는 정부에서 관리한다.
* 낮은 주파수는 적은 전력으로도 멀리까지 통신할 수 있지만, 자연신호와 겹쳐져서 잡음이 많다.
* 높은 주파수는 전력을 많이 잡아먹지만 잡음이 별로 없다. 그러나 비싸다.

### channels
* 2.4GHz ISM band is divided into 14 channels 5MHz apart
* 실사용 가능한 주파수는 3가지이다.

### WLAN Association
> 호스트는 AP와 연결되어야 한다.  

* passive scanning
	* AP가 주기적으로 Beacon을 보낸다.
	* AP정보가 들어있다.
* active scanning
	* host가 요청을 보내면 거기에 대한 response가 AP로 부터 온다.
	* host가 AP를 선택한다.
	* AP가 지원해줘야 사용할 수 있는 기능

### MAC Protocol - Multiple Access
* Multiple Access Problem
	* single shared-media technology - Ethernet, WLANs
	* 한 번에 하나의 스테이션만 데이터를 전송할 수 있다.
* CSMA/CD

### 802.11 MAC CD를 제공하지 않는다.
* 무선은 유선과 다르게 거리가 멀어질 수록 신호가 약해진다.
	* 충돌을 감지하기가 힘들다.
* adaptor가 모든 신호를 감지할 수 없다.
	* the hidden terminal problem
	* fading

### 802.11 MAC Protocol
* PCF (Point Coordination Function)
* DCF (Distributed Coordination Function)
	* 비동기 데이터 서비스
	* CSMA/CA - collision을 사전에 방지한다., binary backoff
	* 모든 단말들이 똑같은 상황을 인식해서 맡은 동작을 하는 방식.
* RTS/CTS mechanism
	* hidden terminal problem을 완화할 수 있다.
	* DCF에서 사용되는 선택적 가상 캐리어 감지 메커니즘.
	* DCF는 숨겨진 termianl 및 / 또는 exported terminal 문제를 완전히 해결하지 못합니다.

## CSMA/CA
CSMA with coliision Avoidance
### IFS (Inter Frame Space)
* DIFS (distributed coordination fuction IFS)
	* 긴 IFS
	* 액세스를 위해 경쟁하는 비동기 프레임의 최소 지연.
	* basic IFS
* SIFS (short IFS)
	* 간격이 짧은 IFS
	* ACK와 같은 즉각적인 응답을 위해서 쓰인다.
* PIFS (Point coordination function IFS)
	* 중간 IFS
	* PCF를 위해서 사용되는 것.
	
### Link Layer Ack
* 전송시 : DIFS
* 응답시 : SIFS






#3-1/컴퓨터네트워크