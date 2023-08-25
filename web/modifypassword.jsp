<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改密码页面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 20px;
            background-color: #f5f5f5;
        }
        h1 {
            text-align: center;
            color: #007bff;
            margin-bottom: 20px;
        }
        .form-container {
            width: 400px;
            max-width: 100%;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }
        .error-message {
            color: red;
        }
        .success-message {
            color: green;
        }
        button {
            display: block;
            margin: 0 auto;
            padding: 10px 20px;
            border: none;
            background-color: #007bff;
            color: #fff;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
    </style>
</head>
<body>
<script type="text/javascript">
    var flag1 = false, flag2 = false, flag3 = false
    window.onload = function() {
        document.getElementById("oldPassword").onblur = function() {
            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if(this.readyState === 4){
                    if(this.status === 200){
                        let json = JSON.parse(this.responseText)
                        if(json.status === true){
                            //条件1
                            flag1 = true
                            document.getElementById("oldPassword-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">";
                            document.getElementById("oldPassword-error").style.color = "blue";
                        }
                        else {
                            flag1 = false
                            document.getElementById("oldPassword-error").innerHTML = "密码错误"
                            document.getElementById("oldPassword-error").style.color = "red";
                        }
                    }
                    else {
                        alert(this.status)
                    }
                }
            }
            xhr.open("GET", "<%=request.getContextPath()%>/verify/password?oldPassword="+this.value, true)
            xhr.send()

            document.getElementById("newPassword").onkeyup = function () {
                const password = document.getElementById("newPassword").value;
                const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
                if(password === ""){
                    document.getElementById("password-error").innerText = "";
                }
                else {
                   if (passwordPattern.test(password)) {
                        //条件2
                        //密码符合要求
                       flag2 = true
                        document.getElementById("password-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">";
                    } else {
                        // 密码不符合要求
                        flag2 = false
                        document.getElementById("password-error").innerHTML = "密码必须是大小写字母、数字和标点符号的组合，并且长度大于8个字符!";
                        document.getElementById("password-error").style.color = "red";
                    }
                }

            }
        }

        document.getElementById("btn").onclick = function() {
            let oldPassword = document.getElementById("oldPassword").value
            let newPassword = document.getElementById("newPassword").value
            if(oldPassword === newPassword){
                flag3 = true
            }
            else {
                flag3 = false
            }
            if(flag1 && flag2){
                if(!flag3){
                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function() {
                        if(this.readyState === 4){
                            if(this.status === 200){
                                let json = JSON.parse(this.responseText)
                                if(json.status === false){
                                    alert("修改失败！！")
                                }
                                else {
                                    alert("修改成功！！账号密码已过期，请重新登录！！")
                                    window.location.href="<%=request.getContextPath()%>/user/logout"
                                }
                            }
                            else {
                                alert(this.status)
                            }
                        }
                    }
                    let newPassword = document.getElementById("newPassword").value
                    xhr.open("GET", "<%=request.getContextPath()%>/admin/change?newPassword=" + newPassword, false)
                    xhr.send()
                }
                else {
                    alert("新旧密码相同！！请输入其他密码！！")
                }
            }
            else {
                alert("请正确填写信息！！")
            }
        }
    }
</script>
<h1>修改密码</h1>
<div class="form-container">
    <label for="oldPassword">请输入旧密码:</label>
    <input type="password" id="oldPassword" name="oldPassword" required>
    <span id="oldPassword-error" class="error-message"></span>
    <label for="newPassword">请输入新密码:</label>
    <input type="password" id="newPassword" name="newPassword" required>
    <span id="password-error" class="error-message"></span>
    <button id="btn">修改</button>
</div>
</body>
</html>
