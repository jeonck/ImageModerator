# ImageModerator

## 동작 흐름 
1. 사용자 인터페이스 (HTML):
사용자는 tool-adult.html 페이지에서 이미지 파일을 선택합니다. 이 페이지에는 이미지 파일을 선택할 수 있는 <input type="file"> 요소와 이미지를 서버로 전송하는 '업로드' 버튼이 포함되어 있습니다.
2. 클라이언트 측 스크립트 (JavaScript - analyzeImage.js):
사용자가 '업로드' 버튼을 클릭하면, analyzeImage.js에 정의된 analyzeImage 함수가 호출됩니다. 이 함수는 선택된 이미지 파일을 FormData 객체에 추가하고, fetch API를 사용하여 이 데이터를 서버의 /analyze-image 엔드포인트로 POST 요청으로 비동기적으로 전송합니다.
서버로부터의 응답을 받으면, 이 응답을 JSON으로 파싱하고, 파싱된 데이터에서 AdultClassificationScore와 RacyClassificationScore 값을 추출하여 해당 값을 HTML 문서의 특정 요소에 표시합니다.
3. 서버 측 처리 (Spring Boot Controller - ImageAnalysisController):
ImageAnalysisController는 /analyze-image 엔드포인트를 통해 클라이언트로부터 이미지 파일을 받습니다.
받은 이미지 파일은 RestTemplate을 사용하여 Azure Content Moderator API(또는 비슷한 서비스)로 전송되어 분석됩니다.
분석 결과로부터 성인물 및 선정적 콘텐츠에 대한 점수를 얻습니다. 이 점수들은 HashMap 객체에 담겨 클라이언트에 JSON 형식으로 응답으로 반환됩니다.
4. 최종 결과 표시 (HTML):
클라이언트 측 JavaScript는 서버로부터 받은 응답을 처리하여, 분석 결과(성인물 및 선정적 콘텐츠 점수)를 tool-adult.html 페이지의 지정된 위치에 동적으로 업데이트합니다. 이 과정에서 페이지 전체를 새로고침하지 않고도 결과를 사용자에게 즉시 보여줄 수 있습니다.
이 전체 과정을 통해 사용자는 웹 페이지에서 이미지를 업로드하고, 업로드된 이미지의 분석 결과를 실시간으로 확인할 수 있습니다. 서버 측에서는 이미지 분석 API를 통해 이미지를 분석하고, 클라이언트 측 JavaScript는 이 분석 결과를 사용하여 사용자 인터페이스를 동적으로 업데이트합니다.

## Controller
이제 Spring Boot에서의 **`ImageAnalysisController`**는 이미지 파일을 받아 Azure Content Moderator API로 전송하고, 분석 결과를 JSON 형식으로 클라이언트에 반환하는 방식으로 잘 설정되어 있습니다. 코드를 통해 확인할 수 있는 몇 가지 주요 요소를 요약해 보겠습니다:

### **핵심 구성 요소**

1. **@RestController 어노테이션**: 이 클래스가 RESTful 서비스를 제공하며, 모든 메소드의 반환값이 응답 본문으로 사용될 것임을 나타냅니다.
2. **@PostMapping 어노테이션**: 특정 HTTP POST 요청을 처리하기 위한 메소드를 정의합니다. 여기서는 **`/analyze-image`** 엔드포인트를 통해 이미지 파일을 받습니다.
3. **MultipartFile**: Spring의 일부로 제공되는 파일 업로드를 위한 API입니다. 이를 통해 업로드된 이미지 파일을 서버에서 처리할 수 있습니다.
4. **HttpHeaders & MediaType**: 요청과 응답의 HTTP 헤더를 설정합니다. 여기서는 요청 헤더에 **`Content-Type`**과 API 키를 설정하여 외부 API에 전송합니다.
5. **RestTemplate**: Spring에서 제공하는 간편한 HTTP 클라이언트로, 외부 API와의 통신을 처리합니다.
6. **Exception Handling**: 예외 처리를 통해 API 호출 중 발생할 수 있는 오류를 관리하고, 오류 메시지와 함께 적절한 HTTP 상태 코드를 반환합니다.

### **JSON 응답 반환**

분석 결과를 받은 후, 결과 데이터를 **`HashMap`**에 저장하고 **`ResponseEntity`** 객체를 통해 클라이언트에 JSON 형식으로 반환합니다. 이 과정에서 **`MediaType.APPLICATION_JSON`**을 명시하여 클라이언트가 응답 본문의 형식을 정확히 인지할 수 있도록 합니다.

### **예외 처리**

서버 측에서 예외가 발생할 경우, 오류 메시지를 포함한 **`HashMap`**을 생성하고, **`ResponseEntity`**를 통해 이를 반환합니다. 이를 통해 클라이언트는 서버의 오류 상황을 적절히 처리할 수 있는 정보를 얻게 됩니다.

이 컨트롤러는 이미지 분석의 전체 프로세스를 아우르며, 클라이언트와의 통신, 외부 API와의 상호작용, 오류 관리를 효과적으로 처리합니다. 이를 통해 안정적이고 유지보수가 용이한 서버 어플리케이션을 구축할 수 있습니다.




