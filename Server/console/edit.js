
// post取得
function getPostById() {

  var id = getParam()["id"];

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
			  .append($("<td>種別</td>"))
			  .append($("<td>サムネイル</td>"))
			  .append($("<td>リンク先</td>"))
			  .append($("<td>操作</td>"))
			);

	  for(var idx=0; idx < json.posts.length; idx++) {

		  var obj = json.posts[idx];
		  $("#list").append(
				    $("<tr></tr>")
			        .append($("<td></td>").text(json.post.source))
			        .append($("<td></td>").text(json.post.title))
			        .append($("<td></td>").text(json.post.relates))
			        .append($("<td></td>").append("<img src="+obj.sumbnail+"/>"))
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

	  $('.rowup').click(function() {
	   var $row = $(this).closest("tr");
	   var $row_prev = $row.prev("tr");
	   if($row.prev.length) {
	       $row.insertBefore($row_prev);
	   }
	  });

	  $('.rowdown').click(function() {
		  var $row = $(this).closest("tr");
		  var $row_next = $row.next("tr");
		  if($row_next.length) {
		      $row.insertAfter($row_next);
		  }
		 });

	  $('.rowdel').click(function() {
		  var row = $(this).closest("tr").remove();
		  $(row).remove();
		 });
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


//キャンセル
function onClickCancel() {

	location.href="item.html";
}