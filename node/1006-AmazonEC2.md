# 1006 class - Amazon EC2 
## Amazon EC2
### 인스턴스 유형에서 앞에 t로 시작하는 것.
cpu가 100프로로 돌릴 수 있는 시간이 한정적이다. (크레딧)
시간을 다 사용하면 내 컴퓨터의 우선순위가 떨어져서 영원히 일이 끝나지 않는 일이 발생할 수 있다.

### 인스턴스 유형 바꾸기
이미 서비스를 돌리고 있는데 
인스턴스 유형을 바꾸고싶다면 인스턴스 중지 후 , 작업-> 인스턴스 설정 -> 인스턴스 유형 변경

Image: storage
Database : textData
Volume: 하드디스크

### 키페어 이름 규칙
키페어 이름에 region을 붙이는 것이 좋다.

### Instance에 접속하기.
퍼블릭 DNS ip 로도 인스턴스 접근가능
`ssh -i keypair public DNS ip`

#node