<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UserSelect Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .button {
            width: 200px;
            height: 200px;
            background-color: #007bff;
            color: #fff;
            font-size: 18px;
            font-weight: bold;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 20px;
            border-radius: 10px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="button" onclick="goToGoodsEditPage()">购物</div>
    <div class="button" onclick="goToUserEditPage()">购物车</div>
    <div class="button" onclick="goToChangePassword()">修改密码</div>
    <div class="button" onclick="goToLookHistory()">购买历史</div>
</div>

<script>
    function goToGoodsEditPage() {
        // 跳转到商品编辑页面的代码
        window.location.href = "<%=request.getContextPath()%>/clist.jsp";
    }

    function goToUserEditPage() {
        // 跳转到用户编辑页面的代码
        window.location.href = "<%=request.getContextPath()%>/goods/lookCar"; // 替换为用户编辑页面的URL
    }

    function goToChangePassword() {
        window.location.href = "<%=request.getContextPath()%>/modifypassword.jsp";
    }

    function goToLookHistory() {
        window.location.href = "<%=request.getContextPath()%>/history.jsp";
    }
</script>
</body>
</html>

