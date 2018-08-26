# null
	``` 
		class User {
			val a: Int = 1

			fun abc(user: User?): Int? {
				return user?.a
			}
		}

		fun main(args: Array<String>) {
			val user = User()
			println(user.abc(user))
		}

		=> 
			fun abc(user: User?): Int {
				return user.a
			} // 불가능하다. user가 null일 가능성이 있는데 반환값에는 null이 들어갈 수 없기 때문에
		=> 
			fun abc(user: User): Int {
				return user.a
			}	// 모두 null이 아님을 확신할 수 있게 해야 한다.

		```
	
