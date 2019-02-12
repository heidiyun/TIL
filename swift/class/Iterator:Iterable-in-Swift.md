# Iterator / Iterable in Swift
## Iterator Pattern : GoF Design Pattern(1995)
> 의도(Intent): 복합 객체의 내부 구조에 상관없이 요소를 열거하는 패턴이다.  

Collection
 Iterator / Iterable
* Iterable: 반복의 대상
* Iterator: 반복자

**Java(Kotlin)**
```java
package xyz.ourguide;

import java.util.Iterator;
import java.util.LinkedList;

class Node {
    int value;
    Node next;

    Node(int value, Node next) {
        this.value = value;
        this.next = next;
    }
}

class SListIterator implements Iterator<Integer> {
    private Node current;

    SListIterator(Node current) {
        this.current = current;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Integer next() {
        int ret = current.value;
        current = current.next;

        return ret;
    }
}

class SList implements Iterable<Integer> {
    Node head;

    public SList() {
        this.head = null;
    }

    public void addFirst(int value) {
        head = new Node(value, head);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new SListIterator(head);
    }
}


public class Main {
    public static void main(String[] args) {
        SList list = new SList();
        // LinkedList<Integer> list = new LinkedList<>();

        list.addFirst(10);
        list.addFirst(20);
        list.addFirst(30);

        // 향상된 for 구문을 사용한다.
        //  => 복합 객체가 Iterable / Iterator 인터페이스를 만족한다면 사용할 수 있다.

        for (int e : list) {
            System.out.println(e);
        }

        /*
        Iterator<Integer> iter = list.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        */

    }
}
```

**Swift**
Iterable -> Sequence
Iterator  -> IteratorProtocol
```swift
import Foundation

// Iterable / Enumerable / Sequence
// Iterator / Enumerator / IteratorProtocol

// Sequence
protocol SListIterable {
    func makeIterator() -> SListIterator
}

protocol SListIterator {
    func next() -> Int?
}

struct MyIterator: IteratorProtocol {
    var current: Node?
    init(_ current: Node?) {
        self.current = current
    }
    
    mutating func next() -> Int? {
        if let current = current {
            defer {
                self.current = current.next
            }

            return current.value
        }

        return nil
    }
}

struct SList: Sequence {
    typealias Iterator = MyIterator
    typealias Element = Int
    
    var head: Node?
    
    mutating func addFront(value: Int) {
        head = Node(value: value, next: head)
    }
    
    func makeIterator() -> SList.Iterator {
        return Iterator(head)
    }
    
//    func makeIterator() -> IteratorProtocol {

//    }
}


class Node {
    var value: Int
    var next: Node?
    init(value: Int, next: Node?) {
        self.value = value
        self.next = next
    }
}



var list = SList()
list.addFront(value: 10)
list.addFront(value: 20)
list.addFront(value: 30)

/*
var iter = list.makeIterator()
while let e = iter.next() {
    print(e)
}
*/
for e in list {
    print(e)
}

// 5min

struct CountDown {

}

for e in CountDown(5) {
    printf(e)
}
```

응용
```swift
struct CountDown {
    var current: Int
    init(_ current: Int) {
        self.current = current;
    }
}

struct CountDownIterator: IteratorProtocol {
    typealias Element = Int
    var current: Int
    
    init(_ current: Int) {
        self.current = current
    }
    
    mutating func next() -> Int? {
        if current > 0 {
            defer {
                current -= 1
            }
            
            return current
        }
        
        return nil
    }
}

// POP(Protocol Oriented Programming)
// 코틀린의 extension과 달리 인터페이스를 더 추가해서 넣어줄 수 있다.
extension CountDown: Sequence {
    func makeIterator() -> CountDownIterator {
        return CountDownIterator(current)
    }
    
    typealias Iterator = CountDownIterator
    typealias Element = Int
}

for e in CountDown(5) {
    print(e)
}
```

#swift