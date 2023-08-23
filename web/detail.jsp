<%@ page import="com.oa.learn.bean.Goods" %>
<%@ page import="java.util.Base64" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品详情</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
            margin-top: 30px;
        }

        hr {
            width: 80%;
            margin: 20px auto;
            border: 1px solid #ccc;
        }

        .details {
            max-width: 400px;
            margin: 30px auto;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .details p {
            margin-bottom: 10px;
        }

        .back-button {
            display: block;
            text-align: center;
            margin-top: 20px;
        }
        .details img {
            display: block; /* 将图片显示为块级元素 */
            margin: 0 auto; /* 设置左右边距为auto，实现水平居中 */
        }
    </style>
</head>
<body>
<%
    Goods good = (Goods)request.getAttribute("good");
%>
<h1>商品详情</h1>
<hr>
<div class="details">
    <img src="data:image/jpeg;base64,<%=good.getImage()%>" alt="图片" style="max-width: 250px; max-height: 250px;">
    <p>编号：<%=good.getNo()%></p>
    <p>名称：<%=good.getName()%></p>
    <p>产家：<%=good.getProducer()%></p>
    <p>进价：<%=good.getBuyPrice()%></p>
    <p>售价：<%=good.getRetailPrice()%></p>
    <p>型号：<%=good.getModel()%></p>
    <p>数量：<%=good.getAmount()%></p>
    <p>生产日期：<%=good.getDate()%></p>
</div>
<div class="back-button">
    <input type="button" value="后退" onclick="window.history.back()"/>
</div>
</body>
</html>
