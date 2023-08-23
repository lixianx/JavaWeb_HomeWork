<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <title>Register Page</title>
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

        .register-form {
            background-color: #fff;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
        }

        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 3px;
            font-size: 16px;
        }

        .form-group input[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 12px 20px;
            border-radius: 3px;
            cursor: pointer;
            font-size: 16px;
        }

        .form-group input[type="submit"]:hover {
            background-color: #0056b3;
        }

        .form-group input[type="submit"]:focus {
            outline: none;
        }


    </style>
    <script>
        let length = false, usernameValid = false, passwordValid = false, again = false, emailValid = false, codeCorrect = false
        window.onload = function() {
            //禁用发送验证码按钮
            document.getElementById("get-verification-code").disabled = true;
            /**
             * 检查用户名合法性
             */
            document.getElementById("username").onkeyup = function() {
                const username = document.getElementById("username").value;
                console.log(username)

                if (username === "") {
                    document.getElementById("username-error").innerHTML = "";
                    length = false
                } else if (username.length < 5) {
                    document.getElementById("username-error").innerHTML = "<font color='red'>用户名太短！</font>";
                    length = false
                } else {
                    document.getElementById("username-error").innerHTML = "";
                    length = true
                }
            }
            /**
             * 异步请求查询用户名是否被占用
             */
            document.getElementById("username").onblur = function () {
                if(document.getElementById("username").value != "" && length){
                    var xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState = 4) {
                            if (xhr.status == 200) {
                                //console.log(xhr.responseText)
                                var json = JSON.parse(xhr.responseText);
                                if (json.value == true) {
                                    document.getElementById("username-error").innerHTML = "<font color='red'>用户名已存在！！</font>"
                                    usernameValid = false
                                } else {
                                    document.getElementById("username-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">"
                                    usernameValid = true
                                }
                            }
                        }
                    }
                    xhr.open("GET", "<%=request.getContextPath()%>/query?username=" + this.value, false)
                    xhr.send()
                }
            }

            /**
             * 检验密码合法性
             */
            document.getElementById("password").onkeyup = function () {
                const password = document.getElementById("password").value;
                const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
                if(password === ""){
                    document.getElementById("password-error").innerText = "";
                    passwordValid = false
                }
                else {
                    if (passwordPattern.test(password)) {
                        //密码符合要求
                        document.getElementById("password-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">"
                        passwordValid = true
                    } else {
                        // 密码不符合要求
                        document.getElementById("password-error").innerHTML = "密码必须是大小写字母、数字和标点符号的组合，并且长度大于8个字符!";
                        document.getElementById("password-error").style.color = "red";
                        passwordValid = false
                    }
                }

            }
            /**
             * 检验两次输入是否一致
             */
            document.getElementById("confirm-password").onblur = function() {
                const password = document.getElementById("confirm-password").value
                const correct = document.getElementById("password").value
                if(password === ""){
                    document.getElementById("confirm-password-error").innerHTML = ""
                    again = false
                }
                else {
                    if(correct === ""){
                        document.getElementById("confirm-password-error").innerHTML = "请先输入密码！"
                        document.getElementById("confirm-password-error").style.color = 'red'
                        again = false
                    }
                    else {
                        if(password === correct){
                            document.getElementById("confirm-password-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">"
                            again = true
                        }
                        else {
                            document.getElementById("confirm-password-error").innerHTML = "两次输入不一致！"
                            document.getElementById("confirm-password-error").style.color = 'red'
                            again = false
                        }
                    }
                }
            }
            /**
             * 检验邮箱合法性
             */
            document.getElementById("email").onblur = function() {
                const email = document.getElementById("email").value
                const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                if (email === "") {
                    document.getElementById("email-error").innerHTML = "";
                    emailValid = false
                } else if (!emailPattern.test(email)) {
                    document.getElementById("email-error").innerHTML = "<font color='red'>请输入正确的邮箱！</font>";
                    emailValid = false
                } else {
                    document.getElementById("email-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">";
                    emailValid = true
                }
            }

            /**
             * 检验电话号码合法性
             */
            document.getElementById("phone").onblur = function() {
                const p = document.getElementById("phone").value
                const phonePattern = /^0?1[3|4|5|6|7|8|9][0-9]\d{8}$/;
                if(p === ""){
                    document.getElementById("phone-error").innerHTML = ""
                    document.getElementById("get-verification-code").disabled = true;
                }
                else if(!phonePattern.test(p)){
                    document.getElementById("phone-error").innerHTML = "<font color='red'>请输入正确的手机号！</font>";
                    document.getElementById("get-verification-code").disabled = true;
                }
                else {
                    document.getElementById("phone-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">";
                    document.getElementById("get-verification-code").disabled = false;
                }
            }


            document.getElementById("get-verification-code").onclick = function() {
                //发送获取验证码异步请求
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4) {
                        if (xhr.status === 200) {
                            //console.log(xhr.responseText)
                            var json = JSON.parse(xhr.responseText);
                            if (json.status === "OK") {
                                document.getElementById("verification-code-error").innerHTML = "<font color='#1e90ff'>验证码已发送，请注意查收！！</font>"
                            } else {
                                document.getElementById("verification-code-error").innerHTML = "<font color='red'>验证码发送失败！！</font>"
                            }
                        }
                    }
                }
                xhr.open("GET", "<%=request.getContextPath()%>/sendCode?phone=" + document.getElementById("phone").value, false)
                xhr.send()

                let countdown = 60; // 倒计时时间，以秒为单位
                let countdownTimer;
                // 禁用按钮以防止多次点击
                document.getElementById("get-verification-code").disabled = true;

                // 设置初始按钮文本
                document.getElementById("get-verification-code").textContent = countdown + " 秒后重新获取";

                // 启动倒计时计时器
                countdownTimer = setInterval(function() {
                    countdown--;

                    if (countdown <= 0) {
                        // 倒计时结束，允许重新获取验证码
                        clearInterval(countdownTimer);
                        document.getElementById("get-verification-code").disabled = false;
                        document.getElementById("get-verification-code").textContent = "获取验证码";
                    } else {
                        // 更新按钮文本
                        document.getElementById("get-verification-code").textContent = countdown + " 秒后重新获取";
                    }
                }, 1000); // 1000毫秒（1秒）为计时器的间隔
            }

            /**
             * 校验验证码
             */
            document.getElementById("verification-code").onblur = function() {
                let Code = document.getElementById("verification-code").value
                if(Code.length === 6){
                    var xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState === 4) {
                            if (xhr.status === 200) {
                                //console.log(xhr.responseText)
                                var json = JSON.parse(xhr.responseText);
                                if (json.status === "1") {
                                    document.getElementById("verification-code-error").innerHTML = "<font color='red'>验证码已超时，请重新发送！！</font>"
                                    codeCorrect = false
                                } else if(json.status === "2"){
                                    document.getElementById("verification-code-error").innerHTML = "<img src=\"${pageContext.request.contextPath}/images/zq3.png\" width=\"25\" height=\"25\">";
                                    codeCorrect = true;
                                } else if(json.status === "3"){
                                    document.getElementById("verification-code-error").innerHTML = "<font color='red'>验证码错误！！</font>"
                                    codeCorrect = false
                                }
                            }
                        }
                    }
                    xhr.open("GET", "<%=request.getContextPath()%>/Check?phone=" + document.getElementById("phone").value + "&code="+document.getElementById("verification-code").value, false)
                    xhr.send()
                }
            }

        }
function inputValid(){
    if (length && usernameValid && passwordValid && again && emailValid && codeCorrect) {
        return true
    }
    alert("请正确填写所有信息！")
    return false
}
    </script>
</head>
<body>
<div class="container">
    <div class="register-form">
        <form action="<%=request.getContextPath()%>/user/register" method="post" onsubmit="return inputValid()">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" name="username" id="username"/>
                <span class="error-message" id="username-error"></span>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" name="password" id="password"/>
                <span class="error-message" id="password-error"></span>
            </div>
            <div class="form-group">
                <label for="confirm-password">Confirm Password:</label>
                <input type="password" name="confirm-password" id="confirm-password"/>
                <span class="error-message" id="confirm-password-error"></span>
                <!-- 密码切换按钮 -->
            </div>
            <div class="form-group">
                <label for="phone">Phone:</label>
                <input type="text" name="phone" id="phone" required/>
                <span class="error-message" id="phone-error"></span>
            </div>
            <div class="form-group" style="display: flex; align-items: center;">
                <label for="verification-code">Verification Code:</label>
                <input type="text" name="verification-code" id="verification-code"/>
                <button id="get-verification-code" style="height: 100%;">获取验证码</button>
            </div>
            <div id="s">
                <span class="error-message" id="verification-code-error"></span>
            </div>



            <div class="form-group">
                <label for="email">Email:</label>
                <input type="text" name="email" id="email" required/>
                <span class="error-message" id="email-error"></span>
            </div>
            <div class="form-group">
                <input type="submit" value="注册"/>
            </div>
        </form>
    </div>
</div>
</body>
</html>
