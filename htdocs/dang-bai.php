<?php
include_once 'set.php';
include_once 'connect.php';

if ($_login == null) {
    header("location:dang-nhap");
}
include('head.php');
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
    <meta name="twitter:image" content="image/logo.jpg">
    <meta name="twitter:image:width" content="200">
    <meta name="twitter:image:height" content="200">
    <link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <script src="assets/jquery/jquery.min.js"></script>
    <script src="assets/notify/notify.js"></script>
    <link rel="icon" href="image/icon.png?v=99">
    <link href="assets/main.css" rel="stylesheet">
</head>

<body>
    <div class="container color-forum pt-1 pb-1">
        <div class="row">
            <div class="col"> <a href="dien-dan" style="color: white">Quay lại diễn đàn</a> </div>
        </div>
    </div>
    <div class="container pt-5 pb-5">
        <div class="row">
            <div class="col-lg-6 offset-lg-3">
                <form method="POST" enctype="multipart/form-data">
                    <div class="form-group">
                        <label><span class="text-danger">*</span> Tiêu đề:</label>
                        <input class="form-control" type="text" name="tieude" id="tieude"
                            placeholder="Nhập tiêu đề bài viết" required>

                        <label><span class="text-danger">*</span> Nội dung:</label>
                        <textarea class="form-control" type="text" name="noidung" id="noidung"
                            placeholder="Nhập nội dung bài viết" required></textarea>
                        <?php
                        $query = "SELECT account.*, account.admin FROM account LEFT JOIN player ON player.account_id = account.id";
                        $result = mysqli_query($conn, $query);

                        if ($row = mysqli_fetch_assoc($result)) {
                            ?>
                            <label><span class="text-danger">*</span> Thể loại:</label>
                            <select class="form-control" name="theloai" id="theloai" required>
                                <?php
                                if ($row['admin'] == 1) {
                                    // Thiết lập giá trị mặc định cho $theloai khi admin = 0
                                    ?>
                                    <option value="0">Thường</option>
                                    <option value="1">Thông Báo</option>
                                    <option value="2">Sự Kiện</option>
                                    <option value="3">Cập Nhật</option>
                                    <?php
                                } else {
                                    ?>
                                    <option value="0">Thường</option>
                                    <?php
                                }
                        }
                        ?>
                        </select>
                        

                        <div id="submit-error" class="alert alert-danger mt-2" style="display: none;"></div>
                    </div>

                    <button class="btn btn-main form-control" type="submit">ĐĂNG BÀI</button>
                </form>
                <script>
                    const form = document.querySelector('form');
                    const submitBtn = form.querySelector('button[type="submit"]');
                    const submitError = form.querySelector('#submit-error');

                    form.addEventListener('submit', (event) => {
                        const titleLength = document.getElementById('tieude').value.trim().length;
                        const contentLength = document.getElementById('noidung').value.trim().length;

                        if (titleLength < 1 || contentLength < 1) {
                            event.preventDefault();

                            submitError.innerHTML = '<strong>Lỗi:</strong> Tiêu đề và nội dung phải có ít nhất 5 ký tự!';
                            submitError.style.display = 'block';
                            submitBtn.scrollIntoView({ behavior: 'smooth', block: 'start' });
                        }
                    });
                </script>
            </div>
        </div>
    </div>
    <script src=" assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="asset/main.js"></script>
</body><!-- Bootstrap core JavaScript -->

</html>
<div class="py-3">
    <div class="table-responsive">
        <?php
        include_once 'set.php';

        // Lấy dữ liệu từ form sử dụng phương thức POST
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            // Lấy giá trị của tiêu đề và nội dung bài viết
            $tieude = htmlspecialchars($_POST["tieude"]);
            $noidung = htmlspecialchars($_POST["noidung"]);
            $theloai = intval($_POST["theloai"]);

            if (isset($_POST['username'])) {
                $_username = $_POST['username'];
            }
            $sql = "SELECT player.name FROM player INNER JOIN account ON account.id = player.account_id WHERE account.username='$_username'";
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_assoc($result);
            $_name = $row['name'];

            // Kiểm tra nếu có tệp tin ảnh được tải lên
            if (isset($_FILES['image']) && !empty($_FILES['image']['name'][0])) {
                $image_files = $_FILES['image'];
                $total_files = count($image_files['name']);

                $image_names = array(); // Mảng để lưu trữ tên tệp tin ảnh
                $upload_directory = "uploads/"; // Thư mục lưu trữ ảnh
        
                for ($i = 0; $i < $total_files; $i++) {
                    $image_filename = $image_files['name'][$i];
                    $image_tmp = $image_files['tmp_name'][$i];

                    $targetFile = $upload_directory . basename($image_filename);

                    // Di chuyển tệp tin ảnh vào thư mục lưu trữ
                    move_uploaded_file($image_tmp, $targetFile);

                    // Thêm tên tệp tin vào mảng
                    $image_names[] = basename($image_filename);
                }

                // Chuyển đổi mảng thành chuỗi JSON
                $image_names_json = json_encode($image_names);

                // Lưu dữ liệu (bao gồm username và danh sách tên tệp tin ảnh) vào cơ sở dữ liệu bằng câu lệnh INSERT INTO
                $sql = "INSERT INTO posts (tieude, noidung, theloai, image, username) VALUES ('$tieude', '$noidung', '$theloai', '$image_names_json', '$_name')";
            } else {
                // Nếu không có tệp tin ảnh được tải lên, lưu dữ liệu (bao gồm username) vào cơ sở dữ liệu bằng câu lệnh INSERT INTO
                $sql = "INSERT INTO posts (tieude, noidung, theloai, username) VALUES ('$tieude', '$noidung', '$theloai', '$_name')";
            }

            if (mysqli_query($conn, $sql)) {
                // Lấy số điểm tích lũy hiện tại của người dùng
                $sql_select = "SELECT a.tichdiem FROM account a INNER JOIN player p ON a.id = p.account_id WHERE p.name = '$_name'";
                $result_select = mysqli_query($conn, $sql_select);
                $row_select = mysqli_fetch_assoc($result_select);
                $tichdiem = $row_select['tichdiem'];

                // Cập nhật giá trị tichdiem trong bảng account
                $sql_update = "UPDATE account SET tichdiem = ($tichdiem + 1) WHERE id = (SELECT account_id FROM player WHERE name = '$_name')";
                mysqli_query($conn, $sql_update);

                echo "Bài viết đã được đăng thành công.";
                // header("Location: baiviet.php");
                // exit;
            } else {
                echo "Lỗi: " . $sql . "<br>" . mysqli_error($conn);
            }
        }

        // Đóng kết nối với cơ sở dữ liệu
        mysqli_close($conn);
        ?>
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
</main>
</body>