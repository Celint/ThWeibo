<?php
include("db.php");

if (!$conn) {
    die("failed to connect mysql" . mysqli_connect_error());
}
mysqli_query($conn, "SET NAMES UTF8");

$id = $_POST["weibo_id"];

$query = "SELECT * FROM comment WHERE comment_weibo_id = {$id}";
$result = mysqli_query($conn, $query);

$comments = [];
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $comment = array(
            "comment_id" => $row["comment_id"],
            "weibo_id" => $row["comment_weibo_id"],
            "username" => $row["comment_username"],
            "comment_content" => $row["comment_content"],
            "comment_time" => $row["comment_time"]
        );
        array_push($comments, $comment);
    }
    for ($i = 0; $i < count($comments) - 1; $i++) {
        for ($j = 0; $j < count($comments) - 1; $j++) {
            if ($comments[$j]["comment_id"] < $comments[$j + 1]["comment_id"]) {
                $temp = $comments[$j];
                $comments[$j] = $comments[$j + 1];
                $comments[$j + 1] = $temp;
            }
        }
    }

    $commentList = array(
        "comments" => $comments
    );

    echo json_encode($commentList, JSON_UNESCAPED_UNICODE);
}
mysqli_close($conn);
