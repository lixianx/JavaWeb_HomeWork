<%@ page import="com.oa.learn.bean.User" %>
<%@ page import="java.time.LocalTime" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>List Page</title>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
        }

        a {
            color: #007bff;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        table {
            border-collapse: collapse;
            width: 80%;
            margin: 20px auto;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .action-buttons a {
            margin-right: 10px;
        }

        .action-buttons a:last-child {
            margin-right: 0;
        }
        .search-container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 12vh; /* 可视区域的高度，使内容垂直居中 */
        }

        /* 输入框样式 */
        #keywords {
            width: 50%; /* 控制搜索框的宽度 */
            height: 30px;
            padding: 5px 10px; /* 减少上下边距，增加左右边距 */
            font-size: 16px;
            border: 2px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .round-button {
            display: inline-block;
            text-decoration: none;
            padding: 10px 20px;
            border-radius: 20px;
            background-color: dodgerblue;
            color: #333;
            border: 2px solid dodgerblue;
            transition: background-color 0.3s, color 0.3s;
        }

        /* 鼠标悬停时的样式 */
        .round-button:hover {
            background-color: #ccc;
            color: #fff;
            cursor: pointer;
        }
        .greeting-container {
            text-align: center;
            margin-bottom: 30px;
        }
        .greeting-text {
            font-size: 24px;
            color: #007bff;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .user-greeting {
            font-size: 18px;
            color: #333;
        }
        .online-count {
            text-align: center;
            font-size: 16px;
            color: #666;
        }
    </style>
</head>
<body>
<script type="text/javascript">
    function add(id, amount) {
        document.getElementById(id).disabled = true
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if(this.readyState === 4){
                if(this.status === 200){
                    let json = JSON.parse(this.responseText);
                    if(json.status === true){
                        document.getElementById(id).style.color = "#f5f5f5"
                        document.getElementById(id).disabled = true
                        document.getElementById(id).textContent = "已加入"
                    }
                    else {
                        alert("操作失败！")
                    }
                }
                else {
                    alert(this.status)
                }
            }
        }
        xhr.open("GET", "<%=request.getContextPath()%>/goods/car?no=" + id + "&maxNum=" + amount, true);
        xhr.send();
    }
    window.onload = function() {
        const keywords = document.getElementById("keywords").value
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if(this.readyState == 4){
                if(this.status == 200){
                    const json = JSON.parse(this.responseText);
                    console.log(json)
                    var html = ""
                    for (let i = 0; i < json.length; i ++){
                        var good = json[i]
                        html += "<tr>"
                        html += "<td>"+good.no+"</td>"
                        html += "<td><img src=\"data:image/jpeg;base64,"+good.image+"\" style=\"max-width: 100px; max-height: 100px;\"></td>"
                        html += "<td>"+good.name+"</td>"
                        html += "<td>"+good.retailPrice+"</td>"
                        html += "<td>"+good.amount+"</td>"
                        html += "<td>"+good.times+"</td>"
                        html += "<td class=\"action-buttons\"><a href=\"goods/detail?f=detail&no="+good.no+"\" class=\"round-button\">详情</a><bount class=\"round-button\" id="+good.no+" onclick=\"add("+good.no+","+good.amount+")\">加入购物车</bount></td>"
                        html += "</tr>"
                    }
                    document.getElementById("goods-body").innerHTML = html
                }
                else {
                    alert(this.status)
                }
            }
        }
        xhr.open("GET", "<%=request.getContextPath()%>/goods/customer?keywords=" + keywords, true)
        xhr.send()
        document.getElementById("keywords").onkeyup = function() {
            const keywords = document.getElementById("keywords").value
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if(this.readyState == 4){
                    if(this.status == 200){
                        const json = JSON.parse(this.responseText);
                        var html = ""
                        for (let i = 0; i < json.length; i ++){
                            var good = json[i]
                            html += "<tr>"
                            html += "<td>"+good.no+"</td>"
                            html += "<td><img src=\"data:image/jpeg;base64,"+good.image+"\" style=\"max-width: 100px; max-height: 100px;\"></td>"
                            html += "<td>"+good.name+"</td>"
                            html += "<td>"+good.retailPrice+"</td>"
                            html += "<td>"+good.amount+"</td>"
                            html += "<td>"+good.times+"</td>"
                            html += "<td class=\"action-buttons\"><a href=\"goods/detail?f=detail&no="+good.no+"\" class=\"round-button\">详情</a><bount class=\"round-button\" id="+good.no+" onclick=\"add("+good.no+","+good.amount+")\">加入购物车</bount></td>"
                            html += "</tr>"
                        }
                        document.getElementById("goods-body").innerHTML = html
                    }
                    else {
                        alert(this.status)
                    }
                }
            }
            xhr.open("GET", "<%=request.getContextPath()%>/goods/customer?keywords=" + keywords, true)
            xhr.send()
        }
    }
</script>
<%
    HttpSession session1 = request.getSession();
    User user = (User)session1.getAttribute("user");
    LocalTime currentTime = LocalTime.now();
    int hour = currentTime.getHour();
    String greeting;
    if (hour >= 5 && hour < 12) {
        greeting = "早上好！";
    } else if (hour >= 12 && hour < 18) {
        greeting = "下午好！";
    } else {
        greeting = "晚上好！";
    }
%>
<div class="greeting-container">
    <div class="greeting-text">尊贵的<%=user.getLevel()%>用户: <%=user.getUsername()%></div>
    <div class="user-greeting"><%=greeting%></div>
</div>
<div class="online-count">当前在线人数: ${onlineCount}</div>
<div class="container">
    <h1>商品信息</h1>
    <div class="logout-link">
        <a href="user/logout">[退出系统]</a>
    </div>
    <hr>
    <div class="search-container">
        <input type="text" id="keywords" placeholder="Search..." />
    </div>
    <table>
        <tr>
            <th>商品编号</th>
            <th>图片</th>
            <th>商品名称</th>
            <th>零售价</th>
            <th>数量</th>
            <th>销量</th>
            <th>操作</th>
        </tr>
        <tbody id="goods-body">
        </tbody>
    </table>
    <hr>
</div>
</body>
</html>
