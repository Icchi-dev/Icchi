
function fetch() {

  if (!authenticate()) {
    return;
  }

  var param = "command=getPost";
  httpPost("srv.php", param, function(response) {
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {
        display(json.data);
      } else {
        alert("ポストの取得に失敗しました");
      }
    } catch(e) {
      alert("ポストの取得に失敗しました");
    }
  });
}

function display(dataList) {

  var table = document.getElementById("post_table");
  for (var i = table.rows.length - 1; i >= 1; i--) {
    table.deleteRow(i);
  }

  for (var i = 0; i < dataList.length; i++) {
    var tr = table.insertRow(-1);

    var tdSource = tr.insertCell(-1);
    tdSource.innerHTML = dataList[i].source;
    var tdTitle = tr.insertCell(-1);
    tdTitle.innerHTML = dataList[i].title
    var tdSumbnail = tr.insertCell(-1);
    tdSumbnail.innerHTML = "<img src='" + dataList[i].sumbnail + "' onerror=\"this.src='img/item_noimg.png'\" style='height:50px'/>";
    var tdLink = tr.insertCell(-1);
    tdLink.innerHTML = "<a href='" + dataList[i].link + "' target='_blank'><img class='item_link' src='img/item_link.png' style='width:24px'></a>"

    var tdForAll = tr.insertCell(-1);
    if (dataList[i].forAll == "1") {
      tdForAll.innerHTML = "○";
    } else {
      tdForAll.innerHTML = "";
    }
    
    var tdControl = tr.insertCell(-1);
    tdControl.innerHTML = "<a href='javascript:onSortOrderUp(" + dataList[i].id + ");'><img class='control_button' src='img/item_up.png'></a>";
    tdControl.innerHTML += "<a href='javascript:onSortOrderDown(" + dataList[i].id + ");'><img class='control_button' src='img/item_down.png'></a>";
    tdControl.innerHTML += "<a href='javascript:onEdit(" + dataList[i].id + ");'><img class='control_button' src='img/item_edit.png'></a>";
    tdControl.innerHTML += "<a href='javascript:onDelete(" + dataList[i].id + ", \"" + dataList[i].title + "\");'><img class='control_button' src='img/delete.png'></a>";
  }
}

function onSortOrderUp(id) {

  var param = "command=postSortOrderUp&id=" + id;
  httpPost("srv.php", param, function(response){
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {
        fetch();
        return;
      }
    } catch (e) {}

    alert("ソート順変更に失敗しました");
  });
}

function onSortOrderDown(id) {
  var param = "command=postSortOrderDown&id=" + id;
  httpPost("srv.php", param, function(response){
    try {
      var json = JSON.parse(response);
      if (json.result == "0") {
        fetch();
        return;
      }
    } catch (e) {}

    alert("ソート順変更に失敗しました");
  });
}

function onEdit(id) {
  location.href="post_edit.html?id=" + id;
}

function onDelete(id, title) {

  if (confirm("「" + title + "」を削除してよろしいですか？")) {
    var param = "command=deletePost&id=" + id;
  	httpPost("srv.php", param, function(response){
      try {
        var json = JSON.parse(response);
    	  if	(json.result == "0") {
          alert("削除しました");
          location.href = "";
    	  } else {
          alert("削除に失敗しました");
        }
      } catch (e) {
        alert("削除に失敗しました");
      }
  	});
  }
}
