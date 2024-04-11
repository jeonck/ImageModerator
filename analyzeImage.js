function analyzeImage() {
    const formData = new FormData();
    const imageFile = document.querySelector('input[type=file]').files[0];
    formData.append('image', imageFile);
 
    fetch('/analyze-image', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            // 서버로부터 받은 응답을 처리합니다.
            console.log(data);
            // 결과 값을 HTML에 표시합니다.
            document.getElementById('adultScore').textContent = data.AdultClassificationScore.toFixed(2); // 점수를 2자리 소수로 표시
            document.getElementById('racyScore').textContent = data.RacyClassificationScore.toFixed(2); // 점수를 2자리 소수로 표시
 
            // 이미지가 부적절한 경우 처리
            if (data.AdultClassificationScore > 0.9 || data.RacyClassificationScore > 0.9) {
                alert('이미지에 부적절한 콘텐츠가 포함될 가능성이 높습니다.');
            } else {
                alert('이미지는 대체로 적절한 것으로 판단됩니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('분석 중 오류가 발생했습니다.');
        });
}
 
// HTML 폼이 제출될 때 analyzeImage 함수가 호출되도록 이벤트 리스너를 설정합니다.
document.querySelector('form').addEventListener('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 방지
    analyzeImage(); // 이미지 분석 함수 호출
});
