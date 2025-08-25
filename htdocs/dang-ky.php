<?php
require_once 'connect.php';
require_once 'set.php';

$_alert = '';
$recafcode = '';
$count = 0;

if (isset($_GET['ref']) && !empty($_GET['ref'])) {
    $recafcode = $_GET['ref'];
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = trim($_POST["username"]);
    $password = trim($_POST["password"]);

    $stmt = $conn->prepare("SELECT * FROM account WHERE username=?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $_alert = "<div class='text-danger pb-2 font-weight-bold'>Tài khoản đã tồn tại.</div>";
    } else {
        $recaf = isset($_POST["recaf"]) && !empty($_POST["recaf"]) ? mysqli_real_escape_string($conn, trim($_POST['recaf'])) : null;
        if (!empty($recaf)) {
            $ip_address = $_SERVER['REMOTE_ADDR'];

            $stmt = $conn->prepare("SELECT COUNT(*) as count FROM account WHERE id=? AND ip_address=?");
            $stmt->bind_param("ss", $recaf, $ip_address);
            $stmt->execute();
            $result = $stmt->get_result();
            $row = $result->fetch_assoc();

            $ip_address = $_SERVER['REMOTE_ADDR'];
            $stmt = $conn->prepare("SELECT COUNT(*) as count FROM account WHERE id!=? AND ip_address=?");
            $stmt->bind_param("ss", $recaf, $ip_address);
            $stmt->execute();
            $result = $stmt->get_result();
            $row = $result->fetch_assoc();

            if ($row['count'] > 0) {
                $_alert = '<div class="text-danger pb-2 font-weight-bold">Không thể nhập mã giới thiệu của bản thân đâu nhé!</div>';
            } else {
                if ($count < 5) {
                    $stmt = $conn->prepare("UPDATE account SET gioithieu = gioithieu + 1 WHERE id = ?");
                    $stmt->bind_param("s", $recaf);
                    if ($stmt->execute()) {
                        $count++;
                        $stmt = $conn->prepare("INSERT INTO account (username, password, recaf) VALUES (?, ?, ?)");
                        $stmt->bind_param("sss", $username, $password, $recaf);
                        if ($stmt->execute()) {
                            $_alert = '<div class="text-danger pb-2 font-weight-bold">Đăng kí thành công!!</div>';
                        } else {
                            $_alert = '<div class="text-danger pb-2 font-weight-bold">Đăng ký thất bại.</div>';
                        }
                    } else {
                        $_alert = '<div class="text-danger pb-2 font-weight-bold">Có lỗi khi cập nhật số lần nhập mã!</div>';
                    }
                } else {
                    $_alert = '<div class="text-danger pb-2 font-weight-bold">Mã giới thiệu này đã đạt đủ số người nhập mã!</div>';
                }
            }
        } else {
            $stmt = $conn->prepare("INSERT INTO account (username, password) VALUES (?, ?)");
            $stmt->bind_param("ss", $username, $password);
            if ($stmt->execute()) {
                $_alert = '<div class="text-danger pb-2 font-weight-bold">Đăng kí thành công!!</div>';
            } else {
                $_alert = '<div class="text-danger pb-2 font-weight-bold">Đăng ký thất bại.</div>';
            }
        }
    }

    $conn->close();
}
?>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Ngọc Rồng Green</title>
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="description"
        content="Website chính thức của Chú Bé Rồng Online – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!">
    <meta name="keywords"
        content="Chú Bé Rồng Online,ngoc rong mobile, game ngoc rong, game 7 vien ngoc rong, game bay vien ngoc rong">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title"
        content="Website chính thức của Chú Bé Rồng Online – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!">
    <meta name="twitter:description"
        content="Website chính thức của Chú Bé Rồng Online – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!">
    <meta name="twitter:image" content="image/logo.png">
    <meta name="twitter:image:width" content="200">
    <meta name="twitter:image:height" content="200">
    <link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <script src="assets/jquery/jquery.min.js"></>
            <script src="assets/notify/notify.js"></script>
    <link rel="icon" href="image/icon.png?v=99">
    <link href="assets/main.css" rel="stylesheet">
</head>

<body>
    <div class="container" style="border-radius: 15px; background: #ffaf4c; padding: 0px">
        <div class="container" style="background-color: #e67e22; border-radius: 15px 15px 0px 0px">
            <div class="row bg pb-3 pt-2">
                <div class="col">
                    <div class="text-center mb-2">
                        <a href="../dien-dan"><img class="rounded" src="../image/logo.png" id="logo"></a>
                    </div>
                    <div class="text-center pt-2">
                        <div style="display: inline-block;">
                            <a href="../download/nro green.apk"> <img class="icon-download" src="../image/android.png">
                            </a><br>
                            <small class="text-dark">
                                <?php echo $_android; ?>
                            </small>
                        </div>
                        <div style="display: inline-block;">
                            <a href="../download/nro green.rar"><img class="icon-download" src="../image/pc.png">
                            </a><br>
                            <small class="text-dark">
                                <?php echo $_windows; ?>
                            </small>
                        </div>
                        <div style="display: inline-block;">
                        <a href="../download/nro green.ipa"><img class="icon-download" src="../image/ip.png">
						</a><br>
                            <small class="text-dark">
                                <?php echo $_iphone; ?>
                            </small>
                        </div>
                        <div>
                            <img height="12" src="image/12.png" style="vertical-align: middle;">
                            <small style="font-size: 10px" id="hour3">Dành cho người chơi trên 12 tuổi. Chơi quá 180
                                phút mỗi ngày sẽ hại sức khỏe.</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container color-main2 pb-2">
            <div class="text-center">
                <div class="row">
                    <div class="col pr-0"> <a href="dang-nhap" class="btn p-1 btn-header">Đăng
                            nhập</a> </div>
                    <div class="col"> <a href="dang-ky" class="btn p-1 btn-header-active">Đăng
                            ký</a> </div>
                </div>
            </div>
        </div>
        <div class="container color-forum pt-1 pb-1">
            <div class="row">
                <div class="col"> <a href="dien-dan" style="color: white">Quay lại diễn đàn</a> </div>
            </div>
        </div>
        <div class="container pt-5 pb-5">
            <div class="row">
                <div class="col-lg-6 offset-lg-3">
                    <h4>ĐĂNG KÝ</h4>
                    <form id="form" method="POST">
                        <div class="form-group">
                            <label>Tài khoản:</label>
                            <input class="form-control" type="text" name="username" id="username"
                                placeholder="Nhập tài khoản">
                        </div>
                        <div class="form-group">
                            <label>Mật khẩu:</label>
                            <input class="form-control" type="password" name="password" id="password"
                                placeholder="Nhập mật khẩu">
                        </div>
                        <div class="form-check form-group">
                            <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" name="accept" id="accept" checked="">
                                Đồng ý <a href="dieu-khoan" target="_blank">Điều khoản sử dụng</a>
                            </label>
                        </div>

                        <?php if (!empty($_alert)) {
                            echo $_alert;
                        } ?>
                        <div id="notify" class="text-danger pb-2 font-weight-bold"></div>
                        <button class="btn btn-main form-control" type="submit" onclick="redirectToRegisterPage()">ĐĂNG
                            KÝ</button>
                    </form>
                    <br>
                    <script>
                                                      function redirectToRegisterPage() {
                                                          <?php if (isset($_SESSION['id'])) { ?>
                var url = "<?php echo $_domain ?>/dang-ky.php?ref=<?php echo $_SESSION['id'] ?>";
                                                          window.location.href = url;
        <?php } ?>
    }
                    </script>
                </div>
            </div>
            <div class="text-center">
            </div>
        </div>
        <div class="border-secondary border-top"></div>
        <div class="container pt-4 pb-4 text-white">
            <div class="row">
                <div class="col">
                    <div class="text-center">
                        <div style="font-size: 13px" class="text-dark">
                            <?php
                        ?>
                        </div>
                    </div>
					<div class="copyright" style="line-height: 7px">

Bản Quyền thuộc về Ngọc Rồng Green

</div>
                </div>
            </div>
        </div>
    </div>
    </div>
    <script src="assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="asset/main.js"></script>
</body>

</html>