# SDN (Software Defined Networking)
원래는 라우터에 Control Plane과 Data Plane이 물리적으로 연결되어 있었다.
logical하게만 분리되어있음. 그래서 서로 의존성이 굉장히 높음.
이 두 구역을 physical 하게 나눌 수 있으면 좋지 않을까? 라는 생각에서 시작됨.

그래서 Control plane은 SDN Controller에 의해서 프로그램적으로 관리된다.
Control plane은 Management plane의 프로그램을 통해 결과를 산출한다.
Controller는 데이터를 수집하고 그 수집한 데이터를 Management Plane에 보내서 결과를 산출. 이제 더이상 라우터라고 부르지 않는다 .  단순히 Forwarding 기능만을 수행하기 때문에 Switch라고 부른다.





#3-1/컴퓨터네트워크