
function chkUser() {
	var flg = true;

	var user_id = document.getElementById("user_id");
	var password = document.getElementById("password");
	var confirmation = document.getElementById("confirmation");

	var u_flg = /^[0-9a-zA-Z]+$/.test(user_id.value);  //正規表現(半角英数字のみ)
	var p_flg = /^[0-9a-zA-Z]+$/.test(password.value);

	var comment_confirmation = document.getElementById("comment_confirmation");

	if (password.value !== confirmation.value) {

		flg = false;
		confirmation.focus();
		confirmation.style.backgroundColor = "#FF9999";
		comment_confirmation.style.display = 'block';

	} else {
		confirmation.style.backgroundColor = "#ffffff";
		comment_confirmation.style.display = 'none';

	}

	var comment_password = document.getElementById("comment_password");

	if (password.value.length < 8 || password.value.length > 32) {

		flg = false;
		password.focus();
		password.style.backgroundColor = "#FF9999";
		comment_password.style.display = 'block';

	} else {

		if (p_flg) {
			password.style.backgroundColor = "#ffffff";
			comment_password.style.display = 'none';

		} else {
			flg = false;

			comment_password.style.display = 'block';
			user_id.style.backgroundColor = "#FF9999";
		}

	}

	var comment_user_id = document.getElementById("comment_user_id");

	if (user_id.value.length < 4 || user_id.value.length > 24) {
		flg = false;
		user_id.focus();
		user_id.style.backgroundColor = "#FF9999";
		comment_user_id.style.display = 'block';

	} else {

		if (u_flg) {
			user_id.style.backgroundColor = "#ffffff";
			comment_user_id.style.display = 'none';

		} else {
			flg = false;

			comment_user_id.style.display = 'block';
			user_id.style.backgroundColor = "#FF9999";
		}

	}

	return flg;
}

function chkShowAll(btn) {   //管理者側従業員削除ボタン用
	var flg = false;
	var employeeCode = document.getElementsByName("employeeCode");

	for (var i = 0; i < employeeCode.length; i++) {
		if (employeeCode[i].checked) {
			flg = true;

			if (document.getElementsByName("chkBtn")[0].value == "delete_submit") {
				var res = confirm("本当に削除しますか？");

				flg = res;
			}

		}
	}

	if (!flg) {
		var comment_show_all = document.getElementById("comment_show_all");
		comment_show_all.style.display = 'block';
	}

	return flg;
}

function setValue(btn) {
	document.getElementsByName("chkBtn")[0].value = btn;
}

function setTime(num) {
	// 桁数が1桁だったら先頭に0を加えて2桁に調整する
	var ret;
	if (num < 10) {
		ret = "0" + num;
	} else {
		ret = num;
	}
	return ret;
}
function showClock() {   //出勤ボタンページの動く時間表示用
	var nowTime = new Date();
	var nowHour = setTime(nowTime.getHours());
	var nowMin = setTime(nowTime.getMinutes());
	var nowSec = setTime(nowTime.getSeconds());
	var msg = nowHour + ":" + nowMin + ":" + nowSec;  //innerHTMLで要素変更
	document.getElementById("RealtimeClockArea").innerHTML = msg;
}
setInterval('showClock()', 1000);  //タイマー処理