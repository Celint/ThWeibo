<?php
include("db.php");
// 检查连通性
if (!$conn) {
    die("failed to connect mysql" . mysqli_connect_error());
}
mysqli_query($conn, "SET NAMES UTF8");
// 获取POST数据
$user_nickname = $_POST["username"];
$user_password = $_POST["password"];
// 进行查询
$query1 = "SELECT * FROM userInfo WHERE user_nickname = '{$user_nickname}'";
$query2 = "SELECT * FROM userInfo WHERE user_nickname = '{$user_nickname}' AND user_password = '{$user_password}'";
$result = mysqli_query($conn, $query1);
$userInfo;
if (mysqli_num_rows($result) > 0) {
    $result = mysqli_query($conn, $query2);
    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        $userInfo = array(
            "username" => $row["user_nickname"],
            "password" => $row["user_password"],
            "iconPath" => $row["user_icon"],
            "status" => 1
        );
    } else {
        $userInfo = array(
            "username" => $user_nickname,
            "password" => $user_password,
            "status" => 2
        );
    }
} else {
    $userInfo = array(
        "username" => $user_nickname,
        "password" => $user_password,
        "status" => 0
    );
}
// 响应数据
echo json_encode($userInfo);
mysqli_close($conn);
