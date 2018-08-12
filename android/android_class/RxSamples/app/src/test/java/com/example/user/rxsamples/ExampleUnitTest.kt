package com.example.user.rxsamples

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun kotlin_libraries() {
        val cities = listOf("Seoul", "Suwon", "Daegu", "Busan")

//        val lengths = cities.map {
//            e -> e.length
//        }
//        val lengths = cities.map { e -> e.length}
//        println(lengths)

//        cities.map { city ->
//            if (city.startsWith("S"))
//                city
//            else null
//        }.forEach{ println(it) }

//        cities.mapNotNull { city ->
//            if (city.startsWith("S"))
//                city
//            else
//                null
//        }.forEach { println(it) }

        // 하나의 일차원적인 형태로 연산의 결과가 나왔으면 flatMap 사용하기.

        val numbers = 1..6
//        numbers.flatMap { number -> 1..number }.forEach { print("$it ") }
//        numbers.map { number -> 1..number }.forEach { print("$it ") }
        // [ 1, [1, 2], [1, 2, 3], [1, 2, 3, 4] ]
        // [1, 1, 2, 1, 2, 3, 1, 2, 3, 4]

        cities.groupBy { city ->
            if (city.startsWith("S"))
                "A"
            else
                "B"
        }.forEach(::print)
    }


    @Test
    fun kotlin_libraries2() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        list.map { it % 2 }.distinct()
        // 중복된 결과를 날려주는 함수.
        // distinctBy -> 중복된 항목을 직접 처리하는 코드를 넘겨준다.
        val users = listOf("Tom", "Andy", "Peter")

        //take 리스트 앞에 있는 5개의 원소를 가져온다.
        // takeLast: 리스트 뒤 부터 원소를 가져온다.
        // takeWhile 특정 조건이 맞을 때 까지 원소를 가져온다.
        // drop : 조건에 맞는 것을 버린다.

        //stream은 데이터가 흘러 들어온다.
        //sequence는 데이터를 일괄적으로 메모리에 할당시키고 처리한다.
        // 그래서 sequence 는 적은 데이터를 처리할 때 유리하다.

    }

    @Test
    fun iter_example() {
        val iterable: Iterable<Int> = listOf(10, 20, 30, 40)
        val iterator: Iterator<Int> = iterable.iterator()

        while (iterator.hasNext()) {
            println(iterator.next())
        }
    }

    @Test
    fun rx_example() {
        Observable.just("Suwon", "Seoul")
                .subscribe { e ->
                    println(e)
                }
    }


    //rxKotlin은 RxJava기반이기 떄문에 nullable이 완벽하게 반영되지 않는다.
    @Test
    fun rx_example1() {

        val observable = Observable.just("Hello", "world", "Show", "Me")

        val disposable = observable.subscribe({ e ->
            println("onNext: $e")
        }, { err ->
            println(err)
        }, {
            println("onCompleted")
        })

        disposable.dispose()

    }

    @Test
    fun rx_example2() {
        val disposeBag = CompositeDisposable()

        val d1 = Observable.just("Hello", "world", "Show", "Me")
                .subscribe({e-> } )
        disposeBag.add(d1)

        val d2 =  Observable.just("Hello", "world", "Show", "Me")
                .subscribe({e-> } )
        disposeBag.add(d2)

//        d1.dispose()
//        d2.dispose()

        disposeBag.dispose()

    }

    @Test
    fun rx_example3() {
        val disposeBag = CompositeDisposable()
//        disposeBag.add(Observable.just("Hello", "world", "Show", "Me")
//                .subscribe({e-> } ))
//        disposeBag.add(Observable.just("Hello", "world", "Show", "Me")
//                .subscribe({e-> } ))

        disposeBag += Observable.just("Hello", "world", "Show", "Me")
                .subscribe({e-> } )

        disposeBag += Observable.just("Hello", "world", "Show", "Me")
                .subscribe({e-> } )


        disposeBag.dispose()
        disposeBag.clear()
        // 같은 역할을 수행한다.

    }
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}
// 컬렉션을 처리하던 방식을 비동기 이벤트에 도입한 것이 Rx의 장점.
