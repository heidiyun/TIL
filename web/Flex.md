# Flex
## 부모 요소 : flex container
* display: flex 속성을 가지고 있다.	
*  flex-direction
	* row (default)
![](Flex/C9227280-9F67-4189-8C96-443D3DDC411E.png)
	* row-reverse
			
![](Flex/FF7CB004-E457-44C0-B28C-B8C30F25CDA4.png)

	* column
	
![](Flex/72DC899C-A2BA-46E1-85A9-71A1CE0A6084.png)

	* column-reverse
![](Flex/3D56B786-89B2-4941-BECB-98E04CFBA29F.png)

* flex-wrap
	* 자식이 부모의 크기를 벗어났을 때 줄을 바꾸는 속성
	* 기본 값 : nowrap -> 그냥 부모를 벗어나도록 냅둠
	* wrap -> 줄바꿈을 실행함.
![](Flex/21447D51-083D-4F2C-8DA9-A4D1A9EBB9E9.png)
	* direction과 wrap 같이 사용할 수 있음 
		* flex-flow : column wrap

* justify-content
	* flex-start(기본)  : 시작 부분 부터차례로 정렬
	* center: 중앙 정렬
	* flex-end : 끝 부분부터 차례로 정렬
	* space-around : 일정한 간격으로 정렬
	* space-between : 끝부분과 시작부분에 먼저 정렬한 뒤 남은 영역을 일정한 간격으로 정렬

![](Flex/B7291366-85E4-48CA-9552-8EE21DDBFE9C.png)

* align-items
	* stretch (기본) : 자식 요소의 높이를 늘려 부모의 높이만큼 채운다.
	* flex-start: y의 시작부분을부터 정렬
	* center :  y의 중앙부분에 정렬
	* baseline : content의 글자가 끝나는 부분을 기준으로 정렬
	* flex-end : y의 끝부분부터 정렬
![](Flex/2CCECE6E-C429-4176-8769-5840ABAA5109.png)

* align-content
	* 자식이 한 줄 이상일 경우 수직 정렬 방법을 설정
	* stretch (기본) : 자식 요소의 높이를 늘려 부모의 높이만큼 채운다.
	* flex-start: y의 시작부분을부터 정렬
	* center :  y의 중앙부분에 정렬
	* flex-end : y의 끝부분부터 정렬
	* space-around : x,y가 교차하는 중앙부분을 기준으로 자식을 일정한 간격으로 정렬
	* space-between: 첫번째 마지막 요소를 y의 시작과 끝에 배치한 후 나머지 부분을 일정한 영역으로 정렬.
![](Flex/19286B6E-25A3-4A83-890B-0E80D421849E.png)


## 자식 요소 : flex item
	* 부모 요소의 flex-direction에 따라 방향이 결정됨. 
	* flex 
		* 아래 세개 요소를 축약한 표현.
		* flex : 1 -> flex : 1 1 0 -> flex container에 따라 크기 변동.
		* 하나의 값만 주면 flex-grow를 조절
		1. initial : 부모가 줄어들면 크기가 같이 줄어들지만, 부모가 커진다고 크기가 같이 커지진 않음.
		2. none : 부모와 상관 없이 내 크기는 유지됨.
		3. auto : 부모에 맞게 크기가 변동.

![](Flex/C1065280-E72D-4A3B-B414-3BF8D448B612.png)

	* flex-grow
		* 양의 정수로 값 표현
		* 0이면 부모의 크기에 상관 없이 원래 크기 유지.
		* 1이면 부모의 크기에 따라 크기 변동
		* 1이상이면 나의 원래 크기에 상관 없이 부모를 가득 채운다.
	* flex-shrink
		* 기본 값 : 1
		* 0이면 flex container의 크기가 나의 크기보다 작아져도 원래 크기 유지
		* 1이상이면 부모에 따라 크기가 작아짐
	* flex-basis
		* 고정 크기 설정
		* 0이면 안드로이드의 wrap_content와 같이 설정됨. 꼭 단위 명시!
		* auto : 각 요소의 content에 따라 가중치 알아서 적용.
		
![](Flex/D1C5B933-7CF7-45D1-8C08-445DB2E8717E.png)

* margin 
	* auto : 중앙에 정렬
	* left & right : 수평정렬 시 사용 
		* auto : 왼쪽 정렬 또는 오른쪽 정렬로 됨.
	* top & bottom : 수직 정렬시 사용
		* auto : 위쪽에 붙여서 정렬, 아래쪽에 붙여서 정렬.
#web