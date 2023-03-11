# DoDamDoDam🐇🐰
구글 플레이스토어에 등록되어 있는 '도담도담' 어플리케이션 코드입니다. 
(https://play.google.com/store/apps/details?id=com.dodamdodam.dodamdodam)

연인간의 추억을 저장하고 생각을 공유하는 어플리케이션입니다.

사용자들의 정보를 파이어베이스에 저장하고 상대방과 나의 정보만 선별하여 보여줍니다.
매일 오는 질문에 대한 답변을 공유할 수도 있고,
사진과 동영상 그리고 간단한 글을 공유하는 공유앨범도 있고,
일정을 공유하고 생일 d-day, 만난 날 d+day를 보여주는 공유캘린더도 있습니다.


<도담도담 시연 영상>
https://www.youtube.com/watch?v=FQ6g7Xi_qQU&t=1s

<도담도담 실행 이미지>

![실행시작](https://user-images.githubusercontent.com/90952132/224460739-015dc2a1-cfc0-4ec9-bb62-b67bb35a192f.jpg)
- 스플래쉬 화면으로 초기 화면으로 자연스럽게 이동합니다.

![질문1](https://user-images.githubusercontent.com/90952132/224460741-67f6ceee-d2c7-4b1a-ad05-392063b2edd1.jpg)
![질문2](https://user-images.githubusercontent.com/90952132/224460742-f82bbfd2-cae2-4206-810a-729cb463f84d.jpg)

- 실시간 데이터베이스를 통해 매일매일 새롭게 나오는 질문들을 배우자와 공유할 수 있습니다.
- 한 사람만 답변을 제출했을 때, 두 사람 모두 답변을 제출했을 때 각각 토끼의 기분 표정변화를 달리 하여 시각적 효과를 주었습니다.
- 하단 제일 왼쪽 리스트 버튼을 클릭하면, 이전까지 했던 질문과 그에 대한 대답들을 리사이클러뷰로 다시 살펴볼 수 있습니다.

![앨범 1](https://user-images.githubusercontent.com/90952132/224460743-cd498ff8-48cd-4e43-86ba-1a0f6e0be638.jpg)
![앨범 2](https://user-images.githubusercontent.com/90952132/224460744-b69c1523-5693-495d-9401-8b0330391b77.jpg)

- 배우자와의 추억을 둘만의 앨범을 통해 공유하고 기록할 수 있습니다.
- PhotoPicker 기능을 통해 총 10장의 사진을 사용자의 갤러리에서 복수선택 가능하게 하고 리사이클러뷰로 선택한 이미지들을 볼 수 있게 구현하였습니다. 
- 사용자가 사진을 잘못 올렸을 경우, 다시 사진을 클릭하면 해당 사진만 삭제할 수 있게 구현하였습니다.
- 파이어스토어 데이터베이스, storage, 실시간 데이터베이스를 동시에 활용하여 사용자가 게시물을 등록, 수정, 삭제 시 실시간으로 업데이트되게끔 구현하였습니다.
- 커스텀 리사이클러뷰로 두 배우자가 올렸던 게시물들을 최신순으로 불러와 나타나게 구현하였습니다.
- 앨범 게시물 클릭 시, 뷰페이저를 통해 여러 이미지들을 슬라이드해서 볼 수 있게 구현하였고, 현재 시간과 작성했던 시간의 차이에 맞게 계산하여 게시물 하단에 며칠 전인지 나타나게끔 구현하였습니다.


![질문1](https://user-images.githubusercontent.com/90952132/224460745-a4440406-9934-4606-98aa-03e664fda675.jpg)
![질문2](https://user-images.githubusercontent.com/90952132/224460748-b80cc6ca-1bae-4b94-8137-4a3dfa7bada1.jpg)

- 명우 파트!

![캘린더 1](https://user-images.githubusercontent.com/90952132/224460751-ecf1c957-9f16-4027-b655-63fb7bd98f9b.jpg)
![설정](https://user-images.githubusercontent.com/90952132/224460753-4f2a0939-ca09-4d1f-96ec-cbaf27bfefe5.jpg)

- 명우 파트!
