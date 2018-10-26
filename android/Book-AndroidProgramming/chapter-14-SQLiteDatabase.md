# 14. SQLite 데이터베이스
애플리케이션이 장기간 데이터를 저장할 곳 -> 폰의 로컬 파일 시스템
안드로이드 장치의 애플리케이션은 자신의 샌드박스에 디렉토리를 갖는다.
해당 샌드박스에 파일을 저장하면 다른 애플리케이션이나 사용자가 접근하는 것을 막아준다.

일반 파일 형태로 파일을 저장하게 되면, 한 가지를 수정하려면 파일 전체를 읽고, 파일 전체를 저장해야 한다. 
파일의 크기가 클 수록 시간이 오래걸린다.
>> SQLite 사용!!

## 스키마(Schema) 정의하기
>   database의 table 이름과 내역을 정의한다.  

| id | uuid     | title                       | date      | solved |
|----|----------|-----------------------------|-----------|--------|
| 1  | 13030303 | 요구르트 훔쳐감             | 2018/9/25 | 0      |
| 2  | 13030304 | 지저분한 싱크대를 그대로 둠 | 2018/9/30 | 1      |

## 데이터베이스 생성하기
안드로이드에서는 데이터베이스 파일을 SQLiteDatabase의 인스턴스로 열수 있는 Context의 메소드를 제공합니다.
* openOrCreateDatabase(…)
* databaseList()

**위의 메소드를 사용하지 말고 항상 아래의 절차에 따르도록 하자.**
1. 데이터베이스의 존재 여부를 확인한다.
2. 데이터베이스가 없다면, 데이터베이스와 테이블을 생성하고 초기 데이터를 추가한다.
3. 데이터베이스가 있다면, 데이터베이스를 열고 버전을 확인한다. (스키마 버전)
4. 구 버전일 경우, 새로운 버전으로 업그레이드하는 코드를 수행한다.
>> 위의 일을 처리하기 위해서 안드로이드에서는 SQLiteOpenHelper 클래스를 제공합니다.

SQLite는 하나의 데이터베이스를 하나의 파일로 저장하고 사용할 수 있게 해줍니다.

```java
SQLiteDatabase databse = new CrimeBaseHelper(mContext).getWritableDatabase();
```
**getWritableDatabase()**
1. _data_data_애플리케이션패키지명_database/데이터베이스이름.db를 연다.
만일 데이터베이스가 없으면 새로운 데이터베이스 파일을 생성한다.
2. 데이터베이스가 최초로 생성되는 경우에는 onCreate(SQLiteDatabase)메소드를 호출하고 사용자가 지정한 버전 번호를 저장한다.
3. 최초로 생성하는 것이 아닌 경우에는 데이터베이스의 버전 번호를 확인한다.
CrimeOpenHelper의 생성자 인자로 전달한 버전 번호가 데이터베이스에 저장된 것보다 높으면 onUpgrade(SQLiteDatabase, int, int)를 호출한다.


## 데이터베이스에 데이터 쓰기
1. ContentValues 클래스를 사용하여 데이터를 추가하거나 갱신할 수 있다.
-> HashMap / Bundle과 비슷하게 키와 값의 쌍으로 되어있다.


```java
insert(String, String, ContentValues)
// 첫번째 인자 : 데이터를 추가할 테이블 이름
// 두번째 인자 : nullColumnHack 생략가능하고 null이 될 수 있다.
// SQL에서 행을 추가할 땐 최소한 하나의 열을 지정해야 한다.
// 세번째 인자 : 아무 값도 갖지않는 ContentValues를 넣으면 메소드가 실행되지 않는다.

update(String, ContentValues, String, String[])
// 첫번째 인자 : 데이터를 업데이트할 테이블 이름
// 세번째 인자 : SQL의 where 절
// 네번째 인자 : where 절에 지정할 값

mDatebase.update(CrimeDbSchema.CrimeTable./NAME/, values,
        CrimeDbSchema.CrimeTable.Cols./UUID/+ " = ?",
        new String[] {uuidString});

// uuidString을 where 절에 바로 넣지 않는 이유?
// String에는 악성 SQL 구문이 포함될 수 있다.
// 그래서 ?를 꼭 사용하자.
```

## 데이터베이스의 데이터 읽기
SQLiteDatabase.query(…)는 여러형태로 오버로딩 되어 있습니다.

```java
public Cursor query(
	String table,
//테이블 이름
	String[] columns,
// 데이터 값을 얻고자 하는 열
	String where,
	String[] whereArgs,
	String groupBy,
	String having,
	String orderBy,
	String limit)
```

Cursor는 쿼리된 결과 데이터를 가져오는 데 사용된다.

```java
String uuidString = cursor.getString(cursor.getColumnIndex(CrimeTable.Cols.UUID));

String title = cursor.getString(cursor.getColumnIndex(CrimeTable.Cols.TITLE));
long date = cursor.getLong(cursor.getColumnIndex(CrimeTable.Cols.DATE));

int isSolved = cursor.getInt(cursor.getColumnIndex(CrimeTable.Cols.SOLVED));
```

이 코드들을 한 곳에서 관리해야 편하다 이때 CursorWrapper를 사용하자.

### CursorWrapper 사용하기.
> Cursor를 wrapping하여 원하는 테이블로부터 데이터를 읽을 수 있고 새로운 메서드도 추가할 수 있다.  

```java

List<Crime> crimes = new ArrayList<>();

CrimeCursorWrapper cursor = queryCrimes(null, null);

try {
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
        crimes.add(cursor.getCrime());
        cursor.moveToNext();
    }
} finally {
    cursor.close();
}

return crimes;

```
커서가 데이터베이스 커서이며, 쿼리 결과의 특정 지점을 가리킨다.
커서의 데이터를 가져오려면 우선 moveToFirst()를 호출하여 첫 번째 요소로 커서를 이동시켜야 한다.
그 다음에 행의 데이터를 읽는다.
다음 행으로 커서를 이동시킬 때 매번 moveToNext()를 호출한다. 
isAfterLast()를 호출하여 쿼리 결과 데이터가 끝났는지 확인한다.
Cursor의  close()를 호출하지 않으면 에러가 발생한다.

## 애플리케이션 컨텍스트
액티비티가 하나라도 존재하면 안드로이드는 애플리케이션 객체도 하나를 생성한다.
사용자가 애플리케이션을 이용하는 동안 액티비티는 수시로 생기거나 없어집니다.
그 동안에도 애플리케이션 객체는 계속 존재하며, 액티비티 보다 더 긴 수명을 가집니다.

CriminalIntent에서의 CrimeLab에서 Context에 대한 객체 참조를 유지하기 때문에 어떤 액티비티를 Context 객체로 저장하면 그 액티비티는 GC에 의해 절대로 소멸되지 않습니다. -> 메모리 낭비로 이어질 수 있다.

이 경우를 방지하기 위해서 애플리케이션 컨텍스트를 사용합니다.


#android/책