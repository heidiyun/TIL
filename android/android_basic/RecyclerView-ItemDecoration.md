# RecyclerView ItemDecoration
RecyclerView를 사용할 때 그 위에 반복적으로 그리고 싶은 것이 있을 때 사용한다. ex) 그래프, 구분선

그리는 것에 대한 속성은 Paint를 사용하여 설정한다. (선의 굵기, 색상 등 등)

```kotlin
paint./color/= Color.parseColor("#4CFFFFFF")
```

## method
1. getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State)

 Margin 또는 Padding과 유사하게 동작하며, outRect의 left, right, top, bottom을 통해 조절합니다. 
각 속성에 대한 디폴트 값은 0으로 영향을 주지않습니다.

2. onDraw / onDrawOver (c: Canvas, parent: RecyclerView, state: RecyclerView.State)

onDraw와 onDrawOver 모두 자바 프로세싱의 onDraw() 함수와 유사하게 동작하며, 실질적으로 그리는 코드를 구현하는 곳입니다.

onDraw와 onDrawOver의 차이점은 list item 위에 그릴 수 있는지에 대한 여부입니다.  onDrawOver은 list item위에 겹쳐지게 그릴 수 있습니다.

ItemDecoration은 픽셀 단위로 처리하기 때문에 모든 기기에서 일정하게 동작하게 하려면 dp를 pixel로 변환해주어서 처리하는게 좋습니다.
```kotlin
parent./context/./convertDpToPixel/(16f)
```

#android/basic