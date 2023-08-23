<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        form {
            max-width: 400px;
            margin: 30px auto;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        form input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 3px;
        }

        form input[type="submit"] {
            display: block;
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        form input[type="submit"]:hover {
            background-color: #0056b3;
        }

        form label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        form h1 {
            text-align: center;
        }
    </style>
</head>
<body>
<form action="<%=request.getContextPath()%>/goods/save" method="post"  enctype="multipart/form-data">
    <h1>Add Page</h1>
    <label for="name">商品名称</label>
    <input type="text" name="name" id="name" required/><br>
    <label for="producer">生产厂家</label>
    <input type="text" name="producer" id="producer" required/><br>
    <label for="date">生产日期</label>
    <input type="text" name="date" id="date" required/><br>
    <label for="model">型号</label>
    <input type="text" name="model" id="model" required/><br>
    <label for="buyPrice">进货价</label>
    <input type="text" name="buyPrice" id="buyPrice" required/><br>
    <label for="retailPrice">零售价</label>
    <input type="text" name="retailPrice" id="retailPrice" required/><br>
    <label for="amount">数量</label>
    <input type="text" name="amount" id="amount" required/><br>
    <label for="image">选择图片：</label>
    <input type="file" id="image" name="image" accept="image/*" required/><br>
    <div id="preview-container">
        <img id="preview-image" src="#" alt="预览图片" style="display: none; max-width: 200px; max-height: 200px;">
    </div>
    <input type="submit" value="保存"/>
</form>
<script>
    const imageInput = document.getElementById('image');
    const previewImage = document.getElementById('preview-image');

    imageInput.addEventListener('change', function() {
        const file = imageInput.files[0];

        if (file) {
            const reader = new FileReader();

            reader.onload = function(e) {
                previewImage.src = e.target.result;
                previewImage.style.display = 'block';
            };

            reader.readAsDataURL(file);
        } else {
            previewImage.src = '#';
            previewImage.style.display = 'none';
        }
    });
</script>
</body>
</html>
