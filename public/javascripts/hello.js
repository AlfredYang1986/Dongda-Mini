
var indexs = document.getElementsByClassName('index');
var dates = document.getElementsByClassName('dateValue');
var buttons = document.getElementsByClassName('checkDetail');
for (var i = 0,len = buttons.length;i< len;i++) {
  var indexValue = indexs[i];
  var dateValue = dates[i];
    var daydate = new Date(dateValue.innerHTML - 0);
    dateValue.innerHTML = daydate.getFullYear() +"-"+ (daydate.getMonth() + 1 )+"-" + daydate.getDate();
    buttons[i].onclick = (function(k){
        var id = indexValue.innerHTML;
        return function(){
            window.location.href = "/admin/query/provider/apply/"+id;
        };
    })(i);

}

window.onload = function() {

}
