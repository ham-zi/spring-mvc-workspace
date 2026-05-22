<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
        .content {
            background-color:rgb(247, 245, 245); 
            width:80%;
            margin:auto;
        }
        .innerOuter {
            border:1px solid lightgray;
            width:80%;
            margin:auto;
            padding:5% 10%;
            background-color:white;
        }

        #enrollForm>table {width:100%;}
        #enrollForm>table * {margin:5px;}
        
        /* 이미지 영역 스타일 강화 */
        .img-wrapper {
            border: 2px dashed #ccc;
            border-radius: 5px;
            padding: 10px;
            text-align: center;
            background-color: #fafafa;
            min-height: 160px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        .img-wrapper img {
            max-width: 100%;
            max-height: 120px;
            object-fit: contain;
            margin-bottom: 5px;
        }
        .img-label {
            font-size: 12px;
            color: #666;
            font-weight: bold;
        }
    </style>
</head>
<body>
        
    <jsp:include page="../include/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>게시글 작성하기</h2>
            <br>

            <form id="enrollForm" method="post" action="/spring/images" enctype="multipart/form-data">
                <table align="center">
                    <tr>
                        <th style="width: 15%;"><label for="title">제목</label></th>
                        <td><input type="text" id="title" class="form-control form-control-sm" name="imageTitle" required></td>
                    </tr>
                    <tr>
                        <th><label for="writer">작성자</label></th>
                        <td><input type="text" id="writer" class="form-control form-control-sm" value="${ userInfo.userName }" name="imageWriter" readonly></td>
                    </tr>
                    <tr>
                        <th><label for="content">내용</label></th>
                        <td><textarea id="content" class="form-control" rows="5" style="resize:none;" name="imageContent" required></textarea></td>
                    </tr>
                    
                    <!-- 이미지 중요 구역: 첨부파일 가로 배치 (최대 5개) -->
                    <tr>	
                        <th>파일 첨부<br><small class="text-muted">(최대 5개)</small></th>
                        <td>
                            <div class="row">
                                <div class="col"><input type="file" name="mainUpfile" class="form-control-file border form-control-sm" onchange="changeImage(this, 1);"></div>
                                <div class="col"><input type="file" name="upfile" class="form-control-file border form-control-sm" onchange="changeImage(this, 2);"></div>
                                <div class="col"><input type="file" name="upfile" class="form-control-file border form-control-sm" onchange="changeImage(this, 3);"></div>
                                <div class="col"><input type="file" name="upfile" class="form-control-file border form-control-sm" onchange="changeImage(this, 4);"></div>
                                <div class="col"><input type="file" name="upfile" class="form-control-file border form-control-sm" onchange="changeImage(this, 5);"></div>
                            </div>
                        </td>
                    </tr>
                    
                    <!-- 이미지 미리보기 구역 -->
                    <tr>
                        <th colspan="2">
                            <div class="row mt-3">
                                <div class="col">
                                    <div class="img-wrapper">
                                        <img id="preview1" src="https://kh-academy.co.kr/resources/images/main/logo.svg" alt="미리보기1">
                                        <div class="img-label">대표 이미지</div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="img-wrapper">
                                        <img id="preview2" src="https://kh-academy.co.kr/resources/images/main/logo.svg" alt="미리보기2">
                                        <div class="img-label">이미지 2</div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="img-wrapper">
                                        <img id="preview3" src="https://kh-academy.co.kr/resources/images/main/logo.svg" alt="미리보기3">
                                        <div class="img-label">이미지 3</div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="img-wrapper">
                                        <img id="preview4" src="https://kh-academy.co.kr/resources/images/main/logo.svg" alt="미리보기4">
                                        <div class="img-label">이미지 4</div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="img-wrapper">
                                        <img id="preview5" src="https://kh-academy.co.kr/resources/images/main/logo.svg" alt="미리보기5">
                                        <div class="img-label">이미지 5</div>
                                    </div>
                                </div>
                            </div>
                        </th>
                    </tr>
                </table>
                <br>

                <div align="center">
                    <button type="submit" class="btn btn-primary">등록하기</button>
                    <button type="reset" class="btn btn-danger" onclick="resetPreviews();">취소하기</button>
                </div>
            </form>
        </div>
        <br><br>

    </div>

    <script>
        // 파일 선택 시 각각의 미리보기 칸에 이미지 출력하는 함수
        function changeImage(file, num){
            const imgEl = document.querySelector('#preview' + num);
            
            if(file.files.length){ 
                const reader = new FileReader();
                reader.readAsDataURL(file.files[0]);
                
                reader.onload = function(e){
                    imgEl.src = e.target.result;
                }
            } else {
                // 파일 선택 취소 시 기본 로고로 변경
                imgEl.src = 'https://kh-academy.co.kr/resources/images/main/logo.svg';
            }
        }

        // 취소하기(reset) 버튼 누르면 미리보기들도 전부 기본 로고로 초기화
        function resetPreviews() {
            for(let i = 1; i <= 5; i++) {
                document.querySelector('#preview' + i).src = 'https://kh-academy.co.kr/resources/images/main/logo.svg';
            }
        }
    </script>
    
    <jsp:include page="../include/footer.jsp" />
    
</body>
</html>