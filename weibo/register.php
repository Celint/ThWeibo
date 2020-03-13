<?php
include("db.php");
// 检查数据库是否连接成功
if (!$conn) {
    die("failed to connect mysql" . mysqli_connect_error());
}
mysqli_query($conn, "SET NAMES UTF8");
// 获取网络请求
$user_nickname = $_POST["username"];
$user_password = $_POST["password"];
$user_icon = $_POST["iconPath"];
if ($user_icon == "") {
    $user_icon = null;
}
// 进行查询和插入
$query = "SELECT * FROM userInfo WHERE user_nickname = '{$user_nickname}'";
if ($user_icon == null) {
    $insert = "INSERT INTO userInfo (user_nickname, user_password, user_icon) values ('{$user_nickname}', '{$user_password}', null)";
} else {
    $insert = "INSERT INTO userInfo (user_nickname, user_password, user_icon) values ('{$user_nickname}', '{$user_password}', '{$user_icon}')";
}

$result = mysqli_query($conn, $query);
$userInfo;
if (mysqli_num_rows($result) > 0) {
    $userInfo = array(
        "username" => $user_nickname,
        "password" => $user_password,
        "iconPath" => $user_icon,
        "status" => 2
    );
} else {
    if (mysqli_query($conn, $insert)) {
        $userInfo = array(
            "username" => $user_nickname,
            "password" => $user_password,
            "iconPath" => $user_icon,
            "status" => 1
        );
    } else {
        $userInfo = array(
            "username" => $user_nickname,
            "password" => $user_password,
            "iconPath" => $user_icon,
            "status" => 0
        );
    }
}
// 响应数据
echo json_encode($userInfo);
mysqli_close($conn);
