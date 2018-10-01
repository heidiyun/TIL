# 9. RecyclerView로 리스트 보여주기.
## 싱글톤
> 오직 하나의 인스턴스만 생성할 수 있는 클래스  
싱글톤은 애플리케이션이 메모리에 있는 한 계속 존재한다. 
싱글톤에 저장한 데이터를 액티비티와 프래그먼트의 생명주기가 변경되는 동안에도 항상 사용할 수 있다.
단, 안드로이드 운영체제가 애플리케이션을 메모리에서 제거할 때는 싱글톤에 저장된 데이터도 함께 소멸된다.

### 사용방법
1. 생성자의 접근지정자를 private로 지정한다.
2. 생성된 인스턴스를 얻을 수 있는 get() 메소드를 만든다.
* 이미 인스턴스가 있다면 get()에서 기존 인스턴스를 반환한다.
* 인스턴스가 없으면 get()에서 생성자를 호출하여 인스턴스 생성 후 반환한다.

```java
public class CrimeLab {
    private static CrimeLab /sCrimeLab/;
    private static CrimeLab get(Context context) {
        if (/sCrimeLab/== null) {
            /sCrimeLab/= new CrimeLab(context);
        }
        return /sCrimeLab/;
    }
    
    private CrimeLab(Context context) {
        
    }
}
```

### 장점
* 메모리 낭비를 방지할 수 있다.
* 싱글톤으로 만들어진 클래스의 인스턴스는 전역 인스턴스이기 때문에 다른 클래스의 인스턴스들이 데이터를 공유하기 쉽다.
* 공통된 객체를 여러개 생성해서 사용해야하는 상황에서 좋다.

### 주의할 점
싱글톤은 앱의 유지 보수를 어렵게 만드는 형태로 잘못 사용될 수 있다.

1. 데이터를 장기간 보관하는 해결책이 아니다.
>> 데이터를 웹 서버로 전송하거나, 디스크에 쓴다.
2. 코드의 단위 테스트를 어렵게 만든다.
싱글톤 인스턴스로 많은 데이터를 공유할 경우, 다른 클래스의 인스턴스들 간에 결합도가 높아진다.
테스트와 유지보수가 어려워진다.

## RecyclerView, Adapter, ViewHolder
### RecyclerView
> ViewGroup의 서브클래스로, 자식 View 객체들의 리스트를 보여준다.  
> 리스트의 각 항목이 하나의 자식 View 객체가 된다.  

한번에 많은 VIew를 미리 생성하기보다는 필요할 때만 View 객체를 생성하는 것이 바람직하다.
RecyclerView는 한 화면을 채우는데 충분한 View의 갯수만큼만 생성한다.
화면이 스크롤되면서 View가 화면을 벗어날 때 RecyclerView를 그 View를 재활용한다.

RecyclerView는 View를 재활용하고 화면에 보여주는 책임만 갖는다.
필요한 View를 얻으려면 Adapter의 서브클래스와 ViewHolder의 서브 클래스가 함께 동작해야 한다.

#### LayoutManager
RecyclerView를 생성한 후 LayoutManager를 꼭 설정해주어야 한다.
RecyclerView는 View객체를 화면에 위치시키는 일을 직접하지 않고 LayoutManager에 위임한다. 
**LayoutManager는 View의 위치와 스크롤 동작을 정의한다.**

### ViewHolder
> 하나의 View를 보존하는 일을 한다.  

```java
class ListRow extends RecyclerView.ViewHolder {
    public ImageView mThumbnail;

    public ListRow(@NonNull View itemView) {
        super(itemView);

        mThumbnail = itemView.findViewById(R.id.thumbnail);
    }
}

/* 사용 */

ListRow row = new ListRow(inflater.inflate(R.layout.list_row, parent, false));
View view = row.itemView;
ImageView thumbnailView = row.mThumbnail;
```

itemView는 슈퍼 클래스인 RecyclerView.ViewHolder가 지정해준 필드로, 
super(itemView)의 인자로 전달한 View 객체 참조를 보존한다. 

```java
/* java */
class RepoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent./context/).inflate(R.layout./repo_item/, parent, false)
)

private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

    @NonNull
    @Override
    public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater./from/(getActivity());
        View view = layoutInflater.inflate(R.layout./list_item_crime/, parent, false);
        return new CrimeHolder(view);
    }


/* kotlin */

/* kotlin에서 findViewById를 사용하지 않게 변하면서, 
ViewHolder의 생성자에서 view를 맵핑해주는 것이 아니라, 바로 레이아웃을 인플레이트 하는 형태로 변하게 되었다.
그래서, ViewHolder의 인자로 View가 아닌 ViewGroup을 직접 받게 되었다. */

class RepoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent./context/).inflate(R.layout./repo_item/, parent, false)
)

inner class SearchListAdapter(val context: Context) : RecyclerView.Adapter<RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(parent)
    }
}
```

### Adapter
> controller 객체로 RecyclerView와 View 사이에 위치한다.  
* 필요한 ViewHolder 객체를 생성한다.
* 모델 계층의 데이터를 ViewHolder와 결합한다.

![](chapter-9-singleton-recyclerview/C9D5B392-5E6A-4C27-925A-30DF6516907E.png)
![RecyclerVIew/Adapter/ViewHolder](https://moringaschool.files.wordpress.com/2015/05/capture1.png)

1. RecyclerView에서 Adapter의 getItemCount() 메소드를 호출하여 리스트에 보여줄 객체 개수를 요청한다.
2. RecyclerView는 Adpater의 onCreateViewHolder(ViewGroup, int) 메소드를 호출하여 ViewHolder 객체를 받는다. 
3. RecyclerView는 onBindViewHolder(ViewHolder, int)를 호출하여, 리스트 항목의 위치와 함께 ViewHolder 객체를 인자로 전달한다.
4. Adpater 위치의 모델 데이터를 찾은 후 ViewHolder의 View에 결합한다.

onCreateViewHolder(…)가 onBindViewHolder(…) 보다 적게 호출될 수 있다. 
충분한 개수의 ViewHolder객체가 생성되면 RecyclerView가 onCreateViewHolder(…)호출을 중단한다.
기존에 생성된 ViewHolder 객체를 재사용하여 시가노가 메모리를 절약한다.





#android/책