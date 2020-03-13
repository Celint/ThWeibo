<?php
include("db.php");
// 检查数据库是否连接成功
if (!$conn) {
    die("failed to connect mysql" . mysqli_connect_error());
}
mysqli_query($conn, "SET NAMES UTF8");
// 进行查询
$query = "SELECT * FROM myblog";
$result = mysqli_query($conn, $query);

$blogs = [];
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $blog = array(
            "id" => $row["weibo_id"],
            "username" => $row["weibo_username"],
            "content" => $row["weibo_content"],
            "pubtime" => $row["weibo_time"]
        );
        array_push($blogs, $blog);
    }
}
// 冒泡排序
for ($i = 0; $i < count($blogs) - 1; $i++) {
    for ($j = 0; $j < count($blogs) - 1; $j++) {
        if (strtotime($blogs[$j]["pubtime"]) < strtotime($blogs[$j + 1]["pubtime"])) {
            $temp = $blogs[$j];
            $blogs[$j] = $blogs[$j + 1];
            $blogs[$j + 1] = $temp;
        }
    }
}
// 封装和响应
$myblogs = array(
    "blogs" => $blogs
);

echo json_encode($myblogs, JSON_UNESCAPED_UNICODE);
mysqli_close($conn);
