<?php
include("db.php");

if (!$conn) {
    die("failed to connect mysql" . mysqli_connect_error());
}
mysqli_query($conn, "SET NAMES UTF8");

$id = $_POST["id"];
$username = $_POST["username"];

$delete = "DELETE FROM myblog WHERE weibo_username = '{$username}' and  weibo_id = {$id}";

if (mysqli_query($conn, $delete)) {
    echo json_encode(array("status" => 1));
} else {
    echo json_encode(array("status" => 0));
}
mysqli_close($conn);
