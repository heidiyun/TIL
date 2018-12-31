# 네트워크 프로그래밍
> 소켓 프로그래밍  

다른 프로세스와 통신할 수 있다.

## Process
> 실행중인 프로그램  
> 프로그램(실행 가능한 파일)의 인스턴스  

운영체제 -> Protected Mode -> 가상 메모리

Protected Mode 이전 Real Mode -> Ms Dos

프로세스가 사용하는 메모리는 반드시 물리적으로 연속적이어야 한다.
(단편화 문제가 발생할 수 있다.)

즉, 가상 메모리는 단편화 문제를 해결하기 위해 나온 것이다.

## 가상 메모리
1. 세그먼트
Windows (intel cpu의 명령어를 기반으로)
2. 페이징
Linux
* 모든 프로세스는 자신만의 페이지 테이블을 가지고 있다.
-> 모든 프로세스는 자신만의 가상 테이블을 가지고 있다.
4바이트로 표현할 수 있는 범위 -> 2^32-1 = 4G
가상적으로 메모리영역이 연속적이지만, 물리적으로는 연속적일 필요는 없다.
서로 다른 프로세스가 다른 프로세스에 접근할 수 없다. (보호모드의 뜻)
즉, 서로 다른 프로세스가 데이트를 교환하기 위해서는 IPC가 필요하다. (운영체제의 도움 없이는 다른 프로세스에 접근할 수 없다.)

### System V IPC (Inter-Process Communication)
1. Message Queue
2. Shared Memory (가상 주소가 같으면 같은 물리 메모리를 참조한다.)
3. Signal  (kill -l / 어떤 시그널을 받으면 해당 동작을 해야 한다.)
4. Semaphore (띄울 수 있는 인스턴스 개수를 제어)

-> 특정 컴퓨터에서만 사용가능 하다. 
 해당 컴퓨터를 벗어나게 되면 프로세스에 접근할 수 없다.
Socket 통신을 사용하면 이러한 제약이 없다.

## Unix
> 모든 것은 파일이다.  

VFS (Virtual File System)
 open/ read / write / close (system call)

## Java File I/O
런타임시 객체에 기능을 추가할 수 있다. (Decoration Pattern)
```java
FileInputStream fis = new FileInputStream("hello.txt");
DataInputStream bis = new DataInputStream(fis);
```
 
**단점**
결국에는 fis의 read를 사용하는 것인데, 단계가 더 추가되니 성능이 느리다.



#java/membership