
function fetch() {

  if (!authenticate()) {
    return;
  }
  
  var param = "command=getParameter";
  httpPost("srv.php", param, function(response) {
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {
        document.getElementById("pointPerItem").value = json.pointPerItem;
        document.getElementById("pointPerMinorItem").value = json.pointPerMinorItem;
        document.getElementById("minorThreshold").value = json.minorThreshold;
      } else {
        alert("パラメータの取得に失敗しました");
      }
    } catch(e) {
      alert("パラメータの取得に失敗しました");
    }
  });
}

function onClickRefresh() {

  var pointPerItem = document.getElementById("pointPerItem").value;
  var pointPerMinorItem = document.getElementById("pointPerMinorItem").value;
  var minorThreshold = document.getElementById("minorThreshold").value;
  if ((!isNumber(pointPerItem)) || (!isNumber(pointPerMinorItem)) || (!isNumber(minorThreshold))) {
    alert("不正な入力値です");
    return;
  }

  var param = "command=postParameter&pointPerItem=" + pointPerItem + "&pointPerMinorItem=" + pointPerMinorItem + "&minorThreshold=" + minorThreshold;
  httpPost("srv.php", param, function(response) {
    alert("更新しました");
  });
}

function isNumber(str) {
  var pattern = /^([1-9]\d*|0)$/;
  return pattern.test(str)
}
