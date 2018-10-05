# CloudWatch Logs
## CloudWatchLogs :  빠른시작
1. CloudWatch logs에 IAM 역할 구성
* https://console.aws.amazon.com/iam/ 접속.
* 역할 만들기
현재 인스턴스에 연결되어 있는 역할의 인라인 정책에 아래 json을 복사합니다.
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "logs:DescribeLogStreams"
    ],
      "Resource": [
        "arn:aws:logs:*:*:*"
    ]
  }
 ]
}
``` 

2. 기존 Amazon EC2 인스턴스에 CloudWatch 로그 설치 및 구성
```
$ sudo yum update -y

$ sudo yum install -y awslogs

$ sudo vi /etc/awslogs/awscli.conf

[plugins]
cwlogs = cwlogs
[default]
region = us-east-1

default region이 us-east-1이다.
region을 수정하면 된다.

$ sudo reboot

$ sudo systemctl start awslogsd
// awslogs 서비스 시작

$ sudo systemctl enable awslogsd.service
// 시스템 부팅시 서비스를 시작한다.
```

여기까지 했으면 instance kernel시스템의 log가 표시된다.

3. Docker awslogs
https://docs.docker.com/config/containers/logging/awslogs/
에 접속.

서버를 실행하는 명령어에 --log-driver=awslogs --log-opt awslogs-region=ap-northeast-2 --log-opt awslogs-group=_var_log/messages 추가한다.

region과 awslogs-group은 사용할 때 필요한 곳으로 바꿔준다.
```
$ docker run --log-driver=awslogs --log-opt awslogs-region=ap-northeast-2 --log-opt awslogs-group=/var/log/messages --name hi-server -p 3000:3000 heidiyun/hello-server:0.0.5

$ docker service create --log-driver=awslogs --log-opt awslogs-region=ap-northeast-2 --log-opt awslogs-group=/var/log/messages --name hi-server -p 3000:3000 heidiyun/hello-server:0.0.5
```

#node