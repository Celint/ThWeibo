<?php
include("db.php");

if (!$conn) {
    die("failed to connect mysql" . mysqli_connect_error());
}
mysqli_query($conn, "SET NAMES UTF8");

$username = $_POST["username"];
$content = $_POST["content"];
$imagePath = $_POST["imagePath"];

$insert;
if ($imagePath == "") {
    $insert = "INSERT INTO myblog(weibo_username, weibo_content, weibo_img) VALUES ('{$username}', '{$content}', null)";
} else {
    $insert = "INSERT INTO myblog(weibo_username, weibo_content, weibo_img) VALUES ('{$username}', '{$content}', '{$imagePath}')";
}

if (mysqli_query($conn, $insert)) {
    echo json_encode(array("status" => 1));
} else {
    echo json_encode(array("status" => 0));
}
mysqli_close($conn);
