# 19. SoundPool을 사용한 오디오 재생
## SoundPool
> 안드로이드 오디오 API를 쉽게 사용할 수 있게 해준다.  
* 애플리케이션 내부의 리소스나 파일 시스템의 파일로부터 음원을 메모리로 로드하고 재생해준다. 
* 한 번에 재생할 수 있는 음원의 최대 개수를 제어할 수 있다.
* 사용자가 애플리케이션의 모든 버튼을 동시에 누르더라도 실행이나 장치에는 영향을 주지 않는다.
* MediaPlayer 서비스를 사용해서 16비트 PCM 모노나 스테레오 스트림으로 음원을 디코딩해준다. 즉, 압축 음원을 애플리케이션에 포함시켜 배포할 수 있다.
압축된 음원의 재생에 따른 CPU 사용과 압축 해제 지연 시간에 대한 부담을 줄일 수 있다.

### SoundPool 생성하기
1. 롤리팝 이전 버전
```kotlin
val soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0);
// 첫번째 인자 : 최대 실행할 수 있는 음원의 개수
// 6번째 음원을 실행하려고 하면 가장 오래된 것의 재생을 중지한다.
// 두번째 인자 : 재생할 오디오 스트림의 종류
// 오디오 스트림의 종류별로 독자적인 볼륨 설정을 갖고 있다.
// 위의 인자는 장치의 음악과 게임이 동일한 볼륨 설정을 갖는다.
// 세번째 인자: 아무런 영향을 주지 않으므로 0으로 설정한다.
```

2. 롤리팝 이후 버전
```kotlin
val soundPool = SoundPool.Builder().setAudioAttributes(audio).setMaxStreams(5).build();
```

### 음원 로드하기
SoundPool을 사용하면 다른 도구를 사용하는 것 보다 응답이 빠르다.
```kotlinval 
val assetFileDescriptor = assets.openFd(sound.assetPath)
val soundId = soundPool.load(assetFileDescriptor, 1)
```

### 음원 재생하기
```kotlin
soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
// 왼쪽 볼륨, 오른쪽 볼륨, 
// 스트림 우선순위 (0이면 최저 우선순위)
// 반복 재생 여부  (0이면 반복 안함)
// 재생률 (1 : 녹음된 속도 그대로, 2 : 두배빠르게, 0.5: 절반 느리게)
```

### 음원 리소스 해지하기.
```kotlin
soundPool.release()
```

## 장치의 방향 회전과 객체의 지속성
장치의 방향이 변경되면 activity가 파괴되어 음원의 재생도 중단 될 것이다.
장치의 방향이 변경될 때 bundle 객체에 보존할 데이터를 넣었지만, 이 경우에는 사용할 수 없다.

Bundle 객체 내부의 Parcelable 데이터를 사용해서 데이터를 보존하고 복원하는데, 객체가 보존 가능한 상태일 때만 가능하다.
SoundPool은 보존 가능한 객체가 아니다.

### 프래그먼트 유보하기.
장치의 구성이 변경되어도 인스턴스를 계속 사용할 수 있도록 해주는 기능이 프래그먼트에 있다. -> retainInstance

```kotlin
class BeatBoxFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /retainInstance/= true
    }
}
```

**retainInstance의 값을 true로 설정하면, 프래그먼트가 액티비티와 함께 소멸되지 않고 보존되었다가 새로운 액티비티 인스턴스에 전달된다.**

프래그먼트를 유보하면 모든 인스턴스 변수를 이전과 동일한 값으로 유지할 수 있다.

### 유보 프래그먼트
프래그먼트 인스턴스는 소멸되지 않으면서 뷰는 소멸되고 재생성할 수 있다.

장치의 구성이 변경되는 동안 FragmentManager는 리스트에 있는 프래그먼트들의 뷰를 소멸시킨다. 구성이 달라졌으므로 새로운 리소스가 필요하고, 더 적합한 리소스들이 사용 가능한 경우에 뷰를 새로 생성한다. 

그 다음 FragmentManager는 각 프래그먼트의 retainInstance 속성을 확인한다.
false면 프래그먼트 인스턴스를 소멸시킨다. 해당 프래그먼트는 새로운 액티비티의 새로운 FragmentManager에 의해 새로 생성된다.

true면 새로운 액티비티가 생성되면 새로운 FragmentManager가 유보 프래그먼트를 찾아서 그것의 뷰만 새로 생성한다. 

프래그먼트는 소멸되는 것이 아니라 호스팅 액티비티로부터 분리되는 것이다.

### 고려사항
* 유보 프래그먼트는 유보되지 않는 프래그먼트보다 더 복잡하다. 오류가 났을 때 그 원인을 파악하기가 더 어렵다.
* 유보 프래그먼트는 구성 변경에 따른 액티비티 소멸 상황만을 처리한다. 운영체제가 메모리를 회수하여 액티비티가 소멸된다면 모든 유보 프래그먼트도 소멸된다. 즉, 프래그먼트 데이터가 유실 될 수 있다. 
* 유보 프래그먼트는 데이터를 짧은 시간동안만 보존할 때만 쓰인다. 
액티비티나 프래그먼트에 오래 보존해야 할 데이터가 있다면 유보 프래그먼트를 쓰는 것은 위험하다.

#android/책