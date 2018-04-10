
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
			  .append($("<td>NO</td>"))
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
			        .append($("<td></td>").text(obj.id))
			        .append($("<td></td>").text(obj.source))
			        .append($("<td></td>").text(obj.title))
			        .append($("<td></td>").text(obj.relates))
			        .append($("<td></td>").append("<img src="+obj.sumbnail+"/>"))
			        .append($("<td></td>").append("<a href="+obj.link+" target='_blank'>リンク先</a>"))
			        .append($("<td></td>")
    						.append("<button type='button' class='subbutton rowup'>↑</button>")
    						.append("<button type='button' class='subbutton rowdown'>↓</button>")
    						.append("<button type='button' class='subbutton'>編集</button>")
    						.append("<button type='button' class='subbutton rowdel'>削除</button>")
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

function onClickCancel() {

	getPost();
}

