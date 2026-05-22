<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>사진 게시판</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"></link>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
        .content {
            background-color: rgb(247, 245, 245);
            width: 80%;
            margin: auto;
        }
        .innerOuter {
            border: 1px solid lightgray;
            width: 80%;
            margin: auto;
            padding: 5% 10%;
            background-color: white;
        }
        
        /* 갤러리 카드 스타일 */
        .thumbnail-card {
            cursor: pointer;
            transition: transform 0.2s ease-in-out;
            margin-bottom: 30px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
        }
        .thumbnail-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0,0,0,0.1);
        }
        .img-wrapper {
            width: 100%;
            height: 200px; /* 썸네일 높이 고정 */
            overflow: hidden;
            background-color: #f8f9fa;
        }
        .img-wrapper img {
            width: 100%;
            height: 100%;
            object-fit: cover; /* 이미지 비율 유지하며 꽉 채우기 */
        }
        .card-title {
            font-size: 1.1rem;
            font-weight: bold;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis; /* 제목 길면 ... 처리 */
        }

        #pagingArea { width: fit-content; margin: auto; }
        
        #searchForm {
            width: 80%;
            margin: auto;
        }
        #searchForm>* {
            float: left;
            margin: 5px;
        }
        .select { width: 20%; }
        .text { width: 53%; }
        .searchBtn { width: 20%; }
    </style>
</head>
<body>
    
    <jsp:include page="../include/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>사진 게시판</h2>
            <br>
            
            <c:if test="${ not empty sessionScope.userInfo }">
                <!-- 로그인 상태일 때만 보이는 사진 업로드(글쓰기) 버튼 -->
                <a class="btn btn-secondary" style="float:right;" href="image/form">사진올리기</a>
            </c:if>
            <br><br>

            <!-- 갤러리 리스트 영역 -->
            <div class="row">
                <script>
                    function toDetail(imageNo){
                        // 컨트롤러 매핑 주소에 맞게 수정해서 쓰세요!
                        location.href = `/spring/images/\${imageNo}`;
                    }
                </script>

                <c:choose>
                    <c:when test="${ not empty map.images }">
                        <c:forEach var="image" items="${ map.images }">
                            <div class="col-12 col-sm-6 col-md-4">
                                <div class="card thumbnail-card" onclick="toDetail(${image.imageNo});">

                                    <div class="img-wrapper">
                                        <c:choose>
                                            <c:when test="${ not empty image.src }">
                                                <img src="${image.src}" alt="${image.imageTitle}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="/spring/resources/imageFiles/asdf.jpg" alt="이미지 없음">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="card-body p-3">
                                        <h5 class="card-title text-dark mb-1">${ image.imageTitle }</h5>
                                        <div class="d-flex justify-content-between text-muted" style="font-size: 0.85rem;">
                                            <span>✍ ${ image.imageWriter }</span>
                                            <span>👁 ${ image.count }</span>
                                        </div>
                                        <div class="text-right text-muted mt-1" style="font-size: 0.8rem;">
                                            ${ image.createDate }
                                        </div>
                                    </div>
                                    
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>                
                    <c:otherwise>
                        <div class="col-12 text-center py-5">
                            <h5 class="text-muted">등록된 사진이 존재하지 않습니다.</h5>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div> <!-- .row 끝 -->
            
            <br>

            <!-- 페이징 영역 (기본 검색/키워드 검색 분기 유지) -->
            <div id="pagingArea">
                <ul class="pagination">
                    <c:if test="${map.pi.currentPage ge 2}">
                        <li class="page-item"><a class="page-link" href="/spring/images?page=${ map.pi.currentPage - 1}">이전</a></li>
                    </c:if>
                    
                    <c:forEach var="num" begin="${ map.pi.startPage }" end="${ map.pi.endPage }">
                        <c:choose>
                            <c:when test="${ empty condition }">
                                <li class="page-item"><a class="page-link" href="/spring/images?page=${ num }">${ num }</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link" href="/spring/images/keyword?page=${ num }&condition=${condition}&keyword=${keyword}">${ num }</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${ map.pi.currentPage lt map.pi.maxPage }">
                        <li class="page-item"><a class="page-link" href="/spring/images?page=${ map.pi.currentPage + 1}">다음</a></li>
                    </c:if>
                </ul>
            </div>

            <br clear="both"><br>

            <!-- 검색 폼 영역 -->
            <form id="searchForm" action="/spring/images/keyword" method="get" align="center">
                <div class="select">
                    <select class="custom-select" name="condition">
                        <option value="writer">작성자</option>
                        <option value="title">제목</option>
                        <option value="content">내용</option>
                    </select>
                </div>
                <div class="text">
                    <input type="text" class="form-control" name="keyword" value="${ keyword }">
                </div>
                <button type="submit" class="searchBtn btn btn-secondary">검색</button>
            </form>
            <br><br>
        </div>
        <br><br>
        
        <script>
            $(function(){
                // 검색 후 선택했던 조건 선택 유지 시키는 스크립트
                if("${condition}" !== "") {
                    $('#searchForm option[value=${condition}]').attr('selected', true);
                }
            });
        </script>
    </div>

    <jsp:include page="../include/footer.jsp" />

</body>
</html>