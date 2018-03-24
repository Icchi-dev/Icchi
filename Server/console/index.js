
function onClickLogin() {

  var name = document.getElementById("name").value;
  var password = document.getElementById("password").value;
  var param = "command=login&name=" + name + "&password=" + password;

  httpPost("srv.php", param, function(response){
    if (response == "0") {
      location.href = "item.html";
    } else {
      alert("ログインに失敗しました");
    }
  });
}
