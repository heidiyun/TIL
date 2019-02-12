# map / flatMap
```swift
let arr1 = [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
let arr2 = [5, 3]
// 변환(transform, map)
// 필터(filter)

// map / flatMap
// Swift flatMap(compactMap)은 Optional을 처리하는 용도로 사용됩니다.
// 4.1에서 flatMap -> compactMap으로 이름이 변경되었습니다.
let result = arr1.compactMap { e -> Int? in
    if e % 2 == 0 {
        return e
    } else {
        return nil
    }
}

print(result)
> [2, 4, 6, 8, 10 ]

// 다른 언어에서 통용되는 flatMap의 기능
print(arr2.flatMap {
    return 0…$0
})
> [ 0, 1, 2, 3, 4, 5, 0, 1, 2, 3 ]

// map
// flatMap
// filter
```
#swift