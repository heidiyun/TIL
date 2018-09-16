# Docker Image 지원
# Docker hub
# node가 업데이트 될 때마다 이미지 지원
FROM node:10.10.0-stretch

#mkdir -p 
RUN mkdir -p /usr/src/app

#cd /usr/src/app
WORKDIR /usr/src/app

# package.json 디렉토리로 복사.
COPY package.json /usr/src/app

RUN npm install

# 소스파일 복사.
COPY . /usr/src/app

# 이미지 만들기 작업 
#---------------------------
# 이미지가 인스턴스화 된 후 실행할 명령

EXPOSE 3000
CMD [ "npm", "run", "publish" ]