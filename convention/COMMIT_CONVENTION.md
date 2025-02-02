# 커밋 메세지 규칙

## <a name="commit"></a> Commit Message Format

*이 사양은 [Angular 커밋 메시지 형식]에서 영감을 얻었으며 이를 대체합니다.*. 

우리는 Git 커밋 메시지의 형식에 대해 매우 정확한 규칙을 가지고 있습니다.  
이 형식은 **커밋 기록을 더 쉽게 읽을 수 있게 해줍니다**.

각 커밋 메시지는 **header**, **body**, **footer**로 구성됩니다.  

```
<header>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

`header`는 필수이며 [커밋 메시지 헤더](#commit-header) 형식을 따라야 합니다.

`body`은 "docs" 타입의 커밋을 제외한 모든 커밋에 필수입니다.
본문이 있는 경우 20자 이상이어야 하며 [커밋 메시지 본문](#commit-body) 형식을 따라야 합니다.

`footer`은 선택 사항입니다. [커밋 메시지 푸터](#commit-footer) 형식은 푸터의 사용 목적과 구조를 설명합니다.

#### <a name="commit-header"></a>Commit Message Header

```
<type>(<scope>): <short summary>
  │       │             │
  │       │             └─⫸ summary는 현재 시제로 작성합니다. 첫 글자를 대문자로 쓰지 않습니다. 끝에 마침표를 찍지 않습니다.
  │       │
  │       └─⫸ Commit Scope: animations|bazel|benchpress|common|compiler|compiler-cli|core|
  │                          elements|forms|http|language-service|localize|platform-browser|
  │                          platform-browser-dynamic|platform-server|router|service-worker|
  │                          upgrade|zone.js|packaging|changelog|docs-infra|migrations|
  │                          devtools
  │
  └─⫸ Commit Type: build|ci|docs|feat|fix|perf|refactor|test
```

`type`과 `summary` 필드는 필수이고, `(<scope>)` 필드는 선택 사항입니다.

##### Type

다음 중 하나여야 합니다:

* **build**: 빌드 시스템 또는 외부 종속성에 영향을 미치는 변경 사항(예시 범위: gulp, 브로콜리, npm)
* **ci**: CI 구성 파일 및 스크립트에 대한 변경 사항(예: CircleCi, SauceLabs) 
* **docs**: 문서만 변경 
* **feat**: 새로운 기능
* **fix**: 버그 수정
* **perf**: 성능 향상을 위한 코드 변경
* **refactor**: 버그 수정이나 기능 추가가 아닌 코드 변경
* **test**: 누락된 테스트 추가 또는 기존 테스트 수정



##### Scope
범위는 커밋 메시지에서 생성된 변경 로그를 읽는 사람이 인식하는 영향을 받는 npm 패키지의 이름이어야 합니다(커밋 메시지에서 생성된 변경 로그를 읽는 사람이 인식하는 대로).

* `animations`
* `bazel`
* `benchpress`
* `common`
* `compiler`
* `compiler-cli`
* `core`
* `elements`
* `forms`
* `http`
* `language-service`
* `localize`
* `platform-browser`
* `platform-browser-dynamic`
* `platform-server`
* `router`
* `service-worker`
* `upgrade`
* `zone.js`

현재 "패키지 이름 사용" 규칙에는 몇 가지 예외가 있습니다:

* `packaging`: 모든 패키지의 npm 패키지 레이아웃을 변경하는 변경 사항(예: 공개 경로 변경, 모든 패키지에 수행된 package.json 변경, d.ts 파일/포맷 변경, 번들 변경 등)에 사용됩니다.

* `changelog`: CHANGELOG.md에서 릴리스 노트를 업데이트하는 데 사용됩니다.

* `dev-infra`: /scripts 및 /tools 디렉터리 내 개발 인프라 관련 변경에 사용됩니다.

* `docs-infra`: 리포지토리의 /adev 디렉토리 내에서 docs-app(angular.dev) 관련 변경에 사용됩니다.

* `migrations`: `ng update` 마이그레이션에 대한 변경에 사용

* `devtools`: 브라우저 확장 프로그램의 변경에 사용 [browser extension](./devtools/README.md).

* `none/empty string`: 모든 패키지에 걸쳐 수행되는 `test` 및 `refactor` 변경에 유용 (예: `test: add missing unit tests`) 및 특정 패키지와 관련이 없는 문서 변경에 유용 (예: `docs: fix typo in tutorial`)


##### Summary

요약 필드를 사용하여 변경 사항을 간결하게 설명하십시오:

* 명령형 현재 시제 사용: "change" 사용 "changed" 나 "changes" 가 아니라
* 첫 글자를 대문자로 하지 않음
* 끝에 점(.)을 찍지 않음

#### <a name="commit-body"></a>Commit Message Body

요약에서와 마찬가지로 명령형 현재 시제를 사용하십시오: "fix" not "fixed" nor "fixes".

커밋 메시지 본문에서 변경의 동기를 설명하십시오. 이 커밋 메시지는 변경을 하는 이유를 설명해야 합니다.   
이전 동작과 새로운 동작의 비교를 포함하여 변경의 영향을 설명할 수 있습니다.

#### <a name="commit-footer"></a>Commit Message Footer

푸터는 변경 사항과 폐기에 대한 정보를 포함할 수 있으며, 이 커밋이 종료하거나 관련된 GitHub 문제, Jira 티켓, 다른 PR을 참조하는 장소이기도 합니다. 예를 들어:

```
BREAKING CHANGE: <주요 변경 사항 요약>
<BLANK LINE>
<주요 변경 사항 설명 + 마이그레이션 지침>
<BLANK LINE>
<BLANK LINE>
Fixes #<이슈 번호>
```

or

```
DEPRECATED: <폐기된 항목>
<BLANK LINE>
<폐기 설명 + 추천되는 업데이트 경로>
<BLANK LINE>
<BLANK LINE>
Closes #<PR 번호>
```

Breaking Change 섹션은 `BREAKING CHANGE: `문구로 시작하여 변경 사항의 요약을 적고, 빈 줄을 하나 넣은 후, 마이그레이션 지침을 포함한 상세한 설명을 적어야 합니다.  

유사하게, Deprecation 섹션은 `DEPRECATED: ` 문구로 시작하여 폐기된 항목의 간단한 설명을 적고, 빈 줄을 하나 넣은 후, 추천되는 업데이트 경로를 포함한 상세한 설명을 적어야 합니다.  

### Revert commits

되돌리기 커밋
커밋이 이전 커밋을 되돌리는 경우, `revert: `으로 시작한 후 되돌리는 커밋의 헤더를 작성해야 합니다.

커밋 메시지 본문 내용에는 다음을 포함해야 합니다:

- 되돌리는 커밋의 SHA에 대한 정보. 형식은 다음과 같습니다: `This reverts commit <SHA>`,
- 커밋 메시지를 되돌리는 이유에 대한 명확한 설명


- 구글 커밋 컨벤션
- https://github.com/angular/angular/blob/main/CONTRIBUTING.md</br>
[coc]: https://github.com/angular/code-of-conduct/blob/main/CODE_OF_CONDUCT.md</br>
[commit-message-format]: https://docs.google.com/document/d/1QrDFcIiPjSLDn3EL15IJygNPiHORgU1_OOAqWjiDU5Y/edit#</br>
[corporate-cla]: https://cla.developers.google.com/about/google-corporate</br>
[dev-doc]: ./contributing-docs/building-and-testing-angular.md </br>
[github]: https://github.com/angular/angular</br>
[discord]: https://discord.gg/angular</br>
[individual-cla]: https://cla.developers.google.com/about/google-individual</br>
[js-style-guide]: https://google.github.io/styleguide/jsguide.html</br>





# korea version
## 관련 이슈가 있다면 header의 short summary 뒤에 붙인다 
> ex) feat: 추천 기능 임시 분리로 인한 표시 문구 변경 (#635)

### 커밋 메시지를 의미 있게 작성한다

1. 제목과 본문을 구분한다. 커밋 메시지는 제목과 본문으**로 구분하는 것이 좋아요. 제목은 변경 사항의 요약을, 본문은 변경 사항의 세부 내용을 설명합니**다.
2. 제목은 50자 이내로 작성한다. 제목은 50자 이내로 작성하는 것이 좋아요. 제목이 너무 길면, 한눈에 파악하기 어려워집니다.
3. 제목은 명령문으로 작성한다. 제목은 명령문으로 작성하는 것이 좋아요. 명령문으로 작성하면, 변경 사항을 명확하게 표현할 수 있습니다.
4. 본문은 72자 이내로 작성한다. 본문은 72자 이내로 작성하는 것이 좋아요. 본문이 너무 길면, 읽기가 어려워집니다.
5. 커밋 유형을 명시한다. 커밋 메시지에는 커밋 유형을 명시하는 것이 좋아요. 커밋 유형을 명시하면, 변경 사항의 종류를 쉽게 파악할 수 있습니다. ￼ 커밋 유형 커밋 메시지에는 커밋 유형을 명시하는 것이 좋아요. 커밋 유형을 명시하면, 변경 사항의 종류를 쉽게 파악할 수 있습니다. 일반적으로 사용되는 커밋 유형은 다음과 같습니다.

다음은 좋은 커밋 메시지의 예시입니다.

```xml
feat: 새 로그인 페이지 추가 이메일과 비밀번호 인증을 지원하는 새 로그인 페이지를 추가했습니다.

이 커밋 메시지는 다음과 같은 특징을 가지고 있습니다.
1. 명확하고 간결하다. 
2. 변경 사항을 정확하게 요약한다.
3. 커밋 유형을 명시한다.
```

결론 좋은 커밋 메시지를 작성하면, 프로젝트의 변경 사항을 쉽게 이해하고 추적할 수 있어요.  
따라서 협업 효율을 높이기 위해서는 좋은 커밋 메시지를 작성하는 것이 중요합니다.

### 실수 시 참고 페이지
https://velog.io/@s33h/Git-앗-커밋-메시지-실수했다


### 커밋 테이블
| 타입        | 설명                                      |
|------------|---------------------------------------------|
| feat       | 새로운 기능 추가                            |
| refactor   | 코드 구조 개선                              |
| docs       | 문서 수정                                   |
| fix        | 버그 수정                                   |
| hotfix     | 치명적 버그 수정                            |
| comment    | 필요한 주석 추가 및 변경                    |
| depend     | 의존성 추가, 삭제                           |
| test       | 테스트 작업                                 |
| rename     | 파일, 폴더명 수정                           |
| remove     | 사용하지 않는 파일 혹은 폴더를 삭제하는 경우       |
| move       | 파일, 폴더 경로 변경                         |
|-|-|-|
| improve    | 향상이 있는 경우. 호환성, 검증 기능, 접근성 등이 될 수 있음 |
| design     | css 등 사용자 ui 디자인 변경                 |
| style      | 코드 스타일 수정                            |
| chore      | 기타 사소한 작업                            |
| conf       | 설정 파일 작업                              |
| db         | DB 관련 작업                              |
