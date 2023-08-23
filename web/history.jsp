<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 20px;
        }
        h1 {
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
<script type="text/javascript">
    window.onload = function() {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if(this.readyState === 4){
                if(this.status === 200){
                    let json = JSON.parse(this.responseText)
                    let html = ""
                    for (let i = 0; i < json.length; i ++){
                        let good = json[i]
                        html += "<tr>"
                        html += "<td>"+good.id+"</td>"
                        html += "<td>"+good.name+"</td>"
                        html += "<td>"+good.num+"</td>"
                        html += "<td>"+formatTimestamp(good.date)+"</td>"
                        html += "</tr>"
                    }
                    document.getElementById("goods-body").innerHTML = html
                }
                else {
                    alert(this.status)
                }
            }
        }
        xhr.open("GET", "<%=request.getContextPath()%>/goods/history", true)
        xhr.send()
    }

    function formatTimestamp(timestamp) {
        return new Date(timestamp);
    }
</script>
    <h1>购买历史</h1>
    <hr>
    <table>
        <tr>
            <th>商品编号</th>
            <th>商品名称</th>
            <th>购买数量</th>
            <th>购买日期</th>
        </tr>
        <tbody id="goods-body">
        </tbody>
    </table>
</body>
</html>
