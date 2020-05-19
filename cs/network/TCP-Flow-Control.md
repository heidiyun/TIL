# TCP - Flow Control
* 흐름 제어는 송신자가 수신자 버퍼를 넘칠 가능성을 제거합니다.
* 수신 응용 프로그램이 읽는 속도와 비교하여 보낸 사람이 보내는 속도와 일치하는 속도 일치 서비스.
 -> 네트워크로 잘 전송되도 receiver의 buffer에 못들어가면 손실이 생긴다. -> ::overflow::

### TCP Window size
* TCP는 가변적인 윈도우 사이즈를 허용합니다.
* 리시버가 윈도우 사이즈를 결정합니다.
	* 리시버가 ACK segment로 윈도우 사이즈를 알려준다.
	* 리시버가 받을 수 있을 만큼의 buffer의 빈 공간

### Receiver Buffer
* RcvBuffer size는 socket options를 통해 설정된다. (기본값은 4096)


#3-1/컴퓨터네트워크