
function getPost() {

  var param = "command=getPost";
  httpPost("srv.php", param, function(response) {
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {

      } else {
        alert("アイテムの取得に失敗しました");
      }
    } catch(e) {
      alert("アイテムの取得に失敗しました");
    }
  });
}
