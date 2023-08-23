<%@ page import="com.oa.learn.bean.Goods" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.oa.learn.bean.GoodInCar" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>myCar Page</title>
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

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.7);
        }

        .modal-content {
            display: block;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            max-width: 80%;
            max-height: 80%;
        }

        .close {
            position: absolute;
            top: 10px;
            right: 10px;
            color: #fff;
            cursor: pointer;
        }

    </style>
</head>
<body>
<script type="text/javascript">
    window.onload = function() {


        const openModalButton = document.getElementById("btn");
        const modal = document.getElementById("myModal");
        const modalImage = document.getElementById("modalImage");

        // 当点击按钮时显示弹出窗口和图片
        openModalButton.addEventListener("click", function() {
            // 设置图片的URL（替换为您的图片URL）
            const imageURL = "<%=request.getContextPath()%>/images/money.png";

            // 显示弹出窗口
            modal.style.display = "block";

            // 设置图片
            modalImage.src = imageURL;
        });

        // 当点击关闭按钮时隐藏弹出窗口
        document.getElementById("closeModal").addEventListener("click", function() {
            modal.style.display = "none";
        });

        // 当用户点击弹出窗口外部区域时隐藏弹出窗口
        window.addEventListener("click", function(event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        });

        const quantityInputs = document.querySelectorAll('input[name="numericInput"]');

        // 为每个数量输入框添加事件监听
        quantityInputs.forEach(input => {
            input.addEventListener('change', updateTotalPrice);
        });

        // 计算并显示初始总价
        updateTotalPrice();

        document.getElementById("btn2").onclick = function() {
            var xhr = new XMLHttpRequest();
            var totalAmount = document.getElementById("howMuch").innerText;
            xhr.onreadystatechange = function() {
                if(xhr.readyState == 4){
                    if(this.status === 200){
                        let json = JSON.parse(this.responseText);
                        let totalCost = parseFloat(json.cost)
                        let cost = parseFloat(totalAmount.substring(1))
                        if(totalCost >= 10000 && totalCost - cost < 10000){
                            alert("交易成功！！您已升级为金牌客户！！")
                            window.location.reload()
                        }
                        else if(totalCost >= 3000 && totalCost - cost < 3000){
                            alert("交易成功！！今已升级为银牌客户！！")
                            window.location.reload()
                        }
                        else{
                            alert("交易成功！！")
                            window.location.reload()
                        }

                    }
                    else {
                        alert(this.status)
                    }
                }
            }
            xhr.open("POST", "<%=request.getContextPath()%>/goods/complete", false)
            //设置请求头
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")


            // 将总金额发送到请求体中
            xhr.send("cost=" + encodeURIComponent(totalAmount));
        }
    }

    function updateQuantity(goodId, quantity) {
        const xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // 更新成功，可能需要做一些界面上的反馈处理
                } else {
                    alert(this.status)
                }
            }
        };
        xhr.open("POST", "<%=request.getContextPath()%>/goods/buy", false);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
        xhr.send("id=" + encodeURIComponent(goodId) + "&quantity=" + encodeURIComponent(quantity));
    }
    // 更新总价的函数
    function updateTotalPrice() {
        // 获取所有商品行
        const rows = document.querySelectorAll('table tr:not(:first-child)');

        let totalPrice = 0.0;

        // 遍历每一行，计算小计并累加到总价中
        rows.forEach(row => {
            const priceCell = row.querySelector('td[id^="Price"]');
            const quantityInput = row.querySelector('input[name="numericInput"]');

            const price = parseFloat(priceCell.innerText);
            const quantity = parseInt(quantityInput.value);

            // 如果输入的数量合法，则计算小计
            if (!isNaN(price) && !isNaN(quantity) && quantity >= 1) {
                const subtotal = price * quantity;
                totalPrice += subtotal;

                const goodId = quantityInput.id;
                updateQuantity(goodId, quantity);
            }
        });

        // 显示总价
        const totalSpan = document.getElementById('howMuch');
        totalSpan.innerText = "￥" + totalPrice.toFixed(2); // 保留两位小数
        totalSpan.style.color = "red"
    }
</script>
<div class="container">
    <h1>我的购物车</h1>
    <div class="logout-link">
    </div>
    <hr>
    <span id="howMuch"></span>
    <button class="round-button" id="btn">结账</button>
    <div class="modal" id="myModal">
        <span class="close" id="closeModal">&times;</span>
        <img class="modal-content" id="modalImage">
    </div>
    <button class="round-button" id="btn2">已完成付款</button>
    <table>
        <tr>
            <th>商品编号</th>
            <th>图片</th>
            <th>商品名称</th>
            <th>单价</th>
            <th>数量</th>
            <th>操作</th>
        </tr>
        <%
            List<GoodInCar> goodList = (List<GoodInCar>) request.getAttribute("carList");
            if(goodList.isEmpty()){
        %>
            <tr>
            <td colspan="6" style="text-align: center;">~~框框里没得菌儿~~</td>
            </tr>
        <%
            }else{
            for (GoodInCar good : goodList) {
                byte[] image = good.getImage();
                String Base64Image = Base64.getEncoder().encodeToString(image);
        %>
        <tr>
            <td><%= good.getId() %></td>
            <td><img src="data:image/jpeg;base64,<%= Base64Image %>" alt="图片" style="max-width: 100px; max-height: 100px;"></td>
            <td><%= good.getName() %></td>
            <td id="Price<%=good.getPrice()%>"><%= good.getPrice() %></td>
            <td><input type="number" id="<%=good.getId()%>" name="numericInput" value=<%=good.getNum()%> min="1" max="<%=good.getMaxNum()%>" step="1"></td>
            <td class="action-buttons">
                <a class="round-button" href="<%=request.getContextPath()%>/goods/delCar?no=<%=good.getId()%>">移除购物车</a>
            </td>
        </tr>
        <%
            }
             }
        %>
    </table>
    <hr>
</div>
</body>
</html>
