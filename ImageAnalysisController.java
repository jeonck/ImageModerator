package com.exstudio.springbootex;
 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
 
@RestController
public class ImageAnalysisController {
 
    @Value("${azure.contentmoderator.subscription_key}")
    private String subscriptionKey;
 
    @Value("${azure.contentmoderator.endpoint}")
    private String endpoint;
 
    @PostMapping("/analyze-image")
    public ResponseEntity<?> analyzeImage(@RequestParam("image") MultipartFile imageFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set("Ocp-Apim-Subscription-Key", subscriptionKey);
 
        try {
            RestTemplate restTemplate = new RestTemplate();
 
            String moderatorUrl = endpoint + "contentmoderator/moderate/v1.0/ProcessImage/Evaluate";
 
            HttpEntity<byte[]> entity = new HttpEntity<>(imageFile.getBytes(), headers);
 
            HashMap response = restTemplate.postForObject(moderatorUrl, entity, HashMap.class);
 
            // API 응답에서 성인물 및 선정적 콘텐츠 점수 추출
            if (response != null) {
                // 변경된 응답 구조에 따라 적절한 키를 사용해야 합니다.
                Double adultScore = (Double) response.get("AdultClassificationScore");
                Double racyScore = (Double) response.get("RacyClassificationScore");
 
                HashMap<String, Object> result = new HashMap<>();
                result.put("AdultClassificationScore", adultScore);
                result.put("RacyClassificationScore", racyScore);
 
                // JSON 응답 반환
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
 
        } catch (Exception e) {
            HashMap<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
