
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
			  .append($("<td>取得元</td>"))
			  .append($("<td>タイトル</td>"))
			  .append($("<td>種別</td>"))
			  .append($("<td>サムネイル</td>"))
			  .append($("<td>リンク先</td>"))
			  .append($("<td>操作</td>"))
			);

	  for(var idx=0; idx < json.posts.length; idx++) {

		  var obj = json.posts[idx];
		  $("#list").append(
				    $("<tr></tr>")
			        .append($("<td></td>").text(obj.source))
			        .append($("<td></td>").text(obj.title))
			        .append($("<td></td>").text(obj.relates))
			        .append($("<td></td>").append("<img src='" + obj.sumbnail + "'/>"))
			        .append($("<td></td>").append("<a href="+obj.link+" target='_blank'>リンク先</a>"))
			        .append($("<td></td>")
    						.append("<button type='button' class='subbutton'"
    								+ " onclick='javascript:onSortOrderUp(" + obj.id + ");'>↑</button>")
    						.append("<button type='button' class='subbutton'"
    								+ " onclick='javascript:onSortOrderDown(" + obj.id + ");'>↓</button>")
    						.append("<button type='button' class='subbutton'"
    								+ " onclick='javascript:onEdit(" + obj.id + ");'>編集</button>")
    						.append("<button type='button' class='subbutton' "
    								+ " onclick='javascript:onDelete(" + obj.id + ", \" " + obj.title + " \" );'>削除</button>")
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
