<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .login-form {
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

        .form-group input[type="checkbox"] {
            display: inline-block;
            margin-right: 5px;
            vertical-align: middle;
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
        .form-group .register-btn {
            background-color: #28a745;
            color: #fff;
            float: right;
        }

        .form-group .register-btn:hover {
            background-color: #218838;
        }
        .center-container {
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="login-form">
        <form action="<%=request.getContextPath()%>/user/login" method="post">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" name="username" id="username" required/>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" name="password" id="password" required/>
            </div>
            <div class="form-group">
                <label for="rememberMe">十天内免登录:</label><input type="checkbox" name="f" value="1" id="rememberMe">
            </div>
            <div class="form-group">
                <input type="submit" value="Login">
            </div>
        </form>
        <div class="form-group">
            <a href="<%=request.getContextPath()%>/register.jsp" class="register-btn">注册</a>
        </div>
    </div>
</div>
<div class="center-container">
       <span>
           © 2023-2024 邢立贤 版权所有
           &nbsp;|&nbsp;
           <a href="http://beian.miit.gov.cn/">滇ICP备2023005629号-1</a>
       </span>
</div>
</body>
</html>