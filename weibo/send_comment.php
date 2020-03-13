<?php
include("db.php");

if (!$conn) {
    die("failed to connect mysql" . mysqli_connect_error());
}
mysqli_query($conn, "SET NAMES UTF8");

$weibo_id = $_POST["weibo_id"];
$username = $_POST["username"];
$comment = $_POST["comment"];

$query_num = "SELECT * FROM comment WHERE comment_weibo_id = {$weibo_id}";
$query_result = mysqli_query($conn, $query_num);
$row = mysqli_num_rows($query_result);
$row += 1;

$insert = "INSERT INTO comment(comment_id, comment_weibo_id, comment_username, comment_content) 
    VALUES ({$row}, {$weibo_id}, '{$username}', '{$comment}')";

if (mysqli_query($conn, $insert)) {
    echo json_encode(array("status" => 1));
} else {
    echo json_encode(array("status" => 0));
}
mysqli_close($conn);
