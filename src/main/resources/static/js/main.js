var selectedFileCount, totalUploadedValue, fileCount, filesUploaded, authToken;
var currentDirectoryStack = new Array();

// When upload is completed
function onUploadComplete(e) {
	swal("Success!", "Finished uploading file(s)", "success");
	resetTable();
	getFoldersList();
}

// Will be called when user select the files
function onFileSelect(e) {
	files = e.target.files; // FileList object
	var output = [];
	fileCount = files.length;
	selectedFileCount = 0;
	for (var i = 0; i < fileCount; i++) {
		var file = files[i];
		output.push(file.name, ' (', file.size, ' bytes, ',
				file.lastModifiedDate.toLocaleDateString(), ')');
		output.push('<br/>');
		selectedFileCount += file.size;
	}
	document.getElementById('selectedFiles').innerHTML = output.join('');

}

// Capture errors
function onUploadFailed(e) {
	swal("Error!", "Error uploading file", "danger");
}

// Get the next file in the queue and send it to the server
function uploadNext() {
	var xhr = new XMLHttpRequest();
	var fd = new FormData();
	var file = document.getElementById('file').files[filesUploaded];
	fd.append("file", file);
	xhr.addEventListener("load", onUploadComplete, false);
	xhr.addEventListener("error", onUploadFailed, false);
	currentDirectory = currentDirectoryStack[currentDirectoryStack.length - 1] + "/" + file.name;
	xhr.open("POST", "/files/content?path=" + currentDirectory);
	xhr.setRequestHeader("Authorization", authToken)
	xhr.send(fd);
}

// Start the process
function startUpload() {
	if (document.getElementById('file').files.length <= 0) {
		swal("Cannot Upload!", "Please select a file to upload", "warning");
	} else {
		totalUploadedValue = filesUploaded = 0;
		uploadNext();
	}
}

// Clear the page
function resetScreen() {
	document.getElementById('bar').style.width = '0%';
	document.getElementById('bar').innerText = '';
	document.getElementById("selectedFiles").innerHTML = '';
	document.getElementById("imageForm").reset();
}

function getFoldersList() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var obj = JSON.parse(xmlhttp.responseText);
			var output = "";
			for (var i = 0; i < obj.length; i++) {
				var folderObj = obj[i];
				output += "<tr>";
				output += "<td>" + folderObj.name + "</td>";
				output += "<td>" + folderObj.createdDate + "</td>";
				output += "<td>" + folderObj.modifiedDate + "</td>";
				output += "<td>" + folderObj.path + "</td>";
				if (folderObj.directory) {
					output += "<td>"
							+ "<input id='navigateChildFolders' type='button' value='View Details' onclick='getFolderDetails(\""
							+ folderObj.path + "\");' />" + "</td>";
				} else {
					output += "<td>"
							+ "<input id='downloadFileContents' type='button' value='Download' onclick='getFileContents(\""
							+ folderObj.path + "\");' />" + "</td>";
				}
				output += "</tr>";
			}
			document.getElementById("selectedFolders").innerHTML = output;
		}
	}
	var currentDirectory = currentDirectoryStack[currentDirectoryStack.length - 1];
	xmlhttp.open("GET", "/folders/content?path=" + currentDirectory, true);
	xmlhttp.setRequestHeader("Authorization", authToken)
	xmlhttp.send();
}

function resetTable() {
	document.getElementById("selectedFolders").innerHTML = '';
}

function getFolderDetails(folderPath) {
	currentDirectoryStack.push(folderPath);
	resetTable();
	getFoldersList();
}

function navigateToParentFolder() {
	var currentDirectory = currentDirectoryStack[currentDirectoryStack.length - 1];
	if (currentDirectory != "/") {
		currentDirectoryStack.pop();
		resetTable();
		getFoldersList();
	}
}

function getFileContents(filePath) {
	var auth = encodeURIComponent(authToken);
	var filepath = encodeURIComponent(filePath);
	window
			.open("/files/ui/content?path=" + filepath + "&Authorization="
					+ auth);

	// var xmlhttp = new XMLHttpRequest();
	// xmlhttp.open("GET", "/files/content?path=" + filePath, true);
	// xmlhttp.setRequestHeader("Authorization",
	// "AcXgzsdy5nXnl0PONFH58g77L6MWC/7+MbfCd8RIPWc=")
	// xmlhttp.send();
}

// Event listeners for button clicks
window.onload = function() {

	var url = document.location.href, params = url.split('?')[1].split('&'), data = {}, tmp;
	for (var i = 0, l = params.length; i < l; i++) {
		tmp = params[i].split('=');
		data[tmp[0]] = tmp[1];
	}
	authToken = data.token;

	document.getElementById('file').addEventListener('change', onFileSelect,
			false);
	document.getElementById('uploadButton').addEventListener('click',
			startUpload, false);
	document.getElementById('resetButton').addEventListener('click',
			resetScreen, false);
	document.getElementById('navigateBack').addEventListener('click',
			navigateToParentFolder, false);
	currentDirectoryStack.push("/");
	getFoldersList();
}
