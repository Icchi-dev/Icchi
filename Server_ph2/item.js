
function fetch() {

  if (!authenticate()) {
    return;
  }
  
  var param = "command=getItem";
  httpPost("srv.php", param, function(response) {
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {
        display(json.data);
      } else {
        alert("アイテムの取得に失敗しました");
      }
    } catch(e) {
      alert("アイテムの取得に失敗しました");
    }
  });
}

function display(dataList) {

  // 件数
  document.getElementById("total_count").innerHTML = "(全" + dataList.length + "件)";

  // リスト表示
  for (var i = 0; i < dataList.length; i++) {
    var table = document.getElementById("item_table");
    var tr = table.insertRow(-1);

    var tdName = tr.insertCell(-1);
    tdName.innerHTML = dataList[i].name;
    var tdLikeCount = tr.insertCell(-1);
    tdLikeCount.innerHTML = dataList[i].likeCount;
    var tdHateCount = tr.insertCell(-1);
    tdHateCount.innerHTML = dataList[i].hateCount;

    var tdDelete = tr.insertCell(-1);
    var href = "javascript:onClickDelete(\"" + dataList[i].id + "\",\"" + dataList[i].name + "\")";
    tdDelete.innerHTML = "<a href='" + href + "'><img src='img/delete.png' style='width:20px'></a>";
  }
}

function onClickDelete(id, name) {

  if (confirm(name + "を削除しますか？")) {
    var param = "command=deleteItem&id=" + id;
    httpPost("srv.php", param, function(response) {
      try {
        var json = JSON.parse(response);
        if (json.result == "0") {
          alert("アイテムを削除しました");
          location.href = "";
        } else {
          alert("アイテムの削除に失敗しました");
        }
      } catch(e) {
        alert("アイテムの削除に失敗しました");
      }
    });
  }
}
