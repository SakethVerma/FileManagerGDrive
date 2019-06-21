function login() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var obj = JSON.parse(xmlhttp.responseText);
			window.location.replace(obj.oauthRedirect);
		}
	}
	xmlhttp.open("GET", "/login?source=ui", true);
	xmlhttp.send();
}

// Event listeners for button clicks
window.onload = function() {
	document.getElementById('loginButton').addEventListener('click', login,
			false);
}
