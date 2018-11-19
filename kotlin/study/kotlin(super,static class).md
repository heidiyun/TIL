# 코틀린 super 생성자 호출하기.
```kotlin
class CrimeBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
}

```

```java
public class CrimeBaseHelper extends SQLiteOpenHelper {
	public CrimeBaseHelper(Context context) {
		super(context, databaseName, null, version);
	}
}

```


# 코틀린 static class 만들기
```java

public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String /NAME/= "crimes";

        public static final class Cols {
            public static final String /UUID/= "uuid";
            public static final String /TITLE/= "title";
            public static final String /DATE/= "date";
            public static final String /SOLVED/= "solved";
            public static final String /SUSPECT/= "suspect";
        }
    }
}

```

```kotlin
package kr.ac.ajou.heidi.criminalintentk.database

class CrimeDbSchema {
    object CrimeTable {
        val NAME = "crimes"

        object Cols {
            val UUID = "uuid"
            val TITLE = "title"
            val DATE = "date"
            val SOLVED = "solved"
            val SUSPECT = "suspect"
        }
    }
}




```