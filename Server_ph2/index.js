
function onKeyDown() {
  if (event.keyCode == "13") {
    onClickLogin();
  }
}

function onClickLogin() {

  var name = document.getElementById("name").value;
  var password = document.getElementById("password").value;
  var param = "command=login" + "&name=" + name + "&password=" + password;

  httpPost("srv.php", param, function(response) {
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {
        location.href = "item.html";
      } else {
        alert("ログインに失敗しました");
      }
    } catch(e) {
      alert("ログインに失敗しました");
    }
  });
}
