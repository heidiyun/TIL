# Transport layer - TCP Congestion control
## Congestion 정의
* Congestion (network congestion)
	* link 또는 node가 너무 많은 데이터를 운반해서 서비스 품질이 저하될 때 발생하는 것.
* Congestion 발생 이유
	* packet switching networks에서 공유 링크의 버퍼를 사용하여 저장하고 전달한다.
	* 무수히 많은 source들이 너무 많은 데이터들을 너무 빨리 보낸다 라우터가 감당할 수 없을 만큼 -> 라우터 버퍼가 오버플로우 걸림
* Congestion 징후
	* 패킷 손실
	* 딜레이가 길어짐
	* 연결이 안됨
* Congestion control의 목적
	* 성능이 극적으로 떨어지는 수준 아래의 통신 네트워크로 트래픽 유입을 유지하는 것
	* 즉, 너무 많은 패킷이 몰려서 네트워크를 사용할 수 없는 상황을 방지하는 것이다.
	
## Congestion 영향
* end-to-end delay
* 네트워크 용량은 정체를 제어하기 위해 제어 신호를 교환함으로써 소비됩니다.







#3-1/컴퓨터네트워크