# DelegatePattern
> 내부적으로 상태가 여러가지 일 때. (인터페이스를 기반으로 하는 리스너 타입)  
> 적절한 프로토콜을 채택하여 프로토콜에서 제공해주는 기능을 구현하여 해야할 일을 위임하는 방식.  

## PickerView
> pickerView에 데이터를 등록하는게 아니라, not set  
> PickerView가 데이터를 꺼내갈 수 있도록 '프로토콜'이 제공된다. -> DataSource -> UIPickerViewDataSource  
> PickerView의 상태에 관련된 '프로토콜'이 제공된다. -> Delegate -> UIPickerViewDelegate  
    
```swift
*class* ViewController: UIViewController, UIPickerViewDataSource {

    *@IBOutlet* *weak* *var* nameLabel: UILabel!
    *@IBOutlet* *weak* *var* pickerView: UIPickerView! 
       
    
    *let* animals = ["사자", "호랑이", "말", "토끼", "뱀"]
    *let* fruits = ["사과", "배", "바나나"]
    
    *override* *func* viewDidLoad() {
        *super*.viewDidLoad()

        pickerView.dataSource = self/
              
    }
}
```
ViewController에서 UIPickerViewDataSource 프로토콜을 채택하였으니, pickerView의 dataSource를 현재 ViewController로 지정해준다.

위와 같은 초기화 코드를 viewDidLoad()에서 작성할 수도 있지만,
스위프트에서는 각 컴포넌트의 초기화를 캡슐화 할 수 있는 기능이 제공합니다. 
( KVO - Key-Value Observing)
-> 프로퍼티의 값이 변경되었음을 통보받는 방식.
-> kotlin의 by

```swift
*class* ViewController: UIViewController, UIPickerViewDataSource {

    *@IBOutlet* *weak* *var* nameLabel: UILabel!
    *@IBOutlet* *weak* *var* pickerView: UIPickerView! {
		didSet {
				pickerView.dataSource = self
			}
		}
       
    
    *let* animals = ["사자", "호랑이", "말", "토끼", "뱀"]
    *let* fruits = ["사과", "배", "바나나"]
    
    *override* *func* viewDidLoad() {
        *super*.viewDidLoad()
              
    }
}
```

위와 같이 바로 상속하지 않고, 클래스를 확장하는 기능을 Swift에서도 제공합니다. 
(POP  - Protocol Oriented Programming)

```swift
*class* ViewController: UIViewController{

    *@IBOutlet* *weak* *var* nameLabel: UILabel!
    *@IBOutlet* *weak* *var* pickerView: UIPickerView! {
		didSet {
				pickerView.dataSource = self
			}
		}
       
    
    *let* animals = ["사자", "호랑이", "말", "토끼", "뱀"]
    
    *override* *func* viewDidLoad() {
        *super*.viewDidLoad()
              
    }
}

*extension* ViewController: UIPickerViewDataSource {
    *func* numberOfComponents(in pickerView: UIPickerView) -> Int {
        *return* 1
    }
    
    *func* pickerView(*_* pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return animals.count
    }
}
```

PickerView의 DataSource를 지정해주었으면, Delegate를 해주어야 합니다.

```swift
*extension* ViewController: UIPickerViewDelegate {
    *func* pickerView(*_* pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? 	{
		// component : column의 개수
		// row : 각 column별 줄의 개수
  }
}
```

## TableView
### xib 에서의 TableView
```swift
*class* XIBController: UIViewController {

    *@IBOutlet* *weak* *var* tableView: UITableView! {
        *didSet* {
            tableView.dataSource = *self*
          }
    }
    
    *override* *func* viewDidLoad() {
        *super*.viewDidLoad()
    }
}



*extension* XIBController: UITableViewDataSource {
*func* tableView(*_* tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        *let* cell = UITableViewCell(style: .default, reuseIdentifier: *nil*)
        /// 지정초기화 생성자/
        cell.textLabel?.text = "Hello"
        *return* cell
    }
}

```
위와 같이 작성하면 View를 재생성하지 않기 때문에 데이터가 많을수록 메모리가 부족해집니다.

```swift
*class* XIBController: UIViewController {

    *@IBOutlet* *weak* *var* tableView: UITableView! {
        *didSet* {
            tableView.dataSource = *self*
          }
    }
    
    *override* *func* viewDidLoad() {
        *super*.viewDidLoad()
    }
}



*extension* XIBController: UITableViewDataSource {
*func* tableView(*_* tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        *let* reuseIdentifier = "MyCell"
        *var* cell: UITableViewCell! = tableView.dequeueReusableCell(withIdentifier: reuseIdentifier)
        *if* cell == *nil* {
            print("Cell 생성")
            /// 어떤 cell을 생성하는지 모른다./
            cell = UITableViewCell(style: .subtitle, reuseIdentifier: reuseIdentifier)
        } *else* {
            print("Cell 재사용")
        }
        
        cell.textLabel?.text = "Hello"
        cell.detailTextLabel?.text = "world"
        *return* cell
        
///        cell!.textLabel?.text = "Hello"/
///        cell!.detailTextLabel?.text = "world"/
///        return cell!/
///          위와 같이 쓸 수도 있다./
// 		kotlin의 !!/
    }
}

```
dequeueResusableCell -> 재사용할 수 있는 cell을 queue에 모아두었다가 내어준다.


register을 사용하여 아래의 storyboard와 비슷한 방법으로 사용할 수 있습니다.
```swift
*class* XIBController: UIViewController {

    *@IBOutlet* *weak* *var* tableView: UITableView! {
        *didSet* {
            tableView.dataSource = *self*
            tableView.register(UITableViewCell.*self*, forCellReuseIdentifier: "MyCell")
        }
    }
    
    *override* *func* viewDidLoad() {
        *super*.viewDidLoad()
    }
}

/// Android - View Holder Pattern/
/// 화면에 보이는 개수 만큼 뷰를 생성한다./

*extension* XIBController: UITableViewDataSource {
    *func* tableView(*_* tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        *return* 30
    }

    *func* tableView(*_* tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        *let* reuseIdentifier = "MyCell"
        *let* cell = tableView.dequeueReusableCell(withIdentifier: reuseIdentifier, for: indexPath)
        cell.textLabel?.text = "Hello"
        cell.detailTextLabel?.text = "World"
        *return* cell
    }
}
```

### storyboard 에서의 TableView
기본 cell을 제공해주는 xib와 달리 storyboard에서는 개발자가 직접 prototypecell을 지정해줍니다. 어떤 cell을 생성해야 하는지 알고 있기 때문에 dequeueResuableCell의 반환타입이 nullable이 아닙니다.

![](DelegatePattern/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202019-01-07%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.40.50.png)

storyboard에서 identifier을 등록해줍니다.

```swift
*class* StoryboardController: UIViewController {

    *@IBOutlet* *weak* *var* tableView: UITableView! {
        *didSet* {
            tableView.dataSource = *self*
        }
    }
    
    *override* *func* viewDidLoad() {
        *super*.viewDidLoad()
            
    }
}

*extension* StoryboardController: UITableViewDataSource {
    
    *func* tableView(*_* tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        *return* 30
    }
    
    *func* tableView(*_* tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        /// 어떤 cell을 생성해야 하는지 알고있다./
        *let* cell = tableView.dequeueReusableCell(withIdentifier: "MyCell", for: indexPath)
        /// nullable이 아니다./
        
        cell.textLabel?.text = "Hello"
        cell.detailTextLabel?.text = "World"
        *return* cell
    }
}

```


#ios