# DRAW-SERVER

너의 질문을 뽑아줘, 드로우 🎲 / 넥스터즈 23기 프로젝트의 체강서버 레포지터리

## 개발환경 세팅

<pre>
$ ./gradlew ktlintApplyToIdea
$ mkdir .git/hooks
$ ./gradlew addKtlintCheckGitPreCommitHook
$ ./gradlew clean check
</pre>

## System Requirement
JDK >= 17

## System Recommendation
OS: Mac OS
JDK: Amazon Coretto 17.0.4

## API endpoint
- https://draw-nexters.kro.kr/

## 브런치 전략
- 리얼배포: prod
- 메인브런치: master
- 신규 기능 개발시: master -> feature/xxx
