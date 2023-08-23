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

  </style>
</head>
<body>
<script>

  window.onload = function() {
    const keywords = document.getElementById("keywords").value
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
      if(this.readyState == 4){
        if(this.status == 200){
          const json = JSON.parse(this.responseText);
          var html = ""
          for (let i = 0; i < json.length; i ++){
            var user = json[i]
            html += "<tr>"
            html += "<td>"+user.id+"</td>"
            html += "<td>"+user.username+"</td>"
            html += "<td>"+user.level+"</td>"
            html += "<td>"+formatTimestamp(user.startDate)+"</td>"
            html += "<td>"+user.totalConsume+"</td>"
            html += "<td>"+user.phone+"</td>"
            html += "<td>"+user.email+"</td>"
            html += "<td class=\"action-buttons\">"
            html += "<a href=\"javascript:void(0)\" onclick=\"reset("+user.id+")\">重置密码</a>"
            html += "<a href=\"javascript:void(0)\" onclick=\"del("+user.id+")\">删除</a></td>"
            html += "</tr>"
          }
          document.getElementById("users-body").innerHTML = html
        }
        else {
          alert(this.status)
        }
      }
    }
    xhr.open("GET", "<%=request.getContextPath()%>/users/edit?keywords=" + keywords, true)
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
              var user = json[i]
              html += "<tr>"
              html += "<td>"+user.id+"</td>"
              html += "<td>"+user.username+"</td>"
              html += "<td>"+user.level+"</td>"
              html += "<td>"+formatTimestamp(user.startDate)+"</td>"
              html += "<td>"+user.totalConsume+"</td>"
              html += "<td>"+user.phone+"</td>"
              html += "<td>"+user.email+"</td>"
              html += "<td class=\"action-buttons\">"
              html += "<a href=\"javascript:void(0)\" onclick=\"reset("+user.id+")\">重置密码</a>"
              html += "<a href=\"javascript:void(0)\" onclick=\"del("+user.id+")\">删除</a></td>"
              html += "</tr>"
            }
            document.getElementById("users-body").innerHTML = html
          }
          else {
            alert(this.status)
          }
        }
      }
      xhr.open("GET", "<%=request.getContextPath()%>/users/edit?keywords=" + keywords, true)
      xhr.send()
    }
  }
  function del(id) {
    if (window.confirm("确认删除?")) {
      document.location.href = "<%= request.getContextPath() %>/user/delete?id=" + id;
    }
  }

  function reset(id){
    if (window.confirm("确认重置密码?")) {
      resetRequest(id)
    }
  }

  function formatTimestamp(timestamp) {
    return new Date(timestamp);
  }

  function resetRequest(id){
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
      if(this.readyState === 4){
        if(this.status === 200){
          let json = JSON.parse(this.responseText)
          if(json.status === 1){
            alert("操作成功！！密码重置为用户名+手机号")
            location.reload()
          }
          else {
            alert("操作失败！！")
          }
        }
        else {
          alert("操作失败！！")
        }
      }
    }
    xhr.open("GET", "<%=request.getContextPath()%>/user/reset?id="+id, true)
    xhr.send()
  }
</script>
当前在线人数:${onlineCount}
<div class="container">
  <h1>用户信息</h1>
  <div class="logout-link">
    <a href="user/logout">退出系统</a>
  </div>
  <hr>
  <div class="search-container">
    <input type="text" id="keywords" placeholder="Search..." />
  </div>
  <table>
    <tr>
      <th>用户ID</th>
      <th>用户名</th>
      <th>用户级别</th>
      <th>注册时间</th>
      <th>累计消费</th>
      <th>手机号</th>
      <th>邮箱</th>
      <th>操作</th>
    </tr>
    <tbody id="users-body">

    </tbody>
  </table>
  <hr>
</div>

</body>
</html>
