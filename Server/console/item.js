
function load() {

  getPost();
  fetchParameter();
}

// 一覧取得
function getPost() {

  var param = "command=getPost";

  httpPost("srv.php", param, function(response){
	  var json = JSON.parse(response);

	  if	(json.result != "0") {
	      alert("ログインに失敗しました");
	      location.href = 'index.html';
	      return;
	  }

	  $("#listheader").empty();
	  $("#list").empty();

	  $("#listheader").append(
			  $("<tr></tr>")
			  .append($("<th>取得元</th>"))
			  .append($("<th>タイトル</th>"))
			  .append($("<th>サムネイル</th>"))
			  .append($("<th>リンク先</th>"))
			  .append($("<th>操作</th>"))
			);

	  for(var idx=0; idx < json.posts.length; idx++) {

		  var obj = json.posts[idx];
		  $("#list").append(
			  $("<tr></tr>")
	       .append($("<td></td>").text(obj.source))
			   .append($("<td></td>").text(obj.title))
			   .append($("<td></td>").append("<img src='" + obj.sumbnail + "' onerror=\"this.src='img/item_noimg.png'\"/>"))
			   .append($("<td></td>").append("<a href='" + obj.link + "' target='_blank'><img class='item_link' src='img/item_link.png'></a>"))
			   .append($("<td></td>")
         .append("<a href='javascript:onSortOrderUp(" + obj.id + ");'><img class='configbutton' src='img/item_up.png'></a>")
         .append("<a href='javascript:onSortOrderDown(" + obj.id + ");'><img class='configbutton' src='img/item_down.png'></a>")
         .append("<a href='javascript:onEdit(" + obj.id + ");'><img class='configbutton' src='img/item_edit.png'></a>")
         .append("<a href='javascript:onDelete(" + obj.id + ", \"" + obj.title + "\");'><img class='configbutton' src='img/item_delete.png'></a>")
    		)
		  );

	  }
  });
}


//↑ボタン
function onSortOrderUp(id) {

var param = "command=postSortOrderUp" + "&id=" + id;

httpPost("srv.php", param, function(response){
  var json = JSON.parse(response);

  if	(json.result != "0") {
      alert("ソート順変更に失敗しました");
      return;
  }

  getPost();
});
}

// ↓ボタン
function onSortOrderDown(id) {

	var param = "command=postSortOrderDown" + "&id=" + id;

	httpPost("srv.php", param, function(response){
	  var json = JSON.parse(response);

	  if	(json.result != "0") {
	      alert("ソート順変更に失敗しました");
	      return;
	  }

	  getPost();
	});
}

// 編集
function onEdit(id) {

	location.href="edit.html?id="+id;
}

// 削除
function onDelete(id,title) {
	var result = confirm("「" + title + "」を削除してよろしいですか？" );

	if(result){
		deleteRequest(id);
	}
}
// 削除
function deleteRequest(id) {
	var param = "command=postDelete" + "&id=" + id;

	httpPost("srv.php", param, function(response){
	  var json = JSON.parse(response);

	  if	(json.result != "0") {
	      alert("削除に失敗しました");
	      return;
	  }

	  getPost();
	});
}
// 新規追加
function onClickAdd() {

  location.href = "edit.html?id=-";
}

// 一致度パラメータの取得
function fetchParameter() {

  var param = "command=getMatchParameter";

  httpPost("../srv.php", param, function(response){
	  var json = JSON.parse(response);
    if (json.result != "0") {
      return;
    }
    var pointPerItem = json.pointPerItem;
    var pointPerMinorItem = json.pointPerMinorItem;
    var minorThreshold = json.minorThreshold;
    document.getElementById("pointPerItem").value = pointPerItem;
    document.getElementById("pointPerMinorItem").value = pointPerMinorItem;
    document.getElementById("minorThreshold").value = minorThreshold;
  });
}

// 一致度パラメータの更新
function onClickParameter() {

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
