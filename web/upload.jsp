<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>图片预览</title>
</head>
<body>
<h1>图片预览</h1>
<label for="image">选择图片：</label>
<input type="file" id="image" name="image" accept="image/*" required/><br>
<div id="preview-container">
  <img id="preview-image" src="#" alt="预览图片" style="display: none; max-width: 200px; max-height: 200px;">
</div>

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
