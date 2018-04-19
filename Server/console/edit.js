
// post取得
function getPostById() {

  var id = getParam()["id"];

  if (id == "-") {   // 新規作成の場合
    return;
  }

  var param = "command=getPostById&id=" + id;

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
			  .append($("<td>サムネイル</td>"))
			  .append($("<td>リンク先</td>"))
			  .append($("<td>操作</td>"))
			);

    var id = json.post.id;
    var title = json.post.title;
    var source = json.post.source;
    var relates = json.post.relates;
    var sumbnail = json.post.sumbnail;
    var link = json.post.link;
    var sortOrder = json.post.sortOrder;

    document.getElementById("source").value = source;
    document.getElementById("title").value = title;
    document.getElementById("sumbnail").value = sumbnail;
    document.getElementById("link").value = link;

  });
}

function getParam() {

    /* アドレスの「?」以降の引数(パラメータ)を取得 */
    var pram=location.search;
    /* 引数がない時は処理しない */
    if (!pram) return false;
    /* 先頭の?をカット */
    pram=pram.substring(1);
    /* 「&」で引数を分割して配列に */
    var pair=pram.split("&");
    var i=temp="";
    var key=new Array();
    for (i=0; i < pair.length; i++) {
        /* 配列の値を「=」で分割 */
        temp=pair[i].split("=");
        keyName=temp[0];
        keyValue=temp[1];
        /* キーと値の連想配列を生成 */
        key[keyName]=keyValue;
    }
    return key;
}

// 編集完了
function onClickEditEnd() {

  var id = getParam()["id"];
  var title = document.getElementById("title").value;
  var source = document.getElementById("source").value;
  var sumbnail = document.getElementById("sumbnail").value;
  var link = document.getElementById("link").value;

  var params = "";
  if (id == "-") {    // 新規作成
    params = "command=createPost";
  } else {            // 編集
    params = "command=setPost";
  }
  params += ("&id=" + id + "&title=" + title + "&source=" + source + "&sumbnail=" + sumbnail + "&link=" + link);

  httpPost("srv.php", params, function(response){
    var json = JSON.parse(response);

    if	(json.result == "0") {
        alert("完了");
        location.href = 'item.html';
        return;
    } else {
      alert("失敗");
    }
  });
}

//キャンセル
function onClickCancel() {

	location.href="item.html";
}
