
function getPost() {

  var param = "command=getPost";

  httpPost("srv.php", param, function(response){
	  var json = JSON.parse(response);

	  if	(json.result != "0") {
	      alert("一覧取得に失敗しました");
	      return;
	  }

	  for(var idx=0; idx < json.posts.length; idx++) {

		  var obj = json.posts[idx];
		  $("#list").append(
				    $("<tr></tr>")
			        .append($("<td></td>").text(obj.no))
			        .append($("<td></td>").text(obj.source))
			        .append($("<td></td>").text(obj.title))
			        .append($("<td></td>").text(obj.relates))
			        .append($("<td></td>").text(obj.sumbnail))
			        .append($("<td></td>").text(obj.link))
			        .append($("<td></td>").append("<button type='button'>編集</button><button type='button'>削除</button>"))
			        );
		  $('.bordered tr').mouseover(function(){
			    $(this).addClass('highlight');
			}).mouseout(function(){
			    $(this).removeClass('highlight');
			});
		  $(".zebra tr:even").addClass('alternate');
	  }



      //alert(response);
//    if (response == "0") {
//      location.href = "item.html";
//    } else {
//      alert("ログインに失敗しました");
//    }
  });
}


