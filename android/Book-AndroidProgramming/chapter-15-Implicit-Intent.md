# 15. 암시적 인텐트
> 암시적 인텐트 : 장치의 다른 애플리케이션에 있는 액티비티를 실행시킬 수 있다.  
> 명시적 인텐트 : 같은 애플리케이션의 액티비티를 실행시킬 수 있다.  

암시적 인텐트에서는 필요한 작업을 알려주면 작업에 적합한 애플리케이션의 액티비티를 안드로이드 운영체제가 찾아서 시작시켜준다.

## 암시적 인텐트 사용하기.
### 구성요소
1. action
Intent Class의 상수.
어떤 일을 할 것인지를 정의한다.
ex) Intent.ACTION_VIEW
2. 데이터의 위치
sheme, host
3.  category
액티비티를 어디서, 언제, 어떻게 사용할지를 나타낸다.

예 : 웹 브라우저를 보기를 원할 때
```xml
<activity
	android:name=".BrowserActivity"
	android:label="@string/app_name">

	<intent-filter>
		<action android:name="android.intent.action.VIEW" />
		<category android:name="android.intent.category.DEFAULT" />
		<data android:scheme="http" android:host="www.heidiyun.com" />
	</intent-filter>
</activity>

```

**암시적 인텐트에 응답하려면 DEFAULT 카테코리를 인텐트 필터에 명시적으로 설정해야 한다.**

명시적 인텐트가 엑스트라를 포함할 수 있는 것처럼, 암시적 인텐트도 추가적인 정보를 포함 할 수 있다.

선택기(사용할 애플리케이션) 화면이 나오지 않는다면 이미 이와 동일한 암시적 인텐트의 디폴트 앱을 설정했거나(항상 이 애플리케이션으로 연결해주자.), 응답할 수 있는 액티비티가 장치에 하나만 있는 경우.

**매번 선택기 화면이 나오게 하는 방법**
```java
/* CrimeFragment */
mReportButton.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent./ACTION_SEND/);
        intent.setType("text/plain");
        intent.putExtra(Intent./EXTRA_TEXT/, getCrimeReport());
        intent.putExtra(Intent./EXTRA_SUBJECT/,
                getString(R.string./crime_report_subject/));
        intent = Intent./createChooser/(intent, getString(R.string./send_report/));
        startActivity(intent);
    }
});


// intent = Intent./createChooser/(intent, getString(R.string./send_report/)); 를 추가하였습니다.
```

### 안드로이드에 연락처 요청하기.

> 사용자들이 연락처에서 용의자를 선택할 수 있게 해주는 암시적 인텐트를 생성할 것이다.  

action : Intent.ACTION_PICK
-> 연락처 데이터 베이스에서 항목 하나를 선택할 수 있게 안드로이드에 요청한다.

```java
final Intent pickContact = new Intent(Intent./ACTION_PICK/,
        ContactsContract.Contacts./CONTENT_URI/);

```

#### 연락처에서 데이터 가져오기.
안드로이드는 ContentProvide를 통해 연락처 정보와 함께 동작하는 상세한 API를 제공한다.
이 클래스의 인스턴스들은 데이터베이스를 포함하며 그것을 다른 애플리케이션에서 사용할 수 있게 해준다. 
ContentResolver를 통해서 ContentProvide를 액세스할 수 있다.

```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity./RESULT_OK/) {
        return;
    }
   if (requestCode == /REQUEST_CONTACT/&& data != null) {
        Uri contactUri = data.getData();
        String[] queryFields = new String[]{
                ContactsContract.Contacts./DISPLAY_NAME/
			// 값을 반환할 쿼리 필드를 지정한다.
};

        Cursor cursor = getActivity().getContentResolver()
                .query(contactUri, queryFields, null, null, null);
// 쿼리에서 반환된 결과 값을 읽거나 쓰는 데 필요한 cursor 객체를 얻는다. 
// contactUri -> SQL Where에 해당한다.

        try {
				// 결과 데이터가 있는지 재확인한다.
            if (cursor.getCount() == 0) {
                return;
            }

				// 첫번째 데이터 행의 첫번째 열을 추출.	
				// 용의자 이름.
            cursor.moveToFirst();
            String suspect = cursor.getString(0);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);

        } finally {
            cursor.close();
        }
    }
}

```

### 연락처 앱의 권한
연락처 앱은 연락처 데이터베이스의 모든 권한을 갖고 있다. 
연락처 앱이 Intent의 데이터 URI를 부모 액티비티에 반환할 때 Intent.FLAT_GRANT_READ_URI_PERMISSION 플래그도 추가해준다.
이 플래그는 부모 액티비티가 데이터를 한 번 읽는 것을 허용한다고 안드로이드에 알려준다.   

### 응답하는 액티비티 확인하기.
안드로이드 운영체제가 인텐트에 부합하는 액티비티를 찾을 수 없다면 앱이 중단될 것이다.
이런 것을 방지하기 위해서 PackageManager를 먼저 확인하자.
```java
PackageManager packageManager = getActivity().getPackageManager();
if (packageManager.resolveActivity(pickContact,
        PackageManager./MATCH_DEFAULT_ONLY/) == null) {
    mSuspectButton.setEnabled(false);
}
```

PackageManager는 안드로이드 장치에 설치된 모든 컴포넌트와 액티비티를 알고 있다.
resolveActivity() 메소드의 첫 번째 인자로 인텐트를 전달하면 해당 인텐트와 일치하는 액티비티를 찾아볼 수 있다. 
두 번째 인자로 플래그를 전달하면 해당 플래그를 갖는 액티비티를 대상으로 찾는다.

#android/책