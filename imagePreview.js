function previewImage(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('imagePreview');
        output.innerHTML = ''; // 기존 미리보기 내용 제거
 
        // 미리보기 이미지 생성 및 속성 설정
        var newImage = document.createElement('img');
        newImage.src = reader.result;
        newImage.style.maxWidth = '350px'; // 미리보기 이미지 최대 너비
        newImage.style.maxHeight = '350px'; // 미리보기 이미지 최대 높이
 
        // 생성된 이미지를 미리보기 컨테이너에 추가
        output.appendChild(newImage);
    };
 
    // 선택된 파일을 Data URL 형식으로 읽기
    reader.readAsDataURL(event.target.files[0]);
}
