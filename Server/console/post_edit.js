
function fetch() {

  if (!authenticate()) {
    return;
  }

  var postId = getPostId();
  if (postId.length == 0) {
    return;
  }

  var param = "command=getPost";
  httpPost("srv.php", param, function(response) {
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {
        for (var i = 0; i < json.data.length; i++) {
          if (json.data[i].id == postId) {
            display(json.data[i]);
          }
        }
      } else {
        alert("ポストの取得に失敗しました");
      }
    } catch(e) {
      alert("ポストの取得に失敗しました");
    }
  });
}

function getPostId() {

  var urlSplit = location.search.split("id=");
  if (urlSplit.length == 2) {
    return urlSplit[1];
  } else {
    return "";
  }
}

function display(postData) {
  document.getElementById("source").value = postData.source;
  document.getElementById("title").value = postData.title;
  document.getElementById("sumbnail").value = postData.sumbnail;
  document.getElementById("link").value = postData.link;
  if (postData.forAll == "1") {
    document.getElementById("forAll").checked = true;
  } else {
    document.getElementById("forAll").checked = false;
  }
}

function onClickEditEnd() {

  var postId = getPostId();
  var title = document.getElementById("title").value;
  var source = document.getElementById("source").value;
  var sumbnail = document.getElementById("sumbnail").value;
  var link = document.getElementById("link").value;
  var forAll = "";
  if (document.getElementById("forAll").checked) {
    forAll = "1";
  } else {
    forAll = "0";
  }

  var params = "";
  if (postId.length == 0) {
    params = "command=createPost";
  } else {
    params = "command=editPost&id=" + postId;
  }
  params += ("&title=" + title + "&source=" + source + "&sumbnail=" + sumbnail + "&link=" + link + "&forAll=" + forAll);

  httpPost("srv.php", params, function(response) {
    try {
      var json = JSON.parse(response);
      if	(json.result == "0") {
        showAlert(true, postId.length == 0);
        location.href = 'post.html';
      } else {
        showAlert(false, postId.length == 0);
      }
    } catch (e) {
      showAlert(false, postId.length == 0);
    }
  });
}

function showAlert(result, isCreate) {

  var msg = "";
  if (result) {
    if (isCreate) {
      msg = "新規作成しました";
    } else {
      msg = "編集が完了しました";
    }
  } else {
    if (isCreate) {
      msg = "新規作成に失敗しました";
    } else {
      msg = "編集に失敗しました";
    }
  }
  alert(msg);
}
